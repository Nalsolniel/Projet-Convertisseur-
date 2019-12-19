
package fr.uvsq.Projet_Convertisseur_;


import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.SingleSelectionModel;

import org.junit.Test;
/**
 * @author Nalsoniel
 *
 */
public class CsvReaderTest {

	
	
		//TraitementFinie
		@Test 
		public final void  TestTruetraitementFini(){
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			boolean[] tab= new boolean[10];
			for (int i=0;i<10;i++) {
				tab[i]=true;
			}
			assertTrue(c.traitementFini(tab));
		
		}
		@Test 
		public final void  TestFalsetraitementFini(){
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			boolean[] tab= new boolean[10];
			for (int i=0;i<10;i++) {
				tab[i]=true;
			}
			tab[5]=false;
			assertFalse(c.traitementFini(tab));
		
		}
		
		
		//casePlusPetitElement
		//public int casePlusPetitElement(String[] matriceATraitee,boolean[] matriceTraitee)
		
		@Test
		public final void TestTruecasePlusPetitElement() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			boolean[] tab= new boolean[10];
			String[] MatriceElem= new String[10];
			for (int i=0;i<10;i++) {
				tab[i]=false;
			}
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
			MatriceElem[4]="tel_mobile";
			MatriceElem[3]="tel_fax_perso";
			assertEquals(c.casePlusPetitElement(MatriceElem,tab),4);
		}
		@Test
		public final void TestTabTruecasePlusPetitElement() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			boolean[] tab= new boolean[10];
			String[] MatriceElem= new String[10];
			for (int i=0;i<10;i++) {
				tab[i]=false;
			}
			tab[1]=true;
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
			MatriceElem[1]="tel_mobile";
			MatriceElem[2]="tel_fax_perso";
			assertEquals(c.casePlusPetitElement(MatriceElem,tab),2);
		}
		@Test
		public final void Test2TruecasePlusPetitElement() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			boolean[] tab= new boolean[10];
			String[] MatriceElem= new String[10];
			for (int i=0;i<10;i++) {
				tab[i]=false;
			}
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
			MatriceElem[1]="tel_mobile";
			MatriceElem[2]="tel_fax_perso";
			MatriceElem[4]="tel_fixe";
			assertEquals(c.casePlusPetitElement(MatriceElem,tab),1);
		}
		
		//ecrireMotPareilActualiserMatriceBoolean exeption
		//public void ecrireMotPareilActualiserMatriceBoolean(String[] matriceATraitee,boolean[] matriceTraitee,FileWriter write)
		
		@Test
		public final void TestTrueecrireMotPareilActualiserMatriceBoolean() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			boolean[] tab= new boolean[10];
			
			
			String[] MatriceElem= new String[10];
			for (int i=0;i<10;i++) {
				tab[i]=false;
			}
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
			MatriceElem[1]="tel_mobile";
			MatriceElem[2]="tel_fax_perso";
			MatriceElem[3]="tel_fixe";

			File f = new File("conf.txt");
			
			
			
			
			try 
			{
			
				
				f.createNewFile();
				FileWriter write = new FileWriter(f);
			
			c.ecrireMotPareilActualiserMatriceBoolean(MatriceElem,tab,write);
			
			write.close();
			
		
			
			
			if(tab[1]==true) {
				if(tab[2]==true) {
					assertEquals(tab[3],true);
					
				}
			}
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		
		}
		
		
		//  genConfFile pense impossible exeption 
		//public void genConfFile(String[] header)
		@Test 
		public final void TestrgenConfFile() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			String[] MatriceElem= new String[10];
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
			MatriceElem[1]="tel_mobile";
			MatriceElem[2]="tel_fax_perso";
			MatriceElem[4]="tel_fixe";
			MatriceElem[3]="tel_fixe";
			MatriceElem[5]="tel_fixe";
			MatriceElem[6]="tel_fixe";
			MatriceElem[7]="tel_fixe";
			MatriceElem[8]="tel_fixe";
			MatriceElem[9]="tel_fixe";
			MatriceElem[0]="Nom";
			c.genConfFile(MatriceElem);
			
		}
		
		
		// peutEcrire
		//public boolean peutEcrire(String[] blackList,String mot)
		@Test
		public final void TestTruepeutEcrire() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			String mot =new String();
			mot="nom";
			boolean[] tab= new boolean[10];
			String[] MatriceElem= new String[10];
			for (int i=0;i<10;i++) {
				tab[i]=false;
			}
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
			MatriceElem[1]="tel_mobile";
			MatriceElem[2]="tel_fax_perso";
			MatriceElem[4]="tel_fixe";
			assertEquals(c.peutEcrire(MatriceElem, mot),true);
			
		}
		
		@Test
		public final void TestfalsepeutEcrire() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			String mot =new String();
			mot="tel_fixe";
			boolean[] tab= new boolean[10];
			String[] MatriceElem= new String[10];
			for (int i=0;i<10;i++) {
				tab[i]=false;
			}
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
			MatriceElem[1]="tel_mobile";
			MatriceElem[2]="tel_fax_perso";
			MatriceElem[4]="tel_fixe";
			assertEquals(c.peutEcrire(MatriceElem, mot),false);
			
		}
		// reader impossible a tester
		//public void reader()
		@Test 
		public final void Testresder() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			c.reader("csv.csv");
			
		}
		
		// selectTab
		//public String[] selectTab(String[] matriceATraitee,boolean[] matriceTraitee)
		
		@Test
		public final void TestTrueselectTab() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			int ok=1;
			boolean[] tab= new boolean[10];
			String[] MatriceElem= new String[10];
			String[] test= new String[10];
			for (int i=0;i<10;i++) {
				tab[i]=false;
			}
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
			MatriceElem[1]="tel_mobile";
			MatriceElem[2]="tel_fax_perso";
			MatriceElem[4]="tel_fixe";
			MatriceElem[3]="tel_fixe";
			MatriceElem[5]="tel_fixe";
			MatriceElem[6]="tel_fixe";
			MatriceElem[7]="tel_fixe";
			MatriceElem[8]="tel_fixe";
			MatriceElem[9]="tel_fixe";
			MatriceElem[0]="Nom";
			//System.out.println("bonjour");
			test= c.selectTab(MatriceElem,tab);
			//System.out.println("bouuuuuuuuuuuunjour");
			for(int i=9;i>0;i--) {
			
				if (test[i]!=null) {
					ok=0;
				}
			}
			if(ok==1) {assertNotEquals(MatriceElem[0],null);}
			
		}
		
		@Test
		public final void TestFalseselectTab() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			int ok=1;
			boolean[] tab= new boolean[10];
			String[] MatriceElem= new String[10];
			String[] test= new String[10];
			for (int i=0;i<10;i++) {
				tab[i]=false;
			}
			tab[1]=true;
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
			MatriceElem[1]="tel_mobile";
			MatriceElem[2]="tel_fax_perso";
			MatriceElem[4]="tel_fixe";
			MatriceElem[3]="tel_fixe";
			MatriceElem[5]="tel_fixe";
			MatriceElem[6]="tel_fixe";
			MatriceElem[7]="tel_fixe";
			MatriceElem[8]="tel_fixe";
			MatriceElem[9]="tel_fixe";
			MatriceElem[0]="Nom";
			//System.out.println("bonjour");
			test= c.selectTab(MatriceElem,tab);
			//System.out.println("bouuuuuuuuuuuunjour");
			for(int i=9;i>0;i--) {
			
				if (test[i]==null) {
					ok=0;
				}
			}
			if(ok==1) {assertEquals(MatriceElem[0],null);}
			
		}
		
		//tailleMaxElementTableau 
		//public int tailleMaxElementTableau(String[] matriceATraitee) 
		@Test
		public final void TestTruetailleMaxElementTableau() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			int ok=1;
			
			String[] MatriceElem= new String[10];
		
			
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
			MatriceElem[1]="tel_mobile";
			MatriceElem[2]="tel_fax_perso";
			MatriceElem[4]="tel_fixe";
			MatriceElem[3]="tel_fixe";
			MatriceElem[5]="tel_2_3_4";
			MatriceElem[6]="tel_fixe";
			MatriceElem[7]="tel_fixe";
			MatriceElem[8]="tel_fixe";
			MatriceElem[9]="tel_fixe";
			
	 {assertEquals(c.tailleMaxElementTableau(MatriceElem),4);}
			
		}
		//  verifMot
		//public String[] verifMot(String[] transition)
		
		@Test
		public final void TestTrueVerifmot() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			String[] mot =new String[2];
			mot[0]="tel";
			mot[1]="fixe";
			int nb=0;
			String[] MatriceElem= new String[10];
			
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
		
			//c.verifMot(mot,MatriceElem);
			MatriceElem=c.verifMot(mot,MatriceElem);
			for (int i=9;i>-1;i--) {
				//MatriceElem[i]=null;
				//System.out.println(MatriceElem[i]);
				if(MatriceElem[i]!=null) {
					nb+=1;
				}
			}
		//	System.out.println(nb);
			assertEquals(nb,2);
		}
		@Test
		public final void Test2TrueVerifmot() {
			csvReader c = new csvReader();
			c.initFichierCSV("csv.csv");
			String[] mot =new String[2];
			mot[0]="tel";
			mot[1]="fixe";
			int nb=0;
			String[] MatriceElem= new String[10];
			
			for (int i=0;i<10;i++) {
				MatriceElem[i]=null;
			}
		
			MatriceElem= c.verifMot(mot,MatriceElem);
			mot[0]="tel";
			mot[1]="mobile";
			
			MatriceElem=c.verifMot(mot,MatriceElem);
			for (int i=9;i>-1;i--) {
				//MatriceElem[i]=null;
				//System.out.println(MatriceElem[i]);
				if(MatriceElem[i]!=null) {
					nb+=1;
				}
			}
			//System.out.println(nb);
		//	assertEquals(nb,3);
		}
		
}