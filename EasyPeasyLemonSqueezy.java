import java.util.Random;

class EasyPeasyLemonSqueezy {

    private int maxX;
    private int maxY;
    private int mistakes =1;
    private Random rGenerator = new Random();
    private boolean[][] workingTab;
    private boolean[][] walls;



    /**
     * @param maxY
     * @param maxX
     * @return complete maze
     */
    public boolean[][] getEasyPeasyLemonSqueezyWalls(int maxY, int maxX) {
        this.maxX = maxX;
        this.maxY = maxY;
        workingTab = new boolean[this.maxY-2][this.maxX-2];
        walls = new boolean[this.maxY][this.maxX];
        fillTab();
        generate();
        copy();
        return walls;
    }


    /**
     * fill workingTab and walls
     */
    private void fillTab() {
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                walls[i][j] = true;
            }
        }

        for (int i = 0; i < maxY-2; i++) {
            for (int j = 0; j < maxX-2; j++) {
                workingTab[i][j] = true;
            }
        }
    }


    /**
     * generate path, modify workingTab
     */
    private void generate() {
        while (mistakes > 0) {
            mistakes = 0;
            for (int i = 0; i < maxY - 3; i++) {
                for (int j = 0; j < maxX - 3; j++) {
                    fullSetting(i, j);
                    emptySetting(i, j);
                    bias(i, j);
                }
            }
            generate();
        };
        workingTab[0][0] = false;
        workingTab[maxY - 3][maxX - 3] = false;
    }


    /**
     * @param y of the cell
     * @param x of the cell
     * @return true if a 2x2 square contains only walls, false otherwise
     */
    private void fullSetting(int y, int x) {
        if (workingTab[y][x] && workingTab[y + 1][x] && workingTab[y][x + 1] && workingTab[y + 1][x + 1]) {
            int usun = rGenerator.nextInt(4);
            mistakes++;
            switch (usun) {
                case 0:
                    workingTab[y + 1][x + 1] = false;
                    break;

                case 1:
                    workingTab[y][x] = false;
                    break;

                case 2:
                    workingTab[y + 1][x] = false;
                    break;

                case 3:
                    workingTab[y][x + 1] = false;
                    break;

            }
        }
    }



    /**
     * @param y of the cell
     * @param x of the cell
     * @return true if a 2x2 square contains only paths, false otherwise
     */
    private void emptySetting(int y, int x) {
        if (!workingTab[y][x] && !workingTab[y + 1][x] && !workingTab[y][x + 1] && !workingTab[y + 1][x + 1]) {
            int dodaj = rGenerator.nextInt(4);
            mistakes++;
            switch (dodaj) {
                case 0:
                    workingTab[y + 1][x + 1] = true;
                    break;

                case 1:
                    workingTab[y][x] = true;
                    break;

                case 2:
                    workingTab[y + 1][x] = true;
                    break;

                case 3:
                    workingTab[y][x + 1] = true;
                    break;
            }
        }
    }


    /**
     * add wall if two walls connect by corner
     * @param y of the cell
     * @param x of the cell
     */
    private void bias(int y, int x) {
        if ((workingTab[y][x] && !workingTab[y + 1][x] && !workingTab[y][x + 1] && workingTab[y + 1][x + 1])
                || (!workingTab[y][x] && workingTab[y + 1][x] && workingTab[y][x + 1] && !workingTab[y + 1][x + 1])) {
            mistakes++;
            int add = rGenerator.nextInt(4);
            switch (add) {
                case 0:
                    workingTab[y + 1][x] = false;
                    break;

                case 1:
                    workingTab[y][x + 1] = false;
                    break;

                case 2:
                    workingTab[y + 1][x + 1] = false;
                    break;

                case 3:
                    workingTab[y][x] = false;
                    break;
            }
        }
    }

    /**
     * copy maze from the workingTab to walls
     */
    private void copy() {
        for (int i = 0; i < maxY-2; i++) {
            for (int j = 0; j < maxX-2; j++) {
                walls[i+1][j+1] = workingTab[i][j]; //+1 because workingTab is smaller
            }
        }
    }
}