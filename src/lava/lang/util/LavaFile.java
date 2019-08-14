package lava.lang.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LavaFile {
	
	private String code;
	
	/**
	 * Generates a lava file that can be used to parse keys and values using Parser.parse([thisLavaFile]).
	 */
	public LavaFile(File f) throws IOException {
		if(getFileExtension(f).equalsIgnoreCase("lava")) {
			Path path = Paths.get(f.getAbsolutePath());
			byte[] bytes = Files.readAllBytes(path);
			this.code = new String(bytes);
		} else {
			System.err.println("Cannot read files that arent lava files!");
		}
	}
	
	/**
	 * returns the code of this object.
	 */
	public String getCode() {
		return this.code;
	}
	
	 private static String getFileExtension(File file) {
		 String fileName = file.getName();
	     if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
	    	 return fileName.substring(fileName.lastIndexOf(".")+1);
	     else return "";
	 }
}
