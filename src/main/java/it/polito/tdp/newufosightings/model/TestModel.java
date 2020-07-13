package it.polito.tdp.newufosightings.model;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class TestModel {

	public static void main(String[] args) {
		SimpleWeightedGraph<State, DefaultWeightedEdge> grafo= new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		State s1=new State("AA");
		State s2=new State("BB");
		grafo.addVertex(s1);
		grafo.addVertex(s2);

		Graphs.addEdge(grafo, s1, s2, 2);
		System.out.println(grafo.containsEdge(s2, s1));
		
	}

}
