import javax.swing.*;
import java.awt.*;

public class MyGraphics extends JPanel {

    public MyGraphics() {
        setPreferredSize(new Dimension(Data.windowX, Data.windowY));
    }

    /**
     * Paints the labyrinth and the path, whenever the JPanel is created or updated (repainted)
     * @param g context of displaying
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        this.setPreferredSize(new Dimension(Data.windowX, Data.windowY));

        g2.setStroke(new BasicStroke(Data.lineThickness));
        if(Data.labyrinth.algorithmType==4) drawGraphLabyrinth(g2,Data.labyrinth);
            else drawLabyrinth(g2, Data.labyrinth);
        if (Data.path) {
            drawPath(g2, Data.labyrinth);
            if(Data.labyrinth.algorithmType==4) Data.labyrinth.recursiveBackTracker.resetVisited();
        }
        Data.path = false;

    }

    /**
     * Draws the labyrinth
     * @param g context of displaying
     * @param labyrinth labyrinth we want do draw
     */
    public static void drawLabyrinth(Graphics2D g, Labyrinth labyrinth) {
        int start = 40;
        for (int y = 0; y < labyrinth.maxY; y++)
            for (int x = 0; x < labyrinth.maxX; x++) {
                if (labyrinth.walls[y][x]) {
                    g.setColor(Color.GRAY);
                    g.fillRect(start + x * 10, start / 3 + y * 10, 10, 10);
                }
            }
        g.setColor(Color.GREEN);
        g.fillRect(start, start / 3, 20, 20);//Start
        g.setColor(Color.RED);
        g.fillRect(start + (labyrinth.maxX - 2) * 10, start / 3 + (labyrinth.maxY - 2) * 10, 20, 20);//End
    }

    /**
     * Repaints and realizes the JPanel
     */
    @Override
    public void repaint(){
        setPreferredSize(new Dimension(Data.windowX, Data.windowY));
        super.repaint();
    }

    /**
     * Draws the path form start to finish
     * @param g context of displaying
     * @param labyrinth labyrinth we want to draw the path in
     */
    public void drawPath(Graphics2D g, Labyrinth labyrinth) {
        if(labyrinth.algorithmType!=4) {
            int start = 40;
            labyrinth.normalSolver();
            for (int y = 0; y < labyrinth.maxY; y++)
                for (int x = 0; x < labyrinth.maxX; x++) {
                    if (labyrinth.path[y][x]) {
                        g.setColor(Color.BLUE);
                        g.fillRect(start + x * 10 + 3, start / 3 + y * 10 + 3, 4, 4);
                    }
                }
            g.setColor(Color.GREEN);
            g.fillRect(start, start / 3, 20, 20);//Start
            g.setColor(Color.RED);
            g.fillRect(start + (labyrinth.maxX - 2) * 10, start / 3 + (labyrinth.maxY - 2) * 10, 20, 20);//End
        }
        else{
            labyrinth.recursiveBackTracker.solve();
            drawGraphLabyrinth(g,labyrinth);
        }
    }

    /**
     * Draws the graph labyrinth
     * @param g2 context of displaying
     * @param labyrinth labyrinth we want do draw
     */
    private void drawGraphLabyrinth(Graphics2D g2,Labyrinth labyrinth) {
        for(int x=0; x<labyrinth.maxX; x++)
            for(int y=0; y<labyrinth.maxY; y++) {
                g2.setColor(this.getBackground());
                drawCell(g2,x, y,labyrinth);
            }
    }

    /**
     * Draws an individual cell of the labyrinth
     * @param g2
     * @param x
     * @param y
     * @param labyrinth
     */

    private void drawCell(Graphics2D g2, int x, int y, Labyrinth labyrinth) {
        LabCell tempCell = labyrinth.recursiveBackTracker.getCell(x, y);
        int bargin=20;
        switch (tempCell.mode()) {
            case END:   // rysujemy czerwony kwadrat
                g2.setColor(Color.red);
                break;
            case START: // rysujemy zielony kwadrat
                g2.setColor(Color.green);
                break;
            case FLOODED:   // ta komórka była odwiedzona w poszukiwaniu drogi od startu do mety
               g2.setColor(Color.lightGray); // szary kwadrat
                break;
            case IS_ON_PATH:
                g2.setColor(Color.gray);
                break;
            default:
                g2.setColor(this.getBackground());
                break;
        }

        g2.fillRect(bargin+x*Data.pixelsPerCell, y*Data.pixelsPerCell,Data.pixelsPerCell,Data.pixelsPerCell);

        g2.setColor(Color.black);
        if(tempCell.hasWallAt(0)) g2.drawLine(bargin+x*Data.pixelsPerCell, y*Data.pixelsPerCell, bargin+(x+1)*Data.pixelsPerCell, y*Data.pixelsPerCell);//góra
        if(tempCell.hasWallAt(1)) g2.drawLine(bargin+(x+1)*Data.pixelsPerCell, y*Data.pixelsPerCell,bargin+(x+1)*Data.pixelsPerCell, (y+1)*Data.pixelsPerCell);//prawo
        if(tempCell.hasWallAt(2)) g2.drawLine(bargin+x*Data.pixelsPerCell,(y+1)*Data.pixelsPerCell,bargin+(x+1)*Data.pixelsPerCell,(y+1)*Data.pixelsPerCell);//dół
        if(tempCell.hasWallAt(3)) g2.drawLine(bargin+x*Data.pixelsPerCell, y*Data.pixelsPerCell, bargin+x*Data.pixelsPerCell, (y+1)*Data.pixelsPerCell);//lewo
    }







}