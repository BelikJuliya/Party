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
import android.util.Pair;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private static final String TAG = "Connection";
    private Person mInviter;
    private MainRepository mRepository;
    private static LruCache<String, Bitmap> memoryCache;
    private Application mApp;
    private RecyclerViewAdapter mAdapter;
    private MutableLiveData<List<Person>> mPeople;
    private HashMap<String, Bitmap> bitmaps = new HashMap<>();
//    private MutableLiveData<Person> mInviterr;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MainRepository(application);
        mApp = application;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public LiveData<List<Person>> getPeople() {
        if (mPeople == null) {
            mPeople = new MutableLiveData<>();
//            readGuests();
            mPeople.setValue(mRepository.readPeople());
        }
        return mPeople;
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public LiveData<HashMap<String, Bitmap>> getBitmap() {
//        if (bitmapsForRecycler == null) {
//            bitmapsForRecycler = new MutableLiveData<>();
////            readGuests();
////            loadBitmapForRecycler();
//        }
//        return bitmapsForRecycler;
//    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void readGuests() {
//        ArrayList<Person> guests = (ArrayList<Person>) mRepository.readPeople();
////        for (Person person : guests) {
////            if (person.isInviter()) {
////                mInviter = person;
////                guests.remove(person);
////            }
////        }
//        mPeople.setValue(guests);
//    }

    public Person getInviter() {
        return mInviter;
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

    public void loadBitmapForRecycler(List<Person> people) {
        if (checkNetwork()){
            for (int i = 0; i < people.size(); i++) {
                RecyclerAsyncTask task = new RecyclerAsyncTask(mAdapter, i);
                task.execute(people.get(i).getAvatar());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readMainPictUrl() {
        return mRepository.readMainPictureUrl();
    }

    static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public void setAdapter(RecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }

    private static Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

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

    public void setNewBitmap(Pair<String, Bitmap> pair) {
        if (!bitmaps.containsKey(pair.first)) {
            bitmaps.put(pair.first, pair.second);
//            bitmapsForRecycler.setValue(bitmaps);
        }
    }
}


