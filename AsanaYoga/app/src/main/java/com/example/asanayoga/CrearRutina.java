package com.example.asanayoga;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CrearRutina extends AppCompatActivity {

    private AutoCompleteTextView buscadorAsanasRutina;
    private RecyclerView listaAsanasDisponibles, listaAsanasActuales;
    private Button btnGuardarRutina, btnBorrarRutina;
    private EditText nombreRutina;

    private AsanaDatabase db;
    private List<Asana> asanasDisponibles, asanasSeleccionadas;
    private RutinaConAsanaAdapter adapterDisponibles;
    private CrearRutinaAdapter adapterSeleccionadas;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_rutina);

        // Inicializar la base de datos y listas
        db = new AsanaDatabase(this);
        asanasDisponibles = new ArrayList<>();
        asanasSeleccionadas = new ArrayList<>();

        // Recuperar el ID del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SesionUsuario", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            finish();
        }

        nombreRutina = findViewById(R.id.nombreRutina);
        buscadorAsanasRutina = findViewById(R.id.buscadorAsanasRutina);
        listaAsanasDisponibles = findViewById(R.id.listaAsanasDisponibles);
        listaAsanasActuales = findViewById(R.id.listaAsanasActuales);
        btnGuardarRutina = findViewById(R.id.btnGuardarRutina);
        btnBorrarRutina = findViewById(R.id.btnBorrarRutina);

        // Configurar RecyclerViews
        listaAsanasDisponibles.setLayoutManager(new LinearLayoutManager(this));
        listaAsanasActuales.setLayoutManager(new LinearLayoutManager(this));

        // Configurar adaptadores
        adapterDisponibles = new RutinaConAsanaAdapter(asanasDisponibles, this::añadirAsanaARutina);
        adapterSeleccionadas = new CrearRutinaAdapter(asanasSeleccionadas, this::eliminarAsanaDeRutina);

        listaAsanasDisponibles.setAdapter(adapterDisponibles);
        listaAsanasActuales.setAdapter(adapterSeleccionadas);

        cargarAsanasDisponibles();

        // Configurar SearchView
        buscadorAsanasRutina.setOnKeyListener((v, keyCode, event) -> {
            String query = buscadorAsanasRutina.getText().toString().trim();
            buscarAsanasDisponibles(query);
            return false;
        });

        btnGuardarRutina.setOnClickListener(v -> guardarRutina());
        btnBorrarRutina.setOnClickListener(v -> limpiarRutinaActual());
    }

    // Método para cargar las asanas disponibles en BBDD
    private void cargarAsanasDisponibles() {
        Cursor cursor = db.obtenerTodasLasAsanas();
        asanasDisponibles.clear();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_NOMBRE));
                Asana asana = new Asana(nombre);
                asanasDisponibles.add(asana);
            }
            cursor.close();
        }
        adapterDisponibles.notifyDataSetChanged();
    }

    // Método para buscar asanas disponibles con el SearchView
    private void buscarAsanasDisponibles(String query) {
        List<Asana> resultadosBusqueda = new ArrayList<>();
        for (Asana asana : asanasDisponibles) {
            if (asana.getNombre().toLowerCase().contains(query.toLowerCase())) {
                resultadosBusqueda.add(asana);
            }
        }
        adapterDisponibles.actualizarLista(resultadosBusqueda);
    }

    // Método para añadir una asana a la rutina(Añade la asana a lista de asanas seleccionadas)
    private void añadirAsanaARutina(Asana asana) {
        if (!asanasSeleccionadas.contains(asana)) {
            asanasSeleccionadas.add(asana);
            adapterSeleccionadas.notifyDataSetChanged();
            Toast.makeText(this, "Asana añadida a la rutina", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "La asana ya está en la rutina", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para guardar la rutina
    private void guardarRutina() {
        if (asanasSeleccionadas.isEmpty()) {
            Toast.makeText(this, "La rutina está vacía. Añade al menos una asana.", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombreRutinaTexto = nombreRutina.getText().toString().trim();
        if (nombreRutinaTexto.isEmpty()) {
            Toast.makeText(this, "Introduce un nombre para la rutina.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear una nueva instancia de Rutina
        Rutina rutina = new Rutina(nombreRutinaTexto, new ArrayList<>(asanasSeleccionadas));

        boolean resultado = db.anadirRutina(rutina.getNombre(), rutina.generarDescripcion(), userId);

        if (resultado) {
            // Obtener el ID de la rutina recién guardada
            int rutinaId = db.obtenerRutinaId(rutina.getNombre(), userId);

            // Añadir cada asana a la rutina
            for (int i = 0; i < asanasSeleccionadas.size(); i++) {
                Asana asana = asanasSeleccionadas.get(i);
                int asanaId = db.obtenerAsanaId(asana.getNombre());  // Obtén el ID de la asana
                db.anadirAsanaARutina(rutinaId, asanaId, i); // Asocia la asana a la rutina
            }

            Toast.makeText(this, "Rutina guardada exitosamente", Toast.LENGTH_SHORT).show();
            limpiarRutinaActual();
        } else {
            Toast.makeText(this, "Error al guardar la rutina.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para eliminar una asana de la rutina(Limpia la lista de asanas seleccionadas)
    private void eliminarAsanaDeRutina(Asana asana) {
        asanasSeleccionadas.remove(asana);
        adapterSeleccionadas.notifyDataSetChanged();
        Toast.makeText(this, "Asana eliminada de la rutina", Toast.LENGTH_SHORT).show();
    }


    // Método para limpiar la rutina actual
    private void limpiarRutinaActual() {
        asanasSeleccionadas.clear();
        adapterSeleccionadas.notifyDataSetChanged();
        buscadorAsanasRutina.setText("");
        Toast.makeText(this, "Rutina actual limpiada", Toast.LENGTH_SHORT).show();
    }
}
