package com.example.asanayoga;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditarPerfil extends AppCompatActivity {
    private EditText editarNombre, editarEmail, editarContrasena;
    private Button btnGuardarCambios;
    private AsanaDatabase asanaDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        // Inicializar vistas y base de datos
        editarNombre = findViewById(R.id.editarNombre);
        editarEmail = findViewById(R.id.editarEmail);
        editarContrasena = findViewById(R.id.editarContrasena);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        asanaDatabase = new AsanaDatabase(this);

        // Obtener ID del usuario desde las preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("SesionUsuario", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);

        cargarDatosUsuario(userId);

        btnGuardarCambios.setOnClickListener(v -> comprobarEditarCampos(userId));
    }

    // Método para comprobar los campos de edición y actualizar el perfil
    public void comprobarEditarCampos(int userId){
        {
            String nuevoNombre = editarNombre.getText().toString().trim();
            String nuevoEmail = editarEmail.getText().toString().trim();
            String nuevaPassword = editarContrasena.getText().toString().trim();

            if (nuevoNombre.isEmpty() || nuevoEmail.isEmpty() || nuevaPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean actualizado = asanaDatabase.actualizarUsuario(userId, nuevoNombre, nuevoEmail, nuevaPassword);
            if (actualizado) {
                Toast.makeText(this, "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar el perfil. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para cargar los datos del usuario en los campos de edición
    private void cargarDatosUsuario(int userId) {
        Cursor cursor = asanaDatabase.getReadableDatabase().query(
                AsanaDatabase.TABLE_USUARIO,
                null,
                AsanaDatabase.COLUMN_USUARIO_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_USUARIO_NOMBRE));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_USUARIO_EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(AsanaDatabase.COLUMN_USUARIO_PASSWORD));

            editarNombre.setText(nombre);
            editarEmail.setText(email);
            editarContrasena.setText(password);
            cursor.close();
        }
    }
}
