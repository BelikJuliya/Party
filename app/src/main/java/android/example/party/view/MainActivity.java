package android.example.party.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.example.party.PicturesDownloadAsyncTask;
import android.example.party.R;
import android.example.party.adapters.RecyclerViewAdapter;
import android.example.party.databinding.ActivityMainBinding;
import android.example.party.databinding.InviterBinding;
import android.example.party.viewModel.MainActivityViewModel;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mViewModel;
    public ActivityMainBinding mActivityMainBinding;
//    public ImageView imageView;
    private static final String PARTY_IMAGE_URL =
           "https://i.imgur.com/uPMGVt1.jpg";

    public MainActivity() {
        mViewModel = new MainActivityViewModel(this);
    }

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
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mViewModel.readGuests());
        recyclerView.setAdapter(adapter);

        startDownloading(mActivityMainBinding.partyPictIv);

        ImageView inviterAvatar = findViewById(R.id.inviter_avatar);
        TextView inviterMessage = findViewById(R.id.inviter_name);
        String inviterName = mViewModel.getInviter().getName();
        inviterMessage.setText(getString(R.string.invite) + " " + inviterName);
        Picasso.get().load(mViewModel.getInviter().getAvatar()).into(inviterAvatar);
        //Glide.with(this).load(mViewModel.getInviter().getAvatar()).into(inviterAvatar);

//        imageView = mActivityMainBinding.partyPictIv;



        //inviter.inviterNameIv.setText(R.string.invite + " " + inviterName);
        //Picasso.get().load(mViewModel.getInviter().getAvatar()).into(inviter.inviterAvatarIv);



//        Picasso.get().load(PARTY_IMAGE_URL).into(activityMainBinding.partyPictIv);
//        mViewModel.
//        activityMainBinding.partyPictIv.setImageBitmap(mViewModel.downLoadPicture(PARTY_IMAGE_URL));
    }

//    @SuppressLint("SetTextI18n")
//    @Override
//    protected void onResume() {
//        super.onResume();
////        ImageView inviterAvatar = findViewById(R.id.inviter_avatar);
////        TextView inviterMessage = findViewById(R.id.inviter_name);
////        String inviterName = mViewModel.getInviter().getName();
////        inviterMessage.setText(R.string.invite + " " + inviterName);
////        Glide.with(this).load(mViewModel.getInviter().getAvatar()).into(inviterAvatar);
//
//    InviterBinding inviter = InviterBinding.inflate(getLayoutInflater());
//    String inviterName = mViewModel.getInviter().getName();
//        inviter.inviterName.setText(R.string.invite + " " + inviterName);
//        Picasso.get().load(mViewModel.getInviter().getAvatar()).into(inviter.inviterAvatar);
//    }

    public void startDownloading(ImageView view) {
        PicturesDownloadAsyncTask task = new PicturesDownloadAsyncTask(this, view);
        task.execute(PARTY_IMAGE_URL);
    }
}
