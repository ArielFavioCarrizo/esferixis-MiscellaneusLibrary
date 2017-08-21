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
package com.esferixis.misc.map;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.esferixis.misc.collection.TransformedCollection;
import com.esferixis.misc.collection.set.TransformedSet;
import com.esferixis.misc.relation.Function;

public class TransformedMap<SK, SV, DK, DV> extends AbstractMap<DK, DV> {	
	protected Map<SK, SV> sourceMap;
	protected final Function<? super SK, DK> sourceToDestinationKeyTransformer;
	protected final Function<Object, SK> destinationToSourceKeyTransformer;
	protected final Function<? super SV, DV> sourceToDestinationValueTransformer;
	protected final Function<Object, SV> destinationToSourceValueTransformer;
	
	/**
	 * @pre El mapa original, el transformador de clave y el de valor de origen a destino no pueden ser nulos
	 * @post Crea un transformador de mapa con el mapa original,
	 * 		 el transformador de clave de origen a destino,
	 * 		 de destino a origen, y el transformador de valor
	 * 		 de origen a destino y de destino a origen especificados
	 */
	public TransformedMap(Map<SK, SV> sourceMap,
			Function<? super SK, DK> sourceToDestinationKeyTransformer,
			Function<Object, SK> destinationToSourceKeyTransformer,
			Function<? super SV, DV> sourceToDestinationValueTransformer,
			Function<Object, SV> destinationToSourceValueTransformer) {
		this(sourceToDestinationKeyTransformer, destinationToSourceKeyTransformer, sourceToDestinationValueTransformer, destinationToSourceValueTransformer);
		if ( sourceMap != null ) {
			this.sourceMap = sourceMap;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @pre El transformador de clave y el de valor de origen a destino no pueden ser nulos
	 * @post Crea un transformador de mapa con el mapa original sin especificar,
	 * 		 el transformador de clave de origen a destino,
	 * 		 de destino a origen, y el transformador de valor
	 * 		 de origen a destino y de destino a origen especificados
	 */
	protected TransformedMap(
			Function<? super SK, DK> sourceToDestinationKeyTransformer,
			Function<Object, SK> destinationToSourceKeyTransformer,
			Function<? super SV, DV> sourceToDestinationValueTransformer,
			Function<Object, SV> destinationToSourceValueTransformer) {
			if ( 
					( sourceMap != null ) &&
					( sourceToDestinationKeyTransformer != null ) &&
					( sourceToDestinationValueTransformer != null )
			) {
				this.sourceToDestinationKeyTransformer = sourceToDestinationKeyTransformer;
				this.destinationToSourceKeyTransformer = destinationToSourceKeyTransformer;
				this.sourceToDestinationValueTransformer = sourceToDestinationValueTransformer;
				this.destinationToSourceValueTransformer = destinationToSourceValueTransformer;
			}
			else {
				throw new NullPointerException();
			}
	}
	
	/**
	 * @pre El mapa original, el transformador de clave y el de valor de origen a destino no pueden ser nulos
	 * @post Crea un transformador de mapa con el mapa original,
	 * 		 el transformador de clave y de valor de origen a destino especificados
	 */
	public TransformedMap(Map<SK, SV> sourceMap,
			Function<? super SK, DK> sourceToDestinationKeyTransformer,
			Function<? super SV, DV> sourceToDestinationValueTransformer) {
		this(sourceMap, sourceToDestinationKeyTransformer, null, sourceToDestinationValueTransformer, null);
	}
	
	/**
	 * @pre El transformador de clave y el de valor de origen a destino no pueden ser nulos
	 * @post Crea un transformador de mapa con el mapa original sin especificar,
	 * 		 el transformador de clave y de valor de origen a destino especificados
	 */
	protected TransformedMap(Function<? super SK, DK> sourceToDestinationKeyTransformer,
			Function<? super SV, DV> sourceToDestinationValueTransformer) {
		this(sourceToDestinationKeyTransformer, null, sourceToDestinationValueTransformer, null);
	}
	
	@Override
	public void clear() {
		this.sourceMap.clear();
	}

	/**
	 * @post Devuelve si contiene la clave especificada,
	 * 		 si no hay transformador de clave de destino a origen usa la implementación
	 * 		 de AbstractMap, caso contrario usa la implementación del mapa origen
	 * @param key
	 * @return
	 */
	@Override
	public boolean containsKey(Object key) {
		if ( this.destinationToSourceKeyTransformer != null ) {
			return this.sourceMap.containsKey( this.destinationToSourceKeyTransformer.evaluate( key ) );
		}
		else {
			return super.containsKey(key);
		}
	}

	/**
	 * @post Devuelve si contiene el valor especificado,
	 * 		 si no hay transformador de valor de destino a origen usa la implementación
	 * 		 de AbstractMap, caso contrario usa la implementación del mapa origen
	 * @param key
	 * @return
	 */
	@Override
	public boolean containsValue(Object value) {
		if ( this.destinationToSourceValueTransformer != null ) {
			return this.sourceMap.containsValue( this.destinationToSourceValueTransformer.evaluate( value ) );
		}
		else {
			return super.containsValue(value);
		}
	}

	/**
	 * @post Devuelve el conjunto de entradas,
	 * 	 	 si hay transformador de clave y de valor de destino a origen
	 * 		 usa la implementación del mapa origen, caso contrario del AbstractMap
	 * @return
	 */
	@Override
	public Set<java.util.Map.Entry<DK, DV>> entrySet() {
		class SourceToDestinationEntryTransformer implements Function< Entry<SK, SV >, Entry<DK, DV> > {

			@Override
			public java.util.Map.Entry<DK, DV> evaluate(
					java.util.Map.Entry<SK, SV> sourceElement) {
				return new SimpleEntry<DK, DV>( TransformedMap.this.sourceToDestinationKeyTransformer.evaluate(sourceElement.getKey()), TransformedMap.this.sourceToDestinationValueTransformer.evaluate(sourceElement.getValue()) );
			}
			
		}
		
		class DestinationToSourceEntryTransformer implements Function< Object, Entry<SK, SV> > {

			@Override
			public Entry<SK, SV> evaluate(Object sourceElement) {
				Entry<?, ?> sourceEntry = (java.util.Map.Entry<?, ?>) sourceElement;
				return new SimpleEntry<SK, SV>( TransformedMap.this.destinationToSourceKeyTransformer.evaluate(sourceEntry.getKey()), TransformedMap.this.destinationToSourceValueTransformer.evaluate(sourceEntry.getValue()) );
			}
			
		}
		
		if ( ( this.destinationToSourceKeyTransformer != null ) && ( this.destinationToSourceValueTransformer != null ) ) {
			return new TransformedSet< Entry<SK, SV>, Entry<DK, DV> >(this.sourceMap.entrySet(), new SourceToDestinationEntryTransformer(), new DestinationToSourceEntryTransformer());
		}
		else {
			return new TransformedSet< Entry<SK, SV>, Entry<DK, DV> >(this.sourceMap.entrySet(), new SourceToDestinationEntryTransformer(), null);
		}
	}

	/**
	 * @post Devuelve si contiene la clave especificada
	 * 		 Si hay transformador de clave de destino a origen usa la implementación del mapa
	 * 		 origen, caso contrario usa la implementación del AbstractMap
	 * @param key
	 * @return
	 */
	@Override
	public DV get(Object key) {
		if ( this.destinationToSourceKeyTransformer != null ) {
			return this.sourceToDestinationValueTransformer.evaluate( this.sourceMap.get( this.destinationToSourceKeyTransformer.evaluate(key) ) );
		}
		else {
			return super.get(key);
		}
	}

	/**
	 * @post Devuelve si está vacío usando la implementación del mapa original
	 */
	@Override
	public boolean isEmpty() {
		return this.sourceMap.isEmpty();
	}

	/**
	 * @post Devuelve una visión del conjunto de claves
	 * 		 Si hay transformador de clave de destino a origen usa la implementación del mapa
	 * 		 original caso contrario del AbstractMap
	 */
	@Override
	public Set<DK> keySet() {
		if ( this.destinationToSourceKeyTransformer != null ) {
			return new TransformedSet<SK, DK>(this.sourceMap.keySet(), this.sourceToDestinationKeyTransformer, this.destinationToSourceKeyTransformer);
		}
		else {
			return super.keySet();
		}
	}

	/**
	 * @pre Tiene que haber transformador de clave y de valor de destino a origen
	 * @post Asocia la clave con el valor especificados usando la implementación del mapa original
	 */
	@Override
	public DV put(DK key, DV value) {
		if ( ( this.destinationToSourceKeyTransformer != null ) && ( this.destinationToSourceValueTransformer != null ) ) {
			return this.sourceToDestinationValueTransformer.evaluate( this.sourceMap.put( this.destinationToSourceKeyTransformer.evaluate(key), this.destinationToSourceValueTransformer.evaluate(value) ) );
		}
		else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * @post Borra la clave especificada
	 * 		 Si hay transformador de clave de destino a origen usa la implementación del mapa original
	 * 		 caso contrario del AbstractMap
	 * @param key
	 * @return
	 */
	@Override
	public DV remove(Object key) {
		if ( this.destinationToSourceKeyTransformer != null ) {
			return this.sourceToDestinationValueTransformer.evaluate( this.sourceMap.remove( this.destinationToSourceKeyTransformer.evaluate( key ) ) );
		}
		else {
			return super.remove(key);
		}
	}

	/**
	 * @post Devuelve el tamaño del mapa usando la implementación del mapa original
	 * @return
	 */
	@Override
	public int size() {
		return this.sourceMap.size();
	}

	/**
	 * @post Devuelve la colección de valores
	 * 		 Si hay transformador de destino a origen de clave y de valor usa la implementación del
	 * 		 mapa original caso contrario del AbstractMap
	 * @return
	 */
	@Override
	public Collection<DV> values() {
		if ( ( this.destinationToSourceKeyTransformer != null ) && ( this.destinationToSourceValueTransformer != null ) ) {
			return new TransformedCollection<SV, DV>(this.sourceMap.values(), this.sourceToDestinationValueTransformer, this.destinationToSourceValueTransformer );
		}
		else {
			return super.values();
		}
	}

}
