package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task2_3_FunctionalTest {
	
		private Parser parser;
		
		@Before
		public void setUp() {
			parser = new Parser();
		}
		
		@Test
	    public void task2() {
	        Option o1 = new Option("o1", Type.STRING);//#1
	        Option o1_e = new Option("o1_e", Type.STRING);//#2
	        Option o2 = new Option("o2", Type.INTEGER);//#3
	        Option o3 = new Option("o3", Type.BOOLEAN);//#4
	        Option o4 = new Option("o4", Type.CHARACTER);//#5
	        Option o5 = new Option("o5", Type.NOTYPE);//#6
	        Option o8 = null;//#7
	        Option o9 = new Option(null, Type.INTEGER);//#8
	        Option op = new Option("op", Type.STRING);//#9
			assertTrue(op.equals(op));//@1
			try {
	        	assertTrue(!op.equals("op"));//@2
	        }catch (Exception e){
	        }
			try {
	        	assertTrue(op.equals(new Option("op",Type.STRING)));//@3
	        }catch (Exception e){
	        }

	        o1.setName(null);
	        assertTrue(!o1.equals(o1_e));//@4
	        o1.setName("o1");
	        assertTrue(!o1.equals(o1_e));//@5
	        o1.toString();
	        try {
	        	assertTrue(!o1.equals(o8));//@6
	        }catch (Exception e){
	        }
	        try {
	        	assertTrue(!o1.equals(new Option("o10", Type.STRING)));//@7
	        }catch (Exception e){
	        }
	        try {
	        	assertFalse((o1.equals(o9))?true:false);//@8
	        }catch (Exception e){
	        }

	        parser.addOption(o1, "o1");
	        parser.addOption(o2);
	        parser.addOption(o3, "o3s");
	        parser.addOption(o4);
	        try {
	            parser.addOption(o5);
	        } catch (IllegalArgumentException e) {
	        }
	        
	        parser.addOption(o1, "o1");

	        try {
	            parser.addOption(new Option(null, Type.STRING));
	        } catch (Exception e) {
	        }
	        try {
	            parser.addOption(new Option("", Type.STRING));
	        } catch (Exception e) {
	        }
	        try {
	            parser.addOption(new Option("1n", Type.STRING));
	        } catch (Exception e) {
	        }
	        try {
	            parser.addOption(new Option("n1", Type.STRING), null);
	        } catch (Exception e) {
	        }

	        try {
	            parser.parse("a11=1");
	        } catch (Exception e) {
	        }

	        parser.parse(null);
	        parser.parse("");
	        parser.parse("--o1=1 -o2 2 --o3 '0' --o4 \"a\"");
	        parser.parse("--o2 2 --o3=");
	        parser.setShortcut("o1", "1");

	        parser.optionExists("o1");
	        parser.shortcutExists("o1");
	        parser.optionOrShortcutExists("o1");
	        parser.optionOrShortcutExists("");
	        assertEquals(parser.getCharacter("o3"), '\0');//@9
	        assertEquals(parser.getString("o1"), "1");//@10
	        assertEquals(parser.getInteger("o2"), 2);//@11

	        parser.replace("--o2", "2", "");
	        assertEquals(parser.getInteger("o2"), 0);//@12
	        parser.parse("--o2=999999999999999999999999999999999999999999999999999");
	        assertEquals(parser.getInteger("o2"), 0);//@13
	        parser.parse("--o2=-FFFFFFFFFFFFFFFFFFFFFFFF");
	        assertEquals(parser.getInteger("o4"), 97);//@14
	        assertEquals(parser.getInteger("o3"), 0); //@15
	        parser.parse("-o3s=False");
	        assertEquals(parser.getInteger("o3"), 0);//@16
	        parser.parse("-o3s=\"0\"");
	        assertEquals(parser.getInteger("o3"), 0);//@17
	        parser.parse("-o3s=True");
	        assertEquals(parser.getInteger("o3"), 1);//@18

	        try {
	            parser.getInteger("--o11");
	        } catch (Exception e) {
	        }

	        try {
	            parser.getInteger("-o11");
	        } catch (Exception e) {
	        }
	        parser.replace("--o2 -o1 o3 o3s", "1", "0");
	        parser.toString();
	    }
		
}
