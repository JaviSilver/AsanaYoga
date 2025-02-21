package com.example.asanayoga;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

//Esta clase gestiona las acciones CRUD(Select, Insert, Update, Delete) de la base de datos


public class AsanaDatabase extends SQLiteOpenHelper {
    // Constantes para la base de datos
    private static final String DATABASE_NAME = "AsanaYoga.db";
    private static final int DATABASE_VERSION = 2;

    //Declaración para las variables de las tablas de la base de datos

    // Tabla Usuario
    public static final String TABLE_USUARIO = "Usuario";
    public static final String COLUMN_USUARIO_ID = "id_usuario";
    public static final String COLUMN_USUARIO_NOMBRE = "nombre";
    public static final String COLUMN_USUARIO_EMAIL = "email";
    public static final String COLUMN_USUARIO_PASSWORD = "password";

    // Tabla Asana
    public static final String TABLE_ASANA = "Asana";
    public static final String COLUMN_ASANA_ID = "id_asana";
    public static final String COLUMN_ASANA_NOMBRE = "nombre";
    public static final String COLUMN_ASANA_DESCRIPCION = "descripcion";

    public static final String COLUMN_ASANA_VARIANTE = "variante";
    public static final String COLUMN_ASANA_DIFICULTAD = "dificultad";
    public static final String COLUMN_ASANA_PARTE_CUERPO = "parte_cuerpo";

    // Tabla Rutina
    public static final String TABLE_RUTINA = "Rutina";
    public static final String COLUMN_RUTINA_ID = "id_rutina";
    public static final String COLUMN_RUTINA_NOMBRE = "nombre";
    public static final String COLUMN_RUTINA_DESCRIPCION = "descripcion";
    public static final String COLUMN_RUTINA_USUARIO_ID = "id_usuario";

    // Tabla Shanti (Votación)
    public static final String TABLE_SHANTI = "Shanti";
    public static final String COLUMN_SHANTI_ID = "id_votacion";
    public static final String COLUMN_SHANTI_HA_VOTADO = "ha_votado";
    public static final String COLUMN_SHANTI_USUARIO_ID = "id_usuario";
    public static final String COLUMN_SHANTI_ASANA_ID = "id_asana";

    // Tabla Rutina_has_Asana
    public static final String TABLE_RUTINA_HAS_ASANA = "Rutina_has_Asana";
    public static final String COLUMN_RUTINA_HAS_ASANA_ID = "id";
    public static final String COLUMN_RUTINA_HAS_ASANA_RUTINA_ID = "id_rutina";
    public static final String COLUMN_RUTINA_HAS_ASANA_ASANA_ID = "id_asana";
    public static final String COLUMN_RUTINA_HAS_ASANA_ORDEN = "orden";

    // Constructor de la clase
    public AsanaDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Metodos para crear las tablas de la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USUARIO_TABLE = "CREATE TABLE " + TABLE_USUARIO + " (" +
                COLUMN_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USUARIO_NOMBRE + " TEXT NOT NULL UNIQUE, " +
                COLUMN_USUARIO_EMAIL + " TEXT NOT NULL UNIQUE, " +
                COLUMN_USUARIO_PASSWORD + " TEXT NOT NULL)";

        String CREATE_ASANA_TABLE = "CREATE TABLE " + TABLE_ASANA + " ("
                + COLUMN_ASANA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ASANA_NOMBRE + " TEXT, "
                + COLUMN_ASANA_VARIANTE + " TEXT, "
                + COLUMN_ASANA_DIFICULTAD + " TEXT, "
                + COLUMN_ASANA_PARTE_CUERPO + " TEXT, "
                + COLUMN_ASANA_DESCRIPCION + " TEXT);";

        String CREATE_RUTINA_TABLE = "CREATE TABLE " + TABLE_RUTINA + " (" +
                COLUMN_RUTINA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RUTINA_NOMBRE + " TEXT NOT NULL, " +
                COLUMN_RUTINA_DESCRIPCION + " TEXT, " +
                COLUMN_RUTINA_USUARIO_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_RUTINA_USUARIO_ID + ") REFERENCES " + TABLE_USUARIO + "(" + COLUMN_USUARIO_ID + ") ON DELETE CASCADE ON UPDATE CASCADE)";

        String CREATE_SHANTI_TABLE = "CREATE TABLE " + TABLE_SHANTI + " (" +
                COLUMN_SHANTI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SHANTI_HA_VOTADO + " BOOLEAN NOT NULL, " +
                COLUMN_SHANTI_USUARIO_ID + " INTEGER NOT NULL, " +
                COLUMN_SHANTI_ASANA_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_SHANTI_USUARIO_ID + ") REFERENCES " + TABLE_USUARIO + "(" + COLUMN_USUARIO_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY(" + COLUMN_SHANTI_ASANA_ID + ") REFERENCES " + TABLE_ASANA + "(" + COLUMN_ASANA_ID + ") ON DELETE CASCADE ON UPDATE CASCADE)";

        String CREATE_RUTINA_HAS_ASANA_TABLE = "CREATE TABLE " + TABLE_RUTINA_HAS_ASANA + " (" +
                COLUMN_RUTINA_HAS_ASANA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RUTINA_HAS_ASANA_RUTINA_ID + " INTEGER NOT NULL, " +
                COLUMN_RUTINA_HAS_ASANA_ASANA_ID + " INTEGER NOT NULL, " +
                COLUMN_RUTINA_HAS_ASANA_ORDEN + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_RUTINA_HAS_ASANA_RUTINA_ID + ") REFERENCES " + TABLE_RUTINA + "(" + COLUMN_RUTINA_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY(" + COLUMN_RUTINA_HAS_ASANA_ASANA_ID + ") REFERENCES " + TABLE_ASANA + "(" + COLUMN_ASANA_ID + ") ON DELETE CASCADE ON UPDATE CASCADE)";


        db.execSQL(CREATE_USUARIO_TABLE);
        db.execSQL(CREATE_ASANA_TABLE);
        db.execSQL(CREATE_RUTINA_TABLE);
        db.execSQL(CREATE_SHANTI_TABLE);
        db.execSQL(CREATE_RUTINA_HAS_ASANA_TABLE);
    }

    //Metodo para actualizar la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHANTI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUTINA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASANA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        onCreate(db);
    }

    //Metodo para agregar un nuevo usuario a la base de datos
    public boolean anadirUsuario(String nombre, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (comprobarSiExisteNombreYEmail(nombre, email)) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_NOMBRE, nombre);
        values.put(COLUMN_USUARIO_EMAIL, email);
        values.put(COLUMN_USUARIO_PASSWORD, password);
        long result = db.insert(TABLE_USUARIO, null, values);
        return result != -1;
    }

    //Metodo para comprobar si el nombre de usuario o el email ya existen en la base de datos como medida preventiva, aunque ya tengo el metodo comprobarUsuario
    private boolean comprobarSiExisteNombreYEmail(String nombre, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USUARIO, null,
                COLUMN_USUARIO_NOMBRE + "=? OR " + COLUMN_USUARIO_EMAIL + "=?",
                new String[]{nombre, email}, null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    //Metodo para comprobar si el usuario existe en la base de datos para iniciar sesion y registrarse
    public boolean comprobarUsuario(String nombre_usuario, String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USUARIO + " WHERE " +
                        COLUMN_USUARIO_NOMBRE + "=? AND " +
                        COLUMN_USUARIO_EMAIL + "=? AND " +
                        COLUMN_USUARIO_PASSWORD + "=?",
                new String[]{nombre_usuario, email, password}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Método para actualizar información del usuario
    public boolean actualizarUsuario(int idUsuario, String nuevoNombre, String nuevoEmail, String nuevaPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_NOMBRE, nuevoNombre);
        values.put(COLUMN_USUARIO_EMAIL, nuevoEmail);
        values.put(COLUMN_USUARIO_PASSWORD, nuevaPassword);

        int rowsAffected = db.update(TABLE_USUARIO, values, COLUMN_USUARIO_ID + "=?", new String[]{String.valueOf(idUsuario)});
        return rowsAffected > 0;
    }


    //Metodo para agregar una nueva asana a la base de datos
    public boolean anadirAsana(String nombre, String variante, String dificultad, String parteCuerpo, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ASANA_NOMBRE, nombre);
        values.put(COLUMN_ASANA_VARIANTE, variante);
        values.put(COLUMN_ASANA_DIFICULTAD, dificultad);
        values.put(COLUMN_ASANA_PARTE_CUERPO, parteCuerpo);
        values.put(COLUMN_ASANA_DESCRIPCION, descripcion);
        long result = db.insert(TABLE_ASANA, null, values);
        return result != -1;
    }

    //Metodo para obtener todas las asanas de la base de datos
    public Cursor obtenerTodasLasAsanas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ASANA, null, null, null, null, null, null);
    }

    //Metodo para obtener el id del usuario por su nombre de usuario y email
    public int obtenerUsuarioId(String nombre_usuario, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USUARIO, new String[]{COLUMN_USUARIO_ID},
                COLUMN_USUARIO_NOMBRE + "=? AND " + COLUMN_USUARIO_EMAIL + "=?",
                new String[]{nombre_usuario, email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USUARIO_ID));
            cursor.close();
            return userId;
        }
        if (cursor != null) cursor.close();
        return -1; // Retorna -1 si no se encuentra el usuario
    }

    //Metodo para obtener el id de la asana por su nombre
    public int obtenerAsanaId(String nombreAsana) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ASANA, new String[]{COLUMN_ASANA_ID},
                COLUMN_ASANA_NOMBRE + "=?", new String[]{nombreAsana},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int asanaId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ASANA_ID));
            cursor.close();
            return asanaId;
        }
        if (cursor != null) cursor.close();
        return -1; // Retorna -1 si no se encuentra la asana
    }

    //Metodo para obtener el id de la rutina por su nombre y el id del usuario
    public int obtenerRutinaId(String nombreRutina, int usuarioId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RUTINA, new String[]{COLUMN_RUTINA_ID},
                COLUMN_RUTINA_NOMBRE + "=? AND " + COLUMN_RUTINA_USUARIO_ID + "=?",
                new String[]{nombreRutina, String.valueOf(usuarioId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int rutinaId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RUTINA_ID));
            cursor.close();
            return rutinaId;
        }
        if (cursor != null) cursor.close();
        return -1; // Retorna -1 si no se encuentra la rutina
    }

    //Metodo para obtener las asanas ordenadas por el número de votos
    public Cursor obtenerRankingAsanasPorVotos() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Realizar una consulta que cuente los votos para cada asana y las ordene por el número de votos de manera descendente
        String query = "SELECT a." + COLUMN_ASANA_NOMBRE + ", COUNT(s." + COLUMN_SHANTI_ASANA_ID + ") AS votos " +
                "FROM " + TABLE_ASANA + " a " +
                "LEFT JOIN " + TABLE_SHANTI + " s ON a." + COLUMN_ASANA_ID + " = s." + COLUMN_SHANTI_ASANA_ID + " " +
                "GROUP BY a." + COLUMN_ASANA_ID + " " +
                "ORDER BY votos DESC";

        return db.rawQuery(query, null);
    }

    //Metodo para agregar una nueva rutina a la base de datos
    public boolean anadirRutina(String nombre, String descripcion, int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RUTINA_NOMBRE, nombre);
        values.put(COLUMN_RUTINA_DESCRIPCION, descripcion);
        values.put(COLUMN_RUTINA_USUARIO_ID, idUsuario);
        long result = db.insert(TABLE_RUTINA, null, values);
        return result != -1;
    }

    //Metodo para agregar una asana a una rutina, este metodo usa la tabla relacionada Rutina_has_Asana
    public void anadirAsanaARutina(int rutinaId, int asanaId, int orden) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RUTINA_HAS_ASANA_RUTINA_ID, rutinaId);
        values.put(COLUMN_RUTINA_HAS_ASANA_ASANA_ID, asanaId);
        values.put(COLUMN_RUTINA_HAS_ASANA_ORDEN, orden);
        db.insert(TABLE_RUTINA_HAS_ASANA, null, values);
    }

    //Metodo para votar una asana
    public boolean votarAsana(int idAsana, Context context) {
        // Obtener el ID del usuario desde las preferencias compartidas
        int idUsuario = context.getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE).getInt("USER_ID", -1);

        // Configurar valores para la inserción
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHANTI_HA_VOTADO, true);
        values.put(COLUMN_SHANTI_USUARIO_ID, idUsuario);
        values.put(COLUMN_SHANTI_ASANA_ID, idAsana);

        // Intentar insertar el voto
        long resultado = db.insert(TABLE_SHANTI, null, values);

        // Comprobar el resultado e informar al usuario
        if (resultado != -1) {
            Toast.makeText(context, "¡Voto registrado!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "Ya has votado esta asana.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //Metodo para obtener las asanas de una rutina
    public Cursor getAsanasByRutina(int rutinaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT a." + COLUMN_ASANA_NOMBRE + ", a." + COLUMN_ASANA_DESCRIPCION +
                        " FROM " + TABLE_RUTINA_HAS_ASANA + " rha " +
                        " JOIN " + TABLE_ASANA + " a ON rha." + COLUMN_RUTINA_HAS_ASANA_ASANA_ID + " = a." + COLUMN_ASANA_ID +
                        " WHERE rha." + COLUMN_RUTINA_HAS_ASANA_RUTINA_ID + " = ?" +
                        " ORDER BY rha." + COLUMN_RUTINA_HAS_ASANA_ORDEN,
                new String[]{String.valueOf(rutinaId)}
        );
    }


    // Metodo para eliminar una rutina y sus relaciones con las asanas
    public boolean eliminarRutina(int rutinaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Inicia una transacción para garantizar consistencia en la eliminación
            db.beginTransaction();

            // Elimina la rutina de la tabla Rutina
            int deletedRutina = db.delete(
                    TABLE_RUTINA,
                    COLUMN_RUTINA_ID + "=?",
                    new String[]{String.valueOf(rutinaId)}
            );

            if (deletedRutina > 0) {
                // Si ambas operaciones fueron exitosas, confirma la transacción
                db.setTransactionSuccessful();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Finaliza la transacción
            db.endTransaction();
        }
        return false;
    }

    // Método para eliminar una asana de la base de datos
    public boolean eliminarAsana(int idAsana) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasAfectadas = db.delete(TABLE_ASANA, COLUMN_ASANA_ID + "=?", new String[]{String.valueOf(idAsana)});
        return filasAfectadas > 0;
    }


}
