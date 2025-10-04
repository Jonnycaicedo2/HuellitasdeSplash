package com.example.huellitasdesplash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.huellitasdesplash.R;
import com.example.huellitasdesplash.models.Articulo;
import java.util.List;

public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.ArticuloViewHolder> {

    private List<Articulo> articulos;
    private OnArticuloClickListener listener;

    public interface OnArticuloClickListener {
        void onArticuloClick(Articulo articulo);
    }

    public ArticuloAdapter(List<Articulo> articulos, OnArticuloClickListener listener) {
        this.articulos = articulos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticuloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_articulo, parent, false);
        return new ArticuloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticuloViewHolder holder, int position) {
        Articulo articulo = articulos.get(position);
        holder.bind(articulo, listener);
    }

    @Override
    public int getItemCount() {
        return articulos.size();
    }

    static class ArticuloViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImagen;
        private TextView tvTitulo, tvSubtitulo, tvCategoria, tvTiempoLectura;

        public ArticuloViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvSubtitulo = itemView.findViewById(R.id.tvSubtitulo);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvTiempoLectura = itemView.findViewById(R.id.tvTiempoLectura);
        }

        public void bind(final Articulo articulo, final OnArticuloClickListener listener) {
            ivImagen.setImageResource(articulo.getImagenResId());
            tvTitulo.setText(articulo.getTitulo());
            tvSubtitulo.setText(articulo.getSubtitulo());
            tvCategoria.setText(articulo.getCategoria());
            tvTiempoLectura.setText(articulo.getTiempoLectura() + " min lectura");

            itemView.setOnClickListener(v -> listener.onArticuloClick(articulo));
        }
    }
}