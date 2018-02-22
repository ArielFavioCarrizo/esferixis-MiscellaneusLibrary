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
package com.esferixis.misc.test.containableStrategy;

import java.util.HashSet;
import java.util.Set;

/**
 *  Contenibles de prueba para las pruebas de JUnit de colecciones desde la estrategia de contenibles
 */
public final class DummyContainable {
	private final String name;
	private final Set<DummyContainable> containers;
	private Object expectedValue;
	
	/**
	 * @pre El nombre no puede ser nulo
	 * @post Crea el contenible de prueba con el nombre especificado
	 */
	public DummyContainable(String name) {
		if ( name != null ) {
			this.name = name;
			this.containers = new HashSet<DummyContainable>();
		}
		else {
			throw new NullPointerException();
		}
		
		this.expectedValue = null;
	}
	
	/**
	 * @post Devuelve el conjunto de contenedores
	 */
	public Set<DummyContainable> containers() {
		return this.containers;
	}
	
	/**
	 * @post Devuelve el nombre
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @post Especifica el valor esperado
	 */
	public void setExpectedValue(Object value) {
		this.expectedValue = value;
	}
	
	/**
	 * @post Devuelve el valor esperado
	 */
	public Object getExpectedValue() {
		return this.expectedValue;
	}
	
	@Override
	public String toString() {
		return "(" + this.getName() + ")";
	}
}
