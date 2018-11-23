import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        BufferedImage img = null;
        int h = 0;
        int w = 0;
        try {
            img = ImageIO.read(new File("snail.bmp"));
            h = img.getHeight();
            w = img.getWidth();
            BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Filter f = new Filter(img, res);
            f.run();
            ImageIO.write(res, "bmp", new File("saved.bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
