package br.com.pdasolucoes.renthusband.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.pdasolucoes.renthusband.model.Usuario;

/**
 * Created by PDA on 08/06/2017.
 */

public class LoginService {

    public static Usuario Login(String email,String senha) {

        String url = WebService.URL + "d0c9c9a0241d/search?email="+email+"&senha="+senha;
        String resposta = WebService.makeRequest(url);
        JSONObject jsonObject;
        Usuario usuario = new Usuario();

        if (resposta == null) {

        } else {

            try {
                JSONArray json = new JSONArray(resposta);
                for (int i=0;i<json.length();i++){
                    jsonObject = json.getJSONObject(i);

                    usuario.setId(jsonObject.getInt("id"));
                    usuario.setEmail(jsonObject.getString("email"));
                    usuario.setSenha(jsonObject.getString("senha"));
                    usuario.setNome(jsonObject.getString("nome"));
                    usuario.setDataNasc(jsonObject.getString("dataNasc"));
                    usuario.setSexo(jsonObject.getString("sexo"));
                    usuario.setEndereco(jsonObject.getString("endereco"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return usuario;
    }

    public static int BuscarUltimoId(){
        int id = 0;

        String url = WebService.URL + "d0c9c9a0241d/";
        String resposta = WebService.makeRequest(url);
        JSONObject jsonObject;

        if (resposta == null) {

        } else {

            try {
                JSONArray json = new JSONArray(resposta);
                for (int i=0;i<json.length();i++){
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
