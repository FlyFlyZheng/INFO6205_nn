package GA_Package;

public class City{
    public int no;
    public int x;
    public int y;

    public City(int no, int x, int y){
        this.no = no;
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return String.valueOf(this.no);
    }
}