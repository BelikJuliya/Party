package android.example.party.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.example.party.R;
import android.example.party.databinding.ActivityMainBinding;
import android.example.party.viewModel.MainActivityViewModel;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.example.party.viewModel.MainActivityViewModel.tryToGetBitmapFromCache;


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

        viewModel.initLRU();

        //if there is a bitmap for main picture in cache, get it from there, otherwise download it from internet
        Bitmap mainPictBm = tryToGetBitmapFromCache(viewModel.readMainPictUrl());
        if (mainPictBm == null) {
            viewModel.loadBitmap(viewModel.readMainPictUrl(), mActivityMainBinding.partyPictImageView);
        } else {
            mActivityMainBinding.partyPictImageView.setImageBitmap(mainPictBm);
        }

        RecyclerView recyclerView = mActivityMainBinding.recycleView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getPeople().observe(this, people -> {
            people.forEach(person -> {
                if (person.isInviter()) {
                    inviterName.setText(getString(R.string.invite) + " " + person.getName());

                    //if there is a bitmap for inviter in cache, get it from there, otherwise download it from internet
                    Bitmap inviterBm = tryToGetBitmapFromCache(person.getUrl());
                    if (inviterBm == null) {
                        viewModel.loadBitmap(person.getUrl(), inviterAvatar);
                    } else {
                        inviterAvatar.setImageBitmap(null);
                    }
                }
                viewModel.loadBitmapForRecycler(recyclerView);
            });
        });
    }
}
