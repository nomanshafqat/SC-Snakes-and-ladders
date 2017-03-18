package com.example.noman.snakeandladders;
import java.util.*;
/**
 * Created by Noman on 3/14/2017.
 */

/**
 * Contains metadata of a level
 * player object
 * Computer object
 * array of ladders
 * array of snakes
*/

public class Level {


    private Player player = null;
    private Computer computer = null;


    private Vector<Ladder> ladders = new Vector<Ladder>(0, 1);
    private Vector<Snake> snakes = new Vector<Snake>(0, 1);


    Level(Player p, Computer c) {
        player = p;
        computer = c;
    }


    /*Getters and setters*/
    void add_ladder(Ladder lad) {
        ladders.addElement(lad);
    }

    void add_snake(Snake sna) {
        snakes.addElement(sna);
    }

    public Player getPlayer() {
        return player;
    }

    public Computer getComputer() {
        return computer;
    }

    Vector<Ladder> getLadders() {
        return ladders;
    }

    Vector<Snake> getSnakes() {
        return snakes;
    }
}

