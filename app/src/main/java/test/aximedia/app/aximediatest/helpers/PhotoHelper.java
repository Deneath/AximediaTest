package test.aximedia.app.aximediatest.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PhotoHelper {
    public static Bitmap resizeBitmap(Bitmap bitmap, int maxSideSize){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float ratioBitmap = width / (float)height;

        int finalWidth;
        int finalHeight;
        if (ratioBitmap > 1)
        {
            finalWidth = maxSideSize;
            finalHeight = height * maxSideSize / width;
        }
        else
        {
            finalHeight = maxSideSize;
            finalWidth = width * maxSideSize / height;
        }

        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true);
    }

    public static Bitmap getBitmapFromPath(String path){
        return BitmapFactory.decodeFile(path);
    }
}
