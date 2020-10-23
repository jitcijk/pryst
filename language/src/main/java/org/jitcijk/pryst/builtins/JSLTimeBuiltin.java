package com.BScProject.truffle.jsl.builtins;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "time")
public abstract class JSLTimeBuiltin extends JSLBuiltinNode {
	
	@Specialization
	@TruffleBoundary
	public long time() {
		long time = System.nanoTime();
		System.out.println(time);
		return time;
	}
}