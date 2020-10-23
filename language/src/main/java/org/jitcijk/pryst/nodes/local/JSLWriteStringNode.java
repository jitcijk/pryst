package com.BScProject.truffle.jsl.nodes.local;

import com.BScProject.truffle.jsl.nodes.JSLType;
import com.BScProject.truffle.jsl.nodes.JSLTypedExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class JSLWriteStringNode extends JSLTypedExpressionNode {
	protected abstract FrameSlot getSlot();
	
	@Specialization
	protected String writeString(VirtualFrame frame, String value) {
		getSlot().setKind(FrameSlotKind.Object);
		frame.setObject(getSlot(), value);
		return value;
	}
	
	public JSLType getType() {
		return JSLType.STRING;
	}
}