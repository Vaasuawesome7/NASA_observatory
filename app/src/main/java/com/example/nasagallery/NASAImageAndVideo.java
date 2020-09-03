package com.example.nasagallery;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NASAImageAndVideo extends AppCompatActivity {

    private MyAdapter adapter;
    private RecyclerView mNASAItems;
    private ArrayList<String> mSearchItems, mNASA_IDs, mNASADesc;
    private EditText mSearchBar;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image_and_video);

        player = MediaPlayer.create(this, R.raw.sound2);

        mNASAItems = findViewById(R.id.nasa_items);
        mSearchBar = findViewById(R.id.search);

        mSearchBar.setOnClickListener(v -> player.start());

        mSearchItems = new ArrayList<>();
        mNASA_IDs = new ArrayList<>();
        mNASADesc = new ArrayList<>();

        adapter = new MyAdapter(mSearchItems);
        setListener();
        mNASAItems.setHasFixedSize(true);
        mNASAItems.setLayoutManager(new LinearLayoutManager(this));
        mNASAItems.setAdapter(adapter);

        implementSearch();

        initNew();
    }

    private void setListener() {
        adapter.setListener(pos -> {
            Intent i = new Intent(NASAImageAndVideo.this, SearchResultImage.class);
            i.putExtra("id", mNASA_IDs.get(pos));
            i.putExtra("desc", mNASADesc.get(pos));
            i.putExtra("name", mSearchItems.get(pos));
            player.start();
            startActivity(i);
            overridePendingTransition(R.anim.righttoleft, R.anim.righttoleft1);
        });
    }

    private void implementSearch() {
        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().equals("")) {
                    mSearchItems.clear();
                    mNASA_IDs.clear();
                    mNASADesc.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mSearchItems.clear();
                    mNASA_IDs.clear();
                    mNASADesc.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                initRetrofit(s.toString());
                if (s.toString().equals("")) {
                    mSearchItems.clear();
                    mNASA_IDs.clear();
                    mNASADesc.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initRetrofit(String text) {
        mSearchItems.clear();
        mNASADesc.clear();
        mNASA_IDs.clear();

        adapter.notifyDataSetChanged();

        if (text.equals("")) {
            mSearchItems.clear();
            mNASADesc.clear();
            mNASA_IDs.clear();

            adapter.notifyDataSetChanged();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://images-api.nasa.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NASA_JSON nasa_json = retrofit.create(NASA_JSON.class);
        Call<ImageAndVideo> call = nasa_json.getSearchResult(text);
        call.enqueue(new Callback<ImageAndVideo>() {
            @Override
            public void onResponse(Call<ImageAndVideo> call, Response<ImageAndVideo> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(NASAImageAndVideo.this, "error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ImageAndVideo body = response.body();
                assert body != null;
                for (int i = 0; i < body.getCollection().getItems().size(); i++) {
                    String name = body.getCollection().getItems().get(i).getData().get(0).getTitle();
                    String description = body.getCollection().getItems().get(i).getData().get(0).getDescription();
                    String nasa_id = body.getCollection().getItems().get(i).getData().get(0).getNasaId();
                    mSearchItems.add(name);
                    mNASADesc.add(description);
                    mNASA_IDs.add(nasa_id);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ImageAndVideo> call, Throwable t) {
                Toast.makeText(NASAImageAndVideo.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initNew() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setSelectedItemId(R.id.nav_library);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            player.start();
            switch (item.getItemId()) {
                case R.id.nav_apod:
                    startActivity(new Intent(getApplicationContext(), NASAAPOD.class));
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                    finish();
                    return true;
                case R.id.nav_library:
                    return true;
            }
            return false;
        });
    }
}