package fr.uvsq.Projet_Convertisseur_;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.sound.sampled.Line;

import org.json.JSONObject;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

public class convertReaderTest {


	// actualiseTableauListe +exeption 
	//public int[] actualiseTableauListe(int[] tab)
	@Test
	public final void TestTrueactualiseTableauListe() {
		
		
		convertCsvJson c =new convertCsvJson();
		int[] tab =new int[4];
		
		
		for(int i=0;i<4;i++) {
			tab[i]=0;
		}
		tab=c.actualiseTableauListe(tab);
		//3311
		if(tab[0]==3) {
			if(tab[1]==3) {
				if(tab[2]==1) {
					
					assertEquals(tab[3],1);
				}
			}
		}
		
	
	}
	

	//actualiseTableauMotArray
	//public String[] actualiseTableauMotArray(String[] tabMot,int[] tab)
	@Test
	public final void TestTrueactualiseTableauMotArray() {
		convertCsvJson c =new convertCsvJson();
		int[] tab =new int[4];
		for(int i=0;i<4;i++) {
			tab[i]=0;
		}
		tab=c.actualiseTableauListe(tab);
		
		try{
			FileReader fr = new FileReader("csv.csv");
			CSVReader reader = new CSVReader(fr);
			String[] line= reader.readNext();
			reader.close();
			
			String[] res=new String[line.length];
		
			res=c.actualiseTableauMotArray(res, tab);
		
			
			
			if(res[0].equals("menuitem")) {
			
				if(res[1].equals("menuitem")) {
					if(res[2]==null) {
						assertEquals(res[3],null);
					}
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		
		
	}
	
	
	//afficheList impossible 
	//public void afficheList()
	@Test
	public final void TestTrueafficheList() {
		convertCsvJson c =new convertCsvJson();
		c.afficheList();
	}
	
	//concatString
	//public String concatString(String data)
	@Test
	public final void TestTrueconcatString() {
		convertCsvJson c =new convertCsvJson();
		
		try{
			FileReader fr = new FileReader("csv.csv");
			CSVReader reader = new CSVReader(fr);
			String[] line= reader.readNext();
			reader.close();
			
			
			String data =new String();
			data="id";
			c.list.add("menu");
			data=c.concatString(data);
			
		boolean tmp=line[2].equals(data);
			
						assertEquals(tmp,true);
			
		} 
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		
		
	}
	
	
	//estUneListe
	//public boolean estUneListe(String[] tab,String data)
	@Test
	public final void TestTrueestUneListe() {
		convertCsvJson c =new convertCsvJson();
		int[] tab =new int[4];
		for(int i=0;i<4;i++) {
			tab[i]=0;
		}
		tab=c.actualiseTableauListe(tab);
		
		try{
			FileReader fr = new FileReader("csv.csv");
			CSVReader reader = new CSVReader(fr);
			String[] line= reader.readNext();
			reader.close();
			
			String[] res=new String[line.length];
			boolean b;
			String data=new String();data="menuitem";
			res=c.actualiseTableauMotArray(res, tab);
			b=c.estUneListe(res, data);
			
						assertEquals(b,true);				
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}	
	}
	@Test
	public final void TestfalseestUneListe() {
		convertCsvJson c =new convertCsvJson();
		int[] tab =new int[4];
		for(int i=0;i<4;i++) {
			tab[i]=0;
		}
		tab=c.actualiseTableauListe(tab);
		
		try{
			FileReader fr = new FileReader("csv.csv");
			CSVReader reader = new CSVReader(fr);
			String[] line= reader.readNext();
			reader.close();
			
			String[] res=new String[line.length];
			boolean b;
			String data=new String();data="id";
			res=c.actualiseTableauMotArray(res, tab);
			b=c.estUneListe(res, data);
			
						assertEquals(b,false);				
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}	
	}
	
	//extractDataConfig
	//public String extractDataConfig(String line)
	@Test
	public final void TetsTrueextractDataConfig() {
		convertCsvJson c =new convertCsvJson();
		String line=new String();
		line="-mobile < mobile";
				line=c.extractDataConfig(line);
				
				boolean r= line.equals("mobile");
				assertEquals(r,true);
	}
	@Test
	public final void Tets2TrueextractDataConfig() {
		convertCsvJson c =new convertCsvJson();
		String line=new String();
		line="---mobile < mobile";
				line=c.extractDataConfig(line);
				boolean r= line.equals("mobile");
				assertEquals(r,true);
	}	
	//extractDataConfigWithDepth
	//public String extractDataConfigWithDepth(String line)
	@Test
	public final void TetsTrueextractDataConfigWithDepth() {
		convertCsvJson c =new convertCsvJson();
		String line=new String();
		line="---mobile < mobile";
				line=c.extractDataConfigWithDepth(line);
			
				boolean r= line.equals("---mobile");
				assertEquals(r,true);
	}
	//extractDataCsv
	//public String extractDataCsv(int[] profondeur,String data)
	@Test
	public final void TetsTrueextractDataCsv() {
		convertCsvJson c =new convertCsvJson();
		
		try{
			FileReader fr = new FileReader("csv.csv");
			CSVReader reader = new CSVReader(fr);
			String[] line= reader.readNext();
			reader.close();
			
			
			String data=new String();data="menu_popup_menuitem_value";
			//data=c.extractDataCsv(profondeur, data);
			//	A FINIR
		
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}	
		
	}
	//extractDataWithoutDepth
	//public String extractDataWithoutDepth(String line)
	@Test
	public final void TetsTrueextractDataWithoutDepth() {
		convertCsvJson c =new convertCsvJson();
		String line=new String();
		line="---mobile < mobile";
				line=c.extractDataWithoutDepth(line);
			
				boolean r= line.equals("mobile < mobile");
				assertEquals(r,true);
	}
	//gereAccumulatePourListe
	//public JSONObject gereAccumulatePourListe(JSONObject pere,int[] profondeur,String data)
	
	//gestionList
	//public void gestionList(String data,JSONObject obj) 

	//leNomDeLaFonction nope
	//public JSONObject leNomDeLaFonction(BufferedReader reader,String line,JSONObject pere,int[] profondeur,int[] estListe,String[] motArray,boolean pereEstListe)

	//nom nope
	//public void nom()
	
	// nombreColonne
	//public int nombreColonne()
	@Test
	public final void TestnombreColonne() {
		int res=0;
		convertCsvJson c =new convertCsvJson();
		res=c.nombreColonne();
		
		assertEquals(res,4);
	}
	
	//nombreElements
	//public int nombreElements()
	@Test
	public final void TestnombreElements() {
		int res=0;
		convertCsvJson c =new convertCsvJson();
		res=c.nombreElements();
		
		assertEquals(res,4);
	}
	
	
	//typeLecture 
	//public String typeLecture(String line)
	@Test
	public final void TestTruetypeLecture() {
		convertCsvJson c =new convertCsvJson();
		String line =new String ();
		boolean r;
		line="-mobile < mobile";
		line=c.typeLecture(line);
		r=line.equals("value");
		assertEquals(r,true);
	}
	@Test
	public final void Test2TruetypeLecture() {
		convertCsvJson c =new convertCsvJson();
		String line =new String ();
		boolean r;
		line="tel : ";
		line=c.typeLecture(line);
		r=line.equals("object");
		assertEquals(r,true);
	}	
}
