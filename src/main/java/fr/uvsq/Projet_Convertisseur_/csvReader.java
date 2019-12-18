package fr.uvsq.Projet_Convertisseur_;
import au.com.bytecode.opencsv.CSVReader;

import java.io.*;

public class csvReader {
	
	public void reader()
	{
		try {
			FileReader fr = new FileReader("csv.csv");
			CSVReader reader = new CSVReader(fr);	
			
			/* On prend la premiere ligne du fichier csv */
			
			String[] line= reader.readNext();
			genConfFile(line);
			
			/* On prend toutes les autres lignes */
			
			while((line = reader.readNext()) != null)
				{
					for (int x=0; x<line.length; x++);
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
	
	
	public boolean traitementFini(boolean[] matriceTraitee)
	{
		for(int i=0;i<matriceTraitee.length;i++) {
			if(matriceTraitee[i] == false) {
				return false;		
			}
		}
		return true;
	}
	
//	Prend le header et rend un tableau de la meme taille contenant seulement les elements commancant comme le premier
//	element non traite
	
	public String[] selectTab(String[] matriceATraitee,boolean[] matriceTraitee)
	{
//		On recup la premiere case du premier ele non traite
		
		String[] s = new String[1];
		
		for(int i=0;i<matriceATraitee.length;i++)
		{
			if(matriceTraitee[i] == false)
			{
				s = matriceATraitee[i].split("_",2);
				i = matriceATraitee.length;
			}
		}
		
//		On va mettre dans un tableau d'une taille identique tous les mots commencant par s
		
		String[] transition;
		String[] finalTab = new String[matriceATraitee.length];
		String concat = "";
		for(int i=0;i<matriceATraitee.length;i++)
		{
			if(matriceTraitee[i] == false)
			{
				transition =  new String[matriceATraitee[i].split("_").length];
				transition = matriceATraitee[i].split("_");
				
				if(transition[0].equals(s[0]))
				{
//					si mot correspond alors reconcat transi et mettre dans final tab
					for(int j=0;j<transition.length;j++)
					{
						if(j == 0) 
						{
							concat = concat +transition[j];
						}
						else
						{
							concat = concat + '_' + transition[j];
						}
					}
					finalTab[i] = concat;
				}
			}
			concat = "";
		}
	
		return finalTab;
	}
	
	public int tailleMaxElementTableau(String[] matriceATraitee) 
	{
		int max = 0;
		for(int i=0;i<matriceATraitee.length;i++)
		{
			if(matriceATraitee[i] != null)
				if(max < matriceATraitee[i].split("_").length)
					max = matriceATraitee[i].split("_").length;
		}
		return max;
	}
	
	public int tailleMaxPourTableauBlacklist()
	{
		int taille = 0;
		try {
			FileReader fr = new FileReader("csv.csv");
			CSVReader reader = new CSVReader(fr);	
			
			/* On prend la premiere ligne du fichier csv */
			
			String[] line= reader.readNext();
			
			for(int i=0;i<line.length;i++)
			{
				taille += line[i].split("_").length;
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
		
		return taille;
	}
	
	public String[] verifMot(String[] transition,String[] TabBlackList)
	{
		boolean blackList = true;
		
		for(int i=0;i<transition.length;i++)
		{
			int j = 0;
			for(j=0;TabBlackList[j] != null;j++)
			{
				if(transition[i].equals(TabBlackList[j]))
				{
					blackList = false;
				}
			}
			if(blackList)
				TabBlackList[j] = transition[i];
			blackList = true;
		}
		return TabBlackList;
	}
	
	public boolean peutEcrire(String[] blackList,String mot)
	{
		
		for(int i = 0;i<blackList.length;i++)
		{
			if(blackList[i]!=  null)
				if(blackList[i].equals(mot))
					return false;
		}
		return true;
	}
	
	public int casePlusPetitElement(String[] matriceATraitee,boolean[] matriceTraitee)
	{
			
		int pronfMin = 0;
		int casePronfMin = 0;
		for(int i = 0; i<matriceATraitee.length;i++)
		{
			if(matriceATraitee[i]!=null && matriceTraitee[i] == false)
			{
				if(pronfMin == 0)
				{
					pronfMin = matriceATraitee[i].split("_").length;
					casePronfMin = i;
				}
				else
				{
					if(pronfMin > matriceATraitee[i].split("_").length)
					{
						pronfMin = matriceATraitee[i].split("_").length;
						casePronfMin = i;
					}
				}
			}
		}
			
		return casePronfMin;
	}
	
	
	
	public void ecrireMotPareilActualiserMatriceBoolean(String[] matriceATraitee,boolean[] matriceTraitee,FileWriter write)
	{
		String[] blackList = new String[tailleMaxPourTableauBlacklist()];
		int pronfActuel = 0;
		int pronfTransi = 0;
		
		for(int k=0;k<matriceATraitee.length;k++)
		{
			
			if(matriceATraitee[k]!=null)
			{
				int casePronfMin = casePlusPetitElement( matriceATraitee, matriceTraitee);
				pronfTransi = matriceATraitee[casePronfMin].split("_").length;
				int pronfMin = matriceATraitee[casePronfMin].split("_").length;
				
				try
				{
					String[] transition =  new String[matriceATraitee[casePronfMin].split("_").length];
					transition = matriceATraitee[casePronfMin].split("_");
					for(int i=1;i<=pronfMin;i++)
					{
						
						if(pronfTransi != pronfActuel)
						{
							for(int cpt = 1;cpt<blackList.length;cpt++)
							{
								blackList[cpt] = null;
							}
							
						}
						
						
						if(peutEcrire(blackList,transition[i-1])||(i!= 1 && transition[i-1].equals(transition[0])))
						{
							for(int j=1;j<i;j++)
							{
								write.write("-");
							}
							if(i == pronfMin)
							{
								write.write(transition[i-1]);
								write.write(" < ");
								write.write(transition[i-1]);
								write.write("\n");
							}
							else
							{
								write.write(transition[i-1]);
								write.write(" : \n");
							}
						}
						matriceTraitee[casePronfMin] = true;
					}
					if(pronfTransi == pronfActuel)
					{
						blackList = verifMot(transition,blackList);
					}
					else
					{
							blackList = verifMot(transition,blackList);
						
						pronfActuel = pronfTransi;
			
					}
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				
			}
			
		}
	}
	
	
	public void genConfFile(String[] header)
	{
		
		File f = new File("conf.txt");
		try 
		{
			f.createNewFile();
			FileWriter write = new FileWriter(f);
			
			boolean[] matriceTraitee = new boolean[header.length];
			String[] matriceATraitee = new String[header.length];
			matriceATraitee = header;
//			init tab boolean 0
			
			for(int i=0;i<header.length;i++)
			{
				matriceTraitee[i] = false;
			}
			
			for(int i=0;i<header.length;i++)
			{
				String[] pronf1= header[i].split("_");
				if(pronf1.length == 1)
				{
					write.write(pronf1[0]);
					write.write(" < ");
					write.write(pronf1[0]);
					write.write("\n");
					matriceTraitee[i] = true;
				}
			}
			
			
			//quand prof > 1
			
			String[] test =  new String[header.length]; 			
			
			
		while(!traitementFini(matriceTraitee))
		{
			test = selectTab(matriceATraitee,matriceTraitee);			
			ecrireMotPareilActualiserMatriceBoolean(test,matriceTraitee,write);
		}
			
			
			write.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
}
