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
package com.esferixis.misc.test.containableStrategy.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.esferixis.misc.containablestrategy.map.ContainableStrategyMap;
import com.esferixis.misc.reference.InmutableReference;
import com.esferixis.misc.test.containableStrategy.DummyContainable;

import junit.framework.Assert;

/**
 * Ésta clase contiene pruebas con contenedores aleatorios
 * en donde comprueba que todos los mapas reporten el mismo
 * resultado
 */

@RunWith(Parameterized.class)
public class ImplementationsConsistencyStochasticMapTest {
	
	private Random random;
	
	// Probabilidad de ciclos en los contenedores
	private final float cyclesProbability;
	
	private static final int containersQuantity=32; // Cantidad de contenedores
	private static final float rootContainerProbability=0.5f; // Probabilidad de crear contenedores raices
	private static final int addRemoveCycles=1024*10; // Cantidad de ciclos de agregado/quitado
	private static final int operationsInAddRemoveStateTransition = 256; // Operaciones de agregado/quitado en transición de estado
	private static final float changeProbabilityInAdd = 0.1f; // Probabilidad de cambio en operaciones de agregado
	
	private static float[] cyclesProbabilityForTesting = new float[]{0.0f, 0.1f, 0.2f};
	
	private static final int rngSeed = 845766; // Semilla para el generador de números aleatorios
	
	@Parameters
	public static Collection<Object[]> parameters() {
		Collection<Object[]> parameters = new ArrayList<Object[]>(cyclesProbabilityForTesting.length);
		for ( float eachProbability : cyclesProbabilityForTesting ) {
			parameters.add(new Object[]{ new Float(eachProbability) });
		}
		
		return parameters;
	}
	
	/**
	 * @pre La probabilidad de ciclos no puede ser nula
	 * @post Crea un test con la probabilidad de ciclos especificada
	 */
	public ImplementationsConsistencyStochasticMapTest(Float cyclesProbability) {
		if ( cyclesProbability != null ) {
			this.cyclesProbability = cyclesProbability;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	@Before
	public void testSetup() {
		this.random = new Random(rngSeed); // Fuente de números aleatorios
	}
	
	/**
	 * @pre No puede ser nulo
	 * @post Realiza una prueba aleatoria con los contenedores especificados
	 */
	private void testWithContainers(List<DummyContainable> containers) {
		// Contenedores ausentes (Inicialmenteno hay ningún contenedor en los mapas)
		final List<DummyContainable> containersAbsent = new ArrayList<DummyContainable>(containers);
		
		// Crear los mapas basados en estrategia de contenibles a probar
		List< ContainableStrategyMap<DummyContainable, Object> > testMaps = new ArrayList< ContainableStrategyMap<DummyContainable, Object> >(ContainableStrategyMapTests.containableStrategyMapFactories.length);
		for ( TestContainableStrategyMapFactory eachFactory : ContainableStrategyMapTests.containableStrategyMapFactories ) {
			testMaps.add(eachFactory.create());
		}
		
		// Lista de contenedores presentes
		List<DummyContainable> containersPresent = new ArrayList<DummyContainable>();
		
		int frequencyAddRemoveState = 0;
		int addRemoveStateDelta = 1;
		
		final List<DummyContainable> notTestedPresentKeys = new ArrayList<DummyContainable>(containers);
		final List<DummyContainable> notTestedAbsentKeys = new ArrayList<DummyContainable>(containers);
		
		for ( int i = 0; i < addRemoveCycles ; i++ ) {
			
			// Operación de agregado/quitado
			{
				// ¿Efectuar operación efectiva?
				boolean effectiveOperation;
			
				boolean remainingStateChange;
				
				// Si está en el extremo
				if ( Math.abs( frequencyAddRemoveState ) == operationsInAddRemoveStateTransition ) {
					// Si está en el lado de agregado
					if ( frequencyAddRemoveState > 0 ) {
						remainingStateChange = !containersAbsent.isEmpty(); // Si faltan agregar elementos
					}
					else { // Si está en el lado de quitado
						remainingStateChange = !containersPresent.isEmpty(); // Si falta quitar elementos
					}
					
					// Si ya no permanece
					if ( !remainingStateChange ) {
						// Cambiar el sentido
						addRemoveStateDelta = -addRemoveStateDelta;
					}
				}
				else {
					remainingStateChange = false;
				}
				
				if ( !remainingStateChange ) {
					frequencyAddRemoveState += addRemoveStateDelta;
				}
				
				effectiveOperation = remainingStateChange || this.random.nextBoolean();
				
				// Si hay que agregar
				if ( this.random.nextInt(operationsInAddRemoveStateTransition*2) < frequencyAddRemoveState + operationsInAddRemoveStateTransition) {
					final DummyContainable keyToBeAdded;
					
					// Si no hay elementos agregados
					if ( containersPresent.isEmpty() ) {
						// Tiene que ser efectiva
						effectiveOperation = true;
					}
					else if ( containersAbsent.isEmpty() ) { // Si están todos
						// No puede ser efectiva
						effectiveOperation = false;
					}
					
					// Elegir en base si es o no es operación efectiva
					if ( effectiveOperation ) {
						// Elegir un elemento ausente
						keyToBeAdded = containersAbsent.remove( this.random.nextInt(containersAbsent.size()) );
						containersPresent.add(keyToBeAdded);
					}
					else {
						// Elegir un elemento presente
						keyToBeAdded = containersPresent.get( this.random.nextInt(containersPresent.size()) );
					}
					
					// Agregar la asociación de la clave elegida
					Object expectedValue;
					if ( effectiveOperation ) {
						expectedValue = null;
					}
					else {
						expectedValue = keyToBeAdded.getExpectedValue();
					}
					
					// Si hay que cambiar la clave
					if ( this.random.nextFloat() < changeProbabilityInAdd ) {
						keyToBeAdded.setExpectedValue(new Object());
					}
					
					for ( ContainableStrategyMap<DummyContainable, Object> testMap : testMaps ) {						
						Assert.assertEquals( expectedValue, testMap.getMap().put(keyToBeAdded, keyToBeAdded.getExpectedValue() ) );
					}
				}
				else { // Caso contrario, hay que quitar
					final DummyContainable keyToBeRemoved;
					
					// Si no hay claves
					if ( containersPresent.isEmpty() ) {
						// No puede ser efectiva
						effectiveOperation = false;
					}
					else if ( containersAbsent.isEmpty() ) { // Si están todos
						// No puede ser no efectiva
						effectiveOperation = true;
					}
					
					final Object expectedReturnValue;
					
					// Elegir en base si es o no es una operación efectiva
					if ( effectiveOperation ) {
						// Elegir un no presente
						keyToBeRemoved = containersPresent.remove( this.random.nextInt(containersPresent.size()) );
						containersAbsent.add(keyToBeRemoved);
						expectedReturnValue = keyToBeRemoved.getExpectedValue();
					}
					else {
						// Elegir un elemento no presente
						keyToBeRemoved = containersAbsent.get( this.random.nextInt(containersAbsent.size()) );
						expectedReturnValue = null;
					}
					
					// Quitar la asociación de la clave elegida
					for ( ContainableStrategyMap<DummyContainable, Object> testMap : testMaps ) {
						Assert.assertEquals(expectedReturnValue, testMap.getMap().remove(keyToBeRemoved) );
					}
				}
			}
			
			// Probar todas las claves
			notTestedPresentKeys.addAll(containersPresent);
			notTestedAbsentKeys.addAll(containersAbsent);
			
			Collections.shuffle(notTestedPresentKeys, this.random);
			Collections.shuffle(notTestedAbsentKeys, this.random);
			
			for ( int remainingKeys=containers.size() ; remainingKeys >=1 ; remainingKeys-- ) {
				DummyContainable selectedKey;
				
				{
					final List<DummyContainable> selectedList;
				
					// Elegir la lista que corresponde
					if ( !notTestedPresentKeys.isEmpty() && (this.random.nextInt(remainingKeys) < notTestedPresentKeys.size()) ) {
						selectedList = notTestedPresentKeys;
					}
					else {
						selectedList = notTestedAbsentKeys;
					}
					
					selectedKey = selectedList.remove(selectedList.size()-1);
				}
				
				InmutableReference< Entry<DummyContainable, Object> > firstEntryReference = null;
				boolean firstMap = true;
				
				for ( ContainableStrategyMap<DummyContainable, Object> testMap : testMaps ) {
					InmutableReference< Entry<DummyContainable, Object> > eachEntryReference = testMap.getEntry(selectedKey);
					
					if ( firstMap ) {
						firstEntryReference = eachEntryReference;
					}
					else {
						if ( firstEntryReference != null ) {
							Assert.assertNotNull(eachEntryReference);
							if ( firstEntryReference.get() != null ) {
								Assert.assertNotNull(eachEntryReference.get());
								Assert.assertEquals(firstEntryReference.get().getKey(), eachEntryReference.get().getKey());
								Assert.assertEquals(firstEntryReference.get().getValue(), eachEntryReference.get().getValue());
							}
							else {
								Assert.assertNull(eachEntryReference.get());
							}
						}
						else {
							Assert.assertNull(eachEntryReference);
						}
					}
					
					firstMap = false;
				}
			}
			
		}
	}
	
	/**
	 * @post Realiza una prueba aleatoria con contenedores que describen un árbol
	 */
	@Test
	public void graphTest() {		
		// Contenedores ausentes (Inicialmenteno hay ningún contenedor en los mapas)
		final List<DummyContainable> containers = new ArrayList<DummyContainable>(containersQuantity);
		
		// Crea contenedores en forma aleatoria
		for ( int i=0; i < containersQuantity; i++ ) {
			final DummyContainable newDummyContainable = new DummyContainable("Containable " + i); // Contenedor
			
			// Especifica un valor arbitrario
			newDummyContainable.setExpectedValue(new Object());
			
			// Si el conjunto de contenedores no está vacío y tiene que ser contenedor raiz por elección aleatoria
			if ( !containers.isEmpty() && ( this.random.nextFloat() <= rootContainerProbability ) ) {
				// Agregar como contenedor uno de los contenedores ya creados
				newDummyContainable.containers().add( containers.get(this.random.nextInt(containers.size())) );
				
				// Si tiene que ser ciclo
				if ( this.random.nextFloat() < this.cyclesProbability ) {
					// Elegir un contenedor ya existente y agregarlo como contenedor
					containers.get(this.random.nextInt(containers.size())).containers().add(newDummyContainable);
				}
			}
			
			// Agregar el contenedor
			containers.add(newDummyContainable);
		}
		
		this.testWithContainers(containers);
	}
}
