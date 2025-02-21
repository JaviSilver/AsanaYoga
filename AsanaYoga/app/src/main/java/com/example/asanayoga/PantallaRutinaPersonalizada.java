package com.example.asanayoga;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class PantallaRutinaPersonalizada extends AppCompatActivity {

    private TextView tituloRutina;
    private RecyclerView listaAsanas;
    private AsanaAdapter asanaAdapter;
    private List<Asana> asanasList;
    private AsanaDatabase db;
    private int rutinaId;
    private Button btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_rutina_personalizada);

        tituloRutina = findViewById(R.id.tituloRutina);
        listaAsanas = findViewById(R.id.listaAsanas);
        btnEliminar = findViewById(R.id.btnEliminar);

        listaAsanas.setLayoutManager(new LinearLayoutManager(this));

        asanasList = new ArrayList<>();
        asanaAdapter = new AsanaAdapter(asanasList);
        listaAsanas.setAdapter(asanaAdapter);

        db = new AsanaDatabase(this);

        // Recuperar el ID de la rutina desde el Intent
        rutinaId = getIntent().getIntExtra("RUTINA_ID", -1);
        String rutinaNombre = getIntent().getStringExtra("RUTINA_NOMBRE");

        // Verificar si se ha pasado el ID de la rutina
        if (rutinaId == -1) {
            Toast.makeText(this, "Error al cargar la rutina.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tituloRutina.setText(rutinaNombre);
        cargarAsanasDeRutina(rutinaId);

        // Configurar la funcionalidad del botón Eliminar
        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    // Método para cargar las asanas de la rutina, cargo la rutina y las asanas por problemas de logica
    private void cargarAsanasDeRutina(int rutinaId) {
        Cursor cursor = db.getAsanasByRutina(rutinaId);

        // Limpiar la lista antes de añadir nuevas asanas
        asanasList.clear();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_NOMBRE));

                // Añadir la asana a la lista
                Asana asana = new Asana(nombre);
                asanasList.add(asana);

                // Log para ver qué datos estamos recibiendo
                Log.d("PantallaRutinaPersonalizada", "Asana: " + nombre);
            }
            cursor.close();
        } else {
            //Esto es para comprobar que no hay asanas en la rutina. Ya que mostraba las asanas en blanco
            // Log.d("PantallaRutinaPersonalizada", "No se encontraron asanas para esta rutina.");

            Toast.makeText(this, "No hay asanas en esta rutina.", Toast.LENGTH_SHORT).show();
        }

        // Notificar al adaptador que los datos han cambiado
        asanaAdapter.notifyDataSetChanged();
    }


    // Método para confirmar la eliminación de la rutina
    private void confirmarEliminacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Rutina")
                .setMessage("¿Estás seguro de que deseas eliminar esta rutina? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarRutina())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Método para eliminar la rutina
    private void eliminarRutina() {
        boolean success = db.eliminarRutina(rutinaId);

        if (success) {
            Toast.makeText(this, "Rutina eliminada correctamente.", Toast.LENGTH_SHORT).show();
            // Redirigir al usuario a la pantalla anterior
            Intent intent = new Intent(this, PantallaUsuario.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al eliminar la rutina.", Toast.LENGTH_SHORT).show();
        }
    }
}
