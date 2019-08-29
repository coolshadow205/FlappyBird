package sample;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;


public class GenerateRandRect {

    /* This class will generate Rectangle with random height and position
     * java.util.ArrayList is used in this class to store the Rectangle.
     * java.util.Random is used to generate random height and position for the rectangle.
     * 'group' It is object of Group Layout in which this class will add all the Rectangle
     *
     * 'heightOfStage' means height of window which is required to generate the height of rectangle
     * 'gapOfBird' gives me the space between two vertical rectangle from which the bird will pass
     * 'spacingBetweenPairRect' It is the horizontal space between the pair of rectangle
     *
     * 'widthOfRect' will specify the width of the Rectangle*/



    private int numOfRect;
    private Group group;
    private Thread callRun;
    private ArrayList<Rectangle> arrayList;
    private int widthOfRect , widthOfStage , gapForBird , spacingBetweenPairRect ,heightOfStage , radiusOfBird , gravity , initialX , initialY;
    private Rectangle demo;
    private Circle bird;
    private boolean close = false;
    private Random rand;
    private BirdHandler birdGenerator;

    private GenerateRandRect(int numOfRect ,int radiusOfBird , int gravity , int initialX , int initialY ,int widthOfStage ,int heightOfStage, int gapForBird ,  int spacingBetweenPairRect , int widthOfRect, Group grp) {
        this.gapForBird = gapForBird;
        this.radiusOfBird = radiusOfBird;
        this.gravity = gravity;
        this.initialX = initialX;
        this.initialY = initialY;
        this.spacingBetweenPairRect = spacingBetweenPairRect;
        this.widthOfStage = widthOfStage;
        this.widthOfRect = widthOfRect;
        this.numOfRect = numOfRect;
        this.heightOfStage = heightOfStage;
        this.group = grp;
        initializeEveryThing();
        generateRect();
    }

    private void initializeEveryThing(){
        rand = new Random();
        birdGenerator = new BirdHandler(heightOfStage , gravity , initialX, initialY, radiusOfBird);
        bird = birdGenerator.getBird();
        arrayList = new ArrayList<Rectangle>();
        Runnable runnableThread = new Runnable() {
            @Override
            public void run() {
                System.out.println("Rect: "+ Platform.isFxApplicationThread());
                try {
                    int i;
                    while(!close) {
                        i=0;
                        while (i != arrayList.size()) {
                            demo = arrayList.get(i);
                            demo.setX(demo.getX() - 10);
                            demo = arrayList.get(++i);
                            demo.setX(demo.getX() - 10);
                            if(i==1)
                                checkBirdHit();
                            i++;

                        }
                        if(!close)
                            checkRectPosition();
                        else
                            break;
                        Thread.sleep(25);
                        birdGenerator.decreaseThePostion();
                    }
                    System.out.println("Breaked");
                }
                catch (InterruptedException ie){
                    System.out.println(ie);
                }
                catch (ArrayIndexOutOfBoundsException r){
                    System.out.println(r.getMessage());
                    r.printStackTrace();
                }
            }
        };
        callRun = new Thread(runnableThread);
    }

    private void checkRectPosition() {
        Rectangle firstRect = arrayList.get(0);
        Rectangle secondRect = arrayList.get(1);
        double position = firstRect.getX()+firstRect.getWidth();
        if( position <= 0) {
            //System.out.println("X is : "+firstRect.getX()+" Width: "+firstRect.getWidth()+"Pos: "+position);
            if(arrayList.size() != 2) {
                Rectangle lastRect = arrayList.get(arrayList.size() - 1);
                double rectX = (lastRect.getX() + lastRect.getWidth() + spacingBetweenPairRect);
                arrayList.remove(firstRect);
                arrayList.remove(secondRect);
                firstRect.setX(rectX);
                secondRect.setX(rectX);
                arrayList.add(firstRect);
                arrayList.add(secondRect);
                return;
            }
            firstRect.setX(widthOfStage);
            secondRect.setX(widthOfStage);
        }
    }
    public void startThread(){
        callRun.start();
        /*new Thread(){
            @Override
            public void run(){
                while(!close)
                    checkBirdHit();
            }
        }.start();*/
    }
    public void jump(){
        if(!close)
            birdGenerator.increaseThePosition();
    }
    private void generateRect() {
        /* This is function will generate random rectangles
         * Rect with random height using nextInt of Random class
         * Random Position At X-AXIS(Horizontal position) is generated only once and then stored in 'rectX'
         * 'rectX' is add with spacingBetweenPairRect and widthOfRect so that the space between each pair will be same
         * 'rectY' will the sum of height and gapOfBird where the height is of the first rectangle in pair
         * The height of second rectangle in pair is the subtraction of heightOfStage-(height+gapOfBird)
         * */
        int rectX , height = 0 , rectY = 0 ;
        rectX = rand.nextInt(widthOfStage);
        for(int i=0 ; i< numOfRect*2 ; i++) {
            if( i%2 == 0) {
                rectX = rectX+spacingBetweenPairRect+widthOfRect;
                height = rand.nextInt((heightOfStage-gapForBird));
                //System.out.println(""+i+" Height "+height+" second height "+(heightOfStage-(height+gapForBird)));
                while( (height < 30) || heightOfStage-(height+gapForBird) < 30) {
                    /* This loop will be executed only when the height of the first rectangle in pair will be < 30
                     * OR when the height of the second rectangle will we < 30
                     * So this will give me new height unless both height is balanced.*/
                    height = rand.nextInt((heightOfStage - gapForBird));
                }
                //System.out.println("Height for"+i+" is: "+height+" and i+1 is: "+(heightOfStage-(height+gapForBird))+" RectX is: "+rectX+" Gap is: "+spacingBetweenPairRect);
                arrayList.add(new Rectangle(rectX, 0, widthOfRect, height));
                rectY = height+gapForBird;
                height = heightOfStage-(height+gapForBird);
            }
            else
                arrayList.add(new Rectangle(rectX, rectY,  widthOfRect, height));
        }
        for (Rectangle rect: arrayList) {
            rect.setFill(Color.rgb(129, 211, 233));
            group.getChildren().add(rect);
        }
        group.getChildren().add(bird);
    }

    private void checkBirdHit(){
        Rectangle firstRect = arrayList.get(0);
        Rectangle secondRect = arrayList.get(1);
        if(bird.getCenterX()+radiusOfBird >= firstRect.getX() && bird.getCenterX()-radiusOfBird <= ( firstRect.getX()+ firstRect.getWidth())){
            if( bird.getCenterY()-radiusOfBird <= firstRect.getHeight() || bird.getCenterY()+radiusOfBird >= (secondRect.getY()+secondRect.getHeight())){
                close = true;
                System.out.println("True: Birdy: "+bird.getCenterY()+" recty : "+firstRect.getY()+"height: "+firstRect.getHeight()+"sey: "+secondRect.getY()+" heig"+secondRect.getHeight());
            }
            else {
                close = false;
//                System.out.println("false: Birdy: "+bird.getCenterY()+" recty : "+firstRect.getY()+" sey: "+secondRect.getY());
            }
        }
        else
            close = false;
    }

    public void close() {
        close = true;
        birdGenerator.close();
    }

    public static GenerateRandRect setupEverything(int numOfRect ,int radiusOfBird , int gravity , int initialX , int initialY , int widthOfStage ,int heightOfStage , int gapForBird,  int spacingBetweenPairRect , int widthOfRect, Group grp) {
        return new GenerateRandRect(numOfRect , radiusOfBird , gravity , initialX , initialY , widthOfStage , heightOfStage , gapForBird ,spacingBetweenPairRect ,widthOfRect ,grp);
    }
}
