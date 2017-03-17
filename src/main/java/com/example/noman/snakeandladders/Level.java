package com.example.noman.snakeandladders;
import java.util.*;
/**
 * Created by Noman on 3/14/2017.
 */
class Ladder{
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
class Snake{
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

public class Level {

    Level(Player p,Computer c) {
        player=p;
        computer=c;
    }

    private Player player=null;
    private Computer computer=null;

    private Vector<Ladder> ladders = new Vector<Ladder>(0, 1);
    private Vector<Snake> snakes = new Vector<Snake>(0, 1);

    void add_ladder(Ladder lad) {
        ladders.addElement(lad);
    }

    void add_snake(Snake sna) {
        snakes.addElement(sna);
    }

    Vector<Ladder> getLadders() {
        return ladders;
    }
    Vector<Snake> getSnakes(){
        return snakes;
    }

    public Player getPlayer() {
        return player;
    }

    public Computer getComputer() {
        return computer;
    }

}
