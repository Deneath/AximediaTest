package test.aximedia.app.aximediatest.di.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import test.aximedia.app.aximediatest.di.ApplicationContext;
import test.aximedia.app.aximediatest.di.DatabaseInfo;

@Module
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application app) {
        mApplication = app;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return "aximedia-test.db";
    }

    @Provides
    @DatabaseInfo
    Integer provideDatabaseVersion() {
        return 1;
    }

}
