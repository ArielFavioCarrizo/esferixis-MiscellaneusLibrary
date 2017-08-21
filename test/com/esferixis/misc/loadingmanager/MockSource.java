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
package com.esferixis.misc.loadingmanager;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class MockSource {
	private final String name;
	private final long size;
	
	private final Set<MockSource> dependencies;
	
	/**
	 * @post Crea una fuente con el nombre y el tamaño especificados
	 */
	public MockSource(String name, long size) {
		this(name, size, new MockSource[0]);
	}
	
	/**
	 * @post Crea una fuente con el nombre, el tamaño y las dependencias especificadas
	 */
	public MockSource(String name, long size, MockSource... dependencies) {
		this(name, size, Arrays.asList(dependencies));
	}
	
	/**
	 * @post Crea una fuente con el nombre, el tamaño y las dependencias especificadas
	 */
	public MockSource(String name, long size, Collection<MockSource> dependencies) {
		this.name = name;
		this.size = size;
		this.dependencies = Collections.unmodifiableSet(new HashSet<MockSource>(dependencies));
	}
	
	/**
	 * @post Devuelve el nombre
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @post Devuelve el tamaño
	 */
	public long getSize() {
		return this.size;
	}
	
	/**
	 * @post Devuelve las dependencias
	 */
	public Set<MockSource> getDependencies() {
		return this.dependencies;
	}
	
	/**
	 * @post Devuelve el hash
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode() + (int) (this.size * 31);
	}
	
	/**
	 * @post Devuelve si es igual al objeto especificado
	 */
	@Override
	public boolean equals(Object other) {
		if ( ( other != null ) && ( other instanceof MockSource ) ) {
			final MockSource otherSource = ( MockSource ) other;
			return otherSource.getName().equals(this.getName()) && ( otherSource.getSize() == this.getSize() );
		}
		else {
			return false;
		}
	}
	
	/**
	 * @post Devuelve una representación en cadena de carácteres
	 */
	@Override
	public String toString() {
		return "MockSource( '" + this.getName() + "', " + this.getSize() + ")";
	}
}