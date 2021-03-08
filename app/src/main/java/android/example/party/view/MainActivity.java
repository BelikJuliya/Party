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
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mActivityMainBinding;
    private static final String TITLE = "Вечеринка";

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(TITLE);
        actionBar.setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = mActivityMainBinding.recycleView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MainActivityViewModel viewModel = new MainActivityViewModel(getApplication());
        viewModel.getPeople().observe(this, person -> {
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(person);
            recyclerView.setAdapter(adapter);
        });

        mActivityMainBinding.partyPictIv.setImageBitmap(viewModel.downloadMainPicture());

        ImageView inviterAvatar = findViewById(R.id.inviter_avatar);
        inviterAvatar.setImageBitmap(viewModel.downloadInviterAvatar());
        TextView inviterMessage = findViewById(R.id.inviter_name);
        String inviterName = viewModel.getInviter().getName();
        inviterMessage.setText(getString(R.string.invite) + " " + inviterName);
    }
}
