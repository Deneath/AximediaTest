package test.aximedia.app.aximediatest.ui.editor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import test.aximedia.app.aximediatest.Application;
import test.aximedia.app.aximediatest.R;
import test.aximedia.app.aximediatest.di.component.DaggerEditorActivityComponent;
import test.aximedia.app.aximediatest.di.component.EditorActivityComponent;
import test.aximedia.app.aximediatest.di.module.EditorActivityModule;
import test.aximedia.app.aximediatest.helpers.DrawView;
import test.aximedia.app.aximediatest.helpers.GlideApp;

public class EditorActivity extends AppCompatActivity implements IEditorView {

    private static final String PICTURE_PATH_TAG = "picture_path";
    private EditorActivityComponent activityComponent;

    @BindView(R.id.containerLayout)
    FrameLayout containerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawView)
    DrawView drawView;

    @Inject
    EditorPresenter presenter;

    public static Intent buildIntent(Context context, String picturePath) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(PICTURE_PATH_TAG, picturePath);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);
        presenter.init(getIntent().getStringExtra(PICTURE_PATH_TAG));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveMenuItem) {

            Bitmap cache = drawView.getDrawingCache();
            presenter.saveImage(drawView.getBitmap());
            return true;
        } else if(item.getItemId() == R.id.cancelMenuItem){
            presenter.undoButtonCLicked();
        } else if(item.getItemId() == android.R.id.home){
            presenter.backClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    public EditorActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerEditorActivityComponent.builder()
                    .editorActivityModule(new EditorActivityModule(this))
                    .applicationComponent(Application.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showMessage(String s) {

    }

    @Override
    public void showImage(String path) {
        GlideApp.with(this).load(path).into(drawView);
    }

    @Override
    public void initViews() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        drawView.setDrawingCacheEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<RectF> rects = drawView.getRects();
        String json = new Gson().toJson(rects);
        outState.putString("rects", json);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Type type = new TypeToken<List<RectF>>() {}.getType();
        List<RectF> rects = new Gson().fromJson(savedInstanceState.getString("rects"), type);
        drawView.setRects(rects);

    }
}
