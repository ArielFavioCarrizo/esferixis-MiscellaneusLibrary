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
package com.esferixis.misc.attribute;

/**
 * @author ariel
 *
 * Clase que representa un atributo que almacena su valor localmente pero
 * su valor es notificado cada vez que se lo modifica
 */
public class StoredAttribute<T> {
	protected T value;
	
	/**
	 * @post Crea el atributo con el valor especificado
	 */
	public StoredAttribute(T value) {
		this.setValue(value);
	}
	
	/**
	 * @post Comprueba las precondiciones
	 */
	protected void checkPreconditions(T newValue) {
		
	}
	
	/**
	 * @post Notifica el nuevo valor de atributo
	 */
	protected void notifyNewValue(T newValue) {
		
	}
	
	/**
	 * @post Devuelve el valor de atributo
	 */
	public final T getValue() {
		return this.value;
	}
	
	/**
	 * @pre Tienen que cumplirse las condiciones específicas del atributo
	 * @post Asigna el valor de atributo
	 */
	public final void setValue(T newValue) {
		this.checkPreconditions(newValue);
		this.value = newValue;
		this.notifyNewValue(newValue);
	}
}
