package com.eleinco.ezvoxptt.common;
//clase de enumeradores
public class Enums {
	//indiica el estado de la conexión con el servidor
	public enum EstadoTiempoReal
	{
		ConectadoSinRegistrar,
		ConectadoRegistrado,
		Desconectado,
		Rechazado
	}
	//tipos de comandos
	public enum TipoComando
	{
		L, //login
		G, //cambiar grupo
		E, //estado de un equipo
		P, //pTT
		T, //radio está hablando
		H //hold, el radio cuelga
	}
}
