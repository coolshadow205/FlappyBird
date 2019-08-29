package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.input.KeyEvent;

public class Main extends Application {

    private Group group;
    private Stage window;
    private GenerateRandRect randomRect;

    @Override
    public void start(Stage windowE) {
        window = windowE;
        group = new Group();

        Scene scene = new Scene(group , 612 , 600);

        randomRect = GenerateRandRect.setupEverything(5 , 30, 2, 100 ,  300,600 , 600 , 250,250 ,100, group);
        scene.setFill(Color.rgb(23, 26, 33));
        EventHandler<KeyEvent> keyEvent = new EventHandler<KeyEvent>(){
            int k=0;
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.SPACE){
                    if(k==0){
                        randomRect.startThread();
                        k=1;
                    }
                    randomRect.jump();
                }
                if(event.getCode() == KeyCode.C)
                    randomRect.close();
            }
        };
        scene.setOnKeyPressed(keyEvent);
        window.setScene(scene);
        window.setX(385);
        window.setY(36);
//      window.setResizable(false);
        window.setTitle("FlappyBird");
        EventHandler<WindowEvent> windowEvent = new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event){
                event.consume();
                randomRect.close();
//                System.out.println("X: "+window.getX()+" Y: "+window.getY()+"Width : "+window.getWidth()+" Height: "+window.getHeight());
                window.close();
                Platform.exit();
            }
        };
        window.setOnCloseRequest(windowEvent);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
