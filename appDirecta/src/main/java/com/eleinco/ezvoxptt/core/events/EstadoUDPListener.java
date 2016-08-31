package com.eleinco.ezvoxptt.core.events;
//interfaz para el evento del estado de la conexion udp
public interface EstadoUDPListener {
	public void EstadoCambiado(boolean conectado);
}
