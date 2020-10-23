package com.BScProject.truffle.jsl.nodes;

import com.BScProject.truffle.jsl.nodes.JSLType;

public abstract class JSLTypedExpressionNode extends JSLExpressionNode {
	public abstract JSLType getType();
}