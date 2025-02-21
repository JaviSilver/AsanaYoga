package com.example.asanayoga;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class PantallaCarga extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(PantallaCarga.this, InicioSesion.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}
