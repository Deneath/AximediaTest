package test.aximedia.app.aximediatest.ui.photos;

import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import test.aximedia.app.aximediatest.base.BasePresenter;
import test.aximedia.app.aximediatest.data.DataManager;
import test.aximedia.app.aximediatest.data.Picture;
import test.aximedia.app.aximediatest.helpers.FileManager;
import test.aximedia.app.aximediatest.helpers.PickerDispatcher;

public class MainPresenter extends BasePresenter<IMainView> {

    private PickerDispatcher pickerDispatcher;
    private DataManager dataManager;
    private boolean isSelectModeEnabled;
    private List<Picture> selectedPictures = new ArrayList<>();

    @Inject
    public MainPresenter(PickerDispatcher pickerDispatcher, DataManager dataManager) {
        this.pickerDispatcher = pickerDispatcher;
        this.dataManager = dataManager;
    }

    void init() {
        view.initViews();
        dataManager.getPictures().subscribe(pictures -> view.showPhotos(pictures),
                throwable -> view.showMessage("Can't load photos"));
    }

    void onFloatingActionButtonClicked() {
        view.showPhotoPicker();
    }

    void onImagePicked(Uri imageUri) {
        String fileName = pickerDispatcher.getFilePath(imageUri);

        File file = new File(fileName);
        try {
            file.createNewFile();
            File newFile = pickerDispatcher.getPhotoFile();
            FileManager.copyFile(file, newFile);
            Picture picture = new Picture(newFile.getPath());
            dataManager.insertPicture(picture)
                    .subscribe(this::notifyViewItemInserted,
                            throwable -> view.showMessage("Can't add photo"));
        } catch (IOException e) {
            e.printStackTrace();
            view.showMessage("Can't add photo");
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Can't add photo");
        }
    }

    private void notifyViewItemInserted(Picture picture) {
        view.notifyItemInserted(picture);
    }

    void onPictureSelected(Picture picture) {
        if (selectedPictures.size() == 0) {
            setSelectModeEnabled(true);
        }
        selectedPictures.add(picture);
    }

    void onPictureDeselected(Picture picture) {
        selectedPictures.remove(picture);
        if (selectedPictures.size() == 0) {
            setSelectModeEnabled(false);
        }
    }

    boolean isSelectModeEnabled() {
        return isSelectModeEnabled;
    }

    private void setSelectModeEnabled(boolean isEnabled){
        isSelectModeEnabled = isEnabled;
        view.setRemoveButtonVisibility(isEnabled);
    }

    void removeButtonClicked() {
        dataManager.removePictures(selectedPictures).subscribe(aBoolean -> {
            if(!aBoolean){
                view.showMessage("Cannot remove pictures");
            }
            for(Picture picture : selectedPictures){
                view.notifyItemRemoved(picture);
                File file = new File(picture.getPath());
                boolean deleted = file.delete();
            }
            selectedPictures.clear();
            setSelectModeEnabled(false);
        }, throwable -> view.showMessage("Cannot remove pictures"));
    }
}
