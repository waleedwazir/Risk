package sample;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.event.ActionEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Controller {

    public Pane pane;
    @FXML
    AnchorPane anchor;
    @FXML
    Button startButton;
    @FXML
    TextField text;
    @FXML
    VBox chatBox;

    @FXML
    ScrollPane scroll; //this must match the fx:id of the ScrollPane element

    @FXML
    Pane names;


    public List<Label> messages = new ArrayList<>();
    public int messageIndex = 0;

    public void updateScroll()
    {
        scroll.vvalueProperty().bind(chatBox.heightProperty());
    }

    //declaration of grid
    static Rectangle[][] grid = new Rectangle[120][200];
    //initialises a Countries object
    Countries countries = new Countries();


    public void newGrid(ActionEvent actionEvent)
    {
        pane.getChildren().clear();
        for (int y = 0; y < 120; y++){
            for (int x = 0; x < 200; x++){
                Rectangle rect = new Rectangle(x * 5, (5 * y), 5, 5);
                rect.setFill(Color.CYAN);
                rect.setStroke(Color.CYAN);
                rect.setOpacity(0.8);
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
                    grid[c.getY()][c.getX()].setOpacity(0.8);

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
            names.toFront();
        }
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
        chatBox.getChildren().add(messages.get(messageIndex));
        messageIndex++;
        updateScroll();//scrolls the pane as text comes in
    }

    public void sendMessageEnter(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.ENTER)
        {
            textOutput(text);

        }
    }
    public void sendMessageButton(ActionEvent actionEvent)
    {
        textOutput(text);
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
            for(int i=0;i<42;i++){
                for(Coordinate c:queue.get(i)){
                    if(c.equals(clicked)){
                        textOutput(new TextField(queue.get(i).getName()));
                        break;
                    }
                }
            }
            bfs.startBFS(clicked, grid, Color.YELLOW);  //currently here for testing the animation of
                                                        //claiming a country
        }


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
}
