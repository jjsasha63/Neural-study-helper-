package com.red;

import com.red.processing.Additional_func;
import com.red.processing.Encrypt_Decrypt;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bouncycastle.crypto.CryptoException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class StudentController {

    public FlowPane flow;
    public static int num;
    public static int[] num_answ;
    public static ArrayList<String>[] answers;
    public static ArrayList<TextField> textFields;
    double x,y;

    public void info_action3(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Info_UI");
    }

    public void text_action3(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Input_UI");
    }

    public void edit_action3(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Edit_UI");
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

    public void next_action3(MouseEvent mouseEvent) {
        ArrayList<String>[] answers_final = new ArrayList[num];
        Line line = new Line();
        line.setEndX(900);
        line.setStrokeWidth(190);
        int mark = 0;
        for (int i = 0;i<textFields.size();i++) {
            answers_final[i] = new ArrayList<>();
            answers_final[i].addAll(Arrays.asList(textFields.get(i).getText().split(" ")));
            answers_final[i].removeIf(item -> item == null || "".equals(item));
            answers_final[i].replaceAll(String::toLowerCase);
        }

        for(int i = 0;i<answers_final.length;i++){
         if(answers_final[i].equals(answers[num_answ[i]])) mark++;
        }
       flow.getChildren().clear();
        if(mark>num/2){
            flow.setStyle("-fx-background-color: #77c2bb");
            line.setStroke(Paint.valueOf("#77c2bb"));
        }
        else {
            flow.setStyle("-fx-background-color:#c27889");
            line.setStroke(Paint.valueOf("#c27889"));
        }
        Label label = new Label("   Your result: " + mark + "/" + num);
        label.setStyle("-fx-font-size: 120");
        flow.getChildren().add(line);
        flow.getChildren().add(label);



    }

    public void load_test(ActionEvent event) throws CryptoException, IOException {
        Random rand = new Random();
        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<Integer> temp_num = new ArrayList<>();
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> temp_answers = new ArrayList<>();
        textFields = new ArrayList<>();
        Additional_func additional_func = new Additional_func();
        File enc_file = additional_func.select_file();
        String path = enc_file.getAbsolutePath().replaceAll("Auto_test_enc.prp","");
        File dec_file = new File(path + "Dec_test.prp");
        Encrypt_Decrypt.decrypt("ADFGXH nice JLKl",enc_file,dec_file);
        Scanner reader = new Scanner(dec_file);
        int total = reader.nextInt();
        num = reader.nextInt();
        num_answ = new int[num];
        answers = new ArrayList[total];
        reader.nextLine();
            for (int i = 0; i < total; i++) {
                questions.add(reader.nextLine());
                temp_answers.add(reader.nextLine());
            }
            for (int i = 0; i < total; i++) {
            answers[i] = new ArrayList<String>();
            answers[i].addAll(Arrays.asList(temp_answers.get(i).split(" ")));
            answers[i].removeIf(item -> item == null || "".equals(item));
            answers[i].replaceAll(String::toLowerCase);
        }
            reader.close();
            dec_file.delete();
        for(int i =0;i<num*2;i++){
            lines.add(new Line());
            lines.get(i).setEndX(982);
            lines.get(i).setStroke(Paint.valueOf("#F4F4F4"));
            lines.get(i).setStrokeWidth(10);
        }
        for(int j = 0;j<total;j++) temp_num.add(j);
        Collections.shuffle(temp_num);
        for (int j=0; j<num; j++) num_answ[j] = temp_num.get(j);
        flow.getChildren().clear();
        flow.setStyle("-fx-background-color: #F4F4F4; ");
        for (int i = 0; i < num; i++) {
            textFields.add(new TextField());
            textFields.get(i).setStyle("-fx-background-color:#4B6EAF; -fx-background-radius: 20;");
            flow.getChildren().add(lines.get(i+num));
            flow.getChildren().add(new Text("№_" + (i+1) + " " + questions.get(num_answ[i])));
            flow.getChildren().add(lines.get(i));
            flow.getChildren().add(textFields.get(i));
        }


    }


}
