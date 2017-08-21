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
package com.esferixis.misc.Essence;

/**
 * Del patrón de diseño Essence
 */
public abstract class Essence {
	private int parametersQuantity; // Cantidad de parámetros
	private int initializedParametersQuantity; // Cantidad de parámetros inicializados
	private boolean validated; // Validado
	
	/**
	 * @post Crea la esencia
	 */
	public Essence() {
		this.parametersQuantity = 0;
		this.initializedParametersQuantity = 0;
		this.validated = false;
	}
	
	/**
	 * @post Crea un atributo obligatorio y lo devuelve
	 */
	protected final <T> MandatoryEssenceAttribute<T> createMandatoryAttribute(Class<T> valueClass) {
		return new MandatoryEssenceAttribute<T>(this);
	}
	
	/**
	 * @post Crea un atributo opcional y lo devuelve
	 */
	protected final <T> OptionalEssenceAttribute<T> createOptionalAttribute(Class<T> valueClass) {
		return new OptionalEssenceAttribute<T>(this, false);
	}

	/**
	 * @post Crea un atributo opcional y lo devuelve
	 */
	protected final <T> OptionalEssenceAttribute<T> createInitIrrestrictedOptionalAttribute(Class<T> valueClass) {
		return new OptionalEssenceAttribute<T>(this, true);
	}
	
	/**
	 * @post Crea un atributo inválido
	 */
	protected final <T> InvalidEssenceAttribute<T> createInvalidEssenceAttribute() {
		return new InvalidEssenceAttribute<T>();
	}
	
	/**
	 * @pre La cantidad de parámetros no puede superar el número máximo de INTEGER
	 * @post Cuenta un atributo obigatorio
	 */
	final void mandatoryAttributeCount() {
		if ( this.parametersQuantity < Integer.MAX_VALUE ) {
			this.parametersQuantity++;
		}
		else {
			throw new IllegalStateException("The attributes quantity is too long");
		}
	}
	
	/**
	 * @pre La cantidad de parámetros inicializados no puede superar la cantidad
	 * 		de parámetros
	 * @post Cuenta un atributo obligatorio inicializado
	 */
	final void initializedMandatoryAttributeCount() {
		if ( this.initializedParametersQuantity < this.parametersQuantity ) {
			this.initializedParametersQuantity++;
		}
		else {
			throw new RuntimeException("Unexpected inconsistency: The number of initialized attributes is greater than attributes");
		}
	}
	
	/**
	 * @pre Todos los parámetros tienen que estar inicializados
	 * @post Valida la esencia
	 */
	protected void validate() {
		if ( this.initializedParametersQuantity != this.parametersQuantity ) {
			throw new IllegalStateException("Unitialized attribute");
		}
		this.validated = true;
	}
	
	/**
	 * @post Devuelve si está validado
	 */
	public boolean isValidated() {
		return this.validated;
	}
}
