package com.BScProject.truffle.jsl.nodes.local;


import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.BScProject.truffle.jsl.nodes.JSLType;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChildren({@NodeChild("array"), @NodeChild("expressionNode")})
public abstract class JSLReadLongArrayValueNode extends JSLTypedExpressionNode {
	@Specialization
	public long read(long[] array, long index) {
		return array[(int) index];
	}
	
	@Override
	public JSLType getType() {
		return JSLType.LONG;
	}
}