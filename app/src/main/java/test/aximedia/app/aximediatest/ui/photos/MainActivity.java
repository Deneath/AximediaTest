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
import test.aximedia.app.aximediatest.ui.photos.adapter.PictureAdapter;

public class MainActivity extends AppCompatActivity implements IMainView {

    private static final int PHOTOPICKER_REQUEST_CODE = 101;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 401;

    @BindView(R.id.photosRecyclerView)
    RecyclerView photosRecyclerView;

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
        photosRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    @Override
    public void showPhotos(List<Picture> pictures) {
        adapter = new PictureAdapter(pictures);
        photosRecyclerView.setAdapter(adapter);
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
}
