package br.com.pdasolucoes.renthusband.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PDA on 08/06/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper{
    private static final String BANCO = "renthusband";
    private static final int VERSAO = 1;

    public DataBaseHelper(Context context) {
        super(context, BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE if not exists usuario(_id INTEGER PRIMARY KEY, nome TEXT NOT NULL, dataNasc TEXT NOT NULL, endereco TEXT NOT NULL, " +
                "sexo TEXT NOT NULL, email TEXT NOT NULL, senha TEXT NOT NULL)");

        db.execSQL("CREATE TABLE if not exists ferramenta(_id INTEGER PRIMARY KEY, nome TEXT NOT NULL, foto blob, descricao TEXT NOT NULL, " +
                "preco REAL NOT NULL, status INTEGER NOT NULL, tipo INTEGER NOT NULL, usuarioDono INTEGER NOT NULL, FOREIGN KEY(usuarioDono) REFERENCES usuario(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
