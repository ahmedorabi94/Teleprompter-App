package com.test.orabi.teleprompter.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.test.orabi.teleprompter.R;
import com.test.orabi.teleprompter.databinding.ActivityDisplayVideoBinding;
import com.test.orabi.teleprompter.viewmodel.DisplayVidoeViewModel;

public class DisplayVideoActivity extends AppCompatActivity {

    private String videoPath;
    private ActivityDisplayVideoBinding binding;
    private DisplayVidoeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_display_video);

        viewModel = ViewModelProviders.of(this).get(DisplayVidoeViewModel.class);

        Intent intent = getIntent();
        videoPath = intent.getStringExtra("video_path");

        binding.videoViewDisplay.setVideoPath(videoPath);
        binding.videoViewDisplay.seekTo(100);
        binding.videoViewDisplay.setMediaController(new MediaController(this));
        binding.videoViewDisplay.requestFocus();

        binding.playCircle.setOnClickListener(v -> {
            if (!binding.videoViewDisplay.isPlaying()) {
                binding.playCircle.setVisibility(View.GONE);
                binding.videoViewDisplay.start();
            }

        });

        binding.videoViewDisplay.setOnCompletionListener(mp -> binding.playCircle.setVisibility(View.VISIBLE));


        binding.deleteBtn.setOnClickListener(v -> ShowDeleteDialog());

        binding.saveBtn.setOnClickListener(v -> ShowSaveDialog());


        viewModel.getIsDeleted().observe(this, isDeleted -> {
            if (isDeleted) {
                Toast.makeText(DisplayVideoActivity.this, "Video Deleted", Toast.LENGTH_SHORT).show();
            }
        });


        viewModel.getIsSaved().observe(this, isSaved -> {
            if (isSaved) {
                Toast.makeText(DisplayVideoActivity.this, "Video Saved to Galley", Toast.LENGTH_SHORT).show();
            }

        });

        viewModel.getIsVideoFound().observe(this, isFound -> {
            if (!isFound) {
                Toast.makeText(DisplayVideoActivity.this, "Video is not found", Toast.LENGTH_SHORT).show();

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.video_display_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.share_item:
                shareData();
                return true;
            case R.id.restart_item:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void shareData() {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(videoPath));
        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_TITLE, "Teleprompter Title");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Teleprompter Subject");

        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send)));

    }


    private void ShowDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this video?");
        builder.setPositiveButton("Delete", (dialogInterface, i) -> viewModel.deleteFile(videoPath));
        builder.setNegativeButton("No", (dialog, i) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void ShowSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save this video to gallery?");
        builder.setPositiveButton("Save", (dialogInterface, i) -> viewModel.saveFile(videoPath));
        builder.setNegativeButton("No", (dialog, i) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
