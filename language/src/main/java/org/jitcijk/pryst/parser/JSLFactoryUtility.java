package com.BScProject.truffle.jsl.parser;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.BScProject.truffle.jsl.nodes.expression.JSLAddDoubleNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLAddLongNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLAddStringNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLBinaryAndNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLBinaryExclusiveOrNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLBinaryInclusiveOrNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLBinaryShiftLeftNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLBinaryShiftRightNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLDivDoubleNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLDivLongNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLDoubleLiteralNode;
import com.BScProject.truffle.jsl.nodes.expression.JSLEqualNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLLessOrEqualDoubleNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLLessOrEqualLongNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLLessThanDoubleNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLLessThanLongNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLLogicalAndNode;
import com.BScProject.truffle.jsl.nodes.expression.JSLLogicalNotNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLLogicalOrNode;
import com.BScProject.truffle.jsl.nodes.expression.JSLLongLiteralNode;
import com.BScProject.truffle.jsl.nodes.expression.JSLModLongNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLMulDoubleNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLMulLongNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLSubDoubleNodeGen;
import com.BScProject.truffle.jsl.nodes.expression.JSLSubLongNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadArrayVariableNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadBooleanVariableNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadDoubleVariableNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadLongVariableNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLReadStringVariableNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLWriteBooleanArrayElementNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLWriteBooleanNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLWriteDoubleArrayElementNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLWriteDoubleNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLWriteLongArrayElementNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLWriteLongNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLWriteStringArrayElementNodeGen;
import com.BScProject.truffle.jsl.nodes.local.JSLWriteStringNodeGen;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.PrimitiveType.Primitive;
import com.oracle.truffle.api.frame.FrameSlot;

/** 
 * Class to hold all of the Utility functions from the NodeFactory. 
 * This class has no state and only has static functions
 * used to reduce size of JSLNodeFactory
 *
 */
public final class JSLFactoryUtility {
	
    /* Checks if targetType can accept goalType, throws parseException if not the case. 
     * Sometimes this will not result in an exception but at runtime we will encounter a invalid conversion
     * due to the fact that we use TypedNodes (if we call writeDouble with a long, it wont accept it).
     */
    public static void doTypeCheck(JSLType targetType, JSLType goalType, boolean isArray) {
    	if(!targetType.canAccept(goalType) && !isArray){
    		throw new JSLParseException(targetType.getBasicType() + " can not accept " + goalType.getBasicType());
    	} else if (!targetType.canAccept(goalType) && isArray){
    		throw new JSLParseException("Array has type " + targetType.getBasicType() + " and does not accept " + goalType.getBasicType());
    	}
    }
    
    /* Converts the JavaParser Type to the proper JSLType */
    public static JSLType typeToJSL(Type type){
    	if (type instanceof PrimitiveType) {
    		Primitive pType = ((PrimitiveType) type).getType();
    		if (pType == Primitive.DOUBLE || pType == Primitive.FLOAT)  {
    			return JSLType.DOUBLE;
    		} else if (pType == Primitive.LONG || pType == Primitive.INT || pType == Primitive.SHORT) {
    			return JSLType.LONG;
    		} else if (pType == Primitive.BOOLEAN){
    			return JSLType.BOOLEAN;
    		} else if (pType == Primitive.CHAR) {
    			return JSLType.STRING;
    		} else {
    			throw new JSLParseException("Primitive type not supported: " + type.toString());
    		}
    	} else if (type instanceof ReferenceType){
    		// TODO This is not... true(?) but for now good enough?
    		if(type instanceof ClassOrInterfaceType) {
    			return JSLType.STRING;
    		} else {
    			return JSLType.ARRAY;
    		}
    	} else if (type instanceof VoidType) {
    		return JSLType.VOID;
    	} else {
    		throw new JSLParseException("Type not supported: " + type.toString());
    	}
    }
    
    
    /* When a variable is declared but no value is specified, this function is called. Defaults longs and doubles to 0. */
    public static JSLTypedExpressionNode makeDefaultAssignment(VariableDeclarator expr) {
    	JSLType type = typeToJSL(expr.getType());
    	if(type == JSLType.DOUBLE) {
			return new JSLDoubleLiteralNode(0);
		} else if (type == JSLType.LONG) {
			return new JSLLongLiteralNode(0);
		} else {
			throw new JSLParseException("Dont know how to do a default assignment with this type: " + expr.getType());
		}
    }
    
    // Bit hacky way extract name string from UnaryExpression
    public static String getNameUnaryExpression(String expression){
    	String noPlus = expression.replace("+", "");
    	String noPlusMinus = noPlus.replace("-", "");
    	String noTilde = noPlusMinus.replace("~", "");
    	return noTilde.replace("!", "");
    }
    
    public static JSLTypedExpressionNode makeTypedBinary(String operator, JSLTypedExpressionNode leftNode, JSLTypedExpressionNode rightNode) {
    	// Make decision based on types of left and right node. So ugly but I did the best I could
    	JSLType leftType = getType(leftNode);
    	JSLType rightType = getType(rightNode);
    	if (leftType == JSLType.LONG && leftType.canAccept(rightType)) {
    		return makeLongBinary(operator, leftNode, rightNode);
    	} else if ((leftType == JSLType.DOUBLE || rightType == JSLType.DOUBLE) && (
    			leftType.canAccept(rightType) || rightType.canAccept(leftType))) {
    		return makeDoubleBinary(operator, leftNode, rightNode);
    	} else if (leftType == JSLType.STRING || rightType == JSLType.STRING) {
    		return makeStringBinary(operator, leftNode, rightNode);
    	} else if (leftType == JSLType.BOOLEAN && rightType == JSLType.BOOLEAN) {
    		return makeBooleanBinary(operator, leftNode, rightNode);
    	} else {
    		throw new JSLParseException("Binary " + operator + " is not supported for " + leftNode.getType().getBasicType() + " and " + rightNode.getType().getBasicType());
    	}
    }
   
    
    /* Extacts type of JSLTypedExpressionNode */
    public static JSLType getType(JSLTypedExpressionNode node) {
    	JSLType result = node.getType(); 
    	if (result.getBasicType() == JSLType.FUNCTION.getBasicType()) {
    		result = node.getType().getFunctionReturn();
    	}
    	return result;
    }
    
    public static JSLTypedExpressionNode makeStringBinary(String operator, JSLTypedExpressionNode leftNode, JSLTypedExpressionNode rightNode) {
    	JSLTypedExpressionNode result;
    	switch (operator) {
    		case "+":
    			result = JSLAddStringNodeGen.create(leftNode, rightNode);
    			break;
    		case "==":
    			result = JSLEqualNodeGen.create(leftNode, rightNode);
    			break;
    		default:
    			throw new JSLParseException("unexpected operation: " + operator);
    	}
    	return result;
    }
    
    /* Makes a write node of the proper type that writes the valueNode to the frameSlot */
    public static JSLTypedExpressionNode makeWriteLocalVariableNode(FrameSlot frameSlot, JSLTypedExpressionNode valueNode, JSLType type) {
		if(type == JSLType.DOUBLE) {
			return JSLWriteDoubleNodeGen.create(valueNode, frameSlot);
		} else if (type == JSLType.LONG) {
			return JSLWriteLongNodeGen.create(valueNode, frameSlot);
		} else if (type == JSLType.STRING) {
			return JSLWriteStringNodeGen.create(valueNode, frameSlot);
		} else if (type == JSLType.BOOLEAN) {
			return JSLWriteBooleanNodeGen.create(valueNode, frameSlot);
		} else {
			throw new JSLParseException("This type is not supported: " + type);
		}
	}
    
    /* Makes a ReadVariableNode of the proper type */
    public static JSLTypedExpressionNode createTypedRead(FrameSlot slot, JSLType type) {
    	if (type == JSLType.LONG) {
    		return JSLReadLongVariableNodeGen.create(slot);
    	} else if (type == JSLType.DOUBLE) {
    		return JSLReadDoubleVariableNodeGen.create(slot);    
    	} else if (type == JSLType.STRING) {
    		return JSLReadStringVariableNodeGen.create(slot);   
    	} else if (type == JSLType.BOOLEAN) {
    		return JSLReadBooleanVariableNodeGen.create(slot);
    	} else {
    		throw new JSLParseException("Do not know this type (yet) in creating read");
    	}
    }
    
    /* Makes a writeNode for arrayNodes with the proper type */
    public static JSLTypedExpressionNode makeWriteLocalArrayNode(FrameSlot frameSlot, JSLTypedExpressionNode index,
    		JSLTypedExpressionNode valueNode, JSLType type) {
    	JSLTypedExpressionNode result;
    	
    	JSLTypedExpressionNode array = JSLReadArrayVariableNodeGen.create(frameSlot);
    	
    	
    	if(type.getTypeOfArrayContents() == JSLType.DOUBLE) {
    		result = JSLWriteDoubleArrayElementNodeGen.create(array, index ,valueNode);
    	} else if (type.getTypeOfArrayContents() == JSLType.LONG){
    		result = JSLWriteLongArrayElementNodeGen.create(array, index, valueNode);
    	} else if (type.getTypeOfArrayContents() == JSLType.BOOLEAN){
    		result = JSLWriteBooleanArrayElementNodeGen.create(array, index, valueNode);
    	} else if (type.getTypeOfArrayContents() == JSLType.STRING)	{
    		result = JSLWriteStringArrayElementNodeGen.create(array, index, valueNode);    	
    	} else {
    		throw new JSLParseException("This array type is not supported " + type.getTypeOfArrayContents());
    	}
    	return result;
    }
    
//    public static JSLTypedExpressionNode applyAssignOperator(String operator, FrameSlot frameSlot, JSLTypedExpressionNode valueNode, JSLType type) {
//    	JSLTypedExpressionNode result;
//    	//TODO
//    	Object targetValue = frameSlot.getIdentifier();
//    	switch(operator) {
//    		case "=": 
//    			result = valueNode;
//    			break;
//    		case "+=":
//    			result = makeAddAssign(targetValue, valueNode, type);
//    	}
//    }
    
    private static JSLTypedExpressionNode makeBooleanBinary(String operator, JSLTypedExpressionNode leftNode, JSLTypedExpressionNode rightNode) {
    	JSLTypedExpressionNode result;
    	switch (operator) {
	    	case "==":
	            result = JSLEqualNodeGen.create(leftNode, rightNode);
	            break;
	        case "!=":
	            result = JSLLogicalNotNodeGen.create(JSLEqualNodeGen.create(leftNode, rightNode));
	            break;
	        case "&&":
	            result = new JSLLogicalAndNode(leftNode, rightNode);
	            break;
	        case "||":
	            result = new JSLLogicalOrNode(leftNode, rightNode);
	            break;
	        default:
	            throw new JSLParseException("unexpected operation: " + operator);
    	}
    	return result;
    }
    
    
    /**
     * Makes a binary expression node with the type Double.
     * @param operator The string containin1g the operator
     * @param leftNode The expression on the left of the operator
     * @param rightNode The expression on the right of the operator
     * @return The resulting JSLTypedExpressionNode of the expression
     */
    public static JSLTypedExpressionNode makeDoubleBinary(String operator, JSLTypedExpressionNode leftNode, JSLTypedExpressionNode rightNode) {
    	JSLTypedExpressionNode result;
    	switch (operator) {
	        case "+":
	            result = JSLAddDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case "*":
	            result = JSLMulDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case "/":
	            result = JSLDivDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case "-":
	            result = JSLSubDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case "%":
	            result = JSLModLongNodeGen.create(leftNode, rightNode);
	            break;
	        case "<":
	            result = JSLLessThanDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case "<=":
	            result = JSLLessOrEqualDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case ">":
	            result = JSLLogicalNotNodeGen.create(JSLLessOrEqualDoubleNodeGen.create(leftNode, rightNode));
	            break;
	        case ">=":
	            result = JSLLogicalNotNodeGen.create(JSLLessThanDoubleNodeGen.create(leftNode, rightNode));
	            break;
	        case "==":
	            result = JSLEqualNodeGen.create(leftNode, rightNode);
	            break;
	        case "!=":
	            result = JSLLogicalNotNodeGen.create(JSLEqualNodeGen.create(leftNode, rightNode));
	            break;
	        case "&&":
	            result = new JSLLogicalAndNode(leftNode, rightNode);
	            break;
	        case "||":
	            result = new JSLLogicalOrNode(leftNode, rightNode);
	            break;
	        default:
	            throw new JSLParseException("unexpected operation: " + operator);
    	}
    	return result;
    }
    
    /**
     * Makes a binary expression with the Long type. (Inputs are both Long)
     * @param operator The string containing the operator
     * @param leftNode The expression on the left of the operator
     * @param rightNode The expression on the right of the operator
     * @return The resulting JSLTypedExpressionNode of the expression
     */
    private static JSLTypedExpressionNode makeLongBinary(String operator, JSLTypedExpressionNode leftNode, JSLTypedExpressionNode rightNode) {
    	JSLTypedExpressionNode result;
    	switch (operator) {
        case "+":
            result = JSLAddLongNodeGen.create(leftNode, rightNode);
            break;
        case "*":
            result = JSLMulLongNodeGen.create(leftNode, rightNode);
            break;
        case "/":
            result = JSLDivLongNodeGen.create(leftNode, rightNode);
            break;
        case "-":
            result = JSLSubLongNodeGen.create(leftNode, rightNode);
            break;
        case "%":
            result = JSLModLongNodeGen.create(leftNode, rightNode);
            break;
        case "<":
            result = JSLLessThanLongNodeGen.create(leftNode, rightNode);
            break;
        case "<=":
            result = JSLLessOrEqualLongNodeGen.create(leftNode, rightNode);
            break;
        case ">":
            result = JSLLogicalNotNodeGen.create(JSLLessOrEqualLongNodeGen.create(leftNode, rightNode));
            break;
        case ">=":
            result = JSLLogicalNotNodeGen.create(JSLLessThanLongNodeGen.create(leftNode, rightNode));
            break;
        case "==":
            result = JSLEqualNodeGen.create(leftNode, rightNode);
            break;
        case "!=":
            result = JSLLogicalNotNodeGen.create(JSLEqualNodeGen.create(leftNode, rightNode));
            break;
        case "&&":
            result = new JSLLogicalAndNode(leftNode, rightNode);
            break;
        case "||":
            result = new JSLLogicalOrNode(leftNode, rightNode);
            break;
        case "<<":
            result = JSLBinaryShiftLeftNodeGen.create(leftNode, rightNode);
            break;
        case ">>":
            result = JSLBinaryShiftRightNodeGen.create(leftNode, rightNode);
            break;
        case "|":
            result = JSLBinaryInclusiveOrNodeGen.create(leftNode, rightNode);
            break; 
        case "^":
            result = JSLBinaryExclusiveOrNodeGen.create(leftNode, rightNode);
            break; 
        case "&":
            result = JSLBinaryAndNodeGen.create(leftNode, rightNode);
            break;             
         
        default:
            throw new JSLParseException("unexpected operation: " + operator);
    	}
    	return result;
    }
    
    
}