package br.com.pdasolucoes.renthusband.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.renthusband.model.Ferramenta;
import br.com.pdasolucoes.renthusband.model.Usuario;

/**
 * Created by PDA on 07/06/2017.
 */

public class CadastroUsuarioService {

    public static void cadastro(Usuario usuario) {
        String resposta = "";

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", usuario.getId());
            jsonObject.put("nome", usuario.getNome());
            jsonObject.put("dataNasc", usuario.getDataNasc());
            jsonObject.put("endereco", usuario.getEndereco());
            jsonObject.put("sexo", usuario.getSexo());
            jsonObject.put("email", usuario.getEmail());
            jsonObject.put("senha", usuario.getSenha());


            try {
                URL url = new URL(WebService.URL + "d0c9c9a0241d");
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

    public static Usuario buscarDono(int id) {

        String url = WebService.URL + "d0c9c9a0241d/search?id="+id;
        String resposta = WebService.makeRequest(url);
        JSONObject jsonObject;
        Usuario u = new Usuario();

        if (resposta == null) {

        } else {

            try {
                JSONArray json = new JSONArray(resposta);
                for (int i = 0; i < json.length(); i++) {
                    jsonObject = json.getJSONObject(i);

                    u.setId(jsonObject.getInt("id"));
                    u.setNome(jsonObject.getString("nome"));
                    u.setDataNasc(jsonObject.getString("dataNasc"));
                    u.setEndereco(jsonObject.getString("endereco"));
                    u.setSexo(jsonObject.getString("sexo"));
                    u.setEmail(jsonObject.getString("email"));

            }

        } catch(JSONException e){
            e.printStackTrace();
        }
    }

        return u;
}
}
