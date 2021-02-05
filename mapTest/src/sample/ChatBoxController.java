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
        //this will be changed to input validation when we get to that stage
            if(!t.getText().equals("") && t.getText().length() <= 21)
            {
                messages.add(new Label(t.getText()));
            }
            else
            {
                messages.add(new Label("Invalid Input!"));
            }
            t.clear();
            vBox.getChildren().add(messages.get(messageIndex));
            messageIndex++;
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
