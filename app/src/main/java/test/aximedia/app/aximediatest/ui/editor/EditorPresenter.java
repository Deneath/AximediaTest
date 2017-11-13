package test.aximedia.app.aximediatest.ui.editor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import javax.inject.Inject;

import test.aximedia.app.aximediatest.base.BasePresenter;

public class EditorPresenter extends BasePresenter<IEditorView> {
    @Inject
    public EditorPresenter() {
    }

    public void init(String picturePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        view.showCanvas(bitmap);
    }
}
