package br.com.pdasolucoes.renthusband;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.renthusband.adapter.ListaProdutosAdapter;
import br.com.pdasolucoes.renthusband.model.Ferramenta;
import br.com.pdasolucoes.renthusband.model.Usuario;
import br.com.pdasolucoes.renthusband.util.CadastroFerramentaService;

public class MainActivity extends AbsRuntimePermission
        implements NavigationView.OnNavigationItemSelectedListener {

    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    public static final int REQUEST_PERMISSION = 10;
    private ListaProdutosAdapter adapter;
    private TextView tvNomeCompleto, tvidade;
    private Usuario usuario;
    private List<Ferramenta> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        requestAppPermissions(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,}, R.string.msg, REQUEST_PERMISSION);

        //pegando o usuario Logado
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //pegando o navigationdrawer
        View v = navigationView.getHeaderView(0);

        //Setando nas variaveis
        tvNomeCompleto = (TextView) v.findViewById(R.id.nomeCompleto);
        tvidade = (TextView) v.findViewById(R.id.idade);
        tvNomeCompleto.setText(usuario.getNome().toString());
        tvidade.setText(usuario.getDataNasc().toString());


        navigationView.setNavigationItemSelectedListener(this);

        gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        AsyncLista task = new AsyncLista();
        task.execute();


    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_compartilhar) {
            Intent i = new Intent(MainActivity.this, CadastroFerramentaActivity.class);
            i.putExtra("usuario", usuario);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_solicitacoes) {

        } else if (id == R.id.nav_perfil) {
            Intent i = new Intent(MainActivity.this, PerfilActivity.class);
            startActivity(i);


        } else if (id == R.id.nav_batepapo) {

        } else if (id == R.id.nav_consultar) {
            Intent i = new Intent(MainActivity.this, ProdutoActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_sair) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public class AsyncLista extends AsyncTask<Object, Integer, List<Ferramenta>> {


        @Override
        protected List<Ferramenta> doInBackground(Object[] params) {
            lista = CadastroFerramentaService.listar(usuario.getId());

            return lista;
        }

        @Override
        protected void onPostExecute(final List<Ferramenta> ferramentas) {
            super.onPostExecute(ferramentas);

            adapter = new ListaProdutosAdapter(ferramentas, MainActivity.this);
            recyclerView.setAdapter(adapter);

        }
    }

}
