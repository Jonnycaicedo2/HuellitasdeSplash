package com.example.huellitasdesplash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.huellitasdesplash.R;
import com.example.huellitasdesplash.models.Producto;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private OnProductoClickListener listener;

    public interface OnProductoClickListener {
        void onProductoClick(Producto producto);
        void onAgregarCarritoClick(Producto producto);
    }

    public ProductoAdapter(List<Producto> productos, OnProductoClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.bind(producto, listener);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombreProducto, tvDescripcionProducto, tvPrecioProducto, tvStockProducto;
        private Button btnAgregarCarrito;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvDescripcionProducto = itemView.findViewById(R.id.tvDescripcionProducto);
            tvPrecioProducto = itemView.findViewById(R.id.tvPrecioProducto);
            tvStockProducto = itemView.findViewById(R.id.tvStockProducto);
            btnAgregarCarrito = itemView.findViewById(R.id.btnAgregarCarrito);
        }

        public void bind(Producto producto, OnProductoClickListener listener) {
            tvNombreProducto.setText(producto.getNombre());
            tvDescripcionProducto.setText(producto.getDescripcion());
            tvPrecioProducto.setText("$" + producto.getPrecio());
            tvStockProducto.setText("Stock: " + producto.getStock());

            // Deshabilitar botón si no hay stock
            if (producto.getStock() <= 0) {
                btnAgregarCarrito.setEnabled(false);
                btnAgregarCarrito.setText("Sin Stock");
                btnAgregarCarrito.setBackgroundTintList(itemView.getContext().getColorStateList(android.R.color.darker_gray));
            } else {
                btnAgregarCarrito.setEnabled(true);
                btnAgregarCarrito.setText("Agregar al Carrito");
                btnAgregarCarrito.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.colorPrimary));
            }

            itemView.setOnClickListener(v -> listener.onProductoClick(producto));
            btnAgregarCarrito.setOnClickListener(v -> listener.onAgregarCarritoClick(producto));
        }
    }
}