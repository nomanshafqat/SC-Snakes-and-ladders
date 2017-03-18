package com.example.noman.snakeandladders;

/**
 * Created by Noman on 3/15/2017.
 */


    /*
    computer class which holds the position of computer
    position=19 means computer is at number 19
    chances is variable which is used to give walkovers to computer on higher levels
    */
public class Computer {

    private int position = 1;
    int chances = 0;

    public int getChances() {
        return chances;
    }

    public int getPosition() {
        return position;
    }

    public void setChances(int chances) {
        this.chances = chances;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
