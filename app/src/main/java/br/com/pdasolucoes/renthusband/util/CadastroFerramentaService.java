package br.com.pdasolucoes.renthusband.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.pdasolucoes.renthusband.model.Ferramenta;

/**
 * Created by PDA on 08/06/2017.
 */

public class CadastroFerramentaService {

    public static void cadastro(Ferramenta ferramenta) {
        String resposta = "";

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",ferramenta.getId());
            jsonObject.put("nome", ferramenta.getNome());
            jsonObject.put("foto", ferramenta.getFoto());
            jsonObject.put("descricao", ferramenta.getDescricao());
            jsonObject.put("preco", ferramenta.getPreco());
            jsonObject.put("status", ferramenta.getStatus());
            jsonObject.put("tipo", ferramenta.getTipo());
            jsonObject.put("usuarioDono", ferramenta.getUsuarioDono().getId());


            try {
                URL url = new URL(WebService.URL + "b62ab90e75cc");
                HttpURLConnection conexao = null;
                conexao = (HttpURLConnection) url.openConnection();
                conexao.setDoInput(true);
                conexao.setDoOutput(true);
                conexao.setRequestProperty("Content-Type", "application/json");
                conexao.setRequestProperty("Accept", "application/json");
                conexao.setRequestMethod("POST");


                conexao.connect();
                OutputStreamWriter wr = new OutputStreamWriter(conexao.getOutputStream());
                wr.write(jsonObject.toString());

                OutputStream os = conexao.getOutputStream();
                os.write(jsonObject.toString().getBytes("UTF-8"));
                //os.close();

                InputStream in;
                int status = conexao.getResponseCode();
                if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
                    Log.d("ERRO", "Error code: " + status);
                    in = conexao.getErrorStream();
                } else {
                    in = conexao.getInputStream();
                }

                resposta = WebService.readStream(in);
                Log.w("Resposta", resposta);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
