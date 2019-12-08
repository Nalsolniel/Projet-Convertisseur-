package fr.uvsq.Projet_Convertisseur_;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import au.com.bytecode.opencsv.CSVWriter;

public class convert  
{
	private static ArrayList<String> createLigne1(int cont, ArrayList<String> l)
	{
		ArrayList<String> ligne1 = new ArrayList<String>();
		int i,j;
		String[] ltemp;
		Stack<String> in = new Stack<String>(); 
		int NBin;
		String res;
		System.out.println("\n\n");
		
		for(i=0; i<cont; i++)
		{
			res = "";
			ltemp = l.get(i).split(" ");
			if(ltemp.length == 2)
			{
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
	
	public static void convert_JSON_CSV(JSONreader r)
	{
		BufferedReader file;
		ArrayList<String> l = new ArrayList<String>();
		int cont = 0;
		try 
		{
			file = new BufferedReader(new FileReader("conf.txt"));
			String tmp = file.readLine();
			while(tmp != null)
			{
				cont++;
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
		
		ArrayList<String> ligne1 = createLigne1(cont,l);
		System.out.println(ligne1);
		
		File f = new File("retour.csv");
		try 
		{
			f.createNewFile();
			FileWriter write = new FileWriter(f);
			CSVWriter csv = new CSVWriter(write,CSVWriter.DEFAULT_SEPARATOR,CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
			String[] line = new String[(ligne1.size())];
			int i;
			for(i=0;i<ligne1.size();i++)
			{
				line[i] = ligne1.get(i);
				line[i] = line[i].replaceAll("-", "");
				System.out.println(line[i]+ "\n--\n");
			}
			
			ArrayList<String[]> obj = new ArrayList<String[]>();
			obj.add(line);
			//obj.add(new String[ligne1.size()]);
		
			Stack<String> pathObj = new Stack<String>();
			parcours(r.getJASON(),obj,pathObj,1);

			for(i=0;i<obj.get(1).length;i++)
			{
				System.out.println(obj.get(1)[i]);
			}
			
			i = 0;
			while(i<obj.size())
			{
				csv.writeNext(obj.get(i));
				i++;
			}
			csv.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	} 
	
	private static void suiv(JSONArray j, ArrayList<String[]> obj, Stack<String> pathObj,int ligne)
	{
		int length = j.length();
		int i,k = 0,doublon = -1;
		int nb[] = new int[length];
		ArrayList<JSONObject> dejaVu = new ArrayList<JSONObject>();
		for(i=0; i<length; i++)
		{
			JSONObject tmp = (JSONObject) j.get(i);
			while(k<dejaVu.size() && doublon == -1)
			{
				if(dejaVu.get(k).keySet().equals(tmp.keySet()))
				{
					doublon = k;
					System.out.println("doublon " + k);
				}
				k++;
			}
			if(doublon == -1)
			{
				dejaVu.add(tmp);
				System.out.println("na "+k);
				nb[k] = 0;
				parcours(tmp, obj,pathObj,ligne + nb[k]);
			}
			else
			{
				nb[doublon] = nb[doublon] + 1;
				parcours(tmp, obj,pathObj,ligne + nb[doublon]);
				System.out.println("ne " + ligne + " " +  nb[doublon]);
			}
			k = 0;
			doublon = -1;
		}
				
	}
	
	private static void parcours(JSONObject j, ArrayList<String[]> obj, Stack<String> pathObj, int ligne)
	{
		Set<String> s = j.keySet();
		Stack<String> newpathObj = new Stack<String>();
		newpathObj.addAll(pathObj);
		System.out.println("ok");
		for(String val : s)
		{
			newpathObj.add(val);
			System.out.println(newpathObj);
			if(j.get(val) instanceof JSONArray)
			{
				suiv(j.getJSONArray(val),obj,newpathObj,ligne);
			}
			else if(j.get(val) instanceof JSONObject)
			{
				parcours(j.getJSONObject(val),obj,newpathObj,ligne);
			}
			else
			{
				addValue(j,obj,newpathObj,ligne);
			}
			newpathObj.pop();
		}
	}
	
	private static void addValue(JSONObject j, ArrayList<String[]> obj, Stack<String> pathObj, int ligne)
	{
		int i = 0;
		int res = -1, noMatch = 0;
		int k;
		String[] temp = null;
		System.out.println("in");
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
		}System.out.println("1");
		while(obj.size()<ligne+1)
		{
			obj.add(new String[obj.get(0).length]);System.out.println("create");
		}System.out.println("2");
		obj.get(ligne)[res] = j.getString(pathObj.get(pathObj.size()-1));
	}
}