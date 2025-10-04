package com.example.huellitasdesplash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.huellitasdesplash.db.MascotaDAO;

public class PerfilMascotaActivity extends AppCompatActivity {

    private EditText inputPetName, inputPetType, inputPetBreed, inputPetAge;
    private Button btnSavePet, btnUploadPhoto, btnRegresar;
    private ImageView imgPetPhoto;
    private MascotaDAO mascotaDAO;
    private SharedPreferences prefs;
    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);

        mascotaDAO = new MascotaDAO(this);
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        // Configurar toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Registrar Mascota");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Referencias
        inputPetName = findViewById(R.id.input_pet_name);
        inputPetType = findViewById(R.id.input_pet_type);
        inputPetBreed = findViewById(R.id.input_pet_breed);
        inputPetAge = findViewById(R.id.input_pet_age);
        btnSavePet = findViewById(R.id.btn_save_pet);
        btnUploadPhoto = findViewById(R.id.btn_upload_photo);
        btnRegresar = findViewById(R.id.btn_regresar); // Nuevo botón
        imgPetPhoto = findViewById(R.id.img_pet_photo);

        // Guardar mascota
        btnSavePet.setOnClickListener(v -> guardarMascota());

        // Subir foto opcional
        btnUploadPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Botón Regresar - Navega al PerfilActivity
        btnRegresar.setOnClickListener(v -> {
            // Regresar al perfil sin guardar
            finish();
        });
    }

    private void guardarMascota() {
        String name = inputPetName.getText().toString().trim();
        String type = inputPetType.getText().toString().trim();
        String breed = inputPetBreed.getText().toString().trim();
        String ageStr = inputPetAge.getText().toString().trim();

        if (name.isEmpty() || type.isEmpty() || breed.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            int userId = prefs.getInt("user_id", -1);

            long id = mascotaDAO.registrarMascota(
                    name, type, breed, age,
                    selectedImageUri != null ? selectedImageUri.toString() : null,
                    userId
            );

            if (id > 0) {
                Toast.makeText(this, "Mascota guardada con éxito", Toast.LENGTH_SHORT).show();

                // Limpiar campos después de guardar
                limpiarCampos();

                // Opción: Preguntar si quiere agregar otra mascota o regresar
                Toast.makeText(this, "Mascota registrada. Puedes agregar otra o regresar.", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Error al guardar mascota", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La edad debe ser un número válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        inputPetName.setText("");
        inputPetType.setText("");
        inputPetBreed.setText("");
        inputPetAge.setText("");
        imgPetPhoto.setImageResource(android.R.drawable.ic_menu_gallery);
        selectedImageUri = null;
        inputPetName.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgPetPhoto.setImageURI(selectedImageUri);
            Toast.makeText(this, "Imagen seleccionada correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Manejar la flecha de retroceso de la toolbar
        finish();
        return true;
    }
}