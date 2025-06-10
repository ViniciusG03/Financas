package com.example.financaspessoais;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "financas_pessoais.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela Transações
    private static final String TABLE_TRANSACOES = "transacoes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRICAO = "descricao";
    private static final String COLUMN_VALOR = "valor";
    private static final String COLUMN_TIPO = "tipo";
    private static final String COLUMN_CATEGORIA = "categoria";
    private static final String COLUMN_DATA = "data";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRANSACOES_TABLE = "CREATE TABLE " + TABLE_TRANSACOES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DESCRICAO + " TEXT,"
                + COLUMN_VALOR + " REAL,"
                + COLUMN_TIPO + " TEXT,"
                + COLUMN_CATEGORIA + " TEXT,"
                + COLUMN_DATA + " TEXT" + ")";
        db.execSQL(CREATE_TRANSACOES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACOES);
        onCreate(db);
    }

    // Adicionar nova transação
    public long addTransacao(Transacao transacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DESCRICAO, transacao.getDescricao());
        values.put(COLUMN_VALOR, transacao.getValor());
        values.put(COLUMN_TIPO, transacao.getTipo());
        values.put(COLUMN_CATEGORIA, transacao.getCategoria());
        values.put(COLUMN_DATA, transacao.getData());

        long id = db.insert(TABLE_TRANSACOES, null, values);
        db.close();
        return id;
    }

    // Buscar transação por ID
    public Transacao getTransacao(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRANSACOES,
                new String[]{COLUMN_ID, COLUMN_DESCRICAO, COLUMN_VALOR,
                        COLUMN_TIPO, COLUMN_CATEGORIA, COLUMN_DATA},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Transacao transacao = new Transacao(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getDouble(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5)
        );

        cursor.close();
        db.close();
        return transacao;
    }

    // Buscar todas as transações
    public List<Transacao> getAllTransacoes() {
        List<Transacao> transacoes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACOES + " ORDER BY " + COLUMN_DATA + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Transacao transacao = new Transacao();
                transacao.setId(cursor.getInt(0));
                transacao.setDescricao(cursor.getString(1));
                transacao.setValor(cursor.getDouble(2));
                transacao.setTipo(cursor.getString(3));
                transacao.setCategoria(cursor.getString(4));
                transacao.setData(cursor.getString(5));

                transacoes.add(transacao);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transacoes;
    }

    // Atualizar transação
    public int updateTransacao(Transacao transacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DESCRICAO, transacao.getDescricao());
        values.put(COLUMN_VALOR, transacao.getValor());
        values.put(COLUMN_TIPO, transacao.getTipo());
        values.put(COLUMN_CATEGORIA, transacao.getCategoria());
        values.put(COLUMN_DATA, transacao.getData());

        int rowsAffected = db.update(TABLE_TRANSACOES, values,
                COLUMN_ID + "=?", new String[]{String.valueOf(transacao.getId())});
        db.close();
        return rowsAffected;
    }

    // Deletar transação
    public void deleteTransacao(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACOES, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Buscar transações por tipo
    public List<Transacao> getTransacoesPorTipo(String tipo) {
        List<Transacao> transacoes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACOES +
                " WHERE " + COLUMN_TIPO + "=? ORDER BY " + COLUMN_DATA + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{tipo});

        if (cursor.moveToFirst()) {
            do {
                Transacao transacao = new Transacao();
                transacao.setId(cursor.getInt(0));
                transacao.setDescricao(cursor.getString(1));
                transacao.setValor(cursor.getDouble(2));
                transacao.setTipo(cursor.getString(3));
                transacao.setCategoria(cursor.getString(4));
                transacao.setData(cursor.getString(5));

                transacoes.add(transacao);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transacoes;
    }

    // Buscar transações por categoria
    public List<Transacao> getTransacoesPorCategoria(String categoria) {
        List<Transacao> transacoes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACOES +
                " WHERE " + COLUMN_CATEGORIA + "=? ORDER BY " + COLUMN_DATA + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{categoria});

        if (cursor.moveToFirst()) {
            do {
                Transacao transacao = new Transacao();
                transacao.setId(cursor.getInt(0));
                transacao.setDescricao(cursor.getString(1));
                transacao.setValor(cursor.getDouble(2));
                transacao.setTipo(cursor.getString(3));
                transacao.setCategoria(cursor.getString(4));
                transacao.setData(cursor.getString(5));

                transacoes.add(transacao);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transacoes;
    }

    // Calcular total por tipo
    public double getTotalPorTipo(String tipo) {
        double total = 0;
        String selectQuery = "SELECT SUM(" + COLUMN_VALOR + ") FROM " + TABLE_TRANSACOES +
                " WHERE " + COLUMN_TIPO + "=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{tipo});

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }
}
