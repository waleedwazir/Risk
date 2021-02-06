package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ChatBoxController
{
    public TextField textInput;
    public ScrollPane scrollPane;
    public VBox vBox;
    private  MainController mainController;

    public ArrayList<Label> messages = new ArrayList<Label>();
    public int messageIndex = 0;

    public void injectMainController(MainController mainController)//sets the main controller to our maincontroller
    {
        this.mainController = mainController;
    }

    public void textOutput(TextField t)
    {
        //lines array that stores each new message for the chatbox
        ArrayList<String> lines = new ArrayList<String>();
        int newMessages; //number of new lines, this accounts for how to increment message index

        //Algorithm for splitting long strings into multiple messages appropriately for the chat box
        if(t.getText().length()>21) {
            String remainder = t.getText();
            while (!remainder.isEmpty()) {
                String line;
                if(remainder.length()>21) {
                    line = remainder.substring(0, 20);
                    int whitespace = line.lastIndexOf(" ");
                    line = line.substring(0, whitespace);
                    remainder = remainder.substring(whitespace+1);
                }else{
                    line = remainder;
                    remainder = "";
                }
                lines.add(line);
            }
            //System.out.print(lines);
        }else{
            lines.add(t.getText());
        }

        //this will be changed to input validation when we get to that stage
            if(!t.getText().equals(""))
            {
                newMessages = lines.size();
                for(String message:lines) {
                    messages.add(new Label(message));
                }
            }
            else
            {
                newMessages = 1;
                messages.add(new Label("Invalid Input!"));
            }
            t.clear();
            for(int i=0;i<newMessages;i++) {
                vBox.getChildren().add(messages.get(messageIndex));
                messageIndex++;
            }
            updateScroll();//scrolls the pane as text comes in

    }
    public void updateScroll()
    {
        scrollPane.vvalueProperty().bind(vBox.heightProperty());
    }

    public void sendMessageButton(ActionEvent actionEvent)
    {
        textOutput(textInput);
    }


    public void sendMessageEnter(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.ENTER)
        {
            textOutput(textInput);
        }
    }



}
