package com.example.huellitasdesplash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.huellitasdesplash.db.CarritoDAO;
import com.example.huellitasdesplash.db.PedidoDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MetodoPagoActivity extends AppCompatActivity {

    private static final String TAG = "MetodoPagoActivity";

    private TextView tvTotalPago;
    private RadioGroup radioGroupMetodosPago;
    private Button btnVolverCarrito, btnProcesarPago;

    private CarritoDAO carritoDAO;
    private PedidoDAO pedidoDAO;
    private int usuarioId = 1; // Usuario simulado
    private int totalPago = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodo_pago);

        try {
            Log.d(TAG, "Iniciando MetodoPagoActivity...");

            carritoDAO = new CarritoDAO(this);
            pedidoDAO = new PedidoDAO(this);

            initViews();
            configurarNavegacion();
            cargarDatosPago();

            Log.d(TAG, "MetodoPagoActivity iniciada correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error crítico en onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar método de pago", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        try {
            tvTotalPago = findViewById(R.id.tvTotalPago);
            radioGroupMetodosPago = findViewById(R.id.radioGroupMetodosPago);
            btnVolverCarrito = findViewById(R.id.btnVolverCarrito);
            btnProcesarPago = findViewById(R.id.btnProcesarPago);

            Log.d(TAG, "Vistas inicializadas correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error inicializando vistas: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar la interfaz", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarNavegacion() {
        try {
            btnVolverCarrito.setOnClickListener(v -> finish());

            btnProcesarPago.setOnClickListener(v -> procesarPago());

        } catch (Exception e) {
            Log.e(TAG, "Error configurando navegación: " + e.getMessage());
        }
    }

    private void cargarDatosPago() {
        try {
            // Obtener total del intent o calcular del carrito
            totalPago = getIntent().getIntExtra("total", 0);

            if (totalPago <= 0) {
                // Si no viene del intent, calcular del carrito
                totalPago = carritoDAO.calcularTotalCarrito(usuarioId);
            }

            // Formatear y mostrar total
            String totalFormateado = String.format(Locale.getDefault(), "$%,d", totalPago);
            tvTotalPago.setText(totalFormateado);

            Log.d(TAG, "Total a pagar: " + totalFormateado);

        } catch (Exception e) {
            Log.e(TAG, "Error cargando datos de pago: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar información de pago", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void procesarPago() {
        try {
            // Validar método de pago seleccionado
            int metodoSeleccionadoId = radioGroupMetodosPago.getCheckedRadioButtonId();

            if (metodoSeleccionadoId == -1) {
                Toast.makeText(this, "❌ Por favor selecciona un método de pago", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar que hay items en el carrito
            if (totalPago <= 0) {
                Toast.makeText(this, "❌ El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener método de pago seleccionado usando if-else en lugar de switch
            String metodoPago = obtenerMetodoPagoSeleccionado(metodoSeleccionadoId);

            if (metodoPago == null) {
                Toast.makeText(this, "❌ Método de pago no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Procesando pago con método: " + metodoPago + ", Total: $" + totalPago);

            // Crear pedido en la base de datos
            boolean pedidoCreado = pedidoDAO.crearPedido(usuarioId, totalPago, metodoPago);

            if (pedidoCreado) {
                // Vaciar carrito
                carritoDAO.vaciarCarrito(usuarioId);

                // Mostrar confirmación
                mostrarConfirmacion(metodoPago);

            } else {
                Toast.makeText(this, "❌ Error al procesar el pedido", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error procesando pago: " + e.getMessage(), e);
            Toast.makeText(this, "⚠️ Error al procesar el pago", Toast.LENGTH_SHORT).show();
        }
    }

    private String obtenerMetodoPagoSeleccionado(int radioButtonId) {
        // Usar if-else en lugar de switch porque R.id no son constantes
        if (radioButtonId == R.id.radioTarjetaCredito) {
            return "Tarjeta de Crédito";
        } else if (radioButtonId == R.id.radioTarjetaDebito) {
            return "Tarjeta de Débito";
        } else if (radioButtonId == R.id.radioEfectivo) {
            return "Efectivo";
        } else if (radioButtonId == R.id.radioTransferencia) {
            return "Transferencia Bancaria";
        } else {
            return null;
        }
    }

    private void mostrarConfirmacion(String metodoPago) {
        try {
            // Crear intent para la actividad de confirmación
            Intent intent = new Intent(this, ConfirmacionActivity.class);
            intent.putExtra("total", totalPago);
            intent.putExtra("metodo_pago", metodoPago);
            intent.putExtra("fecha", new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));

            // Limpiar el stack de actividades y empezar nueva
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // Finalizar esta actividad
            finish();

        } catch (Exception e) {
            Log.e(TAG, "Error mostrando confirmación: " + e.getMessage(), e);
            // Fallback: mostrar toast y volver a tienda
            Toast.makeText(this, "✅ ¡Pedido realizado con éxito! Total: $" + totalPago, Toast.LENGTH_LONG).show();

            // Volver a la tienda
            Intent intent = new Intent(this, TiendaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MetodoPagoActivity destruida");
    }
}