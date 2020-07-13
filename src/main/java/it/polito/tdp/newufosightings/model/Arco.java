package it.polito.tdp.newufosightings.model;

public class Arco {

	private State s1;
	private State s2;
	public State getS1() {
		return s1;
	}
	public void setS1(State s1) {
		this.s1 = s1;
	}
	public State getS2() {
		return s2;
	}
	public void setS2(State s2) {
		this.s2 = s2;
	}
	public Arco(State s1, State s2) {
		super();
		this.s1 = s1;
		this.s2 = s2;
	}
	
}
