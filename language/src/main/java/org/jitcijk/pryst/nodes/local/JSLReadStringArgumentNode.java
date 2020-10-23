package com.BScProject.truffle.jsl.nodes.local;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.BScProject.truffle.jsl.runtime.JSLRuntimeException;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;

public abstract class JSLReadStringArgumentNode extends JSLTypedExpressionNode {
	private final int index;
	
	private final BranchProfile outOfBoundsTaken = BranchProfile.create();
	
	public JSLReadStringArgumentNode(int index) {
		this.index = index;
	}
	
	@Specialization
	public String getString(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		if (index < args.length) {
			return (String) args[index];
		} else {
			outOfBoundsTaken.enter();
			
			throw new JSLRuntimeException(this, "Parameter index out of bounds");
		}
	}	
	
	
	public JSLType getType() {
		return JSLType.STRING;
	}
}