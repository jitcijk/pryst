package com.BScProject.truffle.jsl.nodes.expression;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "~")
@NodeChildren({@NodeChild("node")})
public abstract class JSLBinaryNotNode extends JSLTypedExpressionNode {
	@Specialization
	protected long not(long value) {
		return ~value;
	}
	
	@Override
	public JSLType getType() {
		return JSLType.LONG;
	}
}