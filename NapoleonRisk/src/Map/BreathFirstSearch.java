package Map;

import Controllers.MainController;
import Map.Coordinate;
import Map.Country;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BreathFirstSearch
{

    //bread first search implementation in the context of our grid,
    //used as a tool for animation on countries.
    MainController mainController;

    public void injectMainController(MainController mainController)
    {
        this.mainController = mainController;
    }

    public void startBFS(Coordinate start, Rectangle[][] grid, Color colour, ArrayList<Country> countries, int index){
        //declaring a timeline to which we will add frames to create the animation
        Timeline tl = new Timeline();

        //useful Duration objects, used to create the flow of frames
        Duration timepoint = Duration.ZERO; //the points at which frames will be placed
        Duration pause = Duration.millis(6); //duration between frames

        //declaration of our frame object
        KeyFrame kf;

        //a queue to which we add coordinates to perform animations on in
        //sequential order
        Queue<Coordinate> queue = new LinkedList<>();
        queue.add(start);

        //a visited array that tracks the coordinates that have been visited by our
        //breadth first search, to prevent repetition
        ArrayList<Coordinate> visited = new ArrayList<>();

        //a loop that only ends once the queue is empty
        //holds just 1 coordinate at start
        //the clicked coordinate
        int i = 0;
        while(!queue.isEmpty()){
            //increment the time to place the frame on the timeline at incremented places
            timepoint = timepoint.add(pause);

            //remove first coordinate from the queue
            Coordinate c = queue.remove();

            //add the animation of painting the coordinate on the grid to the frame
            kf = new KeyFrame(timepoint, e -> visit(grid, c, colour));

            //adds the neighbours of the coordinate to the queue
            addNeighbours(grid, c, queue, colour, visited);

            //adds the coordinate to the visited arraylist
            visited.add(c);

            //add the frame to the timeline
            tl.getKeyFrames().add(kf);

            if(queue.isEmpty()){
                if(isBadIndex(index)){
                    Coordinate missing = getCountryRemainder(countries.get(index), visited);
                    if(missing!=null){
                        queue.add(missing);
                    }
                }
            }
        }
        //play the timeline of frames
        tl.play();
    }


    public void addNeighbours(Rectangle[][] grid, Coordinate c, Queue<Coordinate> queue, Color colour, ArrayList<Coordinate> visited){
        //retrieve the x and y of the coordinate
        int y = c.getY();
        int x = c.getX();
        //checks for neighbouring coordinates and adds them to the queue
        //if they aren't a border or if they haven't been visited yet
        //repeat this for all orientations
        if(grid[y][x-1].getFill()!=Color.BLACK && !hasCoord(visited, new Coordinate(y, x-1))){//left
            Coordinate neighbour = new Coordinate(y, x-1);
            queue.add(neighbour);
            visited.add(new Coordinate(y, x-1));
        }
        if(grid[y][x+1].getFill()!=Color.BLACK && !hasCoord(visited, new Coordinate(y, x+1))){//right
            Coordinate neighbour = new Coordinate(y, x+1);
            queue.add(neighbour);
            visited.add(new Coordinate(y, x+1));
        }
        if(grid[y+1][x].getFill()!=Color.BLACK && !hasCoord(visited, new Coordinate(y+1, x))){//down
            Coordinate neighbour = new Coordinate(y+1, x);
            queue.add(neighbour);
            visited.add(new Coordinate(y+1, x));
        }
        if(grid[y-1][x].getFill()!=Color.BLACK && !hasCoord(visited, new Coordinate(y-1, x))){//up
            Coordinate neighbour = new Coordinate(y-1, x);
            queue.add(neighbour);
            visited.add(new Coordinate(y-1, x));
        }
    }

    public void visit(Rectangle[][] grid, Coordinate c, Color colour){
        //set the colour of the square in the grid at
        //specified coordinate
        grid[c.getY()][c.getX()].setFill(colour);
        grid[c.getY()][c.getX()].setStroke(colour);
    }

    public static boolean hasCoord(ArrayList<Coordinate> visited, Coordinate c){
        //function to determine if a coordinate is contained within an arraylist
        for(Coordinate x:visited){//loops through each coordinate in visited
            if(c.getX()==x.getX() && c.getY()==x.getY()){//if the coordinate is found returns true
                return true;
            }
        }
        return false;
    }

    //finds remaining squares that need to be painted when country has segmented parts
    public Coordinate getCountryRemainder(Country country, ArrayList<Coordinate> visited){
        for(Coordinate c:country){
            if(!hasCoord(visited, c)){
                return c;
            }
        }
        return null;
    }

    //countries that have segmented pieces of land
    //returns true if the index of the clicked country
    //is one such country
    public boolean isBadIndex(int index){
        int[] badIndices = {0, 14, 17, 19, 28};
        for(Integer badIndex:badIndices){
            if(badIndex==index){
                return true;
            }
        }
        return false;
    }
}


