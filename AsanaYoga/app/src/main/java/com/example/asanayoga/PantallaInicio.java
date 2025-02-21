package com.example.asanayoga;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PantallaInicio extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AsanaAdapter asanaAdapter;
    private AsanaDatabase asanaDatabase;
    private List<Asana> asanasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);

        asanaDatabase = new AsanaDatabase(this);

        asanasList = new ArrayList<>();
        recyclerView = findViewById(R.id.listaBusquedaAvanzada);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        asanaAdapter = new AsanaAdapter(asanasList);
        recyclerView.setAdapter(asanaAdapter);

        cargarAsanas();
        insertarAsanasDePrueba();

        // Configuracion del boton flotante para agregar asanas
        FloatingActionButton fabAgregarAsana = findViewById(R.id.btnAgregarAsana);
        fabAgregarAsana.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaInicio.this, AgregarAsana.class);
            startActivity(intent);
        });

        // Configuración de la barra de búsqueda avanzada
        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.barraBusquedaAvanzada);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
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

        //Configuracion del menu de navegacion
        BottomNavigationView bottomNavigationView = findViewById(R.id.menuPantallaPrincipal);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            if (item.getItemId() == R.id.item_asanas) {
                intent = new Intent(PantallaInicio.this, PantallaInicio.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.item_busqueda_avanzada) {
                intent = new Intent(PantallaInicio.this, PantallaBusqueda.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.item_rutinas) {
                intent = new Intent(PantallaInicio.this, CrearRutina.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.item_perfil) {
                int userId = getSharedPreferences("SesionUsuario", MODE_PRIVATE).getInt("USER_ID", -1);
                intent = new Intent(PantallaInicio.this, PantallaUsuario.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                return true;
            }
            return false;
        });



    }

    // Insertar asanas de prueba
    private void insertarAsanasDePrueba() {
        // Inserta asanas solo si aún no existen
        if (asanaDatabase.obtenerTodasLasAsanas().getCount() == 0) {
            asanaDatabase.anadirAsana("Perro boca abajo", "Estándar", "Fácil", "Cuerpo entero",
                    "Una postura de estiramiento que fortalece y alinea el cuerpo.");

            asanaDatabase.anadirAsana("Postura del niño", "Estándar", "Fácil", "Cuerpo entero",
                    "Una postura de descanso que relaja y alivia la tensión en la espalda.");

            asanaDatabase.anadirAsana("Postura de la montaña", "Estándar", "Muy fácil", "Piernas",
                    "Postura básica que ayuda a mejorar el equilibrio y la postura.");

            asanaDatabase.anadirAsana("Guerrero II", "Variación", "Intermedio", "Piernas y torso",
                    "Postura de pie que fortalece las piernas y mejora la estabilidad.");

            asanaDatabase.anadirAsana("Postura del árbol", "Equilibrio", "Intermedio", "Piernas",
                    "Una postura de equilibrio que mejora la concentración y la estabilidad.");

            asanaDatabase.anadirAsana("Cobra", "Estándar", "Fácil", "Espalda",
                    "Postura que estira y fortalece la columna, útil para la flexibilidad.");

            asanaDatabase.anadirAsana("Postura del puente", "Estándar", "Intermedio", "Espalda y piernas",
                    "Fortalece la espalda, glúteos y muslos, al mismo tiempo que estira el pecho.");

            asanaDatabase.anadirAsana("Postura del cadáver", "Estándar", "Muy fácil", "Cuerpo entero",
                    "Postura de relajación profunda que calma la mente y reduce el estrés.");

            asanaDatabase.anadirAsana("Postura de la pinza", "Estándar", "Intermedio", "Piernas y espalda",
                    "Estira profundamente la parte posterior de las piernas y la columna.");

            asanaDatabase.anadirAsana("Postura del camello", "Estándar", "Intermedio", "Espalda",
                    "Postura de flexión hacia atrás que abre el pecho y mejora la flexibilidad.");

            asanaDatabase.anadirAsana("Postura del gato-vaca", "Flujo", "Muy fácil", "Columna vertebral",
                    "Un movimiento fluido que aumenta la flexibilidad de la columna.");

            asanaDatabase.anadirAsana("Postura del delfín", "Equilibrio", "Intermedio", "Hombros y brazos",
                    "Fortalece los hombros y los brazos, mejorando la estabilidad.");

            asanaDatabase.anadirAsana("Postura del cuervo", "Equilibrio", "Avanzado", "Brazos y torso",
                    "Postura de equilibrio que requiere fuerza en brazos y concentración.");

            asanaDatabase.anadirAsana("Postura de la rueda", "Estándar", "Avanzado", "Cuerpo entero",
                    "Una postura de flexión hacia atrás que mejora la fuerza y flexibilidad.");

            asanaDatabase.anadirAsana("Postura de la garza", "Equilibrio", "Avanzado", "Piernas y torso",
                    "Postura de equilibrio que estira las piernas y mejora la flexibilidad.");

            asanaDatabase.anadirAsana("Postura del arco", "Estándar", "Intermedio", "Espalda",
                    "Postura que fortalece la espalda y abre el pecho.");

            asanaDatabase.anadirAsana("Postura de la langosta", "Estándar", "Intermedio", "Espalda baja",
                    "Fortalece la zona lumbar y mejora la postura.");

            asanaDatabase.anadirAsana("Postura de la mariposa", "Estándar", "Fácil", "Caderas",
                    "Estira las caderas y mejora la flexibilidad de la zona pélvica.");

            asanaDatabase.anadirAsana("Postura del guerrero III", "Equilibrio", "Intermedio", "Piernas y torso",
                    "Postura de equilibrio que fortalece el núcleo y mejora la estabilidad.");

            asanaDatabase.anadirAsana("Postura de la media luna", "Equilibrio", "Intermedio", "Piernas y torso",
                    "Mejora el equilibrio y fortalece los músculos de las piernas y la cadera.");
        }
    }

    //Metodo para cargar las asanas en el RecyclerView
    private void cargarAsanas() {
        Cursor cursor = null;
        try {
            cursor = asanaDatabase.obtenerTodasLasAsanas();
            asanasList.clear();

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_ASANA_NOMBRE));

                    Asana asana = new Asana(nombre);
                    asanasList.add(asana);
                }
                asanaAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //Metodo para buscar las asanas mediante el SearchView
    private void buscarAsanas(String query) {
        List<Asana> resultadosBusqueda = new ArrayList<>();
        for (Asana asana : asanasList) {
            if (asana.getNombre().toLowerCase().contains(query.toLowerCase())) {
                resultadosBusqueda.add(asana);
            }
        }
        asanaAdapter.actualizarLista(resultadosBusqueda);
    }

    //Metodo para actualizar la lista de asanas cuando se reanuda la actividad
    @Override
    protected void onResume() {
        super.onResume();
        cargarAsanas();
    }
}


