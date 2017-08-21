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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.esferixis.misc.collection.list.BinaryList;
import com.esferixis.misc.collection.set.ArrayHashSet;
import com.esferixis.misc.collection.set.BinarySet;

/**
 * Agregador de funcionalidad a la clase de reflection
 *
 * @param <T>
 */

public final class ClassExtra<T> {
	private final Class<T> targetClass; // Clase objetivo
	
	private final static Map< Class<?>, Integer > hierarchyLevel = new WeakHashMap< Class<?>, Integer>(); // Nivel de jerarquía
	
	/**
	 * @pre La referencia a la clase no puede ser nula
	 * @post Crea el agregador de funcionalidad para la clase especificada
	 */
	public ClassExtra(Class<T> targetClass) {
		if ( targetClass != null ) {
			this.targetClass = targetClass;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve la clase
	 */
	public Class<T> getTargetClass() {
		return this.targetClass;
	}
	
	// Descripción de método
	private static class MethodDescription {
		private final Method method;
		
		/**
		 * @pre La referencia al método no puede ser nula
		 * @post Crea un descriptor de método con el método especificado
		 */
		public MethodDescription(Method method) {
			if ( method != null ) {
				this.method = method;
			}
			else {
				throw new NullPointerException();
			}
		}
		
		/**
		 * @post Devuelve el método
		 */
		public Method getMethod() {
			return this.method;
		}
		
		/**
		 * @post Devuelve el hash
		 */
		@Override
		public int hashCode() {
			return this.method.getName().hashCode() + this.method.getModifiers() + Arrays.deepHashCode( this.method.getParameterTypes() );
		}
		
		/**
		 * @post Devuelve si el objeto es igual al especificado
		 */
		@Override
		public boolean equals(Object other) {
			if ( other != null ) {
				if ( other instanceof MethodDescription ) {
					final MethodDescription otherMethodDescriptor = (MethodDescription) other;
					
					return (
							this.method.getName().equals(otherMethodDescriptor.getMethod().getName()) &&
							this.method.getModifiers() == otherMethodDescriptor.getMethod().getModifiers() &&
							Arrays.deepEquals( this.method.getParameterTypes(), otherMethodDescriptor.getMethod().getParameterTypes() )
					);
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
	}
	
	/**
	 * @post Devuelve todos los métodos de la clase ya sean públicos, protegidos (Heredados) y privados en una colección no modificable
	 */
	public Collection<Method> getAllMethods() {
		return Collections.unmodifiableCollection( this.getAllMethodsByDescription().values() );
	}
	
	/**
	 * @throws SecurityException, NoSuchMethodException 
	 * @post Devuelve el método con el nombre y los tipos de parámetro especificado, ya sea público, protegido (Heredado) o privado
	 */
	@SuppressWarnings("unchecked")
	public Method getAllMethod(String name, Class... parameterTypes) throws SecurityException, NoSuchMethodException {
		Method method;
		try {
			method = this.targetClass.getDeclaredMethod(name, parameterTypes);
		} catch (SecurityException e) {
			throw e;
		} catch (NoSuchMethodException e) {
			if ( this.targetClass != null) {
				try {
					method = new ClassExtra(this.targetClass).getAllMethod(name, parameterTypes);
				} catch (NoSuchMethodException e2) {
					throw e;
				}
			}
			else {
				throw e;
			}
		}
		return method;
	}
	
	/**
	 * @post Devuelve todos los métodos de la clase ya sean públicos, protegidos (Heredados) y privados en un conjunto
	 */
	private Map<MethodDescription, Method> getAllMethodsByDescription() {
		// Crear el mapa de métodos por descriptor de método
		Map<MethodDescription, Method> methodsByMethodDescriptor = new HashMap<MethodDescription, Method>();
		for ( Method eachMethod : this.targetClass.getDeclaredMethods() ) {
			methodsByMethodDescriptor.put( new MethodDescription(eachMethod), eachMethod);
		}
		
		// Si tiene superclase
		if ( this.targetClass.getSuperclass() != null ) {
			// Agrega aquellos métodos de la superclase que no son privados
			for ( Map.Entry<MethodDescription, Method> eachEntry : ( (Map<MethodDescription, Method>) new ClassExtra(this.targetClass.getSuperclass()).getAllMethodsByDescription() ).entrySet() ) {
				if ( !Modifier.isPrivate( eachEntry.getValue().getModifiers() ) ) { // Si no es privado
					Method method = methodsByMethodDescriptor.get( eachEntry.getKey() );
					// Si no está declarado en la clase
					if ( method == null ) {
						method = eachEntry.getValue(); // Tomar el heredado
					}
					// Agregar el método
					methodsByMethodDescriptor.put(eachEntry.getKey(), method);
				}
			}
		}
		
		return methodsByMethodDescriptor;
	}
	
	/**
	 * @post Devuelve si las clases del primer par ordenado de clases son superclases del segundo par ordenado de clases
	 */
	public static <T> boolean isAssignableFrom(BinaryList< Class<? extends T> > sortedPairClass1, BinaryList< Class<? extends T> > sortedPairClass2 ) {
		return (( sortedPairClass1.getElement1().isAssignableFrom( sortedPairClass2.getElement1() ) ) &&
				( sortedPairClass1.getElement2().isAssignableFrom( sortedPairClass2.getElement2() ) )
		);
	}

	/**
	 * @post Devuelve si las clases del primer par de conjunto de clases son superclases del segundo par de conjunto de clases
	 */
	public static <T> boolean isAssignableFrom(BinarySet< Class<? extends T> > setPairClass1, BinarySet< Class<? extends T> > setPairClass2 ) {
		final BinaryList< Class<? extends T> > listPairClass1 = new BinaryList< Class<? extends T> >(setPairClass1.getElement1(), setPairClass1.getElement2() );
		final BinaryList< Class<? extends T> > listPairClass2 = new BinaryList< Class<? extends T> >(setPairClass2.getElement1(), setPairClass2.getElement2() );
		boolean assignable;
		if ( ( isAssignableFrom(listPairClass1, listPairClass2) ) || isAssignableFrom(listPairClass1.opposite(), listPairClass2) ) {
			assignable = true;
		}
		else {
			assignable = false;
		}
		return assignable;
	}
	
	/**
	 * @pre La lista de par de clases especificada no puede ser nula
	 * @post Devuelve una lista de permutaciones de superclases del par ordenado de clases especificado
	 */
	public static <T> List< BinarySet< Class<?> > > getClassListPairSuperclassesPermutations( BinaryList< Class<? extends T> > listPairClass ) {
		if ( listPairClass == null ) {
			throw new NullPointerException();
		}
		
		// Obtener permutaciones posibles
		Boolean[][] superclassesPermutations = new Boolean[][]{ { false, true }, { true, true }, { true, false } };
		
		final List< BinarySet< Class<?> > > superclassesPermutationsSetPairs = new ArrayList< BinarySet< Class<?> > >(superclassesPermutations.length);
		
		// Obtener las permutaciones existentes
		for ( Boolean[] eachSuperClassPermutation : superclassesPermutations ) {
			List< Class<?> > resultClasses = new ArrayList< Class<?> >(2);
			Iterator< Class<? extends T> > classIterator = listPairClass.iterator();
			for ( int i = 0 ; (i < 2) && ( resultClasses != null ) ; i++ ) {
				final Class<?> eachClass = classIterator.next();
				if ( eachSuperClassPermutation[i] ) {
					final Class<?> superClass = eachClass.getSuperclass();
					if ( superClass != null ) {
						resultClasses.add( superClass );
					}
					else {
						resultClasses = null;
					}
				}
				else {
					resultClasses.add(eachClass);
				}
			}
			
			if ( resultClasses != null ) {
				superclassesPermutationsSetPairs.add( new BinarySet< Class<?> >( resultClasses.get(0), resultClasses.get(1) ) );
			}
		}
		
		// Devolverlas
		return Collections.unmodifiableList( superclassesPermutationsSetPairs );
	}
	
	/**
	 * @pre El conjunto de par de clases especificado no puede ser nulo
	 * @post Devuelve una lista de permutaciones de superclases del par de clases especificado
	 */
	public static <T> Set< BinarySet< Class<?> > > getClassSetPairSuperclassesPermutations( BinarySet< Class<? extends T> > setPairClass ) {
		if ( setPairClass != null ) {
			return Collections.unmodifiableSet( new ArrayHashSet< BinarySet< Class<?> > >(getClassListPairSuperclassesPermutations( new BinaryList< Class<? extends T> >(setPairClass) )) );
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve el hash
	 */
	@Override
	public int hashCode() {
		return this.targetClass.hashCode();
	}
	
	/**
	 * @post Devuelve si es igual
	 */
	@Override
	public boolean equals(Object other) {
		if ( other != null ) {
			if ( other instanceof ClassExtra ) {
				return ((ClassExtra<?>) other).getTargetClass().equals(this.getTargetClass());
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
}
