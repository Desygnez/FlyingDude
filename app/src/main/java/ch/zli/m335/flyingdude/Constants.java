package ch.zli.m335.flyingdude;

import android.content.Context;

public class Constants {

        public static int SCREEN_WIDTH;
        public static int SCREEN_HEIGHT;

        public static Context CURRENT_CONTEXT;

        public static long INIT_TIME;

        public static int CURRENT_Y=0, ADDER=15, PLAYER_SIZE=100, MIN_SPACE=300, MAX_SPACE=500, MAX_DELTA=50,
                BLOCK_HEIGHT=200, LEFT_COLLISION=0x1<<0, TOP_COLLISION=0x1<<1,
                RIGHT_COLLISION=0x1<<2, BOTTOM_COLLISION=0x1<<3, SCORE=0, HIGHSCORE=0;

        public static String HIGH_SCORE_FILE = "ch.zli.m355.highScore";

    }

