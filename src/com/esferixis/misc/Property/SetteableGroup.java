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
package com.esferixis.misc.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public final class SetteableGroup<T> implements Setteable<T> {	
	private final Collection< Setteable<T> > setteables;
	
	/**
	 * @post Crea el grupo
	 */
	public SetteableGroup() {
		this.setteables = new ArrayList< Setteable<T> >();
	}
	
	/**
	 * @post Crea el grupo con la colección especificada
	 */
	public SetteableGroup(Collection< Setteable<T> > setteables) {
		if ( setteables != null ) {
			this.setteables = new ArrayList< Setteable<T> >(setteables);
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @pre El asignador no puede ser nulo
	 * @post Agrega un asignable
	 * @param value
	 */
	public void addSetter(Setteable<T> setteable) {
		if ( setteable != null ) {
			this.setteables.add(setteable);
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @pre El asignador no puede ser nulo
	 * @post Quita un asignable
	 * @param value
	 */
	public void removeSetter(Setteable<T> setteable) {
		if ( setteable != null ) {
			this.setteables.remove(setteable);
		}
		else {
			throw new NullPointerException();
		}
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.Property.Setteable#set(java.lang.Object)
	 */
	@Override
	public void set(T value) {
		for ( Setteable<T> eachSetteable : this.setteables ) {
			eachSetteable.set(value);
		}
	}
	
}
