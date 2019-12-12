package fr.uvsq.Projet_Convertisseur_;
import java.io.*;
import org.json.*;

public class convertCsvJson {

		public void nom()
		{
			JSONObject nom = new JSONObject();
			JSONObject nom2 = new JSONObject();
			nom2.accumulate("id", "file");
			nom.accumulate("menu", nom2);
			
			System.out.println(nom);
			
			
	//			BufferedReader reader;
	//			try{
	//				reader = new BufferedReader(new FileReader("~/eclipse-workspace/Projet-Convertisseur-/conf.txt"));
	//				String line = reader.readLine();
	//				while(line != null)
	//				{
	//					
	//				}
	//				reader.close();
	//			}
	//			catch(IOException e)
	//			{
	//				e.printStackTrace();
	//			}
		}
	
	
}
