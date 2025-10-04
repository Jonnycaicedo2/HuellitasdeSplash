package com.example.huellitasdesplash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.huellitasdesplash.db.CarritoDAO;
import com.example.huellitasdesplash.db.ProductoDAO;
import com.example.huellitasdesplash.models.Producto;

public class DetalleProductoActivity extends AppCompatActivity {

    private static final String TAG = "DetalleProductoActivity";

    private ImageView ivImagenProducto;
    private TextView tvNombreProductoDetalle, tvDescripcionProductoDetalle;
    private TextView tvPrecioProductoDetalle, tvStockProductoDetalle, tvCategoriaProductoDetalle;
    private TextView tvCantidad;
    private Button btnMenos, btnMas, btnAgregarCarritoDetalle, btnComprarAhora;
    private Button btnVolverTienda;

    private ProductoDAO productoDAO;
    private CarritoDAO carritoDAO;
    private Producto producto;
    private int cantidad = 1;
    private int usuarioId = 1; // Usuario simulado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        try {
            Log.d(TAG, "Iniciando DetalleProductoActivity...");

            productoDAO = new ProductoDAO(this);
            carritoDAO = new CarritoDAO(this);

            initViews();
            configurarNavegacion();
            configurarControlesCantidad();
            cargarDatosProducto();

            Log.d(TAG, "DetalleProductoActivity iniciada correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error crítico en onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar el producto", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        try {
            ivImagenProducto = findViewById(R.id.ivImagenProducto);
            tvNombreProductoDetalle = findViewById(R.id.tvNombreProductoDetalle);
            tvDescripcionProductoDetalle = findViewById(R.id.tvDescripcionProductoDetalle);
            tvPrecioProductoDetalle = findViewById(R.id.tvPrecioProductoDetalle);
            tvStockProductoDetalle = findViewById(R.id.tvStockProductoDetalle);
            tvCategoriaProductoDetalle = findViewById(R.id.tvCategoriaProductoDetalle);
            tvCantidad = findViewById(R.id.tvCantidad);
            btnMenos = findViewById(R.id.btnMenos);
            btnMas = findViewById(R.id.btnMas);
            btnAgregarCarritoDetalle = findViewById(R.id.btnAgregarCarritoDetalle);
            btnComprarAhora = findViewById(R.id.btnComprarAhora);
            btnVolverTienda = findViewById(R.id.btnVolverTienda);

            Log.d(TAG, "Vistas inicializadas correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error inicializando vistas: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar la interfaz", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarNavegacion() {
        try {
            btnVolverTienda.setOnClickListener(v -> finish());
        } catch (Exception e) {
            Log.e(TAG, "Error configurando navegación: " + e.getMessage());
        }
    }

    private void configurarControlesCantidad() {
        try {
            btnMenos.setOnClickListener(v -> {
                if (cantidad > 1) {
                    cantidad--;
                    actualizarCantidad();
                    validarStock();
                } else {
                    Toast.makeText(this, "La cantidad mínima es 1", Toast.LENGTH_SHORT).show();
                }
            });

            btnMas.setOnClickListener(v -> {
                if (producto != null && cantidad < producto.getStock()) {
                    cantidad++;
                    actualizarCantidad();
                    validarStock();
                } else {
                    Toast.makeText(this, "No hay suficiente stock disponible", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error configurando controles de cantidad: " + e.getMessage());
        }
    }

    private void cargarDatosProducto() {
        try {
            int productoId = getIntent().getIntExtra("producto_id", -1);
            Log.d(TAG, "Cargando producto ID: " + productoId);

            if (productoId == -1) {
                Toast.makeText(this, "Error: Producto no válido", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            producto = productoDAO.obtenerProductoPorId(productoId);

            if (producto == null) {
                Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            mostrarDatosProducto();
            configurarBotonesAccion();

        } catch (Exception e) {
            Log.e(TAG, "Error cargando datos del producto: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar información del producto", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void mostrarDatosProducto() {
        try {
            // Nombre y descripción
            tvNombreProductoDetalle.setText(producto.getNombre());
            tvDescripcionProductoDetalle.setText(producto.getDescripcion());

            // Precio formateado
            String precioFormateado = String.format("$%,d", producto.getPrecio());
            tvPrecioProductoDetalle.setText(precioFormateado);

            // Stock
            String stockTexto = producto.getStock() + " unidades disponibles";
            tvStockProductoDetalle.setText(stockTexto);

            // Categoría con formato
            String categoria = producto.getCategoria();
            tvCategoriaProductoDetalle.setText(categoria.substring(0, 1).toUpperCase() + categoria.substring(1));

            // Imagen del producto (SIN GLIDE)
            cargarImagenProducto();

            // Estado inicial de la cantidad
            actualizarCantidad();
            validarStock();

            Log.d(TAG, "Datos del producto mostrados: " + producto.getNombre());

        } catch (Exception e) {
            Log.e(TAG, "Error mostrando datos del producto: " + e.getMessage());
        }
    }

    private void cargarImagenProducto() {
        try {
            // Imagen por defecto basada en la categoría (SIN Glide)
            int imagenPorDefecto = obtenerImagenPorCategoria(producto.getCategoria());
            ivImagenProducto.setImageResource(imagenPorDefecto);

        } catch (Exception e) {
            Log.e(TAG, "Error cargando imagen: " + e.getMessage());
            // Imagen de error por defecto de Android
            ivImagenProducto.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    private int obtenerImagenPorCategoria(String categoria) {
        if (categoria == null) {
            return android.R.drawable.ic_menu_report_image;
        }

        switch (categoria.toLowerCase()) {
            case "alimentos":
                return R.drawable.categoria_alimentos;
            case "juguetes":
                return R.drawable.categoria_juguetes;
            case "higiene":
                return R.drawable.categoria_higiene;
            case "accesorios":
                return R.drawable.categoria_accesorios;
            default:
                return android.R.drawable.ic_menu_report_image;
        }
    }

    private void configurarBotonesAccion() {
        try {
            btnAgregarCarritoDetalle.setOnClickListener(v -> agregarAlCarrito());
            btnComprarAhora.setOnClickListener(v -> comprarAhora());
        } catch (Exception e) {
            Log.e(TAG, "Error configurando botones de acción: " + e.getMessage());
        }
    }

    private void agregarAlCarrito() {
        try {
            if (!validarStockDisponible()) {
                return;
            }

            boolean exito = carritoDAO.agregarAlCarrito(usuarioId, producto.getId(), cantidad);

            if (exito) {
                Toast.makeText(this, "✅ " + cantidad + " x " + producto.getNombre() + " agregado al carrito", Toast.LENGTH_SHORT).show();

                // Regresar a la tienda después de agregar
                new android.os.Handler().postDelayed(() -> finish(), 1000);
            } else {
                Toast.makeText(this, "❌ Error al agregar al carrito", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error agregando al carrito: " + e.getMessage(), e);
            Toast.makeText(this, "⚠️ Error al agregar al carrito", Toast.LENGTH_SHORT).show();
        }
    }

    private void comprarAhora() {
        try {
            if (!validarStockDisponible()) {
                return;
            }

            // Primero agregar al carrito
            boolean exito = carritoDAO.agregarAlCarrito(usuarioId, producto.getId(), cantidad);

            if (exito) {
                Toast.makeText(this, "🛒 Procediendo al pago...", Toast.LENGTH_SHORT).show();

                // Navegar directamente al método de pago
                Intent intent = new Intent(this, MetodoPagoActivity.class);
                intent.putExtra("producto_directo", true);
                intent.putExtra("producto_nombre", producto.getNombre());
                intent.putExtra("producto_cantidad", cantidad);
                intent.putExtra("total", producto.getPrecio() * cantidad);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "❌ Error al procesar compra", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error en compra directa: " + e.getMessage(), e);
            Toast.makeText(this, "⚠️ Error al procesar compra", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarCantidad() {
        tvCantidad.setText(String.valueOf(cantidad));

        // Actualizar estado de botones
        btnMenos.setEnabled(cantidad > 1);
        btnMas.setEnabled(producto != null && cantidad < producto.getStock());
    }

    private void validarStock() {
        if (producto == null) return;

        try {
            boolean sinStock = producto.getStock() <= 0;
            boolean stockInsuficiente = cantidad > producto.getStock();

            // Actualizar mensaje de stock
            if (sinStock) {
                tvStockProductoDetalle.setText("❌ SIN STOCK");
                tvStockProductoDetalle.setTextColor(getColor(android.R.color.holo_red_dark));
            } else if (stockInsuficiente) {
                tvStockProductoDetalle.setText("⚠️ Stock insuficiente");
                tvStockProductoDetalle.setTextColor(getColor(android.R.color.holo_orange_dark));
            } else {
                tvStockProductoDetalle.setText("✅ " + producto.getStock() + " unidades disponibles");
                tvStockProductoDetalle.setTextColor(getColor(android.R.color.holo_green_dark));
            }

            // Habilitar/deshabilitar botones según stock
            boolean botonesHabilitados = !sinStock && !stockInsuficiente;

            btnAgregarCarritoDetalle.setEnabled(botonesHabilitados);
            btnComprarAhora.setEnabled(botonesHabilitados);

            if (!botonesHabilitados) {
                btnAgregarCarritoDetalle.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
                btnComprarAhora.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
            } else {
                btnAgregarCarritoDetalle.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                btnComprarAhora.setBackgroundTintList(getColorStateList(android.R.color.holo_green_dark));
            }

        } catch (Exception e) {
            Log.e(TAG, "Error validando stock: " + e.getMessage());
        }
    }

    private boolean validarStockDisponible() {
        if (producto == null) return false;

        if (producto.getStock() <= 0) {
            Toast.makeText(this, "❌ Producto sin stock disponible", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cantidad > producto.getStock()) {
            Toast.makeText(this, "❌ Stock insuficiente. Máximo: " + producto.getStock(), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DetalleProductoActivity destruida");
    }
}