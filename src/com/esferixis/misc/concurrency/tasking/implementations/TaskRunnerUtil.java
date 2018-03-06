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

import com.esferixis.misc.concurrency.tasking.Task;
import com.esferixis.misc.concurrency.tasking.TaskRunner;

/**
 * @author arifa
 *
 */
public final class TaskRunnerUtil {
	private TaskRunnerUtil() {}
	
	/**
	 * @post Decora la tarea con el taskRunner
	 * 		 especificado
	 */
	public static final Task decorate(final TaskRunner taskRunner, final Task task) {
		final Task taskToDecorate = task;
		
		return new Task() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2734290912621263639L;

			@Override
			public void run(TaskRunner taskRunner) {
				taskRunner.run(taskToDecorate);
			}

			@Override
			protected long getRelativeRunningTime_implementation() {
				return taskToDecorate.getRelativeRunningTime();
			}
			
		};
	}
	
	/**
	 * @post Realiza la composición de task runners
	 */
	public static TaskRunner compose(final TaskRunner outer, final TaskRunner inner) {
		return new TaskRunner() {

			@Override
			public void run_checked(Task task) {
				outer.run( TaskRunnerUtil.decorate(inner, task) );
			}
			
		};
	}
	
	/**
	 * @post Crea un runnable para ésta tarea, que
	 * 		 ejecuta la tarea con el task runner especificado
	 */
	public static Runnable createRunnable(final TaskRunner taskRunner, final Task task) {
		return new Runnable() {

			@Override
			public void run() {
				taskRunner.run(task);
			}
			
		};
	}
}
