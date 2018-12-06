import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.min;

public class ThreadMaker {
    private BufferedImage start;
    private BufferedImage end;
    private int countOfThreads;
    private String outfile;
    private String infile;

    ThreadMaker(int c, String infile, String outfile) {
        countOfThreads = c;
        this.outfile = outfile;
        this.infile = infile;
    }

    private void ReadFromFile() {
        try {
            start = ImageIO.read(new File(infile));
            end = new BufferedImage(start.getWidth(), start.getHeight(), BufferedImage.TYPE_INT_RGB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void HorizontalStrips() {
        ReadFromFile();
        int i;
        int w = start.getWidth();
        int h = start.getHeight();
        int c = w / countOfThreads;
        for (i = 0; i < w; i += c) {
            int endX = min(w, i + c);
            Filter f = new Filter(start, end, i, 0, endX, h);
            f.start();
            try {
                f.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        WriteToFile();
    }

    public void VerticalStrips() {
        ReadFromFile();
        int i;
        int w = start.getWidth();
        int h = start.getHeight();
        int c = h / countOfThreads;
        for (i = 0; i < h; i += c) {
            int endY = min(h, i + c);
            Filter f = new Filter(start, end, 0, i, w, endY);
            f.start();
            try {
                f.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        WriteToFile();
    }

    private void WriteToFile() {
        try {
            ImageIO.write(end, "bmp", new File(outfile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
