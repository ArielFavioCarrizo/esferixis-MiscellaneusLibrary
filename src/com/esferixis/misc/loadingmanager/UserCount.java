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

import java.util.Set;

import com.esferixis.misc.collection.set.ArraySet;

/**
 * @author ariel
 *
 */
public final class UserCount {
	private int userCount;
	private final Set<LoadingStrategy.Observer> loadingStrategyObservers;
	
	/**
	 * @post Crea un administrador de cuenta de usuarios
	 */
	public UserCount() {
		this.userCount = 0;
		this.loadingStrategyObservers = new ArraySet<LoadingStrategy.Observer>();
	}
	
	/**
	 * @post Incrementa la cuenta de usuarios
	 */
	public void incrementUserCount() {
		if ( this.userCount != Integer.MAX_VALUE ) {
			if ( this.userCount++ == 0 ) {
				for ( LoadingStrategy.Observer eachObserver : this.loadingStrategyObservers ) {
					eachObserver.notifyHasLoadedUsers();
				}
			}
		}
		else {
			throw new IllegalStateException("User count overflow");
		}
	}
	
	/**
	 * @post Decrementa la cuenta de usuarios
	 */
	public void decrementUserCount() {
		if ( this.userCount != 0 ) {
			if ( this.userCount-- == 1 ) {
				for ( LoadingStrategy.Observer eachObserver : this.loadingStrategyObservers ) {
					eachObserver.notifyHasNotLoadedUsers();
				}
			}
		}
		else {
			throw new IllegalStateException("User count underflow");
		}
	}
	
	/**
	 * @pre El observer no puede ser nulo
	 * @post Agrega un observador
	 */
	public void addObserver(LoadingStrategy.Observer observer) {
		if ( observer != null ) {
			this.loadingStrategyObservers.add(observer);
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Quita un observer
	 */
	public void removeObserver(LoadingStrategy.Observer observer) {
		if ( observer != null ) {
			this.loadingStrategyObservers.remove(observer);
		}
		else {
			throw new NullPointerException();
		}
	}
}
