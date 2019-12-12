package fr.uvsq.Projet_Convertisseur_;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	csvReader c = new csvReader();
    	c.reader();
    	convertCsvJson conv = new convertCsvJson();
    	conv.nom();
    }

}
