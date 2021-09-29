package com.example.intents;

import static android.Manifest.permission.CALL_PHONE;
import static android.content.Intent.ACTION_CALL;
import static android.content.Intent.ACTION_CHOOSER;
import static android.content.Intent.ACTION_DIAL;
import static android.content.Intent.ACTION_PICK;
import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.EXTRA_INTENT;
import static android.content.Intent.EXTRA_TITLE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Environment.DIRECTORY_PICTURES;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intents.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private ActivityResultLauncher<Intent> outraActivityResultLauncher;
    private ActivityResultLauncher<String> requisicaoPermissaoActivityResultLauncher;
    private ActivityResultLauncher<Intent> selecionarImagemActivityResultLauncher;
    private ActivityResultLauncher<Intent> escolherApplicativoActivityResultLauncher;

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

        requisicaoPermissaoActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), concedida -> {
            if (!concedida) {
                //requisicao permissao
                requisitarPermissaoLigacao();
            } else {
                chamarTelefone();
            }
        });

        selecionarImagemActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::visualizarImagem);

        escolherApplicativoActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::visualizarImagem);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.outraActivityMi) {
            //Abrir outra Activity
            Intent outraActivityIntent = new Intent(this, OutraActivity.class);
            String blab = activityMainBinding.parametroEt.getText().toString();
            outraActivityIntent.putExtra(PARAMETRO, blab);
            outraActivityResultLauncher.launch(outraActivityIntent);
            return true;
        } else if (item.getItemId() == R.id.viewMi) {
            //Abrir navegador
            String url = activityMainBinding.parametroEt.getText().toString().toLowerCase();

            Intent siteIntent = null;
            String finalUrl = "";
            if (url.contains("https")) {
                siteIntent = new Intent(ACTION_VIEW, Uri.parse(url));
            } else if (url.contains("http")) {
                siteIntent = new Intent(ACTION_VIEW, Uri.parse(url));
            } else {
                finalUrl = "http://" + url;
                siteIntent = new Intent(ACTION_VIEW, Uri.parse(finalUrl));
            }
            startActivity(siteIntent);
            return true;
        } else if (item.getItemId() == R.id.callMi) {
            //Fazer chamada
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(CALL_PHONE) != PERMISSION_GRANTED) {
                    //requisito a permissão para o usuário
                    requisitarPermissaoLigacao();
                } else {
                    //chama o discador
                    chamarTelefone();
                }
            } else {
                //chama o discador
                chamarTelefone();
            }
            return true;
        } else if (item.getItemId() == R.id.dialMi) {
            //Abrir o discador
            Intent discadorIntent = new Intent(ACTION_DIAL);
            discadorIntent.setData(Uri.parse("tel: " + activityMainBinding.parametroEt.getText()));
            startActivity(discadorIntent);
        } else if (item.getItemId() == R.id.pickMi) {
            //Pegar uma imagem
            selecionarImagemActivityResultLauncher.launch(prepararImagemIntent());
            return true;
        } else if (item.getItemId() == R.id.chooserMi) {
            //Abrir lista de aplicativos
            Intent escolherActivityIntent = new Intent(ACTION_CHOOSER);
            escolherActivityIntent.putExtra(EXTRA_INTENT, prepararImagemIntent());
            escolherActivityIntent.putExtra(EXTRA_TITLE, "Escolha um aplicativo");
            escolherApplicativoActivityResultLauncher.launch(escolherActivityIntent);
            return true;
        }

        return false;
    }

    private Intent prepararImagemIntent() {
        Intent pegarImagemIntent = new Intent(ACTION_PICK);
        String diretorio = this.getExternalFilesDir(DIRECTORY_PICTURES).getPath();
        pegarImagemIntent.setDataAndType(Uri.parse(diretorio), "image/*");
        return pegarImagemIntent;
    }

    private void visualizarImagem(ActivityResult resultado) {
        if (resultado.getResultCode() == RESULT_OK) {
            Intent visualizarImagemIntent = new Intent(ACTION_VIEW);
            visualizarImagemIntent.setData(Objects.requireNonNull(resultado.getData()).getData());
            startActivity(visualizarImagemIntent);
        }
    }

    private void chamarTelefone() {
        Intent chamarIntent = new Intent();
        chamarIntent.setAction(ACTION_CALL);
        chamarIntent.setData(Uri.parse("tel: " + activityMainBinding.parametroEt.getText()));
        startActivity(chamarIntent);
    }

    private void requisitarPermissaoLigacao() {
        requisicaoPermissaoActivityResultLauncher.launch(CALL_PHONE);
    }
}