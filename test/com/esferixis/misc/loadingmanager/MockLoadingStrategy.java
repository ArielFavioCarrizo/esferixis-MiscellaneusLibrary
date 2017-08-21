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

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.esferixis.misc.loader.AbstractDataLoader;
import com.esferixis.misc.loader.DataLoader;
import com.esferixis.misc.loadingmanager.LoadingStrategy;

import sun.font.CreatedFontTracker;

public final class MockLoadingStrategy implements LoadingStrategy<MockLoadableElement> {
	private class MockSourceProfile {
		private final Set<MockSource> loadedUsers;
		private final Collection<Observer> observers;
		
		/**
		 * @post Crea el perfil
		 */
		public MockSourceProfile() {
			this.loadedUsers = new HashSet<MockSource>();
			this.observers = new LinkedHashSet<Observer>();
		}
	}
	
	private final Map<MockSource, MockSourceProfile> profilesBySources;
	
	/**
	 * @post Crea la estrategia
	 */
	public MockLoadingStrategy() {
		this.profilesBySources = new HashMap<MockSource, MockSourceProfile>();
	}

	/**
	 * @post Devuelve si están cargados los elementos especificados
	 */
	public boolean hasLoaded(Collection<MockLoadableElement> elements) {
		boolean loaded = true;
		Iterator<MockLoadableElement> elementsIterator = elements.iterator();
		
		while ( elementsIterator.hasNext() && loaded ) {
			loaded = this.profilesBySources.containsKey(elementsIterator.next().getMockSource());
		}
		
		return loaded;
	}
	
	/**
	 * @post Devuelve si están cargados los elementos especificados
	 */
	public boolean hasLoaded(MockLoadableElement... elements) {
		return this.hasLoaded(Arrays.asList(elements));
	}
	
	/* (non-Javadoc)
	 * @see com.esferixis.misc.garbagecollector.LoadingStrategy#getOccupiedSpace(java.lang.Object)
	 */
	@Override
	public long getOccupiedSpace(MockLoadableElement element) {
		return element.getMockSource().getSize();
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.garbagecollector.LoadingStrategy#load(com.esferixis.misc.dynamicFields.DynamicFieldsContainerObject)
	 */
	@Override
	public void load(MockLoadableElement element) {
		final MockSource elementSource = element.getMockSource();
		
		if ( !this.profilesBySources.containsKey(elementSource) ) {
			for ( MockSource eachDependencySource : elementSource.getDependencies() ) {
				if ( !this.profilesBySources.containsKey(eachDependencySource) ) {
					throw new IllegalStateException("Expected loaded dependency with source: " + eachDependencySource);
				}
			}
			
			this.profilesBySources.put(elementSource, new MockSourceProfile());
			
			for ( MockSource eachDependencySource : elementSource.getDependencies() ) {
				final MockSourceProfile profile = this.profilesBySources.get(eachDependencySource);
				profile.loadedUsers.add(elementSource);
				
				if ( profile.loadedUsers.size() == 1 ) {
					for ( Observer eachObserver : profile.observers ) {
						eachObserver.notifyHasLoadedUsers();
					}
				}
			}
		}
		else {
			throw new IllegalStateException("Element " + element + " has been loaded");
		}
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.garbagecollector.LoadingStrategy#unload(com.esferixis.misc.dynamicFields.DynamicFieldsContainerObject)
	 */
	@Override
	public void unload(MockLoadableElement element) {
		final MockSource elementSource = element.getMockSource();
		
		final MockSourceProfile profile = this.profilesBySources.get(elementSource);
		
		if ( profile != null ) {
			if ( profile.loadedUsers.isEmpty() ) {
				for ( MockSource eachDependencySource : elementSource.getDependencies() ) {
					final MockSourceProfile eachDependencyProfile = this.profilesBySources.get(eachDependencySource);
					
					eachDependencyProfile.loadedUsers.remove(elementSource);
					
					if ( eachDependencyProfile.loadedUsers.isEmpty() ) {
						for ( Observer eachObserver : eachDependencyProfile.observers ) {
							eachObserver.notifyHasNotLoadedUsers();
						}
					}
				}
				
				this.profilesBySources.remove(elementSource);
			}
			else {
				throw new IllegalStateException("Element " + element + " has loaded users");
			}
		}
		else {
			throw new IllegalStateException("Element " + element + " hasn't been loaded");
		}
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.garbagecollector.LoadingStrategy#isLoaded(com.esferixis.misc.dynamicFields.DynamicFieldsContainerObject)
	 */
	@Override
	public boolean isLoaded(MockLoadableElement element) {
		return this.profilesBySources.containsKey(element.getMockSource());
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.garbagecollector.LoadingStrategy#attachObserver(com.esferixis.misc.dynamicFields.DynamicFieldsContainerObject, com.esferixis.misc.garbagecollector.LoadingStrategy.Observer)
	 */
	@Override
	public void attachObserver(final MockLoadableElement element,
			final com.esferixis.misc.loadingmanager.LoadingStrategy.Observer observer) {
		final MockSourceProfile profile = this.profilesBySources.get(element.getMockSource());
		
		if ( profile != null) {
			if ( !profile.observers.add(observer) ) {
				throw new IllegalStateException("Observer is attached");
			}
		}
		else {
			throw new IllegalStateException("Expected loaded element");
		}
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.garbagecollector.LoadingStrategy#detachObserver(com.esferixis.misc.dynamicFields.DynamicFieldsContainerObject, com.esferixis.misc.garbagecollector.LoadingStrategy.Observer)
	 */
	@Override
	public void detachObserver(MockLoadableElement element,
			com.esferixis.misc.loadingmanager.LoadingStrategy.Observer observer) {
		final MockSourceProfile profile = this.profilesBySources.get(element.getMockSource());
		
		if ( profile != null) {
			if ( !profile.observers.remove(observer) ) {
				throw new IllegalStateException("Observer isn't attached");
			}
		}
		else {
			throw new IllegalStateException("Expected loaded element");
		}
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.garbagecollector.LoadingStrategy#getDependencies(com.esferixis.misc.dynamicFields.DynamicFieldsContainerObject)
	 */
	@Override
	public Collection<MockLoadableElement> getDependencies(final MockLoadableElement element) {
		final Set<MockSource> dependencies = element.getMockSource().getDependencies();
		
		return new AbstractCollection<MockLoadableElement>() {

			@Override
			public Iterator<MockLoadableElement> iterator() {
				final Iterator<MockSource> sourceDependenciesIterator = dependencies.iterator();
				
				return new Iterator<MockLoadableElement>() {

					@Override
					public boolean hasNext() {
						return sourceDependenciesIterator.hasNext();
					}

					@Override
					public MockLoadableElement next() {
						return new MockLoadableElement(sourceDependenciesIterator.next());
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
					
				};
			}

			@Override
			public int size() {
				return dependencies.size();
			}
			
		};
	}
}
