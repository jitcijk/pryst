package org.jitcijk.pryst.parser;

import org.jitcijk.pryst.nodes.PrystType;
import org.jitcijk.pryst.nodes.PrystTypedExpressionNode;
import org.jitcijk.pryst.nodes.expression.PrystAddDoubleNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystAddLongNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystAddStringNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystBinaryAndNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystBinaryExclusiveOrNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystBinaryInclusiveOrNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystBinaryShiftLeftNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystBinaryShiftRightNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystDivDoubleNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystDivLongNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystDoubleLiteralNode;
import org.jitcijk.pryst.nodes.expression.PrystEqualNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystLessOrEqualDoubleNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystLessOrEqualLongNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystLessThanDoubleNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystLessThanLongNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystLogicalAndNode;
import org.jitcijk.pryst.nodes.expression.PrystLogicalNotNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystLogicalOrNode;
import org.jitcijk.pryst.nodes.expression.PrystLongLiteralNode;
import org.jitcijk.pryst.nodes.expression.PrystModLongNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystMulDoubleNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystMulLongNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystSubDoubleNodeGen;
import org.jitcijk.pryst.nodes.expression.PrystSubLongNodeGen;
import org.jitcijk.pryst.nodes.local.PrystReadArrayVariableNodeGen;
import org.jitcijk.pryst.nodes.local.PrystReadBooleanVariableNodeGen;
import org.jitcijk.pryst.nodes.local.PrystReadDoubleVariableNodeGen;
import org.jitcijk.pryst.nodes.local.PrystReadLongVariableNodeGen;
import org.jitcijk.pryst.nodes.local.PrystReadStringVariableNodeGen;
import org.jitcijk.pryst.nodes.local.PrystWriteBooleanArrayElementNodeGen;
import org.jitcijk.pryst.nodes.local.PrystWriteBooleanNodeGen;
import org.jitcijk.pryst.nodes.local.PrystWriteDoubleArrayElementNodeGen;
import org.jitcijk.pryst.nodes.local.PrystWriteDoubleNodeGen;
import org.jitcijk.pryst.nodes.local.PrystWriteLongArrayElementNodeGen;
import org.jitcijk.pryst.nodes.local.PrystWriteLongNodeGen;
import org.jitcijk.pryst.nodes.local.PrystWriteStringArrayElementNodeGen;
import org.jitcijk.pryst.nodes.local.PrystWriteStringNodeGen;
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
 * used to reduce size of PrystNodeFactory
 *
 */
public final class PrystFactoryUtility {
	
    /* Checks if targetType can accept goalType, throws parseException if not the case. 
     * Sometimes this will not result in an exception but at runtime we will encounter a invalid conversion
     * due to the fact that we use TypedNodes (if we call writeDouble with a long, it wont accept it).
     */
    public static void doTypeCheck(PrystType targetType, PrystType goalType, boolean isArray) {
    	if(!targetType.canAccept(goalType) && !isArray){
    		throw new PrystParseException(targetType.getBasicType() + " can not accept " + goalType.getBasicType());
    	} else if (!targetType.canAccept(goalType) && isArray){
    		throw new PrystParseException("Array has type " + targetType.getBasicType() + " and does not accept " + goalType.getBasicType());
    	}
    }
    
    /* Converts the JavaParser Type to the proper PrystType */
    public static PrystType typeToPryst(Type type){
    	if (type instanceof PrimitiveType) {
    		Primitive pType = ((PrimitiveType) type).getType();
    		if (pType == Primitive.DOUBLE || pType == Primitive.FLOAT)  {
    			return PrystType.DOUBLE;
    		} else if (pType == Primitive.LONG || pType == Primitive.INT || pType == Primitive.SHORT) {
    			return PrystType.LONG;
    		} else if (pType == Primitive.BOOLEAN){
    			return PrystType.BOOLEAN;
    		} else if (pType == Primitive.CHAR) {
    			return PrystType.STRING;
    		} else {
    			throw new PrystParseException("Primitive type not supported: " + type.toString());
    		}
    	} else if (type instanceof ReferenceType){
    		// TODO This is not... true(?) but for now good enough?
    		if(type instanceof ClassOrInterfaceType) {
    			return PrystType.STRING;
    		} else {
    			return PrystType.ARRAY;
    		}
    	} else if (type instanceof VoidType) {
    		return PrystType.VOID;
    	} else {
    		throw new PrystParseException("Type not supported: " + type.toString());
    	}
    }
    
    
    /* When a variable is declared but no value is specified, this function is called. Defaults longs and doubles to 0. */
    public static PrystTypedExpressionNode makeDefaultAssignment(VariableDeclarator expr) {
    	PrystType type = typeToPryst(expr.getType());
    	if(type == PrystType.DOUBLE) {
			return new PrystDoubleLiteralNode(0);
		} else if (type == PrystType.LONG) {
			return new PrystLongLiteralNode(0);
		} else {
			throw new PrystParseException("Dont know how to do a default assignment with this type: " + expr.getType());
		}
    }
    
    // Bit hacky way extract name string from UnaryExpression
    public static String getNameUnaryExpression(String expression){
    	String noPlus = expression.replace("+", "");
    	String noPlusMinus = noPlus.replace("-", "");
    	String noTilde = noPlusMinus.replace("~", "");
    	return noTilde.replace("!", "");
    }
    
    public static PrystTypedExpressionNode makeTypedBinary(String operator, PrystTypedExpressionNode leftNode, PrystTypedExpressionNode rightNode) {
    	// Make decision based on types of left and right node. So ugly but I did the best I could
    	PrystType leftType = getType(leftNode);
    	PrystType rightType = getType(rightNode);
    	if (leftType == PrystType.LONG && leftType.canAccept(rightType)) {
    		return makeLongBinary(operator, leftNode, rightNode);
    	} else if ((leftType == PrystType.DOUBLE || rightType == PrystType.DOUBLE) && (
    			leftType.canAccept(rightType) || rightType.canAccept(leftType))) {
    		return makeDoubleBinary(operator, leftNode, rightNode);
    	} else if (leftType == PrystType.STRING || rightType == PrystType.STRING) {
    		return makeStringBinary(operator, leftNode, rightNode);
    	} else if (leftType == PrystType.BOOLEAN && rightType == PrystType.BOOLEAN) {
    		return makeBooleanBinary(operator, leftNode, rightNode);
    	} else {
    		throw new PrystParseException("Binary " + operator + " is not supported for " + leftNode.getType().getBasicType() + " and " + rightNode.getType().getBasicType());
    	}
    }
   
    
    /* Extacts type of PrystTypedExpressionNode */
    public static PrystType getType(PrystTypedExpressionNode node) {
    	PrystType result = node.getType(); 
    	if (result.getBasicType() == PrystType.FUNCTION.getBasicType()) {
    		result = node.getType().getFunctionReturn();
    	}
    	return result;
    }
    
    public static PrystTypedExpressionNode makeStringBinary(String operator, PrystTypedExpressionNode leftNode, PrystTypedExpressionNode rightNode) {
    	PrystTypedExpressionNode result;
    	switch (operator) {
    		case "+":
    			result = PrystAddStringNodeGen.create(leftNode, rightNode);
    			break;
    		case "==":
    			result = PrystEqualNodeGen.create(leftNode, rightNode);
    			break;
    		default:
    			throw new PrystParseException("unexpected operation: " + operator);
    	}
    	return result;
    }
    
    /* Makes a write node of the proper type that writes the valueNode to the frameSlot */
    public static PrystTypedExpressionNode makeWriteLocalVariableNode(FrameSlot frameSlot, PrystTypedExpressionNode valueNode, PrystType type) {
		if(type == PrystType.DOUBLE) {
			return PrystWriteDoubleNodeGen.create(valueNode, frameSlot);
		} else if (type == PrystType.LONG) {
			return PrystWriteLongNodeGen.create(valueNode, frameSlot);
		} else if (type == PrystType.STRING) {
			return PrystWriteStringNodeGen.create(valueNode, frameSlot);
		} else if (type == PrystType.BOOLEAN) {
			return PrystWriteBooleanNodeGen.create(valueNode, frameSlot);
		} else {
			throw new PrystParseException("This type is not supported: " + type);
		}
	}
    
    /* Makes a ReadVariableNode of the proper type */
    public static PrystTypedExpressionNode createTypedRead(FrameSlot slot, PrystType type) {
    	if (type == PrystType.LONG) {
    		return PrystReadLongVariableNodeGen.create(slot);
    	} else if (type == PrystType.DOUBLE) {
    		return PrystReadDoubleVariableNodeGen.create(slot);    
    	} else if (type == PrystType.STRING) {
    		return PrystReadStringVariableNodeGen.create(slot);   
    	} else if (type == PrystType.BOOLEAN) {
    		return PrystReadBooleanVariableNodeGen.create(slot);
    	} else {
    		throw new PrystParseException("Do not know this type (yet) in creating read");
    	}
    }
    
    /* Makes a writeNode for arrayNodes with the proper type */
    public static PrystTypedExpressionNode makeWriteLocalArrayNode(FrameSlot frameSlot, PrystTypedExpressionNode index,
    		PrystTypedExpressionNode valueNode, PrystType type) {
    	PrystTypedExpressionNode result;
    	
    	PrystTypedExpressionNode array = PrystReadArrayVariableNodeGen.create(frameSlot);
    	
    	
    	if(type.getTypeOfArrayContents() == PrystType.DOUBLE) {
    		result = PrystWriteDoubleArrayElementNodeGen.create(array, index ,valueNode);
    	} else if (type.getTypeOfArrayContents() == PrystType.LONG){
    		result = PrystWriteLongArrayElementNodeGen.create(array, index, valueNode);
    	} else if (type.getTypeOfArrayContents() == PrystType.BOOLEAN){
    		result = PrystWriteBooleanArrayElementNodeGen.create(array, index, valueNode);
    	} else if (type.getTypeOfArrayContents() == PrystType.STRING)	{
    		result = PrystWriteStringArrayElementNodeGen.create(array, index, valueNode);    	
    	} else {
    		throw new PrystParseException("This array type is not supported " + type.getTypeOfArrayContents());
    	}
    	return result;
    }
    
//    public static PrystTypedExpressionNode applyAssignOperator(String operator, FrameSlot frameSlot, PrystTypedExpressionNode valueNode, PrystType type) {
//    	PrystTypedExpressionNode result;
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
    
    private static PrystTypedExpressionNode makeBooleanBinary(String operator, PrystTypedExpressionNode leftNode, PrystTypedExpressionNode rightNode) {
    	PrystTypedExpressionNode result;
    	switch (operator) {
	    	case "==":
	            result = PrystEqualNodeGen.create(leftNode, rightNode);
	            break;
	        case "!=":
	            result = PrystLogicalNotNodeGen.create(PrystEqualNodeGen.create(leftNode, rightNode));
	            break;
	        case "&&":
	            result = new PrystLogicalAndNode(leftNode, rightNode);
	            break;
	        case "||":
	            result = new PrystLogicalOrNode(leftNode, rightNode);
	            break;
	        default:
	            throw new PrystParseException("unexpected operation: " + operator);
    	}
    	return result;
    }
    
    
    /**
     * Makes a binary expression node with the type Double.
     * @param operator The string containin1g the operator
     * @param leftNode The expression on the left of the operator
     * @param rightNode The expression on the right of the operator
     * @return The resulting PrystTypedExpressionNode of the expression
     */
    public static PrystTypedExpressionNode makeDoubleBinary(String operator, PrystTypedExpressionNode leftNode, PrystTypedExpressionNode rightNode) {
    	PrystTypedExpressionNode result;
    	switch (operator) {
	        case "+":
	            result = PrystAddDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case "*":
	            result = PrystMulDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case "/":
	            result = PrystDivDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case "-":
	            result = PrystSubDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case "%":
	            result = PrystModLongNodeGen.create(leftNode, rightNode);
	            break;
	        case "<":
	            result = PrystLessThanDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case "<=":
	            result = PrystLessOrEqualDoubleNodeGen.create(leftNode, rightNode);
	            break;
	        case ">":
	            result = PrystLogicalNotNodeGen.create(PrystLessOrEqualDoubleNodeGen.create(leftNode, rightNode));
	            break;
	        case ">=":
	            result = PrystLogicalNotNodeGen.create(PrystLessThanDoubleNodeGen.create(leftNode, rightNode));
	            break;
	        case "==":
	            result = PrystEqualNodeGen.create(leftNode, rightNode);
	            break;
	        case "!=":
	            result = PrystLogicalNotNodeGen.create(PrystEqualNodeGen.create(leftNode, rightNode));
	            break;
	        case "&&":
	            result = new PrystLogicalAndNode(leftNode, rightNode);
	            break;
	        case "||":
	            result = new PrystLogicalOrNode(leftNode, rightNode);
	            break;
	        default:
	            throw new PrystParseException("unexpected operation: " + operator);
    	}
    	return result;
    }
    
    /**
     * Makes a binary expression with the Long type. (Inputs are both Long)
     * @param operator The string containing the operator
     * @param leftNode The expression on the left of the operator
     * @param rightNode The expression on the right of the operator
     * @return The resulting PrystTypedExpressionNode of the expression
     */
    private static PrystTypedExpressionNode makeLongBinary(String operator, PrystTypedExpressionNode leftNode, PrystTypedExpressionNode rightNode) {
    	PrystTypedExpressionNode result;
    	switch (operator) {
        case "+":
            result = PrystAddLongNodeGen.create(leftNode, rightNode);
            break;
        case "*":
            result = PrystMulLongNodeGen.create(leftNode, rightNode);
            break;
        case "/":
            result = PrystDivLongNodeGen.create(leftNode, rightNode);
            break;
        case "-":
            result = PrystSubLongNodeGen.create(leftNode, rightNode);
            break;
        case "%":
            result = PrystModLongNodeGen.create(leftNode, rightNode);
            break;
        case "<":
            result = PrystLessThanLongNodeGen.create(leftNode, rightNode);
            break;
        case "<=":
            result = PrystLessOrEqualLongNodeGen.create(leftNode, rightNode);
            break;
        case ">":
            result = PrystLogicalNotNodeGen.create(PrystLessOrEqualLongNodeGen.create(leftNode, rightNode));
            break;
        case ">=":
            result = PrystLogicalNotNodeGen.create(PrystLessThanLongNodeGen.create(leftNode, rightNode));
            break;
        case "==":
            result = PrystEqualNodeGen.create(leftNode, rightNode);
            break;
        case "!=":
            result = PrystLogicalNotNodeGen.create(PrystEqualNodeGen.create(leftNode, rightNode));
            break;
        case "&&":
            result = new PrystLogicalAndNode(leftNode, rightNode);
            break;
        case "||":
            result = new PrystLogicalOrNode(leftNode, rightNode);
            break;
        case "<<":
            result = PrystBinaryShiftLeftNodeGen.create(leftNode, rightNode);
            break;
        case ">>":
            result = PrystBinaryShiftRightNodeGen.create(leftNode, rightNode);
            break;
        case "|":
            result = PrystBinaryInclusiveOrNodeGen.create(leftNode, rightNode);
            break; 
        case "^":
            result = PrystBinaryExclusiveOrNodeGen.create(leftNode, rightNode);
            break; 
        case "&":
            result = PrystBinaryAndNodeGen.create(leftNode, rightNode);
            break;             
         
        default:
            throw new PrystParseException("unexpected operation: " + operator);
    	}
    	return result;
    }
    
    
}