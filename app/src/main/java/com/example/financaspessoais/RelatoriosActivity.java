package com.example.financaspessoais;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatoriosActivity extends AppCompatActivity {

    private TextView tvTotalReceitas, tvTotalDespesas, tvSaldoTotal;
    private RecyclerView recyclerViewReceitas, recyclerViewDespesas;

    private DatabaseHelper databaseHelper;
    private CategoriaAdapter receitasAdapter, despesasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);

        inicializarComponentes();
        configurarRecyclerViews();
        carregarDados();
    }

    private void inicializarComponentes() {
        tvTotalReceitas = findViewById(R.id.tv_total_receitas_relatorio);
        tvTotalDespesas = findViewById(R.id.tv_total_despesas_relatorio);
        tvSaldoTotal = findViewById(R.id.tv_saldo_total_relatorio);
        recyclerViewReceitas = findViewById(R.id.recycler_view_receitas);
        recyclerViewDespesas = findViewById(R.id.recycler_view_despesas);

        databaseHelper = new DatabaseHelper(this);
    }

    private void configurarRecyclerViews() {
        recyclerViewReceitas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDespesas.setLayoutManager(new LinearLayoutManager(this));
    }

    private void carregarDados() {
        // Buscar todas as transações
        List<Transacao> todasTransacoes = databaseHelper.getAllTransacoes();

        // Calcular totais
        double totalReceitas = 0;
        double totalDespesas = 0;

        // Mapas para agrupar por categoria
        Map<String, Double> receitasPorCategoria = new HashMap<>();
        Map<String, Double> despesasPorCategoria = new HashMap<>();

        for (Transacao transacao : todasTransacoes) {
            if (transacao.getTipo().equals("RECEITA")) {
                totalReceitas += transacao.getValor();
                receitasPorCategoria.put(transacao.getCategoria(),
                        receitasPorCategoria.getOrDefault(transacao.getCategoria(), 0.0) + transacao.getValor());
            } else {
                totalDespesas += transacao.getValor();
                despesasPorCategoria.put(transacao.getCategoria(),
                        despesasPorCategoria.getOrDefault(transacao.getCategoria(), 0.0) + transacao.getValor());
            }
        }

        double saldoTotal = totalReceitas - totalDespesas;

        // Atualizar TextViews
        tvTotalReceitas.setText(String.format("R$ %.2f", totalReceitas));
        tvTotalDespesas.setText(String.format("R$ %.2f", totalDespesas));
        tvSaldoTotal.setText(String.format("R$ %.2f", saldoTotal));

        // Definir cor do saldo
        if (saldoTotal >= 0) {
            tvSaldoTotal.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvSaldoTotal.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        // Configurar adapters
        List<CategoriaResumo> resumoReceitas = converterMapaParaLista(receitasPorCategoria);
        List<CategoriaResumo> resumoDespesas = converterMapaParaLista(despesasPorCategoria);

        receitasAdapter = new CategoriaAdapter(resumoReceitas, "RECEITA");
        despesasAdapter = new CategoriaAdapter(resumoDespesas, "DESPESA");

        recyclerViewReceitas.setAdapter(receitasAdapter);
        recyclerViewDespesas.setAdapter(despesasAdapter);
    }

    private List<CategoriaResumo> converterMapaParaLista(Map<String, Double> mapa) {
        List<CategoriaResumo> lista = new ArrayList<>();
        for (Map.Entry<String, Double> entry : mapa.entrySet()) {
            lista.add(new CategoriaResumo(entry.getKey(), entry.getValue()));
        }
        return lista;
    }
}
