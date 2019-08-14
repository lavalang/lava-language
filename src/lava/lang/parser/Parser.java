package lava.lang.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lava.lang.util.LavaFile;

public class Parser {
	
	/**
	 * returns a hashmap with all the keys and values of a lava file.
	 * @since version 1.0.0
	 */
	public static HashMap<String, LavaValue> parse(LavaFile code) {
		Tokenizer tokenizer = new Tokenizer(code.getCode());
		ArrayList<Token> tokens = new ArrayList<>();
		while (tokenizer.hasNextToken()) {
			Token t = tokenizer.nextToken();
			tokens.add(t);
		}
		return parse(tokens);
		
	}
	
	private static HashMap<String, LavaValue> parse(ArrayList<Token> toParse) {
		
		HashMap<String, LavaValue> objs = new HashMap<>();
		
		for(Token t : toParse) {
			if(t.getType() == TokenType.IDENTIFIER) {
				
				String key = t.getToken();
				
				try {
					if(toParse.get(toParse.indexOf(t) + 1) != null) {
						Token nextToken = toParse.get(toParse.indexOf(t) + 1); 
						
						if(nextToken.getToken().equals("=")) { 
							if(toParse.get(toParse.indexOf(nextToken) + 1) != null) {
								nextToken = toParse.get(toParse.indexOf(nextToken) + 1);
								
								if(nextToken.getType() == TokenType.INTEGER || 
										nextToken.getType() == TokenType.BOOLEAN || nextToken.getType() == TokenType.STRING) {
									if(!(objs.containsKey(key))) {
										if(nextToken.getType() == TokenType.BOOLEAN) {
											objs.put(key, new LavaValue(nextToken.getToken(), ObjectType.BOOLEAN));
										}
										if(nextToken.getType() == TokenType.STRING) {
											objs.put(key, new LavaValue(nextToken.getToken(), ObjectType.STRING));
										}
										if(nextToken.getType() == TokenType.INTEGER) {
											objs.put(key, new LavaValue(nextToken.getToken(), ObjectType.INTEGER));
										}
									} else {
										System.err.println("The object " + key + " already exists!");
									}
								} else {
									System.err.println("Improper syntax! Usage: [key] = [value]");
								}
							} else {
								System.err.println("Improper syntax! Usage: [key] = [value]");
							}
						} else {
							System.err.println("Improper syntax! Usage: [key] = [value]");
						}
					}
				} catch(Exception e) {
					
					System.err.println("There was an error parsing this file.");
					System.err.println("If you see this error, it could mean one of the values of a lava object in a lava file could be null, "
							+ "\n or, it could be because the value of an object isn't a valid value, such as 'trues' instead of 'true'.");
					System.exit(0);
				}
			}
		}
		return objs;
	}
	
}

class Token {
	
	private String token; private TokenType type;
	
	public Token(String token, TokenType type) {
		this.token = token; this.type = type;
	}
	
	public String getToken() {
		return this.token;
	}
	
	public TokenType getType() {
		return this.type; 
	}
}

class Tokenizer {

	private ArrayList<TokenRegex> tokenDatas;
	
	private String str;
	
	private Token lastToken;
	private boolean pushBack;
	
	public Tokenizer(String str) {
		this.tokenDatas = new ArrayList<TokenRegex>();
		this.str = str;
		
		tokenDatas.add(new TokenRegex(Pattern.compile("^(true|false)"), TokenType.BOOLEAN));
		tokenDatas.add(new TokenRegex(Pattern.compile("^([a-zA-Z][a-zA-Z0-9]*)"), TokenType.IDENTIFIER));
		tokenDatas.add(new TokenRegex(Pattern.compile("^((-)?[0-9]+)"), TokenType.INTEGER));
		tokenDatas.add(new TokenRegex(Pattern.compile("^(\".*\")"), TokenType.STRING));
		tokenDatas.add(new TokenRegex(Pattern.compile("^(\\/\\/.*)"), TokenType.COMMENT));
		
		
		for (String t : new String[] { "=", "\\(", "\\)", "\\.", "\\,", "\\!", "\\:", "\\{", "\\}"}) {
			tokenDatas.add(new TokenRegex(Pattern.compile("^(" + t + ")"), TokenType.TOKEN));
		}
	}
	
	public Token nextToken() {
		str = str.trim();
		
		if (pushBack) {
			pushBack = false;
			return lastToken;
		}
		
		if (str.isEmpty()) {
			return (lastToken = new Token("", TokenType.EMPTY));
		}
		
		for (TokenRegex data : tokenDatas) {
			Matcher matcher = data.getPattern().matcher(str);
			
			if (matcher.find()) {
				String token = matcher.group().trim();
				str = matcher.replaceFirst("");
				
				if (data.getType() == TokenType.STRING) {
					return (lastToken = new Token(token.substring(1, token.length() - 1), TokenType.STRING));
				}
				
				else {
					return (lastToken = new Token(token, data.getType()));
				}
			}
		}
		
		throw new IllegalStateException("Could not parse " + str);
	}
	
	public boolean hasNextToken() {
		return !str.isEmpty();
	}
	
	public void pushBack() {
		if (lastToken != null) {
			this.pushBack = true;
		}
	}
}

class TokenRegex {
	
	private Pattern pattern; private TokenType type;
	
	public TokenRegex(Pattern pattern, TokenType type) {
		this.pattern = pattern; this.type = type;
	}
	
	public Pattern getPattern() {
		return this.pattern;
	}
	
	public TokenType getType() {
		return this.type;
	}
}

enum TokenType {
	
	STRING, INTEGER, TOKEN, COMMENT, IDENTIFIER, EMPTY, BOOLEAN
}

