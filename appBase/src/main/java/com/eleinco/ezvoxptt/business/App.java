package com.eleinco.ezvoxptt.business;

import com.eleinco.ezvoxptt.common.Logg;
import com.eleinco.ezvoxptt.entities.Equipo;
//contiene variables y funcionalidad transversal a la aplicacion
public class App {
	//almacena el id del equipo en bytes
	private static byte[] MiIDBytes = null;
	//private static String miID = null;
	//public static String MiID = "0001";  //PTT
	//public static String MiID = "0002";  //EDWIN
	//public static String MiID = "0003";  //DON JESUS 2
	//public static String MiID = "0004";    //DON JESUS
	//public static String MiID = "0005";    //DON JESUS
	//private static String miID = "0006";    //TABLET
	
	/*++++++++++++++APLICACION WFORMS+++++++++++++++++++++*/
	//public static final int PORT_RECEIVE = 3333;
	//private static String FILE_NAME_ID = "myid.dat";
	public static boolean Loggeado = false;
	/*++++++++++++++APLICACION WFORMS+++++++++++++++++++++*/
	//public static String URL="http://192.168.1.103:8081/ws.asmx";
	//public static String URL="http://50.63.67.95:8181/ws.asmx";

	public static String URL="http://zvoxpttweb.azurewebsites.net/ws.asmx";//almacena la url del servicio web
	// public static String URL="http://zvoxptt.azurewebsites.net/ws.asmx";//almacena la url del servicio web antes
	//public static String URL="http://10.0.2.2:53261/ws.asmx";
	public static String NAMESPACE = "http://10.0.2.2/";//namespace del servicio web
	public static String SOAP_ACTION = "http://10.0.2.2/"; //soap action del webservice
	public static Equipo Me = null; //obtiene la isnstancia del equipo actual
	private static String TAG = "App"; 
	public static long SumBytesSend = 0; //suma de bytes enviados
	public static long SumBytesRec = 0; //suma de bytes recibidos
	public static int SegundosUltimoContactoServidor = 10;

	public static com.eleinco.ezvoxptt.entities.GrupoEquipo GrupoActual = null;	//contiene la instancia del grupo actual
	//obtiene el id del equipo en bytes
	public static byte[] getIDBytes() throws Exception
	{
		if(MiIDBytes != null)
			return MiIDBytes;
			
		String id = "";
		if (Me == null)
		{
			id = GetID();
		}
		else
		{
			id = Me.IDEquipo;
		}
		
		MiIDBytes = id.getBytes();
		return MiIDBytes;
	}
	//calida que el id exista en el servidor
	public static boolean ValidateID(String equipoID) throws Exception
	{
		Me = Equipos.Consultar(equipoID);
		if(Me == null)
			return false;
		return true;
	}
	//obtiene el id del equiop
	public static String GetID() 
	{
		if(Me == null)
		{
			Logg.e(TAG, "No se ha encontrado el identificador");
			return "";
		}
		else
		{
			return Me.IDEquipo;
		}
	}
	/*public static void SaveID(String id, Context contexto) throws IOException, Exception
	{
		if(Archivo.EscribirArchivo(FILE_NAME_ID, id.toUpperCase(), contexto))
		{
			Me = null;
		}
	}*/
	/*public static String ReadID(Context contexto) throws Exception
	{
		return Archivo.LeerArchivo(FILE_NAME_ID, contexto);		
	}*/
}
