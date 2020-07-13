package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.newufosightings.model.Arco;
import it.polito.tdp.newufosightings.model.Collegamento;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public ArrayList<State> loadAllStates() {
		String sql = "SELECT * FROM state";
		ArrayList<State> result = new ArrayList<State>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id").toLowerCase(), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				result.add(state);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public ArrayList<String> getFormaByAnno(int anno) {
		String sql = "SELECT DISTINCT s.shape AS forma " + 
				"from sighting s " + 
				"WHERE YEAR(s.datetime)=? AND s.shape<>'' " + 
				"ORDER BY s.shape ";
		ArrayList<String> result = new ArrayList<String>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String forma = rs.getString("forma");
				result.add(forma);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public ArrayList<Collegamento> getAllCollegamenti(String forma, int anno) {
		String sql = "SELECT n.state1, n.state2, COUNT(*) AS conto " + 
				"FROM neighbor n, sighting s1, sighting s2 " + 
				"WHERE n.state1>n.state2 AND s1.shape=? AND s2.shape=? AND YEAR(s1.datetime)=? AND YEAR(s2.datetime)=? AND s1.state=n.state1 AND s2.state=n.state2 " + 
				"GROUP BY n.state1,n.state2 ";
		ArrayList<Collegamento> result = new ArrayList<Collegamento>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, forma);
			st.setString(2, forma);
			st.setInt(3, anno);
			st.setInt(4, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Collegamento coll = new Collegamento(new State(rs.getString("state1").toLowerCase()),new State(rs.getString("state2").toLowerCase()),rs.getInt("conto"));
				result.add(coll);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public ArrayList<Sighting> getAvvistamentiByAnno(String forma, int anno) {
		String sql = "SELECT * " + 
				"FROM sighting s1 " + 
				"WHERE s1.shape=? AND YEAR(s1.datetime)=? ";
		ArrayList<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			st.setString(1, forma);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public ArrayList<Arco> getAllArchi() {
		String sql = "SELECT * " + 
				"FROM neighbor ";
		ArrayList<Arco> list = new ArrayList<Arco>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Arco (new State(res.getString("state1").toLowerCase()),(new State(res.getString("state2").toLowerCase()))));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}


}

