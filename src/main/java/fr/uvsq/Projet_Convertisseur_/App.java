package fr.uvsq.Projet_Convertisseur_;

public class App 
{
    public static void main( String[] args )
    {
        JSONreader r = new JSONreader("json.JSON"); 
       r.genConfFile();
       // convert_JSON_CSV conv = new convert_JSON_CSV(r);
    }
}
