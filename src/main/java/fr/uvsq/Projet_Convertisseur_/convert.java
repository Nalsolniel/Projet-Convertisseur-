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
					ligne1.add(ltemp[0].replaceAll("-", ""));
				}
				else
				{
					ligne1.add(res + "_" + ltemp[0].replaceAll("-", ""));
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
		
		// création de la première ligne
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
				System.out.println(line[i]+ "\n\n");
			}
			csv.writeNext(line);
			
			ArrayList<String[]> obj = new ArrayList<String[]>();
			obj.add(line);
			obj.add(new String[ligne1.size()]);
			
			parcours(r.getJASON(),obj);
			
			csv.writeNext(obj.get(1));;
			csv.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	} 
	
	private static void suiv(JSONArray j, ArrayList<String[]> obj)
	{
		int length = j.length();
		int i;
		for(i=0; i<length; i++)
		{
			JSONObject tmp = (JSONObject) j.get(i);
			parcours(tmp, obj);
		}
				
	}
	
	private static void parcours(JSONObject j, ArrayList<String[]> obj)
	{
		Set<String> s = j.keySet();
		
		for(String val : s)
		{
			if(j.get(val) instanceof JSONArray)
			{
				suiv(j.getJSONArray(val),obj);
			}
			else if(j.get(val) instanceof JSONObject)
			{
				parcours(j.getJSONObject(val),obj);
			}
			else
			{
				addValue(j,obj,val);
			}
		}
	}
	
	private static void addValue(JSONObject j, ArrayList<String[]> obj, String val)
	{
		int i = 0;
		String[] temp = obj.get(0)[0].split("_");
		String colonne = temp[temp.length-1];
		while(!(val.equals(colonne)) && i<obj.get(1).length-1)
		{
			i++;
			temp = obj.get(0)[i].split("_");
			colonne = temp[temp.length-1];
			System.out.println(colonne + " " + val);
		}
		System.out.println("ok " + colonne + " " + val + "\n");
		obj.get(1)[i] = j.getString(val);
	}
}