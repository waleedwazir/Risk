package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class breadFirstSearch {

    //bread first search implementation in the context of our grid,
    //used as a tool for animation on countries.

    public void startBFS(Coordinate start, Rectangle[][] grid, Color colour){
        //declaring a timeline to which we will add frames to create the animation
        Timeline tl = new Timeline();

        //useful Duration objects, used to create the flow of frames
        Duration timepoint = Duration.ZERO; //the points at which frames will be placed
        Duration pause = Duration.millis(3); //duration between frames

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
        while(!queue.isEmpty()){
            timepoint = timepoint.add(pause);
            Coordinate c = queue.remove();
            kf = new KeyFrame(timepoint, e -> visit(grid, c, colour));
            tl.getKeyFrames().add(kf);
            addNeighbours(grid, c, queue, colour, visited);
            visited.add(c);
        }
        tl.play();
    }

    public void addNeighbours(Rectangle[][] grid, Coordinate c, Queue<Coordinate> queue, Color colour, ArrayList<Coordinate> visited){
        int y = c.getY();
        int x = c.getX();
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
        grid[c.getY()][c.getX()].setFill(colour);
        grid[c.getY()][c.getX()].setStroke(colour);
    }

    public static boolean hasCoord(ArrayList<Coordinate> visited, Coordinate c){
        for(Coordinate x:visited){
            if(c.getX()==x.getX() && c.getY()==x.getY()){
                return true;
            }
        }
        return false;
    }
}


