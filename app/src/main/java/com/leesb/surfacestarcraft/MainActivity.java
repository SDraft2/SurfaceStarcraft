package com.leesb.surfacestarcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity {
    public static int width;
    public static int height;
    GameSurfaceView gameSurfaceView = null;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getBaseContext();

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        width = metrics.widthPixels;
        height = metrics.heightPixels;
        gameSurfaceView = new GameSurfaceView(this, getSharedPreferences("score",MODE_PRIVATE));
        setContentView(gameSurfaceView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOICE_ASSIST:
                gameSurfaceView.fireSuperLaser();
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOICE_ASSIST:
                gameSurfaceView.fireSuperLaser();
                return true;
        }
        return false;
    }
}
