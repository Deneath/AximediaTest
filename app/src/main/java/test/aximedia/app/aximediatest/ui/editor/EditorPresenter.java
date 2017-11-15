package test.aximedia.app.aximediatest.ui.editor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import test.aximedia.app.aximediatest.base.BasePresenter;

import static android.content.ContentValues.TAG;

public class EditorPresenter extends BasePresenter<IEditorView> {

    private String picturePath;

    @Inject
    public EditorPresenter() {
    }

    public void init(String picturePath) {
        this.picturePath = picturePath;
        view.initViews();
        view.showImage(picturePath);
    }

    public void saveImage(Bitmap bitmap) {
        try {
            FileOutputStream fos = new FileOutputStream(picturePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        view.setResult(-1);
        view.finish();
    }

    public void undoButtonCLicked() {
        view.setResult(-1);
        view.finish();
    }

    public void backClicked() {
        view.setResult(-1);
        view.finish();
    }
}
