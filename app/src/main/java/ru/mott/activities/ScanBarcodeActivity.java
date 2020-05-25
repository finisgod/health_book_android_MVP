package ru.mott.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import ru.mott.R;
import ru.mott.account.AccountSingleton;
import ru.mott.architectureInterface.ArchitrectureInterface;
import ru.mott.presenters.ScanBarcodeActivityPresenter;


public class ScanBarcodeActivity extends AppCompatActivity implements ArchitrectureInterface.View {
    private ScanBarcodeActivityPresenter presenter;
    private SurfaceView cameraPreview;
    private CameraSource mCameraSource;

    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private final int FIRST_LAUNCH=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ScanBarcodeActivityPresenter((AccountSingleton) this.getApplication());
        setContentView(R.layout.barcode_check);
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        cameraPreview = findViewById(R.id.camera_preview);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            int NOT_THE_FIRST_LAUNCH = 1;
            createCameraSource(NOT_THE_FIRST_LAUNCH);
        } else {
            requestCameraPermissions();
        }
        if(!presenter.isUserLog()){
            Toast.makeText(getApplicationContext(),"Please relogin",Toast.LENGTH_SHORT).show();
        }

    }

    private void requestCameraPermissions() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }
        ActivityCompat.requestPermissions(this, permissions,
                RC_HANDLE_CAMERA_PERM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {


        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(FIRST_LAUNCH);
            return;
        }

        DialogInterface.OnClickListener listener = (dialog, id) -> finish();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert)
                .setMessage(R.string.permission_required)
                .setPositiveButton("Ok", listener)
                .show();
    }


    private void createCameraSource(int launch_number) {
        Context context = getApplicationContext();
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();


        if (!barcodeDetector.isOperational()) {
            Toast.makeText(this, R.string.barcode_is_not_ready, Toast.LENGTH_LONG).show();
            finish();
        }

        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCameraSource = cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {}

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                int i=0;

                Barcode firstbarcode=barcodes.valueAt(i);
                while (firstbarcode.format !=32) {
                    i++;
                }
                String barcodefound="No";
                for(int j=0; j<presenter.getBarcodeId().size(); j++){
                    if(presenter.getBarcodeId().get(j).equals(firstbarcode.displayValue)){

                        barcodefound="Yes";
                    }


                }
                Intent mIntent = new Intent();
                mIntent.putExtra("barcode",barcodefound);
                presenter.setBarcode(firstbarcode.rawValue);
                setResult(RESULT_OK,mIntent);
                finish();

            }

        });

        if (launch_number == FIRST_LAUNCH) {
            try {
                cameraSource.start(cameraPreview.getHolder());
            } catch (IOException e) {
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

}
