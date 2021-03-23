package android.example.party.model;

import android.app.Application;
import android.example.party.viewModel.MainActivityViewModel;
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
    private JsonDataSource mSource;
    private MainActivityViewModel mViewModel;
    private Application mAppContext;

    public MainRepository(MainActivityViewModel viewModel) {
        mViewModel = viewModel;
        mAppContext = mViewModel.getAppContext();
        mSource = new JsonDataSource(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<Person> readPeople() {
        ArrayList<Person> guests = new ArrayList<>();
        try {
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

    public Application getAppContext() {
        return mAppContext;
    }
}
