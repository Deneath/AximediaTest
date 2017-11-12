package test.aximedia.app.aximediatest.ui.photos.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import test.aximedia.app.aximediatest.R;
import test.aximedia.app.aximediatest.data.Picture;

public class PictureAdapter extends RecyclerView.Adapter<PictureViewHolder> {

    private List<Picture> pictures;
    private PictureViewHolder.IOnPictureActionsListener listener;

    public PictureAdapter(List<Picture> pictures, PictureViewHolder.IOnPictureActionsListener listener) {
        this.pictures = pictures;
        this.listener = listener;
    }

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_picture, parent, false);
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PictureViewHolder holder, int position) {
        holder.bind(pictures.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public void addItem(Picture picture) {
        pictures.add(picture);
        notifyItemInserted(pictures.size() - 1);
    }

    public void removeItem(Picture picture) {
        int position = pictures.indexOf(picture);
        pictures.remove(picture);
        notifyItemRemoved(position);
    }
}
