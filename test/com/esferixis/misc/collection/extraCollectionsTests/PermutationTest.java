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
package com.esferixis.misc.collection.extraCollectionsTests;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.esferixis.misc.collection.CollectionsExtra;
import com.esferixis.misc.collection.set.BinarySet;

/**
 * Pruebas sobre métodos adicionales para colecciones
 */
public class PermutationTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testEmpty() {
		Assert.assertEquals( (Collection< BinarySet<Object> >) Collections.EMPTY_SET, CollectionsExtra.permuteCollectionPair( new BinarySet< Collection<Object> >(Collections.EMPTY_SET, Collections.EMPTY_SET) ) );
	}
	
	@Test
	public void testOnePair() {
		BinarySet<Integer> onePair = new BinarySet<Integer>(1, 2);
		Assert.assertEquals( Collections.singleton( onePair ), CollectionsExtra.permuteCollectionPair( new BinarySet< Collection<Integer> >(Collections.singleton( new Integer(1) ), Collections.singleton( new Integer(2) ) ) ) );
	}
	
	@Test
	public void testTwoLists() {
		Integer[] array1 = new Integer[]{1, 2, 3};
		Integer[] array2 = new Integer[]{4, 5, 6};
		Set< BinarySet<Integer> > result = new HashSet< BinarySet<Integer> >();
		result.add( new BinarySet<Integer>(1, 4) );
		result.add( new BinarySet<Integer>(1, 5) );
		result.add( new BinarySet<Integer>(1, 6) );
		result.add( new BinarySet<Integer>(2, 4) );
		result.add( new BinarySet<Integer>(2, 5) );
		result.add( new BinarySet<Integer>(2, 6) );
		result.add( new BinarySet<Integer>(3, 4) );
		result.add( new BinarySet<Integer>(3, 5) );
		result.add( new BinarySet<Integer>(3, 6) );
		Assert.assertEquals( result, CollectionsExtra.permuteCollectionPair( new BinarySet< Collection<Integer> >( Arrays.asList( array1 ) , Arrays.asList( array2 ) ) ) );
	}
	
	@Test
	public void testOneList() {
		List<Integer> oneList = Arrays.asList( new Integer[]{1, 2, 3} );
		Set< BinarySet<Integer> > result = new HashSet< BinarySet<Integer> >();
		result.add( new BinarySet<Integer>(1, 1) );
		result.add( new BinarySet<Integer>(1, 2) );
		result.add( new BinarySet<Integer>(1, 3) );
		result.add( new BinarySet<Integer>(2, 2) );
		result.add( new BinarySet<Integer>(2, 3) );
		result.add( new BinarySet<Integer>(3, 3) );
		Assert.assertEquals( result, CollectionsExtra.permuteCollectionPair( new BinarySet< Collection<Integer> >( oneList, oneList ) ) );
	}
}
