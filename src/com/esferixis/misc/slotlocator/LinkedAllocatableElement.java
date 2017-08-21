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
package com.esferixis.misc.slotlocator;

/**
 * @author ariel
 *
 */
public abstract class LinkedAllocatableElement<S extends Slot> {
	private LinkedAllocatableElement<S> before, after;
	private S slot;
	private MRULinkedSlotAllocator<S> slotAllocator;
	
	/**
	 * @post Crea un elemento
	 */
	public LinkedAllocatableElement() {
		this.before = null;
		this.after = null;
		this.slot = null;
		this.slotAllocator = null;
	}
	
	/**
	 * @post Verifica que el asignador no haya sido modificado
	 */
	void checkSlotAllocator(MRULinkedSlotAllocator<S> slotAllocator) {
		if ( this.slotAllocator != null ) {
			if ( slotAllocator != this.slotAllocator ) {
				throw new IllegalStateException("Cannot use a element with different slot allocator");
			}
		}
		else {
			this.slotAllocator = slotAllocator;
		}
	}
	
	/**
	 * @post Asigna el slot especificado,
	 * 		 si es nulo, lo desasocia del asignador de slots
	 */
	void setSlot(S slot) {
		this.notifyNewSlot(slot);
		this.slot = slot;
		
		if ( slot == null ) {
			this.slotAllocator = null;
		}
	}
	
	/**
	 * @post Devuelve el slot, si es nulo significa
	 * 		 que no tiene asignado un slot
	 */
	S getSlot() {
		return this.slot;
	}
	
	/**
	 * @post Devuelve el elemento anterior
	 */
	LinkedAllocatableElement<S> getBefore() {
		return this.before;
	}
	
	/**
	 * @post Asigna el elemento anterior
	 */
	void setBefore(LinkedAllocatableElement<S> before) {
		this.before = before;
	}
	
	/**
	 * @post Devuelve el elemento posterior
	 */
	LinkedAllocatableElement<S> getAfter() {
		return this.after;
	}
	
	/**
	 * @post Asigna el elemento posterior
	 */
	void setAfter(LinkedAllocatableElement<S> after) {
		this.after = after;
	}
	
	/**
	 * @post Notifica la existencia de cambio de slot,
	 * 		 El slot actual es el anterior.
	 * 		 Si el slot nuevo es nulo, significa que el
	 * 		 elemento es desasignado
	 */
	protected abstract void notifyNewSlot(S newSlot);
}
