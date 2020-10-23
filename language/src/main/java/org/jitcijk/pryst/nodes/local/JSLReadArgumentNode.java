package com.BScProject.truffle.jsl.nodes.local;

import com.BScProject.truffle.jsl.nodes.JSLExpressionNode;
import com.BScProject.truffle.jsl.runtime.JSLNull;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;

public class JSLReadArgumentNode extends JSLExpressionNode {
	private final int index;
	
	private final BranchProfile outOfBoundsTaken = BranchProfile.create();
	
	public JSLReadArgumentNode(int index) {
		this.index = index;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		if(index < args.length) {
			return args[index];
		} else {
			outOfBoundsTaken.enter();
			return JSLNull.SINGLETON;
		}
	}
}