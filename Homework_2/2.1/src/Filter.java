import java.awt.*;
import java.awt.image.BufferedImage;

public class Filter {

    private BufferedImage start;
    private BufferedImage end;
    private int w;
    private int h;

    Filter(BufferedImage start, BufferedImage end) {
        this.start = start;
        this.end = end;
        w = start.getWidth();
        h = start.getHeight();
    }

    public void run() {
        int i, j;
        for (i = 0; i < w; i++) {
            for (j = 0; j < h; j++) {
                end.setRGB(i, j, newColor(i, j));
            }
        }
    }

    private int newColor(int x, int y) {

        int i = 0;
        int j = 0;
        int pixel = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        int numOfPixel = 0;

        for (i = x - 1; i <= x + 1; i++) {
            for (j = y - 1; j <= y + 1; j++) {
                if ((j >= 0) && (j < h) && (i >= 0) && (i < w)) {
                    try {
                        pixel = start.getRGB(i,j);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.printf("i = %d, j = %d, w = %d, h = %d,  num = %d\n", i, j, w, h, numOfPixel);
                        System.exit(100);
                    }
                    red += (pixel & (0xff0000)) >> 16;
                    green += (pixel & (0xff00)) >> 8;
                    blue += pixel & 0xff;
                    numOfPixel++;
                }
            }
        }
            red /= numOfPixel;
            green /= numOfPixel;
            blue /= numOfPixel;

        Color myRGB = new Color(red, green, blue);
        return (myRGB.getRGB());
    }
}
