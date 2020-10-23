package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@NodeInfo(shortName = "const")
public final class JSLLongLiteralNode extends JSLTypedExpressionNode {
	
	private final long value;
	
	public JSLLongLiteralNode(long value) {
		this.value = value;
	}
	
	@Override
	public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
		return value;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}
	
	@Override
	public JSLType getType() {
		return JSLType.LONG;
	}
}