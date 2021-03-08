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
    private Application mApp;
    private Person mInviter;
    private static final String PARTY_IMAGE_URL = "https://i.imgur.com/uPMGVt1.jpg";
    private MainRepository mRepository;
    private LruCache<String, Bitmap> memoryCache;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mApp = application;
        mRepository = new MainRepository(application);
    }

    private MutableLiveData<List<Person>> people;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public LiveData<List<Person>> getPeople() {
        if (people == null) {
            people = new MutableLiveData<>();
            readGuests();
        }
        return people;
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
        people.setValue(guests);
    }

    public Person getInviter() {
        return mInviter;
    }

    public Bitmap downloadInviterAvatar() {
        Bitmap bitmap = null;
        PicturesDownloadAsyncTask task = new PicturesDownloadAsyncTask();
        task.execute(mInviter.getAvatar());
        try {
            bitmap = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap downloadMainPicture() {
        Bitmap bitmap = null;
        PicturesDownloadAsyncTask task = new PicturesDownloadAsyncTask();
        task.execute(PARTY_IMAGE_URL);
        try {
            bitmap = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap checkIfPicturePresent(String name) {
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

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }
}


