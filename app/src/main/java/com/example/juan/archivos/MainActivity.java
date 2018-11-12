package com.example.juan.archivos;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button escribirIn, escribirEx, leerIn, leerEx;
    TextView tv;
    EditText et;
    boolean sdDisponible = false;
    boolean sdAccesoescritura = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        escribirEx = (Button) findViewById(R.id.EscribirExterno);
        escribirIn = (Button) findViewById(R.id.EscribirInterno);
        leerEx = (Button) findViewById(R.id.LeerExterno);
        leerIn = (Button) findViewById(R.id.LeerInterna);
        tv = (TextView) findViewById(R.id.tv);
        et = (EditText) findViewById(R.id.et);

        escribirIn.setOnClickListener(this);
        escribirEx.setOnClickListener(this);
        leerIn.setOnClickListener(this);
        leerEx.setOnClickListener(this);

        //Environment da infornaciòn sobre cosas relativas al dispositivo
        String estado = Environment.getExternalStorageState();
        if (estado.equals(Environment.MEDIA_MOUNTED)){
            //La sd esta montada y se puede escribir sobre ella
            sdDisponible = true;
            sdAccesoescritura = true;
        }
        else if(estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            //La sd esta montada pero el acceso a escritura no esta disponible
            sdDisponible = true;
            sdAccesoescritura = false;
        }
        else{
            //No esta montada
            sdDisponible = false;
            sdAccesoescritura = false;
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.LeerInterna:
                try {
                    BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("MemoriaInterna.txt")));

                    String texto = fin.readLine();
                    tv.setText(texto);
                    fin.close();

                } catch (Exception e) {

                    Toast.makeText(getBaseContext(),"Error al leer el fichero",Toast.LENGTH_SHORT).show();
                }

            case R.id.LeerExterno:
                if(sdDisponible){
                    try {
                        File ruta_sd = Environment.getExternalStorageDirectory();
                        File f = new File(ruta_sd.getAbsolutePath(),"FicheroSd.txt");

                        BufferedReader fin = new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(f)))    ;

                        String texto = fin.readLine();
                        tv.setText(texto);
                        fin.close();

                    } catch (Exception e) {

                        Toast.makeText(getBaseContext(),"Error al leer el fichero en la SD",Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.EscribirInterno:
                try {
                    OutputStreamWriter fout = new OutputStreamWriter(openFileOutput(
                            "MemoriaInterna.txt", Context.MODE_PRIVATE));

                    fout.write(et.getText().toString());
                    fout.close();
                    Toast.makeText(getBaseContext(),"Se escribio correctamente",Toast.LENGTH_SHORT).show();
                    et.setText("");
                } catch (Exception e){
                    Toast.makeText(getBaseContext(),"Error al escribir en el fichero",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.EscribirExterno:
                if(sdAccesoescritura && sdDisponible){
                    try {
                        File ruta_sd = Environment.getExternalStorageDirectory();
                        File f = new File(ruta_sd.getAbsolutePath(),"FicheroSd.txt");

                        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));

                        fout.write(et.getText().toString());
                        fout.close();
                        Toast.makeText(getBaseContext(),"Se escribio correctamente",Toast.LENGTH_SHORT).show();
                        et.setText("");
                    } catch (Exception e){
                        Toast.makeText(getBaseContext(),"Error al escribir en el fichero en la SD",Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }

    }
}
