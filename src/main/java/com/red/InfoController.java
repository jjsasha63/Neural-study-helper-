package com.red;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class InfoController {

    double x,y;

    public void text_active4(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Input_UI");
    }

    public void edit_action4(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Edit_UI");
    }

    public void student_action4(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Student_UI");
    }

    public void next_action4(MouseEvent mouseEvent) {
    }

    public void dragged(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() -x);
        stage.setY(mouseEvent.getScreenY() -y);
    }

    public void pressed(MouseEvent mouseEvent) {
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }


    public void minimize(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
}
