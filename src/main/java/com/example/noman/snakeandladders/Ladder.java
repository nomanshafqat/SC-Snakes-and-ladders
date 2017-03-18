package com.example.noman.snakeandladders;

/**
 *Ladder class contains top and bottom position of the ladder
 */
public class Ladder{
    int top;
    int bot;

    Ladder(int bottom,int top){
        this.bot=bottom;
        this.top=top;
    }
    public void setFoot(int foot) {
        this.bot = foot;
    }

    public void setHead(int head) {
        this.top = head;
    }

    public int getHead() {
        return top;
    }

    public int getFoot() {
        return bot;
    }
}
