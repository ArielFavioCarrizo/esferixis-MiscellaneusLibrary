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
package com.esferixis.misc.Reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.esferixis.misc.classextra.ClassExtra;

/**
 * Pruebas de unidad del agregador de funcionalidad a la clase de reflection
 */
public class ClassExtraTest {
	
	/**
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	@Test
	public void simpleClassTest() throws SecurityException, NoSuchMethodException {
		class SimpleClass {
			public void method1() {
				
			}
			
			public Integer method2(String a, float b) {
				return null;
			}
			
			protected String method3(int a) {
				return null;
			}
			
			private int method4(int a) {
				return a;
			}
		}
		
		// Agregar los métodos no privados heredados
		Set<Method> expectedMethods = new HashSet<Method>();
		{
			for ( Method eachMethod : Object.class.getDeclaredMethods() ) {
				if ( !Modifier.isPrivate( eachMethod.getModifiers() ) ) {
					expectedMethods.add( eachMethod );
				}
			}
		}
		
		// Agregar los métodos propios
		expectedMethods.add( SimpleClass.class.getDeclaredMethod("method1") );
		expectedMethods.add( SimpleClass.class.getDeclaredMethod("method2", String.class, Float.TYPE) );
		expectedMethods.add( SimpleClass.class.getDeclaredMethod("method3", Integer.TYPE) );
		expectedMethods.add( SimpleClass.class.getDeclaredMethod("method4", Integer.TYPE) );
		
		Assert.assertEquals(expectedMethods, new HashSet<Method>( new ClassExtra<SimpleClass>(SimpleClass.class).getAllMethods() ) );
	}
	
	@Test
	public void InheritanceClassTest() throws SecurityException, NoSuchMethodException {
		class SuperClass {
			public void method1() {
				
			}
			
			public Integer method2(String a, float b) {
				return null;
			}
			
			protected String method3(int a) {
				return null;
			}
			
			private int method4(int a) {
				return a;
			}
		}
		
		class ChildClass extends SuperClass {
			@Override
			protected String method3(int a) {
				return super.method3(a);
			}
			
			public void method5(int a, long b, char c) {
				
			}
			
			protected void method6(long a, int b) {
				
			}
			
			public void method7() {
				
			}
			
			private void method8(boolean a) {
				
			}
		}
		
		// Agregar los métodos no privados heredados
		Set<Method> expectedSuperClassMethods = new HashSet<Method>();
		{
			for ( Method eachMethod : Object.class.getDeclaredMethods() ) {
				if ( !Modifier.isPrivate( eachMethod.getModifiers() ) ) {
					expectedSuperClassMethods.add( eachMethod );
				}
			}
		}
		
		expectedSuperClassMethods.add( SuperClass.class.getDeclaredMethod("method1") );
		expectedSuperClassMethods.add( SuperClass.class.getDeclaredMethod("method2", String.class, Float.TYPE) );
		expectedSuperClassMethods.add( SuperClass.class.getDeclaredMethod("method3", Integer.TYPE) );
		expectedSuperClassMethods.add( SuperClass.class.getDeclaredMethod("method4", Integer.TYPE) );
		
		Assert.assertEquals(expectedSuperClassMethods, new HashSet<Method>( new ClassExtra<SuperClass>(SuperClass.class).getAllMethods() ) );
		
		// Agregar los métodos no privados heredados de Object
		Set<Method> expectedChildClassMethods = new HashSet<Method>();
		{
			for ( Method eachMethod : Object.class.getDeclaredMethods() ) {
				if ( !Modifier.isPrivate( eachMethod.getModifiers() ) ) {
					expectedChildClassMethods.add( eachMethod );
				}
			}
		}
		
		expectedChildClassMethods.add( SuperClass.class.getDeclaredMethod("method1") );
		expectedChildClassMethods.add( SuperClass.class.getDeclaredMethod("method2", String.class, Float.TYPE) );
		expectedChildClassMethods.add( ChildClass.class.getDeclaredMethod("method3", Integer.TYPE) );
		
		expectedChildClassMethods.add( ChildClass.class.getDeclaredMethod("method5", Integer.TYPE, Long.TYPE, Character.TYPE ) );
		expectedChildClassMethods.add( ChildClass.class.getDeclaredMethod("method6", Long.TYPE, Integer.TYPE) );
		expectedChildClassMethods.add( ChildClass.class.getDeclaredMethod("method7") );
		expectedChildClassMethods.add( ChildClass.class.getDeclaredMethod("method8", Boolean.TYPE) );
		
		Assert.assertEquals(expectedChildClassMethods, new HashSet<Method>( new ClassExtra<ChildClass>(ChildClass.class).getAllMethods() ) );
	}
}
