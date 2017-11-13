package test.aximedia.app.aximediatest.helpers;

import android.graphics.Bitmap;

public class PictureHelper {
    public static PictureOrientation getPictureOrientation(Bitmap bitmap) {
        if (bitmap == null) throw new NullPointerException("bitmap");
//        if (bitmap.getHeight() > bitmap.getWidth()) return PictureOrientation.Portrait;
//        else if (bitmap.getHeight() < bitmap.getWidth()) return PictureOrientation.Landscape;
        return bitmap.getHeight() > bitmap.getWidth() ? PictureOrientation.Portrait : bitmap.getHeight() < bitmap.getWidth() ? PictureOrientation.Landscape : PictureOrientation.Landscape ;
    }
}
