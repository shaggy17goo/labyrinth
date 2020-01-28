import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Files;

public class Labyrinth {
    /**
     * Calling all constructors for our generating algorithms
     */
    private RecursiveDivision recursiveDivision = new RecursiveDivision();
    private DepthFirstSearch depthFirstSearch = new DepthFirstSearch();
    RecursiveBackTracker recursiveBackTracker = new RecursiveBackTracker();
    private PrimsAlgorithm primsAlgorithm = new PrimsAlgorithm();
    private EasyPeasyLemonSqueezy easyPeasyLemonSqueezy = new EasyPeasyLemonSqueezy();

    /**
     * Attributes of the labyrinth
     */
    public static int maxX;
    public static int maxY;
    public boolean[][] walls = new boolean[maxY][maxX];
    public LabCell[][] graphCells;// = new LabCell[maxX][maxY] ;
    public boolean[][] path;
    public int algorithmType;
    public int size = 1;

    /**
     * Constructor of Labyrinth class
     *
     * @param maxX          length of the labyrinth
     * @param maxY          Height of the labyrinth
     * @param algorithmType index of the algorithms we want to use
     */
    public Labyrinth(int maxX, int maxY, int algorithmType, int size) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.size = size;
        System.out.println(algorithmType);
        this.algorithmType = algorithmType;
        if (algorithmType < 0) {
            algorithmType = Data.previousType;
            this.algorithmType = Data.previousType;
        }
        System.out.println(algorithmType);
        switch (algorithmType) {
            case 0:
                generateRecursiveDivisionLabyrinth();
                break;
            case 1:
                generateDepthFirstSearchLabyrinth();
                break;
            case 2:
                generateEasyPeasyLemonSqueezyLabyrinth();
                break;
            case 3:
                generatePrimsLabyrinth();
                break;
            case 4:
                generateGraphLabyrinth();
                break;
            default: {
                generateDepthFirstSearchLabyrinth();
                break;
            }
        }
    }

    /**
     * Generate a new recursive division labyrinth
     */
    private void generateRecursiveDivisionLabyrinth() {

        switch (size) {
            case 0: {
                maxX = 17;
                maxY = 17;
            }
            break;
            case 1: {
                maxX = 33;
                maxY = 33;
            }
            break;
            case 2: {
                maxX = 65;
                maxY = 65;
            }
            break;
        }
        walls = new boolean[maxY][maxX];
        walls = recursiveDivision.getRecursiveDivisionWalls(maxY, maxX);
    }

    /**
     * Generate a new depth first search labyrinth
     */
    private void generateDepthFirstSearchLabyrinth() {
        switch (size) {
            case 0: {
                maxX = 15;
                maxY = 15;
            }
            break;
            case 1: {
                maxX = 31;
                maxY = 31;
            }
            break;
            case 2: {
                maxX = 63;
                maxY = 63;
            }
            break;
        }
        walls = new boolean[maxY][maxX];
        walls = depthFirstSearch.getDepthFirstSearchWalls(maxY, maxX);
    }

    /**
     * Generate a new "Autorski" labyrinth
     */
    private void generateEasyPeasyLemonSqueezyLabyrinth() {
        switch (size) {
            case 0: {
                maxX = 17;
                maxY = 17;
            }
            break;
            case 1: {
                maxX = 33;
                maxY = 33;
            }
            break;
            case 2: {
                maxX = 65;
                maxY = 65;
            }
            break;
        }
        walls = new boolean[maxY][maxX];
        walls = easyPeasyLemonSqueezy.getEasyPeasyLemonSqueezyWalls(maxY, maxX);
    }

    /**
     * Generate a new Prim's labyrinth
     */
    private void generatePrimsLabyrinth() {
        switch (size) {
            case 0: {
                maxX = 17;
                maxY = 17;
            }
            break;
            case 1: {
                maxX = 33;
                maxY = 33;
            }
            break;
            case 2: {
                maxX = 65;
                maxY = 65;
            }
            break;
        }
        walls = new boolean[maxY][maxX];
        walls = primsAlgorithm.getPrimsAlgorithmWalls(maxY, maxX);
    }

    /**
     * Generate a new Prim's labyrinth
     */
    private void generateGraphLabyrinth() {
        switch (size) {
            case 0: {
                maxX = 17;
                maxY = 17;
            }
            break;
            case 1: {
                maxX = 33;
                maxY = 33;
            }
            break;
            case 2: {
                maxX = 65;
                maxY = 65;
            }
            break;
        }
        graphCells = recursiveBackTracker.generate(maxX, maxY);
    }


    /**
     * Load Labyrinth from a given .txt file
     *
     * @param fileName String which is a path to a file we want to load
     */
    public void loadLabyrinth(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String s = "";
            String line = "";
            String length, height, type="1";
            boolean firstLine = true;
            boolean[][] walls=new boolean[1][1];
            LabCell[][] graphCells=new LabCell[1][1];
            int y = 0,maxX=65,maxY=65,algorithmType=-1;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    length = line.split(" ")[0];
                    height = line.split(" ")[1];
                    if(line.split(" ").length>2){
                        type=line.split(" ")[2];
                    }
                    if (!stringIsNumeric(length) || !stringIsNumeric(height))
                        throw new NotALabyrinthFileException("File does not contain the size of labyrinth");

                    maxX = Integer.parseInt(length);
                    maxY = Integer.parseInt(height);
                    walls = new boolean[maxY][maxX];
                    graphCells = recursiveBackTracker.generate(maxX,maxY);

                    if(line.split(" ").length>2){
                        algorithmType=Integer.parseInt(type);
                    }
                    else algorithmType=-1;

                    firstLine = false;

                } else {
                    if (algorithmType == 4) {    // this is a backtracker

                        // Take the line we read from file, split it,
                        // check if there is enough pieces.
                        // adjust paths in an existing backtracker:
                        // we need a for() to go through the split line

                        // check if line is correct length:
                        if (line.length() != (4 + 1) * maxX) { // four digits plus a space
                            throw new NotALabyrinthFileException(
                                    "File: " + fileName + ", line no. past header: " + y + ", Bad line length!!");
                        }
                        assert line.trim().split(" ").length == maxX
                                : "File: " + fileName + ", line no: " + y + " - Bad Line Length!!";

                        for (int i = 0; i < maxX; ++i) {
                            s = line.split(" ")[i];
                            // check length of s , shouod be 4
                            if (s.length() != 4) {
                                throw new NotALabyrinthFileException(
                                        "File: " + fileName + ", line no. past header: " + y + ", field no:" + i + ", Bad field lenght!!");
                            }

                            // at this point we've got a String with 4 charackters describing which paths
                            // can be followed from the cell graphCells[i][y].
                            // we need to adjust graphCells[i][y].paths[] accordingly.
                            // assume it's a rectangular backtracker

                            // check if first char is '1' or '0' if not throw exception
                            if (s.charAt(0) == '1') {
                                assert y > 0 : "File: " + fileName + ", line no. past header: " + y + ", field no: " + i +
                                        ", content: \"" + s + "\"" + " - First digit can't be '1' !!!";
                                graphCells[i][y].paths[0] = graphCells[i][y - 1];
                            } else if (s.charAt(0) == '0') {
                                graphCells[i][y].paths[0] = null;
                            } else
                                throw new NotALabyrinthFileException(
                                        "File: " + fileName + ", line no. past header: " + y + ", field no:" + i + ", Bad charackter(only '1' or '0')!!");

                            // check if second char is '1' or '0' if not throw exception
                            if (s.charAt(1) == '1') {
                                assert i + 1 < maxX : "File: " + fileName + ", line no. past header: " + y + ", field no: " + i +
                                        ", content: \"" + s + "\"" + " - Second digit can't be '1' !!!";
                                graphCells[i][y].paths[1] = graphCells[i + 1][y];
                            } else if (s.charAt(1) == '0') {
                                graphCells[i][y].paths[1] = null;
                            } else
                                throw new NotALabyrinthFileException(
                                        "File: " + fileName + ", line no. past header: " + y + ", field no:" + i + ", Bad charackter(only '1' or '0')!!");

                            // check if third char is '1' or '0' if not throw exception
                            if (s.charAt(2) == '1') {
                                assert y + 1 < maxY : "File: " + fileName + ", line no. past header: " + y + ", field no: " + i +
                                        ", content: \"" + s + "\"" + " - Third digit can't be '1' !!!";
                                graphCells[i][y].paths[2] = graphCells[i][y + 1];
                            } else if (s.charAt(2) == '0') {
                                graphCells[i][y].paths[2] = null;
                            } else
                                throw new NotALabyrinthFileException(
                                        "File: " + fileName + ", line no. past header: " + y + ", field no:" + i + ", Bad charackter(only '1' or '0')!!");

                            // check if fourth char is '1' or '0' if not throw exception
                            if (s.charAt(3) == '1') {
                                assert i > 0 : "File: " + fileName + ", line no. past header: " + y + ", field no: " + i +
                                        ", content: \"" + s + "\"" + " - Last digit can't be '1' !!!";
                                graphCells[i][y].paths[3] = graphCells[i - 1][y];
                            } else if (s.charAt(3) == '0') {
                                graphCells[i][y].paths[3] = null;
                            } else
                                throw new NotALabyrinthFileException(
                                        "File: " + fileName + ", line no. past header: " + y + ", field no:" + i + ", Bad charackter(only '1' or '0')!!");
                        }


                    } else {// not a backTracker

                        Kordy k;
                        for (int i = 0; i < line.length(); i += 2) {
                            if (line.substring(i, i + 2).equals("\u2588" + "\u2588")) {
                                walls[y][i / 2] = true;
                            } else if (line.substring(i, i + 2).equals("\u2591" + "\u2591")) walls[y][i / 2] = false;
                            else throw new NotALabyrinthFileException("File is not structured as a labyrinth file");
                        }

                    }
                    y++;
                }
            }
            if(algorithmType==4) {
                this.algorithmType = algorithmType;
                this.maxX = maxX;
                this.maxY = maxY;
                this.graphCells = graphCells;
                Data.windowX = 41 + Data.pixelsPerCell * maxX;
                Data.windowY = 55 + Data.pixelsPerCell * maxY;
            }
            else{
                this.algorithmType = algorithmType;
                this.maxX = maxX;
                this.maxY = maxY;
                this.walls = walls;
                Data.windowX = 80 + 10 * maxX;
                Data.windowY = 80 + 10 * maxY;
            }
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    /**
     * Loads a labyrinth from a .bmp file
     *
     * @param fileName String which is a path to a file we want to load
     * @throws IOException
     */
    public void importFromBMP(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] bytes;
        bytes = Files.readAllBytes(file.toPath());
        try {
            int width = ((bytes[19] << 8) | (bytes[18] & 0x00FF)) / 20;
            int height = ((bytes[23] << 8) | (bytes[22] & 0x00FF)) / 20;
            if (width <= 0 || height <= 0)
                throw new NotALabyrinthFileException("File is not structured as a labyrinth file");

            boolean[][] lab = new boolean[height][width];
            int l = 54;
            for (int i = height - 1; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    if (bytes[l + j * 20 * 3] == -1)
                        lab[i][j] = false;
                    else if (bytes[l + j * 20 * 3] == 0)
                        lab[i][j] = true;
                    else
                        throw new NotALabyrinthFileException("File is not structured as a labyrinth file");
                }
                l = (l + 3 * 20 * 20 * width);
            }

            lab[0][0] = true;
            lab[1][0] = true;
            lab[0][1] = true;
            lab[1][1] = false;

            lab[height - 1][width - 1] = true;
            lab[height - 2][width - 1] = true;
            lab[height - 1][width - 2] = true;
            lab[height - 2][width - 2] = false;
            maxY=height;
            maxX=width;
            walls = lab;

            this.algorithmType = -1;
            Data.windowX = 80 + 10 * maxX;
            Data.windowY = 80 + 10 * maxY;
        } catch (IOException ioe) {
            throw ioe;
        }
    }


    /**
     * Saves the current labyrinth to the file
     *
     * @param filePath String name of a file without any extensions (not a path - just a name)
     * @throws FileNotFoundException if the output file gets corrupted throws an exception
     */
    public void saveLabyrinth(String filePath) throws IOException {
        if (stringContainsIllegalChar(filePath))
            throw new NotAProperFileNameException("Filename contains illegal characters");
        PrintWriter outputFile = new PrintWriter(filePath);
        outputFile.println(maxX + " " + maxY+ " "+ this.algorithmType);
        if(this.algorithmType!=4) {
            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x < maxX; x++) {
                    if (walls[y][x]) outputFile.print("\u2588" + "\u2588");//walls
                    else outputFile.print("\u2591" + "\u2591");//paths
                }
                outputFile.println("");
            }
        }
        else{//this a backTracker
            int numNeighb = graphCells[0][0].paths.length;  // how many neighbours can a cell have
            for (int y = 0; y < maxY; y++) {    // traverse the labyrinth row by row, mind the order of 'x' and 'y'
                for (int x = 0; x < maxX; x++) {
                    // take reference to the neighbouring LabCell that this cell can visit:

                    for (LabCell lc : graphCells[x][y].paths) {
                        if ( lc==null ) {    // if it's null write '0'
                            outputFile.print("0");
                        } else {    // if it's not null write '1'
                            outputFile.print("1");//paths
                        }
                    }
                    outputFile.print(" ");  // every 4 chars are delimited with a space
                }
                outputFile.println();
            }
        }
        outputFile.close();
    }

    /**
     * Saves a labyrinth to a .bmp file
     * @param filePath to the file
     * @throws IOException
     */
    public void exportToBMP(String filePath) throws IOException {
        if (stringContainsIllegalChar(filePath))
            throw new NotAProperFileNameException("Filename contains illegal characters");
        BufferedImage bmp = new BufferedImage(maxX * 20, maxY * 20, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (walls[y][x])
                    for (int k = y * 20; k < y * 20 + 20; k++)
                        for (int l = x * 20; l < x * 20 + 20; l++)
                            bmp.setRGB(l, k, Color.BLACK.getRGB());
                else
                    for (int k = y * 20; k < y * 20 + 20; k++)
                        for (int l = x * 20; l < x * 20 + 20; l++)
                            bmp.setRGB(l, k, Color.WHITE.getRGB());
            }
        }

        /*
        start and finish
         */
        for (int y = 0; y < 40; y++)
            for (int x = 0; x < 40; x++)
                bmp.setRGB(x, y, Color.green.getRGB());

        for (int y = (maxY - 2) * 20; y < maxY * 20; y++)
            for (int x = (maxX - 2) * 20; x < maxX * 20; x++)
                bmp.setRGB(x, y, Color.RED.getRGB());


        try {
            RenderedImage rendImage = bmp;
            ImageIO.write(rendImage, "bmp", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param strNum String we want to check
     * @return true if strNum is a number, false otherwise
     */
    private boolean stringIsNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private boolean stringContainsIllegalChar(String str) {
        str = str.substring(2, str.length() - 4);
        if (str.contains("\\") || str.contains("/") ||
                str.contains("*") || str.contains(":") ||
                str.contains("?") || str.contains("\"") ||
                str.contains("<") || str.contains(">") ||
                str.contains("|")) return true;
        else return false;
    }

    /**
     * @return boolean[][] walls
     */
    public boolean[][] getWalls() {
        return walls;
    }

    /**
     * Turning right
     *
     * @param direction the direction the solver is currently facing
     * @return direction on the right of a given one
     */
    private int turnRight(int direction) {
        //0-up 1-right 2-down 3-left
        return (direction + 1) % 4;
    }

    /**
     * Turning left
     *
     * @param direction the direction the solver is currently facing
     * @return direction on the left of a given one
     */
    private int turnLeft(int direction) {
        //0-up 1-right 2-down 3-left
        if (direction == 0) return 3;
        else return (direction - 1) % 4;
    }

    /**
     * Printing any labyrinth to console
     *
     * @param walls walls of that labyrinth
     */
    public void printLabirytnth(boolean[][] walls) {
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (walls[x][y]) System.out.print("\u2588" + "\u2588");//walls
                else System.out.print("\u2591" + "\u2591");//paths
            }
            System.out.println("");
        }
    }

    /**
     * Solves the labyrinth in which the exit is next to the outside walls (or it's extensions)
     *
     * @return true if you it solved the labyrinth, false if it didn't
     */
    public boolean normalSolver() {


        boolean[][] path = new boolean[maxY][maxX];

        int direction = 2, x = 1, y = 1, endTimer = 0;
        path[x][y] = true;
        while (x != maxX - 2 || y != maxY - 2) {

            switch (direction) {
                case 0: {//up
                    if (!walls[y][x + 1]) {
                        x += 1;
                        path[y][x] = true;
                        direction = turnRight(direction);
                    } else if (!walls[y - 1][x]) {
                        y -= 1;
                        path[y][x] = true;
                    } else if (!walls[y][x - 1]) {
                        x -= 1;
                        path[y][x] = true;
                        direction = turnRight(turnRight(turnRight(direction)));
                    } else {
                        direction = turnRight(direction);
                        //path[x][y]=false;
                    }
                }
                break;
                case 1: {//right
                    if (!walls[y + 1][x]) {

                        y += 1;
                        path[y][x] = true;
                        direction = turnRight(direction);
                    } else if (!walls[y][x + 1]) {
                        x += 1;
                        path[y][x] = true;
                    } else if (!walls[y - 1][x]) {
                        y -= 1;
                        path[y][x] = true;
                        direction = turnRight(turnRight(turnRight(direction)));
                    } else {
                        direction = turnRight(direction);
                    }
                }
                break;
                case 2: {//down
                    if (!walls[y][x - 1]) {
                        x -= 1;
                        path[y][x] = true;
                        direction = turnRight(direction);
                    } else if (!walls[y + 1][x]) {
                        y += 1;
                        path[y][x] = true;
                    } else if (!walls[y][x + 1]) {
                        x += 1;
                        path[y][x] = true;
                        direction = turnRight(turnRight(turnRight(direction)));
                    } else {
                        direction = turnRight(direction);
                    }
                }
                break;
                case 3: {//left
                    if (!walls[y - 1][x]) {
                        y -= 1;
                        path[y][x] = true;
                        direction = turnRight(direction);
                    } else if (!walls[y][x - 1]) {
                        x -= 1;
                        path[y][x] = true;
                    } else if (!walls[y + 1][x]) {
                        y += 1;
                        path[y][x] = true;
                        direction = turnRight(turnRight(turnRight(direction)));
                    } else {
                        direction = turnRight(direction);
                    }
                }
                break;
            }
            endTimer++;
            if (endTimer > maxX * maxY * 2) {
                break;
            }
        }

        if (endTimer > maxX * maxY * 2) {
            this.path = path;
            return false;
        } else {
            this.path = correctionForNormalSolver(path);
            return true;
        }
    }

    /**
     * Correcting for the path finding algorithm
     *
     * @param walls walls of the labyrinth
     * @return boolean[][] representing the path taken to the destination
     */
    private boolean[][] correctionForNormalSolver(boolean[][] walls) {

        boolean[][] path = new boolean[maxY][maxX];
        for (int i = 0; i < maxX; i++)
            for (int j = 0; j < maxY; j++) {
                walls[j][i] = !walls[j][i];
            }
        for (int i = 0; i < maxX; i++)
            for (int j = 0; j < maxY; j++) {
                path[j][i] = false;
            }
        int direction = 2, x = 1, y = 1, endTimer = 0;
        path[y][x] = true;
        while (x != maxX - 2 || y != maxY - 2) {

            switch (direction) {
                case 0: {//up
                    if (!walls[y][x - 1]) {
                        x -= 1;
                        path[y][x] = true;
                        direction = turnLeft(direction);
                    } else if (!walls[y - 1][x]) {
                        y -= 1;
                        path[y][x] = true;
                    } else if (!walls[y][x + 1]) {
                        x += 1;
                        path[y][x] = true;
                        direction = turnLeft(turnLeft(turnLeft(direction)));
                    } else {
                        direction = turnLeft(direction);
                    }
                }
                break;
                case 1: {//Right
                    if (!walls[y - 1][x]) {
                        y -= 1;
                        path[y][x] = true;
                        direction = turnLeft(direction);
                    } else if (!walls[y][x + 1]) {
                        x += 1;
                        path[y][x] = true;
                    } else if (!walls[y + 1][x]) {
                        y += 1;
                        path[y][x] = true;
                        direction = turnLeft(turnLeft(turnLeft(direction)));
                    } else {
                        direction = turnLeft(direction);
                    }
                }
                break;
                case 2: {//down
                    if (!walls[y][x + 1]) {
                        x += 1;
                        path[y][x] = true;
                        direction = turnLeft(direction);
                    } else if (!walls[y + 1][x]) {
                        y += 1;
                        path[y][x] = true;
                    } else if (!walls[y][x - 1]) {
                        x -= 1;
                        path[y][x] = true;
                        direction = turnLeft(turnLeft(turnLeft(direction)));
                    } else {
                        direction = turnLeft(direction);
                    }
                }
                break;
                case 3: {//left
                    if (!walls[y + 1][x]) {
                        y += 1;
                        path[y][x] = true;
                        direction = turnLeft(direction);
                    } else if (!walls[y][x - 1]) {
                        x -= 1;
                        path[y][x] = true;
                    } else if (!walls[y - 1][x]) {
                        y -= 1;
                        path[y][x] = true;
                        direction = turnLeft(turnLeft(turnLeft(direction)));
                    } else {
                        direction = turnLeft(direction);
                    }
                }
                break;
            }
            endTimer++;
            if (endTimer > maxX * maxY * 2) {
                break;
            }
        }
        return path;
    }
}