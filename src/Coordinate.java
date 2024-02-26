public class Coordinate {
    private int x,y;
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Coordinate(Coordinate k){
        this.x = k.getX();
        this.y = k.getY();
    }
    public Coordinate(){
        this.x = 0;
        this.y = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Coordinate changeX(int x) {
        this.x += x;
        return this;
    }
    public Coordinate changeY(int y) {
        this.y += y;
        return this;
    }

    public void set(int x, int y){
        setX(x);
        setY(y);
    }
    public void set(Coordinate k){
        set(k.getX(),k.getY());
    }
    public Coordinate change(int x, int y){
        changeX(x);
        changeY(y);
        return this;
    }

    public int getDistance(Coordinate k){
        return (int)Math.sqrt( Math.pow( k.getX()-getX() ,2) + Math.pow( k.getY() - getY() ,2) );
    }

    public boolean equals(Coordinate k){
        return k.getX() == getX() && k.getY() == getY();
    }

    public void print(){
        System.out.println("( x: " + getX() + " | y: " + getY() + " )");
    }
}
