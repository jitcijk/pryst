package org.jitcijk.pryst.nodes.local;


import org.jitcijk.pryst.nodes.PrystTypedExpressionNode;
import com.oracle.truffle.api.dsl.NodeChild;
import org.jitcijk.pryst.nodes.PrystType;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChildren({@NodeChild("array"), @NodeChild("expressionNode")})
public abstract class PrystReadStringArrayValueNode extends PrystTypedExpressionNode {
	@Specialization
	public String read(String[] array, long index) {
		return array[(int) index];
	}
	
	@Override
	public PrystType getType() {
		return PrystType.STRING;
	}
}