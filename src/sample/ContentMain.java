package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

/**
 * Project:
 * Author : zhaozhen
 * Email  : 2399144252@qq.com
 * Date   : 2019/6/2
 */
public class ContentMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL location = getClass().getResource("content.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();

        //Parent root = FXMLLoader.load(getClass().getResource("content.fxml"));
        primaryStage.setTitle("易点串口通信");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:logo2.png"));

        ContentController contentController = fxmlLoader.getController();
        contentController.init();

        primaryStage.show();
    }

    public void run(){
        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }


}
