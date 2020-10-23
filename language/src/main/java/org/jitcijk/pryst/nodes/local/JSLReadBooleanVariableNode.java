package com.BScProject.truffle.jsl.nodes.local;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class JSLReadBooleanVariableNode extends JSLTypedExpressionNode {
	
	protected abstract FrameSlot getSlot();
	
	@Specialization
	protected boolean readBoolean(VirtualFrame frame) {
		return FrameUtil.getBooleanSafe(frame, getSlot());
	}
	
	public JSLType getType() {
		return JSLType.BOOLEAN;
	}
}