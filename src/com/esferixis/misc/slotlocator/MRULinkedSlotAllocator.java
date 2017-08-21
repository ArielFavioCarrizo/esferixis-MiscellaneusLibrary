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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ariel
 *
 */
public final class MRULinkedSlotAllocator<S extends Slot> {
	protected LinkedAllocatableElement<S> first, last;
	private final List<S> emptySlots;
	
	/**
	 * @post Crea el asignador con los slots especificados
	 */
	public MRULinkedSlotAllocator(Collection<S> slots) {
		this.emptySlots = new ArrayList<S>(slots);
		this.first = null;
		this.last = null;
	}
	
	/**
	 * @pre El elemento no puede ser nulo
	 * @post Devuelve el slot asociado,
	 * 		 si no hay suficientes slots, desasocia un slot.
	 */
	public S get(LinkedAllocatableElement<S> element) {
		if ( element != null ) {
			element.checkSlotAllocator(this);
			
			if ( element.getSlot() == null ) {
				// Si no hay slots libres, liberar el slot del último elemento
				if ( this.emptySlots.isEmpty() ) {
					this.detach(this.last);
				}
				
				// Asignar el slot del último elemento de la lista
				S newSlot = this.emptySlots.get(this.emptySlots.size()-1);
				element.setSlot(newSlot);
				this.emptySlots.remove(this.emptySlots.size()-1);
				
				this.addFirst(element);
			}
			else {
				if ( element != this.first ) {
					// Quita el elemento de la lista
					this.remove(element);
					
					// Y lo vuelve agregar al principio
					this.addFirst(element);
				}
			}
			
			return element.getSlot();
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Agrega un elemento en la lista
	 */
	private void addFirst(LinkedAllocatableElement<S> element) {
		// Agregar el elemento en la primera posición de la lista
		element.setBefore(null);
		element.setAfter(this.first);
		if ( this.first != null ) {
			this.first.setBefore(element);
		}
		this.first = element;
		
		// Si no hay último elemento tomando como el fin de la lista
		if ( this.last == null ) {
			this.last = element;
		}
	}
	
	/**
	 * @post Quita el elemento de la lista
	 */
	private void remove(LinkedAllocatableElement<S> element) {
		if ( element == this.first ) {
			this.first = this.first.getAfter();
		}
		else {
			element.getBefore().setAfter(element.getAfter());
		}
		
		if ( element == this.last ) {
			this.last = this.last.getBefore();
		}
		else {
			element.getAfter().setBefore(element.getBefore());
		}
	}
	
	/**
	 * @pre El elemento no puede ser nulo
	 * @post Desasocia el slot del elemento especificado,
	 * 		 si no tiene slot, no ocurre nada
	 */
	public void detach(LinkedAllocatableElement<S> element) {
		if ( element != null ) {
			element.checkSlotAllocator(this);
			
			final S slot = element.getSlot();
			if ( slot != null ) {
				element.setSlot(null);
				
				this.emptySlots.add(slot);
				
				this.remove(element);
			}
		}
		else {
			throw new NullPointerException();
		}
	}
}
