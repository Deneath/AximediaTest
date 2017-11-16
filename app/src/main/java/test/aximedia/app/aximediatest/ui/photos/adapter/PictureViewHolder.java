package test.aximedia.app.aximediatest.ui.photos.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import test.aximedia.app.aximediatest.R;
import test.aximedia.app.aximediatest.data.Picture;
import test.aximedia.app.aximediatest.helpers.GlideApp;

public class PictureViewHolder extends RecyclerView.ViewHolder {

    private ImageView pictureImageView;
    private ImageView checkImageView;

    PictureViewHolder(View itemView) {
        super(itemView);
        pictureImageView = itemView.findViewById(R.id.pictureImageView);
        checkImageView = itemView.findViewById(R.id.checkImageView);
        checkImageView.setVisibility(View.GONE);
    }

    void bind(Picture picture, IOnPictureActionsListener listener) {
        if(!listener.isSelectModeEnabled()) checkImageView.setVisibility(View.GONE);

        pictureImageView.setOnLongClickListener(view -> {
            checkImageView.setVisibility(View.GONE);
            if (listener.isSelectModeEnabled()) {
                if (checkImageView.getVisibility() == View.GONE) {
                    checkImageView.setVisibility(View.VISIBLE);
                    listener.onPictureSelected(picture);
                } else {
                    checkImageView.setVisibility(View.GONE);
                    listener.onPictureDeselected(picture);
                }
            } else {
                checkImageView.setVisibility(View.VISIBLE);
                listener.onPictureSelected(picture);
            }
            return true;
        });

        pictureImageView.setOnClickListener(view -> {
            if (listener.isSelectModeEnabled()) {
                if (checkImageView.getVisibility() == View.GONE) {
                    checkImageView.setVisibility(View.VISIBLE);
                    listener.onPictureSelected(picture);
                } else {
                    checkImageView.setVisibility(View.GONE);
                    listener.onPictureDeselected(picture);
                }
            } else
                listener.openEditor(picture, getAdapterPosition());
        });

        WindowManager wm = (WindowManager) itemView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int imageSize = width / 3;
        GlideApp.with(itemView.getContext())
                .load(picture.getPath())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .override(imageSize)
                .into(pictureImageView);
    }

    public interface IOnPictureActionsListener {
        void onPictureSelected(Picture picture);

        void onPictureDeselected(Picture picture);

        boolean isSelectModeEnabled();

        void openEditor(Picture picture, int position);
    }
}
