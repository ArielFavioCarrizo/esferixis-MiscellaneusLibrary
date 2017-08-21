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
package com.esferixis.misc.collection.set;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;

/**
 * Decorador de conjunto de prueba
 * 
 * @param <T>
 */
public class TestSet<T> extends AbstractSet<T> {
	protected Set<T> referenceSet;
	protected Set<T> targetSet;
	
	/**
	 * @pre La referencia a la clase de conjunto de referencia y de objetivo no pueden ser nulas
	 * @post Crea un probador de conjunto con el conjunto de referencia y la clase de prueba (Tiene que estar vacía) especificados
	 */
	public TestSet(Class<?> targetSetClass) {
		this.referenceSet = new HashSet<T>();
		
		if ( targetSetClass != null ) {
			try {
				this.targetSet = (Set<T>) targetSetClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Unexpected exception at target set instantation", e);
			}
		}
		else {
			throw new NullPointerException();
		}
		
		Assert.assertTrue( this.targetSet.isEmpty() );
	}
	
	/**
	 * @post Crea un decorador sin inicializar
	 */
	protected TestSet() {
		
	}
	
	/**
	 * @post Hace un test con un iterador
	 * @param it
	 */
	protected void doTestWithIterator(Iterator<T> it) {
		// Test de iterador
		Set<T> remainingElements = new HashSet<T>(this.referenceSet);
		while (it.hasNext()) {
			T eachElement = it.next();
			Assert.assertTrue( remainingElements.remove(eachElement) );
		}
		Assert.assertTrue(remainingElements.isEmpty());
	}
	
	/**
	 * @post Hace un test de integridad
	 */
	protected final void doIntegrityTest() {
		Assert.assertEquals(this.referenceSet.size(), this.targetSet.size());
		
		Assert.assertTrue( this.referenceSet.equals(this.targetSet) );
		Assert.assertTrue( this.targetSet.equals(this.referenceSet) );
		this.doTestWithIterator( this.targetSet.iterator() );
		this.doTestWithIterator( (Iterator<T>) Arrays.asList( this.targetSet.toArray() ).iterator() );
	}
	
	/**
	 * @post Devuelve si contiene el elemento especificado
	 */
	public boolean contains(Object element) {
		boolean containedInReference = this.referenceSet.contains(element);
		Assert.assertEquals( containedInReference, this.targetSet.contains(element) );
		return containedInReference;
	}
	
	/**
	 * @post Devuelve si contiene todos los elementos especificados	
	 */
	public boolean containsAll(Collection<?> elements) {
		boolean containedInReference = this.referenceSet.containsAll(elements);
		Assert.assertEquals( containedInReference, this.targetSet.containsAll(elements) );
		return containedInReference;
	}
	
	/**
	 * @post Agrega un elemento
	 */
	@Override
	public boolean add(T element) {
		boolean changedReference = this.referenceSet.add(element);
		Assert.assertEquals(changedReference, this.targetSet.add(element));
		this.doIntegrityTest();
		return changedReference;
	}
	
	/**
	 * @post Quita un elemento
	 */
	@Override
	public boolean remove(Object element) {
		boolean changedReference = this.referenceSet.remove(element);
		Assert.assertEquals(changedReference, this.targetSet.remove(element));
		this.doIntegrityTest();
		return changedReference;
	}
	
	/**
	 * @post Agrega los elementos especificados
	 */
	@Override
	public boolean addAll(Collection<? extends T> elements) {
		boolean changedReference = this.referenceSet.addAll(elements);
		Assert.assertEquals(changedReference, this.targetSet.addAll(elements));
		this.doIntegrityTest();
		return changedReference;
	}
	
	/**
	 * @post Quita los elementos especificados
	 */
	@Override
	public boolean removeAll(Collection<?> elements) {
		boolean changedReference = this.referenceSet.removeAll(elements);
		Assert.assertEquals(changedReference, this.targetSet.removeAll(elements));
		this.doIntegrityTest();
		return changedReference;
	}
	
	/**
	 * @post Retiene los elementos especificados
	 */
	@Override
	public boolean retainAll(Collection<?> elements) {
		boolean changedReference = this.referenceSet.retainAll(elements);
		Assert.assertEquals(changedReference, this.targetSet.retainAll(elements));
		this.doIntegrityTest();
		return changedReference;
	}

	/**
	 * @post Borra todos los elementos
	 */
	public void clear() {
		this.referenceSet.clear();
		this.targetSet.clear();
		Assert.assertTrue(this.targetSet.isEmpty());
	}
	
	/**
	 * @post Devuelve el iterador
	 */
	@Override
	public Iterator<T> iterator() {
		return this.targetSet.iterator();
	}

	@Override
	public int size() {
		int referenceSize = this.referenceSet.size();
		Assert.assertEquals(referenceSize, this.targetSet.size());
		return referenceSize;
	}
}
