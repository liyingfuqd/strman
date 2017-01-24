package test.strman;

import static junit.framework.Assert.*;

import org.junit.Test;

import strman.Strman;

public class TestStrman {
	
	@Test
	public void testAppend(){
		String res = "abcdefg";
		String str = Strman.append("abc", "d", "e", "f", "g");
		assertEquals(res, str);
	}

	@Test
	public void testAppendArray(){
		String res = "abcdefg";
		String str = Strman.appendArray("abc", new String[]{"d", "e", "f", "g"});
		assertEquals(res, str);
	}
	
	@Test
	public void testAt(){
		String res = "d";
		String str = Strman.at("abcd", 3);
		assertEquals(res, str);
	}
	
	@Test
	public void testBetween(){
		String[] strs = {"name", "value"};
		String[] vals = Strman.between("[name],[value]", "[", "]");
		for(int i=0; i<2; i++){
			assertEquals(strs[i], vals[i]);
		}
	}
	
	@Test
	public void testChars(){
		String[] strs = {"a", "b", "c", "d"};
		String[] vals = Strman.chars("abcd");
		for(int i=0; i<4; i++){
			assertEquals(strs[i], vals[i]);
		}
	}
	
	@Test
	public void testCollapseWhitespace(){
		String str = "hello world";
		String val = Strman.collapseWhitespace("hello        world");
		assertEquals(str, val);
	}
	
	@Test
	public void testContains(){
		String str = "foo bar";
		boolean flag1 = Strman.contains(str, "FOO BAR");
		assertEquals(true, flag1);
		
		boolean flag2 = Strman.contains(str, "FOO BAR", true);
		assertEquals(false, flag2);
	}
	
	@Test
	public void testContainsAll(){
		String[] strs = {"foo", "bar"};
		boolean flag1 = Strman.containsAll("foo bar", strs);
		assertEquals(flag1, true);
	}
	
	@Test
	public void testBase64(){
		System.out.println(Strman.base64Encode("123123"));
	}
	
	
	
	
}
