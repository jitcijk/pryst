package org.jitcijk.pryst.runtime;

import org.jitcijk.pryst.PrystException;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

public final class PrystUndefinedNameException extends PrystException {
	
	private final static long serialVersionUID = 1L;
	
	@TruffleBoundary
	public static PrystUndefinedNameException undefinedFunction(Object name) {
		throw new PrystUndefinedNameException("Undefined function: " + name);
	}
	
    @TruffleBoundary
    public static PrystUndefinedNameException undefinedProperty(Object name) {
        throw new PrystUndefinedNameException("Undefined property: " + name);
    }

    private PrystUndefinedNameException(String message) {
        super(message);
    }
	
}