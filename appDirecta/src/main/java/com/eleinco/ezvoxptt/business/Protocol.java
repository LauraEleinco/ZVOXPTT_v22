package com.eleinco.ezvoxptt.business;
//obtiene la logica de negocio para el protocolo de comunicacion con el servidor
public class Protocol {
	//obtiene el encabezado de la trama
	private static String getHeader(TipoProtocolo t)
	{
		return "@" + t.toString();
	}
	//obtiene la trama de logueo
	public static byte[] Login(String idEquipo)
	{
		//@I000000
		return (getHeader(TipoProtocolo.I) + idEquipo).getBytes();		
	}
	//retorna la trama para solicitar autorizacion para ptt
	public static byte[] SolicitarPTT(String id)
	{
		//@P000000
		return (getHeader(TipoProtocolo.P) + id).getBytes();		
	}
	//obtiene los tipos de protocolo existentes
	private enum TipoProtocolo
	{
		I, //Login o información de IP
		P, //Preguntar si se puede hablar
		G //Cambio de grupo
	}
}
