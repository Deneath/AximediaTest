package test.aximedia.app.aximediatest.di.component;

import dagger.Component;
import test.aximedia.app.aximediatest.ui.photos.MainActivity;
import test.aximedia.app.aximediatest.di.PerActivity;
import test.aximedia.app.aximediatest.di.module.MainActivityModule;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {

    void inject(MainActivity mainActivity);

}
