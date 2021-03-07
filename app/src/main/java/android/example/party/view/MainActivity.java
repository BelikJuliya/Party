package android.example.party.view;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.example.party.PicturesDownloadAsyncTask;
import android.example.party.R;
import android.example.party.adapters.RecyclerViewAdapter;
import android.example.party.databinding.ActivityMainBinding;
import android.example.party.viewModel.MainActivityViewModel;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mActivityMainBinding;
    private static final String PARTY_IMAGE_URL = "https://i.imgur.com/uPMGVt1.jpg";


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        RecyclerView recyclerView = mActivityMainBinding.recycleView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MainActivityViewModel viewModel = new MainActivityViewModel(getApplication());
        viewModel.getPeople().observe(this, person -> {
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(person);
            recyclerView.setAdapter(adapter);
        });

        startDownloading(mActivityMainBinding.partyPictIv);

        ImageView inviterAvatar = findViewById(R.id.inviter_avatar);
        TextView inviterMessage = findViewById(R.id.inviter_name);
        String inviterName = viewModel.getInviter().getName();
        inviterMessage.setText(getString(R.string.invite) + " " + inviterName);
        Picasso.get().load(viewModel.getInviter().getAvatar()).into(inviterAvatar);
    }

    public void startDownloading(ImageView view) {
        PicturesDownloadAsyncTask task = new PicturesDownloadAsyncTask(this, view);
        task.execute(PARTY_IMAGE_URL);
    }
}
