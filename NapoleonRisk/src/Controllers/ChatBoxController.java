package Controllers;

import GameLogic.Gamestate;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ChatBoxController
{
    //instance variables
    public TextField textInput;
    public ScrollPane scrollPane;
    public VBox vBox;
    private MainController mainController;
    private boolean waitingTextInput = false;
    Gamestate gamestate;

    //ArrayList of labels that are displayed in the vBox
    //messageIndex used to track latest entry
    public ArrayList<Label> messages = new ArrayList<Label>();
    public int messageIndex = 0;

    //passes MainController to this one so that its functions can be referenced
    public void injectMainController(MainController mainController)//sets the main controller to our MainController
    {
        this.mainController = mainController;
    }

    //method that outputs text to the chat box
    public void textOutput(TextField t)
    {
        //lines array that stores each new message for the chatbox
        ArrayList<String> lines = new ArrayList<String>();
        int newMessages; //number of new lines, this accounts for how to increment message index

        //Algorithm for splitting long strings into multiple messages appropriately for the chat box
        /*
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
        }*/

        lines.add(t.getText());
        //this will be changed to input validation when we get to that stage
            if(!t.getText().equals(""))
            {
                newMessages = lines.size();
                for(String message:lines) {
                    Text text = new Text(message);
                    text.setFont(Font.font("PT Serif", 11));
                    Label label = new Label(text.getText());

                    //textToPlayerColour is set to either 0 or 1 when we want to output text in the player colour
                    //otherwise it's set to -1
                    if(gamestate.getTextToPlayerColour() == 0 || gamestate.getTextToPlayerColour() == 1)
                    {
                        label.setTextFill(gamestate.getPlayers()[gamestate.getTextToPlayerColour()].getColour());
                    }
                    messages.add(label);
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

    //scrolls the scroll pane when new messages exceed it's current size
    public void updateScroll()
    {
        scrollPane.vvalueProperty().bind(vBox.heightProperty());
    }

    //action event for sending a message from text field when clicking the send button
    public void sendMessageButton(ActionEvent actionEvent)
    {
        if(waitingTextInput){
            gamestate.getTextInput(textInput);
            waitingTextInput = false;
            textInput.clear();
        }else{
            textOutput(textInput);
        }
    }

    //action event for sending a message from the text field when enter is pressed
    public void sendMessageEnter(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.ENTER)
        {
            if(waitingTextInput){
                gamestate.getTextInput(textInput);
                textInput.clear();
            }else{
                textOutput(textInput);
            }
        }
    }

    //setter to allow GameState to know when we are waiting for user input
    public void setWaitingTextInput(boolean state){
        waitingTextInput = state;
    }

    //passes GameState object to this controller
    public void setGameState(Gamestate gamestate){
        this.gamestate = gamestate;
    }


}
