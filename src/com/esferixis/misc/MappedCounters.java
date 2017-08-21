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
package com.esferixis.misc;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Contadores mapeados
 * 
 * @author ariel
 *
 */
public final class MappedCounters<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3275371451354859870L;
	
	private final Map<T, Integer> countersByElement;
	
	/**
	 * @post Crea los contadores mapeados
	 */
	public MappedCounters() {
		this.countersByElement = new HashMap<T, Integer>();
	}
	
	/**
	 * @pre El objeto no puede ser nulo y no tiene que haber overflow
	 * @post Incrementa el contador del objeto especificado
	 */
	public void increment(T element) {
		if ( element != null ) {
			Integer value = this.countersByElement.get(element);
			
			if ( value == null ) {
				value = Integer.MIN_VALUE;
			}
			else {
				if ( value == Integer.MAX_VALUE ) {
					throw new ArithmeticException("Counter overflow");
				}
				else {
					value++;
				}
			}
			
			this.countersByElement.put(element, value);
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @pre El objeto no puede ser nulo y no tiene que estar en cero
	 * @post Decrementa el contador del objeto especificado
	 */
	public void decrement(T element) {
		if ( element != null ) {
			Integer value = this.countersByElement.get(element);
			
			if ( value != null ) {
				if ( value == Integer.MIN_VALUE ) {
					this.countersByElement.remove(element);
				}
				else {
					this.countersByElement.put(element, --value);
				}
			}
			else {
				throw new IllegalStateException("Attemped to decrement counter when it's zero");
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve el conjunto de elementos que no están en cero (Visión de sólo lectura)
	 */
	public Set<T> getNonZeroElements() {
		return Collections.unmodifiableSet(this.countersByElement.keySet());
	}
}
