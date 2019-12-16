package com.example.smartfloodsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import java.util.Random;

import me.itangqi.waveloadingview.WaveLoadingView;


    public class WaterLevel extends Fragment {


        private WaveLoadingView waveLoadingView;
       private SeekBar seekBar;

        int volume = 14;

        int max = 7;
        int min = 1;
        Random random = new Random();

        int randomNum = random.nextInt((max - min) + 1) + min;

        int waterLevel = volume * randomNum;

        public WaterLevel() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_water_level, container, false);
            seekBar = (SeekBar)v.findViewById(R.id.seekBar);
            waveLoadingView = (WaveLoadingView) v.findViewById(R.id.waveLoadingView);
            waveLoadingView.setProgressValue(0);

            waveLoadingView.setProgressValue(waterLevel);


            if (waterLevel < 50) {
                waveLoadingView.setBottomTitle(String.format("%d%%", waterLevel));
                waveLoadingView.setCenterTitle("");
                waveLoadingView.setTopTitle("");
            } else if (waterLevel < 80) {
                waveLoadingView.setBottomTitle("");
                waveLoadingView.setCenterTitle(String.format("%d%%", waterLevel));

                waveLoadingView.setTopTitle("");
            } else {
                waveLoadingView.setBottomTitle("");
                waveLoadingView.setCenterTitle("");
                waveLoadingView.setTopTitle(String.format("%d%%", waterLevel));

            }

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    waveLoadingView.setProgressValue(progress);
                    if (progress < 50) {
                        waveLoadingView.setBottomTitle(String.format("%d%%", progress));
                        waveLoadingView.setCenterTitle("");
                        waveLoadingView.setTopTitle("");
                    } else if (progress < 80) {
                        waveLoadingView.setBottomTitle("");
                        waveLoadingView.setCenterTitle(String.format("%d%%", progress));
                        waveLoadingView.setTopTitle("");
                    } else {
                        waveLoadingView.setBottomTitle("");
                        waveLoadingView.setCenterTitle("");
                        waveLoadingView.setTopTitle(String.format("%d%%", progress));

                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            return v;
        }
    }
