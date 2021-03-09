package android.example.party.model;

import android.app.Application;
import android.content.Context;
import android.example.party.viewModel.Person;
import android.os.Build;

import androidx.annotation.RequiresApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainRepository {
    private Context mContext;
    private JsonDataSource mSource;

    public MainRepository(Application context) {
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<Person> readPeople() {
        ArrayList<Person> guests = new ArrayList<>();
        try {
            mSource = new JsonDataSource(mContext);
            JSONObject obj = new JSONObject(Objects.requireNonNull(mSource.getJson()));
            JSONArray jsonArray = obj.getJSONArray("people");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Person person = new Person(jsonObject.getString("name"),
                        jsonObject.getString("url"), jsonObject.getBoolean("isInviter"));
                guests.add(person);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return guests;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readMainPictureUrl() {
        String url = null;
        mSource = new JsonDataSource(mContext);
        try {
            JSONObject obj = new JSONObject(Objects.requireNonNull(mSource.getJson()));
            JSONArray jsonArray = obj.getJSONArray("mainPicture");
            for (int i = 0; i < jsonArray.length(); i++) {
                url = jsonArray.getJSONObject(i).getString("url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }
}
