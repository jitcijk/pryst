package org.jitcijk.pryst.nodes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.object.Shape;

// Heavily inspired by the Cover language implementation (C++ in Truffle)

public class PrystType {
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
	
	public static final PrystType VOID = new PrystType(BasicType.VOID);
	public static final PrystType LONG = new PrystType(BasicType.LONG);
	public static final PrystType DOUBLE = new PrystType(BasicType.DOUBLE);
	public static final PrystType STRING = new PrystType(BasicType.STRING);
	public static final PrystType BOOLEAN = new PrystType(BasicType.BOOLEAN);
	public static final PrystType ARRAY = new PrystType(BasicType.ARRAY);
	public static final PrystType FUNCTION = new PrystType(BasicType.FUNCTION);
	public static final PrystType OBJECT = new PrystType(BasicType.OBJECT);
	
	private BasicType basicType;
	
	private PrystType[] functionArguments;
	private PrystType functionReturn;
	
	/*
	 * For variables.
	 */
	private Map<String, PrystType> objectMembers = new HashMap<String, PrystType>();
	private Shape shape;
	
	private PrystType arrayType;
	
	public PrystType(BasicType basicType) {
		this.basicType = basicType;
	}
	
	public PrystType[] getFunctionArguments() {
		return functionArguments;
	}
	
	public PrystType setFunctionArguments(PrystType[] arguments){
		this.functionArguments = arguments;
		return this;
	}
	
	public void setFunctionReturn(PrystType functionReturn) {
		this.functionReturn = functionReturn;
	}
	
	public PrystType getFunctionReturn() {
		return functionReturn;
	}
	
	public Map<String, PrystType> getObjectMembers() {
		return objectMembers;
	}
	
	public PrystType setObjectMembers(Map<String, PrystType> objectMembers) {
		this.objectMembers = objectMembers;
		return this;
	}
	
//	public FrameSlotind getFrameSlotKind(IASTNode node) {
//		//TODO
//	}
	
	public BasicType getBasicType() {
		return basicType;
	}
	
	public PrystType getTypeOfArrayContents() {
		return arrayType;
	}
	
	public PrystType setArrayType(PrystType arrayType){
		this.arrayType = arrayType;
		return this;
	}
	
	public boolean canAccept(PrystType type) {
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
		
		PrystType other = (PrystType) obj;
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