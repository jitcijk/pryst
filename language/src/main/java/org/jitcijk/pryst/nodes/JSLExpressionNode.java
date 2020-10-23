package com.BScProject.truffle.jsl.nodes;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.Instrumentable;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.BScProject.truffle.jsl.nodes.JSLTypesGen;

@TypeSystemReference(JSLTypes.class)
@NodeInfo(description = "The abstract base node for all expressions")
@Instrumentable(factory = JSLExpressionNodeWrapper.class)
public abstract class JSLExpressionNode extends JSLStatementNode {
	
	// The execute method for when no specialization is possible
	// Every subclass must implement this since it is the most generic case
	public abstract Object executeGeneric(VirtualFrame frame);
	
	
	// Return type is discarded
	@Override 
	public void executeVoid(VirtualFrame frame){
		executeGeneric(frame);
	}
	
	// Specialized executes
	public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
		return JSLTypesGen.expectLong(executeGeneric(frame));
	}
	
	public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
		return JSLTypesGen.expectDouble(executeGeneric(frame));
	}
	
	public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
		return JSLTypesGen.expectBoolean(executeGeneric(frame));
	}
}