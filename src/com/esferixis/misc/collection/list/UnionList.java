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
package com.esferixis.misc.collection.list;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import com.esferixis.misc.collection.UnionCollection;

/**
 * Lista de unión entre listas
 * 
 * @author ariel
 *
 * @param <T>
 */

public class UnionList<T> extends AbstractList<T> {
	private final List< List<T> > lists; // Listas
	private final UnionCollection<T> unionCollection;
	
	/**
	 * @post Crea una lista de unión con la lista de listas especificada
	 */
	public UnionList(List< List<T> > lists) {
		this.lists = lists;
		this.unionCollection = new UnionCollection<T>( (List) this.lists);
	}
	
	/**
	 * @post Crea una lista de unión con la array de listas especificado
	 */
	public UnionList(List<T>... lists) {
		this( Arrays.asList(lists) );
	}

	/**
	 * @post Vacía la lista vaciando las listas
	 */
	@Override
	public void clear() {
		this.unionCollection.clear();
	}
	
	/**
	 * @post Devuelve si contiene el elemento especificado
	 */
	@Override
	public boolean contains(Object element) {
		return this.unionCollection.contains(element);
	}
	
	/**
	 * @post Agrega el elemento en el índice especificado
	 */
	@Override
	public void add(int index, T element) {
		this.addAll(index, Collections.singleton(element) );
	}
	
	// Operación con listas
	private abstract class ListOperation {
		/**
		 * @pre La lista no puede ser nula
		 * @post Efectúa una operación con el índice, la lista y el offset especificados,
		 * 		 Devuelve si tiene que continuar.
		 */
		public abstract boolean doListOperation(List<T> list, int offset);
	}
	
	// Operación con índice
	private abstract class IndexOperation extends ListOperation {
		private int generalIndex;
		private boolean founded;
		
		/**
		 * @post Asigna el índice general
		 */
		public void setGeneralIndex(int generalIndex) {
			this.generalIndex = generalIndex;
			this.founded = false;
		}
		
		/**
		 * @post Efectúa la operación de lista con el índice cuando el offset es adecuado
		 */
		public final boolean doListOperation(List<T> list, int offset) {
			if ( this.generalIndex >= offset ) {
				this.doIndexOperation(list, this.generalIndex-offset);
				this.founded = true;
			}
			
			return !this.founded; // Si fue encontrado, no continuar
		}
		
		/**
		 * @pre La lista no puede ser nula
		 * @post Efectúa una operación con el índice y la lista especificados
		 */
		public abstract void doIndexOperation(List<T> list, int index);
		
		/**
		 * @post Devuelve si fue encontrado
		 */
		public boolean isFounded() {
			return this.founded;
		}
	}
	
	/**
	 * @pre La operación no puede ser nula
	 * @post Efectúa la operación de lista especificada
	 */
	private void doListOperation(ListOperation operation) {
		if ( operation == null ) {
			throw new NullPointerException();
		}
		
		final Iterator< List<T> > it = this.lists.iterator();
		int offset=0;
		boolean continueFinding=true;
		
		while ( it.hasNext() && continueFinding ) {
			final List<T> eachList = it.next();
			continueFinding = operation.doListOperation(eachList, offset);
			offset += eachList.size();
		}
	}
	
	/**
	 * @pre La operación no puede ser nula
	 * @post Efectúa la operación de índice especificada con el índice especificado
	 */
	private void doIndexOperation(int index, IndexOperation operation) {
		if ( operation == null ) {
			throw new NullPointerException();
		}
		
		if ( index >= 0 ) {
			operation.setGeneralIndex(index);
			this.doListOperation(operation);
		}
		
		if ( !operation.isFounded() ) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	/**
	 * @pre La colección de elementos no puede ser nula y tiene que ser un índice válido
	 * @post Agrega los elementos en el índice especificado
	 */
	@Override
	public boolean addAll(int index, Collection<? extends T> elements) {
		class LocalOperation extends IndexOperation {
			private final Collection<? extends T> elements;
			private boolean modified;
			
			/**
			 * @pre La colección elementos no puede ser nula
			 * @post Crea una operación de agregado con los elementos especificados
			 */
			public LocalOperation(Collection<? extends T> elements) {
				if ( elements == null ) {
					throw new NullPointerException();
				}
				
				this.elements = elements;
				this.modified = false;
			}
			
			@Override
			public void doIndexOperation(List<T> list, int index) {
				this.modified = list.addAll(index, elements);
			}
			
			/**
			 * @post Devuelve si hubo cambios
			 */
			public boolean isModified() {
				return this.modified;
			}
		}
		
		final LocalOperation operation = new LocalOperation(elements);
		this.doIndexOperation(index, operation );
		return operation.isModified();
	}
	
	/**
	 * @post Devuelve la última lista
	 */
	private List<T> lastList() {
		return this.lists.get(this.lists.size()-1);
	}
	
	/**
	 * @post Agrega el elemento en la última lista
	 */
	@Override
	public boolean add(T element) {
		return this.lastList().add(element);
	}
	
	/**
	 * @post Agrega los elementos que están en la colección especificada en la última lista
	 */
	@Override
	public boolean addAll(Collection<? extends T> elements) {
		return this.lastList().addAll(elements);
	}
	
	/**
	 * @post Devuelve el elemento que está en la posición especificada
	 */
	@Override
	public T get(int index) {
		class LocalOperation extends IndexOperation {
			private T element;
			
			/**
			 * @post Crea un obtenedor de elemento
			 */
			public LocalOperation() {
				this.element = null;
			}
			
			@Override
			public void doIndexOperation(List<T> list, int index) {
				this.element = list.get(index);
			}
			
			/**
			 * @post Devuelve el elemento
			 */
			public T getElement() {
				return this.element;
			}
		}
		
		LocalOperation operation = new LocalOperation();
		this.doIndexOperation(index, operation);
		return operation.getElement();
	}
	
	/**
	 * @post Devuelve el hash
	 */
	@Override
	public int hashCode() {
		return this.unionCollection.hashCode();
	}
	
	/**
	 * @post Devuelve la primer ocurrencia del elemento especificado
	 */
	@Override
	public int indexOf(Object element) {
		class LocalOperation extends ListOperation {
			private int generalIndex;
			private final Object searchedElement;
			
			/**
			 * @post Crea un buscador de índice de primer elemento encontrado que concida con el especificado de primero a último
			 */
			public LocalOperation(Object searchedElement) {
				this.generalIndex = -1;
				this.searchedElement = searchedElement;
			}
			
			@Override
			public boolean doListOperation(List<T> list, int offset) {
				final int localIndex = list.indexOf(this.searchedElement);
				if ( localIndex != -1 ) {
					this.generalIndex = localIndex + offset;
					return false;
				}
				else {
					return true;
				}
			}
			
			/**
			 * @post Devuelve el índice general del elemento
			 */
			public int getGeneralIndex() {
				return this.generalIndex;
			}
			
		}
		
		LocalOperation operation = new LocalOperation(element);
		this.doListOperation(operation);
		return operation.getGeneralIndex();
	}
	
	/**
	 * @post Devuelve el tamaño de la lista
	 */
	@Override
	public int size() {
		return this.unionCollection.size();
	}
	
	/**
	 * @post Devuelve el iterador
	 */
	@Override
	public Iterator<T> iterator() {		
		return this.listIterator();
	}
	
	/**
	 * @post Devuelve el iterador con el índice especificado.
	 * 		 Los efectos que produzcan las modificaciones de las listas
	 * 		 dependen de sus implementaciones.
	 * 		 Y los efectos que produzcan las modificaciones sobre la
	 * 		 lista de listas depende de la implementación de ésta
	 */
	@Override
	public ListIterator<T> listIterator(int index) {
		class LocalListIterator implements ListIterator<T> {
			private ListIterator< List<T> > listIterator;
			private ListIterator<T> elementIterator;
			private ListIterator<T> lastElementIterator; // Iterador del último elemento
			
			/**
			 * @pre El índice tiene que estar en el rango
			 * @post Crea el iterador de lista con el índice especificado
			 */
			public LocalListIterator(int index) {
				int offset=0;
				
				this.listIterator = UnionList.this.lists.listIterator();
				
				// Buscar la lista que le corresponde y obtener el iterador
				this.elementIterator = null;
				while ( this.listIterator.hasNext() && (this.elementIterator == null) ) {
					final List<T> eachList = this.listIterator.next();
					
					if ( index >= offset ) {
						this.elementIterator = eachList.listIterator(index-offset);
					}
					
					offset += eachList.size();
				}
				
				if ( this.elementIterator == null ) {
					throw new IndexOutOfBoundsException();
				}
				
				this.lastElementIterator = null;
			}
			
			@Override
			public void add(T element) {
				this.elementIterator.add(element);
			}

			@Override
			public boolean hasNext() {
				if ( this.elementIterator.hasNext() ) {
					return true;
				}
				else {
					final boolean randomAccess = UnionList.this.lists instanceof RandomAccess;
					final ListIterator< List<T> > originalListIterator;
					int numberOfLists=0;
					
					boolean hasNext = false;
					if ( randomAccess ) {
						originalListIterator = UnionList.this.lists.listIterator(this.listIterator.nextIndex()-1);
					}
					else {
						originalListIterator = this.listIterator;
					}
					
					while ( this.listIterator.hasNext() && (!hasNext) ) {
						final List<T> eachList = this.listIterator.next();
						hasNext = eachList.iterator().hasNext();
						
						numberOfLists++;
					}
					
					if ( !randomAccess ) {
						for ( int i = 0 ; i < numberOfLists ; i++ ) {
							this.listIterator.previous();
						}
						this.listIterator = originalListIterator;
					}
					
					return hasNext;
				}
			}

			@Override
			public boolean hasPrevious() {
				if ( this.elementIterator.hasPrevious() ) {
					return true;
				}
				else {
					final boolean randomAccess = UnionList.this.lists instanceof RandomAccess;
					final ListIterator< List<T> > originalListIterator;
					int numberOfLists=0;
					
					boolean hasPrevious = false;
					if ( randomAccess ) {
						originalListIterator = UnionList.this.lists.listIterator(this.listIterator.nextIndex()-1);
					}
					else {
						originalListIterator = this.listIterator;
					}
					
					while ( this.listIterator.hasPrevious() && (!hasPrevious) ) {
						final List<T> eachList = this.listIterator.next();
						hasPrevious = eachList.listIterator(eachList.size()).hasPrevious();
						
						numberOfLists++;
					}
					
					if ( !randomAccess ) {
						for ( int i = 0 ; i < numberOfLists ; i++ ) {
							this.listIterator.next();
						}
						this.listIterator = originalListIterator;
					}
					
					return hasPrevious;
				}
			}

			@Override
			public T next() {
				this.lastElementIterator = this.elementIterator;
				
				// Si no hay elemento siguiente en el iterador de listas de la lista actual
				// buscar en la lista siguiente, y así sucesivamente
				// hasta encontrarlo o hasta que no haya más listas
				while ( ( !this.elementIterator.hasNext() ) && this.listIterator.hasNext() ) {
					this.elementIterator = this.listIterator.next().listIterator();
				}
				
				return this.elementIterator.next();
			}

			@Override
			public int nextIndex() {
				int index = this.elementIterator.nextIndex();
				int listIndex=0;
				
				// Iterar hasta la primer lista para obtener el offset sumando los tamaños
				while ( this.listIterator.hasPrevious() ) {
					index += this.listIterator.previous().size();
					listIndex++;
				}
				
				// Obtener el iterador de listas en la posición en que estaba
				this.listIterator = UnionList.this.lists.listIterator(listIndex);
				this.listIterator.next();
				
				return index;
			}

			@Override
			public T previous() {
				this.lastElementIterator = this.elementIterator;
				
				// Si no hay elementos previos en el iterador de listas de la lista actual
				// buscar en la anterior
				if ( !this.elementIterator.hasPrevious() ) {
					List<T> previousList = this.listIterator.previous();
					this.elementIterator = previousList.listIterator(previousList.size());
				}
				
				return this.elementIterator.previous();
			}

			@Override
			public int previousIndex() {
				int index = this.elementIterator.previousIndex();
				int listIndex=0;
				
				// Iterar hasta la primer lista para obtener el offset sumando los tamaños
				while ( this.listIterator.hasPrevious() ) {
					index += this.listIterator.previous().size();
					listIndex++;
				}
				
				// Obtener el iterador de listas en la posición en que estaba
				this.listIterator = UnionList.this.lists.listIterator(listIndex);
				this.listIterator.next();
				
				return index;
			}

			@Override
			public void remove() {
				if ( this.lastElementIterator != null ) {
					this.lastElementIterator.remove();
				}
				else {
					throw new IllegalStateException();
				}
			}

			@Override
			public void set(T element) {
				if ( this.lastElementIterator != null ) {
					this.lastElementIterator.set(element);
				}
				else {
					throw new IllegalStateException();
				}
			}
			
		}
		
		return new LocalListIterator(index);
	}
	
	/**
	 * @post Quita el elemento que tiene el índice especificado
	 */
	@Override
	public T remove(int index) {
		T element = null;
		boolean founded=false;
		
		if ( index >= 0 ) {
			final Iterator< List<T> > it = this.lists.iterator();
			int offset=0;
		
			while ( it.hasNext() && (!founded) ) {
				final List<T> eachList = it.next();
				if ( index >= offset ) {
					element = eachList.remove(index);
					founded = true;
				}
				offset += eachList.size();
			}
		}
		
		if ( founded ) {
			return element;
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}
}
