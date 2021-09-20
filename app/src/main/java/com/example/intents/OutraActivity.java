package com.example.intents;

import static com.example.intents.MainActivity.PARAMETRO;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intents.databinding.ActivityOutraBinding;

import java.util.Objects;

public class OutraActivity extends AppCompatActivity {
    private ActivityOutraBinding activityOutraBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOutraBinding = ActivityOutraBinding.inflate(getLayoutInflater());
        setContentView(activityOutraBinding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Outra Activity");
        getSupportActionBar().setSubtitle("Recebe e retorna um valor");

        activityOutraBinding.recebidoTv.setText(getIntent().getStringExtra(PARAMETRO));

        activityOutraBinding.retornarBt.setOnClickListener(view -> {
            String result = activityOutraBinding.retornoEt.getText().toString();

            Intent ri = new Intent();
            ri.putExtra(PARAMETRO, result);
            setResult(RESULT_OK, ri);
            finish();
        });
    }
}