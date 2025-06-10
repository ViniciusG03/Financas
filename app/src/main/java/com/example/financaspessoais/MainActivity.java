package com.example.financaspessoais;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvSaldo, tvTotalReceitas, tvTotalDespesas;
    private RecyclerView recyclerViewTransacoes;
    private FloatingActionButton fabAddTransacao;
    private Button btnRelatorios;

    private DatabaseHelper databaseHelper;
    private TransacaoAdapter transacaoAdapter;
    private List<Transacao> listaTransacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();
        configurarRecyclerView();
        configurarEventos();
        atualizarDados();
    }

    private void inicializarComponentes() {
        tvSaldo = findViewById(R.id.tv_saldo);
        tvTotalReceitas = findViewById(R.id.tv_total_receitas);
        tvTotalDespesas = findViewById(R.id.tv_total_despesas);
        recyclerViewTransacoes = findViewById(R.id.recycler_view_transacoes);
        fabAddTransacao = findViewById(R.id.fab_add_transacao);
        btnRelatorios = findViewById(R.id.btn_relatorios);

        databaseHelper = new DatabaseHelper(this);
        listaTransacoes = new ArrayList<>();
    }

    private void configurarRecyclerView() {
        transacaoAdapter = new TransacaoAdapter(listaTransacoes, this);
        recyclerViewTransacoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTransacoes.setAdapter(transacaoAdapter);
    }

    private void configurarEventos() {
        fabAddTransacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTransacaoActivity.class);
                startActivity(intent);
            }
        });

        btnRelatorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RelatoriosActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarDados();
    }

    private void atualizarDados() {
        listaTransacoes.clear();
        listaTransacoes.addAll(databaseHelper.getAllTransacoes());
        transacaoAdapter.notifyDataSetChanged();

        calcularResumoFinanceiro();
    }

    private void calcularResumoFinanceiro() {
        double totalReceitas = 0;
        double totalDespesas = 0;

        for (Transacao transacao : listaTransacoes) {
            if (transacao.getTipo().equals("RECEITA")) {
                totalReceitas += transacao.getValor();
            } else {
                totalDespesas += transacao.getValor();
            }
        }

        double saldo = totalReceitas - totalDespesas;

        tvSaldo.setText(String.format("R$ %.2f", saldo));
        tvTotalReceitas.setText(String.format("R$ %.2f", totalReceitas));
        tvTotalDespesas.setText(String.format("R$ %.2f", totalDespesas));

        // Definir cor do saldo
        if (saldo >= 0) {
            tvSaldo.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvSaldo.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    public void excluirTransacao(int id) {
        databaseHelper.deleteTransacao(id);
        atualizarDados();
    }
}