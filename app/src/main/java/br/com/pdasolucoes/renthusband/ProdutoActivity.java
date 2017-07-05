package br.com.pdasolucoes.renthusband;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.pdasolucoes.renthusband.model.Aluguel;
import br.com.pdasolucoes.renthusband.model.Ferramenta;
import br.com.pdasolucoes.renthusband.model.Usuario;
import br.com.pdasolucoes.renthusband.util.CadastroUsuarioService;
import br.com.pdasolucoes.renthusband.util.ServiceAluguel;

public class ProdutoActivity extends AppCompatActivity {


    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView tvNomeProduto, tvDescricao, tvPreco, tvNomeDonoFerremanta, tvEmail;
    EditText editDias;
    Button buttonSolicitar, buttonConfirma, buttonCancela;
    AlertDialog dialog, dialogLoad;
    Usuario u = new Usuario();
    Ferramenta f;
    Aluguel aluguel = new Aluguel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        f = (Ferramenta) getIntent().getExtras().get("ferramenta");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        View v = findViewById(R.id.content_produto);
        tvNomeProduto = (TextView) v.findViewById(R.id.nomeProduto);
        tvDescricao = (TextView) v.findViewById(R.id.descricaoProduto);
        tvPreco = (TextView) v.findViewById(R.id.precoProduto);
        buttonSolicitar = (Button) v.findViewById(R.id.buttonSolicitar);
        tvNomeDonoFerremanta = (TextView) v.findViewById(R.id.nomeDono);

        byte[] decodedString = Base64.decode(f.getFoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Drawable drawable = new BitmapDrawable(getResources(), decodedByte);
        collapsingToolbarLayout.setBackground(drawable);

        tvNomeProduto.setText(f.getNome());
        tvDescricao.setText(f.getDescricao());
        tvPreco.setText(f.getPreco() + "");

        AsyncNomeDono task = new AsyncNomeDono();
        task.execute();


        View view = View.inflate(ProdutoActivity.this, R.layout.view_solicita, null);
        tvEmail = (TextView) view.findViewById(R.id.emailContato);
        editDias = (EditText) view.findViewById(R.id.editDias);
        buttonConfirma = (Button) view.findViewById(R.id.confirmaSolicitacao);
        buttonCancela = (Button) view.findViewById(R.id.cancela);

        AlertDialog.Builder builder = new AlertDialog.Builder(ProdutoActivity.this);
        builder.setView(view);
        dialog = builder.create();
        buttonSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        buttonCancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setView(R.layout.view_load);
        dialogLoad = builder1.create();
        buttonConfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editDias.getText().toString().equals("")) {
                    aluguel.setDiasSolicitacao(Integer.parseInt(editDias.getText().toString()));
                    dialog.dismiss();

                    dialogLoad.show();
                    AsyncSolicitar task1 = new AsyncSolicitar();
                    task1.execute();
                } else {
                    Toast.makeText(ProdutoActivity.this, "Preencha a quantidade", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public class AsyncNomeDono extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            u = CadastroUsuarioService.buscarDono(f.getIdUsuario());

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            tvNomeDonoFerremanta.setText("Falar com: " + u.getNome());
            tvEmail.setText(u.getEmail());

        }
    }

    public class AsyncSolicitar extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] params) {
            SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            aluguel.setId(ServiceAluguel.BuscarUltimoId()+1);
            aluguel.setDataSolicitacao(sdf.format(new Date()));
            aluguel.setConfirmacao(0);
            aluguel.setIdFerramenta(f.getId());
            aluguel.setIdUsuarioSolicita(preferences.getInt("idUsuarioLogado", 0));

            ServiceAluguel.cadastro(aluguel);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (dialogLoad.isShowing()) {
                dialogLoad.dismiss();
            }
        }
    }
}
