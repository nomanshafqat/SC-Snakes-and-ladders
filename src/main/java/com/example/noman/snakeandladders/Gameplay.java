package com.example.noman.snakeandladders;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.*;

import java.util.Random;
import java.util.Vector;
import android.view.MotionEvent;

public class Gameplay extends AppCompatActivity {

    MySurfaceView mySurfaceView;

    /**
     * Gets from the shared variable and sets up metadata for level
     */
    private Level getlevel() {

        int levelnum;

        //Gets the current level of the user from shared variable
        SharedPreferences mPrefs = getSharedPreferences("label", 0);
        levelnum = mPrefs.getInt("level", 0);

        //for game log
        System.out.println("chances and level " + levelnum);

        Level level;

        Computer computer = new Computer();
        computer.setChances(levelnum);

        Player player=new Player();

        //Creating a new level with  a player and a computer
        level = new Level(player, computer);

        //ladder positions
        level.add_ladder(new Ladder(4, 22));
        level.add_ladder(new Ladder(11, 33));
        level.add_ladder(new Ladder(47, 56));
        level.add_ladder(new Ladder(58, 80));

        //snake positions
        level.add_snake(new Snake(94, 71));
        level.add_snake(new Snake(76, 53));
        level.add_snake(new Snake(60, 43));
        level.add_snake(new Snake(27, 8));
        level.add_snake(new Snake(99, 77));
        level.add_snake(new Snake(37, 25));


        return level;

    }

    /**
     * Contains game state of the game
     */
    private class Gamestate {
        int state = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        mySurfaceView = new MySurfaceView(this, getlevel());
        setContentView(mySurfaceView);
        mySurfaceView.onResumeMySurfaceView();


    }

    private class MySurfaceView extends SurfaceView implements Runnable {
       /*  GAME STATES */
        private static final int PLAYERWAITING = 0;
        private static final int PLAYERROLL = 1;
        private static final int COMPUTERROLL = 2;
        private static final int PWINS = 4;
        private static final int CWINS = 5;

        //class initialized which holdes state variable
        Gamestate gamestate = new Gamestate();

        //holds the dice value for current roll
        int dice_value;

        /*
        this array has x,y positions of the numbers
        grid[1][1] holds the y pixel position of number 1
        grid[76][0] holds the x pixel position of number 76 of the game
         */
        int[][] grid = new int[101][2];

        /* contains meta data of the level goto the level class for more information*/
        Level level;

        //canvas and runnable interface variables
        Thread thread = null;
        SurfaceHolder surfaceHolder;
        volatile boolean running = false;
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random random;

        /**Constructor which gets an extra variable level which tells
           this class to draw the grid according to the Level metadata*/
        public MySurfaceView(Context context, Level level) {
            super(context);
            this.level = level;
            surfaceHolder = getHolder();
            random = new Random();
        }

        public void onResumeMySurfaceView() {
            running = true;
            thread = new Thread(this);
            thread.start();
        }

        public void onPauseMySurfaceView() {
            boolean retry = true;
            running = false;
            while (retry) {
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                     e.printStackTrace();
                }
            }
        }

        //draws player box on the grid
        public void draw_player(Canvas canvas) {
            int ppos = level.getPlayer().getposition();

            int lx = grid[ppos][0];
            int ly = grid[ppos][1];
            int rx = (int) (grid[ppos][0] + canvas.getWidth() * 0.1);
            int ry = (int) (grid[ppos][1] + (canvas.getHeight()-300) * 0.1);

            //draw rect at player  position
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xff008800);
            canvas.drawRect(lx, ly, rx, ry, paint);

            //draw P on player position rectangle
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((float) (canvas.getWidth() * .05));
            paint.setColor(0xffffffff);
            canvas.drawText("You", (lx + rx) / 2, (ly + ry) / 2, paint);


        }

        //draws computer box on the grid
        public void draw_computer(Canvas canvas) {

            int cpos = level.getComputer().getPosition();

            int lx = grid[cpos][0];
            int ly = grid[cpos][1];
            int rx = (int) (grid[cpos][0] + canvas.getWidth() * 0.1);
            int ry = (int) (grid[cpos][1] + (canvas.getHeight()-300) * 0.1);

            //draw rect on computer position
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xffFF0000);
            canvas.drawRect(lx, ly, rx, ry, paint);

            //draw C on computer position rectange
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((float) (canvas.getWidth() * .05));
            paint.setColor(0xffffffff);
            canvas.drawText("C", (lx + rx) / 2, (ly + ry) / 2, paint);

        }

        public void draw_grid(Canvas canvas) {

            int w = canvas.getWidth();
            //300 is the bottom margin
            int h = canvas.getHeight() - 300;
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);


            for (int k = 1; k < 101; k++) {

                int i = grid[k][0];
                int j = grid[k][1];

                paint.setColor(0xff000000);
                canvas.drawRect(i, j, i + (float) (0.1 * w), j + (float) (0.1 * h), paint);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize((float) (w * .05));
                canvas.drawText(Integer.toString(k), i + (float) (0.1 * w) / 2, (float) ((0.1 * h) / 1.6) + j, paint);
            }

        }

        //calculates and stores the x,y position of the boxes
        public void initialise_grid(Canvas canvas) {

            int w = canvas.getWidth();
            int h = canvas.getHeight() - 300;

            int mid = 100;int or = -1; int iter = 10;

            for (int y = 0; y < h; y += (h) * 0.1) {

                //Seprating odd and even rows
                if (iter % 2 == 0) {
                    mid = iter * 10;
                    or = -1;
                } else {
                    mid = iter * 10 - 10 + 1;
                    or = 1;
                }

                for (int x = 0; x < w; x += w * 0.1) {
                     if (mid > 0) {
                        grid[mid][0] = x;
                        grid[mid][1] = y;
                        mid += or;
                    }
                }
                iter--;
            }
        }

        //draws Ladders after fetching vector of ladders from level object
        public void draw_ladders(Canvas canvas) {

            int mheight = (int) (canvas.getHeight() * 0.1);

            Vector<Ladder> ladders = level.getLadders();

            for (int i = 0; i < ladders.size(); i++) {

                //imports picture and draws from drawable
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ladder);

                double delx = grid[ladders.get(i).top][0] - grid[ladders.get(i).bot][0];
                double dely = grid[ladders.get(i).top][1] - grid[ladders.get(i).bot][1];

                //matrix for translating and rotating the ladder image
                Matrix matrix = new Matrix();
                matrix.setTranslate((float) (grid[ladders.get(i).top][0]), (float) (grid[ladders.get(i).top][1] + mheight / 1.7));
                matrix.preRotate((float) ((180 / 3.142) * Math.atan(-delx / dely)));
                matrix.preScale((float) .5, (float) ((Math.sqrt((dely * dely) + (delx * delx))) / (bitmap.getHeight())));


                canvas.drawBitmap(bitmap, matrix, paint);
            }
        }

        //draws snakes after fetching vector of snakes from level object
        public void draw_snakes(Canvas canvas) {

            int mheight = (int) (canvas.getHeight() * 0.1);
            int mwidth = (int) (canvas.getWidth() * 0.1);

            Vector<Snake> snakes = level.getSnakes();

            for (int i = 0; i < snakes.size(); i++) {       // TODO Auto-generated method stub
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sanake);

                //fetches the position from the grid for the head and tail of the snake
                int leftx = grid[snakes.get(i).top][0] + mwidth / 2;
                int lefty = grid[snakes.get(i).top][1] + mheight / 2;
                int rightx = grid[snakes.get(i).bot][0] + mwidth / 2;
                int righty = grid[snakes.get(i).bot][1] + mheight / 2;

                //makes the rectangle out of those coordinates and prints
                canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(leftx, lefty, rightx, righty), paint);
            }
        }

        //The text displayed when promting to user to touch to roll the dice
        public void draw_Roll_text(Canvas canvas) {


            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(13);
            paint.setColor(0xffffffff);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((float) (canvas.getWidth() * .1));
            canvas.drawText("Touch to ROLL", (int) (canvas.getWidth() * .5), (int) (canvas.getHeight() * .9), paint);


        }

        //draws all the text dispalyed on the screen for the computer
        public void draw_computer_turn_text(Canvas canvas) {

            paint.setTextSize((float) (canvas.getWidth() * .1));
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xff00ff00);
            canvas.drawText("Computer's turn", (int) (canvas.getWidth() * .5), (int) (canvas.getHeight() * .2), paint);
            paint.setTextSize((float) (canvas.getWidth() * .3));

            String score = "";
            int temp = dice_value;
            if (temp > 6) {
                do {
                    score += "6+";
                    temp -= 6;
                } while (temp > 6);
            }
            score += temp;

            canvas.drawText(score, (int) (canvas.getWidth() * .5), (int) (canvas.getHeight() * .6), paint);

            paint.setColor(0xffffffff);
            paint.setTextSize((float) (canvas.getWidth() * .1));

            canvas.drawText("Touch to continue", (int) (canvas.getWidth() * .5), (int) (canvas.getHeight() * .9), paint);

        }

        //draws all the text dispalyed on the screen for the player
        public void draw_player_turn_text(Canvas canvas) {
            paint.setTextSize((float) (canvas.getWidth() * .3));
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xffff0000);
            String score = "";
            int temp = dice_value;
            if (temp > 6) {
                do {
                    score += "6+";
                    temp -= 6;
                } while (temp > 6);
            }
            score += temp;

            canvas.drawText(score, (int) (canvas.getWidth() * .5), (int) (canvas.getHeight() * .6), paint);

            paint.setTextSize((float) (canvas.getWidth() * .1));
            paint.setColor(0xffffffff);

            canvas.drawText("Touch to continue", (int) (canvas.getWidth() * .5), (int) (canvas.getHeight() * .9), paint);
        }

        //draws the rolled result of the dice acording to the state
        public void rolled_dice_draw(Canvas canvas) {
            //if state is computer
            if (gamestate.state == COMPUTERROLL) {
                draw_computer_turn_text(canvas);
            } else {
                //if state is user rll
                draw_player_turn_text(canvas);


            }


        }

        //Game loop
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (running) {

                 if (surfaceHolder.getSurface().isValid()) {

                    Canvas canvas = surfaceHolder.lockCanvas();

                    //clear canvas
                    canvas.drawColor(0xff0000aa);

                    //Initialize and draw the Grid
                    initialise_grid(canvas);
                    draw_grid(canvas);

                    //Draw ladders , snakes and players
                    draw_ladders(canvas);
                    draw_snakes(canvas);
                    draw_computer(canvas);
                    draw_player(canvas);

                    //Draw text according the state
                    if (gamestate.state == PLAYERWAITING) {
                        draw_Roll_text(canvas);
                    }
                    if (gamestate.state == PLAYERROLL) {
                        rolled_dice_draw(canvas);
                    }
                    if (gamestate.state == COMPUTERROLL) {
                        rolled_dice_draw(canvas);
                    }
                    if (gamestate.state == PWINS) {
                        player_wins(canvas);
                    }
                    if (gamestate.state == CWINS) {
                        computer_wins(canvas);
                    }

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        public void handle_touch() {
            //Changing states in a loop until one wins the game
            if (gamestate.state == PLAYERWAITING) {
                gamestate.state = PLAYERROLL;
            } else if (gamestate.state == PLAYERROLL) {
                gamestate.state = COMPUTERROLL;
            } else if (gamestate.state == COMPUTERROLL) {
                gamestate.state = PLAYERWAITING;
            } else if (gamestate.state == PWINS || gamestate.state == CWINS) {

                onPauseMySurfaceView();
                finish();
            }

        }

        //called when user wins the game and used for updating the data
        void update_wins() {
            SharedPreferences mPrefs = getSharedPreferences("label", 0);

            int w = mPrefs.getInt("win", 0);
            int l = mPrefs.getInt("loss", 0);
            int lev = mPrefs.getInt("level", 1);
            int pts = mPrefs.getInt("points", 0);

            System.out.print(w + "new win" + (w + 1) + "pts" + pts + "\n");

            //Updating WINS
            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putInt("win", w + 1).commit();

            int points = level.getPlayer().getposition() - level.getComputer().getPosition();

            //Updating Points
            mEditor.putInt("points", pts + points).commit();

            System.out.println("nxt level=" + (float) w / (float) (w + l));

            //Moving to new level if win % >33%
            if (((float) w / (float) (w + l)) > 0.33 && (w + l) > 10) {

                System.out.println("Promoted to Next level" + (lev + 1));
                mEditor.putInt("level", lev + 1).commit();
                mEditor.putInt("win", 0).commit();
                mEditor.putInt("loss", 0).commit();

            }


        }


        //when user losses the game it updates the sharedPreference variable
        void update_losses() {
            SharedPreferences mPrefs = getSharedPreferences("label", 0);
            int a = mPrefs.getInt("loss", 0);
            //for the log
            System.out.print(a + "new loss" + (a + 1) + "\n");

            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putInt("loss", a + 1).commit();
        }

        //when Player wins this function is called to draw thepage for it
        public void player_wins(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            paint.setColor(0xff000000);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((float) (canvas.getWidth() * .1));
            canvas.drawText("You WIN", (float) (canvas.getWidth()) / 2, (float) ((canvas.getHeight()) / 2.2), paint);
            canvas.drawText("Touch to return", (float) (canvas.getWidth()) / 2, (float) ((canvas.getHeight()) - 200), paint);

            int points = level.getPlayer().getposition() - level.getComputer().getPosition();
            canvas.drawText("Your earn " + Integer.toString(points) + " points", (float) (canvas.getWidth()) / 2, (float) ((canvas.getHeight()) - 400), paint);

        }

        //when computer wins this function is called to draw thepage for it
        public void computer_wins(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            paint.setColor(0xff000000);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize((float) (canvas.getWidth() * .1));
            canvas.drawText("COMPUTER WINS", (float) (canvas.getWidth()) / 2, (float) ((canvas.getHeight()) / 2.2), paint);
            canvas.drawText("Touch to return", (float) (canvas.getWidth()) / 2, (float) ((canvas.getHeight()) - 200), paint);
        }

        public void add_score_to_player(int dice_value) {

            if (gamestate.state == PLAYERROLL) {
                int position = level.getPlayer().getposition();
                if (position + dice_value <= 100) {
                    if (position + dice_value == 100) {
                        gamestate.state = PWINS;

                    } else {

                        level.getPlayer().setposition(dice_value + (level.getPlayer().getposition()));
                    }
                }


            } else if (gamestate.state == COMPUTERROLL) {
                int position = level.getComputer().getPosition();
                if (position + dice_value <= 100) {

                    if (position + dice_value == 100) {
                        gamestate.state = CWINS;


                    } else {

                        level.getComputer().setPosition(dice_value + (level.getComputer().getPosition()));


                    }
                }

            }

        }

        //return flag 1= snake_found and 0 not found and 2 is for avoiding the snake
        public int check_snake(int dice_value) {
            for (int i = 0; i < level.getSnakes().size(); i++) {
                if (gamestate.state == PLAYERROLL) {
                    if (level.getSnakes().get(i).getHead() == (dice_value + level.getPlayer().getposition())) {
                        System.out.println("player bit by snake at "+level.getSnakes().get(i).getHead());
                        level.getPlayer().setposition(level.getSnakes().get(i).bot);
                        return 1;

                    }
                } else if (gamestate.state == COMPUTERROLL) {
                     if (level.getSnakes().get(i).getHead() == (dice_value + level.getComputer().getPosition())) {
                        if (level.getComputer().getChances() == 0) {
                            System.out.println("Computer bit by snake at"+level.getSnakes().get(i).getHead());

                            level.getComputer().setPosition(level.getSnakes().get(i).bot);
                            return 1;
                        } else {
                            System.out.println("escaped " + (dice_value + level.getComputer().getPosition()));
                            level.getComputer().setChances(level.getComputer().getChances() - 1);


                            return 2;
                        }

                    }
                }

            }
            return 0;
        }

        //return 1= ladder_found and return 0 not found
        public int check_ladder(int dice_value) {

            for (int i = 0; i < level.getLadders().size(); i++) {

                //if there is ladder at the current + dice position
                if (gamestate.state == PLAYERROLL) {
                     if (level.getLadders().get(i).getFoot() == (dice_value + level.getPlayer().getposition())) {
                        System.out.println("player got ladder at"+level.getLadders().get(i).getFoot());

                        level.getPlayer().setposition(level.getLadders().get(i).top);
                        return 1;
                    }
                } else if (gamestate.state == COMPUTERROLL) {

                    if (level.getLadders().get(i).getFoot() == (dice_value + level.getComputer().getPosition())) {
                        System.out.println("Computer got ladder at"+level.getLadders().get(i).getFoot());

                        level.getComputer().setPosition(level.getLadders().get(i).top);

                        return 1;

                    }
                }

            }

            return 0;
        }

        public void touched() {

            //finds a number 1-6
            int temp = ((int) (Math.random() * 5.9) + 1);
            //while it is 6 keeps on adding in the number
            while (temp % 6 == 0) {
                temp += ((int) (Math.random() * 5.9) + 1);

            }

            System.out.println(temp);
            //checks for the snake
            int sflag = check_snake(temp);
            //if computer finds a snake and it also has walkovers left
            if (sflag == 2) {
                temp += 1;
                if (temp == 7) {
                    temp -= 2;
                }
            }
            //checks if a ladder is present or not
            int lflag = check_ladder(temp);

            //if no ladder and no snake simple adds to the number
            if (lflag == 0 && (sflag == 0 || sflag == 2)) {
                dice_value = temp;
                add_score_to_player(temp);
            }
            //if there is a ladder retained the previous state and calls itself
            if (lflag == 1) {
                gamestate.state -= 1;
                touched();

            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            int action = event.getAction();
            switch (action) {
                //if clicked
                case MotionEvent.ACTION_DOWN:

                    if (gamestate.state == CWINS) {
                        update_losses();
                    } else if (gamestate.state == PWINS) {
                        update_wins();
                    }
                    //change game state and perform routine
                    handle_touch();
                    touched();

                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                case MotionEvent.ACTION_OUTSIDE:
                    break;
                default:
            }
            return true; //processed
        }


    }

    @Override
    public void onBackPressed() {
        System.out.print("Back detected");
        mySurfaceView.onPauseMySurfaceView();
         this.finish();
    }

}