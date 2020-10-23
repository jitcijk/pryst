package org.jitcijk.pryst.runtime;

import org.jitcijk.pryst.PrystLanguage;
import com.oracle.truffle.api.interop.CanResolve;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;

@MessageResolution(receiverType = PrystNull.class, language = PrystLanguage.class)
public class PrystNullMessageResolution {
	
	@Resolve(message = "IS_NULL")
	public abstract static class PrystForeignIsNullNode extends Node {
		public Object access(Object receiver) {
			return PrystNull.SINGLETON == receiver;
		}
	}
	
	@CanResolve
	public abstract static class CheckNull extends Node {
		public static boolean test(TruffleObject receiver) {
			return receiver instanceof PrystNull;
		}
	}
}