package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task3_TDD1 {
	
	private Parser parser;	
	
	@Before
	public void setUp() {
		parser = new Parser();
	}
	
	
	@Test
    public void testbulk_1236() {
		/**
		 * this tests for addAll support bulk insertion of option
		 * 
		 * Test for bulk specification 1: pairs(#1,#2),(#3,#4),(#5.#6) tests Option match shortcuts;
		 * pair(#1#2, #3#4, #5#6)tests Option and shortcut tests matched Type.
		 * Test for bulk specification 2: #0 use space-separated
		 * Test for bulk specification 3: #0 use extra space eg:"opt1    opt2 opt3"
		 * Test for bulk specification 6: #0 use more shortcut and #7 use to check if extra shortcut 'o4'
		 * has been omitted
		 */
		
		parser.addAll("opt1    opt2 opt3", "o1 o2 o3 o4", "STRING INTEGER BOOLEAN");//#0
        parser.parse("--opt1=1 --opt2=2 --opt3=true");
        assertEquals(parser.getString("opt1"), "1");//#1
        assertEquals(parser.getString("o1"), "1");//#2
        assertEquals(parser.getInteger("opt2"), 2);//#3
        assertEquals(parser.getInteger("o2"), 2);//#4
        assertEquals(parser.getBoolean("opt3"), true);//#5
        assertEquals(parser.getBoolean("o3"), true);//#6
        boolean o4NotExists = false;
        try {
            parser.getInteger("o4");
        } catch (Exception e) {
            o4NotExists = true;
        }
        assertTrue(o4NotExists);//#7
    }


    @Test
    public void testbulk_4() {
        /**
		 * this tests for addAll support bulk insertion of option
		 * 
		 * Test for bulk specification 4: #0 use no shortcut for 'shortcuts is optional'
		 */
        parser.addAll("opt1 opt2 opt3", "STRING INTEGER BOOLEAN");//#0
        parser.parse("--opt1=1 --opt2=2 --opt3=true");
        assertEquals(parser.getString("opt1"), "1");
        assertEquals(parser.getInteger("opt2"), 2);
        assertEquals(parser.getBoolean("opt3"), true);
    }

    @Test
    public void testbulk_5() {
        /**
		 * this tests for addAll support bulk insertion of option
		 * 
		 * Test for bulk specification 5: #0 use less shortcut and #2 use to check if supposed
		 * shortcut for opt2 exist
		 */
        parser.addAll("opt1    opt2", "o1", "STRING INTEGER");//#0
        assertEquals(parser.getInteger("o1"), 0);//#1
        boolean o2NotExists = false;
        try {
            parser.getInteger("o2");
        } catch (Exception e) {
            o2NotExists = true;
        }
        assertTrue(o2NotExists);//#2
    }

    @Test
    public void testbulk_7() {
    	/**
		 * this tests for addAll support bulk insertion of option
		 * 
		 * Test for bulk specification 7: #0 use only one type STRING, #1 & #2 tests if the latter 
		 * option3 and shortcut o4 is also STRING type
		 */
        parser.addAll("opt1 opt2 opt3 opt4","o1 o2 o3 o4", "STRING");//#0
        parser.parse("--opt1 s1 --opt2 s2 --opt3 s3 --opt4 s4");
        assertEquals(parser.getInteger("opt3"), 0);//#1
        assertEquals(parser.getString("o4"),"s4");//#2
    }
    
    @Test
    public void testgroup_1() {
        /**
    	 * this tests for addAll support  group initialization
    	 * Test for group specification 1: group applies in options and shortcuts
    	 */
    	parser.addAll("opt9-11", "o1-3", "INTEGER");
        parser.parse ("--opt9 9 --opt10 10 --opt11 11");
        assertEquals(parser.getInteger("o1"), 9);
        assertEquals(parser.getInteger("opt10"), 10);
        assertEquals(parser.getInteger("o3"), 11);
    }
    
    @Test
    public void testgroup_2() {
        /**
    	 * this tests for addAll support  group initialization
    	 * Test for group specification 2: group allows regex '(([A-Za-z0-9_])+(([A-Z]-[A-Z]+)|[a-z]-[a-z]+|[0-9]-[0-9]+))'
    	 */
    	parser.addAll("opt1-3 opta optb-d optE-F", "o1-9", "INTEGER CHARACTER STRING");
        parser.parse ("--opt2 2 --opta a --optc bcd --optF EF");
        assertEquals(parser.getInteger("o2"), 2);
        assertEquals(parser.getCharacter("opta"), 'a');
        assertEquals(parser.getString("optc"), "bcd");
        assertEquals(parser.getString("o9"), "EF");
    }
    
    @Test
    public void testgroup_367() {
        /**
    	 * this tests for addAll support  group initialization
    	 * Test for group specification 3: both range values are inclusive
    	 * Test for group specification 6: groups are defined with letters(uppercase/lowercase) and numbers only,
    	 * pair(#8-10) testify the invaild form option name '£1'
    	 * Test for group specification 7: Type is related to the entire corresponding group, pairs(#0-7) testify
    	 * their correspondence
    	 */
    	parser.addAll("opt0-9 opta-z optA-Z", "o0-9 ca-z CA-Z", "INTEGER CHARACTER STRING");//#0
        parser.parse ("--opt1 1 -o9 9 --opta a -cz z --optA sss -CZ sss");//#1
        assertEquals(parser.getInteger("o1"), 1);//#2
        assertEquals(parser.getInteger("opt9"), 9);//#3
        assertEquals(parser.getCharacter("ca"), 'a');//#4
        assertEquals(parser.getCharacter("optz"), 'z');//#5
        assertEquals(parser.getString("CA"), "sss");//#6
        assertEquals(parser.getString("optZ"), "sss");//#7
        try {
        	parser.addAll("opt11 £1","INTEGER");//#8
        }catch (Exception e) {
        	parser.parse("--opt11 11");//#9
        	//System.out.println("*********");
        }
        assertEquals(parser.getInteger("opt11"), 11);//#10
    }
    
    @Test
    public void testgroup_459() {
        /**
    	 * this tests for addAll support  group initialization
    	 * Test for group specification 4: pair(#0-4) test 'optc-F' failure
    	 * Why use try to catch Exception: as specification 4 asks: 'the invalid form option should be omitted', so
    	 * I also found that if it is omitted, it will be lefted out to the store function and trigger the name check
    	 * in Optionmap class provided. As the name check define there's no other characters allowed except number,
    	 * letter, underscore.
    	 * Test for group specification 5: pair(#5-7) test invalid form 'optx-z2' failure; pair(#3 #4), (#8 #9) test
    	 * ignored altogether
    	 * Test for group specification 9: pair(#10-13) test the provided invalid case 'g1234-7ab' has been ignored
    	 * as required.
    	 */
    	
    	try {
    		parser.addAll("opt1 optb optc-F", "o1-2 ", "INTEGER CHARACTER");//#0
    	} catch (Exception e) {
    		parser.parse("--opt1 1");//#1
    		//System.out.println("*********");
    	}
        assertEquals(parser.getInteger("opt1"),1);//#2
        parser.parse("-o2 b");//#3
        assertEquals(parser.getCharacter("optb"),'b');//#4
        try {
    		parser.addAll("opt11 optb1 optx-z2", "o11-5 ", "INTEGER STRING");//#5
    	} catch (Exception e) {
    		parser.parse("--opt11 11");//#6
    		//System.out.println("*********");
    	}
        assertEquals(parser.getInteger("o11"),11);//#7
        parser.parse("-o12 b1");//#8
        assertEquals(parser.getString("optb1"),"b1");//#9
        try {
    		parser.addAll("g1 g1234-7ab","INTEGER");//#10
    	} catch (Exception e) {
    		parser.parse("--g1 1");//#11
    		//System.out.println("*********");
    	}
        assertEquals(parser.getInteger("g1"),1);//#12
        assertFalse(parser.optionExists("g1234-7ab"));//#13
    }
    
    @Test
    public void testgroup_81011() {
        /**
    	 * this tests for addAll support  group initialization
    	 * Test for group specification 8: test on provided example 'g129-11' success as showed on pair(#2-4)
    	 * Test for group specification 10: test decreasing on provided example 'g125-2' success as showed on pair(#5-7)
    	 * Test for group specification 11: test multiple option groups can correspond to one shortcut group: #3, #5, #7
    	 * and vise versa: #10, #12, #14
    	 */
    	parser.addAll("g129-11 g125-2", "o1-7", "INTEGER");//#0
        parser.parse ("--g129 129 --g1210 1210 --g1211 1211 --g125 125 --g124 124 --g123 123 --g122 122");//#1
        assertEquals(parser.getInteger("g129"), 129);//#2
        assertEquals(parser.getInteger("o2"), 1210);//#3
        assertEquals(parser.getInteger("g1211"), 1211);//#4
        assertEquals(parser.getInteger("o4"), 125);//#5
        assertEquals(parser.getInteger("g123"), 123);//#6
        assertEquals(parser.getInteger("o7"), 122);//#7
        
        parser.addAll("one1-5", "m1-3 m4-5", "INTEGER");//#8
        parser.parse("-m1 1 -m2 2 -m3 3 -m4 4 -m5 5");//#9
        assertEquals(parser.getInteger("one1"), 1);//#10
        assertEquals(parser.getInteger("m2"), 2);//#11
        assertEquals(parser.getInteger("one3"), 3);//#12
        assertEquals(parser.getInteger("m4"), 4);//#13
        assertEquals(parser.getInteger("one5"), 5);//#14
    }
    
    @Test
    public void testgroup12(){
    	/**
    	 * this tests for addAll support  group initialization
    	 * Test for group specification 12: if the Type is not one of 'STRING, INTEGER, CHARACTER or BOOLEAN',
    	 * throw an exception as required.
    	 */
    	boolean err=false;
    	try {
    		parser.addAll("opt1-2 opterr","o1-2","INTEGER abc");
    	} catch (Exception e) {
    		err=true;
    	}
    	assertTrue(err);
    	parser.parse("-o1 1 --opt2 2");
    	assertEquals(parser.getInteger("opt1"),1);
    	assertEquals(parser.getInteger("o2"),2);
    }
 
}
