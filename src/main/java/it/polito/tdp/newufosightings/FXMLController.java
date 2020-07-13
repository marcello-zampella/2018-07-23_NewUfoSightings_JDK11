package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

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

//controller turno A --> switchare al branch master_turnoB per turno B

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
    private Button btnSelezionaAnno;

    @FXML
    private ComboBox<String> cmbBoxForma;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private TextField txtT1;

    @FXML
    private TextField txtAlfa;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String s=this.cmbBoxForma.getValue();
    	if(s==null) {
    		this.txtResult.setText("FAI UNA SCELTA!");
    		return;
    	}
    	this.model.creaGrafo(s,Integer.parseInt(this.txtAnno.getText()));
    	
    	SimpleWeightedGraph<State, DefaultWeightedEdge> grafo=model.getGrafo();
    	for(State state: grafo.vertexSet()) {
    		this.txtResult.appendText("\n somma pesi archi stato "+state+": ");
    		int somma=0;
    		for(DefaultWeightedEdge e:grafo.outgoingEdgesOf(state)) {
    			somma+=grafo.getEdgeWeight(e);
    		}
    		this.txtResult.appendText(""+somma+"\n");
    	}
    	
    	
		this.btnSimula.setDisable(false);
    	
    }

    @FXML
    void doSelezionaAnno(ActionEvent event) {
    	String s=this.txtAnno.getText();
    	if(!this.isInteger(s)) {
    		this.txtResult.setText("INSERISCI VALORE INTERO \n");
    		return;
    	}
    	int anno=Integer.parseInt(s);
    	if(anno<1910 || anno>2014) {
    		this.txtResult.setText("INSERISCI VALORE INTERO COMPRESO TRA 1910 E 2014 \n");
    		return;
    	}
    	this.cmbBoxForma.getItems().clear();
    	this.cmbBoxForma.getItems().addAll(model.getForma(anno));
		this.btnCreaGrafo.setDisable(false);


    

    }

    @FXML
    void doSimula(ActionEvent event) {
    	String giornis=this.txtT1.getText();
    	String alfas=this.txtAlfa.getText();
    	if(!this.isInteger(giornis) || !this.isInteger(alfas)) {
    		this.txtResult.appendText("DEVI INSERIRE UN NUMERO INTERO SIA PER T1 CHE PER ALFA \n");
    		return;
    	}
    	int giorni=Integer.parseInt(giornis);
    	if(giorni<1 || giorni>364) {
    		this.txtResult.appendText("GIORNI NUMERO POSITIVO DIVERSO DA 0 MINORE DI 365 \n");
    		return;
    	}
    	int alfa=Integer.parseInt(alfas);
    	if(alfa<0 || alfa>100) {
    		this.txtResult.appendText("ALFA NUMERO POSITIVO MINORE O UGUALE A 100 \n");
    		return;
    	}
    	
    	HashMap<State,Double> risultato=model.simula(giorni,alfa);
    	this.txtResult.appendText("\n ** RISULTATI SIMULAZIONE ** \n");
    	for(State stato: risultato.keySet()) {
    		this.txtResult.appendText(stato+" DEFCON FINALE="+risultato.get(stato)+"\n");
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
        assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.btnCreaGrafo.setDisable(true);
		this.btnSimula.setDisable(true);
	}
}
