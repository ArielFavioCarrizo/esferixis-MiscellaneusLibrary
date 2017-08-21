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
package com.esferixis.misc.containablestrategy.set;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

import com.esferixis.misc.binaryclassifier.AndBinaryClassifier;
import com.esferixis.misc.binaryclassifier.BinaryClassifier;
import com.esferixis.misc.collection.set.FilteredSet;
import com.esferixis.misc.containablestrategy.ContainableStrategy;
import com.esferixis.misc.containablestrategy.binaryclassifier.ContainedBinaryClassifier;
import com.esferixis.misc.containablestrategy.binaryclassifier.ContainerBinaryClassifier;
import com.esferixis.misc.containablestrategy.collection.AbstractContainableStrategyCollection;
import com.esferixis.misc.iterator.FilteredIterator;
import com.esferixis.misc.reference.InmutableReference;



public abstract class AbstractContainableStrategySet<T> extends AbstractContainableStrategyCollection<T> implements ContainableStrategySet<T> {
	
	/**
	 * @pre La estrategia no puede ser nula
	 * @post Crea el conjunto de estrategia de contenibles con la estrategia especificada
	 */
	public AbstractContainableStrategySet(
			ContainableStrategy containableStrategy) {
		super(containableStrategy);
	}

	/**
	 * Conjunto de elementos
	 * 
	 * Implementación que extiende de AbstractSet
	 * haciendo uso de las implementaciones de los métodos implementados en el conjunto de estrategia
	 */
	protected class ElementsSet extends AbstractSet<T> {

		/**
		 * @post Crea la visión de elementos
		 */
		public ElementsSet() {
			
		}
		
		@Override
		public boolean add(T element) {
			return AbstractContainableStrategySet.this.add(element);
		}
		
		@Override
		public Iterator<T> iterator() {
			return AbstractContainableStrategySet.this.iterator();
		}

		@Override
		public int size() {
			return AbstractContainableStrategySet.this.size();
		}
		
	}
	
	/**
	 * @post Devuelve la colección de contenibles
	 * 
	 * 		 Ésta implementación devuelve una instancia de ElementsSet
	 */
	@Override
	public Set<T> elements() {
		return new ElementsSet();
	}
	
	/**
	 * Conjunto de elementos raices
	 * Ésta implementación se basa en el subconjunto basado en estrategia de contenibles
	 */
	protected class RootElementsSet extends AbstractSet<T> {
		/**
		 * @post Crea la visión de elementos raices
		 */
		public RootElementsSet() {
			
		}
		
		/**
		 * @pre El elemento no tiene que contener ningún elemento
		 * 		que pertenezca a la colección original que no sea contenedor de éste
		 * 		O sea que no haya algún elemento que no sea mutualmente contenedor
		 * 	 	con éste y éste contenido.
		 * @post Agrega el elemento especificado con el método add de la colección
		 * 		 origen
		 */
		@Override
		public boolean add(T element) {
			ContainableStrategySet<T> containedVision = AbstractContainableStrategySet.this.containedContainableStrategyCollection(element);
			if ( !containedVision.isEmpty() ) { // Si hay elementos contenidos por el especificado
				ContainableStrategySet<T> containerContainedVision = containedVision.containerContainableStrategyCollection(element);
				// Y dentro de ellos no hay elementos que contengan al especificado
				if ( containerContainedVision.isEmpty() ) {
					// Entonces no es raíz
					throw new IllegalArgumentException("Attemped to add an nonroot element");
				}
			}
			
			return AbstractContainableStrategySet.this.add(element);
		}
		
		/**
		 * @post Devuelve si contiene el elemento especificado
		 * 
		 * 		 Ésta implementación evalúa si hay un elemento
		 * 	 	 igual con el método contains de la colección de elementos
		 * 		 y que la cantidad de elementos que lo contienen sea igual a la
		 * 		 cantidad de elementos que están contenidos
		 */
		@Override
		public boolean contains(Object element) {
			final boolean equals;
			// Si hay un elemento igual en la colección origen
			if ( AbstractContainableStrategySet.this.elements().contains( element ) ) {
				final ContainableStrategySet<T> containerVision = AbstractContainableStrategySet.this.containerContainableStrategyCollection(element);
				/**
				 * La cantidad de elementos que lo contienen tiene que ser igual
				 * a la cantidad de elementos entre ellos que están contenidos por el especificado,
				 * caso contrario hay algunos que contienen al especificado pero no están
				 * contenidos por éste y por lo tanto el especificado no es raíz
				 */
				equals = ( containerVision.size() == containerVision.containedContainableStrategyCollection(element).size() );
			}
			else { // Caso contrario
				equals = false;
			}
			return equals;
		}
		
		/**
		 * @post Devuelve un iterador
		 * 
		 * 		 Ésta implementación devuelve el iterador de elementos filtrado
		 * 		 con un clasificador binario que evalúa que la cantidad de elementos que
		 * 		 contienen a cada especificado sea igual a la cantidad de entre ellos
		 * 		 que están contenidos por el especificado
		 */
		@Override
		public Iterator<T> iterator() {
			class ExclusionClassifier implements BinaryClassifier<T> {
				
				public ExclusionClassifier() {
					
				}
				
				@Override
				public boolean evaluate(T element) {
					final ContainableStrategySet<T> containerVision = AbstractContainableStrategySet.this.containerContainableStrategyCollection(element);
					/**
					 * La cantidad de elementos que lo contienen tiene que ser igual
					 * a la cantidad de elementos entre ellos que están contenidos por el especificado,
					 * caso contrario hay algunos que contienen al especificado pero no están
					 * contenidos por éste y por lo tanto el elemento dado no es raíz
					 */
					return ( containerVision.size() == containerVision.containedContainableStrategyCollection(element).size() );
				}
				
			}
			return new FilteredIterator<T>(AbstractContainableStrategySet.this.iterator(), new ExclusionClassifier());
		}

		/**
		 * @post Devuelve el tamaño de la colección
		 * 
		 * 		 Ésta implementación obtiene el tamaño contando cada elemento con el iterador
		 */
		@Override
		public int size() {
			int count=0;
			Iterator<T> iterator = this.iterator();
			while ( iterator.hasNext() ) {
				iterator.next();
				count++;
			}
			return count;
		}
		
		/**
		 * @post Devuelve si está vacía
		 * 
		 * 		 Ésta implementación devuelve !this.iterator.hasNext()
		 */
		@Override
		public boolean isEmpty() {
			return !this.iterator().hasNext();
		}
	}
	
	/**
	 * @post Devuelve una visión del conjunto de elementos que sólo contiene aquellos
	 * 		 elementos que no están contenidos por ningún otro salvo por aquellos que estén
	 * 		 contenidos,
	 * 		 o sea los elementos raices, asegurando que dichos elementos
	 * 		 contienen todos los elementos por la propiedad de transitividad.
	 * 		 Si se intenta agregar un elemento que está contenido por otros que no lo contienen
	 * 		 lanza IllegalArgumentException
	 * 
	 * 		 Ésta implementación devuelve una instancia de RootElementsSet
	 */
	public Set<T> getRootElements() {
		return new RootElementsSet();
	}
	
	/**
	 * Subconjunto basado en estrategia de contenibles
	 * 
	 * Ésta implementación extiende del subconjunto basado en estrategia
	 * de contenibles abstracto y se basa en el conjunto de elementos
	 * filtrado y un clasificador binario de condición necesaria de pertenencia
	 */
	protected class SubContainableStrategySet extends AbstractContainableStrategySet<T>  {
		protected final BinaryClassifier<Object> filter;
		
		protected final InmutableReference<Object> mandatoryContainer;
		protected final InmutableReference<Object> mandatoryContained;
		
		/**
		 * @pre Por lo menos una de las referencias de contenedor y contenibles requeridos no tiene que ser nula
		 * @post Crea la base de visión de subcolección con la referencia
		 * 		 al elemento contenedor y al contenible necesarios.
		 * 	 	 Si alguno de ellos es nulo, se considerará que no existe
		 */
		public SubContainableStrategySet(InmutableReference<Object> mandatoryContainer, InmutableReference<Object> mandatoryContained) {
			super(AbstractContainableStrategySet.this.getContainableStrategy());
			BinaryClassifier<Object> filter;
			
			if ( mandatoryContainer != null ) {
				filter = new ContainedBinaryClassifier(this.getContainableStrategy(), mandatoryContainer.get());
			}
			else {
				filter = null;
			}
				
			if ( mandatoryContained != null ) {
				BinaryClassifier<Object> containerBinaryClassifier = new ContainerBinaryClassifier(this.getContainableStrategy(), mandatoryContained.get());
				
				if ( filter != null ) {
					filter = new AndBinaryClassifier<Object>(filter, containerBinaryClassifier);
				}
				else {
					filter = containerBinaryClassifier;
				}
			}
			
			if ( filter != null ) {
				this.filter = filter;
				this.mandatoryContained = mandatoryContained;
				this.mandatoryContainer = mandatoryContainer;
			}
			else {
				throw new NullPointerException();
			}
		}
		
		/**
		 * @post Devuelve la colección de contenibles
		 * 
		 * 		 Ésta implementación devuelve la colección de elementos original filtrada
		 */
		@Override
		public Set<T> elements() {
			return new FilteredSet<T>(AbstractContainableStrategySet.this.elements(), this.filter);
		}
		
		/**
		 * @post Devuelve la cantidad de elementos
		 * 
		 * 		 Ésta implementación devuelve el tamaño de la colección de elementos
		 */
		public int size() {
			return this.elements().size();
		}
		
		/**
		 * @post Devuelve si está vacía
		 * 
		 * 		 Ésta implementación devuelve si está vacía la colección de elementos
		 */
		public boolean isEmpty() {
			return this.elements().isEmpty();
		}
		
		/**
		 * @pre El elemento especificado tiene que pasar por el filtro
		 * @post Agrega el elemento especificado y devuelve si hubo cambio
		 * 
		 * 		 Ésta implementación usa el método add de la colección de elementos
		 */
		public boolean add(T element) {
			return this.elements().add(element);
		}
		
		/**
		 * @post Devuelve el iterador de elementos
		 * 
		 * 		 Ésta implementación devuelve el iterador de la colección de elementos
		 */
		@Override
		public Iterator<T> iterator() {
			return this.elements().iterator();
		}
		
		/**
		 * @pre Por lo menos una de las referencias no tiene que ser nula
		 * @post Crea la visión de subcolección con la referencia
		 * 		 al elemento contenedor y al contenible necesarios.
		 * 	 	 Si alguno de ellos es nulo, se considerará que no existe.
		 * 
		 * 		 Ésta implementación invoca al método de la colección original
		 * 		 si se puede reducir a una subcolección con mayor
		 * 		 exigencia (Condición más fuerte) por propiedad de transitividad,
		 * 		 caso contrario usa el método de la superclase.
		 */
		@Override
		protected ContainableStrategySet<T> subContainableStrategyCollectionFactory(InmutableReference<Object> mandatoryContainer, InmutableReference<Object> mandatoryContained) {
			if ( ( mandatoryContainer != null ) || ( mandatoryContained != null ) ) {
				boolean sourceReutilization=true; // ¿Se reutilizarán los métodos de la colección origen?
			
				InmutableReference<Object> subMandatoryContainer=null, subMandatoryContained=null;
			
				if ( ( this.mandatoryContainer != null ) && ( mandatoryContainer != null ) ) {
					if ( AbstractContainableStrategySet.this.getContainableStrategy().contains(mandatoryContainer.get(), this.mandatoryContainer.get()) ) {
						subMandatoryContainer = this.mandatoryContainer;
					}
					else if ( AbstractContainableStrategySet.this.getContainableStrategy().contains(this.mandatoryContainer.get(), mandatoryContainer.get()) ) {
						subMandatoryContainer = mandatoryContainer;
					}
					else {
						sourceReutilization = false;
					}
				}
				else {
					if ( mandatoryContainer != null ) {
						subMandatoryContainer = mandatoryContainer;
					}
					else {
						subMandatoryContainer = this.mandatoryContainer;
					}
				}
			
				if ( ( this.mandatoryContained != null ) && ( mandatoryContained != null ) ) {
					if ( this.getContainableStrategy().contains(mandatoryContained.get(), this.mandatoryContained.get()) ) {
						subMandatoryContained = mandatoryContained;
					}
					else if ( this.getContainableStrategy().contains(this.mandatoryContained.get(), mandatoryContained.get()) ) {
						subMandatoryContained = this.mandatoryContained;
					}
					else {
						sourceReutilization = false;
					}
				}
				else {
					if ( mandatoryContained != null ) {
						subMandatoryContained = mandatoryContained;
					}
					else {
						subMandatoryContained = this.mandatoryContained;
					}
				}
			
				final ContainableStrategySet<T> subContainableStrategyCollection;
				if ( sourceReutilization ) {
					subContainableStrategyCollection = AbstractContainableStrategySet.this.subContainableStrategyCollectionFactory(subMandatoryContainer, subMandatoryContained);
				}
				else {
					subContainableStrategyCollection = super.subContainableStrategyCollection(subMandatoryContainer, subMandatoryContained);
				}
			
				return subContainableStrategyCollection;
			}
			else {
				throw new NullPointerException();
			}
		}
	}
	
	/**
	 * @pre Por lo menos una de las referencias no tiene que ser nula
	 * @post Crea la visión de subcolección con la referencia
	 * 		 al elemento contenedor y al contenible necesarios.
	 * 	 	 Si alguno de ellos es nulo, se considerará que no existe
	 * 
	 * 		 Ésta implementación devuelve una instancia de SubContainableStrategyCollection
	 */
	protected ContainableStrategySet<T> subContainableStrategyCollectionFactory(InmutableReference<Object> mandatoryContainer, InmutableReference<Object> mandatoryContained) {
		return new SubContainableStrategySet(mandatoryContainer, mandatoryContained);
	}
	
	
	/**
	 * @post Devuelve una visión de la colección basada en estrategia
	 * 		 de contenibles que sólo contiene aquellos elementos
	 * 		 contenidos por el elemento contenedor especificado
	 * 
	 * 		 Si se intenta agregar un elemento que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 * 
	 * 		 Ésta implementación devuelve this.subContainableStrategyCollectionFactory(new InmutableReference<Object>(mandatoryContainer), null)
	 */
	public ContainableStrategySet<T> containedContainableStrategyCollection(Object mandatoryContainer) {
		return this.subContainableStrategyCollectionFactory(new InmutableReference<Object>(mandatoryContainer), null);
	}
	
	/**
	 * @post Devuelve una visión de la colección basada en estrategia
	 * 		 de contenibles que sólo contiene aquellos elementos
	 * 		 que contienen el elemento contenible especificado
	 * 
	 * 		 Si se intenta agregar un elemento que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 * 
	 * 		 Ésta implementación devuelve this.subContainableStrategyCollectionFactory(null, new InmutableReference<Object>(mandatoryContained))
	 */
	public ContainableStrategySet<T> containerContainableStrategyCollection(Object mandatoryContained) {
		return this.subContainableStrategyCollectionFactory(null, new InmutableReference<Object>(mandatoryContained));
	}
	
	/**
	 * @post Devuelve una visión de la colección basada en estrategia
	 * 		 de contenibles que sólo contiene aquellos elementos
	 * 		 que están contenidos por el elemento contenedor y que
	 * 		 contienen al elemento contenible especificados.
	 * 
	 * 		 Si se intenta agregar un elemento que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 * 
	 * 		 Ésta implementación devuelve this.subContainableStrategyCollectionFactory(new InmutableReference<Object>(mandatoryContainer), new InmutableReference<Object>(mandatoryContained))
	 */
	public ContainableStrategySet<T> subContainableStrategyCollection(Object mandatoryContainer, Object mandatoryContained) {
		return this.subContainableStrategyCollectionFactory(new InmutableReference<Object>(mandatoryContainer), new InmutableReference<Object>(mandatoryContained));
	}
}
