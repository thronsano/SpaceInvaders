package graphics;

import javafx.scene.image.Image;
import java.io.Serializable;

/**
 * Created by Szaman on 26.10.2017.
 */


public class Frame implements  Comparable<Frame>, Serializable {
    private String[][] frame;
    private int frameNumber;

    public Frame(int frameNumber) {
        this.frameNumber = frameNumber;
        this.frame = new String[Settings.WIDTH][Settings.HEIGHT];
    }

    public void SetFrame(String[][] frame) {
        this.frame = frame;
    }

    public String[][] getFrame() {
        return frame;
    }

    @Override
    public int compareTo(Frame o) {
        return Integer.compare(this.frameNumber,o.getFrameNumber());
    }

    public int getFrameNumber() {
        return frameNumber;
    }
}
