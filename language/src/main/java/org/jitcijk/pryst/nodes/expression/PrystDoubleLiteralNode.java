package org.jitcijk.pryst.nodes.expression;

import org.jitcijk.pryst.nodes.PrystType;
import org.jitcijk.pryst.nodes.PrystTypedExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@NodeInfo(shortName = "const")
public final class PrystDoubleLiteralNode extends PrystTypedExpressionNode {
	private final double value;
	
	public PrystDoubleLiteralNode(double value) {
		this.value = value;
	}
	
	@Override
	public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
		return value;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}
	
	@Override 
	public PrystType getType() {
		return PrystType.DOUBLE;
	}
}