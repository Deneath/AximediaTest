package test.aximedia.app.aximediatest.ui.photos.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import test.aximedia.app.aximediatest.R;
import test.aximedia.app.aximediatest.data.Picture;

class PictureViewHolder extends RecyclerView.ViewHolder {

    //@BindView(R.id.pictureImageView)
    ImageView pictureImageView;

    PictureViewHolder(View itemView) {
        super(itemView);
        //ButterKnife.bind(itemView);
        pictureImageView = itemView.findViewById(R.id.pictureImageView);
    }

    void bind(Picture picture) {
//        Uri uri = Uri.parse(picture.getPath());
//        pictureImageView.setImageURI(uri);
        Glide.with(itemView).load(picture.getPath()).into(pictureImageView);
    }
}
