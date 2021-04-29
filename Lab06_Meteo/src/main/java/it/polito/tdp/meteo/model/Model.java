package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	List<Citta> SoluzioneMigliore = new ArrayList<Citta>();
	List<Citta> citta = new ArrayList<Citta>();
	Map<String,Citta> cittamappa = new HashMap<String,Citta>();
	int costoMIN;
	List<Rilevamento> rilevamenti;
	boolean nuovacitta;
	int contatore;
	
	MeteoDAO meteoro = new MeteoDAO();
	
	public Model() {
		
		for(Citta c: meteoro.getCitta()) {
			cittamappa.put(c.getNome(), c);
			cittamappa.get(c.getNome()).setRilevamenti(meteoro.getRilevamentiCitta(c.getNome()));
		}

		nuovacitta = true;
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		
		
		double MediaTorino = GetMediadelMesediUnaLocalita(mese,"Torino");
		double MediaMilano = GetMediadelMesediUnaLocalita(mese,"Milano");
		double MediaGenova = GetMediadelMesediUnaLocalita(mese,"Genova");
		String aux = "";
		
		aux = GetStringdiLoc(MediaTorino,"Torino",mese)+"\n"+GetStringdiLoc(MediaMilano,"Milano",mese)+"\n"+GetStringdiLoc(MediaGenova,"Genova",mese)+"\n";
		
		return aux.trim();
	}
	
	// of course you can change the String output with what you think works best
	public String trovaSequenza(int mese) {
		
		List<Citta> SoluzioneParziale = new ArrayList<Citta>();
		List<Citta> InsiemeOpzioni = new ArrayList<Citta>();
		for(Citta c: cittamappa.values()) {
			InsiemeOpzioni.add(new Citta(c.getNome(),c.getRilevamentoMese(mese)));
		}
		
		costoMIN = 400*4 + 100*15;
		nuovacitta = true;
		contatore = 0;
		
		Ricorsione(0,0,InsiemeOpzioni,SoluzioneParziale,null);
		
		String aux = "";
		for(int i = 0; i < SoluzioneMigliore.size();i++) {
			aux += SoluzioneMigliore.get(i).getNome()+"\n";
		}
		
		aux += "\n"+"Costo di: "+costoMIN;
		
		return aux.trim();
	}
	
	private void Ricorsione(int costo, int giorno,List<Citta> InsiemeOpzioni, List<Citta> SoluzioneParziale, Citta cittaattuale) {
		
		if(giorno == NUMERO_GIORNI_TOTALI) {
			if(costo<costoMIN) {
			SoluzioneMigliore = new ArrayList<Citta>(SoluzioneParziale);
			costoMIN = costo;
			}
			return;
		}
		else {
			for(Citta c: InsiemeOpzioni) {
				if(giorno>2) {
					if(SoluzioneParziale.get(giorno-1).equals(SoluzioneParziale.get(giorno-2)) && SoluzioneParziale.get(giorno-2).equals(SoluzioneParziale.get(giorno-3)))
					{ 
							nuovacitta = true;
					}
					else {
							nuovacitta = false;
					}
				}
				else if(giorno == 0) {
					nuovacitta = true;
				}
				else {
					nuovacitta = false;
				}
				
				int umidita = c.getRilevamenti().get(giorno).getUmidita();
				if(c.equals(cittaattuale)) {
					if(c.getCounter()<NUMERO_GIORNI_CITTA_MAX && (umidita+costo) < costoMIN) {
						SoluzioneParziale.add(c);
						c.increaseCounter();
						Ricorsione(umidita+costo,giorno+1,InsiemeOpzioni,SoluzioneParziale,c);
						SoluzioneParziale.remove(c);
						c.setCounter(c.getCounter()-1);
					}
				}
				
				else {
					if(c.getCounter()<NUMERO_GIORNI_CITTA_MAX && (umidita+costo+COST) < costoMIN && nuovacitta==true) {
						SoluzioneParziale.add(c);
						c.increaseCounter();
						Ricorsione(umidita+costo+COST,giorno+1,InsiemeOpzioni,SoluzioneParziale,c);
						SoluzioneParziale.remove(c);
						c.setCounter(c.getCounter()-1);
						
					}
					
				}
			}
			
			
			
		}
		
		
		
	}	

	public double GetMediadelMesediUnaLocalita(int mese, String localita) {
		
		List<Rilevamento> Rilevamenti = new ArrayList<Rilevamento>(meteoro.getAllRilevamentiLocalitaMese(mese, localita));
		
		double aux = 0;
		double media;
		
		for(Rilevamento r : Rilevamenti) {
			aux += r.getUmidita();
		}
		
		media = aux/Rilevamenti.size();
		
		return media;
	}
	
	public String GetStringdiLoc(double media,String loc,int mese) {
		String s = "";
		
		s = "La media di umidita della citta' di "+loc+" nel mese "+mese+" e' stata di: "+media;
		
		return s;
	}
}
