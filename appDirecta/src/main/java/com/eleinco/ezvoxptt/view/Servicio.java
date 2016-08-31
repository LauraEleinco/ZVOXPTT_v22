package com.eleinco.ezvoxptt.view;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.eleinco.ezvoxptt.business.App;
import com.eleinco.ezvoxptt.business.Login;

import java.io.IOException;
//clase que extiende de service, permite crear un servicio android
public class Servicio extends  Service{
	boolean runn = true;
	boolean yacorriendo = false;
	ActPrincipal principal = null;
	String TAG = "Servicio";
	Thread hiloConfirm = null;
	@Override
    public void onCreate() {
		//Receiver r  = new Receiver();           
     	//registerReceiver(r, new IntentFilter(Intent.ACTION_MEDIA_BUTTON));           
    }
	@Override
    public int onStartCommand(Intent intenc, int flags, final int idArranque) 
	{		 
		//al dar inicio al servicio
		
  		if(principal != null)
  			principal.ActualizarGrupoActual();
 		if (yacorriendo)
    	{
 			return START_STICKY;
    	}
		Log.d(TAG, "Iniciando hilo de confirmación...");
 		hiloConfirm = new Thread(new Runnable() {
			@Override
			public void run() {
				while(runn)
				{
					try
					{
						Thread.sleep(1000);
						App.SegundosUltimoContactoServidor++;
	
						if (App.SegundosUltimoContactoServidor == 30 || Login.LoginAhora)
						{
							Login.LoginAhora = false;
							Log.d(TAG, "30 segundos sin recibir o login forzado... comienza login automático...");
							Login.IniciarLogin();
							Thread.sleep(15000);
							App.SegundosUltimoContactoServidor = 0;
						}
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
			}			
			
		});
 		hiloConfirm.start();
 		
    	yacorriendo = true;    	
    	com.eleinco.ezvoxptt.business.Voz.PrepararAudio(this); //inicia la preparación del audio
    	return START_STICKY;
    }
    @Override
    public void onDestroy() {
     runn = false;
     yacorriendo = false;
        Toast.makeText(this,"Servicio detenido", Toast.LENGTH_SHORT).show();
          //reproductor.stop();
        try {
			com.eleinco.ezvoxptt.business.Server.Stop();
			com.eleinco.ezvoxptt.business.Voz.Finalizar();
        } 
        catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public IBinder onBind(Intent intencion) {
          return null;
    }
}
