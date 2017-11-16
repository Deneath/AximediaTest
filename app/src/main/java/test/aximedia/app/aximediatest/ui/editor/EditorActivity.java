package test.aximedia.app.aximediatest.ui.editor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

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

public class EditorActivity extends AppCompatActivity implements IEditorView {

    private static final String PICTURE_PATH_TAG = "picture_path";
    private static final String PICTURE_POSITION_TAG = "picture_position";
    private EditorActivityComponent activityComponent;

    @BindView(R.id.containerLayout)
    FrameLayout containerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawView)
    DrawView drawView;

    @Inject
    EditorPresenter presenter;

    public static Intent buildIntent(Context context, String picturePath, int position) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(PICTURE_PATH_TAG, picturePath);
        intent.putExtra(PICTURE_POSITION_TAG, position);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);
        presenter.init(getIntent().getStringExtra(PICTURE_PATH_TAG), getIntent().getIntExtra(PICTURE_POSITION_TAG, -1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveMenuItem) {
            Bitmap bitmap = drawView.getBitmap();
            presenter.saveImage(bitmap);
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
    public void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showImage(Bitmap bitmap) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;

        drawView.setBitmap(bitmap, screenWidth, screenHeight);
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
    public void applyRestoredInstanceState(List<RectF> rects, int oldWidth, int oldHeight) {
        drawView.setRects(rects, oldWidth, oldHeight);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState, drawView.getRects(),
                drawView.getBitmap().getWidth(), drawView.getBitmap().getHeight());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        presenter.onRestoreInstanceState(savedInstanceState);
    }
}
