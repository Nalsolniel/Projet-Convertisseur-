package fr.uvsq.Projet_Convertisseur_;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONreader 
{
	JSONreader(String name) 
	{
		BufferedReader file;
		String str = "";
		try 
		{
			file = new BufferedReader(new FileReader(name));
			String tmp = file.readLine();
			while(tmp != null)
			{
				str = str + tmp;
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
			
		JSONObject test = new JSONObject(str);
		aff(test);
		genConfFile(test);
	
	}
	
	public void genConfFile(JSONObject j)
	{
		String conf = genConfString(j);
		File f = new File("conf.txt");
		try 
		{
			f.createNewFile();
			FileWriter write = new FileWriter(f);
			write.write(conf);
			write.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public String genConfString(JSONObject j)
	{
		Set<String> s = j.keySet();
		String res = "";
		
		for(String val : s)
		{
			if(j.get(val) instanceof JSONArray)
			{
				res = res + val + " <- " + val + "\n";
				res = res + Confsuiv(j.getJSONArray(val));
			}
			else
			{
				res = res + val + " <- " + val + "\n";
			}
		}
		return res;
	}
	
	public String Confsuiv(JSONArray j)
	{
		int length = j.length();
		int i;
		String res = "";
		for(i=0; i<length; i++)
		{
			JSONObject tmp = (JSONObject) j.get(i);
			res = res + genConfString(tmp);
		}
		return res;		
	}
	
	
	
	
	public void suiv(JSONArray j)
	{
		int length = j.length();
		int i;
		for(i=0; i<length; i++)
		{
			JSONObject tmp = (JSONObject) j.get(i);
			aff(tmp);
		}
				
	}
	
	public void aff(JSONObject j)
	{
		int length = j.length();
		Set<String> s = j.keySet();
		
		for(String val : s)
		{
			if(j.get(val) instanceof JSONArray)
			{
				System.out.println(val +" : ");
				suiv(j.getJSONArray(val));
			}
			else
			{
				System.out.println(val +" : " + j.get(val));
			}
		}
	}
	
}
