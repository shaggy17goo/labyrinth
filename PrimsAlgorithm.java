import java.util.ArrayList;
import java.util.Random;

public class PrimsAlgorithm extends Algorithm {
    int maxY;
    int maxX;
    /**
     * Attributes of a recursive division labyrinth
     */
    private boolean[][] walls, visited;
    private Random r = new Random();
    private ArrayList<Kordy> wallsList = new ArrayList<>();

    public boolean[][] getPrimsAlgorithmWalls(int maxY, int maxX) {
        this.maxX = maxX;
        this.maxY = maxY;
        walls = new boolean[this.maxY][this.maxX];
        visited = new boolean[this.maxY][this.maxX];
        fullWalls();
        algorithm(1, 1);
        return walls;
    }

    /**
     * Makes a maze using Prim's algorithm
     *
     * @param x of the starting cell
     * @param y of the starting cell
     */
    private void algorithm(int x, int y) {

        fullWalls();//setting all the walls to true
        walls[y][x] = false;//making the (x,y) cell a start of our labyrinth
        visited[y][x] = true;
        addNeighboringWalls(x, y);
        int random = 0;
        while (!wallsList.isEmpty()) {
            random = r.nextInt(wallsList.size());
            x = wallsList.get(random).getX();
            y = wallsList.get(random).getY();
            if (canBeRemoved(x, y)) {
                walls[y][x] = false;
                for (int i = 0; i < 4; i++) {
                    if (!walls[neighbors(x, y)[i].getY()][neighbors(x, y)[i].getX()] &&
                            !visited[neighbors(x, y)[i].getY()][neighbors(x, y)[i].getX()]) {
                        x = neighbors(x, y)[i].getX();
                        y = neighbors(x, y)[i].getY();
                        walls[y][x] = false;
                        visited[y][x] = true;
                        break;

                    }
                }
                addNeighboringWalls(x, y);
                //direction=direction(x,y,)

            }
            wallsList.remove(random);
        }
    }

    /**
     * Making the grid full of walls
     */
    private void fullWalls() {
        for (int y = 0; y < maxY; y += 2)
            for (int x = 0; x < maxX; x++) {
                walls[y][x] = true;
            }
        for (int y = 0; y < maxY; y++)
            for (int x = 0; x < maxX; x += 2) {
                walls[y][x] = true;
            }
        for (int x = 0; x < maxX; x++) {
            walls[0][x] = true;
            walls[maxY - 1][x] = true;
        }
        for (int y = 0; y < maxY; y++) {
            walls[y][0] = true;
            walls[y][maxX - 1] = true;
        }
        for (int y = 0; y < maxY; y++)
            for (int x = 0; x < maxX; x++) {
                visited[y][x] = false;
            }
    }

    /**
     * @param x of the cell
     * @param y of the cell
     * @return Kordy[] containing neighboring fields
     */
    private Kordy[] neighbors(int x, int y) {
        Kordy[] result = {
                new Kordy(x - 1, y),
                new Kordy(x, y + 1),
                new Kordy(x + 1, y),
                new Kordy(x, y - 1)
        };

        return result;

    }

    /**
     * @param x of the cell
     * @param y of the cell
     * @return amount of neighboring paths
     */
    private int howManyNeighbors(int x, int y) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            if (neighbors(x, y)[i].getY() > 0 && neighbors(x, y)[i].getX() > 0 &&
                    !walls[neighbors(x, y)[i].getY()][neighbors(x, y)[i].getX()] &&
                    visited[neighbors(x, y)[i].getY()][neighbors(x, y)[i].getX()])
                result++;
        }
        return result;
    }

    /**
     * @param x of the cell
     * @param y of the cell
     * @return true if the wall can be removed, false otherwise
     */
    boolean canBeRemoved(int x, int y) {
        boolean result = false;
        if (!walls[y][x]) result = false;
        else if (howManyNeighbors(x, y) == 1) result = true;

        return result;
    }

    /**
     * Adds neighboring walls to the list
     *
     * @param x of the cell
     * @param y of the cell
     */
    private void addNeighboringWalls(int x, int y) {
        for (int i = 0; i < 4; i++) {
            if (neighbors(x, y)[i].getX() > 0 && neighbors(x, y)[i].getY() > 0 && neighbors(x, y)[i].getX() < maxX - 1 && neighbors(x, y)[i].getY() < maxY - 1 && walls[neighbors(x, y)[i].getY()][neighbors(x, y)[i].getX()])
                wallsList.add(neighbors(x, y)[i]);
        }
    }


}
