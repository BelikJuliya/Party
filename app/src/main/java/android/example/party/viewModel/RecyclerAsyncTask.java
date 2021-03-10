package android.example.party.viewModel;

import android.example.party.view.adapters.RecyclerViewAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static android.example.party.viewModel.MainActivityViewModel.addBitmapToMemoryCache;

public class RecyclerAsyncTask extends AsyncTask<List<Person>, Void, List<Person>> {
    private WeakReference<RecyclerView> mRecyclerView;
    private final String TAG = this.getClass().getSimpleName();
    private static final int SUCCESS = 200;

    public RecyclerAsyncTask(RecyclerView recyclerView) {
        mRecyclerView = new WeakReference<>(recyclerView);
    }

    @Override
    protected List<Person> doInBackground(List<Person>... people) {
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        List<Person> guests = new ArrayList<>();
        for (int i = 0; i < people[0].size(); i++) {
            if (!people[0].get(i).isInviter()) {
                try {
                    URL url = new URL(people[0].get(i).getUrl());
                    Log.i(TAG, "doInBackground: trying to connect to " + url.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == SUCCESS) {
                        Log.i(TAG, "doInBackground: connected successfully");
                    } else {
                        Log.e(TAG, "doInBackground: connection failed, error: " + responseCode);
                    }
                    in = new BufferedInputStream(urlConnection.getInputStream());
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    guests.add(new Person(people[0].get(i).getName(), bitmap));

                    if (bitmap != null) {
                        addBitmapToMemoryCache(people[0].get(i).getUrl(), bitmap);
                    }

                } catch (UnknownHostException e) {
                    Log.d(TAG, "doInBackground: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return guests;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostExecute(List<Person> people) {
        mRecyclerView.get().setAdapter(new RecyclerViewAdapter(people));
    }
}
