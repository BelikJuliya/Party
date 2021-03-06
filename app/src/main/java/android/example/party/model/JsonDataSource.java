package android.example.party.model;

import android.app.Application;
import android.example.party.R;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class JsonDataSource {
    private Application mAppContext;
    private String mJson;
    private MainRepository mRepository;

    JsonDataSource(MainRepository repository) {
        mRepository = repository;
        mAppContext = mRepository.getAppContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    String loadJSONFromAsset() {
        if (mJson == null) {
            try {
                InputStream is = mAppContext.getApplicationContext().getResources().openRawResource(R.raw.guests);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                mJson = new String(buffer, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return mJson;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    String getJson() {
        loadJSONFromAsset();
        return mJson;
    }
}
