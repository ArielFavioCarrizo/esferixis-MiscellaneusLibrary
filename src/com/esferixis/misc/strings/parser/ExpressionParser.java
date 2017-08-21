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
package com.esferixis.misc.strings.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ariel
 *
 */
public final class ExpressionParser<T> {
	private Map<String, FunctionParser<?>> functionsParsersPerName;
	
	/**
	 * @pre El array de parseadores de función no pueden ser nulos.
	 * 		Y Ninguno de los parseadores de entidad puede ser nulo
	 * @post Crea el parser con los parsers de función especificados
	 */
	public ExpressionParser(FunctionParser<?>... functionParsers) {
		if ( functionParsers != null ) {
			this.functionsParsersPerName = new HashMap<String, FunctionParser<?>>();
			
			functionParsers = functionParsers.clone();
			
			for ( FunctionParser<?> eachEntityParser : functionParsers ) {
				if ( eachEntityParser != null ) {
					this.functionsParsersPerName.put(eachEntityParser.getFunctionName(), eachEntityParser);
				}
				else {
					throw new NullPointerException();
				}
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	public static final void checkParametersQuantity(List<String> parameters, int expectedQuantity) {
		if ( parameters.size() != expectedQuantity ) {
			throw new ParseException("Expected " + expectedQuantity + " parameters");
		}
	}
	
	/**
	 * @pre La cadena no puede ser nula
	 * @post Separa en parámetros
	 */
	public static final List<String> separateParameters(String parametersString) throws ParseException {
		if ( parametersString != null ) {
			int bracesLevel = 0;
			ArrayList<String> parameters = new ArrayList<String>();
			
			int parameterStartIndex = 0;
			
			for ( int i = 0 ; i <= parametersString.length() ; i++ ) {
				final boolean end = ( i == parametersString.length() );
		
				if ( !end ) {
					switch ( parametersString.charAt(i) ) {
					case '(':					
					case '[':
						bracesLevel++;
						break;
					case ')':
					case ']':
						bracesLevel--;
						
						if ( bracesLevel < 0 ) {
							throw new ParseException("Missing brace '('");
						}
						
						break;
					}
				}
				
				if ( end || ( parametersString.charAt(i) == ',' ) ) {
					if ( bracesLevel == 0 ) {
						parameters.add( parametersString.substring(parameterStartIndex, i).trim() );
						parameterStartIndex = i + 1;
					}
				}
			}
			
			if ( bracesLevel > 0 ) {
				throw new ParseException("Missing brace ')'");
			}
			
			if ( ( parameters.size() == 1 ) && ( parameters.get(0).isEmpty() ) ) {
				parameters.clear();
			}
		
			parameters.trimToSize();
		
			return Collections.unmodifiableList(parameters);
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @pre La cadena y la clase de elementos no pueden ser nulas
	 * @post Parsea la cadena especificada.
	 * 		 Indicando que parsea los elementos de la clase especificada
	 * @param entityString
	 * @param filterEntityClass
	 * @return
	 * @throws NullPointerException
	 * @throws ParseException
	 */
	public <S extends T> S parse(String entityString, Class<S> filterEntityClass) throws NullPointerException, ParseException{
		if ( ( entityString != null ) && ( filterEntityClass != null ) ) {
			
			entityString = entityString.trim();
			
			final String[] splitEntityString = entityString.split("\\(", 2);
			
			if ( splitEntityString.length == 2 ) {
				if ( splitEntityString[1].charAt(splitEntityString[1].length()-1) == ')' ) {
					final String entityName = splitEntityString[0].trim();
					
					final FunctionParser<?> entityParser = this.functionsParsersPerName.get(entityName);
					
					if ( entityParser != null ) {
						final Object resultEntity = entityParser.parse(splitEntityString[1].substring(0, splitEntityString[1].length()-1).trim());
						
						if ( filterEntityClass.isInstance(resultEntity) ) {
							return (S) resultEntity;
						}
						else {
							throw new ParseException("Expected " + filterEntityClass.getName());
						}
					}
					else {
						throw new ParseException("Unrecognized entity name '" + entityName + "'");
					}
				}
				else {
					throw new ParseException("Missing brace ')'");
				}
			}
			else {
				throw new ParseException("Missing brace '('");
			}
		}
		else {
			throw new NullPointerException();
		}
	}
}
