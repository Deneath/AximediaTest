package test.aximedia.app.aximediatest.ui.editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;

import java.util.List;

import test.aximedia.app.aximediatest.base.IView;

public interface IEditorView extends IView {
    void showImage(Bitmap bitmap);
    void initViews();

    void finish();

    void setResult(int i, Intent intent);

    void applyRestoredInstanceState(List<RectF> rects, int oldWidth, int oldHeight);
}
