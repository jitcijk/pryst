package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.BScProject.truffle.jsl.runtime.JSLNull;
import com.oracle.truffle.api.frame.VirtualFrame;

//
public class JSLNopExpressionNode extends JSLTypedExpressionNode {

	@Override
	public JSLType getType() {
		return JSLType.VOID;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return JSLNull.SINGLETON;
	}
	
}