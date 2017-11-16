package test.aximedia.app.aximediatest.ui.editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import test.aximedia.app.aximediatest.base.BasePresenter;
import test.aximedia.app.aximediatest.helpers.FileManager;
import test.aximedia.app.aximediatest.helpers.PhotoHelper;

public class EditorPresenter extends BasePresenter<IEditorView> {

    private String picturePath;
    private int picturePosition;

    @Inject
    EditorPresenter() {
    }

    void init(String picturePath, int picturePosition) {
        this.picturePath = picturePath;
        this.picturePosition = picturePosition;
        view.initViews();
        Bitmap bitmap = PhotoHelper.resizeBitmap(PhotoHelper.getBitmapFromPath(picturePath), 1920);
        view.showImage(bitmap);
    }

    void saveImage(Bitmap bitmap) {
        FileManager.saveBitmapToPath(bitmap, picturePath);
        finishView();
    }

    void undoButtonCLicked() {
        finishView();
    }

    void backClicked() {
        finishView();
    }

    private void finishView(){
        Intent intent = new Intent();
        intent.putExtra("position", picturePosition);
        view.setResult(-1, intent);
        view.finish();
    }

    void onSaveInstanceState(Bundle outState, List<RectF> rects, int width, int height) {
        String json = new Gson().toJson(rects);
        outState.putString("rects", json);
        outState.putInt("oldWidth", width);
        outState.putInt("oldHeight", height);
    }

    void onRestoreInstanceState(Bundle savedInstanceState) {
        Type type = new TypeToken<List<RectF>>() {}.getType();
        List<RectF> rects = new Gson().fromJson(savedInstanceState.getString("rects"), type);
        int oldWidth = savedInstanceState.getInt("oldWidth");
        int oldHeight = savedInstanceState.getInt("oldHeight");
        view.applyRestoredInstanceState(rects, oldWidth, oldHeight);
    }
}
