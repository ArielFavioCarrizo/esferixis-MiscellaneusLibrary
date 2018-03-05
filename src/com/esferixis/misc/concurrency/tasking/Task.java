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
package com.esferixis.misc.concurrency.tasking;

import java.io.Serializable;

/**
 * Tarea para paradigma funcional
 * 
 * @author Ariel Favio Carrizo
 *
 */
public abstract class Task implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2075330337289065621L;
	
	private long relativeRunningTime;
	private boolean hasCalculatedRelativeSerialRunningTime;
	
	/**
	 * @post Crea la tarea
	 */
	public Task() {
		this.hasCalculatedRelativeSerialRunningTime = false;
	}
	
	/**
	 * @post Devuelve el tiempo que lleva estimado
	 * 		 para ejecutarla
	 */
	public long getRelativeRunningTime() {
		if ( !this.hasCalculatedRelativeSerialRunningTime ) {
			this.relativeRunningTime = this.getRelativeRunningTime_implementation();
			this.hasCalculatedRelativeSerialRunningTime = true;
		}
		
		return this.relativeRunningTime;
	}
	
	/**
	 * @post Ejecuta la tarea con el ejecutador de
	 * 		 tareas especificado
	 * 
	 * 		 Si la tarea va a dar un resultado,
	 * 		 éste resultado tiene que ser procesado
	 * 		 ejecutando subtareas con el ejecutador de tareas
	 * 		 especificado
	 * 
	 * 		 En lo posible debe evitarse que una tarea reciba un objeto mutable
	 * 		 provisto por la tarea padre.
	 */
	public abstract void run(TaskRunner taskRunner);
	
	/**
	 * @post Devuelve el tiempo que lleva estimado
	 * 		 para ejecutarla (Implementación)
	 * 
	 * 		 Sólo involucra ésta tarea en particular, no involucra
	 * 		 a las subtareas que pueda ocasionar la ejecución
	 * 		 de ésta tarea
	 */
	protected abstract long getRelativeRunningTime_implementation();
}
