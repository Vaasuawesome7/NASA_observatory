package com.example.nasagallery;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

    private String mVideoUrl;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private YouTubePlayerView mNASAVideo;

    //YouTubePlayer.OnInitializedListener mOnInitializedListener;

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
        mVideoUrl = "";

        mDateSetListener = (view, year, month, dayOfMonth) -> {
            mYear = year;
            mMonth = month + 1;
            mDay = dayOfMonth;
            String date = getDate();
            Toast.makeText(NASAAPOD.this, date , Toast.LENGTH_SHORT).show();
            initRetrofit(date);
        };

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
                System.out.println("----------------------");
                System.out.println("DATE: " + nasa.getDate());
                System.out.println("TITLE: " + nasa.getTitle());
                System.out.println("URL: " + nasa.getUrl());
                System.out.println("HD URL: " + nasa.getHdurl());
                System.out.println("SERVICE VERSION: " + nasa.getServiceVersion());
                System.out.println("MEDIA TYPE: " + nasa.getMediaType());
                System.out.println("EXPLANATION: " + nasa.getExplanation());

                if (isMediaTypeImage(nasa.getMediaType())) {
                    mNASAPhoto.setVisibility(View.VISIBLE);
                    String url = nasa.getHdurl();
                    Picasso.get().load(url).into(mNASAPhoto);
                }
                else {
                    System.out.println("VIDEO");
                    mNASAVideo.setVisibility(View.VISIBLE);
                    mVideoUrl = nasa.getUrl();


                    Toast.makeText(NASAAPOD.this, "came", Toast.LENGTH_SHORT).show();
                    mNASAVideo.initialize(YouTubeConfig.getApiKey(), NASAAPOD.this);
                    Toast.makeText(NASAAPOD.this, "here", Toast.LENGTH_SHORT).show();

                    //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(mVideoUrl));
                    //startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<NASAGallery> call, Throwable t) {
                Toast.makeText(NASAAPOD.this, "System error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month + 1;
        mDay = dayOfMonth;
        String date = getDate();
        Toast.makeText(this, date , Toast.LENGTH_SHORT).show();
        initRetrofit(date);
    }

     */
    public void pick(View view) {
        mNASAPhoto.setVisibility(View.GONE);
        findViewById(R.id.NASA_video).setVisibility(View.GONE);
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
        System.out.println("here" + mVideoUrl);
        String link = getLinkFromURL(mVideoUrl);
        //youTubePlayer.loadVideo(link);
        System.out.println("DURATION: " + youTubePlayer.getDurationMillis());
        if (!b)
            youTubePlayer.cueVideo(link);
        Toast.makeText(NASAAPOD.this, "success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getProvider().initialize(YouTubeConfig.getApiKey(), this);
        }
    }

    protected YouTubePlayer.Provider getProvider() {
        return mNASAVideo;
    }
}