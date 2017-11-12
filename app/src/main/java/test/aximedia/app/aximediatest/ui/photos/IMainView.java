package test.aximedia.app.aximediatest.ui.photos;

import java.util.List;

import test.aximedia.app.aximediatest.base.IView;
import test.aximedia.app.aximediatest.data.Picture;

public interface IMainView extends IView {
    void showPhotoPicker();

    void notifyItemInserted(Picture picture);

    void initViews();

    void showPhotos(List<Picture> pictures);

    void setRemoveButtonVisibility(boolean isVisible);

    void notifyItemRemoved(Picture picture);
}
