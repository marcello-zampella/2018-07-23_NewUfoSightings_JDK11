package it.polito.tdp.newufosightings.model;

import java.util.Random;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class TestModel {

	public static void main(String[] args) {
		
		Random rand=new Random();
		for(int i=0;i<100;i++) {
			System.out.println(rand.nextInt(51+1-50)+50);
		}
		
	}

}
