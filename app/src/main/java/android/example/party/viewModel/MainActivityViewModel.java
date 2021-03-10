package android.example.party.viewModel;

import android.app.Application;
import android.content.Context;
import android.example.party.model.MainRepository;
import android.example.party.view.adapters.RecyclerViewAdapter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private static final String TAG = "Connection";
    private MainRepository mRepository;
    private static LruCache<String, Bitmap> memoryCache;
    private Application mApp;
    private RecyclerViewAdapter mAdapter;
    private MutableLiveData<List<Person>> mPeople;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MainRepository(application);
        mApp = application;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public LiveData<List<Person>> getPeople() {
        if (mPeople == null) {
            mPeople = new MutableLiveData<>();
            mPeople.setValue(mRepository.readPeople());
        }
        return mPeople;
    }

    public void initLRU() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void loadBitmap(String url, ImageView imageView) {
        if (checkNetwork()) {
            //Bitmap bitmap = getBitmapFromMemCache(url);
//            if (bitmap != null) {
//                return bitmap;
//            } else {
            PicturesDownloadAsyncTask task = new PicturesDownloadAsyncTask(imageView);
            task.execute(url);
//                try {
//                    //bitmap = task.get();
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//                return bitmap;
        }
//        } else {
//            return null;
        //}
    }

    public void loadBitmapForRecycler(RecyclerView recyclerView) {
        if (checkNetwork()) {
            RecyclerAsyncTask task = new RecyclerAsyncTask(recyclerView);
            task.execute(mPeople.getValue());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readMainPictUrl() {
        return mRepository.readMainPictureUrl();
    }

    static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (tryToGetBitmapFromCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

//    private static Bitmap getBitmapFromMemCache(String key) {
//        return memoryCache.get(key);
//    }

    public boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) mApp.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            Log.d(TAG, "onCreate: internet connected");
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            Log.d(TAG, "onCreate: mobile internet is in use");
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            Log.d(TAG, "onCreate: WIFI is in use");
            return true;
        }
        return false;
    }

    public static Bitmap tryToGetBitmapFromCache(String url){
        return memoryCache.get(url);
    }
}


