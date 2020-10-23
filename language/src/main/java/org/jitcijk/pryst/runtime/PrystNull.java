package org.jitcijk.pryst.runtime;

import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;


/**
 * In Truffle it is bad practice to use the java builtin null since it might cause problems 
 * with the internals of Truffle. Therefore we implement a singleton object that we can use
 * instead of the builtin null.
 * 
 */
public final class PrystNull implements TruffleObject {
	
	public static final PrystNull SINGLETON = new PrystNull();
	
	// Private so only one instance can be there
	private PrystNull() {
	}
	
	@Override 
	public String toString(){
		return "null";
	}
	
    /**
     * Copied:
     * In case you want some of your objects to co-operate with other languages, you need to make
     * them implement {@link TruffleObject} and provide additional {@link SLNullMessageResolution
     * foreign access implementation}.
     */
    @Override
    public ForeignAccess getForeignAccess() {
        return PrystNullMessageResolutionForeign.createAccess();
    }
}