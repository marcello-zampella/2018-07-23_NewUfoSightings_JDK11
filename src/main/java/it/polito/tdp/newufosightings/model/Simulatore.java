package it.polito.tdp.newufosightings.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.model.Evento.TipoEvento;

public class Simulatore {
	
	
	PriorityQueue<Evento> queue= new PriorityQueue<Evento>();
	HashMap<State,Double> livelliDefcon=new HashMap<State,Double>();
	int alfa;
	Random rand=new Random();
	private int giorni;
	SimpleWeightedGraph<State, DefaultWeightedEdge> grafo;
	LocalDateTime temporanea;
	
	public void init(int giorni, int alfa, ArrayList<Sighting> avvistamenti, ArrayList<State> stati, SimpleWeightedGraph<State, DefaultWeightedEdge> grafo) {
		this.grafo=grafo;
		this.alfa=alfa;
		this.giorni=giorni;

		for(State s:stati) {
			livelliDefcon.put(s, 5.0);
		}
		queue.clear();
		temporanea=avvistamenti.get(0).getDatetime();
		for(Sighting avvistamento: avvistamenti) {
			int durata=rand.nextInt((10-1)+1)+1;
			
			Evento ei=new Evento(new State(avvistamento.getState()),avvistamento.getDatetime(),TipoEvento.INIZIO_ALLERTA);
			queue.add(ei);
			if(avvistamento.getDatetime().isAfter(temporanea)) {
				temporanea=avvistamento.getDatetime();
			}
		}
	}

	public void run() {
		
		while(!queue.isEmpty()) {
			
			Evento ev=queue.poll();
			
			switch(ev.getTipo()) {
			
			case INIZIO_ALLERTA:
				
				State stato=ev.getStato();
				
				double decremento=0.0;
				LocalDateTime tempoNuovo=ev.getTempo().plusDays(giorni);
				if(this.livelliDefcon.get(stato)>=(2)) {
					decremento=1.0;
				}
				double nuovoLivello=this.livelliDefcon.get(stato)-decremento;
				this.livelliDefcon.put(stato, nuovoLivello);
				Evento e=new Evento(stato,tempoNuovo,decremento,TipoEvento.FINE_ALLERTA);
				queue.add(e);
				
				if(alfa>=rand.nextInt(100)+1) {
					for(State temp:Graphs.neighborListOf(grafo, stato)) {
						decremento=0;
						if(this.livelliDefcon.get(temp)>=1.5) {
							decremento=0.5;
						}
						this.livelliDefcon.put(temp, this.livelliDefcon.get(temp)-decremento);
						e=new Evento(temp,tempoNuovo,decremento,TipoEvento.FINE_ALLERTA);
						queue.add(e);
					}
				}
				if(temporanea.isEqual(ev.getTempo())) {
					queue.clear();
				}
				break;
				
			case FINE_ALLERTA:
				
				State stat=ev.getStato();
				double livelloNuovo=this.livelliDefcon.get(stat)+ev.getDecremento();
				if(livelloNuovo<=5)
					this.livelliDefcon.put(stat, livelloNuovo);
				if(livelloNuovo>5) {
					System.out.println("AAAAAAAAAAAAAAAAAAAA");
				}
				break;
			}
		}
		
		
		
	}

	public HashMap<State, Double> getLivelliDefcon() {
		return livelliDefcon;
	}
	
	
	
	
	

}
