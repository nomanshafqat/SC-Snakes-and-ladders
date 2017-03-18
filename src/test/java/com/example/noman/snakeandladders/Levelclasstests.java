package com.example.noman.snakeandladders;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class Levelclasstests {
    @Test
    public void addition_isCorrect() throws Exception {

        assertEquals(4, 2 + 2);
    }
    @Test
    public void addladder(){
        Level level=new Level( new Player(),new Computer());

        level.add_ladder(new Ladder(1,6));
        assertEquals(level.getLadders().get(0).getHead(),6);


    }
    @Test
    public void addsnakes(){
        Level level=new Level( new Player(),new Computer());

        level.add_snake(new Snake(55,3));
        assertEquals(level.getSnakes().get(0).getHead(),55);


    }

}