package org.jitcijk.pryst.nodes;

import org.jitcijk.pryst.nodes.PrystType;

public abstract class PrystTypedExpressionNode extends PrystExpressionNode {
	public abstract PrystType getType();
}