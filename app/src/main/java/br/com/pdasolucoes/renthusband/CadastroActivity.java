package br.com.pdasolucoes.renthusband;

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.renthusband.dao.UsuarioDao;
import br.com.pdasolucoes.renthusband.model.Usuario;
import br.com.pdasolucoes.renthusband.util.CadastroUsuarioService;

import static br.com.pdasolucoes.renthusband.R.array.*;

/**
 * Created by PDA on 05/06/2017.
 */

public class CadastroActivity extends AppCompatActivity {

    private AutoCompleteTextView tvNome, tvEndereco, tvDataNasc, tvEmail, tvSenha;
    private String[] sexo = new String[]{"Masculino", "Feminino", "Outro"};
    private RadioGroup radioGroup;
    private Button btCadastrar;
    private Usuario u = new Usuario();
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        usuarioDao = new UsuarioDao(this);

        tvNome = (AutoCompleteTextView) findViewById(R.id.nome);
        tvDataNasc = (AutoCompleteTextView) findViewById(R.id.dataNasc);
        tvEndereco = (AutoCompleteTextView) findViewById(R.id.endereco);
        tvEmail = (AutoCompleteTextView) findViewById(R.id.email);
        tvSenha = (AutoCompleteTextView) findViewById(R.id.senha);
        radioGroup = (RadioGroup) findViewById(R.id.radioButtonSexo);
        btCadastrar = (Button) findViewById(R.id.cadastrar);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);

                u.setSexo(rb.getText().toString());
            }
        });


        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                u.setNome(tvNome.getText().toString());
                u.setDataNasc(tvDataNasc.getText().toString());
                u.setEndereco(tvEndereco.getText().toString());
                u.setEmail(tvEmail.getText().toString());
                u.setSenha(tvSenha.getText().toString());

                usuarioDao.incluir(u);

                AsyncCadastroUsuario task = new AsyncCadastroUsuario();
                task.execute();
            }
        });

    }

    public class AsyncCadastroUsuario extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            u.setId(usuarioDao.buscarUltimoId());
            CadastroUsuarioService.cadastro(u);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }



}
