/**
 * 
 */
package fr.uvsq.Projet_Convertisseur_;


import static org.junit.Assert.*;
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
		
		
		
		
		
		
}
