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
	
	private  ArrayList<String> genHead(ArrayList<String> l)
	{
		String[] ltemp;
		Stack<String> in = new Stack<String>();
		ArrayList<String> ligne1 = new ArrayList<String>();
		int NBin,i,j;
		String res;
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
	
	public convert_JSON_CSV(JSONreader r)
	{
		ArrayList<String> ligne1 = createLigne1(r);
		File f = new File("retour.csv");
		try 
		{
			f.createNewFile();
			FileWriter write = new FileWriter(f);
			CSVWriter csv = new CSVWriter(write,CSVWriter.DEFAULT_SEPARATOR,CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
			int i;
			
			String[] line = supIndent(ligne1);
			
			obj = new ArrayList<String[]>();
			obj.add(line);
			Stack<String> pathObj = new Stack<String>();
			parcours(r.getJASON(),pathObj,1);
			
			int j;
			for(i=0;i<obj.size();i++)
			{
				for(j=0;j<obj.get(i).length;j++)
				{
					System.out.println(". " +obj.get(i)[j]);
				}
			}
			CSV = modify();

			i = 0;
			while(i<CSV.size()-1)
			{
				csv.writeNext(CSV.get(i));
				i++;
			}
			csv.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	} 
	
	private ArrayList<String[]> modify()
	{
		BufferedReader file;
		ArrayList<String> l = new ArrayList<String>();
		CSV = new ArrayList<String[]>();
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
		
		ArrayList<String> ligne1 = genHead(l);
		Stack<String> chemin = new Stack<String>();
		String[] line = supIndent(ligne1);
		CSV.add(line);
		while(CSV.size() <= obj.size())
		{
			CSV.add(new String[line.length]);
		}
		createCSV(chemin, l);
		return CSV;
	}
	
	private void createCSV(Stack<String> chemin, ArrayList<String> l)
	{
		int i,length,j,pop,k;
		int cont = 0;
		
		for(i=0;i<l.size();i++)
		{
			length = l.get(i).split(" ").length;
			if(length == 2)
			{
				chemin.add(l.get(i).split(" ")[0]);
			}
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
					chemin.pop();
				}
				//3ieme mot : ajout de la première valeur a la colonne
				concat(chemin,l.get(i).split(" ")[2],l.get(i).split(" ")[0],true);
				
				//aplication des operateurs 
				for(k=3;k<l.get(i).split(" ").length;k=k+2)
				{
					operator(chemin,l,k,i);
				}
			}
			cont = 0;
		}
	}
	
	private void operator(Stack<String> chemin, ArrayList<String> l, int nbOp, int ligne)
	{
		try 
		{
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
			else if(l.get(ligne).split(" ")[nbOp].equals("+"))
			{
				add(chemin,l.get(ligne).split(" ")[0],l.get(ligne).split(" ")[nbOp+1]);
			}
			else if(l.get(ligne).split(" ")[nbOp].equals("-"))
			{
				supp(chemin,l.get(ligne).split(" ")[0],l.get(ligne).split(" ")[nbOp+1]);
			}
			else if(l.get(ligne).split(" ")[nbOp].equals("/"))
			{
				div(chemin,l.get(ligne).split(" ")[0],l.get(ligne).split(" ")[nbOp+1]);
			}
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
	
	private void add(Stack<String> chemin, String val, String add)
	{
		int colonne = getColonne(chemin,val);
		int colonneCVS = getColonne(chemin,add);
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
	
	private void supp(Stack<String> chemin, String val, String add)
	{
		int colonne = getColonne(chemin,val);
		int colonneCVS = getColonne(chemin,add);
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
	
	private void mult(Stack<String> chemin, String val, String add)
	{
		int colonne = getColonne(chemin,val);
		int colonneCVS = getColonne(chemin,add);
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
	
	private void div(Stack<String> chemin, String val, String add)
	{
		int colonne = getColonne(chemin,val);
		int colonneCVS = getColonne(chemin,add);
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
	
	private int getColonne(Stack<String> chemin, String val)
	{
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
		for(i=0;i<obj.get(0).length;i++)
		{
			if(tmp.equals(obj.get(0)[i]))
			{
				colonne = i;
			}
		}
		return colonne;
	}
	
	private void concat(Stack<String> chemin, String val, String add)
	{
		int colonneCVS = getColonne(chemin,val);
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
	
	private void concat(Stack<String> chemin, String val, String val2, Boolean premier) 
	{
		int colonne = getColonne(chemin,val);
		int colonneCVS = getColonne(chemin,val2);
		int i;
		if(colonne != -1 && colonneCVS != -1)
		{
			for(i=1;i<obj.size();i++) 
			{
				if(obj.get(i)[colonne] != null)
				{
					if(CSV.get(i)[colonneCVS] != null )
					{
						CSV.get(i)[colonneCVS] = CSV.get(i)[colonneCVS] + obj.get(i)[colonne];System.out.println("concat : " + CSV.get(i)[colonneCVS]);
					}
					else
					{
						CSV.get(i)[colonneCVS] = obj.get(i)[colonne];
					}
				}
			}
		}
	}

	private void suiv(JSONArray j, Stack<String> pathObj,int ligne,int liste)
	{
		int length = j.length();
		int i,k = 0,doublon = -1;
		int nb[] = new int[length];
		ArrayList<JSONObject> dejaVu = new ArrayList<JSONObject>();
		for(i=0; i<length; i++)
		{
			if(j.get(i) instanceof JSONObject) 
			{
				JSONObject tmp = (JSONObject) j.get(i);
				while(k<dejaVu.size() && doublon == -1)
				{
					if(tmp.keySet().containsAll(dejaVu.get(k).keySet()) || dejaVu.get(k).keySet().containsAll(tmp.keySet())) 
					{
						doublon = k;
					}
					else
					{
						if(tmp.keySet().size()>dejaVu.get(k).keySet().size())
						{
							dejaVu.set(k, tmp);
						}
					}
					k++;
				}
				if(doublon == -1)
				{
					dejaVu.add(tmp);
					nb[k] = 0;
					parcours(tmp,pathObj,ligne + nb[k]);
				}
				else
				{
					nb[doublon] = nb[doublon] + 1;
					parcours(tmp,pathObj,ligne + nb[doublon]);
				}
				k = 0;
				doublon = -1;
			}
			else if(j.get(i) instanceof JSONArray)
			{
				System.out.println(i+" :");
				JSONArray tmp = j.getJSONArray(i);//System.out.println(j.get(i));
				suiv(tmp, pathObj, doublon,i);
			}
			else if(j.get(i) instanceof String)
			{
				pathObj.add("" + liste);
				pathObj.add("" + i);
				//System.out.println("ok " + j.get(i) + " " +pathObj+ " " + i +" "+liste);
				addValue((String) j.get(i),pathObj,i );
				pathObj.pop();
				pathObj.pop();
			}
			else
			{
				System.out.println("++++++++++" +	j.get(i));
			}
		}		
	}
	
	private void parcours(JSONObject j, Stack<String> pathObj, int ligne)
	{
		Set<String> s = j.keySet();
		Stack<String> newpathObj = new Stack<String>();
		newpathObj.addAll(pathObj);
		for(String val : s)
		{
			newpathObj.add(val);
			if(j.get(val) instanceof JSONArray)
			{
				suiv(j.getJSONArray(val),newpathObj,ligne,0);
			}
			else if(j.get(val) instanceof JSONObject)
			{
				parcours(j.getJSONObject(val),newpathObj,ligne);
			}
			else
			{
				addValue(j,newpathObj,ligne);
			}
			newpathObj.pop();
		}
	}
	
	private void addValue(Object j, Stack<String> pathObj, int ligne)
	{
		//System.out.println(j +" " +  pathObj);
		int i = 0;
		int res = -1, noMatch = 0;
		int k;
		String[] temp = null;
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
				if(noMatch == 0)
				{
					res = i;
				}
				noMatch = 0;
			}
			i++; 
		}
		while(obj.size()<ligne+1)
		{
			obj.add(new String[obj.get(0).length]);
		}
		if(res != -1)
		{//System.out.println("match");
			if(j instanceof JSONObject)
			{
				JSONObject ret = (JSONObject) j;
				obj.get(ligne)[res] = ret.getString(pathObj.get(pathObj.size()-1));
			}
			else if(j instanceof String)
			{
				String ret = (String) j;
				obj.get(ligne)[res] = ret;
				//System.out.println("verif "+ret + " "+ obj.get(ligne)[res]);
			}
		}
	}
}