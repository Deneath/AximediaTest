package test.aximedia.app.aximediatest.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {

    private Paint paint;
    private List<RectF> rects;
    private RectF rect;
    private PointF startPoint;
    private Bitmap source;

    public Bitmap getBitmap() {
        Canvas c = new Canvas(source);
        for (RectF rect : rects)
            c.drawRect(rect, paint);
        return source;
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        rect = new RectF();
        paint = new Paint();
        paint.setARGB(150, 255, 0, 0);
        rects = new ArrayList<>();
    }

    public DrawView(Context context) {
        super(context);
        rect = new RectF();
        paint = new Paint();
        paint.setARGB(180, 255, 0, 0);
        rects = new ArrayList<>();
    }

    public void setBitmap(Bitmap bitmap, int screenWidth, int screenHeight) {
        source = PhotoHelper.resizeBitmap(bitmap, bitmap.getHeight() > bitmap.getWidth() ?
                screenHeight - 81 :
                screenWidth);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(source.getWidth(), source.getHeight());
        params.gravity = Gravity.CENTER;
        setLayoutParams(params);
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
        canvas.drawBitmap(source, 0, 0, null);
        canvas.drawRect(rect, paint);
        for (RectF rect : rects)
            canvas.drawRect(rect, paint);
    }

    public List<RectF> getRects() {
        return rects;
    }

    public void setRects(List<RectF> rects, float oldWidth, float oldHeight) {
        float newWidth = source.getWidth();
        float newHeight = source.getHeight();
        if (newWidth > oldWidth && newHeight > oldHeight) {
            float widthDx = newWidth / oldWidth;
            float heightDx = newHeight / oldHeight;
            for (RectF rect : rects) {
                rect.set(rect.left * widthDx, rect.top * heightDx, rect.right * widthDx, rect.bottom * heightDx);
            }
        } else if (newWidth < oldWidth && newHeight < oldHeight) {
            float widthDx = oldWidth / newWidth;
            float heightDx = oldHeight / newHeight;
            for (RectF rect : rects) {
                rect.set(rect.left / widthDx, rect.top / heightDx, rect.right / widthDx, rect.bottom / heightDx);
            }
        }
        this.rects = rects;
    }
}