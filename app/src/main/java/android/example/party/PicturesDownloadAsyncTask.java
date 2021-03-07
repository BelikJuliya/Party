package android.example.party;

import android.example.party.view.MainActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class PicturesDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "Picture";
    private WeakReference<MainActivity> mActivity;
    private ImageView mImageView;


    public PicturesDownloadAsyncTask(MainActivity activity, ImageView image) {
        mActivity = new WeakReference<>(activity);
        mImageView = image;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = null;

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strings[0]);
            Log.d(TAG, "doInBackground: trying to connect");
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            Log.d(TAG, "doInBackground: " + responseCode);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
            e.printStackTrace();
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }
}