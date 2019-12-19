package fr.uvsq.Projet_Convertisseur_;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class JSONrederTest {

	@Test
	public void test() {
	//	fail("Not yet implemented");
	}
	//aff
	//public void aff(JSONObject j)
	@Test
	public final void TestTrueaff() {
		String file=new String();file="json.JSON";
		JSONreader c = new JSONreader(file);
		c.aff(c.getJASON());
	}
	//Confsuiv
	//public String Confsuiv(JSONArray j, int depth,String res)
	
	//genConfFile
	//public void genConfFile()
	
	
	//genConfString
	//public String genConfString(JSONObject j, int depth,String res)
	@Test
	public final void TestTruegenConfString() {
		String file=new String();file="test.JSON";
		JSONreader c = new JSONreader(file);
		c.genConfFile(); 
		String res =new String();res="";
		String attendu =new String();
		attendu="members : \n" + 
				"-name < name\n" + 
				"-age < age\n" + 
				"formed < formed\n" + 
				"";
		res= c.genConfString(c.getJASON(),0,res);
		System.out.println("blop");
System.out.println(attendu);
System.out.println("tartine");
System.out.println(res);
		assertEquals(res,attendu);
		
	}
	
	//getJASON
	//public JSONObject getJASON()
	@Test
	public final void TestTruegetJASON() {
		String file=new String();file="json.JSON";
		JSONreader c = new JSONreader(file);
		c.getJASON();
	}
	
	//suiv
	//public void suiv(JSONArray j)
	@Test
	public final void TestTruesuiv() {
		String file=new String();file="json.JSON";
		JSONreader c = new JSONreader(file);
	//	c.suiv(c.getJASON());
	}
}
