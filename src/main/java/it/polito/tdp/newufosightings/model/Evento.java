package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;


public class Evento implements Comparable<Evento> {//ATTENZIONE: Comparable compara l'oggetto sè stesso ad un altro, Comparator compara 2 oggetti tra loro
	
	private LocalDateTime tempo;
	private TipoEvento tipo;
	private State stato;
	
	public enum TipoEvento {
		INIZIO_AVVISTAMENTO,
		FINE_AVVISTAMENTO,
		FINE_EMERGENZA
	}
	
	
	
	public Evento(State stato, LocalDateTime lt, TipoEvento tipo) {
		this.tempo=lt;
		this.tipo=tipo;
		this.stato=stato;
	}

	





	public State getStato() {
		return stato;
	}







	public void setStato(State stato) {
		this.stato = stato;
	}







	public void setTempo(LocalDateTime tempo) {
		this.tempo = tempo;
	}







	public void setTipo(TipoEvento tipo) {
		this.tipo = tipo;
	}







	public LocalDateTime getTempo() {
		return tempo;
	}


	public TipoEvento getTipo() {
		return tipo;
	}


	@Override
	public int compareTo(Evento other) {
		// TODO Auto-generated method stub
		return tempo.compareTo(other.tempo);
	}



	@Override
	public String toString() {
		return "Evento [tempo=" + tempo + ", tipo=" + tipo + "]";
	}
	
	

}
