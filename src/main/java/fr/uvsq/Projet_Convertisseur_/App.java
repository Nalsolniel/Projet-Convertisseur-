package fr.uvsq.Projet_Convertisseur_;

public class App 
{
    public static void main( String[] args )
    {
        JSONreader r = new JSONreader("json.JSON"); 
      // r.genConfFile();
       new convert_JSON_CSV(r);
    }
}
