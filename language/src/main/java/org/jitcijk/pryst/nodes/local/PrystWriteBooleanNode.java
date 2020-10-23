package org.jitcijk.pryst.nodes.local;

import org.jitcijk.pryst.nodes.PrystType;
import org.jitcijk.pryst.nodes.PrystTypedExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class PrystWriteBooleanNode extends PrystTypedExpressionNode {
	protected abstract FrameSlot getSlot();
	
	@Specialization
	protected boolean writeDouble(VirtualFrame frame, boolean value) {
		getSlot().setKind(FrameSlotKind.Double);
		frame.setBoolean(getSlot(), value);
		return value;
	}
	
	public PrystType getType() {
		return PrystType.BOOLEAN;
	}
}