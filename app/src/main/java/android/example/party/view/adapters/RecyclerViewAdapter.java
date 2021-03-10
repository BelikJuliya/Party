package android.example.party.view.adapters;

import android.example.party.viewModel.Person;
import android.example.party.databinding.GuestBinding;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.GuestViewHolder> {
    private List<Person> mGuests;
    private static GuestBinding mGuestBinding;
    private Bitmap mBitmap;
    private HashMap<Integer, Bitmap> map;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public RecyclerViewAdapter(List<Person> demoItemForAdapters) {
        mGuests = demoItemForAdapters;
//        mGuests = demoItemForAdapters.stream().filter(Person::isInviter).collect(Collectors.toList());
//        mGuests = demoItemForAdapters.stream().map(if(person -> person.isInviter()))
//        demoItemForAdapters.forEach(person -> {
//            if (!person.isInviter()) mGuests.remove(person);
//        });

//        demoItemForAdapters.stream().filter(person -> {
//            if (!person.isInviter())
//        })
//        mGuests = demoItemForAdapters.stream().filter(person -> !person.isInviter()).collect(Collectors.toList());
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
        holder.mAvatar.setImageBitmap(mBitmap);
        holder.mName.setText(mGuests.get(position).getName());
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

    public void setNewBitmap(Bitmap bitmap, int position) {
        map.put(position, bitmap);
//        this.mBitmap = mBitmap;
        notifyDataSetChanged();
    }
}