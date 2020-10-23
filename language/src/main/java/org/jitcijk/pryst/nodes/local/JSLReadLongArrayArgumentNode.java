package com.BScProject.truffle.jsl.nodes.local;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.BScProject.truffle.jsl.runtime.JSLRuntimeException;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;

public abstract class JSLReadLongArrayArgumentNode extends JSLTypedExpressionNode {
	private final int index;
	
	private final BranchProfile outOfBoundsTaken = BranchProfile.create();
	
	public JSLReadLongArrayArgumentNode(int index) {
		this.index = index;
	}
	
	@Specialization
	public long[] getArray(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		if (index < args.length) {
			return (long[]) args[index];
		} else {
			outOfBoundsTaken.enter();
			throw new JSLRuntimeException(this, "Parameter index out of bounds");
		}
	}
	
	public JSLType getType() {
		return JSLType.ARRAY.setArrayType(JSLType.LONG);
	}
}