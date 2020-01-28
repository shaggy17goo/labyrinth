import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class DepthFirstSearch {
    private int maxY;
    private int maxX;
    private Stack<Kordy> stack = new Stack();
    private Random rGenerator = new Random();

    //final version of tab to return
    private boolean[][] walls = new boolean[maxY][maxX];

    //tab to generate/work on
    boolean workingTab[][] = new boolean[maxY + 6][maxX + 6];

    // list includes already checked directions for cell (LEFT/RIGHT/UP/DOWN)
    private ArrayList<EnumDirection.Direction> randomList = new ArrayList<>(4);

    private static class EnumDirection {
        public enum Direction {
            UP, DOWN, LEFT, RIGHT;
        }
    }

    /**
     * @param maxY
     * @param maxX
     * @return complete maze
     */
    public boolean[][] getDepthFirstSearchWalls(int maxY, int maxX) {
        this.maxX = maxX;
        this.maxY = maxY;
        workingTab = new boolean[maxY + 6][maxX + 6];
        walls = new boolean[maxY][maxX];
        fillTab();
        generatePath((maxY + 6) / 2, (maxX + 6) / 2);
        mazeCorrection();
        copy();
        return walls;
    }

    /**
     * fill workingTab with true, make a frame around the workingTab of width 3 with false
     */
    private void fillTab() {
        for (int j = 0; j < maxX + 6; j++) {        // up and down edge
            workingTab[0][j] = false;
            workingTab[1][j] = false;
            workingTab[2][j] = false;
            workingTab[maxY + 3][j] = false;
            workingTab[maxY + 4][j] = false;
            workingTab[maxY + 5][j] = false;
        }

        for (int i = 3; i < maxY + 3; i++) {        // left and right edge
            workingTab[i][0] = false;
            workingTab[i][1] = false;
            workingTab[i][2] = false;
            workingTab[i][maxX + 3] = false;
            workingTab[i][maxX + 4] = false;
            workingTab[i][maxX + 5] = false;
            for (int j = 3; j < maxX + 3; j++) {     // mid
                workingTab[i][j] = true;
            }
        }
    }

    /**
     * generate path, modify workingTab
     * @param y of the cell
     * @param x of the cell
     */
    private void generatePath(int y, int x) {
        clearRandomLists();
        while (randomList.size() < 4) {     //while all direction not check
            EnumDirection.Direction next = randomize();
            if (isPossible(y, x, next)) {
                setPass(y, x, next);
                y = newY(y, next);
                x = newX(x, next);
                generatePath(y, x);
            } else
                addToRandomList(next);
        }
        if (stack.size() >= 2) {
            Kordy top = this.stack.pop();
            generatePath(top.getY(), top.getX());
        }
    }

    /**
     * @return one random direction (U-up, D-down, L-left, R-right)
     */
    private EnumDirection.Direction randomize() {
        int random;
        random = rGenerator.nextInt(4);
        if (random == 0) return EnumDirection.Direction.UP;
        if (random == 1) return EnumDirection.Direction.DOWN;
        if (random == 2) return EnumDirection.Direction.LEFT;
        return EnumDirection.Direction.RIGHT;
    }


    /**
     * @param y of the cell
     * @param x of the cell
     * @param nextCell
     * @return true if move is possible, false otherwise
     */
    private boolean isPossible(int y, int x, EnumDirection.Direction nextCell) {
        EnumDirection.Direction next = nextCell;

        //checking square 3x3 under the current cell, if all walls - setPass possible, return true
        if (next.equals(EnumDirection.Direction.DOWN)) {
            if (workingTab[y + 3][x] && workingTab[y + 2][x] && workingTab[y + 1][x] && (workingTab[y + 1][x + 1] && workingTab[y + 1][x - 1])
                    && (workingTab[y + 3][x + 1] && workingTab[y + 3][x - 1]) && (workingTab[y + 2][x + 1] && workingTab[y + 2][x - 1]))
                return true;
        }

        //checking square 3x3 over the current cell, if all walls - setPass possible, return true
        if (next.equals(EnumDirection.Direction.UP)) {
            if (workingTab[y - 3][x] && workingTab[y - 2][x] && workingTab[y - 1][x] && (workingTab[y - 1][x + 1] && workingTab[y - 1][x - 1])
                    && (workingTab[y - 3][x + 1] && workingTab[y - 3][x - 1]) && (workingTab[y - 2][x + 1] && workingTab[y - 2][x - 1]))
                return true;
        }

        //checking square 3x3 on the left current cell, if all walls - setPass possible, return true
        if (next.equals(EnumDirection.Direction.LEFT)) {
            if (workingTab[y][x - 3] && workingTab[y][x - 2] && workingTab[y][x - 1] && (workingTab[y + 1][x - 1] && workingTab[y - 1][x - 1])
                    && (workingTab[y + 1][x - 3] && workingTab[y - 1][x - 3]) && (workingTab[y + 1][x - 2] && workingTab[y - 1][x - 2]))
                return true;
        }
        //checking square 3x3 on the right current cell, if all walls - setPass possible, return true
        if (next.equals(EnumDirection.Direction.RIGHT)) {
            if (workingTab[y][x + 3] && workingTab[y][x + 2] && workingTab[y][x + 1] && (workingTab[y - 1][x + 1] && workingTab[y + 1][x - 1])
                    && (workingTab[y - 1][x + 3] && workingTab[y + 1][x + 3]) && (workingTab[y - 1][x + 2] && workingTab[y + 1][x + 2]))
                return true;
        }
        return false;
    }


    /**
     * make a pass in the random picked and checked direction (2 cells in one move)
     * add path's cells to stack
     * @param y of the cell
     * @param x of the cell
     * @param next
     */
    private void setPass(int y, int x, EnumDirection.Direction next) {
        if (next.equals(EnumDirection.Direction.UP)) {
            workingTab[y - 1][x] = false;
            workingTab[y - 2][x] = false;
            this.stack.push(new Kordy(x, y - 1));
            this.stack.push(new Kordy(x, y - 2));
        }

        if (next.equals(EnumDirection.Direction.DOWN)) {
            workingTab[y + 1][x] = false;
            workingTab[y + 2][x] = false;
            this.stack.push(new Kordy(x, y + 1));
            this.stack.push(new Kordy(x, y + 2));
        }

        if (next.equals(EnumDirection.Direction.LEFT)) {
            workingTab[y][x - 1] = false;
            workingTab[y][x - 2] = false;
            this.stack.push(new Kordy(x - 1, y));
            this.stack.push(new Kordy(x - 2, y));
        }

        if (next.equals(EnumDirection.Direction.RIGHT)) {
            workingTab[y][x + 1] = false;
            workingTab[y][x + 2] = false;
            this.stack.push(new Kordy(x + 1, y));
            this.stack.push(new Kordy(x + 2, y));
        }
    }

    /**
     * @param y of the cell
     * @param next
     * @return newY if moved up or down
     */
    private int newY(int y, EnumDirection.Direction next) {
        if (next.equals(EnumDirection.Direction.UP))
            return (y - 2);     //2 cells over
        if (next.equals(EnumDirection.Direction.DOWN))
            return (y + 2);     //2 cell under
        else
            return y;
    }

    /**
     * @param x of the cell
     * @param next
     * @returnn newX if moved left or right
     */
    private int newX(int x, EnumDirection.Direction next) {
        if (next.equals(EnumDirection.Direction.LEFT))
            return (x - 2);     //2 cells to the left
        if (next.equals(EnumDirection.Direction.RIGHT))
            return (x + 2);     //2 cells to the right
        else
            return x;
    }

    /**
     * add checked direction to the list
     * @param direction
     */
    private void addToRandomList(EnumDirection.Direction direction) {
        boolean add = true;
        for (int i = 0; i < randomList.size(); i++) {
            if (randomList.get(i) == direction)
                add = false;
        }
        if (add)
            randomList.add(direction);
    }

    /**
     * clear randomList
     */
    private void clearRandomLists() {
        if (randomList.size() > 0) {
            randomList.remove(0);
            clearRandomLists();
        }
    }

    /**
     * @param y of the cell
     * @param x of the cell
     * @return true if a 2x2 square contains only walls, false otherwise
     */
    private boolean fullSetting(int y, int x) {
        if (workingTab[y][x] && workingTab[y + 1][x] && workingTab[y][x + 1] && workingTab[y + 1][x + 1])
            return true;
        else
            return false;
    }

    /**
     * @param y of the cell
     * @param x of the cell
     * @return true if a 2x2 square contains only paths, false otherwise
     */
    private boolean emptySetting(int y, int x) {
        if (!workingTab[y][x] && !workingTab[y + 1][x] && !workingTab[y][x + 1] && !workingTab[y + 1][x + 1])
            return true;
        else
            return false;
    }

    /**
     * Correcting double walls, and double passes
     */
    private void mazeCorrection() {
        for (int i = 3; i < maxY + 1; i++) {
            for (int j = 3; j < maxX + 1; j++) {
                if (fullSetting(i, j)) {
                    workingTab[i + 1][j + 1] = false;
                }
                if (emptySetting(i, j)) {
                    workingTab[i][j] = true;
                }
            }
        }
    }

    /**
     * copy the maze from the workingTab to walls
     */
    private void copy() {
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                walls[i][j] = workingTab[i + 3][j + 3]; //+3 because working tab has a frame
            }
        }
    }
}
