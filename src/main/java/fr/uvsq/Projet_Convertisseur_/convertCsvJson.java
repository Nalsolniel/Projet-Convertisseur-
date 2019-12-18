package fr.uvsq.Projet_Convertisseur_;
import java.io.*;
import au.com.bytecode.opencsv.CSVReader;
import java.util.*;
//import org.json.simple.JSONObject;
import org.json.*;

public class convertCsvJson {
	
	List<String> list = new ArrayList<String>(); //stock le nom des objets json en cours d'utilisation
	List<JSONObject> objets = new ArrayList<JSONObject>();	//stock les objets json en cours d'utilisation
	int[] profondeurActuel = new int[nombreElements()]; //tableau contenant la profondeur actuelle de chaque element c'est à dire
														//dans quelle ligne du fichier .csv nous somme en train de lire
	
//		fonction qui va s'occupper de la gestion des listes afin de garder toujours le chemin absolu courant
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
		
//		Permet de savoir si la ligne lue dans le fichier de configuration est un objet ( : ) ou une valeur ( < )
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
		
		//renvoi un String contenant le premier mot de la ligne du fichier de configuration lue avec sa profondeur (les -)
		public String extractDataConfigWithDepth(String line)
		{
			String data = line.substring(0,line.indexOf(32));
			return data;			
		}
		//Renvoi un String contenant le premier mot de la ligne du fichier de configuration lue  sans sa profondeur
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
		//Renvoie l'integralite de la ligne du fichier de configuration lue sans la profondeur ( - )
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
		//Renvoie le chemin absolue du String en parametre c'est a dire la concatenation du pere racine et de ses fils jusqu'a cet element
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
	
		
/*		A SUPPRIMER
		
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
		} */
		
		//Retourne le nombre d'element/le nombre de colonne dasn le fichier .csv
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
		
		//Fonction qui renvoie true si le parametre data est present dans le tableau tab, renvoie false sinon
		public boolean estUneListe(String[] tab,String data)
		{
			for(int i=0;i<tab.length;i++)
				if(data.equals(tab[i]))
					return true;
			return false;
		}
		
		//Les deux fonctions suivantes retournent l'emplacement de la colonne du parametre data dans le fichier .csv
		//La seul difference est que dans l'une on initialise la lecture du fichier .csv dans l'autre on lui passe en parametre
		public int positionColonne(String[] ligne,String data)
		{
			int colonne = 0;
			
			for(int i=0;i<ligne.length;i++)
			{
				if(ligne[i].equals(data))
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
		
		
		public void actualiseTabProfondeurActuel(String[] tabOperation)
		{
			
			boolean[] estVerif = new boolean[nombreElements()];
			
			for(int cpt=0;cpt<estVerif.length;cpt++)
			{
				estVerif[cpt] = false;
			}
			
			CSVReader reader = null;
			for(int i=0;i<tabOperation.length;i+=2)
			{
				String value = concatString(tabOperation[i]);
				String[] ligne = null;
				int colonne = 0;
				
				try{
					
					FileReader fr = new FileReader("csv.csv");
					reader = new CSVReader(fr);
					
				 ligne = reader.readNext();
				 colonne = positionColonne(ligne,value);
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
				if(estVerif[colonne] == false )
				{
					estVerif[colonne] = true;
					profondeurActuel[colonne] += 1;
				}
			}
			try {
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
			
		}
				
		public JSONObject gereAccumulatePourListe(JSONObject pere,int[] profondeur,String data)
		{
			String value = concatString(data);		
			
			String[] ligne = null;
			
			try{
				
				FileReader fr = new FileReader("csv.csv");
				CSVReader reader = new CSVReader(fr);
				
				ligne = reader.readNext();
				
				int colonne = positionColonne(ligne,value); 
						
				ligne = reader.readNext();

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
			
		public JSONObject gereAccumulatePourListe(JSONObject pere,int[] profondeur,String data,String[] tabOperation)
		{
			String res = null;
//			String value = concatString(data);	
			String[] ligne = null;
			String[] tabOperationTransition = new String[tabOperation.length];
			for(int cpt=0;cpt<tabOperation.length;cpt++)
			{
				tabOperationTransition[cpt] = tabOperation[cpt];
			}
			
			for(int i=0;i<profondeur.length;i++)
			{
				profondeurActuel[i] = 1;
			}
			
			try{
				
				FileReader fr = new FileReader("csv.csv");
				CSVReader reader = new CSVReader(fr);
				
				ligne = reader.readNext();
				
				while((ligne = reader.readNext())!=null)
				{
					res = resOperation(tabOperationTransition,profondeur);

					pere.accumulate(data, res);
					
					for(int cpt=0;cpt<tabOperation.length;cpt++)
					{
						tabOperationTransition[cpt] = tabOperation[cpt];
					}
					actualiseTabProfondeurActuel(tabOperation);
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
	
		public String[] retourneOperandesEtOperation(String line)
		{
//			System.out.println(line.split(" ").length);
			String[] tab = new String[line.split(" ").length-2];
			
			boolean operandePresente = false;
			
			for(int i=0;i<line.length();i++)
			{
				if(line.charAt(i) == '+' || line.charAt(i) == '-' || line.charAt(i) == '*' || line.charAt(i) == '/' || line.charAt(i) == '|')
				{
					operandePresente = true;
				}
			}
			
			if(operandePresente)
			{
				
				String[] transi = line.split(" ");
				for(int i=0;i<tab.length;i++)
				{
					tab[i] = transi[i+2];
				}
			}
			
			
//			for(int i=0;i<tab.length;i++)
//			{
//				System.out.println(tab[i]);
//			}
			
			
			//la dans tab 0 et 2 on a les perandes il faut maintenant recuperer le chemin abolu gace a la list string puis de la
			//trouver la colonne dans le csv correspondant a ce nom et avec le numero de colonne lire la ligne appropriee 
			//on met ensuite les val dasn tab 0 et 2 et c'est presque gagne
			
			return tab;
		}
		
		public String operationConcatenation(String tab1,String tab2,int[] profondeur,int ope)
		{
			if(ope == 1)	
			{
				tab1 = extractDataCsvListeOperateur(profondeur,tab1);
			}
			tab2 = extractDataCsvListeOperateur(profondeur,tab2);
			return tab1+tab2;
		}
			
		public String operationAddition(String tab1,String tab2,int[] profondeur,int ope)
		{
			int res = 0;
			int val1 = 0;
			int val2 = 0;
			String sVal1 = tab1;
			String sVal2 = null;
			try{
				
				
				if(tab1.charAt(0) != '"' && tab1.charAt(tab1.length()-1) != '"')
				{
					if(ope == 1)
					{
						sVal1 = extractDataCsvListeOperateur(profondeur,tab1);
					}
					val1 = Integer.parseInt(sVal1);
				}
				else
				{
					sVal1 = tab1.substring(1,tab1.length()-1);
					val1 = Integer.parseInt(sVal1); 
				}
				if(tab2.charAt(0) != '"' && tab2.charAt(tab1.length()-1) != '"')
				{
					sVal2 = extractDataCsvListeOperateur(profondeur,tab2);
					val2 = Integer.parseInt(sVal2);
				}
				else
				{
					sVal2 = tab2.substring(1,tab2.length()-1);
					val2 = Integer.parseInt(sVal2);
				}
				
				
				res = val1 + val2;
			}
			catch (NumberFormatException e)
			{
				System.out.println("VALEUR NON NUMERQUE");
			}
			
			
			return String.valueOf(res);
		}
		
		public String operationSoustraction(String tab1,String tab2,int[] profondeur,int ope)
		{
			int res = 0;
			int val1 = 0;
			int val2 = 0;
			String sVal1 = tab1;
			String sVal2 = null;
			try{
				
				
				if(tab1.charAt(0) != '"' && tab1.charAt(tab1.length()-1) != '"')
				{
					if(ope == 1)
					{
						sVal1 = extractDataCsvListeOperateur(profondeur,tab1);
					}
					val1 = Integer.parseInt(sVal1);
				}
				else
				{
					sVal1 = tab1.substring(1,tab1.length()-1);
					val1 = Integer.parseInt(sVal1); 
				}
				if(tab2.charAt(0) != '"' && tab2.charAt(tab1.length()-1) != '"')
				{
					sVal2 = extractDataCsvListeOperateur(profondeur,tab2);
					val2 = Integer.parseInt(sVal2);
				}
				else
				{
					sVal2 = tab2.substring(1,tab2.length()-1);
					val2 = Integer.parseInt(sVal2);
				}
				
				
				res = val1 - val2;
			}
			catch (NumberFormatException e)
			{
				System.out.println("VALEUR NON NUMERQUE");
			}
			
			
			return String.valueOf(res);
		}
		
		public String operationMultiplication(String tab1,String tab2,int[] profondeur,int ope)
		{
			int res = 0;
			int val1 = 0;
			int val2 = 0;
			String sVal1 = tab1;
			String sVal2 = null;
			try{
				
				
				if(tab1.charAt(0) != '"' && tab1.charAt(tab1.length()-1) != '"')
				{
					if(ope == 1)
					{
						sVal1 = extractDataCsvListeOperateur(profondeur,tab1);
					}
					val1 = Integer.parseInt(sVal1);
				}
				else
				{
					sVal1 = tab1.substring(1,tab1.length()-1);
					val1 = Integer.parseInt(sVal1); 
				}
				if(tab2.charAt(0) != '"' && tab2.charAt(tab1.length()-1) != '"')
				{
					sVal2 = extractDataCsvListeOperateur(profondeur,tab2);
					val2 = Integer.parseInt(sVal2);
				}
				else
				{
					sVal2 = tab2.substring(1,tab2.length()-1);
					val2 = Integer.parseInt(sVal2);
				}
				
				
				res = val1 * val2;
			}
			catch (NumberFormatException e)
			{
				System.out.println("VALEUR NON NUMERQUE");
			}
			
			
			return String.valueOf(res);
		}
		
		public String operationDivision(String tab1,String tab2,int[] profondeur,int ope)
		{
			int res = 0;
			int val1 = 0;
			int val2 = 0;
			String sVal1 = tab1;
			String sVal2 = null;
			try{
				
				
				if(tab1.charAt(0) != '"' && tab1.charAt(tab1.length()-1) != '"')
				{
					if(ope == 1)
					{
						sVal1 = extractDataCsvListeOperateur(profondeur,tab1);
					}
					val1 = Integer.parseInt(sVal1);
				}
				else
				{
					sVal1 = tab1.substring(1,tab1.length()-1);
					val1 = Integer.parseInt(sVal1); 
				}
				if(tab2.charAt(0) != '"' && tab2.charAt(tab1.length()-1) != '"')
				{
					sVal2 = extractDataCsvListeOperateur(profondeur,tab2);
					val2 = Integer.parseInt(sVal2);
				}
				else
				{
					sVal2 = tab2.substring(1,tab2.length()-1);
					val2 = Integer.parseInt(sVal2);
				}
				
				
				try {
				res = val1 / val2;
				}
				catch(ArithmeticException e)
				{
					System.out.println("DIVISION PAR 0 IMPOSSIBLE");
				}
			}
			catch (NumberFormatException e)
			{
				System.out.println("VALEUR NON NUMERQUE");
			}
			
			
			return String.valueOf(res);
			
		}
		
		public String resOperation(String[] tabOperation,int[] profondeur)
		{
			int ope =0;
			for(ope=1;ope<tabOperation.length;ope=ope+2)
			{
				if(tabOperation[ope].charAt(0) == '+')
				{
					tabOperation[ope+1] = operationAddition(tabOperation[ope-1],tabOperation[ope+1],profondeur,ope);
				}
				if(tabOperation[ope].charAt(0) == '-')
				{
					tabOperation[ope+1] = operationSoustraction(tabOperation[ope-1],tabOperation[ope+1],profondeur,ope);
				}
				if(tabOperation[ope].charAt(0) == '*')
				{
					tabOperation[ope+1] = operationMultiplication(tabOperation[ope-1],tabOperation[ope+1],profondeur,ope);;;
				}
				if(tabOperation[ope].charAt(0) == '/')
				{
					tabOperation[ope+1] = operationDivision(tabOperation[ope-1],tabOperation[ope+1],profondeur,ope);
				}
				if(tabOperation[ope].charAt(0) == '|')
				{
					tabOperation[ope+1] = operationConcatenation(tabOperation[ope-1],tabOperation[ope+1],profondeur,ope);
				}
			}
			return tabOperation[ope-1];
		}
			
		public JSONObject traitement(BufferedReader reader,String line,JSONObject pere,int[] profondeur,int[] estListe,String[] motArray,boolean pereEstListe)
		{
			if(line == null)
				return pere;
			else {
				String type = typeLecture(line);
				String data = extractDataConfig(line);
			
				if(type == "value")
				{
					String[] tabOperation = retourneOperandesEtOperation(line);
					if(tabOperation.length > 1)
					{
						try{
							line = reader.readLine();
							}
						catch(IOException e)
						{
							e.printStackTrace();
						}
							pere = gereAccumulatePourListe(pere,estListe, data,tabOperation);
							traitement(reader,line,pere,profondeur,estListe,motArray,true);
						
					}
					else {
						pere.accumulate(data, extractDataCsv(profondeur,data));
						try{
							line = reader.readLine();
							}
						catch(IOException e)
						{
							e.printStackTrace();
						}
						
						if(pereEstListe == true || (estListe[positionColonne(data)]>1))
						{
							pere = gereAccumulatePourListe(pere,estListe, data);
							traitement(reader,line,pere,profondeur,estListe,motArray,true);
						}
						else
						{							
							traitement(reader,line,pere,profondeur,estListe,motArray,false);
						}
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
						objets.get(objets.size()-2).accumulate(data, traitement(reader,lineTransi,objets.get(objets.size()-1),profondeur,estListe,motArray,true));
					}
					else
					{
						objets.get(objets.size()-2).accumulate(data, traitement(reader,lineTransi,objets.get(objets.size()-1),profondeur,estListe,motArray,false));
					}
				}
			}
			return pere;
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
		
		public void initialisation()
		{
			int nbColonne = nombreElements();
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
					
					all = traitement(reader, line,all,profondeur,estListe,motArray,false);
					System.out.println(all);
					
					reader.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
		        try {
		        	FileWriter file = new FileWriter("employees.json");
		            file.write(all.toString(4));
		            file.flush();
		            file.close();
		            
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			
		}
		
		
		public String extractDataCsvListeOperateur(int[] profondeur,String data)
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
					{
						colonne = i;
					}
				}	
				
				for(int i=1;i<=profondeurActuel[colonne];i++)
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
					{
						colonne = i;
					}
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
	
	
}
