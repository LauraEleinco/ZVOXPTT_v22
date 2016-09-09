package com.eleinco.ezvoxptt.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
//cclase que permite escribir y leer archivos encriptados
public class Archivo {
	//Escribe un archivo cifrado
	private static boolean EscribirArchivo(String nombre, String datos, Context contexto) throws IOException, Exception
	{
		File mydir = CrearDirectorio(contexto);
        File fileWithinMyDir = new File(mydir, nombre); //Getting a file within the dir.
       
		FileOutputStream out = new FileOutputStream(fileWithinMyDir);
		out.write(Crypt.encrypt(datos).getBytes());		
		out.close();
		return true;
	}
	//Lee un archivo cifrado
	private static String LeerArchivo(String nombre, Context contexto) throws Exception
	{
		File mydir = CrearDirectorio(contexto);
		
		File fileWithinMyDir = new File(mydir, nombre); //Getting a file within the dir.
		
		StringBuilder text = new StringBuilder();		
	    BufferedReader br = new BufferedReader(new FileReader(fileWithinMyDir));
	    String line;

	    while ((line = br.readLine()) != null)
	    {
	        text.append(line);
	    }
	    br.close();
	    String datos = Crypt.decrypt(text.toString());

		return datos;
	}
	//Crea un directorio
	private static File CrearDirectorio(Context contexto)
	{
		File mydir = contexto.getDir("mvoxptt", Context.MODE_PRIVATE); //Creating an internal dir;
		return mydir;
	}
}
