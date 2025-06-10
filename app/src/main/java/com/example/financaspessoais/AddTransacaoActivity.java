package com.example.financaspessoais;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTransacaoActivity extends AppCompatActivity {

    private EditText etDescricao, etValor, etData;
    private RadioGroup rgTipo;
    private Spinner spinnerCategoria;
    private Button btnSalvar, btnCancelar;

    private DatabaseHelper databaseHelper;
    private Calendar calendar;

    // Categorias predefinidas
    private String[] categoriasReceita = {"Salário", "Freelance", "Investimentos", "Vendas", "Outros"};
    private String[] categoriasDespesa = {"Alimentação", "Transporte", "Moradia", "Saúde", "Educação",
            "Lazer", "Compras", "Contas", "Outros"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transacao);

        inicializarComponentes();
        configurarEventos();
        configurarSpinner();
    }

    private void inicializarComponentes() {
        etDescricao = findViewById(R.id.et_descricao);
        etValor = findViewById(R.id.et_valor);
        etData = findViewById(R.id.et_data);
        rgTipo = findViewById(R.id.rg_tipo);
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        btnSalvar = findViewById(R.id.btn_salvar);
        btnCancelar = findViewById(R.id.btn_cancelar);

        databaseHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();

        // Definir data atual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        etData.setText(dateFormat.format(calendar.getTime()));
    }

    private void configurarEventos() {
        etData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });

        rgTipo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                configurarSpinner();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarTransacao();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configurarSpinner() {
        String[] categorias;
        int selectedId = rgTipo.getCheckedRadioButtonId();

        if (selectedId == R.id.rb_receita) {
            categorias = categoriasReceita;
        } else {
            categorias = categoriasDespesa;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    private void mostrarDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        etData.setText(dateFormat.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void salvarTransacao() {
        String descricao = etDescricao.getText().toString().trim();
        String valorStr = etValor.getText().toString().trim();
        String data = etData.getText().toString();
        String categoria = spinnerCategoria.getSelectedItem().toString();

        // Validações
        if (descricao.isEmpty()) {
            etDescricao.setError("Descrição é obrigatória");
            etDescricao.requestFocus();
            return;
        }

        if (valorStr.isEmpty()) {
            etValor.setError("Valor é obrigatório");
            etValor.requestFocus();
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor <= 0) {
                etValor.setError("Valor deve ser maior que zero");
                etValor.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etValor.setError("Valor inválido");
            etValor.requestFocus();
            return;
        }

        if (rgTipo.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecione o tipo da transação", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determinar tipo
        String tipo;
        if (rgTipo.getCheckedRadioButtonId() == R.id.rb_receita) {
            tipo = "RECEITA";
        } else {
            tipo = "DESPESA";
        }

        // Criar e salvar transação
        Transacao transacao = new Transacao(descricao, valor, tipo, categoria, data);
        long id = databaseHelper.addTransacao(transacao);

        if (id != -1) {
            Toast.makeText(this, "Transação salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar transação", Toast.LENGTH_SHORT).show();
        }
    }
}