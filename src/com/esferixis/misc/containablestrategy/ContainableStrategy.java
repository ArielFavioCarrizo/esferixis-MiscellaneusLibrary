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
package com.esferixis.misc.containablestrategy;

public interface ContainableStrategy {
	/**
	 * @post Devuelve si el contenedor contiene el contenible especificado
	 * 		 Si el contenedor es nulo supone que sólo el contenible nulo está contenido
	 * 		 Los contenedores que no están cubiertos por ésta estrategia por definición
	 * 		 no contienen ningún contenible.
	 * 		 Los contenibles que no están cubiertos por ésta estrategia por definición no están contenidos
	 * 		 por ningún contenedor.
	 * 
	 * 		 Éste método es:
	 * 		 Reflexivo: Todo contenible se contiene a sí mismo
	 * 		 Transitivo:
	 * 		 A, B y C sean contenibles
	 * 		 B está contenido por A y C está contenido por B
	 * 		 entonces C está contenido por A
	 * 		 Consistente: Sea A y B contenibles, entonces reporta  que A contiene o no contiene a B
	 * 		 en todas las invocaciones posibles mientras alguno de ellos no se modifique de manera tal
	 * 		 que según la postcondición haya cambio en las relaciones.
	 */
	public abstract boolean contains(Object container, Object containable);
}
