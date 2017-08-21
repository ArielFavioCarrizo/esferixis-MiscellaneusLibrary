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
package com.esferixis.misc.containablestrategy.collection;

/**
 * Colección abstracta desde la estrategia de contenibles.
 * Es una implementación "esqueleto" para facilitar la implementación de ésta interface.
 * 
 * Cualquiera de los métodos no abstractos pueden ser sobreescritos para una implementación más eficiente
 */
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import com.esferixis.misc.binaryclassifier.AndBinaryClassifier;
import com.esferixis.misc.binaryclassifier.BinaryClassifier;
import com.esferixis.misc.collection.FilteredCollection;
import com.esferixis.misc.containablestrategy.ContainableStrategy;
import com.esferixis.misc.containablestrategy.binaryclassifier.ContainedBinaryClassifier;
import com.esferixis.misc.containablestrategy.binaryclassifier.ContainerBinaryClassifier;
import com.esferixis.misc.iterator.FilteredIterator;
import com.esferixis.misc.reference.InmutableReference;



public abstract class AbstractContainableStrategyCollection<T> implements ContainableStrategyCollection<T> {
	protected final ContainableStrategy containableStrategy;
	protected final int containableStrategyHashCodeBase;
	
	/**
	 * @pre La estrategia no puede ser nula
	 * @post Crea la colección de estrategia de contenibles con la estrategia especificada
	 */
	public AbstractContainableStrategyCollection(ContainableStrategy containableStrategy) {
		if ( containableStrategy != null ) {
			this.containableStrategy = containableStrategy;
			this.containableStrategyHashCodeBase = this.containableStrategy.hashCode() * 31;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve la estrategia de contenibles
	 */
	@Override
	public ContainableStrategy getContainableStrategy() {
		return this.containableStrategy;
	}
	
	/**
	 * @post Verifica que la colección de estrategia no sea nula
	 * 		 y tenga una estrategia de contenibles igual
	 * 
	 *		 Si es nula lanza NullPointerException y si la estrategia de contenibles
	 *		 es distinta devuelve IllegalArgumentException
	 */
	protected final void checkContainableStrategyCollection(ContainableStrategyCollection<?> collection) {
		if ( collection != null ) {
			if ( !collection.getContainableStrategy().equals(this.containableStrategy) ) {
				throw new IllegalArgumentException("Containable Strategy mismatch");
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * Colección de elementos
	 * 
	 * Implementación que extiende de AbstractCollection
	 * haciendo uso de las implementaciones de los métodos implementados en la colección
	 * de estrategia
	 */
	protected class ElementsCollection extends AbstractCollection<T> {

		/**
		 * @post Crea la visión de elementos
		 */
		public ElementsCollection() {
			
		}
		
		@Override
		public boolean add(T element) {
			return AbstractContainableStrategyCollection.this.add(element);
		}
		
		@Override
		public Iterator<T> iterator() {
			return AbstractContainableStrategyCollection.this.iterator();
		}

		@Override
		public int size() {
			return AbstractContainableStrategyCollection.this.size();
		}
		
	}
	
	/**
	 * @post Devuelve la colección de contenibles
	 * 
	 * 		 Ésta implementación devuelve una instancia de ElementsCollection
	 */
	@Override
	public Collection<T> elements() {
		return new ElementsCollection();
	}
	
	/**
	 * Colección de elementos raices
	 * Ésta implementación se basa en la subcolección basada en estrategia de contenibles
	 */
	protected class RootElementsCollection extends AbstractCollection<T> {
		
		/**
		 * @post Crea la visión de elementos raices
		 */
		public RootElementsCollection() {
			
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
			ContainableStrategyCollection<T> containedVision = AbstractContainableStrategyCollection.this.containedContainableStrategyCollection(element);
			if ( !containedVision.isEmpty() ) { // Si hay elementos contenidos por el especificado
				ContainableStrategyCollection<T> containerContainedVision = containedVision.containerContainableStrategyCollection(element);
				// Y dentro de ellos no hay elementos que contengan al especificado
				if ( containerContainedVision.isEmpty() ) {
					// Entonces no es raíz
					throw new IllegalArgumentException("Attemped to add an nonroot element");
				}
			}
			
			return AbstractContainableStrategyCollection.this.add(element);
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
			if ( AbstractContainableStrategyCollection.this.elements().contains( element ) ) {
				final ContainableStrategyCollection<T> containerVision = AbstractContainableStrategyCollection.this.containerContainableStrategyCollection(element);
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
					final ContainableStrategyCollection<T> containerVision = AbstractContainableStrategyCollection.this.containerContainableStrategyCollection(element);
					/**
					 * La cantidad de elementos que lo contienen tiene que ser igual
					 * a la cantidad de elementos entre ellos que están contenidos por el especificado,
					 * caso contrario hay algunos que contienen al especificado pero no están
					 * contenidos por éste y por lo tanto el elemento dado no es raíz
					 */
					return ( containerVision.size() == containerVision.containedContainableStrategyCollection(element).size() );
				}
				
			}
			return new FilteredIterator<T>(AbstractContainableStrategyCollection.this.iterator(), new ExclusionClassifier());
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
	 * @post Devuelve una visión de la colección de elementos que sólo contiene aquellos
	 * 		 elementos que no están contenidos por ningún otro salvo por aquellos que estén
	 * 		 contenidos,
	 * 		 o sea los elementos raices, asegurando que dichos elementos
	 * 		 contienen todos los elementos por la propiedad de transitividad.
	 * 		 Si se intenta agregar un elemento que está contenido por otros que no lo contienen
	 * 		 lanza IllegalArgumentException
	 * 
	 * 		 Ésta implementación devuelve una instancia de RootElementsCollection
	 */
	public Collection<T> getRootElements() {
		return new RootElementsCollection();
	}
	
	/**
	 * @post Devuelve si contiene el elemento especificado
	 * 
	 * 		 Ésta implementación devuelve !this.containerContainableStrategyCollection(containable).isEmpty();
	 */
	@Override
	public boolean contains(Object containable) {
		return !this.containerContainableStrategyCollection(containable).isEmpty();
	}

	/**
	 * @pre La colección de estrategia no puede ser nula y la estrategia de contenibles tiene que ser la misma
	 * @post Devuelve si contiene todos los elementos de la colección especificada
	 * 		 
	 * 		 Ésta implementación recorre la colección con el iterador
	 * 		 buscando elementos no contenidos.
	 * 		 Si encuentra algún elemento no contenido devuelve false, caso contrario true
	 * 
	 *   	 Si la colección basada en contenibles especificada implementa FastRootElementsAccess
	 *   	 itera la colección de elementos raices caso contrario itera toda la colección
	 *   	 basada en contenibles
	 */
	@Override
	public boolean containsAll(ContainableStrategyCollection<?> elements) {
		this.checkContainableStrategyCollection(elements);
		
		Iterator<?> iterator;
		if ( elements instanceof FastRootElementsAccess ) {
			iterator = elements.getRootElements().iterator();
		}
		else {
			iterator = elements.iterator();
		}
		
		boolean contained = true;
		while ( iterator.hasNext() && contained ) {
			contained = this.contains(iterator.next());
		}
		
		return contained;
	}
	
	/**
	 * @post Devuelve la cantidad de elementos
	 */
	@Override
	public abstract int size();
	
	/**
	 * @post Devuelve si está vacía
	 * 
	 * 	 	 Ésta implementación consulta el tamaño para tal fin
	 */
	@Override
	public boolean isEmpty() {
		return (this.size() == 0);
	}
	
	/**
	 * @post Agrega el elemento especificado
	 * 
	 * 		 Ésta implementación siempre lanza la excepción UnsupportedOperationException
	 */
	@Override
	public boolean add(T element) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @post Agrega todos los elementos de la colección de estrategia de contenibles especificada y devuelve
	 * 		 si hubo algún cambio
	 * 		 
	 * 		 Ésta implementación itera la colección de contenibles especificada agregando cada elemento
	 * 		 con el método add
	 */
	@Override
	public boolean addAll(ContainableStrategyCollection<? extends T> elements) {
		boolean changed = false;
		
		this.checkContainableStrategyCollection(elements);
		
		for ( T eachElement : elements ) {
			changed |= this.add(eachElement);
		}
		
		return changed;
	}

	/**
	 * @post Quita todos los elementos contenidos por el elemento especificado y devuelve si ha cambiado
	 * 		 la colección
	 * 
	 * 		 Ésta implementación obtiene la visión de los contenidos por el elemento
	 * 		 especificado, luego evalúa si está vacía, si no lo está
	 * 		 la vacía y devuelve si no estaba vacía
	 */
	@Override
	public boolean remove(Object container) {
		final ContainableStrategyCollection<T> containedContainableStrategyCollection = this.containedContainableStrategyCollection(container);
		boolean isEmpty = containedContainableStrategyCollection.isEmpty();
		if ( !isEmpty ) {
			containedContainableStrategyCollection.clear();
		}
		return !isEmpty;
	}

	/**
	 * @pre La colección de estrategia no puede ser nula y la estrategia de contenibles tiene que ser la misma
	 * @post Quita todos los elementos contenidos por los elementos de la colección de estrategia especificada y devuelve
	 * 		 si hubo cambio
	 * 
	 * 		 Ésta implementación itera la colección especificada removiendo cada elemento contenido
	 * 		 en la colección de estrategia especificada
	 */
	@Override
	public boolean removeAll(ContainableStrategyCollection<?> elements) {
		this.checkContainableStrategyCollection(elements);
		
		Iterator<T> iterator = this.iterator();
		boolean changed = false;
		
		while ( iterator.hasNext() ) {
			// Si está contenido
			if ( elements.contains(iterator.next()) ) {
				// Removerlo
				iterator.remove();
				
				// Y notificar el cambio
				changed = true;
			}
		}
		
		return changed;
	}

	/**
	 * @pre La colección de estrategia no puede ser nula y la estrategia de contenibles tiene que ser la misma
	 * @post Elimina todos aquellos elementos que no están contenidos en los elementos de la colección
	 * 		 especificada y devuelve si hubo cambio
	 * 
	 * 		 Ésta implementación itera la colección especificada removiendo cada elemento
	 * 		 no contenido en la colección de estrategia especificada
	 */
	@Override
	public boolean retainAll(ContainableStrategyCollection<?> elements) {
		this.checkContainableStrategyCollection(elements);
		
		Iterator<T> iterator = this.iterator();
		boolean changed = false;
		
		while ( iterator.hasNext() ) {
			// Si no está contenido
			if ( !elements.contains(iterator.next()) ) {
				// Removerlo
				iterator.remove();
				
				// Y notificar el cambio
				changed = true;
			}
		}
		
		return changed;
	}
	
	/**
	 * @post Quita todos los elementos de la colección basada en estrategia de contenibles
	 * 
	 * 		 Ésta implementación itera la colección quitando cada elemento
	 */
	@Override
	public void clear() {
		final Iterator<T> iterator = this.iterator();
		
		while ( iterator.hasNext() ) {
			iterator.next();
			iterator.remove();
		}
	}

	/**
	 * @post Devuelve un array con los elementos
	 * 
	 * 		 Ésta implementación invoca la implementación de la colección de elementos
	 */
	@Override
	public Object[] toArray() {
		return this.elements().toArray();
	}

	/**
	 * @post Devuelve el array con los elementos de la colección de contenibles si tiene tamaño suficiente
	 * 		 y si tiene un elemento que le sigue inmediatamente al fin de la colección lo deja en "null".
	 * 		 Si los elementos no entran en el array crea un nuevo array.
	 * 
	 * 		 Ésta implementación invoca la implementación de la colección de elementos
	 */
	@Override
	public <V> V[] toArray(V[] array) {
		return this.elements().toArray(array);
	}
	
	/**
	 * @post Devuelve el iterador de elementos
	 */
	@Override
	public abstract Iterator<T> iterator();
	
	/**
	 * Subcolección basada en estrategia de contenibles
	 * 
	 * Ésta implementación extiende de la colección basada en estrategia
	 * de contenibles abstracta y se basa en la colección de elementos
	 * filtrada y un clasificador binario de condición necesaria de pertenencia
	 */
	protected class SubContainableStrategyCollection extends AbstractContainableStrategyCollection<T>  {
		protected final BinaryClassifier<Object> filter;
		
		protected final InmutableReference<Object> mandatoryContainer;
		protected final InmutableReference<Object> mandatoryContained;
		
		/**
		 * @pre Por lo menos una de las referencias de contenedor y contenibles requeridos no tiene que ser nula
		 * @post Crea la base de visión de subcolección con la referencia
		 * 		 al elemento contenedor y al contenible necesarios.
		 * 	 	 Si alguno de ellos es nulo, se considerará que no existe
		 */
		public SubContainableStrategyCollection(InmutableReference<Object> mandatoryContainer, InmutableReference<Object> mandatoryContained) {
			super(AbstractContainableStrategyCollection.this.getContainableStrategy());
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
		 * @post Devuelve el contenedor requerido
		 */
		public InmutableReference<Object> getMandatoryContainer() {
			return this.mandatoryContainer;
		}
		
		/**
		 * @post Devuelve el contenible requerido
		 */
		public InmutableReference<Object> getMandatoryContained() {
			return this.mandatoryContained;
		}
		
		/**
		 * @post Devuelve el filtro
		 */
		public BinaryClassifier<Object> getFilter() {
			return this.filter;
		}
		
		/**
		 * @post Devuelve la colección de contenibles
		 * 
		 * 		 Ésta implementación devuelve la colección de elementos original filtrada
		 */
		public Collection<T> elements() {
			return new FilteredCollection<T>(AbstractContainableStrategyCollection.this.elements(), this.filter);
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
		protected ContainableStrategyCollection<T> subContainableStrategyCollectionFactory(InmutableReference<Object> mandatoryContainer, InmutableReference<Object> mandatoryContained) {
			if ( ( mandatoryContainer != null ) || ( mandatoryContained != null ) ) {
				boolean sourceReutilization=true; // ¿Se reutilizarán los métodos de la colección origen?
			
				InmutableReference<Object> subMandatoryContainer=null, subMandatoryContained=null;
			
				if ( ( this.mandatoryContainer != null ) && ( mandatoryContainer != null ) ) {
					if ( AbstractContainableStrategyCollection.this.getContainableStrategy().contains(mandatoryContainer.get(), this.mandatoryContainer.get()) ) {
						subMandatoryContainer = this.mandatoryContainer;
					}
					else if ( AbstractContainableStrategyCollection.this.getContainableStrategy().contains(this.mandatoryContainer.get(), mandatoryContainer.get()) ) {
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
			
				final ContainableStrategyCollection<T> subContainableStrategyCollection;
				if ( sourceReutilization ) {
					subContainableStrategyCollection = AbstractContainableStrategyCollection.this.subContainableStrategyCollectionFactory(subMandatoryContainer, subMandatoryContained);
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
	protected ContainableStrategyCollection<T> subContainableStrategyCollectionFactory(InmutableReference<Object> mandatoryContainer, InmutableReference<Object> mandatoryContained) {
		return new SubContainableStrategyCollection(mandatoryContainer, mandatoryContained);
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
	public ContainableStrategyCollection<T> containedContainableStrategyCollection(Object mandatoryContainer) {
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
	public ContainableStrategyCollection<T> containerContainableStrategyCollection(Object mandatoryContained) {
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
	public ContainableStrategyCollection<T> subContainableStrategyCollection(Object mandatoryContainer, Object mandatoryContained) {
		return this.subContainableStrategyCollectionFactory(new InmutableReference<Object>(mandatoryContainer), new InmutableReference<Object>(mandatoryContained));
	}
	
	/**
	 * @post Devuelve una representación en cadena de carácteres de la colección basada
	 * 		 en estrategia de contenibles
	 * 
	 * 		 Ésta implementación usa la implementación de la colección de elementos
	 */
	@Override
	public String toString() {
		return this.elements().toString();
	}
}
