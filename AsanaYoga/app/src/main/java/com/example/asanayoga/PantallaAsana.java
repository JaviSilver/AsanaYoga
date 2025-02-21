package com.example.asanayoga;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaAsana extends AppCompatActivity {

    private TextView nombreAsana, descripcionAsana, infoAdicional, votosContador;
    private ImageView fotoAsana;
    private Button btnVotar, btnEliminar;
    private AsanaDatabase db;
    private int asanaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_asana);

        // Referenciar vistas
        nombreAsana = findViewById(R.id.nombreAsana);
        descripcionAsana = findViewById(R.id.descripcionAsana);
        fotoAsana = findViewById(R.id.asanaImagen);
        infoAdicional = findViewById(R.id.infoAdicional);
        votosContador = findViewById(R.id.votosContador);
        btnVotar = findViewById(R.id.btnVotar);
        btnEliminar = findViewById(R.id.btnEliminarAsana);

        db = new AsanaDatabase(this);

        // Recupera el nombre de la asana seleccionada desde PantallaInicio
        String nombreAsana = getIntent().getStringExtra("NOMBRE_ASANA");
        cargarAsana(nombreAsana);

        btnVotar.setOnClickListener(v -> votarAsana());

        btnEliminar.setOnClickListener(v -> eliminarAsana());
    }

    // Método para cargar los datos de la asana
    private void cargarAsana(String nombre) {
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.query(AsanaDatabase.TABLE_ASANA, null,
                AsanaDatabase.COLUMN_ASANA_NOMBRE + "=?",
                new String[]{nombre}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            asanaId = cursor.getInt(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_ID));
            String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_DESCRIPCION));
            String variante = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_VARIANTE));
            String dificultad = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_DIFICULTAD));
            String parteCuerpo = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_PARTE_CUERPO));

            nombreAsana.setText(nombre);
            descripcionAsana.setText(descripcion);
            infoAdicional.setText("Información Adicional:\nVariante: " + variante + "\nDificultad: " + dificultad + "\nParte del cuerpo: " + parteCuerpo);

            // Actualizar contador de votos
            actualizarContadorVotos();

            cursor.close();
        }

        fotoAsana.setImageResource(R.drawable.foto_asana_ejemplo);
    }

    // Método para registrar un voto
    private void votarAsana() {
        boolean resultado = db.votarAsana(asanaId, this);
        if (resultado) {
            Toast.makeText(this, "¡Voto registrado!", Toast.LENGTH_SHORT).show();
            actualizarContadorVotos(); // Actualizar los votos tras registrar uno nuevo
        } else {
            Toast.makeText(this, "Ya has votado esta asana.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para actualizar el contador de votos
    private void actualizarContadorVotos() {
        SQLiteDatabase db = this.db.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + AsanaDatabase.TABLE_SHANTI +
                " WHERE " + AsanaDatabase.COLUMN_SHANTI_ASANA_ID + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(asanaId)});

        if (cursor != null && cursor.moveToFirst()) {
            int votos = cursor.getInt(0);
            votosContador.setText("Votos: " + votos);
            cursor.close();
        }
    }

    // Método para eliminar la asana
    private void eliminarAsana() {
        boolean eliminado = db.eliminarAsana(asanaId);
        if (eliminado) {
            Toast.makeText(this, "Asana eliminada correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al eliminar la asana", Toast.LENGTH_SHORT).show();
        }
    }
}
