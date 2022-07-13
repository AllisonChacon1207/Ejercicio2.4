package com.example.ejercicio2_4;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ejercicio2_4.config.SQLiteConexion;
import com.example.ejercicio2_4.config.Transacciones;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private CaptureBitmapView captureBitmapView;
    EditText Descripcion;
    Bitmap imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Descripcion = ( EditText) findViewById(R.id.editTextDescripcion);
        Button salvar = (Button) findViewById(R.id.btnSalvar);
        Button btnListarFirmas = (Button) findViewById(R.id.btnMostrarFirmas);

        LinearLayout mContent = (LinearLayout) findViewById(R.id.signLayout);
        captureBitmapView = new CaptureBitmapView(this, null);
        mContent.addView(captureBitmapView, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { guardarDatos();}
        });


        btnListarFirmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.ejercicio2_4.ActivityListar.class);
                startActivity(intent);

            }
        });
    }


   private void salvar() {

        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        Bitmap signature = captureBitmapView.getBitmap();
        ContentValues valores = new ContentValues();
        valores.put(Transacciones.Descripcion, Descripcion.getText().toString());
        valores.put(Transacciones.ImgFirma, signature.toString());

        if (Descripcion.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Favor no dejar campos vacios", Toast.LENGTH_LONG).show();
        } else {

            Long Registro = db.insert(Transacciones.TablaSignature, Transacciones.Descripcion, valores);
            Toast.makeText(getApplicationContext(), "Registro INGRESADO : Codigo :" + Registro.toString(), Toast.LENGTH_LONG).show();

            db.close();
        }
        LimpiarPantalla();
    }
    private void guardarDatos() {
        try {
            firmas(captureBitmapView.getBitmap());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            MediaStore.Images.Media.insertImage(getContentResolver(), imagen, imageFileName , "yourDescription");

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            LimpiarPantalla();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error a guardar Datos ",Toast.LENGTH_LONG).show();
        }


    }

    private void firmas( Bitmap bitmap) {

        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ArrayFoto  = stream.toByteArray();

        ContentValues valores = new ContentValues();

        valores.put(Transacciones.Descripcion,Descripcion.getText().toString());
        valores.put(String.valueOf(Transacciones.ImgFirma),ArrayFoto);

        Long resultado = db.insert(Transacciones.TablaSignature, null, valores);

        Toast.makeText(getApplicationContext(), "Registro ingreso con exito: " + resultado.toString()
                ,Toast.LENGTH_LONG).show();

        db.close();
    }



    private void LimpiarPantalla()
    {
    Descripcion.setText("");
    captureBitmapView.ClearCanvas();

    }

}
