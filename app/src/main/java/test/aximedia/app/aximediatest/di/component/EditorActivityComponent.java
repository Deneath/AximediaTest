package test.aximedia.app.aximediatest.di.component;

import dagger.Component;
import test.aximedia.app.aximediatest.di.PerActivity;
import test.aximedia.app.aximediatest.di.module.EditorActivityModule;
import test.aximedia.app.aximediatest.ui.editor.EditorActivity;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = EditorActivityModule.class)
public interface EditorActivityComponent {
    void inject(EditorActivity activity);
}
