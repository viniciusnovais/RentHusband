package br.com.pdasolucoes.renthusband;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.File;

import br.com.pdasolucoes.renthusband.dao.FerramentaDao;
import br.com.pdasolucoes.renthusband.model.Ferramenta;
import br.com.pdasolucoes.renthusband.model.Usuario;
import br.com.pdasolucoes.renthusband.util.CadastroFerramentaService;

/**
 * Created by PDA on 08/06/2017.
 */

public class CadastroFerramentaActivity extends AppCompatActivity {

    private AutoCompleteTextView tvDescricao, tvPreco;
    private Spinner spinnerTipo, spinnerProduto;
    private ImageView imageFoto;
    private Button btCadastrar;
    private Bitmap originalBitmap, resizedBitmap, photo;
    private Uri uriImagem;
    private final int CAMERA = 0, GALERIA = 1;
    private FerramentaDao ferramentaDao;
    private Ferramenta f = new Ferramenta();
    private AlertDialog dialogOpcoes;
    private String[] tipo = new String[]{"Industrial", "Doméstico"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ferramenta);

        ferramentaDao = new FerramentaDao(this);

        tvDescricao = (AutoCompleteTextView) findViewById(R.id.descricao);
        tvPreco = (AutoCompleteTextView) findViewById(R.id.preco);
        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);
        spinnerProduto = (Spinner) findViewById(R.id.spinnerProduto);
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

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.produto));
        spinnerProduto.setAdapter(adapter1);

        spinnerProduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                f.setNome(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Dialog com as opçoes da camera
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        String[] item = new String[2];
        item[0] = getString(R.string.camera);
        item[1] = getString(R.string.galeria);

        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                switch (which) {
                    //Abrir Camera
                    case 0:
                        intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA);
                        break;
                    //Abrir Galeria
                    case 1:
                        intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALERIA);
                        break;
                    default:
                        break;
                }
            }
        });

        dialogOpcoes = builder.create();

        imageFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpcoes.show();
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        byte[] imageByte;
        if (resultCode == CadastroFerramentaActivity.RESULT_OK) {
            //Colocar imagem da Camera no Dialog
            switch (requestCode) {

                case CAMERA:
                    photo = (Bitmap) data.getExtras().get("data");
                    originalBitmap = photo;
                    resizedBitmap = Bitmap.createScaledBitmap(
                            originalBitmap, 530, 530, false);

                    imageFoto.setImageBitmap(resizedBitmap);

                    imageByte = getBitmapAsByteArray(originalBitmap);
                    String encoded = Base64.encodeToString(imageByte, Base64.DEFAULT);
                    f.setFoto(encoded);
                    break;
                //Trazer imagem da galeria e colocar no dialog

                case GALERIA:
                    String[] colunaCaminhoArquivo = {MediaStore.Images.Media.DATA};
                    uriImagem = data.getData();
                    Cursor cursor = getContentResolver().query(
                            uriImagem, colunaCaminhoArquivo, null, null, null);
                    cursor.moveToFirst();
                    int colunaIndex = cursor
                            .getColumnIndex(colunaCaminhoArquivo[0]);
                    String caminhoImagem = cursor.getString(colunaIndex);
                    cursor.close();
                    uriImagem = Uri.fromFile(new File(caminhoImagem));
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImagem);
                        originalBitmap = photo;
                        resizedBitmap = Bitmap.createScaledBitmap(
                                originalBitmap, 530, 530, false);
                        imageFoto.setImageBitmap(resizedBitmap);

                        imageByte = getBitmapAsByteArray(originalBitmap);
                        String encoded2 = Base64.encodeToString(imageByte, Base64.DEFAULT);
                        f.setFoto(encoded2);
                    } catch (Exception e) {
                    }
                    break;
            }
        }
    }

    public class AsyncCadastrarFeramenta extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            f.setId(ferramentaDao.buscarUltimoId());

            CadastroFerramentaService.cadastro(f);
            return null;
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
}
