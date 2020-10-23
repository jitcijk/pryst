package com.BScProject.truffle.jsl.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class JSLNopExpression extends JSLTypedExpressionNode {
	@Override
	public void executeVoid(VirtualFrame frame) {
	}
	
	@Override
	public JSLType getType() {
		return JSLType.BOOLEAN;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return true;
	}
}