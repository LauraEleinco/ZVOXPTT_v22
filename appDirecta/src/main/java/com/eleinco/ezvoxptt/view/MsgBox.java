package com.eleinco.ezvoxptt.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

//clase que permite mostrar un mensaje
public class MsgBox {
	private static String title = "ZVox PTT";
	//muestra un mensaje
	public static void Show(String msg, Context context)
	{
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
		{
		   public void onClick(DialogInterface dialog, int which) {
			   alertDialog.cancel();
		   }
		});
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();
	}
	//muestra un mensaje especificando el titulo
	public static void Show(String titulo, String msg, Context context)
	{
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(titulo);
		alertDialog.setMessage(msg);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
		{
		   public void onClick(DialogInterface dialog, int which) {
			   alertDialog.cancel();
		   }
		});
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();
	}
	//muestra un mensaje especificando el listener para cuando se acepte el dialogo
	public static void Show(String msg, Context context, DialogInterface.OnClickListener listener)
	{
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setButton("OK", listener);
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}
}
