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

import java.util.Comparator;

public class GenericComparator<T> implements Comparator<T> {
	private final Comparator<? super T> targetComparator;
	
	/**
	 * @post Crea el comparador con el comparador objetivo especificado,
	 * 		 si no se especifica un comparador usará el orden natural de los elementos
	 */
	public GenericComparator(Comparator<? super T> comparator) {
		this.targetComparator = comparator;
	}

	/**
	 * @post Devuelve la comparación del primer elemento con el segundo
	 * 		 usando el comparador objetivo si lo hay, caso contrario
	 * 		 hace la comparación con el orden natural
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int compare(T element1, T element2) {
		if ( this.targetComparator != null ) {
			return this.targetComparator.compare(element1, element2);
		}
		else {
			return ( (Comparable<T>) element1 ).compareTo(element2);
		}
	}
	
	/**
	 * @post Devuelve el comparador objetivo
	 */
	public Comparator<? super T> getTargetComparator() {
		return this.targetComparator;
	}
	
	/**
	 * @post Devuelve si es igual al objeto especificado
	 */
	@Override
	public boolean equals(Object other) {
		if ( other != null ) {
			if ( other instanceof GenericComparator<?> ) {
				GenericComparator<?> otherGenericComparator = (GenericComparator<?>) other;
				if ( this.targetComparator == null ) {
					return ( otherGenericComparator.targetComparator == null );
				}
				else {
					return this.targetComparator.equals(otherGenericComparator.targetComparator);
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * @post Devuelve el hash
	 */
	@Override
	public int hashCode() {
		int hash;
		if ( this.targetComparator != null ) {
			hash = this.targetComparator.hashCode();
		}
		else {
			hash = GenericComparator.this.getClass().hashCode();
		}
		return hash;
	}
}
