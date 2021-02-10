package GameLogic;

import Player.Player;
import javafx.scene.control.TextField;
import Controllers.ChatBoxController;
import Controllers.MainController;
import javafx.scene.paint.Color;

public class Gamestate {
    Player player1 = new Player();
    Player player2 = new Player();
    Player[] players = {player1, player2};

    Dice dice = new Dice();

    //Contoller object that the gamestate interacts with
    ChatBoxController chatBoxController;
    MainController mainController;

    //used for the logic implementation of receiving payer names
    boolean waitingPlayerNames = false;


    //initialises the two controllers so that the gamestate object can reference the appropriate ones
    public void setController(ChatBoxController chatBoxController, MainController mainController){
        this.chatBoxController = chatBoxController;
        this.mainController = mainController;
    }


    //game start logic, welcomes and initialise players, then starts user input logic
    public void Gamestart(){
        chatBoxController.textOutput(new TextField("Welcome to Risk!"));
        setPlayerName(players, 0);
        players[0].setColors(Color.TOMATO); //Set player colours
        players[1].setColors(Color.ROYALBLUE);
    }

    //algorithm and game logic for retreiving player information from controller
    public void setPlayerName(Player[] players, int index){
        chatBoxController.textOutput(new TextField("Player "+(++index)+", enter your name:"));
        chatBoxController.setWaitingTextInput(true);
        if(players[0].getName()==null) {
            waitingPlayerNames = true;
        }
    }

    //player array getter
    public Player[] getPlayers()
    {
        return players;
    }

    //method for retreiving input from the chatbox controller and the processes it appropriately
    public void getTextInput(TextField t){
        chatBoxController.textOutput(new TextField("> "+t.getText()));
        if(waitingPlayerNames){
            players[0].setName(t.getText());
            waitingPlayerNames = false;
            setPlayerName(players, 1);
        }else{
            players[1].setName(t.getText());
            mainController.distributeCountries();
            System.out.println(dice.rollXDice(5));
            chatBoxController.setWaitingTextInput(false);
        }
    }

    //logic implementation for the logic initialisation of claiming countries, the rest of the implementation
    //is handled in the main controller
    public void startClaimPhase()
    {
        mainController.setPlayerClaim(true);
        chatBoxController.textOutput(new TextField("Claim your countries!"));
        chatBoxController.textOutput(new TextField(players[0].getName()+" claim a country!"));
    }

}
