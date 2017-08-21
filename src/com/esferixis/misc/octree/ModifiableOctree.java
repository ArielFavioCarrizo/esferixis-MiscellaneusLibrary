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

/**
 * Octree modificable
 */
public abstract class ModifiableOctree<T> extends Octree<T> {
	// Fábrica abstracta
	public static abstract class Factory {
		/**
		 * @post Crea un octree modificable
		 */
		public abstract <T> ModifiableOctree<T> create();
	}
	
	public abstract class Node extends Octree<T>.Node {
		/**
		 * @pre La ubicación no puede ser nula
		 * @post Devuelve el nodo dada la posición especificada
		 */
		@Override
		public abstract Node get(NodeUbication ubication);
		
		/**
		 * @post Devuelve el nodo padre
		 */
		@Override
		public abstract Node getParent();
		
		/**
		 * @pre La ubicación no puede ser nula
		 * @post Si existe un nodo vecino en la ubicación
		 * 		 especificada lo devuelve
		 */
		@Override
		public Node getNeighboorNode(NodeUbication neighboorUbication) {
			return (Node) super.getNeighboorNode(neighboorUbication);
		}
		
		/**
		 * @pre La ubicación no tiene que ser nula.
		 * 		El nodo especificado no tiene que tener padre
		 * 		salvo que se especifique el mismo nodo que estaba
		 * 		antes.
		 * 		Si se rompe con ésta precondición lanza
		 * 		IllegalStateException.
		 * @post Especifica el nodo en la ubicación especificada,
		 * 		 si es null en su lugar hace una poda.
		 */
		public abstract void set(NodeUbication ubication, Node node);
		
		/**
		 * @post Especifica el elemento
		 */
		public abstract void setElement(T element);
	}
	
	/**
	 * @post Crea un nodo con el elemento especificado
	 */
	public abstract ModifiableOctree<T>.Node newNode(T element);
}
