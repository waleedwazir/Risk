package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.show();
        Controller controller = new Controller();

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int y = (int) event.getY()/5;
                int x = (int) event.getX()/5;
                System.out.println(y+"\t"+x);
                if(!(x>199)) {
                    controller.determineClick(y, x);
                }
            }
        });
    }




    public static void main(String[] args) {
        launch(args);
    }
}
