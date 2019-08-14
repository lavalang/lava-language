package lava.lang.parser;

public class LavaValue {
	
	private Object value; private ObjectType type;
	
	public LavaValue(Object value, ObjectType type) {
		this.value = value; this.type = type;
	}
	
	/**
	 * returns the value of this LavaValue.
	 */
	public Object getValue() {
		return this.value;
	}
	
	/**
	 * returns the value type of this LavaValue.
	 */
	public ObjectType getType() {
		return this.type;
	}
}

enum ObjectType {
	BOOLEAN, STRING, INTEGER
}
