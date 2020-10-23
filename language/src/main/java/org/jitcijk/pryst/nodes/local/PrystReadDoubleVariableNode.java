package org.jitcijk.pryst.nodes.local;

import org.jitcijk.pryst.nodes.PrystType;
import org.jitcijk.pryst.nodes.PrystTypedExpressionNode;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class PrystReadDoubleVariableNode extends PrystTypedExpressionNode {
	
	protected abstract FrameSlot getSlot();
	
	@Specialization
	protected double readDouble(VirtualFrame frame) {
		return FrameUtil.getDoubleSafe(frame, getSlot());
	}
	
	public PrystType getType() {
		return PrystType.DOUBLE;
	}
}