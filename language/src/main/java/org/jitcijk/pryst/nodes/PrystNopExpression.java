package org.jitcijk.pryst.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class PrystNopExpression extends PrystTypedExpressionNode {
	@Override
	public void executeVoid(VirtualFrame frame) {
	}
	
	@Override
	public PrystType getType() {
		return PrystType.BOOLEAN;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return true;
	}
}