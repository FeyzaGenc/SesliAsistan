package com.example.sesliasistan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Ilaclar extends AppCompatActivity {
    SQLiteDatabase db=null;
    TextView txtTarih;
    Button btn;
    TableLayout table_layout;
    DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilaclar);
        setTitle("İlaç listesi");
        db=openOrCreateDatabase("dtAsistan",MODE_PRIVATE,null);
        String tabloAyar="Create table if not exists ilaclar(ilac_adi text,miktar text,birim text,tarih text,saat text)";
        db.execSQL(tabloAyar);
        db.close();

        btn=findViewById(R.id.btnEkle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ekle=new Intent(Ilaclar.this,IlacEkle.class);
                startActivity(ekle);
                finish();
            }
        });
        table_layout = findViewById(R.id.tablo);
        final Calendar calendar=Calendar.getInstance();
        int yil=calendar.get(Calendar.YEAR);
        int ay=calendar.get(Calendar.MONTH);
        int gun=calendar.get(Calendar.DAY_OF_MONTH);
        txtTarih=findViewById(R.id.txtTarih);


        int ay2=ay+1;
        String tarih=gun+"."+ay2+"."+yil;
        txtTarih.setText(tarih);
        txtTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        Ilaclar.this,onDateSetListener,yil,ay,gun);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();


            }
        });
        onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yil, int ay, int gun) {
                ay=ay+1;
                String tarih=gun+"."+ay+"."+yil;
                txtTarih.setText(tarih);
                table_layout.removeAllViews();
                listele();

            }
        };
        listele();
    }

    @SuppressLint("Range")
    private  void listele(){

        db=openOrCreateDatabase("dtAsistan",MODE_PRIVATE,null);
        String listele="SELECT ilac_adi ,miktar ,birim ,saat FROM ilaclar where tarih='"+txtTarih.getText().toString()+"'";
        Cursor c=db.rawQuery(listele,null);
        TableRow row1 = new TableRow(this);
        for (int k = 1; k <= 3; k++) {
            TextView tv1 = new TextView(this);
            //tv1.setTextSize(Integer.parseInt(punto));
            //tv.setTextColor(Color.WHITE);
            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            if(k==1){
                tv1.setText("ILACINIZ ");}
            if(k==2){
                tv1.setText(" MIKTARI ");
                tv1.setPadding(50, 50, 50, 50);}
            if(k==3){
                tv1.setText(" SAATI ");}


            row1.addView(tv1);

        }
        table_layout.addView(row1);
        while (c.moveToNext()) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            for (int j = 1; j <= 4; j++) {

                TextView tv = new TextView(this);

                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                //tv.setBackgroundColor(Color.DKGRAY);
                tv.setPadding(5, 5, 5, 5);
                if (j == 1) {
                    tv.setText(c.getString(c.getColumnIndex("ilac_adi")));
                }
                if (j == 2) {
                    tv.setText(c.getString(c.getColumnIndex("miktar"))+" "+c.getString(c.getColumnIndex("birim")));
                    tv.setPadding(50, 50, 50, 50);
                }

                if (j == 3) {
                    tv.setText(c.getString(c.getColumnIndex("saat")));
                }


                row.addView(tv);

            }
            table_layout.addView(row);

        }
        c.close();
        db.close();
    }
}