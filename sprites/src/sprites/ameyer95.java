package sprites;

/**
 * Created by annameyer on 5/17/17.
 */

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import javafx.animation.ScaleTransition;


public class ameyer95 extends Sprite  {
    public AudioClip audioClip;
    public ImageView imageView;

    public ameyer95(){
        Image image1 = new Image(getClass().getResourceAsStream("/res/ameyer951.png"));
        this.imageView = new ImageView();
        this.imageView.setImage(image1);
        this.getChildren().add(this.imageView);

        this.audioClip = new AudioClip(getClass().getResource("/res/ameyer95.wav").toString());
    }

    public void setSize(double width, double height) {
        super.setSize(width, height);

        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);


        ScaleTransition st = new ScaleTransition(Duration.millis(2000), this);
        st.setByX(width*.003);
        st.setByY(width*.003);
        st.setCycleCount(999999);
        st.setAutoReverse(true);

        st.play();
    }

    public void step() {
        Point2D position = this.getPosition();
        Point2D velocity = this.getVelocity();

        if (velocity.getY() < 0 && velocity.getX() > 0) {
            Image image2 = new Image(getClass().getResourceAsStream("/res/ameyer952.png"));
            this.imageView.setImage(image2);
            this.setPosition(position.getX() + velocity.getX(), position.getY() + velocity.getY());
        }
        else {
            Image image1 = new Image(getClass().getResourceAsStream("/res/ameyer951.png"));
            this.imageView.setImage(image1);
            this.setPosition(position.getX() + velocity.getX(), position.getY()+velocity.getY());
        }
    }

    @Override
    public void makeSound() {
        // or I could put the picture animation here?
        this.audioClip.play();
    }
}
