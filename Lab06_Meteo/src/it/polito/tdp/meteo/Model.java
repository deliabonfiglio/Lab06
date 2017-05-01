package it.polito.tdp.meteo;

import java.util.*;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private MeteoDAO meteodao;
	private int mese;
	private List<Citta> cities= null;
	
	private List<SimpleCity> best=null;
	private double puntiBest;
	
	public Model() {
		meteodao= new MeteoDAO();
		best = new ArrayList<SimpleCity>();
		puntiBest=Double.MAX_VALUE;
		
		cities = new ArrayList<Citta>();
		for (String s : meteodao.getAllCities())
			cities.add(new Citta(s));
		
	}

	public String getUmiditaMedia(int mese) {
		String result="";
		this.mese= mese;
		double umidita = 0.0;
		
		for(Citta citta: cities){
			umidita = meteodao.getAvgRilevamentiLocalitaMese(mese, citta.getNome());
			result += citta.getNome()+ " umidità: "+umidita+"\n"; 
		}
		
		return result;
	}


	public String trovaSequenza(int mese) {
	this.mese= mese;

	this.resetCities(mese);
	ricerca(new ArrayList<SimpleCity>(), 0);
	
	System.out.println("Best: "+best.toString()+" "+puntiBest);
		return best.toString()+" cost: "+puntiBest;
	}

	private void resetCities(int mese) {
		best=new ArrayList<SimpleCity>();
		puntiBest= Double.MAX_VALUE;
		
		for(Citta ctemp:cities){
			ctemp.setCounter(0);
			ctemp.setRilevamenti(meteodao.getAllRilevamentiLocalitaMese(mese, ctemp.getNome()));
			ctemp.setCosto(meteodao.getAvgRilevamentiLocalitaMese(mese, ctemp.getNome()));
		}		
	}

	private void ricerca(List<SimpleCity> parziale, int step) {
		if(step== NUMERO_GIORNI_TOTALI){
			double punti = this.punteggioSoluzione(parziale);
			
			if(punti < puntiBest){  // ho trovato una soluzione migliore
				puntiBest = punti;
				best= new ArrayList<SimpleCity>(parziale);	
				
				System.out.println("MIGLIORE: "+best.toString()+" cost: "+puntiBest);
			}
		}
		for(Citta ctemp: cities){
			SimpleCity sc= new SimpleCity(ctemp.getNome());
			sc.setCosto(ctemp.getCosto());
			//devo settare il costo della simplecity
			
			parziale.add(sc);
			ctemp.increaseCounter();
			
				if(controllaParziale(parziale))
					ricerca(parziale, step+1);
				
//devo RIMUOVERE L'ULTIMO OGGETTO AGGIUNTO, MA NON COME OGGETTO! DEVO DARE L'INDICE!!!
//nb: LO STEP MI DA L'INDICE DI CHI STO AGGIUNGENDO
// step = parziale.size()-1

			//parziale.remove(parziale.size()-1);
			parziale.remove(step);
			ctemp.decreaseCounter();
		}
		
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		//controllo soluzione non nulla e con dimensione !=0
		if(soluzioneCandidata.size()==0 || soluzioneCandidata ==null)
			return Double.MAX_VALUE;
		
		//la soluzioneCandidata deve contenere tutte le citta
		for(Citta ctemp : cities){
			SimpleCity sc= new SimpleCity(ctemp.getNome());
			
			if(!soluzioneCandidata.contains(sc))
				return Double.MAX_VALUE;
		}
		
		SimpleCity sc_prec =soluzioneCandidata.get(0);
		
		for(SimpleCity sc: soluzioneCandidata){
			if(!sc_prec.equals(sc)){
				score += COST;
			} 
		
		sc_prec= sc;
		score += sc.getCosto();
		}
		
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {
		//soluzione non valida se nulla
		if(parziale == null)
			return false;
		
		//soluzione valida se la dimensione è 0
		if(parziale.size()==0)
			return true;
		
		//controllo vincolo numero giorni totali <= 6 su ogni citta
		for(Citta ctemp: cities){
			if(ctemp.getCounter()> NUMERO_GIORNI_CITTA_MAX)
				return false;
		}
				
		// controllo vincolo numero giorni consecutivi == 3 per ogni citta contenuta nella soluzione parziale
		int count=0;
		SimpleCity sc_prec = parziale.get(0);
		
		for (SimpleCity sc : parziale) {
			if (!sc_prec.equals(sc)) {
				if (count < NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
					return false;
				}
				count = 1;
				sc_prec = sc;
			} else {
				count++;
			}
		}

		return true;
	}


}