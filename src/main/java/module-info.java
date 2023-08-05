module org.tcathebluecreper.mclangtool {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.tcathebluecreper.mclangtool to javafx.fxml;
    exports org.tcathebluecreper.mclangtool;
}