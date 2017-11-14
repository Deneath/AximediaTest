package test.aximedia.app.aximediatest.ui.editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.aximedia.app.aximediatest.Application;
import test.aximedia.app.aximediatest.R;
import test.aximedia.app.aximediatest.di.component.DaggerEditorActivityComponent;
import test.aximedia.app.aximediatest.di.component.EditorActivityComponent;
import test.aximedia.app.aximediatest.di.module.EditorActivityModule;
import test.aximedia.app.aximediatest.helpers.PictureHelper;
import test.aximedia.app.aximediatest.helpers.PictureOrientation;

public class EditorActivity extends AppCompatActivity implements IEditorView {

    private static final String PICTURE_PATH_TAG = "picture_path";
    private EditorActivityComponent activityComponent;

    @BindView(R.id.containerLayout)
    FrameLayout containerLayout;

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
        setContentView(R.layout.activity_editor);

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
        DrawView view = new DrawView(this, bitmap);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.gravity = Gravity.CENTER;
        view.setDrawingCacheEnabled(true);
        containerLayout.addView(view, layoutParams);
    }

    class DrawView extends View {
        private RectF bitmapRect;
        private Bitmap source;
        private Paint paint;

        private List<RectF> rects;
        private RectF rect;
        private PointF startPoint;

        public DrawView(Context context, Bitmap bitmap) {
            super(context);
            source = bitmap;
            bitmapRect = new RectF();
            rect = new RectF();
            paint = new Paint();
            paint.setARGB(180, 255, 0, 0);
            rects = new ArrayList<>();
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    rect.set(x, y, x, y);
                    startPoint = new PointF(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    rect.set(x < startPoint.x ? x : startPoint.x,
                            y < startPoint.y ? y : startPoint.y,
                            x > startPoint.x ? x : startPoint.x,
                            y > startPoint.y ? y : startPoint.y);
                    break;
                case MotionEvent.ACTION_UP:
                    rects.add(new RectF(rect));
                    rect.setEmpty();
                    break;
            }
            invalidate();
            return true;
        }

        protected void onDraw(Canvas canvas) {
            applyBitmapToCanvas(canvas);
            canvas.drawRect(rect, paint);
            for (RectF rect : rects)
                canvas.drawRect(rect, paint);
        }

        private void applyBitmapToCanvas(Canvas canvas) {
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
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(finalWidth, finalHeight);
            layoutParams.gravity = Gravity.CENTER;
            setLayoutParams(layoutParams);
            canvas.drawBitmap(source, null, bitmapRect, null);
        }
    }
}
