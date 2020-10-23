package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@NodeInfo(shortName = "const")
public final class JSLBooleanLiteralNode extends JSLTypedExpressionNode {
	private final boolean value;
	
	public JSLBooleanLiteralNode(boolean value) {
		this.value = value;
	}
	
	@Override
	public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
		return value;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}
	
	@Override 
	public JSLType getType() {
		return JSLType.BOOLEAN;
	}
}