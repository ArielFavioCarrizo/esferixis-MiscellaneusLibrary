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
package com.esferixis.misc.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.esferixis.misc.collection.UnionCollection;

/**
 * Test de unión de colecciones
 */
public class UnionCollectionTest {
	private List<Integer> unitedList;
	private UnionCollection<Integer> unionCollection;
	
	@SuppressWarnings("unchecked")
	@Before
	public void beforeTest() {
		List<Integer> list1, list2;
		list1 = Arrays.asList( new Integer[]{1,2,3,4,5} );
		list2 = Arrays.asList( new Integer[]{6,7,8,9,10} );
		this.unitedList = new LinkedList<Integer>();
		this.unitedList.addAll(list1);
		this.unitedList.addAll(list2);
		this.unionCollection = new UnionCollection<Integer>( Arrays.asList( (Collection<Integer>[]) new Collection[]{list1, list2} ) ); 
	}
	
	@Test
	public void equalsTest() {
		List<Integer> listFromUnionCollection = new LinkedList<Integer>();
		listFromUnionCollection.addAll(this.unionCollection);
		Assert.assertEquals(this.unitedList, listFromUnionCollection);
	}
	
	@Test
	public void iteratorTest() {
		List<Integer> listFromUnionCollection = new LinkedList<Integer>();
		listFromUnionCollection.addAll(this.unionCollection);
		Iterator<Integer> iteratorFromList = listFromUnionCollection.iterator();
		Iterator<Integer> iteratorFromUnionCollection = this.unionCollection.iterator();
		while ( iteratorFromUnionCollection.hasNext() && iteratorFromList.hasNext() ) {
			Assert.assertEquals(iteratorFromList.next(), iteratorFromUnionCollection.next());
		}
		Assert.assertFalse(iteratorFromList.hasNext());
		Assert.assertFalse(iteratorFromUnionCollection.hasNext());
	}
}
