package fr.uvsq.Projet_Convertisseur_;

import java.util.Scanner;

public class interact 
{
	int typeFichier;
	String fileName;
	String retour;
	JSONreader json;
	interact()
	{
		typeFichier = -1;
		fileName = "";
		retour = "";
	}
	
	public void Start()
	{
		Scanner scan = new Scanner(System.in);
		while(typeFichier != 49 && typeFichier != 50)
		{
			System.out.println("Quel type de fichier voulez vous convertir ? (1) JSON (2) CSV :");
			typeFichier = scan.next().charAt(0);
		}
		typeFichier = typeFichier - 48;
		searchFileName(scan);
		System.out.println("le fichier de configuration par défaut a été généré, il se nomme conf.txt");
		while(retour == "")
		{
			System.out.println("Entrez le nom du fichier de retour pour lancer la conversion");
			retour = scan.next();
		}
		if(typeFichier == 1)
		{
			new convert_JSON_CSV(json,retour);
		}
		else
		{
			
		}
		System.out.println("La conversion est terminée et écrite dans le fichier : "+retour);
	}
	private void searchFileName(Scanner scan)
	{
		boolean ok = true;
		while(ok)
		{
			System.out.println("Quel est le nom du fichier à convertir");
			fileName = scan.next();
			if(typeFichier == 1)
			{
				if(fileName.contains(".json"))
				{
					json = new JSONreader(fileName);
					json.genConfFile();
					ok = false;
				}
				else
				{
					System.out.println("le fichier n'est pas un fichier json");
				}
			}
			else
			{
				if(fileName.contains(".csv"))
				
				{
					ok = false;
				}
				else
				{
					System.out.println("le fichier n'est pas un fichier csv");
				}
			}
		}
	}
}
