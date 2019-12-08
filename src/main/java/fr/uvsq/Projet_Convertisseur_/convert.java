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
				line[i] = line[i].replaceAll("-", "");
				System.out.println(line[i]+ "\n--\n");
			}
			csv.writeNext(line);
			
			ArrayList<String[]> obj = new ArrayList<String[]>();
			obj.add(line);
			obj.add(new String[ligne1.size()]);
			
			Stack<String> pathObj = new Stack<String>();
			parcours(r.getJASON(),obj,pathObj);

			for(i=0;i<obj.get(1).length;i++)
			{
				System.out.println(obj.get(1)[i]);
			}
			
			csv.writeNext(obj.get(1));;
			csv.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	} 
	
	private static void suiv(JSONArray j, ArrayList<String[]> obj, Stack<String> pathObj)
	{
		int length = j.length();
		int i;
		for(i=0; i<length; i++)
		{
			JSONObject tmp = (JSONObject) j.get(i);
			parcours(tmp, obj,pathObj);
		}
				
	}
	
	private static void parcours(JSONObject j, ArrayList<String[]> obj, Stack<String> pathObj)
	{
		Set<String> s = j.keySet();
		Stack<String> newpathObj = new Stack<String>();
		newpathObj.addAll(pathObj);

		for(String val : s)
		{
			newpathObj.add(val);
			System.out.println(newpathObj);
			if(j.get(val) instanceof JSONArray)
			{
				suiv(j.getJSONArray(val),obj,newpathObj);
			}
			else if(j.get(val) instanceof JSONObject)
			{
				parcours(j.getJSONObject(val),obj,newpathObj);
			}
			else
			{
				addValue(j,obj,newpathObj);
			}
			newpathObj.pop();
		}
	}
	
	private static void addValue(JSONObject j, ArrayList<String[]> obj, Stack<String> pathObj)
	{
		int i = 0;
		int res = -1, noMatch = 0;
		int k;
		String[] temp = null;
		
		while(i<obj.get(0).length && res == -1)
		{
			temp = obj.get(0)[i].split("_");
			if(pathObj.size() == temp.length)
			{
				System.out.println("match size"+ pathObj.size() + temp.length);
				for(k=0;k<temp.length;k++)
				{
					System.out.println(temp[k]+" "+pathObj.get(k));
					if(!(temp[k].equals(pathObj.get(k))))
					{
						System.out.println("err "+temp[k]+" "+pathObj.get(k));
						noMatch = 1;
					}
					
				}
				if(noMatch == 0)
				{
					System.out.println("match okok");
					res = i;
					System.out.println(res);
				}
				noMatch = 0;
			}
			i++; 
		}
		System.out.println(res);
		obj.get(1)[res] = j.getString(pathObj.get(pathObj.size()-1));
		System.out.println("ok " + obj.get(0)[res] + " " + pathObj + " "+ res +"\n");
	}
}