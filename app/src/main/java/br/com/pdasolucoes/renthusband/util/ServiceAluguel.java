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

import br.com.pdasolucoes.renthusband.model.Aluguel;

/**
 * Created by PDA on 05/07/2017.
 */

public class ServiceAluguel {


    public static void cadastro(Aluguel a) {

        String resposta = "";

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", a.getId());
            jsonObject.put("dataSolicitacao", a.getDataSolicitacao());
            jsonObject.put("diasSolicitacao", a.getDiasSolicitacao());
            jsonObject.put("confirmacao", a.getConfirmacao());
            jsonObject.put("dataAluguel", a.getDataAluguel());
            jsonObject.put("dataDevolucao", a.getDataDevolucao());
            jsonObject.put("ferramenta", a.getIdFerramenta());
            jsonObject.put("usuarioSolicita", a.getIdUsuarioSolicita());


            try {
                URL url = new URL(WebService.URL + "1bd5b175dcd1");
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

    public static int BuscarUltimoId() {
        int id = 0;

        String url = WebService.URL + "1bd5b175dcd1";
        String resposta = WebService.makeRequest(url);
        JSONObject jsonObject;

        if (resposta == null) {

        } else {

            try {
                JSONArray json = new JSONArray(resposta);
                for (int i = 0; i < json.length(); i++) {
                    jsonObject = json.getJSONObject(i);

                    id = jsonObject.getInt("id");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return id;
    }
}
