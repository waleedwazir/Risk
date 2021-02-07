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


    ChatBoxController chatBoxController;
    MainController mainController;

    boolean waitingPlayerNames = false;


    public void setController(ChatBoxController chatBoxController, MainController mainController){
        this.chatBoxController = chatBoxController;
        this.mainController = mainController;
    }


    public void Gamestart(){

        chatBoxController.textOutput(new TextField("Welcome to Risk!"));
        setPlayerName(players, 0);
        players[0].setColors(Color.RED);
        players[1].setColors(Color.BLUE);
    }

    public void setPlayerName(Player[] players, int index){
        chatBoxController.textOutput(new TextField("Player "+(++index)+", enter your name:"));
        chatBoxController.setWaitingTextInput(true);
        if(players[0].getName()==null) {
            waitingPlayerNames = true;
        }
    }
    public Player[] getPlayers()
    {
        return players;
    }

    public void getTextInput(TextField t){
        chatBoxController.textOutput(new TextField("Player name: "+t.getText()));
        if(waitingPlayerNames){
            players[0].setName(t.getText());
            waitingPlayerNames = false;
            setPlayerName(players, 1);
        }else{
            players[1].setName(t.getText());
            startClaimPhase();
            chatBoxController.setWaitingTextInput(false);
        }
    }
    public void startClaimPhase()
    {
        mainController.setPlayerClaim(true);
        chatBoxController.textOutput(new TextField("Claim your countries!"));
        chatBoxController.textOutput(new TextField(players[0].getName()+" claim a country!"));
    }

}
