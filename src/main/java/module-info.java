module fr.cyu {
    requires javafx.controls;
	  requires javafx.fxml;
    exports fr.cyu.chromatynk;
    exports fr.cyu.chromatynk.ast;
    exports fr.cyu.chromatynk.editor;
    exports fr.cyu.chromatynk.util;
    
	  opens fr.cyu.chromatynk.editor to javafx.fxml;
}