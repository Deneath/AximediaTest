package test.aximedia.app.aximediatest.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import test.aximedia.app.aximediatest.Application;
import test.aximedia.app.aximediatest.data.DataManager;
import test.aximedia.app.aximediatest.data.DbHelper;
import test.aximedia.app.aximediatest.di.ApplicationContext;
import test.aximedia.app.aximediatest.di.module.ApplicationModule;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(Application app);

    @ApplicationContext
    Context getContext();

    DataManager getDataManager();

    DbHelper getDbHelper();
}
