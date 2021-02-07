package Controllers;


import GameLogic.Gamestate;
import Map.*;
import Player.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;
import Map.Reader;
import javafx.util.Duration;

public class MainController
{

    public Pane keyPane;
    @FXML
    AnchorPane anchor;
    @FXML
    Pane nodeList;

    @FXML
    Pane names;
    public ArrayList<Text> nodeValues = new ArrayList<>();

    //declaration of grid
    static Rectangle[][] grid = new Rectangle[120][200];

    //initialises a Countries object
    Countries countries = new Countries();

    Gamestate gamestate;
    Player[] players;
    private int countriesClaimed = 0;

    public boolean playerClaim = false;
    public boolean neutralsClaim = true;
    private boolean animationActive = false;

    breadFirstSearch bfs = new breadFirstSearch(); //object used for animation //object used for animation

    private int randomIndex;

    private Color[] neutralColours = {Color.YELLOW,Color.ORANGE,Color.YELLOWGREEN,Color.VIOLET};

    @FXML private ChatBoxController chatBoxController;//reference to the chat box controller

    @FXML private void initialize()//allows the program to reference both controls without creating new instances of them
    {
        chatBoxController.injectMainController(this);
        bfs.injectMainController(this);
        keyPane.setStyle("-fx-background-color: #ffffff");
        this.gamestate = new Gamestate();
        chatBoxController.setGameState(gamestate);
        players = gamestate.getPlayers();

        newGrid();
        nodeList.setVisible(true);
        initializeNodes();
        names.toFront();
        nodeList.toFront();

        gamestate.setController(chatBoxController, this);
        gamestate.Gamestart();

    }

    public Boolean getAnimationActive()
    {
        return animationActive;
    }
    public void setAnimationActive(Boolean state)
    {
        animationActive = state;
    }

    public void setPlayerClaim(Boolean state)
    {
        playerClaim = state;
    }
    public Boolean getPlayerClaim()
    {
        return playerClaim;
    }


    public void newGrid()
    {
        for (int y = 0; y < 120; y++){
            for (int x = 0; x < 200; x++){
                Rectangle rect = new Rectangle(x * 5, (5 * y), 5, 5);
                rect.setFill(Color.CYAN);
                rect.setStroke(Color.CYAN);
                anchor.getChildren().add(rect);
                grid[y][x] = rect;
            }
        }
        int index = 0;//index to notify when the border coordinates are reached
        int numberOfCountries = 41;

        for (Country country : countries.getCountries())
        {
            if (index > numberOfCountries)//when index is greater than number of countries setFill is changed to black for the borders
            {
                for (Coordinate c : country)
                {
                    grid[c.getY()][c.getX()].setFill(Color.BLACK);
                    grid[c.getY()][c.getX()].setStroke(Color.BLACK);
                }
            } else
            {
                for (Coordinate c : country)
                {
                    grid[c.getY()][c.getX()].setFill(Color.GRAY);
                    grid[c.getY()][c.getX()].setStroke(Color.GRAY);
                }
            }
            index++;
        }


        //incrementNodeValue((0));//increments the value on the Indonesia node by 1

    }

    public void claimCountry(int countryIndex)
    {

        for(int i =0; i< nodeValues.size();i++)
            if (nodeValues.get(i) instanceof Text) {
                // clear
                    //this will be moved later, initialises a nodes value
                    //casts the node twice so its a Text object and sets the value
                if(i == countryIndex)
                {
                    ((Text) nodeValues.get(i)).setText("1");
                }
            }
    }


    //Determining the country a player clicks on
    public void determineClick(int y, int x){

        if(playerClaim)
        {
            int playerIndex = countriesClaimed%2;

            Coordinate clicked = new Coordinate(y, x);  //intialises a coordinate object at the y and x in
                                                        //the context of the grid
            ArrayList<Country> queue = new ArrayList<Country>(); //create queue of Country objects

            //if statement to prevent unnecessary searches in sea and on borders
            if(grid[y][x].getFill()!=Color.CYAN && grid[y][x].getFill()!=Color.BLACK){

                //loops through the countries array and perform insertion sort on the country objects
                //into a queue based on the distance of those countries middle node to the point that
                //was clicked
                for(int i=0;i<42;i++){
                    insert(countries.getCountries().get(i), queue, y , x);
                }
                //loops through the queue and determines if the country in the index
                //of the queue contains the clicked square
                String countryName="";
                for(int i=0;i<42;i++){
                    for(Coordinate c:queue.get(i)){
                        if(c.equals(clicked)){
                            countryName = queue.get(i).getName();
                            //chatBoxController.textOutput(new TextField(countryName));
                            break;
                        }
                    }
                }

                int countryIndex = getCountryIndex(countries.getCountries(), countryName);
                bfs.startBFS(clicked, grid, players[playerIndex].getColors(), countries.getCountries(), countryIndex);
                claimCountry(countryIndex);
                players[playerIndex].addCountry(countries.getCountries().get(countryIndex));
                countriesClaimed++;

                if(countriesClaimed != 18 )//temp value, this tracks when to stop allowing players to claim territory
                {
                    if(playerIndex == 0){

                            chatBoxController.textOutput(new TextField(players[playerIndex+1].getName()+" claim a country!"));

                    }else{

                        if(neutralsClaim)
                        {
                            if(countriesClaimed <= 12)
                            {
                                for(int i=0;i<4;i++)
                                {
                                    Coordinate neutralCountry = neutralChooseCountry();
                                    determineNeutral(neutralCountry.getY(),neutralCountry.getX(),neutralColours[i]);
                                }
                                /*
                                Timeline timeline = new Timeline();
                                Duration timePoint = Duration.ZERO;
                                Duration pause = Duration.seconds(3);
                                KeyFrame kF;
                                for(int i=0;i<4;i++)
                                {
                                    Coordinate neutralCountry = neutralChooseCountry();
                                    Color neutralColor = neutralColours[i];
                                    timePoint.add(pause);
                                    kF = new KeyFrame(timePoint,e -> determineNeutral(neutralCountry.getY(),neutralCountry.getX(),neutralColor));
                                    timeline.getKeyFrames().add(kF);
                                }
                                timeline.play();

                                 */

                            }

                            chatBoxController.textOutput(new TextField("Neutral claimed a country"));

                        }

                        chatBoxController.textOutput(new TextField(players[playerIndex-1].getName()+" claim a country"));
                    }

                }else{
                    playerClaim = false;
                    chatBoxController.textOutput(new TextField("Claiming phase over!"));
                }

            }
        }




    }

    public void initializeNodes()
    {
        //loops through all children of the nodeList which are StackPanes that contain the nodes
       for(Node node: nodeList.getChildren())
            if (node instanceof StackPane) {
                // clear
                if(((StackPane)node).getChildren().get(2) instanceof Text)//the object at the second index is a Text object
                {
                    //this will be moved later, initialises a nodes value
                    //casts the node twice so its a Text object and sets the value
                    ((Text) ((StackPane)node).getChildren().get(2)).setText("1");
                    //fills an ArrayList with all the Text objects that contrain the values for our nodes
                    nodeValues.add(((Text) ((StackPane)node).getChildren().get(2)));
                }
            }
        for(Text armySize:nodeValues)
        {
            armySize.setText("0");
        }
    }

    public void incrementNodeValue(int index)
    {
        //stores the integer value of the string on our node
        int value = Integer.valueOf(nodeValues.get(index).getText());

        //testing purpose to make sure change is happening, outputs to the chat box
        chatBoxController.textOutput(new TextField("Old value: " + value));


        //increments the value
        value++;

        //sets the text to the string value of our integer
        nodeValues.get(index).setText((String.valueOf(value)));

        //outputs new value to textbox
        chatBoxController.textOutput(new TextField("New value: " + value));
    }

    public void insert(Country country, ArrayList<Country> queue, int y, int x){
        Coordinate clicked = new Coordinate(y, x);
        if(queue.size()==0){
            queue.add(country);
            return;
        }
        for(int i=0;i<queue.size();i++){
            if(country.getCoordinates().get(0).getDistance(clicked)<queue.get(i).getCoordinates().get(0).getDistance(clicked)){
                queue.add(i, country);
                return;
            }
        }
        queue.add(country);
    }

    //method called when scene is clicked after map is initialised
    public void anchorOnClick(MouseEvent mouseEvent)
    {
        //determines the x and y in relation to the context of the
        //grid
        int y = (int) mouseEvent.getY()/5;
        int x = (int) mouseEvent.getX()/5;
        if(!(x>199)) {
            determineClick(y, x);
        }
    }

    public int getCountryIndex(ArrayList<Country> countries, String name){
        for(int i=0;i<42;i++){
            if(countries.get(i).getName()==name){
                return i;
            }
        }
        return -1;
    }
    public int getCountriesClaimed()
    {
        return getCountriesClaimed();
    }

    public void neutralClaimCountry(int index)
    {

        if(nodeValues.get(index) instanceof Text)
        {
            (nodeValues.get(index)).setText("1");
        }
    }
    public Coordinate neutralChooseCountry()
    {
        Random rand = new Random();
        int upperBound = 42;
        randomIndex = rand.nextInt(upperBound);

        if(nodeValues.get(randomIndex) instanceof Text)
        {
            while((nodeValues).get(randomIndex).getText() != "0")
            {
                randomIndex = rand.nextInt(upperBound);
            }
        }
        neutralClaimCountry(randomIndex);
        return countries.getCountries().get(randomIndex).getCoordinates().get(0);
    }

    public void determineNeutral(int y, int x,Color color){

            Coordinate clicked = new Coordinate(y, x);  //intialises a coordinate object at the y and x in
            int countryIndex = getCountryIndex(countries.getCountries(), Reader.getCountryName(randomIndex) );
            bfs.startBFS(clicked, grid, color, countries.getCountries(), countryIndex);

    }

    public void neutralTimelineRecursion(Coordinate coordinate, int n)
    {
        Timeline timeline = new Timeline();
        Duration tp = Duration.ZERO;
        KeyFrame KF = new KeyFrame(tp,e -> determineNeutral(coordinate.getY(), coordinate.getX(), neutralColours[n]));
        timeline.getKeyFrames().add(KF);
        timeline.play();

        if(n < 3)
        {
            timeline.setOnFinished(e -> neutralTimelineRecursion(neutralChooseCountry(),n + 1));
        }
    }

}
