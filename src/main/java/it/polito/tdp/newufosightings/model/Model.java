package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	public Model() {
		dao=new NewUfoSightingsDAO();
	}
	
	private NewUfoSightingsDAO dao;
	private SimpleWeightedGraph<State, DefaultWeightedEdge> grafo;
	private ArrayList<State> stati;
	private ArrayList<Sighting> avvistamenti;

	public SimpleWeightedGraph<State, DefaultWeightedEdge> creaGrafo(int xg, int anno) {
		grafo= new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		stati=dao.loadAllStates();
		Graphs.addAllVertices(grafo, stati);
		ArrayList<Collegamento> collegamenti=dao.getAllCollegamenti(xg,anno);
		for(Collegamento coll:collegamenti) {
			int peso=coll.getPeso();
			State s1=coll.getS1();
			State s2=coll.getS2();
			if((s1.getId().equals("sd") && s1.getId().equals("ne"))||(s2.getId().equals("sd") && s1.getId().equals("ne"))) {
				System.out.println("QUI");
			}
			if(!grafo.containsEdge(s1, s2)) {
				Graphs.addEdge(grafo, s1, s2, peso);
			} else {
				DefaultWeightedEdge e= grafo.getEdge(s1, s2);
				int pesoarco=(int) grafo.getEdgeWeight(e);
				peso+=pesoarco;
				grafo.setEdgeWeight(e, peso);
			}
		}
		avvistamenti=dao.getSighting(xg,anno);
		return grafo;
	}

	public HashMap<State, Integer> simula(int t1, int t2) {
		Simulatore sim=new Simulatore();
		sim.init(t1,t2,avvistamenti,stati);
		sim.run();
		return sim.getContatoreDefcon();
		
	}

}
