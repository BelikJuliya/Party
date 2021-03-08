package android.example.party.model;

import android.content.Context;
import android.example.party.R;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonDataSource {

    private Context mContext;

    public JsonDataSource(Context context) {
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
     String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = mContext.getApplicationContext().getResources().openRawResource(R.raw.guests);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
