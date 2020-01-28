import java.util.Stack;

/**
 * Pojedyncza komórka labiryntu.
 * @author mpanka
 * Wszystkie komórki będą docelowo przechowywane w tablicy 2(n) wymiarowej
 * Każda komórka ma referencję na sąsiada do którego można przejść. Tzn że jest przejście, nie ma ściany i również
 * jest referencja zwrotna w tej drugiej komórce.
 * Są też referencje na wszystkich sąsiadów służące do poruszania się po strukturze bez odwoływania się do indeksów
 * tablicy. Te referencje nie mają nic wspólnego z tym czy jest ściana czy nie.
 * Przechowywanie komórek w tablicy jest istotne dla metod rysujących - bo są znane indeksy.
 */

public class LabCell {

    // 0 - top
    // 1 - right
    // 2 - bottom
    // 3 - left
    LabCell(int k) {
        sides = k;
        neighbours = new LabCell[k];    // sąsiedzi
        paths = new LabCell[k]; // przejścia do sąsiadów
        for (int i=0; i<sides; ++i) {
            neighbours[i] = null;
            paths[i] = null;
        }
        visited = false;
        mode = LabCellMode.NOT_VISITED;
    }

    private int sides;  // Tyle boków ma komórka;
    public LabCell[]   neighbours, // Ref. używana przy generowaniu lab. w celu poruszania się po siatce.
                        paths;  // Ref. służąca do wyznaczania dróg.
    private boolean visited;
    private LabCellMode mode;


    /**
     * Robimy przejście z tej komórki w kierunku @param dir, tzn dowiązujemy wskaźniki na sąsiada.
     * Najpierw dowiązujemy do sąsiada, a potem od sąsiada, do siebie.
     * Indeks wskazujący na samego siebie dla sąsiada zanjdujemy tak: (dir+sides/2)%sides. działa dla 4 i 6
     */
    public void makePass(LabCell c, int dir) {
        paths[dir] = c;
        c.paths[(dir+sides/2)%4] = this;
    }

    public boolean hasAllNeighboursVisited() {
        for (int i=0; i<sides; ++i) {
            if (neighbours[i] != null && !neighbours[i].isVisited()) return false;
        }
        return true;
    }

    public boolean hasAllPathsVisited() {
        for (int i=0; i<sides; ++i) {
            if (paths[i] != null && !paths[i].isVisited()) return false;
        }
        return true;
    }

    public boolean canBoreInto(int dir) {   // Można wiercić w kierunku dir. Podczas tworzenia labiryntu
        dir %= sides;
        return (neighbours[dir] != null) && !neighbours[dir].visited;
    }

    public boolean canFollowPath(int dir) {   // Można przejść w kierunku dir. Podczas znajdowania rozwiązania
        dir %= sides;
        return (paths[dir] != null) && !paths[dir].visited;
    }

    /**
     * Równość dwóch komórek labiryntu oznacza, że leżą one w tym samym miejscu w tablicy.
     * Niestety nie przechowujemy w komórce jej współrzędnych, dlatego musimy przejść po sąsiadach w górę i w lewo,
     * by ocenić pozycję komórki w tablicy.
     * @param c
     * @return
     */
    public boolean equals(LabCell c) {

        if (c==null) return false;

        int myX = 0,    // Licznik kroków w górę
            myY = 0,    // Licznik kroków w lewo.
            cX = 0,
            cY = 0;
        LabCell currentCell = this;
        while(currentCell.neighbours[0]!=null) {
            ++myX;
            currentCell = currentCell.neighbours[0];
        }

        currentCell = c;
        while(currentCell.neighbours[0]!=null) {
            ++cX;
            currentCell = currentCell.neighbours[0];
        }

        if (myX != cX) return false;

        currentCell = this;
        while(currentCell.neighbours[3]!=null) {
            ++myY;
            currentCell = currentCell.neighbours[3];
        }

        currentCell = c;
        while(currentCell.neighbours[3]!=null) {
            ++cY;
//            cellStack.push(currentCell);
            currentCell = currentCell.neighbours[3];
        }

        if (myY!=cY) return false;
        else return true;

    }

    public LabCell[] getNeighbours() { return neighbours; }
    public LabCell[] getPaths() { return paths; }
    public LabCellMode mode() { return mode; }
    public void setMode(LabCellMode m) { mode = m; }
    public void setVisited(boolean b) { visited = b; }
    public boolean isVisited() { return visited; }
    public boolean hasWallAt(int dir) { return paths[dir%sides]==null; }    // dir%sides - na wypadek gdy dir za duży

}
