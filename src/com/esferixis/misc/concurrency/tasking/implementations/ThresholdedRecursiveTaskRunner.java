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
package com.esferixis.misc.concurrency.tasking.implementations;

import com.esferixis.misc.Preconditions;
import com.esferixis.misc.concurrency.tasking.TaskRunner;
import com.esferixis.misc.concurrency.tasking.Task;

/**
 * @author Ariel Favio Carrizo
 * 
 *		   Ejecutador de tareas recursivo,
 *		   
 */
public final class ThresholdedRecursiveTaskRunner extends TaskRunner {
	private final TaskRunner criticalConditionTaskRunner;
	private final TaskRunner lowerTaskRunner;
	
	private final long recursionLevelLimit;
	private final long recursionLevel;
	
	/**
	 * @pre Ninguno de los ejecutadores de tareas provistos pueden ser nulos,
	 * 		y el límite de nivel de recursión tiene que ser positivo
	 * 
	 * @post Crea un ejecutador de tareas recursivo
	 * 		 con el ejecutador de tareas de condición crítica y el inferior especificados.
	 * 
	 * 		 El ejecutador de tareas superior se ejecuta cuando el nivel
	 * 		 de recursividad alcanza el valor crítico.
	 * 		 
	 * 		 El ejecutador de tareas inferior se ejecuta por cada
	 * 		 tarea
	 */
	public ThresholdedRecursiveTaskRunner(TaskRunner criticalConditionTaskRunner, TaskRunner lowerTaskRunner, long recursionLevelLimit) {
		this(criticalConditionTaskRunner, lowerTaskRunner, recursionLevelLimit, 0);
	}
	
	/**
	 * @pre Ninguno de los ejecutadores de tareas provistos pueden ser nulos,
	 * 		y el límite de nivel de recursión tiene que ser positivo.
	 * 
	 * 		También, el nivel de recursividad no puede ser negativo
	 * 
	 * @post Crea un ejecutador de tareas recursivo
	 * 		 con el ejecutador de tareas de condición crítica, el inferior, 
	 * 		 el nivel de recursión límite, y el actual
	 * 		 especificados.
	 * 
	 * 		 El ejecutador de tareas superior se ejecuta cuando el nivel
	 * 		 de recursividad alcanza el valor crítico.
	 * 		 
	 * 		 El ejecutador de tareas inferior se ejecuta por cada
	 * 		 tarea
	 */
	private ThresholdedRecursiveTaskRunner(TaskRunner criticalConditionTaskRunner, TaskRunner lowerTaskRunner, long recursionLevelLimit, long recursionLevel) {
		Preconditions.checkNotNull(criticalConditionTaskRunner, "upperTaskRunner");
		Preconditions.checkNotNull(lowerTaskRunner, "lowerTaskRunner");
		Preconditions.checkIsPositive(recursionLevelLimit, "recursionLevelLimit");
		Preconditions.checkIsNotNegative(recursionLevel, "recursionLevel");
		
		this.criticalConditionTaskRunner = criticalConditionTaskRunner;
		this.lowerTaskRunner = lowerTaskRunner;
		this.recursionLevelLimit = recursionLevelLimit;
		this.recursionLevel = recursionLevel;
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.concurrency.tasking.TaskRunner#run_checked(com.esferixis.misc.concurrency.tasking.Task)
	 */
	@Override
	protected void run_checked(Task task) {
		final Task decoratedTask = TaskRunnerUtil.decorate(this.lowerTaskRunner, task);
		final TaskRunner nextTaskRunner;
		
		if ( this.recursionLevel >= this.recursionLevelLimit ) {
			nextTaskRunner = TaskRunnerUtil.compose(this.criticalConditionTaskRunner, new ThresholdedRecursiveTaskRunner(this.criticalConditionTaskRunner, this.lowerTaskRunner, this.recursionLevelLimit) );
		}
		else {
			nextTaskRunner = new ThresholdedRecursiveTaskRunner(this.criticalConditionTaskRunner, this.lowerTaskRunner, this.recursionLevelLimit, this.recursionLevel+1 );
		}
		
		nextTaskRunner.run(decoratedTask);
	}
}
