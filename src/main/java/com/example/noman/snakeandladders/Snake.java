package com.example.noman.snakeandladders;

/**
 *Snake class contains top and bottom position of the snake
 */
public class Snake{
    int top;
    int bot;

    Snake(int top,int bottom){
        this.bot=bottom;
        this.top=top;
    }
    public int getFoot() {
        return bot;
    }

    public int getHead() {
        return top;
    }

    public void setFoot(int foot) {
        this.bot = foot;
    }

    public void setHead(int head) {
        this.top = head;
    }
}
