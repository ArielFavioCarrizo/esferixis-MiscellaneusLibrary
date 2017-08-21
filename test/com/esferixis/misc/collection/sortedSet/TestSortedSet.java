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
package com.esferixis.misc.collection.sortedSet;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.esferixis.misc.collection.set.TestSet;

import junit.framework.Assert;

/**
 * Decorador de conjunto ordenado de prueba
 */
public class TestSortedSet<T> extends TestSet<T> {
	
	/**
	 * @pre La referencia a la clase de conjunto de referencia y de objetivo no pueden ser nulas
	 * @post Crea un probador de conjunto con el conjunto de referencia y la clase de prueba (Tiene que estar vacía) especificados
	 */
	public TestSortedSet(Class<?> targetSortedSetClass) {
		this( targetSortedSetClass, null );
	}
	
	/**
	 * @pre La referencia a la clase de conjunto de referencia y de objetivo no pueden ser nulas
	 * @post Crea un probador de conjunto con el conjunto de referencia y la clase de prueba (Tiene que estar vacía) especificados
	 */
	public TestSortedSet(Class<?> targetSortedSetClass, Comparator<T> comparator) {
		super();
		this.referenceSet = new TreeSet<T>();
		
		if ( targetSortedSetClass != null ) {
			try {
				this.targetSet = (SortedSet<T>) ( targetSortedSetClass.getConstructor( Comparator.class ).newInstance( comparator ) );
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
	 * @post Hace un test con un iterador
	 * @param it
	 */
	@Override
	protected void doTestWithIterator(Iterator<T> it) {
		Iterator<T> referenceIterator = this.referenceSet.iterator();
		// Test de iterador
		while (referenceIterator.hasNext() && it.hasNext()) {
			Assert.assertTrue(referenceIterator.hasNext());
			Assert.assertEquals(referenceIterator.next(), it.next());
		}
		
		Assert.assertFalse(referenceIterator.hasNext());
		Assert.assertFalse(it.hasNext());
	}
	
	
}
