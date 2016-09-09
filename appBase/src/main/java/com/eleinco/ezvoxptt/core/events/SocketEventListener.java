package com.eleinco.ezvoxptt.core.events;
//interfaz para el evento de escucha del socket
public interface SocketEventListener {
	public void DatosRecibidos(String datos);
	public void EstadoCambiado(boolean conectado);
}
