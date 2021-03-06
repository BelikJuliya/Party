package android.example.party.adapters;

import android.example.party.Person;
import android.example.party.R;
import android.example.party.databinding.GuestBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;

import com.squareup.picasso.Picasso;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.GuestViewHolder> {
    private List<Person> mGuests;
    private static GuestBinding mGuestBinding;

    public RecyclerViewAdapter(List<Person> demoItemForAdapters) {
        mGuests = demoItemForAdapters;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //mGuestBinding = DataBindingUtil.inflate(inflater, R.layout.guest, parent, false);
        //mGuestBinding = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest, parent, false);
        mGuestBinding = GuestBinding.inflate(inflater, parent, false);
        return new GuestViewHolder(mGuestBinding);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        Person person = mGuests.get(position);
        Picasso.get().load(person.getAvatar()).into(holder.mAvatar);
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