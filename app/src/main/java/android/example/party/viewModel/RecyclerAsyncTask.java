package android.example.party.viewModel;

import android.example.party.view.adapters.RecyclerViewAdapter;
import android.graphics.Bitmap;

public class RecyclerAsyncTask extends PicturesDownloadAsyncTask {
    private RecyclerViewAdapter mAdapter;
    private int id;

    public RecyclerAsyncTask(RecyclerViewAdapter adapter, int id) {
        super(adapter);
        mAdapter = adapter;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mAdapter.setNewBitmap(bitmap, id);
    }
}
