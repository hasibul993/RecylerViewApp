package com.recyclerviewapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;
import com.recyclerviewapp.Adapter.CustomAdapter;
import com.recyclerviewapp.Database.DatabaseAccess;
import com.recyclerviewapp.Helper.DividerItemDecoration;
import com.recyclerviewapp.Helper.GridDividerDecoration;
import com.recyclerviewapp.Model.Model;
import com.recyclerviewapp.rest.ApiClient;
import com.recyclerviewapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    EditText searchET;
    Gson gson = new Gson();
    ArrayList<Model> modelArrayList = new ArrayList<>();
    DatabaseAccess databaseAccess;
    private SharedPreferences sharedpreferences;
    ApiInterface apiService;

    ProgressDialog progressDialog;

    public static final String mypreference = "mySongs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchET = (EditText) findViewById(R.id.searchET);
        //searchET.setFocusable(false);

        InitDailog();

        databaseAccess = DatabaseAccess.getInstance(MainActivity.this);

        modelArrayList = databaseAccess.GetSongListFromSongsDB(null, false);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        SetAdapter(modelArrayList);

        searchET.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (count >= 2) {
                        GetSearchSongsLocally(s.toString(), true);
                    } else {
                        modelArrayList = databaseAccess.GetSongListFromSongsDB(null, false);
                        customAdapter.UpdateList(modelArrayList);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        GetDataFromServer();
        //  GetMthode();
        if (isFirstLoadSuccess()) {
            // GetSearchSongsLocally(null, false);
        } else {
            //if (Utility.getInstance().IsInternetConnected(MainActivity.this))
            //  GetDataFromServer();
            //else
            //  Toast.makeText(MainActivity.this, "Please check your internet", Toast.LENGTH_SHORT).show();

        }

    }

    private void SetAdapter(ArrayList<Model> modelArrayList) {
        try {
            customAdapter = new CustomAdapter(MainActivity.this, modelArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL_LIST, 0, 1));
            recyclerView.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void SetGridAdapter(ArrayList<Model> modelArrayList) {
        try {
            customAdapter = new CustomAdapter(MainActivity.this, modelArrayList);
            // set a GridLayoutManager with default vertical orientation and 2 number of columns
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
            recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Vertical Orientation
            recyclerView.addItemDecoration(new GridDividerDecoration(MainActivity.this));
            recyclerView.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void GetSearchSongsLocally(String searchText, boolean isLimit) {
        try {
            modelArrayList = databaseAccess.GetSongListFromSongsDB(searchText, isLimit);
            customAdapter.UpdateList(modelArrayList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void GetDataFromServer() {
        try {

            progressDialog.show();

            apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<List<Model>> call = apiService.getSongList();
            call.enqueue(new Callback<List<Model>>() {
                @Override
                public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {

                    int statusCode = response.code();
                    List<Model> modelArrayList = response.body();
                    customAdapter.UpdateList(modelArrayList);
                    databaseAccess.InsertSongsInBatchInMasterDB(modelArrayList);
                    SaveFirstLoad();
                    Log.e("TAG", response.toString());
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Model>> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("TAG", t.toString());
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (progressDialog != null)
                progressDialog.dismiss();
        }
    }


    private void GetMthode() {
        try {

            progressDialog.show();

            apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<Model> call = apiService.getMovieDetails("123");
            call.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, Response<Model> response) {
                    int statusCode = response.code();
                    Model modelArrayList = response.body();
                    Log.e("TAG", response.toString());

                    if (progressDialog != null)
                        progressDialog.dismiss();

                }

                @Override
                public void onFailure(Call<Model> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("TAG", t.toString());
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void SaveFirstLoad() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("IsFirstLoadSucces", true);
        editor.commit();
    }

    public boolean isFirstLoadSuccess() {
        return sharedpreferences.getBoolean("IsFirstLoadSucces", false);
    }

    private void InitDailog() {
        try {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("loading....");
          // progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // show it

        } catch (Exception ex) {

        }
    }
}
