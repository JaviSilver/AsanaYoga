package com.example.asanayoga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class InicioSesion extends AppCompatActivity {

    private EditText nombreUsuarioEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button btnIniciarSesion;
    private Button btnRegistrarse;
    private AsanaDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        nombreUsuarioEditText = findViewById(R.id.nombreUsuario);
        emailEditText = findViewById(R.id.emailUsuario);
        passwordEditText = findViewById(R.id.contrasenaUsuario);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        db = new AsanaDatabase(this);

        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());
        btnRegistrarse.setOnClickListener(v -> registrarUsuario());
    }

    private void iniciarSesion() {
        String nombre_usuario = nombreUsuarioEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (nombre_usuario.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.comprobarUsuario(nombre_usuario, email, password)) {
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            int userId = db.obtenerUsuarioId(nombre_usuario, email);

            // Guardar el userId en SharedPreferences
            getSharedPreferences("SesionUsuario", MODE_PRIVATE)
                    .edit()
                    .putInt("USER_ID", userId)
                    .apply();

            // Pasar a PantallaInicio
            Intent intent = new Intent(InicioSesion.this, PantallaInicio.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarUsuario() {
        String nombre_usuario = nombreUsuarioEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (nombre_usuario.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!db.comprobarUsuario(nombre_usuario, email, password)) {
            if (db.anadirUsuario(nombre_usuario, email, password)) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                iniciarSesion(); // Redirigir al perfil tras el registro
            } else {
                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "El nombre de usuario o el email ya existen", Toast.LENGTH_SHORT).show();
        }
    }
}
