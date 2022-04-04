package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task1_1_FunctionalTest {
	
	private Parser parser;	
	
	@Before
	public void setUp() {
		parser = new Parser();
	}
	
	@Test
	// this test for 1.3 & 1.4 specification: trigger bug #8
	public void test1() {
	  parser.addOption(new Option("a1", Type.INTEGER),"b");
	  parser.parse("--a1=1");
	  parser.addOption(new Option("a1", Type.CHARACTER));
	  assertEquals(parser.getInteger("a1"),49);
	}
	
	@Test
	// this test for 1.3 1.4 specification: trigger bug #6
	public void test2() {
	    Option o1 = new Option("o1", Type.STRING);
	    Option o2 = new Option("o2", Type.STRING);
	    assertTrue(!o1.equals(o2));
	}
	
	@Test
	// this test for 1.3 1.4 specification: trigger bug #11
	public void test3() {
	    boolean err_1 = false;
		//parser.addOption(new Option("a1!1", Type.STRING), "A");
	    try {
	    	parser.addOption(new Option("a1!1", Type.STRING));
		  }
		  catch(Exception e) {
			  err_1 = true;
		  }
	    assertTrue(err_1);
	}
	
    @Test
	// this test for 1.3 specification: trigger bug #4
	public void test4() {
	  parser.addOption(new Option("a", Type.STRING),"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
	  assertTrue(parser.optionExists("a"));
	}
	
	@Test
	// this test for 1.4 specification: trigger bug #17
	public void test5() {
	  parser.addOption(new Option("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", Type.STRING));
	  assertTrue(parser.optionExists("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
	}
	
	@Test
	// this test for 1.5 specification: trigger bug #9
	public void test6() {
	  parser.addOption(new Option("a1", Type.INTEGER), "a");
	  //int value3=parser.parse(" ");
	  assertSame(parser.parse(" "),0);
	}

	@Test
	// this test for 1.5 specification: trigger bug #13
	public void test7() {
	  parser.addOption(new Option("a1", Type.STRING), "A");
	  parser.addOption(new Option("a2", Type.STRING), "a");
	  parser.parse("--a1 '=-a=1' -a='=2'");
	  assertEquals(parser.getString("a1"),"=-a=1");
	}
	
	@Test
	// this test for 1.5 specification: trigger bug #19
	public void test8() {
	  parser.addOption(new Option("b1", Type.STRING), "B");
	  parser.addOption(new Option("b2", Type.STRING), "b");
	  parser.parse("--b1 \"-b=1\" -b=2");
	  assertEquals(parser.getString("b1"),"-b=1");
	}
	
	@Test
	// this test for 1.5 specification: trigger bug #20
	public void test9() {
	  parser.addOption(new Option("a1", Type.STRING), "A");
	  parser.addOption(new Option("a2", Type.STRING), "a");
	  parser.parse("--a1 =-a=1 -a='2'");
	  assertEquals(parser.getString("a1"),"=-a=1");
	}
	
	@Test
	// this test for 1.6 specification: trigger bug #16
	public void test10() {
	  parser.addOption(new Option("a1", Type.STRING), "a2");
	  boolean err = false;
	  try {
		  String st = parser.getString(null);
	  }
	  catch(Exception e) {
		  err = true;
	  }
	assertTrue(err);
	}
	
	@Test
	// this test for 1.6 specification: trigger bug #1
	public void test11() {
	  parser.addOption(new Option("a1", Type.STRING));
	  parser.parse("--a1");
	  assertEquals(parser.getCharacter("a1"),'\0');
	}
	
	@Test
	// this test for 1.6 specification: trigger bug #14
	public void test12() {
	  parser.addOption(new Option("a1", Type.STRING), "A");
	  parser.parse("-A='1\\s2\\n3\\f4\\r5'");
	  assertEquals(parser.getString("A"),"1\\s2\\n3\\f4\\r5");
	}
	
	@Test
	// this test for 1.6 specification: trigger bug #10
	public void test13() {
	  parser.addOption(new Option("a1", Type.CHARACTER), "A");
	  parser.parse("-A=''");
	  assertEquals(parser.getCharacter("A"),'\0');
	}
	
	@Test
	// this test for 1.6 specification: trigger bug #2
	public void test14() {
	  parser.addOption(new Option("c1", Type.STRING),"C");
	  parser.parse("--c1");
	  assertSame(parser.getBoolean("c1"),false);
	}
	
	@Test
	// this test for 1.6 specification: trigger bug #7
	public void test15() {
	  parser.addOption(new Option("c1", Type.STRING),"C");
	  parser.parse("--c1=1234567890");
	  assertEquals(parser.getInteger("c1"),1234567890);
	}
	
	@Test
	// this test for 1.6 specification: trigger bug #15
	public void test16() {
	  parser.addOption(new Option("d1", Type.STRING),"D");
	  parser.parse("--d1=99999999999999999999999999999999");
	  assertEquals(parser.getInteger("d1"),0);
	}
	
	@Test
	// this test for 1.6 specification: trigger bug #3
	public void test17() {
	  parser.addOption(new Option("e1", Type.BOOLEAN),"E");
	  parser.parse("--e1=abcdefghijk");
	  assertEquals(parser.getInteger("e1"),1);
	}
	
	@Test
	// this test for 1.6 specification: trigger bug #5
	public void test18() {
	  parser.addOption(new Option("e1", Type.INTEGER),"E");
	  parser.parse("--e1=-1");
	  assertEquals(parser.getInteger("e1"),-1);
	}

	@Test
	// this test for 1.8 specification: trigger bug #12
	public void test19(){
	    parser.addOption(new Option("a", Type.STRING), "A");
	    parser.parse("--a=old");
	    parser.replace("-A", "old", "new");
	    assertEquals(parser.getString("a"), "new");
	}

	@Test
	// this test for 1.8 specification: trigger bug #18
	public void test20(){
	    parser.addOption(new Option("a", Type.STRING), "A");
	    parser.parse("--a=old");
	    parser.replace("-A    ", "old", "new");
	    assertEquals(parser.getString("a"), "new");
	}
	

}
