package org.example;

import javafx.scene.paint.Color;

public class v3MisSile extends v3Atom {
    public v3MisSile(Color fill) {
        super(5, fill, false);
    }

    public v3MisSile(int radius, Color fill) {
        super(radius, fill, true);
    }
}
