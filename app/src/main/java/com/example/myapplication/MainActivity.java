package com.example.myapplication;

import static android.text.TextUtils.isEmpty;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    // Setup Server information
    protected static String server = "192.168.42.142";
    protected static int port = 5000;
    private Socket client;
    private PrintWriter printwriter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Capturamos el boton de Enviar
        View button = findViewById(R.id.button_send);

        // Llama al listener del boton Enviar
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    // Creación de un cuadro de dialogo para confirmar pedido
    private void showDialog() throws Resources.NotFoundException {
        CheckBox sabanas = (CheckBox) findViewById(R.id.checkBox_sabanas);
        EditText text = (EditText)findViewById(R.id.NumberInput);
        final String value = text.getText().toString();

        if (!sabanas.isChecked() || isEmpty(value)) {
            // Mostramos un mensaje emergente;
            Toast.makeText(getApplicationContext(), "Selecciona al menos un elemento y una cantidad", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Enviar")
                    .setMessage("Se va a proceder al envio de ")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                // Catch ok button and send information
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    // 1. Extraer los datos de la vista

                                    // 2. Firmar los datos

                                    // 3. Enviar los datos

                                    //String mensaje = "Petición enviada correctamente. " + "Numero - " + value;
                                    String mensaje = "1-2-3";
                                    new Thread(new ClientThread(mensaje)).start();
                                    //Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                                }
                            }

                    )
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
    }

    class ClientThread implements Runnable {
        private final String message;

        ClientThread(String message) {
            this.message = message;
        }
        @Override
        public void run() {
            Log.e("INFO","Corre Hilo");
            try {
                // the IP and port should be correct to have a connection established
                // Creates a stream socket and connects it to the specified port number on the named host.
                Log.e("INFO","INTENTAMOS");
                client = new Socket(server, port); // connect to server
                Log.e("INFO","Conectamos");
                printwriter = new PrintWriter(client.getOutputStream(),true);
                Log.e("INFO","Mandamos Mensaje");
                printwriter.write(message); // write the message to output stream
                Log.e("INFO","Mensaje Mandado");
                printwriter.flush();
                printwriter.close();

                // closing the connection
                client.close();
                Log.e("Error","Conexión Establecida");
            } catch (IOException e) {
                Log.e("Error","Error en la conexión");
                e.printStackTrace();
            }

            // updating the UI
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("Error","Esto Que");
                }
            });
        }
    }

}
