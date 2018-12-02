import java.util.Random;
public class Environment {

    Point[][] map;

    public Environment(){

    }

    public void generateMap(){
        int len= Config.LENGTH;
        map = new Point[len][len];

        for(int i=0;i<len;i++){
            for(int j=0;j<len;j++){
                map[i][j] = new Point(i,j);
            }
        }

        //set wall
//        for(int i=0;i<len;i++){
//            map[0][i].setStatus(Point.IS_WALL);
//            map[len-1][i].setStatus(Point.IS_WALL);
//            map[i][0].setStatus(Point.IS_WALL);
//            map[i][len-1].setStatus(Point.IS_WALL);
//        }

        //set random cup
        Random random= new Random();
        int NumberOfCups =0;
        while(NumberOfCups<=Config.CUP_NUM) {
            int randomX = random.nextInt(len);
            int randomY = random.nextInt(len);
            map[randomX][randomY].setStatus(Point.CUP);
            NumberOfCups++;
        }
    }

    public void printMap(){
        int len = Config.LENGTH;
        for(int i=0; i<len; i++){

        }
    }
}
