package com.example.nasagallery;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NASAAPOD extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    private int mDay, mMonth, mYear;

    private ImageView mNASAPhoto;
    private TextView mNASATitle, mNASAExplanation;

    private String mVideoUrl;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private YouTubePlayerView mNASAVideo;
    private YouTubePlayer myYouTubePlayer;

    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_apod);

        Calendar c = Calendar.getInstance();
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = c.get(Calendar.MONTH);
        mYear = c.get(Calendar.YEAR);

        mNASAPhoto = findViewById(R.id.NASA_photo);
        mNASAVideo = findViewById(R.id.NASA_video);
        mNASAExplanation = findViewById(R.id.explanation);
        mNASATitle = findViewById(R.id.title_of_view);

        mVideoUrl = "";
        player = MediaPlayer.create(this, R.raw.sound2);

        mDateSetListener = (view, year, month, dayOfMonth) -> {
            player.start();
            mYear = year;
            mMonth = month + 1;
            mDay = dayOfMonth;
            String date = getDate();
            initRetrofit(date);
            mNASAPhoto.setVisibility(View.GONE);
            mNASAVideo.setVisibility(View.GONE);
            mNASATitle.setText("");
            mNASAExplanation.setText("");
            if (myYouTubePlayer!= null)
                myYouTubePlayer.release();
        };

        initNew();
    }

    private void initNew() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setSelectedItemId(R.id.nav_apod);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            player.start();
            switch (item.getItemId()) {
                case R.id.nav_library:
                    startActivity(new Intent(getApplicationContext(), NASAImageAndVideo.class));
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                    finish();
                    return true;
                case R.id.nav_apod:
                    return true;
            }
            return false;
        });
    }

    private String getLinkFromURL(String mVideoUrl) {
        int beg = mVideoUrl.lastIndexOf('/') + 1;
        int end = mVideoUrl.indexOf('?');
        String link = mVideoUrl.substring(beg, end);
        System.out.println("THE FINAL KEY: " + link);
        return link;
    }

    private void initRetrofit(String date) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/planetary/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NASA_JSON nasa_json = retrofit.create(NASA_JSON.class);
        Call<NASAGallery> call = nasa_json.getObject(date);

        call.enqueue(new Callback<NASAGallery>() {
            @Override
            public void onResponse(Call<NASAGallery> call, Response<NASAGallery> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(NASAAPOD.this, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                NASAGallery nasa = response.body();
                assert nasa != null;
                mNASAExplanation.setText(nasa.getExplanation());
                mNASATitle.setText(nasa.getTitle());

                if (isMediaTypeImage(nasa.getMediaType())) {
                    mNASAPhoto.setVisibility(View.VISIBLE);
                    String url = nasa.getUrl();
                    Picasso.get()
                            .load(url)
                            .placeholder(R.drawable.astronaut)
                            .into(mNASAPhoto);
                }
                else {
                    mNASAVideo.setVisibility(View.VISIBLE);
                    mVideoUrl = nasa.getUrl();
                    mNASAVideo.initialize(YouTubeConfig.getApiKey(), NASAAPOD.this);
                }
            }

            @Override
            public void onFailure(Call<NASAGallery> call, Throwable t) {
                Toast.makeText(NASAAPOD.this, "System error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pick(View view) {

        player.start();
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("ResourceType") DatePickerDialog dialog = new DatePickerDialog(
                NASAAPOD.this,
                Color.BLUE,
                mDateSetListener,
                y, m, d
        );
        dialog.show();
    }

    private String getDate() {
        String year = mYear + "", month = mMonth + "", day = mDay + "";
        if (mYear<10)
            year = "0" + year;
        if (mMonth<10)
            month = "0" + month;
        if (mDay<10)
            day = "0" + day;

        return year + "-" + month + "-" + day;
    }

    private boolean isMediaTypeImage(String txt) {
        return txt.equals("image");
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        String link = getLinkFromURL(mVideoUrl);
        setPlayer(youTubePlayer);
        if (!b) {
            youTubePlayer.loadVideo(link);
            youTubePlayer.play();
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        errorReason.getErrorDialog(NASAAPOD.this, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_REQUEST) {
            Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
            getProvider().initialize(YouTubeConfig.getApiKey(), this);
        }
    }

    protected YouTubePlayer.Provider getProvider() {
        return mNASAVideo;
    }

    private void setPlayer(YouTubePlayer player) {
        myYouTubePlayer = player;
    }

}