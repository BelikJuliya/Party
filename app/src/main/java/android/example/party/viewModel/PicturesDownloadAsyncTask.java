package android.example.party.viewModel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import static android.example.party.viewModel.MainActivityViewModel.addBitmapToMemoryCache;

public class PicturesDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private final String TAG = this.getClass().getSimpleName();
    private static final int SUCCESS = 200;

    @Override
    protected Bitmap doInBackground(String... strings) {

        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        try {
            URL url = new URL(strings[0]);
            Log.i(TAG, "doInBackground: trying to connect to " + url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == SUCCESS) {
                Log.i(TAG, "doInBackground: connected successfully");
            } else {
                Log.e(TAG, "doInBackground: connection failed, error: " + responseCode);
            }
            in = new BufferedInputStream(urlConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(in);
            addBitmapToMemoryCache(strings[0], bitmap);

        } catch (UnknownHostException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}