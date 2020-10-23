package com.BScProject.truffle.jsl.nodes.local;

import com.BScProject.truffle.jsl.nodes.JSLExpressionNode;
import com.BScProject.truffle.jsl.nodes.JSLStatementNode;
import com.BScProject.truffle.jsl.runtime.JSLRuntimeException;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class JSLCreateLocalLongArrayNode extends JSLStatementNode {
	private final FrameSlot frameSlot;
	@Child private JSLExpressionNode size;
	
	public JSLCreateLocalLongArrayNode(FrameSlot frameSlot, JSLExpressionNode size) {
		this.frameSlot = frameSlot;
		this.size = size;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		int s;
		try {
			s = (int) size.executeLong(frame);
		} catch (UnexpectedResultException ex) {
			throw new JSLRuntimeException(this, ex);
		}
		frame.setObject(frameSlot, new long[s]);
	}
}

