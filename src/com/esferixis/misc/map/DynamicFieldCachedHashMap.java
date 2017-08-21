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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.esferixis.misc.dynamicFields.DynamicField;
import com.esferixis.misc.dynamicFields.DynamicFieldsContainerObject;
import com.esferixis.misc.reference.InmutableReference;

import java.util.AbstractSet;

public final class DynamicFieldCachedHashMap<K extends DynamicFieldsContainerObject, V> extends AbstractMap<K, V> implements Map<K, V> {
	private class ValueContainer {
		private V value;
		private boolean invalid;
	}
	
	private final DynamicField<ValueContainer> valueContainerField;
	private final Map<K, ValueContainer> backingMap;
	
	private final Set<java.util.Map.Entry<K, V>> entrySet;
	
	public DynamicFieldCachedHashMap() {
		this.valueContainerField = new DynamicField<ValueContainer>();
		this.backingMap = new HashMap<K, ValueContainer>();
		
		this.entrySet = new AbstractSet<java.util.Map.Entry<K, V>>() {

			@Override
			public Iterator<java.util.Map.Entry<K, V>> iterator() {
				return new Iterator<java.util.Map.Entry<K, V>>() {
					private final Iterator<java.util.Map.Entry<K, ValueContainer>> backingIterator = DynamicFieldCachedHashMap.this.backingMap.entrySet().iterator();
					private ValueContainer lastValueContainer = null;
					
					@Override
					public boolean hasNext() {
						return this.backingIterator.hasNext();
					}

					@Override
					public java.util.Map.Entry<K, V> next() {
						final Entry<K, ValueContainer> backingEntry = this.backingIterator.next();
						lastValueContainer = backingEntry.getValue();
						
						return new Entry<K, V>() {

							@Override
							public K getKey() {
								return backingEntry.getKey();
							}

							@Override
							public V getValue() {
								return backingEntry.getValue().value;
							}

							@Override
							public V setValue(V value) {
								V oldValue = backingEntry.getValue().value;
								backingEntry.getValue().value = value;
								return oldValue;
							}
							
						};
					}

					@Override
					public void remove() {
						if ( this.lastValueContainer != null ) {
							this.lastValueContainer.value = null;
							this.lastValueContainer.invalid = true;
							
							this.backingIterator.remove();
							
							this.lastValueContainer = null;
						}
						else {
							throw new IllegalStateException();
						}
					}
					
				};
			}

			@Override
			public int size() {
				return DynamicFieldCachedHashMap.this.backingMap.size();
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		for ( ValueContainer eachValueContainer : this.backingMap.values() ) {
			eachValueContainer.value = null;
			eachValueContainer.invalid = true;
		}
		
		this.backingMap.clear();
	}

	/**
	 * @post Devuelve el contenedor de valor para la clave especificada,
	 * 		 si no existe o es inválido, devuelve null
	 */
	private ValueContainer getValueContainerByKey(Object keyObject) {
		ValueContainer valueContainer;
		
		if ( keyObject != null ) {
			if ( keyObject instanceof DynamicFieldsContainerObject ) {
				DynamicFieldsContainerObject key = (DynamicFieldsContainerObject) keyObject;
				
				InmutableReference<ValueContainer> valueContainerReference = key.dynamicFieldsContainer().get(this.valueContainerField);
				
				if ( ( valueContainerReference != null ) && ( valueContainerReference.get() != null ) ) {
					if ( !valueContainerReference.get().invalid ) {
						valueContainer = valueContainerReference.get();
					}
					else {
						key.dynamicFieldsContainer().remove(this.valueContainerField);
						valueContainer = null;
					}
				}
				else {
					valueContainer = this.backingMap.get(key);
					
					if ( valueContainer != null ) {
						key.dynamicFieldsContainer().add(this.valueContainerField, valueContainer);
					}
				}
			}
			else  {
				valueContainer = null;
			}
		}
		else {
			valueContainer = this.backingMap.get(null);
		}
		
		if ( ( valueContainer != null ) && ( valueContainer.invalid ) ) {
			valueContainer = null;
		}
		
		return valueContainer;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return (this.getValueContainerByKey(key) != null);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.entrySet;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public V get(Object key) {
		final ValueContainer valueContainer = this.getValueContainerByKey(key);
		return ( ( valueContainer != null ) ? valueContainer.value : null );
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.backingMap.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value) {
		ValueContainer valueContainer;
		
		if ( key != null ) {
			InmutableReference<ValueContainer> valueContainerReference = key.dynamicFieldsContainer().get(this.valueContainerField);
			
			if ( ( valueContainerReference != null ) && ( valueContainerReference.get() != null ) ) {
				valueContainer = valueContainerReference.get();
			}
			else {
				valueContainer = this.backingMap.get(key);
				
				if ( valueContainer == null ) {
					valueContainer = new ValueContainer();
					this.backingMap.put(key, valueContainer);
				}
				
				key.dynamicFieldsContainer().add(this.valueContainerField, valueContainer);
			}
		}
		else {
			valueContainer = this.backingMap.get(null);
			
			if ( valueContainer == null ) {
				valueContainer = new ValueContainer();
				this.backingMap.put(null, valueContainer);
			}
		}
		
		valueContainer.value = value;
		valueContainer.invalid = false;
		
		return value;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public V remove(Object keyObject) {
		ValueContainer valueContainer;
		V oldValue;
		
		if ( keyObject != null ) {
			if ( keyObject instanceof DynamicFieldsContainerObject ) {
				DynamicFieldsContainerObject key = (DynamicFieldsContainerObject) keyObject;
				
				InmutableReference<ValueContainer> valueContainerReference = key.dynamicFieldsContainer().get(this.valueContainerField);
				
				if ( ( valueContainerReference != null ) && ( valueContainerReference.get() != null ) ) {
					key.dynamicFieldsContainer().remove(this.valueContainerField);
				}
				
				valueContainer = this.backingMap.remove(key);
			}
			else  {
				valueContainer = null;
			}
		}
		else {
			valueContainer = this.backingMap.remove(null);
		}
		
		if ( valueContainer != null ) {
			oldValue = valueContainer.value;
			valueContainer.value = null;
			valueContainer.invalid = true;
		}
		else {
			oldValue = null;
		}
		
		// TODO Auto-generated method stub
		return oldValue;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return this.backingMap.size();
	}
}
