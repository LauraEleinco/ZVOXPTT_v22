package com.eleinco.ezvoxptt.core.events;
//interfaz para el evento del servicio android
public interface ServicioEventListener {	
	public void NoTieneGrupos();
	public void ErrorDeRed();
	public void ConectadoYRegistrado();
	public void Desconectado();
	public void SinRegistrarID();
	public void IDNoValido();
	public void EquipoHablando(String grupoId, String equipoId, boolean hablando);
}
