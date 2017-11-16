package test.aximedia.app.aximediatest.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import test.aximedia.app.aximediatest.di.ApplicationContext;
import test.aximedia.app.aximediatest.di.DatabaseInfo;

@Singleton
public class DbHelper extends SQLiteOpenHelper {

    //PICTURE TABLE
    private static final String PICTURE_TABLE_NAME = "pictures";
    private static final String PICTURE_COLUMN_PICTURE_ID = "id";
    private static final String PICTURE_COLUMN_PICTURE_PATH = "path";

    @Inject
    DbHelper(@ApplicationContext Context context,
             @DatabaseInfo String dbName,
             @DatabaseInfo Integer version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PICTURE_TABLE_NAME);
        onCreate(db);
    }

    private void createTable(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + PICTURE_TABLE_NAME + "("
                            + PICTURE_COLUMN_PICTURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + PICTURE_COLUMN_PICTURE_PATH + " VARCHAR(100) "
                            + ")"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(subscriber -> subscriber.onNext(func.call()));
    }

    Observable<Picture> getPictureObservable(Long pictureId) {
        return makeObservable(getPicture(getReadableDatabase(), pictureId))
                .subscribeOn(Schedulers.computation());
    }

    private Callable<Picture> getPicture(final SQLiteDatabase db, final Long pictureId) throws Resources.NotFoundException, NullPointerException {
        return () -> {
            Cursor cursor = null;
            try {
                cursor = db.rawQuery(
                        "SELECT * FROM "
                                + PICTURE_TABLE_NAME
                                + " WHERE "
                                + PICTURE_COLUMN_PICTURE_ID
                                + " = ? ",
                        new String[]{pictureId + ""});

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    Picture picture = new Picture();
                    picture.setId(cursor.getLong(cursor.getColumnIndex(PICTURE_COLUMN_PICTURE_ID)));
                    picture.setPath(cursor.getString(cursor.getColumnIndex(PICTURE_COLUMN_PICTURE_PATH)));
                    return picture;
                } else {
                    throw new Resources.NotFoundException("Picture with id " + pictureId + " does not exists");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        };
    }

    Observable<List<Picture>> getPicturesObservable() {
        return makeObservable(getPictures(getReadableDatabase()))
                .subscribeOn(Schedulers.computation());
    }

    private Callable<List<Picture>> getPictures(final SQLiteDatabase db) {
        return () -> {
            ArrayList<Picture> pictures = new ArrayList<>();
            Cursor cursor = null;
            try {

                cursor = db.rawQuery(
                        "SELECT * FROM " + PICTURE_TABLE_NAME,
                        null);

                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        Picture picture = new Picture();
                        picture.setId(cursor.getLong(cursor.getColumnIndex(PICTURE_COLUMN_PICTURE_ID)));
                        picture.setPath(cursor.getString(cursor.getColumnIndex(PICTURE_COLUMN_PICTURE_PATH)));

                        pictures.add(picture);
                        cursor.moveToNext();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return pictures;
        };

    }

    Observable<Picture> insertPictureObservable(Picture picture) throws Exception {
        return makeObservable(insertPicture(picture)).subscribeOn(Schedulers.computation());
    }

    private Callable<Picture> insertPicture(Picture picture) throws Exception {
        return () -> {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(PICTURE_COLUMN_PICTURE_PATH, picture.getPath());
                picture.setId(db.insert(PICTURE_TABLE_NAME, null, contentValues));
                return picture;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        };
    }

    Observable<Boolean> removePicturesObservable(List<Picture> pictures) {
        return makeObservable(removePictures(pictures)).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }

    private Callable<Boolean> removePictures(List<Picture> pictures) {
        return () -> {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                for (Picture picture : pictures) {
                    db.delete(PICTURE_TABLE_NAME,
                            PICTURE_COLUMN_PICTURE_ID + "=" + picture.getId(),
                            null);
                }
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        };
    }
}
