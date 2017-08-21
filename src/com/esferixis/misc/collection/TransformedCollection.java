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
package com.esferixis.misc.collection;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import com.esferixis.misc.iterator.TransformedIterator;
import com.esferixis.misc.relation.Function;

public class TransformedCollection<S, D> extends AbstractCollection<D> {
	protected Collection<S> sourceCollection;
	protected final Function<? super S, D> sourceToDestination;
	protected final Function<Object, S> destinationToSource;
	
	/**
	 * @pre El transformador de origen a destino no puede ser nulo
	 * @post Crea la colección transformada con el conjunto origen, el transformador de origen a destino,
	 * 		 y el transformador de destino a origen especificados
	 * @param 
	 * @return
	 */
	public TransformedCollection(Collection<S> sourceCollection, Function<? super S, D> sourceToDestination, Function<Object, S> destinationToSource) {
		this(sourceToDestination, destinationToSource);
		if ( ( sourceCollection != null ) ) {
			this.sourceCollection = sourceCollection;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @pre Ninguno de los parámetros puede ser nulo
	 * @post Crea la colección transformada con el conjunto origen sin especificar, el transformador de origen a destino,
	 * 		 y el transformador de destino a origen especificados
	 * @param 
	 * @return
	 */
	protected TransformedCollection(Function<? super S, D> sourceToDestination, Function<Object, S> destinationToSource) {
		if ( ( sourceToDestination != null ) ) {
			this.sourceToDestination = sourceToDestination;
			this.destinationToSource = destinationToSource;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	@Override
	public boolean add(D element) {
		if ( this.destinationToSource != null ) {
			return this.sourceCollection.add(this.destinationToSource.evaluate(element));
		}
		else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public void clear() {
		this.sourceCollection.clear();
	}
	
	@Override
	public boolean contains(Object other) {
		if ( this.destinationToSource != null ) {
			return this.sourceCollection.contains( this.destinationToSource.evaluate( other ) );
		}
		else {
			return super.contains(other);
		}
	}

	@Override
	public boolean isEmpty() {
		return this.sourceCollection.isEmpty();
	}

	@Override
	public Iterator<D> iterator() {
		return new TransformedIterator<S, D>(this.sourceCollection.iterator(), this.sourceToDestination);
	}

	@Override
	public boolean remove(Object other) {
		if ( this.destinationToSource != null ) {
			return this.sourceCollection.remove( this.destinationToSource.evaluate( other ) );
		}
		else {
			return super.remove(other);
		}
	}

	@Override
	public int size() {
		return this.sourceCollection.size();
	}
}
