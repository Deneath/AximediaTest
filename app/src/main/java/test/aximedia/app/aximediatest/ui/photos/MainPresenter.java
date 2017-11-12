package test.aximedia.app.aximediatest.ui.photos;

import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.inject.Inject;

import test.aximedia.app.aximediatest.base.BasePresenter;
import test.aximedia.app.aximediatest.data.DataManager;
import test.aximedia.app.aximediatest.data.Picture;
import test.aximedia.app.aximediatest.helpers.PickerDispatcher;

public class MainPresenter extends BasePresenter<IMainView> {

    private PickerDispatcher pickerDispatcher;
    private DataManager dataManager;

    @Inject
    public MainPresenter(PickerDispatcher pickerDispatcher, DataManager dataManager) {
        this.pickerDispatcher = pickerDispatcher;
        this.dataManager = dataManager;
    }

    void init() {
        view.initViews();
        dataManager.getPictures().subscribe(pictures -> view.showPhotos(pictures), throwable -> view.showMessage("Can't load photos"));
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
            copyFile(file, newFile);
            Picture picture = new Picture(newFile.getPath());
            dataManager.insertPicture(picture).subscribe(newPicture -> {
                view.notifyItemInserted(newPicture);
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source;
        FileChannel destination;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (source != null) {
            destination.transferFrom(source, 0, source.size());
            source.close();
        }
        destination.close();

    }
}
