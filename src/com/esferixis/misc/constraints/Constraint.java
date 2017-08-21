/**
 * Copyright (c) 2017 Ariel Favio Carrizo
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
package com.esferixis.misc.constraints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class Constraint<T> {
	/**
	 * @post Crea la restricción
	 */
	public Constraint() {
		
	}
	
	/**
	 * @post Verifica que el valor especificado sea correcto
	 */
	public abstract void checkValue(T value);
	
	/**
	 * @post Crea una agrupación de varias restricciones
	 */
	public static final <T> Constraint<T> union(Collection< Constraint<? super T> > originalConstraints) {
		if ( originalConstraints != null ) {
			final Collection< Constraint<? super T> > constraints = Collections.unmodifiableList(new ArrayList< Constraint<? super T> >(originalConstraints) );
			
			return new Constraint<T>() {
				@Override
				public void checkValue(T value) {
					for ( Constraint<? super T> eachConstraint : constraints ) {
						eachConstraint.checkValue(value);
					}
				}
			};
		}
		else {
			throw new NullPointerException();
		}
	}
}
