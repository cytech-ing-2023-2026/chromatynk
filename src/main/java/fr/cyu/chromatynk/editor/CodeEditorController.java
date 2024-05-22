package fr.cyu.chromatynk.editor;

import fr.cyu.chromatynk.Chromatynk;
import fr.cyu.chromatynk.ChromatynkException;
import fr.cyu.chromatynk.eval.*;
import fr.cyu.chromatynk.parsing.ParsingException;
import fr.cyu.chromatynk.parsing.Token;
import fr.cyu.chromatynk.util.Tuple2;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The controller for the code editor
 *
 * @author JordanViknar
 */
public class CodeEditorController implements Initializable {
    // The main elements
    @FXML
    private CodeArea codeArea;
    @FXML
    private Canvas canvas;

    // Interaction buttons
    @FXML
    private Button runButton;
    @FXML
    private Button clearTextAreaButton;

    // Bottom bar
    @FXML
    private HBox bottomBar;
    @FXML
    private Label infoLabel;
    @FXML
    private Label statusLabel;

	// Execution menu
	@FXML
	private Menu executionMenu;
    // Step-by-step mode
    @FXML
    private CheckMenuItem stepByStepCheckbox;
    @FXML
    private HBox stepByStepControls;
    @FXML
    private Label stepLabel;
	// Speed
	@FXML
	private ToggleGroup radioSpeedGroup;

    // Tabs
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab plusTab;

    private int tabCounter = 1;
    private Map<Tab, String> tabContentMap = new HashMap<>(); // Map to store content of each tab

	// Output area
	@FXML
	private TextArea outputArea;

    private final Stage primaryStage;
    private FileMenuController fileMenuController;
	private ImageMenuController imageMenuController;
    private Clock timeoutClock;
    private Clock secondaryClock;
    private ExecutionTimer currentExecution;

    @SuppressWarnings("exports")
    public CodeEditorController(Stage primaryStage) {this.primaryStage = primaryStage;}

    private Clock getPeriodClock() {
        long period = switch (((RadioMenuItem)radioSpeedGroup.getSelectedToggle()).getId()) {
            case "speed16" -> 1000/16;
            case "speed8" -> 1000/8;
            case "speed4" -> 1000/4;
            case "speed2" -> 1000/2;
            case "speed1" -> 1000;
            default -> 0;
        };

        return new PeriodClock(period);
    }

    private Clock getClock() {
        return new AndClock(timeoutClock, secondaryClock);
    }

    /**
     * This function is used to setup the UI elements of the code editor Java-side.
     * Basically : it's for things we cannot do in FXML.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeoutClock = TimeoutClock.fps(30); //TODO change
        secondaryClock = getPeriodClock();

        Executor executor = Executors.newSingleThreadExecutor();

        //Line numbers
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        //Lexical highlighting
        Subscription subscription = this.codeArea
                .multiPlainChanges()
                .successionEnds(Duration.ofMillis(125))
                .retainLatestUntilLater(executor)
                .supplyTask(() -> computeHighlighting(executor))
                .awaitLatest(codeArea.multiPlainChanges())
                .subscribe(tr -> {
                    tr.ifSuccess(highlighting -> codeArea.setStyleSpans(0, highlighting));
                    tr.ifFailure(Throwable::printStackTrace);
                });

        //Auto indent in body
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        codeArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher(codeArea.getParagraph(currentParagraph - 1).getSegments().get(0));
                if (m0.find()) Platform.runLater(() -> codeArea.insertText(caretPosition, m0.group()));
            }
        });

        // Set background color of the canvas
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Makes the bottom bar's background slightly darker
        bottomBar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.07);");

        // Only show relevant UI when in step-by-step mode
        stepByStepControls.setVisible(stepByStepCheckbox.isSelected());
        stepByStepCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {stepByStepControls.setVisible(newValue);});

        // Handle tab changes
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (oldTab != null && oldTab != plusTab) {
                // Save content of the previous tab
                tabContentMap.put(oldTab, codeArea.getText());
            }
            if (newTab != null && newTab != plusTab) {
                // Load content of the new tab
                codeArea.replaceText(tabContentMap.getOrDefault(newTab, ""));
            }
            if (newTab == plusTab) {
                // Create a new tab
                tabCounter++;
                Tab newTabInstance = new Tab("Onglet " + tabCounter);
                newTabInstance.setClosable(true);

                // Add the new tab before the plus tab
                int plusTabIndex = tabPane.getTabs().indexOf(plusTab);
                tabPane.getTabs().add(plusTabIndex, newTabInstance);

                // Initialize content for the new tab
                tabContentMap.put(newTabInstance, "");

                // Add event handler to remove tab content when the tab is closed
                newTabInstance.setOnClosed(event -> tabContentMap.remove(newTabInstance));

                // Select the new tab
                tabPane.getSelectionModel().select(newTabInstance);
            }
        });

        this.fileMenuController = new FileMenuController(primaryStage, codeArea);
		this.imageMenuController = new ImageMenuController(primaryStage, canvas);
    }

    public Task<StyleSpans<Collection<String>>> computeHighlighting(Executor executor) {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws ParsingException {
                List<Token> tokens = Chromatynk.lexSource(text);
                StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

                int lastKeywordEnd = 0;

                for (Token token : tokens) {
                    String cssClass = switch (token) {
                        case Token.LiteralBool ignored -> "boolean";
                        case Token.LiteralString ignored -> "string";
                        case Token.LiteralInt ignored -> "num";
                        case Token.LiteralFloat ignored -> "num";
                        case Token.LiteralColor ignored -> "color";
                        case Token.Identifier ignored -> "identifier";
                        case Token.Keyword ignored -> "keyword";
                        default -> "default";
                    };

                    Tuple2<Integer, Integer> range1d = token.range().toCursorRange(text);

                    spansBuilder.add(Collections.singleton("default"), Math.max(0, range1d.a() - lastKeywordEnd));
                    spansBuilder.add(Collections.singleton(cssClass), range1d.b() - range1d.a());
                    lastKeywordEnd = range1d.b();
                }

                var res = spansBuilder.create();
                return res;
            }
        };

        executor.execute(task);
        return task;
    }

    public void openFile() {
        fileMenuController.openFile();
    }

    public void saveFile() {
        fileMenuController.saveFile();
    }

	public void saveImage() {
		imageMenuController.saveImage();
	}

    public void clearTextArea() {
        codeArea.clear();
    }

    public void quit() {
        primaryStage.close();
    }

    private void postExecution() {
        tabPane.setDisable(false);
        codeArea.setDisable(false);
        runButton.setDisable(false);
        clearTextAreaButton.setDisable(false);
        executionMenu.setDisable(false);
    }

    private void onError(String source, Throwable throwable) {
        if(throwable instanceof ChromatynkException e) {
            outputArea.setText(e.getFullMessage(source));
        } else {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            outputArea.setText(sw.toString());
        }

        infoLabel.setText("ERREUR - Dessin échoué");
        statusLabel.setText("L'exécution a été arrêtée par une erreur.");

        postExecution();
    }

    private void onSuccess() {
        infoLabel.setText("INFO - Dessin complété");
        statusLabel.setText("Les instructions de dessin ont pu être complétées.");
        postExecution();
    }

    private void onProgress(EvalContext context) {
        stepLabel.setText("Instruction " + context.getNextAddress()+1);
    }

    public void runScript() {
        // Disable tab system, code area, execution menu and buttons
        tabPane.setDisable(true);
        codeArea.setDisable(true);
        runButton.setDisable(true);
        clearTextAreaButton.setDisable(true);
		executionMenu.setDisable(true);
		// Empty output and canvas
		outputArea.setText("");
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        try {
            EvalContext context = Chromatynk.compileSource(codeArea.getText(), graphicsContext);
            currentExecution = new ExecutionTimer(codeArea.getText(), context, getClock(), this::onSuccess, e -> onError(codeArea.getText(), e), this::onProgress);
            currentExecution.start();
        } catch (Throwable t) {
            onError(codeArea.getText(), t);
        }
    }

    public void refreshSecondaryClock() {
        secondaryClock = stepByStepCheckbox.isSelected() ? new StepByStepClock(false) : getPeriodClock();
    }

    public void nextInstruction() {
        if(secondaryClock instanceof StepByStepClock c) c.resume();
    }
}
