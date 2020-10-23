package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLExpressionNode;
import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;

public abstract class JSLShortCircuitNode extends JSLTypedExpressionNode {
	@Child private JSLExpressionNode left;
	@Child private JSLExpressionNode right;
	
	private final ConditionProfile evaluateRightProfile = ConditionProfile.createCountingProfile();
	
	public JSLShortCircuitNode(JSLExpressionNode left, JSLExpressionNode right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return executeBoolean(frame);
	}
	
	@Override 
	public boolean executeBoolean(VirtualFrame frame) {
		boolean leftValue;
		try {
			leftValue = left.executeBoolean(frame);
		} catch (UnexpectedResultException ex) {
			throw new UnsupportedSpecializationException(this, new Node[]{left,right}, new Object[]{ex.getResult(), null});
		}
		
		boolean rightValue;
		try {
			if (evaluateRightProfile.profile(isEvaluateRight(leftValue))) {
                rightValue = right.executeBoolean(frame);
            } else {
                rightValue = false;
            }
		} catch (UnexpectedResultException ex) {
			throw new UnsupportedSpecializationException(this, new Node[]{left, right}, new Object[]{leftValue, ex.getMessage()});
		}
		
		return execute(leftValue, rightValue);
	}
	
	public JSLType getType() {
		return JSLType.BOOLEAN;
	}
	
	protected abstract boolean isEvaluateRight(boolean leftValue);
	
	protected abstract boolean execute(boolean leftValue, boolean rightValue);
	
}