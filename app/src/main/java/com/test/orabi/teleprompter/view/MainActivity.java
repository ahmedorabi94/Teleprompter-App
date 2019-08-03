package com.test.orabi.teleprompter.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.test.orabi.teleprompter.R;
import com.test.orabi.teleprompter.databinding.ActivityMainBinding;
import com.test.orabi.teleprompter.repository.data.Tele;
import com.test.orabi.teleprompter.view.adapter.TeleAdapter;
import com.test.orabi.teleprompter.viewmodel.MainViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    private static final int REQUEST_CODE = 100;
    private TeleAdapter teleAdapter;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;


    private List<Tele> teleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.listView.setEmptyView(binding.emptyView);


        binding.fab.setOnClickListener(view -> viewModel.showPopup(binding.fab, MainActivity.this));

        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, AddTextActivity.class);
            intent.putExtra("row_id", teleList.get(position).getId());
            MainActivity.this.startActivity(intent);
        });


        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);


        viewModel.getAllTelesLiveData().observe(this, teles -> {
            teleAdapter = new TeleAdapter(MainActivity.this, teles);
            binding.listView.setAdapter(teleAdapter);
            teleList = teles;
        });


    }


    private void openFileManger() {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }
        assert intent != null;
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                // String text = readTextFromUri(uri);
                String text = viewModel.readTextFromUri(uri);

                Intent intent = new Intent(MainActivity.this, AddTextActivity.class);
                intent.putExtra("body", text);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_text_popup:
                Intent intent = new Intent(MainActivity.this, AddTextActivity.class);
                startActivity(intent);
                return true;
            case R.id.import_text:
                openFileManger();
                return true;
            default:
                return false;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.insert_dummy_data) {
            // viewModel.insertDummyData();
            // insertDummyData();
            viewModel.insertDummyTele();
            return true;
        }
        if (id == R.id.delete_all_data) {
            // viewModel.showDeleteConfirmationDialog();
            showDeleteConfirmationDialog();
            return true;
        }
        if (id == R.id.settings_item) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete All The Data?");
        builder.setPositiveButton("Delete", (dialog, which) -> viewModel.deleteAllTeles());
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
