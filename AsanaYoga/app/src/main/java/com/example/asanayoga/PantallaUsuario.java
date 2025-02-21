package com.example.asanayoga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PantallaUsuario extends AppCompatActivity {

    private TextView nombrePerfil, textoEmail;
    private ImageView fotoPerfil;
    private ImageView btnAcercaDe;
    private Button btnEditarPerfil, btnCerrarSesion;
    private RecyclerView listaRutinasGuardadas;

    private AsanaDatabase db;
    private int userId;
    private List<Rutina> rutinasList; // Lista de rutinas
    private RutinaAdapter rutinaAdapter; // Adaptador para el RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_usuario);


        fotoPerfil = findViewById(R.id.fotoPerfil);
        btnAcercaDe = findViewById(R.id.btnAcercaDe);
        nombrePerfil = findViewById(R.id.nombrePerfil);
        textoEmail = findViewById(R.id.textoEmail);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        listaRutinasGuardadas = findViewById(R.id.listaRutinasGuardadas);

        // Configurar el RecyclerView
        listaRutinasGuardadas.setLayoutManager(new LinearLayoutManager(this));
        rutinasList = new ArrayList<>();
        rutinaAdapter = new RutinaAdapter(rutinasList, this); // Asegúrate de pasar el contexto aquí
        listaRutinasGuardadas.setAdapter(rutinaAdapter);

        db = new AsanaDatabase(this);

        // Recuperar el userId almacenado
        SharedPreferences sharedPreferences = getSharedPreferences("SesionUsuario", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);

        if (userId != -1) {
            // Cargar la información del usuario y las rutinas guardadas
            cargarInformacionUsuario(userId);
            cargarRutinasGuardadas(userId);
        }

        // Botón para editar el perfil
        btnEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaUsuario.this, EditarPerfil.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });


        // Botón para acerca de
        btnAcercaDe.setOnClickListener(v -> acercaDe());

        // Botón para cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    //Método para cargar la información del usuario y las rutinas guardadas
    private void cargarInformacionUsuario(int userId) {
        Cursor cursor = db.getReadableDatabase().query(
                AsanaDatabase.TABLE_USUARIO,
                new String[]{AsanaDatabase.COLUMN_USUARIO_NOMBRE, AsanaDatabase.COLUMN_USUARIO_EMAIL},
                AsanaDatabase.COLUMN_USUARIO_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_USUARIO_NOMBRE));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_USUARIO_EMAIL));

            nombrePerfil.setText(nombre);
            textoEmail.setText(email);

            // Configurar una imagen de perfil por defecto
            fotoPerfil.setImageResource(R.drawable.foto_perfil_javi);
            cursor.close();
        } else {
            Toast.makeText(this, "No se encontró información del usuario.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //Método para abrir la actividad AcercaDe
    private void acercaDe(){
        Intent intent = new Intent(PantallaUsuario.this, AcercaDe.class);
        startActivity(intent);
    }

    //Método para cargar las rutinas guardadas, lo separo del metodo cargarInformacionUsuario para mayor claritud
    private void cargarRutinasGuardadas(int userId) {
        Cursor cursor = db.getReadableDatabase().query(
                AsanaDatabase.TABLE_RUTINA,
                new String[]{AsanaDatabase.COLUMN_RUTINA_NOMBRE, AsanaDatabase.COLUMN_RUTINA_DESCRIPCION, AsanaDatabase.COLUMN_RUTINA_ID},
                AsanaDatabase.COLUMN_RUTINA_USUARIO_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );

        rutinasList.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombreRutina = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_RUTINA_NOMBRE));
                int rutinaId = cursor.getInt(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_RUTINA_ID));
                rutinasList.add(new Rutina(rutinaId, nombreRutina)); // Agregar Rutina con su ID
            } while (cursor.moveToNext());
            cursor.close();
        }

        rutinaAdapter.notifyDataSetChanged();

        if (rutinasList.isEmpty()) {
            // Usar Handler para retrasar el mensaje de Toast
            new Handler().postDelayed(() ->
                            Toast.makeText(this, "No tienes rutinas guardadas.", Toast.LENGTH_SHORT).show(),
                    2000 // 3 segundos de retraso (3000 ms)
            );
        }
    }

    //Método para cerrar sesión
    private void cerrarSesion() {
        SharedPreferences.Editor editor = getSharedPreferences("SesionUsuario", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, InicioSesion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
