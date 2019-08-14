package main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import lava.lang.parser.LavaValue;
import lava.lang.parser.Parser;
import lava.lang.util.LavaFile;

public class Main {
	
	/**
	 * used for testing lava.
	 */
	public static void main(String[] args) throws IOException {
		
		HashMap<String, LavaValue> objs = Parser.parse(new LavaFile(new File("test.lava")));
		for(String s : objs.keySet()) {
			System.out.println(s + " = " + objs.get(s).getValue() + " - " + objs.get(s).getType());
		}
		System.exit(0);
	}
	
}
