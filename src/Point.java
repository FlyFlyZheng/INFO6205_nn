public class Point {
    public Point(int x,int y){
        this.x=x;
        this.y=y;
        this.status=IS_EMPLY;
    }

    public final static int IS_WALL=0;
    public final static int IS_EMPLY=1;
    public final static int CUP =2;


    private int x;
    private int y;
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
