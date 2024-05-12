module fr.cyu {
    requires javafx.controls;
	requires javafx.fxml;
    exports fr.cyu.chromatynk;
    exports fr.cyu.chromatynk.ast.statement;
    exports fr.cyu.chromatynk.ast.expr;
    exports fr.cyu.chromatynk.editor;
	opens fr.cyu.chromatynk.editor to javafx.fxml;
}