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

package com.esferixis.misc.classextra;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.esferixis.misc.containablestrategy.ExplicitContainableStrategy;
import com.esferixis.misc.reference.DynamicReference;

/**
 * Estrategia de contenibles explícita de herencia de clases
 * 
 * @author ariel
 *
 */

public final class ClassInheritanceExplicitContainableStrategy extends ExplicitContainableStrategy< Class<?> > {
	public static ClassInheritanceExplicitContainableStrategy INSTANCE = new ClassInheritanceExplicitContainableStrategy();
	
	/**
	 * @post Crea la estrategia de contenibles de herencia de clase
	 */
	private ClassInheritanceExplicitContainableStrategy() {
		super( (Class<Class<?>>) (Object) Class.class );
	}
	
	/**
	 * @post Devuelve los contenedores del contenible especificado.
	 * 		 Si el contenible es nulo entonces devuelve el contenedor nulo.
	 * 
	 * 		 Ésta implementación devuelve un conjunto con la superclase si existe (containable.getSuperclass())
	 * 		 y las interfaces (containable.getInterfaces()) si el contenible especificado no es nulo,
	 * 		 caso contrario devuelve Collections.singleton( null )
	 */
	public Set< Class<?> > getContainers(Class<?> containable) {
		if ( containable != null ) {
			Set< Class<?> > containers = new HashSet< Class<?> >();
			
			final Class<?> superClass = containable.getSuperclass();
			
			if ( superClass != null ) {
				containers.add( superClass );
			}
			
			containers.addAll( Arrays.asList( containable.getInterfaces() ) );
			
			return (Set<Class<?>>) (Set<?>) containers;
		}
		else {
			return Collections.singleton(null);
		}
	}
	
	/**
	 * @post Devuelve si el contenedor contiene el contenible especificado
	 * 		 Si el contenedor es nulo supone que sólo el contenible nulo está contenido.
	 * 
	 * 		 Si la referencia al conjunto de contenedores encontrados es nula, 
	 * 		 el contenedor y el contenible no son nulos devuelve container.isAssignableFrom(containable),
	 * 		 caso contrario usa la implementación de la superclase.
	 */
	@Override
	public boolean contains(DynamicReference< Set< Class<?> > > foundedContainers, Class<?> container, Class<?> containable) {
		if ( ( foundedContainers == null ) && ( ( container != null ) && ( containable != null ) ) ) {
			return container.isAssignableFrom(containable);
		}
		else {
			return super.contains(foundedContainers, container, containable);
		}
	}
}
