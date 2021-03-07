package android.example.party.viewModel;

import android.app.Application;
import android.example.party.Person;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.example.party.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivityViewModel extends AndroidViewModel {
    private Application mApp;
    private Person mInviter;

    private static final String PARTY_IMAGE_URL = "https://i.imgur.com/uPMGVt1.jpg";

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mApp = application;
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
        ArrayList<Person> guests = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(Objects.requireNonNull(loadJSONFromAsset()));
            JSONArray jsonArray = obj.getJSONArray("people");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Person person = new Person(jsonObject.getString("name"), jsonObject.getString("url"));
                guests.add(person);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (Person person : guests) {
            if (person.getName().equals("Кристина")){
                mInviter = person;
                guests.remove(person);
            }
        }
        people.setValue(guests);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = mApp.getApplicationContext().getResources().openRawResource(R.raw.guests);
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

//    public List<Person> readPeopleFromJSON(String json){
//        StringReader reader = new StringReader(json);
//        ObjectMapper mapper = new ObjectMapper();
//        ArrayList <Person> people = null;
//        try {
//            people = mapper.readValue(reader, mapper.getTypeFactory().constructCollectionType(List.class, Person.class));
    //people =Arrays.asList(mapper.readValue(json, Person[].class))
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return people;
//    }



    public Bitmap downLoadPicture(String url) {

        Bitmap bitmap = null;
        try {
            URL connection = new URL(url);
            HttpURLConnection urlConnection;
            urlConnection = (HttpURLConnection) connection.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Person getInviter() {
        return mInviter;
    }
}


