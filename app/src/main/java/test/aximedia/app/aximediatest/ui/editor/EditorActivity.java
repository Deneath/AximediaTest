package test.aximedia.app.aximediatest.ui.editor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import javax.inject.Inject;

import butterknife.ButterKnife;
import test.aximedia.app.aximediatest.Application;
import test.aximedia.app.aximediatest.di.component.DaggerEditorActivityComponent;
import test.aximedia.app.aximediatest.di.component.EditorActivityComponent;
import test.aximedia.app.aximediatest.di.module.EditorActivityModule;
import test.aximedia.app.aximediatest.helpers.GlideApp;
import test.aximedia.app.aximediatest.helpers.PictureOrientation;
import test.aximedia.app.aximediatest.helpers.PictureHelper;

public class EditorActivity extends AppCompatActivity implements IEditorView {

    private static final String PICTURE_PATH_TAG = "picture_path";
    private EditorActivityComponent activityComponent;

    @Inject
    EditorPresenter presenter;

    public static Intent buildIntent(Context context, String picturePath) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(PICTURE_PATH_TAG, picturePath);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);
        presenter.init(getIntent().getStringExtra(PICTURE_PATH_TAG));

    }

    public EditorActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerEditorActivityComponent.builder()
                    .editorActivityModule(new EditorActivityModule(this))
                    .applicationComponent(Application.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showMessage(String s) {

    }

    @Override
    public void showCanvas(Bitmap bitmap) {
        setContentView(new DrawView(this, bitmap));
    }

    class DrawView extends View {
        private RectF bitmapRect;
        private Bitmap source;

        public DrawView(Context context, Bitmap bitmap) {
            super(context);
            source = bitmap;
            bitmapRect = new RectF();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            applyBitmapToCanvas(canvas);
        }

        private void applyBitmapToCanvas(Canvas canvas){
            int maxSideSize = PictureHelper.getPictureOrientation(source) == PictureOrientation.Portrait ?
                    canvas.getHeight() :
                    canvas.getWidth();
            int width = source.getWidth();
            int height = source.getHeight();
            float ratioBitmap = width / (float) height;

            int finalWidth;
            int finalHeight;
            if (ratioBitmap > 1) {
                finalWidth = maxSideSize;
                finalHeight = height * maxSideSize / width;
            } else {
                finalHeight = maxSideSize;
                finalWidth = width * maxSideSize / height;
            }

            if (PictureHelper.getPictureOrientation(source) == PictureOrientation.Landscape) {
                bitmapRect.set(0, (canvas.getHeight() / 2) - (finalHeight / 2),
                        canvas.getWidth(), canvas.getHeight() - (canvas.getHeight() / 2) + (finalHeight / 2));

            } else if (PictureHelper.getPictureOrientation(source) == PictureOrientation.Portrait) {
                bitmapRect.set((canvas.getWidth() / 2) - (finalWidth / 2), 0,
                        canvas.getWidth() - (canvas.getWidth() / 2) + (finalWidth / 2), canvas.getHeight());

            }

            canvas.drawBitmap(source, null, bitmapRect, null);
        }
    }
}
