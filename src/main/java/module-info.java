module com.starmcc.mkv.to.mp4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    requires java.desktop;


    opens com.starmcc.mkv.to.mp4 to javafx.fxml;
    opens com.starmcc.mkv.to.mp4.frame to javafx.fxml;
    opens com.starmcc.mkv.to.mp4.controller to javafx.fxml;
    exports com.starmcc.mkv.to.mp4;
    exports com.starmcc.mkv.to.mp4.frame;
}
