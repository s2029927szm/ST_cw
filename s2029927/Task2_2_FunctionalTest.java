package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task2_2_FunctionalTest {
	
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
	        Option o44= new Option("o44", Type.STRING);//#7
	        
	        Option o6 = new Option("o6", Type.INTEGER);//#8
	        Option o8 = null;//#9
	        Option o9 = new Option(null, Type.INTEGER);//#10
	
	        o1.setName(null);
	        assertTrue(!o1.equals(o1_e)); //test suit #1
	        o1.setName("o1");
	        assertTrue(!o1.equals(o1_e)); //test suit #2
	        o1.toString();
	        

	        try {
	        	assertTrue(!o1.equals(o8));//test suit #3
	        }catch (Exception e){
	        }
	        try {
	        	assertTrue(!o1.equals(new Option("o10", Type.STRING)));//test suit #4
	        }catch (Exception e){
	        }
	        try {
	        	assertFalse((o1.equals(o9))?true:false);//test suit #5
	        }catch (Exception e){
	        }
	        
	        
	        
	        
	        
	
	        parser.addOption(o1, "o1");
	        parser.addOption(o2);
	        parser.addOption(o3, "o3s");
	        parser.addOption(o4);
	        parser.addOption(o44,"o44");
	        try {
	        	parser.addOption(o6, null);
	        }catch (Exception e){
	        }
	        
	        try {
	        	parser.addOption(o6, "£");
	        }catch (Exception e){
	        }
	        
	        try {
	            parser.addOption(o5);
	        } catch (IllegalArgumentException e) {
	        }
	
	        parser.addOption(o1, "o1");
	        parser.addOption(o1);
	
	        try {
	            parser.addOption(new Option(null, Type.STRING));
	        }catch (Exception e){
	        }
	        try {
	            parser.addOption(new Option("", Type.STRING));
	        }catch (Exception e){
	        }
	        try {
	            parser.addOption(new Option("1n", Type.STRING));
	        }catch (Exception e){
	        }
	        try {
	            parser.addOption(new Option("n1", Type.STRING), null);
	        }catch (Exception e){
	        }
	
	        try {
	            parser.parse("a11=1");
	        }catch (Exception e){
	        }
	
	        assertEquals(parser.parse(null), -1); //test suit #6
	        assertEquals(parser.parse(""), -2); //test suit #7
	        assertEquals(parser.parse("--o1=1 -o2 2 --o3 '0' --o4 \"a\""), 0); //test suit #8
	        assertEquals(parser.parse("--o2 2"), 0); //test suit #9
	        //assertEquals(parser.parse("-o3s=false"), 0); //test suit #10
	        assertEquals(parser.parse("o3s="), 0); //test suit #11
	
	        parser.setShortcut("o1", "1");
	        parser.setShortcut("", "1");
	
	        assertTrue(parser.optionExists("o1")); //test suit #12
	        assertTrue(parser.shortcutExists("o1")); //test suit #13
	        assertTrue(parser.optionOrShortcutExists("o3s")); //test suit #14
	        assertTrue(parser.optionOrShortcutExists("o3"));//test suit #15
	        assertFalse(parser.optionOrShortcutExists(""));//test suit #16
	
	        assertEquals(parser.getString("o1"), "1"); //test suit #17
	        assertEquals(parser.getString("o2"), "2"); //test suit #18
	        assertEquals(parser.getInteger("o2"), 2);//test suit #19
	        parser.parse("--o2=999999999999999999999999999999999999999999999999999");
	        assertEquals(parser.getInteger("o2"), 0); //test suit #20
	        parser.parse("--o2=-FFFFFFFFFFFFFFFFFFFFFFFF");
	        assertEquals(parser.getInteger("o2"), 0); //test suit #21
	        //parser.parse("--o2=' =1 =23'");
	        try {
	        	parser.parse("a");
	        }catch (Exception e) {
	        }
	        
	        assertEquals(parser.getCharacter("o3"), '\0');//test suit #22
	        parser.parse("--o44=\"\"");
	        assertEquals(parser.getInteger("o3"), 0); //test suit #23
	        assertEquals(parser.getBoolean("o44"), false);//test suit #24
	        parser.parse("-o3s=False");
	        parser.parse("--o44=False");
	        assertEquals(parser.getInteger("o3"), 0);//test suit #25
	        assertEquals(parser.getBoolean("o44"), false);//test suit #26
	        parser.parse("-o3s=\"0\"");
	        parser.parse("--o44=\"0\"");
	        assertEquals(parser.getInteger("o3"), 0);//test suit #27
	        assertEquals(parser.getBoolean("o44"), false);//test suit #28
	        parser.parse("-o3s=True");
	        parser.parse("--o44=True");
	        assertEquals(parser.getInteger("o3"), 1);//test suit #29
	        assertEquals(parser.getBoolean("o44"), true);//test suit #30
	        
	        assertEquals(parser.getInteger("o4"), 97); //test suit #31
	
	        boolean o11NotExists = false;
	        try {
	            parser.getInteger("--o11");
	        } catch (Exception e) {
	            o11NotExists = true;
	        }
	        assertTrue(o11NotExists); //test suit #32
	
	        boolean o11ShortcutNotExists = false;
	        try {
	            parser.getInteger("-o11");
	        } catch (Exception e) {
	            o11ShortcutNotExists = true;
	        }
	        assertTrue(o11ShortcutNotExists); //test suit #33
	
	        parser.replace("--o2 -o1 o3 o3s", "1", "0");
	        
	        parser.toString();
	        try {
	        	parser.parse("--o1='1' --o2=\"Flase --o2=Flase\" --o2=\"'abc\" --o2=\"abc'\" --o4=\"abc\"");
	        }catch (Exception e) {
	        }

	    }
		@Test
		public void abc(){
			Option op = new Option("op", Type.STRING);
			assertTrue(op.equals(op));//test suit #34
			try {
	        	assertTrue(!op.equals("op"));//test suit #35
	        }catch (Exception e){
	        }
			try {
	        	assertTrue(op.equals(new Option("op",Type.STRING)));//test suit #36
	        }catch (Exception e){
	        }
		}
		
}
