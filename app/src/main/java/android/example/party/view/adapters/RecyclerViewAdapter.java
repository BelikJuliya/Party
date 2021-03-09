package android.example.party.view.adapters;

import android.example.party.viewModel.MainActivityViewModel;
import android.example.party.viewModel.Person;
import android.example.party.databinding.GuestBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.GuestViewHolder> {
    private List<Person> mGuests;
    private static GuestBinding mGuestBinding;
    private MainActivityViewModel mViewModel;

    public RecyclerViewAdapter(List<Person> demoItemForAdapters, MainActivityViewModel viewModel) {
        mGuests = demoItemForAdapters;
        mViewModel = viewModel;
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
        //holder.mAvatar.setImageBitmap(mViewModel.loadBitmap(person.getAvatar()));
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