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
package com.esferixis.misc.octree;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.esferixis.misc.octree.ModifiableOctree;
import com.esferixis.misc.octree.SimpleHeapOctree;
import com.esferixis.misc.octree.ModifiableOctree.Node;
import com.esferixis.misc.octree.Octree.NodeUbication;

/**
 * Pruebas de unidad para octrees
 */

@RunWith(value = Parameterized.class)
public class OctreeTest {
	private final ModifiableOctree.Factory octreeFactory;
	private ModifiableOctree<Object> octree;
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] octrees = new Object[][]{ { new SimpleHeapOctree.Factory() } };
		return Arrays.asList( octrees );
	}
	
	public OctreeTest(ModifiableOctree.Factory octreeFactory) {
		this.octreeFactory = octreeFactory;
	}
	
	@Before
	public void createOctree() {
		this.octree = this.octreeFactory.create();
	}
	
	@Test
	public void testEmptyNode() {
		Object rootNodeElement = new Object();
		ModifiableOctree<Object>.Node rootNode = this.octree.newNode(rootNodeElement);
		
		for ( NodeUbication eachUbication : NodeUbication.ubications() ) {
			Assert.assertNull( rootNode.get(eachUbication) );
			Assert.assertNull( rootNode.getNeighboorNode(eachUbication) );
		}
		
		Assert.assertNull( rootNode.getParent() );
		
		Assert.assertEquals(rootNodeElement, rootNode.getElement() );
	}
}
