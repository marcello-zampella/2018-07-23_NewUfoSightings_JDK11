package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
						res.getString("city"), new State(res.getString("state")), res.getString("country"), res.getString("shape"),
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

	public ArrayList<Collegamento> getAllCollegamenti(int xg, int anno) {
		String sql = "SELECT n.state1 AS stato1, n.state2 AS stato2, COUNT(*) AS conto " + 
				"FROM sighting s1, sighting s2, neighbor n " + 
				"WHERE YEAR(s1.datetime)=? AND YEAR(s2.datetime)=? AND DATEDIFF(s1.datetime,s2.datetime)<=? AND DATEDIFF(s1.datetime,s2.datetime)>=0 AND s1.id<>s2.id " + 
				"AND s1.state=n.state1 AND s2.state=n.state2 " + 
				"group BY n.state1, n.state2";
		ArrayList<Collegamento> result = new ArrayList<Collegamento>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			st.setInt(3, xg);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Collegamento coll =new Collegamento( new State(rs.getString("stato1").toLowerCase()),
						new State(rs.getString("stato2").toLowerCase()),
						rs.getInt("conto"));
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

	public ArrayList<Sighting> getSighting(int xg, int anno) {
		String sql = "SELECT DISTINCT s1.id, s1.datetime,s1.state,s1.duration " + 
				"FROM sighting s1, sighting s2 " + 
				"WHERE YEAR(s1.datetime)=? AND YEAR(s2.datetime)=? AND DATEDIFF(s1.datetime,s2.datetime)<=? AND s1.id<>s2.id ";
		ArrayList<Sighting> list = new ArrayList<Sighting>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			st.setInt(3, xg);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						new State(res.getString("state")),
						res.getInt("duration")));
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

}

