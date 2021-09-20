package com.example.intents;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.intents.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private ActivityResultLauncher<Intent> outraActivityResultLauncher;
    public static String PARAMETRO = "PARAMETRO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Tratando Intents");
        getSupportActionBar().setSubtitle("Principais tipos");

        outraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                activityMainBinding.retornoTv.setText(Objects.requireNonNull(result.getData()).getStringExtra(PARAMETRO));
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent outraActivityIntent = new Intent(this, OutraActivity.class);
        String s = activityMainBinding.parametroEt.getText().toString();
        outraActivityIntent.putExtra(PARAMETRO, s);
        outraActivityResultLauncher.launch(outraActivityIntent);
        return true;
    }
}