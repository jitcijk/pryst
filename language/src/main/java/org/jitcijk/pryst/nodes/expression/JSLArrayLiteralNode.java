package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "const")
public final class JSLArrayLiteralNode extends JSLTypedExpressionNode {
	private final Object[] value;
	
	public JSLArrayLiteralNode(Object[] value) {
		this.value = value;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}
	
	@Override 
	public JSLType getType() {
		return JSLType.ARRAY;
	}
}