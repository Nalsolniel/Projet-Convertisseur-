/**
 * 
 */
package fr.uvsq.Projet_Convertisseur_;


import static org.junit.Assert.*;

import java.io.FileWriter;

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
			boolean[] tab= new boolean[10];
			for (int i=0;i<10;i++) {
				tab[i]=true;
			}
			assertTrue(c.traitementFini(tab));
		
		}
		@Test 
		public final void  TestFalsetraitementFini(){
			csvReader c = new csvReader();
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
		
		//ecrireMotPareilActualiserMatriceBoolean
		//public void ecrireMotPareilActualiserMatriceBoolean(String[] matriceATraitee,boolean[] matriceTraitee,FileWriter write)
		
		
		
		
}
