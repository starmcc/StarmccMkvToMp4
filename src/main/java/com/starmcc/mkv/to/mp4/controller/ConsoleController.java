package com.starmcc.mkv.to.mp4.controller;

import com.starmcc.mkv.to.mp4.frame.StarmccConstant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsoleController  implements Initializable {

    @FXML
    private TextArea consoleTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StarmccConstant.consoleTextArea = consoleTextArea;
    }
}
