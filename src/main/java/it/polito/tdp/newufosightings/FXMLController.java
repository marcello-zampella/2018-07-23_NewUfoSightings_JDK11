package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno B --> switchare al branch master_turnoA per turno A

public class FXMLController {
	
	private Model model;

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtAnno;

    @FXML
    private TextField txtxG;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private TextField txtT1;

    @FXML
    private TextField txtT2;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String xgs=this.txtxG.getText();
    	String annos=this.txtAnno.getText();
    	if(!this.isInteger(xgs)  || !this.isInteger(annos)) {
    		this.txtResult.appendText("DEVI INSERIRE VALORI NUMERICI INTERI PER ENTRAMBI\n");
    		return;
    	}
    	int xg=Integer.parseInt(xgs);
    	if(xg<1 || xg>180) {
    		this.txtResult.appendText("PER XG DEVI INSERIRE VALORE INTERO COMPRESO TRA 1 E 180\n");
    		return;
    	}
    	int anno=Integer.parseInt(annos);
    	if(anno<1906 || anno>2014) {
    		this.txtResult.appendText("PER ANNO DEVI INSERIRE VALORE INTERO COMPRESO TRA 1906 e 2014\n");
    		return;
    	}
    	SimpleWeightedGraph<State, DefaultWeightedEdge> grafo=model.creaGrafo(xg,anno);
    	this.txtResult.setText("CREATO GRAFO CON "+grafo.vertexSet().size()+" NODI E "+grafo.edgeSet().size()+" ARCHI\n");
    	for(State s: grafo.vertexSet()) {
    		int pesoTotale=0;
    		for(DefaultWeightedEdge e: grafo.outgoingEdgesOf(s)) {
    			pesoTotale+=grafo.getEdgeWeight(e);
    		}
    		this.txtResult.appendText(s+" totale peso archi "+pesoTotale+"\n");
    	}

		this.btnSimula.setDisable(false);
    }

    @FXML
    void doSimula(ActionEvent event) {
    	String t1s=this.txtT1.getText();
    	String t2s=this.txtT2.getText();
    	if(!this.isInteger(t1s) || !this.isInteger(t2s)) {
    		this.txtResult.setText("DEVI INSERIRE VALORI INTERI SIA PER T1 CHE PER T2\n");
    		return;
    	}
    	int t1=Integer.parseInt(t1s);
    	int t2=Integer.parseInt(t2s);
    	if(t1<1 || t1>=365) {
    		this.txtResult.setText("T1 DEVE AVERE UN VALORE COMPRESO TRA 1 E 364\n");
    		return;
    	}
    	if(t1<2 || t2>=365) {
    		this.txtResult.setText("T2 DEVE AVERE UN VALORE COMPRESO TRA 1 E 364\n");
    		return;
    	}
    	HashMap<State, Integer> mappa=model.simula(t1,t2);
    	this.txtResult.appendText("\n *** SIMULAZIONE *** \n");
    	for(State s: mappa.keySet()) {
    		this.txtResult.appendText(s+" e' stato in emergenza "+mappa.get(s)+"\n");
    	}


    }
    
    public static boolean isInteger(String str) { 
	  	  try {  
	  	    Integer.parseInt(str);  
	  	    return true;
	  	  } catch(NumberFormatException e){  
	  	    return false;  
	  	  }  
	  	}

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtxG != null : "fx:id=\"txtxG\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT2 != null : "fx:id=\"txtT2\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.btnSimula.setDisable(true);
	}
}
