package org.jitcijk.pryst.nodes.expression;

import org.jitcijk.pryst.nodes.PrystType;
import org.jitcijk.pryst.nodes.PrystTypedExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "|")
@NodeChildren({@NodeChild("leftNode"), @NodeChild("rightNode")})
public abstract class PrystBinaryInclusiveOrNode extends PrystTypedExpressionNode {
	@Specialization
	protected long or(long left, long right) {
		return left | right;
	}
	
	@Override
	public PrystType getType() {
		return PrystType.LONG;
	}
}