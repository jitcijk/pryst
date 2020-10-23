package org.jitcijk.pryst.nodes.local;

import org.jitcijk.pryst.nodes.PrystType;
import org.jitcijk.pryst.nodes.PrystTypedExpressionNode;
import org.jitcijk.pryst.runtime.PrystRuntimeException;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeChildren({@NodeChild("array"), @NodeChild("index"), @NodeChild("value")})
@NodeInfo(shortName="=")
public abstract class PrystWriteDoubleArrayElementNode extends PrystTypedExpressionNode {
	@Specialization
	protected double writeDoubleArrayElement(double[] array, long index, double value) {
		try {
			array[(int) index] = value;
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new PrystRuntimeException(this, "index "+index+" out of bounds");
		}
		return value;
	}
	
	@Override
	public PrystType getType() {
		return PrystType.DOUBLE;
	}
}