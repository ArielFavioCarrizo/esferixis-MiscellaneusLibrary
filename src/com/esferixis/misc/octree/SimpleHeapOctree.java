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

import java.lang.reflect.Array;

/**
 * Octree almacenado en heap
 */
public final class SimpleHeapOctree<T> extends ModifiableOctree<T> {
	// Fábrica
	public static final class Factory extends ModifiableOctree.Factory {

		@Override
		public <T> ModifiableOctree<T> create() {
			return new SimpleHeapOctree<T>();
		}
		
	}
	
	private static int getIndex(boolean value) {
		return ( value ? 0 : 1 );
	}
	
	public final class Node extends ModifiableOctree<T>.Node {
		private final Node[][][] nodes;
		private Node parent;
		
		private T element;
		
		private Node(T element) {
			this.nodes = (Node[][][]) Array.newInstance(Node[][].class, 2);
			for ( int i = 0 ; i < 2 ; i++ ) {
				this.nodes[i] = (Node[][]) Array.newInstance(Node[].class, 2);
				for ( int j = 0 ; j < 2 ; j++ ) {
					this.nodes[i][j] = (Node[]) Array.newInstance(Node.class, 2);
					for ( int k = 0 ; k < 2 ; k++ ) {
						this.nodes[i][j][k] = null;
					}
				}
			}
			
			this.element = element;
		}
		
		@Override
		public void set(com.esferixis.misc.octree.ModifiableOctree.NodeUbication ubication,
				ModifiableOctree<T>.Node node) {
			Node newNode = (Node) node;
			
			Node oldNode = (Node) this.get(ubication);
			
			if ( newNode != oldNode ) {
			
				if ( oldNode != null ) {
					if ( oldNode.getParent() != null ) {
						throw new IllegalStateException("Attemped to set an node with a parent");
					}
				}
			
				this.nodes[getIndex(ubication.isRight())][getIndex(ubication.isUp())][getIndex(ubication.isBack())] = newNode;
			
				if ( newNode != null ) {
					newNode.parent = this;
				}
			}
		}

		@Override
		public ModifiableOctree<T>.Node get(com.esferixis.misc.octree.ModifiableOctree.NodeUbication ubication) {
			return this.nodes[getIndex(ubication.isRight())][getIndex(ubication.isUp())][getIndex(ubication.isBack())];
		}

		@Override
		public Node getParent() {
			return this.parent;
		}

		@Override
		public T getElement() {
			return this.element;
		}

		@Override
		public void setElement(T element) {
			this.element = element;
		}
		
	}

	@Override
	public ModifiableOctree<T>.Node newNode(T element) {
		return new Node(element);
	}
}
