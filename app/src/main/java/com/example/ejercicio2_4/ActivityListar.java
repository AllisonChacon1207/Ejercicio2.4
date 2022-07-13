package com.example.ejercicio2_4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ejercicio2_4.config.SQLiteConexion;
import com.example.ejercicio2_4.config.Transacciones;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ActivityListar extends AppCompatActivity {

    SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
    Button btnAtras;
    ArrayList<Signaturess> listaFirmas = new ArrayList<Signaturess>();
    ImageView imageView;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        SQLiteDatabase db = conexion.getWritableDatabase();
        String sql = "SELECT * FROM Signature";
        Cursor cursor = db.rawQuery(sql, new String[] {});

        while (cursor.moveToNext()){
            listaFirmas.add(new Signaturess(cursor.getString(0) , cursor.getBlob(1)));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        AdaptadorFirmas adaptador = new AdaptadorFirmas(this);
       list = findViewById(R.id.lista);
       list.setAdapter(adaptador);

       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               obtenerFoto(i);
           }
       });

       btnAtras = (Button) findViewById(R.id.btnAtras);
       btnAtras.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(),MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
               finish();
           }
       });
    }

    private void obtenerFoto( int id) {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Signaturess lista_Firmas = null;
        listaFirmas = new ArrayList<Signaturess>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.TablaSignature,null);

        while (cursor.moveToNext())
        {
            lista_Firmas = new Signaturess();
            lista_Firmas.setDescripcion(cursor.getString(0));
            listaFirmas.add(lista_Firmas);
        }
        cursor.close();
        Signaturess signature = listaFirmas.get(id);
    }


    class AdaptadorFirmas extends ArrayAdapter<Signaturess> {

        AppCompatActivity appCompatActivity;

        AdaptadorFirmas(AppCompatActivity context) {
            super(context, R.layout.single_row, listaFirmas);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.single_row, null);

            imageView = item.findViewById(R.id.imgFirma);

            SQLiteDatabase db = conexion.getWritableDatabase();

            String sql = "SELECT * FROM Signature";

            Cursor cursor = db.rawQuery(sql, new String[] {});
            Bitmap bitmap = null;
            TextView textView1 = item.findViewById(R.id.descripcionImagen);

            if (cursor.moveToNext()){
                textView1.setText(listaFirmas.get(position).getDescripcion());
                byte[] blob = listaFirmas.get(position).getImage();
                ByteArrayInputStream bais = new ByteArrayInputStream(blob);
                bitmap = BitmapFactory.decodeStream(bais);
                imageView.setImageBitmap(bitmap);
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();

            return(item);
        }
    }
}