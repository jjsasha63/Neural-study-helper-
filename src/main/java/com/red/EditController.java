package com.red;

import com.jfoenix.controls.*;
import com.red.processing.Additional_func;
import com.red.processing.Encrypt_Decrypt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.bouncycastle.crypto.CryptoException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EditController {


    @FXML public AnchorPane anchor;
    public FlowPane flow;
    public Boolean state;
    static public ArrayList<Boolean> hold_sentences;
    static ArrayList<String>[] words, answers;
    public JFXComboBox number_sentences;
    public ScrollPane scroll;
    double x,y;


    public void info_action2(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Info_UI");
    }

    public void text_action2(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Input_UI");
    }

    public void student_action2(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Student_UI");
    }

    public void next_action2(MouseEvent mouseEvent) throws IOException {
        App.setRoot("Student_UI");
    }

    public void load_data(ActionEvent actionEvent) {
        ArrayList<String> res = Controller.arr_result;
        List<ToggleButton>[] buttonlist = new ArrayList[res.size()];
        List<ToggleButton> sentence = new ArrayList<>();
        scroll.setFitToWidth(true);
        words = new ArrayList[res.size()];
        answers = new ArrayList[res.size()];
        flow.setStyle("-fx-background-color: #FFF; -fx-background-radius: 20; -fx-border-radius: 20; ");
        ArrayList<Line> lines = new ArrayList<>();
        String temp = new String();
        hold_sentences = new ArrayList<>();
        state = false;
        ObservableList<Integer> combo_list = FXCollections.observableArrayList();
        for(int i=0;i<words.length;i++) combo_list.add(i+1);
        number_sentences.setItems(combo_list);
        number_sentences.getSelectionModel().select(words.length-1);


        for (int i = 0; i < res.size(); i++) {
            buttonlist[i] = new ArrayList<ToggleButton>();
            words[i] = new ArrayList<String>();
            answers[i] = new ArrayList<String>();
        }


        for(int j=0;j<res.size();j++) {
            for (char i:res.get(j).toCharArray()) {
                if(i == ' '){
                    words[j].add(temp);
                    temp = "";
                    words[j].removeIf(item -> item == null || "".equals(item));
                }
                else temp += i;
            }
        }

        int i=0,j=0;
        flow.getChildren().clear();

       for(ArrayList<String> a:words){
          sentence.add(new ToggleButton("№-"+(i+1)));
          sentence.get(i).setStyle("-fx-background-color:#009688; -fx-background-radius: 20;");
           sentence.get(i).setTextFill(Color.WHITE);
          flow.getChildren().add(sentence.get(i));
          final int temp_index = i;
          final ToggleButton temp_butt = sentence.get(i);
          hold_sentences.add(true);
          sentence.get(i).setOnAction(new EventHandler<ActionEvent>() {
              @Override
              public void handle(ActionEvent event) {
                  if (temp_butt.isSelected()) {
                      temp_butt.setStyle("-fx-background-color:#8f0015; -fx-background-radius: 20;");
                      temp_butt.setTextFill(Color.WHITE);
                      hold_sentences.set(temp_index,false);
                  } else {
                      temp_butt.setStyle("-fx-background-color:#009688; -fx-background-radius: 20;");
                      temp_butt.setTextFill(Color.WHITE);
                      hold_sentences.set(temp_index,true);

                  }
              }
          });

           for(String b:a){

             buttonlist[i].add(new ToggleButton(b));
             buttonlist[i].get(j).setStyle("-fx-background-color: #7B8084; -fx-background-radius: 20;");
             buttonlist[i].get(j).setTextFill(Color.WHITE);
             final int temp_ind = j;
             final ToggleButton temp_button = buttonlist[i].get(j);
             answers[i].add("");
             buttonlist[i].get(j).setOnAction(new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                      if (!state) {
                          if (temp_button.isSelected()) {
                              temp_button.setStyle("-fx-background-color:#77c2bb; -fx-background-radius: 20;");
                              temp_button.setTextFill(Color.WHITE);
                              temp_button.setText("________");
                              words[temp_index].set(temp_ind,"__________");
                              answers[temp_index].set(temp_ind,b);
                          } else {
                              temp_button.setStyle("-fx-background-color: #7B8084; -fx-background-radius: 20;");
                              temp_button.setTextFill(Color.WHITE);
                              temp_button.setText(b);
                              words[temp_index].set(temp_ind,b);
                              answers[temp_index].set(temp_ind,"");
                          }
                      }
                      else {
                          if (temp_button.isSelected()) {
                              temp_button.setStyle("-fx-background-color:#c27889; -fx-background-radius: 20;");
                              temp_button.setTextFill(Color.WHITE);
                              temp_button.setText("");
                              words[temp_index].set(temp_ind,"");
                          } else {
                              temp_button.setStyle("-fx-background-color: #7B8084; -fx-background-radius: 20;");
                              temp_button.setTextFill(Color.WHITE);
                              temp_button.setText(b);
                              words[temp_index].set(temp_ind,b);
                          }
                      }

             }});
             j++;
           }
           flow.getChildren().addAll(buttonlist[i]);
           lines.add(new Line());
           lines.get(i).setEndX(900);
           lines.get(i).setStroke(Paint.valueOf("#FFF"));
           lines.get(i).setStrokeWidth(10);
           flow.getChildren().add(lines.get(i));
           i++;
           j=0;
       }

    }


    public void save_document(ActionEvent actionEvent) throws IOException {
        Additional_func additional_func = new Additional_func();
        ArrayList<String>[] text = words;
     //   String path = additional_func.select_dir().getAbsolutePath();
        String temp = new String();
        XWPFDocument document = new XWPFDocument();
        XWPFDocument document_answ = new XWPFDocument();
        FileOutputStream out = new FileOutputStream(additional_func.select_dir("DOCX files (*.docx)","*.docx","Save questions file"));
        FileOutputStream out_answ = new FileOutputStream(additional_func.select_dir("DOCX files (*.docx)","*.docx","Save answers file"));
        XWPFParagraph paragraph = document.createParagraph();
        XWPFParagraph paragraph_answ = document_answ.createParagraph();
        XWPFRun run = paragraph.createRun();
        XWPFRun run_answ = paragraph_answ.createRun();
        run.setText("Test generated by a Study Preparator  " + LocalDateTime.now());
        run.addBreak();
        run.addBreak();
        run.addBreak();
        run_answ.setText("Answers");
        run_answ.addBreak();
        run_answ.addBreak();

        for (int i = 0; i < text.length; i++){
            if (hold_sentences.get(i)) {
                run_answ.setText("Question №-" + (i+1) + " - ");
                run.setText("№-" + (i+1) + " ");
                for (int j = 0; j < text[i].size(); j++) {
                    run.setText(text[i].get(j) + " ");
                    run_answ.setText(answers[i].get(j) + "/");
                }
                run_answ.addBreak();
                run_answ.addBreak();
                run.addBreak();
                run.addBreak();
                run.setText("Answer: __________________________________________________________________");
                run.addBreak();
                run.addBreak();
            }

    }
        document.write(out);
        document_answ.write(out_answ);
        document.close();
        document_answ.close();
        out_answ.close();
        out.close();

    }

    public void save_pr_file(ActionEvent actionEvent) throws IOException, CryptoException {
        Additional_func additional_func = new Additional_func();
     //   String path = additional_func.select_dir().getAbsolutePath();
        ArrayList<String>[] text = words;
        File enc_file = additional_func.select_dir("PRP files (*.prp)","*.prp","Save app file");
        File dec_file = new File(enc_file.getAbsolutePath().replaceAll(enc_file.getName(),"") + "\\Auto_test.prp");
        FileWriter fileWriter = new FileWriter(dec_file);
        int total = 0 , num = number_sentences.getSelectionModel().getSelectedIndex() + 1;
        for(Boolean a:hold_sentences){
            if(a) total++;
        }
        if(num > total) num = total;
            fileWriter.write(total + "\n" + num + "\n");

        for (int i = 0; i < text.length; i++){
            if (hold_sentences.get(i)) {
                for (int j = 0; j < text[i].size(); j++) {
                   fileWriter.write(text[i].get(j) + " ");
                }
                fileWriter.write("\n");
                for (int j = 0; j < text[i].size(); j++) {
                   fileWriter.write(answers[i].get(j) + " ");
                }
                fileWriter.write("\n");
            }
        }
        fileWriter.close();
        Encrypt_Decrypt.encrypt("ADFGXH nice JLKl",dec_file,enc_file);
        enc_file.setReadOnly();
        dec_file.delete();

    }


    public void edit_action(ActionEvent event) {
        if(!state) state = true;
        else state = false;
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

