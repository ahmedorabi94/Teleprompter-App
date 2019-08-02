package com.test.orabi.teleprompter.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.test.orabi.teleprompter.R;
import com.test.orabi.teleprompter.databinding.ActivityAddTextBinding;
import com.test.orabi.teleprompter.viewmodel.AddTextViewModel;

public class AddTextActivity extends AppCompatActivity {

    private ActivityAddTextBinding binding;
    private boolean textHasChanged = false;


    int rowId;
    AddTextViewModel viewModel;

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener touchListener = (v, event) -> {
        textHasChanged = true;
        binding.fabRecordVideo.hide();
        return false;
    };

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_text);

        viewModel = ViewModelProviders.of(this).get(AddTextViewModel.class);

        rowId = getIntent().getIntExtra("row_id", -1);

        Log.e("Add Text : ", "Row Id " + rowId);


        binding.edTitle.setOnTouchListener(touchListener);
        binding.edBody.setOnTouchListener(touchListener);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        binding.fabRecordVideo.setOnClickListener(v -> {

            Intent intent1 = new Intent(getApplicationContext(), ScrollTextActivity.class);
            intent1.putExtra("body", binding.edBody.getText().toString());
            startActivity(intent1);


        });


        String body_text = getIntent().getStringExtra("body");


        if (rowId == -1 && body_text == null) {
            setTitle("Add Text");
            invalidateOptionsMenu();
            binding.fabRecordVideo.hide();
        }


        if (rowId != -1 && body_text == null) {
            setTitle("Edit Text");
            viewModel.getTele(rowId);
            observeViewModel(viewModel);

        }


        if (rowId == -1 && body_text != null) {
            setTitle("Add Text");
            binding.edBody.setText(body_text);
            binding.fabRecordVideo.hide();
        }


        if (shouldAskPermissions()) {
            askPermissions();
        }


    }


    private void observeViewModel(AddTextViewModel viewModel) {

        viewModel.getTeleLiveData().observe(this, tele -> {

            if (tele == null){
                return;
            }

            binding.edTitle.setText(tele.getTitle());
            binding.edBody.setText(tele.getBody());
        });


    }


    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_text_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (rowId == -1) {
            MenuItem item = menu.findItem(R.id.delete_current_item);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_btn) {
            InsertNewText();
            finish();
            return true;
        }
        if (id == R.id.delete_current_item) {
            showDeleteConfirmationDialog();
            return true;
        }
        if (id == android.R.id.home) {
            if (!textHasChanged) {
                NavUtils.navigateUpFromSameTask(AddTextActivity.this);
                return true;
            }
            ShowDialog();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete This Text?");
        builder.setPositiveButton("Delete", (dialog, which) -> deleteCurrentItem());
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard Your Changes and Quite Editing?");
        builder.setPositiveButton("DISCARD", (dialogInterface, i) -> NavUtils.navigateUpFromSameTask(AddTextActivity.this));
        builder.setNegativeButton("KEEP EDITING", (dialog, i) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteCurrentItem() {

        viewModel.deleteTele(rowId);
        finish();
    }

    private void InsertNewText() {

        String title = binding.edTitle.getText().toString();
        String body = binding.edBody.getText().toString();


        if (rowId == -1) {

            viewModel.insertNewTele(title, body);

        } else {
            int row_updated = viewModel.updateTele(rowId, title, body);
            if (row_updated != -1) {
                Toast.makeText(this, "Text Updated ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Text Updated Failed", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    public void onBackPressed() {

        if (!textHasChanged) {
            super.onBackPressed();
            return;
        }

        ShowDialog();
    }


}
