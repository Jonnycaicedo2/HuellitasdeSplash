package com.example.huellitasdesplash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class ConfirmacionActivity extends AppCompatActivity {

    private TextView tvConfirmacion, tvDetalles;
    private Button btnVolverTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion);

        tvConfirmacion = findViewById(R.id.tvConfirmacion);
        tvDetalles = findViewById(R.id.tvDetalles);
        btnVolverTienda = findViewById(R.id.btnVolverTienda);

        // Obtener datos del intent
        Intent intent = getIntent();
        int total = intent.getIntExtra("total", 0);
        String metodoPago = intent.getStringExtra("metodo_pago");
        String fecha = intent.getStringExtra("fecha");

        // Mostrar confirmación
        tvConfirmacion.setText("¡Pedido Confirmado! 🎉");

        String detalles = String.format(Locale.getDefault(),
                "Total: $%,d\nMétodo de pago: %s\nFecha: %s\n\n¡Gracias por tu compra!",
                total, metodoPago, fecha);
        tvDetalles.setText(detalles);

        btnVolverTienda.setOnClickListener(v -> {
            Intent tiendaIntent = new Intent(this, TiendaActivity.class);
            tiendaIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(tiendaIntent);
            finish();
        });
    }
}