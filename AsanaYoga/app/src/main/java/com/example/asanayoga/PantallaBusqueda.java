package com.example.asanayoga;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PantallaBusqueda extends AppCompatActivity {

    private Button rankingButton;
    private Spinner tipoSpinner, dificultadSpinner, parteCuerpoSpinner;
    private RecyclerView recyclerView;
    private AsanaAdapter asanaAdapter;
    private AsanaDatabase asanaDatabase;
    private Button aplicarFiltroButton;
    private List<Asana> asanasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_busqueda);


        asanaDatabase = new AsanaDatabase(this);

        //Asignación de los Spinners
        tipoSpinner = findViewById(R.id.tipoSpinner);
        dificultadSpinner = findViewById(R.id.difficultadSpinner);
        parteCuerpoSpinner = findViewById(R.id.parteCuerpoSpinner);


        // Configura las opciones de los Spinners
        configurarSpinners();

        asanasList = new ArrayList<>();
        recyclerView = findViewById(R.id.listaBusquedaAvanzada);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        asanaAdapter = new AsanaAdapter(asanasList);
        recyclerView.setAdapter(asanaAdapter);


        // Botón para aplicar filtros
        aplicarFiltroButton = findViewById(R.id.aplicarFiltroButton);
        aplicarFiltroButton.setOnClickListener(v -> aplicarFiltros());

        // Botón para obtener el ranking de asanas por votos
        rankingButton = findViewById(R.id.btnRanking);
        rankingButton.setOnClickListener(v -> obtenerRankingAsanas());

        // Barra de búsqueda
        androidx.appcompat.widget.SearchView barraBusquedaAvanzada = findViewById(R.id.barraBusquedaAvanzada);
        barraBusquedaAvanzada.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarAsanas(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarAsanas(newText);
                return true;
            }
        });
    }

    // Metodo para buscar asanas por nombre con el SearchView
    private void buscarAsanas(String query) {
        Cursor cursor = asanaDatabase.obtenerTodasLasAsanas();
        asanasList.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_NOMBRE));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_DESCRIPCION));
                String variante = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_VARIANTE));
                String dificultad = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_DIFICULTAD));
                String parteCuerpo = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_PARTE_CUERPO));

                if (nombre.toLowerCase().contains(query.toLowerCase())) {
                    asanasList.add(new Asana(nombre, variante, dificultad, parteCuerpo, descripcion));
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();

        if (asanasList.isEmpty()) {
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
        }
        asanaAdapter.notifyDataSetChanged();
    }

    // Metodo para aplicar los filtros al darle al boton de aplicar filtros
    private void aplicarFiltros() {
        String tipoSeleccionado = tipoSpinner.getSelectedItem().toString();
        String dificultadSeleccionada = dificultadSpinner.getSelectedItem().toString();
        String parteCuerpoSeleccionada = parteCuerpoSpinner.getSelectedItem().toString();

        Cursor cursor = asanaDatabase.obtenerTodasLasAsanas();
        asanasList.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_NOMBRE));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_DESCRIPCION));
                String variante = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_VARIANTE));
                String dificultad = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_DIFICULTAD));
                String parteCuerpo = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_PARTE_CUERPO));

                // Filtra los asanas según los valores seleccionados en los Spinners
                if ((tipoSeleccionado.equals("Cualquiera") || variante.equalsIgnoreCase(tipoSeleccionado)) &&
                        (dificultadSeleccionada.equals("Cualquiera") || dificultad.equalsIgnoreCase(dificultadSeleccionada)) &&
                        (parteCuerpoSeleccionada.equals("Cualquiera") || parteCuerpo.equalsIgnoreCase(parteCuerpoSeleccionada))) {
                    asanasList.add(new Asana(nombre, variante, dificultad, parteCuerpo, descripcion));
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();

        if (asanasList.isEmpty()) {
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
        }
        asanaAdapter.notifyDataSetChanged();
    }

    // Metodo para obtener el ranking de asanas por votos cuando pulsas el boton de ranking
    private void obtenerRankingAsanas() {
        Cursor cursor = asanaDatabase.obtenerRankingAsanasPorVotos();
        asanasList.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_NOMBRE));
                // Agrega la asana al listado
                asanasList.add(new Asana(nombre));
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();

        if (asanasList.isEmpty()) {
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
        }
        asanaAdapter.notifyDataSetChanged();
    }

    //Metodo para cargar los valores de los spinners
    private void configurarSpinners() {
        // Opciones para el Spinner de tipo
        List<String> tipos = new ArrayList<>();
        tipos.add("Cualquiera");
        tipos.add("Estándar");
        tipos.add("Equilibrio");
        tipos.add("Flujo");
        tipos.add("Variación");

        // Opciones para el Spinner de dificultad
        List<String> dificultades = new ArrayList<>();
        dificultades.add("Cualquiera");
        dificultades.add("Muy fácil");
        dificultades.add("Fácil");
        dificultades.add("Intermedio");
        dificultades.add("Avanzado");

        // Opciones para el Spinner de parte del cuerpo
        List<String> partesCuerpo = new ArrayList<>();
        partesCuerpo.add("Cualquiera");
        partesCuerpo.add("Cuerpo entero");
        partesCuerpo.add("Piernas");
        partesCuerpo.add("Espalda");
        partesCuerpo.add("Hombros y brazos");
        partesCuerpo.add("Columna vertebral");
        partesCuerpo.add("Caderas");

        // Configura los adaptadores para los Spinners
        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoSpinner.setAdapter(tipoAdapter);

        ArrayAdapter<String> dificultadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dificultades);
        dificultadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dificultadSpinner.setAdapter(dificultadAdapter);

        ArrayAdapter<String> parteCuerpoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, partesCuerpo);
        parteCuerpoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parteCuerpoSpinner.setAdapter(parteCuerpoAdapter);
    }

}
