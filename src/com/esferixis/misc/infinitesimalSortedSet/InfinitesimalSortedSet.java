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

/**
 * Éste conjunto no es un conjunto de elementos al estilo de java.util.Set<T>,
 * no es modificable y puede tener continuidades
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

import com.esferixis.misc.GenericComparator;
import com.esferixis.misc.collection.CollectionsExtra;
import com.esferixis.misc.collection.sortedSet.SortedSetSortedListVision;



public final class InfinitesimalSortedSet<T> {
	private final GenericComparator<? super T> genericComparator;
	private final List< SetPrimitive<T> > setPrimitives;
	
	/**
	 * @post Crea un conjunto vacío con el orden natural de los elementos
	 */
	public InfinitesimalSortedSet() {
		this( (Comparator<T>) null);
	}
	
	/**
	 * @post Crea un conjunto vacío con el comparador especificado
	 */
	public InfinitesimalSortedSet(Comparator<? super T> comparator) {
		this(comparator, new ArrayList< SetPrimitive<T> >());
	}
	
	/**
	 * @post Crea un conjunto con el orden natural de los elementos y el punto especificado
	 */
	public InfinitesimalSortedSet(T element) {
		this((Comparator<T>) null, element);
	}
	
	/**
	 * @post Crea un conjunto con el comparador y el punto especificado
	 */
	public InfinitesimalSortedSet(Comparator<? super T> comparator, T element) {
		this(comparator, Collections.singletonList(new SetPrimitive<T>(comparator, element) ));
	}
	
	/**
	 * @post Crea un conjunto con el conjunto de elementos ordenado especificado
	 */
	public InfinitesimalSortedSet(SortedSet<? extends T> elements) {
		this.genericComparator = new GenericComparator(elements.comparator());
		this.setPrimitives = new ArrayList< SetPrimitive<T> >(elements.size());
		for ( T eachElement : elements ) {
			this.setPrimitives.add(new SetPrimitive<T>(eachElement));
		}
	}
	
	/**
	 * @post Crea un conjunto con la colección de elementos especificada de acuerdo al orden natural
	 */
	public InfinitesimalSortedSet(Collection<? extends T> elements) {
		this( (SortedSet<? extends T>) new TreeSet(elements) );
	}
	
	/**
	 * @post Crea un conjunto con el comparador y la lista de primitivas especificados
	 */
	private InfinitesimalSortedSet(Comparator<? super T> comparator, List< SetPrimitive<T> > setPrimitives) {
		this.genericComparator = new GenericComparator<T>(comparator);
		this.setPrimitives = setPrimitives;
	}
	
	/**
	 * @pre El intervalo no puede ser nulo
	 * @post Crea un conjunto con el intervalo especificado
	 */
	public InfinitesimalSortedSet(Interval<T> interval) {
		if ( interval != null ) {
			this.genericComparator = new GenericComparator(interval.getElementComparator());
			
			boolean addMaxSetPrimitive;
			T pointElement=null; // Elemento de punto
			if ( interval.getMaxSetPrimitive() != null ) {
				if ( interval.getMinSetPrimitive() != null ) {
					// Si el intervalo no tiene longitud cero
					if ( interval.getMinSetPrimitive().compareTo( interval.getMaxSetPrimitive() ) != 0 ) {
						// Entonces agregar la otra primitiva de conjunto
						addMaxSetPrimitive = true;
					}
					else {
						addMaxSetPrimitive = false;
						pointElement = interval.getMinSetPrimitive().getReferenceElement();
					}
				}
				else {
					addMaxSetPrimitive = true;
				}
			}
			else {
				addMaxSetPrimitive = false;
			}
			
			int size = addMaxSetPrimitive ? 1 : 0;
			if ( interval.getMinSetPrimitive() != null ) {
				size++;
			}
			this.setPrimitives = new ArrayList< SetPrimitive<T> >(size);
			
			if ( pointElement != null ) { // Si hay elemento de punto
				this.setPrimitives.add( new SetPrimitive<T>(this.getComparator(), pointElement) );
			}
			else { // Si no lo hay, no es un punto
				if ( interval.getMinSetPrimitive() != null ) {
					this.setPrimitives.add( interval.getMinSetPrimitive() );
				}
			
				if ( addMaxSetPrimitive ) {
					this.setPrimitives.add( interval.getMaxSetPrimitive() );
				}
			}
		}
		else {
			throw new NullPointerException();
		}
	}

	/**
	 * @post Devuelve el comparador
	 */
	public Comparator<? super T> getComparator() {
		return this.genericComparator.getTargetComparator();
	}
	
	// Operador binario
	private abstract class BinaryOperator {
		/**
		 * @post Devuelve el resultado de la operación entre dos primitivas con igual frontera
		 */
		protected abstract SetPrimitive<T> equalBorderSetPrimitiveOperation(SetPrimitive<T> setPrimitive1, SetPrimitive<T> setPrimitive2);
		
		/**
		 * @post Devuelve si incluye o excluye los elementos que tienen borde más pequeño que la primitiva
		 */
		protected abstract boolean addLesserBorderSetPrimitives(SetPrimitive<T> endSetPrimitive);
		
		/**
		 * @post Devuelve si incluye el excedente de una lista
		 */
		protected abstract boolean addExcedent();
		
		/**
		 * @pre El otro conjunto no puede ser nulo y tiene que tener el mismo comparador
		 * @post Efectúa la operación especificada con el conjunto infinitesimal especificado
		 * 		 asumiendo que si son iguales entonces devuelve el mismo conjunto
		 */
		public InfinitesimalSortedSet<T> doOperation(InfinitesimalSortedSet<T> other) {
			// Verificar que tengan el mismo comparador
			if ( other != null ) {
				if ( !( other.genericComparator.equals(InfinitesimalSortedSet.this.genericComparator) ) ) {
					throw new IllegalArgumentException("Comparator mismatch");
				}
			}
			else {
				throw new NullPointerException();
			}
			
			InfinitesimalSortedSet<T> resultInfinitesimalSortedSet;
			
			if ( other != InfinitesimalSortedSet.this ) { // Si no son los mismos conjuntos
				List< SetPrimitive<T> > list1 = InfinitesimalSortedSet.this.setPrimitives;
				List< SetPrimitive<T> > list2 = other.setPrimitives;
				
				List< SetPrimitive<T> > resultList = new ArrayList< SetPrimitive<T> >(list1.size()+list2.size());
				
				while ( (!list1.isEmpty()) || (!list2.isEmpty()) ) { // Seguir mientras haya por lo menos una lista no vacía
					List< SetPrimitive<T> > listToBeAdded;
					
					// Si las dos listas tienen elementos
					if ( ( !list1.isEmpty() ) && ( !list2.isEmpty() ) ) {
						
						// Comparación entre listas de fronteras menores
						final int minBorderListsComparison = list1.get(0).compareTo( list2.get(0) );
						
						if ( minBorderListsComparison == 0 ) { // Si las fronteras son iguales
							SetPrimitive<T> endSetPrimitive = this.equalBorderSetPrimitiveOperation(list1.get(0), list2.get(0));
							
							// Quitar la primer primitiva a ambas listas
							list1 = list1.subList(1, list1.size());
							list2 = list2.subList(1, list2.size());
							
							// Si hay primitiva final agregarla
							if ( endSetPrimitive != null ) {
								listToBeAdded = Collections.singletonList(endSetPrimitive);
							}
							else { // Caso contrario
								listToBeAdded = Collections.emptyList(); // No se agregarán elementos
							}
						}
						else {
							// Cubrir todos los elementos que estén desde la menor frontera menor hasta la mayor excluyéndola de la lista
							
							// Clasifica las listas por frontera menor, en menor y mayor
							final List< SetPrimitive<T> > minBorderList, maxBorderList;
							if ( minBorderListsComparison < 0 ) { // Si la frontera de la lista 1 es menor que la 2
								minBorderList = list1; // La lista 1 tiene menor frontera menor
								maxBorderList = list2; // La lista 2 tiene mayor frontera menor
							}
							else { // Caso contrario
								minBorderList = list2; // La lista 2 tiene menor frontera menor
								maxBorderList = list1; // La lista 1 tiene mayor frontera menor
							}
							
							final SetPrimitive<T> firstSetPrimitiveMaxBorderList = maxBorderList.get(0);
							
							// Buscar el índice de la primitiva con frontera mayor de la lista con frontera menor menor que sea menor a la
							// frontera de la lista que tiene la frontera menor mayor
							int toIndex = CollectionsExtra.quadraticSearch(minBorderList, firstSetPrimitiveMaxBorderList);
							//int toIndex = Collections.binarySearch(minBorderList, firstSetPrimitiveMaxBorderList, InfinitesimalSortedSet.this.borderSetPrimitiveComparator);
							if ( toIndex < 0 ) {
								toIndex = -(toIndex + 1);
							}
							
							// La lista 1 será la lista que contiene a todos elementos suyos que son superiores o iguales a la frontera menor mayor
							list1 = minBorderList.subList(toIndex, minBorderList.size());
							
							// Y la otra lista la lista mayor frontera menor
							list2 = maxBorderList;
							
							// Si la primitiva final (La que tiene la frontera menor con la lista con frontera menor mayor) incluye las primitivas anteriores
							if ( this.addLesserBorderSetPrimitives(firstSetPrimitiveMaxBorderList) ) {
								// Lista que contiene todos los elementos de la lista con menor frontera menor que tienen frontera menor a la frontera menor mayor entre listas
								listToBeAdded = minBorderList.subList(0, toIndex);
							}
							else { // Caso contrario
								// No se agregarán elementos
								listToBeAdded = Collections.emptyList();
							}
						}
					}
					else { // Si sólo hay una
						final List< SetPrimitive<T> > nonEmptyList;
						if ( !list1.isEmpty() ) { // Si la lista 1 no está vacía
							nonEmptyList = list1; // Entonces la lista 1 no está vacía
							// Y quedará vacía
							list1 = Collections.emptyList();
						}
						else { // Caso contrario, la lista 2 no está vacía
							nonEmptyList = list2; // Entonces la lista 2 no está vacía
							// Y quedará vacía
							list2 = Collections.emptyList();
						}
						
						if ( this.addExcedent() ) {
							listToBeAdded = nonEmptyList;
						}
						else {
							listToBeAdded = Collections.emptyList();
						}
					}
					
					// Agregar la lista uniéndola con la anterior si tiene elementos
					if ( !listToBeAdded.isEmpty() ) {
						SetPrimitive<T> unionSetPrimitive = null;
						
						boolean contiguous=false;
						if ( !resultList.isEmpty() ) {
							final SetPrimitive<T> greaterResultListSetPrimitive = resultList.get(resultList.size()-1); // Primer primitiva
							final SetPrimitive<T> lesserListToBeAdded = listToBeAdded.get(0); // Segunda primitiva
							
							// Si coinciden en el límite
							if ( greaterResultListSetPrimitive.equalsLimit( lesserListToBeAdded ) ) {
								// Si una primitiva es cerrada y la otra es abierta
								if ( greaterResultListSetPrimitive.isClosed() == !lesserListToBeAdded.isClosed() ) {
									contiguous = true; // Entonces son contiguas
									
									// Si ninguna primitiva tiene orientación cero
									if ( ( greaterResultListSetPrimitive.getOrientation() != 0 ) && (lesserListToBeAdded.getOrientation() != 0 ) ) {
										unionSetPrimitive = null; // Entonces no hay primitiva de unión porque no hay acotación
									}
									else { // Caso contrario
										// Encontrar la primitiva que no es un punto
										SetPrimitive<T> nonPointSetPrimitive;
										if ( lesserListToBeAdded.getOrientation() != 0 ) {
											nonPointSetPrimitive = lesserListToBeAdded;
										}
										else {
											nonPointSetPrimitive = greaterResultListSetPrimitive;
										}
										
										// Hallar la primitiva de unión
										unionSetPrimitive = new SetPrimitive<T>(nonPointSetPrimitive.getReferenceElement(), true, nonPointSetPrimitive.getOrientation() );
									}
									
								}
							}
						}
						
						// Si es contigua
						if ( contiguous ) {
							// Quitar el último elemento si la lista de resultado tiene elementos
							if ( !resultList.isEmpty() ) {
								resultList.remove(resultList.size()-1);
							}
							
							// Agregar la primitiva de unión si hay
							if ( unionSetPrimitive != null ) {
								resultList.add( unionSetPrimitive );
							}
							
							// Agregar lo que queda
							resultList.addAll(listToBeAdded.subList(1, listToBeAdded.size()));
						}
						else { // Caso contrario
							resultList.addAll(listToBeAdded); // Agregar tal cual como está
						}
					}
				}
				
				// Crear el conjunto infinitesimal ordenado con el mismo comparador y la lista de primitivas ordenada obtenida
				resultInfinitesimalSortedSet = new InfinitesimalSortedSet<T>(InfinitesimalSortedSet.this.getComparator(), resultList);
			}
			else {
				resultInfinitesimalSortedSet = InfinitesimalSortedSet.this;
			}
			
			return resultInfinitesimalSortedSet;
		}
	}
	
	/**
	 * @post Devuelve la unión entre éste conjunto infinitesimal con otro conjunto infinitesimal
	 */
	public InfinitesimalSortedSet<T> union(InfinitesimalSortedSet<T> other) {
		class UnionOperator extends BinaryOperator {

			@Override
			protected SetPrimitive<T> equalBorderSetPrimitiveOperation(
					SetPrimitive<T> setPrimitive1, SetPrimitive<T> setPrimitive2) {
				final SetPrimitive<T> unionSetPrimitive;
				
				// Si la primer primitiva es cerrada
				if ( setPrimitive1.isClosed() ) {
					// También lo es la otra
					
					// Si la orientación de ambas primitivas es igual o si la primitiva segunda es un punto
					if ( ( setPrimitive1.getOrientation() == setPrimitive2.getOrientation() ) || ( setPrimitive2.getOrientation() == 0 ) ) {
						unionSetPrimitive = setPrimitive1; // La unión entre las dos es la primera
					}
					else { // Caso contrario
						unionSetPrimitive = null; // No hay ya que la unión no es acotada
					}
				}
				else { // Caso contrario
					// La primera es igual a la segunda
					unionSetPrimitive = setPrimitive1; // Ambas son la unión entre las dos
				}
				
				return unionSetPrimitive;
			}

			@Override
			protected boolean addLesserBorderSetPrimitives(
					SetPrimitive<T> endSetPrimitive) {
				return (endSetPrimitive.getOrientation() >= 0);
			}

			@Override
			protected boolean addExcedent() {
				return true;
			}
		}
		
		return (new UnionOperator()).doOperation(other);
	}
	
	/**
	 * @post Devuelve la intersección entre éste conjunto infinitesimal con otro conjunto infinitesimal
	 */
	public InfinitesimalSortedSet<T> intersection(InfinitesimalSortedSet<T> other) {
		class IntersectionOperator extends BinaryOperator {

			@Override
			protected SetPrimitive<T> equalBorderSetPrimitiveOperation(
					SetPrimitive<T> setPrimitive1, SetPrimitive<T> setPrimitive2) {
				final SetPrimitive<T> intersectionSetPrimitive;
				final T commonElement = setPrimitive1.getReferenceElement();
				
				// Si la primer primitiva es cerrada
				if ( setPrimitive1.isClosed() ) {
					// Si la orientación de ambas es diferente
					if ( setPrimitive1.getOrientation() != setPrimitive2.getOrientation() ) {
						// La intersección es un punto
						intersectionSetPrimitive = new SetPrimitive<T>(InfinitesimalSortedSet.this.getComparator(), commonElement);
					}
					else {  // Caso contrario
						intersectionSetPrimitive = setPrimitive1; // Por ser iguales la intersección es la primera
					}
				}
				else { // Caso contrario
					// La primera es igual a la segunda
					intersectionSetPrimitive = setPrimitive1; // Ambas son la intersección entre las dos
				}
				
				return intersectionSetPrimitive;
			}

			@Override
			protected boolean addLesserBorderSetPrimitives(
					SetPrimitive<T> endSetPrimitive) {
				return ( endSetPrimitive.getOrientation() < 0);
			}

			@Override
			protected boolean addExcedent() {
				return false;
			}
			
		}
		
		return (new IntersectionOperator()).doOperation(other);
	}
	
	/**
	 * @post Devuelve el conjunto opuesto
	 */
	public InfinitesimalSortedSet<T> opposite() {
		List< SetPrimitive<T> > resultList = new ArrayList< SetPrimitive<T> >(this.setPrimitives.size()*2+2);
		
		SetPrimitive<T> previousSetPrimitive=null;
		for ( SetPrimitive<T> eachSetPrimitive : this.setPrimitives ) {
			if ( eachSetPrimitive.getOrientation() != 0 ) { // Si no es un punto
				boolean addOppositeElement=true;
				
				// Si es un intervalo abierto que apunta hacia los positivos
				if ( ( !eachSetPrimitive.isClosed() ) && ( eachSetPrimitive.getOrientation() > 0 ) ) {
					// Si hay primitiva anterior
					if ( previousSetPrimitive != null ) {
						// Y el intervalo anterior es uno abierto que apunta hacia los negativos
						if ( ( !previousSetPrimitive.isClosed() ) && ( previousSetPrimitive.getOrientation() < 0 ) ) {
							// Si coinciden en el límite
							if ( eachSetPrimitive.equalsLimit( previousSetPrimitive ) ) {
								// Entonces es un punto
								resultList.set( resultList.size()-1, new SetPrimitive<T>(this.getComparator(), eachSetPrimitive.getReferenceElement()) );
								addOppositeElement = false;
							}
						}
					}
				}
				
				// Si es un intervalo que apunta hacia los positivos y no hay primitiva anterior
				if ( ( eachSetPrimitive.getOrientation() > 0 ) && ( previousSetPrimitive == null ) ) {
					// Agregar la primitiva abierta de infinito negativo
					resultList.add( new SetPrimitive<T>(this.getComparator(), null, false, 1) );
				}
				
				
				if ( addOppositeElement ) {
					// Agregar el elemento opuesto
					resultList.add( new SetPrimitive<T>(this.getComparator(), eachSetPrimitive.getReferenceElement(), !eachSetPrimitive.isClosed(), 1 - eachSetPrimitive.getOrientation() ) );
				}
			}
			else { // Caso contrario
				// El opuesto consiste en dos intervalos abiertos que cubren hacia los positivos y negativos, excepto el punto
				resultList.add( new SetPrimitive<T>(this.getComparator(), eachSetPrimitive.getReferenceElement(), false, -1 ) );
				resultList.add( new SetPrimitive<T>(this.getComparator(), eachSetPrimitive.getReferenceElement(), false, 1 ) );
			}
			
			previousSetPrimitive = eachSetPrimitive;
		}
		
		// Si hay última primitiva del opuesto y apunta hacia los positivos
		if ( !resultList.isEmpty() ) {
			if ( resultList.get(resultList.size()-1).getOrientation() > 0 ) {
				// Agregar la primitiva abierta de infinito positivo
				resultList.add( new SetPrimitive<T>(this.getComparator(), null, false, -1) );
			}
		}
		
		return new InfinitesimalSortedSet<T>(this.getComparator(), resultList);
	}
	
	/**
	 * @post Devuelve si el conjunto contiene el elemento especificado
	 */
	public boolean contains(T element) {
		boolean containedElement;
		int index = Collections.binarySearch(this.setPrimitives, new SetPrimitive<T>(this.getComparator(), element));
		if ( index < 0 ) {
			index = -(index + 1);
		}
		containedElement = this.setPrimitives.get(index).contains(element);
		
		if ( containedElement ) {
			if ( index != 0 ) {
				containedElement = this.setPrimitives.get(index-1).contains(element);
			}
		}
		return containedElement;
	}
	
	/**
	 * @post Devuelve si el conjunto contiene a todo el otro conjunto
	 */
	public boolean containsAll(InfinitesimalSortedSet<T> other) {
		/**
		 * Si el opuesto del conjunto se interseca con el otro conjunto
		 * entonces hay elementos del otro conjunto que no están en el primero
		 * por lo tanto no contiene todo
		 * caso contrario no hay elementos del otro conjunto que no estén
		 * en el primero y por lo tanto contiene todo
		 */
		return this.opposite().intersection(other).isEmpty();
	}
	
	/**
	 * @pre La colección de elementos no puede ser nula
	 * @post Devuelve si el conjunto tiene todos los elementos de la colección especificada
	 */
	public boolean containsAll(Collection<? extends T> elements) {
		if ( elements != null ) {
			
			boolean containsAllElements=true;

			Iterator<? extends T> elementIterator = elements.iterator();
		
			while ( elementIterator.hasNext() && containsAllElements ) {
				containsAllElements = this.contains(elementIterator.next());
			}
		
			return containsAllElements;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve el la primer primitiva de conjunto si el conjunto no está vacío,
	 * 		 caso contrario devuelve null
	 */
	public SetPrimitive<T> firstSetPrimitive() {
		SetPrimitive<T> first;
		if ( !this.isEmpty() ) {
			first = this.setPrimitives.get(0);
		}
		else {
			first = null;
		}
		return first;
	}
	
	/**
	 * @post Devuelve la última primitiva de conjunto si el conjunto no está vacío,
	 * 		 caso contrario devuelve null
	 */
	public SetPrimitive<T> lastSetPrimitive() {
		SetPrimitive<T> last;
		if ( !this.isEmpty() ) {
			last = this.setPrimitives.get(this.setPrimitives.size()-1);
		}
		else {
			last = null;
		}
		return last;
	}
	
	/**
	 * @post Devuelve si es un conjunto vacío
	 */
	public boolean isEmpty() {
		return this.setPrimitives.isEmpty();
	}
	
	/**
	 * @post Devuelve el conjunto de primitivas de conjunto ordenado por frontera (Sólo lectura)
	 */
	public SortedSet< SetPrimitive<T> > setPrimitives() {
		return Collections.unmodifiableSortedSet( new SortedSetSortedListVision< SetPrimitive<T> >(this.setPrimitives) );
	}
	
	/**
	 * @post Devuelve el iterable de intervalos
	 */
	public Iterable< Interval<T> > intervalIterable() {
		return new Iterable< Interval<T> >() {

			@Override
			public Iterator<Interval<T>> iterator() {
				class LocalIterator implements Iterator< Interval<T> > {
					private Iterator< SetPrimitive<T> > setPrimitiveIterator;
					
					/**
					 * @post Crea el iterador
					 */
					public LocalIterator() {
						this.setPrimitiveIterator = InfinitesimalSortedSet.this.setPrimitives().iterator();
					}
					
					@Override
					public boolean hasNext() {
						return this.setPrimitiveIterator.hasNext();
					}

					@Override
					public Interval<T> next() {
						if ( this.hasNext() ) { // Si hay iterador de primitivas pendiente
							SetPrimitive<T> setPrimitive = this.setPrimitiveIterator.next();
							Interval<T> nextInterval;
							if ( setPrimitive.getOrientation() > 0 ) {
								SetPrimitive<T> nextPrimitive = this.setPrimitiveIterator.next();
								nextInterval = new Interval<T>(setPrimitive, nextPrimitive);
							}
							else if ( setPrimitive.getOrientation() < 0 ) {
								nextInterval = new Interval<T>(null, setPrimitive);
							}
							else {
								nextInterval = new Interval<T>(setPrimitive);
							}
							return nextInterval;
						}
						else {
							throw new NoSuchElementException();
						}
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				}
				return new LocalIterator();
			}
			
		};
	}
	
	/**
	 * @post Devuelve el conjunto contiguo que contiene todos los elementos
	 */
	public InfinitesimalSortedSet<T> getEnclosingContiguousSet() {
		if ( this.setPrimitives().size() > 1 ) {
			SetPrimitive<T> firstSetPrimitive = this.setPrimitives().first();
			SetPrimitive<T> lastSetPrimitive = this.setPrimitives().last();
			
			if ( firstSetPrimitive.getOrientation() == 0 ) {
				firstSetPrimitive = new SetPrimitive<T>(this.getComparator(), firstSetPrimitive.getReferenceElement(), true, 1);
			}
				
			if ( lastSetPrimitive.getOrientation() == 0 ) {
				lastSetPrimitive = new SetPrimitive<T>(this.getComparator(), lastSetPrimitive.getReferenceElement(), true, 1);
			}
				
			return new InfinitesimalSortedSet<T>( new Interval<T>(firstSetPrimitive, lastSetPrimitive) );
			}
		else {
			return this;
		}
	}
	
	/**
	 * @post Devuelve un conjunto con los puntos que están en los límites
	 */
	public InfinitesimalSortedSet<T> getLimitsPointSet() {
		List< SetPrimitive<T> > pointSetPrimitives = new ArrayList< SetPrimitive<T> >(this.setPrimitives.size());
		
		T lastElement = null;
		for ( SetPrimitive<T> eachSetPrimitive : this.setPrimitives ) {
			final T eachElement = eachSetPrimitive.getReferenceElement();
			boolean addElement;
			
			if ( (eachElement != null) && ( lastElement != null ) ) {
				addElement = !eachElement.equals(lastElement);
			}
			else {
				addElement = true;
			}
			
			if ( addElement ) {
				pointSetPrimitives.add( new SetPrimitive<T>(eachElement) );
			}
			
			lastElement = eachElement;
		}
		
		return new InfinitesimalSortedSet<T>(this.getComparator(), pointSetPrimitives);
	}
	
	/**
	 * @post Devuelve la representación en cadena de carácteres
	 */
	@Override
	public String toString() {
		return "{ " + this.setPrimitives() + " }";
	}
	
	/**
	 * @post Devuelve el hash
	 */
	@Override
	public int hashCode() {
		return this.setPrimitives().hashCode();
	}
	
	/**
	 * @post Devuelve si es igual al objeto especificado
	 */
	@Override
	public boolean equals(Object other) {
		if ( other != null ) {
			if ( other instanceof InfinitesimalSortedSet ) {
				return this.setPrimitives.equals( ( (InfinitesimalSortedSet<?>) other).setPrimitives );
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
}
