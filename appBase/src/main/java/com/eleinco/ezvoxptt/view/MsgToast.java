package com.eleinco.ezvoxptt.view;

import android.content.Context;
import android.widget.Toast;
//clase que permite mostrar un mensaje tipo Toast
public class MsgToast {
	//Muestra un mensaje toast
	public static void Show(String mensaje, Context contexto)
	{
		Toast.makeText(contexto, mensaje, 3000);
	}
}
