package GameLogic;

import Map.Country;
import Player.*;
import javafx.scene.control.TextField;
import Controllers.ChatBoxController;
import Controllers.MainController;
import javafx.scene.paint.Color;


public class Gamestate {
    Player player1 = new Player(Color.TOMATO, 36);
    Player player2 = new Player(Color.ROYALBLUE, 36);
    Player[] players = {player1, player2};
    Player[] neutrals = new Player[4];
    private Color[] neutralColours = {Color.YELLOW,Color.ORANGE,Color.YELLOWGREEN,Color.VIOLET};
    private int turn = 0;


    Army[] armies;

    //Controller object that the GameState interacts with
    ChatBoxController chatBoxController;
    MainController mainController;

    int textToPlayerColour = -1;

    //used for the logic implementation of receiving payer names
    boolean waitingPlayer1Name = false;
    boolean waitingPlayer2Name = false;
    boolean waitingPlayer1Deployment = false;
    boolean waitingPlayer2Deployment = false;


    //initialises the two controllers so that the gamestate object can reference the appropriate ones
    public void setController(ChatBoxController chatBoxController, MainController mainController){
        this.chatBoxController = chatBoxController;
        this.mainController = mainController;
    }


    //game start logic, welcomes and initialise players, then starts user input logic
    public void Gamestart(){
        for(int i=0;i<4;i++) {
            neutrals[i] = new Player(neutralColours[i]);
        }

        chatBoxController.textOutput(new TextField("Welcome to Risk!"));
        setPlayerName(players, 0);
    }

    //algorithm and game logic for retreiving player information from controller
    public void setPlayerName(Player[] players, int index){
        chatBoxController.textOutput(new TextField("Player "+(++index)+", enter your name:"));
        chatBoxController.setWaitingTextInput(true);
        if(players[0].getName()==null) {
            waitingPlayer1Name = true;
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
        if(waitingPlayer1Name){
            players[0].setName(t.getText());

            //outputs next message in player 1's colour
            textToPlayerColour = 0;
            chatBoxController.textOutput(new TextField(players[0].getName()+" you are RED"));
            textToPlayerColour = -1;

            waitingPlayer1Name = false;
            waitingPlayer2Name = true;
            setPlayerName(players, 1);

        }else if(waitingPlayer2Name){
            players[1].setName(t.getText());

            //outputs next message in player 2's colour
            textToPlayerColour = 1;
            chatBoxController.textOutput(new TextField(players[1].getName()+" you are BLUE"));
            textToPlayerColour = -1;

            mainController.distributeCountries();
            chatBoxController.setWaitingTextInput(false);
            waitingPlayer2Name = false;

        }else if(waitingPlayer1Deployment)
        {
            String countryName = t.getText();
            int countryIndex = Country.getIndexFromCountryName(countryName);
            Army army = armies[countryIndex];
            army.incrementSize(3);
            mainController.updateNode(army);
            waitingPlayer1Deployment = false;
            waitingPlayer2Deployment = true;
            chatBoxController.textOutput(new TextField(players[1].getName()+" deploy troops!"));

        }else if(waitingPlayer2Deployment)
        {
            String countryName = t.getText();
            int countryIndex = Country.getIndexFromCountryName(countryName);
            Army army = armies[countryIndex];
            army.incrementSize(3);
            mainController.updateNode(army);

            //logic for neutrals will go here

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

    public Player[] getNeutrals(){
        return neutrals;
    }

    //method that will call all the phases of the GameState
    public void GameTurns()
    {
        deploymentPhase();
    }

    //begins the deployment phase, allowing players and neutrals to deploy their troops
    //called in main controller
    public void deploymentPhase()
    {
        waitingPlayer1Deployment = true;
        chatBoxController.setWaitingTextInput(true);
        chatBoxController.textOutput(new TextField("Input the name of the country you want to deploy troops on!"));
        chatBoxController.textOutput(new TextField(players[0].getName()+" deploy troops!"));
    }
    public void passArmies(Army[] armies)
    {
        this.armies = armies;
    }
    public int getTextToPlayerColour()
    {
        return textToPlayerColour;
    }



}
