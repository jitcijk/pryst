package org.jitcijk.pryst.nodes.expression;

import org.jitcijk.pryst.nodes.PrystType;
import org.jitcijk.pryst.nodes.PrystTypedExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "const")
public final class PrystArrayLiteralNode extends PrystTypedExpressionNode {
	private final Object[] value;
	
	public PrystArrayLiteralNode(Object[] value) {
		this.value = value;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}
	
	@Override 
	public PrystType getType() {
		return PrystType.ARRAY;
	}
}