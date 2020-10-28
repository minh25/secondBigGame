package engine_circle;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.v1;

public class CircleNFx extends Circle {
    protected Point velocity;

    public CircleNFx() {
        this.velocity = new Point();
        this.setFill(Color.BLACK);
        this.setRadius(Math.random()*50);
        this.setCenterX(Math.random() * (v1.pref_width - 2 * this.getRadius()) + this.getRadius());
        this.setCenterY(Math.random() * (v1.pref_height - 2 * this.getRadius()) + this.getRadius());

        ManagerShape.circleNFxList.add(this);
    }

    public CircleNFx(double centerX, double centerY) {
        this.velocity = new Point();
        this.setFill(Color.BLACK);
        this.setCenterX(centerX);
        this.setCenterY(centerY);
        this.setRadius(Math.random() * Math.min(50,
                Math.min(
                        Math.min(v1.pref_width - centerX, centerX),
                        Math.min(v1.pref_height - centerY, centerY))));

        ManagerShape.circleNFxList.add(this);
    }

    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

    public Point getVelocity() {
        return velocity;
    }

    public void update() {
        this.setCenterX(this.getCenterX() + velocity.x);
        this.setCenterY(this.getCenterY() + velocity.y);

        if (this.getCenterX() - this.getRadius() < 0 || this.getCenterX() + this.getRadius() > v1.pref_width) {
            velocity.x = -velocity.x;
        }

        if (this.getCenterY() - this.getRadius() < 0 || this.getCenterY() + this.getRadius() > v1.pref_height) {
            velocity.y = -velocity.y;
        }
    }
}
