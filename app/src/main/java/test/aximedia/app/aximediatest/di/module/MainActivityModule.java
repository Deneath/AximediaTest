package test.aximedia.app.aximediatest.di.module;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import test.aximedia.app.aximediatest.ui.photos.MainPresenter;
import test.aximedia.app.aximediatest.data.DataManager;
import test.aximedia.app.aximediatest.di.ActivityContext;
import test.aximedia.app.aximediatest.di.ApplicationContext;
import test.aximedia.app.aximediatest.helpers.PickerDispatcher;

@Module
public class MainActivityModule {

    private Activity mActivity;

    public MainActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    //todo unused
    @Provides
    ContentResolver provideContentResolver(@ApplicationContext Context context){
        return context.getContentResolver();
    }

    @Provides
    MainPresenter providePresenter(PickerDispatcher pickerDispatcher, DataManager dataManager) {
        return new MainPresenter(pickerDispatcher, dataManager);
    }

    @Provides
    PickerDispatcher providePickerDispatcher() {
        return new PickerDispatcher(provideContext());
    }

}
