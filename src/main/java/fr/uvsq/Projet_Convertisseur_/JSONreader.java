package fr.uvsq.Projet_Convertisseur_;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONreader 
{
	JSONObject j;

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
			
		j = new JSONObject(str);
		aff(j);
	}
	
	public JSONObject getJASON()
	{return j;}
	
	public void genConfFile()
	{
		String conf = genConfString(j,0);
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
	
	public String genConfString(JSONObject j, int depth)
	{
		Set<String> s = j.keySet();
		String res = "";
		int i;
		for(String val : s)
		{
			if(j.get(val) instanceof JSONArray)
			{
				for(i=0; i<depth; i++) {res = res + "-";}
				res = res + val + " : " + "\n";
				res = res + Confsuiv(j.getJSONArray(val),depth+1);
			}
			else if(j.get(val) instanceof JSONObject)
			{
				for(i=0; i<depth; i++) {res = res + "-";}
				res = res + val + " : " + "\n";
				res = res + genConfString(j.getJSONObject(val), depth+1);
			}
			else
			{
				for(i=0; i<depth; i++) {res = res + "-";}
				res = res + val + " < " + val + "\n";
			}
		}
		return res;
	}
	
	public String Confsuiv(JSONArray j, int depth)
	{
		int length = j.length();
		int i;
		int k = 0;
		int stop = 0;
		ArrayList<JSONObject> dejaVu = new ArrayList<JSONObject>();
		String res = "";
		for(i=0; i<length; i++)
		{
			JSONObject tmp = (JSONObject) j.get(i);
			JSONObject tmpVu;
			while(k<dejaVu.size() && stop == 0) 
			{
				System.out.println("k:"+tmp.keySet());
				tmpVu = dejaVu.get(k);
				if(tmp.keySet().containsAll(tmpVu.keySet()) || tmpVu.keySet().containsAll(tmp.keySet())) 
				{
					stop = 1;
				}
				else
				{
					if(tmp.keySet().size()>tmpVu.keySet().size())
					{
						dejaVu.set(k, tmp);
					}
				}

				k = k + 1;
			}
			if(stop == 0)
			{
				res = res + genConfString(tmp,depth);
				dejaVu.add(tmp);
			}
		}System.out.println("ok");
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
		Set<String> s = j.keySet();
		
		for(String val : s)
		{
			if(j.get(val) instanceof JSONArray)
			{
				System.out.println(val +" : ");
				suiv(j.getJSONArray(val));
			}
			else if(j.get(val) instanceof JSONObject)
			{
				System.out.println(val +" : ");
				aff(j.getJSONObject(val));
			}
			else
			{
				System.out.println(val +" : " + j.get(val));
			}
		}
	}
	
}


