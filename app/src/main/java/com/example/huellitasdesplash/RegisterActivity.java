package com.example.huellitasdesplash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.huellitasdesplash.db.UsuarioDAO;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvGoLogin;
    private UsuarioDAO usuarioDAO;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usuarioDAO = new UsuarioDAO(this);
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        // Referencias
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoLogin = findViewById(R.id.tvGoLogin);

        // Acción registro
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar formato de email básico
            if (!email.contains("@") || !email.contains(".")) {
                Toast.makeText(this, "Por favor ingresa un email válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar longitud de contraseña
            if (password.length() < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar si ya existe correo
            if (usuarioDAO.existeCorreo(email)) {
                Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show();
                return;
            }

            long id = usuarioDAO.registrarUsuario(name, email, password);
            if (id > 0) {
                // Guardar sesión automáticamente después del registro
                prefs.edit().putInt("user_id", (int) id).apply();

                Toast.makeText(this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();

                // Redirigir a PerfilMascota para que registre su primera mascota
                Intent intent = new Intent(RegisterActivity.this, PerfilMascotaActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "No se pudo registrar la cuenta", Toast.LENGTH_SHORT).show();
            }
        });

        // Acción ir a login
        tvGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}