package fr.uvsq.Projet_Convertisseur_;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

public class convert  
{
	public static void convert_JSON_CSV(JSONObject j)
	{
		BufferedReader file;
		int cont = 0;
		ArrayList<String> l = new ArrayList<String>();
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
		
		int i;
		String[] ltemp;
		for(i=0; i<cont; i++)
		{
			ltemp = l.get(i).split(" ");
			if(ltemp.length == 2)
			{
				
			}
		}
	} 
}