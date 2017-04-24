package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	MeteoDAO meteoDAO = new MeteoDAO();

	public Model() {

	}
//visualizzare il valore dell’umidità media per quel mese in ciascuna delle città presenti nel database
	public String getUmiditaMedia(int mese) {
		String result = "";
		double umidita = 0.0;
		
		List<String> localita= new ArrayList<String>(meteoDAO.getAllCities());
		for(String s: localita){
			umidita = meteoDAO.getAvgRilevamentiLocalitaMese(mese, s);
			result += s.toString() +" "+ umidita+ "\n";
		}
		
		return result;
	}

	public String trovaSequenza(int mese) {

		return "TODO!";
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}

}
