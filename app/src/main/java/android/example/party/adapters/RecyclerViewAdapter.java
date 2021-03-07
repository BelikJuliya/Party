package android.example.party.adapters;

import android.app.Activity;
import android.example.party.Person;
import android.example.party.PicturesDownloadAsyncTask;
import android.example.party.databinding.GuestBinding;
import android.example.party.view.MainActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.squareup.picasso.Picasso;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.GuestViewHolder> {
    private List<Person> mGuests;
    private static GuestBinding mGuestBinding;
    private MainActivity mActivity;

    public RecyclerViewAdapter(List<Person> demoItemForAdapters, MainActivity activity) {
        mGuests = demoItemForAdapters;
        mActivity = activity;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        mGuestBinding = GuestBinding.inflate(inflater, parent, false);
        return new GuestViewHolder(mGuestBinding);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        Person person = mGuests.get(position);
//        Picasso.get().load(person.getAvatar()).into(holder.mAvatar);
        PicturesDownloadAsyncTask task = new PicturesDownloadAsyncTask(mActivity);
        task.execute(person.getAvatar());
        try {
            holder.mAvatar.setImageBitmap(task.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        holder.mName.setText(person.getName());
    }

    @Override
    public int getItemCount() {
        return mGuests.size();
    }

    static class GuestViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        ImageView mAvatar;

        GuestViewHolder(@NonNull GuestBinding binding) {
            super(binding.getRoot());
            mGuestBinding = binding;
            mName = binding.guestNameTv;
            mAvatar = binding.guestAvatarIv;
        }
    }
}