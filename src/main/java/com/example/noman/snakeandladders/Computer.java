package com.example.noman.snakeandladders;

/**
 * Created by Noman on 3/15/2017.
 */

public class Computer {
   private int position=1;
   int chances=0;

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
