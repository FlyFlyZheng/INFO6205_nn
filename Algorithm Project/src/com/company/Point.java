package com.company;

public class Point {
    public Point(int x,int y){
        this.x=x;
        this.y=y;
        this.status=ISEMPLY;
    }

    final static int ISWALL=0;
    final static int ISEMPLY=1;
    final static int HASCUP =2;


    int x;
    int y;
    int status;

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
