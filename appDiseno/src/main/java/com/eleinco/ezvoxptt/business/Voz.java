package com.eleinco.ezvoxptt.business;

import android.content.Context;
import android.media.AudioManager;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;
import android.util.Log;

import com.eleinco.ezvoxptt.common.Settings;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

//--->Fabian//import android.media.AudioFormat;
//--->Fabian//import android.media.AudioManager;
//--->Fabian//import android.media.AudioRecord;
//--->Fabian//import android.media.AudioTrack;
//--->Fabian//import android.media.MediaRecorder;

//permite el proceso y control del audio
public class Voz {
	private static String TAG = "Voz";

	//declaramos las variables para luego preparar el objeto AudioStream
	private static AudioStream audioStream;
	private static AudioGroup audioGroup;
	//private static String destino="50.63.67.95"; //los quemé acá para probar
	//private static String destino="186.86.155.34"; //los quemé acá para probar
	//private static int puerto=3000;

	//--->Fabian//private static int MilisegundosSinRecibirAudio = 0;
	// the minimum buffer size needed for audio recording
	//--->Fabian//private static int BUFFER_SIZE_IN;
	//--->Fabian//private static int BUFFER_SIZE_OUT;
	// the audio recording options
	//private static final int RECORDING_RATE = 44100;
	//--->Fabian//private static final int RECORDING_RATE = 8000;
	//private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
	//--->Fabian//private static final int CHANNEL = AudioFormat.CHANNEL_CONFIGURATION_DEFAULT;
	//--->Fabian//private static final int FORMAT = AudioFormat.ENCODING_PCM_16BIT;
	//private static final int FORMAT = AudioFormat.ENCODING_PCM_8BIT;
	// the audio recorder
	//--->Fabian//private static AudioRecord recorder;
	// the Audio tracker
	//--->Fabian//private static AudioTrack audioTrack;

	public static boolean Running = false;
	public static boolean IsPTT = false;
	public static int PttPermitido = 0; //0: Pendiente, -1: No, 1: Si
	public static com.eleinco.ezvoxptt.view.ActPrincipal actPrincipal;

	//crea las variables y configuraciones necesarias para grabar y reproducir

	/**
	 * Ésta función preparará el objeto AudioStream, creando una instancia que será usada para establecer los parametros por defecto del protocolo RTP (ip y puerto local, uso de audioGroup para controlar el mic y el reproductor).
	 * @param contexto contexto que permitirá instanciar el objeto.
	 * @return
	 */

	public static boolean PrepararAudio(Context contexto)
	{
		try{

			audioGroup=new AudioGroup();
			audioGroup.setMode(AudioGroup.MODE_NORMAL);
			audioStream = new AudioStream(InetAddress.getByName(String.valueOf(getLocalIPAddress()))); //establecer permisos en manifest para poder tener acceso a ip y audiostream
			audioStream.setCodec(AudioCodec.GSM);

		} catch (Exception e) {
			e.printStackTrace();
		}

		/*/--->Fabian//Logg.d(TAG, "Creando el AudioRecord con AudioSource " + MediaRecorder.AudioSource.DEFAULT);
		BUFFER_SIZE_IN = AudioRecord.getMinBufferSize(
		        RECORDING_RATE, CHANNEL, FORMAT);
		BUFFER_SIZE_OUT = AudioTrack.getMinBufferSize(
				RECORDING_RATE, AudioFormat.CHANNEL_OUT_MONO, FORMAT);
				
         recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                 RECORDING_RATE, CHANNEL, FORMAT, BUFFER_SIZE_IN * 10);
         
         Log.d(TAG, "Creando el AudioTrack");
         audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 
         		RECORDING_RATE, AudioFormat.CHANNEL_OUT_MONO, FORMAT, 
         		BUFFER_SIZE_OUT * 4, AudioTrack.MODE_STREAM);
         
         StartStreaming(contexto);         /*/
		 StartStreaming(contexto);
         return true;
	}
	//recibe y procesa los datos de audio recibidos desde el servidor
	public static void DatosVozRecibidos(byte[] datos, int length)
	{

		if (false)
		{
			/*/--->Fabian//MilisegundosSinRecibirAudio = 0;
			if (audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
	        {
				audioTrack.play();					
	        }
			audioTrack.write(datos, 0, length);/*/
			return;
		}

		//Los primeros 4 bytes son el total de bytes, se ignoran
		//El byte 5 es '@', se ignora

		//Preguntar por la posicion 5, byte 6, Tipo de trama
		if (datos[5] == 0x4F) // O
		{
			//Login OK
			com.eleinco.ezvoxptt.business.App.Loggeado = true;
		}
		else if (datos[5] == 0x47) // G
		{
			//Grupos cambiados correctamente
		}
		else if (datos[5] == 0x59) // Y
		{
			//Puede hablar
			PttPermitido = 1;
		}
		else if(datos[5] == 0x4E) //N
		{
			PttPermitido = -1;
		}
		else if(datos[5] == 0x56) //V
		{
			try
			{
				//XXXX@V000000DATOSVOZ (0000:ID)
				String idHablando = new String(datos, 6, 6);

				if(actPrincipal != null)
					actPrincipal.HablandoRadio(idHablando, true);

				/*/--->Fabian//MilisegundosSinRecibirAudio = 0;
				if (audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
		        {
					audioTrack.play();					
		        }
				
		        //Añadir los datos al AudioTrack para ser reproducidos
				audioTrack.write(datos, 12, length - 8);/*/
				audioStream.join(null);
				audioGroup.setMode(AudioGroup.MODE_MUTED);
				audioStream.setMode(RtpStream.MODE_RECEIVE_ONLY);
				audioStream.join(audioGroup);


			}
			catch(Exception ex)
			{
				Log.e("TAGGGGGGGGG", ex.toString());
			}
		}
	}

	public static void Asociar(int puerto)
	{
		audioStream.join(null);
		//Se debe enviar el encabezado de stream para que funcione, así cambiara el modo del audiogroup de los demas a rx
		try {
			audioStream.associate(InetAddress.getByName(Settings.SERVER), puerto);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		audioGroup.setMode(AudioGroup.MODE_MUTED);
		audioStream.setMode(RtpStream.MODE_RECEIVE_ONLY);
		audioStream.join(audioGroup);

		Log.d(TAG, "Escuchando...");
		//IsPTT = true;
	}

	//inicia la grabación

	/**
	 *Inicia la asociación al servidor y establece el control del audiogroup para transmitir lo que se escuche en el mic por el Streaming RTP establecido.
	 */
	public static void Grabar() {
		audioStream.join(null);
		//Se debe enviar el encabezado de stream para que funcione, así cambiara el modo del audiogroup de los demas a rx
		//try {
		//	audioStream.associate(InetAddress.getByName(destino), puerto);
		//} catch (UnknownHostException e) {
		//	e.printStackTrace();
		//}
		audioGroup.setMode(AudioGroup.MODE_ECHO_SUPPRESSION);
		audioStream.setMode(RtpStream.MODE_SEND_ONLY);
		audioStream.join(audioGroup);

        //Fabian//recorder.startRecording();
        Log.d(TAG, "Grabador iniciado");
		Log.d(TAG, "Streaming RTP iniciado");
        //IsPTT = true;
	}

	/**
	 *Detiene el control del audiogroup para anular lo que se escuche en el mic (el Streaming RTP aun seguirá activo en modo recepción).
	 */
	public static void Parar()
	{
        //IsPTT = true;
		audioStream.join(null); //anula la asociación con el audiogroup
		audioStream.setMode(RtpStream.MODE_RECEIVE_ONLY);
		audioGroup.setMode(AudioGroup.MODE_MUTED);//deshabilito el mic
		//audioStream.setMode(RtpStream.MODE_RECEIVE_ONLY); //lo pongo en modo recepción
		audioStream.join(audioGroup); //de nuevo inicio el streaming sobre el objeto ya creado.
        //if(recorder != null)
        //	recorder.stop();
        Log.d(TAG, "Grabador detenido");
		Log.d(TAG, "Streaming RTP Detenido");
	}

	//inicia un hilo que permite reproducir el adio recibido
	private static void StartStreaming(final Context contexto)
	{

	}

	/**
	 * Finaliza el Streaming RTP, se usa normalmente al finalizar la aplicación para liberar la instancia del objeto AudioStream.
	 */
    public static void Finalizar() {
        Log.i(TAG, "Stopping the audio stream");
        Running = false;
		audioStream.join(null);
		audioGroup=null;
		audioStream.release();
		audioStream = null;
        /*if(recorder != null)
        {
        	recorder.stop();
        	recorder.release();
            recorder = null;
        }
        if(audioTrack != null) 
        {
        	audioTrack.stop();
        	audioTrack.release();
            audioTrack = null;
        }*/
    }

	/**
	 * Obtiene la IP local del equipo
	 * @return IP
	 */

	public static String getLocalIPAddress () {
		String ip=null;
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					System.out.println("ip1--:"+inetAddress);
					System.out.println("ip2--:"+inetAddress.getHostAddress());

					//para obtener IPV4
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						//ip= inetAddress.getAddress();
						ip= inetAddress.getHostAddress();
						System.out.println("ip--:"+ip.toString());
					}
				}
			}
		} catch (SocketException ex) {
			Log.i("SocketException ", ex.toString());
		}
		return ip;
	}
}
