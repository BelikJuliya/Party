package android.example.party.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.example.party.R;
import android.example.party.view.adapters.RecyclerViewAdapter;
import android.example.party.databinding.ActivityMainBinding;
import android.example.party.viewModel.MainActivityViewModel;
import android.example.party.viewModel.Person;
import android.os.Build;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mActivityMainBinding;
    private static final String TITLE = "Вечеринка";

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityViewModel viewModel = new MainActivityViewModel(getApplication());

        if (!viewModel.checkNetwork()) {
            Toast.makeText(this, "Please check internet connection", Toast.LENGTH_SHORT).show();
        }

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(TITLE);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView inviterAvatar = findViewById(R.id.inviter_avatar);
        TextView inviterName = findViewById(R.id.inviter_name);
        viewModel.loadBitmap(viewModel.readMainPictUrl(), mActivityMainBinding.partyPictImageView);

        RecyclerView recyclerView = mActivityMainBinding.recycleView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel.initLRU();
        viewModel.getPeople().observe(this, people -> {
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(people);
            viewModel.setAdapter(adapter);
            viewModel.loadBitmapForRecycler(people);
            recyclerView.setAdapter(adapter);

            for (Person person : people) {
                if (person.isInviter()){
                    inviterName.setText(getString(R.string.invite) + " " + person.getName());
                    viewModel.loadBitmap(person.getAvatar(), inviterAvatar);
                }
            }
        });
    }
}
