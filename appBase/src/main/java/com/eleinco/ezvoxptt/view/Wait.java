package com.eleinco.ezvoxptt.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
//clase que permite mostrar un dialogo de espera o progreso
public class Wait {
	private static ProgressDialog progressDialog = null;
	//muestra un dialogo de espera con un mensaje
	public static void Show(String mensaje, Activity act){
		if(progressDialog != null)
			Close();
		progressDialog = ProgressDialog.show(act,
			"Por favor espere...",
			mensaje,
			true, false);
		progressDialog.show();
	}
	//muestra un dialogo de espera especificando el ecento cuando este sea cancelado
	public static void Show(String mensaje, Activity act, OnCancelListener oncancel){
		if(progressDialog != null)
			Close();
		progressDialog = ProgressDialog.show(act,
			"Por favor espere...",
			mensaje,
			true, true, oncancel);
		progressDialog.show();
	}
	//cierra el dialogo de espera activo
	public static void Close(){
		if(progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	//cambia el mensaje edl dialogo de espera activo
	public static void ChangeMessage(String message)
	{
		if(progressDialog != null &&
				progressDialog.isShowing())
			progressDialog.setMessage(message);		
	}
}
