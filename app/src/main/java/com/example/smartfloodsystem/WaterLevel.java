package com.example.smartfloodsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Random;

import me.itangqi.waveloadingview.WaveLoadingView;

public class WaterLevel extends AppCompatActivity {

    WaveLoadingView waveLoadingView;

    int volume = 14;
    int max = 10;
    int min = 1;
    Random random = new Random();

    int randomNum = random.nextInt((max - min) + 1) + min;

    int waterLevel = volume * randomNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_level);

        waveLoadingView = (WaveLoadingView)findViewById(R.id.waveLoadingView);
        waveLoadingView.setProgressValue(0);

        waveLoadingView.setProgressValue(waterLevel);

        if(waterLevel<50)
        {
            waveLoadingView.setBottomTitle(String.format("%d%%",waterLevel));
            waveLoadingView.setCenterTitle("");
            waveLoadingView.setTopTitle("");
        }
        else if(waterLevel<80)
        {
            waveLoadingView.setBottomTitle("");
            waveLoadingView.setCenterTitle(String.format("%d%%",waterLevel));
            waveLoadingView.setTopTitle("");
        }
        else
        {
            waveLoadingView.setBottomTitle("");
            waveLoadingView.setCenterTitle("");
            waveLoadingView.setTopTitle(String.format("%d%%",waterLevel));
        }

    }


}
