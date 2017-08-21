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

package com.esferixis.misc.containablestrategy.binaryclassifier;

import com.esferixis.misc.binaryclassifier.BinaryClassifier;
import com.esferixis.misc.containablestrategy.ContainableStrategy;

/**
 * Clasificador binario que evalúa si los objetos son contenidos por un
 * contenedor especificado
 */
public class ContainerBinaryClassifier implements BinaryClassifier<Object> {
	private final ContainableStrategy containableStrategy;
	private final Object mandatoryContained;
	
	/**
	 * @pre La estrategia de contenibles no puede ser nula
	 * @post Crea el clasificador con la estrategia de contenibles y el contenible especificados
	 */
	public ContainerBinaryClassifier(ContainableStrategy containableStrategy, Object mandatoryContained) {
		if ( containableStrategy != null ) {
			this.containableStrategy = containableStrategy;
			this.mandatoryContained = mandatoryContained;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve el contenedor
	 */
	public final Object getMandatoryContained() {
		return this.mandatoryContained;
	}
	
	/**
	 * @post Evalúa si el elemento especificado pertenece al contenedor
	 */
	@Override
	public boolean evaluate(Object element) {
		return this.containableStrategy.contains(element, this.mandatoryContained);
	}
}
