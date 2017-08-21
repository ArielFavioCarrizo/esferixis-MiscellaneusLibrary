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
package com.esferixis.misc.loadingmanager;

import java.util.Arrays;
import java.util.Collection;

import com.esferixis.misc.dynamicFields.DynamicFieldsContainerObject;
import com.esferixis.misc.loader.DataLoadingErrorException;

/**
 * @author ariel
 *
 */
public abstract class LoadingManager<T extends DynamicFieldsContainerObject> {
	protected final LoadingStrategy<T> loadingStrategy;
	
	protected final long maxElements, capacity;
	
	private boolean destroyed;
	
	/**
	 * @post Crea el "garbage" collector con la estrategia de carga,
	 *		 el límite de elementos y la capacidad especificados
	 */
	public LoadingManager(LoadingStrategy<T> loadingStrategy, long maxElements, long capacity) {
		if ( loadingStrategy != null ) {
			this.loadingStrategy = loadingStrategy;
			this.maxElements = maxElements;
			this.capacity = capacity;
			
			this.destroyed = false;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Verifica que no haya sido destruido
	 */
	private void checkDestroyed() {
		if ( this.destroyed ) {
			throw new IllegalStateException("Cannot use an destroyed garbage collected loader");
		}
	}
	
	/**
	 * @post Asegura que los elementos especificados, se hayan cargado.
	 * 		 Si no es posible, lanza OutOfSpace.
	 * 		 En éste caso no garantiza que los elementos hayan quedado en el estado anterior.
	 * 
	 * 		 Las dependencias se cargan automáticamente
	 * 
	 * @throws OutOfMemoryError
	 */
	public final void loadElements(Collection<T> elements) throws NullPointerException, DataLoadingErrorException {
		this.checkDestroyed();
		
		if ( elements != null ) {
			this.loadElements_internal(elements);
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Asegura que los elementos especificados, se hayan cargado.
	 * 		 Si no es posible, lanza OutOfSpace.
	 * 		 En éste caso no garantiza que los elementos hayan quedado en el estado anterior.
	 * 
	 * 		 Las dependencias se cargan automáticamente
	 * 
	 * @throws OutOfMemoryError
	 */
	public final void loadElements(T... elements) throws NullPointerException, DataLoadingErrorException {
		this.checkDestroyed();
		
		if ( elements != null ) {
			this.loadElements(Arrays.asList(elements));
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Asegura que los elementos especificados, se hayan cargado (Implementación interna)
	 *		 Si no es posible, lanza OutOfSpace.
	 * 		 En éste caso no garantiza que los elementos hayan quedado en el estado anterior.
	 */
	protected abstract void loadElements_internal(Collection<T> elements) throws NullPointerException, DataLoadingErrorException;
	
	/**
	 * @post Destruye el "garbage collector"
	 */
	public final void destroy() {
		this.checkDestroyed();
		this.destroy_internal();
	}
	
	/**
	 * @post Destruye el "garbage collector" (Implementación interna)
	 */
	protected abstract void destroy_internal();
}

