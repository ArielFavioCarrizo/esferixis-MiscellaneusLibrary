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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Octree
 */
public abstract class Octree<T> {
	public static class NodeUbication {
		private static final NodeUbication[] ubications = new NodeUbication[]{
				new NodeUbication(false, false, false),
				new NodeUbication(true, false, false),
				new NodeUbication(false, true, false),
				new NodeUbication(true, true, false),
				new NodeUbication(false, false, true),
				new NodeUbication(true, false, true),
				new NodeUbication(false, true, true),
				new NodeUbication(true, true, true)
		};
		private final boolean isRight, isUp, isBack;
		
		/**
		 * @post Crea una ubicación de nodo
		 */
		public NodeUbication(boolean isRight, boolean isUp, boolean isBack) {
			this.isRight = isRight;
			this.isUp = isUp;
			this.isBack = isBack;
		}
		
		/**
		 * @post Devuelve si está a la derecha
		 */
		public boolean isRight() {
			return this.isRight;
		}
		
		/**
		 * @post Devuelve si está arriba
		 */
		public boolean isUp() {
			return this.isUp;
		}
		
		/**
		 * @post Devuelve si está atrás
		 */
		public boolean isBack() {
			return this.isBack;
		}
		
		/**
		 * @post Devuelve la ubicación opuesta
		 */
		public NodeUbication opposite() {
			return new NodeUbication(!this.isRight, !this.isUp, !this.isBack);
		}
		
		/**
		 * @post Devuelve si es igual
		 */
		@Override
		public boolean equals(Object other) {
			if ( other != null ) {
				if ( other instanceof NodeUbication ) {
					NodeUbication otherUbication = (NodeUbication) other;
					return ( otherUbication.isRight == this.isRight ) && ( otherUbication.isUp == this.isUp ) && ( otherUbication.isBack );
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		
		/**
		 * @post Devuelve el hash
		 */
		@Override
		public int hashCode() {
			return new Boolean(this.isRight).hashCode() + 31 * new Boolean(this.isUp).hashCode() + 31 * 31 * new Boolean(this.isBack).hashCode();
		}
		
		/**
		 * @post Devuelve una lista de todas las ubicaciones posibles
		 */
		public static List<NodeUbication> ubications() {
			return Collections.unmodifiableList( Arrays.asList( ubications ) );
		}
	}
	
	public abstract class Node {
		/**
		 * @pre La ubicación no puede ser nula
		 * @post Devuelve el nodo dada la posición especificada
		 */
		public abstract Node get(NodeUbication ubication);
		
		/**
		 * @post Devuelve el nodo padre
		 */
		public abstract Node getParent();
		
		/**
		 * @pre Tiene que tener nodo padre
		 * @post Devuelve la ubicación relativa de éste nodo
		 * 		 en el nodo padre
		 * 
		 * 		 Ésta implementación recorre el nodo raíz
		 * 		 para buscar la ubicación
		 */
		public NodeUbication getUbication() {
			Node parent = this.getParent();
			if ( parent != null ) {
				Iterator<NodeUbication> ubicationsIterator = NodeUbication.ubications().iterator();
				NodeUbication foundedUbication = null;
				while ( ubicationsIterator.hasNext() && (foundedUbication == null) ) {
					NodeUbication eachUbication = ubicationsIterator.next();
					
					if ( this.get(eachUbication) == this ) {
						foundedUbication = eachUbication;
					}
				}
				
				return foundedUbication;
			}
			else {
				throw new IllegalStateException("It hasn't parent node");
			}
		}
		
		/**
		 * @pre La ubicación no puede ser nula
		 * @post Si existe un nodo vecino en la ubicación
		 * 		 especificada lo devuelve.
		 * 		 El nodo vecino es el que se encuentra en la posición
		 * 		 especificada con la máxima profundad posible no más profundo
		 * 		 que éste nodo
		 * 
		 * 		 Ésta implementación recorre recursivamente los nodos
		 */
		public Node getNeighboorNode(NodeUbication neighboorUbication) {
			NodeUbication thisRelativeUbication = neighboorUbication.opposite();
			
			// Busca el nodo padre que contenga recursivamente a éste nodo
			// y un nodo a la ubicación especificada
			final Node deepestCandidateParent;
			int relativeLevel = 0;
			
			Node neighboor = null;
			
			{
				Node eachNode = this;
				boolean endSearch = false;
			
				while ( !endSearch ) {
					if ( eachNode.getParent() != null ) {
						if ( eachNode.getUbication().equals(thisRelativeUbication) ) {
							endSearch = true;
						}
					}
					else {
						endSearch = true;
					}
					relativeLevel++;
					eachNode = this.getParent();
				}
				
				deepestCandidateParent = eachNode;
			}
			
			if ( deepestCandidateParent != null ) {
				// Ahora busca el nodo más profundo que pueda encontrar
				// con a lo sumo el mismo nivel en la misma ubicación
				Node neighboorCandidate = deepestCandidateParent.get(thisRelativeUbication);
				relativeLevel--;
				
				while ( neighboor == null ) {
					Node nextNode = neighboorCandidate.get(thisRelativeUbication);
					if ( ( relativeLevel == 0 ) || ( nextNode == null ) ) {
						neighboor = neighboorCandidate;
					}
					
					neighboorCandidate = nextNode;
					relativeLevel--;
				}
			}
			
			return neighboor;
		}

		/**
		 * @post Devuelve el elemento
		 */
		public abstract T getElement();
		
		/**
		 * @post Devuelve un iterador recursivo de todos los elementos
		 * 
		 * 		 Ésta implementación recorre recursivamente los nodos
		 */
		public Iterator<T> recursiveElementsIterator() {
			return new Iterator<T>(){
				private Stack<Node> nodesStack;
				private T nextElement;
				
				{
					this.nodesStack = new Stack<Node>();
					this.nodesStack.push(Node.this);
					this.nextElement = null;
				}
				
				private void obtainNextElement() {
					if ( this.nextElement == null ) {
						while ( !nodesStack.isEmpty() && (this.nextElement == null) ) {
							Node eachNode = nodesStack.pop();
							for ( NodeUbication eachUbication : NodeUbication.ubications ) {
								Node eachChildren = eachNode.get(eachUbication);
								if ( eachChildren != null ) {
									nodesStack.push( eachChildren );
								}
							}
							
							this.nextElement = eachNode.getElement();
						}
					}
				}
				
				@Override
				public boolean hasNext() {
					this.obtainNextElement();
					return ( this.nextElement != null );
				}

				@Override
				public T next() {
					if ( this.hasNext() ) {
						T element = this.nextElement;
						this.nextElement = null;
						return element;
					}
					else {
						throw new NoSuchElementException();
					}
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
				
			};
		}
	}
}
