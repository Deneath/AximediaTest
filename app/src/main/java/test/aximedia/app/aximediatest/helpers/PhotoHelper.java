package test.aximedia.app.aximediatest.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoHelper {
    public static void resizeImage(int sideSize, String imagePath){
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        float ratio = Math.min(
                (float) sideSize / bitmap.getWidth(),
                (float) sideSize / bitmap.getHeight());
        int width = Math.round((float) ratio * bitmap.getWidth());
        int height = Math.round((float) ratio * bitmap.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width,
                height, false);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
