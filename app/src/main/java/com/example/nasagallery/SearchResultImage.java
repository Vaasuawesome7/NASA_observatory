package com.example.nasagallery;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultImage extends AppCompatActivity {

    private String mNASA_ID;
    private String mNASADesc;
    private ImageView mNASAImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_image);

        mNASA_ID = "";
        mNASADesc = "";

        if (getIntent().getExtras() != null) {
            mNASA_ID = getIntent().getExtras().getString("id");
            mNASADesc = getIntent().getExtras().getString("desc");
        }

        mNASAImage = findViewById(R.id.nasa_image);

        initRetrofit();
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://images-api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NASA_JSON nasa_json = retrofit.create(NASA_JSON.class);
        Call<RecievedItems> call = nasa_json.getItemFromSearch(mNASA_ID);
        call.enqueue(new Callback<RecievedItems>() {
            @Override
            public void onResponse(Call<RecievedItems> call, Response<RecievedItems> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SearchResultImage.this, "error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                RecievedItems body = response.body();
                assert body!=null;
                String link = body.getCollection().getItems().get(0).getHref();
                Picasso.get().load(link).into(mNASAImage);
                System.out.println(link);
            }

            @Override
            public void onFailure(Call<RecievedItems> call, Throwable t) {
                Toast.makeText(SearchResultImage.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}