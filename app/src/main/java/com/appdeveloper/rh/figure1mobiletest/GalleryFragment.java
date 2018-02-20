package com.appdeveloper.rh.figure1mobiletest;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    RecyclerView rv;
    EditText searchET;

    GalleryAdapter adapter;
    private OkHttpClient httpClient;
    ArrayList<ImageObject> imagesList = new ArrayList<>();

    int mImagesSize = 0;
    int mLastPosition = 0;
    int mPosition = 0;
    int mPage = 0;
    String mSearch = "";
    String mUrl = "";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        prefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = prefs.edit();

        mSearch = prefs.getString("search", "funny");
        mPage = prefs.getInt("page", 0);
        mPosition = prefs.getInt("position", 0);

        rv = (RecyclerView) v.findViewById(R.id.imageRV);
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 20;
            }
        });

        searchET = (EditText) v.findViewById(R.id.SearchET);
        searchET.setText(mSearch);

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.notifyDataSetChanged();
                mSearch = s.toString();
                editor.putString("search", mSearch);
                editor.commit();
                setUrl(mSearch, mPage);
                imagesList.clear();
                fetchData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = ((LinearLayoutManager) rv.getLayoutManager());
                mLastPosition = layoutManager.findLastVisibleItemPosition();
                if (mLastPosition >= mImagesSize - 2) {
                    mImagesSize = 0;
                    Log.e("test", "loading more!");
                    loadMore();
                }
            }
        });

        setUrl(mSearch, mPage);
        fetchData();

        return v;
    }

    private void setUrl(String search, int page) {
        mUrl = "https://api.imgur.com/3/gallery/r/" + search + "/" + page + ".json";
    }

    private void fetchData() {
        httpClient = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url(mUrl)
                .header("Authorization", "Client-ID e0d9382b96f900b")
                .header("User-Agent", "Figure1Test")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "An error has occurred " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    Log.e("JSON", data.toString());
                    JSONArray items = data.getJSONArray("data");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        ImageObject image = new ImageObject();
                        if (item.getBoolean("is_album")) {
                            image.id = item.getString("cover");
                        } else {
                            image.id = item.getString("id");
                        }
                        image.title = item.getString("title");

                        imagesList.add(image); // Add photo to list
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        render(imagesList);
                    }
                });
            }
        });
    }

    private void render(final ArrayList<ImageObject> images) {
        mImagesSize = images.size();
        adapter = new GalleryAdapter(getActivity().getBaseContext(), images);

        rv.setAdapter(adapter);
        rv.scrollToPosition(mLastPosition - 2);

        adapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                ImageObject objectToPass = images.get(position);
                Intent intent = new Intent(getActivity().getBaseContext(), DetailActivity.class);
                intent.putExtra("theObject", objectToPass);
                startActivity(intent);
            }
        });
    }

    private void loadMore() {
        setUrl(mSearch, mPage++);
        fetchData();
    }
}
