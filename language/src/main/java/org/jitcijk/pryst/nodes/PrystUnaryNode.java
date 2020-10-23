package org.jitcijk.pryst.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;

@NodeChildren({@NodeChild("node")})
public abstract class PrystUnaryNode extends PrystExpressionNode {
	
}