package GameLogic;

import Cards.Card;
import Cards.Deck;
import Map.Country;
import Player.*;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import Controllers.ChatBoxController;
import Controllers.MainController;
import javafx.scene.paint.Color;
import java.util.Random;


public class Gamestate {
    Player player1 = new Player(Color.TOMATO, 36);
    Player player2 = new Player(Color.ROYALBLUE, 36);
    Player[] players = {player1, player2};
    Player[] neutrals = new Player[4];
    private Color[] neutralColours = {Color.YELLOW,Color.ORANGE,Color.YELLOWGREEN,Color.VIOLET};
    private int turn = 0;
    private Dice dice = new Dice();


    Army[] armies;


    //Controller object that the GameState interacts with
    ChatBoxController chatBoxController;
    MainController mainController;

    //this needs to be changed
    Deck deck;
    Card card = new Card();

    int textToPlayerColour = -1;

    //used for the logic implementation of receiving payer names
    boolean waitingPlayer1Name = false;
    boolean waitingPlayer2Name = false;
    boolean waitingPlayer1Deployment = false;
    boolean waitingPlayer2Deployment = false;

    boolean waitingPlayer1Roll = false;
    boolean waitingPlayer2Roll = false;

    //stores the value of the players rolls
    private int player1Roll;
    private int player2Roll;



    //initialises the two controllers so that the gamestate object can reference the appropriate ones
    public void setController(ChatBoxController chatBoxController, MainController mainController){
        this.chatBoxController = chatBoxController;
        this.mainController = mainController;
        deck = new Deck(mainController.getCountries());
    }


    //game start logic, welcomes and initialise players, then starts user input logic
    public void Gamestart(){
        for(int i=0;i<4;i++) {
            neutrals[i] = new Player(neutralColours[i]);
        }

        chatBoxController.textOutput(new TextField("Welcome to Risk!"));
        chatBoxController.textOutput(new TextField("Clicking on a country will print it's name to the console,"));
        chatBoxController.textOutput(new TextField("this feature is for ease of use and eliminates the need of typing!"));
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

            card = deck.draw();
            players[0].getHand().add(card);
            chatBoxController.textOutput(new TextField(players[0].getName()+" drew the " + card.toString()+" card"));

            waitingPlayer1Name = false;
            waitingPlayer2Name = true;
            setPlayerName(players, 1);

        }else if(waitingPlayer2Name){
            players[1].setName(t.getText());

            //outputs next message in player 2's colour
            textToPlayerColour = 1;
            chatBoxController.textOutput(new TextField(players[1].getName()+" you are BLUE"));

            textToPlayerColour = -1;

            card = deck.draw();
            players[1].getHand().add(card);
            chatBoxController.textOutput(new TextField(players[1].getName()+" drew the " + card.toString() +" card"));

            mainController.distributeCountries();
            chatBoxController.setWaitingTextInput(false);
            waitingPlayer2Name = false;

        }else if(waitingPlayer1Deployment)
        {
            String countryName = t.getText();
            int countryIndex = Country.getIndexFromCountryName(countryName);
            if(countryIndex!=-1) {
                Army army = armies[countryIndex];
                chatBoxController.setWaitingTextInput(true);
                if (army.getPlayer() == players[0]) {
                    chatBoxController.textOutput(new TextField("Troops deployed to " + Country.getCountryNameFromIndex(countryIndex) + "!"));
                    players[0].decrementTroops(3);
                    army.incrementSize(3);
                    mainController.updateNode(army);
                    waitingPlayer1Deployment = false;
                    waitingPlayer2Deployment = true;
                    chatBoxController.textOutput(new TextField(players[1].getName() + " deploy troops!"));
                } else {
                    chatBoxController.textOutput(new TextField("Invalid selection, choose a country you own!"));
                }
            }else{
                chatBoxController.textOutput(new TextField("Invalid input! Please try again."));
            }

        }else if(waitingPlayer2Deployment)
        {
            String countryName = t.getText();
            int countryIndex = Country.getIndexFromCountryName(countryName);
            if(countryIndex!=-1) {
                Army army = armies[countryIndex];
                if (army.getPlayer() == players[1]) {
                    chatBoxController.textOutput(new TextField("Troops deployed to " + Country.getCountryNameFromIndex(countryIndex) + "!"));
                    players[1].decrementTroops(3);
                    army.incrementSize(3);
                    mainController.updateNode(army);
                    waitingPlayer2Deployment = false;
                    deploymentPhase(2);
                } else {
                    chatBoxController.textOutput(new TextField("Invalid selection, choose a country you own!"));
                    chatBoxController.setWaitingTextInput(true);
                }
            }else{
                chatBoxController.textOutput(new TextField("Invalid input! Please try again."));
            }

        }else if(waitingPlayer1Roll)
        {
            if(t.getText().equals("roll"))
            {
                player1Roll = dice.throwDice();
                chatBoxController.textOutput(new TextField(players[0].getName()+" rolled a "+player1Roll));
                waitingPlayer1Roll = false;
                waitingPlayer2Roll = true;
                chatBoxController.textOutput(new TextField(players[1].getName()+" roll your dice!"));
            }else
            {
                chatBoxController.textOutput(new TextField("Invalid command -> use \"roll\" to roll a dice!"));
            }

        }else if(waitingPlayer2Roll)
        {
            if(t.getText().equals("roll"))
            {
                player2Roll = dice.throwDice();
                chatBoxController.textOutput(new TextField(players[1].getName()+" rolled a "+player2Roll));
                if(player1Roll == player2Roll)
                {
                    waitingPlayer1Roll = true;
                    waitingPlayer2Roll = false;
                    chatBoxController.textOutput(new TextField("It was a tie! Please roll again!"));
                    chatBoxController.textOutput(new TextField(players[0].getName()+" roll your dice!"));
                }
                else if(player2Roll > player1Roll)
                {
                    Player temp = players[0];
                    players[0] = players[1];
                    players[1] = temp;
                }
                if(!(player1Roll == player2Roll))
                {
                    waitingPlayer2Roll = false;
                    chatBoxController.textOutput(new TextField(players[0].getName()+" won !!! They get to go first!"));
                    chatBoxController.setWaitingTextInput(false);
                    chatBoxController.textOutput(new TextField(">>>The Deployment Phase will now being<<<"));
                    GameTurns(2);
                }
            }else
            {
                chatBoxController.textOutput(new TextField("Invalid command -> use \"roll\" to roll a dice!"));
            }
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
    public void GameTurns(int phase)
    {
        if(phase==1) {
            rollForTurnOrder();
        }else if (phase == 2)
        {
            deploymentPhase(1);
        }
    }

    //begins the deployment phase, allowing players and neutrals to deploy their troops
    //called in main controller
    public void deploymentPhase(int phase)
    {
        if(phase==1 && players[0].getTroops()>0) {//start player deployment logic
            waitingPlayer1Deployment = true;
            chatBoxController.setWaitingTextInput(true);
            chatBoxController.textOutput(new TextField("Input the name of the country you want to deploy troops on!"));
            chatBoxController.textOutput(new TextField(players[0].getName() + " deploy troops!"));
        }else if(neutrals[0].getTroops()>0){//start neutral deployment logic
                Thread neutralDeploy = new Thread(() -> {
                    for(int i=0;i<4;i++){
                        Random generator = new Random();
                        Object[] values = neutrals[i].getAssignedCountries().values().toArray();
                        Country randomCountry = (Country) values[generator.nextInt(values.length)];
                        Army army = armies[randomCountry.getIndex()];
                        neutrals[i].decrementTroops(1);
                        army.incrementSize(1);
                        mainController.updateNode(army);
                        Platform.runLater(() -> chatBoxController.textOutput(new TextField("Neutral deployed a troop to "+randomCountry.getName()+"!")));
                        try {
                            Thread.sleep(300);
                        } catch(InterruptedException v){System.out.println(v);}
                    }
                    Platform.runLater(() -> deploymentPhase(1));

                });
                neutralDeploy.start();
            }else if(players[0].getTroops()>0){
            deploymentPhase(1);
        }else{
            chatBoxController.textOutput(new TextField("Deployment phase over!"));
            GameTurns(3);
        }


    }

    public void passArmies(Army[] armies)
    {
        this.armies = armies;
    }

    public int getTextToPlayerColour()
    {
        return textToPlayerColour;
    }

    //called in GameTurns to roll for turn order between the two players
    public void rollForTurnOrder()
    {
        chatBoxController.textOutput(new TextField("Players will now roll dice to determine turn order! Type \"roll\" to do so"));
        chatBoxController.textOutput(new TextField(players[0].getName() +" roll your dice!"));
        waitingPlayer1Roll = true;
        chatBoxController.setWaitingTextInput(true);
    }



}
