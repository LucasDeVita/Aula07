package br.usjt.aula07;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import br.usjt.aula07.BD.PaisesDB;

import java.io.IOException;


/**
 * Created by Lucas De Vita on 24/04/18.
 * RA: 81617007
        */

public class MainActivity extends Activity {
    Spinner spinnerContinente;
    String continente = "all";
    public static final String URL = "https://restcountries.eu/rest/v2/";
    public static final String PAISES = "br.alexsander_souza.paises";
    Pais[] paises;
    Intent intent;
    ProgressBar timer;
    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerContinente = (Spinner) findViewById(R.id.spinnerContinente);
        spinnerContinente.setOnItemSelectedListener(new RegiaoSelecionada());
        timer = (ProgressBar)findViewById(R.id.timer);
        timer.setVisibility(View.INVISIBLE);
        contexto = this;
    }

    public void listarPaises(View view) {
        intent = new Intent(this, ListarPaisesActivity.class);
        if(Paises.isConnected(this)) {
            timer.setVisibility(View.VISIBLE);
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                paises = Paises.buscarPaises(URL, continente);
                                //insere no banco o que conseguiu
                                PaisesDB db = new PaisesDB(contexto);
                                db.inserePaises(paises);
                                runOnUiThread(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      intent.putExtra(PAISES, paises);
                                                      startActivity(intent);
                                                      timer.setVisibility(View.INVISIBLE);
                                                  }
                                              }
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
        } else {
            Toast.makeText(this, "Rede inativa. Usando armazenamento local.",
                    Toast.LENGTH_SHORT).show();
            new CarregaPaisesDoBanco().execute("pais");
        }
    }

    private class RegiaoSelecionada implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            continente = (String) parent.getItemAtPosition(position);
            if (continente.equals("Todos")) {
                continente = "all";
            } else {
                continente = "region/"+continente.toLowerCase();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class CarregaPaisesDoBanco extends AsyncTask<String, Void, Pais[]> {

        @Override
        protected Pais[] doInBackground(String... params) {
            PaisesDB db = new PaisesDB(contexto);
            Pais[] paises = db.selecionaPaises();
            return paises;
        }

        public void onPostExecute(Pais[] paises){
            intent.putExtra(PAISES, paises);
            startActivity(intent);
        }
    }
}
