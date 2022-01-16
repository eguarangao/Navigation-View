package com.example.tareavistadenavegacin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tareavistadenavegacin.modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable {

    private Button btnIngresar;
    private EditText username, password;
    private RequestQueue requestQueue;

    private String url = "https://my-json-server.typicode.com/eguarangao/Json/Usuarios";
    private int pos;
    List<Usuario> listUsuario = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//inicializamos los componentes respectivos
        btnIngresar = (Button) findViewById(R.id.btnIniciarSesion);
        username = (EditText) findViewById(R.id.edtxNombre);
        password = (EditText) findViewById(R.id.edtxContraseña);

        getUsuario();
    }

    public void iniciarSesion(View view) {

        if (validar(listUsuario))
        {
            Usuario unUsuario=listUsuario.get(pos);
            Intent it=new Intent(this,activityNavigation.class);
            it.putExtra("usuario",unUsuario);
            startActivity(it);

        }else{
            Toast.makeText(this,"Usuario y Contraseña INCORRECTOS. ",Toast.LENGTH_LONG).show();
        }

    }


    //Procedimiento para consumir servicio web con Volley
    private void getUsuario() {

        JsonArrayRequest requestJson = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        enviarUsuario(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error al conectarse:" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(requestJson);
    }


    //Procedimiento para guardar en el objeto Usuario lo consumido en el servicio
    private List<Usuario> enviarUsuario(JSONArray jArray) {

        for (int i=0;i<jArray.length();i++){
            try {
                //Pasamos al objeto json los valores de el primer usuario
                JSONObject objetoJson=new JSONObject(jArray.get(i).toString());

                //Agregamos al objeto Usuario los datos recogidos
                listUsuario.add(new Usuario(objetoJson.getString("nomUsuario"),
                        objetoJson.getString("nombres"),
                        objetoJson.getString("contraseña"),
                        objetoJson.getString("img"),
                        objetoJson.getJSONArray("opciones"),
                        objetoJson.getString("rol"))
                );


            }
            catch (JSONException e){
                Toast.makeText(this,"Error al cargar los datos al objeto: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        return listUsuario;
    }


    //Procedimiento para validar que el usuario y la contraseña sean correctas
    private boolean validar(List<Usuario> listausuario){
        String auxNombreUsuario= username.getText().toString();
        String auxContrasena= password.getText().toString();
        pos=0;

        try {
            for(Usuario usu : listausuario) {
                if ((auxNombreUsuario.equals(usu.getNomUsuario().toString()))&&(auxContrasena.equals(usu.getContrasena().toString()))) {
                    return true;
                }
                pos++;
            }

        }
        catch (Exception e){
            Toast.makeText(this,"Error al validar el usuario: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return  false;
    }
}