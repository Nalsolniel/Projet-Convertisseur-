package fr.uvsq.Projet_Convertisseur_;
import au.com.bytecode.opencsv.CSVReader;

import java.io.*;

public class csvReader {
	
	void reader()
	{
		try {
			FileReader fr = new FileReader("csv.csv");
			CSVReader reader = new CSVReader(fr);	
			String[] header;
			while((header = reader.readNext()) != null)
				{
					for (int x=0; x<header.length; x++)
						System.out.print(header[x]);
					System.out.println();
				}
		
			reader.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}	
}
