package test.aximedia.app.aximediatest.ui.editor;

import test.aximedia.app.aximediatest.base.IView;

public interface IEditorView extends IView {
    void showImage(String bitmap);
    void initViews();

    void finish();

    void setResult(int i);
}
