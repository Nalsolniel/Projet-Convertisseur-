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

/*constructeur de la class JSONreader
 * prend en argument un String contenant le nom du fichier JSON et crée un JSONObject j*/
public class JSONreader 
{
	JSONObject j;
	
	JSONreader(String name)   
	{ 
		BufferedReader file;
		String str = "";
		//lecture du fichier 
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
		//création du json object 
		j = new JSONObject(str);
	}
	
	/*récupération du JSONobject*/
	public JSONObject getJASON()
	{return j;}
	
	
	/*création du fichier de configuration*/
	public void genConfFile()
	{
		String res = "";
		String conf = genConfString(j,0,res);
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
	
	/*parcours d'un objet JSON pour produire le fichier de configuration*/
	public String genConfString(JSONObject j, int depth,String res)
	{
		Set<String> s = j.keySet();
		int i;
		for(String val : s)
		{
			if(j.get(val) instanceof JSONArray)
			{
				for(i=0; i<depth; i++) {res = res + "-";}
				res = res + val + " : " + "\n";
				res = Confsuiv(j.getJSONArray(val),depth+1,res);
			}
			else if(j.get(val) instanceof JSONObject)
			{
				for(i=0; i<depth; i++) {res = res + "-";}
				res = res + val + " : " + "\n";
				res = genConfString(j.getJSONObject(val), depth+1,res);
			}
			else
			{
				for(i=0; i<depth; i++) {res = res + "-";}
				res = res + val + " < " + val + "\n";
			}
		}
		return res;
	}
	
	/*création du fichier de configuration a partir d'une JSONArray*/
	public String Confsuiv(JSONArray j, int depth,String res)
	{
		int length = j.length();
		int i;
		int k = 0;
		int var = 0;
		int stop = 0;
		ArrayList<JSONObject> dejaVu = new ArrayList<JSONObject>();
		for(i=0; i<length; i++)
		{
			//traitement d'un JSON pour crée le fichier de configuration
			if(j.get(i) instanceof JSONObject) 
			{
				JSONObject tmp = (JSONObject) j.get(i);
				JSONObject tmpVu;
				while(k<dejaVu.size() && stop == 0) 
				{
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
					res = genConfString(tmp,depth,res);
					dejaVu.add(tmp);
				}
			}
			//traitement d'un JSONArray pour générer un fuchier de configuration
			else if(j.get(i) instanceof JSONArray)
			{
				String indent = "";
				int it;
				for(it=0;it<depth;it++)
				{
					indent =  indent + "-";
				}
				res = res + indent + i + " :\n";
				res = Confsuiv(j.getJSONArray(i), depth+1, res);
			}
			//évaluation d'un int, float, double, boolean
			else
			{
				if(var == 0)
				{
					String indent = "";
					int it; 
					for(it=0;it<depth;it++)
					{
						indent =  indent + "-";
					}
					res = res + indent + i + " < " + i + "\n";
					var = 1;
				}
			}
		}
		return res;		
	}
	
	/*fonction qui affiche une JSONArray*/
	public void suiv(JSONArray j)
	{ 
		int length = j.length();
		int i;
		for(i=0; i<length; i++)
		{
			if(j.get(i) instanceof JSONObject) 
			{
				JSONObject tmp = (JSONObject) j.get(i);
				aff(tmp);
			}
			else if(j.get(i) instanceof JSONArray)
			{
				System.out.println(i+" :");
				JSONArray tmp = j.getJSONArray(i);//System.out.println(j.get(i));
				suiv(tmp);
			}
			else
			{
				System.out.println(j.get(i));
			}
		}
				
	}
	/*fonction qui affiche un JSONObject */
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


