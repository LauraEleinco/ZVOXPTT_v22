package com.eleinco.ezvoxptt.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
//clase que permite mostrar un dialogo de pregunta (si o no)
public class Confirm {
	//muestra un cuandro de pregunta 
	public static void Show(String msg, Context contexto, DialogInterface.OnClickListener listener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
		builder.setMessage(msg).setPositiveButton("SI", listener)
		    .setNegativeButton("NO", listener).show();
	}
	//muestra un cuadro de pregunta indicando el texto de los dos botones
	public static void Show(String msg, String txtPositivo, String txtNegativo, Context contexto, DialogInterface.OnClickListener listener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
		builder.setMessage(msg).setPositiveButton(txtPositivo, listener)
		    .setNegativeButton(txtNegativo, listener).show();
	}
}
