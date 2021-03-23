package android.example.party.viewModel;

import android.app.Application;
import android.content.Context;
import android.example.party.model.MainRepository;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

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
    private MainRepository mRepository;
    private static LruCache<String, Bitmap> memoryCache;
    private Application mApp;
    private MutableLiveData<List<Person>> mPeople;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mApp = application;
        mRepository = new MainRepository(this);
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
        if (isNetworkAvailable()) {
            PicturesDownloadAsyncTask task = new PicturesDownloadAsyncTask(imageView);
            task.execute(url);
        }
    }

    public void loadBitmapForRecycler(RecyclerView recyclerView) {
        if (isNetworkAvailable()) {
            RecyclerAsyncTask task = new RecyclerAsyncTask(recyclerView);
            task.execute(mPeople.getValue());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readMainPictUrl() {
        return mRepository.readMainPictureUrl();
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (tryToGetBitmapFromCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public static Bitmap tryToGetBitmapFromCache(String url) {
        return memoryCache.get(url);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mApp.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public Application getAppContext() {
        return mApp;
    }
}


