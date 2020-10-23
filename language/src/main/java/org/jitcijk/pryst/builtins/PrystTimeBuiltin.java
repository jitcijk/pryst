package org.jitcijk.pryst.builtins;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "time")
public abstract class PrystTimeBuiltin extends PrystBuiltinNode {
	
	@Specialization
	@TruffleBoundary
	public long time() {
		long time = System.nanoTime();
		System.out.println(time);
		return time;
	}
}