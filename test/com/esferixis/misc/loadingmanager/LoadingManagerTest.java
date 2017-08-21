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

import org.junit.Before;
import org.junit.Test;

import com.esferixis.misc.loader.DataLoadingErrorException;
import com.esferixis.misc.loader.MemoryLoader;
import com.esferixis.misc.loadingmanager.LinkedMruLoadingManager;
import com.esferixis.misc.loadingmanager.LoadingManager;
import com.esferixis.misc.loadingmanager.OutOfSpace;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Assert;

public class LoadingManagerTest {
	private LoadingManager<MockLoadableElement> garbageCollectedLoader;
	private MockLoadingStrategy loadingStrategy;
	
	/**
	 * @post Crea el cargador
	 */
	@Before
	public void prepare() {
		this.loadingStrategy = new MockLoadingStrategy();
		this.garbageCollectedLoader = new LinkedMruLoadingManager<MockLoadableElement>(loadingStrategy, 5, 25);
	}
	
	private void testLoading(Collection<MockLoadableElement> elements) {
		try {
			this.garbageCollectedLoader.loadElements(elements);
		} catch (DataLoadingErrorException e) {
			throw new RuntimeException(e);
		}
		
		Assert.assertTrue(this.loadingStrategy.hasLoaded(elements));
	}
	
	private void testLoading(MockLoadableElement... elements) {
		this.testLoading(Arrays.asList(elements));
	}
	
	@Test
	public void testOne() {
		MockLoadableElement element = new MockLoadableElement(new MockSource("Source 1", 5));
		
		this.testLoading(element);
	}
	
	@Test
	public void testAllElementsInCapacity() {
		MockLoadableElement element1 = new MockLoadableElement(new MockSource("Source 1", 5));
		MockLoadableElement element2 = new MockLoadableElement(new MockSource("Source 2", 7));
		MockLoadableElement element3 = new MockLoadableElement(new MockSource("Source 3", 3));
		MockLoadableElement element4 = new MockLoadableElement(new MockSource("Source 4", 8));
		
		this.testLoading(element1, element2, element3, element4);
	}
	
	@Test
	public void testElementsWithUnloading1() {
		MockLoadableElement element1 = new MockLoadableElement(new MockSource("Source 1", 5));
		MockLoadableElement element2 = new MockLoadableElement(new MockSource("Source 2", 7));
		MockLoadableElement element3 = new MockLoadableElement(new MockSource("Source 3", 3));
		MockLoadableElement element4 = new MockLoadableElement(new MockSource("Source 4", 8));
		MockLoadableElement element5 = new MockLoadableElement(new MockSource("Source 5", 6));
		MockLoadableElement element6 = new MockLoadableElement(new MockSource("Source 6", 7));
		
		this.testLoading(element1, element2, element3, element4);
		
		this.testLoading(element5, element6);
	}
	
	@Test
	public void testElementsWithUnloading2() {
		MockLoadableElement element1 = new MockLoadableElement(new MockSource("Source 1", 5));
		MockLoadableElement element2 = new MockLoadableElement(new MockSource("Source 2", 7));
		MockLoadableElement element3 = new MockLoadableElement(new MockSource("Source 3", 3));
		MockLoadableElement element4 = new MockLoadableElement(new MockSource("Source 4", 8));
		MockLoadableElement element5 = new MockLoadableElement(new MockSource("Source 5", 6));
		MockLoadableElement element6 = new MockLoadableElement(new MockSource("Source 6", 7));
		MockLoadableElement element7 = new MockLoadableElement(new MockSource("Source 7", 5));
		MockLoadableElement element8 = new MockLoadableElement(new MockSource("Source 8", 3));
		MockLoadableElement element9 = new MockLoadableElement(new MockSource("Source 9", 2));
		MockLoadableElement element10 = new MockLoadableElement(new MockSource("Source 10", 5));
		
		this.testLoading(element1, element2);
		this.testLoading(element5, element8, element10);
		this.testLoading(element3, element8, element4);
		this.testLoading(element3, element10, element2, element7);
		this.testLoading(element1, element4, element6, element9);
		this.testLoading(element3, element5);
		this.testLoading(element4, element6, element7);
	}
	
	@Test
	public void testWithDependencies1() {
		final MockSource source1 = new MockSource("Source 1", 5);
		final MockSource source1_2 = new MockSource("Source 1.2", 3, source1);
		final MockSource source2 = new MockSource("Source 2", 8);
		final MockSource source5 = new MockSource("Source 5", 2);
		
		MockLoadableElement element1 = new MockLoadableElement(source1);
		MockLoadableElement element1_1 = new MockLoadableElement(new MockSource("Source 1.1", 7, source1));
		MockLoadableElement element1_2 = new MockLoadableElement(source1_2);
		MockLoadableElement element2 = new MockLoadableElement(source2);
		MockLoadableElement element3 = new MockLoadableElement(new MockSource("Source 3", 6));
		MockLoadableElement element1_2_1 = new MockLoadableElement(new MockSource("Source 1.2.1", 7, source1_2));
		MockLoadableElement element2_1 = new MockLoadableElement(new MockSource("Source 2.1", 5, source2));
		MockLoadableElement element4 = new MockLoadableElement(new MockSource("Source 4", 3));
		MockLoadableElement element5 = new MockLoadableElement(source5);
		MockLoadableElement element5_1 = new MockLoadableElement(new MockSource("Source 5.1", 5, source5));
		
		this.testLoading(element1, element1_1, element1_2, element1_2_1);
		this.testLoading(element2, element3);
		this.testLoading(element2, element2_1, element5, element5_1);
		this.testLoading(element4, element5);
	}
	
	@Test
	public void testWithDependencies2() {
		final MockSource source1 = new MockSource("Source 1", 5);
		final MockSource source1_2 = new MockSource("Source 1.2", 3, source1);
		final MockSource source2 = new MockSource("Source 2", 8);
		final MockSource source5 = new MockSource("Source 5", 2);
		
		MockLoadableElement element1 = new MockLoadableElement(source1);
		MockLoadableElement element1_1 = new MockLoadableElement(new MockSource("Source 1.1", 7, source1));
		MockLoadableElement element1_2 = new MockLoadableElement(source1_2);
		MockLoadableElement element2 = new MockLoadableElement(source2);
		MockLoadableElement element3 = new MockLoadableElement(new MockSource("Source 3", 6));
		MockLoadableElement element1_2_1 = new MockLoadableElement(new MockSource("Source 1.2.1", 7, source1_2));
		MockLoadableElement element2_1 = new MockLoadableElement(new MockSource("Source 2.1", 5, source2));
		MockLoadableElement element4 = new MockLoadableElement(new MockSource("Source 4", 3));
		MockLoadableElement element5 = new MockLoadableElement(source5);
		MockLoadableElement element5_1 = new MockLoadableElement(new MockSource("Source 5.1", 5, source5));
		
		this.testLoading(element1_2_1, element3);
		this.testLoading(element4, element5);
		this.testLoading(element2_1,  element5_1);
	}
	
	@Test
	public void testWithDependencies3() {
		final MockSource source1 = new MockSource("Source 1", 5);
		final MockSource source1_2 = new MockSource("Source 1.2", 3, source1);
		final MockSource source2 = new MockSource("Source 2", 8);
		final MockSource source5 = new MockSource("Source 5", 2);
		
		MockLoadableElement element1 = new MockLoadableElement(source1);
		MockLoadableElement element1_1 = new MockLoadableElement(new MockSource("Source 1.1", 7, source1));
		MockLoadableElement element1_2 = new MockLoadableElement(source1_2);
		MockLoadableElement element2 = new MockLoadableElement(source2);
		MockLoadableElement element3 = new MockLoadableElement(new MockSource("Source 3", 6));
		MockLoadableElement element1_2_1 = new MockLoadableElement(new MockSource("Source 1.2.1", 7, source1_2));
		MockLoadableElement element2_1 = new MockLoadableElement(new MockSource("Source 2.1", 5, source2));
		MockLoadableElement element4 = new MockLoadableElement(new MockSource("Source 4", 3));
		MockLoadableElement element5 = new MockLoadableElement(source5);
		MockLoadableElement element5_1 = new MockLoadableElement(new MockSource("Source 5.1", 5, source5));
		
		this.testLoading(element2_1,  element5_1);
		
		try {
			this.testLoading(element1_2_1, element3, element2_1);
		}
		catch ( OutOfSpace e ) {
			Assert.assertTrue(this.loadingStrategy.hasLoaded(element2_1, element5_1));
		}
	}
}
