package com.BScProject.truffle.jsl.nodes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.object.Shape;

// Heavily inspired by the Cover language implementation (C++ in Truffle)

public class JSLType {
	public enum BasicType {
		LONG,
		DOUBLE,
		STRING,
		BOOLEAN,
		ARRAY,
		ARRAY_ELEMENT,
		FUNCTION,
		OBJECT,
		VOID
	}
	
	public static final JSLType VOID = new JSLType(BasicType.VOID);
	public static final JSLType LONG = new JSLType(BasicType.LONG);
	public static final JSLType DOUBLE = new JSLType(BasicType.DOUBLE);
	public static final JSLType STRING = new JSLType(BasicType.STRING);
	public static final JSLType BOOLEAN = new JSLType(BasicType.BOOLEAN);
	public static final JSLType ARRAY = new JSLType(BasicType.ARRAY);
	public static final JSLType FUNCTION = new JSLType(BasicType.FUNCTION);
	public static final JSLType OBJECT = new JSLType(BasicType.OBJECT);
	
	private BasicType basicType;
	
	private JSLType[] functionArguments;
	private JSLType functionReturn;
	
	/*
	 * For variables.
	 */
	private Map<String, JSLType> objectMembers = new HashMap<String, JSLType>();
	private Shape shape;
	
	private JSLType arrayType;
	
	public JSLType(BasicType basicType) {
		this.basicType = basicType;
	}
	
	public JSLType[] getFunctionArguments() {
		return functionArguments;
	}
	
	public JSLType setFunctionArguments(JSLType[] arguments){
		this.functionArguments = arguments;
		return this;
	}
	
	public void setFunctionReturn(JSLType functionReturn) {
		this.functionReturn = functionReturn;
	}
	
	public JSLType getFunctionReturn() {
		return functionReturn;
	}
	
	public Map<String, JSLType> getObjectMembers() {
		return objectMembers;
	}
	
	public JSLType setObjectMembers(Map<String, JSLType> objectMembers) {
		this.objectMembers = objectMembers;
		return this;
	}
	
//	public FrameSlotind getFrameSlotKind(IASTNode node) {
//		//TODO
//	}
	
	public BasicType getBasicType() {
		return basicType;
	}
	
	public JSLType getTypeOfArrayContents() {
		return arrayType;
	}
	
	public JSLType setArrayType(JSLType arrayType){
		this.arrayType = arrayType;
		return this;
	}
	
	public boolean canAccept(JSLType type) {
		CompilerAsserts.neverPartOfCompilation();
		if(this.equals(type)) {
			return true;
		}
		// double accepts long
		if(getBasicType() == BasicType.DOUBLE && type.getBasicType() == BasicType.LONG) {
			
			return true;
		}
		// TODO Check types!
		return false;
	}
	
	// Wait. Do I want to do this?!
	@Override 
	public int hashCode() {
		CompilerAsserts.neverPartOfCompilation();
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrayType == null) ? 0 : arrayType.hashCode());
		
		result = prime * result + ((basicType == null) ? 0 : basicType.hashCode());
		
		result = prime * result + Arrays.hashCode(functionArguments);
		
		result = prime * result + ((functionReturn == null) ? 0 : functionReturn.hashCode());
		
		result = prime * result + ((objectMembers == null) ? 0 : objectMembers.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		CompilerAsserts.neverPartOfCompilation();
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		JSLType other = (JSLType) obj;
		if (arrayType == null) {
			if (other.arrayType != null) {
				return false;
			}
		} else if (!arrayType.equals(other.arrayType)) {
			return false;
		}
		
		if (basicType != other.basicType) {
			return false;
		}
		
		if (!Arrays.equals(functionArguments, other.functionArguments)) {
			return false;
		}
		
		if (functionReturn == null) {
			if (other.functionReturn != null) {
				return false;
			}
		} else if (!functionReturn.equals(other.functionReturn)) {
			return false;
		}
		
		if (objectMembers == null) {
			if(other.objectMembers != null) {
				return false;
			}
		} else if (!objectMembers.equals(other.objectMembers)) {
			return false;
		}
		
		return true;
		
		
	}
	
	@Override
	public String toString() {
		return "JeSSEL type [basicType="+ basicType +", functionArguments="+ Arrays.toString(functionArguments) +", functionReturn="+ functionReturn +", objectMembers="+ objectMembers +", arrayType="+ arrayType +"]";
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	
	
}