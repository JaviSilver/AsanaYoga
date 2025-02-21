package com.example.asanayoga;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AgregarAsana extends AppCompatActivity {

    private EditText nombreAsana, descripcionAsana, varianteAsana, dificultadAsana, parteCuerpoAsana;
    private Button btnguardarAsana;
    private AsanaDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_asana);

        db = new AsanaDatabase(this);

        // Referencias a los campos de texto y botón
        nombreAsana= findViewById(R.id.nombreAgregarAsana);
        descripcionAsana= findViewById(R.id.descripcionAgregarAsana);
        varianteAsana = findViewById(R.id.varianteAgregarAsana);
        dificultadAsana= findViewById(R.id.dificultadAgregarAsana);
        parteCuerpoAsana= findViewById(R.id.btnGuardarAsana);
        btnguardarAsana= findViewById(R.id.guardarAsanaButton);

        btnguardarAsana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarAsana();
            }
        });
    }

    // Metodo para agregar una nueva asana a la base de datos
    private void agregarAsana() {
        String nombre = nombreAsana.getText().toString().trim();
        String descripcion = descripcionAsana.getText().toString().trim();
        String variante = varianteAsana.getText().toString().trim();
        String dificultad = dificultadAsana.getText().toString().trim();
        String parteCuerpo = parteCuerpoAsana.getText().toString().trim();

        if (nombre.isEmpty() || dificultad.isEmpty()) {
            Toast.makeText(this, "Nombre y Dificultad son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = db.anadirAsana(nombre, variante, dificultad, parteCuerpo, descripcion);
        if (isInserted) {
            Toast.makeText(this, "Asana agregada exitosamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AgregarAsana.this, PantallaInicio.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al agregar la asana", Toast.LENGTH_SHORT).show();
        }
    }
}
