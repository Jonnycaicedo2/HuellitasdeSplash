package com.example.huellitasdesplash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.huellitasdesplash.R;
import com.example.huellitasdesplash.models.CarritoItem;
import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<CarritoItem> carritoItems;
    private OnCarritoItemClickListener listener;

    public interface OnCarritoItemClickListener {
        void onEliminarClick(int itemId);
        void onCantidadChange(int itemId, int nuevaCantidad);
    }

    public CarritoAdapter(List<CarritoItem> carritoItems, OnCarritoItemClickListener listener) {
        this.carritoItems = carritoItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        CarritoItem item = carritoItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return carritoItems != null ? carritoItems.size() : 0;
    }

    public void actualizarDatos(List<CarritoItem> nuevosItems) {
        this.carritoItems = nuevosItems;
        notifyDataSetChanged();
    }

    public class CarritoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombreProducto, tvPrecio, tvCantidad, tvSubtotal;
        private Button btnMenos, btnMas;
        private ImageButton btnEliminar;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProductoCarrito);
            tvPrecio = itemView.findViewById(R.id.tvPrecioProductoCarrito);
            tvCantidad = itemView.findViewById(R.id.tvCantidadCarrito);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotalCarrito);
            btnMenos = itemView.findViewById(R.id.btnMenosCarrito);
            btnMas = itemView.findViewById(R.id.btnMasCarrito);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCarrito);
        }

        public void bind(CarritoItem item) {
            tvNombreProducto.setText(item.getProducto().getNombre());
            tvPrecio.setText("$" + item.getProducto().getPrecio());
            tvCantidad.setText(String.valueOf(item.getCantidad()));
            tvSubtotal.setText("$" + item.getSubtotal());

            // Botón eliminar
            btnEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEliminarClick(item.getId());
                }
            });

            // Botón disminuir cantidad
            btnMenos.setOnClickListener(v -> {
                int nuevaCantidad = item.getCantidad() - 1;
                if (nuevaCantidad >= 0) {
                    if (listener != null) {
                        listener.onCantidadChange(item.getId(), nuevaCantidad);
                    }
                }
            });

            // Botón aumentar cantidad
            btnMas.setOnClickListener(v -> {
                int nuevaCantidad = item.getCantidad() + 1;
                if (listener != null) {
                    listener.onCantidadChange(item.getId(), nuevaCantidad);
                }
            });
        }
    }
}