package engine_circle;

import java.util.ArrayList;
import java.util.List;

public class ManagerShape {
    static List<CircleNFx> circleNFxList = new ArrayList<>();

    static public void update() {
        for (CircleNFx circleNFx: circleNFxList ) {
            circleNFx.update();
        }
    }
}
