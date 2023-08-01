module org.tcathebluecreper.mclangtool {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.tcathebluecreper.mclangtool to javafx.fxml;
    exports org.tcathebluecreper.mclangtool;
}