package com.example.nasagallery;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image_and_video);

        mNASAItems = findViewById(R.id.nasa_items);
        mSearchBar = findViewById(R.id.search);

        mSearchItems = new ArrayList<>();
        mNASA_IDs = new ArrayList<>();
        mNASADesc = new ArrayList<>();

        adapter = new MyAdapter(mSearchItems);
        setListener();
        mNASAItems.setHasFixedSize(true);
        mNASAItems.setLayoutManager(new LinearLayoutManager(this));
        mNASAItems.setAdapter(adapter);

        implementSearch();
    }

    private void setListener() {
        adapter.setListener(new MyAdapter.OnNASAItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent i = new Intent(NASAImageAndVideo.this, SearchResultImage.class);
                i.putExtra("id", mNASA_IDs.get(pos));
                i.putExtra("desc", mNASADesc.get(pos));
                startActivity(i);
            }
        });
    }

    private void implementSearch() {
        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initRetrofit(s.toString());
            }
        });
    }

    private void initRetrofit(String text) {
        mSearchItems.clear();
        mNASADesc.clear();
        mNASA_IDs.clear();

        adapter.notifyDataSetChanged();

        if (text.equals("")) {
            adapter.notifyDataSetChanged();
            return;
        }

        System.out.println("continue");
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
                System.out.println(text + " " + body.getCollection().getItems().size());
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
}