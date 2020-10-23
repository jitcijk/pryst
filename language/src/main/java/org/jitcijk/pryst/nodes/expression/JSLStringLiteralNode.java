package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "const")
public final class JSLStringLiteralNode extends JSLTypedExpressionNode {
	private final String value;
	
	public JSLStringLiteralNode(String value) {
		this.value = value;
	}
	
	@Override
	public String executeGeneric(VirtualFrame frame) {
		return value;
	}
	
	public JSLType getType() {
		return JSLType.STRING;
	}
}