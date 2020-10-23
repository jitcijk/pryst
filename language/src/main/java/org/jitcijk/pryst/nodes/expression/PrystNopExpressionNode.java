package org.jitcijk.pryst.nodes.expression;

import org.jitcijk.pryst.nodes.PrystType;
import org.jitcijk.pryst.nodes.PrystTypedExpressionNode;
import org.jitcijk.pryst.runtime.PrystNull;
import com.oracle.truffle.api.frame.VirtualFrame;

//
public class PrystNopExpressionNode extends PrystTypedExpressionNode {

	@Override
	public PrystType getType() {
		return PrystType.VOID;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return PrystNull.SINGLETON;
	}
	
}