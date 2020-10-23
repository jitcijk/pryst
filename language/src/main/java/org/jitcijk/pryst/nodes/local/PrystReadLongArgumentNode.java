package org.jitcijk.pryst.nodes.local;

import org.jitcijk.pryst.nodes.PrystType;
import org.jitcijk.pryst.nodes.PrystTypedExpressionNode;
import org.jitcijk.pryst.runtime.PrystRuntimeException;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;

public abstract class PrystReadLongArgumentNode extends PrystTypedExpressionNode {
	private final int index;
	
	private final BranchProfile outOfBoundsTaken = BranchProfile.create();
	
	public PrystReadLongArgumentNode(int index) {
		this.index = index;
	}
	
	@Specialization
	public long getLong(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		if (index < args.length) {
			return (long) args[index];
		} else {
			outOfBoundsTaken.enter();
			throw new PrystRuntimeException(this, "Parameter index out of bounds");
		}
	}
	
	public PrystType getType() {
		return PrystType.LONG;
	}
}