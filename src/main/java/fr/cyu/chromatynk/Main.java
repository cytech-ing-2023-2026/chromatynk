package fr.cyu.chromatynk;

import fr.cyu.chromatynk.editor.CodeEditor;

public class Main {

    /**
     * Workaround necessary to launch the JavaFX application from a .jar
     * @param args the command arguments
     */
    public static void main(final String[] args) {
        CodeEditor.launch(CodeEditor.class, args);
    }
}
