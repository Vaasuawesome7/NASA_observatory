package com.example.nasagallery;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private int mDay, mMonth, mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar c = Calendar.getInstance();
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = c.get(Calendar.MONTH);
        mYear = c.get(Calendar.YEAR);
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
                    Toast.makeText(MainActivity.this, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                NASAGallery nasa = response.body();
                assert nasa != null;
                System.out.println("----------------------");
                System.out.println("DATE: " + nasa.getDate());
                System.out.println("TITLE: " + nasa.getTitle());
                System.out.println("URL: " + nasa.getUrl());
                System.out.println("HD URL: " + nasa.getHdurl());
                System.out.println("MEDIA TYPE: " + nasa.getMediaType());
                System.out.println("EXPLANATION: " + nasa.getExplanation());
            }

            @Override
            public void onFailure(Call<NASAGallery> call, Throwable t) {
                Toast.makeText(MainActivity.this, "System error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month + 1;
        mDay = dayOfMonth;
        String date = getDate();
        Toast.makeText(this, date , Toast.LENGTH_SHORT).show();
        initRetrofit(date);
    }

    public void pick(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
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
}