package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "<=")
@NodeChildren({@NodeChild("leftNode"), @NodeChild("rightNode")})
public abstract class JSLLessOrEqualLongNode extends JSLTypedExpressionNode {
	
	@Specialization
	protected boolean lessOrEqual(long left, long right) {
		return left <= right;
	}
	
	public JSLType getType() {
		return JSLType.BOOLEAN;
	}
}