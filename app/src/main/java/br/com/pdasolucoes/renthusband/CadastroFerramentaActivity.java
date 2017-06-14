package br.com.pdasolucoes.renthusband;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import br.com.pdasolucoes.renthusband.dao.FerramentaDao;
import br.com.pdasolucoes.renthusband.model.Ferramenta;
import br.com.pdasolucoes.renthusband.model.Usuario;
import br.com.pdasolucoes.renthusband.util.CadastroFerramentaService;

/**
 * Created by PDA on 08/06/2017.
 */

public class CadastroFerramentaActivity extends AppCompatActivity {

    private AutoCompleteTextView tvNomeProduto, tvDescricao, tvPreco;
    private Spinner spinnerTipo;
    private ImageView imageFoto;
    private Button btCadastrar;
    private FerramentaDao ferramentaDao;
    private Ferramenta f = new Ferramenta();
    private String[] tipo = new String[]{"Industrial", "Doméstico"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ferramenta);

        ferramentaDao = new FerramentaDao(this);

        tvNomeProduto = (AutoCompleteTextView) findViewById(R.id.nomeProduto);
        tvDescricao = (AutoCompleteTextView) findViewById(R.id.descricao);
        tvPreco = (AutoCompleteTextView) findViewById(R.id.preco);
        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);
        imageFoto = (ImageView) findViewById(R.id.foto);
        btCadastrar = (Button) findViewById(R.id.cadastrar);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tipo);
        spinnerTipo.setAdapter(adapter);

        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                f.setTipo(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.setNome(tvNomeProduto.getText().toString());
                f.setDescricao(tvDescricao.getText().toString());
                f.setPreco(Double.parseDouble(tvPreco.getText().toString()));
                f.setStatus(0);
                f.setUsuarioDono((Usuario) getIntent().getSerializableExtra("usuario"));

                ferramentaDao.incluir(f);

                AsyncCadastrarFeramenta task = new AsyncCadastrarFeramenta();
                task.execute();
            }
        });

    }

    public class AsyncCadastrarFeramenta extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            f.setId(ferramentaDao.buscarUltimoId());

            CadastroFerramentaService.cadastro(f);
            return null;
        }
    }
}
