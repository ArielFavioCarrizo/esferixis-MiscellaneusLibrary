/**
 * Copyright (c) 2018 Ariel Favio Carrizo
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
package com.esferixis.misc.functional.values;

import com.esferixis.misc.Preconditions;
import com.esferixis.misc.concurrency.tasking.TaskRunner;
import com.esferixis.misc.functional.Consumer;

/**
 * Valor inmediato
 * 
 * @author Ariel Favio Carrizo
 *
 */
public final class ImmediateValue<T> implements Value<T> {
	private final T value;
	
	/**
	 * @pre El valor no puede ser nulo
	 * @post Crea un valor inmediato con el valor especificado
	 */
	public ImmediateValue(T value) {
		Preconditions.checkNotNull(value, "value");
		
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.functional.values.Value#get(com.esferixis.misc.functional.Consumer, com.esferixis.misc.concurrency.tasking.TaskRunner)
	 */
	@Override
	public void get(Consumer<T> consumer, TaskRunner taskRunner) {
		consumer.accept(this.value, taskRunner);
	}
}
