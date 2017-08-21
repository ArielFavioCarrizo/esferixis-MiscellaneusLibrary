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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.esferixis.misc.dynamicFields.DynamicFieldsContainerObject;
import com.esferixis.misc.loader.DataLoadingErrorException;
import com.esferixis.misc.map.DynamicFieldCachedHashMap;
import com.esferixis.misc.reference.DynamicReference;

/**
 * @author ariel
 *
 */
public final class LinkedMruLoadingManager<T extends DynamicFieldsContainerObject> extends LoadingManager<T> {
	private final class Node {
		private Node beforeNode;
		private Node afterNode;
		
		private final T element;
		private long occupiedSpace;
		
		private final NodeElementObserver nodeElementObserver;
		
		public Node(DynamicReference<Node> nodeReference, T element) {
			if ( element != null ) {
				this.element = element;
				
				this.beforeNode = null;
				this.afterNode = null;
				
				this.nodeElementObserver = new NodeElementObserver(nodeReference, this);
			}
			else {
				throw new NullPointerException();
			}
		}
	}
	
	private final class NodeElementObserver implements LoadingStrategy.Observer {
		private final DynamicReference<Node> nodeReference;
		private final Node node;
		
		/**
		 * @post Crea el observer de nodo
		 */
		public NodeElementObserver(DynamicReference<Node> nodeReference, Node node) {
			if ( nodeReference != null ) {
				this.nodeReference = nodeReference;
				this.node = node;
				this.nodeReference.set(this.node);
			}
			else {
				throw new NullPointerException();
			}
		}
		
		/* (non-Javadoc)
		 * @see com.esferixis.misc.garbagecollector.LoadingStrategy.Observer#notifyHasLoadedDependencies()
		 */
		@Override
		public void notifyHasLoadedUsers() {
			LinkedMruLoadingManager.this.removeNodeInLinkedLists(this.node);
			this.nodeReference.set(null);
		}

		/* (non-Javadoc)
		 * @see com.esferixis.misc.garbagecollector.LoadingStrategy.Observer#notifyHasUnloadedDependencies()
		 */
		@Override
		public void notifyHasNotLoadedUsers() {
			this.nodeReference.set(this.node);
			LinkedMruLoadingManager.this.insertNodeAtFirstPosition(this.node);
		}
		
	}
	
	private final Map<T, DynamicReference< Node > > nodePerElement;
	
	private Node firstNode;
	private Node lastNode;
	
	private long nodesQuantity;
	private long freeSpace;
	
	private final List<T> oldLoadedElements;
	
	/**
	 * @param loadingStrategy
	 * @param maxElements
	 * @param capacity
	 */
	public LinkedMruLoadingManager(LoadingStrategy<T> loadingStrategy, long maxElements, long capacity) {
		super(loadingStrategy, maxElements, capacity);
		
		this.firstNode = null;
		this.nodesQuantity = 0;
		
		this.freeSpace = capacity;
		
		this.nodePerElement = new DynamicFieldCachedHashMap<T, DynamicReference< Node > >();
		
		this.oldLoadedElements = new ArrayList<T>();
	}
	
	/**
	 * @post Inserta el nodo en la primera posición
	 */
	private void insertNodeAtFirstPosition(Node node) {
		node.beforeNode = null;
		node.afterNode = this.firstNode;
		
		if ( this.firstNode != null ) {
			this.firstNode.beforeNode = node;
		}
		
		this.firstNode = node;
		
		if ( this.lastNode == null ) {
			this.lastNode = node;
		}
	}
	
	/**
	 * @post Borra el nodo solamente de las listas enlazadas
	 */
	private void removeNodeInLinkedLists(Node node) {		
		if ( node.beforeNode != null ) {
			node.beforeNode.afterNode = node.afterNode;
		}
		else {
			this.firstNode = node.afterNode;
		}
		
		if ( node.afterNode != null ) {
			node.afterNode.beforeNode = node.beforeNode;
		}
		else {
			this.lastNode = node.beforeNode;
		}
	}
	
	/**
	 * @pre El nodo tiene que estar presente y no tiene
	 * 		que tener dependencias cargadas
	 * @post Descarga el nodo, y sólo el nodo especificado
	 */
	private void unloadNode(Node node) {
		this.loadingStrategy.detachObserver(node.element, node.nodeElementObserver);
		this.loadingStrategy.unload(node.element);
		
		this.nodePerElement.remove(node.element);
		
		this.removeNodeInLinkedLists(node);
		
		this.freeSpace += node.occupiedSpace;
		this.nodesQuantity--;
	}
	
	/* (non-Javadoc)
	 * @see com.esferixis.misc.garbagecollector.GarbageCollectedLoader#loadElements_internal(java.lang.Iterable)
	 */
	@Override
	protected void loadElements_internal(Collection<T> elements) throws NullPointerException, DataLoadingErrorException, OutOfSpace {
		int quantity = elements.size();
		
		if ( quantity > this.maxElements ) {
			throw new OutOfSpace("Too many elements");
		}
		
		final List<T> elementsToBeLoaded;
		
		{
			List<T> elementsToBeLoadedOriginal = new ArrayList<T>(elements.size()*2);
			
			final Stack<T> elementStack = new Stack<T>();
			elementStack.addAll(elements);
			
			boolean hasUnloadedDependencies = false;
			
			while ( !elementStack.isEmpty() ) {
				final T eachElement = elementStack.pop();
				
				elementsToBeLoadedOriginal.add(eachElement);
				
				if ( !this.loadingStrategy.isLoaded(eachElement) ) {
					Collection<T> dependencies = this.loadingStrategy.getDependencies(eachElement);
					
					hasUnloadedDependencies |= !dependencies.isEmpty();
					
					for ( T eachDependency : dependencies ) {
						elementStack.push(eachDependency);
					}
				}
			}
			
			// Importante para que las dependencias se carguen en el orden correcto
			if ( hasUnloadedDependencies ) {
				Collections.reverse(elementsToBeLoadedOriginal);
			}
			
			elementsToBeLoaded = Collections.unmodifiableList(new ArrayList<T>(elementsToBeLoadedOriginal));
		}
		
		for ( T eachElement : elementsToBeLoaded ) {
			DynamicReference<Node> associatedNodeReference = this.nodePerElement.get(eachElement);
			
			// Si ya está cargado, no está reservado y no está al principio
			if ( associatedNodeReference != null ) {
				if ( ( associatedNodeReference.get() != null ) && ( associatedNodeReference.get() != this.firstNode ) ) {
					this.removeNodeInLinkedLists(associatedNodeReference.get());
					
					this.insertNodeAtFirstPosition(associatedNodeReference.get());
				}
			}
			else {
				long requiredSpace = this.loadingStrategy.getOccupiedSpace(eachElement);
				
				// Descargar el último elemento que se pueda descargar, hasta que haya suficiente espacio
				Node eachNode = this.lastNode;
				
				while ( ( this.freeSpace < requiredSpace ) || ( this.nodesQuantity == this.capacity ) ) {
					if ( !elementsToBeLoaded.contains(eachNode.element) ) {
						this.unloadNode(eachNode);
						eachNode = this.lastNode;
					}
					else {
						eachNode = eachNode.beforeNode;
						
						// ¡SIN ESPACIO!
						if ( eachNode == null ) {
							// Vuelve al estado anterior
							this.loadElements(this.oldLoadedElements);
							
							throw new OutOfSpace("Too many elements");
						}
					}
				}
				
				// Cargar el elemento y crear el nodo
				this.loadingStrategy.load(eachElement);
				
				associatedNodeReference = new DynamicReference<Node>();
				associatedNodeReference.set(new Node(associatedNodeReference, eachElement));
				
				associatedNodeReference.get().occupiedSpace = requiredSpace;
				associatedNodeReference.get().nodeElementObserver.notifyHasNotLoadedUsers();
				
				this.loadingStrategy.attachObserver(associatedNodeReference.get().element, associatedNodeReference.get().nodeElementObserver);
				
				this.freeSpace -= requiredSpace;
				this.nodesQuantity++;
				
				this.nodePerElement.put(eachElement, associatedNodeReference);
			}
		}
		
		this.oldLoadedElements.clear();
		this.oldLoadedElements.addAll(elements);
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.garbagecollector.GarbageCollectedLoader#destroy_internal()
	 */
	@Override
	protected void destroy_internal() {
		while ( this.firstNode != null ) {
			this.unloadNode(this.firstNode);
		}
	}
	
}
