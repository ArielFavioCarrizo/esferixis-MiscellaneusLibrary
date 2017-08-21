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
package com.esferixis.misc.dynamicFields;

import java.util.Map;

import com.esferixis.misc.map.ArrayMap;
import com.esferixis.misc.reference.InmutableReference;

/**
 * Contenedor de miembros dinámicos, optimizado
 * para lectura
 * 
 * @author ariel
 *
 */
public final class DynamicFieldsContainer {
	/**
	 * 
	 */
	private Map<DynamicField<?>, InmutableReference<?>> fieldsToValue;
	
	public DynamicFieldsContainer() {
		this.fieldsToValue = new ArrayMap<DynamicField<?>, InmutableReference<?>>();
	}
	
	/**
	 * @pre El miembro no puede ser nulo y no tiene que estar agregado
	 * @post Agrega el miembro especificado con el valor especificado
	 */
	public <T> void add(DynamicField<T> dynamicField, T value) {
		if ( !this.fieldsToValue.containsKey(dynamicField) ) {
			this.fieldsToValue.put(dynamicField, new InmutableReference<T>(value));
		}
		else {
			throw new IllegalStateException("Field exists");
		}
	}
	
	/**
	 * @post Asigna el valor especificado asociado
	 * 		 al miembro especificado
	 */
	public <T> void set(DynamicField<T> dynamicField, T value) {
		if ( this.fieldsToValue.containsKey(dynamicField) ) {
			this.fieldsToValue.put(dynamicField, new InmutableReference<T>(value));
		}
		else {
			throw new IllegalStateException("Field doesn't exists");
		}
	}
	
	/**
	 * @pre El miembro no puede ser nulo
	 * @post Devuelve el valor asociado al miembro, si no existe
	 * 		 devuelve null
	 */
	@SuppressWarnings("unchecked")
	public <T> InmutableReference<T> get(DynamicField<T> dynamicField) {
		if ( dynamicField != null ) {
			return (InmutableReference<T>) this.fieldsToValue.get(dynamicField);
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @pre El miembro tiene que estar agregado
	 * @post Elimina el miembro especificado, y devuelve su valor
	 */
	@SuppressWarnings("unchecked")
	public <T> InmutableReference<T> remove(DynamicField<T> dynamicField) {
		if ( this.fieldsToValue.containsKey(dynamicField) ) {
			return (InmutableReference<T>) this.fieldsToValue.remove(dynamicField);
		}
		else {
			throw new IllegalStateException("Field doesn't exists");
		}
	}
}
