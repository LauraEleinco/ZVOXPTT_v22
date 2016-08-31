package com.diex.communityhelp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ADMID on 12/05/2016.
 */
public class Id  extends AppCompatActivity {
    EditText claveAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id);
        claveAdmin=(EditText)findViewById(R.id.ingresoAdmin);
    }

    public void show(View v) {
        final MaterialDialog materialDialog = new MaterialDialog(this);

        materialDialog.setTitle("Administración")
                .setContentView( R.layout.ingreso_admin)
                .setMessage(
                        "Clave de Adminstrador")
                .setPositiveButton("INGRESA", new View.OnClickListener() {
                    @Override public void onClick(View v) {




                       // String clave=  claveAdmin.getText().toString();
                        if(claveAdmin.getText().equals("a")){
                            Intent i = new Intent(Id.this, MainActivity.class);
                            startActivity(i);
                        }



                        materialDialog.dismiss();


                    }
                })
                .setNegativeButton("CANCELAR",
                        new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                materialDialog.dismiss();
                                Toast.makeText(Id.this,
                                        "Cancel", Toast.LENGTH_LONG)
                                        .show();
                            }
                        })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Toast.makeText(Id.this,
                                        "onDismiss",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                .show();


    }

}
