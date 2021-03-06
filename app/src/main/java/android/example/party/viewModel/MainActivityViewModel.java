package android.example.party.viewModel;

import android.example.party.Person;
import android.example.party.view.MainActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

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

public class MainActivityViewModel {
    private MainActivity mActivity;
    private Person mInviter;
    private static final String PARTY_IMAGE_URL =
            "https://st4.depositphotos.com/5586578/26114/i/450/depositphotos_261146100-stock-photo-girls-party-champagne-glasses-special.jpg";

    public MainActivityViewModel(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<Person> readGuests() {
        ArrayList<Person> people = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(Objects.requireNonNull(loadJSONFromAsset()));
            JSONArray jsonArray = obj.getJSONArray("people");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Person person = new Person(jsonObject.getString("name"), jsonObject.getString("url"));
                people.add(person);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (Person person : people) {
            if (person.getName().equals("Кристина")){
                mInviter = person;
                people.remove(person);
            }
        }

        return people;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = mActivity.getResources().openRawResource(R.raw.guests);
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
