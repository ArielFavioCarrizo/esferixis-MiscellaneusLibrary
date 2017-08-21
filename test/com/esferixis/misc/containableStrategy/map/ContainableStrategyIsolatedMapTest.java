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
package com.esferixis.misc.containableStrategy.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.esferixis.misc.containableStrategy.DummyContainable;
import com.esferixis.misc.containablestrategy.map.ContainableStrategyMap;
import com.esferixis.misc.containablestrategy.map.WeakCachedHashExplicitContainableStrategyMap;


@RunWith(Parameterized.class)

/**
 * Pruebas de JUnit del mapa desde la estrategia de contenibles
 *
 */
public class ContainableStrategyIsolatedMapTest {
	private final TestContainableStrategyMapFactory containableStrategyMapFactory;
	private ContainableStrategyMap<DummyContainable, Object> containableStrategyMap;
	private Map<DummyContainable, Object> expectedMap;
	
	@Parameters
	public static Collection<Object[]> factories() {
		Collection<Object[]> parameters = new ArrayList<Object[]>(ContainableStrategyMapTests.containableStrategyMapFactories.length);
		for ( TestContainableStrategyMapFactory eachFactory : ContainableStrategyMapTests.containableStrategyMapFactories ) {
			parameters.add( new Object[]{ eachFactory } );
		}
		return parameters;
	}
	
	/**
	 * @post Crea un test con la fábrica de colección basada en estrategia contenibles especificada
	 */
	public ContainableStrategyIsolatedMapTest(final TestContainableStrategyMapFactory factory) {
		this.containableStrategyMapFactory = factory;
	}
	
	@Before
	public void testSetup() {
		this.containableStrategyMap = this.containableStrategyMapFactory.create();
		this.expectedMap = new HashMap<DummyContainable, Object>();
	}
	
	/**
	 * @post Test de mapa vacío
	 */
	@Test
	public void testEmpty() {
		Assert.assertEquals( Collections.emptySet(), this.containableStrategyMap.keySet().elements() );
		Assert.assertEquals( Collections.emptyMap(), this.containableStrategyMap.getMap() );
	}
	
	/**
	 * @post Test con un elemento
	 * 
	 *          A   B
	 */
	@Test
	public void testOneElement() {
		DummyContainable containableA = new DummyContainable("Containable A");
		String containableAvalue = "Containable A value";
		DummyContainable containableB = new DummyContainable("Containable B");
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableA) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableB) );
		
		this.containableStrategyMap.put(containableA, containableAvalue);
		this.expectedMap.put(containableA, containableAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableA) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableB) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
	}
	
	/**
	 * @post Test con tres elementos
	 * 
	 *          A    B    C
	 */
	@Test
	public void testTwoElements() {
		DummyContainable containableA = new DummyContainable("Containable A");
		String containableAvalue = "Containable A value";
		DummyContainable containableB = new DummyContainable("Containable B");
		String containableBvalue = "Containable B value";
		DummyContainable containableC = new DummyContainable("Containable B");
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableA) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableB) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		this.containableStrategyMap.put(containableA, containableAvalue);
		this.expectedMap.put(containableA, containableAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableA) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableB) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableC) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		this.containableStrategyMap.put(containableB, containableBvalue);
		this.expectedMap.put(containableB, containableBvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableA) );
		Assert.assertEquals(containableBvalue, this.containableStrategyMap.get(containableB) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableC) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
	}
	
	/**
	 * @post Test con un elemento en el mapa y elementos contenidos
	 * 
	 *              A    C
	 *              |
	 *              AB
	 */
	@Test
	public void testOneElementWithContained() {
		DummyContainable containableA = new DummyContainable("Containable A");
		String containableAvalue = "Containable A value";
		DummyContainable containableAB = new DummyContainable("Containable A-B");
		containableAB.containers().add(containableA);
		DummyContainable containableC = new DummyContainable("Containable C");
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableA) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableAB) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableC) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenedor A
		this.containableStrategyMap.put(containableA, containableAvalue);
		this.expectedMap.put(containableA, containableAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableA));
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableAB));
		Assert.assertNull( this.containableStrategyMap.getEntry(containableC) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
	}
	
	/**
	 * @post Test con dos elementos, uno contenido por otro
	 * 
	 *           A         D
	 *           |
	 *           AB
	 *           |
	 *           ABC
	 */
	@Test
	public void testOneContainedByOne() {
		DummyContainable containableA = new DummyContainable("Containable A");
		String containableAvalue = "Containable A value";
		DummyContainable containableAB = new DummyContainable("Containable A-B");
		containableAB.containers().add(containableA);
		String containableABvalue = "Containable A-B value";
		DummyContainable containableABC = new DummyContainable("Containable A-B-C");
		containableABC.containers().add(containableAB);
		DummyContainable containableD = new DummyContainable("Containable D");
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableA) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableAB) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableABC) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenedor A
		this.containableStrategyMap.put(containableA, containableAvalue);
		this.expectedMap.put(containableA, containableAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableA));
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableAB));
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableABC));
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenedor A-B
		this.containableStrategyMap.put(containableAB, containableABvalue);
		this.expectedMap.put(containableAB, containableABvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableA));
		Assert.assertEquals(containableABvalue, this.containableStrategyMap.get(containableAB));
		Assert.assertEquals(containableABvalue, this.containableStrategyMap.get(containableABC));
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Quitar contenedor A
		this.containableStrategyMap.getMap().remove(containableA);
		this.expectedMap.remove(containableA);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableA) );
		Assert.assertEquals(containableABvalue, this.containableStrategyMap.get(containableAB));
		Assert.assertEquals(containableABvalue, this.containableStrategyMap.get(containableABC));
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Quitar contenedor A-B
		this.containableStrategyMap.getMap().remove(containableAB);
		this.expectedMap.remove(containableAB);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableA) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableAB) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableABC) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenedor A
		this.containableStrategyMap.put(containableA, containableAvalue);
		this.expectedMap.put(containableA, containableAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableA));
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableAB));
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableABC));
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenedor A-B
		this.containableStrategyMap.put(containableAB, containableABvalue);
		this.expectedMap.put(containableAB, containableABvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableA));
		Assert.assertEquals(containableABvalue, this.containableStrategyMap.get(containableAB));
		Assert.assertEquals(containableABvalue, this.containableStrategyMap.get(containableABC));
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Quitar contenedor A
		this.containableStrategyMap.getMap().remove(containableA);
		this.expectedMap.remove(containableA);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableA) );
		Assert.assertEquals(containableABvalue, this.containableStrategyMap.get(containableAB));
		Assert.assertEquals(containableABvalue, this.containableStrategyMap.get(containableABC));
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		// Agregar contenedor A
		this.containableStrategyMap.put(containableA, containableAvalue);
		this.expectedMap.put(containableA, containableAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals(containableAvalue, this.containableStrategyMap.get(containableA));
		Assert.assertEquals(containableABvalue, this.containableStrategyMap.get(containableAB));
		Assert.assertEquals(containableABvalue, this.containableStrategyMap.get(containableABC));
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Quitar el contenedor A y todos sus contenidos
		this.containableStrategyMap.remove(containableA);
		this.expectedMap.clear();
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableA) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableAB) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableABC) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
	}
	
	/**
	 * @post Test con 3 elementos, uno contenedor de dos
	 * 
	 *              A      D
	 *             / \
	 *           AB   AC
	 */
	@Test
	public void testOneContainerOfTwo() {
		DummyContainable containableA = new DummyContainable("Containable A");
		String containableAvalue = "Containable A value";
		DummyContainable containableAB = new DummyContainable("Containable A-B");
		String containableABvalue = "Containable AB value";
		containableAB.containers().add(containableA);
		DummyContainable containableAC = new DummyContainable("Containable A-C");
		String containableACvalue = "Containable AC value";
		containableAC.containers().add(containableA);
		DummyContainable containableD = new DummyContainable("Containable D");
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableA) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableAB) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableAC) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenedor A
		this.containableStrategyMap.put(containableA, containableAvalue);
		this.expectedMap.put(containableA, containableAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( containableAvalue, this.containableStrategyMap.get(containableA) );
		Assert.assertEquals( containableAvalue, this.containableStrategyMap.get(containableAB) );
		Assert.assertEquals( containableAvalue, this.containableStrategyMap.get(containableAC) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenedor A-B
		this.containableStrategyMap.put(containableAB, containableABvalue);
		this.expectedMap.put(containableAB, containableABvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( containableAvalue, this.containableStrategyMap.get(containableA) );
		Assert.assertEquals( containableABvalue, this.containableStrategyMap.get(containableAB) );
		Assert.assertEquals( containableAvalue, this.containableStrategyMap.get(containableAC) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenedor A-C
		this.containableStrategyMap.put(containableAC, containableACvalue);
		this.expectedMap.put(containableAC, containableACvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( containableAvalue, this.containableStrategyMap.get(containableA) );
		Assert.assertEquals( containableABvalue, this.containableStrategyMap.get(containableAB) );
		Assert.assertEquals( containableACvalue, this.containableStrategyMap.get(containableAC) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Quitar contenedor A
		this.containableStrategyMap.getMap().remove(containableA);
		this.expectedMap.remove(containableA);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableA) );
		Assert.assertEquals( containableABvalue, this.containableStrategyMap.get(containableAB) );
		Assert.assertEquals( containableACvalue, this.containableStrategyMap.get(containableAC) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
	}
	
	/**
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @post Test con claves mutualmente contenidas
	 * 
	 * 					M
	 * 				   / \
	 * 				  /   \
	 * 				 /     \
	 * 		      MBA-------MAB
	 *             |         |
	 *           MBAC       MABD
	 */
	@Test
	public void testMutuallyContainers() {
		DummyContainable containableM = new DummyContainable("Containable M");
		String containableMvalue = "Containable M value";
		
		DummyContainable containableMBA = new DummyContainable("Containable M-B-A");
		String containableMBAvalue = "Containable B-A value";
		
		DummyContainable containableMAB = new DummyContainable("Containable M-A-B");
		String containableMABvalue = "Containable A-B value";
		
		DummyContainable containableMBAC = new DummyContainable("Containable M-B-A-C");
		String containableMBACvalue = "Containable B-A-C value";
		DummyContainable containableMABD = new DummyContainable("Containable M-A-B-D");
		String containableMABDvalue = "Containable A-B-D value";
		
		containableMBA.containers().add(containableM);
		containableMBA.containers().add(containableMAB);
		
		containableMAB.containers().add(containableM);
		containableMAB.containers().add(containableMBA);
		
		containableMBAC.containers().add(containableMBA);
		containableMABD.containers().add(containableMAB);
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableM) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableMAB) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableMBA) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableMBAC) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableMABD) );

		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenible MBA
		this.containableStrategyMap.put(containableMBA, containableMBAvalue);
		this.expectedMap.put(containableMBA, containableMBAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableM) );
		Assert.assertEquals( containableMBAvalue, this.containableStrategyMap.get(containableMAB) );
		Assert.assertEquals( containableMBAvalue, this.containableStrategyMap.get(containableMBA) );
		Assert.assertEquals( containableMBAvalue, this.containableStrategyMap.get(containableMBAC) );
		Assert.assertEquals( containableMBAvalue, this.containableStrategyMap.get(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenible MAB
		this.containableStrategyMap.put(containableMAB, containableMABvalue);
		this.expectedMap.put(containableMAB, containableMABvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableM) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBA) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMAB) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBAC) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Quitar contenible MBA
		this.containableStrategyMap.getMap().remove(containableMBA);
		this.expectedMap.remove(containableMBA);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableM) );
		Assert.assertEquals( containableMABvalue, this.containableStrategyMap.get(containableMAB) );
		Assert.assertEquals( containableMABvalue, this.containableStrategyMap.get(containableMBA) );
		Assert.assertEquals( containableMABvalue, this.containableStrategyMap.get(containableMBAC) );
		Assert.assertEquals( containableMABvalue, this.containableStrategyMap.get(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenible MBA
		this.containableStrategyMap.put(containableMBA, containableMBAvalue);
		this.expectedMap.put(containableMBA, containableMBAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableM) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBA) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMAB) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBAC) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenible MBAC
		this.containableStrategyMap.put(containableMBAC, containableMBACvalue);
		this.expectedMap.put(containableMBAC, containableMBACvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableM) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBA) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMAB) );
		Assert.assertEquals( containableMBACvalue, this.containableStrategyMap.get(containableMBAC) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenible MABD
		this.containableStrategyMap.put(containableMABD, containableMABDvalue);
		this.expectedMap.put(containableMABD, containableMABDvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableM) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBA) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMAB) );
		Assert.assertEquals( containableMBACvalue, this.containableStrategyMap.get(containableMBAC) );
		Assert.assertEquals( containableMABDvalue, this.containableStrategyMap.get(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Quitar todas las claves contenedoras contenidas por MAB
		this.containableStrategyMap.remove(containableMAB);
		this.expectedMap.clear();
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableM) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableMAB) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableMBA) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableMBAC) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenedor M
		this.containableStrategyMap.put(containableM, containableMvalue);
		this.expectedMap.put(containableM, containableMvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableM) );
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableMAB) );
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableMBA) );
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableMBAC) );
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenible MBA
		this.containableStrategyMap.put(containableMBA, containableMBAvalue);
		this.expectedMap.put(containableMBA, containableMBAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableM) );
		
		if ( this.containableStrategyMap instanceof WeakCachedHashExplicitContainableStrategyMap<?, ?> ) {
			((WeakCachedHashExplicitContainableStrategyMap<?, ?>) this.containableStrategyMap).clearCache();
		}
		
		Assert.assertEquals( containableMBAvalue, this.containableStrategyMap.get(containableMAB) );
		
		Assert.assertEquals( containableMBAvalue, this.containableStrategyMap.get(containableMBA) );
		Assert.assertEquals( containableMBAvalue, this.containableStrategyMap.get(containableMBAC) );
		Assert.assertEquals( containableMBAvalue, this.containableStrategyMap.get(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenible MAB
		this.containableStrategyMap.put(containableMAB, containableMABvalue);
		this.expectedMap.put(containableMAB, containableMABvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableM) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBA) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMAB) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBAC) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Quitar contenible MBA
		this.containableStrategyMap.getMap().remove(containableMBA);
		this.expectedMap.remove(containableMBA);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableM) );
		Assert.assertEquals( containableMABvalue, this.containableStrategyMap.get(containableMAB) );
		Assert.assertEquals( containableMABvalue, this.containableStrategyMap.get(containableMBA) );
		Assert.assertEquals( containableMABvalue, this.containableStrategyMap.get(containableMBAC) );
		Assert.assertEquals( containableMABvalue, this.containableStrategyMap.get(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenible MBA
		this.containableStrategyMap.put(containableMBA, containableMBAvalue);
		this.expectedMap.put(containableMBA, containableMBAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableM) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBA) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMAB) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBAC) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenible MBAC
		this.containableStrategyMap.put(containableMBAC, containableMBACvalue);
		this.expectedMap.put(containableMBAC, containableMBACvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableM) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBA) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMAB) );
		Assert.assertEquals( containableMBACvalue, this.containableStrategyMap.get(containableMBAC) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Agregar contenible MABD
		this.containableStrategyMap.put(containableMABD, containableMABDvalue);
		this.expectedMap.put(containableMABD, containableMABDvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableM) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMBA) );
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(containableMAB) );
		Assert.assertEquals( containableMBACvalue, this.containableStrategyMap.get(containableMBAC) );
		Assert.assertEquals( containableMABDvalue, this.containableStrategyMap.get(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		// Quitar todas las claves contenedoras contenidas por MBA
		this.containableStrategyMap.remove(containableMBA);
		this.expectedMap.remove( containableMBAC );
		this.expectedMap.remove( containableMABD );
		this.expectedMap.remove( containableMBA );
		this.expectedMap.remove( containableMAB );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableM) );
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableMAB) );
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableMBA) );
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableMBAC) );
		Assert.assertEquals( containableMvalue, this.containableStrategyMap.get(containableMABD) );
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
	}
	
	/**
	 * @post Test con claves mutualmente contenidas con sus contenedores propios
	 * 		 
	 * 	      M       N
	 * 		  |       |
	 * 		  V       V
	 * 		 MBA-----NAB
	 */
	@Test
	public void testTwoMutallyContainersWithOnewayContainers() {
		DummyContainable containableM = new DummyContainable("Containable M");
		String containableMvalue = "Containable M";
		
		DummyContainable containableN = new DummyContainable("Containable N");
		String containableNvalue = "Containable N";
		
		DummyContainable containableMBA = new DummyContainable("Containable M-(B-A)");
		String containableMBAvalue = "Containable M-(B-A) value";
		containableMBA.containers().add(containableM);
		
		DummyContainable containableNAB = new DummyContainable("Containable N-(A-B)");
		String containableMABvalue = "Containable N-(A-B) value";
		containableNAB.containers().add(containableN);
		
		containableMBA.containers().add(containableNAB);
		containableNAB.containers().add(containableMBA);
		
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableM) );
		Assert.assertNull( this.containableStrategyMap.getEntry(containableN));
		Assert.assertNull( this.containableStrategyMap.getEntry(containableMBA));
		Assert.assertNull( this.containableStrategyMap.getEntry(containableNAB));
		
		// Asocia el contenedor M-(B-A) con su valor
		this.containableStrategyMap.getMap().put(containableMBA, containableMBAvalue);
		this.expectedMap.put(containableMBA, containableMBAvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableM) );
		Assert.assertEquals(containableMBAvalue, this.containableStrategyMap.get(containableMBA));
		Assert.assertNull( this.containableStrategyMap.getEntry(containableN) );
		Assert.assertEquals(containableMBAvalue, this.containableStrategyMap.get(containableNAB));
		
		// Asocia el contenedor N con su valor
		this.containableStrategyMap.getMap().put(containableN, containableNvalue);
		this.expectedMap.put(containableN, containableNvalue);
		Assert.assertEquals( this.expectedMap, this.containableStrategyMap.getMap() );
		
		Assert.assertNull( this.containableStrategyMap.getEntry(containableM) );
		Assert.assertEquals( containableMBAvalue, this.containableStrategyMap.get(containableMBA) );
		Assert.assertEquals( containableNvalue, this.containableStrategyMap.get(containableN) );
		Assert.assertEquals( containableMBAvalue, this.containableStrategyMap.get(containableNAB) );
	}
	
	/**
	 * @post Realiza un test con los siguientes contenedores
	 * 			 0
	 * 			 |
	 * 			 V
	 * 	         1<--->4<--->5
	 */
	@Test
	public void testThreeBidirectionalContainersWithOneContained() {
		DummyContainable container0 = new DummyContainable("Container 0");
		DummyContainable container1 = new DummyContainable("Container 1");
		DummyContainable container4 = new DummyContainable("Container 4");
		DummyContainable container5 = new DummyContainable("Container 5");
		
		container1.containers().add(container0);
		container1.containers().add(container4);
		
		container4.containers().add(container1);
		container4.containers().add(container5);
		
		container5.containers().add(container4);
		
		Assert.assertEquals(this.expectedMap, this.containableStrategyMap.getMap());
		
		Object container1value = new Object();
		this.containableStrategyMap.getMap().put(container1, container1value);
		this.expectedMap.put(container1, container1value);
		Object container5value = new Object();
		this.containableStrategyMap.getMap().put(container5, container5value);
		this.expectedMap.put(container5, container5value);
		
		Assert.assertEquals(this.expectedMap, this.containableStrategyMap.getMap());
		
		Assert.assertTrue( this.containableStrategyMap.ambiguousKey(container4) );
	}
}
