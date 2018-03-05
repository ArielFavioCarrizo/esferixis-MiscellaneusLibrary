/**
 * Copyright (c) 2018 Ariel Favio Carrizo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'esferixis' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.esferixis.misc;

/**
 * @author Precondiciones
 *
 */
public final class Preconditions {
	/**
	 * @post Verifica que el elemento no sea nulo,
	 * 		 con el nombre de argumento especificado
	 */
	public static <T> void checkNotNull(T element, String argumentName) {
		if ( argumentName != null ) {
			if ( element == null ) {
				throw new NullPointerException("Expected non null '" + argumentName + "'");
			}
		}
		else {
			throw new NullPointerException("Expected non null argumentName");
		}
	}
	
	/**
	 * @post Verifica que el número sea positivo
	 */
	public static void checkIsPositive(long number, String argumentName) {
		if ( argumentName != null ) {
			if ( !( number > 0 ) ) {
				throw new NullPointerException("Expected positive '" + argumentName + "'");
			}
		}
		else {
			throw new NullPointerException("Expected non null argumentName");
		}
	}
}
