package com.starmcc.mkv.to.mp4.controller;

import com.starmcc.mkv.to.mp4.frame.FxManager;
import com.starmcc.mkv.to.mp4.frame.StageEnum;
import com.starmcc.mkv.to.mp4.frame.StarmccConstant;
import com.starmcc.mkv.to.mp4.service.FfmpegService;
import com.starmcc.mkv.to.mp4.utils.CmdUtil;
import com.starmcc.mkv.to.mp4.utils.PropertiesUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ListView<String> outputList;
    @FXML
    private TextField inputVideoPathText;
    @FXML
    private TextField outputFolderPathText;
    @FXML
    private ToggleGroup outputCodeGroup;
    @FXML
    private Button exportVideoBtn;
    @FXML
    private Button exportSrtBtn;
    @FXML
    private TextField videoCodeText;
    @FXML
    private TextField audioCodeText;
    @FXML
    private TextField subCodeText;
    @FXML
    private CheckBox consoleCheckBox;
    @FXML
    private TextField ffmpegPathText;

    // =============================静态变量=========================


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 多选模式
        outputList.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        outputCodeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                StarmccConstant.selectType = Integer.parseInt((String) newValue.getUserData());
            } else {
                StarmccConstant.selectType = 0;
            }
            videoCodeText.setDisable(StarmccConstant.selectType != 3);
            audioCodeText.setDisable(StarmccConstant.selectType != 3);
            subCodeText.setDisable(StarmccConstant.selectType != 3);
        });

        outputList.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends String> change) -> {
            StarmccConstant.selectList = change.getList();
            exportRefreshStatus();
        });
        inputVideoPathText.textProperty().addListener((observable, oldValue, newValue) -> {
            StarmccConstant.inputVideoPath = newValue;
            exportRefreshStatus();
        });
        outputFolderPathText.textProperty().addListener((observable, oldValue, newValue) -> {
            PropertiesUtil.actionGlobalConfig(StarmccConstant.ConfigKeyEnum.OUTPUT_FOLDER, newValue);
            exportRefreshStatus();
        });
        ffmpegPathText.textProperty().addListener((observable, oldValue, newValue) -> {
            PropertiesUtil.actionGlobalConfig(StarmccConstant.ConfigKeyEnum.FFMPEG, newValue);
            exportRefreshStatus();
        });
        inputVideoPathText.setOnKeyPressed(keyEvent -> clearInputData(keyEvent, inputVideoPathText));
        outputFolderPathText.setOnKeyPressed(keyEvent -> clearInputData(keyEvent, outputFolderPathText));
        ffmpegPathText.setOnKeyPressed(keyEvent -> clearInputData(keyEvent, ffmpegPathText));


        videoCodeText.textProperty().addListener((observable, oldValue, newValue) -> StarmccConstant.videoCode = newValue);
        audioCodeText.textProperty().addListener((observable, oldValue, newValue) -> StarmccConstant.audioCode = newValue);
        subCodeText.textProperty().addListener((observable, oldValue, newValue) -> StarmccConstant.subCode = newValue);
        String outputPath = PropertiesUtil.actionGlobalConfig(StarmccConstant.ConfigKeyEnum.OUTPUT_FOLDER);
        if (outputPath.isEmpty()) {
            outputPath = CmdUtil.getDesktopPath();
            PropertiesUtil.actionGlobalConfig(StarmccConstant.ConfigKeyEnum.OUTPUT_FOLDER, outputPath);
        }
        outputFolderPathText.setText(outputPath);
        ffmpegPathText.setText(PropertiesUtil.actionGlobalConfig(StarmccConstant.ConfigKeyEnum.FFMPEG));

        StarmccConstant.consoleWindowStatus = consoleCheckBox.isSelected();
        consoleCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> StarmccConstant.consoleWindowStatus = newValue);

        // 绑定事件
        // 设置拖放事件处理器
        inputVideoPathText.setOnDragOver(this::allowDrag);
        inputVideoPathText.setOnDragDropped(this::ondragDropped);
        outputList.setOnDragOver(this::allowDrag);
        outputList.setOnDragDropped(this::ondragDropped);

        // 初始化ffmpeg路径
        Path path = Paths.get("./ffmpeg.exe");
        if (Files.exists(path)) {
            ffmpegPathText.setText(path.toString());
        }

    }

    private void clearInputData(KeyEvent keyEvent, TextField textField) {
        if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
            textField.clear();
        }
    }

    private void allowDrag(DragEvent event) {
        // 允许拖放复制操作
        event.acceptTransferModes(TransferMode.LINK);
        event.consume();
    }

    private void ondragDropped(DragEvent event) {
        // 获取拖放板
        Dragboard dragboard = event.getDragboard();
        // 检查是否包含文件
        if (dragboard.hasFiles()) {
            // 获取拖放的文件列表
            List<File> files = dragboard.getFiles();
            if (!files.isEmpty()) {
                readVideoInfo(files.get(0));
            }
        }
        event.setDropCompleted(true);
        event.consume();
    }

    private boolean exportRefreshStatus() {
        int testNum = 0;
        if (StarmccConstant.selectList.isEmpty()) {
            testNum++;
        }
        if (StarmccConstant.inputVideoPath.isEmpty()) {
            testNum++;
        }
        if (outputFolderPathText.getText().isEmpty()) {
            testNum++;
        }
        if (ffmpegPathText.getText().isEmpty()) {
            testNum++;
        }
        boolean notSelect = testNum != 0;
        exportVideoBtn.setDisable(notSelect);
        exportSrtBtn.setDisable(notSelect);
        return notSelect;
    }


    @FXML
    public void searchInputVideoPathEvent(ActionEvent actionEvent) {
        // 创建 FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("寻找视频文件..");

        // 设置文件选择器的初始目录
        String parentPath = this.getStartFolderPath(inputVideoPathText.getText());
        fileChooser.setInitialDirectory(new File(parentPath));

        // 添加文件类型过滤
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("mkv File", "*.mkv")
        );
        // 打开文件选择对话框
        File selectedFile = fileChooser.showOpenDialog(StarmccConstant.STAGE_CACHE.get(StageEnum.main));
        if (Objects.isNull(selectedFile)) {
            return;
        }
        readVideoInfo(selectedFile);
    }

    private void readVideoInfo(File selectedFile) {
        inputVideoPathText.setText(selectedFile.getAbsolutePath());
        FxManager.task(StageEnum.main, label -> {
            // 读取视频
            List<String> list = null;
            try {
                list = FfmpegService.getInstance().queryVideoInfo(ffmpegPathText.getText(), inputVideoPathText.getText());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            ObservableList<String> items = FXCollections.observableArrayList();
            items.addAll(list);
            Platform.runLater(() -> outputList.setItems(items));
        });
    }

    @FXML
    public void searchOutputFolderPathEvent(ActionEvent actionEvent) {
        // 创建 FileChooser
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("选择输出目录..");

        // 设置文件选择器的初始目录
        String startFolderPath = this.getStartFolderPath(outputFolderPathText.getText());
        fileChooser.setInitialDirectory(new File(startFolderPath));

        // 打开文件选择对话框
        File selectedFile = fileChooser.showDialog(StarmccConstant.STAGE_CACHE.get(StageEnum.main));
        if (Objects.nonNull(selectedFile)) {
            outputFolderPathText.setText(selectedFile.getAbsolutePath() + "\\");
        }
    }

    @FXML
    public void exportVideoEvent(ActionEvent actionEvent) {
        if (exportRefreshStatus()) {
            return;
        }

        ArrayList<String> args = new ArrayList<>();
        if (StarmccConstant.selectType == 3) {
            if (StarmccConstant.videoCode.isEmpty()) {
                videoCodeText.setText("libx264");
            }
            if (StarmccConstant.audioCode.isEmpty()) {
                videoCodeText.setText("aac");
            }
            if (StarmccConstant.subCode.isEmpty()) {
                videoCodeText.setText("mov_text");
            }
            args.add(StarmccConstant.videoCode);
            args.add(StarmccConstant.audioCode);
            args.add(StarmccConstant.subCode);
        }
        FxManager.task(StageEnum.main, label -> {
            FfmpegService.getInstance().exportVideo(
                    ffmpegPathText.getText(),
                    outputFolderPathText.getText(),
                    StarmccConstant.inputVideoPath,
                    new ArrayList<>(StarmccConstant.selectList),
                    StarmccConstant.selectType,
                    args
            );
        });
    }

    @FXML
    public void exportSrtEvent(ActionEvent actionEvent) {
        if (exportRefreshStatus()) {
            return;
        }
        if (StarmccConstant.selectList.size() > 1) {
            return;
        }
        FxManager.task(StageEnum.main, label -> {
            FfmpegService.getInstance().exportSrt(
                    ffmpegPathText.getText(),
                    outputFolderPathText.getText(),
                    StarmccConstant.inputVideoPath,
                    StarmccConstant.selectList.get(0)
            );
        });


    }

    @FXML
    public void aboutAction(ActionEvent actionEvent) {
        FxManager.showAlert("author：starmcc\ngitHub:https://github.com/starmcc", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void searchFfmpegPathEvent(ActionEvent actionEvent) {
        // 创建 FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("寻找Ffmpeg依赖文件..");

        // 设置文件选择器的初始目录
        String parentPath = this.getStartFolderPath(ffmpegPathText.getText());
        fileChooser.setInitialDirectory(new File(parentPath));

        // 添加文件类型过滤
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("ffmpeg", "*.exe")
        );
        // 打开文件选择对话框
        File selectedFile = fileChooser.showOpenDialog(StarmccConstant.STAGE_CACHE.get(StageEnum.main));
        if (Objects.isNull(selectedFile)) {
            return;
        }
        ffmpegPathText.setText(selectedFile.getAbsolutePath());
    }


    private String getStartFolderPath(String defPath) {
        if (defPath != null && !defPath.isEmpty()) {
            Path folder = Paths.get(defPath);
            if (Files.exists(folder)) {
                if (!Files.isDirectory(folder)) {
                    return folder.getParent().toString();
                }
                return folder.toString();
            }
        }
        return CmdUtil.getDesktopPath();
    }
}
