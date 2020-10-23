package org.jitcijk.pryst.nodes.expression;

import org.jitcijk.pryst.nodes.PrystType;
import org.jitcijk.pryst.nodes.PrystTypedExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@NodeInfo(shortName = "const")
public final class PrystBooleanLiteralNode extends PrystTypedExpressionNode {
	private final boolean value;
	
	public PrystBooleanLiteralNode(boolean value) {
		this.value = value;
	}
	
	@Override
	public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
		return value;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}
	
	@Override 
	public PrystType getType() {
		return PrystType.BOOLEAN;
	}
}