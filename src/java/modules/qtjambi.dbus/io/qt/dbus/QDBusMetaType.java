/****************************************************************************
**
** Copyright (C) 2009-2023 Dr. Peter Droste, Omix Visualization GmbH & Co. KG. All rights reserved.
**
** This file is part of Qt Jambi.
**
** $BEGIN_LICENSE$
** GNU Lesser General Public License Usage
** This file may be used under the terms of the GNU Lesser
** General Public License version 2.1 as published by the Free Software
** Foundation and appearing in the file LICENSE.LGPL included in the
** packaging of this file.  Please review the following information to
** ensure the GNU Lesser General Public License version 2.1 requirements
** will be met: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3.0 as published by the Free Software
** Foundation and appearing in the file LICENSE.GPL included in the
** packaging of this file.  Please review the following information to
** ensure the GNU General Public License version 3.0 requirements will be
** met: http://www.gnu.org/copyleft/gpl.html.
** $END_LICENSE$
**
** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
**
****************************************************************************/

package io.qt.dbus;

import java.io.Serializable;
import java.util.Objects;

import io.qt.core.QMetaType;

public final class QDBusMetaType {
	static {
		QtJambi_LibraryUtilities.initialize();
	}
	
	public static QMetaType registerDBusMetaType(Class<?> clazz, QMetaType... instantiations) {
		QMetaType metaType = QMetaType.fromType(clazz, instantiations);
		if(metaType!=null && metaType.isValid()) {
			registerDBusMetaType(metaType.id(), null, null, null);
		}else {
			throw new RuntimeException("Unable to find meta type for class "+clazz.getName());
		}
		return metaType;
	}
	
	public static QMetaType registerDBusMetaType(QMetaType metaType) {
		if(metaType!=null && metaType.isValid()) {
			registerDBusMetaType(metaType.id(), null, null, null);
		}
		return metaType;
	}
	
	public interface MarshallFunction<U> extends java.util.function.BiConsumer<QDBusArgument, U>, Serializable{
	}
	
	public interface DemarshallFunction<U> extends java.util.function.Function<QDBusArgument, U>, Serializable{
	}
	
	public static <T> QMetaType registerDBusMetaType(MarshallFunction<T> marshallFunction, DemarshallFunction<T> demarshallFunction) {
		int[] marshallFunctionTypes = QtJambi_LibraryUtilities.internal.lambdaMetaTypes(MarshallFunction.class, Objects.requireNonNull(marshallFunction));
		int[] demarshallFunctionTypes = QtJambi_LibraryUtilities.internal.lambdaMetaTypes(DemarshallFunction.class, Objects.requireNonNull(demarshallFunction));
		Class<?>[] marshallFunctionClassTypes = QtJambi_LibraryUtilities.internal.lambdaClassTypes(MarshallFunction.class, Objects.requireNonNull(marshallFunction));
		Class<?>[] demarshallFunctionClassTypes = QtJambi_LibraryUtilities.internal.lambdaClassTypes(DemarshallFunction.class, Objects.requireNonNull(demarshallFunction));
		if(marshallFunctionTypes==null || demarshallFunctionTypes==null 
				|| marshallFunctionTypes.length!=3 || demarshallFunctionTypes.length!=2)
			throw new IllegalArgumentException("Marshall and/or demarshall function not a lambda expression.");
		if(demarshallFunctionTypes[0]==0 || marshallFunctionTypes[2]==0)
			throw new IllegalArgumentException("Unable to recognize meta type.");
		if(demarshallFunctionTypes[0]!=marshallFunctionTypes[2] || demarshallFunctionClassTypes[0]!=marshallFunctionClassTypes[2]) {
			throw new IllegalArgumentException(String.format("Marshalled type %1$s (%2$s) is different from demarshalled type %3$s (%4$s).", marshallFunctionClassTypes[2].getTypeName(), new QMetaType(marshallFunctionTypes[2]).name(), demarshallFunctionClassTypes[0].getTypeName(), new QMetaType(demarshallFunctionTypes[0]).name()));
		}
		registerDBusMetaType(demarshallFunctionTypes[0], demarshallFunctionClassTypes[0], marshallFunction, demarshallFunction);
		return new QMetaType(demarshallFunctionTypes[0]);
	}
	
	private static native <T> void registerDBusMetaType(int metaType, Class<?> classType, MarshallFunction<T> marshallFunction, DemarshallFunction<T> demarshallFunction);
	
	public static native Object demarshall(QDBusArgument arg, QMetaType id) throws UnsupportedOperationException;
	
	public static void marshall(QDBusArgument arg, Object value) throws UnsupportedOperationException {
		marshall(arg, null, value);
	}
	
	public static native void marshall(QDBusArgument arg, QMetaType id, Object value) throws UnsupportedOperationException;
	
	public static native String typeToSignature(QMetaType metaType);
	
	public static native QMetaType signatureToMetaType(String signature);
}
