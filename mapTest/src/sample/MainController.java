package sample;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import java.util.ArrayList;

public class MainController
{

    public Pane pane;
    @FXML
    AnchorPane anchor;
    @FXML
    Button startButton;
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

    @FXML private ChatBoxController chatBoxController;//reference to the chat box controller

    @FXML private void initialize()//allows the program to reference both controls without creating new instances of them
    {
        chatBoxController.injectMainController(this);
        this.gamestate = new Gamestate();
        chatBoxController.setGameState(gamestate);
    }


    public void newGrid(ActionEvent actionEvent)
    {
        pane.getChildren().removeAll();
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
        setNodeValues();//initialises node ArrayList
        names.setVisible(true);
        nodeList.setVisible(true);
        names.setVisible(true);
        names.toFront();
        nodeList.toFront();
        gamestate.setController(chatBoxController, this);
        gamestate.Gamestart();
        //incrementNodeValue((0));//increments the value on the Indonesia node by 1

    }

    public void setNodeValues()
    {
        //loops through all children of the nodeList which are StackPanes that contain the nodes
        for(Node node: nodeList.getChildren())
            if (node instanceof StackPane) {
                // clear
                if(((StackPane)node).getChildren().get(2) instanceof Text)//the object at the second index is a Text object
                {
                    //this will be moved later, initialises a nodes value
                    //casts the node twice so its a Text object and sets the value
                    ((Text) ((StackPane)node).getChildren().get(2)).setText("5");

                    //fills an ArrayList with all the Text objects that contrain the values for our nodes
                    nodeValues.add(((Text) ((StackPane)node).getChildren().get(2)));
                }
            }
    }


    //Determining the country a player clicks on
    public void determineClick(int y, int x){
        breadFirstSearch bfs = new breadFirstSearch(); //object used for animation
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
                        chatBoxController.textOutput(new TextField(countryName));
                        break;
                    }
                }
            }
            bfs.startBFS(clicked, grid, Color.YELLOW, countries.getCountries(), getCountryIndex(countries.getCountries(), countryName));
            //currently here for testing the animation of
            //claiming a country
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

}
