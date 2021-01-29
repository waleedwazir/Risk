package sample;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.event.ActionEvent;

import java.io.File;
import java.util.ArrayList;

public class Controller {

    public Pane pane;
    public Button printResults;
    @FXML
    AnchorPane anchor;
    @FXML
    Button startButton;

    static Rectangle[][] grid = new Rectangle[120][200];
    ArrayList<ArrayList<Coordinate>> countries = new ArrayList<ArrayList<Coordinate>>();

    public void readFile() {
        File f = new File("./resources/countries.txt");
        Reader reader = new Reader(f);
        reader.run();
        countries = reader.getCountries();
    }

    public void newGrid(ActionEvent actionEvent) {
        pane.getChildren().clear();
        readFile();
        for (int y = 0; y < 120; y++) {
            for (int x = 0; x < 200; x++) {
                Rectangle rect = new Rectangle(x * 5, (5 * y), 5, 5);
                rect.setFill(Color.CYAN);
                rect.setStroke(Color.CYAN);
                rect.setOpacity(0.8);
                anchor.getChildren().add(rect);
                grid[y][x] = rect;
            }
        }
        for(ArrayList<Coordinate> country:countries){
            for(Coordinate c:country){
                grid[c.getX()][c.getY()].setFill(Color.GRAY);
                grid[c.getX()][c.getY()].setStroke(Color.GRAY);
            }

        }
        printResults.toFront();

    }

    public static void fillRectangle(int y, int x){
        grid[y][x].setFill(Color.GREEN);
    }

    public void printResult(ActionEvent actionEvent){
        System.out.print("Final results:\n");
        for(int y=0;y<120;y++){
            for(int x=0;x<200;x++){
                if(grid[y][x].getFill()==Color.GREEN){
                    System.out.println(y+"\t"+x);
                }
            }
        }
    }
}
