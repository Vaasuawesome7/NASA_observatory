package com.example.nasagallery;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    private LottieAnimationView animationView;
    private Button apod, lib;
    private Animation fade, fade1;
    private TextView cred, welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        animationView = findViewById(R.id.launch_anim);
        apod = findViewById(R.id.apod);
        lib = findViewById(R.id.lib);
        fade = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        fade1 = AnimationUtils.loadAnimation(this, R.anim.fadein);
        cred = findViewById(R.id.credential);
        welcome = findViewById(R.id.welcome);
    }

    public void apod(View view) {
        MediaPlayer.create(SplashScreen.this, R.raw.sound2).start();
        System.out.println(animationView.getDuration());
        animationView.playAnimation();
        long time = animationView.getDuration();
        apod.setVisibility(View.GONE);
        apod.setClickable(false);
        lib.setVisibility(View.GONE);
        lib.setClickable(false);
        apod.startAnimation(fade);
        lib.startAnimation(fade);
        cred.setVisibility(View.VISIBLE);
        cred.startAnimation(fade1);
        welcome.setVisibility(View.INVISIBLE);
        welcome.startAnimation(fade);
        new CountDownTimer(time/2, time/2) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashScreen.this, NASAAPOD.class));
                MediaPlayer.create(SplashScreen.this, R.raw.start).start();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        }.start();
    }

    public void library(View view) {
        MediaPlayer.create(SplashScreen.this, R.raw.sound2).start();
        System.out.println(animationView.getDuration());
        animationView.playAnimation();
        long time = animationView.getDuration();
        apod.setVisibility(View.GONE);
        apod.setClickable(false);
        lib.setVisibility(View.GONE);
        lib.setClickable(false);
        apod.startAnimation(fade);
        lib.startAnimation(fade);
        cred.setVisibility(View.VISIBLE);
        cred.startAnimation(fade1);
        welcome.setVisibility(View.INVISIBLE);
        welcome.startAnimation(fade);
        new CountDownTimer(time/2, time/2) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashScreen.this, NASAImageAndVideo.class));
                MediaPlayer.create(SplashScreen.this, R.raw.start).start();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        }.start();
    }
}