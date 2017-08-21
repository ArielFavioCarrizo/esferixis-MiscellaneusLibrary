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
package com.esferixis.misc.iterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.esferixis.misc.binaryclassifier.BinaryClassifier;
import com.esferixis.misc.iterator.FilteredIterator;


@RunWith(Parameterized.class)

/**
 * Pruebas de JUnit del iterador filtrado
 */
public class FilteredIteratorTest {
	private final Collection<Object> elements;
	private final Set<Object> excludedElements;
	
	@Parameters
	public static Collection<Object[]> testCombinations() {
		return Arrays.asList( new Object[][]{
				{ Collections.emptyList(), Collections.emptySet() },
				{ Collections.emptyList(), new HashSet<Integer>(Arrays.asList(0, 1, 2) ) },
				{ Arrays.asList(0, 1, 3), Collections.emptySet() },
				{ Arrays.asList(6, 3, 4, 5), new HashSet<Integer>(Arrays.asList(3) ) },
				{ Arrays.asList(6, 3, 4, 5), new HashSet<Integer>(Arrays.asList(4) ) },
				{ Arrays.asList(5, 6, 5, 3, 4, 8, 9, 10, 30, 40, 3, 18, 25, 26, 40), new HashSet<Integer>(Arrays.asList(3, 4, 25) ) }
		} );
	}
	
	/**
	 * @pre Ninguno de los parámetros pueden ser nulos
	 * @post Crea un test con los elementos y los elementos excluidos especificados
	 */
	public FilteredIteratorTest(Collection<?> elements, Set<?> excludedElements) {
		if ( ( elements != null ) && ( excludedElements != null ) ) {
			this.elements = Collections.unmodifiableCollection(elements);
			this.excludedElements = Collections.unmodifiableSet(excludedElements);
		}
		else {
			throw new NullPointerException();
		}
	}
	
	private class InclusionFilter<T> implements BinaryClassifier<T> {
		private Set<T> excludedElements;
		
		/**
		 * @pre El conjunto de elementos excluidos no puede ser nulo
		 * @post Crea un filtro de inclusión con el conjunto de elementos excluido especificado
		 */
		public InclusionFilter(Set<T> excludedElements) {
			if ( excludedElements != null ) {
				this.excludedElements = excludedElements;
			}
			else {
				throw new NullPointerException();
			}
		}
		
		/**
		 * @post Devuelve si el elemento especificado será excluido o no
		 */
		@Override
		public boolean evaluate(T element) {
			return !this.excludedElements.contains(element);
		}
		
	}
	
	private <T> void filteredIteratorTest(Collection<T> elements, Set<T> excludedElements) {
		Iterator<T> filteredIterator = new FilteredIterator<T>(elements.iterator(), new InclusionFilter<T>(excludedElements));
		
		List<T> iterationList = new LinkedList<T>(elements);
		iterationList.removeAll(excludedElements);
		
		Iterator<T> iterationListIterator = iterationList.iterator();
		
		while ( filteredIterator.hasNext() && iterationListIterator.hasNext() ) {
			Assert.assertEquals(iterationListIterator.next(), filteredIterator.next());
		}
		
		Assert.assertEquals(iterationListIterator.hasNext(), filteredIterator.hasNext());
	}
	
	@Test
	public void doTest() {
		this.filteredIteratorTest(this.elements, this.excludedElements);
	}
}
