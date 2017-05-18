package sprites;

/**
 * Created by annameyer on 5/17/17.
 */

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.lang.Math;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class ameyer95 extends Sprite  {
    public AudioClip audioClip;
    public ImageView imageView;

    public ameyer95(){
        Image image1 = new Image(getClass().getResourceAsStream("/res/ameyer951.png"));
        Image image2 = new Image(getClass().getResourceAsStream("/res/ameyer952.png"));
        this.imageView = new ImageView();
        this.imageView.setImage(image1);
        this.getChildren().add(this.imageView);

        this.audioClip = new AudioClip(getClass().getResource("/res/ameyer95.wav").toString());
    }

    public void setSize(double width, double height) {
        super.setSize(width, height);
        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);
    }

    public void step() {
        Point2D position = this.getPosition();
        Point2D velocity = this.getVelocity();
        if(position.getY() > 320 && position.getX() < 400) {
            Image image2 = new Image(getClass().getResourceAsStream("/res/ameyer952.png"));
            PauseTransition pause = new PauseTransition(Duration.seconds(4));
            pause.play();
            this.imageView.setImage(image2);
            this.setPosition(position.getX() + 30, position.getY() - Math.random()*300);
            this.makeSound();
            pause.play();
        }
        else if(position.getY() > 320) {
            Image image2 = new Image(getClass().getResourceAsStream("/res/ameyer952.png"));
            PauseTransition pause = new PauseTransition(Duration.seconds(4));
            pause.play();
            this.imageView.setImage(image2);
            this.setPosition(position.getX(), position.getY() - Math.random()*300);
            this.makeSound();
            pause.play();
        }
        else {
            Image image1 = new Image(getClass().getResourceAsStream("/res/ameyer951.png"));
            this.imageView.setImage(image1);
            this.setPosition(position.getX() + velocity.getX(), position.getY() + velocity.getY());
        }
    }

    public void setPosition() {

    }

    @Override
    public void makeSound() {
        this.audioClip.play();
    }
}
