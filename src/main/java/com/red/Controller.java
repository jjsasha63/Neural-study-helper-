package com.red;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import com.red.processing.Classifier;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;

public class Controller {


    public JFXTextArea text_main;
    public JFXTextField text_title;
    public static ArrayList<String> arr_result;
    double x,y;


    public void info_action1(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Info_UI");
    }

    public void edit_action1(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Edit_UI");
    }

    public void student_action1(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Student_UI");
    }

    public void next_action1(MouseEvent mouseEvent) throws IOException, InterruptedException {
        if(!text_main.getText().isEmpty() && !text_title.getText().isEmpty()) {
            main_text_pr("text");
            App.setRoot("Edit_UI");
        }  else {
            System.out.println("Entry error");
        }

    }

    public void main_text_pr(String type) throws IOException, InterruptedException {
        arr_result  = new ArrayList<>();
        Classifier classifier = new Classifier();
        String result = new String(), res = new String();
        int counter = 0;
        boolean activ = false;
        char[] text_arr;

        String title = new String();
            text_arr = text_main.getText().toCharArray();
            title = text_title.getText();


        for(char i: text_arr){
            if(counter==5){
               String output = classifier.sum(result, title);
                res += output;
                result = "";
                counter = 0;
                activ = true;
            }else result += i;
            if(i == '.'){
                counter++;
            }
        }
        if(type == "text" && activ == false){
            res = classifier.sum(text_main.getText(), title);
        }

        result = res.replaceAll("-LSB- 5 ","");
        result = result.replaceAll("-LSB- 3 ","");
        result = result.replaceAll("-LSB- 2 ","");
        result = result.replaceAll("-RSB- 2 ","");
        result = result.replaceAll("-RSB- 3 ","");
        result = result.replaceAll("-RSB- 5 ","");
        result = result.replaceAll("-RSB- ","");
        result = result.replaceAll("-LSB- ","");
        result = result.replaceAll("-LRB- ","");
        result = result.replaceAll("-RRB- ","");
        result = result.replaceAll("\n","");



        String[] n_arr = result.split("\\.");
        for(int i=0;i<n_arr.length-1;i++){
            arr_result.add(n_arr[i]);
        }



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
