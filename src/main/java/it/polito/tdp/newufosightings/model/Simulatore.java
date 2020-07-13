package it.polito.tdp.newufosightings.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import it.polito.tdp.newufosightings.model.Evento.TipoEvento;


public class Simulatore {
	
	HashMap<State,Integer> defcon=new HashMap<State,Integer>();
	int giorniAvvistamento;
	int giorniEmergenza;
	PriorityQueue<Evento> queue= new PriorityQueue<Evento>();
	HashMap<State,Integer>  contatoreDefcon;


	public void init(int t1, int t2, ArrayList<Sighting> avvistamenti, ArrayList<State> stati) {
		contatoreDefcon=new HashMap<State,Integer>();
		for(State s:stati) {
			defcon.put(s, 5);
			contatoreDefcon.put(s, 0);
		}
		giorniAvvistamento=t1;
		giorniEmergenza=t2;
		queue.clear();
		for(Sighting s: avvistamenti) {
			
			Evento e=new Evento(s.getState(),s.getDatetime(),TipoEvento.INIZIO_AVVISTAMENTO);
			queue.add(e);
		}
	}

	public void run() {
		
		while(!queue.isEmpty()) {
			Evento ev=queue.poll();
			switch(ev.getTipo()) {
			
			case INIZIO_AVVISTAMENTO:
				State stato=ev.getStato();
				int def=defcon.get(stato);
				
				if(def==1) { //sono in emergenza, quindi scarto evento
					break;
				} else {
					LocalDateTime vecchio=ev.getTempo();
					int nuovoDefcon=def-1;
					defcon.put(stato, nuovoDefcon);
					if(nuovoDefcon==1) { //entro in emergenza
						queue.add(new Evento(stato,vecchio.plusDays(giorniEmergenza),TipoEvento.FINE_EMERGENZA));
						contatoreDefcon.put(stato, contatoreDefcon.get(stato)+1);
					} else { //abbasso, ma non sono in emergenza
						queue.add(new Evento(stato,vecchio.plusDays(giorniAvvistamento),TipoEvento.FINE_AVVISTAMENTO));
					}
				}
				break;
				
			case FINE_AVVISTAMENTO:
				State stato2=ev.getStato();
				int def2=defcon.get(stato2);
				if(def2==1) { //sono in emergenza, quindi scarto evento
					break;
				}
				defcon.put(stato2, defcon.get(stato2)+1);
				if(defcon.get(stato2)>5) {
					System.out.println("PROBLEMA!");
				}
				break;
				
			case FINE_EMERGENZA:
				State stato3=ev.getStato();
				defcon.put(stato3, 5);
				break;
			}
		}
		
	}

	public HashMap<State, Integer> getContatoreDefcon() {
		return contatoreDefcon;
	}
	
	

}
