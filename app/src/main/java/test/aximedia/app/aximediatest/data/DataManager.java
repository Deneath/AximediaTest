package test.aximedia.app.aximediatest.data;

import android.content.res.Resources;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;


@Singleton
public class DataManager {

    private DbHelper mDbHelper;

    @Inject
    public DataManager(DbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    public Observable<Picture> insertPicture(Picture picture) throws Exception {
        return mDbHelper.insertPictureObservable(picture);
    }

    public Observable<Picture> getUser(Long pictureId) throws Resources.NotFoundException, NullPointerException {
        return mDbHelper.getPictureObservable(pictureId);
    }

    public Observable<List<Picture>> getPictures() {
        return mDbHelper.getPicturesObservable();
    }
}
