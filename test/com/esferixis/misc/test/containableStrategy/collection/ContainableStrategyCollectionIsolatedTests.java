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

package com.esferixis.misc.test.containableStrategy.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.esferixis.misc.containablestrategy.collection.ContainableStrategyCollection;
import com.esferixis.misc.test.containableStrategy.DummyContainable;

@RunWith(Parameterized.class)

/**
 * Pruebas de colecciones aisladas en JUnit de la colección desde la estrategia de contenibles
 */
public class ContainableStrategyCollectionIsolatedTests {
	private final TestContainableStrategyCollectionFactory containableStrategyCollectionFactory;
	private ContainableStrategyCollection<DummyContainable> containableStrategyCollection;
	
	@Parameters
	public static Collection<Object[]> factories() {
		Collection<Object[]> parameters = new ArrayList<Object[]>(ContainableStrategyCollectionTests.containableStrategyCollectionFactories.length);
		for ( TestContainableStrategyCollectionFactory eachFactory : ContainableStrategyCollectionTests.containableStrategyCollectionFactories ) {
			parameters.add( new Object[]{ eachFactory } );
		}
		return parameters;
	}
	
	/**
	 * @post Crea un test con la fábrica de colección basada en estrategia contenibles especificada
	 */
	public ContainableStrategyCollectionIsolatedTests(final TestContainableStrategyCollectionFactory factory) {
		this.containableStrategyCollectionFactory = factory;
	}
	
	@Before
	public void createCollection() {
		this.containableStrategyCollection = this.containableStrategyCollectionFactory.create();
	}
	
	/**
	 * @post Test de elemento vacío
	 */
	@Test
	public void testEmpty() {
		Assert.assertTrue(containableStrategyCollection.isEmpty());
		Assert.assertTrue(containableStrategyCollection.getRootElements().isEmpty());
	}
	
	/**
	 * @post Test de presencia de un sólo elemento
	 */
	@Test
	public void testOneElement() {
		Assert.assertTrue(containableStrategyCollection.isEmpty());
		Assert.assertTrue(containableStrategyCollection.getRootElements().isEmpty());
		
		DummyContainable element = new DummyContainable("A");
		this.containableStrategyCollection.add(element);
		
		Assert.assertEquals(Collections.singletonList(element), new ArrayList<DummyContainable>(containableStrategyCollection.elements()));
		Assert.assertEquals(Collections.singletonList(element), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
	}
	
	/**
	 * @post Test de presencia de un sólo elemento y sus contenidos
	 */
	@Test
	public void testOneElementAndContainables() {
		Assert.assertTrue(containableStrategyCollection.isEmpty());
		
		DummyContainable containableA = new DummyContainable("A");
		this.containableStrategyCollection.add(containableA);
		
		Assert.assertEquals(Collections.singletonList(containableA), new ArrayList<DummyContainable>(containableStrategyCollection.elements()));
		Assert.assertEquals(Collections.singletonList(containableA), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		DummyContainable containableAB = new DummyContainable("A-B");
		containableAB.containers().add(containableA);
		
		this.containableStrategyCollection.add(containableAB);

		Assert.assertEquals(Arrays.asList(containableA, containableAB), new ArrayList<DummyContainable>(containableStrategyCollection.elements()));
		Assert.assertEquals(Collections.singletonList(containableA), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		DummyContainable containableC = new DummyContainable("C");
		Assert.assertFalse( containableStrategyCollection.contains(containableC) );
		
		Assert.assertEquals(Arrays.asList(containableA, containableAB), new ArrayList<DummyContainable>(containableStrategyCollection.elements()));
		Assert.assertEquals(Collections.singletonList(containableA), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		Assert.assertFalse( containableStrategyCollection.elements().contains(containableC) );
		Assert.assertFalse( containableStrategyCollection.getRootElements().contains(containableC) );
	}
	
	/**
	 * @post Test de presencia con elementos de ramales diferentes
	 */
	@Test
	public void testThreeElements() {
		DummyContainable containableA = new DummyContainable("A");
		
		DummyContainable containableAA = new DummyContainable("A-A");
		containableAA.containers().add(containableA);
		
		DummyContainable containableAB = new DummyContainable("A-B");
		containableAB.containers().add(containableA);
		
		DummyContainable containableAC = new DummyContainable("A-C");
		containableAC.containers().add(containableA);
		
		DummyContainable containableAAA = new DummyContainable("A-A-A");
		containableAAA.containers().add(containableAA);
		
		DummyContainable containableABA = new DummyContainable("A-B-A");
		containableABA.containers().add(containableAB);
		
		DummyContainable containableB = new DummyContainable("B");
		
		this.containableStrategyCollection.elements().add(containableAA);
		
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAAA) );
		
		Assert.assertFalse( this.containableStrategyCollection.contains(containableABA) );
		
		Assert.assertEquals(Collections.singletonList(containableAA), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		this.containableStrategyCollection.elements().remove(containableAA);
		this.containableStrategyCollection.elements().add(containableAAA);
		
		Assert.assertFalse( this.containableStrategyCollection.contains(containableAA) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAAA) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableAC) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableA) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableB) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableABA) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableAB) );
		
		Assert.assertEquals(Collections.singletonList(containableAAA), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		this.containableStrategyCollection.elements().add(containableA);
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAA) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAAA) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAC) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableA) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableB) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableABA) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAB) );
		
		Assert.assertEquals(Collections.singletonList(containableA), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		this.containableStrategyCollection.elements().remove(containableAAA);
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAA) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAAA) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAC) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableA) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableB) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableABA) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAB) );
		
		Assert.assertEquals(Collections.singletonList(containableA), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		this.containableStrategyCollection.elements().add(containableAB);
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAA) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAAA) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAC) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableA) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableB) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableABA) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAB) );
		
		Assert.assertEquals(Collections.singletonList(containableA), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		this.containableStrategyCollection.elements().remove(containableA);
		Assert.assertFalse( this.containableStrategyCollection.contains(containableAA) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableAAA) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableAC) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableA) );
		Assert.assertFalse( this.containableStrategyCollection.contains(containableB) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableABA) );
		Assert.assertTrue( this.containableStrategyCollection.contains(containableAB) );
		
		Assert.assertEquals(Collections.singletonList(containableAB), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
	}
	
	/**
	 * @post Test de remoción de un elemento
	 */
	@Test
	public void testRemoveContainedByElement() {
		DummyContainable containableA = new DummyContainable("A");
		
		DummyContainable containableAB = new DummyContainable("A-B");
		containableAB.containers().add(containableA);
		
		this.containableStrategyCollection.add(containableA);
		Assert.assertEquals(Collections.singletonList(containableA), new ArrayList<DummyContainable>( this.containableStrategyCollection.elements() ) );
		Assert.assertEquals(Collections.singletonList(containableA), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		this.containableStrategyCollection.remove(containableAB);
		Assert.assertEquals(Collections.singletonList(containableA), new ArrayList<DummyContainable>( this.containableStrategyCollection.elements() ) );
		Assert.assertEquals(Collections.singletonList(containableA), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		this.containableStrategyCollection.remove(containableA);
		
		Assert.assertTrue( this.containableStrategyCollection.isEmpty() );
		Assert.assertTrue( this.containableStrategyCollection.getRootElements().isEmpty() );
	}
	
	/**
	 * @post Test de remoción de varios ramales
	 */
	@Test
	public void testRemoveContainedByElements() {
		DummyContainable containableA = new DummyContainable("A");
		
		DummyContainable containableAA = new DummyContainable("A-A");
		containableAA.containers().add(containableA);
		
		DummyContainable containableAB = new DummyContainable("A-B");
		containableAB.containers().add(containableA);
		
		DummyContainable containableAC = new DummyContainable("A-C");
		containableAC.containers().add(containableA);
		
		DummyContainable containableAAA = new DummyContainable("A-A-A");
		containableAAA.containers().add(containableAA);
		
		DummyContainable containableABA = new DummyContainable("A-B-A");
		containableABA.containers().add(containableAB);
		
		DummyContainable containableB = new DummyContainable("B");
		
		Assert.assertTrue(this.containableStrategyCollection.isEmpty());
		Assert.assertTrue(this.containableStrategyCollection.getRootElements().isEmpty());
		
		List<DummyContainable> expectedElements = new LinkedList<DummyContainable>( Arrays.asList(new DummyContainable[]{
		containableA, containableAA, containableAB, containableAC, containableAAA, containableABA, containableB } ) );
		for ( DummyContainable eachElement : expectedElements ) {
			this.containableStrategyCollection.add(eachElement);
		}
		Assert.assertEquals(expectedElements, new LinkedList<DummyContainable>( this.containableStrategyCollection.elements() ));
		Assert.assertEquals(Arrays.asList(containableA, containableB), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		// Remover A-A y AAA
		this.containableStrategyCollection.remove(containableAA);
		expectedElements.remove(containableAA);
		expectedElements.remove(containableAAA);
		Assert.assertEquals(expectedElements, new LinkedList<DummyContainable>( this.containableStrategyCollection.elements() ));
		Assert.assertEquals(Arrays.asList(containableA, containableB), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		// Agregar A-A
		this.containableStrategyCollection.add(containableAA);
		expectedElements.add(containableAA);
		
		Assert.assertEquals(expectedElements, new LinkedList<DummyContainable>( this.containableStrategyCollection.elements() ));
		Assert.assertEquals(Arrays.asList(containableA, containableB), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		// Quitar A-C
		this.containableStrategyCollection.remove(containableAC);
		expectedElements.remove(containableAC);
		Assert.assertEquals(expectedElements, new LinkedList<DummyContainable>( this.containableStrategyCollection.elements() ));
		Assert.assertEquals(Arrays.asList(containableA, containableB), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		// Quitar A-B
		this.containableStrategyCollection.remove(containableAB);
		expectedElements.remove(containableAB);
		expectedElements.remove(containableABA);
		Assert.assertEquals(expectedElements, new LinkedList<DummyContainable>( this.containableStrategyCollection.elements() ));
		Assert.assertEquals(Arrays.asList(containableA, containableB), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		// Agregar A-A-A
		this.containableStrategyCollection.add(containableAAA);
		expectedElements.add(containableAAA);
		Assert.assertEquals(expectedElements, new LinkedList<DummyContainable>( this.containableStrategyCollection.elements() ));
		Assert.assertEquals(Arrays.asList(containableA, containableB), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		// Agregar A-B-A
		this.containableStrategyCollection.add(containableABA);
		expectedElements.add(containableABA);
		Assert.assertEquals(expectedElements, new LinkedList<DummyContainable>( this.containableStrategyCollection.elements() ));
		Assert.assertEquals(Arrays.asList(containableA, containableB), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
		
		// Quitar A
		this.containableStrategyCollection.remove(containableA);
		expectedElements.remove(containableA);
		expectedElements.remove(containableAA);
		expectedElements.remove(containableAAA);
		expectedElements.remove(containableABA);
		Assert.assertEquals(expectedElements, new LinkedList<DummyContainable>( this.containableStrategyCollection.elements() ));
		Assert.assertEquals(Arrays.asList(containableB), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
	}
	
	/**
	 * @post Realiza un test entre dos elementos mutualmente contenedores
	 */
	@Test
	public void testWithTwoMutuallyContainers() {
		DummyContainable containableA = new DummyContainable("A");
		DummyContainable containableB = new DummyContainable("B");
		
		containableA.containers().add(containableB);
		containableB.containers().add(containableA);
		
		Assert.assertTrue(containableStrategyCollection.isEmpty());
		Assert.assertTrue(containableStrategyCollection.getRootElements().isEmpty());
		
		this.containableStrategyCollection.add(containableA);
		this.containableStrategyCollection.add(containableB);
		
		Assert.assertEquals(Arrays.asList(containableA, containableB), new LinkedList<DummyContainable>( this.containableStrategyCollection.elements() ));
		Assert.assertEquals(Arrays.asList(containableA, containableB), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
	}
	
	/**
	 * @post Realiza un test entre tres elementos mutualmente contenedores
	 */
	@Test
	public void testWithThreeMutuallyContainers() {
		DummyContainable containableA = new DummyContainable("A");
		DummyContainable containableB = new DummyContainable("B");
		DummyContainable containableC = new DummyContainable("C");
		
		containableA.containers().add(containableB);
		containableA.containers().add(containableC);
		
		containableB.containers().add(containableA);
		containableB.containers().add(containableC);
		
		containableC.containers().add(containableA);
		containableC.containers().add(containableB);
		
		Assert.assertTrue(containableStrategyCollection.isEmpty());
		Assert.assertTrue(containableStrategyCollection.getRootElements().isEmpty());
		
		this.containableStrategyCollection.add(containableA);
		this.containableStrategyCollection.add(containableB);
		this.containableStrategyCollection.add(containableC);
		
		Assert.assertEquals(Arrays.asList(containableA, containableB, containableC), new LinkedList<DummyContainable>( this.containableStrategyCollection.elements() ));
		Assert.assertEquals(Arrays.asList(containableA, containableB, containableC), new ArrayList<DummyContainable>(containableStrategyCollection.getRootElements()));
	}
}
