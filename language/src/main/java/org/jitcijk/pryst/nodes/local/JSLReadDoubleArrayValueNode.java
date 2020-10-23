package com.BScProject.truffle.jsl.nodes.local;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChildren({@NodeChild("array"), @NodeChild("expressionNode")})
public abstract class JSLReadDoubleArrayValueNode extends JSLTypedExpressionNode {
	@Specialization
	public double read(double[] array, long index) {
		return array[(int) index];
	}
	
	@Override
	public JSLType getType() {
		return JSLType.DOUBLE;
	}
}