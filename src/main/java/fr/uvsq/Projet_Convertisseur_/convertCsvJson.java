package fr.uvsq.Projet_Convertisseur_;
import java.io.*;
import au.com.bytecode.opencsv.CSVReader;
import java.util.*;
//import org.json.simple.JSONObject;
import org.json.*;

public class convertCsvJson {
	
	List<String> list = new ArrayList<String>();
	List<JSONObject> objets = new ArrayList<JSONObject>();		
	

		public void gestionList(String data,JSONObject obj) 
		{
			
			if(objets.size()==1)
			{
				list.add(data);
				objets.add(obj);
			}
			else {
			int cpt = 0;
			for(cpt=0;cpt<data.length() && (data.charAt(cpt) == '-');cpt++);
			
			if(objets.size()-1 > cpt)
			{
				while(cpt<objets.size()-1)
				{
					list.remove(list.size()-1);
					objets.remove(objets.size()-1);
				}
			}
			list.add(data);
			objets.add(obj);
			}
		}
		
		
		public String typeLecture(String line)
		{
			for(int i=0;i<line.length();i++)
			{
				if(line.charAt(i) == '<'){
					return "value";}
				else if(line.charAt(i) == ':') {
					return "object";}
			}
			return "error";
		}
		
		public String extractDataConfigWithDepth(String line)
		{
			String data = line.substring(0,line.indexOf(32));
			return data;			
		}
		
		
		public String extractDataConfig(String line)
		{
			int cpt=0;
			
			for(int i=0;i<line.length();i++)
			{
				if(line.charAt(i) == '-')
					cpt++;
			}
			String data = line.substring(cpt,line.indexOf(32));
			return data;			
		}
		
		public String extractDataWithoutDepth(String line)
		{
			int cpt=0;
			
			for(int i=0;i<line.length();i++)
			{
				if(line.charAt(i) == '-')
					cpt++;
			}
			String data = line.substring(cpt,line.length());
			return data;			
		}
		
		public String concatString(String data)
		{

			
			String dataConcat = "";
			
			int tailleList = list.size();
			
			for(int i=0;i<tailleList;i++)
			{
				String transi = list.get(0);
				String transiWithoutDepth = extractDataWithoutDepth(list.get(0));

				if(i == 0)
					dataConcat = transiWithoutDepth;
				else
					dataConcat = dataConcat + '_' + transiWithoutDepth;
				list.remove(0);
				list.add(transi);
			}
			if(dataConcat == "")
				dataConcat = data;
			else
				dataConcat = dataConcat + '_' + data;
			
			return dataConcat;
		}
		
		public int nombreColonne()
		{
			String[] line = null;
			
			try{
				
				FileReader fr = new FileReader("csv.csv");
				CSVReader reader = new CSVReader(fr);
				
				line = reader.readNext();

				reader.close();
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			
			return line.length;
		}
		
		
		public String extractDataCsv(int[] profondeur,String data)
		{
			
			int colonne = 0;
			String[] line = null;
			try{
				
				FileReader fr = new FileReader("csv.csv");
				CSVReader reader = new CSVReader(fr);
				
				line = reader.readNext();
				
				for(int i=0;i<line.length;i++)
				{
					if(line[i].equals(concatString(data)))
						colonne = i;
				}			
				
				for(int i=1;i<=profondeur[colonne];i++)
				{
					line = reader.readNext();
				}
				
				reader.close();
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			return line[colonne];
		}
		
		public void afficheList()
		{
			System.out.println("la liste contient de l'ele 0 a n : ");
			for(int i=0;i<list.size();i++)
			{
				System.out.print(list.get(i));
				System.out.print(" / ");
			}
			System.out.println();
		}
		
		public boolean estUneListe(String[] tab,String data)
		{
			for(int i=0;i<tab.length;i++)
				if(data.equals(tab[i]))
					return true;
			return false;
		}
		
		public int positionColonne(String[] ligne,String value)
		{
			int colonne = 0;
			
			for(int i=0;i<ligne.length;i++)
			{
				if(ligne[i].equals(value))
				{
					colonne = i;
				}
			}
			
			return colonne;
		}
		
		public int positionColonne(String data)
		{
			int colonne = 0;
			
			String dataConcat = concatString(data);
			
			
			String[] line = null;
			try{
				
				FileReader fr = new FileReader("csv.csv");
				CSVReader reader = new CSVReader(fr);
				
				line = reader.readNext();
				
				colonne = positionColonne(line, dataConcat);
				
				reader.close();
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			
			
			return colonne;
		}
		
		public JSONObject gereAccumulatePourListe(JSONObject pere,int[] profondeur,String data)
		{
			String value = concatString(data);
			
			System.out.print("data :");
			System.out.println(data);
			System.out.println();
			
			System.out.print("value :");
			System.out.println(value);
			System.out.println();			
			
			String[] ligne = null;
			
			try{
				
				FileReader fr = new FileReader("csv.csv");
				CSVReader reader = new CSVReader(fr);
				
				ligne = reader.readNext();
				
				int colonne = positionColonne(ligne,value); 
						
				ligne = reader.readNext();
				System.out.println();
				System.out.println();
				System.out.println("//////////////////////////////////////////////");
				System.out.println(pere);
				System.out.println("//////////////////////////////////////////////");
				System.out.println();
				System.out.println();
				
				while((ligne = reader.readNext())!=null)
				{
					
					if(ligne[colonne].length()>=1)
					{
						pere.accumulate(data, ligne[colonne]);
					}
			
				}
				
				reader.close();
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			

			return pere;
		}
		
		public JSONObject leNomDeLaFonction(BufferedReader reader,String line,JSONObject pere,int[] profondeur,int[] estListe,String[] motArray,boolean pereEstListe)
		{
			if(line == null)
				return pere;
			else {
				String type = typeLecture(line);
				String data = extractDataConfig(line);
				
				System.out.print("je lis data : ");
				System.out.println(data);
				System.out.println();
				
				System.out.print("je lis line : ");
				System.out.println(line);
				System.out.println();
				
				
				
				if(type == "value")
				{
					pere.accumulate(data, extractDataCsv(profondeur,data));
					try{
						line = reader.readLine();
						}
					catch(IOException e)
					{
						e.printStackTrace();
					}
					
//					si pere est liste ou que l'element est une liste
					
					if(pereEstListe == true || (estListe[positionColonne(data)]>1))
					{
						System.out.println("je suis une value est mon pere une liste");
						System.out.println();
						
						pere = gereAccumulatePourListe(pere,estListe, data);
						leNomDeLaFonction(reader,line,pere,profondeur,estListe,motArray,true);
					}
					else
					{
						System.out.println("je suis une value est mon pere n'est pas une liste");
						System.out.println();
						
						leNomDeLaFonction(reader,line,pere,profondeur,estListe,motArray,false);
					}
					
				}
				
				else if(type == "object")
				{
					
					JSONObject fils = new JSONObject();
					gestionList(extractDataConfigWithDepth(line),fils);
					
					
					String lineTransi = "";
					
					try{
						lineTransi = reader.readLine();
						}
					catch(IOException e)
					{
						e.printStackTrace();
					}
					if(estUneListe(motArray, data))
					{	
						System.out.println("je suis un objet est une liste");
						System.out.println();
						
						objets.get(objets.size()-2).accumulate(data, leNomDeLaFonction(reader,lineTransi,objets.get(objets.size()-1),profondeur,estListe,motArray,true));
					}
					else
					{
						System.out.println("je suis un objet mais pas une liste");
						System.out.println();
						objets.get(objets.size()-2).accumulate(data, leNomDeLaFonction(reader,lineTransi,objets.get(objets.size()-1),profondeur,estListe,motArray,false));
					}
				}
			}
			return pere;
		}
		
		public int nombreElements()
		{
			BufferedReader reader;
			String line = "";
			try{
				reader = new BufferedReader(new FileReader("csv.csv"));
				line = reader.readLine();
				
				reader.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return line.split(",").length;			
		}
		
		public int[] actualiseTableauListe(int[] tab)
		{
			
			String[] line = null;
			
			try{
				
				FileReader fr = new FileReader("csv.csv");
				CSVReader reader = new CSVReader(fr);
				
				line = reader.readNext();
				line = reader.readNext();

				while(line != null)
				{
					
					for(int i=0;i<line.length;i++)
					{
						if(line[i].length()>=1)
						{
							tab[i] += 1;
						}
					}
					line = reader.readNext();
				}
				
				reader.close();
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			
			return tab;
		}
		
		public String[] actualiseTableauMotArray(String[] tabMot,int[] tab)
		{
			String[] line = null;
			
			try{
				
				FileReader fr = new FileReader("csv.csv");
				CSVReader reader = new CSVReader(fr);
				
				line = reader.readNext();
				reader.close();
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
				
			
			for(int i=0;i<tab.length;i++)
			{
				if(tab[i]>=2)
				{
					if(line[i].split("_").length==1)
						tabMot[i] = "all";
					else
					{
						String[] transi = line[i].split("_");
						tabMot[i] = transi[transi.length-2];
					}
				}
			}
			return tabMot;
		}
		
		public void nom()
		{
			int nbColonne = nombreColonne();
			String[] motArray = new String[nbColonne];
			int[] estListe = new int[nbColonne];
			JSONObject all = new JSONObject();
			objets.add(all);
			
			int[] profondeur = new int[nombreElements()];
			
			for(int i=0;i<estListe.length;i++)
			{
				estListe[i] = 0;
			}
	
			estListe = actualiseTableauListe(estListe);
			motArray = actualiseTableauMotArray(motArray,estListe);

			
			for(int i=0;i<profondeur.length;i++)
				profondeur[i] = 1;
					
				BufferedReader reader;
				try{
					reader = new BufferedReader(new FileReader("conf.txt"));
					String line = reader.readLine();
					
					all = leNomDeLaFonction(reader, line,all,profondeur,estListe,motArray,false);
					System.out.println(all);
					
					reader.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
				
//			Utiliser ca obliger les include et donc impossible de accuulate mais put donc warning
		        try {
		        	FileWriter file = new FileWriter("employees.json");
		            file.write(all.toString(4));
		            file.flush();
		 
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			
		}
	
	
}
