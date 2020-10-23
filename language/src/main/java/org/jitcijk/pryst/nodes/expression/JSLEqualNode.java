package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.BScProject.truffle.jsl.runtime.JSLFunction;
import com.BScProject.truffle.jsl.runtime.JSLNull;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "==")
@NodeChildren({@NodeChild("leftNode"), @NodeChild("rightNode")})
public abstract class JSLEqualNode extends JSLTypedExpressionNode {
	
	@Specialization
	protected boolean equal(long left, long right) {
		return left==right;
	}
	
	@Specialization
	protected boolean equal(double left, double right) {
		// Doubles comparing with ==?
		return left==right;
	}
	
	@Specialization
	protected boolean equal(boolean left, boolean right) {
		return left == right;
	}
	
	@Specialization
	protected boolean equal(String left, String right) {
		return left.equals(right);
	}
	
	@Specialization
	protected boolean equal(JSLFunction left, JSLFunction right) {
		return left == right;
	}
	
	@Specialization
	protected boolean equal(JSLNull left, JSLNull right) {
		return left == right;
	}
	
	@Specialization(guards = "left.getClass() != right.getClass()")
	protected boolean equal(Object left, Object right) {
		return false;
	}
	
	public JSLType getType() {
		return JSLType.BOOLEAN;
	}
	
}