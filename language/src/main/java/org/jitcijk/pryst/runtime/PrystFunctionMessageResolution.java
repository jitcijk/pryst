package org.jitcijk.pryst.runtime;

import org.jitcijk.pryst.nodes.call.PrystDispatchNode;
import org.jitcijk.pryst.nodes.call.PrystDispatchNodeGen;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.CanResolve;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import org.jitcijk.pryst.runtime.PrystContext;
import org.jitcijk.pryst.PrystLanguage;

/**
 * The class containing all message resolution implementations of {@link SLFunction}.
 */
@MessageResolution(receiverType = PrystFunction.class, language = PrystLanguage.class)
public class PrystFunctionMessageResolution {
	
	@Resolve(message = "EXECUTE")
	public abstract static class ForeignFunctionExecuteNode extends Node {
		
		@Child private PrystDispatchNode dispatchNode = PrystDispatchNodeGen.create();
		
		public Object access(VirtualFrame frame, PrystFunction receiver, Object[] arguments) {
			Object[] arr = new Object[arguments.length];
			
			for(int i = 0; i < arr.length; i++) {
				arr[i] = PrystContext.fromForeignValue(arguments[i]);
			}
			Object result = dispatchNode.executeDispatch(frame, receiver, arr);
			return result;
		}
	}
	
	@Resolve(message = "IS_EXECUTABLE")
	public abstract static class PrystForeignIsExecutableNode extends Node {
		public Object access(Object receiver) {
			return receiver instanceof PrystFunction;
		}
	}
	
	@CanResolve
	public abstract static class CheckFunction extends Node {
		protected static boolean test(TruffleObject receiver) {
			return receiver instanceof PrystFunction;
		}
	}
	
}