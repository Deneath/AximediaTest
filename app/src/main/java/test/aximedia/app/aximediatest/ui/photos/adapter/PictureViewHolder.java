package test.aximedia.app.aximediatest.ui.photos.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import test.aximedia.app.aximediatest.R;
import test.aximedia.app.aximediatest.data.Picture;
import test.aximedia.app.aximediatest.helpers.GlideApp;

class PictureViewHolder extends RecyclerView.ViewHolder {

    private ImageView pictureImageView;

    PictureViewHolder(View itemView) {
        super(itemView);
        pictureImageView = itemView.findViewById(R.id.pictureImageView);
    }

    void bind(Picture picture) {
        WindowManager wm = (WindowManager) itemView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int imageSize = width / 3;
        GlideApp.with(itemView.getContext())
                .load(picture.getPath())
                .centerCrop()
                .override(imageSize)
                .into(pictureImageView);
    }
}
