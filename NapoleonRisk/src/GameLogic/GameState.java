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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class GameState
{
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

    //declare deck
    Deck deck;

    int textToPlayerColour = -1;
    int attackingCountryIndex;
    int defendingCountryIndex;
    int sizeOfAttackingArmy;
    int goldenGrid = 1;
    int goldenHorse = 4;

    //used for the logic implementation of receiving payer names
    boolean waitingPlayer1Name = false;
    boolean waitingPlayer2Name = false;
    boolean waitingPlayer1Deployment = false;
    boolean waitingPlayer2Deployment = false;
    boolean waitingPlayer1Roll = false;
    boolean waitingPlayer2Roll = false;
    boolean waitingPlayer1Option = false;
    boolean waitingPlayer2Option = false;
    boolean waitingPlayer1SizeOfAttackingArmy = false;
    boolean waitingPlayer2SizeOfAttackingArmy = false;
    boolean waitingPlayer1Attack = false;
    boolean waitingPlayer2Attack = false;
    boolean waitingPlayer1Reinforce = false;
    boolean waitingPlayer2Reinforce = false;
    boolean waitingPlayer1Fortify = false;
    boolean waitingPlayer2Fortify = false;
    boolean waitingPlayer1FortifyDestination = false;
    boolean waitingPlayer2FortifyDestination = false;
    boolean waitingPlayer1FortifyAmount = false;
    boolean waitingPlayer2FortifyAmount = false;
    boolean territoryConqueredPlayer1 = false;
    boolean territoryConqueredPlayer2 = false;
    boolean waitingPlayer1CardOption = false;
    boolean waitingPlayer2CardOption = false;

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
    public void getTextInput(TextField t)
    {
        chatBoxController.textOutput(new TextField("> " + t.getText()));

        //game logic decides which input we are waiting for from the user, this if else if...
        //then decides what to do with the received data
        if (waitingPlayer1Name)
        {
            if(!(t.getText().equals("")))
            {
                players[0].setName(t.getText());

                //outputs next message in player 1's colour
                textToPlayerColour = 0;
                chatBoxController.textOutput(new TextField(players[0].getName() + " you are RED"));
                textToPlayerColour = -1;
                waitingPlayer1Name = false;
                waitingPlayer2Name = true;
                setPlayerName(players, 1);
            }else
            {
                chatBoxController.textOutput(new TextField("Player name cannot be empty!"));
                setPlayerName(players,0);
            }


        } else if (waitingPlayer2Name)
        {
            if(!(t.getText().equals("")))
            {
                players[1].setName(t.getText());

                //outputs next message in player 2's colour
                textToPlayerColour = 1;
                chatBoxController.textOutput(new TextField(players[1].getName() + " you are BLUE"));
                textToPlayerColour = -1;
                mainController.distributeCountries();
                chatBoxController.setWaitingTextInput(false);
                waitingPlayer2Name = false;
            }else
            {
                chatBoxController.textOutput(new TextField("Player name cannot be empty!"));
                setPlayerName(players,1);
            }

        } else if (waitingPlayer1Deployment)
        {
            String countryName = t.getText();
            int countryIndex = Country.getIndexFromCountryName(countryName);
            if (countryIndex != -1)
            {
                chatBoxController.setWaitingTextInput(true);
                Army army = armies[countryIndex];
                if (army.getPlayer() == players[0])
                {
                    chatBoxController.textOutput(new TextField("Troops deployed to " + Country.getCountryNameFromIndex(countryIndex) + "!"));
                    if(players[0].getTroops()>=3) {
                        players[0].decrementTroops(3);
                        army.incrementSize(3);
                    }else{
                        army.incrementSize(players[0].getTroops());
                        players[0].decrementTroops(players[0].getTroops());
                    }
                    mainController.updateNode(army);
                    waitingPlayer1Deployment = false;
                    if(players[1].getTroops()>0) {
                        waitingPlayer2Deployment = true;
                        chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " deploy troops!"));
                    }else{
                        deploymentPhase(2);
                    }
                } else
                {
                    chatBoxController.textOutput(new TextField("Invalid selection, choose a country you own!"));
                }
            } else
            {
                chatBoxController.setWaitingTextInput(true);
                chatBoxController.textOutput(new TextField("Invalid input! Please try again."));
            }

        } else if (waitingPlayer2Deployment)
        {
            String countryName = t.getText();
            int countryIndex = Country.getIndexFromCountryName(countryName);
            if (countryIndex != -1)
            {
                Army army = armies[countryIndex];
                if (army.getPlayer() == players[1])
                {
                    chatBoxController.textOutput(new TextField("Troops deployed to " + Country.getCountryNameFromIndex(countryIndex) + "!"));
                    if(players[1].getTroops()>=3) {
                        players[1].decrementTroops(3);
                        army.incrementSize(3);
                    }else{
                        army.incrementSize(players[1].getTroops());
                        players[1].decrementTroops(players[1].getTroops());
                    }
                    mainController.updateNode(army);
                    waitingPlayer2Deployment = false;
                    deploymentPhase(2);
                } else
                {
                    chatBoxController.textOutput(new TextField("Invalid selection, choose a country you own!"));
                    chatBoxController.setWaitingTextInput(true);
                }
            } else
            {
                chatBoxController.setWaitingTextInput(true);
                chatBoxController.textOutput(new TextField("Invalid input! Please try again."));
            }

        } else if (waitingPlayer1Roll)
        {
            if (t.getText().equals("roll"))
            {
                player1Roll = dice.throwDice();
                chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " rolled a " + player1Roll));
                waitingPlayer1Roll = false;
                waitingPlayer2Roll = true;
                chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " roll your dice!"));
            } else
            {
                chatBoxController.textOutput(new TextField("Invalid command -> use \"roll\" to roll a dice!"));
                chatBoxController.setWaitingTextInput(true);
            }

        } else if (waitingPlayer2Roll)
        {
            if (t.getText().equals("roll"))
            {
                player2Roll = dice.throwDice();
                chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " rolled a " + player2Roll));
                if (player1Roll == player2Roll)
                {
                    waitingPlayer1Roll = true;
                    waitingPlayer2Roll = false;
                    chatBoxController.textOutput(new TextField("It was a tie! Please roll again!"));
                    chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " roll your dice!"));
                } else if (player2Roll > player1Roll)
                {
                    Player temp = players[0];
                    players[0] = players[1];
                    players[1] = temp;
                }
                if (!(player1Roll == player2Roll))
                {
                    waitingPlayer2Roll = false;
                    chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " won !!! They get to go first!"));
                    chatBoxController.setWaitingTextInput(false);
                    chatBoxController.textOutput(new TextField(" "));
                    chatBoxController.textOutput(new TextField(">>>The Deployment Phase will now begin<<<"));
                    chatBoxController.textOutput(new TextField(" "));
                    GameTurns(2);
                }
            } else
            {
                chatBoxController.textOutput(new TextField("Invalid command -> use \"roll\" to roll a dice!"));
                chatBoxController.setWaitingTextInput(true);
            }
        } else if (waitingPlayer1Option)
        {
            int countryIndex = Country.getIndexFromCountryName(t.getText());
            if (t.getText().equals("skip"))
            {
                waitingPlayer1Option = false;
                waitingPlayer1Fortify = true;
                chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " it is your chance to fortify!"));
                chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " enter country you wish to move troops from or \"skip\"!"));
                chatBoxController.setWaitingTextInput(true);
            } else if (countryIndex != -1 && !allAdjacentAllies(countryIndex,players[0]))
            {
                Army army = armies[countryIndex];
                if (army.getPlayer() == players[0] && army.getArmySize()>1)
                {
                    waitingPlayer1Option = false;
                    attackingCountryIndex = Country.getIndexFromCountryName(t.getText());
                    chatBoxController.textOutput(new TextField("Enter how many troops you wish to attack with:"));
                    waitingPlayer1SizeOfAttackingArmy = true;
                    chatBoxController.setWaitingTextInput(true);
                }else
                {
                    if (army.getPlayer() == players[0] && army.getArmySize() == 1){
                        chatBoxController.textOutput(new TextField("Invalid selection, can not attack with this country!"));
                    }else {
                        chatBoxController.textOutput(new TextField("Invalid selection, cannot choose a country you don't own!"));
                    }
                    chatBoxController.textOutput(new TextField("Choose a different country:"));
                    chatBoxController.setWaitingTextInput(true);
                }
            }else if(countryIndex != -1 && allAdjacentAllies(countryIndex,players[0]))
            {
                chatBoxController.textOutput(new TextField("Invalid input! There are no enemy territories to attack from this country!"));
                chatBoxController.textOutput(new TextField("Choose a different country or skip:"));
                chatBoxController.setWaitingTextInput(true);
            }
            else
            {
                chatBoxController.textOutput(new TextField("Invalid input! choose a country"));
                chatBoxController.setWaitingTextInput(true);
            }

        } else if (waitingPlayer2Option)
        {
            int countryIndex = Country.getIndexFromCountryName(t.getText());
            //if next is skip or if all armies are 1
            if (t.getText().equals("skip"))
            {
                chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " it is your chance to fortify!"));
                chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " enter country you wish to move troops from or \"skip\"!"));
                chatBoxController.setWaitingTextInput(true);
                waitingPlayer2Option = false;
                waitingPlayer2Fortify = true;
            } else if (countryIndex != -1 && !allAdjacentAllies(countryIndex,players[1]))
            {
                Army army = armies[countryIndex];
                if (army.getPlayer() == players[1]  && army.getArmySize()>1)
                {
                    waitingPlayer2Option = false;
                    attackingCountryIndex = Country.getIndexFromCountryName(t.getText());
                    chatBoxController.textOutput(new TextField("Enter how many troops you wish to attack with:"));
                    waitingPlayer2SizeOfAttackingArmy = true;
                    chatBoxController.setWaitingTextInput(true);
                }else
                {
                    if (army.getPlayer() == players[1] && army.getArmySize() == 1){
                        chatBoxController.textOutput(new TextField("Invalid selection, can not attack with this country!"));
                    }else {
                        chatBoxController.textOutput(new TextField("Invalid selection, cannot choose a country you don't own!"));
                    }
                    chatBoxController.textOutput(new TextField("Choose a different country:"));
                    chatBoxController.setWaitingTextInput(true);
                }
            }else if(countryIndex != -1 && allAdjacentAllies(countryIndex,players[1]))
            {
                chatBoxController.textOutput(new TextField("Invalid input! There are no enemy territories to attack from this country!"));
                chatBoxController.textOutput(new TextField("Choose a different country or skip:"));
                chatBoxController.setWaitingTextInput(true);
            }
            else
            {
                chatBoxController.textOutput(new TextField("Invalid input! choose a country"));
                chatBoxController.setWaitingTextInput(true);
            }
        } else if (waitingPlayer1Attack)
        {
            if(t.getText().equals("skip"))
            {

            }
            defendingCountryIndex = Country.getIndexFromCountryName(t.getText());
            Country countryAtk = mainController.getCountries().getCountries().get(attackingCountryIndex);

            //include check that it is not the players own country
            if(countryAtk.isAdjacent(defendingCountryIndex) && armies[defendingCountryIndex].getPlayer()!=armies[attackingCountryIndex].getPlayer())
            {
                boolean won;
                if(armies[defendingCountryIndex].getArmySize() > 1 && sizeOfAttackingArmy > 1)
                {
                    won = determineRollWinner(armies[attackingCountryIndex], armies[defendingCountryIndex], sizeOfAttackingArmy, 2);
                }else
                {
                    won = determineRollWinner(armies[attackingCountryIndex], armies[defendingCountryIndex], sizeOfAttackingArmy, 1);
                }
                if(won){
                    territoryConqueredPlayer1 = true;
                    armies[defendingCountryIndex].getPlayer().removeCountry(armies[defendingCountryIndex].getCountry());
                    armies[defendingCountryIndex].setPlayer(armies[attackingCountryIndex].getPlayer());
                    armies[attackingCountryIndex].getPlayer().addCountry(armies[defendingCountryIndex].getCountry());
                    if(players[1].isEliminated())
                    {
                        endGame(players[0]);
                        chatBoxController.textInput.setDisable(true);
                        chatBoxController.button.setDisable(true);
                        return;
                    }
                    chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + ", you conquered "+armies[defendingCountryIndex].getCountry().getName()+"!"));
                    chatBoxController.textOutput(new TextField("Enter how many troops you would like to move there."));
                    waitingPlayer1Attack = false;
                    waitingPlayer1Reinforce = true;
                    chatBoxController.setWaitingTextInput(true);

                }else {
                    chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " enter country you wish to attack from or \"skip\"!"));
                    waitingPlayer1Attack = false;
                    waitingPlayer1Option = true;
                    chatBoxController.setWaitingTextInput(true);
                }
            }else
            {
                if(armies[defendingCountryIndex].getPlayer()==armies[attackingCountryIndex].getPlayer()){
                    chatBoxController.textOutput(new TextField("Invalid entry! You cannot attack yourself!"));
                } else {
                    chatBoxController.textOutput(new TextField("Invalid entry! You can only attack adjacent countries!"));
                }
                chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " enter country you wish to attack from or \"skip\"!"));
                chatBoxController.setWaitingTextInput(true);
            }


        } else if (waitingPlayer2Attack)
        {
            defendingCountryIndex = Country.getIndexFromCountryName(t.getText());
            Country countryAtk = mainController.getCountries().getCountries().get(attackingCountryIndex);

            //include check that it is not the players own country
            if(countryAtk.isAdjacent(defendingCountryIndex) && armies[defendingCountryIndex].getPlayer()!=armies[attackingCountryIndex].getPlayer())
            {
                boolean won;
                if(armies[defendingCountryIndex].getArmySize() > 1 && sizeOfAttackingArmy > 1)
                {
                    won = determineRollWinner(armies[attackingCountryIndex], armies[defendingCountryIndex], sizeOfAttackingArmy, 2);
                }else
                {
                    won = determineRollWinner(armies[attackingCountryIndex], armies[defendingCountryIndex], sizeOfAttackingArmy, 1);
                }
                if(won){
                    territoryConqueredPlayer2 = true;
                    armies[defendingCountryIndex].getPlayer().removeCountry(armies[defendingCountryIndex].getCountry());
                    armies[defendingCountryIndex].setPlayer(armies[attackingCountryIndex].getPlayer());
                    armies[attackingCountryIndex].getPlayer().addCountry(armies[defendingCountryIndex].getCountry());
                    if(players[0].isEliminated())
                    {
                        endGame(players[1]);
                        chatBoxController.textInput.setDisable(true);
                        chatBoxController.button.setDisable(true);
                        return;
                    }
                    chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + ", you conquered "+armies[defendingCountryIndex].getCountry().getName()+"!"));
                    chatBoxController.textOutput(new TextField("Enter how many troops you would like to move there."));
                    waitingPlayer2Attack = false;
                    waitingPlayer2Reinforce = true;
                    chatBoxController.setWaitingTextInput(true);
                }else {
                    chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " enter country you wish to attack from or \"skip\"!"));
                    waitingPlayer2Attack = false;
                    waitingPlayer2Option = true;
                    chatBoxController.setWaitingTextInput(true);
                }
            }else
            {
                if(armies[defendingCountryIndex].getPlayer()==armies[attackingCountryIndex].getPlayer()){
                    chatBoxController.textOutput(new TextField("Invalid entry! You cannot attack yourself!"));
                }else {
                    chatBoxController.textOutput(new TextField("Invalid entry! You can only attack adjacent countries!"));
                }
                chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " enter country you wish to attack from or \"skip\"!"));
                chatBoxController.setWaitingTextInput(true);
            }

        } else if (waitingPlayer1SizeOfAttackingArmy)
        {
            try
            {
                int maxAttackSize = armies[attackingCountryIndex].getArmySize() - 1;
                int input = Integer.parseInt(t.getText());
                if (input >= 3)
                {
                    sizeOfAttackingArmy = Math.min(3, maxAttackSize);
                    waitingPlayer1SizeOfAttackingArmy = false;
                } else if (input > 0)
                {
                    sizeOfAttackingArmy = Math.min(input, maxAttackSize);
                    waitingPlayer1SizeOfAttackingArmy = false;
                } else
                {
                    chatBoxController.textOutput(new TextField("Invalid! Please enter a valid input"));
                    chatBoxController.setWaitingTextInput(true);
                }
            } catch (NumberFormatException e)
            {
                chatBoxController.textOutput(new TextField("Invalid! Please enter a valid input"));
                chatBoxController.setWaitingTextInput(true);
            }

            if (!waitingPlayer1SizeOfAttackingArmy)
            {
                waitingPlayer1Attack = true;
                chatBoxController.textOutput(new TextField("Enter country you wish to attack:"));
                chatBoxController.setWaitingTextInput(true);
            }
        } else if (waitingPlayer2SizeOfAttackingArmy)
        {
            try
            {
                int maxAttackSize = armies[attackingCountryIndex].getArmySize() - 1;
                int input = Integer.parseInt(t.getText());
                if (input >= 3)
                {
                    sizeOfAttackingArmy = Math.min(3, maxAttackSize);
                    waitingPlayer2SizeOfAttackingArmy = false;
                } else if (input > 0)
                {
                    sizeOfAttackingArmy = Math.min(input, maxAttackSize);
                    waitingPlayer2SizeOfAttackingArmy = false;
                } else
                {
                    chatBoxController.textOutput(new TextField("Invalid! Please enter a valid input"));
                    chatBoxController.setWaitingTextInput(true);
                }
            } catch (NumberFormatException e)
            {
                chatBoxController.textOutput(new TextField("Invalid! Please enter a valid input"));
                chatBoxController.setWaitingTextInput(true);
            }

            if (!waitingPlayer2SizeOfAttackingArmy)
            {
                waitingPlayer2Attack = true;
                chatBoxController.textOutput(new TextField("Enter country you wish to attack:"));
                chatBoxController.setWaitingTextInput(true);
            }
        } else if(waitingPlayer1Reinforce)
        {
            try
            {
                int numTroops = Integer.parseInt(t.getText());
                if(armies[attackingCountryIndex].getArmySize()>numTroops && numTroops>0) {
                    armies[attackingCountryIndex].incrementSize(-numTroops);
                    armies[defendingCountryIndex].incrementSize(numTroops);
                    waitingPlayer1Reinforce = false;
                    waitingPlayer1Option = true;
                    chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " it is your turn, enter a country to attack from or \"skip\"."));
                    chatBoxController.setWaitingTextInput(true);
                    mainController.updateNode(armies[attackingCountryIndex]);
                    mainController.updateNode(armies[defendingCountryIndex]);
                }else{
                    if(numTroops<1){
                        chatBoxController.textOutput(new TextField("You must move at least one troop"));
                    }else {
                        chatBoxController.textOutput(new TextField("You cannot move that many troops!"));
                    }
                    chatBoxController.setWaitingTextInput(true);
                }
            }catch (NumberFormatException e)
            {
                chatBoxController.textOutput(new TextField("Invalid! Please enter a valid input"));
                chatBoxController.setWaitingTextInput(true);
            }

        } else if(waitingPlayer2Reinforce){
            try
            {
                int numTroops = Integer.parseInt(t.getText());
                if(armies[attackingCountryIndex].getArmySize()>numTroops && numTroops>0) {
                    armies[attackingCountryIndex].incrementSize(-numTroops);
                    armies[defendingCountryIndex].incrementSize(numTroops);
                    waitingPlayer2Reinforce = false;
                    waitingPlayer2Option = true;
                    chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " it is your turn, enter a country to attack from or \"skip\"."));
                    chatBoxController.setWaitingTextInput(true);
                    mainController.updateNode(armies[attackingCountryIndex]);
                    mainController.updateNode(armies[defendingCountryIndex]);
                }else{
                    if(numTroops<1){
                        chatBoxController.textOutput(new TextField("You must move at least one troop!"));
                    }else {
                        chatBoxController.textOutput(new TextField("You cannot move that many troops!"));
                    }
                    chatBoxController.setWaitingTextInput(true);
                }
            }catch (NumberFormatException e)
            {
                chatBoxController.textOutput(new TextField("Invalid! Please enter a valid input"));
                chatBoxController.setWaitingTextInput(true);
            }

        }
        else if(waitingPlayer1Fortify)
        {
            int countryIndex = Country.getIndexFromCountryName(t.getText());
            if (t.getText().equals("skip"))
            {
                waitingPlayer1Fortify = false;
                cardExchangePlayer1();
            } else if (countryIndex != -1)
            {
                Army army = armies[countryIndex];
                if (army.getPlayer() == players[0] && army.getArmySize()>1)
                {
                    waitingPlayer1Fortify = false;
                    attackingCountryIndex = Country.getIndexFromCountryName(t.getText());
                    chatBoxController.textOutput(new TextField("Enter the destination of the troops:"));
                    waitingPlayer1FortifyDestination = true;
                    chatBoxController.setWaitingTextInput(true);
                }else
                {
                    if (army.getPlayer() == players[0] && army.getArmySize() == 1){
                        chatBoxController.textOutput(new TextField("Invalid selection, can not select this country!"));
                    }else {
                        chatBoxController.textOutput(new TextField("Invalid selection, choose a country you own!"));
                    }
                    chatBoxController.setWaitingTextInput(true);
                }
            }else
            {
                chatBoxController.textOutput(new TextField("Invalid input! choose a country"));
                chatBoxController.setWaitingTextInput(true);
            }
        }
        else if(waitingPlayer1FortifyDestination)
        {
            defendingCountryIndex = Country.getIndexFromCountryName(t.getText());
            if (defendingCountryIndex != -1)
            {
                if(isConnected(armies[attackingCountryIndex],armies[defendingCountryIndex]))
                {
                    waitingPlayer1FortifyDestination = false;
                    waitingPlayer1FortifyAmount = true;
                    chatBoxController.textOutput(new TextField("Enter how many troops to move:"));
                    chatBoxController.setWaitingTextInput(true);
                }else
                {
                    chatBoxController.textOutput(new TextField("Country is not connected!"));
                    chatBoxController.textOutput(new TextField("Enter country to move troops from:"));
                    waitingPlayer1FortifyDestination = false;
                    waitingPlayer1Fortify = true;
                    chatBoxController.setWaitingTextInput(true);
                }

            }else
            {
                chatBoxController.textOutput(new TextField("Invalid input! choose a country"));
                chatBoxController.setWaitingTextInput(true);
            }
        }
        else if(waitingPlayer1FortifyAmount)
        {
            try
            {
                int maxTroops = armies[attackingCountryIndex].getArmySize() - 1;
                int input = Integer.parseInt(t.getText());
                if (input > 0 && input <= maxTroops)
                {
                    waitingPlayer1FortifyAmount = false;
                    armies[defendingCountryIndex].incrementSize(input);
                    armies[attackingCountryIndex].incrementSize(-input);
                    mainController.updateNode(armies[attackingCountryIndex]);
                    mainController.updateNode(armies[defendingCountryIndex]);
                    cardExchangePlayer1();
                }  else
                {
                    chatBoxController.textOutput(new TextField("Invalid! Please enter a valid input"));
                    chatBoxController.setWaitingTextInput(true);
                }
            } catch (NumberFormatException e)
            {
                chatBoxController.textOutput(new TextField("Invalid! Please enter a valid input"));
                chatBoxController.setWaitingTextInput(true);
            }
        }
        else if(waitingPlayer2Fortify)
        {
            int countryIndex = Country.getIndexFromCountryName(t.getText());
            if (t.getText().equals("skip"))
            {
                waitingPlayer2Fortify = false;
                cardExchangePlayer2();
            } else if (countryIndex != -1)
            {
                Army army = armies[countryIndex];
                if (army.getPlayer() == players[1] && army.getArmySize()>1)
                {
                    waitingPlayer2Fortify = false;
                    attackingCountryIndex = Country.getIndexFromCountryName(t.getText());
                    chatBoxController.textOutput(new TextField("Enter the destination of the troops:"));
                    waitingPlayer2FortifyDestination = true;
                    chatBoxController.setWaitingTextInput(true);
                }else
                {
                    if (army.getPlayer() == players[1] && army.getArmySize() == 1){
                        chatBoxController.textOutput(new TextField("Invalid selection, can not select with this country!"));
                    }else {
                        chatBoxController.textOutput(new TextField("Invalid selection, choose a country you own!"));
                    }
                    chatBoxController.setWaitingTextInput(true);
                }
            }else
            {
                chatBoxController.textOutput(new TextField("Invalid input! choose a country"));
                chatBoxController.setWaitingTextInput(true);
            }
        }
        else if(waitingPlayer2FortifyDestination)
        {
            defendingCountryIndex = Country.getIndexFromCountryName(t.getText());
            if (defendingCountryIndex != -1)
            {
                if(isConnected(armies[attackingCountryIndex],armies[defendingCountryIndex]))
                {
                    waitingPlayer2FortifyDestination = false;
                    waitingPlayer2FortifyAmount = true;
                    chatBoxController.textOutput(new TextField("Enter how many troops to move:"));
                    chatBoxController.setWaitingTextInput(true);
                }else
                {
                    chatBoxController.textOutput(new TextField("Country is not connected!"));
                    chatBoxController.textOutput(new TextField("Enter country to move troops from:"));
                    waitingPlayer2FortifyDestination = false;
                    waitingPlayer2Fortify = true;
                    chatBoxController.setWaitingTextInput(true);
                }

            }else
            {
                chatBoxController.textOutput(new TextField("Invalid input! choose a country"));
                chatBoxController.setWaitingTextInput(true);
            }
        }
        else if(waitingPlayer2FortifyAmount)
        {
            try
            {
                int maxTroops = armies[attackingCountryIndex].getArmySize() - 1;
                int input = Integer.parseInt(t.getText());
                if (input > 0 && input <= maxTroops)
                {
                    armies[defendingCountryIndex].incrementSize(input);
                    armies[attackingCountryIndex].incrementSize(-input);
                    mainController.updateNode(armies[attackingCountryIndex]);
                    mainController.updateNode(armies[defendingCountryIndex]);
                    waitingPlayer2FortifyAmount = false;
                    cardExchangePlayer2();
                }  else
                {
                    chatBoxController.textOutput(new TextField("Invalid! Please enter a valid input"));
                    chatBoxController.setWaitingTextInput(true);
                }
            } catch (NumberFormatException e)
            {
                chatBoxController.textOutput(new TextField("Invalid! Please enter a valid input"));
                chatBoxController.setWaitingTextInput(true);
            }
        }else if(waitingPlayer1CardOption){
            if(t.getText().equalsIgnoreCase("skip") && !(players[0].getHandObject().mustTurnIn())){
                waitingPlayer2Option = true;
                waitingPlayer1CardOption = false;
                chatBoxController.setWaitingTextInput(true);
                chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " it is your turn!"));
                chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " enter country you wish to attack from or \"skip\"!"));
            }else if(players[0].getHandObject().validExchangeCommand(t.getText())){
                if(players[0].getHandObject().exchangeCards(t.getText()) == true) {
                    int newTroops = getExchangeTroops();
                    players[0].addTroops(newTroops);
                    chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " you have received " + newTroops + " extra troops!"));
                    chatBoxController.textOutput(new TextField(" "));
                    waitingPlayer2Option = true;
                    waitingPlayer1CardOption = false;
                    chatBoxController.setWaitingTextInput(true);
                    chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " it is your turn!"));
                    chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " enter country you wish to attack from or \"skip\"!"));
                }else{
                    chatBoxController.textOutput(new TextField("You don't own those cards! Enter a set that you do own or \"skip\"."));
                    chatBoxController.setWaitingTextInput(true);
                }
            }else{
                if(players[0].getHandObject().mustTurnIn())
                    chatBoxController.textOutput(new TextField("You must turn in a set of cards!"));
                else {
                    chatBoxController.textOutput(new TextField("Invalid command!"));
                    chatBoxController.textOutput(new TextField("Invalid input, either \"skip\" or choose an exchange of types you can do"));
                }
                chatBoxController.setWaitingTextInput(true);
            }
        }else if(waitingPlayer2CardOption){
            if(t.getText().equalsIgnoreCase("skip") && !players[1].getHandObject().mustTurnIn()){
                waitingPlayer2CardOption = false;
                chatBoxController.setWaitingTextInput(true);
                GameTurns(4);
            }else if(players[1].getHandObject().validExchangeCommand(t.getText())){
                if(players[1].getHandObject().exchangeCards(t.getText()) == true) {
                    int newTroops = getExchangeTroops();
                    players[1].addTroops(newTroops);
                    chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " you have received " + newTroops + " extra troops!"));
                    chatBoxController.textOutput(new TextField(" "));
                    waitingPlayer2CardOption = false;
                    chatBoxController.setWaitingTextInput(true);
                    GameTurns(4);
                }else{
                    chatBoxController.textOutput(new TextField("You don't own those cards! Enter a set that you do own or \"skip\"."));
                    chatBoxController.setWaitingTextInput(true);
                }
            }else{
                if(players[1].getHandObject().mustTurnIn())
                    chatBoxController.textOutput(new TextField("You must turn in a set of cards!"));
                else {
                    chatBoxController.textOutput(new TextField("Invalid command!"));
                    chatBoxController.textOutput(new TextField("Invalid input, either \"skip\" or choose an exchange of types you can do"));
                }
                chatBoxController.setWaitingTextInput(true);
            }
        }
    }



    //logic implementation for the logic initialisation of claiming countries, the rest of the implementation
    //is handled in the main controller
    public void startClaimPhase()
    {
        mainController.setPlayerClaim(true);
        chatBoxController.textOutput(new TextField("Claim your countries!"));
        chatBoxController.textOutput(new TextField(players[0].getNameWithColour()+" claim a country!"));
    }

    public Player[] getNeutrals(){
        return neutrals;
    }

    //method that will call all the phases of the GameState
    public void GameTurns(int phase)
    {
        if (phase == 1)
        {
            rollForTurnOrder();
        } else if (phase == 2)
        {
            deploymentPhase(1);
        } else if (phase == 3)
        {
            chatBoxController.textOutput(new TextField("Type \"skip\" if you have no more attacks to make"));
            chatBoxController.textOutput(new TextField("To attack enter the name of the country you wish to attack from"));
            chatBoxController.textOutput(new TextField(" "));
            chatBoxController.textOutput(new TextField(">>> Combat Phase <<<"));
            chatBoxController.textOutput(new TextField(" "));
            chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " it is your turn!"));
            chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " enter country you wish to attack from or \"skip\"!"));
            chatBoxController.setWaitingTextInput(true);
            waitingPlayer1Option = true;
        }else if (phase == 4){
            players[0].setTroops(players[0].getTroops() + players[0].getExtraTroops());
            chatBoxController.textOutput(new TextField(players[0].getNameWithColour()+", you get "+players[0].getExtraTroops()+" extra troops!"));
            players[1].setTroops(players[1].getTroops() + players[1].getExtraTroops());
            chatBoxController.textOutput(new TextField(players[1].getNameWithColour()+", you get "+players[1].getExtraTroops()+" extra troops!"));
            for(int i=0;i<4;i++){
                if(!neutrals[i].isEliminated())
                {
                    chatBoxController.textOutput(new TextField("Neutral player "+(i+1)+" got "+neutrals[i].getExtraTroops()+" extra troops!"));
                    neutrals[i].setTroops(neutrals[i].getExtraTroops());
                }
            }
            chatBoxController.textOutput(new TextField(" "));
            chatBoxController.textOutput(new TextField(">>>Deployment Phase<<<"));
            chatBoxController.textOutput(new TextField(" "));
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
            chatBoxController.textOutput(new TextField(players[0].getNameWithColour() + " deploy troops!"));
        }else if(neutrals[0].getTroops()>0){//start neutral deployment logic
                Thread neutralDeploy = new Thread(() -> {
                    for(int i=0;i<4;i++){//loops through the 4 neutrals does all their deployment logic
                        if(!neutrals[i].isEliminated())
                        {
                            Random generator = new Random();
                            Object[] values = neutrals[i].getAssignedCountries().values().toArray();
                            Country randomCountry = (Country) values[generator.nextInt(values.length)];
                            Army army = armies[randomCountry.getIndex()];
                            neutrals[i].decrementTroops(1);
                            army.incrementSize(1);
                            mainController.updateNode(army);
                            Platform.runLater(() -> chatBoxController.textOutput(new TextField("Neutral deployed a troop to "+randomCountry.getName()+"!")));
                            try {
                                Thread.sleep(300); //this prevents all the countries been claimed instantly
                                //and provides somewhat of an animation
                            } catch(InterruptedException v){System.out.println(v);}
                        }
                    }
                    Platform.runLater(() -> deploymentPhase(1)); //loops back to the player deployment phase

                });
                neutralDeploy.start();
        }else if(players[0].getTroops()>0){
            deploymentPhase(1);//loops back to player deployment phase if players still have troops to deploy
        }else if(players[0].getTroops()==0 && players[1].getTroops()>0) {
            waitingPlayer2Deployment = true;
            chatBoxController.setWaitingTextInput(true);
            chatBoxController.textOutput(new TextField("Input the name of the country you want to deploy troops on!"));
            chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " deploy troops!"));
        }else
        {
            //end of deployment phase
            chatBoxController.textOutput(new TextField("Deployment phase over!"));
            //initiates next piece of game logic
            GameTurns(3);
        }
    }

    //retrieves the instantiated armies array from mainController
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
        chatBoxController.textOutput(new TextField(players[0].getNameWithColour() +" roll your dice!"));
        waitingPlayer1Roll = true;
        chatBoxController.setWaitingTextInput(true);
    }

    void drawCard(int playerIndex){
        if(playerIndex==0)
            territoryConqueredPlayer1 = false;
        else
            territoryConqueredPlayer2 = false;
        Card drawCard = deck.draw();
        players[playerIndex].addCard(drawCard);
        chatBoxController.textOutput(new TextField(players[playerIndex].getName() + " drew the " + drawCard.toString() + " card"));
    }

    public boolean determineRollWinner(Army attackingCountry, Army defendingCountry, int numOfAttackers, int numOfDefenders)
    {
        boolean won = false;

        Dice attacker = new Dice();
        Dice defender = new Dice();

        attacker.rollXDice(numOfAttackers);
        defender.rollXDice(numOfDefenders);

        ArrayList<Integer> attackerRolls = attacker.getRolls();
        ArrayList<Integer> defenderRolls = defender.getRolls();

        int attackingLosses = 0;
        int defendingLosses = 0;

        System.out.println("attacking"+ attackingCountry.getArmySize());
        for(int num: attackerRolls)
        {
            System.out.println(num);
        }
        System.out.println("defending"+ defendingCountry.getArmySize());
        for(int num: defenderRolls)
        {
            System.out.println(num);
        }
        chatBoxController.textOutput(new TextField("Attacker rolls: " + attackerRolls.toString()));
        chatBoxController.textOutput(new TextField("Defender rolls: " + defenderRolls.toString()));
        if(numOfAttackers > numOfDefenders)
        {
            for(int i=0;i<numOfDefenders;i++)
            {
                if(attackerRolls.get(i) <= defenderRolls.get(i))
                {
                    attackingCountry.incrementSize(-1);
                    attackingLosses++;
                }else
                {
                    //defender losses piece
                    defendingCountry.incrementSize(-1);
                    defendingLosses++;
                }
            }
        }else
        {
            for(int i=0;i<numOfAttackers;i++)
            {
                if(attackerRolls.get(i) <= defenderRolls.get(i))
                {
                    //attacker loses piece
                    attackingCountry.incrementSize(-1);
                    attackingLosses++;
                }else
                {
                    //defender losses piece
                    defendingCountry.incrementSize(-1);
                    defendingLosses++;
                }
            }
        }
        if(defendingCountry.getArmySize()==0){
            won = true;
            mainController.conquer(attackingCountry, defendingCountry);
        }
        System.out.println("attacking"+ attackingCountry.getArmySize());
        System.out.println("defending"+ defendingCountry.getArmySize());

        mainController.updateNode(attackingCountry);
        mainController.updateNode(defendingCountry);

        if(attackingLosses < defendingLosses)
        {
            chatBoxController.textOutput(new TextField(attackingCountry.getPlayer().getNameWithColour() + " your army in "+attackingCountry.getCountry().getName()+" won the combat!"));
        }else if(attackingLosses > defendingLosses)
        {
            chatBoxController.textOutput(new TextField(attackingCountry.getPlayer().getNameWithColour() + " your army in "+attackingCountry.getCountry().getName()+" lost the combat!"));
        }else
        {
            chatBoxController.textOutput(new TextField(attackingCountry.getPlayer().getNameWithColour() + " your army in "+attackingCountry.getCountry().getName()+" tied the combat!"));
        }

        chatBoxController.textOutput(new TextField("Attacking army lost: " + attackingLosses));
        chatBoxController.textOutput(new TextField("Defending army lost: " + defendingLosses));
        return won;
    }
    
    public void endGame(Player player)
    {
        chatBoxController.textOutput(new TextField(player.getNameWithColour() +" wins!!!"));
        chatBoxController.textOutput(new TextField("Game Over"));
        return;
    }

    boolean isConnected(Army origin, Army destination){
        Queue<Army> queue = new LinkedList<Army>();
        ArrayList<Army> visitedCountries = new ArrayList<Army>();
        queue.add(origin);
        while(!queue.isEmpty()){
            Army visiting = queue.remove();
            if(visiting == destination){
                return true;
            }
            addNeighbours(visiting, queue, visitedCountries);
        }
        return false;
    }

    void addNeighbours(Army visited, Queue<Army> queue, ArrayList<Army> visitedCountries){
        int[] adjacentIndices = visited.getCountry().getAdjacentIndices();
        visitedCountries.add(visited);
        for(Integer i:adjacentIndices){
            if(armies[i].getPlayer() == visited.getPlayer() && !isVisited(visitedCountries, armies[i])) {
                queue.add(armies[i]);
            }
        }
    }

    boolean isVisited(ArrayList<Army> visitedCountries, Army visiting){
        if(visitedCountries.contains(visiting)){
            return true;
        }else{
            return false;
        }
    }

    void printHand(Player player){
        chatBoxController.textOutput(new TextField(player.getNameWithColour()+"'s Hand:"));
        for(Card card:player.getHand()){
            chatBoxController.textOutput(new TextField(card.toString()));
        }
    }

    void cardExchangePlayer1(){
        chatBoxController.textOutput(new TextField(" "));
        chatBoxController.textOutput(new TextField(">>>Card Exchange Phase<<<"));
        if(territoryConqueredPlayer1){
            drawCard(0);
        }
        printHand(players[0]);
        if(players[0].getHandObject().canExchange()){
            chatBoxController.textOutput(new TextField("Enter the types of cards you would like to exchange (ex. \"iii\", \"ica\" etc.) or \"skip\"!"));
            waitingPlayer1CardOption = true;
        }else {
            chatBoxController.textOutput(new TextField("Unfortunately you have no valid exchanges currently."));
            chatBoxController.textOutput(new TextField(" "));
            waitingPlayer2Option = true;
            waitingPlayer1Fortify = false;
            chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " it is your turn!"));
            chatBoxController.textOutput(new TextField(players[1].getNameWithColour() + " enter country you wish to attack from or \"skip\"!"));
        }
        chatBoxController.setWaitingTextInput(true);
    }

    void cardExchangePlayer2(){
        chatBoxController.textOutput(new TextField(" "));
        chatBoxController.textOutput(new TextField(">>>Card Exchange Phase<<<"));
        if(territoryConqueredPlayer2){
            drawCard(1);
        }
        printHand(players[1]);
        if(players[1].getHandObject().canExchange()){
            chatBoxController.textOutput(new TextField("Enter the types of cards you would like to exchange (ex. \"iii\", \"ica\" etc.) or \"skip\"!"));
            waitingPlayer2CardOption = true;
            chatBoxController.setWaitingTextInput(true);
        }else {
            chatBoxController.textOutput(new TextField("Unfortunately you have no valid exchanges currently."));
            chatBoxController.textOutput(new TextField(" "));
            waitingPlayer2Fortify = false;
            chatBoxController.setWaitingTextInput(true);
            GameTurns(4);
        }
    }

    boolean allAdjacentAllies(int index,Player player)
    {
        boolean surrounded = true;
        Country country = mainController.getCountries().getCountries().get(index);
        int[] adjacent = country.getAdjacentIndices();
        for(int indexOfAdjacent: adjacent)
        {
           if(!player.getAssignedCountries().containsKey(indexOfAdjacent))
           {
               surrounded = false;
           }
        }
        return surrounded;
    }

    int getExchangeTroops(){
        int ret = goldenHorse;
        if (goldenHorse>=10)
            goldenHorse+=5;
        else
            goldenHorse+=2;
        if (goldenHorse>60)
            goldenHorse = 60;
        mainController.goldGrid(goldenGrid);
        goldenGrid++;
        return ret;
        }
        
}
