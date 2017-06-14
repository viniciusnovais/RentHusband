package br.com.pdasolucoes.renthusband.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.pdasolucoes.renthusband.model.Usuario;

/**
 * Created by PDA on 08/06/2017.
 */

public class UsuarioDao {

    private DataBaseHelper helper;
    private SQLiteDatabase database;

    public UsuarioDao(Context context) {
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


    public long incluir(Usuario usuario) {
        ContentValues values = new ContentValues();

        values.put("nome", usuario.getNome());
        values.put("dataNasc", usuario.getDataNasc());
        Log.w("dataNasc","passou");
        values.put("endereco", usuario.getEndereco());
        values.put("sexo", usuario.getSexo());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());

        return getDataBase().insert("usuario", null, values);
    }

    public int buscarUltimoId() {
        int id = 0;
        Cursor cursor = getDataBase().rawQuery("SELECT MAX(_id) as maxId FROM usuario", null);

        try {
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("maxId"));
            }
        } finally {
            cursor.close();
        }

        return id;
    }

    public Usuario usuario() {
        Usuario u = new Usuario();
        Cursor cursor = getDataBase().rawQuery("SELECT * FROM usuario where _id=1", null);

        try {
            while (cursor.moveToNext()) {
                u.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                u.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                u.setSexo(cursor.getString(cursor.getColumnIndex("sexo")));
                u.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
                u.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                u.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
                u.setDataNasc(cursor.getString(cursor.getColumnIndex("dataNasc")));
            }
        } finally {
            cursor.close();
        }

        return u;
    }

}
