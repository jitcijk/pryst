package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLExpressionNode;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "||")
public final class JSLLogicalOrNode extends JSLShortCircuitNode {
	
	public JSLLogicalOrNode(JSLExpressionNode left, JSLExpressionNode right) {
		super(left, right);
	}
	
	@Override
	protected boolean isEvaluateRight(boolean left) {
		return !left;
	}
	
	@Override
	protected boolean execute(boolean left, boolean right) {
		return left || right;
	}
}