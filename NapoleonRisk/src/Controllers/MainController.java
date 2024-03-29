package Controllers;


import GameLogic.GameState;
import Map.*;
import Player.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
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


public class MainController
{

    @FXML
    AnchorPane anchor;
    @FXML
    Pane nodeList;//pane which contains all the stack panes of nodes
    @FXML
    Pane names;//pane which contains all the names of countries

    //ArrayList of values at the nodes
    public ArrayList<Text> nodeValues = new ArrayList<>();

    //declaration of grid
    static Rectangle[][] grid = new Rectangle[120][200];

    //initialises a Countries object
    Countries countries = new Countries();

    GameState gamestate;//declaring GameState object
    Player[] players;//declaring players array
    Army[] armies = new Army[42];

    private int countriesClaimed = 0;//counter used during the ClaimPhase

    public boolean playerClaim = false;//set to true in GameState when it's time for players to claim initial countries
    public boolean neutralsClaim = true;//sets to false when neutrals have been allocated countries

    BreathFirstSearch bfs = new BreathFirstSearch(); //object used for animation //object used for animation

    private int randomIndex;//used as the index for getting a random country to assign to neutral players

    Player[] neutrals = new Player[4];
     //stores colours of neutral players

    @FXML private ChatBoxController chatBoxController;//reference to the chat box controller

    @FXML private void initialize()//allows the program to reference both controls without creating new instances of them
    {
        chatBoxController.injectMainController(this);//passes MainController to ChatBoxController
        this.gamestate = new GameState(); //initialises GameState Object
        chatBoxController.setGameState(gamestate);//Outputs instructions to user and starts the game

        players = gamestate.getPlayers();//fetches the players from GameState

        newGrid();//creates a 200 x 120 grid of rectangle and colours them to displayed the map

        initializeNodes();//sets all node values to 0

        //pulls these panes to front
        names.toFront();
        nodeList.toFront();


        //passes both controllers to GameState , creating new instances of these will not work!
        gamestate.setController(chatBoxController, this);
        gamestate.Gamestart();

    }

    //initialises all nodes to 0
    public void initializeNodes()
    {
        //loops through all children of the nodeList which are StackPanes that contain the nodes
        for(Node node: nodeList.getChildren())
            if (node instanceof StackPane) {
                if(((StackPane)node).getChildren().get(2) instanceof Text)//the object at the second index is a Text object
                {
                    //casts the node twice so its a Text object and sets the value
                    ((Text) ((StackPane)node).getChildren().get(2)).setText("0");
                    //fills an ArrayList with all the Text objects that contrain the values for our nodes
                    nodeValues.add(((Text) ((StackPane)node).getChildren().get(2)));
                }
            }

    }
    //function for GameState to set when its time to allow players to claim countries
    public void setPlayerClaim(Boolean state)
    {
        playerClaim = state;
    }

    public Boolean getPlayerClaim()
    {
        return playerClaim;
    }

    //claims a country for a player
    public void claimCountry(int countryIndex)
    {
        //a node's index is the same as the countries they are in
        //sets a country's node to 1
        for(int i =0; i< nodeValues.size();i++)
            if (nodeValues.get(i) instanceof Text) {
                //sets the value of a node at a given index to 1
                if(i == countryIndex)
                {
                    (nodeValues.get(i)).setText("1");
                }
            }
    }

    //prints grid of rectangles and fills in the countries
    public void newGrid()
    {
        for (int y = 0; y < 120; y++){
            for (int x = 0; x < 200; x++){
                Rectangle rect = new Rectangle(x * 5, (5 * y), 5, 5);
                if(y>=95 && y<=115 && x>=4 && x<=36){
                    int xCheck = x-4;
                    Color colour = Color.GRAY;
                    if(y%5==0 || xCheck%8==0){
                        colour = Color.BLACK;
                    }
                    if((y>=111 && x>=29) || (y<=99 && x<=11)){
                        colour = Color.CYAN;
                    }
                    rect.setFill(colour);
                    rect.setStroke(colour);
                }else {
                    rect.setFill(Color.CYAN);
                    rect.setStroke(Color.CYAN);
                }
                anchor.getChildren().add(rect);
                grid[y][x] = rect;
            }
        }
        goldGrid(0);
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

    //Determining the country a player clicks on
    public void determineClick(int y, int x){
            Coordinate clicked = new Coordinate(y, x);  //initialises a coordinate object at the y and x in
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
                            chatBoxController.setTextInput(new TextField(countryName));
                            break;
                        }
                    }
                }
                //int countryIndex = getCountryIndex(countries.getCountries(), countryName);//returns index of a country in countries by its name
            }
    }

    //game logic for distributing countries at the beginning of the game
    public void distributeCountries()
    {
        neutrals = gamestate.getNeutrals();
        Thread thread = new Thread(() -> {
            while (countriesClaimed != 18)
            {
                int playerIndex = countriesClaimed%2;
                if(playerIndex == 0)
                {
                    Platform.runLater(() -> setCountryColour(players[playerIndex]));
                    try {
                        Thread.sleep(500);
                    } catch(InterruptedException v){System.out.println(v);}
                }else
                {
                    Platform.runLater(() -> setCountryColour(players[playerIndex]));
                    try {
                        Thread.sleep(500);
                    } catch(InterruptedException v){System.out.println(v);}
                    if(countriesClaimed <= 12)
                    {
                        for(int i=0;i<4;i++)//claims a random country 4 times , using the colours from neutrals array
                        {
                            int index = i;
                            Platform.runLater(() -> setCountryColour(neutrals[index]));
                            try {
                                Thread.sleep(500);
                            } catch(InterruptedException v){System.out.println(v);}
                        }
                    }

                }//adds the country to player object
                countriesClaimed++;}
            //starts the next part of the game logic
            Platform.runLater(() -> gamestate.passArmies(armies));
            Platform.runLater(() -> gamestate.GameTurns(1));

        });
        thread.start();
    }

    //method to find a random country and assign it to the passed player
    public void setCountryColour(Player player)
    {
        Country neutralCountry = chooseRandomEmptyCountry();
        int countryIndex = getCountryIndex(countries.getCountries(), neutralCountry.getName());
        armies[countryIndex] = new Army(player, 1, neutralCountry);
        player.decrementTroops(1);
        setColourCountry(neutralCountry, player.getColour());
        player.addCountry(countries.getCountries().get(countryIndex));//adds the country to player object
    }

    //method that claims a county for a neutral player
    public void claimEmptyCountry(int index)
    {
        if(nodeValues.get(index) instanceof Text)
        {
            (nodeValues.get(index)).setText("1");
        }
    }

    //initialises a random number from 0 - 41
    //if the country at that index is empty, claim it. Otherwise repeat until a country that is empty is claimed
    public Country chooseRandomEmptyCountry()
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
        claimEmptyCountry(randomIndex);
        return countries.getCountries().get(randomIndex);
        //return countries.getCountries().get(randomIndex).getCoordinates().get(0);
    }

    public void updateNode(Army army)
    {
        int index = getCountryIndex(countries.getCountries(),army.getCountry().getName());
        if(nodeValues.get(index) instanceof Text)
        {
            (nodeValues.get(index)).setText(String.valueOf(army.getArmySize()));
        }
    }

    //insertion sorts countries into a queue based on there distance to beacon nodes
    //part of an algorithm to find what country is clicked on by the player
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
        System.out.println(y+"\t"+x);
        if(!(y>119)) {
            determineClick(y, x);
        }
    }

    //returns a country's index from its name
    public int getCountryIndex(ArrayList<Country> countries, String name){
        for(int i=0;i<42;i++){
            if(countries.get(i).getName()==name){
                return i;
            }
        }
        return -1;
    }

    //bfs search when a neutral player claims a country
    //has some lag issues at the moment
    public void determineNeutral(int y, int x,Color color){
            Coordinate clicked = new Coordinate(y, x);  //initialises a coordinate object at the y and x in
            int countryIndex = getCountryIndex(countries.getCountries(), Reader.getCountryName(randomIndex) );
            bfs.startBFS(clicked, grid, color, countries.getCountries(), countryIndex);
    }

    //sets a country's colour without using the bts
    public void setColourCountry(Country country, Color colour){
        for(Coordinate c:country){
            grid[c.getY()][c.getX()].setFill(colour);
            grid[c.getY()][c.getX()].setStroke(colour);
        }
    }

    public Countries getCountries()
    {
        return countries;
    }

    //function to determine if a player can attack
    public boolean canAttack(Player player){
        for(Army army:armies){
            if(army.getPlayer() == player){
                if(army.getArmySize()>1){
                    return true;
                }
            }
        }
        return false;
    }

    public void conquer(Army attacker, Army defender){
        bfs.startBFS(defender.getCountry().getCoordinates().get(0), grid, attacker.getPlayer().getColour(), countries.getCountries(), defender.getCountry().getIndex());
    }

    Coordinate[] goldGridCoordinates = {new Coordinate(102, 8),
            new Coordinate(107, 8),
            new Coordinate(112, 8),
            new Coordinate(98, 16),
            new Coordinate(102, 16),
            new Coordinate(107, 16),
            new Coordinate(112, 16),
            new Coordinate(98, 24),
            new Coordinate(102, 24),
            new Coordinate(107, 24),
            new Coordinate(112, 24),
            new Coordinate(98, 32),
            new Coordinate(102, 32),
            new Coordinate(107, 32)};

    public void goldGrid(int gridIndex){
        if(gridIndex>13){
            gridIndex = 13;
        }
        if(gridIndex<0){
            gridIndex = 0;
        }
        bfs.startBFS(goldGridCoordinates[gridIndex], grid, Color.GOLD, countries.getCountries(), 42);
    }
}
