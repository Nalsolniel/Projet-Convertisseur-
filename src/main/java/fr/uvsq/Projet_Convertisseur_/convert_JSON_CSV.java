package fr.uvsq.Projet_Convertisseur_;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;
import org.json.JSONArray;
import org.json.JSONObject;

import au.com.bytecode.opencsv.CSVWriter;

public class convert_JSON_CSV  
{
	ArrayList<String[]> obj;
	ArrayList<String[]> CSV;
	
	/*constructeur de la classe convert_JSON_CSV
	 * prend en paramètre un JSONreader
	 * la création de l'objet produit la traduction du fichier JSON en un fichier CSV*/
	public convert_JSON_CSV(JSONreader r)
	{
		ArrayList<String> ligne1 = createLigne1(r);
		File f = new File("retour.csv");
		try 
		{
			//initialisation du CSVwriter
			f.createNewFile();
			FileWriter write = new FileWriter(f);
			CSVWriter csv = new CSVWriter(write,CSVWriter.DEFAULT_SEPARATOR,CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
			int i;
			
			//création du nom des colonnes du fichier temporaire
			String[] line = supIndent(ligne1);
			
			//création du fichier csv temporaire et initialisation des colonnes 
			obj = new ArrayList<String[]>();
			obj.add(line);
			Stack<String> pathObj = new Stack<String>();
			parcours(r.getJASON(),pathObj,1);
			
			//application des modification apporté le fichier de configuration
			CSV = modify();
			
			//écriture du fichier csv
			i = 0;
			int j,test;
			while(i<CSV.size())
			{
				test = 0;
				for(j=0;j<CSV.get(0).length;j++)
				{
					if(CSV.get(i)[j] != null)
					{
						test = 1;
					}
				}
				if(test == 1)
				{
					csv.writeNext(CSV.get(i));
				}
				test = 0;
				i++;
			}
			csv.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	} 
	
	/*génération de la première ligne du fichier csv a partir d'un String qui contient les donées du fichiers de configurations*/
	private  ArrayList<String> genHead(ArrayList<String> l)
	{
		String[] ltemp;
		Stack<String> in = new Stack<String>();
		ArrayList<String> ligne1 = new ArrayList<String>();
		int NBin,i,j;
		String res;
		//parcours du fichier de configurations et génération de la 1ere ligne
		for(i=0; i<l.size(); i++)
		{
			res = "";
			ltemp = l.get(i).split(" ");
			if(ltemp.length == 2)
			{
				NBin = 0;
				j = 0;
				while(ltemp[0].charAt(j) == '-') 
				{
					NBin++;
					j++;
				}
				j=0;
				while(NBin < in.size())
				{
					in.pop();
				}
				in.add(ltemp[0]);
			}
			else
			{
				NBin = 0;
				j = 0;
				while(ltemp[0].charAt(j) == '-') 
				{
					NBin++;
					j++;
				}
				j=0;
				while(NBin < in.size())
				{
					in.pop();
				}
				for(j=0;j<NBin;j++)
				{
					if(res.isEmpty())
					{
						res = in.get(j);
					}
					else
					{
						res = res + "_" + in.get(j);
					}
				}
				if(res.isEmpty())
				{
					ligne1.add(ltemp[0]);
				}
				else
				{
					ligne1.add(res + "_" + ltemp[0]);
				}
			}
		}
		return ligne1;
	}
	/*création du tableau de String corespondant a la 1ere ligne d'un fichier csv*/
	private ArrayList<String> createLigne1(JSONreader r)
	{
		ArrayList<String> l = new ArrayList<String>();
		int i;
		
		String res = "";
		String conf = r.genConfString(r.getJASON(),0,res);
		String TabRes[] = conf.split("\n");
		for(i=0;i<TabRes.length;i++)
		{
			l.add(TabRes[i]);
		}
		ArrayList<String> ligne1 = genHead(l);
		return ligne1;
	}
	
	/*supréssion de l'indentation du fichier de configuration ( caractère '-') */
	private static String[] supIndent(ArrayList<String> ligne1)
	{
		int i;
		String[] line = new String[(ligne1.size())];
		for(i=0;i<ligne1.size();i++)
		{
			line[i] = ligne1.get(i);
			line[i] = line[i].replaceAll("-", "");
		}
		return line;
	}
	
	/*fonction qui a partir d'une modélisation d'un fichier csv et d'un fichier de configuration une nouvelle 
	 *représentation d'un fichier csv ayant subi des modification */
	private ArrayList<String[]> modify()
	{
		BufferedReader file;
		ArrayList<String> l = new ArrayList<String>();
		CSV = new ArrayList<String[]>();
		//lecture du fichier de configuration 
		try 
		{
			file = new BufferedReader(new FileReader("conf.txt"));
			String tmp = file.readLine();
			while(tmp != null)
			{
				l.add(tmp);
				tmp = file.readLine();
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
		
		//création de la ligne d'entête
		ArrayList<String> ligne1 = genHead(l);
		Stack<String> chemin = new Stack<String>();
		String[] line = supIndent(ligne1);
		//initialisation des lignes du fichier csv
		CSV.add(line);
		while(CSV.size() <= obj.size())
		{
			CSV.add(new String[line.length]);
		}
		//opération pour optenir les modification de la configuration
		createCSV(chemin, l);
		return CSV;
	}
	
	/*fonction qui affecte les valeurs et aplique les modifications sur le fichier csv*/
	private void createCSV(Stack<String> chemin, ArrayList<String> l)
	{
		int i,length,j,pop,k;
		int cont = 0;
		//pour toutes les colonnes 
		for(i=0;i<l.size();i++)
		{
			length = l.get(i).split(" ").length;
			//entrée dans une profondeur d'objet suplémentaire 
			if(length == 2)
			{
				j = 0;
				while(l.get(i).split(" ")[0].charAt(j) == '-')
				{
					cont++;j++;
				}
				pop = chemin.size()-cont;
				for(j=0;j<(pop);j++)
				{
					System.out.println("pop "+chemin.pop());

				}
				chemin.add(l.get(i).split(" ")[0]);
			}
			//affectation d'une valeurs et de ses opérations
			else
			{
				j = 0;
				while(l.get(i).split(" ")[0].charAt(j) == '-')
				{
					cont++;j++;
				}
				pop = chemin.size()-cont;
				for(j=0;j<(pop);j++)
				{
					System.out.println("pop "+chemin.pop());

				}
				//ajout de la première valeur dans la colonne
				concat(chemin,l.get(i).split(" ")[2],l.get(i).split(" ")[0],true);
				System.out.println(chemin);
				
				//aplication des operateurs 
				for(k=3;k<l.get(i).split(" ").length;k=k+2)
				{
					System.out.println("                               ok");
					operator(chemin,l,k,i);
				}
			}
			cont = 0;
		}
	}
	
	/*fonction qui applique une opération sur une colonnes de cvs*/
	private void operator(Stack<String> chemin, ArrayList<String> l, int nbOp, int ligne)
	{
		try 
		{
			//concaténation
			if(l.get(ligne).split(" ")[nbOp].equals("|"))
			{
				if(l.get(ligne).split(" ")[nbOp+1].charAt(0) != '"')
				{
					concat(chemin, l.get(ligne).split(" ")[nbOp+1], l.get(ligne).split(" ")[0], false);
				}
				else
				{
					concat(chemin,l.get(ligne).split(" ")[0],l.get(ligne).split(" ")[nbOp+1]);
				}
			}
			//addition 
			else if(l.get(ligne).split(" ")[nbOp].equals("+"))
			{
				add(chemin,l.get(ligne).split(" ")[0],l.get(ligne).split(" ")[nbOp+1]);
			}
			//supression
			else if(l.get(ligne).split(" ")[nbOp].equals("-"))
			{
				supp(chemin,l.get(ligne).split(" ")[0],l.get(ligne).split(" ")[nbOp+1]);
			}
			//division
			else if(l.get(ligne).split(" ")[nbOp].equals("/"))
			{
				div(chemin,l.get(ligne).split(" ")[0],l.get(ligne).split(" ")[nbOp+1]);
			}
			//multiplication
			else if(l.get(ligne).split(" ")[nbOp].equals("*"))
			{
				mult(chemin,l.get(ligne).split(" ")[0],l.get(ligne).split(" ")[nbOp+1]);
			}
			else
			{
				System.out.println("exception : operateur inexistant");
			}
		}
		catch (NumberFormatException nfe) 
		{
			System.out.println("la colonne " + "n'est pas composé d'entier" );
		}
	}
	
	/*aplication de l'addition sur une colonne
	 * return l'exception NumberFormatException si l'une des deux opérande ne sont pas des entiers*/
	private void add(Stack<String> chemin, String val, String add)
	{
		int colonne = getColonne(chemin,val,0);
		int colonneCVS = getColonne(chemin,add,1);
		int i,valu;
		for(i=1;i<obj.size();i++)
		{
			if(obj.get(i)[colonne] != null && CSV.get(i)[colonneCVS] != null)
			{
				valu = Integer.parseInt(obj.get(i)[colonne]) + Integer.parseInt(obj.get(i)[colonne]);
				CSV.get(i)[colonneCVS] = "" + valu;
			}
		}
	}
	
	/*aplication de la supression sur une colonne
	 * return l'exception NumberFormatException si l'une des deux opérande ne sont pas des entiers*/
	private void supp(Stack<String> chemin, String val, String add)
	{
		int colonne = getColonne(chemin,val,0);
		int colonneCVS = getColonne(chemin,add,1);
		int i,valu;
		for(i=1;i<obj.size();i++)
		{
			if(obj.get(i)[colonne] != null && CSV.get(i)[colonneCVS] != null)
			{
				valu = Integer.parseInt(obj.get(i)[colonne]) - Integer.parseInt(obj.get(i)[colonne]);
				CSV.get(i)[colonneCVS] = "" + valu;
			}
		}
	}
	
	/*aplication de la multiplication sur une colonne
	 * return l'exception NumberFormatException si l'une des deux opérande ne sont pas des entiers*/
	private void mult(Stack<String> chemin, String val, String add)
	{
		int colonne = getColonne(chemin,val,0);
		int colonneCVS = getColonne(chemin,add,1);
		int i,valu;
		for(i=1;i<obj.size();i++)
		{
			if(obj.get(i)[colonne] != null && CSV.get(i)[colonneCVS] != null)
			{
				valu = Integer.parseInt(obj.get(i)[colonne]) * Integer.parseInt(obj.get(i)[colonne]);
				CSV.get(i)[colonneCVS] = "" + valu;
			}
		}
	}
	
	/*aplication de la division sur une colonne
	 * return l'exception NumberFormatException si l'une des deux opérande ne sont pas des entiers*/
	private void div(Stack<String> chemin, String val, String add)
	{
		int colonne = getColonne(chemin,val,0);
		int colonneCVS = getColonne(chemin,add,1);
		int i,valu;
		for(i=1;i<obj.size();i++)
		{
			if(obj.get(i)[colonne] != null && CSV.get(i)[colonneCVS] != null)
			{
				valu = Integer.parseInt(obj.get(i)[colonne]) / Integer.parseInt(obj.get(i)[colonne]);
				CSV.get(i)[colonneCVS] = "" + valu;
			}
		}
	}
	
	/*obtention du numéro de la colonne du modélisation d'un fichier csv*/
	private int getColonne(Stack<String> chemin, String val ,int elem)
	{
		ArrayList<String[]> op;
		if(elem == 0)
		{
			op = obj;
		}
		else
		{
			op = CSV;
		}
		String tmp = "";
		int i;
		int colonne = -1;
		if(!chemin.empty())
		{
			tmp = chemin.get(0);
		}
		for(i=1;i<chemin.size();i++)
		{
			tmp = tmp + "_" + chemin.get(i);
		}
		tmp = tmp + "_" + val;
		tmp = tmp.replaceAll("-", "");
		if(tmp.charAt(0)=='_')
		{
			tmp = tmp.replaceFirst("_", "");
		}
		for(i=0;i<op.get(0).length;i++)
		{
			if(tmp.equals(op.get(0)[i]))
			{
				colonne = i;
			}
		}
		return colonne;
	}
	
	/*concaténaton entre une colonne et un String */
	private void concat(Stack<String> chemin, String val, String add)
	{
		int colonneCVS = getColonne(chemin,val,1);
		int i;
		if(colonneCVS != -1)
		{
			for(i=1;i<CSV.size();i++)
			{
				if(CSV.get(i)[colonneCVS] != null)
				{
					CSV.get(i)[colonneCVS] = CSV.get(i)[colonneCVS] + add;
					CSV.get(i)[colonneCVS] = CSV.get(i)[colonneCVS].replaceAll("\"", "");
				}
			}
		}
	}
	
	/*concaténation de deux colonnes*/
	private void concat(Stack<String> chemin, String val, String val2, Boolean premier) 
	{
		int colonne = getColonne(chemin,val,0);
		int colonneCVS = getColonne(chemin,val2,1);
		int i;
		if(colonne != -1 && colonneCVS != -1)
		{
			for(i=1;i<obj.size();i++) 
			{
				if(obj.get(i)[colonne] != null)
				{
					if(CSV.get(i)[colonneCVS] != null )
					{
						CSV.get(i)[colonneCVS] = CSV.get(i)[colonneCVS] + obj.get(i)[colonne];
					}
					else
					{
						CSV.get(i)[colonneCVS] = obj.get(i)[colonne];
					}
				}
			}
		}
	}
	
	/*parcours d'une JSONArray pour écrire la modélisation d'un fichier csv*/
	private void suiv(JSONArray j, Stack<String> pathObj,int ligne,int liste)
	{
		int length = j.length();
		int i,k = 0,doublon = -1;
		int nb[] = new int[length];
		ArrayList<JSONObject> dejaVu = new ArrayList<JSONObject>();
		for(i=0; i<length; i++)
		{
			//evaluation d'un objet JSON
			if(j.get(i) instanceof JSONObject) 
			{
				JSONObject tmp = (JSONObject) j.get(i);
				while(k<dejaVu.size() && doublon == -1)
				{
					//recherche d'objet en doublons
					if(tmp.keySet().containsAll(dejaVu.get(k).keySet()) || dejaVu.get(k).keySet().containsAll(tmp.keySet())) 
					{
						doublon = k;
					}
					else
					{
						if(tmp.keySet().size()>dejaVu.get(k).keySet().size())//ajout a la liste des doublons 
						{
							dejaVu.set(k, tmp);
						}
					}
					k++;
				}
				if(doublon == -1)//si l'objet n'existe pas en double
				{
					dejaVu.add(tmp);
					nb[k] = 0;
					parcours(tmp,pathObj,ligne + nb[k]);
				}
				else//si un objet du même type a déja été évalué
				{
					nb[doublon] = nb[doublon] + 1;
					parcours(tmp,pathObj,ligne + nb[doublon]);
				}
				k = 0;
				doublon = -1;
			}
			//évaluation d'un JSONArray
			else if(j.get(i) instanceof JSONArray)
			{
				pathObj.add(""+i);
				suiv(j.getJSONArray(i), pathObj, ligne, liste);
				pathObj.pop();
			}
			//évaluation d'un String
			else if(j.get(i) instanceof String)
			{
				pathObj.add("" + liste);
				addValue((String) j.get(i),pathObj,i+1);
				pathObj.pop();
			}
		}		
	}
	/*parcours d'un objet JSON*/
	private void parcours(JSONObject j, Stack<String> pathObj, int ligne)
	{
		Set<String> s = j.keySet();
		Stack<String> newpathObj = new Stack<String>();
		newpathObj.addAll(pathObj);
		for(String val : s)
		{
			newpathObj.add(val);
			//évaluation d'un objet JSON
			if(j.get(val) instanceof JSONArray)
			{
				suiv(j.getJSONArray(val),newpathObj,ligne,0);
			}
			//évaluation d'une JSONArray
			else if(j.get(val) instanceof JSONObject)
			{
				parcours(j.getJSONObject(val),newpathObj,ligne);
			}
			//évaluation d'une valeurs (int,String,bool)
			else
			{
				addValue(j,newpathObj,ligne);
			}
			newpathObj.pop();
		}
	}
	/*fonction qui ajoute une valeurs dans la représentation temporaire du fichiers csv (obj)*/
	private void addValue(Object j, Stack<String> pathObj, int ligne)
	{
		int i = 0;
		int res = -1, noMatch = 0;
		int k;
		String[] temp = null;
		//parcours de tous les noms de colonnes possible pour trouver l'emplacement d'ajout de la valeur
		while(i<obj.get(0).length && res == -1)
		{
			temp = obj.get(0)[i].split("_");
			if(pathObj.size() == temp.length)
			{
				for(k=0;k<temp.length;k++)
				{
					if(!(temp[k].equals(pathObj.get(k))))
					{
						noMatch = 1;
					}
				}
				if(noMatch == 0)//si l'intégralité des termes composant les colonnes sont identique
				{
					res = i;
				}
				noMatch = 0;
			}
			i++; 
		}
		//création de colonnes potentiellement manquante
		while(obj.size()<ligne+1)
		{
			obj.add(new String[obj.get(0).length]);
		}
		if(res != -1)
		{
			//ajout d'un JSONObject 
			if(j instanceof JSONObject)
			{
				JSONObject ret = (JSONObject) j;
				obj.get(ligne)[res] = ret.getString(pathObj.get(pathObj.size()-1));
			}
			//ajout d'un String
			else if(j instanceof String)
			{
				String ret = (String) j;
				obj.get(ligne)[res] = ret;
			}
			//ajout d'un entier
			else if(j instanceof Integer)
			{
				Integer ret = (Integer) j;
				obj.get(ligne)[res] = "" + ret;
			}
		}
	}
}