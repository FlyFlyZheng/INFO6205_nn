package com.company;

import java.util.Random;

public class Enviroment {
    public Enviroment(){

    }

    public void generateMap(){
        int x= Config.lengthX;
        int y= Config.lengthY;

        Point[][] map = new Point[x][y];

        for(int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                map[i][j] = new Point(i,j);
            }
        }

        for(int i=0;i<x;i++){
            map[0][i].setStatus(Point.ISWALL);
            map[y][i].setStatus(Point.ISWALL);
            map[i][0].setStatus(Point.ISWALL);
            map[i][x].setStatus(Point.ISWALL);
        }


        Random random= new Random();



        int NumberOfCups =0;
        while(NumberOfCups<=25){
            int randomX=random.nextInt(12);
            int randomY=random.nextInt(12);
            map[randomX][randomY].setStatus(Point.HASCUP);

        }


    }

}
