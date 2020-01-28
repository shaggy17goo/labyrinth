
import java.io.IOException;

/**
 * Class for storing current Labirynt objects and data associated with it
 */
public class Data {
    public static int previousType;
    /**
     * current labyrinth
     */
    public static Labyrinth labyrinth;
    /**
     * Length and height of the window containing labyrinth
     */
    public static int windowX, windowY;
    /**
     * Determines if the labyrinth is able to be solved
     */
    public static boolean path;
    public static int   pixelsPerCell=10,
            lineThickness=2;
    public static int size=0;
    /**
     * Constructor
     * @param length max X of labyrinth
     * @param height max Y of labyrinth
     * @param algorithmType integer index of the labyrinth we want to generate
     */
    public Data(int length, int height, int algorithmType, int size){
        labyrinth=new Labyrinth(length,height,algorithmType, size);
        path=false;
        this.size=size;
        windowX=80+10*labyrinth.maxX;
        windowY=80+10*labyrinth.maxY;
    }

    /**
     * Generate a new Labyrinth
     * @param algorithmType integer index of the labyrinth we want to generate
     */
    public static void generateNewLabyrinth(int algorithmType,int size) {
        labyrinth=new Labyrinth(labyrinth.maxX,labyrinth.maxY,algorithmType, size);
        windowX=80+10*labyrinth.maxX;
        windowY=80+10*labyrinth.maxY;
    }

    /**
     * Generate a new graph labyrinth
     * @param size size of the labyrinth
     */
    public static void generateGraphLabyrinth(int size) {
        labyrinth=new Labyrinth(labyrinth.maxX,labyrinth.maxY,4, size);
        windowX=41+pixelsPerCell*labyrinth.maxX;
        windowY=1+pixelsPerCell*labyrinth.maxY;
    }


    /**
     * Saves the labyrinth to a given file
     * @param filePath name of the file we want to save it to
     */
    public static void saveLabyrinth(String filePath) throws IOException {
        try {
            if(filePath.substring(filePath.length()-4).equals(".txt"))
                labyrinth.saveLabyrinth(filePath);
            else if(filePath.substring(filePath.length()-4).equals(".bmp"))
                labyrinth.exportToBMP(filePath);
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * loads the labyrinth from a file
     * @param filePath
     */
    public static void loadLabyrinth(String filePath) throws IOException {
        try {
            if(labyrinth.algorithmType>=0) {
                previousType = labyrinth.algorithmType;
            }
            if(filePath.substring(filePath.length()-4).equals(".txt"))
                labyrinth.loadLabyrinth(filePath);
            else if(filePath.substring(filePath.length()-4).equals(".bmp"))
                labyrinth.importFromBMP(filePath);

        } catch (IOException ioe) {
            throw ioe;
        }
    }

}
