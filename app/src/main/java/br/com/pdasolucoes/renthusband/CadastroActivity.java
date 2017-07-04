package br.com.pdasolucoes.renthusband;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.RunnableFuture;

import br.com.pdasolucoes.renthusband.dao.UsuarioDao;
import br.com.pdasolucoes.renthusband.model.Usuario;
import br.com.pdasolucoes.renthusband.util.CadastroUsuarioService;
import br.com.pdasolucoes.renthusband.util.LoginService;

import static br.com.pdasolucoes.renthusband.R.array.*;

/**
 * Created by PDA on 05/06/2017.
 */

public class CadastroActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private AutoCompleteTextView tvNome, tvEndereco, tvDataNasc, tvEmail, tvSenha, tvConfirmarSenha;
    private String[] sexo = new String[]{"Masculino", "Feminino", "Outro"};
    private RadioGroup radioGroup;
    private Button btCadastrar;
    private Usuario u = new Usuario();
    private UsuarioDao usuarioDao;
    private Calendar calendar;
    private AlertDialog dialog, dialogSucess;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        usuarioDao = new UsuarioDao(this);
        calendar = Calendar.getInstance();

        tvNome = (AutoCompleteTextView) findViewById(R.id.nome);
        tvDataNasc = (AutoCompleteTextView) findViewById(R.id.dataNasc);
        tvEndereco = (AutoCompleteTextView) findViewById(R.id.endereco);
        tvEmail = (AutoCompleteTextView) findViewById(R.id.email);
        tvSenha = (AutoCompleteTextView) findViewById(R.id.senha);
        tvConfirmarSenha = (AutoCompleteTextView) findViewById(R.id.confirmarSenha);
        radioGroup = (RadioGroup) findViewById(R.id.radioButtonSexo);
        btCadastrar = (Button) findViewById(R.id.cadastrar);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.view_load);
        dialog = builder.create();

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setView(R.layout.sucess);
        dialogSucess = builder2.create();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);

                u.setSexo(rb.getText().toString());
            }
        });

        tvDataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CadastroActivity.this, CadastroActivity.this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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

                ValidaTamanhoSenha(tvSenha);
                if (ValidaCadastro()) {

                    usuarioDao.incluir(u);

                    AsyncCadastroUsuario task = new AsyncCadastroUsuario();
                    task.execute();
                }
            }
        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {


        calendar.set(Calendar.YEAR, ano);
        calendar.set(Calendar.MONTH, mes);
        calendar.set(Calendar.DAY_OF_MONTH, dia);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        tvDataNasc.setText(sdf.format(calendar.getTime()));

    }

    public class AsyncCadastroUsuario extends AsyncTask<Object, Integer, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {


            u.setId(LoginService.BuscarUltimoId() + 1);
            CadastroUsuarioService.cadastro(u);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (dialog.isShowing()) {
                dialog.dismiss();
                limpar();
            }

            dialogSucess.show();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (dialogSucess.isShowing()) {
                        dialogSucess.dismiss();
                    }
                }
            };

            dialogSucess.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    handler.removeCallbacks(runnable);
                }
            });

            handler.postDelayed(runnable, 100000);
            finish();
        }

}


    private boolean ValidaCadastro() {

        if (tvNome.getText().toString().trim().equals("") || tvDataNasc.getText().toString().trim().equals("")
                || tvEmail.getText().toString().trim().equals("") || tvEndereco.getText().toString().trim().equals("")
                || tvConfirmarSenha.getText().toString().trim().equals("")) {
            Toast.makeText(CadastroActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!ValidaIdade(calendar)) {
            Toast.makeText(CadastroActivity.this, "Usuário deve ser maior de 18 anos", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!ValidaTamanhoSenha(tvSenha)) {
            Toast.makeText(CadastroActivity.this, "Senha deve ter mínimo de 5 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!tvConfirmarSenha.getText().toString().equals(tvSenha.getText().toString())) {
            Toast.makeText(CadastroActivity.this, "Senhas não conferem", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean ValidaIdade(final Calendar dataNasc) {
        final Date hoje = new Date();

        dataNasc.add(Calendar.YEAR, 18);
        Date data = dataNasc.getTime();

        if (hoje.before(data)) {
            dataNasc.add(Calendar.YEAR, -18);
            return false;
        }
        dataNasc.add(Calendar.YEAR, -18);

        return true;
    }

    private boolean ValidaTamanhoSenha(EditText edit) {

        int tamanho = edit.getText().length();

        if (tamanho < 5) {
            return false;
        }
        return true;
    }

    private void limpar() {
        tvNome.setText("");
        tvEndereco.setText("");
        tvConfirmarSenha.setText("");
        tvSenha.setText("");
        tvDataNasc.setText("");
        tvEmail.setText("");
    }

}
