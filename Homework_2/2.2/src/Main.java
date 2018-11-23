public class Main {
    public static void main(String[] args) {
        int numberOfThreads = 3;
        String infile = "dog.bmp";
        String outfile = "saved.bmp";

        ThreadMaker make = new ThreadMaker(numberOfThreads, infile, outfile);
        make.VerticalStrips();
    }
}