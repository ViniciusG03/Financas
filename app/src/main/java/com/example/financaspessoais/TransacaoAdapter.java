package com.example.financaspessoais;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransacaoAdapter extends RecyclerView.Adapter<TransacaoAdapter.TransacaoViewHolder> {

    private List<Transacao> transacoes;
    private Context context;
    private MainActivity mainActivity;

    public TransacaoAdapter(List<Transacao> transacoes, MainActivity mainActivity) {
        this.transacoes = transacoes;
        this.mainActivity = mainActivity;
        this.context = mainActivity;
    }

    @Override
    public TransacaoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transacao, parent, false);
        return new TransacaoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransacaoViewHolder holder, int position) {
        Transacao transacao = transacoes.get(position);

        holder.tvDescricao.setText(transacao.getDescricao());
        holder.tvValor.setText(String.format("R$ %.2f", transacao.getValor()));
        holder.tvCategoria.setText(transacao.getCategoria());
        holder.tvData.setText(transacao.getData());

        // Definir cor e ícone baseado no tipo
        if (transacao.getTipo().equals("RECEITA")) {
            holder.tvValor.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.ivTipo.setImageResource(R.drawable.ic_arrow_up);
            holder.ivTipo.setColorFilter(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvValor.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.ivTipo.setImageResource(R.drawable.ic_arrow_down);
            holder.ivTipo.setColorFilter(context.getResources().getColor(android.R.color.holo_red_dark));
        }

        // Configurar clique longo para excluir
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mostrarDialogoExclusao(transacao);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return transacoes.size();
    }

    private void mostrarDialogoExclusao(Transacao transacao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Excluir Transação");
        builder.setMessage("Tem certeza que deseja excluir esta transação?");

        builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainActivity.excluirTransacao(transacao.getId());
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public class TransacaoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDescricao, tvValor, tvCategoria, tvData;
        public ImageView ivTipo;

        public TransacaoViewHolder(View view) {
            super(view);
            tvDescricao = view.findViewById(R.id.tv_descricao);
            tvValor = view.findViewById(R.id.tv_valor);
            tvCategoria = view.findViewById(R.id.tv_categoria);
            tvData = view.findViewById(R.id.tv_data);
            ivTipo = view.findViewById(R.id.iv_tipo);
        }
    }
}
