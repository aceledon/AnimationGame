package game;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Code based on Lee Stemkoski's tutorial and example codes
 * https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835
 * https://github.com/tutsplus/Introduction-to-JavaFX-for-Game-Development
 *
 * Additional information on Transition and Translation By Jeff Friesen
 * from http://www.informit.com/articles/article.aspx?p=2359759
 */

public class GUI extends Application{


    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage window) throws Exception {

        //* WINDOW PROPERTIES *//
        final int width = 695;
        final int height = 419;
        window.setTitle("Andrea's App");


        //* SCEEN START GAME *//
        Group startGameGroup = new Group(); //root node
        Scene startGameScene = new Scene(startGameGroup);
        Canvas startGameCanvas = new Canvas(width, height);

        GraphicsContext startGameGraphics = startGameCanvas.getGraphicsContext2D();
        Image startGameBG = new Image("super-mario-bg.png");
        Button btnStart = new Button("Start Game");
        startGameGroup.getChildren().addAll(startGameCanvas, btnStart);
        startGameGraphics.drawImage(startGameBG, 0, 0);

        window.setScene(startGameScene);


        //* SCEEN GAME ON *//
        Group gameOnGroup = new Group(); //root node
        Scene gameOnScene = new Scene(gameOnGroup);
        Canvas gameOnCanvas = new Canvas(width, height);
        gameOnGroup.getChildren().add(gameOnCanvas);
        GraphicsContext gameOnGraphics = gameOnCanvas.getGraphicsContext2D();

        Font theFont = Font.font("Calibri", FontWeight.BOLD, 24);
        gameOnGraphics.setFont(theFont);
        gameOnGraphics.setFill(Color.RED);



        //* KEY ON ACTION METHODS *//

		/* this lists holds which movement to perform based on the pressed key.
		 * Movement is removed from array when key is released
		 */
        ArrayList<String> moves = new ArrayList<String>();

        //Pressing a key
        gameOnScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e)
            {
                String code = e.getCode().toString(); //identifies which key was pressed (returns RIGHT, LEFT, UP or DOWN)
                //System.out.println(code);
                if (!moves.contains(code))
                    moves.add(code);
            }
        });

        //releasing a key
        gameOnScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                moves.remove(code);
            }
        });



        //*CREATING CHARACTERS OBJECTS AND ITS POSITIONS*//

        Mario mario = new Mario();
        Turtle turtle = new Turtle();

        int totalCoins = (int) (Math.random()*20) + 5;					// random number of coins (min 5, max 25)
        ArrayList<Character> coinsList = new ArrayList<Character>();  	//List to hold coins available

        //creates total number of coin objects and adds them to the list
        for (int i = 0; i < totalCoins; i++) {
            Coin coin = new Coin();
            coinsList.add(coin);
        }


        btnStart.setOnAction(e -> {
            //CHANGING SCENE
            window.setScene(gameOnScene);

            //* ANIMATION STARTS *//
            LongValue lastNanoTime = new LongValue(System.nanoTime());
            IntValue score = new IntValue(0);
            int marioSpeed = 150;

            new AnimationTimer() {

                public void handle(long currentNanoTime) {
                    // calculate time since last update
                    double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                    lastNanoTime.value = currentNanoTime;

                    // mario move logic
                    mario.setVelocity(0,0);
                    if (moves.contains("LEFT")) {
                        mario.setImage("mario-runs-left.png");
                        mario.addVelocity(-marioSpeed,0);
                    }
                    if (moves.contains("RIGHT")) {
                        mario.setImage("mario-runs-right.png");
                        mario.addVelocity(marioSpeed,0);
                    }
                    if (moves.contains("UP"))
                        mario.addVelocity(0,-marioSpeed);

                    if (moves.contains("DOWN"))
                        mario.addVelocity(0,marioSpeed);

                    mario.update(elapsedTime);
                    System.out.println("Mario: " + mario.toString());

                    //turtle path
                    double turtleSpeed = 1;
                    if(turtle.getPositionX() < 200.0) {
                        turtleSpeed = 1;
                    }
                    if(turtle.getPositionX() > 400.0) {
                        turtleSpeed = -1;
                    }
                    if (turtle.getPositionX() > 500.0 && turtle.getVelocityX() ==0) {
                        turtle.setImage("paratroopa-left.png");
                    }
                    if (turtle.getPositionX() < 300.0 && turtle.getVelocityX() ==0) {
                        turtle.setImage("paratroopa-right.png");
                    }

                    turtle.addVelocity(turtleSpeed,0);
                    System.out.println("Turtle: " + turtle.toString() +"\n");
                    turtle.update(elapsedTime);

                    //Turtle collision
                    if(turtle.intersects(mario)){
                        stop();
                        //window.setScene(gameOverScene);
                    }


                    // Coin collection
					/*Using the Iterator allows to remove coins from the coinsList array*/
                    Iterator<Character> coinIter = coinsList.iterator();

                    while (coinIter.hasNext())	{
                        Character coin = coinIter.next();
                        if (mario.intersects(coin)) {
                            coinIter.remove();
                            score.value++;
                        }
                    }


                    // render
                    Image background = new Image("mario_sprite_background.png");
                    //graphics.clearRect(0, 0, width, height); // REQUIRED IF I HAD NO BACKGROUND
                    gameOnGraphics.drawImage(background, 0, 0);
                    mario.render(gameOnGraphics);

                    //displays coins
                    for (Character coin : coinsList)
                        coin.render(gameOnGraphics);

                    String pointsText = "Score: $" + (100 * score.value);
                    gameOnGraphics.fillText(pointsText, 20, 30);

                    turtle.render(gameOnGraphics);

                }
            }.start();
        });

        //* SHOW WINDOW *//
        window.show();
    }




}