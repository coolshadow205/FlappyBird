package sample;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class BirdHandler {

    private int heightOfStage;
    private Circle bird;
    private int gravity , initialX , initialY , radius;
    private Thread startThread;
    private boolean close = false;

    public BirdHandler( int heightOfStage , int gravity , int initialXOfBird , int initialYOfBird , int radiusOfBird ){
        this.heightOfStage = heightOfStage;
        this.gravity = gravity;
        this.initialX = initialXOfBird;
        this.initialY = initialYOfBird;
        this.radius = radiusOfBird;
        initializeEveryThing();
    }
    private void initializeEveryThing(){
        bird = new Circle(initialX , initialY , radius);
        bird.setFill(Color.rgb(255, 26, 26));
    }
    public void increaseThePosition(){

        if(bird.getCenterY()+radius > 75   ) {
//            System.out.println("Cricle y: "+bird.getCenterY()+radius);
            for (int i = 1; i <= 40; i++) {
                bird.setCenterY(bird.getCenterY() - 1);
            }
        }
    }
    public void close(){
        close = true;
    }
    public Circle getBird(){
        return this.bird;
    }
    public void decreaseThePostion(){
        if(bird.getCenterY()+radius < heightOfStage)
            bird.setCenterY(bird.getCenterY() + gravity);
    }
}
