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

/**
 * Atributo de esencia inválido
 * Todo intento de usarlo lanza UnsupportedOperationException
 */

package com.esferixis.misc.Essence;

public final class InvalidEssenceAttribute<T> implements EssenceAttribute<T> {

	private static class InvalidAttributeException extends UnsupportedOperationException {
		private static final long serialVersionUID = 6952164522138717304L;
		
		public InvalidAttributeException() {
			super("Invalid attribute");
		}
	}
	
	/**
	 * @post Crea el atributo inválido
	 */
	InvalidEssenceAttribute() {
		
	}
	
	@Override
	public T get() {
		throw new InvalidAttributeException();
	}

	@Override
	public void init(T value) {
		throw new InvalidAttributeException();
	}

	@Override
	public void initializationValidation() {
		throw new InvalidAttributeException();
	}

	@Override
	public boolean isInitialized() {
		throw new InvalidAttributeException();
	}

}
