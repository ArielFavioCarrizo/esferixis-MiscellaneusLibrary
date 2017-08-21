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
package com.esferixis.misc.infinitesimalSortedSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;

import junit.framework.Assert;

import org.junit.Test;

import com.esferixis.misc.collection.list.BinaryList;
import com.esferixis.misc.infinitesimalSortedSet.InfinitesimalSortedSet;
import com.esferixis.misc.infinitesimalSortedSet.Interval;
import com.esferixis.misc.infinitesimalSortedSet.SetPrimitive;

/**
 * Pruebas de JUnit del conjunto de elementos ordenado infinitesimal
 * 
 * @author ariel
 *
 */
public class InfinitesimalSortedSetTest {
	
	/**
	 * @post Test con conjunto vacío
	 */
	@Test
	public void emptySet() {
		InfinitesimalSortedSet<Float> emptySet = new InfinitesimalSortedSet<Float>();
		Assert.assertTrue( emptySet.isEmpty() );
	}
	
	/**
	 * @post Test con un punto
	 */
	@Test
	public void pointSet() {
		Float element = new Float(10.0f);
		InfinitesimalSortedSet<Float> pointSet = new InfinitesimalSortedSet<Float>(element);
		Assert.assertEquals(Collections.singleton(new SetPrimitive<Float>(new Float(element))), pointSet.setPrimitives());
	}
	
	/**
	 * @post Test con un intervalo que encierra un punto
	 */
	@Test
	public void intervalPointSet() {
		float element = 10.0f;
		InfinitesimalSortedSet<Float> pointSet1 = new InfinitesimalSortedSet<Float>(new Interval<Float>(new SetPrimitive<Float>(new Float(element), true, 1), new SetPrimitive<Float>(new Float(element), true, -1) ));
		InfinitesimalSortedSet<Float> pointSet2 = new InfinitesimalSortedSet<Float>(new Float(element));
		Assert.assertEquals(Collections.singleton(new SetPrimitive<Float>(element)), pointSet1.setPrimitives());
		Assert.assertEquals(pointSet2, pointSet1);
	}
	
	/**
	 * @post Test con un intervalo con infinitos puntos
	 */
	@Test
	public void infinitesimalIntervalSet() {
		float minValue = 0.0f;
		float maxValue = 10.0f;
		
		List<Boolean> booleanValues = new BinaryList<Boolean>(false, true);
		for ( boolean closedPrimitive1 : booleanValues ) {
			for ( boolean closedPrimitive2 : booleanValues ) {
				final SetPrimitive<Float> eachMinPrimitive = new SetPrimitive<Float>(minValue, closedPrimitive1, 1);
				final SetPrimitive<Float> eachMaxPrimitive = new SetPrimitive<Float>(maxValue, closedPrimitive2, -1);
				InfinitesimalSortedSet<Float> intervalSet = new InfinitesimalSortedSet<Float>( new Interval<Float>(eachMinPrimitive, eachMaxPrimitive) );
				Assert.assertEquals(new BinaryList< SetPrimitive<Float> >(eachMinPrimitive, eachMaxPrimitive), new ArrayList< SetPrimitive<Float> >(intervalSet.setPrimitives()) );
			}
		}
	}
	
	/**
	 * @post Test de unión entre dos intervalos que no se intersecan
	 */
	@Test
	public void twoNonIntersectingIntervalsSetsUnion() {
		InfinitesimalSortedSet<Float> set1 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(0.0f, true, 1), new SetPrimitive<Float>(10.0f, false, -1)) );
		InfinitesimalSortedSet<Float> set2 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(20.0f, false, 1), new SetPrimitive<Float>(30.0f, true, -1)) );
		
		List< SetPrimitive<Float> > expectedSetPrimitives = new ArrayList< SetPrimitive<Float> >(4);
		expectedSetPrimitives.add( new SetPrimitive<Float>(0.0f, true, 1) );
		expectedSetPrimitives.add( new SetPrimitive<Float>(10.0f, false, -1) );
		expectedSetPrimitives.add( new SetPrimitive<Float>(20.0f, false, 1) );
		expectedSetPrimitives.add( new SetPrimitive<Float>(30.0f, true, -1) );
		Assert.assertEquals(expectedSetPrimitives, new ArrayList< SetPrimitive<Float> >(set1.union(set2).setPrimitives() ) );
	}
	
	/**
	 * @post Test de unión entre dos intervalos cuya intersección es un intervalo
	 */
	@Test
	public void twoIntervalIntersectingIntervalSetsUnion() {
		InfinitesimalSortedSet<Float> set1 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(0.0f, true, 1), new SetPrimitive<Float>(15.0f, false, -1)) );
		InfinitesimalSortedSet<Float> set2 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(5.0f, false, 1), new SetPrimitive<Float>(20.0f, true, -1)) );
		
		List< SetPrimitive<Float> > expectedSetPrimitives = new ArrayList< SetPrimitive<Float> >(4);
		expectedSetPrimitives.add( new SetPrimitive<Float>(0.0f, true, 1) );
		expectedSetPrimitives.add( new SetPrimitive<Float>(20.0f, true, -1) );
		Assert.assertEquals(expectedSetPrimitives, new ArrayList< SetPrimitive<Float> >(set1.union(set2).setPrimitives() ) );
	}
	
	/**
	 * @post Test de unión entre dos intervalos que tienen de intersección un punto
	 */
	@Test
	public void twoPointIntersectingIntervalSetsUnion() {
		InfinitesimalSortedSet<Float> set1 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(0.0f, true, 1), new SetPrimitive<Float>(10.0f, true, -1)) );
		InfinitesimalSortedSet<Float> set2 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(10.0f, true, 1), new SetPrimitive<Float>(20.0f, true, -1)) );
		
		List< SetPrimitive<Float> > expectedSetPrimitives = new ArrayList< SetPrimitive<Float> >(4);
		expectedSetPrimitives.add( new SetPrimitive<Float>(0.0f, true, 1) );
		expectedSetPrimitives.add( new SetPrimitive<Float>(20.0f, true, -1) );
		Assert.assertEquals(expectedSetPrimitives, new ArrayList< SetPrimitive<Float> >(set1.union(set2).setPrimitives() ) );
	}
	
	/**
	 * @post Test de unión entre dos intervalos contiguos
	 */
	@Test
	public void twoContiguousIntersectingIntervalSetsUnion() {
		InfinitesimalSortedSet<Float> set1 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(0.0f, true, 1), new SetPrimitive<Float>(10.0f, false, -1)) );
		InfinitesimalSortedSet<Float> set2 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(10.0f, true, 1), new SetPrimitive<Float>(20.0f, true, -1)) );
		
		List< SetPrimitive<Float> > expectedSetPrimitives = new ArrayList< SetPrimitive<Float> >(4);
		expectedSetPrimitives.add( new SetPrimitive<Float>(0.0f, true, 1) );
		expectedSetPrimitives.add( new SetPrimitive<Float>(20.0f, true, -1) );
		Assert.assertEquals(expectedSetPrimitives, new ArrayList< SetPrimitive<Float> >(set1.union(set2).setPrimitives() ) );
	}
	
	/**
	 * @post Test de intersección entre dos intervalos que no se intersecan
	 */
	@Test
	public void twoNonIntersectingIntervalsSetsIntersection() {
		InfinitesimalSortedSet<Float> set1 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(0.0f, true, 1), new SetPrimitive<Float>(10.0f, false, -1)) );
		InfinitesimalSortedSet<Float> set2 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(20.0f, false, 1), new SetPrimitive<Float>(30.0f, true, -1)) );
		
		Assert.assertTrue( set1.intersection(set2).isEmpty() );
	}
	
	/**
	 * @post Test de intersección entre dos intervalos cuya intersección es un intervalo
	 */
	@Test
	public void twoIntervalIntersectingIntervalSetsIntersection() {
		InfinitesimalSortedSet<Float> set1 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(0.0f, true, 1), new SetPrimitive<Float>(15.0f, false, -1)) );
		InfinitesimalSortedSet<Float> set2 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(5.0f, false, 1), new SetPrimitive<Float>(20.0f, true, -1)) );
		
		List< SetPrimitive<Float> > expectedSetPrimitives = new ArrayList< SetPrimitive<Float> >(4);
		expectedSetPrimitives.add( new SetPrimitive<Float>(5.0f, false, 1) );
		expectedSetPrimitives.add( new SetPrimitive<Float>(15.0f, false, -1) );
		Assert.assertEquals(expectedSetPrimitives, new ArrayList< SetPrimitive<Float> >(set1.intersection(set2).setPrimitives() ) );
	}
	
	/**
	 * @post Test de intersección entre dos intervalos que tienen de intersección un punto
	 */
	@Test
	public void twoPointIntersectingIntervalSetsIntersection() {
		InfinitesimalSortedSet<Float> set1 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(0.0f, true, 1), new SetPrimitive<Float>(10.0f, true, -1)) );
		InfinitesimalSortedSet<Float> set2 = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(10.0f, true, 1), new SetPrimitive<Float>(20.0f, true, -1)) );
		
		Assert.assertEquals(Collections.singletonList(new SetPrimitive<Float>(10.0f)), new ArrayList< SetPrimitive<Float> >(set1.intersection(set2).setPrimitives() ) );
	}
	
	/**
	 * @post Test de unión entre el dominio completo y un intervalo
	 */
	@Test
	public void testDomainSetWithIntervalSetUnion() {
		InfinitesimalSortedSet<Float> domainSet = new InfinitesimalSortedSet<Float>( new Interval<Float>(new SetPrimitive<Float>(null, false, 1), new SetPrimitive<Float>(null, false, -1)) );
		InfinitesimalSortedSet<Float> intervalSet = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(0.0f, true, 1), new SetPrimitive<Float>(20.0f, true, -1)) );
		
		Assert.assertEquals(domainSet, domainSet.union(intervalSet) );
	}
	
	/**
	 * @post Test de intersección entre el dominio completo y un intervalo
	 */
	@Test
	public void testDomainSetWithIntervalSetIntersection() {
		InfinitesimalSortedSet<Float> domainSet = new InfinitesimalSortedSet<Float>( new Interval<Float>(new SetPrimitive<Float>(null, false, 1), new SetPrimitive<Float>(null, false, -1)) );
		InfinitesimalSortedSet<Float> intervalSet = new InfinitesimalSortedSet<Float>( new Interval<Float>( new SetPrimitive<Float>(0.0f, true, 1), new SetPrimitive<Float>(20.0f, true, -1)) );
		
		Assert.assertEquals(intervalSet, domainSet.intersection(intervalSet) );
	}
	
	/**
	 * @post Test de unión entre 3 conjuntos disconexos
	 */
	@Test
	public void testThreeDisconnectedSetsUnion() {
		InfinitesimalSortedSet<Float> set1 = new InfinitesimalSortedSet<Float>( new Interval<Float>(new SetPrimitive<Float>(0.0f, false, 1), new SetPrimitive<Float>(10.0f, true, -1) ) );
		InfinitesimalSortedSet<Float> set2 = new InfinitesimalSortedSet<Float>( new Interval<Float>(new SetPrimitive<Float>(20.0f, true, 1), new SetPrimitive<Float>(30.0f, false, -1) ) );
		InfinitesimalSortedSet<Float> set3 = new InfinitesimalSortedSet<Float>( new Interval<Float>(new SetPrimitive<Float>(30.0f, false, 1), new SetPrimitive<Float>(50.0f, false, -1) ) );
		List< SetPrimitive<Float> > expectedSetPrimitives = new ArrayList< SetPrimitive<Float> >();
		expectedSetPrimitives.addAll(set1.setPrimitives());
		expectedSetPrimitives.addAll(set2.setPrimitives());
		expectedSetPrimitives.addAll(set3.setPrimitives());
		Assert.assertEquals(expectedSetPrimitives, new ArrayList< SetPrimitive<Float> >(set2.union(set1).union(set3).setPrimitives()));
	}
	
	/**
	 * @post Test de unión entre 100 conjuntos disconexos
	 */
	@Test
	public void testOneHundredDisconnectedSetsUnion() {
		final Random rndGenerator = new Random(423462);
		
		List< InfinitesimalSortedSet<Float> > disconnectedSets = new ArrayList< InfinitesimalSortedSet<Float> >();
		List< SetPrimitive<Float> > expectedSetPrimitives = new ArrayList< SetPrimitive<Float> >();
		
		float insertionLimit = -20.0f;
		final float maxAddRange = 50.0f;
		final float minAddRange = 0.00001f;
		
		SetPrimitive<Float> lastSetPrimitive=null;
		for ( int i = 0 ; i < 100 ; i++ ) {
			final SetPrimitive<Float> minSetPrimitive;
			final SetPrimitive<Float> maxSetPrimitive;
			
			boolean insertionLimitAdd = true;
			if ( lastSetPrimitive != null ) {
				if ( !lastSetPrimitive.isClosed() ) {
					insertionLimitAdd = rndGenerator.nextBoolean();
				}
			}
			
			if ( insertionLimitAdd ) {
				insertionLimit += (float) rndGenerator.nextDouble() * (maxAddRange - minAddRange) + minAddRange;
				minSetPrimitive = new SetPrimitive<Float>(insertionLimit, rndGenerator.nextBoolean(), 1);
			}
			else {
				minSetPrimitive = new SetPrimitive<Float>(insertionLimit, false, 1);
			}
			
			float randomAdd = (float) rndGenerator.nextDouble() * (maxAddRange - minAddRange) + minAddRange;
			if ( minSetPrimitive.isClosed() ) {
				if ( rndGenerator.nextBoolean() ) {
					insertionLimit += randomAdd;
				}
				maxSetPrimitive = new SetPrimitive<Float>(insertionLimit, true, -1);
			}
			else {
				insertionLimit += randomAdd;
				maxSetPrimitive = new SetPrimitive<Float>(insertionLimit, rndGenerator.nextBoolean(), -1);
			}
			
			InfinitesimalSortedSet<Float> lastSet = new InfinitesimalSortedSet<Float>( new Interval<Float>(minSetPrimitive, maxSetPrimitive) );
			disconnectedSets.add( lastSet );
			
			lastSetPrimitive = maxSetPrimitive;
			
			expectedSetPrimitives.addAll( lastSet.setPrimitives() );
		}
		
		List< InfinitesimalSortedSet<Float> > disconnectedSetsToBeJoined = new LinkedList< InfinitesimalSortedSet<Float> >(disconnectedSets);
		
		while ( disconnectedSetsToBeJoined.size() != 1 ) {
			InfinitesimalSortedSet<Float> removedSetToBeJoined1 = disconnectedSetsToBeJoined.remove(rndGenerator.nextInt(disconnectedSetsToBeJoined.size()));
			InfinitesimalSortedSet<Float> removedSetToBeJoined2 = disconnectedSetsToBeJoined.remove(rndGenerator.nextInt(disconnectedSetsToBeJoined.size()));
			disconnectedSetsToBeJoined.add(removedSetToBeJoined1.union(removedSetToBeJoined2) );
		}
		
		SortedSet< SetPrimitive<Float> > resultSetPrimitives = disconnectedSetsToBeJoined.get(0).setPrimitives();
		
		Assert.assertEquals(expectedSetPrimitives, new ArrayList< SetPrimitive<Float> >(resultSetPrimitives) );
	}
	
	/**
	 * @post Test de unión entre 100 conjuntos conexos
	 */
	@Test
	public void testOneHundredConnectedSetsUnion() {
		final Random rndGenerator = new Random(423462);
		
		List< InfinitesimalSortedSet<Float> > connectedSets = new ArrayList< InfinitesimalSortedSet<Float> >();
		
		SetPrimitive<Float> minSetPrimitive=null;
		SetPrimitive<Float> maxSetPrimitive=null;
		
		float insertionLimit = -20.0f;
		final float range = 50.0f;
		
		SetPrimitive<Float> lastSetPrimitive=null;
		for ( int i = 0 ; i < 100 ; i++ ) {
			final SetPrimitive<Float> minIntervalSetPrimitive;
			final SetPrimitive<Float> maxIntervalSetPrimitive;
			
			if ( rndGenerator.nextBoolean() || ( lastSetPrimitive == null ) ) {
				minIntervalSetPrimitive = new SetPrimitive<Float>(insertionLimit - rndGenerator.nextFloat() * range, rndGenerator.nextBoolean(), 1);
			}
			else {
				minIntervalSetPrimitive = new SetPrimitive<Float>(insertionLimit, !lastSetPrimitive.isClosed(), 1);
			}
			
			float randomAdd = rndGenerator.nextFloat() * range;
			if ( minIntervalSetPrimitive.isClosed() ) {
				if ( rndGenerator.nextBoolean() ) {
					insertionLimit += randomAdd;
				}
				maxIntervalSetPrimitive = new SetPrimitive<Float>(insertionLimit, true, -1);
			}
			else {
				insertionLimit += randomAdd;
				maxIntervalSetPrimitive = new SetPrimitive<Float>(insertionLimit, rndGenerator.nextBoolean(), -1);
			}
			
			InfinitesimalSortedSet<Float> lastSet = new InfinitesimalSortedSet<Float>( new Interval<Float>(minIntervalSetPrimitive, maxIntervalSetPrimitive) );
			connectedSets.add( lastSet );
			
			boolean isMin;
			if ( minSetPrimitive != null ) {
				isMin = ( minIntervalSetPrimitive.compareTo(minSetPrimitive) < 0 );
			}
			else {
				isMin = true;
			}
			
			if ( isMin ) {
				minSetPrimitive = minIntervalSetPrimitive;
			}
			
			boolean isMax;
			if ( maxSetPrimitive != null ) {
				isMax = ( maxIntervalSetPrimitive.compareTo(maxSetPrimitive) > 0 );
			}
			else {
				isMax = true;
			}
			
			if ( isMax ) {
				maxSetPrimitive = maxIntervalSetPrimitive;
			}
			
			lastSetPrimitive = minIntervalSetPrimitive;
		}
		
		List< InfinitesimalSortedSet<Float> > connectedSetsToBeJoined = new LinkedList< InfinitesimalSortedSet<Float> >(connectedSets);
		
		while ( connectedSetsToBeJoined.size() != 1 ) {
			InfinitesimalSortedSet<Float> removedSetToBeJoined1 = connectedSetsToBeJoined.remove(rndGenerator.nextInt(connectedSetsToBeJoined.size()));
			InfinitesimalSortedSet<Float> removedSetToBeJoined2 = connectedSetsToBeJoined.remove(rndGenerator.nextInt(connectedSetsToBeJoined.size()));
			connectedSetsToBeJoined.add(removedSetToBeJoined1.union(removedSetToBeJoined2) );
		}
		
		SortedSet< SetPrimitive<Float> > resultSetPrimitives = connectedSetsToBeJoined.get(0).setPrimitives();
		
		List<SetPrimitive<Float>> expectedSetPrimitives;
		if ( minSetPrimitive.compareTo(maxSetPrimitive ) == 0 ) {
			Float commonElement = minSetPrimitive.getReferenceElement();
			expectedSetPrimitives = Collections.singletonList( new SetPrimitive<Float>(commonElement) );
		}
		else {
			expectedSetPrimitives = new BinaryList< SetPrimitive<Float> >(minSetPrimitive, maxSetPrimitive);
		}
		
		Assert.assertEquals(expectedSetPrimitives, new ArrayList< SetPrimitive<Float> >(resultSetPrimitives) );
	}
	
	/**
	 * @post Test de intersección entre 100 conjuntos disconexos
	 */
	@Test
	public void testOneHundredDisconnectedSetsIntersection() {
		final Random rndGenerator = new Random(423462);
		
		List< InfinitesimalSortedSet<Float> > disconnectedSets = new ArrayList< InfinitesimalSortedSet<Float> >();
		
		float insertionLimit = -20.0f;
		final float maxAddRange = 50.0f;
		final float minAddRange = 0.00001f;
		
		SetPrimitive<Float> lastSetPrimitive=null;
		for ( int i = 0 ; i < 100 ; i++ ) {
			final SetPrimitive<Float> minSetPrimitive;
			final SetPrimitive<Float> maxSetPrimitive;
			
			boolean insertionLimitAdd = true;
			if ( lastSetPrimitive != null ) {
				if ( !lastSetPrimitive.isClosed() ) {
					insertionLimitAdd = rndGenerator.nextBoolean();
				}
			}
			
			if ( insertionLimitAdd ) {
				insertionLimit += (float) rndGenerator.nextDouble() * (maxAddRange - minAddRange) + minAddRange;
				minSetPrimitive = new SetPrimitive<Float>(insertionLimit, rndGenerator.nextBoolean(), 1);
			}
			else {
				minSetPrimitive = new SetPrimitive<Float>(insertionLimit, false, 1);
			}
			
			float randomAdd = (float) rndGenerator.nextDouble() * (maxAddRange - minAddRange) + minAddRange;
			if ( minSetPrimitive.isClosed() ) {
				if ( rndGenerator.nextBoolean() ) {
					insertionLimit += randomAdd;
				}
				maxSetPrimitive = new SetPrimitive<Float>(insertionLimit, true, -1);
			}
			else {
				insertionLimit += randomAdd;
				maxSetPrimitive = new SetPrimitive<Float>(insertionLimit, rndGenerator.nextBoolean(), -1);
			}
			
			InfinitesimalSortedSet<Float> lastSet = new InfinitesimalSortedSet<Float>( new Interval<Float>(minSetPrimitive, maxSetPrimitive) );
			disconnectedSets.add( lastSet );
			
			lastSetPrimitive = maxSetPrimitive;
		}
		
		for ( InfinitesimalSortedSet<Float> eachSet1 : disconnectedSets ) {
			for ( InfinitesimalSortedSet<Float> eachSet2 : disconnectedSets ) {
				if ( eachSet2 != eachSet1 ) {
					Assert.assertTrue(eachSet1.intersection(eachSet2).isEmpty());
				}
			}
		}
	}
}
