package test.aximedia.app.aximediatest.ui.photos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test.aximedia.app.aximediatest.Application;
import test.aximedia.app.aximediatest.R;
import test.aximedia.app.aximediatest.data.Picture;
import test.aximedia.app.aximediatest.di.component.DaggerMainActivityComponent;
import test.aximedia.app.aximediatest.di.component.MainActivityComponent;
import test.aximedia.app.aximediatest.di.module.MainActivityModule;
import test.aximedia.app.aximediatest.helpers.PickerDispatcher;
import test.aximedia.app.aximediatest.ui.editor.EditorActivity;
import test.aximedia.app.aximediatest.ui.photos.adapter.PictureAdapter;
import test.aximedia.app.aximediatest.ui.photos.adapter.PictureViewHolder;

public class MainActivity extends AppCompatActivity implements IMainView, PictureViewHolder.IOnPictureActionsListener {

    private static final int PHOTOPICKER_REQUEST_CODE = 101;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 401;
    private static final int EDITOR_REQUEST_CODE = 102;

    @BindView(R.id.photosRecyclerView)
    RecyclerView photosRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    MenuItem removeMenuItem;

    @Inject
    MainPresenter presenter;
    @Inject
    PickerDispatcher pickerDispatcher;

    private PictureAdapter adapter;

    private MainActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);
        presenter.setView(this);
        ButterKnife.bind(this);

        presenter.init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTOPICKER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
                    presenter.onImagePicked(imageUri);
                }
                break;
            case EDITOR_REQUEST_CODE:
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        removeMenuItem = menu.findItem(R.id.removeMenuItem);
        removeMenuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.removeMenuItem) {
            presenter.removeButtonClicked();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(pickerDispatcher.getImagePickerIntent(), PHOTOPICKER_REQUEST_CODE);
                }
                break;
            }
        }
    }

    public MainActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerMainActivityComponent.builder()
                    .mainActivityModule(new MainActivityModule(this))
                    .applicationComponent(Application.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }

    @OnClick(R.id.fab)
    void fabClicked() {
        presenter.onFloatingActionButtonClicked();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPhotoPicker() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        } else {
            startActivityForResult(pickerDispatcher.getImagePickerIntent(), PHOTOPICKER_REQUEST_CODE);
        }
    }

    @Override
    public void notifyItemInserted(Picture picture) {
        adapter.addItem(picture);
    }

    @Override
    public void initViews() {
        photosRecyclerView.setHasFixedSize(true);
        photosRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    @Override
    public void showPhotos(List<Picture> pictures) {
        adapter = new PictureAdapter(pictures, this);
        photosRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setRemoveButtonVisibility(boolean isVisible) {
        removeMenuItem.setVisible(isVisible);
    }

    @Override
    public void notifyItemRemoved(Picture picture) {
        adapter.removeItem(picture);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onPictureSelected(Picture picture) {
        presenter.onPictureSelected(picture);
    }

    @Override
    public void onPictureDeselected(Picture picture) {
        presenter.onPictureDeselected(picture);
    }

    @Override
    public boolean isSelectModeEnabled() {
        return presenter.isSelectModeEnabled();
    }

    @Override
    public void openEditor(Picture picture) {
        Intent intent = EditorActivity.buildIntent(this, picture.getPath());
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }
}
