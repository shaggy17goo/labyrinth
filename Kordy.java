public class Kordy{
    private static int maxX,maxY;
    private int x,y;
    private int terrain;

    /**
     * Konstruktor dodający typ terenu
     * @param x
     * @param y
     * @param terrain
     */
    public Kordy(int x, int y, int terrain){
        this.x=x;
        this.y=y;
        this.terrain=terrain;
    }

    /**
     * Konstruktor podstawowy funkcji
     * @param x
     * @param y
     */
    public Kordy(int x, int y){
        this.x=x;
        this.y=y;
    }

    /**
     * Konstruktor dodający maksymalny
     * @param maxX
     */
    public Kordy(int maxX){
        this.x=maxX;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    @Override
    public int hashCode() {

        return this.x*maxX+y;
    }
    @Override
    public boolean equals(Object obj){
        int x=((Kordy)obj).getX();
        int y=((Kordy)obj).getY();
        return this.x==x && this.y==y;
    }
}
