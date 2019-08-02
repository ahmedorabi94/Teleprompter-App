package com.test.orabi.teleprompter.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.test.orabi.teleprompter.R;
import com.test.orabi.teleprompter.databinding.ActivityScrollTextBinding;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ScrollTextActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    public static final String TEXT_COLOR_KEY = "text_color_key";


    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;

    private boolean mInitSuccesful = false;


    private Long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long startHTime = 0L;
    private Handler customHandler = new Handler();
    private File mOutputFile;
    private ActivityScrollTextBinding binding;


    private Runnable updateTimerThread = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        @Override
        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startHTime;
            long updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;

            binding.tvTimer.setText("" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_scroll_text);

        binding.surfaceView.setVisibility(View.INVISIBLE);

        SurfaceHolder mHolder = binding.surfaceView.getHolder();
        mHolder.addCallback(this);

        requestCameraPermissions();


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String text_color = settings.getString(TEXT_COLOR_KEY, "#ff9e9e9e");

        Intent intent = getIntent();
        String body = intent.getStringExtra("body");

        binding.vmTextView.setTextColor(Color.parseColor(text_color));
        binding.vmTextView.append(body);


        binding.vmTextView.setMovementMethod(new ScrollingMovementMethod());
        binding.vmTextView.pauseMarquee();


        binding.recordFab.setOnClickListener(v -> {


            if (areCameraPermissionGranted()) {
                if (!isRecording) {
                    binding.surfaceView.setVisibility(View.VISIBLE);
                    mMediaRecorder.start();
                    startHTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    binding.vmTextView.resumeMarquee();
                    isRecording = true;

                }

            } else {
                requestCameraPermissions();
                Toast.makeText(getApplicationContext(), "Permission Granted, Press Play To Start", Toast.LENGTH_SHORT).show();

            }


        });


        binding.StopFab.setOnClickListener(v -> {
            if (isRecording) {
                try {
                    mMediaRecorder.stop();
                } catch (RuntimeException e) {
                    // RuntimeException is thrown when stop() is called immediately after start().
                    // In this case the output file is not properly constructed ans should be deleted.
                    //noinspection ResultOfMethodCallIgnored
                    mOutputFile.delete();
                }

                releaseMediaRecorder();
                mCamera.lock();
                isRecording = false;
                releaseCamera();


                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);

                timeSwapBuff = 0L;
                startHTime = 0L;

                binding.vmTextView.stopMarquee();

                SaveThisVideoDialog(true);


            }

        });


        binding.SaveFab.setOnClickListener(v -> SaveThisVideoDialog(false));


        float scaledDensity = getApplication().getResources().getDisplayMetrics().scaledDensity;


        binding.SubFab.setOnClickListener(v -> {

            float textSize = binding.vmTextView.getTextSize();
            float newsize = textSize / scaledDensity;


            if (newsize <= 56 && newsize > 14) {
                float SelectedSize = newsize - 3;
                binding.vmTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, SelectedSize);

            }

        });

        binding.AddFab.setOnClickListener(v -> {

            float textSize = binding.vmTextView.getTextSize();
            float newsize = textSize / scaledDensity;


            if (newsize >= 14 && newsize < 56) {
                float SelectedSize = newsize + 3;
                binding.vmTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, SelectedSize);

            }


        });


        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int progress = seekBar.getProgress();
                progress = progress + 1;

                binding.vmTextView.setPixelYOffSet(progress);

            }
        });


    }


    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            if (mCamera != null) {
                mCamera.lock();
            }
        }
    }


    private final String[] requiredPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };

    private static final int MEDIA_RECORDER_REQUEST = 0;


    private boolean areCameraPermissionGranted() {

        for (String permission : requiredPermissions) {
            if (!(ActivityCompat.checkSelfPermission(this, permission) ==
                    PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    private void requestCameraPermissions() {

        if (!areCameraPermissionGranted()) {
            ActivityCompat.requestPermissions(
                    this,
                    requiredPermissions,
                    MEDIA_RECORDER_REQUEST);
        }


    }


    private void initRecorder(Surface surface) {


        // BEGIN_INCLUDE (configure_preview)
        mCamera = CameraHelper.getDefaultCameraInstance();

        // We need to make sure that our preview and recording video size are supported by the
        // camera. Query camera to find all the sizes and choose the optimal size given the
        // dimensions of our preview surface.
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                mSupportedPreviewSizes, binding.surfaceView.getWidth(), binding.surfaceView.getHeight());

        // Use the same size for recording profile.
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;

        // likewise for the camera object itself.
        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mCamera.setParameters(parameters);
//        try {
//            // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
//            // with {@link SurfaceView}
//            mCamera.setPreviewDisplay(mHolder);
//        } catch (IOException e) {
//            //   Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.getMessage());
//            return false;
//        }
        // END_INCLUDE (configure_preview)


        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setPreviewDisplay(surface);

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);


        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(profile);

        // Step 4: Set output file
        mOutputFile = CameraHelper.getOutputMediaFile();
        if (mOutputFile == null) {
            mInitSuccesful = false;
            return;
        }

        mMediaRecorder.setOutputFile(mOutputFile.getPath());
        // END_INCLUDE (configure_media_recorder)

        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            //  Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            mInitSuccesful = false;
            return;

        } catch (IOException e) {
            //   Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            mInitSuccesful = false;
            return;
        }
        mInitSuccesful = true;

    }


    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
        // release the camera immediately on pause event
        releaseCamera();

        binding.vmTextView.pauseMarquee();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.vmTextView.stopMarquee();
    }

    private void SaveThisVideoDialog(boolean isFinish) {


        if (mOutputFile != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Save This Video?");
            builder.setPositiveButton("Save", (dialog, which) -> {
                Intent intent1 = new Intent(getApplicationContext(), DisplayVideoActivity.class);
                intent1.putExtra("video_path", mOutputFile.toString());
                startActivity(intent1);
                finish();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                if (isFinish) {
                    finish();
                } else {
                    dialog.dismiss();

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "There is no video to save", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (!mInitSuccesful) {
            initRecorder(holder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseMediaRecorder();
        releaseCamera();
    }


    @Override
    public void onBackPressed() {


        if (mOutputFile == null) {
            super.onBackPressed();
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard Your Changes and Quite Making Video?");
        builder.setPositiveButton("DISCARD", (dialog, which) -> {
            mOutputFile.delete();
            finish();
        });
        builder.setNegativeButton("KEEP EDITING", (dialog, i) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
