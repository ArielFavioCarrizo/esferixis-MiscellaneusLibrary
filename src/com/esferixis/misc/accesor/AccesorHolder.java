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
package com.esferixis.misc.accesor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Almacenador de accesor
 * 
 * @author ariel
 *
 */
public final class AccesorHolder<A> {
	private final Class<A> accesorClass;
	private A accesor;
	
	/**
	 * @post Crea el almacenador de accesor con la clase
	 * 		 de accesor especificada
	 * @param accesorClass
	 */
	public AccesorHolder(Class<A> accesorClass) {
		if ( accesorClass != null ) {
			this.accesorClass = accesorClass;
			this.accesor = null;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @pre El almacenador de accesor tiene que figurar en la lista
	 * 		blanca del accesor
	 * @post Devuelve el accesor
	 */
	public A get() {
		if ( this.accesor == null ) {
			boolean foundedAnnotation = false;
			
			for ( Field eachAccesorClassField : this.accesorClass.getDeclaredFields() ) {
				if ( eachAccesorClassField.isAnnotationPresent(AccesorWhiteList.class) ) {
					if ( !foundedAnnotation ) {
						A accesorInstance = null;
						{
							final Constructor<?>[] constructors = this.accesorClass.getDeclaredConstructors();
							
							if ( constructors.length == 1 ) {
								final Constructor<?> constructor = constructors[0];
								
								if ( Modifier.isPrivate(constructor.getModifiers()) ) {
									if ( constructor.getParameters().length == 0 ) {
										constructor.setAccessible(true);
										
										try {
											accesorInstance = (A) constructor.newInstance();
										} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
											throw new RuntimeException(e);
										}
									}
								}
							}
							
							if ( accesorInstance == null ) {
								throw new RuntimeException("Expected one private constructor with no parameters in '" + this.accesorClass + "'");
							}
						}
						
						final int modifiers = eachAccesorClassField.getModifiers();
						if ( Modifier.isStatic(modifiers) && Modifier.isPrivate(modifiers) && Modifier.isFinal(modifiers) ) {
							if ( eachAccesorClassField.getType().isArray() && eachAccesorClassField.getType().getComponentType().equals(Class.class) ) {
								eachAccesorClassField.setAccessible(true);
								
								try {
									Class<?>[] packageAccesorsClasses = (Class<?>[]) eachAccesorClassField.get(null);
									
									for ( Class<?> eachPackageAccesorsClass : packageAccesorsClasses ) {
										if ( Modifier.isFinal( eachPackageAccesorsClass.getModifiers() ) ) {
											boolean foundField = false;
											
											if ( eachPackageAccesorsClass.getConstructors().length == 0 ) {
												for ( Field eachPackageAccesorsClassField : eachPackageAccesorsClass.getDeclaredFields() ) {
													if ( eachPackageAccesorsClassField.getModifiers() == ( Modifier.STATIC | Modifier.FINAL ) ) {
														eachPackageAccesorsClassField.setAccessible(true);
														Object fieldValue =  eachPackageAccesorsClassField.get(null);
														
														if ( fieldValue instanceof AccesorHolder<?> ) {
															final AccesorHolder eachAccesorHolder = (AccesorHolder) fieldValue;
															
															if ( eachAccesorHolder.accesorClass.equals(this.accesorClass) ) {
																if ( !foundField ) {
																	if ( eachAccesorHolder.accesor == null ) {
																		eachAccesorHolder.accesor = accesorInstance;
																	}
																	else if ( eachAccesorHolder.accesor != accesorInstance ) {
																		throw new RuntimeException("'" + this.accesorClass + "' conflicts with '" + eachAccesorHolder.accesor.getClass() + "' in '" + eachPackageAccesorsClass + "'");
																	}
																}
																else {
																	throw new RuntimeException("Duplicate field: " + eachPackageAccesorsClassField);
																}
															}
														}
														else {
															throw new RuntimeException("Expected that '" + eachPackageAccesorsClassField + "' to be of type '" + AccesorHolder.class + "'");
														}
													}
													else {
														throw new RuntimeException("Expected that '" + eachPackageAccesorsClassField + "' to be default, static and final");
													}
												}
											}
											else {
												throw new RuntimeException("Expected no public constructors in '" + eachPackageAccesorsClass + "'");
											}
										}
										else {
											throw new RuntimeException("Expected that '" + eachPackageAccesorsClass + "' to be final");
										}
									}
								} catch (IllegalAccessException e) {
									throw new RuntimeException(e);
								}
							}
							else {
								throw new RuntimeException("Expected that accesor white list field is an array of type Class<?> in '" + this.accesorClass + "'");
							}
						}
						else {
							throw new RuntimeException("Expected private static final accesor white list field in '" + this.accesorClass + "'");
						}
						
						foundedAnnotation = true;
					}
					else {
						throw new RuntimeException("Expected one AccesorWhiteList annotation in '" + this.accesorClass + "'");
					}
				}
			}
			
			if ( this.accesor == null ) {
				throw new RuntimeException("Accesor holder not found in white list of '" + this.accesorClass + "'");
			}
		}
		
		return this.accesor;
	}
}
