package android.example.party.viewModel;

import android.app.Application;
import android.example.party.model.MainRepository;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivityViewModel extends AndroidViewModel {
    private Person mInviter;
    private MainRepository mRepository;
    private static LruCache<String, Bitmap> memoryCache;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MainRepository(application);
    }

    private MutableLiveData<List<Person>> mPeople;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public LiveData<List<Person>> getPeople() {
        if (mPeople == null) {
            mPeople = new MutableLiveData<>();
            readGuests();
        }
        return mPeople;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void readGuests() {
        ArrayList<Person> guests = (ArrayList<Person>) mRepository.readPeople();
        for (Person person : guests) {
            if (person.isInviter()) {
                mInviter = person;
                guests.remove(person);
            }
        }
        mPeople.setValue(guests);
    }

    public Person getInviter() {
        return mInviter;
    }

    public Bitmap initLRU() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        return null;
    }

    public Bitmap loadBitmap(String url) {
        final String imageKey = String.valueOf(url);

        Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            return bitmap;
        } else {
            PicturesDownloadAsyncTask task = new PicturesDownloadAsyncTask();
            task.execute(url);
            try {
                bitmap = task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readMainPictUrl(){
        return mRepository.readMainPictureUrl();
    }

    static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    private static Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }
}


