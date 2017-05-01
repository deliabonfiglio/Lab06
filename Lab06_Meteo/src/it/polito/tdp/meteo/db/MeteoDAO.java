package it.polito.tdp.meteo.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE localita= ? AND MONTH(Data)= ?";
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, localita);
			st.setInt(2, mese);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}	
	
	public List<String> getAllCities() {

		final String sql = "SELECT DISTINCT Localita FROM situazione ";
		List<String> citta = new ArrayList<String>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				String r = rs.getString("Localita");
				citta.add(r);
			}

			conn.close();
			return citta;
			
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	

//visualizzare il valore dell’umidità media per quel mese in ciascuna delle città presenti nel database.
	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		final String sql = "SELECT AVG(Umidita) AS UmiditaMedia FROM situazione WHERE localita= ? AND MONTH(Data)= ?";

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, localita);
			st.setInt(2, mese);
			ResultSet rs = st.executeQuery();

			double media = 0.0;
			
			if (rs.next())
				media= rs.getDouble("UmiditaMedia");
			
			conn.close();
			return media;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}