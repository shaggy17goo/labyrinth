import java.util.Random;
import java.util.Stack;
/**
 * @author mpanka
 * To jest implementacja Recursive Back Tracker zwana też DFS.
 * Generuje idealne labirynty, tzn wszystkie komórki są połączone ścieżką.
 * Spójne w sensie teorii grafów.
 */
public class RecursiveBackTracker {
    /**
     * Domyślnie labirynt prostokątny
     */
    public RecursiveBackTracker() {
        sides = 4;
        width = height = 0; // Chcemy by było widać, że labirynt przed utworzeniem ma rozmiary 0x0
        numCellsVisited = solutionPathLength = 0;
    }

    public RecursiveBackTracker(int n) {
        sides = n;
        width = height = 0; // Chcemy by było widać, że labirynt przed utworzeniem ma rozmiary 0x0
    }

    /**
     * Tylu sąsiadów może mieć komórka. Zwykle 4 ale może być np. 6 dla plastra miodu lub 3D.
     * W tym momencie nie wiem jak pracować z innymi @param n niż 4 ale jest potencjał na przyszłość.
    */
    private int sides, // liczba sąsiadów, liczba możliwych kierunków wyjścia z komórki
                width,
                height,
                numCellsVisited,    // liczba komórek sprawdzonych podczas poszukiwania rozwiązania
                solutionPathLength; // długość znalezioinego rozwiązania

    private LabCell[][] labir;

    /**
     * @returns LabCell[][] labir
     */
    public LabCell[][] generate(int maxX, int maxY) {

        if (sides!=4) return null; // Na tę chwilę tylko prostokątne.

        width = maxX;
        height = maxY;
        numCellsVisited = solutionPathLength = 0;

        Random r = new Random();
        Stack<LabCell> cellStack = new Stack<LabCell>();
        LabCell prevCell,   // poprzednia komórka
                currentCell;    // bierząca komórka
        int nextDirection;

        fillGrid(maxX, maxY);   //

        prevCell = currentCell = labir[r.nextInt(maxX)][r.nextInt(maxY)];  // Zaczynamy w losowej komórce
        currentCell.setVisited(true);   // oznaczamy, że tu już byliśmy.
        cellStack.push(currentCell);    // wrzucamy bierzącą komórkę na stos.

        /**
         * Tu następuje właściwa generacja labiryntu.
         * Dopóki jest gdzie się cofnąć, to iterujemy. Jeśli nie ma gdzie się cofnąć,
         * tzn że odwiedziliśmy wszystkie komórki i kończymy.
         */
        while (!cellStack.empty()) {
            do {
                if (cellStack.isEmpty()) break;
                currentCell = cellStack.pop();  // bierzemy komórkę ze stosu
            } while (currentCell.hasAllNeighboursVisited());    // taką, która ma jakichś sąsiadów do odwiedzenia
            while (!currentCell.hasAllNeighboursVisited()) {    // wiadomo, że jakiegoś sasiada można odwiedzić
                do {    // FIXME: pętla nieskończona gdy wszyscy odwiedzeni
                    nextDirection = r.nextInt(sides);   // losujemy jeden z możliwych kierunków
                } while (!currentCell.canBoreInto(nextDirection));   // aż w tym kierunku można iść

                cellStack.push(currentCell);    // wrzucamy bierzącą komórkę na stos.
                prevCell = currentCell; // poprzednia staje się bierzącą
                currentCell = currentCell.getNeighbours()[nextDirection];   // bierząca przesuwa się do przodu
                prevCell.makePass(currentCell, nextDirection); // otwieramy drogę pomiędzy tymi komórkami
                currentCell.setVisited(true);   // Zaznaczamy, że już tu byliśmy
            }
        }

        return  labir;
    }

    /**
     * Domyślnie przechodzimy z narożnika do narożnika
     * @return
     */
    public boolean solve() {
        if (height==0 && width==0) return false;
        return solve(labir[0][0], labir[height-1][width-1]);
    }

    public boolean solve(LabCell startCell, LabCell endCell) {
        if (startCell==null || endCell==null)   return false;
        if (startCell.equals(endCell)) {
            solutionPathLength = 0;
            numCellsVisited = 1;
            return true;
        }

        boolean solved = false;
        Random r = new Random();
        Stack<LabCell> cellStack = new Stack<>();
        LabCell currentCell = startCell;
        int nextDirection;

        resetVisited();
        solutionPathLength = 0;
        numCellsVisited = 0;

        currentCell.setVisited(true);
        currentCell.setMode(LabCellMode.IS_ON_PATH);
        cellStack.push(currentCell);

        while ( !cellStack.empty() && !solved ) {

            do {
                if (cellStack.isEmpty()) break;
                currentCell.setMode(LabCellMode.FLOODED);
                currentCell = cellStack.pop();  // bierzemy komórkę ze stosu
            } while (currentCell.hasAllPathsVisited());    // taką, z której jest jakieś wyjście

            currentCell.setVisited(true);
            currentCell.setMode(LabCellMode.IS_ON_PATH);

            while ( !currentCell.hasAllPathsVisited() && !solved ) {    // wiadomo, że którąś ścieżką  można iść
                do {
                    nextDirection = r.nextInt(sides);   // losujemy jeden z możliwych kierunków
                } while (!currentCell.canFollowPath(nextDirection));   // aż w tym kierunku można iść

                cellStack.push(currentCell);    // wrzucamy bierzącą komórkę na stos.
                currentCell = currentCell.paths[nextDirection];   // bierząca przesuwa się do przodu
                if (currentCell.equals(endCell)) {
                    solved = true;
                    ++numCellsVisited;
                    break;  // wychodzimy z while()
                }
                currentCell.setVisited(true);
                currentCell.setMode(LabCellMode.IS_ON_PATH);
                ++numCellsVisited;
            }
        }

        startCell.setMode(LabCellMode.START);
        endCell.setMode(LabCellMode.END);
        solutionPathLength = cellStack.size();

        return solved;

    }

    public boolean solveRandom() {
        /**
         * Losujemy start i metę i znajdujemy drogę, lub nie.
         */
        Random r = new Random();
        return solve( labir[r.nextInt(width-1)][r.nextInt(height-1)],
                labir[r.nextInt(width-1)][r.nextInt(height-1)] );
    }

    private void fillGrid(int width, int height) {
/**
 * Tworzymy tablicę wszystkich komórek.
 * Łączymy sąsiadów referencjami.
 */
        labir = new LabCell[width][height];    // Alokujemy tablicę na wszystkie komórki.
        LabCell currentCell;

        for (int x=0; x<width; ++x)  // przechodzimy przez całą tablicę wiersz po wierszu
            for (int y=0; y<height; ++y) {    // kolumna po kolumnie
                labir[x][y] = new LabCell(sides);  // robimy komórki
            }
        for (int x=0; x<width; ++x)
            for (int y=0; y<height; ++y) {
//                currentCell = labir[y][x] = new LabCell(sides);  // robimy komórki
                currentCell = labir[x][y];
                if (y>0) {  // jeśli nie jesteśmy już w pierwszym wierszu, to istnieje sąsiad u góry(top)
                    currentCell.getNeighbours()[0] = labir[x][y-1]; // wskazujemy na górnego sąsiada
                    labir[x][y-1].getNeighbours()[2] = currentCell; // dla górnego sąsiada jesteśmy sąsiadem dolnym.
                }
                if (y<height-1) { // jeśli nie jesteśmy jeszcze w ostatnim wierszu
                    currentCell.getNeighbours()[2] = labir[x][y+1]; // wskazujemy na dolnego sąsiada
                    labir[x][y+1].getNeighbours()[0] = currentCell; // dla dolnego sąsiada jesteśmy sąsiadem górnym.
                }
                if (x>0) {  // jeśli nie jestesmy już w pierwszej kolumnie, to istnieje sąsiad lewy
                    currentCell.getNeighbours()[3] = labir[x-1][y];
                    labir[x-1][y].getNeighbours()[1] = currentCell;
                }
                if (x<width-1) {   // jesli nie jesteśmy jeszcze w ostatniej kolumnie, to istnieje sąsiad prawy
                    currentCell.getNeighbours()[1] = labir[x+1][y];
                    labir[x+1][y].getNeighbours()[3] = currentCell;
                }
            }
    }


    public void resetVisited() {
        /**
         * Resetujemy pole visited w każdej komórce
         */
        for (int x=0; x<width; ++x)
            for (int y=0; y<height; ++y) {
                labir[x][y].setVisited(false);
                labir[x][y].setMode(LabCellMode.NOT_VISITED);
            }
    }


    public LabCell getCell(int x, int y) { return labir[x][y]; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getNumCellsVisited() { return numCellsVisited; }
    public int getSolutionPathLength() { return solutionPathLength; }
}
