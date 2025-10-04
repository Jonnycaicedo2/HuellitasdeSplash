package com.example.huellitasdesplash;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.huellitasdesplash.db.UsuarioDAO;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText etNombre, etEmail, etTelefono, etDireccion;
    private Button btnGuardar;
    private SharedPreferences prefs;
    private UsuarioDAO usuarioDAO;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        usuarioDAO = new UsuarioDAO(this);
        userId = prefs.getInt("user_id", -1);

        inicializarVistas();
        cargarDatosUsuario();
        configurarBotones();
    }

    private void inicializarVistas() {
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etTelefono = findViewById(R.id.etTelefono);
        etDireccion = findViewById(R.id.etDireccion);
        btnGuardar = findViewById(R.id.btnGuardar);
    }

    private void cargarDatosUsuario() {
        if (userId != -1) {
            String nombre = usuarioDAO.obtenerNombreUsuario(userId);
            String email = usuarioDAO.obtenerCorreoUsuario(userId);

            etNombre.setText(nombre != null ? nombre : "");
            etEmail.setText(email != null ? email : "");

            // Cargar datos adicionales de SharedPreferences (si existen)
            etTelefono.setText(prefs.getString("user_phone", ""));
            etDireccion.setText(prefs.getString("user_address", ""));
        }
    }

    private void configurarBotones() {
        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void guardarCambios() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();

        // Validaciones
        if (nombre.isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            etNombre.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("El email es obligatorio");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Ingresa un email válido");
            etEmail.requestFocus();
            return;
        }

        // Verificar si el email ya existe (excluyendo al usuario actual)
        if (usuarioDAO.existeCorreoExcluyendoUsuario(userId, email)) {
            etEmail.setError("Este email ya está registrado");
            etEmail.requestFocus();
            return;
        }

        // Actualizar en base de datos
        boolean actualizado = usuarioDAO.actualizarPerfilCompleto(userId, nombre, email);

        if (actualizado) {
            // Guardar datos adicionales en SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_name", nombre);
            editor.putString("user_phone", telefono);
            editor.putString("user_address", direccion);
            editor.apply();

            Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
        }
    }
}