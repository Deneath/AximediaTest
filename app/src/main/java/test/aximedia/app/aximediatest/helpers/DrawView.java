package test.aximedia.app.aximediatest.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DrawView extends android.support.v7.widget.AppCompatImageView {

    private Paint paint;
    private List<RectF> rects;
    private RectF rect;
    private PointF startPoint;
    private Bitmap source;

    public Bitmap getBitmap(){
        return source;
    }

    public DrawView(Context context, AttributeSet attrs){
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
        super.onDraw(canvas);
        canvas.drawRect(rect, paint);
        for (RectF rect : rects)
            canvas.drawRect(rect, paint);
        canvas.save();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        if(drawable == null) return;

        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        source = bitmap.copy(bitmap.getConfig(), true);
        Canvas canvas = new Canvas(source);
        draw(canvas);
    }

    public List<RectF> getRects(){
        return rects;
    }

    public void setRects(List<RectF> rects){
        this.rects = rects;
    }


}