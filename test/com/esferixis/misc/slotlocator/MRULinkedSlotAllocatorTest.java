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
package com.esferixis.misc.slotlocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.esferixis.misc.slotlocator.LinkedAllocatableElement;
import com.esferixis.misc.slotlocator.MRULinkedSlotAllocator;
import com.esferixis.misc.slotlocator.Slot;

public class MRULinkedSlotAllocatorTest {
	private class TestSlot extends Slot {
		private final String title;
		
		public TestSlot(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return "'" + this.title + "'";
		}
	}
	
	private class TestElement extends LinkedAllocatableElement<TestSlot> {
		private final String title;
		public TestElement(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return "'" + this.title + "'";
		}

		/* (non-Javadoc)
		 * @see com.esferixis.misc.slotlocator.LinkedAllocatableElement#notifyNewSlot(com.esferixis.misc.slotlocator.Slot)
		 */
		@Override
		protected void notifyNewSlot(TestSlot newSlot) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private MRULinkedSlotAllocator<TestSlot> slotAllocator;
	
	private TestSlot testedGetSlot(TestElement element) {
		TestSlot elementSlot = slotAllocator.get(element);
		Assert.assertNotNull(elementSlot);
		Assert.assertEquals(elementSlot, element.getSlot());
		return elementSlot;
	}
	
	@Test
	public void testNotAllocated() {
		TestElement element = new TestElement("One element");
		Assert.assertNull( element.getSlot() );
	}
	
	@Test
	public void testAllocateOne() {
		List<TestSlot> slots = new ArrayList<TestSlot>();
		for ( int i = 0 ; i < 5 ; i++ ) {
			slots.add(new TestSlot("Slot " + i));
		}
		
		MRULinkedSlotAllocator<TestSlot> slotAllocator = new MRULinkedSlotAllocator<TestSlot>(slots);
		
		TestElement element = new TestElement("One element");
		Assert.assertNull( element.getSlot() );
		
		TestSlot elementSlot = slotAllocator.get(element);
		Assert.assertNotNull(elementSlot);
		Assert.assertEquals(elementSlot, element.getSlot());
	}
	
	@Test
	public void testAllocateAll() {
		List<TestSlot> slots = new ArrayList<TestSlot>();
		for ( int i = 0 ; i < 5 ; i++ ) {
			slots.add(new TestSlot("Slot " + i));
		}
		
		this.slotAllocator = new MRULinkedSlotAllocator<TestSlot>(slots);
		
		Set<TestSlot> allocatedSlots = new HashSet<TestSlot>();
		
		for ( int i = 0 ; i < 5 ; i++ ) {
			TestElement eachElement = new TestElement("Element " + i);
			
			TestSlot elementSlot = this.testedGetSlot(eachElement);
			
			Assert.assertTrue(allocatedSlots.add(elementSlot));
		}
	}
	
	@Test
	public void testAllocateWithOverflow1() {
		List<TestSlot> slots = new ArrayList<TestSlot>();
		for ( int i = 0 ; i < 5 ; i++ ) {
			slots.add(new TestSlot("Slot " + i));
		}
		
		this.slotAllocator = new MRULinkedSlotAllocator<TestSlot>(slots);
		
		List<TestSlot> allocatedSlots = new ArrayList<TestSlot>();
		
		for ( int i = 0 ; i < 5 ; i++ ) {
			TestElement eachElement = new TestElement("Element " + i);
			
			TestSlot elementSlot = this.testedGetSlot(eachElement);
			
			Assert.assertFalse( allocatedSlots.contains(elementSlot) );
			allocatedSlots.add(elementSlot);
		}
		
		for ( int i = 0 ; i < 30 ; i++ ) {
			TestElement eachElement = new TestElement("Element " + i);
			
			TestSlot elementSlot = this.testedGetSlot(eachElement);
					
			Assert.assertEquals(allocatedSlots.get(i % allocatedSlots.size()), elementSlot);
		}
	}
	
	@Test
	public void testAllocateWithOverflow2() {
		TestSlot slot;
		
		List<TestSlot> slots = new ArrayList<TestSlot>();
		for ( int i = 0 ; i < 5 ; i++ ) {
			slots.add(new TestSlot("Slot " + i));
		}
		
		this.slotAllocator = new MRULinkedSlotAllocator<TestSlot>(slots);
		
		List<TestSlot> allocatedSlots = new ArrayList<TestSlot>();
		
		List<TestElement> elements = new ArrayList<TestElement>();
		
		for ( int i = 0 ; i < 8 ; i++ ) {
			elements.add( new TestElement("Element " + i) );
		}
		
		allocatedSlots.add( this.testedGetSlot( elements.get(0) ) );
		
		// E0->S0
		
		slot = this.testedGetSlot( elements.get(1) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		allocatedSlots.add(slot);
		
		/**
		 * E1->S1
		 * E0->S0
		 */
		
		slot = this.testedGetSlot( elements.get(2) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		allocatedSlots.add(slot);
		
		/**
		 * E2->S2
		 * E1->S1
		 * E0->S0
		 */
		
		Assert.assertEquals( allocatedSlots.get(0), this.testedGetSlot( elements.get(0) ) );
		
		/**
		 * E0->S0
		 * E2->S2
		 * E1->S1
		 */
		
		slot = this.testedGetSlot( elements.get(3) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		allocatedSlots.add(slot);
		
		/**
		 * E3->S3
		 * E0->S0
		 * E2->S2
		 * E1->S1
		 */
		
		Assert.assertEquals( allocatedSlots.get(2), this.testedGetSlot( elements.get(2) ) );
		
		/**
		 * E2->S2
		 * E3->S3
		 * E0->S0
		 * E1->S1
		 */
		
		slot = this.testedGetSlot( elements.get(4) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		allocatedSlots.add(slot);
		
		/**
		 * E4->S4
		 * E2->S2
		 * E3->S3
		 * E0->S0
		 * E1->S1
		 */
		
		slot = this.testedGetSlot( elements.get(5) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E5->S1
		 * E4->S4
		 * E2->S2
		 * E3->S3
		 * E0->S0
		 */
		
		slot = this.testedGetSlot( elements.get(2) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E2->S2
		 * E5->S1
		 * E4->S4
		 * E3->S3
		 * E0->S0
		 */
		
		slot = this.testedGetSlot( elements.get(6) );
		Assert.assertEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E6->S0
		 * E2->S2
		 * E5->S1
		 * E4->S4
		 * E3->S3
		 */
		
		slot = this.testedGetSlot( elements.get(4) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E4->S4
		 * E6->S0
		 * E2->S2
		 * E5->S1
		 * E3->S3
		 */
		
		slot = this.testedGetSlot( elements.get(0) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E0->S3
		 * E4->S4
		 * E6->S0
		 * E2->S2
		 * E5->S1
		 */
		
		slot = this.testedGetSlot( elements.get(7) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E7->S1
		 * E0->S3
		 * E4->S4
		 * E6->S0
		 * E2->S2
		 */
		
		slot = this.testedGetSlot( elements.get(6) );
		Assert.assertEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E6->S0
		 * E7->S1
		 * E0->S3
		 * E4->S4
		 * E2->S2
		 */
		
		slot = this.testedGetSlot( elements.get(7) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E7->S1
		 * E6->S0
		 * E0->S3
		 * E4->S4
		 * E2->S2
		 */
		
		slot = this.testedGetSlot( elements.get(3) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E3->S2
		 * E7->S1
		 * E6->S0
		 * E0->S3
		 * E4->S4
		 */
		
		slot = this.testedGetSlot( elements.get(4) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E4->S4
		 * E3->S2
		 * E7->S1
		 * E6->S0
		 * E0->S3
		 */
		
		slot = this.testedGetSlot( elements.get(2) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E2->S3
		 * E4->S4
		 * E3->S2
		 * E7->S1
		 * E6->S0
		 */
		
		slot = this.testedGetSlot( elements.get(6) );
		Assert.assertEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E6->S0
		 * E2->S3
		 * E4->S4
		 * E3->S2
		 * E7->S1
		 */
		
		slot = this.testedGetSlot( elements.get(0) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E0->S1
		 * E6->S0
		 * E2->S3
		 * E4->S4
		 * E3->S2
		 */
		
		slot = this.testedGetSlot( elements.get(6) );
		Assert.assertEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E6->S0
		 * E0->S1
		 * E2->S3
		 * E4->S4
		 * E3->S2
		 */
		
		slot = this.testedGetSlot( elements.get(2) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E2->S3
		 * E6->S0
		 * E0->S1
		 * E4->S4
		 * E3->S2
		 */
		
		slot = this.testedGetSlot( elements.get(4) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E4->S4
		 * E2->S3
		 * E6->S0
		 * E0->S1
		 * E3->S2
		 */
		
		slot = this.testedGetSlot( elements.get(3) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E3->S2
		 * E4->S4
		 * E2->S3
		 * E6->S0
		 * E0->S1
		 */
		
		slot = this.testedGetSlot( elements.get(1) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E1->S1
		 * E3->S2
		 * E4->S4
		 * E2->S3
		 * E6->S0
		 */
		
		slot = this.testedGetSlot( elements.get(5) );
		Assert.assertEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E5->S0
		 * E1->S1
		 * E3->S2
		 * E4->S4
		 * E2->S3
		 */
		
		slot = this.testedGetSlot( elements.get(7) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E7->S3
		 * E5->S0
		 * E1->S1
		 * E3->S2
		 * E4->S4
		 */
		
		slot = this.testedGetSlot( elements.get(6) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertNotEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E6->S4
		 * E7->S3
		 * E5->S0
		 * E1->S1
		 * E3->S2
		 */
		
		slot = this.testedGetSlot( elements.get(0) );
		Assert.assertNotEquals( allocatedSlots.get(0), slot );
		Assert.assertNotEquals( allocatedSlots.get(1), slot );
		Assert.assertEquals( allocatedSlots.get(2), slot );
		Assert.assertNotEquals( allocatedSlots.get(3), slot );
		Assert.assertNotEquals( allocatedSlots.get(4), slot );
		
		/**
		 * E0->S2
		 * E6->S4
		 * E7->S3
		 * E5->S0
		 * E1->S1
		 */
	}
}
