package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private GridPane rootLayout;


    /**
     * 关闭登陆框
     * @param event 事件
     */
    @FXML protected void close(ActionEvent event) {
        Stage stage = (Stage) rootLayout.getScene().getWindow();
        stage.close();
    }

    /**
     * 点击登陆
     * @param event 事件
     */
    @FXML protected void login(ActionEvent event) {
        Stage stage = (Stage) rootLayout.getScene().getWindow();
        new ContentMain().run();
        stage.close();
    }


}