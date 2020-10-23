package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@NodeInfo(description = "A parenthesized expression")
public class JSLParenExpressionNode extends JSLTypedExpressionNode {
	@Child private JSLTypedExpressionNode expression;
	private JSLType type;
	
	public JSLParenExpressionNode(JSLTypedExpressionNode expression) {
		this.expression = expression;
		this.type = expression.getType();
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return expression.executeGeneric(frame);
	}
	
	@Override
	public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
		return expression.executeLong(frame);
	}
	
	@Override
	public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
		return expression.executeDouble(frame);
	}
	
	@Override
	public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
		return expression.executeBoolean(frame);
	}
	
	@Override
	public JSLType getType() {
		return type;
	}
}