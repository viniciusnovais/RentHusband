package br.com.pdasolucoes.renthusband.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.pdasolucoes.renthusband.model.Ferramenta;

/**
 * Created by PDA on 08/06/2017.
 */

public class FerramentaDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public FerramentaDao(Context context) {
        helper = new DataBaseHelper(context);
    }

    public SQLiteDatabase getDataBase() {
        if (database == null) {
            database = helper.getWritableDatabase();
        }

        return database;
    }

    public void close() {
        helper.close();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }


    public long incluir(Ferramenta ferramenta) {
        ContentValues values = new ContentValues();

        values.put("nome", ferramenta.getNome());
        values.put("foto", ferramenta.getFoto());
        values.put("descricao", ferramenta.getDescricao());
        values.put("preco", ferramenta.getPreco());
        values.put("status", ferramenta.getStatus());
        values.put("tipo", ferramenta.getTipo());
        values.put("usuarioDono", ferramenta.getUsuarioDono().getId());

        return getDataBase().insert("ferramenta", null, values);
    }

    public int buscarUltimoId() {
        int id = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT MAX(_id) as maxId FROM ferramenta", null);

        try {
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("maxId"));
            }
        } finally {
            cursor.close();
        }

        return id;
    }


}
