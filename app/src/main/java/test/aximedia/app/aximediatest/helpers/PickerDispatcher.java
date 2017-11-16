package test.aximedia.app.aximediatest.helpers;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

import javax.inject.Inject;

import test.aximedia.app.aximediatest.di.ApplicationContext;

public class PickerDispatcher {

    private Context context;

    @Inject
    public PickerDispatcher(@ApplicationContext Context context) {
        this.context = context;
    }

    public File getPhotoFile() {
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile("Photo_"
                    + new Timestamp(System.currentTimeMillis()).toString().replace(" ", "").replaceAll("[^\\d]", "")
                    + "", ".jpg", externalFilesDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File getEditedPhotoFile() {
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile("EditedPhoto_"
                    + new Timestamp(System.currentTimeMillis()).toString().replace(" ", "").replaceAll("[^\\d]", "")
                    + "", ".jpg", externalFilesDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Intent getImagePickerIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return Intent.createChooser(intent, "Select Picture");
    }

    public String getFilePath(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        String path = "";
        if (cursor != null) {
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = context.getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            if (cursor != null) {
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        }
        return path;
    }


}
