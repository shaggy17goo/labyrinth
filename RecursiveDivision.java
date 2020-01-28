import java.util.Random;

public class RecursiveDivision {
    /**
     * Attributes of a recursive division labyrinth
     */
    private boolean[][] walls;
    int maxY;
    int maxX;
    private Random r;

    /**
     * @param maxY height of the labyrinth
     * @param maxX length of the labyrinth
     * @return boolean[][] representing walls of this labyrinth
     */
    public boolean[][] getRecursiveDivisionWalls(int maxY, int maxX) {
        this.maxX = maxX;
        this.maxY = maxY;
        walls = new boolean[maxY][maxX];
        zeroing();
        divide(0, maxY, 0, maxX);
        poprawki();
        return walls;
    }

    /**
     * Assigns false to the whole table
     */
    private void zeroing() {
        r = new Random();
        //zeroing
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                walls[i][j] = false;
            }
        }
    }

    /**
     * Recursively divides given areas with a wall and picks random wholes in them
     *
     * @param y1 y start coordinate
     * @param y2 y end  coordinate
     * @param x1 x start  coordinate
     * @param x2 x end  coordinate
     */
    private void divide(int y1, int y2, int x1, int x2) {

        int x = 0, y = 0;
        if (x2 - x1 > 3 && y2 - y1 > 3) {
            y = y1 + (y2 - y1) / 2;
            x = x1 + (x2 - x1) / 2;


            for (int i = x1; i < x2; i++)
                walls[i][y] = true;

            for (int i = 0; i < includes(length(x1, x2)); i++)//creating holes in the walls
                walls[getNewRandom(x1, x2, x)][y] = false;


            for (int i = y1; i < y2; i++)
                walls[x][i] = true;

            for (int i = 0; i < includes(length(y1, y2)); i++)//creating holes in the walls
                walls[x][getNewRandom(y1, y2, y)] = false;


            divide(y1, y, x1, x);//upper left
            divide(y, y2, x, x2);//lower right
            divide(y, y2, x1, x);//lower left
            divide(y1, y, x, x2);//ypper right

        }

    }

    /**
     * tries to make the labyrinth better
     * Turns the outside border to walls
     * Delete wall when one empty cell is rung by walls
     * Add wall when one wall is rung by empty cells
     */

    private void poprawki() {

        //Turns the outside border to walls
        for (int i = 0; i < maxY; i++) {
            walls[0][i] = true;
            walls[maxY - 1][i] = true;
        }
        for (int i = 0; i < maxX; i++) {
            walls[i][0] = true;
            walls[i][maxX - 1] = true;
        }


        //Delete wall when one empty cell is rung by walls
        for (int i = 1; i < maxY - 1; i++) {
            for (int j = 1; j < maxX - 1; j++) {
                if (walls[i - 1][j] && walls[i + 1][j] && walls[i][j + 1] && walls[i][j - 1]) { // if one empty cell is ring by walls
                    int random;
                    while (true) { //new random if current is wrong(delete wall in outside border
                        random = r.nextInt(4); //0- up, 1- down, 2-left,3-right
                        if (random == 0 && i != 1)
                            break;
                        if (random == 1 && i != maxY - 2)
                            break;
                        if (random == 2 && j != 1)
                            break;
                        if (random == 3 && j != maxX - 2)
                            break;
                    }
                    switch (random) { //delete a wall
                        case 0:
                            walls[i - 1][j] = false;
                            break;
                        case 1:
                            walls[i + 1][j] = false;
                            break;
                        case 2:
                            walls[i][j - 1] = false;
                            break;
                        case 3:
                            walls[i][j + 1] = false;
                            break;
                    }
                }
            }
        }


        //Add wall when one wall is rung by empty cells
        for (int i = 1; i < maxX - 1; i++) {
            for (int j = 1; j < maxY - 1; j++) {
                if (walls[i][j] && !walls[i - 1][j] && !walls[i + 1][j] && !walls[i][j + 1] && !walls[i][j - 1]) { //if one wall is ring by empty cells
                    int random;
                    random = r.nextInt(4); //0- up, 1- down, 2-left,3-right
                    switch (random) { //made a wall
                        case 0:
                            walls[i - 1][j] = true;
                            break;
                        case 1:
                            walls[i + 1][j] = true;
                            break;
                        case 2:
                            walls[i][j - 1] = true;
                            break;
                        case 3:
                            walls[i][j + 1] = true;
                            break;
                    }
                }
            }
        }

    }


    /**
     * @param min       low end of the range
     * @param max       high end of the range
     * @param exception the number we don't want to get
     * @return a random number in range (min,max) and without it being the exception number
     */
    private int getNewRandom(int min, int max, int exception) {//guarentee not to get a problematic result
        boolean exceptions = true;
        int random = 0;
        while (exceptions && max - min > 0) {
            random = r.nextInt(max - min) + min;
            if (random != min && random != max && random != exception) exceptions = false;

        }
        return random;
    }

    /**
     * @param a1 start point
     * @param a2 end point
     * @return the distance between two given points
     */
    private int length(int a1, int a2) {
        return a2 - a1;
    }

    /**
     * Dictates how many holes should be made in the wall of a given length
     *
     * @param howLong the length of the wall
     * @return amount of holes in that wall
     */
    private int includes(int howLong) {//how many holes in the walls
        if (howLong > 60) return 8;
        if (howLong > 30 && howLong <= 60) return 5;
        if (howLong > 15 && howLong <= 30) return 5;
        if (howLong > 5 && howLong <= 15) return 4;
        else return 1;
    }

}