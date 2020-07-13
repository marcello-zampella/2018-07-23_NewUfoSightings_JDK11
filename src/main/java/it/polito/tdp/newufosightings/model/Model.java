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
	private ArrayList<String> forme;
	private ArrayList<State> stati;
	private SimpleWeightedGraph<State, DefaultWeightedEdge> grafo;
	
	public ArrayList<String> getForma(int anno) {
		
		forme=dao.getFormaByAnno(anno);
		return forme;

		
	}

	public void creaGrafo(String forma, int anno) {
		stati=dao.loadAllStates();
		grafo=new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, stati);
		ArrayList<Collegamento> collegamenti=dao.getAllCollegamenti(forma,anno);
		for(Collegamento c: collegamenti) {
			Graphs.addEdge(grafo, c.getS1(), c.getS2(), c.getPeso());
		}
		
		avvistamenti=dao.getAvvistamentiByAnno(forma,anno);
	
	}

	public SimpleWeightedGraph<State, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	private ArrayList<Sighting> avvistamenti;


	public HashMap<State, Double> simula(int giorni, int alfa) {
		Simulatore sim=new Simulatore();
		sim.init(giorni,alfa,avvistamenti,stati,grafo);
		sim.run();
		return sim.getLivelliDefcon();
		
	}
	

}
