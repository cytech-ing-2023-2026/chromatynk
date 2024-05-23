module fr.cyu {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.swing;
    requires org.fxmisc.richtext;
    requires reactfx;

    exports fr.cyu.chromatynk;
    exports fr.cyu.chromatynk.ast;
    exports fr.cyu.chromatynk.editor;
    exports fr.cyu.chromatynk.eval;
    exports fr.cyu.chromatynk.parsing;
    exports fr.cyu.chromatynk.typing;
    exports fr.cyu.chromatynk.util;

    opens fr.cyu.chromatynk.editor to javafx.fxml;
}