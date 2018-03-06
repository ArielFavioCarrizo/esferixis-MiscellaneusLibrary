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

import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.esferixis.misc.concurrency.tasking.TaskRunner;
import com.esferixis.misc.functional.Consumer;

/**
 * @author Ariel Favio Carrizo
 *
 *		   Valor diferido
 */
public final class DeferredValue<T> implements Value<T> {
	private Optional<T> settedValue;
	private final Stack<Consumer<T>> pendingConsumers;
	
	private Lock stateLock;
	
	/**
	 * @post Crea un valor diferido con el consumidor del consumidor que
	 * 		 que inicializa el valor y el ejecutador de tareas especificado
	 */
	public DeferredValue(Consumer<Consumer<T>> consumerOfValueInitializerConsumer, TaskRunner taskRunner) {
		this.pendingConsumers = new Stack<Consumer<T>>();
		this.settedValue = Optional.empty();
		this.stateLock = new ReentrantLock();
		
		final DeferredValue<T> thisDeferredValue = this;
		
		consumerOfValueInitializerConsumer.accept(new Consumer<T>() {

			@Override
			public void accept(T value, TaskRunner taskRunner) {
				thisDeferredValue.set(value, taskRunner);
			}
			
		}, taskRunner);
	}
	
	/**
	 * @pre El valor no tiene que estar seteado
	 * @post Setea un valor con el valor
	 * 		 y el ejecutador de tareas especificado
	 */
	private void set(T value, TaskRunner taskRunner) {
		this.stateLock.lock();
		
		try {
			if ( !this.settedValue.isPresent() ) {
				this.settedValue = Optional.of(value);
			}
			else {
				throw new IllegalStateException("Value has been setted");
			}
		} finally {
			this.stateLock.unlock();
		}
		
		while ( !this.pendingConsumers.empty() ) {
			this.pendingConsumers.pop().accept(value, taskRunner);
		}
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.functional.Value#get(java.util.function.Consumer, com.esferixis.misc.concurrency.tasking.TaskRunner)
	 */
	@Override
	public void get(Consumer<T> consumer, TaskRunner taskRunner) {
		this.stateLock.lock();
		boolean valueHasBeenSetted;
		
		try {
			if ( this.settedValue.isPresent() ) {
				valueHasBeenSetted = true;
			}
			else {
				this.pendingConsumers.push(consumer);
				valueHasBeenSetted = false;
			}
		}
		finally {
			this.stateLock.unlock();
		}
		
		if ( valueHasBeenSetted ) {
			consumer.accept(this.settedValue.get(), taskRunner);
		}
	}
}
