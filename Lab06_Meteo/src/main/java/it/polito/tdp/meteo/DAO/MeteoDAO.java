package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
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

		String sql = "SELECT Localita,DATA,umidita FROM situazione WHERE localita = ? AND MONTH(DATA) = ? ";
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		
		try {
			Connection conn = ConnectDB.getConnection();
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
	
	public List<Rilevamento> GetRilevamentidiunMese(int mese){
		
		String sql = "SELECT Localita,DATA,umidita FROM situazione WHERE DAY(DATA) < 16 AND MONTH(DATA) = ? GROUP BY umidita asc ";
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			//st.setString(1, localita);
			st.setInt(1, mese);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				//Citta c = new Citta(rs.getString("Localita"),r);
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	public List<Citta> getCitta(){
		
String sql = "SELECT distinct Localita FROM situazione ";
		
		
		List<Citta> citta = new ArrayList<Citta>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				//Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				Citta c = new Citta(rs.getString("Localita"),null);
				citta.add(c);
			}

			conn.close();
			return citta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	


	public List<Rilevamento> getRilevamentiCitta(String localita){
	
		String sql = "SELECT Localita,DATA,umidita FROM situazione WHERE localita = ? ";
		
		
		List<Rilevamento> Rilevamenti = new ArrayList<Rilevamento>();
		
		try {
			
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				//Citta c = new Citta(rs.getString("Localita"),null);
				//citta.add(c);
				Rilevamenti.add(r);
			}

			conn.close();
			return Rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
}
