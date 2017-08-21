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

import java.util.Collection;

import com.esferixis.misc.ElementCallback;
import com.esferixis.misc.dynamicFields.DynamicFieldsContainerObject;
import com.esferixis.misc.loader.AbstractDataLoader;

/**
 * @author ariel
 *
 */
public interface LoadingStrategy<T extends DynamicFieldsContainerObject> {
	public interface Observer {
		/**
		 * @post Notifica que tiene usuarios cargados
		 */
		public void notifyHasLoadedUsers();
		
		/**
		 * @post Notifica que no tiene usuarios cargados
		 */
		public void notifyHasNotLoadedUsers();
	}
	
	/**
	 * @post Devuelve el espacio ocupado por el elemento
	 * 		 especificado
	 */
	public long getOccupiedSpace(T element);
	
	/**
	 * @post Devuelve las dependencias para el elemento de datos especificado
	 */
	public Collection<T> getDependencies(T dataElement);
	
	/**
	 * @post Carga el elemento especificado
	 */
	public void load(T element);
	
	/**
	 * @pre Las dependencias tienen que estar descargadas
	 * @post Descarga el elemento especificado, si tiene observers se desacoplan
	 */
	public void unload(T element);
	
	/**
	 * @post Devuelve si el elemento está cargado
	 */
	public boolean isLoaded(T element);
	
	/**
	 * @pre El elemento tiene que estar cargado
	 * @post Acopla el observer al elemento especificado
	 */
	public void attachObserver(T element, Observer observer);
	
	/**
	 * @pre El elemento tiene que estar cargado
	 * @post Desacopla el observer del elemento especificado
	 */
	public void detachObserver(T element, Observer observer);
}
