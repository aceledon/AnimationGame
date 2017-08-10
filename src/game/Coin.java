package game;

public class Coin extends Character {

    double x = Math.random()*550 + 80;   //coins must only display greater than 80px on the X Axis
    double y = Math.random()*200 + 50;   //coins must only display greater than 50px on the Y Axis

    public Coin(){
        super();
        this.setImage("golden-coin.png");
        this.setPosition(x, y);

    }


}
