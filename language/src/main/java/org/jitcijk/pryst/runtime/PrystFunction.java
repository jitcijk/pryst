package org.jitcijk.pryst.runtime;


import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.utilities.CyclicAssumption;
import org.jitcijk.pryst.runtime.PrystFunctionMessageResolutionForeign;
import org.jitcijk.pryst.nodes.PrystUndefinedFunctionRootNode;

/**
 *  In JeSSEL we do not have classes so is function redefenition is allowed? 
 *	TODO ask Tijs
 */

public final class PrystFunction implements TruffleObject {
	private final String name;
	
	private RootCallTarget callTarget;
	
	/**
	 * Assumption about redefinition of the function.
	 * If redefinition is not allowed we can throw this out.
	 * Cyclic means that if this assumption is no longer true,
	 * we assume the new state as being stable.
	 */
	private final CyclicAssumption callTargetStable;
	
	protected PrystFunction(String name) {
		this.name = name;
		this.callTarget = Truffle.getRuntime().createCallTarget(new PrystUndefinedFunctionRootNode(name));
		this.callTargetStable = new CyclicAssumption(name);
	}
	
	public String getName() {
		return name;
	}
	
	protected void setCallTarget(RootCallTarget callTarget) {
		this.callTarget = callTarget;
		callTargetStable.invalidate();
	}
	
	public RootCallTarget getCallTarget() {
		return callTarget;
	}
	
	public Assumption getCallTargetStable(){
		return callTargetStable.getAssumption();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
    /** Copied from SLLanguage:
     * In case you want some of your objects to co-operate with other languages, you need to make
     * them implement {@link TruffleObject} and provide additional
     * {@link SLFunctionMessageResolution foreign access implementation}.
     */
    @Override
    public ForeignAccess getForeignAccess() {
        return PrystFunctionMessageResolutionForeign.createAccess();
    }
	
	
	
	
}