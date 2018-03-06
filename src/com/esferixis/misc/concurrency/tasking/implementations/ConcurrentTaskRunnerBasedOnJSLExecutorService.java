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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.esferixis.misc.concurrency.Shutdownable;
import com.esferixis.misc.concurrency.tasking.TaskRunner;
import com.esferixis.misc.concurrency.tasking.Task;

/**
 * @author Ariel Favio Carrizo
 * 
 * Ejecutador de tareas concurrente basado en ejecutador de tareas
 * del Java Standard Library
 */
public final class ConcurrentTaskRunnerBasedOnJSLExecutorService extends TaskRunner implements Shutdownable {
	private final ExecutorService executor;
	
	/**
	 * @post Crea el ejecutador de tareas
	 */
	public ConcurrentTaskRunnerBasedOnJSLExecutorService() {
		this.executor = Executors.newWorkStealingPool();
	}
	
	/* (non-Javadoc)
	 * @see com.esferixis.misc.concurrency.tasking.AbstractTaskRunner#run_checked(com.esferixis.misc.concurrency.functional.Task)
	 */
	@Override
	protected void run_checked(final Task task) {	
		this.executor.execute( TaskRunnerUtil.createRunnable(this, task) );
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.concurrency.Shutdownable#shutdown()
	 */
	@Override
	public void shutdown() {
		this.executor.shutdown();
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.concurrency.Shutdownable#lockThisThreadUntilHasBeenShutdown()
	 */
	@Override
	public void lockThisThreadUntilHasBeenShutdown() {
		try {
			while ( !this.executor.awaitTermination(10, TimeUnit.SECONDS ) ) {}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
