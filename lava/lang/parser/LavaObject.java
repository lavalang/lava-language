package lava.lang.parser;

public class LavaObject {
	private Object value; private ObjectType type;
	
	public LavaObject(Object value, ObjectType type) {
		this.value = value; this.type = type;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public ObjectType getType() {
		return this.type;
	}
}

enum ObjectType {
	BOOLEAN, STRING, INTEGER
}
