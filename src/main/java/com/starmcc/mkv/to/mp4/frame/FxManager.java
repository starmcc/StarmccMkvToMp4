package com.starmcc.mkv.to.mp4.frame;

import com.starmcc.mkv.to.mp4.service.ThreadPoolService;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class FxManager {

    public static Optional<ButtonType> showAlert(String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(content);
        // 显示弹框
        return alert.showAndWait();
    }

    public static Stage open(Stage parent, StageEnum stageEnum) {
        Stage stage = new Stage();
        Scene scene = null;
        try {
            FXMLLoader xxx = new FXMLLoader(stageEnum.buildPath());
            Parent root = xxx.load();
            if (Objects.isNull(stageEnum.getWidth()) || Objects.isNull(stageEnum.getHeight())) {
                scene = new Scene(root);
            } else {
                scene = new Scene(root, stageEnum.getWidth(), stageEnum.getHeight());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle(stageEnum.getTitle());
        stage.setScene(scene);
        stage.setResizable(false);
        if (Objects.nonNull(parent)) {
            stage.initOwner(parent);
        }
        stage.getIcons().add(new Image("icon.png"));

        stage.show();
        StarmccConstant.STAGE_CACHE.put(stageEnum.name(), stage);
        return stage;
    }

    /**
     * 同步任务
     *
     * @param stageEnum 页面枚举
     * @param consumer  消息
     * @return {@link Pane}
     */
    public static void task(StageEnum stageEnum, Consumer<Label> consumer) {
        Pane stagePane = getStageParentPane(stageEnum);
        if (Objects.isNull(stagePane)) {
            throw new RuntimeException("find stageEnum error");
        }
        if (Objects.isNull(consumer)) {
            throw new RuntimeException("find consumer error");
        }
        String message = "Loading..";
        Map<String, Object> map = buildBasicPage(message, stagePane);
        Pane pane = (Pane) map.get("pane");
        Label label = (Label) map.get("label");
        pane.setId(stageEnum.getTitle() + "-loading");
        Platform.runLater(() -> {
            stagePane.getChildren().add(pane);
            stagePane.setDisable(true);
        });
        ThreadPoolService.execute(() -> {
            try {
                consumer.accept(label);
            } catch (Exception e) {
                throw new RuntimeException();
            } finally {
                Platform.runLater(() -> {
                    stagePane.getChildren().remove(pane);
                    stagePane.setDisable(false);
                });
            }
        });
    }

    /**
     * 构建加载中面板
     *
     * @return {@link Pane}
     */
    private static Map<String, Object> buildBasicPage(String message, Pane stagePane) {
        StackPane stackPane = new StackPane();
        stackPane.setPrefWidth(stagePane.getWidth());
        stackPane.setPrefHeight(stagePane.getHeight());
        stackPane.setStyle("-fx-background-color: rgba(0,0,0,0.8);-fx-opacity: 1.0;");
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Label label = new Label();
        label.setAlignment(Pos.CENTER);
        label.setText(message);
        label.setFont(new Font(14));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-opacity: 1.0;");
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setProgress(-1F);
        progressIndicator.setPrefWidth(56);
        progressIndicator.setPrefHeight(56);
        progressIndicator.setStyle("-fx-opacity: 1.0;");
        vBox.getChildren().addAll(progressIndicator, label);
        Map<String, Object> map = new HashMap<>(16);
        map.put("label", label);
        stackPane.getChildren().add(vBox);
        map.put("pane", stackPane);
        return map;
    }

    /**
     * 获取窗口Pane
     *
     * @param stageEnum 页面枚举
     * @return {@link Pane}
     */
    private static Pane getStageParentPane(StageEnum stageEnum) {
        Stage stage = StarmccConstant.STAGE_CACHE.get(stageEnum);
        if (Objects.isNull(stage)) {
            return null;
        }
        Pane root = (Pane) stage.getScene().getRoot();
        if (root instanceof BorderPane) {
            // 如果是自己构建的BorderPane布局，则使用center中的节点
            BorderPane borderPane = (BorderPane) root;
            root = (Pane) borderPane.getCenter();
        }
        return root;
    }
}
