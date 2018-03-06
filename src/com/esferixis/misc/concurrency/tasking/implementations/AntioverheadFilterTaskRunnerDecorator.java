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

import java.util.function.Supplier;

import com.esferixis.misc.Preconditions;
import com.esferixis.misc.concurrency.tasking.TaskRunner;
import com.esferixis.misc.concurrency.tasking.Task;

/**
 * @author arifa
 *
 * Decorador del ejecutador de tareas que implementa
 * un filtro de overhead.
 * 
 * No lanza una tarea en el ejecutador de tareas
 * cubierto hasta asegurarse que haya alcanzado
 * un tiempo de ejecución relativo crítico
 */
public final class AntioverheadFilterTaskRunnerDecorator extends TaskRunner {
	private final TaskRunner upperTaskRunner;
	private final Supplier<TaskRunner> lowerTaskRunnerSupplier;
	
	private final TaskRunner lowerTaskRunner;
	
	private final long relativeRunningTimeThreshold;
	private long accumulatedRelativeRunningTime;
	
	/**
	 * @pre El ejecutador de tareas superior y la fábrica del ejecutador
	 * 		tareas de capa inferior no pueden ser nulos
	 * 		El threshold de tiempo de ejecución relativo no puede ser negativo
	 * 
	 * @post Crea el decorador con el runner de tareas superior,
	 * 		 el proveedor de runner de tarea inferior,
	 * 		 y el threshold de tiempo de ejecución
	 * 		 especificados
	 */
	public AntioverheadFilterTaskRunnerDecorator(final TaskRunner upperTaskRunner, final Supplier<TaskRunner> lowerTaskRunnerSupplier, final long relativeRunningTimeThreshold) {
		Preconditions.checkNotNull(upperTaskRunner, "upperTaskRunner");
		Preconditions.checkNotNull(lowerTaskRunnerSupplier, "lowerTaskRunnerSupplier");
		
		Preconditions.checkIsPositive(relativeRunningTimeThreshold, "relativeRunningTimeThreshold");
		
		this.upperTaskRunner = upperTaskRunner;
		this.lowerTaskRunnerSupplier = lowerTaskRunnerSupplier;
		this.lowerTaskRunner = this.lowerTaskRunnerSupplier.get();
		
		this.relativeRunningTimeThreshold = 0;
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.concurrency.tasking.TaskRunner#run_checked(com.esferixis.misc.concurrency.functional.Task)
	 */
	@Override
	protected void run_checked(final Task task) {
		long nextAccumulatedRelativeRunningTime = this.accumulatedRelativeRunningTime + task.getRelativeRunningTime();
		final TaskRunner nextTaskRunner;
		
		if ( nextAccumulatedRelativeRunningTime > relativeRunningTimeThreshold ) {
			nextTaskRunner = this.fork();
			nextAccumulatedRelativeRunningTime = 0;
		}
		else {
			nextTaskRunner = TaskRunnerUtil.compose(this.lowerTaskRunner, this );
		}
		
		this.accumulatedRelativeRunningTime = nextAccumulatedRelativeRunningTime;
		
		nextTaskRunner.run(task);
	}

	/**
	 * @post Forkea el task runner
	 */
	private TaskRunner fork() {
		return TaskRunnerUtil.compose(
				new AntioverheadFilterTaskRunnerDecorator(this.upperTaskRunner, this.lowerTaskRunnerSupplier, this.relativeRunningTimeThreshold),
				this.upperTaskRunner
		);
	}
}
