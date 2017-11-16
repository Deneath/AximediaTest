package test.aximedia.app.aximediatest;


import android.content.Context;

import test.aximedia.app.aximediatest.di.component.ApplicationComponent;
import test.aximedia.app.aximediatest.di.component.DaggerApplicationComponent;
import test.aximedia.app.aximediatest.di.module.ApplicationModule;

public class Application extends android.app.Application {
    protected ApplicationComponent applicationComponent;

    public static Application get(Context context) {
        return (Application) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
