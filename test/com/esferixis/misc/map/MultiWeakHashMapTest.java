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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.esferixis.misc.map.MultiWeakHashMap;
import com.esferixis.misc.reference.DynamicReference;

public final class MultiWeakHashMapTest {
	private static abstract class Option {
		private final String title;
		
		public Option(String title) {
			this.title = title;
		}
		
		/**
		 * @post Efectúa la operación solicitada
		 */
		public abstract void doAction();
	}
	
	public static void main(String[] args) {
		final Map<String, String> map = new MultiWeakHashMap<String, String>();
		
		final DynamicReference<Boolean> exitFlag = new DynamicReference<Boolean>(false);
		
		final Scanner scanner = new Scanner(System.in);
		
		final List<String> storedKeys = new LinkedList<String>();
		
		final Option[] options = new Option[]{
			new Option("Put and store key") {

				@Override
				public void doAction() {
					System.out.println("Enter key: ");
					final String key = scanner.next();
					
					System.out.println("Enter value: ");
					final String value = scanner.next();
					
					map.put(key, value);
					
					storedKeys.add(key);
				}
				
			},
			new Option("Get and store key") {

				@Override
				public void doAction() {
					System.out.println("Enter key: ");
					final String key = scanner.next();
					storedKeys.add(key);
					
					System.out.println("Value: " + map.get(key));
				}
				
			},
			new Option("Print map") {

				@Override
				public void doAction() {
					System.out.println(map);
				}
				
			},
			new Option("View stored keys") {

				@Override
				public void doAction() {
					System.out.println(storedKeys);
				}
				
			},
			new Option("Remove stored key by index") {

				@Override
				public void doAction() {
					System.out.println("Enter index: ");
					
					int index = scanner.nextInt();
					
					if ( ( index >= 0 ) && ( index < storedKeys.size() ) ) {
						storedKeys.remove(index);
					}
					else {
						System.out.println("Invalid index");
					}
				}
				
			},
			new Option("Force GC cycle") {

				@Override
				public void doAction() {
					System.gc();
				}
				
			},
			new Option("Exit") {

				@Override
				public void doAction() {
					exitFlag.set(true);
				}
				
			}
		};
		
		while ( !exitFlag.get() ) {
			for ( int i = 0 ; i < options.length; i++) {
				System.out.println(i + ". " + options[i].title);
			}
			
			System.out.println("Enter option number: ");
			
			int optionNumber = scanner.nextInt();
			
			if ( ( optionNumber >= 0 ) && ( optionNumber < options.length ) ) {
				options[optionNumber].doAction();
			}
			else {
				System.out.println("Invalid option number");
			}
		}
		
		scanner.close();
	}
}
