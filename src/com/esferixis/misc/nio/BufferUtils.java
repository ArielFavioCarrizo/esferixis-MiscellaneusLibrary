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
package com.esferixis.misc.nio;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/**
 * Utilidad de manejo de buffers nativos
 */
public final class BufferUtils {
	/**
	 * @post Crea un buffer de bytes directo del tamaño especificado
	 */
	public static ByteBuffer createByteBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
	}
	
	/**
	 * @post Crea un buffer de shorts directo del tamaño especificado
	 */
	public static ShortBuffer createShortBuffer(int size) {
		return createByteBuffer(size << getElementSizeExponent(ShortBuffer.class) ).asShortBuffer();
	}
	
	/**
	 * @post Crea un buffer de integers directo del tamaño especificado
	 */
	public static IntBuffer createIntBuffer(int size) {
		return createByteBuffer(size << getElementSizeExponent(IntBuffer.class)).asIntBuffer();
	}
	
	/**
	 * @post Crea un buffer de longs directo del tamaño especificado
	 */
	public static LongBuffer createLongBuffer(int size) {
		return createByteBuffer(size << getElementSizeExponent(LongBuffer.class)).asLongBuffer();
	}
	
	/**
	 * @post Crea un buffer de floats directo del tamaño especificado
	 */
	public static FloatBuffer createFloatBuffer(int size) {
		return createByteBuffer(size << getElementSizeExponent(FloatBuffer.class)).asFloatBuffer();
	}
	
	/**
	 * @post Crea un buffer de doubles directo del tamaño especificado
	 */
	public static DoubleBuffer createDoubleBuffer(int size) {
		return createByteBuffer(size << getElementSizeExponent(DoubleBuffer.class)).asDoubleBuffer();
	}
	
	/**
	 * @post Devuelve el exponente del tamaño del elemento en base a la clase
	 */
	public static int getElementSizeExponent(Class<? extends Buffer> bufferClass) {
		if ( bufferClass == ByteBuffer.class ) {
			return 0;
		}
		else if ( ( bufferClass == ShortBuffer.class ) || ( bufferClass == CharBuffer.class ) ) {
			return 1;
		}
		else if ( ( bufferClass == FloatBuffer.class ) || ( bufferClass == IntBuffer.class ) ) {
			return 2;
		}
		else if ( ( bufferClass == LongBuffer.class ) || ( bufferClass == DoubleBuffer.class ) ) {
			return 3;
		}
		else {
			throw new IllegalArgumentException("Unsupported buffer class: " + bufferClass);
		}
	}
}
