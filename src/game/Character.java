package game;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;


public class Character {
    /**
     * Character is a super class that defines the properties of each image in the scene
     * It is used to reset position of the character, it's traveling velocity and image, if required
     */

    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;

    //CONSTRUCTORS
    public Character() {
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
    }

    public Character(Image img) {
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
        image = img;
    }


    //SETTERS

    public void setImage(Image i) {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename) {
        Image i = new Image(filename);
        setImage(i);
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }


    public void addVelocity(double x, double y) {
        velocityX += x;
        velocityY += y;
    }

    public void update(double time) {
	/* character updates position based on current position,
	 * velocity at which is traveling and time that elapsed since last position
     */
        positionX += velocityX * time;
        positionY += velocityY * time;
    }


    public void render(GraphicsContext gc) {
        gc.drawImage( image, positionX, positionY );
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX,positionY,width,height);
    }


    public boolean intersects(Character s) {
	/*recursive method that compares the area occupied (2D rectangle of the image size) by the the parameterized
	 * character with the area occupied by the another character
	 * returns true if the areas are touching
     */
        return s.getBoundary().intersects( this.getBoundary() );
    }

    public double getPositionX() {
        return positionX;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public String toString() {
        return " Position: [" + positionX + "," + positionY + "]"
                + " \tVelocity: [" + velocityX + "," + velocityY + "]";
    }
}