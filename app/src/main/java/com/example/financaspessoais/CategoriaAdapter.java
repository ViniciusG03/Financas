package com.example.financaspessoais;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private List<CategoriaResumo> categorias;
    private String tipo;

    public CategoriaAdapter(List<CategoriaResumo> categorias, String tipo) {
        this.categorias = categorias;
        this.tipo = tipo;
    }

    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria_resumo, parent, false);
        return new CategoriaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoriaViewHolder holder, int position) {
        CategoriaResumo categoria = categorias.get(position);

        holder.tvNomeCategoria.setText(categoria.getCategoria());
        holder.tvValorCategoria.setText(String.format("R$ %.2f", categoria.getValor()));

        // Definir cor baseada no tipo
        if (tipo.equals("RECEITA")) {
            holder.tvValorCategoria.setTextColor(
                    holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvValorCategoria.setTextColor(
                    holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class CategoriaViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNomeCategoria, tvValorCategoria;

        public CategoriaViewHolder(View view) {
            super(view);
            tvNomeCategoria = view.findViewById(R.id.tv_nome_categoria);
            tvValorCategoria = view.findViewById(R.id.tv_valor_categoria);
        }
    }
}