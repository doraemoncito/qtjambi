/****************************************************************************
 **
 ** Copyright (C) 1992-2009 Nokia. All rights reserved.
 ** Copyright (C) 2009-2023 Dr. Peter Droste, Omix Visualization GmbH & Co. KG. All rights reserved.
 **
 ** This file is part of Qt Jambi.
 **
 ** $BEGIN_LICENSE$
 **
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
 **
 ** $END_LICENSE$
 **
 ** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
 ** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 **
 ****************************************************************************/

package generator;

import io.qt.*;
import io.qt.internal.QtJambiObject.QPrivateConstructor;
import io.qt.core.*;

class QtJambi_LibraryUtilities_2_{
    if(!Boolean.getBoolean("io.qt.no-library-shutdown-hook")) {
        shutdownHook = RetroHelper.newShutdownThread(QtJambi_LibraryUtilities::shutdown, "QtJambi_LibraryShutdown");
        shutdownHook.setContextClassLoader(null);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
    java.io.File coreLib = LibraryUtility.loadQtCore();
    try{
        java.io.File qtjambiLib = LibraryUtility.loadQtJambiLibrary();
        if(LibraryUtility.operatingSystem!=LibraryUtility.OperatingSystem.Android) {
            java.util.List<String> paths = new java.util.ArrayList<>();
            String path;
            switch(LibraryUtility.operatingSystem) {
            case MacOS:
                path = io.qt.QtUtilities.getenv("DYLD_LIBRARY_PATH");
                String path2 = io.qt.QtUtilities.getenv("DYLD_FRAMEWORK_PATH");
                java.util.List<String> paths2 = new java.util.ArrayList<>();
                paths2.add(qtjambiLib.getParentFile().getAbsolutePath());
                if(path2!=null && !path2.isEmpty()) {
                    for(String p : path2.split("\\"+java.io.File.pathSeparator)) {
                        if(!paths2.contains(p))
                            paths2.add(p);
                    }
                }
                path2 = String.join(java.io.File.pathSeparator, paths2);
                io.qt.QtUtilities.putenv("DYLD_FRAMEWORK_PATH", path2);
                break;
            case Windows:
                path = io.qt.QtUtilities.getenv("PATH");
                break;
            default:
                path = io.qt.QtUtilities.getenv("LD_LIBRARY_PATH");
                break;
            }
            if(LibraryUtility.operatingSystem!=LibraryUtility.OperatingSystem.MacOS)
                paths.add(coreLib.getParentFile().getAbsolutePath());
            if(!paths.contains(qtjambiLib.getParentFile().getAbsolutePath()))
                paths.add(qtjambiLib.getParentFile().getAbsolutePath());
            if(path!=null && !path.isEmpty()) {
                for(String p : path.split("\\"+java.io.File.pathSeparator)) {
                    if(!paths.contains(p))
                        paths.add(p);
                }
            }
            path = String.join(java.io.File.pathSeparator, paths);
            switch(LibraryUtility.operatingSystem) {
            case MacOS:
                io.qt.QtUtilities.putenv("DYLD_LIBRARY_PATH", path);
                break;
            case Windows:
                io.qt.QtUtilities.putenv("PATH", path);
                break;
            default:
                io.qt.QtUtilities.putenv("LD_LIBRARY_PATH", path);
                break;
            }
        }
    } catch(UnsatisfiedLinkError t) {
        switch(LibraryUtility.operatingSystem) {
        case MacOS:
            if(coreLib!=null) {
                java.io.File prl = new java.io.File(coreLib.getParentFile(), "Resources/QtCore.prl");
                if(prl.exists()) {
                    java.util.Properties prlProp = new java.util.Properties();
                    try(java.io.FileInputStream inStream = new java.io.FileInputStream(prl)){
                        prlProp.load(inStream);
                    } catch(Throwable t2) {}
                    String version = prlProp.getProperty("QMAKE_PRL_VERSION", "");
                    if(!version.isEmpty()) {
                        if(!version.startsWith(qtMajorVersion + "." + qtMinorVersion + ".")) {
                            throw new LinkageError("Cannot combine QtJambi " + qtMajorVersion + "." + qtMinorVersion + " with Qt " + version + "." + t.getMessage(), t);
                        }
                    }
                }
            }
            break;
        case Windows:
            if(coreLib!=null) {
                java.io.File prl = new java.io.File(coreLib.getParentFile(), "Qt"+qtMajorVersion+"Core.prl");
                if(!prl.exists()) {
                    prl = new java.io.File(coreLib.getParentFile().getParentFile(), "lib\\Qt"+qtMajorVersion+"Core.prl");
                }
                if(prl.exists()) {
                    java.util.Properties prlProp = new java.util.Properties();
                    try(java.io.FileInputStream inStream = new java.io.FileInputStream(prl)){
                        prlProp.load(inStream);
                    } catch(Throwable t2) {}
                    String version = prlProp.getProperty("QMAKE_PRL_VERSION", "");
                    if(!version.isEmpty()) {
                        if(!version.startsWith(qtMajorVersion + "." + qtMinorVersion + ".")) {
                            throw new LinkageError("Cannot combine QtJambi " + qtMajorVersion + "." + qtMinorVersion + " with Qt " + version + ". " + t.getMessage(), t);
                        }
                    }
                }
                if(new java.io.File(coreLib.getParentFile(), "libstdc++-6.dll").exists() || LibraryUtility.isMinGWBuilt()) {
                    throw new LinkageError("Cannot combine msvc-based QtJambi with mingw-based Qt library. Please install and use Qt (MSVC 2019 x64) instead. " + t.getMessage(), t);
                }else {
                    throw new LinkageError("Cannot combine mingw-based QtJambi with msvc-based Qt library. Please install and use Qt (MinGW x64) instead. " + t.getMessage(), t);
                }
            }
            break;
        default:
            if(coreLib!=null) {
                java.io.File prl = new java.io.File(coreLib.getParentFile(), "Qt"+qtMajorVersion+"Core.prl");
                if(prl.exists()) {
                    java.util.Properties prlProp = new java.util.Properties();
                    try(java.io.FileInputStream inStream = new java.io.FileInputStream(prl)){
                        prlProp.load(inStream);
                    } catch(Throwable t2) {}
                    String version = prlProp.getProperty("QMAKE_PRL_VERSION", "");
                    if(!version.isEmpty()) {
                        if(!version.startsWith(qtMajorVersion + "." + qtMinorVersion + ".")) {
                            throw new LinkageError("Cannot combine QtJambi " + qtMajorVersion + "." + qtMinorVersion + " with Qt " + version + ". " + t.getMessage(), t);
                        }
                    }
                }
            }
            break;
        }
        throw t;
    }
}// class

class QObject___ extends QObject {
    
    /**
     * Parameter type for declarative constructors, i.e. constructors being
     * called by QML only.
     */
    protected static final class QDeclarativeConstructor { 
        private QDeclarativeConstructor(Class<?> cls, long placement) { this.placement = placement; this.cls = cls; }
        @io.qt.NativeAccess
        private final long placement;
        @io.qt.NativeAccess
        private final Class<?> cls;
    }
    
    private static String classToScope(Class<?> cls) {
        if(cls.isEnum()) {
            if(cls.getDeclaringClass()!=null) {
                return classToScope(cls.getDeclaringClass());
            }else {
                return cls.getPackage().getName();
            }
        }else if(cls.isAnonymousClass()) {
            if(cls.getEnclosingClass()!=null) {
                return classToScope(cls.getEnclosingClass());
            }
        }else if(cls.getEnclosingClass()!=null) {
            return classToScope(cls.getEnclosingClass())+"$"+cls.getSimpleName();
        }
        return cls.getName();
    }
    
    /**
     * Returns a translated version of source, or source itself if there is no appropriate translated version.
     * Note that if you need to translate in a static context, you can use QCoreApplication::translate(),
     * which is a static method.
     *
     * @param source the source text to translate.
     * @return translated version of the source text.
     * @see <a href="@docRoot/qobject.html#tr">QObject::tr(const char *, const char *, int)</a>
     */
    public static String tr(String source) {
        String scope = classToScope(QtJambi_LibraryUtilities.internal.callerClassProvider().get());
        return QCoreApplication.translate(scope, source);
    }
    
    /**
     * Returns a translated version of source, or source itself if there is no appropriate translated version.
     * The comment is used to help translators translate the source text.
     * Note that if you need to translate in a static context, you can use QCoreApplication::translate(),
     * which is a static method.
     *
     * @param source the source text to translate.
     * @param comment helps the translator translate the source text.
     * @return translated version of the source text.
     * @see <a href="@docRoot/qobject.html#tr">QObject::tr(const char *, const char *, int)</a>
     */
    public static String tr(String source, String comment) {
        String scope = classToScope(QtJambi_LibraryUtilities.internal.callerClassProvider().get());
        return QCoreApplication.translate(scope, source, comment);
    }
    
    /**
     * Returns a translated version of source, or source itself if there is no appropriate translated version.
     * If %n occurs in the source text, it will be substituted with count.
     * The comment is used to help translators translate the source text.
     * Note that if you need to translate in a static context, you can use QCoreApplication::translate(),
     * which is a static method.
     *
     * @param source the source text to translate.
     * @param comment helps the translator translate the source text.
     * @param count in source %n will be substituted by count.
     * @return translated version of the source text.
     * @see <a href="@docRoot/qobject.html#tr">QObject::tr(const char *, const char *, int)</a>
     */
    public static String tr(String source, String comment, int count) {
        String scope = classToScope(QtJambi_LibraryUtilities.internal.callerClassProvider().get());
        return QCoreApplication.translate(scope, source, comment, count);
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes no
     * parameters.
     */
    public final class Signal0 extends QMetaObject.AbstractPublicSignal0 {
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes one
     * parameter.
     *
     * @param <A> The type of the single parameter of the signal.
     */
    public final class Signal1<A> extends QMetaObject.AbstractPublicSignal1<A> {
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes two
     * parameters.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     */
    public final class Signal2<A, B> extends QMetaObject.AbstractPublicSignal2<A, B> {
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes three
     * parameters.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     */
    public final class Signal3<A, B, C> extends QMetaObject.AbstractPublicSignal3<A, B, C> {
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes four
     * parameters.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     */
    
    public final class Signal4<A, B, C, D> extends QMetaObject.AbstractPublicSignal4<A, B, C, D> {
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes five
     * parameters.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     */
    public final class Signal5<A, B, C, D, E> extends QMetaObject.AbstractPublicSignal5<A, B, C, D, E> {
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes six
     * parameters.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     */
    public final class Signal6<A, B, C, D, E, F> extends QMetaObject.AbstractPublicSignal6<A, B, C, D, E, F> {
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes seven
     * parameters.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     */
    public final class Signal7<A, B, C, D, E, F, G> extends QMetaObject.AbstractPublicSignal7<A, B, C, D, E, F, G> {
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes eight
     * parameters.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     */
    public final class Signal8<A, B, C, D, E, F, G, H> extends QMetaObject.AbstractPublicSignal8<A, B, C, D, E, F, G, H> {
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes nine
     * parameters.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     */
    public final class Signal9<A, B, C, D, E, F, G, H, I> extends QMetaObject.AbstractPublicSignal9<A, B, C, D, E, F, G, H, I>{
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes one
     * parameters and providing default value for the parameter.
     *
     * @param <A> The type of the first parameter of the signal.
     */
    public final class Signal1Default1<A> extends QMetaObject.AbstractPublicSignal1<A> implements QMetaObject.Emitable0, QMetaObject.Connectable0 {
        
        @SuppressWarnings("unused")
        private Signal1Default1() {
            this.arg1Default = null;
        }
        
        public Signal1Default1(Supplier<? extends A> arg1Default){
            super();
            if(arg1Default!=null){
                this.arg1Default = arg1Default;
            }else{
                throw new QNoDefaultValueException(1);
            }
        }
        
        private final Supplier<? extends A> arg1Default;
        
        @Override
        public final void emit() {
            emitDefaultSignal(arg1Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes two
     * parameters and providing a default value for parameter no. 2.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     */
    public final class Signal2Default1<A, B> extends QMetaObject.AbstractSignal2Default1<A, B>{
        
        @SuppressWarnings("unused")
        private Signal2Default1() {
            super();
        }

        public Signal2Default1(Supplier<? extends B> arg2Default) {
            super(arg2Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes two
     * parameters and providing default values for parameters 1 and 2.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     */
    public final class Signal2Default2<A, B> extends QMetaObject.AbstractSignal2Default1<A, B> implements QMetaObject.Emitable0, QMetaObject.Connectable0 {
        
        @SuppressWarnings("unused")
        private Signal2Default2() {
            this.arg1Default = null;
        }
        
        public Signal2Default2(Supplier<? extends A> arg1Default, Supplier<? extends B> arg2Default){
            super(arg2Default);
            if(arg1Default!=null){
                this.arg1Default = arg1Default;
            }else{
                throw new QNoDefaultValueException(1);
            }
        }
        
        private final Supplier<? extends A> arg1Default;
        
        @Override
        public final void emit() {
            emitDefaultSignal(arg1Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes three
     * parameters and providing a default value for parameter no. 3.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     */
    public final class Signal3Default1<A, B, C> extends QMetaObject.AbstractSignal3Default1<A, B, C> {
        
        @SuppressWarnings("unused")
        private Signal3Default1() {
        }
        
        public Signal3Default1(Supplier<? extends C> arg3Default) {
            super(arg3Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes three
     * parameters and providing default values for parameters 2 and 3.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     */
    public final class Signal3Default2<A, B, C> extends QMetaObject.AbstractSignal3Default2<A, B, C> {
        
        @SuppressWarnings("unused")
        private Signal3Default2() {
        }
        
        public Signal3Default2(Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default){
            super(arg2Default, arg3Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes three
     * parameters and providing default values for parameters 1 to 3.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     */
    public final class Signal3Default3<A, B, C> extends QMetaObject.AbstractSignal3Default2<A, B, C> implements QMetaObject.Emitable0, QMetaObject.Connectable0 {
        
        @SuppressWarnings("unused")
        private Signal3Default3() {
            this.arg1Default = null;
        }
        
        public Signal3Default3(Supplier<? extends A> arg1Default, Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default){
            super(arg2Default, arg3Default);
            if(arg2Default!=null){
                this.arg1Default = arg1Default;
            }else{
                throw new QNoDefaultValueException(1);
            }
        }
        
        private final Supplier<? extends A> arg1Default;
        
        @Override
        public final void emit() {
            emitDefaultSignal(arg1Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes four
     * parameters and providing a default value for parameter no. 4.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     */
    public final class Signal4Default1<A, B, C, D> extends QMetaObject.AbstractSignal4Default1<A, B, C, D>{
        
        @SuppressWarnings("unused")
        private Signal4Default1() {
        }
        
        public Signal4Default1(Supplier<? extends D> arg4Default){
            super(arg4Default);
        }
        
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes four
     * parameters and providing default values for parameters 3 and 4.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     */
    public final class Signal4Default2<A, B, C, D> extends QMetaObject.AbstractSignal4Default2<A, B, C, D>{
        
        @SuppressWarnings("unused")
        private Signal4Default2() {
        }
        
        public Signal4Default2(Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default){
            super(arg3Default, arg4Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes four
     * parameters and providing default values for parameters 2 to 4.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     */
    public final class Signal4Default3<A, B, C, D> extends QMetaObject.AbstractSignal4Default3<A, B, C, D>{
        
        @SuppressWarnings("unused")
        private Signal4Default3() {
        }
        
        public Signal4Default3(Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default){
            super(arg2Default, arg3Default, arg4Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes four
     * parameters and providing default values for parameters 1 to 4.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     */
    public final class Signal4Default4<A, B, C, D> extends QMetaObject.AbstractSignal4Default3<A, B, C, D> implements QMetaObject.Emitable0, QMetaObject.Connectable0 {
        
        @SuppressWarnings("unused")
        private Signal4Default4() {
            this.arg1Default = null;
        }
        
        public Signal4Default4(Supplier<? extends A> arg1Default, Supplier<? extends B> arg2Default, 
                Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default){
            super(arg2Default, arg3Default, arg4Default);
            if(arg1Default!=null){
                this.arg1Default = arg1Default;
            }else{
                throw new QNoDefaultValueException(1);
            }
        }
        
        private final Supplier<? extends A> arg1Default;
        
        @Override
        public final void emit() {
            emitDefaultSignal(arg1Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes five
     * parameters and providing a default value for parameter no. 5.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     */
    public final class Signal5Default1<A, B, C, D, E> extends QMetaObject.AbstractSignal5Default1<A, B, C, D, E>{
        
        @SuppressWarnings("unused")
        private Signal5Default1() {
        }
        
        public Signal5Default1(Supplier<? extends E> arg5Default){
            super(arg5Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes five
     * parameters and providing default values for parameters 4 and 5.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     */
    public final class Signal5Default2<A, B, C, D, E> extends QMetaObject.AbstractSignal5Default2<A, B, C, D, E>{
        
        @SuppressWarnings("unused")
        private Signal5Default2() {
        }
        
        public Signal5Default2(Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default){
            super(arg4Default, arg5Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes five
     * parameters and providing default values for parameters 3 to 5.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     */
    public final class Signal5Default3<A, B, C, D, E> extends QMetaObject.AbstractSignal5Default3<A, B, C, D, E>{
        
        @SuppressWarnings("unused")
        private Signal5Default3() {
        }
        
        public Signal5Default3(Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default){
            super(arg3Default, arg4Default, arg5Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes five
     * parameters and providing default values for parameters 2 to 5.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     */
    public final class Signal5Default4<A, B, C, D, E> extends QMetaObject.AbstractSignal5Default4<A, B, C, D, E>{
        
        @SuppressWarnings("unused")
        private Signal5Default4() {
        }
        
        public Signal5Default4(Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default){
            super(arg2Default, arg3Default, arg4Default, arg5Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes five
     * parameters and providing default values for parameters 1 to 5.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     */
    public final class Signal5Default5<A, B, C, D, E> extends QMetaObject.AbstractSignal5Default4<A, B, C, D, E> implements QMetaObject.Emitable0, QMetaObject.Connectable0{
        
        @SuppressWarnings("unused")
        private Signal5Default5() {
            this.arg1Default = null;
        }
        
        public Signal5Default5(Supplier<? extends A> arg1Default, Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default){
            super(arg2Default, arg3Default, arg4Default, arg5Default);
            if(arg1Default!=null){
                this.arg1Default = arg1Default;
            }else{
                throw new QNoDefaultValueException(1);
            }
        }
        
        private final Supplier<? extends A> arg1Default;
        
        @Override
        public final void emit() {
            emitDefaultSignal(arg1Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes six
     * parameters and providing a default value for parameter no. 6.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     */
    public final class Signal6Default1<A, B, C, D, E, F> extends QMetaObject.AbstractSignal6Default1<A, B, C, D, E, F>{
        
        @SuppressWarnings("unused")
        private Signal6Default1() {
        }
        
        public Signal6Default1(Supplier<? extends F> arg6Default){
            super(arg6Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes six
     * parameters and providing default values for parameters 5 and 6.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     */
    public final class Signal6Default2<A, B, C, D, E, F> extends QMetaObject.AbstractSignal6Default2<A, B, C, D, E, F>{
        
        @SuppressWarnings("unused")
        private Signal6Default2() {
        }
        
        public Signal6Default2(Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default){
            super(arg5Default, arg6Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes six
     * parameters and providing default values for parameters 4 to 6.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     */
    public final class Signal6Default3<A, B, C, D, E, F> extends QMetaObject.AbstractSignal6Default3<A, B, C, D, E, F>{
        
        @SuppressWarnings("unused")
        private Signal6Default3() {
        }
        
        public Signal6Default3(Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default){
            super(arg4Default, arg5Default, arg6Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes six
     * parameters and providing default values for parameters 3 to 6.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     */
    public final class Signal6Default4<A, B, C, D, E, F> extends QMetaObject.AbstractSignal6Default4<A, B, C, D, E, F>{
        
        @SuppressWarnings("unused")
        private Signal6Default4() {
        }
        
        public Signal6Default4(Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default){
            super(arg3Default, arg4Default, arg5Default, arg6Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes six
     * parameters and providing default values for parameters 2 to 6.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     */
    public final class Signal6Default5<A, B, C, D, E, F> extends QMetaObject.AbstractSignal6Default5<A, B, C, D, E, F>{
        
        @SuppressWarnings("unused")
        private Signal6Default5() {
        }
        
        public Signal6Default5(Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default){
            super(arg2Default, arg3Default, arg4Default, arg5Default, arg6Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes six
     * parameters and providing default values for parameters 1 to 6.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     */
    public final class Signal6Default6<A, B, C, D, E, F> extends QMetaObject.AbstractSignal6Default5<A, B, C, D, E, F> implements QMetaObject.Emitable0, QMetaObject.Connectable0{
        
        @SuppressWarnings("unused")
        private Signal6Default6() {
            this.arg1Default = null;
        }
        
        public Signal6Default6(Supplier<? extends A> arg1Default, Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default){
            super(arg2Default, arg3Default, arg4Default, arg5Default, arg6Default);
            this.arg1Default = arg1Default;
            if(this.arg1Default==null){
                throw new QNoDefaultValueException(1);
            }
        }
        
        private final Supplier<? extends A> arg1Default;
        
        @Override
        public final void emit() {
            emitDefaultSignal(arg1Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes seven
     * parameters and providing a default value for parameter no. 7.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     */
    public final class Signal7Default1<A, B, C, D, E, F, G> extends QMetaObject.AbstractSignal7Default1<A, B, C, D, E, F, G>{
        
        @SuppressWarnings("unused")
        private Signal7Default1() {
        }
        
        public Signal7Default1(Supplier<? extends G> arg7Default){
            super(arg7Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes seven
     * parameters and providing default values for parameters 6 and 7.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     */
    public final class Signal7Default2<A, B, C, D, E, F, G> extends QMetaObject.AbstractSignal7Default2<A, B, C, D, E, F, G> {
        
        @SuppressWarnings("unused")
        private Signal7Default2() {
        }
        
        public Signal7Default2(Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default){
            super(arg6Default, arg7Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes seven
     * parameters and providing default values for parameters 5 to 7.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     */
    public final class Signal7Default3<A, B, C, D, E, F, G> extends QMetaObject.AbstractSignal7Default3<A, B, C, D, E, F, G> {
        
        @SuppressWarnings("unused")
        private Signal7Default3() {
        }
        
        public Signal7Default3(Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default){
            super(arg5Default, arg6Default, arg7Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes seven
     * parameters and providing default values for parameters 4 to 7.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     */
    public final class Signal7Default4<A, B, C, D, E, F, G> extends QMetaObject.AbstractSignal7Default4<A, B, C, D, E, F, G> {
        
        @SuppressWarnings("unused")
        private Signal7Default4() {
        }
        
        public Signal7Default4(Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default){
            super(arg4Default, arg5Default, arg6Default, arg7Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes seven
     * parameters and providing default values for parameters 3 to 7.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     */
    public final class Signal7Default5<A, B, C, D, E, F, G> extends QMetaObject.AbstractSignal7Default5<A, B, C, D, E, F, G> {
        
        @SuppressWarnings("unused")
        private Signal7Default5() {
        }
        
        public Signal7Default5(Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default){
            super(arg3Default, arg4Default, arg5Default, arg6Default, arg7Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes seven
     * parameters and providing default values for parameters 2 to 7.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     */
    public final class Signal7Default6<A, B, C, D, E, F, G> extends QMetaObject.AbstractSignal7Default6<A, B, C, D, E, F, G> {
        
        @SuppressWarnings("unused")
        private Signal7Default6() {
        }
        
        public Signal7Default6(Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default){
            super(arg2Default, arg3Default, arg4Default, arg5Default, arg6Default, arg7Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes seven
     * parameters and providing default values for parameters 1 to 7.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     */
    public final class Signal7Default7<A, B, C, D, E, F, G> extends QMetaObject.AbstractSignal7Default6<A, B, C, D, E, F, G> implements QMetaObject.Emitable0, QMetaObject.Connectable0 {
        
        @SuppressWarnings("unused")
        private Signal7Default7() {
            this.arg1Default = null;
        }
        
        public Signal7Default7(Supplier<? extends A> arg1Default, Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default){
            super(arg2Default, arg3Default, arg4Default, arg5Default, arg6Default, arg7Default);
            this.arg1Default = arg1Default;
            if(this.arg1Default==null){
                throw new QNoDefaultValueException(2);
            }
        }
        
        private final Supplier<? extends A> arg1Default;
        
        @Override
        public final void emit() {
            emitDefaultSignal(arg1Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes eight
     * parameters and providing a default value for parameter no. 8.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     */
    public final class Signal8Default1<A, B, C, D, E, F, G, H> extends QMetaObject.AbstractSignal8Default1<A, B, C, D, E, F, G, H>{
        
        @SuppressWarnings("unused")
        private Signal8Default1() {
        }
        
        public Signal8Default1(Supplier<? extends H> arg8Default){
            super(arg8Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes eight
     * parameters and providing default values for parameters 7 and 8.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     */
    public final class Signal8Default2<A, B, C, D, E, F, G, H> extends QMetaObject.AbstractSignal8Default2<A, B, C, D, E, F, G, H> {
        
        @SuppressWarnings("unused")
        private Signal8Default2() {
        }
        
        public Signal8Default2(Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default){
            super(arg7Default, arg8Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes eight
     * parameters and providing default values for parameters 6 to 8.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     */
    public final class Signal8Default3<A, B, C, D, E, F, G, H> extends QMetaObject.AbstractSignal8Default3<A, B, C, D, E, F, G, H> {
        
        @SuppressWarnings("unused")
        private Signal8Default3() {
        }
        
        public Signal8Default3(Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default){
            super(arg6Default, arg7Default, arg8Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes eight
     * parameters and providing default values for parameters 5 to 8.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     */
    public final class Signal8Default4<A, B, C, D, E, F, G, H> extends QMetaObject.AbstractSignal8Default4<A, B, C, D, E, F, G, H> {
        
        @SuppressWarnings("unused")
        private Signal8Default4() {
        }
        
        public Signal8Default4(Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default){
            super(arg5Default, arg6Default, arg7Default, arg8Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes eight
     * parameters and providing default values for parameters 4 to 8.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     */
    public final class Signal8Default5<A, B, C, D, E, F, G, H> extends QMetaObject.AbstractSignal8Default5<A, B, C, D, E, F, G, H> {
        
        @SuppressWarnings("unused")
        private Signal8Default5() {
        }
        
        public Signal8Default5(Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default){
            super(arg4Default, arg5Default, arg6Default, arg7Default, arg8Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes eight
     * parameters and providing default values for parameters 3 to 8.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     */
    public final class Signal8Default6<A, B, C, D, E, F, G, H> extends QMetaObject.AbstractSignal8Default6<A, B, C, D, E, F, G, H> {
        
        @SuppressWarnings("unused")
        private Signal8Default6() {
        }
        
        public Signal8Default6(Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default){
            super(arg3Default, arg4Default, arg5Default, arg6Default, arg7Default, arg8Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes eight
     * parameters and providing default values for parameters 2 to 8.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     */
    public final class Signal8Default7<A, B, C, D, E, F, G, H> extends QMetaObject.AbstractSignal8Default7<A, B, C, D, E, F, G, H> {
        
        @SuppressWarnings("unused")
        private Signal8Default7() {
        }
        
        public Signal8Default7(Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default){
            super(arg2Default, arg3Default, arg4Default, arg5Default, arg6Default, arg7Default, arg8Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes eight
     * parameters and providing default values for parameters 1 to 8.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     */
    public final class Signal8Default8<A, B, C, D, E, F, G, H> extends QMetaObject.AbstractSignal8Default7<A, B, C, D, E, F, G, H> implements QMetaObject.Emitable0, QMetaObject.Connectable0 {
        
        @SuppressWarnings("unused")
        private Signal8Default8() {
            this.arg1Default = null;
        }
        
        public Signal8Default8(Supplier<? extends A> arg1Default, Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default){
            super(arg2Default, arg3Default, arg4Default, arg5Default, arg6Default, arg7Default, arg8Default);
            this.arg1Default = arg1Default;
            if(this.arg1Default==null){
                throw new QNoDefaultValueException(1);
            }
        }
        
        private final Supplier<? extends A> arg1Default;
        
        @Override
        public final void emit() {
            emitDefaultSignal(arg1Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes nine
     * parameters and providing a default value for parameter no. 9.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     */
    public final class Signal9Default1<A, B, C, D, E, F, G, H, I> extends QMetaObject.AbstractSignal9Default1<A, B, C, D, E, F, G, H, I>{
        
        @SuppressWarnings("unused")
        private Signal9Default1() {
        }
        
        public Signal9Default1(Supplier<? extends I> arg9Default){
            super(arg9Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes nine
     * parameters and providing default values for parameters 8 and 9.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     */
    public final class Signal9Default2<A, B, C, D, E, F, G, H, I> extends QMetaObject.AbstractSignal9Default2<A, B, C, D, E, F, G, H, I> {
        
        @SuppressWarnings("unused")
        private Signal9Default2() {
        }
        
        public Signal9Default2(Supplier<? extends H> arg8Default, Supplier<? extends I> arg9Default){
            super(arg8Default, arg9Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes nine
     * parameters and providing default values for parameters 7 to 9.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     */
    public final class Signal9Default3<A, B, C, D, E, F, G, H, I> extends QMetaObject.AbstractSignal9Default3<A, B, C, D, E, F, G, H, I> {
        
        @SuppressWarnings("unused")
        private Signal9Default3() {
        }
        
        public Signal9Default3(Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default, Supplier<? extends I> arg9Default){
            super(arg7Default, arg8Default, arg9Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes nine
     * parameters and providing default values for parameters 6 to 9.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     */
    public final class Signal9Default4<A, B, C, D, E, F, G, H, I> extends QMetaObject.AbstractSignal9Default4<A, B, C, D, E, F, G, H, I> {
        
        @SuppressWarnings("unused")
        private Signal9Default4() {
        }
        
        public Signal9Default4(Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default, Supplier<? extends I> arg9Default){
            super(arg6Default, arg7Default, arg8Default, arg9Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes nine
     * parameters and providing default values for parameters 5 to 9.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     */
    public final class Signal9Default5<A, B, C, D, E, F, G, H, I> extends QMetaObject.AbstractSignal9Default5<A, B, C, D, E, F, G, H, I> {
        
        @SuppressWarnings("unused")
        private Signal9Default5() {
        }
        
        public Signal9Default5(Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default, Supplier<? extends I> arg9Default){
            super(arg5Default, arg6Default, arg7Default, arg8Default, arg9Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes nine
     * parameters and providing default values for parameters 4 to 9.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     */
    public final class Signal9Default6<A, B, C, D, E, F, G, H, I> extends QMetaObject.AbstractSignal9Default6<A, B, C, D, E, F, G, H, I> {
        
        @SuppressWarnings("unused")
        private Signal9Default6() {
        }
        
        public Signal9Default6(Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default, Supplier<? extends I> arg9Default){
            super(arg4Default, arg5Default, arg6Default, arg7Default, arg8Default, arg9Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes nine
     * parameters and providing default values for parameters 3 to 9.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     */
    public final class Signal9Default7<A, B, C, D, E, F, G, H, I> extends QMetaObject.AbstractSignal9Default7<A, B, C, D, E, F, G, H, I> {
        
        @SuppressWarnings("unused")
        private Signal9Default7() {
        }
        
        public Signal9Default7(Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default, Supplier<? extends I> arg9Default){
            super(arg3Default, arg4Default, arg5Default, arg6Default, arg7Default, arg8Default, arg9Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes nine
     * parameters and providing default values for parameters 2 to 9.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     */
    public final class Signal9Default8<A, B, C, D, E, F, G, H, I> extends QMetaObject.AbstractSignal9Default8<A, B, C, D, E, F, G, H, I> {
        
        @SuppressWarnings("unused")
        private Signal9Default8() {
        }
        
        public Signal9Default8(Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default, Supplier<? extends I> arg9Default){
            super(arg2Default, arg3Default, arg4Default, arg5Default, arg6Default, arg7Default, arg8Default, arg9Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes nine
     * parameters and providing default values for parameters 1 to 9.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     */
    public final class Signal9Default9<A, B, C, D, E, F, G, H, I> extends QMetaObject.AbstractSignal9Default8<A, B, C, D, E, F, G, H, I> implements QMetaObject.Emitable0, QMetaObject.Connectable0 {
        
        @SuppressWarnings("unused")
        private Signal9Default9() {
            this.arg1Default = null;
        }
        
        public Signal9Default9(Supplier<? extends A> arg1Default, Supplier<? extends B> arg2Default, Supplier<? extends C> arg3Default, Supplier<? extends D> arg4Default, Supplier<? extends E> arg5Default, Supplier<? extends F> arg6Default, Supplier<? extends G> arg7Default, Supplier<? extends H> arg8Default, Supplier<? extends I> arg9Default){
            super(arg2Default, arg3Default, arg4Default, arg5Default, arg6Default, arg7Default, arg8Default, arg9Default);
            this.arg1Default = arg1Default;
            if(this.arg1Default==null){
                throw new QNoDefaultValueException(1);
            }
        }
        
        private final Supplier<? extends A> arg1Default;
        
        @Override
        public final void emit() {
            emitDefaultSignal(arg1Default);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes no
     * parameters.
     *
     * Private signals do not have an emit method, as they can only be
     * emitted from inside the declaring class.
     */
    public final class PrivateSignal0 extends QMetaObject.AbstractPrivateSignal0 {
                
        public PrivateSignal0() {
            super();
        }
        
        /**
         * Emits the signal.
         */
        @io.qt.QtUninvokable
        private final void emit() {
            emitSignal();
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes one
     * parameter.
     *
     * Private signals do not have an emit method, as they can only be
     * emitted from inside the declaring class.
     *
     * @param <A> The type of the single parameter of the signal.
     */
    public final class PrivateSignal1<A> extends QMetaObject.AbstractPrivateSignal1<A> {
                
        public PrivateSignal1() {
            super();
        }
        
        /**
         * Emits the signal.
         */
        @io.qt.QtUninvokable
        private final void emit(A arg1) {
            emitSignal(arg1);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes two
     * parameters.
     *
     * Private signals do not have an emit method, as they can only be
     * emitted from inside the declaring class.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     */
    public final class PrivateSignal2<A, B> extends QMetaObject.AbstractPrivateSignal2<A, B> {
                
        public PrivateSignal2() {
            super();
        }
        
        /**
         * Emits the signal.
         */
        @io.qt.QtUninvokable
        private final void emit(A arg1, B arg2) {
            emitSignal(arg1, arg2);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes three
     * parameters.
     *
     * Private signals do not have an emit method, as they can only be
     * emitted from inside the declaring class.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     */
    public final class PrivateSignal3<A, B, C> extends QMetaObject.AbstractPrivateSignal3<A, B, C> {
                
        public PrivateSignal3() {
            super();
        }
        
        /**
         * Emits the signal.
         */
        @io.qt.QtUninvokable
        private final void emit(A arg1, B arg2, C arg3) {
            emitSignal(arg1, arg2, arg3);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes four
     * parameters.
     *
     * Private signals do not have an emit method, as they can only be
     * emitted from inside the declaring class.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     */
    
    public final class PrivateSignal4<A, B, C, D> extends QMetaObject.AbstractPrivateSignal4<A, B, C, D> {
                
        public PrivateSignal4() {
            super();
        }
        
        /**
         * Emits the signal.
         */
        @io.qt.QtUninvokable
        private final void emit(A arg1, B arg2, C arg3, D arg4) {
            emitSignal(arg1, arg2, arg3, arg4);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes five
     * parameters.
     *
     * Private signals do not have an emit method, as they can only be
     * emitted from inside the declaring class.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     */
    public final class PrivateSignal5<A, B, C, D, E> extends QMetaObject.AbstractPrivateSignal5<A,B,C,D,E> {
                
        public PrivateSignal5() {
            super();
        }
        
        /**
         * Emits the signal.
         */
        @io.qt.QtUninvokable
        private final void emit(A arg1, B arg2, C arg3, D arg4, E arg5) {
            emitSignal(arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes six
     * parameters.
     *
     * Private signals do not have an emit method, as they can only be
     * emitted from inside the declaring class.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     */
    public final class PrivateSignal6<A, B, C, D, E, F> extends QMetaObject.AbstractPrivateSignal6<A, B, C, D, E, F> {
                
        public PrivateSignal6() {
            super();
        }
        
        /**
         * Emits the signal.
         */
        @io.qt.QtUninvokable
        private final void emit(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6) {
            emitSignal(arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes seven
     * parameters.
     *
     * Private signals do not have an emit method, as they can only be
     * emitted from inside the declaring class.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     */
    public final class PrivateSignal7<A, B, C, D, E, F, G> extends QMetaObject.AbstractPrivateSignal7<A, B, C, D, E, F, G> {
                
        public PrivateSignal7() {
            super();
        }
        
        /**
         * Emits the signal.
         */
        @io.qt.QtUninvokable
        private final void emit(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7) {
            emitSignal(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes eight
     * parameters.
     *
     * Private signals do not have an emit method, as they can only be
     * emitted from inside the declaring class.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     */
    public final class PrivateSignal8<A, B, C, D, E, F, G, H> extends QMetaObject.AbstractPrivateSignal8<A, B, C, D, E, F, G, H> {
                
        public PrivateSignal8() {
            super();
        }
        
        /**
         * Emits the signal.
         */
        @io.qt.QtUninvokable
        private final void emit(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6,
                G arg7, H arg8) {
            emitSignal(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Declare and instantiate a field of this class in your
     * QObject subclass to declare a signal that takes nine
     * parameters.
     *
     * Private signals do not have an emit method, as they can only be
     * emitted from inside the declaring class.
     *
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     */
    public final class PrivateSignal9<A, B, C, D, E, F, G, H, I> extends QMetaObject.AbstractPrivateSignal9<A, B, C, D, E, F, G, H, I> {
                
        public PrivateSignal9() {
            super();
        }
        
        /**
         * Emits the signal.
         */
        @io.qt.QtUninvokable
        private final void emit(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6,
                G arg7, H arg8, I arg9) {
            emitSignal(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    }
    
    /**
     * Emits a private signal. This method may only be called from inside the signal owning object.
     * 
     * @param signal the signal to be emitted
     * @throws io.qt.QSignalAccessException if signal is emitted from outside the declaring class.
     */
    protected static void emit(PrivateSignal0 signal) throws io.qt.QSignalAccessException {
        Class<?> callerClass = QtJambi_LibraryUtilities.internal.callerClassProvider().get();
        Class<?> signalDeclaringClass = signal.signalDeclaringClass();
        if(callerClass==signalDeclaringClass || (MultiSignal.class.isAssignableFrom(callerClass) && callerClass.getEnclosingClass()==signalDeclaringClass)) {
            signal.emit();
        }else {
            throw new io.qt.QSignalAccessException(signalDeclaringClass, signal.name(), callerClass);
        }
    }
    
    /**
     * Emits a private signal. This method may only be called from inside the signal owning object.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be emitted
     * @param arg1 The argument for the first parameter of the signal.
     * @throws io.qt.QSignalAccessException if signal is emitted from outside the declaring class.
     */
    protected static <A> void emit(PrivateSignal1<A> signal, A arg1) throws io.qt.QSignalAccessException {
        Class<?> callerClass = QtJambi_LibraryUtilities.internal.callerClassProvider().get();
        Class<?> signalDeclaringClass = signal.signalDeclaringClass();
        if(callerClass==signalDeclaringClass || (MultiSignal.class.isAssignableFrom(callerClass) && callerClass.getEnclosingClass()==signalDeclaringClass)) {
            signal.emit(arg1);
        }else {
            throw new io.qt.QSignalAccessException(signalDeclaringClass, signal.name(), callerClass);
        }
    }
    
    /**
     * Emits a private signal. This method may only be called from inside the signal owning object.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be emitted
     * @param arg1 The argument for the first parameter of the signal.
     * @param arg2 The argument for the second parameter of the signal.
     * @throws io.qt.QSignalAccessException if signal is emitted from outside the declaring class.
     */
    protected static <A,B> void emit(PrivateSignal2<A,B> signal, A arg1, B arg2) throws io.qt.QSignalAccessException {
        Class<?> callerClass = QtJambi_LibraryUtilities.internal.callerClassProvider().get();
        Class<?> signalDeclaringClass = signal.signalDeclaringClass();
        if(callerClass==signalDeclaringClass || (MultiSignal.class.isAssignableFrom(callerClass) && callerClass.getEnclosingClass()==signalDeclaringClass)) {
            signal.emit(arg1, arg2);
        }else {
            throw new io.qt.QSignalAccessException(signalDeclaringClass, signal.name(), callerClass);
        }
    }
    
    /**
     * Emits a private signal. This method may only be called from inside the signal owning object.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be emitted
     * @param arg1 The argument for the first parameter of the signal.
     * @param arg2 The argument for the second parameter of the signal.
     * @param arg3 The argument for the third parameter of the signal.
     * @throws io.qt.QSignalAccessException if signal is emitted from outside the declaring class.
     */
    protected static <A,B,C> void emit(PrivateSignal3<A,B,C> signal, A arg1, B arg2, C arg3) throws io.qt.QSignalAccessException {
        Class<?> callerClass = QtJambi_LibraryUtilities.internal.callerClassProvider().get();
        Class<?> signalDeclaringClass = signal.signalDeclaringClass();
        if(callerClass==signalDeclaringClass || (MultiSignal.class.isAssignableFrom(callerClass) && callerClass.getEnclosingClass()==signalDeclaringClass)) {
            signal.emit(arg1, arg2, arg3);
        }else {
            throw new io.qt.QSignalAccessException(signalDeclaringClass, signal.name(), callerClass);
        }
    }
    
    /**
     * Emits a private signal. This method may only be called from inside the signal owning object.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be emitted
     * @param arg1 The argument for the first parameter of the signal.
     * @param arg2 The argument for the second parameter of the signal.
     * @param arg3 The argument for the third parameter of the signal.
     * @param arg4 The argument for the fourth parameter of the signal.
     * @throws io.qt.QSignalAccessException if signal is emitted from outside the declaring class.
     */
    protected static <A,B,C,D> void emit(PrivateSignal4<A,B,C,D> signal, A arg1, B arg2, C arg3, D arg4) throws io.qt.QSignalAccessException {
        Class<?> callerClass = QtJambi_LibraryUtilities.internal.callerClassProvider().get();
        Class<?> signalDeclaringClass = signal.signalDeclaringClass();
        if(callerClass==signalDeclaringClass || (MultiSignal.class.isAssignableFrom(callerClass) && callerClass.getEnclosingClass()==signalDeclaringClass)) {
            signal.emit(arg1, arg2, arg3, arg4);
        }else {
            throw new io.qt.QSignalAccessException(signalDeclaringClass, signal.name(), callerClass);
        }
    }
    
    /**
     * Emits a private signal. This method may only be called from inside the signal owning object.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be emitted
     * @param arg1 The argument for the first parameter of the signal.
     * @param arg2 The argument for the second parameter of the signal.
     * @param arg3 The argument for the third parameter of the signal.
     * @param arg4 The argument for the fourth parameter of the signal.
     * @param arg5 The argument for the fifth parameter of the signal.
     * @throws io.qt.QSignalAccessException if signal is emitted from outside the declaring class.
     */
    protected static <A,B,C,D,E> void emit(PrivateSignal5<A,B,C,D,E> signal, A arg1, B arg2, C arg3, D arg4, E arg5) throws io.qt.QSignalAccessException {
        Class<?> callerClass = QtJambi_LibraryUtilities.internal.callerClassProvider().get();
        Class<?> signalDeclaringClass = signal.signalDeclaringClass();
        if(callerClass==signalDeclaringClass || (MultiSignal.class.isAssignableFrom(callerClass) && callerClass.getEnclosingClass()==signalDeclaringClass)) {
            signal.emit(arg1, arg2, arg3, arg4, arg5);
        }else {
            throw new io.qt.QSignalAccessException(signalDeclaringClass, signal.name(), callerClass);
        }
    }
    
    /**
     * Emits a private signal. This method may only be called from inside the signal owning object.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be emitted
     * @param arg1 The argument for the first parameter of the signal.
     * @param arg2 The argument for the second parameter of the signal.
     * @param arg3 The argument for the third parameter of the signal.
     * @param arg4 The argument for the fourth parameter of the signal.
     * @param arg5 The argument for the fifth parameter of the signal.
     * @param arg6 The argument for the sixth parameter of the signal.
     * @throws io.qt.QSignalAccessException if signal is emitted from outside the declaring class.
     */
    protected static <A,B,C,D,E,F> void emit(PrivateSignal6<A,B,C,D,E,F> signal, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6) throws io.qt.QSignalAccessException {
        Class<?> callerClass = QtJambi_LibraryUtilities.internal.callerClassProvider().get();
        Class<?> signalDeclaringClass = signal.signalDeclaringClass();
        if(callerClass==signalDeclaringClass || (MultiSignal.class.isAssignableFrom(callerClass) && callerClass.getEnclosingClass()==signalDeclaringClass)) {
            signal.emit(arg1, arg2, arg3, arg4, arg5, arg6);
        }else {
            throw new io.qt.QSignalAccessException(signalDeclaringClass, signal.name(), callerClass);
        }
    }
    
    /**
     * Emits a private signal. This method may only be called from inside the signal owning object.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be emitted
     * @param arg1 The argument for the first parameter of the signal.
     * @param arg2 The argument for the second parameter of the signal.
     * @param arg3 The argument for the third parameter of the signal.
     * @param arg4 The argument for the fourth parameter of the signal.
     * @param arg5 The argument for the fifth parameter of the signal.
     * @param arg6 The argument for the sixth parameter of the signal.
     * @param arg7 The argument for the seventh parameter of the signal.
     * @throws io.qt.QSignalAccessException if signal is emitted from outside the declaring class.
     */
    protected static <A,B,C,D,E,F,G> void emit(PrivateSignal7<A,B,C,D,E,F,G> signal, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7) throws io.qt.QSignalAccessException {
        Class<?> callerClass = QtJambi_LibraryUtilities.internal.callerClassProvider().get();
        Class<?> signalDeclaringClass = signal.signalDeclaringClass();
        if(callerClass==signalDeclaringClass || (MultiSignal.class.isAssignableFrom(callerClass) && callerClass.getEnclosingClass()==signalDeclaringClass)) {
            signal.emit(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        }else {
            throw new io.qt.QSignalAccessException(signalDeclaringClass, signal.name(), callerClass);
        }
    }
    
    /**
     * Emits a private signal. This method may only be called from inside the signal owning object.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param signal the signal to be emitted
     * @param arg1 The argument for the first parameter of the signal.
     * @param arg2 The argument for the second parameter of the signal.
     * @param arg3 The argument for the third parameter of the signal.
     * @param arg4 The argument for the fourth parameter of the signal.
     * @param arg5 The argument for the fifth parameter of the signal.
     * @param arg6 The argument for the sixth parameter of the signal.
     * @param arg7 The argument for the seventh parameter of the signal.
     * @param arg8 The argument for the eighth parameter of the signal.
     * @throws io.qt.QSignalAccessException if signal is emitted from outside the declaring class.
     */
    protected static <A,B,C,D,E,F,G,H> void emit(PrivateSignal8<A,B,C,D,E,F,G,H> signal, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8) throws io.qt.QSignalAccessException {
        Class<?> callerClass = QtJambi_LibraryUtilities.internal.callerClassProvider().get();
        Class<?> signalDeclaringClass = signal.signalDeclaringClass();
        if(callerClass==signalDeclaringClass || (MultiSignal.class.isAssignableFrom(callerClass) && callerClass.getEnclosingClass()==signalDeclaringClass)) {
            signal.emit(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        }else {
            throw new io.qt.QSignalAccessException(signalDeclaringClass, signal.name(), callerClass);
        }
    }
    
    /**
     * Emits a private signal. This method may only be called from inside the signal owning object.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     * @param signal the signal to be emitted
     * @param arg1 The argument for the first parameter of the signal.
     * @param arg2 The argument for the second parameter of the signal.
     * @param arg3 The argument for the third parameter of the signal.
     * @param arg4 The argument for the fourth parameter of the signal.
     * @param arg5 The argument for the fifth parameter of the signal.
     * @param arg6 The argument for the sixth parameter of the signal.
     * @param arg7 The argument for the seventh parameter of the signal.
     * @param arg8 The argument for the eighth parameter of the signal.
     * @param arg9 The argument for the ninth parameter of the signal.
     * @throws io.qt.QSignalAccessException if signal is emitted from outside the declaring class.
     */
    protected static <A,B,C,D,E,F,G,H,I> void emit(PrivateSignal9<A,B,C,D,E,F,G,H,I> signal, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8, I arg9) throws io.qt.QSignalAccessException {
        Class<?> callerClass = QtJambi_LibraryUtilities.internal.callerClassProvider().get();
        Class<?> signalDeclaringClass = signal.signalDeclaringClass();
        if(callerClass==signalDeclaringClass || (MultiSignal.class.isAssignableFrom(callerClass) && callerClass.getEnclosingClass()==signalDeclaringClass)) {
            signal.emit(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
        }else {
            throw new io.qt.QSignalAccessException(signalDeclaringClass, signal.name(), callerClass);
        }
    }
    
    /**
     * Wrapper class supporting the definition of overloaded signal in a class.
     * To implement multi signals subclass this MultiSignal class and add overloaded emit methods.
     */
    public abstract class MultiSignal extends QMetaObject.AbstractMultiSignal {
        protected MultiSignal(){
            super();
        }
        
        /**
         * Returns the object containing this signal.
         * @return the signal containing object
         */
        @Override
        public final QObject containingObject() {
            return QObject.this;
        }
    
        /**
         * Removes the given connection from this signal.
         *
         * @param connection the connection to be removed
         * @return <code>true</code> if the disconnection was successful.
         */
        public final boolean disconnect(QMetaObject.Connection connection) {
            return super.disconnect(connection);
        }
        
        /**
         * Disconnects all overloaded signals.
         * @return <code>true</code> if any disconnection was successful.
         */
        @Override
        public final boolean disconnectAll() {
            return super.disconnectAll();
        }
        
        /**
         * Connects the signal to a method in an object. Whenever it is emitted, the method will be invoked
         * on the given object.
         *
         * @param receiver  The object that owns the method
         * @param method    The signature of the method excluding return type and argument names, such as "setText(String)".
         * @param connectionType One of the connection types defined in the Qt interface.
         * @throws QNoSuchSlotException Raised if the method passed in the slot object was not found
         * @throws java.lang.RuntimeException Raised if the signal object could not be successfully introspected or if the
         *                                    signatures of the signal and slot are incompatible.
         */
        public final QMetaObject.Connection connect(Object receiver, String method, Qt.ConnectionType... type) {
            if (receiver == null)
                throw new NullPointerException("Receiver must be non-null");
            if (method == null)
                throw new NullPointerException("Slot must be non-null");
            return super.connect(receiver, method, type);
        }
        
        /**
         * Disconnects the signal from a method in an object if the two were previously connected by a call to connect.
         *
         * @param receiver The object to which the signal is connected
         * @param method The method in the receiver object to which the signal is connected
         * @return true if the connection was successfully removed, otherwise false. The method will return false if the
         * connection has not been previously established by a call to connect.
         * @throws QNoSuchSlotException Raised if the method passed in the slot object was not found
         */
        public final boolean disconnect(Object receiver, String method) {
            if (method != null && receiver == null)
                throw new IllegalArgumentException("Receiver cannot be null if you specify a method");
            return super.disconnect(receiver, method);
        }
        
        /**
         * Initializes a connection to the <i>slot</i>.
         * 
         * @param slot the slot to be connected
         * @param connectionType type of connection
         * @return connection if successful or <code>null</code> otherwise
         * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
         * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
         */
        public final io.qt.core.QMetaObject.Connection connect(io.qt.core.QMetaObject.Slot0 slot, io.qt.core.Qt.ConnectionType... connectionType) throws io.qt.QNoSuchSignalException{
            return super.connect(slot, connectionType);
        }

        /**
         * Removes the connection to the given <i>slot</i>.
         * 
         * @param slot the slot to be disconnected
         * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
         */
        public final boolean disconnect(io.qt.core.QMetaObject.Slot0 slot) {
            return super.disconnect(slot);
        }
        
        /**
         * Initializes a connection to the <i>signal</i>.
         * 
         * @param signal the signal to be connected
         * @param connectionType type of connection
         * @return connection if successful or <code>null</code> otherwise
         * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
         */
        public final io.qt.core.QMetaObject.Connection connect(io.qt.core.QMetaObject.Connectable0 signal, io.qt.core.Qt.ConnectionType... connectionType) throws io.qt.QNoSuchSignalException{
            return super.connect((QMetaObject.AbstractSignal)signal, connectionType);
        }
    
        /**
         * Removes the connection to the given <i>signal</i>.
         * 
         * @param signal the signal to be disconnected
         * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
         */
        public final boolean disconnect(io.qt.core.QMetaObject.Connectable0 signal) {
            return super.disconnect((QMetaObject.AbstractSignal)signal);
        }
        
        /**
         * Initializes a connection to the <i>signal</i>.
         * 
         * @param signal the signal to be connected
         * @param connectionType type of connection
         * @return connection if successful or <code>null</code> otherwise
         * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
         */
        public final io.qt.core.QMetaObject.Connection connect(io.qt.core.QMetaObject.AbstractPublicSignal0 signal, io.qt.core.Qt.ConnectionType... connectionType) throws io.qt.QNoSuchSignalException{
            return super.connect(signal, connectionType);
        }
    
        /**
         * Removes the connection to the given <i>signal</i>.
         * 
         * @param signal the signal to be disconnected
         * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
         */
        public final boolean disconnect(io.qt.core.QMetaObject.AbstractPublicSignal0 signal) {
            return super.disconnect(signal);
        }
        
        @io.qt.QtUninvokable
        private QMetaObject.Connection connectSlot(QMetaObject.AbstractSlot slotObject, Qt.ConnectionType[] connectionType) {
            return super.connect(slotObject, connectionType);
        }
        
        @io.qt.QtUninvokable
        private boolean disconnectSlot(QMetaObject.AbstractSlot slotObject) {
            return super.disconnect(slotObject);
        }
        
        @io.qt.QtUninvokable
        private QMetaObject.Connection connectSignal(QMetaObject.AbstractSignal slotObject, Qt.ConnectionType[] connectionType) {
            return super.connect(slotObject, connectionType);
        }
        
        @io.qt.QtUninvokable
        private boolean disconnectSignal(QMetaObject.AbstractSignal slotObject) {
            return super.disconnect(slotObject);
        }
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal0 signal, QMetaObject.Slot0 slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal1<?> signal, QMetaObject.Slot0 slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal1<A> signal, QMetaObject.Slot1<? super A> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal2<?,?> signal, QMetaObject.Slot0 slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal2<A,?> signal, QMetaObject.Slot1<? super A> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal2<A,B> signal, QMetaObject.Slot2<? super A,? super B> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal3<?,?,?> signal, QMetaObject.Slot0 slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal3<A,?,?> signal, QMetaObject.Slot1<? super A> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal3<A,B,?> signal, QMetaObject.Slot2<? super A,? super B> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal3<A,B,C> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal4<?,?,?,?> signal, QMetaObject.Slot0 slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal4<A,?,?,?> signal, QMetaObject.Slot1<? super A> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal4<A,B,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal4<A,B,C,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal4<A,B,C,D> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal5<?,?,?,?,?> signal, QMetaObject.Slot0 slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal5<A,?,?,?,?> signal, QMetaObject.Slot1<? super A> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal5<A,B,?,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal5<A,B,C,?,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal5<A,B,C,D,?> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal5<A,B,C,D,E> signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal6<?,?,?,?,?,?> signal, QMetaObject.Slot0 slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal6<A,?,?,?,?,?> signal, QMetaObject.Slot1<? super A> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal6<A,B,?,?,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal6<A,B,C,?,?,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal6<A,B,C,D,?,?> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal6<A,B,C,D,E,?> signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E,F> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal6<A,B,C,D,E,F> signal, QMetaObject.Slot6<? super A,? super B,? super C,? super D,? super E,? super F> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal7<?,?,?,?,?,?,?> signal, QMetaObject.Slot0 slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal7<A,?,?,?,?,?,?> signal, QMetaObject.Slot1<? super A> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal7<A,B,?,?,?,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal7<A,B,C,?,?,?,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal7<A,B,C,D,?,?,?> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal7<A,B,C,D,E,?,?> signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E,F> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal7<A,B,C,D,E,F,?> signal, QMetaObject.Slot6<? super A,? super B,? super C,? super D,? super E,? super F> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E,F,G> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal7<A,B,C,D,E,F,G> signal, QMetaObject.Slot7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal8<?,?,?,?,?,?,?,?> signal, QMetaObject.Slot0 slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal8<A,?,?,?,?,?,?,?> signal, QMetaObject.Slot1<? super A> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal8<A,B,?,?,?,?,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal8<A,B,C,?,?,?,?,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,?,?,?,?> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,E,?,?,?> signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E,F> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,E,F,?,?> signal, QMetaObject.Slot6<? super A,? super B,? super C,? super D,? super E,? super F> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E,F,G> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,E,F,G,?> signal, QMetaObject.Slot7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E,F,G,H> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,E,F,G,H> signal, QMetaObject.Slot8<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal9<?,?,?,?,?,?,?,?,?> signal, QMetaObject.Slot0 slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal9<A,?,?,?,?,?,?,?,?> signal, QMetaObject.Slot1<? super A> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal9<A,B,?,?,?,?,?,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal9<A,B,C,?,?,?,?,?,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,?,?,?,?,?> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,?,?,?,?> signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E,F> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,F,?,?,?> signal, QMetaObject.Slot6<? super A,? super B,? super C,? super D,? super E,? super F> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E,F,G> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,F,G,?,?> signal, QMetaObject.Slot7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E,F,G,H> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,F,G,H,?> signal, QMetaObject.Slot8<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     */
    public static <A,B,C,D,E,F,G,H,I> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,F,G,H,I> signal, QMetaObject.Slot9<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H,? super I> slot, Qt.ConnectionType... connectionType) {
        return signal.connect(slot, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QMetaObject.AbstractPrivateSignal0 signal, QMetaObject.Slot0 slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QMetaObject.AbstractPrivateSignal1<?> signal, QMetaObject.Slot0 slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(QMetaObject.AbstractPrivateSignal1<A> signal, QMetaObject.Slot1<? super A> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QMetaObject.AbstractPrivateSignal2<?,?> signal, QMetaObject.Slot0 slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(QMetaObject.AbstractPrivateSignal2<A,?> signal, QMetaObject.Slot1<? super A> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B> boolean disconnect(QMetaObject.AbstractPrivateSignal2<A,B> signal, QMetaObject.Slot2<? super A,? super B> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QMetaObject.AbstractPrivateSignal3<?,?,?> signal, QMetaObject.Slot0 slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(QMetaObject.AbstractPrivateSignal3<A,?,?> signal, QMetaObject.Slot1<? super A> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B> boolean disconnect(QMetaObject.AbstractPrivateSignal3<A,B,?> signal, QMetaObject.Slot2<? super A,? super B> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C> boolean disconnect(QMetaObject.AbstractPrivateSignal3<A,B,C> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QMetaObject.AbstractPrivateSignal4<?,?,?,?> signal, QMetaObject.Slot0 slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(QMetaObject.AbstractPrivateSignal4<A,?,?,?> signal, QMetaObject.Slot1<? super A> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B> boolean disconnect(QMetaObject.AbstractPrivateSignal4<A,B,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C> boolean disconnect(QMetaObject.AbstractPrivateSignal4<A,B,C,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D> boolean disconnect(QMetaObject.AbstractPrivateSignal4<A,B,C,D> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QMetaObject.AbstractPrivateSignal5<?,?,?,?,?> signal, QMetaObject.Slot0 slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(QMetaObject.AbstractPrivateSignal5<A,?,?,?,?> signal, QMetaObject.Slot1<? super A> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B> boolean disconnect(QMetaObject.AbstractPrivateSignal5<A,B,?,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C> boolean disconnect(QMetaObject.AbstractPrivateSignal5<A,B,C,?,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D> boolean disconnect(QMetaObject.AbstractPrivateSignal5<A,B,C,D,?> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E> boolean disconnect(QMetaObject.AbstractPrivateSignal5<A,B,C,D,E> signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QMetaObject.AbstractPrivateSignal6<?,?,?,?,?,?> signal, QMetaObject.Slot0 slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(QMetaObject.AbstractPrivateSignal6<A,?,?,?,?,?> signal, QMetaObject.Slot1<? super A> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B> boolean disconnect(QMetaObject.AbstractPrivateSignal6<A,B,?,?,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C> boolean disconnect(QMetaObject.AbstractPrivateSignal6<A,B,C,?,?,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D> boolean disconnect(QMetaObject.AbstractPrivateSignal6<A,B,C,D,?,?> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E> boolean disconnect(QMetaObject.AbstractPrivateSignal6<A,B,C,D,E,?> signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F> boolean disconnect(QMetaObject.AbstractPrivateSignal6<A,B,C,D,E,F> signal, QMetaObject.Slot6<? super A,? super B,? super C,? super D,? super E,? super F> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QMetaObject.AbstractPrivateSignal7<?,?,?,?,?,?,?> signal, QMetaObject.Slot0 slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(QMetaObject.AbstractPrivateSignal7<A,?,?,?,?,?,?> signal, QMetaObject.Slot1<? super A> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B> boolean disconnect(QMetaObject.AbstractPrivateSignal7<A,B,?,?,?,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C> boolean disconnect(QMetaObject.AbstractPrivateSignal7<A,B,C,?,?,?,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D> boolean disconnect(QMetaObject.AbstractPrivateSignal7<A,B,C,D,?,?,?> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E> boolean disconnect(QMetaObject.AbstractPrivateSignal7<A,B,C,D,E,?,?> signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F> boolean disconnect(QMetaObject.AbstractPrivateSignal7<A,B,C,D,E,F,?> signal, QMetaObject.Slot6<? super A,? super B,? super C,? super D,? super E,? super F> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G> boolean disconnect(QMetaObject.AbstractPrivateSignal7<A,B,C,D,E,F,G> signal, QMetaObject.Slot7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QMetaObject.AbstractPrivateSignal8<?,?,?,?,?,?,?,?> signal, QMetaObject.Slot0 slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(QMetaObject.AbstractPrivateSignal8<A,?,?,?,?,?,?,?> signal, QMetaObject.Slot1<? super A> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B> boolean disconnect(QMetaObject.AbstractPrivateSignal8<A,B,?,?,?,?,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C> boolean disconnect(QMetaObject.AbstractPrivateSignal8<A,B,C,?,?,?,?,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D> boolean disconnect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,?,?,?,?> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E> boolean disconnect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,E,?,?,?> signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F> boolean disconnect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,E,F,?,?> signal, QMetaObject.Slot6<? super A,? super B,? super C,? super D,? super E,? super F> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G> boolean disconnect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,E,F,G,?> signal, QMetaObject.Slot7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G,H> boolean disconnect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,E,F,G,H> signal, QMetaObject.Slot8<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QMetaObject.AbstractPrivateSignal9<?,?,?,?,?,?,?,?,?> signal, QMetaObject.Slot0 slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(QMetaObject.AbstractPrivateSignal9<A,?,?,?,?,?,?,?,?> signal, QMetaObject.Slot1<? super A> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B> boolean disconnect(QMetaObject.AbstractPrivateSignal9<A,B,?,?,?,?,?,?,?> signal, QMetaObject.Slot2<? super A,? super B> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C> boolean disconnect(QMetaObject.AbstractPrivateSignal9<A,B,C,?,?,?,?,?,?> signal, QMetaObject.Slot3<? super A,? super B,? super C> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D> boolean disconnect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,?,?,?,?,?> signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E> boolean disconnect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,?,?,?,?> signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F> boolean disconnect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,F,?,?,?> signal, QMetaObject.Slot6<? super A,? super B,? super C,? super D,? super E,? super F> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G> boolean disconnect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,F,G,?,?> signal, QMetaObject.Slot7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G,H> boolean disconnect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,F,G,H,?> signal, QMetaObject.Slot8<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G,H,I> boolean disconnect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,F,G,H,I> signal, QMetaObject.Slot9<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H,? super I> slot) {
        return signal.disconnect(slot);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     */
    public static QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal0 signal, QMetaObject.Connectable0 connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connect(connectSignal, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QMetaObject.AbstractPrivateSignal0 signal, QMetaObject.Connectable0 connectSignal) {
        return signal.disconnect(connectSignal);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     */
    public static <A> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal1<A> signal, QMetaObject.Connectable1<? super A> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connect(connectSignal, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(QMetaObject.AbstractPrivateSignal1<A> signal, QMetaObject.Connectable1<? super A> connectSignal) {
        return signal.disconnect(connectSignal);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     */
    public static <A,B> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal2<A,B> signal, QMetaObject.Connectable2<? super A,? super B> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connect(connectSignal, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B> boolean disconnect(QMetaObject.AbstractPrivateSignal2<A,B> signal, QMetaObject.Connectable2<? super A,? super B> connectSignal) {
        return signal.disconnect(connectSignal);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     */
    public static <A,B,C> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal3<A,B,C> signal, QMetaObject.Connectable3<? super A,? super B,? super C> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connect(connectSignal, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C> boolean disconnect(QMetaObject.AbstractPrivateSignal3<A,B,C> signal, QMetaObject.Connectable3<? super A,? super B,? super C> connectSignal) {
        return signal.disconnect(connectSignal);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     */
    public static <A,B,C,D> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal4<A,B,C,D> signal, QMetaObject.Connectable4<? super A,? super B,? super C,? super D> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connect(connectSignal, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D> boolean disconnect(QMetaObject.AbstractPrivateSignal4<A,B,C,D> signal, QMetaObject.Connectable4<? super A,? super B,? super C,? super D> connectSignal) {
        return signal.disconnect(connectSignal);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     */
    public static <A,B,C,D,E> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal5<A,B,C,D,E> signal, QMetaObject.Connectable5<? super A,? super B,? super C,? super D,? super E> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connect(connectSignal, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E> boolean disconnect(QMetaObject.AbstractPrivateSignal5<A,B,C,D,E> signal, QMetaObject.Connectable5<? super A,? super B,? super C,? super D,? super E> connectSignal) {
        return signal.disconnect(connectSignal);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     */
    public static <A,B,C,D,E,F> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal6<A,B,C,D,E,F> signal, QMetaObject.Connectable6<? super A,? super B,? super C,? super D,? super E,? super F> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connect(connectSignal, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F> boolean disconnect(QMetaObject.AbstractPrivateSignal6<A,B,C,D,E,F> signal, QMetaObject.Connectable6<? super A,? super B,? super C,? super D,? super E,? super F> connectSignal) {
        return signal.disconnect(connectSignal);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     */
    public static <A,B,C,D,E,F,G> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal7<A,B,C,D,E,F,G> signal, QMetaObject.Connectable7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connect(connectSignal, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G> boolean disconnect(QMetaObject.AbstractPrivateSignal7<A,B,C,D,E,F,G> signal, QMetaObject.Connectable7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> connectSignal) {
        return signal.disconnect(connectSignal);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     */
    public static <A,B,C,D,E,F,G,H> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,E,F,G,H> signal, QMetaObject.Connectable8<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connect(connectSignal, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G,H> boolean disconnect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,E,F,G,H> signal, QMetaObject.Connectable8<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H> connectSignal) {
        return signal.disconnect(connectSignal);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     */
    public static <A,B,C,D,E,F,G,H,I> QMetaObject.Connection connect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,F,G,H,I> signal, QMetaObject.Connectable9<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H,? super I> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connect(connectSignal, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G,H,I> boolean disconnect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,F,G,H,I> signal, QMetaObject.Connectable9<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H,? super I> connectSignal) {
        return signal.disconnect(connectSignal);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     * @throws io.qt.QMisfittingSignatureException Raised if slot does not match to any of the signatures.
     */
    public static QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Slot0 slot, Qt.ConnectionType... connectionType) {
        return signal.connectSlot(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     * @throws io.qt.QMisfittingSignatureException Raised if slot does not match to any of the signatures.
     */
    public static <A> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Slot1<? super A> slot, Qt.ConnectionType... connectionType) {
        return signal.connectSlot(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     * @throws io.qt.QMisfittingSignatureException Raised if slot does not match to any of the signatures.
     */
    public static <A,B> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Slot2<? super A,? super B> slot, Qt.ConnectionType... connectionType) {
        return signal.connectSlot(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     * @throws io.qt.QMisfittingSignatureException Raised if slot does not match to any of the signatures.
     */
    public static <A,B,C> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Slot3<? super A,? super B,? super C> slot, Qt.ConnectionType... connectionType) {
        return signal.connectSlot(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     * @throws io.qt.QMisfittingSignatureException Raised if slot does not match to any of the signatures.
     */
    public static <A,B,C,D> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot, Qt.ConnectionType... connectionType) {
        return signal.connectSlot(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     * @throws io.qt.QMisfittingSignatureException Raised if slot does not match to any of the signatures.
     */
    public static <A,B,C,D,E> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot, Qt.ConnectionType... connectionType) {
        return signal.connectSlot(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     * @throws io.qt.QMisfittingSignatureException Raised if slot does not match to any of the signatures.
     */
    public static <A,B,C,D,E,F> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Slot6<? super A,? super B,? super C,? super D,? super E,? super F> slot, Qt.ConnectionType... connectionType) {
        return signal.connectSlot(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     * @throws io.qt.QMisfittingSignatureException Raised if slot does not match to any of the signatures.
     */
    public static <A,B,C,D,E,F,G> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Slot7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> slot, Qt.ConnectionType... connectionType) {
        return signal.connectSlot(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     * @throws io.qt.QMisfittingSignatureException Raised if slot does not match to any of the signatures.
     */
    public static <A,B,C,D,E,F,G,H> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Slot8<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H> slot, Qt.ConnectionType... connectionType) {
        return signal.connectSlot(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     * @param signal the signal to be connected
     * @param slot the slot to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QUninvokableSlotException Raised if slot is annotated <code>&commat;QtUninvokable</code>.
     * @throws io.qt.QMisfittingSignatureException Raised if slot does not match to any of the signatures.
     */
    public static <A,B,C,D,E,F,G,H,I> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Slot9<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H,? super I> slot, Qt.ConnectionType... connectionType) {
        return signal.connectSlot(slot, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QMisfittingSignatureException Raised if connectSignal does not match to any of the signatures.
     */
    public static QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Connectable0 connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connectSignal((QMetaObject.AbstractSignal)connectSignal, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QMisfittingSignatureException Raised if connectSignal does not match to any of the signatures.
     */
    public static <A> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Connectable1<? super A> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connectSignal((QMetaObject.AbstractSignal)connectSignal, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QMisfittingSignatureException Raised if connectSignal does not match to any of the signatures.
     */
    public static <A,B> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Connectable2<? super A,? super B> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connectSignal((QMetaObject.AbstractSignal)connectSignal, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QMisfittingSignatureException Raised if connectSignal does not match to any of the signatures.
     */
    public static <A,B,C> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Connectable3<? super A,? super B,? super C> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connectSignal((QMetaObject.AbstractSignal)connectSignal, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QMisfittingSignatureException Raised if connectSignal does not match to any of the signatures.
     */
    public static <A,B,C,D> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Connectable4<? super A,? super B,? super C,? super D> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connectSignal((QMetaObject.AbstractSignal)connectSignal, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QMisfittingSignatureException Raised if connectSignal does not match to any of the signatures.
     */
    public static <A,B,C,D,E> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Connectable5<? super A,? super B,? super C,? super D,? super E> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connectSignal((QMetaObject.AbstractSignal)connectSignal, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QMisfittingSignatureException Raised if connectSignal does not match to any of the signatures.
     */
    public static <A,B,C,D,E,F> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Connectable6<? super A,? super B,? super C,? super D,? super E,? super F> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connectSignal((QMetaObject.AbstractSignal)connectSignal, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QMisfittingSignatureException Raised if connectSignal does not match to any of the signatures.
     */
    public static <A,B,C,D,E,F,G> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Connectable7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connectSignal((QMetaObject.AbstractSignal)connectSignal, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QMisfittingSignatureException Raised if connectSignal does not match to any of the signatures.
     */
    public static <A,B,C,D,E,F,G,H> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Connectable8<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connectSignal((QMetaObject.AbstractSignal)connectSignal, connectionType);
    }
    
    /**
     * Initializes a connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     * @param signal the signal to be connected
     * @param connectSignal the signal to be connected
     * @param connectionType type of connection
     * @return connection if successful or <code>null</code> otherwise
     * @throws io.qt.QMisfittingSignatureException Raised if their signatures are incompatible.
     * @throws io.qt.QMisfittingSignatureException Raised if connectSignal does not match to any of the signatures.
     */
    public static <A,B,C,D,E,F,G,H,I> QMetaObject.Connection connect(MultiSignal signal, QMetaObject.Connectable9<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H,? super I> connectSignal, Qt.ConnectionType... connectionType) {
        return signal.connectSignal((QMetaObject.AbstractSignal)connectSignal, connectionType);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(MultiSignal signal, QMetaObject.Slot0 slot) {
        return signal.disconnectSlot(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(MultiSignal signal, QMetaObject.Slot1<? super A> slot) {
        return signal.disconnectSlot(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B> boolean disconnect(MultiSignal signal, QMetaObject.Slot2<? super A,? super B> slot) {
        return signal.disconnectSlot(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C> boolean disconnect(MultiSignal signal, QMetaObject.Slot3<? super A,? super B,? super C> slot) {
        return signal.disconnectSlot(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D> boolean disconnect(MultiSignal signal, QMetaObject.Slot4<? super A,? super B,? super C,? super D> slot) {
        return signal.disconnectSlot(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E> boolean disconnect(MultiSignal signal, QMetaObject.Slot5<? super A,? super B,? super C,? super D,? super E> slot) {
        return signal.disconnectSlot(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F> boolean disconnect(MultiSignal signal, QMetaObject.Slot6<? super A,? super B,? super C,? super D,? super E,? super F> slot) {
        return signal.disconnectSlot(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G> boolean disconnect(MultiSignal signal, QMetaObject.Slot7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> slot) {
        return signal.disconnectSlot(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G,H> boolean disconnect(MultiSignal signal, QMetaObject.Slot8<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H> slot) {
        return signal.disconnectSlot(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>slot</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param slot the slot to be disconnected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G,H,I> boolean disconnect(MultiSignal signal, QMetaObject.Slot9<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H,? super I> slot) {
        return signal.disconnectSlot(slot);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be connected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(MultiSignal signal, QMetaObject.Connectable0 connectSignal) {
        return signal.disconnectSignal((QMetaObject.AbstractSignal)connectSignal);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be connected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A> boolean disconnect(MultiSignal signal, QMetaObject.Connectable1<? super A> connectSignal) {
        return signal.disconnectSignal((QMetaObject.AbstractSignal)connectSignal);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be connected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B> boolean disconnect(MultiSignal signal, QMetaObject.Connectable2<? super A,? super B> connectSignal) {
        return signal.disconnectSignal((QMetaObject.AbstractSignal)connectSignal);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be connected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C> boolean disconnect(MultiSignal signal, QMetaObject.Connectable3<? super A,? super B,? super C> connectSignal) {
        return signal.disconnectSignal((QMetaObject.AbstractSignal)connectSignal);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be connected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D> boolean disconnect(MultiSignal signal, QMetaObject.Connectable4<? super A,? super B,? super C,? super D> connectSignal) {
        return signal.disconnectSignal((QMetaObject.AbstractSignal)connectSignal);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be connected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E> boolean disconnect(MultiSignal signal, QMetaObject.Connectable5<? super A,? super B,? super C,? super D,? super E> connectSignal) {
        return signal.disconnectSignal((QMetaObject.AbstractSignal)connectSignal);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be connected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F> boolean disconnect(MultiSignal signal, QMetaObject.Connectable6<? super A,? super B,? super C,? super D,? super E,? super F> connectSignal) {
        return signal.disconnectSignal((QMetaObject.AbstractSignal)connectSignal);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be connected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G> boolean disconnect(MultiSignal signal, QMetaObject.Connectable7<? super A,? super B,? super C,? super D,? super E,? super F,? super G> connectSignal) {
        return signal.disconnectSignal((QMetaObject.AbstractSignal)connectSignal);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be connected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G,H> boolean disconnect(MultiSignal signal, QMetaObject.Connectable8<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H> connectSignal) {
        return signal.disconnectSignal((QMetaObject.AbstractSignal)connectSignal);
    }
    
    /**
     * Removes the connection between the given <i>signal</i> and <i>connectSignal</i>.
     * 
     * @param <A> The type of the first parameter of the signal.
     * @param <B> The type of the second parameter of the signal.
     * @param <C> The type of the third parameter of the signal.
     * @param <D> The type of the fourth parameter of the signal.
     * @param <E> The type of the fifth parameter of the signal.
     * @param <F> The type of the sixth parameter of the signal.
     * @param <G> The type of the seventh parameter of the signal.
     * @param <H> The type of the eighth parameter of the signal.
     * @param <I> The type of the ninth parameter of the signal.
     * @param signal the signal to be disconnected
     * @param connectSignal the signal to be connected
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static <A,B,C,D,E,F,G,H,I> boolean disconnect(MultiSignal signal, QMetaObject.Connectable9<? super A,? super B,? super C,? super D,? super E,? super F,? super G,? super H,? super I> connectSignal) {
        return signal.disconnectSignal((QMetaObject.AbstractSignal)connectSignal);
    }
    
    /**
     * <p>Creates a connection of the given <i>type</i> from the <i>signal</i> in
     * the <i>sender</i> object to the <i>method</i> in the <i>receiver</i> object.</p>
     * <p>Returns a handle to the connection that can be used to disconnect
     * it later.</p>
     * 
     * <p><code>
     * QLabel label = new QLabel();<br>
     * QScrollBar scrollBar = new QScrollBar();<br>
     * QObject.connect(scrollBar, "valueChanged(int)", label,  "setNum(int)");<br>
     * </code></p>
     * 
     * <p>This example ensures that the label always displays the current
     * scroll bar value. Note that the signal and slots parameters must not
     * contain any variable names, only the type. E.g. the following would
     * not work and throw {@link QNoSuchSignalException}/{@link QNoSuchSlotException}:</p>
     * 
     * <p><code>
     * QObject.connect(scrollBar, "valueChanged(int value)", label,  "setNum(int value)");
     * </code></p>
     * 
     * <p>A signal can also be connected to another signal:</p>
     * 
     * <p><code>
     * public class MyWidget extends QWidget {<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public final Signal0 buttonClicked = new Signal0();<br>
     * <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public MyWidget(){<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;myButton = new QPushButton(this);<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;connect(myButton, "clicked()", this, "buttonClicked()");<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;}<br>
     * <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;private QPushButton myButton;<br>
     * }
     * </code></p>
     * 
     * <p>or alternatively:</p>
     * 
     * <p><code>
     * clicked.connect(myButton.buttonClicked);
     * </code></p>
     * 
     * <p>In this example, the <i>MyWidget</i> constructor relays a signal from
     * a private member variable, and makes it available under a name
     * that relates to <i>MyWidget</i>.</p>
     * 
     * <p>A signal can be connected to many slots and signals. Many signals
     * can be connected to one slot.</p>
     * 
     * <p>If a signal is connected to several slots, the slots are activated
     * in the same order in which the connections were made, when the
     * signal is emitted.</p>
     * 
     * <p>In case the connection was successfully initialized the function returns 
     * a QMetaObject.Connection that represents a handle to this connection. 
     * The connection handle will be invalid
     * if it cannot create the connection, for example, if QObject is unable
     * to verify the existence of either <i>signal</i> or <i>method</i>, or if their
     * signatures aren't compatible.
     * In this case, the function returns null.</p>
     * 
     * <p>By default, a signal is emitted for every connection you make;
     * two signals are emitted for duplicate connections. You can break
     * all of these connections with a single disconnect() call.
     * If you pass the Qt.ConnectionType.UniqueConnection <i>type</i>, the connection will only
     * be made if it is not a duplicate. If there is already a duplicate
     * (exact same signal to the exact same slot on the same objects),
     * the connection will fail and connect will return null.</p>
     * 
     * <p>Note: Qt.ConnectionType.UniqueConnections do not work for lambdas; 
     * they only apply to connecting to member functions.</p>
     * 
     * <p>The optional <i>type</i> parameter describes the type of connection
     * to establish. In particular, it determines whether a particular
     * signal is delivered to a slot immediately or queued for delivery
     * at a later time. If the signal is queued, the parameters must be
     * of types that are known to Qt's meta-object system, because Qt
     * needs to copy the arguments to store them in an event behind the
     * scenes. If you try to use a queued connection and get the error
     * message</p>
     * 
     * <p><code>
     * QObject::connect: Cannot queue arguments of type 'MyType'
     * (Make sure 'MyType' is registered using qRegisterMetaType().)
     * </code></p>
     * 
     * <p>call {@link QMetaType#registerMetaType(Class)} to register the data type before you
     * establish the connection.</p>
     * 
     * @see #disconnect()
     * @see #sender()
     * @see QMetaType#registerMetaType(Class)
     */
    public static QMetaObject.Connection connect(QObject sender, String signal, QObject receiver, String slot, Qt.ConnectionType... connectionType) {
        if(signal.isEmpty())
            throw new IllegalArgumentException("Empty string not allowed as signal.");
        if(slot.isEmpty())
            throw new IllegalArgumentException("Empty string not allowed as slot.");
        QMetaMethod signalMethod = sender.metaObject().method(signal);
        if(signalMethod==null || !signalMethod.isValid())
            throw new QNoSuchSignalException(signal);
        QMetaMethod slotMethod = receiver.metaObject().method(slot);
        if(slotMethod==null || !slotMethod.isValid())
            throw new QNoSuchSlotException(receiver, slot);
        return connect(
                sender,
                signalMethod,
                receiver,
                slotMethod,
                connectionType
            );
    }
    
    /**
     * <p>Creates a connection of the given <code>type</code> from the <code>signal</code> in
     * the <code>sender</code> object to the <code>method</code> in the <code>receiver</code> object.
     * Returns a handle to the connection that can be used to disconnect it later.</p>
     *
     * <p>The Connection handle will be invalid  if it cannot create the
     * connection, for example, the parameters were invalid.
     * You can check {@link QMetaObject.Connection#isConnected()} on the returned connection object.</p>
     *
     * <p>This function works in the same way as
     * {@link #connect(QObject, String, QObject, String, io.qt.core.Qt.ConnectionType...)}
     * but it uses QMetaMethod to specify signal and method.</p>
     *
     * @see #connect(QObject, String, QObject, String, io.qt.core.Qt.ConnectionType...)
     */
    public static QMetaObject.Connection connect(QObject sender, QMetaMethod signal, QObject receiver, QMetaMethod slot, Qt.ConnectionType... connectionType) {
        java.util.Objects.requireNonNull(signal);
        java.util.Objects.requireNonNull(slot);
        byte flags = 0;
        if(connectionType!=null && connectionType.length>0) {
            for (Qt.ConnectionType c : connectionType) {
                flags |= c.value();
            }
        }
        return QMetaObject.connectMethods(
                java.util.Objects.requireNonNull(sender),
                signal.methodIndex(),
                signal.enclosingMetaObject().metaObjectPointer,
                receiver,
                slot.methodIndex(),
                slot.enclosingMetaObject().metaObjectPointer,
                flags
            );
    }
    
    /**
     * <p>Disconnects <i>signal</i> in object sender from method in object
     * <i>receiver</i>. Returns <code>true</code> if the connection is successfully broken;
     * otherwise returns <code>false</code>.</p>
     * 
     * <p>A signal-slot connection is removed when either of the objects
     * involved are destroyed.</p>
     * 
     * <p>disconnect() is typically used in three ways, as the following
     * examples demonstrate.</p>
     * <ol>
     * <li>Disconnect everything connected to an object's signals:
     * 
     *    <p><code>disconnect(myObject, null, null, null);</code></p>
     * 
     *    equivalent to the non-static overloaded function
     * 
     *    <p><code>myObject.disconnect();</code></p>
     * </li>
     * <li>Disconnect everything connected to a specific signal:
     * 
     *    <p><code>disconnect(myObject, "mySignal()", null, null);</code></p>
     * 
     *    equivalent to the non-static overloaded function
     * 
     *    <p><code>myObject.mySignal.disconnect();</code></p>
     * </li>
     * <li>Disconnect a specific receiver:
     * 
     *    <p><code>disconnect(myObject, null, myReceiver, null);</code></p>
     * 
     *    equivalent to the non-static overloaded function
     * 
     *    <p><code>myObject.disconnect(myReceiver);</code></p>
     * </li>
     * </ol>
     * 
     * <p>0 may be used as a wildcard, meaning "any signal", "any receiving
     * object", or "any slot in the receiving object", respectively.</p>
     * 
     * <p>The <i>sender</i> may never be 0. (You cannot disconnect signals from
     * more than one object in a single call.)</p>
     * 
     * <p>If <i>signal</i> is 0, it disconnects <i>receiver</i> and <i>method</i> from
     * any signal. If not, only the specified signal is disconnected.</p>
     * 
     * <p>If <i>receiver</i> is 0, it disconnects anything connected to
     * <i>signal</i>. If not, QMetaObject.Slots in objects other than <i>receiver</i> are not
     * disconnected.</p>
     * 
     * <p>If <i>method</i> is 0, it disconnects anything that is connected to
     * <i>receiver</i>. If not, only slots named <i>method</i> will be disconnected,
     * and all other slots are left alone. The <i>method</i> must be 0 if 
     * <i>receiver</i> is left out, so you cannot disconnect a
     * specifically-named slot on all objects.</p>
     * 
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QObject sender, String signal, QObject receiver, String slot) {
        if(signal!=null && signal.isEmpty())
            throw new IllegalArgumentException("Empty string not allowed as signal.");
        if(slot!=null && slot.isEmpty())
            throw new IllegalArgumentException("Empty string not allowed as slot.");
        QMetaMethod signalMethod = signal==null ? null : sender.metaObject().method(signal);
        if(signal!=null && signalMethod==null)
            throw new QNoSuchSignalException(signal);
        QMetaMethod slotMethod = slot==null ? null : receiver.metaObject().method(slot);
        if(slot!=null && slotMethod==null)
            throw new QNoSuchSlotException(receiver, slot);
        return disconnect(
                sender,
                signalMethod,
                receiver,
                slotMethod
            );
    }
    
    /**
     * <p>Disconnects <i>signal</i> in object <i>sender</i> from <i>method</i> in object
     * <i>receiver</i> receiver. Returns <code>true</code> if the connection is successfully broken;
     * otherwise returns <code>false</code>.</p>
     *
     * <p>This function provides the same possibilities like
     * {@link #disconnect(QObject, String, QObject, String)}
     * but uses QMetaMethod to represent the signal and the method to be disconnected.</p>
     *
     * <p>Additionally this function returns false and no signals and slots disconnected
     * if:</p>
     * <ol>
     *
     *      <li><i>signal</i> is not a member of sender class or one of its parent classes.</li>
     *
     *      <li><i>method</i> is not a member of receiver class or one of its parent classes.</li>
     *
     *      <li><i>signal</i> instance represents not a signal.</li>
     *
     * </ol>
     *
     * <p><code>null</code> may be used as wildcard in the meaning "any signal" or "any slot in receiving object".
     * In the same way 0 can be used for <i>receiver</i> in the meaning "any receiving object". In this case
     * method should also be <code>null</code>. <i>sender</i> parameter should be never 0.</p>
     *
     * @see #disconnect(QObject, String, QObject, String)
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public static boolean disconnect(QObject sender, QMetaMethod signal, QObject receiver, QMetaMethod slot) {
        return QMetaObject.disconnectMethods(
                java.util.Objects.requireNonNull(sender),
                signal==null ? -1 : signal.methodIndex(),
                signal==null ? 0 : signal.enclosingMetaObject().metaObjectPointer,
                receiver,
                slot==null ? -1 : slot.methodIndex(),
                slot==null ? 0 : slot.enclosingMetaObject().metaObjectPointer
            );
    }
    
    /**
     * <p>Disconnect a connection.</p>
     * 
     * <p>If the connection is invalid or has already been disconnected, do nothing
     * and return false.</p>
     * @param connection the connection
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     * 
     */
    public static boolean disconnect(QMetaObject.Connection connection) {
        return CoreUtility.disconnectOne(connection);
    }
    
    /**
     * Disconnect all connections originating in this object.
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public final boolean disconnect() {
        return CoreUtility.disconnectAll(this, null);
    }
    
    /**
     * Disconnect all connections made from this signal emitter to a specific object.
     *
     * @param receiver The receiver to disconnect, or null to disconnect all receivers
     * @return <code>true</code> if successfully disconnected, or <code>false</code> otherwise.
     */
    public final boolean disconnect(Object receiver) {
        if(receiver instanceof QMetaObject.Connection)
            return disconnect((QMetaObject.Connection)receiver);
        else return CoreUtility.disconnectAll(this, receiver);
    }
    
    /**
     * Returns a pointer to the meta-object of this object.
     * @return the object's meta-object
     */
    @io.qt.QtUninvokable
    public native final QMetaObject metaObject();
        
    /**
     * Casts an object to the given <i>targetType</i>. Returns null if object is not instance of <i>targetType</i>.
     * @param <T> type
     * @param targetType
     * @return the object as targetType or null
     */
    @io.qt.QtUninvokable
    public final <T extends QtObjectInterface> T qt_metacast(Class<T> targetType) {
        return QMetaObject.cast(targetType, this);
    }
    
    /**
     * Returns true if this object is an instance of a class that inherits className or a QObject subclass that inherits className; otherwise returns false.
     * A class is considered to inherit itself.
     * @param targetType
     * @return <code>true</code> if object instance of targetType
     */
    @io.qt.QtUninvokable
    public final boolean inherits(Class<?> targetType){
        return targetType.isInstance(this) || inherits(QMetaObject.internalNameOfType(targetType).replace("*", ""));
    }
    
    /**
     * @see #receivers(String)
     */
    @io.qt.QtUninvokable
    protected final int receivers(io.qt.core.QMetaObject.AbstractSignal signalObject){
        if(signalObject.methodIndex()>=0 && signalObject.containingObject()==this) {
            QMetaMethod method = metaObject().methods().get(signalObject.methodIndex());
            return receivers("2" + method.cppMethodSignature());
        }
        return 0;
    }

    /**
     * <p>See <a href="@docRoot/qobject.html#qobject_cast">qobject_cast&lt;T>(QObject *)</a></p>
     */
    @QtUninvokable
    public static <T extends QtObjectInterface> T qobject_cast(Class<T> targetType, QObject object) {
        return QMetaObject.cast(targetType, object);
    }
    
}// class

class QObject_5__ extends QObject {
    private static class CoreUtility extends io.qt.internal.CoreUtility{
        protected static void emitNativeSignal(QObject sender, int methodIndex, long metaObjectId, Object... args) {
            io.qt.internal.CoreUtility.emitNativeSignal(sender, methodIndex, metaObjectId, args);
        }

        protected static boolean disconnectAll(QtSignalEmitterInterface sender, Object receiver) {
            return io.qt.internal.CoreUtility.disconnectAll(sender, receiver);
        }

        protected static boolean disconnectOne(QMetaObject.Connection connection) {
            return io.qt.internal.CoreUtility.disconnectOne(connection);
        }
    }

    static QThread getQPropertyThread(io.qt.internal.ClassAnalyzerUtility.LambdaInfo info) {
        return null;
    }
}// class

class QObject_6__ extends QObject {

    private static Runnable MIGHT_HAVE_SIGNAL = ()->{};
    private static Runnable NO_SIGNAL = ()->{};
    
    private static QMetaMethod findNotifySignalByBindables(QObject object, java.lang.reflect.Field reflectedField, QUntypedPropertyData property) {
        for(QMetaProperty metaProperty : object.metaObject().properties()) {
            if(metaProperty.isBindable()) {
                QUntypedBindable bindable = metaProperty.bindable(object);
                if(bindable.data()==property) {
                    CoreUtility.registerPropertyField(metaProperty, reflectedField);
                    QMetaMethod notifySignal = metaProperty.notifySignal();
                    if(notifySignal.isValid()) {
                        if(notifySignal.parameterCount()==0) {
                            return notifySignal;
                        }else if(notifySignal.parameterCount()==1 && metaProperty.metaType().equals(notifySignal.parameterMetaType(0))) {
                            return notifySignal;
                        }else {
                            return null;
                        }
                    }else {
                        return null;
                    }
                }
            }
        }
        return null;
    }
    
    private class EmitSignal0 implements Runnable{
        @Override
        public void run() {
            try {
                CoreUtility.emitNativeSignal(QObject.this, methodIndex, metaObjectId);
            }catch(QNoNativeResourcesException e){}
        }
    
        private final int methodIndex;
        private final long metaObjectId;
    
        public EmitSignal0(int methodIndex, long metaObjectId) {
            super();
            this.methodIndex = methodIndex;
            this.metaObjectId = metaObjectId;
        }
    }
    
    private class EmitSignal1<T> implements Runnable{
        @Override
        public void run() {
            try {
                CoreUtility.emitNativeSignal(QObject.this, methodIndex, metaObjectId, supplier.get());
            }catch(QNoNativeResourcesException e){}
        }
    
        private final int methodIndex;
        private final long metaObjectId;
        private final java.util.function.Supplier<? extends T> supplier;
        
        public EmitSignal1(int methodIndex, long metaObjectId, java.util.function.Supplier<? extends T> supplier) {
            super();
            this.supplier = supplier;
            this.methodIndex = methodIndex;
            this.metaObjectId = metaObjectId;
        }
    }
    
    private static abstract class AbstractPropertyCore<T>{
        abstract QMetaType valueMetaType(QProperty<T> property);
        abstract void initialize(QProperty<T> property);
        abstract void emitSignal(QProperty<T> property);
        abstract boolean hasSignal(QProperty<T> property);
        abstract T value(QBindingStorage bindingStorage, QProperty<T> property);
        abstract QPropertyBindingData bindingData(QBindingStorage bindingStorage, QProperty<T> property, boolean create);
        abstract boolean setValueBypassingBindings(QProperty<T> property, Object val);
    }
    
    private static class PropertyCore<T> extends AbstractPropertyCore<T>{
        PropertyCore(QMetaType metaType) {
            super();
            this.metaType = metaType;
        }
        final QMetaType valueMetaType(QProperty<T> property) { return metaType; }
        final void initialize(QProperty<T> property) {}
        void emitSignal(QProperty<T> property) {}
        boolean hasSignal(QProperty<T> property) { return false; }
        final T value(QBindingStorage bindingStorage, QProperty<T> property){
            bindingStorage.registerDependency(property);
            return io.qt.core.QProperty.getValueBypassingBindings(QtJambi_LibraryUtilities.internal.nativeId(property), QtJambi_LibraryUtilities.internal.checkedNativeId(metaType));
        }
        final QPropertyBindingData bindingData(QBindingStorage bindingStorage, QProperty<T> property, boolean create) {
            return bindingStorage.bindingData(property, create);
        }
        boolean setValueBypassingBindings(QProperty<T> property, Object val) {
            return io.qt.core.QProperty.setValueBypassingBindings(QtJambi_LibraryUtilities.internal.nativeId(property), QtJambi_LibraryUtilities.internal.checkedNativeId(metaType), val);
        }
        private final QMetaType metaType;
    }
    
    private final static class Signal0PropertyCore<T> extends PropertyCore<T>{
        public Signal0PropertyCore(QMetaType metaType, int methodIndex, long metaObjectId) {
            super(metaType);
            this.methodIndex = methodIndex;
            this.metaObjectId = metaObjectId;
        }
        void emitSignal(QProperty<T> property) {
            try {
                CoreUtility.emitNativeSignal(java.util.Objects.requireNonNull(property.owner()), methodIndex, metaObjectId);
            }catch(QNoNativeResourcesException e){}
        }
        boolean hasSignal(QProperty<T> property) { return true; }
        private final int methodIndex;
        private final long metaObjectId;
    }
    
    private final static class Signal1PropertyCore<T> extends PropertyCore<T>{
        public Signal1PropertyCore(QMetaType metaType, int methodIndex, long metaObjectId) {
            super(metaType);
            this.methodIndex = methodIndex;
            this.metaObjectId = metaObjectId;
        }
        void emitSignal(QProperty<T> property) {
            try {
                CoreUtility.emitNativeSignal(java.util.Objects.requireNonNull(property.owner()), methodIndex, metaObjectId, property.getValueBypassingBindings());
            }catch(QNoNativeResourcesException e){}
        }
        boolean hasSignal(QProperty<T> property) { return true; }
        private final int methodIndex;
        private final long metaObjectId;
    }
    
    private static class RCPropertyCore<T> extends PropertyCore<T>{
        @SuppressWarnings("unused")
        private Object __rcValue;
        RCPropertyCore(QMetaType metaType) {
            super(metaType);
        }
        final boolean setValueBypassingBindings(QProperty<T> property, Object val) {
            if(super.setValueBypassingBindings(property, val)) {
                __rcValue = val;
                return true;
            }
            else return false;
        }
    }
    
    private final static class RCSignal0PropertyCore<T> extends RCPropertyCore<T>{
        public RCSignal0PropertyCore(QMetaType metaType, int methodIndex, long metaObjectId) {
            super(metaType);
            this.methodIndex = methodIndex;
            this.metaObjectId = metaObjectId;
        }
        void emitSignal(QProperty<T> property) {
            try {
                CoreUtility.emitNativeSignal(java.util.Objects.requireNonNull(property.owner()), methodIndex, metaObjectId);
            }catch(QNoNativeResourcesException e){}
        }
        boolean hasSignal(QProperty<T> property) { return true; }
        private final int methodIndex;
        private final long metaObjectId;
    }
    
    private final static class RCSignal1PropertyCore<T> extends RCPropertyCore<T>{
        public RCSignal1PropertyCore(QMetaType metaType, int methodIndex, long metaObjectId) {
            super(metaType);
            this.methodIndex = methodIndex;
            this.metaObjectId = metaObjectId;
        }
        void emitSignal(QProperty<T> property) {
            try {
                CoreUtility.emitNativeSignal(java.util.Objects.requireNonNull(property.owner()), methodIndex, metaObjectId, property.getValueBypassingBindings());
            }catch(QNoNativeResourcesException e){}
        }
        boolean hasSignal(QProperty<T> property) { return true; }
        private final int methodIndex;
        private final long metaObjectId;
    }
    
    private static class InitializingPropertyCore<T> extends AbstractPropertyCore<T>{
        QMetaType valueMetaType(QProperty<T> property) {
            initialize(property);
            return property.core.valueMetaType(property);
        }
        void initialize(QProperty<T> property) {
            initialize(property, null);
        }
        final void initialize(QProperty<T> property, T val) {
            io.qt.internal.PropertyInfo result = CoreUtility.analyzeProperty(property.owner(), property);
            if(result.property!=null) {
                if(result.notifySignal==null) {
                    if(result.metaType.flags().testFlag(QMetaType.TypeFlag.IsPointer) || result.metaType.name().contains("*")) {
                        property.core = new RCPropertyCore<>(result.metaType);
                    }else {
                        property.core = new PropertyCore<>(result.metaType);
                    }
                }else {
                    if(result.notifySignal.parameterCount()==0) {
                        if(result.metaType.flags().testFlag(QMetaType.TypeFlag.IsPointer) || result.metaType.name().contains("*")) {
                            property.core = new RCSignal0PropertyCore<>(result.metaType, result.notifySignal.methodIndex(), result.notifySignal.enclosingMetaObject().metaObjectPointer);
                        }else {
                            property.core = new Signal0PropertyCore<>(result.metaType, result.notifySignal.methodIndex(), result.notifySignal.enclosingMetaObject().metaObjectPointer);
                        }                                                
                    }else {
                        if(result.metaType.flags().testFlag(QMetaType.TypeFlag.IsPointer) || result.metaType.name().contains("*")) {
                            property.core = new RCSignal1PropertyCore<>(result.metaType, result.notifySignal.methodIndex(), result.notifySignal.enclosingMetaObject().metaObjectPointer);
                        }else {
                            property.core = new Signal1PropertyCore<>(result.metaType, result.notifySignal.methodIndex(), result.notifySignal.enclosingMetaObject().metaObjectPointer);
                        }                        
                    }
                }
                io.qt.core.QProperty.initialize_native(property, result.metaType, val);
            }else {
                property.core = new PropertyCore<>(result.metaType);
                io.qt.core.QProperty.initialize_native(property, result.metaType, val);
                QMetaMethod notifySignal = findNotifySignalByBindables(property.owner(), result.reflectedField, property);
                if(notifySignal!=null) {
                    if(notifySignal.parameterCount()==0) {
                        if(result.metaType.flags().testFlag(QMetaType.TypeFlag.IsPointer) || result.metaType.name().contains("*")) {
                            property.core = new RCSignal0PropertyCore<>(result.metaType, notifySignal.methodIndex(), result.notifySignal.enclosingMetaObject().metaObjectPointer);
                        }else {
                            property.core = new Signal0PropertyCore<>(result.metaType, notifySignal.methodIndex(), result.notifySignal.enclosingMetaObject().metaObjectPointer);
                        }
                    }else {
                        if(result.metaType.flags().testFlag(QMetaType.TypeFlag.IsPointer) || result.metaType.name().contains("*")) {
                            property.core = new RCSignal1PropertyCore<>(result.metaType, notifySignal.methodIndex(), result.notifySignal.enclosingMetaObject().metaObjectPointer);
                        }else {
                            property.core = new Signal1PropertyCore<>(result.metaType, notifySignal.methodIndex(), result.notifySignal.enclosingMetaObject().metaObjectPointer);
                        }
                    }
                }
            }
        }
        final T value(QBindingStorage bindingStorage, QProperty<T> property){
            initialize(property);
            return property.core.value(bindingStorage, property);
        }
        final QPropertyBindingData bindingData(QBindingStorage bindingStorage, QProperty<T> property, boolean create) {
            initialize(property);
            return property.core.bindingData(bindingStorage, property, create);
        }
        void emitSignal(QProperty<T> property) {
            initialize(property);
            property.core.emitSignal(property);
        }
        boolean hasSignal(QProperty<T> property) {
            initialize(property);
            return property.core.hasSignal(property);
        }
        boolean setValueBypassingBindings(QProperty<T> property, Object val) {
            initialize(property);
            return property.core.setValueBypassingBindings(property, val);
        }
    }
    
    private final static class ValueInitializingPropertyCore<T> extends InitializingPropertyCore<T>{
        public ValueInitializingPropertyCore(T val) {
            super();
            this.val = val;
        }
        
        void initialize(QProperty<T> property) {
            super.initialize(property, val);
        }
    
        private final T val;
    }
    
    private final static class BindingInitializingPropertyCore<T> extends InitializingPropertyCore<T>{
        public BindingInitializingPropertyCore(QUntypedPropertyBinding binding) {
            super();
            this.binding = binding;
        }
        
        void initialize(QProperty<T> property) {
            super.initialize(property, null);
            property.bindingData().setBinding(binding, property);
        }
    
        private final QUntypedPropertyBinding binding;
    }
    
    /**
     * <p>The <code>QProperty</code> class enables automatic property bindings.
     * The Java type <code>QProperty</code> corresponds to the C++ type <code><a href="@docRoot/qobjectbindableproperty.html">QObjectBindableProperty</a></code>.</p>
     * <p>It is only allowed to use <code>QProperty</code> as <code>final</code>-declared member variable of a <code>QObject</code> subtype.</p>
     * <p>Example:</p>
     * <code>
     * public class MyObject extends QObject{<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;private final QProperty&lt;QColor> color = new QProperty&lt;>();<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public final Signal0 colorChanged = new Signal0();<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public QColor color(){<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return color.value();<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;}<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public void setColor(QColor color){<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;color.setValue(color);<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;}<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public QBindable&lt;QColor> bindableColor(){<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return new QBindable&lt;>(color);<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;}<br>
     * }
     * </code>
     * <p><code>QProperty</code> fields should meet the following naming conventions to let
     * metaobject system identify them as accessible property. For a property called "<code>foo</code>"
     * call the <code>QProperty</code> field either "<code>foo</code>", "<code>fooProp</code>" or "<code>fooProperty</code>".
     * Then, QtJambi will identify the method "<code>public T foo()</code>" as it's getter,
     * "<code>public void setFoo(T)</code>" as it's setter,
     * "<code>public final Signal0 fooChanged</code>" as it's notify signal and 
     * "<code>public QBindable&lt;T> bindableFoo()</code>" as it's bindable supplier.
     * </p>
     * <p>Instead of meeting these naming conventions you can use the <code>io.qt.QtProperty...</code> annotations
     * to make the fields and methods identified as property:</p>
     * <code>
     * public class MyObject extends QObject{<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;@QtPropertyMember(name="color")<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;private final QProperty&lt;QColor> _c = new QProperty&lt;>();<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;@QtPropertyNotify(name="color")<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public final Signal0 colorChangeAppeared = new Signal0();<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;@QtPropertyReader(name="color")<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public QColor get_color(){<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return _c.value();<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;}<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;@QtPropertyWriter(name="color")<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public void change_color(QColor color){<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;_c.setValue(color);<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;}<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;@QtPropertyBindable(name="color")<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public QBindable&lt;QColor> get_bindable_color(){<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return new QBindable&lt;>(_c);<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;}<br>
     * }
     * </code>
     * <p>By declaring a <code>QProperty</code> field <code>public</code> 
     * or by using the <code>QtPropertyMember</code> annotation
     * QtJambi identifies a readable, writable and bindable property 
     * without the need to specify getter, setter and bindable methods:</p>
     * <code>
     * public class MyObject extends QObject{<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public final QProperty&lt;QColor> color = new QProperty&lt;>();<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public final Signal0 colorChanged = new Signal0();<br>
     * }
     * </code>
     * <p>For primitive-typed implementations see:</p>
     * <ul>
     * <li>{@link QBooleanProperty}</li>
     * <li>{@link QByteProperty}</li>
     * <li>{@link QShortProperty}</li>
     * <li>{@link QIntProperty}</li>
     * <li>{@link QLongProperty}</li>
     * <li>{@link QFloatProperty}</li>
     * <li>{@link QDoubleProperty}</li>
     * <li>{@link QCharProperty}</li>
     * </ul>
     * <p>See <a href="@docRoot/qobjectbindableproperty.html">QObjectBindableProperty</a></p>
     */
    public final class QProperty<T> extends QPropertyData<T> {
        
        QObject owner() {
            return QObject.this;
        }
    
        /**
         * <p>Constructs a property whose type is taken from it's field declaration.</p>
         */
        public QProperty() {
            super(null);
            core = new InitializingPropertyCore<>();
        }
    
        /**
         * <p>Constructs a property with the provided <code>initialValue</code>.</p>
         * <p>The property type is taken from it's field declaration. 
         * The <code>initialValue</code> has to be assignable to the property type. Otherwise, a default value is used.</p>
         * @param initialValue
         */
        public QProperty(T initialValue) {
            super(null);
            core = new ValueInitializingPropertyCore<>(initialValue);
        }
        
        /**
         * <p>Constructs a property with the provided binding.</p>
         * <p>The property type is taken from it's field declaration. 
         * The <code>binding</code>'s type has to be assignable to the property type. Otherwise, a default value is used.</p>
         * @param binding
         */
        public QProperty(QUntypedPropertyBinding binding) {
            super(null);
            core = new BindingInitializingPropertyCore<>(binding);
        }
        
        /**
         * <p>Constructs a property bound to the provided <code>functor</code>.</p>
         * <p>The property type is taken from it's field declaration. 
         * The <code>functor</code>'s return type ({@link QtUtilities.Supplier#get()}) has to be assignable to the property type. 
         * Otherwise, a default value is used.</p>
         * @param functor
         */
        public QProperty(QtUtilities.Supplier<? extends T> functor) {
            this(new QPropertyBinding<>(functor));
        }
        
        /**
         * Returns the value of the property. This may evaluate a binding expression that is tied to this property, before returning the value.
         * <p>See <a href="@docRoot/qobjectbindableproperty.html#value">QObjectBindableProperty::value()const</a></p>
         * @return value
         */
        @QtUninvokable
        public final T value()
        {
            return core.value(bindingStorage(), this);
        }
        
        /**
         * <p>Assigns newValue to this property and removes the property's associated binding, if present.</p>
         * <p>See <a href="@docRoot/qobjectbindableproperty.html#setValue">QObjectBindableProperty::setValue(T)</a></p>
         * @param newValue
         */
        @SuppressWarnings("unchecked")
        @QtUninvokable
        public final void setValue(T newValue)
        {
            QMetaType valueMetaType = core.valueMetaType(this);
            if(newValue==null && !valueMetaType.flags().testFlag(QMetaType.TypeFlag.IsPointer))
                newValue = (T)valueMetaType.create();
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            if(bd!=null)
                bd.removeBinding();
            if (core.setValueBypassingBindings(this, newValue))
                notifyProperty(bd);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be equals to the property's type <code>T</code>, 
         * otherwise the property remains unchanged.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @SuppressWarnings("unchecked")
        @QtUninvokable
        public final QPropertyBinding<T> setBinding(QPropertyBinding<T> newBinding)
        {
            if(newBinding!=null && !io.qt.core.QProperty.checkType(core.valueMetaType(this), newBinding.valueMetaType()))
                return new QPropertyBinding<>();
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QPropertyBinding)
                return (QPropertyBinding<T>)oldBinding;
            else
                return new QPropertyBinding<>(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be equals to the property's type <code>T</code>, 
         * otherwise the property remains unchanged and the method returns <code>false</code>.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * <p>Returns true if the type of this property is the same as the type the binding function returns; false otherwise.</p>
         * @param newBinding
         * @return true if types match, false otherwise.
         */
        @QtUninvokable
        public final boolean setBinding(QUntypedPropertyBinding newBinding)
        {
            if(newBinding!=null && !io.qt.core.QProperty.checkType(core.valueMetaType(this), newBinding.valueMetaType()))
                return false;
            QPropertyBindingData bd = bindingData();
            bd.setBinding(newBinding, this);
            notifyProperty(bd);
            return true;
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>functor</code> and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated by invoking {@link QtUtilities.Supplier#get()} of <code>functor</code>.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param functor
         * @return oldBinding
         */
        @QtUninvokable
        public final QPropertyBinding<T> setBinding(QtUtilities.Supplier<? extends T> functor)
        {
            try {
                QPropertyBinding.setPendingMetaType(this::valueMetaType);
                return setBinding(new QPropertyBinding<>(functor));
            }finally {
                QPropertyBinding.setPendingMetaType(null);
            }
        }
        
        @QtUninvokable
        private QPropertyBinding<T> makeBinding()
        {
            core.initialize(this);
            try {
                QPropertyBinding.setPendingMetaType(this::valueMetaType);
                return new QPropertyBinding<>(this::value);
            }finally {
                QPropertyBinding.setPendingMetaType(null);
            }
        }
        
        /**
         * Checks if the property has a binding.
         * @return true if the property has a binding, false otherwise.
         */
        @QtUninvokable
        public final boolean hasBinding() {
            QPropertyBindingData bd = core.bindingData(bindingStorage(), this, false);
            return bd!=null && bd.hasBinding(); 
        }
        
        /**
         * Returns the binding expression that is associated with this property. 
         * A default constructed {@link QPropertyBinding&lt;T>} will be returned if no such association exists.
         * @return binding
         */
        @QtUninvokable
        public final QPropertyBinding<T> binding()
        {
            return new QPropertyBinding<>(this);
        }
        
        /**
         * <p>Disassociates the binding expression from this property and returns it.</p>
         * <p>After calling this function, the value of the property will only change if you assign a new value to it, or when a new binding is set.</p>
         * @return the removed binding
         */
        @QtUninvokable
        public final QPropertyBinding<T> takeBinding()
        {
            return setBinding(new QPropertyBinding<>());
        }
        
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the property changes.</p>
         * <p>The returned property change handler object keeps track of the registration. 
         * As long as the change handler is alive i.e. as long as a reference to the {@link QPropertyChangeHandler} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         */
        @QtUninvokable
        public final QPropertyChangeHandler onValueChanged(Runnable f)
        {
            return new QPropertyChangeHandler(bindingData(), f);
        }
        
        /**
         * Subscribes the given functor f as a callback that is called immediately and whenever the value of the property changes in the future.
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         * @see #onValueChanged(Runnable)
         */
        @QtUninvokable
        public final QPropertyChangeHandler subscribe(Runnable f)
        {
            f.run();
            return onValueChanged(f);
        }
    
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
         * <p>The returned property notifier object keeps track of the registration. 
         * As long as the notifier is alive i.e. as long as a reference to the {@link QPropertyNotifier} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property notifier
         * @see QPropertyNotifier
         */
        @io.qt.QtUninvokable
        public final QPropertyNotifier addNotifier(Runnable f)
        {
            return new QPropertyNotifier(bindingData(), f);
        }
        
        @QtUninvokable
        QPropertyBindingData bindingData() {
            return core.bindingData(bindingStorage(), this, true);
        }
        
        @QtUninvokable
        private void notifyProperty(QPropertyBindingData binding){
            if (binding!=null)
                binding.notifyObservers(this);
            core.emitSignal(this);
        }
        
        /**
         * Programmatically signals a change of the property. Any binding which depend on it will be notified, and if the property has a signal, it will be emitted.
         * <p>See <a href="@docRoot/qobjectbindableproperty.html#notify">QObjectBindableProperty::notify()</a></p>
         */
        @QtUninvokable
        public final void notifyProperty() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            notifyProperty(bd);
        }
        
        @QtUninvokable
        private void emitSignal() {
            core.emitSignal(this);
        }
        
        @QtUninvokable
        private boolean hasSignal() {
            return core.hasSignal(this);
        }
        
        QMetaType valueMetaType() {
            return core.valueMetaType(this);
        }
        
        /**
         * {@inheritDoc}
         */
        @QtUninvokable
        @Override
        public final T getValueBypassingBindings() {
            return io.qt.core.QProperty.getValueBypassingBindings(QtJambi_LibraryUtilities.internal.nativeId(this), QtJambi_LibraryUtilities.internal.checkedNativeId(core.valueMetaType(this)));
        }
        
        /**
         * {@inheritDoc}
         */
        @QtUninvokable
        @Override
        public final boolean setValueBypassingBindings(T val) {
            return core.setValueBypassingBindings(this, val);
        }
        
        private AbstractPropertyCore<T> core;
    }
    
    /**
     * QBooleanProperty is primitive-typed version of QProperty&lt;Boolean&gt;.
     * @see QProperty
     */
    public final class QBooleanProperty extends QBooleanPropertyData {

        QObject owner() {
            return QObject.this;
        }

        /**
         * Constructs a <code>boolean</code>-typed property.
         */
        public QBooleanProperty() {
            super();
        }
    
        /**
         * Constructs a <code>boolean</code>-typed property with the provided initialValue.
         * @param initialValue
         */
        public QBooleanProperty(boolean initialValue) {
            super(initialValue);
        }
        
        /**
         * Constructs a <code>boolean</code>-typed property with the provided binding.
         * @param binding
         */
        public QBooleanProperty(QBooleanPropertyBinding binding) {
            super();
            Runnable signal = this.signal;
            this.signal = MIGHT_HAVE_SIGNAL;
            bindingData().setBinding(binding, this);
            this.signal = signal;
        }
        
        /**
         * Constructs a <code>boolean</code>-typed property with the provided binding.
         * @param binding
         */
        public QBooleanProperty(QPropertyBinding<@QtPrimitiveType Boolean> binding) {
            super();
            if(io.qt.core.QBooleanProperty.checkType(binding.valueMetaType())){
                Runnable signal = this.signal;
                this.signal = MIGHT_HAVE_SIGNAL;
                bindingData().setBinding(binding, this);
                this.signal = signal;
            }
        }
        
        /**
         * <p>Constructs a <code>boolean</code>-typed property bound to the provided <code>functor</code>.</p>
         * @param functor
         */
        public QBooleanProperty(java.util.function.BooleanSupplier functor) {
            this(new QBooleanPropertyBinding(functor));
        }
        
        /**
         * Returns the value of the property. This may evaluate a binding expression that is tied to this property, before returning the value.
         * @return value
         */
        @QtUninvokable
        public final boolean value()
        {
            bindingStorage().registerDependency(this);
            return this.getValueBypassingBindings();
        }
        
        /**
         * <p>Assigns newValue to this property and removes the property's associated binding, if present.</p>
         * @param newValue
         */
        @QtUninvokable
        public final void setValue(boolean newValue)
        {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            if(bd!=null)
                bd.removeBinding();
            if (this.setValueBypassingBindings(newValue))
                notifyProperty(bd);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QBooleanPropertyBinding setBinding(QBooleanPropertyBinding newBinding)
        {
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QBooleanPropertyBinding)
                return (QBooleanPropertyBinding)oldBinding;
            else
                return new QBooleanPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>boolean</code>,
         * otherwise the property remains unchanged.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QBooleanPropertyBinding setBinding(QPropertyBinding<@QtPrimitiveType Boolean> newBinding)
        {
            if(!io.qt.core.QBooleanProperty.checkType(newBinding.valueMetaType()))
                return new QBooleanPropertyBinding();
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QBooleanPropertyBinding)
                return (QBooleanPropertyBinding)oldBinding;
            else
                return new QBooleanPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>boolean</code>, 
         * otherwise the property remains unchanged and the method returns <code>false</code>.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * <p>Returns true if the type of this property is the same as the type the binding function returns; false otherwise.</p>
         * @param newBinding
         * @return true if types match, false otherwise.
         */
        @QtUninvokable
        public final boolean setBinding(QUntypedPropertyBinding newBinding)
        {
            if(io.qt.core.QBooleanProperty.checkType(newBinding.valueMetaType())) {
                QPropertyBindingData bd = bindingData();
                bd.setBinding(newBinding, this);
                notifyProperty(bd);
                return true;
            }else {
                return false;
            }
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>functor</code> and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated by invoking {@link java.util.function.BooleanSupplier#getAsBoolean()} of <code>functor</code>.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param functor
         * @return oldBinding
         */
        @QtUninvokable
        public final QBooleanPropertyBinding setBinding(java.util.function.BooleanSupplier functor)
        {
            return setBinding(new QBooleanPropertyBinding(functor));
        }
        
        @QtUninvokable
        private QBooleanPropertyBinding makeBinding()
        {
            return new QBooleanPropertyBinding(this::value);
        }
        
        /**
         * Checks if the property has a binding.
         * @return true if the property has a binding, false otherwise.
         */
        @QtUninvokable
        public final boolean hasBinding() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            return bd!=null && bd.hasBinding(); 
        }
        
        /**
         * Returns the binding expression that is associated with this property. 
         * A default constructed {@link QBooleanPropertyBinding} will be returned if no such association exists.
         * @return binding
         */
        @QtUninvokable
        public final QBooleanPropertyBinding binding()
        {
            return new QBooleanPropertyBinding(this);
        }
        
        /**
         * <p>Disassociates the binding expression from this property and returns it.</p>
         * <p>After calling this function, the value of the property will only change if you assign a new value to it, or when a new binding is set.</p>
         * @return the removed binding
         */
        @QtUninvokable
        public final QBooleanPropertyBinding takeBinding()
        {
            return setBinding(new QBooleanPropertyBinding());
        }
        
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the property changes.</p>
         * <p>The returned property change handler object keeps track of the registration. 
         * As long as the change handler is alive i.e. as long as a reference to the {@link QPropertyChangeHandler} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         */
        @QtUninvokable
        public final QPropertyChangeHandler onValueChanged(Runnable f)
        {
            return new QPropertyChangeHandler(bindingData(), f);
        }
    
        /**
         * Subscribes the given functor f as a callback that is called immediately and whenever the value of the property changes in the future.
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         * @see #onValueChanged(Runnable)
         */
        @QtUninvokable
        public final QPropertyChangeHandler subscribe(Runnable f)
        {
            f.run();
            return onValueChanged(f);
        }
    
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
         * <p>The returned property notifier object keeps track of the registration. 
         * As long as the notifier is alive i.e. as long as a reference to the {@link QPropertyNotifier} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property notifier
         * @see QPropertyNotifier
         */
        @io.qt.QtUninvokable
        public final QPropertyNotifier addNotifier(Runnable f)
        {
            return new QPropertyNotifier(bindingData(), f);
        }
        
        @QtUninvokable
        QPropertyBindingData bindingData() { 
            return bindingStorage().bindingData(this, true);
        }
        
        @QtUninvokable
        private void notifyProperty(QPropertyBindingData binding){
            if (binding!=null)
                binding.notifyObservers(this);
            emitSignal();
        }
        
        /**
         * Programmatically signals a change of the property. Any binding which depend on it will be notified, and if the property has a signal, it will be emitted.
         * <p>See <a href="@docRoot/qobjectbindableproperty.html#notify">QObjectBindableProperty::notify()</a></p>
         */
        @QtUninvokable
        public final void notifyProperty() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            notifyProperty(bd);
        }
        
        private Runnable signal = () -> {
            io.qt.internal.PropertyInfo result = CoreUtility.analyzeProperty(QObject.this, this);
            QMetaMethod notifySignal;
            if(result.property!=null) {
                notifySignal = result.notifySignal;
            }else {
                notifySignal = findNotifySignalByBindables(QObject.this, result.reflectedField, this);
            }
            if(notifySignal==null) {
                this.signal = NO_SIGNAL;
            }else{
                if(notifySignal.parameterCount()==0)
                    this.signal = new EmitSignal0(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer);
                else
                    this.signal = new EmitSignal1<>(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer, this::getValueBypassingBindings);
            }
            this.signal.run();
        };
        
        @QtUninvokable
        private boolean hasSignal() {
            return this.signal != NO_SIGNAL;
        }
        
        @QtUninvokable
        private void emitSignal() {
            this.signal.run();
        }
    }
    
    /**
     * QByteProperty is primitive-typed version of QProperty&lt;Byte&gt;.
     * @see QProperty
     */
    public final class QByteProperty extends QBytePropertyData {

        QObject owner() {
            return QObject.this;
        }

        /**
         * Constructs a <code>byte</code>-typed property.
         */
        public QByteProperty() {
            super();
        }
    
        /**
         * Constructs a <code>byte</code>-typed property with the provided initialValue.
         * @param initialValue
         */
        public QByteProperty(byte initialValue) {
            super(initialValue);
        }
        
        /**
         * Constructs a <code>byte</code>-typed property with the provided binding.
         * @param binding
         */
        public QByteProperty(QBytePropertyBinding binding) {
            super();
            Runnable signal = this.signal;
            this.signal = MIGHT_HAVE_SIGNAL;
            bindingData().setBinding(binding, this);
            this.signal = signal;
        }
        
        /**
         * Constructs a <code>byte</code>-typed property with the provided binding.
         * @param binding
         */
        public QByteProperty(QPropertyBinding<@QtPrimitiveType Byte> binding) {
            super();
            if(io.qt.core.QByteProperty.checkType(binding.valueMetaType())){
                Runnable signal = this.signal;
                this.signal = MIGHT_HAVE_SIGNAL;
                bindingData().setBinding(binding, this);
                this.signal = signal;
            }
        }
        
        /**
         * <p>Constructs a <code>byte</code>-typed property bound to the provided <code>functor</code>.</p>
         * @param functor
         */
        public QByteProperty(io.qt.QtUtilities.ByteSupplier functor) {
            this(new QBytePropertyBinding(functor));
        }
        
        /**
         * Returns the value of the property. This may evaluate a binding expression that is tied to this property, before returning the value.
         * @return value
         */
        @QtUninvokable
        public final byte value()
        {
            bindingStorage().registerDependency(this);
            return this.getValueBypassingBindings();
        }
        
        /**
         * <p>Assigns newValue to this property and removes the property's associated binding, if present.</p>
         * @param newValue
         */
        @QtUninvokable
        public final void setValue(byte newValue)
        {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            if(bd!=null)
                bd.removeBinding();
            if (this.setValueBypassingBindings(newValue))
                notifyProperty(bd);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QBytePropertyBinding setBinding(QBytePropertyBinding newBinding)
        {
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QBytePropertyBinding)
                return (QBytePropertyBinding)oldBinding;
            else
                return new QBytePropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>byte</code>,
         * otherwise the property remains unchanged.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QBytePropertyBinding setBinding(QPropertyBinding<@QtPrimitiveType Byte> newBinding)
        {
            if(!io.qt.core.QByteProperty.checkType(newBinding.valueMetaType()))
                return new QBytePropertyBinding();
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QBytePropertyBinding)
                return (QBytePropertyBinding)oldBinding;
            else
                return new QBytePropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>byte</code>, 
         * otherwise the property remains unchanged and the method returns <code>false</code>.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * <p>Returns true if the type of this property is the same as the type the binding function returns; false otherwise.</p>
         * @param newBinding
         * @return true if types match, false otherwise.
         */
        @QtUninvokable
        public final boolean setBinding(QUntypedPropertyBinding newBinding)
        {
            if(io.qt.core.QByteProperty.checkType(newBinding.valueMetaType())) {
                QPropertyBindingData bd = bindingData();
                bd.setBinding(newBinding, this);
                notifyProperty(bd);
                return true;
            }else {
                return false;
            }
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>functor</code> and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated by invoking {@link java.util.function.ByteSupplier#getAsByte()} of <code>functor</code>.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param functor
         * @return oldBinding
         */
        @QtUninvokable
        public final QBytePropertyBinding setBinding(io.qt.QtUtilities.ByteSupplier functor)
        {
            return setBinding(new QBytePropertyBinding(functor));
        }
        
        @QtUninvokable
        private QBytePropertyBinding makeBinding()
        {
            return new QBytePropertyBinding(this::value);
        }
        
        /**
         * Checks if the property has a binding.
         * @return true if the property has a binding, false otherwise.
         */
        @QtUninvokable
        public final boolean hasBinding() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            return bd!=null && bd.hasBinding(); 
        }
        
        /**
         * Returns the binding expression that is associated with this property. 
         * A default constructed {@link QBytePropertyBinding} will be returned if no such association exists.
         * @return binding
         */
        @QtUninvokable
        public final QBytePropertyBinding binding()
        {
            return new QBytePropertyBinding(this);
        }
        
        /**
         * <p>Disassociates the binding expression from this property and returns it.</p>
         * <p>After calling this function, the value of the property will only change if you assign a new value to it, or when a new binding is set.</p>
         * @return the removed binding
         */
        @QtUninvokable
        public final QBytePropertyBinding takeBinding()
        {
            return setBinding(new QBytePropertyBinding());
        }
        
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the property changes.</p>
         * <p>The returned property change handler object keeps track of the registration. 
         * As long as the change handler is alive i.e. as long as a reference to the {@link QPropertyChangeHandler} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         */
        @QtUninvokable
        public final QPropertyChangeHandler onValueChanged(Runnable f)
        {
            return new QPropertyChangeHandler(bindingData(), f);
        }
    
        /**
         * Subscribes the given functor f as a callback that is called immediately and whenever the value of the property changes in the future.
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         * @see #onValueChanged(Runnable)
         */
        @QtUninvokable
        public final QPropertyChangeHandler subscribe(Runnable f)
        {
            f.run();
            return onValueChanged(f);
        }
    
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
         * <p>The returned property notifier object keeps track of the registration. 
         * As long as the notifier is alive i.e. as long as a reference to the {@link QPropertyNotifier} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property notifier
         * @see QPropertyNotifier
         */
        @io.qt.QtUninvokable
        public final QPropertyNotifier addNotifier(Runnable f)
        {
            return new QPropertyNotifier(bindingData(), f);
        }
        
        @QtUninvokable
        QPropertyBindingData bindingData() { 
            return bindingStorage().bindingData(this, true);
        }
        
        @QtUninvokable
        private void notifyProperty(QPropertyBindingData binding){
            if (binding!=null)
                binding.notifyObservers(this);
            emitSignal();
        }
        
        /**
         * Programmatically signals a change of the property. Any binding which depend on it will be notified, and if the property has a signal, it will be emitted.
         * <p>See <a href="@docRoot/qobjectbindableproperty.html#notify">QObjectBindableProperty::notify()</a></p>
         */
        @QtUninvokable
        public final void notifyProperty() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            notifyProperty(bd);
        }
        
        private Runnable signal = () -> {
            io.qt.internal.PropertyInfo result = CoreUtility.analyzeProperty(QObject.this, this);
            QMetaMethod notifySignal;
            if(result.property!=null) {
                notifySignal = result.notifySignal;
            }else {
                notifySignal = findNotifySignalByBindables(QObject.this, result.reflectedField, this);
            }
            if(notifySignal==null) {
                this.signal = NO_SIGNAL;
            }else{
                if(notifySignal.parameterCount()==0)
                    this.signal = new EmitSignal0(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer);
                else
                    this.signal = new EmitSignal1<>(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer, this::getValueBypassingBindings);
            }
            this.signal.run();
        };
        
        @QtUninvokable
        private boolean hasSignal() {
            return this.signal != NO_SIGNAL;
        }
        
        @QtUninvokable
        private void emitSignal() {
            this.signal.run();
        }
    }
    
    /**
     * QShortProperty is primitive-typed version of QProperty&lt;Short&gt;.
     * @see QProperty
     */
    public final class QShortProperty extends QShortPropertyData {

        QObject owner() {
            return QObject.this;
        }

        /**
         * Constructs a <code>short</code>-typed property.
         */
        public QShortProperty() {
            super();
        }
    
        /**
         * Constructs a <code>short</code>-typed property with the provided initialValue.
         * @param initialValue
         */
        public QShortProperty(short initialValue) {
            super(initialValue);
        }
        
        /**
         * Constructs a <code>short</code>-typed property with the provided binding.
         * @param binding
         */
        public QShortProperty(QShortPropertyBinding binding) {
            super();
            Runnable signal = this.signal;
            this.signal = MIGHT_HAVE_SIGNAL;
            bindingData().setBinding(binding, this);
            this.signal = signal;
        }
        
        /**
         * Constructs a <code>short</code>-typed property with the provided binding.
         * @param binding
         */
        public QShortProperty(QPropertyBinding<@QtPrimitiveType Short> binding) {
            super();
            if(io.qt.core.QShortProperty.checkType(binding.valueMetaType())){
                Runnable signal = this.signal;
                this.signal = MIGHT_HAVE_SIGNAL;
                bindingData().setBinding(binding, this);
                this.signal = signal;
            }
        }
        
        /**
         * <p>Constructs a <code>short</code>-typed property bound to the provided <code>functor</code>.</p>
         * @param functor
         */
        public QShortProperty(QtUtilities.ShortSupplier functor) {
            this(new QShortPropertyBinding(functor));
        }
        
        /**
         * Returns the value of the property. This may evaluate a binding expression that is tied to this property, before returning the value.
         * @return value
         */
        @QtUninvokable
        public final short value()
        {
            bindingStorage().registerDependency(this);
            return this.getValueBypassingBindings();
        }
        
        /**
         * <p>Assigns newValue to this property and removes the property's associated binding, if present.</p>
         * @param newValue
         */
        @QtUninvokable
        public final void setValue(short newValue)
        {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            if(bd!=null)
                bd.removeBinding();
            if (this.setValueBypassingBindings(newValue))
                notifyProperty(bd);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QShortPropertyBinding setBinding(io.qt.QtUtilities.ShortSupplier functor)
        {
            return setBinding(new QShortPropertyBinding(functor));
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>short</code>,
         * otherwise the property remains unchanged.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QShortPropertyBinding setBinding(QShortPropertyBinding newBinding)
        {
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QShortPropertyBinding)
                return (QShortPropertyBinding)oldBinding;
            else
                return new QShortPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>short</code>,
         * otherwise the property remains unchanged.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QShortPropertyBinding setBinding(QPropertyBinding<@QtPrimitiveType Short> newBinding)
        {
            if(!io.qt.core.QShortProperty.checkType(newBinding.valueMetaType()))
                return new QShortPropertyBinding();
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QShortPropertyBinding)
                return (QShortPropertyBinding)oldBinding;
            else
                return new QShortPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>short</code>, 
         * otherwise the property remains unchanged and the method returns <code>false</code>.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * <p>Returns true if the type of this property is the same as the type the binding function returns; false otherwise.</p>
         * @param newBinding
         * @return true if types match, false otherwise.
         */
        @QtUninvokable
        public final boolean setBinding(QUntypedPropertyBinding newBinding)
        {
            if(io.qt.core.QShortProperty.checkType(newBinding.valueMetaType())) {
                QPropertyBindingData bd = bindingData();
                bd.setBinding(newBinding, this);
                notifyProperty(bd);
                return true;
            }else {
                return false;
            }
        }
        
        @QtUninvokable
        private final QShortPropertyBinding makeBinding()
        {
            return new QShortPropertyBinding(this::value);
        }
        
        /**
         * Checks if the property has a binding.
         * @return true if the property has a binding, false otherwise.
         */
        @QtUninvokable
        public final boolean hasBinding() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            return bd!=null && bd.hasBinding(); 
        }
        
        /**
         * Returns the binding expression that is associated with this property. 
         * A default constructed {@link QShortPropertyBinding} will be returned if no such association exists.
         * @return binding
         */
        @QtUninvokable
        public final QShortPropertyBinding binding()
        {
            return new QShortPropertyBinding(this);
        }
        
        /**
         * <p>Disassociates the binding expression from this property and returns it.</p>
         * <p>After calling this function, the value of the property will only change if you assign a new value to it, or when a new binding is set.</p>
         * @return the removed binding
         */
        @QtUninvokable
        public final QShortPropertyBinding takeBinding()
        {
            return setBinding(new QShortPropertyBinding());
        }
        
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the property changes.</p>
         * <p>The returned property change handler object keeps track of the registration. 
         * As long as the change handler is alive i.e. as long as a reference to the {@link QPropertyChangeHandler} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         */
        @QtUninvokable
        public final QPropertyChangeHandler onValueChanged(Runnable f)
        {
            return new QPropertyChangeHandler(bindingData(), f);
        }
    
        /**
         * Subscribes the given functor f as a callback that is called immediately and whenever the value of the property changes in the future.
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         * @see #onValueChanged(Runnable)
         */
        @QtUninvokable
        public final QPropertyChangeHandler subscribe(Runnable f)
        {
            f.run();
            return onValueChanged(f);
        }
    
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
         * <p>The returned property notifier object keeps track of the registration. 
         * As long as the notifier is alive i.e. as long as a reference to the {@link QPropertyNotifier} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property notifier
         * @see QPropertyNotifier
         */
        @io.qt.QtUninvokable
        public final QPropertyNotifier addNotifier(Runnable f)
        {
            return new QPropertyNotifier(bindingData(), f);
        }
        
        @QtUninvokable
        QPropertyBindingData bindingData() { 
            return bindingStorage().bindingData(this, true);
        }
        
        @QtUninvokable
        private void notifyProperty(QPropertyBindingData binding){
            if (binding!=null)
                binding.notifyObservers(this);
            emitSignal();
        }
        
        /**
         * Programmatically signals a change of the property. Any binding which depend on it will be notified, and if the property has a signal, it will be emitted.
         * <p>See <a href="@docRoot/qobjectbindableproperty.html#notify">QObjectBindableProperty::notify()</a></p>
         */
        @QtUninvokable
        public final void notifyProperty() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            notifyProperty(bd);
        }
        
        private Runnable signal = () -> {
            io.qt.internal.PropertyInfo result = CoreUtility.analyzeProperty(QObject.this, this);
            QMetaMethod notifySignal;
            if(result.property!=null) {
                notifySignal = result.notifySignal;
            }else {
                notifySignal = findNotifySignalByBindables(QObject.this, result.reflectedField, this);
            }
            if(notifySignal==null) {
                this.signal = NO_SIGNAL;
            }else{
                if(notifySignal.parameterCount()==0)
                    this.signal = new EmitSignal0(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer);
                else
                    this.signal = new EmitSignal1<>(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer, this::getValueBypassingBindings);
            }
            this.signal.run();
        };
        
        @QtUninvokable
        private boolean hasSignal() {
            return this.signal != NO_SIGNAL;
        }
        
        @QtUninvokable
        private void emitSignal() {
            this.signal.run();
        }
    }
    
    /**
     * QIntProperty is primitive-typed version of QProperty&lt;Integer&gt;.
     * @see QProperty
     */
    public final class QIntProperty extends QIntPropertyData {

        QObject owner() {
            return QObject.this;
        }

        /**
         * Constructs an <code>int</code>-typed property.
         */
        public QIntProperty() {
            super();
        }
    
        /**
         * Constructs an <code>int</code>-typed property with the provided initialValue.
         * @param initialValue
         */
        public QIntProperty(int initialValue) {
            super(initialValue);
        }
        
        /**
         * Constructs an <code>int</code>-typed property with the provided binding.
         * @param binding
         */
        public QIntProperty(QIntPropertyBinding binding) {
            super();
            Runnable signal = this.signal;
            this.signal = MIGHT_HAVE_SIGNAL;
            bindingData().setBinding(binding, this);
            this.signal = signal;
        }
        
        /**
         * Constructs an <code>int</code>-typed property with the provided binding.
         * @param binding
         */
        public QIntProperty(QPropertyBinding<@QtPrimitiveType Integer> binding) {
            super();
            if(io.qt.core.QIntProperty.checkType(binding.valueMetaType())){
                Runnable signal = this.signal;
                this.signal = MIGHT_HAVE_SIGNAL;
                bindingData().setBinding(binding, this);
                this.signal = signal;
            }
        }
        
        /**
         * <p>Constructs an <code>int</code>-typed property bound to the provided <code>functor</code>.</p>
         * @param functor
         */
        public QIntProperty(java.util.function.IntSupplier functor) {
            this(new QIntPropertyBinding(functor));
        }
        
        /**
         * Returns the value of the property. This may evaluate a binding expression that is tied to this property, before returning the value.
         * @return value
         */
        @QtUninvokable
        public final int value()
        {
            bindingStorage().registerDependency(this);
            return this.getValueBypassingBindings();
        }
        
        /**
         * <p>Assigns newValue to this property and removes the property's associated binding, if present.</p>
         * @param newValue
         */
        @QtUninvokable
        public final void setValue(int newValue)
        {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            if(bd!=null)
                bd.removeBinding();
            if (this.setValueBypassingBindings(newValue))
                notifyProperty(bd);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QIntPropertyBinding setBinding(QIntPropertyBinding newBinding)
        {
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QIntPropertyBinding)
                return (QIntPropertyBinding)oldBinding;
            else
                return new QIntPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>int</code>,
         * otherwise the property remains unchanged.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QIntPropertyBinding setBinding(QPropertyBinding<@QtPrimitiveType Integer> newBinding)
        {
            if(!io.qt.core.QIntProperty.checkType(newBinding.valueMetaType()))
                return new QIntPropertyBinding();
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QIntPropertyBinding)
                return (QIntPropertyBinding)oldBinding;
            else
                return new QIntPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>int</code>, 
         * otherwise the property remains unchanged and the method returns <code>false</code>.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * <p>Returns true if the type of this property is the same as the type the binding function returns; false otherwise.</p>
         * @param newBinding
         * @return true if types match, false otherwise.
         */
        @QtUninvokable
        public final boolean setBinding(QUntypedPropertyBinding newBinding)
        {
            if(io.qt.core.QIntProperty.checkType(newBinding.valueMetaType())) {
                QPropertyBindingData bd = bindingData();
                bd.setBinding(newBinding, this);
                notifyProperty(bd);
                return true;
            }else {
                return false;
            }
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>functor</code> and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated by invoking {@link java.util.function.IntSupplier#getAsInt()} of <code>functor</code>.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param functor
         * @return oldBinding
         */
        @QtUninvokable
        public final QIntPropertyBinding setBinding(java.util.function.IntSupplier functor)
        {
            return setBinding(new QIntPropertyBinding(functor));
        }
        
        @QtUninvokable
        private QIntPropertyBinding makeBinding()
        {
            return new QIntPropertyBinding(this::value);
        }
        
        /**
         * Checks if the property has a binding.
         * @return true if the property has a binding, false otherwise.
         */
        @QtUninvokable
        public final boolean hasBinding() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            return bd!=null && bd.hasBinding(); 
        }
        
        /**
         * Returns the binding expression that is associated with this property. 
         * A default constructed {@link QIntPropertyBinding} will be returned if no such association exists.
         * @return binding
         */
        @QtUninvokable
        public final QIntPropertyBinding binding()
        {
            return new QIntPropertyBinding(this);
        }
        
        /**
         * <p>Disassociates the binding expression from this property and returns it.</p>
         * <p>After calling this function, the value of the property will only change if you assign a new value to it, or when a new binding is set.</p>
         * @return the removed binding
         */
        @QtUninvokable
        public final QIntPropertyBinding takeBinding()
        {
            return setBinding(new QIntPropertyBinding());
        }
        
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the property changes.</p>
         * <p>The returned property change handler object keeps track of the registration. 
         * As long as the change handler is alive i.e. as long as a reference to the {@link QPropertyChangeHandler} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         */
        @QtUninvokable
        public final QPropertyChangeHandler onValueChanged(Runnable f)
        {
            return new QPropertyChangeHandler(bindingData(), f);
        }
    
        /**
         * Subscribes the given functor f as a callback that is called immediately and whenever the value of the property changes in the future.
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         * @see #onValueChanged(Runnable)
         */
        @QtUninvokable
        public final QPropertyChangeHandler subscribe(Runnable f)
        {
            f.run();
            return onValueChanged(f);
        }
    
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
         * <p>The returned property notifier object keeps track of the registration. 
         * As long as the notifier is alive i.e. as long as a reference to the {@link QPropertyNotifier} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property notifier
         * @see QPropertyNotifier
         */
        @io.qt.QtUninvokable
        public final QPropertyNotifier addNotifier(Runnable f)
        {
            return new QPropertyNotifier(bindingData(), f);
        }
        
        @QtUninvokable
        QPropertyBindingData bindingData() { 
            return bindingStorage().bindingData(this, true);
        }
        
        @QtUninvokable
        private void notifyProperty(QPropertyBindingData binding){
            if (binding!=null)
                binding.notifyObservers(this);
            emitSignal();
        }
        
        /**
         * Programmatically signals a change of the property. Any binding which depend on it will be notified, and if the property has a signal, it will be emitted.
         * <p>See <a href="@docRoot/qobjectbindableproperty.html#notify">QObjectBindableProperty::notify()</a></p>
         */
        @QtUninvokable
        public final void notifyProperty() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            notifyProperty(bd);
        }
        
        private Runnable signal = () -> {
            io.qt.internal.PropertyInfo result = CoreUtility.analyzeProperty(QObject.this, this);
            QMetaMethod notifySignal;
            if(result.property!=null) {
                notifySignal = result.notifySignal;
            }else {
                notifySignal = findNotifySignalByBindables(QObject.this, result.reflectedField, this);
            }
            if(notifySignal==null) {
                this.signal = NO_SIGNAL;
            }else{
                if(notifySignal.parameterCount()==0)
                    this.signal = new EmitSignal0(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer);
                else
                    this.signal = new EmitSignal1<>(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer, this::getValueBypassingBindings);
            }
            this.signal.run();
        };
        
        @QtUninvokable
        private boolean hasSignal() {
            return this.signal != NO_SIGNAL;
        }
        
        @QtUninvokable
        private void emitSignal() {
            this.signal.run();
        }
    }
    
    /**
     * QLongProperty is primitive-typed version of QProperty&lt;Long&gt;.
     * @see QProperty
     */
    public final class QLongProperty extends QLongPropertyData {

        QObject owner() {
            return QObject.this;
        }

        /**
         * Constructs a <code>long</code>-typed property.
         */
        public QLongProperty() {
            super();
        }
    
        /**
         * Constructs a <code>long</code>-typed property with the provided initialValue.
         * @param initialValue
         */
        public QLongProperty(long initialValue) {
            super(initialValue);
        }
        
        /**
         * Constructs a <code>long</code>-typed property with the provided binding.
         * @param binding
         */
        public QLongProperty(QLongPropertyBinding binding) {
            super();
            Runnable signal = this.signal;
            this.signal = MIGHT_HAVE_SIGNAL;
            bindingData().setBinding(binding, this);
            this.signal = signal;
        }
        
        /**
         * Constructs a <code>long</code>-typed property with the provided binding.
         * @param binding
         */
        public QLongProperty(QPropertyBinding<@QtPrimitiveType Long> binding) {
            super();
            if(io.qt.core.QLongProperty.checkType(binding.valueMetaType())){
                Runnable signal = this.signal;
                this.signal = MIGHT_HAVE_SIGNAL;
                bindingData().setBinding(binding, this);
                this.signal = signal;
            }
        }
        
        /**
         * <p>Constructs a <code>long</code>-typed property bound to the provided <code>functor</code>.</p>
         * @param functor
         */
        public QLongProperty(java.util.function.LongSupplier functor) {
            this(new QLongPropertyBinding(functor));
        }
        
        /**
         * Returns the value of the property. This may evaluate a binding expression that is tied to this property, before returning the value.
         * @return value
         */
        @QtUninvokable
        public final long value()
        {
            bindingStorage().registerDependency(this);
            return this.getValueBypassingBindings();
        }
        
        /**
         * <p>Assigns newValue to this property and removes the property's associated binding, if present.</p>
         * @param newValue
         */
        @QtUninvokable
        public final void setValue(long newValue)
        {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            if(bd!=null)
                bd.removeBinding();
            if (this.setValueBypassingBindings(newValue))
                notifyProperty(bd);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QLongPropertyBinding setBinding(QLongPropertyBinding newBinding)
        {
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QLongPropertyBinding)
                return (QLongPropertyBinding)oldBinding;
            else
                return new QLongPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>long</code>,
         * otherwise the property remains unchanged.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QLongPropertyBinding setBinding(QPropertyBinding<@QtPrimitiveType Long> newBinding)
        {
            if(!io.qt.core.QLongProperty.checkType(newBinding.valueMetaType()))
                return new QLongPropertyBinding();
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QLongPropertyBinding)
                return (QLongPropertyBinding)oldBinding;
            else
                return new QLongPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>long</code>, 
         * otherwise the property remains unchanged and the method returns <code>false</code>.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * <p>Returns true if the type of this property is the same as the type the binding function returns; false otherwise.</p>
         * @param newBinding
         * @return true if types match, false otherwise.
         */
        @QtUninvokable
        public boolean setBinding(QUntypedPropertyBinding newBinding)
        {
            if(io.qt.core.QLongProperty.checkType(newBinding.valueMetaType())) {
                QPropertyBindingData bd = bindingData();
                bd.setBinding(newBinding, this);
                notifyProperty(bd);
                return true;
            }else {
                return false;
            }
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>functor</code> and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated by invoking {@link java.util.function.LongSupplier#getAsLong()} of <code>functor</code>.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param functor
         * @return oldBinding
         */
        @QtUninvokable
        public final QLongPropertyBinding setBinding(java.util.function.LongSupplier functor)
        {
            return setBinding(new QLongPropertyBinding(functor));
        }
        
        @QtUninvokable
        private final QLongPropertyBinding makeBinding()
        {
            return new QLongPropertyBinding(this::value);
        }
        
        /**
         * Checks if the property has a binding.
         * @return true if the property has a binding, false otherwise.
         */
        @QtUninvokable
        public final boolean hasBinding() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            return bd!=null && bd.hasBinding(); 
        }
        
        /**
         * Returns the binding expression that is associated with this property. 
         * A default constructed {@link QLongPropertyBinding} will be returned if no such association exists.
         * @return binding
         */
        @QtUninvokable
        public final QLongPropertyBinding binding()
        {
            return new QLongPropertyBinding(this);
        }
        
        /**
         * <p>Disassociates the binding expression from this property and returns it.</p>
         * <p>After calling this function, the value of the property will only change if you assign a new value to it, or when a new binding is set.</p>
         * @return the removed binding
         */
        @QtUninvokable
        public final QLongPropertyBinding takeBinding()
        {
            return setBinding(new QLongPropertyBinding());
        }
        
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the property changes.</p>
         * <p>The returned property change handler object keeps track of the registration. 
         * As long as the change handler is alive i.e. as long as a reference to the {@link QPropertyChangeHandler} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         */
        @QtUninvokable
        public final QPropertyChangeHandler onValueChanged(Runnable f)
        {
            return new QPropertyChangeHandler(bindingData(), f);
        }
    
        /**
         * Subscribes the given functor f as a callback that is called immediately and whenever the value of the property changes in the future.
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         * @see #onValueChanged(Runnable)
         */
        @QtUninvokable
        public final QPropertyChangeHandler subscribe(Runnable f)
        {
            f.run();
            return onValueChanged(f);
        }
    
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
         * <p>The returned property notifier object keeps track of the registration. 
         * As long as the notifier is alive i.e. as long as a reference to the {@link QPropertyNotifier} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property notifier
         * @see QPropertyNotifier
         */
        @io.qt.QtUninvokable
        public final QPropertyNotifier addNotifier(Runnable f)
        {
            return new QPropertyNotifier(bindingData(), f);
        }
        
        @QtUninvokable
        final QPropertyBindingData bindingData() {
            return bindingStorage().bindingData(this, true);
        }
        
        @QtUninvokable
        private void notifyProperty(QPropertyBindingData binding){
            if (binding!=null)
                binding.notifyObservers(this);
            emitSignal();
        }

        /**
         * Programmatically signals a change of the property. Any binding which depend on it will be notified, and if the property has a signal, it will be emitted.
         * <p>See <a href="@docRoot/qobjectbindableproperty.html#notify">QObjectBindableProperty::notify()</a></p>
         */
        @QtUninvokable
        public final void notifyProperty() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            notifyProperty(bd);
        }
        
        private Runnable signal = () -> {
            io.qt.internal.PropertyInfo result = CoreUtility.analyzeProperty(QObject.this, this);
            QMetaMethod notifySignal;
            if(result.property!=null) {
                notifySignal = result.notifySignal;
            }else {
                notifySignal = findNotifySignalByBindables(QObject.this, result.reflectedField, this);
            }
            if(notifySignal==null) {
                this.signal = NO_SIGNAL;
            }else{
                if(notifySignal.parameterCount()==0)
                    this.signal = new EmitSignal0(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer);
                else
                    this.signal = new EmitSignal1<>(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer, this::getValueBypassingBindings);
            }
            this.signal.run();
        };
        
        @QtUninvokable
        private boolean hasSignal() {
            return this.signal != NO_SIGNAL;
        }
        
        @QtUninvokable
        private void emitSignal() {
            this.signal.run();
        }
    }
    
    /**
     * QFloatProperty is primitive-typed version of QProperty&lt;Float&gt;.
     * @see QProperty
     */
    public final class QFloatProperty extends QFloatPropertyData {

        QObject owner() {
            return QObject.this;
        }

        /**
         * Constructs a <code>float</code>-typed property.
         */
        public QFloatProperty() {
            super();
        }
    
        /**
         * Constructs a <code>float</code>-typed property with the provided initialValue.
         * @param initialValue
         */
        public QFloatProperty(float initialValue) {
            super(initialValue);
        }
        
        /**
         * Constructs a <code>float</code>-typed property with the provided binding.
         * @param binding
         */
        public QFloatProperty(QFloatPropertyBinding binding) {
            super();
            Runnable signal = this.signal;
            this.signal = MIGHT_HAVE_SIGNAL;
            bindingData().setBinding(binding, this);
            this.signal = signal;
        }
        
        /**
         * Constructs a <code>float</code>-typed property with the provided binding.
         * @param binding
         */
        public QFloatProperty(QPropertyBinding<@QtPrimitiveType Float> binding) {
            super();
            if(io.qt.core.QFloatProperty.checkType(binding.valueMetaType())){
                Runnable signal = this.signal;
                this.signal = MIGHT_HAVE_SIGNAL;
                bindingData().setBinding(binding, this);
                this.signal = signal;
            }
        }
        
        /**
         * <p>Constructs a <code>float</code>-typed property bound to the provided <code>functor</code>.</p>
         * @param functor
         */
        public QFloatProperty(io.qt.QtUtilities.FloatSupplier functor) {
            this(new QFloatPropertyBinding(functor));
        }
        
        /**
         * Returns the value of the property. This may evaluate a binding expression that is tied to this property, before returning the value.
         * @return value
         */
        @QtUninvokable
        public final float value()
        {
            bindingStorage().registerDependency(this);
            return this.getValueBypassingBindings();
        }
        
        /**
         * <p>Assigns newValue to this property and removes the property's associated binding, if present.</p>
         * @param newValue
         */
        @QtUninvokable
        public final void setValue(float newValue)
        {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            if(bd!=null)
                bd.removeBinding();
            if (this.setValueBypassingBindings(newValue))
                notifyProperty(bd);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QFloatPropertyBinding setBinding(QFloatPropertyBinding newBinding)
        {
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QFloatPropertyBinding)
                return (QFloatPropertyBinding)oldBinding;
            else
                return new QFloatPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>float</code>,
         * otherwise the property remains unchanged.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QFloatPropertyBinding setBinding(QPropertyBinding<@QtPrimitiveType Float> newBinding)
        {
            if(!io.qt.core.QFloatProperty.checkType(newBinding.valueMetaType()))
                return new QFloatPropertyBinding();
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QFloatPropertyBinding)
                return (QFloatPropertyBinding)oldBinding;
            else
                return new QFloatPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>float</code>, 
         * otherwise the property remains unchanged and the method returns <code>false</code>.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * <p>Returns true if the type of this property is the same as the type the binding function returns; false otherwise.</p>
         * @param newBinding
         * @return true if types match, false otherwise.
         */
        @QtUninvokable
        public final boolean setBinding(QUntypedPropertyBinding newBinding)
        {
            if(io.qt.core.QFloatProperty.checkType(newBinding.valueMetaType())) {
                QPropertyBindingData bd = bindingData();
                bd.setBinding(newBinding, this);
                notifyProperty(bd);
                return true;
            }else {
                return false;
            }
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>functor</code> and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated by invoking {@link io.qt.QtUtilities.FloatSupplier#getAsFloat()} of <code>functor</code>.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param functor
         * @return oldBinding
         */
        @QtUninvokable
        public final QFloatPropertyBinding setBinding(io.qt.QtUtilities.FloatSupplier functor)
        {
            return setBinding(new QFloatPropertyBinding(functor));
        }
        
        @QtUninvokable
        private QFloatPropertyBinding makeBinding()
        {
            return new QFloatPropertyBinding(this::value);
        }
        
        /**
         * Checks if the property has a binding.
         * @return true if the property has a binding, false otherwise.
         */
        @QtUninvokable
        public final boolean hasBinding() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            return bd!=null && bd.hasBinding(); 
        }
        
        /**
         * Returns the binding expression that is associated with this property. 
         * A default constructed {@link QFloatPropertyBinding} will be returned if no such association exists.
         * @return binding
         */
        @QtUninvokable
        public final QFloatPropertyBinding binding()
        {
            return new QFloatPropertyBinding(this);
        }
        
        /**
         * <p>Disassociates the binding expression from this property and returns it.</p>
         * <p>After calling this function, the value of the property will only change if you assign a new value to it, or when a new binding is set.</p>
         * @return the removed binding
         */
        @QtUninvokable
        public final QFloatPropertyBinding takeBinding()
        {
            return setBinding(new QFloatPropertyBinding());
        }
        
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the property changes.</p>
         * <p>The returned property change handler object keeps track of the registration. 
         * As long as the change handler is alive i.e. as long as a reference to the {@link QPropertyChangeHandler} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         */
        @QtUninvokable
        public final QPropertyChangeHandler onValueChanged(Runnable f)
        {
            return new QPropertyChangeHandler(bindingData(), f);
        }
    
        /**
         * Subscribes the given functor f as a callback that is called immediately and whenever the value of the property changes in the future.
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         * @see #onValueChanged(Runnable)
         */
        @QtUninvokable
        public final QPropertyChangeHandler subscribe(Runnable f)
        {
            f.run();
            return onValueChanged(f);
        }
    
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
         * <p>The returned property notifier object keeps track of the registration. 
         * As long as the notifier is alive i.e. as long as a reference to the {@link QPropertyNotifier} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property notifier
         * @see QPropertyNotifier
         */
        @io.qt.QtUninvokable
        public final QPropertyNotifier addNotifier(Runnable f)
        {
            return new QPropertyNotifier(bindingData(), f);
        }
        
        @QtUninvokable
        final QPropertyBindingData bindingData() {
            return bindingStorage().bindingData(this, true);
        }
        
        @QtUninvokable
        private void notifyProperty(QPropertyBindingData binding){
            if (binding!=null)
                binding.notifyObservers(this);
            emitSignal();
        }
        
        /**
         * Programmatically signals a change of the property. Any binding which depend on it will be notified, and if the property has a signal, it will be emitted.
         * <p>See <a href="@docRoot/qobjectbindableproperty.html#notify">QObjectBindableProperty::notify()</a></p>
         */
        @QtUninvokable
        public final void notifyProperty() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            notifyProperty(bd);
        }
        
        private Runnable signal = () -> {
            io.qt.internal.PropertyInfo result = CoreUtility.analyzeProperty(QObject.this, this);
            QMetaMethod notifySignal;
            if(result.property!=null) {
                notifySignal = result.notifySignal;
            }else {
                notifySignal = findNotifySignalByBindables(QObject.this, result.reflectedField, this);
            }
            if(notifySignal==null) {
                this.signal = NO_SIGNAL;
            }else{
                if(notifySignal.parameterCount()==0)
                    this.signal = new EmitSignal0(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer);
                else
                    this.signal = new EmitSignal1<>(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer, this::getValueBypassingBindings);
            }
            this.signal.run();
        };
        
        @QtUninvokable
        private boolean hasSignal() {
            return this.signal != NO_SIGNAL;
        }
        
        @QtUninvokable
        private void emitSignal() {
            this.signal.run();
        }
    }
    
    /**
     * QDoubleProperty is primitive-typed version of QProperty&lt;Double&gt;.
     * @see QProperty
     */
    public final class QDoubleProperty extends QDoublePropertyData {

        QObject owner() {
            return QObject.this;
        }

        /**
         * Constructs a <code>double</code>-typed property.
         */
        public QDoubleProperty() {
            super();
        }
    
        /**
         * Constructs a <code>double</code>-typed property with the provided initialValue.
         * @param initialValue
         */
        public QDoubleProperty(double initialValue) {
            super(initialValue);
        }
        
        /**
         * Constructs a <code>double</code>-typed property with the provided binding.
         * @param binding
         */
        public QDoubleProperty(QDoublePropertyBinding binding) {
            super();
            Runnable signal = this.signal;
            this.signal = MIGHT_HAVE_SIGNAL;
            bindingData().setBinding(binding, this);
            this.signal = signal;
        }
        
        /**
         * Constructs a <code>double</code>-typed property with the provided binding.
         * @param binding
         */
        public QDoubleProperty(QPropertyBinding<@QtPrimitiveType Double> binding) {
            super();
            if(io.qt.core.QDoubleProperty.checkType(binding.valueMetaType())){
                Runnable signal = this.signal;
                this.signal = MIGHT_HAVE_SIGNAL;
                bindingData().setBinding(binding, this);
                this.signal = signal;
            }
        }
        
        /**
         * <p>Constructs a <code>double</code>-typed property bound to the provided <code>functor</code>.</p>
         * @param functor
         */
        public QDoubleProperty(java.util.function.DoubleSupplier functor) {
            this(new QDoublePropertyBinding(functor));
        }
        
        /**
         * Returns the value of the property. This may evaluate a binding expression that is tied to this property, before returning the value.
         * @return value
         */
        @QtUninvokable
        public final double value()
        {
            bindingStorage().registerDependency(this);
            return this.getValueBypassingBindings();
        }
        
        /**
         * <p>Assigns newValue to this property and removes the property's associated binding, if present.</p>
         * @param newValue
         */
        @QtUninvokable
        public final void setValue(double newValue)
        {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            if(bd!=null)
                bd.removeBinding();
            if (this.setValueBypassingBindings(newValue))
                notifyProperty(bd);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QDoublePropertyBinding setBinding(QDoublePropertyBinding newBinding)
        {
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QDoublePropertyBinding)
                return (QDoublePropertyBinding)oldBinding;
            else
                return new QDoublePropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>double</code>,
         * otherwise the property remains unchanged.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QDoublePropertyBinding setBinding(QPropertyBinding<@QtPrimitiveType Double> newBinding)
        {
            if(!io.qt.core.QDoubleProperty.checkType(newBinding.valueMetaType()))
                return new QDoublePropertyBinding();
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QDoublePropertyBinding)
                return (QDoublePropertyBinding)oldBinding;
            else
                return new QDoublePropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>double</code>, 
         * otherwise the property remains unchanged and the method returns <code>false</code>.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * <p>Returns true if the type of this property is the same as the type the binding function returns; false otherwise.</p>
         * @param newBinding
         * @return true if types match, false otherwise.
         */
        @QtUninvokable
        public final boolean setBinding(QUntypedPropertyBinding newBinding)
        {
            if(io.qt.core.QDoubleProperty.checkType(newBinding.valueMetaType())) {
                QPropertyBindingData bd = bindingData();
                bd.setBinding(newBinding, this);
                notifyProperty(bd);
                return true;
            }else {
                return false;
            }
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>functor</code> and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated by invoking {@link java.util.function.DoubleSupplier#getAsDouble()} of <code>functor</code>.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param functor
         * @return oldBinding
         */
        @QtUninvokable
        public final QDoublePropertyBinding setBinding(java.util.function.DoubleSupplier functor)
        {
            return setBinding(new QDoublePropertyBinding(functor));
        }
        
        @QtUninvokable
        private QDoublePropertyBinding makeBinding()
        {
            return new QDoublePropertyBinding(this::value);
        }
        
        /**
         * Checks if the property has a binding.
         * @return true if the property has a binding, false otherwise.
         */
        @QtUninvokable
        public final boolean hasBinding() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            return bd!=null && bd.hasBinding(); 
        }
        
        /**
         * Returns the binding expression that is associated with this property. 
         * A default constructed {@link QDoublePropertyBinding} will be returned if no such association exists.
         * @return binding
         */
        @QtUninvokable
        public final QDoublePropertyBinding binding()
        {
            return new QDoublePropertyBinding(this);
        }
        
        /**
         * <p>Disassociates the binding expression from this property and returns it.</p>
         * <p>After calling this function, the value of the property will only change if you assign a new value to it, or when a new binding is set.</p>
         * @return the removed binding
         */
        @QtUninvokable
        public final QDoublePropertyBinding takeBinding()
        {
            return setBinding(new QDoublePropertyBinding());
        }
        
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the property changes.</p>
         * <p>The returned property change handler object keeps track of the registration. 
         * As long as the change handler is alive i.e. as long as a reference to the {@link QPropertyChangeHandler} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         */
        @QtUninvokable
        public final QPropertyChangeHandler onValueChanged(Runnable f)
        {
            return new QPropertyChangeHandler(bindingData(), f);
        }
    
        /**
         * Subscribes the given functor f as a callback that is called immediately and whenever the value of the property changes in the future.
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         * @see #onValueChanged(Runnable)
         */
        @QtUninvokable
        public final QPropertyChangeHandler subscribe(Runnable f)
        {
            f.run();
            return onValueChanged(f);
        }
    
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
         * <p>The returned property notifier object keeps track of the registration. 
         * As long as the notifier is alive i.e. as long as a reference to the {@link QPropertyNotifier} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property notifier
         * @see QPropertyNotifier
         */
        @io.qt.QtUninvokable
        public final QPropertyNotifier addNotifier(Runnable f)
        {
            return new QPropertyNotifier(bindingData(), f);
        }
        
        @QtUninvokable
        final QPropertyBindingData bindingData() {
            return bindingStorage().bindingData(this, true);
        }
        
        @QtUninvokable
        private void notifyProperty(QPropertyBindingData binding){
            if (binding!=null)
                binding.notifyObservers(this);
            emitSignal();
        }
        
        /**
         * Programmatically signals a change of the property. Any binding which depend on it will be notified, and if the property has a signal, it will be emitted.
         * <p>See <a href="@docRoot/qobjectbindableproperty.html#notify">QObjectBindableProperty::notify()</a></p>
         */
        @QtUninvokable
        public final void notifyProperty() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            notifyProperty(bd);
        }
        
        private Runnable signal = () -> {
            io.qt.internal.PropertyInfo result = CoreUtility.analyzeProperty(QObject.this, this);
            QMetaMethod notifySignal;
            if(result.property!=null) {
                notifySignal = result.notifySignal;
            }else {
                notifySignal = findNotifySignalByBindables(QObject.this, result.reflectedField, this);
            }
            if(notifySignal==null) {
                this.signal = NO_SIGNAL;
            }else{
                if(notifySignal.parameterCount()==0)
                    this.signal = new EmitSignal0(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer);
                else
                    this.signal = new EmitSignal1<>(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer, this::getValueBypassingBindings);
            }
            this.signal.run();
        };
        
        @QtUninvokable
        private boolean hasSignal() {
            return this.signal != NO_SIGNAL;
        }
        
        @QtUninvokable
        private void emitSignal() {
            this.signal.run();
        }
    }
    
    /**
     * QCharProperty is primitive-typed version of QProperty&lt;Character&gt;.
     * @see QProperty
     */
    public final class QCharProperty extends QCharPropertyData {

        QObject owner() {
            return QObject.this;
        }

        /**
         * Constructs a <code>char</code>-typed property.
         */
        public QCharProperty() {
            super();
        }
    
        /**
         * Constructs a <code>char</code>-typed property with the provided initialValue.
         * @param initialValue
         */
        public QCharProperty(char initialValue) {
            super(initialValue);
        }
        
        /**
         * Constructs a <code>char</code>-typed property with the provided binding.
         * @param binding
         */
        public QCharProperty(QCharPropertyBinding binding) {
            super();
            Runnable signal = this.signal;
            this.signal = MIGHT_HAVE_SIGNAL;
            bindingData().setBinding(binding, this);
            this.signal = signal;
        }
        
        /**
         * Constructs a <code>char</code>-typed property with the provided binding.
         * @param binding
         */
        public QCharProperty(QPropertyBinding<@QtPrimitiveType Character> binding) {
            super();
            if(io.qt.core.QCharProperty.checkType(binding.valueMetaType())){
                Runnable signal = this.signal;
                this.signal = MIGHT_HAVE_SIGNAL;
                bindingData().setBinding(binding, this);
                this.signal = signal;
            }
        }
        
        /**
         * <p>Constructs a <code>char</code>-typed property bound to the provided <code>functor</code>.</p>
         * @param functor
         */
        public QCharProperty(io.qt.QtUtilities.CharSupplier functor) {
            this(new QCharPropertyBinding(functor));
        }
        
        /**
         * Returns the value of the property. This may evaluate a binding expression that is tied to this property, before returning the value.
         * @return value
         */
        @QtUninvokable
        public final char value()
        {
            bindingStorage().registerDependency(this);
            return this.getValueBypassingBindings();
        }
        
        /**
         * <p>Assigns newValue to this property and removes the property's associated binding, if present.</p>
         * @param newValue
         */
        @QtUninvokable
        public final void setValue(char newValue)
        {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            if(bd!=null)
                bd.removeBinding();
            if (this.setValueBypassingBindings(newValue))
                notifyProperty(bd);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QCharPropertyBinding setBinding(QCharPropertyBinding newBinding)
        {
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QCharPropertyBinding)
                return (QCharPropertyBinding)oldBinding;
            else
                return new QCharPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression and returns the previously associated binding.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>char</code>,
         * otherwise the property remains unchanged.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param newBinding
         * @return oldBinding
         */
        @QtUninvokable
        public final QCharPropertyBinding setBinding(QPropertyBinding<@QtPrimitiveType Character> newBinding)
        {
            if(!io.qt.core.QCharProperty.checkType(newBinding.valueMetaType()))
                return new QCharPropertyBinding();
            QPropertyBindingData bd = bindingData();
            QUntypedPropertyBinding oldBinding = bd.setBinding(newBinding, this);
            notifyProperty(bd);
            if(oldBinding instanceof QCharPropertyBinding)
                return (QCharPropertyBinding)oldBinding;
            else
                return new QCharPropertyBinding(oldBinding);
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>newBinding</code> expression.</p>
         * <p>The binding's value type ({@link QUntypedPropertyBinding#valueMetaType()}) has to be <code>char</code>, 
         * otherwise the property remains unchanged and the method returns <code>false</code>.</p>
         * <p>The first time the property value is read, the binding is evaluated. 
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * <p>Returns true if the type of this property is the same as the type the binding function returns; false otherwise.</p>
         * @param newBinding
         * @return true if types match, false otherwise.
         */
        @QtUninvokable
        public final boolean setBinding(QUntypedPropertyBinding newBinding)
        {
            if(io.qt.core.QCharProperty.checkType(newBinding.valueMetaType())) {
                QPropertyBindingData bd = bindingData();
                bd.setBinding(newBinding, this);
                notifyProperty(bd);
                return true;
            }else {
                return false;
            }
        }
        
        /**
         * <p>Associates the value of this property with the provided <code>functor</code> and returns the previously associated binding.</p>
         * <p>The first time the property value is read, the binding is evaluated by invoking {@link io.qt.QtUtilities.CharSupplier#getAsChar()} of <code>functor</code>.
         * Whenever a dependency of the binding changes, the binding will be re-evaluated the next time the value of this property is read.</p>
         * @param functor
         * @return oldBinding
         */
        @QtUninvokable
        public final QCharPropertyBinding setBinding(io.qt.QtUtilities.CharSupplier functor)
        {
            return setBinding(new QCharPropertyBinding(functor));
        }
        
        @QtUninvokable
        private QCharPropertyBinding makeBinding()
        {
            return new QCharPropertyBinding(this::value);
        }
        
        /**
         * Checks if the property has a binding.
         * @return true if the property has a binding, false otherwise.
         */
        @QtUninvokable
        public final boolean hasBinding() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            return bd!=null && bd.hasBinding(); 
        }
        
        /**
         * Returns the binding expression that is associated with this property. 
         * A default constructed {@link QCharPropertyBinding} will be returned if no such association exists.
         * @return binding
         */
        @QtUninvokable
        public final QCharPropertyBinding binding()
        {
            return new QCharPropertyBinding(this);
        }
        
        /**
         * <p>Disassociates the binding expression from this property and returns it.</p>
         * <p>After calling this function, the value of the property will only change if you assign a new value to it, or when a new binding is set.</p>
         * @return the removed binding
         */
        @QtUninvokable
        public final QCharPropertyBinding takeBinding()
        {
            return setBinding(new QCharPropertyBinding());
        }
        
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the property changes.</p>
         * <p>The returned property change handler object keeps track of the registration. 
         * As long as the change handler is alive i.e. as long as a reference to the {@link QPropertyChangeHandler} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         */
        @QtUninvokable
        public final QPropertyChangeHandler onValueChanged(Runnable f)
        {
            return new QPropertyChangeHandler(bindingData(), f);
        }
    
        /**
         * Subscribes the given functor f as a callback that is called immediately and whenever the value of the property changes in the future.
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         * @see #onValueChanged(Runnable)
         */
        @QtUninvokable
        public final QPropertyChangeHandler subscribe(Runnable f)
        {
            f.run();
            return onValueChanged(f);
        }
    
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
         * <p>The returned property notifier object keeps track of the registration. 
         * As long as the notifier is alive i.e. as long as a reference to the {@link QPropertyNotifier} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property notifier
         * @see QPropertyNotifier
         */
        @io.qt.QtUninvokable
        public final QPropertyNotifier addNotifier(Runnable f)
        {
            return new QPropertyNotifier(bindingData(), f);
        }
        
        @QtUninvokable
        final QPropertyBindingData bindingData() {
            return bindingStorage().bindingData(this, true);
        }
        
        @QtUninvokable
        private void notifyProperty(QPropertyBindingData binding){
            if (binding!=null)
                binding.notifyObservers(this);
            emitSignal();
        }
        
        /**
         * Programmatically signals a change of the property. Any binding which depend on it will be notified, and if the property has a signal, it will be emitted.
         * <p>See <a href="@docRoot/qobjectbindableproperty.html#notify">QObjectBindableProperty::notify()</a></p>
         */
        @QtUninvokable
        public final void notifyProperty() {
            QPropertyBindingData bd = bindingStorage().bindingData(this);
            notifyProperty(bd);
        }
        
        private Runnable signal = () -> {
            io.qt.internal.PropertyInfo result = CoreUtility.analyzeProperty(QObject.this, this);
            QMetaMethod notifySignal;
            if(result.property!=null) {
                notifySignal = result.notifySignal;
            }else {
                notifySignal = findNotifySignalByBindables(QObject.this, result.reflectedField, this);
            }
            if(notifySignal==null) {
                this.signal = NO_SIGNAL;
            }else{
                if(notifySignal.parameterCount()==0)
                    this.signal = new EmitSignal0(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer);
                else
                    this.signal = new EmitSignal1<>(notifySignal.methodIndex(), notifySignal.enclosingMetaObject().metaObjectPointer, this::getValueBypassingBindings);
            }
            this.signal.run();
        };
        
        @QtUninvokable
        private boolean hasSignal() {
            return this.signal != NO_SIGNAL;
        }
        
        @QtUninvokable
        private void emitSignal() {
            this.signal.run();
        }
    }
    
    private abstract class QAbstractComputedProperty extends QUntypedPropertyData {

        QObject owner() {
            return QObject.this;
        }

        @QtUninvokable
        public final boolean hasBinding() { return false; }
                
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the property changes.</p>
         * <p>The returned property change handler object keeps track of the registration. 
         * As long as the change handler is alive i.e. as long as a reference to the {@link QPropertyChangeHandler} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         */
        @QtUninvokable
        public final QPropertyChangeHandler onValueChanged(Runnable f)
        {
            return new QPropertyChangeHandler(bindingData(), f);
        }
    
        /**
         * Subscribes the given functor f as a callback that is called immediately and whenever the value of the property changes in the future.
         * @param f
         * @return property change handler
         * @see QPropertyChangeHandler
         * @see #onValueChanged(Runnable)
         */
        @QtUninvokable
        public final QPropertyChangeHandler subscribe(Runnable f)
        {
            f.run();
            return onValueChanged(f);
        }
    
        /**
         * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
         * <p>The returned property notifier object keeps track of the registration. 
         * As long as the notifier is alive i.e. as long as a reference to the {@link QPropertyNotifier} instance exists, 
         * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
         * @param f
         * @return property notifier
         * @see QPropertyNotifier
         */
        @io.qt.QtUninvokable
        public final QPropertyNotifier addNotifier(Runnable f)
        {
            return new QPropertyNotifier(bindingData(), f);
        }
        
        @QtUninvokable
        final QPropertyBindingData bindingData() { 
            return bindingStorage().bindingData(this, true);
        }
        
        @QtUninvokable
        public abstract QMetaType valueMetaType();
        
        /**
         * Programmatically signals a change of the property. Any binding which depend on it will be notified, and if the property has a signal, it will be emitted.
         */
        @QtUninvokable
        public final void notifyProperty() {
            // computed property can't store a binding, so there's nothing to mark
            io.qt.core.QBindingStorage storage = bindingStorage();
            QPropertyBindingData bd = storage.bindingData(this, false);
            if (bd!=null)
                bd.notifyObservers(this, bindingStorage());
        }
    }
    
    /**
     * <p>The <code>QComputedProperty</code> class is a template class for externally computed/stored values.
     * Create an instance of <code>QComputedProperty</code> by providing a value supplier.</p>
     * <p>Example:</p>
     * <code>
     * private final QComputedProperty&lt;QColor> color = new QComputedProperty&lt;>(this::computeOptimalColor);
     * </code>
     * <p>The Java type <code>QComputedProperty</code> corresponds to the C++ type <code>QObjectComputedProperty</code>.</p>
     * <p>For primitive-typed implementations see:</p>
     * <ul>
     * <li>{@link QComputedBooleanProperty}</li>
     * <li>{@link QComputedByteProperty}</li>
     * <li>{@link QComputedShortProperty}</li>
     * <li>{@link QComputedIntProperty}</li>
     * <li>{@link QComputedLongProperty}</li>
     * <li>{@link QComputedFloatProperty}</li>
     * <li>{@link QComputedDoubleProperty}</li>
     * <li>{@link QComputedCharProperty}</li>
     * </ul>
     */
    public final class QComputedProperty<T> extends QAbstractComputedProperty {
        
        private final QtUtilities.Supplier<? extends T> getter;
        
        /**
         * Constructs a computed-value property whose type corresponds to the given value supplier.
         * @param getter supplying the computed value
         */
        public QComputedProperty(QtUtilities.Supplier<? extends T> getter) {
            super();
            QPropertyBinding.analyzeMetaType(getter);
            this.getter = getter;
        }
    
        /**
         * Returns the value of the property computed by the given getter.
         * @return value
         */
        @QtUninvokable
        public final T value()
        {
            bindingStorage().registerDependency(this);
            return getter.get();
        }
        
        @QtUninvokable
        public final QMetaType valueMetaType() {
            return QPropertyBinding.analyzeMetaType(getter);
        }
    }
    
    /**
     * QComputedBooleanProperty is primitive-typed version of QComputedProperty&lt;Boolean&gt;.
     * @see QComputedProperty
     */
    public final class QComputedBooleanProperty extends QAbstractComputedProperty {
        
        private final java.util.function.BooleanSupplier getter;
    
        /**
         * Constructs a <code>boolean</code>-typed computed-value property according to the given value supplier.
         * @param getter supplying the computed value
         */
        public QComputedBooleanProperty(java.util.function.BooleanSupplier getter) {
            super();
            this.getter = getter;
        }
    
        /**
         * Returns the value of the property computed by the given getter.
         * @return value
         */
        @QtUninvokable
        public final boolean value()
        {
            bindingStorage().registerDependency(this);
            return getter.getAsBoolean();
        }
        
        @QtUninvokable
        public final QMetaType valueMetaType() {
            return new QMetaType(QMetaType.Type.Bool);
        }
    }
    
    /**
     * QComputedByteProperty is primitive-typed version of QComputedProperty&lt;Byte&gt;.
     * @see QComputedProperty
     */
    public final class QComputedByteProperty extends QAbstractComputedProperty {
        
        private final QtUtilities.ByteSupplier getter;
    
        /**
         * Constructs a <code>byte</code>-typed computed-value property according to the given value supplier.
         * @param getter supplying the computed value
         */
        public QComputedByteProperty(QtUtilities.ByteSupplier getter) {
            super();
            this.getter = getter;
        }
    
        /**
         * Returns the value of the property computed by the given getter.
         * @return value
         */
        @QtUninvokable
        public final byte value()
        {
            bindingStorage().registerDependency(this);
            return getter.getAsByte();
        }
        
        @QtUninvokable
        private QBytePropertyBinding makeBinding()
        {
            return new QBytePropertyBinding(this::value);
        }
        
        @QtUninvokable
        public final QMetaType valueMetaType() {
            return new QMetaType(QMetaType.Type.SChar);
        }
    }
    
    /**
     * QComputedShortProperty is primitive-typed version of QComputedProperty&lt;Short&gt;.
     * @see QComputedProperty
     */
    public final class QComputedShortProperty extends QAbstractComputedProperty {
        
        private final QtUtilities.ShortSupplier getter;
    
        /**
         * Constructs a <code>short</code>-typed computed-value property according to the given value supplier.
         * @param getter supplying the computed value
         */
        public QComputedShortProperty(QtUtilities.ShortSupplier getter) {
            super();
            this.getter = getter;
        }
    
        /**
         * Returns the value of the property computed by the given getter.
         * @return value
         */
        @QtUninvokable
        public final short value()
        {
            bindingStorage().registerDependency(this);
            return getter.getAsShort();
        }
        
        @QtUninvokable
        private QShortPropertyBinding makeBinding()
        {
            return new QShortPropertyBinding(this::value);
        }
        
        @QtUninvokable
        public final QMetaType valueMetaType() {
            return new QMetaType(QMetaType.Type.Short);
        }
    }
    
    /**
     * QComputedIntProperty is primitive-typed version of QComputedProperty&lt;Integer&gt;.
     * @see QComputedProperty
     */
    public final class QComputedIntProperty extends QAbstractComputedProperty {
        
        private final java.util.function.IntSupplier getter;
    
        /**
         * Constructs a <code>int</code>-typed computed-value property according to the given value supplier.
         * @param getter supplying the computed value
         */
        public QComputedIntProperty(java.util.function.IntSupplier getter) {
            super();
            this.getter = getter;
        }
    
        /**
         * Returns the value of the property computed by the given getter.
         * @return value
         */
        @QtUninvokable
        public final int value()
        {
            bindingStorage().registerDependency(this);
            return getter.getAsInt();
        }
        
        @QtUninvokable
        private QIntPropertyBinding makeBinding()
        {
            return new QIntPropertyBinding(this::value);
        }
        
        @QtUninvokable
        public final QMetaType valueMetaType() {
            return new QMetaType(QMetaType.Type.Int);
        }
    }
    
    /**
     * QComputedLongProperty is primitive-typed version of QComputedProperty&lt;Long&gt;.
     * @see QComputedProperty
     */
    public final class QComputedLongProperty extends QAbstractComputedProperty {
        
        private final java.util.function.LongSupplier getter;
    
        /**
         * Constructs a <code>long</code>-typed computed-value property according to the given value supplier.
         * @param getter supplying the computed value
         */
        public QComputedLongProperty(java.util.function.LongSupplier getter) {
            super();
            this.getter = getter;
        }
    
        /**
         * Returns the value of the property computed by the given getter.
         * @return value
         */
        @QtUninvokable
        public final long value()
        {
            bindingStorage().registerDependency(this);
            return getter.getAsLong();
        }
        
        @QtUninvokable
        private QLongPropertyBinding makeBinding()
        {
            return new QLongPropertyBinding(this::value);
        }
        
        @QtUninvokable
        public final QMetaType valueMetaType() {
            return new QMetaType(QMetaType.Type.LongLong);
        }
    }
    
    /**
     * QComputedFloatProperty is primitive-typed version of QComputedProperty&lt;Float&gt;.
     * @see QComputedProperty
     */
    public final class QComputedFloatProperty extends QAbstractComputedProperty {
        
        private final QtUtilities.FloatSupplier getter;
    
        /**
         * Constructs a <code>float</code>-typed computed-value property according to the given value supplier.
         * @param getter supplying the computed value
         */
        public QComputedFloatProperty(QtUtilities.FloatSupplier getter) {
            super();
            this.getter = getter;
        }
    
        /**
         * Returns the value of the property computed by the given getter.
         * @return value
         */
        @QtUninvokable
        public final float value()
        {
            bindingStorage().registerDependency(this);
            return getter.getAsFloat();
        }
        
        @QtUninvokable
        private QFloatPropertyBinding makeBinding()
        {
            return new QFloatPropertyBinding(this::value);
        }
        
        @QtUninvokable
        public final QMetaType valueMetaType() {
            return new QMetaType(QMetaType.Type.Float);
        }
    }
    
    /**
     * QComputedDoubleProperty is primitive-typed version of QComputedProperty&lt;Double&gt;.
     * @see QComputedProperty
     */
    public final class QComputedDoubleProperty extends QAbstractComputedProperty {
        
        private final java.util.function.DoubleSupplier getter;
    
        /**
         * Constructs a <code>double</code>-typed computed-value property according to the given value supplier.
         * @param getter supplying the computed value
         */
        public QComputedDoubleProperty(java.util.function.DoubleSupplier getter) {
            super();
            this.getter = getter;
        }
    
        /**
         * Returns the value of the property computed by the given getter.
         * @return value
         */
        @QtUninvokable
        public final double value()
        {
            bindingStorage().registerDependency(this);
            return getter.getAsDouble();
        }
        
        @QtUninvokable
        private QDoublePropertyBinding makeBinding()
        {
            return new QDoublePropertyBinding(this::value);
        }
        
        @QtUninvokable
        public final QMetaType valueMetaType() {
            return new QMetaType(QMetaType.Type.Double);
        }
    }
    
    /**
     * QComputedCharProperty is primitive-typed version of QComputedProperty&lt;Character&gt;.
     * @see QComputedProperty
     */
    public final class QComputedCharProperty extends QAbstractComputedProperty {
        
        private final QtUtilities.CharSupplier getter;
    
        /**
         * Constructs a <code>char</code>-typed computed-value property according to the given value supplier.
         * @param getter supplying the computed value
         */
        public QComputedCharProperty(QtUtilities.CharSupplier getter) {
            super();
            this.getter = getter;
        }
    
        /**
         * Returns the value of the property computed by the given getter.
         * @return value
         */
        @QtUninvokable
        public final char value()
        {
            bindingStorage().registerDependency(this);
            return getter.getAsChar();
        }
        
        @QtUninvokable
        private QCharPropertyBinding makeBinding()
        {
            return new QCharPropertyBinding(this::value);
        }
        
        @QtUninvokable
        public final QMetaType valueMetaType() {
            return new QMetaType(QMetaType.Type.QChar);
        }
    }

    private static class CoreUtility extends io.qt.internal.CoreUtility{
        static io.qt.internal.PropertyInfo analyzeProperty(QObject object, QtObject property) {
            return io.qt.internal.CoreUtility.analyzeProperty(object, property, io.qt.internal.PropertyInfo.class);
        }

        protected static void registerPropertyField(QMetaProperty metaProperty, java.lang.reflect.Field field) {
            io.qt.internal.CoreUtility.registerPropertyField(metaProperty, field);
        }

        protected static void emitNativeSignal(QObject sender, int methodIndex, long metaObjectId, Object... args) {
            io.qt.internal.CoreUtility.emitNativeSignal(sender, methodIndex, metaObjectId, args);
        }

        protected static boolean disconnectAll(QtSignalEmitterInterface sender, Object receiver) {
            return io.qt.internal.CoreUtility.disconnectAll(sender, receiver);
        }

        protected static boolean disconnectOne(QMetaObject.Connection connection) {
            return io.qt.internal.CoreUtility.disconnectOne(connection);
        }
    }

    static QThread getQPropertyThread(io.qt.internal.ClassAnalyzerUtility.LambdaInfo info) {
        if(info!=null) {
            if(info.owner==null && info.isStatic && info.lambdaArgs.size()>0) {
                Object arg = info.lambdaArgs.get(0);
                if(arg instanceof QUntypedPropertyData) {
                    if(arg instanceof QProperty) {
                        QProperty<?> property = (QProperty<?>)arg;
                        return property.owner().thread();
                    }else if(arg instanceof QByteProperty) {
                        QByteProperty property = (QByteProperty)arg;
                        return property.owner().thread();
                    }else if(arg instanceof QShortProperty) {
                        QShortProperty property = (QShortProperty)arg;
                        return property.owner().thread();
                    }else if(arg instanceof QIntProperty) {
                        QIntProperty property = (QIntProperty)arg;
                        return property.owner().thread();
                    }else if(arg instanceof QLongProperty) {
                        QLongProperty property = (QLongProperty)arg;
                        return property.owner().thread();
                    }else if(arg instanceof QFloatProperty) {
                        QFloatProperty property = (QFloatProperty)arg;
                        return property.owner().thread();
                    }else if(arg instanceof QDoubleProperty) {
                        QDoubleProperty property = (QDoubleProperty)arg;
                        return property.owner().thread();
                    }else if(arg instanceof QCharProperty) {
                        QCharProperty property = (QCharProperty)arg;
                        return property.owner().thread();
                    }else if(arg instanceof QBooleanProperty) {
                        QBooleanProperty property = (QBooleanProperty)arg;
                        return property.owner().thread();
                    }else if(arg instanceof QAbstractComputedProperty) {
                        QAbstractComputedProperty property = (QAbstractComputedProperty)arg;
                        return property.owner().thread();
                    }
                }
            }else if(info.owner instanceof QUntypedPropertyData) {
                if(info.owner instanceof QProperty) {
                    QProperty<?> property = (QProperty<?>)info.owner;
                    return property.owner().thread();
                }else if(info.owner instanceof QByteProperty) {
                    QByteProperty property = (QByteProperty)info.owner;
                    return property.owner().thread();
                }else if(info.owner instanceof QShortProperty) {
                    QShortProperty property = (QShortProperty)info.owner;
                    return property.owner().thread();
                }else if(info.owner instanceof QIntProperty) {
                    QIntProperty property = (QIntProperty)info.owner;
                    return property.owner().thread();
                }else if(info.owner instanceof QLongProperty) {
                    QLongProperty property = (QLongProperty)info.owner;
                    return property.owner().thread();
                }else if(info.owner instanceof QFloatProperty) {
                    QFloatProperty property = (QFloatProperty)info.owner;
                    return property.owner().thread();
                }else if(info.owner instanceof QDoubleProperty) {
                    QDoubleProperty property = (QDoubleProperty)info.owner;
                    return property.owner().thread();
                }else if(info.owner instanceof QCharProperty) {
                    QCharProperty property = (QCharProperty)info.owner;
                    return property.owner().thread();
                }else if(info.owner instanceof QBooleanProperty) {
                    QBooleanProperty property = (QBooleanProperty)info.owner;
                    return property.owner().thread();
                }else if(info.owner instanceof QAbstractComputedProperty) {
                    QAbstractComputedProperty property = (QAbstractComputedProperty)info.owner;
                    return property.owner().thread();
                }
            }
        }
        return null;
    }

}// class

class QSignalTransition___{
    
    /**
     * Overloaded constructor for {@link #QSignalTransition(io.qt.core.QMetaObject.AbstractSignal, io.qt.core.QState)}.
     */
    public QSignalTransition(io.qt.core.QMetaObject.AbstractSignal signal) {
        this(signal, null);
    }
    
    /**
     * Constructs a new signal transition associated with the given signal of the given sender object and with the given sourceState.
     */
    public QSignalTransition(io.qt.core.QMetaObject.AbstractSignal signal, QState sourceState) {
        super((QPrivateConstructor)null);
        if(signal.containingObject() instanceof io.qt.core.QObject) {
            io.qt.core.QObject sender = (io.qt.core.QObject)signal.containingObject();
            String signalSignature = "2" + sender.metaObject().methods().get(signal.methodIndex()).cppMethodSignature();
            initialize_native(this, sender, signalSignature, sourceState);
        }else {
            throw new IllegalArgumentException("Signal is not owned by a QObject.");
        }
    }
    
    /**
     * Sets the signal associated with this signal transition.
     */
    @io.qt.QtUninvokable
    public final void setSignal(io.qt.core.QMetaObject.AbstractSignal signal){
        if(signal.containingObject() instanceof io.qt.core.QObject) {
            io.qt.core.QObject sender = (io.qt.core.QObject)signal.containingObject();
            String signalSignature = "2" + sender.metaObject().methods().get(signal.methodIndex()).cppMethodSignature();
            setSenderObject(sender);
            setSignal(new io.qt.core.QByteArray(signalSignature));
        }else {
            throw new IllegalArgumentException("Signal is not owned by a QObject.");
        }
    }
    
}// class

class QState___{
    
    /**
     * Adds a transition associated with the given signal of the given sender object, and returns the new QSignalTransition object. 
     * The transition has this state as the source, and the given target as the target state.
     */
    @io.qt.QtUninvokable
    public final QSignalTransition addTransition(io.qt.core.QMetaObject.AbstractSignal signal, QAbstractState target) {
        if(signal.containingObject() instanceof io.qt.core.QObject && signal.methodIndex()!=0) {
            io.qt.core.QObject sender = (io.qt.core.QObject)signal.containingObject();
            String signalSignature = "2" + sender.metaObject().methods().get(signal.methodIndex()).cppMethodSignature();
            return addTransition(sender, signalSignature, target);
        }else {
            throw new IllegalArgumentException("Signal is not owned by a QObject.");
        }
    }
    
}// class

abstract class QUrl___ extends QUrl{
    
    /**
     * Supertype interface for {@link UrlFormattingOption} and {@link ComponentFormattingOption}
     * to be joined in QFiags type {@link FormattingOptions}
     */
    public interface FormattingOption extends io.qt.QtFlagEnumerator{
        
        public final static FormattingOption None                   = UrlFormattingOption.None;
        public final static FormattingOption RemoveScheme           = UrlFormattingOption.RemoveScheme;
        public final static FormattingOption RemovePassword         = UrlFormattingOption.RemovePassword;
        public final static FormattingOption RemoveUserInfo         = UrlFormattingOption.RemoveUserInfo;
        public final static FormattingOption RemovePort             = UrlFormattingOption.RemovePort;
        public final static FormattingOption RemoveAuthority        = UrlFormattingOption.RemoveAuthority;
        public final static FormattingOption RemovePath             = UrlFormattingOption.RemovePath;
        public final static FormattingOption RemoveQuery            = UrlFormattingOption.RemoveQuery;
        public final static FormattingOption RemoveFragment         = UrlFormattingOption.RemoveFragment;
        public final static FormattingOption PreferLocalFile        = UrlFormattingOption.PreferLocalFile;
        public final static FormattingOption StripTrailingSlash     = UrlFormattingOption.StripTrailingSlash;
        public final static FormattingOption RemoveFilename         = UrlFormattingOption.RemoveFilename;
        public final static FormattingOption NormalizePathSegments  = UrlFormattingOption.NormalizePathSegments;
        public final static FormattingOption EncodeSpaces           = ComponentFormattingOption.EncodeSpaces;
        public final static FormattingOption EncodeUnicode          = ComponentFormattingOption.EncodeUnicode;
        public final static FormattingOption EncodeDelimiters       = ComponentFormattingOption.EncodeDelimiters;
        public final static FormattingOption EncodeReserved         = ComponentFormattingOption.EncodeReserved;
        public final static FormattingOption DecodeReserved         = ComponentFormattingOption.DecodeReserved;
        public final static FormattingOption FullyEncoded           = ComponentFormattingOption.FullyEncoded;
        public final static FormattingOption FullyDecoded           = ComponentFormattingOption.FullyDecoded;
            
        public static FormattingOptions flags(FormattingOption... values) {
            return new FormattingOptions(values);
        }
        
        public default io.qt.QFlags<?> asFlags() {
            return new FormattingOptions(value());
        }
        
        public static FormattingOption resolve(int value) {
            try {
                return UrlFormattingOption.resolve(value);
            }catch(io.qt.QNoSuchEnumValueException e){
                return ComponentFormattingOption.resolve(value);
            }
        }
        
        public static FormattingOption[] values() {
            UrlFormattingOption[] v1 = UrlFormattingOption.values();
            ComponentFormattingOption[] v2 = ComponentFormattingOption.values();
            FormattingOption[] values = new FormattingOption[v1.length + v2.length];
            System.arraycopy(v1, 0, values, 0, v1.length);
            System.arraycopy(v2, 0, values, v1.length, v2.length);
            return values;
        }
    }
    
    /**
     * QFlags type for joining {@link UrlFormattingOption} and {@link ComponentFormattingOption}.
     */
    public static final class FormattingOptions extends io.qt.QFlags<FormattingOption> implements Comparable<FormattingOptions> {
        private static final long serialVersionUID = -4458464052834800982L;

        public FormattingOptions(FormattingOption ... args) {
            super(args); 
            for (FormattingOption arg : args) {
                if( !(arg instanceof UrlFormattingOption) 
                        && !(arg instanceof ComponentFormattingOption) ) {
                    throw new IllegalArgumentException("Custom implementations of FormattingOption (" + QtJambi_LibraryUtilities.internal.getClass(arg).getName() + ") are not allowed.");
                }
            }
        }
        
        public FormattingOptions(int value) { 
            super(value); 
        }
        
        @Override
        public FormattingOptions clone() {
            return new FormattingOptions(value());
        }
        
        @Override
        public FormattingOptions combined(FormattingOption flag) {
            if( !(flag instanceof UrlFormattingOption) 
                    && !(flag instanceof ComponentFormattingOption) ) {
                throw new IllegalArgumentException("Custom implementations of FormattingOption (" + QtJambi_LibraryUtilities.internal.getClass(flag).getName() + ") are not allowed.");
            }
            return new FormattingOptions(value() | flag.value());
        }
        
        @Override
        public FormattingOption[] flags() {
            return super.flags(FormattingOption.values());
        }

        @Override
        public int compareTo(FormattingOptions o) {
                return Integer.compare(value(), o.value());
        }
    }

    /**
     * <p>See <a href="@docRoot/qurl.html#toDisplayString">QUrl::toDisplayString(QUrl::FormattingOptions)const</a></p>
     */
    @io.qt.QtUninvokable
    public final java.lang.String toDisplayString(FormattingOption... options)    {
        return toDisplayString(new FormattingOptions(options));
    }

    /**
     * <p>See <a href="@docRoot/qurl.html#toString">QUrl::toString(QUrl::FormattingOptions)const</a></p>
     */
    @io.qt.QtUninvokable
    public final java.lang.String toString(FormattingOption... options)    {
        return toString(new FormattingOptions(options));
    }
    
    /**
     * <p>See <a href="@docRoot/qurl.html#url">QUrl::url(QUrl::FormattingOptions)const</a></p>
     */
    @io.qt.QtUninvokable
    public final java.lang.String url(FormattingOption... options)    {
        return url(new FormattingOptions(options));
    }

    /**
     * <p>See <a href="@docRoot/qurl.html#toEncoded">QUrl::toEncoded(QUrl::FormattingOptions)const</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QByteArray toEncoded(FormattingOption... options)    {
        return toEncoded(new FormattingOptions(options));
    }
    
    /**
     * <p>See <a href="@docRoot/qurl.html#toStringList">QUrl::toStringList(QList&lt;QUrl&gt;,QUrl::FormattingOptions)</a></p>
     */
    public static QStringList toStringList(java.util.Collection<? extends io.qt.core.QUrl> uris, FormattingOption... options){
        return toStringList(uris, new FormattingOptions(options));
    }
    
    /**
     * <p>See <a href="@docRoot/qurl.html#adjusted">QUrl::adjusted(QUrl::FormattingOptions)const</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QUrl adjusted(io.qt.core.QUrl.FormattingOption... options){
        return adjusted(new FormattingOptions(options));
    }
    
    /**
     * @deprecated Use <code>new QUrl("qrc:path")</code> instead.
     */
    @Deprecated
    public static io.qt.core.QUrl fromClassPath(java.lang.String classpath){
        return new io.qt.core.QUrl("qrc:"+classpath);
    }
}// class

abstract class QAbstractItemModel___ extends QAbstractItemModel {

    /**
     * <p>See <a href="@docRoot/qabstractitemmodel.html#setData">QAbstractItemModel::setData(QModelIndex,QVariant,int)</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean setData(int row, int col, Object value) {
        return setData(this.index(row, col), value, io.qt.core.Qt.ItemDataRole.DisplayRole);
    }
    
    /**
     * <p>See <a href="@docRoot/qabstractitemmodel.html#setData">QAbstractItemModel::setData(QModelIndex,QVariant,int)</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean setData(int row, int col, Object value, int role) {
        return setData(this.index(row, col), value, role);
    }

    /**
     * <p>See <a href="@docRoot/qabstractitemmodel.html#data">QAbstractItemModel::data(QModelIndex,int)const</a></p>
     */
    @io.qt.QtUninvokable
    public final Object data(int row, int col) {
        return data(this.index(row, col), Qt.ItemDataRole.DisplayRole);
    }
    
    /**
     * <p>See <a href="@docRoot/qabstractitemmodel.html#data">QAbstractItemModel::data(QModelIndex,int)</a></p>
     */
    @io.qt.QtUninvokable
    public final Object data(int row, int col, int role) {
        return data(this.index(row, col), role);
    }

    /**
     * <p>See <a href="@docRoot/qabstractitemmodel.html#createIndex-1">QAbstractItemModel::createIndex(int,int,quintptr)</a></p>
     */
    @io.qt.QtUninvokable
    protected final io.qt.core.QModelIndex createIndex(int row, int column){
        return createIndex(row, column, 0);
    }
}// class

class QTimer___ extends QTimer {
    
    private static class QSingleShotTimer extends QObject {
        private int timerId;
        public final Signal0 timeout = new Signal0();
        private final QObject receiver;
        private final QMetaObject.Slot0 slot;

        private QSingleShotTimer(int msec, Qt.TimerType timeType, QObject obj, String method) {
            super(QAbstractEventDispatcher.instance());
            this.slot = null;
            this.receiver = null;
            if(QAbstractEventDispatcher.instance()==null){
                dispose();
                return;
            }
            QtJambi_LibraryUtilities.internal.setCppOwnership(this);
            timeout.connect(obj, method);
            timerId = startTimer(msec, timeType);
        }

        private QSingleShotTimer(int msec, Qt.TimerType timeType, QObject obj, QMetaObject.Slot0 slot) {
            super(QAbstractEventDispatcher.instance());
            QMetaObject.Connection connection = timeout.connect(slot);
            if(!connection.isConnected() || (obj!=null && connection.receiver()!=obj)) {
                this.slot = slot;
                this.receiver = obj;
            }else {
                this.slot = null;
                this.receiver = null;
            }
            if(obj!=null && thread()!=obj.thread()) {
                QCoreApplication.instance().aboutToQuit.connect(this::disposeLater);
                setParent(null);
                this.moveToThread(obj.thread());
            }else if(QAbstractEventDispatcher.instance()==null){
                dispose();
                return;
            }
            QtJambi_LibraryUtilities.internal.setCppOwnership(this);
            timerId = startTimer(msec, timeType);
        }
    
        protected void timerEvent(QTimerEvent e) {
            if (timerId > 0)
                killTimer(timerId);
            timerId = -1;
            if(slot!=null) {
                if(receiver==null || !receiver.isDisposed()) {
                    try {
                        slot.invoke();
                    } catch (Throwable e1) {
                    }
                }
            }else {
                timeout.emit();
            }
            disposeLater();
        }
    }

    /**
     * This static function calls a slot after a given time interval.
     * 
     * It is very convenient to use this function because you do not need
     * to bother with a timerEvent or create a local QTimer object.
     * 
     * The receiver is the receiving object and the member is the slot. The
     * time interval is msec milliseconds.
     * 
     * @see #start()
     * @see <a href="@docRoot/qtimer.html#singleShot">QTimer::singleShot(int,const QObject*,const char *)</a>
     */
     public static void singleShot(int msec, QObject obj, String method) {
        singleShot(msec, defaultTypeFor(msec), obj, method);
    }

    /**
     * This static function calls slot after a given time interval.
     * 
     * It is very convenient to use this function because you do not need
     * to bother with a timerEvent or create a local QTimer object.
     * 
     * The time interval is msec milliseconds.
     * 
     * If context is destroyed before the interval occurs, the method will not be called.
     * The function will be run in the thread of context. The context's thread must have
     * a running Qt event loop.
     * 
     * @see #start()
     * @since 5.4
     * @see <a href="@docRoot/qtimer.html#singleShot-6">QTimer::singleShot(int,const QObject*,Functor)</a>
     */    
    public static void singleShot(int msec, QObject context, QMetaObject.Slot0 slot) {
        singleShot(msec, defaultTypeFor(msec), context, slot);
    }

    /**
     * This static function calls a slot after a given time interval.
     * 
     * It is very convenient to use this function because you do not need
     * to bother with a timerEvent or create a local QTimer object.
     * 
     * The receiver is the receiving object and the member is the slot. The
     * time interval is msec milliseconds. The timerType affects the
     * accuracy of the timer.
     * 
     * @see #start()
     * @see <a href="@docRoot/qtimer.html#singleShot-1">QTimer::singleShot(int,Qt::TimerType,const QObject*,const char *)</a>
     */
    public static void singleShot(int msec, Qt.TimerType timeType, QObject obj, String method) {
        new QSingleShotTimer(msec, timeType, obj, method);
    }

    /**
     * This static function calls slot after a given time interval.
     * 
     * It is very convenient to use this function because you do not need
     * to bother with a timerEvent or
     * create a local QTimer object.
     * 
     * The time interval is msec milliseconds.
     * 
     * @see #start()
     * @since 5.4
     * @see <a href="@docRoot/qtimer.html#singleShot-2">QTimer::singleShot(int,Qt::TimerType,PointerToMemberFunction)</a>
     */
    public static void singleShot(int msec, QMetaObject.Slot0 slot) {
        singleShot(msec, defaultTypeFor(msec), null, slot);
    }

    /**
     * This static function calls slot after a given time interval.
     * 
     * It is very convenient to use this function because you do not need
     * to bother with a timerEvent or
     * create a local QTimer object.
     * 
     * The time interval is msec milliseconds. The timerType affects the
     * accuracy of the timer.
     * 
     * @see #start()
     * @since 5.4
     * @see <a href="@docRoot/qtimer.html#singleShot-3">QTimer::singleShot(int,Qt::TimerType,PointerToMemberFunction)</a>
     */
    public static void singleShot(int msec, Qt.TimerType timeType, QMetaObject.Slot0 slot) {
        singleShot(msec, timeType, null, slot);
    }

    /**
     * This static function calls slot after a given time interval.
     * 
     * It is very convenient to use this function because you do not need
     * to bother with a timerEvent or
     * create a local QTimer object.
     * 
     * The time interval is msec milliseconds. The timerType affects the
     * accuracy of the timer.
     * 
     * If context is destroyed before the interval occurs, the method will not be called.
     * The function will be run in the thread of context. The context's thread must have
     * a running Qt event loop.
     * 
     * @see #start()
     * @since 5.4
     * @see <a href="@docRoot/qtimer.html#singleShot-7">QTimer::singleShot(int,Qt::TimerType,const QObject*,Functor)</a>
     */
    public static void singleShot(int msec, Qt.TimerType timeType, QObject context, QMetaObject.Slot0 slot) {
        new QSingleShotTimer(msec, timeType, context, slot);
    }
    
    private static Qt.TimerType defaultTypeFor(int msecs){ 
        return msecs >= 2000 ? Qt.TimerType.CoarseTimer : Qt.TimerType.PreciseTimer; 
    }

    /**
     * <p>See <a href="@docRoot/qtimer.html#callOnTimeout">QObject::callOnTimeout(Args&amp&;amp;...)</a></p>
     */
    public final QMetaObject.Connection callOnTimeout(QMetaObject.Slot0 slot, Qt.ConnectionType... type){
        return timeout.connect(slot, type);
    }

    /**
     * <p>See <a href="@docRoot/qtimer.html#callOnTimeout">QObject::callOnTimeout(Args&amp&;amp;...)</a></p>
     */
    public final QMetaObject.Connection callOnTimeout(QMetaObject.Connectable0 connectable, Qt.ConnectionType... type){
        return timeout.connect(connectable, type);
    }

    /**
     * <p>See <a href="@docRoot/qtimer.html#callOnTimeout">QObject::callOnTimeout(Args&amp&;amp;...)</a></p>
     */
    public final QMetaObject.Connection callOnTimeout(QObject receiver, String method, Qt.ConnectionType... type){
        return timeout.connect(receiver, method, type);
    }
    
}// class

class QCoreApplication___ extends QCoreApplication {
    
    private static boolean __qt_isInitializing = false;

    @io.qt.QtUninvokable
    private static native void preinit();

    /**
     * <p>Overloaded function for {@link #sendPostedEvents(io.qt.core.QObject, int)}</p>
     */
    public static void sendPostedEvents(io.qt.core.QObject receiver, QEvent.Type event_type){
        sendPostedEvents(receiver, event_type.value());
    }

    /**
     * Initializes the QCoreApplication with the given arguments.
     * @param args
     * @return QCoreApplication instance
     */
    @io.qt.QtUninvokable
    public static QCoreApplication initialize(String args[]) {
        return initialize(null, args, QCoreApplication::new);
    }

    /**
     * Initializes the QCoreApplication with the given arguments and applicationName.
     * @param applicationName
     * @param args
     * @return QCoreApplication instance
     */
    @io.qt.QtUninvokable
    public static QCoreApplication initialize(String applicationName, String args[]) {
        return initialize(applicationName, args, QCoreApplication::new);
    }

    /**
     * Initializes a custom QCoreApplication subclass with the given arguments.
     * @param <T> QCoreApplication subclass
     * @param args
     * @param constructor
     * @return T instance
     */
    @io.qt.QtUninvokable
    public static <T extends QCoreApplication> T initialize(String args[], java.util.function.Function<String[],T> constructor) {
        return initialize(null, args, constructor);
    }
    
    /**
     * Initializes a custom QCoreApplication subclass with the given arguments and applicationName.
     * @param <T> QCoreApplication subclass
     * @param applicationName
     * @param args
     * @param constructor
     * @return T instance
     */
    @io.qt.QtUninvokable
    public static <T extends QCoreApplication> T initialize(String applicationName, String args[], java.util.function.Function<String[],T> constructor) {
        if (instance() != null)
            throw new RuntimeException("QCoreApplication can only be initialized once");
        preinit();
        String[] _args = new String[args.length+1];
        if(applicationName!=null && !applicationName.isEmpty()){
            _args[0] = applicationName;
            setApplicationName(applicationName);
        }else{
            _args[0] = applicationName();
        }
        System.arraycopy(args, 0, _args, 1, args.length);
        args = _args;
        __qt_isInitializing = true;
        try {
            T application = java.util.Objects.requireNonNull(constructor.apply(args), "Constructor function does not instantiate QCoreApplication.");
            QtJambi_LibraryUtilities.internal.setCppOwnership(application);
            String bootPath = System.getProperty("sun.boot.library.path", "");
            if(!bootPath.isEmpty()) {
                removeLibraryPath(QDir.fromNativeSeparators(bootPath));
            }
            return application;
        } catch (Error e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException("Cannot initialize QCoreApplication", e);
        }finally{
            __qt_isInitializing = false;
        }
    }

    /**
     * Destroys the QCoreApplication instance and purges Qt.
     * @see <a href="@docRoot/qcoreapplication.html#dtor.QCoreApplication">~QCoreApplication()</a>
     */
    @io.qt.QtUninvokable
    public static void shutdown() {
        QCoreApplication app = instance();
        if(app != null) {
            QThread currentThread = QThread.currentThread();
            if(currentThread!=app.thread())
                throw new io.qt.QThreadAffinityException("Must not call QCoreApplication.shutdown() from outside main thread.", null, app.thread(), currentThread);
            if(app.thread().loopLevel()>0)
                throw new IllegalStateException("Must not call QCoreApplication.shutdown() in event loop.");
            System.gc();
            QCoreApplication.sendPostedEvents(null, QEvent.Type.DeferredDispose.value());    // allow deleteLater() to work some magic
            processEvents();    // process quit
            QCoreApplication.sendPostedEvents(null, QEvent.Type.DeferredDispose.value());    // allow deleteLater() to work some magic
            if(app instanceof io.qt.gui.QGuiApplication) {
                boolean deleted = false;
                if(app instanceof io.qt.widgets.QApplication) {
                    for(io.qt.widgets.QWidget widget : new java.util.ArrayList<>(io.qt.widgets.QApplication.topLevelWidgets())) {
                        if(widget!=null && !widget.isDisposed() && QtJambi_LibraryUtilities.internal.isJavaOwnership(widget)) {
                            widget.dispose();
                            deleted = true;
                        }
                    }
                    // second chance
                    for(io.qt.widgets.QWidget widget : new java.util.ArrayList<>(io.qt.widgets.QApplication.topLevelWidgets())) {
                        if(widget!=null && !widget.isDisposed()) {
                            if(!QtJambi_LibraryUtilities.internal.isJavaOwnership(widget))
                                QLogging.qWarning("QCoreApplication.shutdown() called before closing all widgets. Deleting widget %1$s [\"%2$s\"] (%3$s).", widget.objectName(), widget.windowTitle(), widget.metaObject().className());
                            widget.dispose();
                            deleted = true;
                        }
                    }
                }
                
                for(io.qt.gui.QWindow window : new java.util.ArrayList<>(io.qt.gui.QGuiApplication.topLevelWindows())) {
                    if(window!=null && !window.isDisposed() && QtJambi_LibraryUtilities.internal.isJavaOwnership(window)) {
                        if(currentThread==window.thread())
                            window.dispose();
                        else
                            window.disposeLater();
                        deleted = true;
                    }
                }
                // second chance
                for(io.qt.gui.QWindow window : new java.util.ArrayList<>(io.qt.gui.QGuiApplication.topLevelWindows())) {
                    if(window!=null && !window.isDisposed()) {
                        if(!QtJambi_LibraryUtilities.internal.isJavaOwnership(window))
                            QLogging.qWarning("QCoreApplication.shutdown() called before closing all windows. Deleting window %1$s [\"%2$s\"] (%3$s).", window.objectName(), window.title(), window.metaObject().className());
                        if(currentThread==window.thread())
                            window.dispose();
                        else
                            window.disposeLater();
                        deleted = true;
                    }
                }
                if(deleted){
                    System.gc();
                    QCoreApplication.sendPostedEvents(null, QEvent.Type.DeferredDispose.value());    // allow deleteLater() to work some magic
                    processEvents();    // process quit
                    QCoreApplication.sendPostedEvents(null, QEvent.Type.DeferredDispose.value());    // allow deleteLater() to work some magic
                }
            }
            app.dispose();
            app = null;        // discard hard-reference
            System.gc();
            QCoreApplication.sendPostedEvents(null, QEvent.Type.DeferredDispose.value());    // allow deleteLater() to work some magic
            processEvents();    // process quit
            QCoreApplication.sendPostedEvents(null, QEvent.Type.DeferredDispose.value());    // allow deleteLater() to work some magic
        }
        if (instance() != null)
            QLogging.qWarning("Failed to delete QCoreApplication instance.");
        System.gc();
        QCoreApplication.sendPostedEvents(null, QEvent.Type.DeferredDispose.value());
    }
    
    /**
     * Adds a pre-routine to be executed before initializing QCoreApplication.
     * @see initialize(String[])
     */
    @io.qt.QtUninvokable
    public static void addPreRoutine(Runnable startUpFunction) {
        preRoutines.add(startUpFunction);
    }

    /**
     * Adds a post-routine to be executed when deleting QCoreApplication.
     * @see <a href="@docRoot/qcoreapplication.html#qAddPostRoutine">qAddPostRoutine(QtCleanUpFunction)</a>
     * @see shutdown()
     */
    @io.qt.QtUninvokable
    public static void addPostRoutine(Runnable cleanUpFunction) {
        postRoutines.add(cleanUpFunction);
    }

    /**
     * Removes a previously added post-routine.
     * @see <a href="@docRoot/qcoreapplication.html#qRemovePostRoutine">qRemovePostRoutine(QtCleanUpFunction)</a>
     * @see addPostRoutine(Runnable)
     * @see shutdown()
     */
    @io.qt.QtUninvokable
    public static void removePostRoutine(Runnable cleanUpFunction){
        postRoutines.remove(cleanUpFunction);
    }

    private static final java.util.List<Runnable> preRoutines = java.util.Collections.synchronizedList(new java.util.ArrayList<>());

    private static final java.util.List<Runnable> postRoutines = java.util.Collections.synchronizedList(new java.util.ArrayList<>());

    @io.qt.NativeAccess
    @io.qt.QtUninvokable
    private static void execPreRoutines() {
        for(Runnable preRoutine : new java.util.ArrayList<>(preRoutines)) {
            try {
                preRoutine.run();
            }catch(Throwable t) {
                java.util.logging.Logger.getLogger("io.qt.core").throwing("Runnable", "run", t);
            }
        }
        preRoutines.clear();
    }

    @io.qt.NativeAccess
    @io.qt.QtUninvokable
    private static void execPostRoutines() {
        for(Runnable postRoutine : new java.util.ArrayList<>(postRoutines)) {
            try {
                postRoutine.run();
            }catch(Throwable t) {
                java.util.logging.Logger.getLogger("io.qt.core").throwing("Runnable", "run", t);
            }
        }
        postRoutines.clear();
    }
}// class
    
class QCoreApplication__62_ {
    @io.qt.QtUninvokable
    public final <QNativeInterface extends io.qt.QtObjectInterface> QNativeInterface nativeInterface(Class<QNativeInterface> nativeInterfaceClass) {
        return resolveInterface(nativeInterfaceClass);
    }
}// class

class QCoreApplication__65_ {
    /**
     * <p>See <a href="https://doc.qt.io/qt/qcoreapplication.html#requestPermission">QCoreApplication::requestPermission&lt;Functor>(const QPermission, Functor functor)</a></p>
     */
    @io.qt.QtUninvokable
    public final void requestPermission(io.qt.core.QPermission.Request permission, QMetaObject.Slot0 functor){
        requestPermission(new QPermission(permission), functor);
    }

    /**
     * <p>See <a href="https://doc.qt.io/qt/qcoreapplication.html#requestPermission">QCoreApplication::requestPermission&lt;Functor>(const QPermission, Functor functor)</a></p>
     */
    @io.qt.QtUninvokable
    public final void requestPermission(io.qt.core.QPermission.Request permission, QMetaObject.Slot1<QPermission> functor){
        requestPermission(new QPermission(permission), functor);
    }

    /**
     * <p>See <a href="https://doc.qt.io/qt/qcoreapplication.html#requestPermission">QCoreApplication::requestPermission&lt;Functor>(const QPermission, Functor functor)</a></p>
     */
    @io.qt.QtUninvokable
    public final void requestPermission(io.qt.core.QPermission permission, QMetaObject.Slot0 functor){
        QObject context = QtJambi_LibraryUtilities.internal.lambdaContext(functor);
        requestPermissionSlot0(
                        QtJambi_LibraryUtilities.internal.nativeId(this),
                        QtJambi_LibraryUtilities.internal.checkedNativeId(permission),
                        context==null ? 0 : QtJambi_LibraryUtilities.internal.nativeId(context),
                                functor);
    }

    /**
     * <p>See <a href="https://doc.qt.io/qt/qcoreapplication.html#requestPermission">QCoreApplication::requestPermission&lt;Functor>(const QPermission, Functor functor)</a></p>
     */
    @io.qt.QtUninvokable
    public final void requestPermission(io.qt.core.QPermission permission, QMetaObject.Slot1<QPermission> functor){
        QObject context = QtJambi_LibraryUtilities.internal.lambdaContext(functor);
        requestPermissionSlot1(
                        QtJambi_LibraryUtilities.internal.nativeId(this),
                        QtJambi_LibraryUtilities.internal.checkedNativeId(permission),
                        context==null ? 0 : QtJambi_LibraryUtilities.internal.nativeId(context),
                                functor);
    }

    private native final void requestPermissionSlot1(long __this__nativeId, long permission, long context, QMetaObject.Slot1<QPermission> functor);

    private native final void requestPermissionSlot0(long __this__nativeId, long permission, long context, QMetaObject.Slot0 functor);

    /**
     * <p>See <a href="@docRoot/qcoreapplication.html#checkPermission">QCoreApplication::checkPermission(QPermission)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.Qt.PermissionStatus checkPermission(io.qt.core.QPermission.Request permission){
        return checkPermission(new QPermission(permission));
    }
}// class

class QPermission___{

    /**
     * interface type for permission requests
     */
    public interface Request extends io.qt.QtObjectInterface{
    }

    /**
     * <p>See <a href="@docRoot/qpermission.html#QPermission">QPermission::QPermission&lt;T>(T)</a></p>
     */
    public QPermission(Request request){
        super((QPrivateConstructor)null);
        initialize_native(this);
        setData(QtJambi_LibraryUtilities.internal.nativeId(this), request);
    }

    private static native void setData(long nativeId, Request request);

    /**
     * <p>See <a href="@docRoot/qpermission.html#QPermission">QPermission::data&lt;T>()</a></p>
     */
    public final Request data() {
        return data(QtJambi_LibraryUtilities.internal.nativeId(this));
    }

    private static native Request data(long nativeId);

}// class

class QTranslator___ extends QTranslator {
}// class

class QItemSelection___ extends QItemSelection {
    
    public QItemSelection(java.util.Collection<? extends QItemSelectionRange> other) {
        super((QPrivateConstructor)null);
        initialize_native(this, other);
    }
    
    private native static <T> void initialize_native(QItemSelection instance, java.util.Collection<? extends QItemSelectionRange> other);
}// class

class QXmlStreamAttributes___ extends QXmlStreamAttributes {
    
    public QXmlStreamAttributes(java.util.Collection<io.qt.core.QXmlStreamAttribute> collection){
        this();
        append(collection);
    }
}// class

class autoclosedelete {
    /**
     * {@inheritDoc}
     */
    @Override
    public void close(){
        dispose();
    }
}// class

class QDebug___ extends QDebug {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public QDebug append(CharSequence csq, int start, int end) {
        return append(csq==null ? "null" : csq.subSequence(start, end));
    }

    /**
     * <p>See <a href="@docRoot/qt.html#endl">Qt::endl</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug endl(){
        endl(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void endl(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#flush">Qt::flush</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug flush(){
        flush(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void flush(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#reset">Qt::reset</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug reset(){
        reset(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void reset(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#bin">Qt::bin</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug bin(){
        bin(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void bin(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#oct">Qt::oct</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug oct(){
        oct(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void oct(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#hex">Qt::hex</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug hex(){
        hex(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void hex(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#dec">Qt::dec</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug dec(){
        dec(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void dec(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#showbase">Qt::showbase</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug showbase(){
        showbase(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void showbase(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#forcesign">Qt::forcesign</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug forcesign(){
        forcesign(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void forcesign(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#forcepoint">Qt::forcepoint</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug forcepoint(){
        forcepoint(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void forcepoint(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#noshowbase">Qt::noshowbase</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug noshowbase(){
        noshowbase(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void noshowbase(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#noforcesign">Qt::noforcesign</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug noforcesign(){
        noforcesign(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void noforcesign(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#noforcepoint">Qt::noforcepoint</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug noforcepoint(){
        noforcepoint(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void noforcepoint(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#uppercasebase">Qt::uppercasebase</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug uppercasebase(){
        uppercasebase(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void uppercasebase(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#uppercasedigits">Qt::uppercasedigits</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug uppercasedigits(){
        uppercasedigits(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void uppercasedigits(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#lowercasebase">Qt::lowercasebase</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug lowercasebase(){
        lowercasebase(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void lowercasebase(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#lowercasedigits">Qt::lowercasedigits</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug lowercasedigits(){
        lowercasedigits(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void lowercasedigits(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#fixed">Qt::fixed</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug fixed(){
        fixed(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void fixed(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#scientific">Qt::scientific</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug scientific(){
        scientific(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void scientific(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#left">Qt::left</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug left(){
        left(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void left(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#right">Qt::right</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug right(){
        right(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void right(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#center">Qt::center</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug center(){
        center(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void center(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#bom">Qt::bom</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug bom(){
        bom(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void bom(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#ws">Qt::ws</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug ws(){
        ws(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void ws(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#qSetFieldWidth">qSetFieldWidth(int width)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug qSetFieldWidth(int width){
        setFieldWidth(QtJambi_LibraryUtilities.internal.nativeId(this), width);
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void setFieldWidth(long __this__nativeId, int width);
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#qSetRealNumberPrecision">qSetRealNumberPrecision(int precision)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug qSetRealNumberPrecision(int precision){
        setRealNumberPrecision(QtJambi_LibraryUtilities.internal.nativeId(this), precision);
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void setRealNumberPrecision(long __this__nativeId, int precision);
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#qSetPadChar">qSetPadChar(QChar ch)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug qSetPadChar(char ch){
        setPadChar(QtJambi_LibraryUtilities.internal.nativeId(this), ch);
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void setPadChar(long __this__nativeId, char ch);
    
    /**
     * <p>See <a href="@docRoot/qdebug.html#QDebug-1">QDebug::QDebug(QString*)</a></p>
     */
    @SuppressWarnings("hiding")
    public <String extends Object & Appendable & CharSequence> QDebug(String string){
        this(new QTextStream.StringDevice<String>(java.util.Objects.requireNonNull(string, "Argument 'string': null not expected."), io.qt.core.QIODevice.OpenModeFlag.WriteOnly.asFlags()));
    }
    
    @io.qt.QtUninvokable
    public final io.qt.core.QDebug append(java.lang.Object obj){
        QMetaType metaType = QList.getMetaType(obj);
        debugStream(QtJambi_LibraryUtilities.internal.nativeId(this), QtJambi_LibraryUtilities.internal.checkedNativeId(metaType), obj);
        return this;
    }
    
    @io.qt.QtUninvokable
    private native static void debugStream(long debug, long metaType, Object value);
}// class

class QTextStream___ extends QTextStream {
    /**
     * <p>See <a href="@docRoot/qtextstream.html#string">QTextStream::string() const</a></p>
     */
    public final CharSequence string() {
        QIODevice device = device_private();
        if(device instanceof StringDevice) {
            flush();
            return ((StringDevice<?>) device).string;
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public QTextStream append(CharSequence csq, int start, int end) {
        return append(csq==null ? "null" : csq.subSequence(start, end));
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#device">QTextStream::device()const</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QIODevice device(){
        io.qt.core.QIODevice __qt_return_value = device_private();
        if(__qt_return_value instanceof StringDevice)
            return null;
        return __qt_return_value;
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#setString">QTextStream::setString(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    @SuppressWarnings("hiding")
    public <String extends Object & Appendable & CharSequence> void setString(String string, io.qt.core.QIODevice.OpenModeFlag ... openMode) {
        setString(string, new io.qt.core.QIODevice.OpenMode(openMode));
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#setString">QTextStream::setString(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    @SuppressWarnings("hiding")
    public <String extends Object & Appendable & CharSequence> void setString(String string) {
        setString(string, new io.qt.core.QIODevice.OpenMode(4));
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#setString">QTextStream::setString(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    @SuppressWarnings("hiding")
    public <String extends Object & Appendable & CharSequence> void setString(String string, io.qt.core.QIODevice.OpenMode openMode) {
        flush();
        setDevice(new StringDevice<String>(java.util.Objects.requireNonNull(string, "Argument 'string': null not expected."), openMode));
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#setString">QTextStream::setString(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    public void setString(String string, io.qt.core.QIODevice.OpenModeFlag ... openMode) {
        setString(string, new io.qt.core.QIODevice.OpenMode(openMode));
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#setString">QTextStream::setString(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    public void setString(String string) {
        setString(string, new io.qt.core.QIODevice.OpenMode(4));
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#setString">QTextStream::setString(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    public void setString(String string, io.qt.core.QIODevice.OpenMode openMode) {
        setString(string==null ? new StringBuilder() : new StringBuilder(string), openMode);
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#QTextStream-3">QTextStream::QTextStream(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    @SuppressWarnings("hiding")
    public <String extends Object & Appendable & CharSequence> QTextStream(String string, io.qt.core.QIODevice.OpenModeFlag ... openMode) {
        this(string, new io.qt.core.QIODevice.OpenMode(openMode));
    }
    /**
     * <p>See <a href="@docRoot/qtextstream.html#QTextStream-3">QTextStream::QTextStream(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    @SuppressWarnings("hiding")
    public <String extends Object & Appendable & CharSequence> QTextStream(String string) {
        this(string, new io.qt.core.QIODevice.OpenMode(3));
    }
    /**
     * <p>See <a href="@docRoot/qtextstream.html#QTextStream-3">QTextStream::QTextStream(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    @SuppressWarnings("hiding")
    public <String extends Object & Appendable & CharSequence> QTextStream(String string, io.qt.core.QIODevice.OpenMode openMode){
        this(new StringDevice<String>(java.util.Objects.requireNonNull(string, "Argument 'string': null not expected."), openMode));
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#QTextStream-3">QTextStream::QTextStream(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    public QTextStream(String string, io.qt.core.QIODevice.OpenModeFlag ... openMode) {
        this(string, new io.qt.core.QIODevice.OpenMode(openMode));
    }
    /**
     * <p>See <a href="@docRoot/qtextstream.html#QTextStream-3">QTextStream::QTextStream(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    public QTextStream(String string) {
        this(string, new io.qt.core.QIODevice.OpenMode(3));
    }
    /**
     * <p>See <a href="@docRoot/qtextstream.html#QTextStream-3">QTextStream::QTextStream(QString *, QIODeviceBase::OpenMode)</a></p>
     */
    public QTextStream(String string, io.qt.core.QIODevice.OpenMode openMode){
        this(string==null ? new StringBuilder() : new StringBuilder(string), openMode);
    }
    
    @SuppressWarnings("hiding")
    static final class StringDevice<String extends Object & Appendable & CharSequence> extends QIODevice{
        StringDevice(String string, io.qt.core.QIODevice.OpenModeFlag... openMode) {
            this(string, io.qt.core.QIODevice.OpenModeFlag.flags(openMode));
        }
        
        StringDevice(String string, io.qt.core.QIODevice.OpenMode openMode) {
            super();
            this.string = string;
            openMode.set(QIODevice.OpenModeFlag.Text);
            this.setOpenMode(openMode);
        }

        private final String string;
        private int cursor;

        @Override
        protected int readData(byte[] data) {
            int i = -1;
            if(data!=null && openMode().testFlag(QIODevice.OpenModeFlag.ReadOnly) && data.length>0){
                i = 0;
                while(i < data.length && cursor<string.length()) {
                    char c = string.charAt(cursor++);
                    byte[] _data = java.lang.String.valueOf(c).getBytes();
                    if(_data.length < i+data.length) {
                        System.arraycopy(_data, 0, data, i, _data.length);
                        ++i;
                    }else {
                        --cursor;
                        break;
                    }
                }
            }
            return i;
        }

        @Override
        protected int writeData(byte[] data) {
            if(data!=null && openMode().testFlag(QIODevice.OpenModeFlag.WriteOnly) && data.length>0){
                java.lang.String strg = new java.lang.String(data);
                try {
                    string.append(strg);
                    cursor = string.length();
                    return data.length;
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }
            }else return -1;
        }

        @Override
        public boolean isSequential() {
            return this.openMode().testFlag(QIODevice.OpenModeFlag.WriteOnly);
        }

        @Override
        public boolean seek(long pos) {
            if(pos==0) {
                setOpenMode(QIODevice.OpenModeFlag.ReadOnly, QIODevice.OpenModeFlag.Text);
                cursor = 0;
            }
            return super.seek(pos);
        }
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#operator-lt-lt-1">QTextStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QTextStream append(java.lang.String s){
        return writeString(s);
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#operator-lt-lt-1">QTextStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QTextStream append(java.lang.CharSequence s){
        return writeString(s);
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#operator-lt-lt-1">QTextStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QTextStream append(double v){
        return writeDouble(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#operator-lt-lt-1">QTextStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QTextStream append(byte v){
        return writeByte(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#operator-lt-lt-1">QTextStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QTextStream append(io.qt.core.QByteArray v){
        return writeBytes(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#operator-lt-lt-1">QTextStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QTextStream append(char v){
        return writeChar(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#operator-lt-lt-1">QTextStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QTextStream append(float v){
        return writeFloat(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#operator-lt-lt-1">QTextStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QTextStream append(int v){
        return writeInt(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#operator-lt-lt-1">QTextStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QTextStream append(long v){
        return writeLong(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#operator-lt-lt-1">QTextStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QTextStream append(short v){
        return writeShort(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qt.html#endl">Qt::endl</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream endl(){
        endl(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void endl(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#flush">Qt::flush</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream flush(){
        flush(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void flush(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#reset">Qt::reset</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream reset(){
        reset(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void reset(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#bin">Qt::bin</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream bin(){
        bin(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void bin(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#oct">Qt::oct</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream oct(){
        oct(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void oct(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#hex">Qt::hex</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream hex(){
        hex(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void hex(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#dec">Qt::dec</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream dec(){
        dec(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void dec(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#showbase">Qt::showbase</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream showbase(){
        showbase(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void showbase(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#forcesign">Qt::forcesign</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream forcesign(){
        forcesign(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void forcesign(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#forcepoint">Qt::forcepoint</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream forcepoint(){
        forcepoint(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void forcepoint(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#noshowbase">Qt::noshowbase</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream noshowbase(){
        noshowbase(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void noshowbase(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#noforcesign">Qt::noforcesign</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream noforcesign(){
        noforcesign(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void noforcesign(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#noforcepoint">Qt::noforcepoint</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream noforcepoint(){
        noforcepoint(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void noforcepoint(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#uppercasebase">Qt::uppercasebase</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream uppercasebase(){
        uppercasebase(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void uppercasebase(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#uppercasedigits">Qt::uppercasedigits</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream uppercasedigits(){
        uppercasedigits(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void uppercasedigits(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#lowercasebase">Qt::lowercasebase</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream lowercasebase(){
        lowercasebase(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void lowercasebase(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#lowercasedigits">Qt::lowercasedigits</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream lowercasedigits(){
        lowercasedigits(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void lowercasedigits(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#fixed">Qt::fixed</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream fixed(){
        fixed(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void fixed(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#scientific">Qt::scientific</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream scientific(){
        scientific(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void scientific(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#left">Qt::left</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream left(){
        left(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void left(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#right">Qt::right</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream right(){
        right(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void right(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#center">Qt::center</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream center(){
        center(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void center(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#bom">Qt::bom</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream bom(){
        bom(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void bom(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qt.html#ws">Qt::ws</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream ws(){
        ws(QtJambi_LibraryUtilities.internal.nativeId(this));
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void ws(long __this__nativeId);
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#qSetFieldWidth">qSetFieldWidth(int width)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream qSetFieldWidth(int width){
        setFieldWidth(QtJambi_LibraryUtilities.internal.nativeId(this), width);
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void setFieldWidth(long __this__nativeId, int width);
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#qSetRealNumberPrecision">qSetRealNumberPrecision(int precision)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream qSetRealNumberPrecision(int precision){
        setRealNumberPrecision(QtJambi_LibraryUtilities.internal.nativeId(this), precision);
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void setRealNumberPrecision(long __this__nativeId, int precision);
    
    /**
     * <p>See <a href="@docRoot/qtextstream.html#qSetPadChar">qSetPadChar(QChar ch)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QTextStream qSetPadChar(char ch){
        setPadChar(QtJambi_LibraryUtilities.internal.nativeId(this), ch);
        return this;
    }
    
    @io.qt.QtUninvokable
    private native void setPadChar(long __this__nativeId, char ch);
    
}// class

class QBitArray___ extends QBitArray {
    
    @io.qt.QtUninvokable
    public final boolean[] toBitArray() {
        long size = size();
        if(size>Integer.MAX_VALUE)
            throw new RuntimeException("Size of QBitArray exceeds max array size.");
        boolean[] res = new boolean[(int)size];

        for (int i = 0; i < size; i++) {
            res[i] = testBit(i);
        }
        return res;
    }
    
    public static io.qt.core.QBitArray fromBits(boolean[] data){
        io.qt.core.QBitArray array = new io.qt.core.QBitArray(data.length);
        for (int i = 0; i < data.length; i++) {
            if(data[i])
                array.setBit(i);
        }
        return array;
    }
    
    public static io.qt.core.QBitArray fromBits(byte[] data){
        return fromBits(java.nio.ByteBuffer.wrap(data), data.length*8);
    }
}// class

// hfr

class QDate___ extends QDate {
    
    /**
     * Result class for {@link QDate#weekNumber()}
     */
    public final static class Week{
        private Week(int weekNumber, int yearNumber){
            this.weekNumber = weekNumber;
            this.yearNumber = yearNumber;
        }
        public final int weekNumber;
        public final int yearNumber;
    }
}// class

class QByteArray_5_12__ extends QByteArray {

    /**
     * <p>Overloaded function for {@link #compare(byte[], io.qt.core.Qt.CaseSensitivity)}
     *  with <code>cs = io.qt.core.Qt.CaseSensitivity.CaseSensitive</code>.</p>
     */
    @io.qt.QtUninvokable
    public final int compare(byte[] b) {
        return compare(new QByteArray(b));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#compare">QByteArray::compare(const char*,Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(byte[] b, io.qt.core.Qt.CaseSensitivity cs) {
        return compare(new QByteArray(b), cs);
    }
    
    /**
     * <p>Overloaded function for {@link #compare(String, io.qt.core.Qt.CaseSensitivity)}
     *  with <code>cs = io.qt.core.Qt.CaseSensitivity.CaseSensitive</code>.</p>
     */
    @io.qt.QtUninvokable
    public final int compare(String str) {
        return compare(new QByteArray(str));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#compare">QByteArray::compare(const char*,Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(String str, io.qt.core.Qt.CaseSensitivity cs) {
        return compare(new QByteArray(str), cs);
    }
}// class

class QByteArray_5_15__ extends QByteArray {
    /**
     * Result class for {@link #fromBase64Encoding(io.qt.core.QByteArray, io.qt.core.QByteArray.Base64Options)}
     */
    public static class FromBase64Result{
        public final io.qt.core.QByteArray decoded;
        public final io.qt.core.QByteArray.Base64DecodingStatus decodingStatus;
        private FromBase64Result(QByteArray decoded, int decodingStatus) {
            super();
            this.decoded = decoded;
            this.decodingStatus = Base64DecodingStatus.resolve(decodingStatus);
        }
    }
}// class

class QByteArray___ extends QByteArray {
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#QByteArray">QByteArray(const char *)</a></p>
     */
    public QByteArray(byte data[]) {
        this(java.nio.ByteBuffer.wrap(data), data.length);
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#prepend">QByteArray::prepend(const char *)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray prepend(byte[] data) {
        return this.prepend(java.nio.ByteBuffer.wrap(data), data.length);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#append">QByteArray::append(const char *)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QByteArray append(byte[] a){
        return append(java.nio.ByteBuffer.wrap(a), a.length);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#operator-eq-1">QByteArray::operator=(const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QByteArray set(byte[] data){
        return set(java.nio.ByteBuffer.wrap(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#operator-eq-1">QByteArray::operator=(const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QByteArray set(String data){
        return set(new QByteArray(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearraylist.html#join">QByteArrayList::join()</a></p>
     */
    @io.qt.QtUninvokable
    public native static QByteArray join(java.util.Collection<QByteArray> stringList);
    
    /**
     * <p>See <a href="@docRoot/qbytearraylist.html#join">QByteArrayList::join(char)</a></p>
     */
    @io.qt.QtUninvokable
    public native static QByteArray join(java.util.Collection<QByteArray> stringList, byte sep);
    
    /**
     * <p>See <a href="@docRoot/qbytearraylist.html#join">QByteArrayList::join(QByteArray)</a></p>
     */
    @io.qt.QtUninvokable
    public native static QByteArray join(java.util.Collection<QByteArray> stringList, QByteArray sep);
    
}// class

class QByteArray_5__ extends QByteArray {
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#QByteArray-1">QByteArray::QByteArray(const char*)</a></p>
     */
    public QByteArray(String s) {
        this();
        append(s);
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#QByteArray-1">QByteArray::QByteArray(const char*,int)</a></p>
     */
    public QByteArray(byte data[], int size) {
        this(java.nio.ByteBuffer.wrap(data), size==-1 ? data.length : Math.min(size, data.length));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#prepend">QByteArray::prepend(const char *,int)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray prepend(byte[] data, int size) {
        return this.prepend(java.nio.ByteBuffer.wrap(data), size==-1 ? data.length : Math.min(size, data.length));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#append">QByteArray::append(const char *,int)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QByteArray append(byte[] a, int size){
        return append(java.nio.ByteBuffer.wrap(a), size==-1 ? a.length : Math.min(size, a.length));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#count-2">QByteArray::count(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int count(byte data[]) {
        return count(java.nio.ByteBuffer.wrap(data));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#contains-2">QByteArray::contains(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean contains(byte data[]) {
        return contains(java.nio.ByteBuffer.wrap(data));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#startsWith-2">QByteArray::startsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean startsWith(String str) {
        return startsWith(new QByteArray(str));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#contains-2">QByteArray::contains(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean contains(String str) {
        return contains(new QByteArray(str));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#contains-2">QByteArray::contains(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int count(String str) {
        return count(new QByteArray(str));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#endsWith-2">QByteArray::endsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean endsWith(String str) {
        return endsWith(new QByteArray(str));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#prepend-3">QByteArray::prepend(const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray prepend(String str) {
        return prepend(new QByteArray(str));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-1">QByteArray::replace(int,int,const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(int index, int len, byte[] data) {
        this.replace(index, len, java.nio.ByteBuffer.wrap(data), data.length);
        return this;
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#insert-3">QByteArray::insert(int,const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray insert(int i, byte[] data) {
        this.insert(i, java.nio.ByteBuffer.wrap(data), data.length);
        return this;
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-8">QByteArray::replace(QByteArray,const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(QByteArray before, String after) {
        return replace(before, new QByteArray(after));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-8">QByteArray::replace(const char*,QByteArray)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(byte[] before, QByteArray after) {
        return replace(new QByteArray(before), after);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-8">QByteArray::replace(QByteArray,const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(QByteArray before, byte[] after) {
        return replace(before, new QByteArray(after));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-5">QByteArray::replace(const char*,const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(String before, String after) {
        return replace(new QByteArray(before), new QByteArray(after));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-5">QByteArray::replace(const char*,const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(byte[] before, byte[] after) {
        return replace(new QByteArray(before), new QByteArray(after));
    }
}// class

class QByteArray_6__ extends QByteArray {

    private static byte[] getBytes(String s) {
        if(s==null)
            return new byte[0];
        try {
            return s.getBytes("UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return s.getBytes();
        }
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#QByteArray">QByteArray::QByteArray(const char*)</a></p>
     */
    public QByteArray(String s) {
        this(getBytes(s));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#QByteArray">QByteArray::QByteArray(const char*,qsizetype)</a></p>
     */
    public QByteArray(byte data[], long size) {
        this(java.nio.ByteBuffer.wrap(data), size==-1 ? data.length : Math.min(size, data.length));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#prepend">QByteArray::prepend(const char *,qsizetype)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray prepend(byte[] data, long size) {
        return this.prepend(java.nio.ByteBuffer.wrap(data), size==-1 ? data.length : Math.min(size, data.length));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#append">QByteArray::append(const char *,qsizetype)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QByteArray append(byte[] a, long size){
        return append(java.nio.ByteBuffer.wrap(a), size==-1 ? a.length : Math.min(size, a.length));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#contains">QByteArray::contains(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean contains(String str) {
        return contains(new QByteArrayView(str));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#count">QByteArray::count(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long count(String str) {
        return count(new QByteArrayView(str));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#endsWith">QByteArray::endsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean endsWith(String str) {
        return endsWith(new QByteArrayView(str));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#endsWith">QByteArray::endsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean endsWith(byte[] str) {
        return endsWith(new QByteArrayView(str));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#endsWith">QByteArray::endsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean endsWith(java.nio.ByteBuffer str) {
        return endsWith(new QByteArrayView(str));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#prepend">QByteArray::prepend(const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray prepend(String str) {
        return prepend(getBytes(str));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#startsWith">QByteArray::startsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean startsWith(String str) {
        return startsWith(new QByteArrayView(str));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#startsWith">QByteArray::startsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean startsWith(byte[] str) {
        return startsWith(new QByteArrayView(str));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#startsWith">QByteArray::startsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean startsWith(java.nio.ByteBuffer str) {
        return startsWith(new QByteArrayView(str));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#append">QByteArray::append(const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QByteArray append(String a){
        return append(getBytes(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#compare">QByteArray::compare(const char*, Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(String str) {
        return compare(str, io.qt.core.Qt.CaseSensitivity.CaseSensitive);
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#compare">QByteArray::compare(const char*, Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(String str, io.qt.core.Qt.CaseSensitivity cs) {
        return compare(new QByteArrayView(str), cs);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#count">QByteArray::count(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long count(byte data[]) {
        return count(new QByteArrayView(data));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#contains">QByteArray::contains(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean contains(byte data[]) {
        return contains(new QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#compare">QByteArray::compare(const char*, Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(byte[] b) {
        return compare(b, io.qt.core.Qt.CaseSensitivity.CaseSensitive);
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#compare">QByteArray::compare(const char*, Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(byte[] b, io.qt.core.Qt.CaseSensitivity cs) {
        return compare(new QByteArrayView(b), cs);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#count">QByteArray::count(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long count(java.nio.ByteBuffer data) {
        return count(new QByteArrayView(data));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#contains">QByteArray::contains(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean contains(java.nio.ByteBuffer data) {
        return contains(new QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#compare">QByteArray::compare(const char*, Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(java.nio.ByteBuffer b) {
        return compare(b, io.qt.core.Qt.CaseSensitivity.CaseSensitive);
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#compare">QByteArray::compare(const char*, Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(java.nio.ByteBuffer b, io.qt.core.Qt.CaseSensitivity cs) {
        return compare(new QByteArrayView(b), cs);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#indexOf">QByteArray::indexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(String strg) {
        return indexOf(strg, (int)0);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#indexOf">QByteArray::indexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(String strg, long from){
        return indexOf(new io.qt.core.QByteArrayView(strg), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#indexOf">QByteArray::indexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(byte[] other) {
        return indexOf(other, (int)0);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#indexOf">QByteArray::indexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(byte[] other, long from){
        return indexOf(new io.qt.core.QByteArrayView(other), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#indexOf">QByteArray::indexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(java.nio.ByteBuffer other) {
        return indexOf(other, (int)0);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#indexOf">QByteArray::indexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(java.nio.ByteBuffer other, long from){
        return indexOf(new io.qt.core.QByteArrayView(other), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#indexOf">QByteArray::indexOf(QByteArray)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(QByteArray other) {
        return indexOf(other, (long)0);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#indexOf">QByteArray::indexOf(QByteArray,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(QByteArray other, long from){
        return indexOf(new io.qt.core.QByteArrayView(other), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#lastIndexOf">QByteArray::lastIndexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(String strg) {
        return lastIndexOf(strg, (long)0);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#lastIndexOf">QByteArray::lastIndexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(String strg, long from){
        return lastIndexOf(new io.qt.core.QByteArrayView(strg), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#lastIndexOf">QByteArray::lastIndexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(byte[] other) {
        return lastIndexOf(other, (long)0);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#lastIndexOf">QByteArray::lastIndexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(byte[] other, long from){
        return lastIndexOf(new io.qt.core.QByteArrayView(other), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#lastIndexOf">QByteArray::lastIndexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(java.nio.ByteBuffer other) {
        return lastIndexOf(other, (long)0);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#lastIndexOf">QByteArray::lastIndexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(java.nio.ByteBuffer other, long from){
        return lastIndexOf(new io.qt.core.QByteArrayView(other), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#lastIndexOf">QByteArray::lastIndexOf(QByteArray)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(QByteArray other) {
        return lastIndexOf(other, (long)0);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#lastIndexOf">QByteArray::lastIndexOf(QByteArray,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(QByteArray other, long from){
        return lastIndexOf(new io.qt.core.QByteArrayView(other), from);
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace">QByteArray::replace(qsizetype,qsizetype,QByteArrayView)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(long index, long len, byte[] data) {
        this.replace(index, len, java.nio.ByteBuffer.wrap(data), data.length);
        return this;
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#insert">QByteArray::insert(qsizetype,QByteArrayView)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray insert(long i, byte[] data) {
        this.insert(i, java.nio.ByteBuffer.wrap(data), data.length);
        return this;
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-4">QByteArray::replace(QByteArrayView,QByteArrayView)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(QByteArray before, byte[] after) {
        return replace(new QByteArrayView(before), new QByteArrayView(after));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-4">QByteArray::replace(QByteArrayView,QByteArrayView)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(QByteArray before, String after) {
        return replace(new QByteArrayView(before), new QByteArrayView(after));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-4">QByteArray::replace(QByteArrayView,QByteArrayView)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(byte[] before, String after) {
        return replace(new QByteArrayView(before), new QByteArrayView(after));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-4">QByteArray::replace(QByteArrayView,QByteArrayView)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(byte[] before, QByteArray after) {
        return replace(new QByteArrayView(before), new QByteArrayView(after));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-4">QByteArray::replace(QByteArrayView,QByteArrayView)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(String before, QByteArray after) {
        return replace(new QByteArrayView(before), new QByteArrayView(after));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-4">QByteArray::replace(QByteArrayView,QByteArrayView)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(String before, byte[] after) {
        return replace(new QByteArrayView(before), new QByteArrayView(after));
    }

    /**
     * <p>See <a href="@docRoot/qbytearray.html#replace-4">QByteArray::replace(QByteArrayView,QByteArrayView)</a></p>
     */
    @io.qt.QtUninvokable
    public final QByteArray replace(String before, String after) {
        return replace(new QByteArrayView(before), new QByteArrayView(after));
    }
}// class

class QByteArray_63__ extends QByteArray {
    /**
     * <p>See <a href="@docRoot/qbytearraylist.html#join">QByteArrayList::join(QByteArrayView)</a></p>
     */
    @io.qt.QtUninvokable
    public native static QByteArray join(java.util.Collection<QByteArray> stringList, QByteArrayView sep);
}// class

class QByteArrayView___ extends QByteArray {
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#QByteArrayView">QByteArrayView::QByteArrayView(const char*)</a></p>
     */
    public QByteArrayView(byte[] data){
        super((QPrivateConstructor)null);
        Long[] pointerOut = {null};
        initialize_native(this, data, pointerOut);
        if(pointerOut[0]!=null) {
            long pointer = pointerOut[0];
            purgeTask = ()->purgeBytes(pointer);
            io.qt.QtUtilities.getSignalOnDispose(this).connect(purgeTask::run);                
        }
    }
    private native static void initialize_native(QByteArrayView instance, byte[] data, Long[] pointerOut);
    private native static void purgeBytes(long pointer);
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#QByteArrayView">QByteArrayView::QByteArrayView(const char*)</a></p>
     */
    public QByteArrayView(String data){
        super((QPrivateConstructor)null);
        Long[] pointerOut = {null};
        initialize_native(this, data, pointerOut);
        if(pointerOut[0]!=null) {
            long pointer = pointerOut[0];
            purgeTask = ()->purgeString(pointer);
            io.qt.QtUtilities.getSignalOnDispose(this).connect(purgeTask::run);                
        }
    }
    private native static void initialize_native(QByteArrayView instance, String data, Long[] pointerOut);
    private native static void purgeString(long pointer);
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#QByteArrayView">QByteArrayView::QByteArrayView(const char*)</a></p>
     */
    public QByteArrayView(java.nio.ByteBuffer data){
        super((QPrivateConstructor)null);
        if(data.isDirect()) {
            initialize_native(this, data);
            purgeTask = data::hashCode;
            io.qt.QtUtilities.getSignalOnDispose(this).connect(purgeTask::run);
        }else {
            Long[] pointerOut = {null};
            initialize_native(this, data, pointerOut);
            if(pointerOut[0]!=null) {
                long pointer = pointerOut[0];
                purgeTask = ()->purgeBuffer(pointer);
                io.qt.QtUtilities.getSignalOnDispose(this).connect(purgeTask::run);
            }
        }
    }
    private native static void initialize_native(QByteArrayView instance, java.nio.ByteBuffer data);
    private native static void initialize_native(QByteArrayView instance, java.nio.Buffer data, Long[] pointerOut);
    private native static void purgeBuffer(long pointer);
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#QByteArrayView">QByteArrayView::QByteArrayView(QByteArray)</a></p>
     */
    public QByteArrayView(QByteArray data){
        super((QPrivateConstructor)null);
        if(data!=null) {
            initialize_native(this, data);
            purgeTask = data::isDisposed;
            io.qt.QtUtilities.getSignalOnDispose(this).connect(purgeTask::run);
        }else {
            initialize_native(this);
        }
    }
    private native static void initialize_native(QByteArrayView instance, QByteArray data);
    
    private Runnable purgeTask;
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#compare">QByteArrayView::compare(QByteArray,Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(io.qt.core.QByteArray a){
        return compare(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#compare">QByteArrayView::compare(QByteArray,Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(io.qt.core.QByteArray a, io.qt.core.Qt.CaseSensitivity cs){
        return compare(new io.qt.core.QByteArrayView(a), cs);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#compare">QByteArrayView::compare(const char*,Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(byte[] a){
        return compare(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#compare">QByteArrayView::compare(const char*,Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(byte[] a, io.qt.core.Qt.CaseSensitivity cs){
        return compare(new io.qt.core.QByteArrayView(a), cs);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#compare">QByteArrayView::compare(const char*,Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(java.nio.ByteBuffer a){
        return compare(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#compare">QByteArrayView::compare(const char*,Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(java.nio.ByteBuffer a, io.qt.core.Qt.CaseSensitivity cs){
        return compare(new io.qt.core.QByteArrayView(a), cs);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#compare">QByteArrayView::compare(const char*,Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(String a){
        return compare(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#compare">QByteArrayView::compare(const char*,Qt::CaseSensitivity)const</a></p>
     */
    @io.qt.QtUninvokable
    public final int compare(String a, io.qt.core.Qt.CaseSensitivity cs){
        return compare(new io.qt.core.QByteArrayView(a), cs);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#contains">QByteArrayView::contains(QByteArray)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean contains(io.qt.core.QByteArray a){
        return contains(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#contains">QByteArrayView::contains(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean contains(byte[] a){
        return contains(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#contains">QByteArrayView::contains(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean contains(String a){
        return contains(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#contains">QByteArrayView::contains(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean contains(java.nio.ByteBuffer a){
        return contains(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#QtUninvokable">QByteArrayView::QtUninvokable(QByteArray)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean endsWith(io.qt.core.QByteArray a){
        return endsWith(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#endsWith">QByteArrayView::endsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean endsWith(byte[] a){
        return endsWith(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#endsWith">QByteArrayView::endsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean endsWith(String a){
        return endsWith(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#endsWith">QByteArrayView::endsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean endsWith(java.nio.ByteBuffer a){
        return endsWith(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#startsWith">QByteArrayView::startsWith(QByteArray)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean startsWith(io.qt.core.QByteArray a){
        return startsWith(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#startsWith">QByteArrayView::startsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean startsWith(byte[] a){
        return startsWith(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#startsWith">QByteArrayView::startsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean startsWith(String a){
        return startsWith(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#startsWith">QByteArrayView::startsWith(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean startsWith(java.nio.ByteBuffer a){
        return startsWith(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#count">QByteArrayView::count(QByteArray)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long count(io.qt.core.QByteArray a){
        return count(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#count">QByteArrayView::count(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long count(byte[] a){
        return count(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#count">QByteArrayView::count(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long count(String a){
        return count(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#count">QByteArrayView::count(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long count(java.nio.ByteBuffer a){
        return count(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#indexOf">QByteArrayView::indexOf(QByteArray)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(io.qt.core.QByteArray a){
        return indexOf(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#indexOf">QByteArrayView::indexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(byte[] a){
        return indexOf(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#indexOf">QByteArrayView::indexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(String a){
        return indexOf(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#indexOf">QByteArrayView::indexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(java.nio.ByteBuffer a){
        return indexOf(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#indexOf">QByteArrayView::indexOf(QByteArray,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(io.qt.core.QByteArray a, long from){
        return indexOf(new io.qt.core.QByteArrayView(a), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#indexOf">QByteArrayView::indexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(byte[] a, long from){
        return indexOf(new io.qt.core.QByteArrayView(a), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#indexOf">QByteArrayView::indexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(String a, long from){
        return indexOf(new io.qt.core.QByteArrayView(a), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#indexOf">QByteArrayView::indexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long indexOf(java.nio.ByteBuffer a, long from){
        return indexOf(new io.qt.core.QByteArrayView(a), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#lastIndexOf">QByteArrayView::lastIndexOf(QByteArray)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(io.qt.core.QByteArray a){
        return lastIndexOf(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#lastIndexOf">QByteArrayView::lastIndexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(byte[] a){
        return lastIndexOf(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#lastIndexOf">QByteArrayView::lastIndexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(String a){
        return lastIndexOf(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#lastIndexOf">QByteArrayView::lastIndexOf(const char*)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(java.nio.ByteBuffer a){
        return lastIndexOf(new io.qt.core.QByteArrayView(a));
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#lastIndexOf">QByteArrayView::lastIndexOf(QByteArray,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(io.qt.core.QByteArray a, long from){
        return lastIndexOf(new io.qt.core.QByteArrayView(a), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#lastIndexOf">QByteArrayView::lastIndexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(byte[] a, long from){
        return lastIndexOf(new io.qt.core.QByteArrayView(a), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#lastIndexOf">QByteArrayView::lastIndexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(String a, long from){
        return lastIndexOf(new io.qt.core.QByteArrayView(a), from);
    }
    
    /**
     * <p>See <a href="@docRoot/qbytearrayview.html#lastIndexOf">QByteArrayView::lastIndexOf(const char*,qsizetype)const</a></p>
     */
    @io.qt.QtUninvokable
    public final long lastIndexOf(java.nio.ByteBuffer a, long from){
        return lastIndexOf(new io.qt.core.QByteArrayView(a), from);
    }
}// class

class QStringConverter___{
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForData">QStringConverter::encodingForData(QByteArrayView,char16_t)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForHtml(io.qt.core.QByteArray data){
        return encodingForHtml(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForData">QStringConverter::encodingForData(QByteArrayView,char16_t)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForData(io.qt.core.QByteArray data){
        return encodingForData(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForData">QStringConverter::encodingForData(QByteArrayView,char16_t)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForData(io.qt.core.QByteArray data, char expectedFirstCharacter){
        return encodingForData(new io.qt.core.QByteArrayView(data), expectedFirstCharacter);
    }
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForHtml">QStringConverter::encodingForHtml(QByteArrayView)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForHtml(byte[] data){
        return encodingForHtml(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForData">QStringConverter::encodingForData(QByteArrayView,char16_t)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForData(byte[] data){
        return encodingForData(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForData">QStringConverter::encodingForData(QByteArrayView,char16_t)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForData(byte[] data, char expectedFirstCharacter){
        return encodingForData(new io.qt.core.QByteArrayView(data), expectedFirstCharacter);
    }
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForHtml">QStringConverter::encodingForHtml(QByteArrayView)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForHtml(String data){
        return encodingForHtml(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForData">QStringConverter::encodingForData(QByteArrayView,char16_t)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForData(String data){
        return encodingForData(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForData">QStringConverter::encodingForData(QByteArrayView,char16_t)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForData(String data, char expectedFirstCharacter){
        return encodingForData(new io.qt.core.QByteArrayView(data), expectedFirstCharacter);
    }
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForHtml">QStringConverter::encodingForHtml(QByteArrayView)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForHtml(java.nio.ByteBuffer data){
        return encodingForHtml(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForData">QStringConverter::encodingForData(QByteArrayView,char16_t)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForData(java.nio.ByteBuffer data){
        return encodingForData(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qstringconverter.html#encodingForData">QStringConverter::encodingForData(QByteArrayView,char16_t)</a></p>
     */
    public static java.util.Optional<io.qt.core.QStringConverter.Encoding> encodingForData(java.nio.ByteBuffer data, char expectedFirstCharacter){
        return encodingForData(new io.qt.core.QByteArrayView(data), expectedFirstCharacter);
    }
    
}// class

class QUuid_63__{
    
    /**
     * <p>See <a href="@docRoot/quuid.html#fromRfc4122">QUuid::fromRfc4122(QByteArrayView)</a></p>
     */
    public static io.qt.core.QUuid fromRfc4122(io.qt.core.QByteArray data){
        return fromRfc4122(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/quuid.html#fromRfc4122">QUuid::fromRfc4122(QByteArrayView)</a></p>
     */
    public static io.qt.core.QUuid fromRfc4122(byte[] data){
        return fromRfc4122(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/quuid.html#fromRfc4122">QUuid::fromRfc4122(QByteArrayView)</a></p>
     */
    public static io.qt.core.QUuid fromRfc4122(String data){
        return fromRfc4122(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/quuid.html#fromRfc4122">QUuid::fromRfc4122(QByteArrayView)</a></p>
     */
    public static io.qt.core.QUuid fromRfc4122(java.nio.ByteBuffer data){
        return fromRfc4122(new io.qt.core.QByteArrayView(data));
    }
    
}// class

class QIODevice_prefix__ extends QIODevice {
        /**
         * Creates QIODevice from direct buffer. The new I/O device is not open.
         * @param buffer
         * @return new new QIODevice or null if no direct buffer
         */
        public native static QIODevice fromDirectBuffer(java.nio.Buffer buffer);
        
        /**
         * Creates a new open QIODevice for reading from given InputStream
         * @param stream
         * @return new open read-only QIODevice
         */
        public static QIODevice fromInputStream(java.io.InputStream stream) {
            return new QIODevice() {
                {
                    setOpenMode(QIODevice.OpenModeFlag.ReadOnly);
                }
                
                @Override
                public boolean atEnd() {
                    return super.atEnd();
                }

                @Override
                public long bytesAvailable() {
                    try {
                        return stream.available();
                    } catch (java.io.IOException e) {
                        return 0;
                    }
                }

                @Override
                public long bytesToWrite() {
                    return 0;
                }

                @Override
                public boolean canReadLine() {
                    return false;
                }

                @Override
                public void close() {
                    try {
                        stream.close();
                    } catch (java.io.IOException e) {
                    }
                    super.close();
                }

                @Override
                public boolean isSequential() {
                    return true;
                }

                @Override
                public boolean open(OpenMode mode) {
                    if(mode.value()==OpenModeFlag.ReadOnly.value())
                        return super.open(mode);
                    else return false;
                }

                @Override
                public long pos() {
                    return super.pos();
                }

                @Override
                protected int readLineData(byte[] data) {
                    return 0;
                }

                @Override
                public boolean reset() {
                    try {
                        stream.reset();
                    } catch (java.io.IOException e) {
                        return false;
                    }
                    return super.reset();
                }

                @Override
                public boolean seek(long pos) {
                    return false;
                }

                @Override
                public long size() {
                    return super.size();
                }
}// QIODevice_prefix__

class QIODevice_6_1__ {

                @Override
                protected long skipData(long maxSize) {
                    try {
                        stream.skip(maxSize);
                    } catch (java.io.IOException e) {
                        return 0;
                    }
                    return super.skipData(maxSize);
                }
}// QIODevice_6_1__

class QIODevice_infix__ {

                @Override
                protected int writeData(byte[] data) {
                    return 0;
                }
                
                @Override
                protected int readData(byte[] data) {
                    try {
                        return stream.read(data);
                    } catch (java.io.IOException e) {
                        return 0;
                    }
                }
            };
        }
        
        /**
         * Creates a new open QIODevice for writing to given InputStream
         * @param stream
         * @return new open write-only QIODevice
         */
        public static QIODevice fromOutputStream(java.io.OutputStream stream) {
            return new QIODevice() {
                {
                    setOpenMode(QIODevice.OpenModeFlag.WriteOnly);
                }
                
                @Override
                public boolean atEnd() {
                    return super.atEnd();
                }

                @Override
                public long bytesAvailable() {
                    return 0;
                }

                @Override
                public long bytesToWrite() {
                    return 0;
                }

                @Override
                public boolean canReadLine() {
                    return false;
                }

                @Override
                public void close() {
                    try {
                        stream.close();
                    } catch (java.io.IOException e) {
                    }
                    super.close();
                }

                @Override
                public boolean isSequential() {
                    return true;
                }

                @Override
                public boolean open(OpenMode mode) {
                    if(mode.value()==OpenModeFlag.WriteOnly.value())
                        return super.open(mode);
                    else return false;
                }

                @Override
                public long pos() {
                    return super.pos();
                }

                @Override
                protected int readLineData(byte[] data) {
                    return 0;
                }

                @Override
                public boolean reset() {
                    return false;
                }

                @Override
                public boolean seek(long pos) {
                    return false;
                }

                @Override
                public long size() {
                    return super.size();
                }
}// QIODevice_infix__

class QIODevice_6_2__ {

                @Override
                protected long skipData(long maxSize) {
                    return 0;
                }
}// QIODevice_6_2__

class QIODevice_suffix__ {

                @Override
                protected int writeData(byte[] data) {
                    try {
                        stream.write(data);
                        return data.length;
                    } catch (java.io.IOException e) {
                        return 0;
                    }
                }
                
                @Override
                protected int readData(byte[] data) {
                    return 0;
                }
            };
        }
}// class

class QCalendar___ extends QCalendar {
    public static final int Unspecified = unspecified();
    private static native int unspecified();
}// class

class QSignalMapper___ extends QSignalMapper {
        private java.util.HashMap<Object, Object> __rcObjectForObject;
}// class

class QAbstractFileEngine_MapExtensionReturn___ extends QAbstractFileEngine_MapExtensionReturn {
    
    private io.qt.QNativePointer currentAddressNativePointer; // don't garbage collect while in use
    public final void setAddress(String address) {
        currentAddressNativePointer = address != null ? io.qt.QNativePointer.createCharPointer(address) : null;
        address_private(currentAddressNativePointer);
    }

    public final String address() {
        io.qt.QNativePointer np = address_private();
        return np != null ? charPointerToString(np) : null;
    }
}// class

class QAbstractFileEngine_UnMapExtensionOption___ extends QAbstractFileEngine_UnMapExtensionOption {
    
    private io.qt.QNativePointer currentAddressNativePointer; // don't garbage collect while in use
    public final void setAddress(String address) {
        currentAddressNativePointer = address != null ? io.qt.QNativePointer.createCharPointer(address) : null;
        address_private(currentAddressNativePointer);
    }

    public final String address() {
        io.qt.QNativePointer np = address_private();
        return np != null ? charPointerToString(np) : null;
    }
}// class

class QFutureInterfaceBase___ {
    @io.qt.QtUninvokable
    public native final void reportException(Throwable e);
}// class

class QtFuture___ {

    /**
     * <p>See <a href="@docRoot/qtfuture.html#connect">QtFuture::connect(Sender *, Signal)</a></p>
     */
    @io.qt.QtUninvokable
    public static QFuture<Void> connect(QMetaObject.AbstractPrivateSignal0 signal) {
        if(signal.containingObject() instanceof QObject) {
            QObject sender = (QObject)signal.containingObject();
            QFutureInterface<Void> promise = QFutureInterface.createVoidFutureInterface();
            promise.reportStarted();
            QMetaObject.Connection[] connections = {null, null};
            connections[0] = signal.connect(()->{
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            connections[1] = sender.destroyed.connect(()->{
                promise.reportCanceled();
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            return promise.future();
        }
        return null;
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#connect">QtFuture::connect(Sender *, Signal)</a></p>
     */
    @io.qt.QtUninvokable
    public static <A> QFuture<A> connect(QMetaObject.AbstractPrivateSignal1<A> signal) {
        if(signal.containingObject() instanceof QObject) {
            QObject sender = (QObject)signal.containingObject();
            QFutureInterface<A> promise = new QFutureInterface<>();
            promise.reportStarted();
            QMetaObject.Connection[] connections = {null, null};
            connections[0] = signal.connect(value->{
                promise.reportResult(value);
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            connections[1] = sender.destroyed.connect(()->{
                promise.reportCanceled();
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            return promise.future();
        }
        return null;
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#connect">QtFuture::connect(Sender *, Signal)</a></p>
     */
    @io.qt.QtUninvokable
    public static <A,B> QFuture<QPair<A,B>> connect(QMetaObject.AbstractPrivateSignal2<A,B> signal) {
        if(signal.containingObject() instanceof QObject) {
            QObject sender = (QObject)signal.containingObject();
            QFutureInterface<QPair<A,B>> promise = new QFutureInterface<>();
            promise.reportStarted();
            QMetaObject.Connection[] connections = {null, null};
            connections[0] = signal.connect((a,b)->{
                promise.reportResult(new QPair<>(a,b));
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            connections[1] = sender.destroyed.connect(()->{
                promise.reportCanceled();
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            return promise.future();
        }
        return null;
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#connect">QtFuture::connect(Sender *, Signal)</a></p>
     */
    @io.qt.QtUninvokable
    public static <A,B,C> QFuture<Object[]> connect(QMetaObject.AbstractPrivateSignal3<A,B,C> signal) {
        if(signal.containingObject() instanceof QObject) {
            QObject sender = (QObject)signal.containingObject();
            QFutureInterface<Object[]> promise = new QFutureInterface<>();
            promise.reportStarted();
            QMetaObject.Connection[] connections = {null, null};
            connections[0] = signal.connect((a,b,c)->{
                promise.reportResult(new Object[] {a,b,c});
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            connections[1] = sender.destroyed.connect(()->{
                promise.reportCanceled();
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            return promise.future();
        }
        return null;
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#connect">QtFuture::connect(Sender *, Signal)</a></p>
     */
    @io.qt.QtUninvokable
    public static <A,B,C,D> QFuture<Object[]> connect(QMetaObject.AbstractPrivateSignal4<A,B,C,D> signal) {
        if(signal.containingObject() instanceof QObject) {
            QObject sender = (QObject)signal.containingObject();
            QFutureInterface<Object[]> promise = new QFutureInterface<>();
            promise.reportStarted();
            QMetaObject.Connection[] connections = {null, null};
            connections[0] = signal.connect((a,b,c,d)->{
                promise.reportResult(new Object[] {a,b,c,d});
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            connections[1] = sender.destroyed.connect(()->{
                promise.reportCanceled();
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            return promise.future();
        }
        return null;
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#connect">QtFuture::connect(Sender *, Signal)</a></p>
     */
    @io.qt.QtUninvokable
    public static <A,B,C,D,E> QFuture<Object[]> connect(QMetaObject.AbstractPrivateSignal5<A,B,C,D,E> signal) {
        if(signal.containingObject() instanceof QObject) {
            QObject sender = (QObject)signal.containingObject();
            QFutureInterface<Object[]> promise = new QFutureInterface<>();
            promise.reportStarted();
            QMetaObject.Connection[] connections = {null, null};
            connections[0] = signal.connect((a,b,c,d,e)->{
                promise.reportResult(new Object[] {a,b,c,d,e});
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            connections[1] = sender.destroyed.connect(()->{
                promise.reportCanceled();
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            return promise.future();
        }
        return null;
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#connect">QtFuture::connect(Sender *, Signal)</a></p>
     */
    @io.qt.QtUninvokable
    public static <A,B,C,D,E,F> QFuture<Object[]> connect(QMetaObject.AbstractPrivateSignal6<A,B,C,D,E,F> signal) {
        if(signal.containingObject() instanceof QObject) {
            QObject sender = (QObject)signal.containingObject();
            QFutureInterface<Object[]> promise = new QFutureInterface<>();
            promise.reportStarted();
            QMetaObject.Connection[] connections = {null, null};
            connections[0] = signal.connect((a,b,c,d,e,f)->{
                promise.reportResult(new Object[] {a,b,c,d,e,f});
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            connections[1] = sender.destroyed.connect(()->{
                promise.reportCanceled();
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            return promise.future();
        }
        return null;
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#connect">QtFuture::connect(Sender *, Signal)</a></p>
     */
    @io.qt.QtUninvokable
    public static <A,B,C,D,E,F,G> QFuture<Object[]> connect(QMetaObject.AbstractPrivateSignal7<A,B,C,D,E,F,G> signal) {
        if(signal.containingObject() instanceof QObject) {
            QObject sender = (QObject)signal.containingObject();
            QFutureInterface<Object[]> promise = new QFutureInterface<>();
            promise.reportStarted();
            QMetaObject.Connection[] connections = {null, null};
            connections[0] = signal.connect((a,b,c,d,e,f,g)->{
                promise.reportResult(new Object[] {a,b,c,d,e,f,g});
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            connections[1] = sender.destroyed.connect(()->{
                promise.reportCanceled();
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            return promise.future();
        }
        return null;
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#connect">QtFuture::connect(Sender *, Signal)</a></p>
     */
    @io.qt.QtUninvokable
    public static <A,B,C,D,E,F,G,H> QFuture<Object[]> connect(QMetaObject.AbstractPrivateSignal8<A,B,C,D,E,F,G,H> signal) {
        if(signal.containingObject() instanceof QObject) {
            QObject sender = (QObject)signal.containingObject();
            QFutureInterface<Object[]> promise = new QFutureInterface<>();
            promise.reportStarted();
            QMetaObject.Connection[] connections = {null, null};
            connections[0] = signal.connect((a,b,c,d,e,f,g,h)->{
                promise.reportResult(new Object[] {a,b,c,d,e,f,g,h});
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            connections[1] = sender.destroyed.connect(()->{
                promise.reportCanceled();
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            return promise.future();
        }
        return null;
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#connect">QtFuture::connect(Sender *, Signal)</a></p>
     */
    @io.qt.QtUninvokable
    public static <A,B,C,D,E,F,G,H,I> QFuture<Object[]> connect(QMetaObject.AbstractPrivateSignal9<A,B,C,D,E,F,G,H,I> signal) {
        if(signal.containingObject() instanceof QObject) {
            QObject sender = (QObject)signal.containingObject();
            QFutureInterface<Object[]> promise = new QFutureInterface<>();
            promise.reportStarted();
            QMetaObject.Connection[] connections = {null, null};
            connections[0] = signal.connect((a,b,c,d,e,f,g,h,i)->{
                promise.reportResult(new Object[] {a,b,c,d,e,f,g,h,i});
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            connections[1] = sender.destroyed.connect(()->{
                promise.reportCanceled();
                promise.reportFinished();
                QObject.disconnect(connections[0]);
                QObject.disconnect(connections[1]);
            });
            return promise.future();
        }
        return null;
    }
    
}// class

class QtFuture_6_1__ {
    /**
     * <p>See <a href="@docRoot/qtfuture.html#makeReadyFuture">QtFuture::makeReadyFuture(T)</a></p>
     */
    @io.qt.QtUninvokable
    public static <T> QFuture<T> makeReadyFuture(T value)
    {
        QFutureInterface<T> promise = new QFutureInterface<>();
        promise.reportStarted();
        promise.reportResult(value);
        promise.reportFinished();
        return promise.future();
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#makeReadyFuture-2">QtFuture::makeReadyFuture(QList&lt;T>)</a></p>
     */
    @io.qt.QtUninvokable
    public static <T> QFuture<T> makeReadyFuture(java.util.Collection<T> values)
    {
        QFutureInterface<T> promise = new QFutureInterface<>();
        promise.reportStarted();
        promise.reportResults(values);
        promise.reportFinished();
        return promise.future();
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#makeReadyFuture-1">QtFuture::makeReadyFuture()</a></p>
     */
    @io.qt.QtUninvokable
    public static QFuture<Void> makeReadyFuture()
    {
        QFutureInterface<Void> promise = QFutureInterface.createVoidFutureInterface();
        promise.reportStarted();
        promise.reportFinished();
        return promise.future();
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#makeExceptionalFuture">QtFuture::makeExceptionalFuture(QException)</a></p>
     */
    @io.qt.QtUninvokable
    public static QFuture<Void> makeExceptionalFuture(Throwable exception)
    {
        QFutureInterface<Void> promise = QFutureInterface.createVoidFutureInterface();
        promise.reportStarted();
        promise.reportException(exception);
        promise.reportFinished();
        return promise.future();
    }
}// class

class QtFuture_6_3__ {
    /**
     * <p>See <a href="@docRoot/qtfuture.html#whenAny-1">QtFuture::whenAny(Futures)</a></p>
     */
    @io.qt.QtUninvokable
    public static QFuture<QFuture<?>> whenAny(QFuture<?>... futures) {
        if(futures==null || futures.length==0)
            return makeReadyFuture(java.util.Collections.emptyList());
        QPromise<QFuture<?>> promise = new QPromise<>();
        promise.start();
        java.util.concurrent.atomic.AtomicBoolean ab = new java.util.concurrent.atomic.AtomicBoolean();
        for (QFuture<?> qFuture : futures) {
            qFuture.then(f->{
                if(ab.compareAndExchange(false, true)) {
                    promise.addResult(f);
                    promise.finish();
                }
            });
        }
        return promise.future();
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#whenAny-1">QtFuture::whenAny(Futures)</a></p>
     */
    @io.qt.QtUninvokable
    public static QFuture<QFuture<?>> whenAny(java.util.Collection<QFuture<?>> futures) {
        if(futures==null || futures.size()==0)
            return makeReadyFuture(java.util.Collections.emptyList());
        QPromise<QFuture<?>> promise = new QPromise<>();
        promise.start();
        java.util.concurrent.atomic.AtomicBoolean ab = new java.util.concurrent.atomic.AtomicBoolean();
        for (QFuture<?> qFuture : futures) {
            qFuture.then(f->{
                if(ab.compareAndExchange(false, true)) {
                    promise.addResult(f);
                    promise.finish();
                }
            });
        }
        return promise.future();
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#whenAll-1">QtFuture::whenAll(Futures)</a></p>
     */
    @io.qt.QtUninvokable
    public static QFuture<QList<QFuture<?>>> whenAll(QFuture<?>... futures) {
        if(futures==null || futures.length==0)
            return makeReadyFuture(java.util.Collections.emptyList());
        QPromise<QList<QFuture<?>>> promise = new QPromise<>();
        promise.start();
        QList<QFuture<?>> list = new QList<>(QMetaType.Type.QVariant);
        list.resize(futures.length);
        java.util.concurrent.atomic.AtomicInteger ai = new java.util.concurrent.atomic.AtomicInteger(futures.length);
        int i=0;
        for (QFuture<?> qFuture : futures) {
            int _i = i++;
            qFuture.then(f->{
                list.set(_i, f);
                if(ai.incrementAndGet()==0) {
                    promise.addResult(list);
                    promise.finish();
                }
            });
        }
        return promise.future();
    }
    
    /**
     * <p>See <a href="@docRoot/qtfuture.html#whenAll-1">QtFuture::whenAll(Futures)</a></p>
     */
    @io.qt.QtUninvokable
    public static QFuture<QList<QFuture<?>>> whenAll(java.util.Collection<QFuture<?>> futures) {
        if(futures==null || futures.size()==0)
            return makeReadyFuture(java.util.Collections.emptyList());
        QPromise<QList<QFuture<?>>> promise = new QPromise<>();
        promise.start();
        QList<QFuture<?>> list = new QList<>(QMetaType.Type.QVariant);
        list.resize(futures.size());
        java.util.concurrent.atomic.AtomicInteger ai = new java.util.concurrent.atomic.AtomicInteger(futures.size());
        int i=0;
        for (QFuture<?> qFuture : futures) {
            int _i = i++;
            qFuture.then(f->{
                list.set(_i, f);
                if(ai.incrementAndGet()==0) {
                    promise.addResult(list);
                    promise.finish();
                }
            });
        }
        return promise.future();
    }
}// class

class QJsonDocument___{
    
    /**
     * Result class for {@link #fromJson(io.qt.core.QByteArray)}
     */
    public static final class FromJsonResult{
        private FromJsonResult(QJsonDocument document, QJsonParseError error) {
            super();
            this.document = document;
            this.error = error;
        }
        public final io.qt.core.QJsonDocument document;
        public final io.qt.core.QJsonParseError error;
    }
}// class


class QJsonDocument_5__{
    
    /**
     * <p>Overloaded function for {@link #fromRawData(java.nio.ByteBuffer, io.qt.core.QJsonDocument.DataValidation)}.</p>
     */
    public static io.qt.core.QJsonDocument fromRawData(String data) {
        return fromRawData(data, io.qt.core.QJsonDocument.DataValidation.Validate);
    }
    
    /**
     * <p>Overloaded function for {@link #fromRawData(java.nio.ByteBuffer, io.qt.core.QJsonDocument.DataValidation)}.</p>
     */
    public static io.qt.core.QJsonDocument fromRawData(String data, io.qt.core.QJsonDocument.DataValidation validation) {
        return fromRawData(data.getBytes(), validation);
    }
    
    /**
     * <p>Overloaded function for {@link #fromRawData(java.nio.ByteBuffer, io.qt.core.QJsonDocument.DataValidation)}.</p>
     */
    public static io.qt.core.QJsonDocument fromRawData(byte[] data) {
        return fromRawData(data, io.qt.core.QJsonDocument.DataValidation.Validate);
    }
    
    /**
     * <p>Overloaded function for {@link #fromRawData(java.nio.ByteBuffer, io.qt.core.QJsonDocument.DataValidation)}.</p>
     */
    public static io.qt.core.QJsonDocument fromRawData(byte[] data, io.qt.core.QJsonDocument.DataValidation validation) {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(data);
        return fromRawData(buffer, buffer.capacity(), validation);
    }
    
}// class

class QLockFile__{

    /**
     * Result class for {@link #getLockInfo()}
     */
    public static class LockInfo {
            private LockInfo(long pid, String hostname, String appname) {
                this.pid = pid;
                this.hostname = hostname;
                this.appname = appname;
            }
            
            public final long pid;
            public final String hostname;
            public final String appname;
    }
}// class


class QPropertyAnimation___ extends QPropertyAnimation {
    
    /**
     * <p>Overloaded constructor for {@link #QPropertyAnimation(io.qt.core.QObject,io.qt.core.QByteArray,io.qt.core.QObject)}
     * with <code>parent = null</code>.</p>
     */
    public QPropertyAnimation(io.qt.core.QObject target, String propertyName){
        this(target, new io.qt.core.QByteArray(propertyName), null);
    }
    
    /**
     * <p>Overloaded constructor for {@link #QPropertyAnimation(io.qt.core.QObject,io.qt.core.QByteArray,io.qt.core.QObject)}.</p>
     */
    public QPropertyAnimation(io.qt.core.QObject target, String propertyName, io.qt.core.QObject parent){
        this(target, new io.qt.core.QByteArray(propertyName), parent);
    }
    
    /**
     * <p>Overloaded function for {@link #setPropertyName(io.qt.core.QByteArray)}.</p>
     */
    public final void setPropertyName(String propertyName)    {
        setPropertyName(new io.qt.core.QByteArray(propertyName));
    }
    
}// class

class QMetaType_5__ extends QMetaType {
    private static int metaTypeIdByName(java.lang.String typeName) {return type(typeName);}
    
    public static QMetaType fromObject(Object obj) {
        if(obj==null)
            return new QMetaType(QMetaType.Type.Nullptr);
        Class<?> objClass = QtJambi_LibraryUtilities.internal.getClass(obj);
        if(objClass==QList.class) {
            return QMetaType.fromType(QList.class, ((QList<?>)obj).elementMetaType());
        }else if(objClass==QLinkedList.class) {
            return QMetaType.fromType(QLinkedList.class, ((QLinkedList<?>)obj).elementMetaType());
        }else if(objClass==QVector.class) {
            return QMetaType.fromType(QVector.class, ((QVector<?>)obj).elementMetaType());
        }else if(objClass==QQueue.class) {
            return QMetaType.fromType(QQueue.class, ((QQueue<?>)obj).elementMetaType());
        }else if(objClass==QStack.class) {
            return QMetaType.fromType(QStack.class, ((QStack<?>)obj).elementMetaType());
        }else if(objClass==QSet.class) {
            return QMetaType.fromType(QSet.class, ((QSet<?>)obj).elementMetaType());
        }else if(objClass==QMultiMap.class) {
            return QMetaType.fromType(QMultiMap.class, ((QMultiMap<?,?>)obj).keyMetaType(), ((QMultiMap<?,?>)obj).valueMetaType());
        }else if(objClass==QMap.class) {
            return QMetaType.fromType(QMap.class, ((QMap<?,?>)obj).keyMetaType(), ((QMap<?,?>)obj).valueMetaType());
        }else if(objClass==QMultiHash.class) {
            return QMetaType.fromType(QMultiHash.class, ((QMultiHash<?,?>)obj).keyMetaType(), ((QMultiHash<?,?>)obj).valueMetaType());
        }else if(objClass==QHash.class) {
            return QMetaType.fromType(QHash.class, ((QHash<?,?>)obj).keyMetaType(), ((QHash<?,?>)obj).valueMetaType());
        }
        return QMetaType.fromType(objClass);
    }
}// class

class QMetaType_6__ extends QMetaType {
    private static int metaTypeIdByName(java.lang.String typeName) {return fromName(typeName).id();}
    
    public static QMetaType fromObject(Object obj) {
        if(obj==null)
            return new QMetaType(QMetaType.Type.Nullptr);
        Class<?> objClass = QtJambi_LibraryUtilities.internal.getClass(obj);
        if(objClass==QList.class) {
            return QMetaType.fromType(QList.class, ((QList<?>)obj).elementMetaType());
        }else if(objClass==QQueue.class) {
            return QMetaType.fromType(QQueue.class, ((QQueue<?>)obj).elementMetaType());
        }else if(objClass==QStack.class) {
            return QMetaType.fromType(QStack.class, ((QStack<?>)obj).elementMetaType());
        }else if(objClass==QSet.class) {
            return QMetaType.fromType(QSet.class, ((QSet<?>)obj).elementMetaType());
        }else if(objClass==QMultiMap.class) {
            return QMetaType.fromType(QMultiMap.class, ((QMultiMap<?,?>)obj).keyMetaType(), ((QMultiMap<?,?>)obj).valueMetaType());
        }else if(objClass==QMap.class) {
            return QMetaType.fromType(QMap.class, ((QMap<?,?>)obj).keyMetaType(), ((QMap<?,?>)obj).valueMetaType());
        }else if(objClass==QMultiHash.class) {
            return QMetaType.fromType(QMultiHash.class, ((QMultiHash<?,?>)obj).keyMetaType(), ((QMultiHash<?,?>)obj).valueMetaType());
        }else if(objClass==QHash.class) {
            return QMetaType.fromType(QHash.class, ((QHash<?,?>)obj).keyMetaType(), ((QHash<?,?>)obj).valueMetaType());
        }
        return QMetaType.fromType(objClass);
    }

    /**
     * <p>See <a href="@docRoot/qmetatype.html#fromName">QMetaType::fromName(const char*)</a></p>
     */
    public static io.qt.core.QMetaType fromName(String name){
        return fromName(new io.qt.core.QByteArrayView(name));
    }
    
    /**
     * <p>See <a href="@docRoot/qmetatype.html#fromName">QMetaType::fromName(QByteArray)</a></p>
     */
    public static io.qt.core.QMetaType fromName(QByteArray name){
        return fromName(new io.qt.core.QByteArrayView(name));
    }
    
    /**
     * <p>See <a href="@docRoot/qmetatype.html#fromName">QMetaType::fromName(const char*)</a></p>
     */
    public static io.qt.core.QMetaType fromName(byte[] name){
        return fromName(new io.qt.core.QByteArrayView(name));
    }
    
}// class

class QMetaType___ extends QMetaType {
    
    /**
     * Overloaded constructor for {@link #QMetaType(int)}.
     * @param type
     */
    public QMetaType(QMetaType.Type type){
        this(type.value());
    }
    
    /**
     * Returns the type name associated with this <code>QMetaType</code> as {@link String}.
     * @return type name
     */
    @Override
    @io.qt.QtUninvokable
    public final String toString() {
        return ""+name();
    }
    
    /**
     * @deprecated Use {@link #qRegisterMetaType(Class, QMetaType...)} instead.
     */
    @Deprecated
    public static int registerMetaType(Class<?> clazz, QMetaType... instantiations){
        return qRegisterMetaType(clazz, instantiations);
    }
    
    /**
     * @deprecated Use {@link #qMetaTypeId(Class, QMetaType...)} instead.
     */
    @Deprecated
    public static int metaTypeId(Class<?> clazz, QMetaType... instantiations){
        return qMetaTypeId(clazz, instantiations);
    }
    
    /**
     * Writes a value to data stream.
     * @param <U>
     */
    @FunctionalInterface
    public interface DataStreamInFn<U> extends java.util.function.BiConsumer<QDataStream, U>, java.io.Serializable{
    }
    
    /**
     * Reads a value from data stream.
     * @param <U>
     */
    @FunctionalInterface
    public interface DataStreamOutFn<U> extends java.util.function.Function<QDataStream, U>, java.io.Serializable{
    }
    
    /**
     * Registers datastream operators for meta type <code>T</code>.
     * @param datastreamInFn (only lambda expressions allowed)
     * @param datastreamOutFn (only lambda expressions allowed)
     * @return the registered meta type
     */
    public static <T> int registerDataStreamOperators(DataStreamInFn<T> datastreamInFn, DataStreamOutFn<T> datastreamOutFn) {
        java.util.Objects.requireNonNull(datastreamInFn);
        java.util.Objects.requireNonNull(datastreamOutFn);
        int[] datastreamInTypes = QtJambi_LibraryUtilities.internal.lambdaMetaTypes(DataStreamInFn.class, datastreamInFn);
        int[] datastreamOutTypes = QtJambi_LibraryUtilities.internal.lambdaMetaTypes(DataStreamOutFn.class, datastreamOutFn);
        Class<?>[] datastreamOutFnClassTypes = QtJambi_LibraryUtilities.internal.lambdaClassTypes(DataStreamOutFn.class, datastreamOutFn);
        Class<?>[] datastreamInFnClassTypes = QtJambi_LibraryUtilities.internal.lambdaClassTypes(DataStreamInFn.class, datastreamInFn);
        if(datastreamInTypes==null || datastreamOutTypes==null 
                || datastreamInTypes.length!=3 || datastreamOutTypes.length!=2)
            throw new IllegalArgumentException("DataStreamIn and/or DataStreamOut function not a lambda expression.");
        if(datastreamOutTypes[0]==0 || datastreamInTypes[2]==0)
            throw new IllegalArgumentException("Unable to recognize meta type.");
        if(datastreamOutTypes[0]!=datastreamInTypes[2] || datastreamOutFnClassTypes[0]!=datastreamInFnClassTypes[2]) {
            throw new IllegalArgumentException(String.format("DataStreamIn type %1$s (%2$s) is different from DataStreamOut type %3$s (%4$s).", datastreamInFnClassTypes[2].getTypeName(), new QMetaType(datastreamInTypes[2]).name(), datastreamOutFnClassTypes[0].getTypeName(), new QMetaType(datastreamOutTypes[0]).name()));
        }
        io.qt.internal.MetaTypeUtility.registerDataStreamOperators(datastreamOutTypes[0], datastreamOutFnClassTypes[0], datastreamInFn, datastreamOutFn);
        return datastreamOutTypes[0];
    }
    
    /**
     * Writes a value to debug stream.
     * @param <U>
     */
    public interface DebugStreamFn<U> extends java.util.function.BiConsumer<QDebug, U>, java.io.Serializable{
    }
    
    /**
     * Registers debug stream operator for meta type <code>T</code>.
     * @param debugstreamFn (only lambda expressions allowed)
     * @return the registered meta type
     */
    public static <T> int registerDebugStreamOperator(DebugStreamFn<T> debugstreamFn) {
        java.util.Objects.requireNonNull(debugstreamFn);
        int[] debugstreamTypes = QtJambi_LibraryUtilities.internal.lambdaMetaTypes(DebugStreamFn.class, debugstreamFn);
        Class<?>[] debugstreamClassTypes = QtJambi_LibraryUtilities.internal.lambdaClassTypes(DebugStreamFn.class, debugstreamFn);
        if(debugstreamTypes==null || debugstreamTypes.length!=3)
            throw new IllegalArgumentException("DebugStreamFn function not a lambda expression.");
        if(debugstreamTypes[2]==0)
            throw new IllegalArgumentException("Unable to recognize meta type.");
        io.qt.internal.MetaTypeUtility.registerDebugStreamOperator(debugstreamTypes[2], debugstreamClassTypes[2], debugstreamFn);
        return debugstreamTypes[2];
    }
	
    /**
     * <p>Convert value of T1 into T2</p>
     * @param <T1>
     * @param <T2>
     */
    @FunctionalInterface
    public interface ConverterFn<T1,T2> extends java.util.function.Function<T1, T2>, java.io.Serializable {
    }
    
    /**
     * Registers debug stream operator for meta type <code>T</code>.
     * @param debugstreamFn (only lambda expressions allowed)
     * @return the registered meta type
     */
    public static <T1,T2> boolean registerConverter(ConverterFn<T1,T2> converterFn) {
        java.util.Objects.requireNonNull(converterFn);
        int[] converterTypes = QtJambi_LibraryUtilities.internal.lambdaMetaTypes(ConverterFn.class, converterFn);
        Class<?>[] converterClassTypes = QtJambi_LibraryUtilities.internal.lambdaClassTypes(ConverterFn.class, converterFn);
        if(converterTypes==null || converterTypes.length!=2)
            throw new IllegalArgumentException("ConverterFn function not a lambda expression.");
        if(converterTypes[0]==0 || converterTypes[1]==0)
            throw new IllegalArgumentException("Unable to recognize meta type.");
        return io.qt.internal.MetaTypeUtility.registerConverter(converterTypes[0], converterClassTypes[0], converterTypes[1], converterClassTypes[1], converterFn);
    }
    
    @io.qt.QtUninvokable
    private static native Class<?> javaType(long __this__nativeId);
    
    /**
     * Returns the Java class for this meta type.
     * @return java class
     */
    @io.qt.QtUninvokable
    public final Class<?> javaType(){
        return javaType(QtJambi_LibraryUtilities.internal.nativeId(this));
    }
    
    /**
     * Returns the Java class for the given meta type ID.
     * @return java class
     */
    @io.qt.QtUninvokable
    public static native Class<?> javaType(int metaTypeId);

    /**
     * Interface super type for all generic classes representing a QMetaType-registered but Java-unknown value type.
     */
    public interface GenericTypeInterface{
        @io.qt.QtUninvokable
        public QMetaType metaType();
    }
    
    /**
     * This class represents a QMetaType-registered but Java-unknown enumerator type which can be used in QFlags.
     */
    public static final class GenericFlag implements io.qt.QtFlagEnumerator{
        private GenericFlag(int value) {
            super();
            this.value = value;
        }
    
        private final int value;
        
        @Override 
        public int value() { 
            return value; 
        }
        
        @Override 
        public int ordinal() { return -1; }
        
        @Override 
        public String name() { return "unknown"; }
        
        @Override 
        public Class<? extends io.qt.QtFlagEnumerator> getDeclaringClass() { return GenericFlag.class; }
    }
    
    /**
     * This class represents a QMetaType-registered but Java-unknown QFlags type.
     */
    public static final class GenericFlags extends io.qt.QFlags<GenericFlag> implements GenericTypeInterface, Comparable<io.qt.QFlags<?>>{
        private static final long serialVersionUID = -7659504264600507749L;
    
        @io.qt.NativeAccess
        private GenericFlags(int type, int value) {
            super(value);
            this.type = type;
        }
        
        private final int type;
        
        @Override
        public final QMetaType metaType() {
            return new QMetaType(type);
        }
        
        public GenericFlag[] flags(){
            return new GenericFlag[]{new GenericFlag(value())};
        }
    
        @Override
        public GenericFlags clone() {
            return new GenericFlags(type, value());
        }
    
        @Override
        public GenericFlags combined(GenericFlag flag) {
            return new GenericFlags(type, value() | flag.value());
        }

        @Override
        public int compareTo(io.qt.QFlags<?> o) {
                return Integer.compare(value(), o.value());
        }
    }
    
    /**
     * This class represents a QMetaType-registered but Java-unknown enumerator.
     */
    private abstract static class AbstractGenericEnumerator implements io.qt.QtAbstractEnumerator, GenericTypeInterface{
        @io.qt.NativeAccess
        private AbstractGenericEnumerator(int type, int index, String name) {
            super();
            this.type = type;
            this.index = index;
            this.name = name;
        }
    
        private final int type;
        private final int index;
        private final String name;
    
        
        @Override
        public final QMetaType metaType() {
            return new QMetaType(type);
        }
    
        @Override
        public final int ordinal() {
            return index;
        }
    
        @Override
        public final String name() {
            return name==null ? "unknown" : name;
        }
        
        abstract String valueString();
    
        @Override
        public String toString() {
            if(name==null) {
                return String.format("%1$s(%2$s)", typeName(type), valueString());
            }else {
                return String.format("%1$s(%2$s:%3$s)", typeName(type), name, valueString());
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + type;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (QtJambi_LibraryUtilities.internal.<Object>getClass(this) != QtJambi_LibraryUtilities.internal.getClass(obj))
                return false;
            AbstractGenericEnumerator other = (AbstractGenericEnumerator) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (type != other.type)
                return false;
            return true;
        }
    }
    
    /**
     * This class represents a QMetaType-registered but Java-unknown 32 Bit enumerator.
     */
    public final static class GenericEnumerator extends AbstractGenericEnumerator implements io.qt.QtEnumerator{
        @io.qt.NativeAccess
        private GenericEnumerator(int type, int index, int value, String name) {
            super(type, index, name);
            this.value = value;
        }
        
        private final int value;
        
        @Override
        public int value() {
            return value;
        }
    
        @Override
        public Class<? extends io.qt.QtEnumerator> getDeclaringClass() {
            return GenericEnumerator.class;
        }
    
        String valueString() {
            return Integer.toString(value);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + value;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (QtJambi_LibraryUtilities.internal.<Object>getClass(this) != QtJambi_LibraryUtilities.internal.getClass(obj))
                return false;
            GenericEnumerator other = (GenericEnumerator) obj;
            if (value != other.value)
                return false;
            return true;
        }
    }
    
    /**
     * This class represents a QMetaType-registered but Java-unknown 8 Bit enumerator.
     */
    public final static class GenericByteEnumerator extends AbstractGenericEnumerator implements io.qt.QtByteEnumerator{
        @io.qt.NativeAccess
        private GenericByteEnumerator(int type, int index, byte value, String name) {
            super(type, index, name);
            this.value = value;
        }
        
        private final byte value;
        
        @Override
        public byte value() {
            return value;
        }
    
        @Override
        public Class<? extends io.qt.QtByteEnumerator> getDeclaringClass() {
            return GenericByteEnumerator.class;
        }
    
        String valueString() {
            return Byte.toString(value);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + value;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (QtJambi_LibraryUtilities.internal.<Object>getClass(this) != QtJambi_LibraryUtilities.internal.getClass(obj))
                return false;
            GenericByteEnumerator other = (GenericByteEnumerator) obj;
            if (value != other.value)
                return false;
            return true;
        }
    }
    
    /**
     * This class represents a QMetaType-registered but Java-unknown 16 Bit enumerator.
     */
    public final static class GenericShortEnumerator extends AbstractGenericEnumerator implements io.qt.QtShortEnumerator{
        @io.qt.NativeAccess
        private GenericShortEnumerator(int type, int index, short value, String name) {
            super(type, index, name);
            this.value = value;
        }
        
        private final short value;
        
        @Override
        public short value() {
            return value;
        }
    
        @Override
        public Class<? extends io.qt.QtShortEnumerator> getDeclaringClass() {
            return GenericShortEnumerator.class;
        }
    
        String valueString() {
            return Short.toString(value);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + value;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            GenericShortEnumerator other = (GenericShortEnumerator) obj;
            if (value != other.value)
                return false;
            return true;
        }
    }
    
    /**
     * This class represents a QMetaType-registered but Java-unknown 64 Bit enumerator.
     */
    public final static class GenericLongEnumerator extends AbstractGenericEnumerator implements io.qt.QtLongEnumerator{
        @io.qt.NativeAccess
        private GenericLongEnumerator(int type, int index, long value, String name) {
            super(type, index, name);
            this.value = value;
        }
        
        private final long value;
        
        @Override
        public long value() {
            return value;
        }
    
        @Override
        public Class<? extends io.qt.QtLongEnumerator> getDeclaringClass() {
            return GenericLongEnumerator.class;
        }
    
        String valueString() {
            return Long.toString(value);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + (int) (value ^ (value >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            GenericLongEnumerator other = (GenericLongEnumerator) obj;
            if (value != other.value)
                return false;
            return true;
        }
    }
    
    /**
     * This class represents a QMetaType-registered but Java-unknown value type.
     */
    public final static class GenericValue extends io.qt.QtObject implements Cloneable, GenericTypeInterface{
        @io.qt.NativeAccess
        private GenericValue(QPrivateConstructor p) { super(p); }
        
        private native GenericValue clone(int type);
        private native java.nio.ByteBuffer buffer(int type);
        
        @Override
        public GenericValue clone() {
            return clone(type);
        }

        @io.qt.NativeAccess
        private final int type = QMetaType.Type.UnknownType.value();
        
        @io.qt.QtUninvokable
        public QMetaType metaType() {
            return new QMetaType(type);
        }
        
        public java.nio.ByteBuffer buffer(){
            return buffer(type);
        }
    }
    
    /**
     * This class represents a QMetaType-registered but Java-unknown object type.
     */
    public final static class GenericObject extends io.qt.QtObject implements GenericTypeInterface{
        private GenericObject(QPrivateConstructor p) { super(p); }
        
        @io.qt.NativeAccess
        private final int type = QMetaType.Type.UnknownType.value();
        
        @io.qt.QtUninvokable
        public QMetaType metaType() {
            return new QMetaType(type);
        }
    }
    
    /**
     * This class represents a QMetaType-registered but Java-unknown gadget type.
     */
    public final static class GenericGadget extends io.qt.QtGadget implements GenericTypeInterface{
        private GenericGadget(QPrivateConstructor p) {super(p);}
        
        @io.qt.NativeAccess
        private final int type = QMetaType.Type.UnknownType.value();
        
        @io.qt.QtUninvokable
        public QMetaType metaType() {
            return new QMetaType(type);
        }
    }
    
    @SuppressWarnings("unchecked")
    static <T> T cast(Class<T> javaType, Object object) {
        if (object != null && javaType != null) {
            if (javaType.isPrimitive()) {
                if (javaType == boolean.class)
                    return (T)(object==null ? Boolean.FALSE : Boolean.class.cast(object));
                else if (javaType == char.class)
                    return (T)(object==null ? Character.valueOf('\0') : Character.class.cast(object));
                else {
                    if (javaType == int.class)
                        return (T)(Integer.valueOf(object==null ? 0 : Number.class.cast(object).intValue()));
                    else if (javaType == double.class)
                        return (T)(Double.valueOf(object==null ? 0.0 : Number.class.cast(object).doubleValue()));
                    else if (javaType == long.class)
                        return (T)(Long.valueOf(object==null ? 0L : Number.class.cast(object).longValue()));
                    else if (javaType == float.class)
                        return (T)(Float.valueOf(object==null ? 0.f : Number.class.cast(object).floatValue()));
                    else if (javaType == short.class)
                        return (T)(Short.valueOf(object==null ? (short)0 : Number.class.cast(object).shortValue()));
                    else if (javaType == byte.class)
                        return (T)(Byte.valueOf(object==null ? (byte)0 : Number.class.cast(object).byteValue()));
                    else
                        return null;
                }
            } else {
                if (java.util.Collection.class.isAssignableFrom(javaType)) {
                    return (T)java.util.Collection.class.cast(object);
                } else if (java.util.Map.class.isAssignableFrom(javaType)) {
                    return (T)java.util.Map.class.cast(object);
                } else {
                    return javaType.cast(object);
                }
            }
        }
        return (T)object;
    }
}// class

class QVariant_5__ {    
    /**
     * @deprecated Use {@link QDataStream#writeObject(Object)} instead.
     */
    @Deprecated
    public static void saveObject(QDataStream stream, Object object){
        saveObject(stream, object, null);
    }
    
    /**
     * @deprecated Use {@link QDataStream#writeObject(Object)} instead.
     */
    @Deprecated
    public static void saveObject(QDataStream stream, Object object, Boolean[] ok){
        QVariant variant = new QVariant(object);
        int metaType = variant.userType();
        stream.append(metaType);
        boolean isOk = QMetaType.save(stream, metaType, object);
        if(ok!=null && ok.length>0)
            ok[0] = isOk;
    }
    
    /**
     * @deprecated Use {@link QDataStream#readObject(Class, QMetaType...)} instead.
     */
    @Deprecated
    public static Object loadObject(QDataStream stream){
        return loadObject(stream, null);
    }
    
    /**
     * @deprecated Use {@link QDataStream#readObject(Class, QMetaType...)} instead.
     */
    @Deprecated
    public static Object loadObject(QDataStream stream, Boolean ok[]){
        java.util.Optional<Object> optional = QMetaType.load(stream, stream.readInt());
        if(ok!=null && ok.length>0)
            ok[0] = optional.isPresent();
        return optional.orElse(null);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(int)}
     */
    public static Object convert(Object obj, int metaType) {
        return convert(obj, metaType, null);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(int)}
     */
    public static Object convert(Object obj, int metaType, boolean ok[]) {
        if(obj==null){
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return QMetaType.create(metaType);
        }
        QVariant variant = new QVariant(obj);
        if(obj!=null && variant.userType()==metaType) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return obj;
        }else if(variant.userType()==metaType || variant.convert(metaType)) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return variant.value();
        }else {
            if(ok!=null && ok.length>0)
                ok[0] = false;
            return null;
        }
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(int)}
     */
    public static Object convert(Object obj, QMetaType metaType) {
        return convert(obj, metaType.id(), null);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(int)}
     */
    public static Object convert(Object obj, QMetaType metaType, boolean ok[]) {
        return convert(obj, metaType.id(), ok);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(int)}
     */
    public static Object convert(Object obj, QMetaType.Type metaType) {
        return convert(obj, metaType.value(), null);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(int)}
     */
    public static Object convert(Object obj, QMetaType.Type metaType, boolean ok[]) {
        return convert(obj, metaType.value(), ok);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(int)}
     */
    public static Object convert(Object obj, Type metaType) {
        return convert(obj, metaType.value(), null);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(int)}
     */
    public static Object convert(Object obj, Type metaType, boolean ok[]) {
        return convert(obj, metaType.value(), ok);
    }
    
    /**
     * <p>See <a href="@docRoot/qvariant.html#canConvert">QVariant::canConvert(int)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean canConvert(io.qt.core.QMetaType.Type targetType){
        return canConvert(targetType.value());
    }
    
    /**
     * <p>See <a href="@docRoot/qvariant.html#canConvert">QVariant::canConvert(int)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean canConvert(Type targetType){
        return canConvert(targetType.value());
    }

    /**
     * <p>See <a href="@docRoot/qvariant.html#convert">QVariant::convert(int)</a></p>
     */
    public final boolean convert(Class<?> cl, QMetaType...instantiations) {
        return convert(QMetaType.fromType(cl, instantiations).id());
    }
    
    /**
     * <p>See <a href="@docRoot/qvariant.html#QVariant-1">QVariant::QVariant(int,const void*)</a></p>
     */
    public QVariant(QMetaType.Type metaType){
        this(metaType.value(), null); 
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(int)}
     */
    @Deprecated
    public static boolean canConvert(Object obj, int targetType) {
        QMetaType objectType = QMetaType.fromObject(obj);
        if(objectType.id()==targetType)
            return true;
        QVariant variant = new QVariant(objectType.id(), obj);
        return variant.canConvert(targetType);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(int)}
     */
    public static boolean canConvert(Object obj, QMetaType.Type type) {
        return canConvert(obj, type.value());
    }
}// class

class QVariant_6__ {
    
    /**
     * <p>See <a href="https://doc.qt.io/qt/qvariant.html#QVariant-1">QVariant::QVariant(QMetaType,const void*)</a></p>
     */
    public QVariant(io.qt.core.QMetaType.Type type, java.lang.Object copy){
        this(new QMetaType(type), copy);
    }
    
    /**
     * @deprecated Use {@link QDataStream#writeObject(Object)} instead.
     */
    @Deprecated
    public static void saveObject(QDataStream stream, Object object){
        saveObject(stream, object, null);
    }
    
    /**
     * @deprecated Use {@link QDataStream#writeObject(Object)} instead.
     */
    @Deprecated
    public static void saveObject(QDataStream stream, Object object, Boolean[] ok){
        QVariant variant = new QVariant(object);
        QMetaType metaType = variant.metaType();
        stream.append(metaType.id());
        boolean isOk = metaType.save(stream, object);
        if(ok!=null && ok.length>0)
            ok[0] = isOk;
    }
    
    /**
     * @deprecated Use {@link QDataStream#readObject(Class, QMetaType...)} instead.
     */
    @Deprecated
    public static Object loadObject(QDataStream stream){
        return loadObject(stream, null);
    }
    
    /**
     * @deprecated Use {@link QDataStream#readObject(Class, QMetaType...)} instead.
     */
    @Deprecated
    public static Object loadObject(QDataStream stream, Boolean ok[]){
        QMetaType metaType = new QMetaType(stream.readInt());
        java.util.Optional<Object> optional = metaType.load(stream);
        if(ok!=null && ok.length>0)
            ok[0] = optional.isPresent();
        return optional.orElse(null);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(QMetaType)}
     */
    public static Object convert(Object obj, QMetaType metaType) {
        return convert(obj, metaType, null);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(QMetaType)}
     */
    public static Object convert(Object obj, QMetaType metaType, boolean ok[]) {
        java.util.Objects.requireNonNull(metaType);
        if(obj==null){
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return metaType.create();
        }
        QVariant variant = new QVariant(obj);
        if(obj!=null && variant.metaType().equals(metaType)) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return obj;
        }else if(variant.metaType().equals(metaType) || variant.convert(metaType)) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return variant.value();
        }else {
            if(ok!=null && ok.length>0)
                ok[0] = false;
            return null;
        }
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(int)}
     */
    public static Object convert(Object obj, int metaType) {
        return convert(obj, metaType, null);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(QMetaType)}
     */
    public static Object convert(Object obj, int metaType, boolean ok[]) {
        return convert(obj, new QMetaType(metaType), ok);
    }
    
    /**
     * <p>See <a href="@docRoot/qvariant.html#canConvert">QVariant::canConvert(QMetaType)const</a></p>
     */
    @io.qt.QtUninvokable
    public final boolean canConvert(io.qt.core.QMetaType.Type targetType){
        return canConvert(new QMetaType(targetType));
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(QMetaType)}
     */
    public static Object convert(Object obj, QMetaType.Type type) {
        return convert(obj, new QMetaType(type));
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(QMetaType)}
     */
    public static Object convert(Object obj, QMetaType.Type type, boolean ok[]) {
        return convert(obj, new QMetaType(type), ok);
    }
    
    /**
     * <p>See <a href="@docRoot/qvariant.html#canConvert">QVariant::canConvert(QMetaType)const</a></p>
     */
    @Deprecated
    @io.qt.QtUninvokable
    public final boolean canConvert(Type targetType){
        return canConvert(new QMetaType(targetType.value()));
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvert(Object obj, QMetaType type) {
        QMetaType objectType = QMetaType.fromObject(obj);
        if(objectType.equals(type))
            return true;
        QVariant variant = new QVariant(objectType, obj);
        return variant.canConvert(type);
    }

    /**
     * <p>See <a href="@docRoot/qvariant.html#convert">QVariant::convert(QMetaType)</a></p>
     */
    public final boolean convert(Class<?> cl, QMetaType...instantiations) {
        return convert(QMetaType.fromType(cl, instantiations));
    }
    
    /**
     * <p>See <a href="@docRoot/qvariant.html#QVariant-1">QVariant::QVariant(QMetaType,const void*)</a></p>
     */
    public QVariant(QMetaType.Type metaType){
        this(new QMetaType(metaType), null); 
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvert(Object obj, QMetaType.Type type) {
        return canConvert(obj, new QMetaType(type));
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    @Deprecated
    public static boolean canConvert(Object obj, int targetType) {
        return canConvert(obj, new QMetaType(targetType));
    }
}// class

class QVariant___ {
    
    static Class<?> getComplexType(Class<?> primitiveType) {
        if (primitiveType == int.class)
            return Integer.class;
        else if (primitiveType == double.class)
            return Double.class;
        else if (primitiveType == long.class)
            return Long.class;
        else if (primitiveType == float.class)
            return Float.class;
        else if (primitiveType == short.class)
            return Short.class;
        else if (primitiveType == boolean.class)
            return Boolean.class;
        else if (primitiveType == char.class)
            return Character.class;
        else if (primitiveType == byte.class)
            return Byte.class;
        else
            return primitiveType;
    }
    
    /**
     * Use {@link QMetaType#fromObject(Object)} instead.
     */
    @Deprecated
    public static int type(Object obj) {
        return QMetaType.fromObject(obj).id();
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(QMetaType)}
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object obj, Class<T> cl, QMetaType...instantiations) {
        boolean[] ok = {false};
        Object result = convert(obj, QMetaType.fromType(cl, instantiations), ok);
        Class<?> _cl = cl.isPrimitive() ? getComplexType(cl) : cl;
        return (ok[0] || _cl.isInstance(result)) ? (T)result : null;
    }
    
    /**
     * Use {@link #isValid()} instead.
     */
    @Deprecated
    public static boolean isValid(Object variant) {
        return new QVariant(variant).isValid();
    }
        
    private static Class<?> dbusVariant;
    private static boolean dbusVariantResolved;
    
    private static boolean isDBusVariant(Object value, Class <?> cl) {
        if(!dbusVariantResolved) {
            dbusVariantResolved = true;
            Class<?> _dbusVariant = null;
            try {
                _dbusVariant = Class.forName("io.qt.dbus.QDBusVariant");
            } catch (Exception e) {
            }
            dbusVariant = _dbusVariant;
        }
        return dbusVariant!=null && cl!=dbusVariant && dbusVariant.isInstance(value);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static <E extends Enum<?>> boolean canConvertToEnum(Class<E> enumClass, Object obj)
    {
        if(enumClass.isInstance(obj)) {
            return true;
        }else {
            QVariant variant = new QVariant(obj);
            if(io.qt.QtEnumerator.class.isAssignableFrom(enumClass) 
                    || io.qt.QtByteEnumerator.class.isAssignableFrom(enumClass) 
                    || io.qt.QtShortEnumerator.class.isAssignableFrom(enumClass)) {
                return variant.canConvert(QMetaType.Type.Int);
            }else if(io.qt.QtLongEnumerator.class.isAssignableFrom(enumClass)) {
                return variant.canConvert(QMetaType.Type.Long);
            }else if (variant.canConvert(QMetaType.Type.Int)) {
                boolean[] ok = {false};
                int value = variant.toInt(ok);
                E[] constants = enumClass.getEnumConstants();
                return constants!=null && ok[0] && value<constants.length;
            }else return false;
        }
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToFlags(Object obj)
    {
        return obj instanceof io.qt.QFlags || new QVariant(obj).canConvert(QMetaType.Type.Int);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static <E extends Enum<?>> E toEnum(Class<E> enumClass, Object obj) {
        return toEnum(enumClass, obj, (boolean[])null);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static <E extends Enum<?>> E toEnum(Class<E> enumClass, Object obj, boolean ok[]) {
        if(enumClass.isInstance(obj)) {
            try {
                if(ok!=null && ok.length>0)
                    ok[0] = true;
                return enumClass.cast(obj);
            } catch (Exception e) {
            }
        }else {
            QVariant variant = new QVariant(obj);
            if(io.qt.QtEnumerator.class.isAssignableFrom(enumClass)) {
                try {
                    java.lang.reflect.Method resolveMethod = enumClass.getMethod("resolve", int.class);
                    int value = variant.toInt(ok);
                    if(ok==null || ok[0])
                        return enumClass.cast(resolveMethod.invoke(null, value));
                } catch (Exception e) {
                }
            }else if(io.qt.QtByteEnumerator.class.isAssignableFrom(enumClass)) {
                try {
                    java.lang.reflect.Method resolveMethod = enumClass.getMethod("resolve", byte.class);
                    int value = variant.toInt(ok);
                    if(ok==null || ok[0])
                        return enumClass.cast(resolveMethod.invoke(null, (byte)value));
                } catch (Exception e) {
                }
            }else if(io.qt.QtShortEnumerator.class.isAssignableFrom(enumClass)) {
                try {
                    java.lang.reflect.Method resolveMethod = enumClass.getMethod("resolve", short.class);
                    int value = variant.toInt(ok);
                    if(ok==null || ok[0])
                        return enumClass.cast(resolveMethod.invoke(null, value));
                } catch (Exception e) {
                }
            }else if(io.qt.QtLongEnumerator.class.isAssignableFrom(enumClass)) {
                try {
                    java.lang.reflect.Method resolveMethod = enumClass.getMethod("resolve", long.class);
                    long value = variant.toLong(ok);
                    if(ok==null || ok[0])
                        return enumClass.cast(resolveMethod.invoke(null, value));
                } catch (Exception e) {
                }
            }else{
                int value = variant.toInt(ok);
                E[] constants = enumClass.getEnumConstants();
                if(constants!=null && (ok==null || value<constants.length)) {
                    return constants[value];
                }
            }
        }
        if(ok!=null && ok.length>0)
            ok[0] = false;
        return null;
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(QMetaType)}
     */
    public static <F extends io.qt.QFlags<?>> F toFlags(Class<F> flagsClass, Object obj) {
        return toFlags(flagsClass, obj, null);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #convert(QMetaType)}
     */
    public static <F extends io.qt.QFlags<?>> F toFlags(Class<F> flagsClass, Object obj, boolean ok[]) {
        if(flagsClass.isInstance(obj)) {
            try{
                if(ok!=null && ok.length>0)
                    ok[0] = true;
                return flagsClass.cast(obj);
            } catch (Exception e) {
            }
        }else {
            QVariant variant = new QVariant(obj);
            int value = variant.toInt(ok);
            if(ok==null || ok[0]){
                try{
                    return flagsClass.getConstructor(int.class).newInstance(value);
                } catch (Exception e) {
                }
            }
        }
        if(ok!=null && ok.length>0)
            ok[0] = false;
        return null;
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToDouble(Object obj){
        return obj instanceof Number || canConvert(obj, QMetaType.Type.Double);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toDouble()}
     */
    public static double toDouble(Object obj) { return toDouble(obj, null); }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toDouble()}
     */
    public static double toDouble(Object obj, boolean ok[]){
        if (obj==null) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return 0.0;
        } else if (obj instanceof Number) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return ((Number) obj).doubleValue();
        } else {
            QVariant variant = new QVariant(obj);
            return variant.toDouble(ok);
        }
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToFloat(Object obj){
        return obj instanceof Number || canConvert(obj, QMetaType.Type.Float);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toFloat()}
     */
    public static float toFloat(Object obj) { return toFloat(obj, null); }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toFloat()}
     */
    public static float toFloat(Object obj, boolean ok[]){
        if (obj==null) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return 0.f;
        } else if (obj instanceof Float) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return (Float)obj;
        } else if (obj instanceof Number) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return ((Number) obj).floatValue();
        } else {
            QVariant variant = new QVariant(obj);
            return variant.toFloat(ok);
        }
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToString(Object obj){
        return true;
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #toString()}
     */
    public static String toString(Object obj){
        return obj==null ? new String() : obj.toString();
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToBoolean(Object obj){
        return obj instanceof Boolean || obj instanceof Number || canConvert(obj, QMetaType.Type.Bool);
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #toBoolean()}
     */
    public static boolean toBoolean(Object obj){
        if (obj==null) {
            return false;
        } else if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue() != 0;
        } else {
            return new QVariant(obj).toBoolean();
        }
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToByteArray(Object obj){
        return obj instanceof QByteArray || canConvert(obj, QMetaType.Type.QByteArray);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toByteArray()}
     */
    public static QByteArray toByteArray(Object obj)
    {
        if(obj instanceof QByteArray) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QByteArray)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QByteArray);
            if(returned instanceof QByteArray) {
                return (QByteArray)returned;
            }
        }
        return new QByteArray();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToBitArray(Object obj)
    {
        return obj instanceof QBitArray || canConvert(obj, QMetaType.Type.QBitArray);
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #toBitArray()}
     */
    public static QBitArray toBitArray(Object obj)
    {
        if(obj instanceof QBitArray) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QBitArray)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QBitArray);
            if(returned instanceof QBitArray) {
                return (QBitArray)returned;
            }            
        }
        return new QBitArray();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToChar(Object obj)
    {
        return obj instanceof Character || canConvert(obj, QMetaType.Type.QChar);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toChar()}
     */
    public static char toChar(Object obj)
    {
        if(obj==null)
            return 0;
        else if (obj instanceof Character)
            return (Character) obj;
        else
            return new QVariant(obj).toChar();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToDate(Object obj)
    {
        return obj instanceof QDate || canConvert(obj, QMetaType.Type.QDate);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toDate()}
     */
    public static QDate toDate(Object obj)
    {
        if(obj instanceof QDate) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QDate)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QDate);
            if(returned instanceof QDate) {
                return (QDate)returned;
            }
        }
        return new QDate();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToDateTime(Object obj)
    {
        return obj instanceof QDateTime || canConvert(obj, QMetaType.Type.QDateTime);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toDateTime()}
     */
    public static QDateTime toDateTime(Object obj)
    {
        if(obj instanceof QDateTime) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QDateTime)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QDateTime);
            if(returned instanceof QDateTime) {
                return (QDateTime)returned;
            }
        }
        return new QDateTime();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToInt(Object obj)
    {
        return obj instanceof Number || canConvert(obj, QMetaType.Type.Int);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toInt()}
     */
    public static int toInt(Object obj) { return toInt(obj, null); }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toFloat()}
     */
    public static int toInt(Object obj, boolean ok[]){
        if(obj==null) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return 0;
        }else if (obj instanceof Number) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return ((Number) obj).intValue();
        } else {
            QVariant variant = new QVariant(obj);
            return variant.toInt(ok);
        }
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToLine(Object obj)
    {
        return obj instanceof QLine || canConvert(obj, QMetaType.Type.QLine);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toLine()}
     */
    public static QLine toLine(Object obj)
    {
        if(obj instanceof QLine) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QLine)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QLine);
            if(returned instanceof QLine) {
                return (QLine)returned;
            }
        }
        return new QLine();
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToLineF(Object obj)
    {
        return obj instanceof QLineF || canConvert(obj, QMetaType.Type.QLineF);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toLineF()}
     */
    public static QLineF toLineF(Object obj)
    {
        if(obj instanceof QLineF) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QLineF)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QLineF);
            if(returned instanceof QLineF) {
                return (QLineF)returned;
            }
        }
        return new QLineF();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToLocale(Object obj)
    {
        return obj instanceof QLocale || canConvert(obj, QMetaType.Type.QLocale);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toLocale()}
     */
    public static QLocale toLocale(Object obj)
    {
        if(obj instanceof QLocale) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QLocale)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QLocale);
            if(returned instanceof QLocale) {
                return (QLocale)returned;
            }
        }
        return new QLocale();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToPoint(Object obj)
    {
        return obj instanceof QPoint || canConvert(obj, QMetaType.Type.QPoint);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toPoint()}
     */
    public static QPoint toPoint(Object obj)
    {
        if(obj instanceof QPoint) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QPoint)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QPoint);
            if(returned instanceof QPoint) {
                return (QPoint)returned;
            }
        }
        return new QPoint();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToPointF(Object obj)
    {
        return obj instanceof QPointF || canConvert(obj, QMetaType.Type.QPointF);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toPointF()}
     */
    public static QPointF toPointF(Object obj)
    {
        if(obj instanceof QPointF) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QPointF)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QPointF);
            if(returned instanceof QPointF) {
                return (QPointF)returned;
            }
        }
        return new QPointF();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToRect(Object obj)
    {
        return obj instanceof QRect || canConvert(obj, QMetaType.Type.QRect);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toRect()}
     */
    public static QRect toRect(Object obj)
    {
        if(obj instanceof QRect) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QRect)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QRect);
            if(returned instanceof QRect) {
                return (QRect)returned;
            }
        }
        return new QRect();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToRectF(Object obj)
    {
        return obj instanceof QRectF || canConvert(obj, QMetaType.Type.QRectF);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toRectF()}
     */
    public static QRectF toRectF(Object obj)
    {
        if(obj instanceof QRectF) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QRectF)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QRectF);
            if(returned instanceof QRectF) {
                return (QRectF)returned;
            }
        }
        return new QRectF();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToRegularExpression(Object obj)
    {
        return obj instanceof QRegularExpression || canConvert(obj, QMetaType.Type.QRegularExpression);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toRegularExpression()}
     */
    public static QRegularExpression toRegularExpression(Object obj)
    {
        if(obj instanceof QRegularExpression) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QRegularExpression)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QRegularExpression);
            if(returned instanceof QRegularExpression) {
                return (QRegularExpression)returned;
            }
        }
        return new QRegularExpression();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToSize(Object obj)
    {
        return obj instanceof QSize || canConvert(obj, QMetaType.Type.QSize);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toSize()}
     */
    public static QSize toSize(Object obj)
    {
        if(obj instanceof QSize) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QSize)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QSize);
            if(returned instanceof QSize) {
                return (QSize)returned;
            }
        }
        return new QSize();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToSizeF(Object obj)
    {
        return obj instanceof QSizeF || canConvert(obj, QMetaType.Type.QSizeF);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toSizeF()}
     */
    public static QSizeF toSizeF(Object obj)
    {
        if(obj instanceof QSizeF) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QSizeF)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QSizeF);
            if(returned instanceof QSizeF) {
                return (QSizeF)returned;
            }
        }
        return new QSizeF();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToTime(Object obj)
    {
        return obj instanceof QTime || canConvert(obj, QMetaType.Type.QTime);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toTime()}
     */
    public static QTime toTime(Object obj)
    {
        if(obj instanceof QTime) {
            if(!((io.qt.QtObjectInterface)obj).isDisposed())
                return (QTime)obj;
        }else if(obj!=null){
            Object returned = convert(obj, QMetaType.Type.QTime);
            if(returned instanceof QTime) {
                return (QTime)returned;
            }
        }
        return new QTime();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToLong(Object obj)
    {
        return obj instanceof Number || canConvert(obj, QMetaType.Type.LongLong);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toLong()}
     */
    public static long toLong(Object obj, boolean ok[]){
        if(obj==null) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return 0L;
        }else if (obj instanceof Number) {
            if(ok!=null && ok.length>0)
                ok[0] = true;
            return ((Number) obj).longValue();
        } else {
            QVariant variant = new QVariant(obj);
            return variant.toLong(ok);
        }
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToList(Object obj)
    {
        return obj instanceof java.util.Collection || canConvert(obj, QMetaType.Type.QVariantList);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toList()}
     */
    @SuppressWarnings("unchecked")
    public static QList<Object> toList(Object obj)
    {
        if (obj instanceof java.util.Collection){
            if (obj instanceof QList && ((QList<?>)obj).elementMetaType().javaType()==Object.class)
                return (QList<Object>)obj;
            QList<Object> list = QList.createVariantList();
                list.addAll((java.util.Collection<?>) obj);
            return list;
        }else if(obj!=null) {
            Object returned = convert(obj, QMetaType.Type.QVariantList);
            if(returned instanceof QList) {
                return (QList<Object>)returned;
            }
        }
        return QList.createVariantList();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToStringList(Object obj)
    {
        return obj instanceof java.util.Collection || canConvert(obj, QMetaType.Type.QStringList);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toStringList()}
     */
    public static QStringList toStringList(Object obj)
    {
        if (obj instanceof java.util.Collection){
            if (obj instanceof QStringList) {
                return (QStringList)obj;
            }
            QStringList list = new QStringList();
            for(Object entry : (java.util.Collection<?>)obj) {
                list.add(entry==null ? null : entry.toString());
            }
            return list;
        }else if(obj!=null) {
            Object returned = convert(obj, QMetaType.Type.QStringList);
            if(returned instanceof QStringList) {
                return (QStringList)returned;
            }
        }
        return new QStringList();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToCollection(Object obj)
    {
        return obj instanceof java.util.Collection || canConvert(obj, QMetaType.Type.QVariantList);
    }
    
    /**
     * Convenient static function for type conversion.
     * @see {@link #toList()}
     */
    public static java.util.Collection<?> toCollection(Object obj)
    {
        if (obj instanceof java.util.Collection){
            return (java.util.Collection<?>)obj;
        }else if(obj!=null) {
            Object returned = convert(obj, QMetaType.Type.QVariantList);
            if(returned instanceof java.util.Collection) {
                return (java.util.Collection<?>)returned;
            }
        }
        return QList.createVariantList();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #canConvert(QMetaType)}
     */
    public static boolean canConvertToMap(Object obj)
    {
        return obj instanceof java.util.Map || canConvert(obj, QMetaType.Type.QVariantHash);
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #toHash()}
     */
    @SuppressWarnings("unchecked")
    public static QHash<String,Object> toHash(Object obj)
    {
        if (obj instanceof java.util.Map) {
            if(obj instanceof QHash 
                    && ((QHash<?,?>)obj).keyMetaType().javaType()==String.class
                    && ((QHash<?,?>)obj).valueMetaType().javaType()==Object.class) {
                return (QHash<String,Object>) obj;
            }
            java.util.Map<?, ?> otherMap = (java.util.Map<?, ?>) obj;
            QHash<String,Object> map = QHash.createVariantHash();
            for (java.util.Map.Entry<?, ?> e : otherMap.entrySet())
                map.put(e.getKey().toString(), e.getValue());
            return map;
        }else if(obj!=null) {
            Object returned = convert(obj, QMetaType.Type.QVariantHash);
            if(returned instanceof QHash) {
                return (QHash<String,Object>)returned;
            }
        }
        return QHash.createVariantHash();
    }

    /**
     * Convenient static function for type conversion.
     * @see {@link #toMap()}
     */
    @SuppressWarnings("unchecked")
    public static QMap<String,Object> toMap(Object obj)
    {
        if (obj instanceof java.util.Map) {
            if(obj instanceof QMap 
                    && ((QMap<?,?>)obj).keyMetaType().javaType()==String.class
                    && ((QMap<?,?>)obj).valueMetaType().javaType()==Object.class) {
                return (QMap<String,Object>) obj;
            }
            java.util.Map<?, ?> otherMap = (java.util.Map<?, ?>) obj;
            QMap<String,Object> map = QMap.createVariantMap();
            for (java.util.Map.Entry<?, ?> e : otherMap.entrySet())
                map.put(e.getKey().toString(), e.getValue());
            return map;
        }else if(obj!=null) {
            Object returned = convert(obj, QMetaType.Type.QVariantHash);
            if(returned instanceof QMap) {
                return (QMap<String,Object>)returned;
            }
        }
        return QMap.createVariantMap();
    }
}// class

class QPartialOrdering___ {
    /**
     * <p>See <a href="@docRoot/qpartialordering.html#Equivalent-var">QPartialOrdering::Equivalent</a></p>
     */
    public static final int Equivalent = 0;
    /**
     * <p>See <a href="@docRoot/qpartialordering.html#Less-var">QPartialOrdering::Less</a></p>
     */
    public static final int Greater = 1;
    /**
     * <p>See <a href="@docRoot/qpartialordering.html#Greater-var">QPartialOrdering::Greater</a></p>
     */
    public static final int Less = -1;
    /**
     * <p>See <a href="@docRoot/qpartialordering.html#Unordered-var">QPartialOrdering::Unordered</a></p>
     */
    public static final int Unordered = -127;
}// class

class QMetaMethod___ {
    private Class<?> returnClassType;
    private java.util.List<Class<?>> parameterClassTypes;
    
    /**
     * <p>See <a href="@docRoot/qmetamethod.html#parameterTypes">QMetaMethod::parameterTypes() const</a></p>
     */
    @io.qt.QtUninvokable
    public final java.util.List<Class<?>> parameterClassTypes() {
        if(parameterClassTypes==null) {
            parameterClassTypes = java.util.Collections.unmodifiableList(parameterClassTypes_native());
        }
        return parameterClassTypes;
    }
    
    @io.qt.QtUninvokable
    private native final java.util.List<Class<?>> parameterClassTypes_native();
    
    /**
     * <p>See <a href="@docRoot/qmetamethod.html#parameterType">QMetaMethod::parameterType(int index) const</a></p>
     */
    @io.qt.QtUninvokable
    public final Class<?> parameterClassType(int index) {
        return parameterClassTypes().get(index);
    }
    
    /**
     * <p>See <a href="@docRoot/qmetamethod.html#returnType">QMetaMethod::returnType() const</a></p>
     */
    @io.qt.QtUninvokable
    public final Class<?> returnClassType(){
        if(returnClassType==null)
            returnClassType = returnClassType_native();
        return returnClassType;
    }
    
    @io.qt.QtUninvokable
    private native final Class<?> returnClassType_native();
        
    /**
     * <p>See <a href="@docRoot/qmetamethod.html#methodSignature">QMetaMethod::methodSignature() const</a></p>
     */
    @io.qt.QtUninvokable
    public final String methodSignature() {
        if(!isValid())
            return "";
        StringBuilder args = new StringBuilder();
        java.util.List<Class<?>> parameterClassTypes = parameterClassTypes();
        for(int i=0; i<parameterClassTypes.size(); ++i) {
            if(i!=0)
                args.append(',');
            Class<?> parameterType = parameterClassTypes.get(i);
            if(parameterType!=null){
                if(parameterType.isArray()) {
                    int arrayDepth = 1;
                    Class<?> componentType = parameterType.getComponentType();
                    while(true) {
                        if(componentType.isArray()) {
                            ++arrayDepth;
                            componentType = componentType.getComponentType();
                        }else {
                            args.append(componentType.getName());
                            for (int j = 0; j < arrayDepth; j++) {
                                args.append("[]");
                            }
                            break;
                        }
                    }
                }else {
                    args.append(parameterType.getName());
                }
            }else {
                args.append("<unknown type>");
            }
        }
        return String.format("%1$s(%2$s)", name(), args);
    }
    
    @Override
    @io.qt.QtUninvokable
    public final String toString() {
        return methodSignature();
    }
    
    /**
     * <p>See <a href="@docRoot/qmetamethod.html#invoke-1">QMetaMethod::invoke(QObject *object, QGenericReturnArgument returnValue, QGenericArgument val0 = QGenericArgument(nullptr), QGenericArgument val1 = QGenericArgument(), QGenericArgument val2 = QGenericArgument(), QGenericArgument val3 = QGenericArgument(), QGenericArgument val4 = QGenericArgument(), QGenericArgument val5 = QGenericArgument(), QGenericArgument val6 = QGenericArgument(), QGenericArgument val7 = QGenericArgument(), QGenericArgument val8 = QGenericArgument(), QGenericArgument val9 = QGenericArgument()) const</a></p>
     */
    @io.qt.QtUninvokable
    public final Object invoke(QObject object, Object... args) throws IllegalArgumentException, io.qt.QUnsuccessfulInvocationException {
        return invoke(object, Qt.ConnectionType.AutoConnection, args);
    }
    
    /**
     * <p>See <a href="@docRoot/qmetamethod.html#invoke">QMetaMethod::invoke(QObject *object, Qt::ConnectionType connectionType, QGenericReturnArgument returnValue, QGenericArgument val0 = QGenericArgument(nullptr), QGenericArgument val1 = QGenericArgument(), QGenericArgument val2 = QGenericArgument(), QGenericArgument val3 = QGenericArgument(), QGenericArgument val4 = QGenericArgument(), QGenericArgument val5 = QGenericArgument(), QGenericArgument val6 = QGenericArgument(), QGenericArgument val7 = QGenericArgument(), QGenericArgument val8 = QGenericArgument(), QGenericArgument val9 = QGenericArgument()) const</a></p>
     */
    @io.qt.QtUninvokable
    public final Object invoke(QObject object, Qt.ConnectionType connection, Object... args) throws IllegalArgumentException, io.qt.QUnsuccessfulInvocationException {
        if(!isValid())
            throw new io.qt.QUnsuccessfulInvocationException("Cannot invoke an invalid QMetaMethod.");
        if(parameterCount() != args.length) {
            throw new IllegalArgumentException(String.format("Wrong number of arguments. expected: %2$s, given: %1$s", args.length, parameterCount()));
        }
        {
            QMetaObject enclosingMetaObject = enclosingMetaObject();
            QMetaObject objectMO = object.metaObject();
            if(objectMO!=enclosingMetaObject && !objectMO.inherits(enclosingMetaObject)) {
                throw new IllegalArgumentException(String.format("Given object is not an instance of %1$s", enclosingMetaObject.className()));
            }
        }
        if(connection==Qt.ConnectionType.BlockingQueuedConnection && object.thread()==QThread.currentThread()) {
            throw new io.qt.QUnsuccessfulInvocationException("Blocking-queued invocation on object whose thread is the current thread is not allowed.");
        }
        Class<?>[] argClassTypes = new Class<?>[1+parameterCount()];
        Class<?> returnType = returnClassType();
        argClassTypes[0] = returnType==null ? void.class : returnType;
        if(argClassTypes[0]!=void.class && argClassTypes[0]!=Void.class) {
            if(connection==Qt.ConnectionType.QueuedConnection) {
                throw new io.qt.QUnsuccessfulInvocationException("Unable to invoke methods with return values in queued connections.");
            }else if(connection==Qt.ConnectionType.AutoConnection) {
                if(QThread.currentThread() != object.thread()) {
                    throw new io.qt.QUnsuccessfulInvocationException("Unable to invoke methods with return values in queued connections (auto connection with different threads).");
                }
            }
        }
        java.util.List<Class<?>> parameterClassTypes = parameterClassTypes();
        for(int i=0; i<parameterClassTypes.size(); ++i) {
            argClassTypes[i+1] = parameterClassTypes.get(i);
        }
        return invoke_native(object, argClassTypes, connection.ordinal(), args);
    }
    
    @io.qt.QtUninvokable
    private native Object invoke_native(QObject object__id, Class<?>[] argClassTypes, int connection, Object[] args);
    
    /**
     * <p>See <a href="@docRoot/qmetamethod.html#invokeOnGadget">QMetaMethod::invokeOnGadget(void *gadget, QGenericReturnArgument returnValue, QGenericArgument val0 = QGenericArgument(nullptr), QGenericArgument val1 = QGenericArgument(), QGenericArgument val2 = QGenericArgument(), QGenericArgument val3 = QGenericArgument(), QGenericArgument val4 = QGenericArgument(), QGenericArgument val5 = QGenericArgument(), QGenericArgument val6 = QGenericArgument(), QGenericArgument val7 = QGenericArgument(), QGenericArgument val8 = QGenericArgument(), QGenericArgument val9 = QGenericArgument()) const</a></p>
     */
    @io.qt.QtUninvokable
    public final Object invokeOnGadget(Object object, Object... args) throws IllegalArgumentException, io.qt.QUnsuccessfulInvocationException {
        if(!isValid())
            throw new io.qt.QUnsuccessfulInvocationException("Cannot invoke an invalid QMetaMethod.");
        if(parameterCount() != args.length) {
            throw new IllegalArgumentException(String.format("Wrong number of arguments. expected: %2$s, given: %1$s", args.length, parameterCount()));
        }
        {
            QMetaObject enclosingMetaObject = enclosingMetaObject();
            QMetaObject objectMO = QMetaObject.forGadget(object);
            if(objectMO!=enclosingMetaObject && !objectMO.inherits(enclosingMetaObject)) {
                throw new IllegalArgumentException(String.format("Given gadget is not an instance of %1$s", enclosingMetaObject.className()));
            }
        }
        Class<?>[] argClassTypes = new Class<?>[1+parameterCount()];
        Class<?> returnType = returnClassType();
        argClassTypes[0] = returnType==null ? void.class : returnType;
        java.util.List<Class<?>> parameterClassTypes = parameterClassTypes();
        for(int i=0; i<parameterClassTypes.size(); ++i) {
            argClassTypes[i+1] = parameterClassTypes.get(i);
        }
        return invoke_native(object, argClassTypes, args);
    }
    
    @io.qt.QtUninvokable
    private native Object invoke_native(Object object, Class<?>[] argClassTypes, Object[] args);
    
    /**
     * Returns this meta method as Java reflection method.
     */
    @io.qt.QtUninvokable
    public final java.lang.reflect.Method toReflectedMethod() {
        if(isValid()) {
            switch(methodType()) {
            case Method:
            case Slot:
                java.lang.reflect.AccessibleObject reflected = toReflected();
                if(reflected instanceof java.lang.reflect.Method){
                    return (java.lang.reflect.Method)reflected;
                }
                try {
                    java.util.List<Class<?>> parameterTypes = parameterClassTypes();
                    return this.enclosingMetaObject().type().getDeclaredMethod(name().toString(), parameterTypes.toArray(new Class<?>[parameterTypes.size()]));
                } catch (NoSuchMethodException | SecurityException e) {
                }
                break;
            default:
                throw new IllegalArgumentException("Method " + this + " is a " + methodType().name().toLowerCase() + ".");
            }
        }
        return null;
    }
    
    @io.qt.QtUninvokable
    private native java.lang.reflect.AccessibleObject toReflected();
    
    /**
     * Returns this meta method as Java reflection constructor.
     */
    @io.qt.QtUninvokable
    public final java.lang.reflect.Constructor<?> toReflectedConstructor() {
        if(isValid()) {
            if(methodType()==MethodType.Constructor) {
                java.lang.reflect.AccessibleObject reflected = toReflected();
                if(reflected instanceof java.lang.reflect.Constructor<?>){
                    return (java.lang.reflect.Constructor<?>)reflected;
                }
                try {
                    java.util.List<Class<?>> parameterTypes = parameterClassTypes();
                    return this.enclosingMetaObject().type().getDeclaredConstructor(parameterTypes.toArray(new Class<?>[parameterTypes.size()]));
                } catch (NoSuchMethodException | SecurityException e) {
                }
            }else {
                throw new IllegalArgumentException("Method " + this + " is not a constructor.");
            }
        }
        return null;
    }
    
    /**
     * Returns this meta method as signal object.
     */
    @io.qt.QtUninvokable
    public final QMetaObject.AbstractSignal toSignal(QObject sender) {
        if(isValid()) {
            if(methodType()==MethodType.Signal) {
                QtJambi_LibraryUtilities.internal.checkedNativeId(java.util.Objects.requireNonNull(sender));
                return findSignal(sender);
            }else {
                throw new IllegalArgumentException("Method " + this + " is not a signal.");
            }
        }
        return null;
    }

    private QMetaObject.AbstractSignal findSignal(QObject sender) {
        int methodIndex = methodIndex();
        Class<?> cls = enclosingMetaObject().type();
        if(cls==null)
            cls = QtJambi_LibraryUtilities.internal.getClass(sender);
        if(cls!=null) {
            String name = name().toString();
            while(cls!=null && cls!=Object.class) {
                try {
                    java.lang.reflect.Field f = cls.getDeclaredField(name);
                    if (!java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                        if (QMetaObject.AbstractSignal.class.isAssignableFrom(f.getType())) {
                                try {
                                    QMetaObject.AbstractSignal signal = (QMetaObject.AbstractSignal)QtJambi_LibraryUtilities.internal.readField(sender, f);
                                    if (signal != null) {
                                        if(signal.methodIndex()==methodIndex)
                                            return signal;
                                        if(name.equals(signal.name()) && signal.argumentTypes().equals(parameterClassTypes())) {
                                            return signal;
                                        }
                                    }
                                }catch(Throwable t) {}
                        } else if (QMetaObject.AbstractMultiSignal.class.isAssignableFrom(f.getType())) {
                            try {
                                QMetaObject.AbstractMultiSignal multiSignal = (QMetaObject.AbstractMultiSignal) QtJambi_LibraryUtilities.internal.readField(sender, f);
                                for (QMetaObject.AbstractSignal signal : multiSignal.allSignals()) {
                                    if (signal.methodIndex()==methodIndex) {
                                        return signal;
                                    }
                                }
                            }catch(Throwable t) {}
                        }
                    }
                }catch(Throwable t) {}
                cls = cls.getSuperclass();
            }
        }
        return getExtraSignal(QtJambi_LibraryUtilities.internal.nativeId(sender), QtJambi_LibraryUtilities.internal.nativeId(this));
    }

    private native static QMetaObject.AbstractSignal getExtraSignal(long sender__id, long method__id);


    /**
     * Returns corresponding meta method for given signal object.
     */
    @io.qt.QtUninvokable
    public static QMetaMethod fromSignal(QMetaObject.Signal signal) {
        QMetaMethod method = null;
        io.qt.QtSignalEmitterInterface containingObject = signal.containingObject();
        if(containingObject instanceof QObject) {
            if(signal.methodIndex()>=0) {
                method = ((QObject)containingObject).metaObject().method(signal.methodIndex());
            }else{
                java.util.List<Class<?>> signalTypeClasses = signal.argumentTypes();
                method = ((QObject)containingObject).metaObject().method(signal.name(), signalTypeClasses.toArray(new Class[signalTypeClasses.size()]));
            }
        }
        return method;
    }
    
    /**
     * Returns corresponding meta method for given reflection constructor.
     */
    public static QMetaMethod fromReflectedConstructor(java.lang.reflect.Constructor<?> constructor) {
        QMetaObject mo = QMetaObject.forType(constructor.getDeclaringClass());
        if(mo!=null) {
            return mo.constructor(constructor.getParameterTypes());
        }
        return null;
    }
    
    /**
     * Returns corresponding meta method for given reflection method.
     */
    public static QMetaMethod fromReflectedMethod(java.lang.reflect.Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        QMetaMethod qmethod = null;
        QMetaObject mo = QMetaObject.forType(declaringClass);
        if(mo!=null) {
            Object[] ok = {method};
            qmethod = methodFromMethod(mo.metaObjectPointer, ok);
            if(qmethod==null && ok[0]==null) {
                qmethod = mo.method(method.getName(), method.getParameterTypes());
                if(qmethod!=null && qmethod.methodType()==MethodType.Signal) {
                    qmethod = null;
                }
            }
        }
        return qmethod;
    }
    
    private static native QMetaMethod methodFromMethod(long metaObjectPointer, Object[] method);
    
    private static QMetaMethod fromMethodImpl(QMetaObject.AbstractSlot method) {
        io.qt.internal.ClassAnalyzerUtility.LambdaInfo info = io.qt.internal.ClassAnalyzerUtility.lambdaInfo(method);
        if(info!=null && info.lambdaArgs.isEmpty()) {
            if(info.owner instanceof QMetaObject.Signal && info.reflectiveMethod.getName().equals("emit"))
                return fromSignal((QMetaObject.Signal)info.owner);
            if(info.reflectiveMethod!=null)
                return fromReflectedMethod(info.reflectiveMethod);
            if(info.reflectiveConstructor!=null)
                return fromReflectedConstructor(info.reflectiveConstructor);
        }
        return null;
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <R> QMetaMethod fromMethod(QMetaObject.Method0<R> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,R> QMetaMethod fromMethod(QMetaObject.Method1<A,R> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,R> QMetaMethod fromMethod(QMetaObject.Method2<A,B,R> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,R> QMetaMethod fromMethod(QMetaObject.Method3<A,B,C,R> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D,R> QMetaMethod fromMethod(QMetaObject.Method4<A,B,C,D,R> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D,E,R> QMetaMethod fromMethod(QMetaObject.Method5<A,B,C,D,E,R> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D,E,F,R> QMetaMethod fromMethod(QMetaObject.Method6<A,B,C,D,E,F,R> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D,E,F,G,R> QMetaMethod fromMethod(QMetaObject.Method7<A,B,C,D,E,F,G,R> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D,E,F,G,H,R> QMetaMethod fromMethod(QMetaObject.Method8<A,B,C,D,E,F,G,H,R> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D,E,F,G,H,I,R> QMetaMethod fromMethod(QMetaObject.Method9<A,B,C,D,E,F,G,H,I,R> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static QMetaMethod fromMethod(QMetaObject.Slot0 method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A> QMetaMethod fromMethod(QMetaObject.Slot1<A> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B> QMetaMethod fromMethod(QMetaObject.Slot2<A,B> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C> QMetaMethod fromMethod(QMetaObject.Slot3<A,B,C> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D> QMetaMethod fromMethod(QMetaObject.Slot4<A,B,C,D> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D,E> QMetaMethod fromMethod(QMetaObject.Slot5<A,B,C,D,E> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D,E,F> QMetaMethod fromMethod(QMetaObject.Slot6<A,B,C,D,E,F> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D,E,F,G> QMetaMethod fromMethod(QMetaObject.Slot7<A,B,C,D,E,F,G> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D,E,F,G,H> QMetaMethod fromMethod(QMetaObject.Slot8<A,B,C,D,E,F,G,H> method) {
        return fromMethodImpl(method);
    }
    
    /**
     * Returns corresponding meta method for given method handle.
     */
    public static <A,B,C,D,E,F,G,H,I> QMetaMethod fromMethod(QMetaObject.Slot9<A,B,C,D,E,F,G,H,I> method) {
        return fromMethodImpl(method);
    }
}// class

class QMetaProperty___{
    
    /**
     * Returns the property notifier as signal object.
     */
    @io.qt.QtUninvokable
    public final QMetaObject.AbstractSignal notifySignal(QObject object) {
        QMetaMethod notifySignal = notifySignal();
        return notifySignal==null ? null : notifySignal.toSignal(object);
    }
    
    /**
     * <p>See <a href="@docRoot/qmetaproperty.html#resetOnGadget">QMetaProperty::resetOnGadget(const void *) const</a></p>
     */
    @io.qt.QtUninvokable
    public native final boolean resetOnGadget(Object object);
    
    /**
     * <p>See <a href="@docRoot/qmetaproperty.html#readOnGadget">QMetaProperty::readOnGadget(const void *) const</a></p>
     */
    @io.qt.QtUninvokable
    public native final Object readOnGadget(Object object);
    
    /**
     * <p>See <a href="@docRoot/qmetaproperty.html#writeOnGadget">QMetaProperty::writeOnGadget(void *, const QVariant &) const</a></p>
     */
    @io.qt.QtUninvokable
    public native final boolean writeOnGadget(Object object, Object value);
    
    @Override
    @io.qt.QtUninvokable
    public final String toString() {
        return name();
    }
    
    private Class<?> classType;
    
    /**
     * <p>See <a href="@docRoot/qmetaproperty.html#metaType">QMetaProperty::metaType() const</a></p>
     */
    @io.qt.QtUninvokable
    public final Class<?> classType(){
        if(classType==null)
            classType = classType_native();
        if(classType==null)
            classType = QMetaType.javaType(this.userType());
        return classType;
    }
    
    @io.qt.QtUninvokable
    private native final Class<?> classType_native();
}// class

class QMetaEnum___{
    
    @Override
    @io.qt.QtUninvokable
    public final String toString() {
        return name();
    }

    /**
     * <p>See <a href="@docRoot/qmetaenum.html#keysToValue">QMetaEnum::keysToValue(const char *) const</a></p>
     */
    @io.qt.QtUninvokable
    public Integer keysToValue(String... keys) {
        return keysToValue(new QStringList(keys).join('|'));
    }
    
    /**
     * <p>See <a href="@docRoot/qmetaenum.html#keysToValue">QMetaEnum::keysToValue(const char *) const</a></p>
     */
    @io.qt.QtUninvokable
    public Integer keysToValue(java.util.Collection<String> keys) {
        return keysToValue((keys instanceof QStringList ? (QStringList)keys : new QStringList(keys)).join('|'));
    }

    /**
     * Returns all enum entries of this enum type.
     */
    @io.qt.QtUninvokable
    public io.qt.QtAbstractEnumerator[] entries() {
        return enclosingMetaObject().enumEntries(this);
    }
    
    /**
     * Returns the enum entry of the given value.
     */
    @io.qt.QtUninvokable
    public io.qt.QtAbstractEnumerator entry(int index) {
        return enclosingMetaObject().enumEntry(this, index);
    }
    
    /**
     * Returns the enum entry of the given name.
     */
    @io.qt.QtUninvokable
    public io.qt.QtAbstractEnumerator entry(String name) {
        return enclosingMetaObject().enumEntry(this, name);
    }
    
    /**
     * Returns the java class of this enum type.
     */
    @io.qt.QtUninvokable
    public Class<?> type() {
        return enclosingMetaObject().enumType(this);
    }
    
    @io.qt.QtUninvokable
    @io.qt.NativeAccess
    private static Class<?> flagsType(Class<?> enumType){
        try {
            Class<?> result = enumType.getMethod("asFlags").getReturnType();
            if(result==io.qt.QFlags.class && enumType.getDeclaringClass()!=null) {
                for(Class<?> cls : enumType.getDeclaringClass().getDeclaredClasses()) {
                    if(io.qt.QFlags.class.isAssignableFrom(cls)) {
                        if(cls.getGenericSuperclass() instanceof java.lang.reflect.ParameterizedType) {
                            java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType)cls.getGenericSuperclass();
                            if(pt.getRawType()==io.qt.QFlags.class) {
                                java.lang.reflect.Type[] types = pt.getActualTypeArguments();
                                if(types.length==1 && types[0]==enumType)
                                    return cls;
                            }
                        }
                    }
                }
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Returns flags for the given value.
     */
    @io.qt.QtUninvokable
    public io.qt.QFlags<?> flags(int value) {
        Class<?> enumType = type();
        if(io.qt.QtFlagEnumerator.class.isAssignableFrom(enumType)) {
            Class<?> flagsType = flagsType(enumType);
            if(flagsType!=null)
                return flags(flagsType, value);
        }else if(io.qt.QFlags.class.isAssignableFrom(enumType)) {
            return flags(enumType, value);
        }
        return null;
    }
    
    /**
     * Returns flags for the given enum names.
     */
    @io.qt.QtUninvokable
    public io.qt.QFlags<?> flags(String... names) {
        @SuppressWarnings("unchecked")
        io.qt.QFlags<io.qt.QtFlagEnumerator> flags = (io.qt.QFlags<io.qt.QtFlagEnumerator>)flags(0);
        for (String name : names) {
            io.qt.QtAbstractEnumerator entry = entry(name);
            if(entry instanceof io.qt.QtFlagEnumerator) {
                flags.set((io.qt.QtFlagEnumerator)entry);
            }else {
                throw new io.qt.QNoSuchEnumValueException(name);
            }
        }
        return flags;
    }
    
    @io.qt.QtUninvokable
    private static native io.qt.QFlags<? extends io.qt.QtFlagEnumerator> flags(Class<?> cls, int value);
    
    /**
     * Returns the enum entry of the given value.
     */
    @io.qt.QtUninvokable
    public io.qt.QtAbstractEnumerator resolve(int value) {
        Class<?> type = type();
        if(type.isEnum()) {
            byte bitSize = 4;
            if(io.qt.QtByteEnumerator.class.isAssignableFrom(type)) {
                bitSize = 1;
            }else if(io.qt.QtShortEnumerator.class.isAssignableFrom(type)) {
                bitSize = 2;
            }else if(io.qt.QtLongEnumerator.class.isAssignableFrom(type)) {
                bitSize = 8;
            }
            return resolveEntry(type, value, bitSize);
        }else {
            return enclosingMetaObject().getEnumEntry(this, value);
        }
    }
    
    @io.qt.QtUninvokable
    private static native io.qt.QtAbstractEnumerator resolveEntry(Class<?> cls, int value, byte bitSize);

}// class

class Qt___ extends Qt {
    
    public final static QMetaObject staticMetaObject = QMetaObject.forType(Qt.class);
    
}// class

class QTimeZone___{
    // No known zone > 12 hrs West of Greenwich (Baker Island, USA)
    public static final int MinUtcOffsetSecs = -14 * 3600;
    // No known zone > 14 hrs East of Greenwich (Kiritimati, Christmas Island, Kiribati)
    public static final int MaxUtcOffsetSecs = +14 * 3600;
}// class

class QCollatorSortKey___{
    
    @io.qt.QtUninvokable
    @Override
    public final int compareTo(io.qt.core.QCollatorSortKey key){
        return compare(key);
    }
    
}// class

class QCborValue_java__{
    
    /**
     * Result class for {@link #fromCbor(io.qt.core.QByteArray)}
     */
    public static final class FromCborResult{
        private FromCborResult(io.qt.core.QCborValue value, io.qt.core.QCborParserError error) {
            super();
            this.value = value;
            this.error = error;
        }
        public final io.qt.core.QCborValue value;
        public final io.qt.core.QCborParserError error;
    }
}// class

class QCborStreamReader_java__{
    
    /**
     * <p>See <a href="@docRoot/qcborstreamreader.html#QCborStreamReader-1">QCborStreamReader::QCborStreamReader(const char*,qsizetype)</a></p>
     */
    public QCborStreamReader(byte[] data){
        this(new io.qt.core.QByteArray(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qcborstreamreader.html#addData">QCborStreamReader::addData(QByteArray)</a></p>
     */
    @io.qt.QtUninvokable
    public final void addData(byte[] data){
        addData(new io.qt.core.QByteArray(data));
    }
}// class

class QFactoryLoader_62_{
    private QJsonValue getIID(int index){
        return metaData().get(index).value("IID");
    }
}// class

class QFactoryLoader_63_{
    private QJsonValue getIID(int index){
        return metaData().get(index).toCbor().value(QtPluginMetaDataKeys.IID.value()).toJsonValue();
    }
}// class

class QFactoryLoader__{

    public static void registerFactory(Class<? extends QtObjectInterface> factoryClass){
        Method createMethod = null;
        for(Method method : factoryClass.getDeclaredMethods()) {
            if(!Modifier.isStatic(method.getModifiers())
                    && Modifier.isPublic(method.getModifiers())
                    && method.getReturnType()!=void.class) {
                createMethod = method;
                break;
            }
        }
        if(createMethod==null) {
            throw new IllegalArgumentException("Missing factory method in interface "+factoryClass.getName());
        }
        QPluginLoader.qRegisterPluginInterface(factoryClass);
    }
    
    public <P extends QtObjectInterface, R> R loadPlugin(QMetaObject.Method2<P, String, R> create, String key) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, R> R loadPlugin(QMetaObject.Method3<P, String, A, R> create, String key, A a) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, R> R loadPlugin(QMetaObject.Method4<P, String, A, B, R> create, String key, A a, B b) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, C, R> R loadPlugin(QMetaObject.Method5<P, String, A, B, C, R> create, String key, A a, B b, C c) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b, c);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b, c);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, C, D, R> R loadPlugin(QMetaObject.Method6<P, String, A, B, C, D, R> create, String key, A a, B b, C c, D d) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b, c, d);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b, c, d);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, C, D, E, R> R loadPlugin(QMetaObject.Method7<P, String, A, B, C, D, E, R> create, String key, A a, B b, C c, D d, E e) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b, c, d, e);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b, c, d, e);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, C, D, E, F, R> R loadPlugin(QMetaObject.Method8<P, String, A, B, C, D, E, F, R> create, String key, A a, B b, C c, D d, E e, F f) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b, c, d, e, f);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b, c, d, e, f);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, C, D, E, F, G, R> R loadPlugin(QMetaObject.Method9<P, String, A, B, C, D, E, F, G, R> create, String key, A a, B b, C c, D d, E e, F f, G g) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b, c, d, e, f, g);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, key, a, b, c, d, e, f, g);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, R> R loadPlugin(QMetaObject.Method1<P, R> create, String key) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory);
                        } catch (Throwable e) {
                            Logger.getLogger(QFactoryLoader.class.getName()).throwing(QFactoryLoader.class.getName(), "loadPlugin", e);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, R> R loadPlugin(QMetaObject.Method2<P, A, R> create, String key, A a) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, R> R loadPlugin(QMetaObject.Method3<P, A, B, R> create, String key, A a, B b) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, C, R> R loadPlugin(QMetaObject.Method4<P, A, B, C, R> create, String key, A a, B b, C c) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, C, D, R> R loadPlugin(QMetaObject.Method5<P, A, B, C, D, R> create, String key, A a, B b, C c, D d) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c, d);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c, d);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, C, D, E, R> R loadPlugin(QMetaObject.Method6<P, A, B, C, D, E, R> create, String key, A a, B b, C c, D d, E e) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c, d, e);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c, d, e);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, C, D, E, F, R> R loadPlugin(QMetaObject.Method7<P, A, B, C, D, E, F, R> create, String key, A a, B b, C c, D d, E e, F f) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c, d, e, f);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c, d, e, f);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, C, D, E, F, G, R> R loadPlugin(QMetaObject.Method8<P, A, B, C, D, E, F, G, R> create, String key, A a, B b, C c, D d, E e, F f, G g) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c, d, e, f, g);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c, d, e, f, g);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public <P extends QtObjectInterface, A, B, C, D, E, F, G, H, R> R loadPlugin(QMetaObject.Method9<P, A, B, C, D, E, F, G, H, R> create, String key, A a, B b, C c, D d, E e, F f, G g, H h) {
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<P> factoryClass = io.qt.internal.ClassAnalyzerUtility.getFactoryClass(create);
                if(factoryClass!=null && factoryClass==registeredPluginInterface(iidValue.toString())) {
                    QObject factoryObject = instance(index);
                    P factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c, d, e, f, g, h);
                        } catch (RuntimeException | Error ex) {
                            throw ex;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else if(factoryClass==null && io.qt.core.QOperatingSystemVersion.current().isAnyOfType(io.qt.core.QOperatingSystemVersion.OSType.Android)) {
                    QObject factoryObject = instance(index);
                    @SuppressWarnings("unchecked")
                    P factory = (P)factoryObject;
                    if(factory!=null){
                        try {
                            return create.invoke(factory, a, b, c, d, e, f, g, h);
                        } catch (Throwable ex) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public QObject loadPlugin(String key, Object... args){
        return loadPlugin(QObject.class, key, args);
    }
    
    public <P extends QtObjectInterface> P loadPlugin(Class<P> pluginClass, String key, Object... args){
        int index = indexOf(key);
        if (index != -1) {
            QJsonValue iidValue = getIID(index);
            if(iidValue.isString()) {
                Class<? extends QtObjectInterface> factoryClass = registeredPluginInterface(iidValue.toString());
                if(factoryClass!=null) {
                    QObject factoryObject = instance(index);
                    QtObjectInterface factory = QMetaObject.cast(factoryClass, factoryObject);
                    if(factory!=null){
                        Method createMethod = null;
                        for(Method method : factoryClass.getDeclaredMethods()) {
                            Class<?>[] argClassTypes = method.getParameterTypes();
                            if(argClassTypes.length==args.length
                                    && !Modifier.isStatic(method.getModifiers())
                                    && Modifier.isPublic(method.getModifiers())
                                    && pluginClass==method.getReturnType()) {
                                createMethod = method;
                                for (int i = 0; i < method.getParameterCount(); i++) {
                                    Class<?> argClassType = argClassTypes[i];
                                    if(argClassType.isPrimitive()) {
                                        if(args[i]==null
                                                || (argClassType==int.class && !(args[i] instanceof Integer))
                                                || (argClassType==short.class && !(args[i] instanceof Short))
                                                || (argClassType==byte.class && !(args[i] instanceof Byte))
                                                || (argClassType==long.class && !(args[i] instanceof Long))
                                                || (argClassType==boolean.class && !(args[i] instanceof Boolean))
                                                || (argClassType==float.class && !(args[i] instanceof Float))
                                                || (argClassType==char.class && !(args[i] instanceof Character))
                                                || (argClassType==double.class && !(args[i] instanceof Double)) 
                                            ) {
                                            createMethod = null;
                                            break;
                                        }
                                    }else if(args[i]!=null && !argClassType.isInstance(args[i])){
                                        createMethod = null;
                                        break;
                                    }
                                }
                                if(createMethod!=null)
                                    break;
                            }else if(argClassTypes.length==args.length+1
                                    && argClassTypes[0]==String.class
                                    && !Modifier.isStatic(method.getModifiers())
                                    && Modifier.isPublic(method.getModifiers())
                                    && pluginClass==method.getReturnType()) {
                                createMethod = method;
                                for (int i = 0; i <= method.getParameterCount(); i++) {
                                    Class<?> argClassType = argClassTypes[i+1];
                                    if(argClassType.isPrimitive()) {
                                        if(args[i]==null
                                                || (argClassType==int.class && !(args[i] instanceof Integer))
                                                || (argClassType==short.class && !(args[i] instanceof Short))
                                                || (argClassType==byte.class && !(args[i] instanceof Byte))
                                                || (argClassType==long.class && !(args[i] instanceof Long))
                                                || (argClassType==boolean.class && !(args[i] instanceof Boolean))
                                                || (argClassType==float.class && !(args[i] instanceof Float))
                                                || (argClassType==char.class && !(args[i] instanceof Character))
                                                || (argClassType==double.class && !(args[i] instanceof Double)) 
                                            ) {
                                            createMethod = null;
                                            break;
                                        }
                                    }else if(args[i]!=null && !argClassType.isInstance(args[i])){
                                        createMethod = null;
                                        break;
                                    }
                                }
                                if(createMethod!=null)
                                    break;
                            }
                        }
                        if(createMethod!=null) {
                            try {
                                Object result;
                                if(createMethod.getParameterCount()==args.length+1) {
                                    Object[] _args = new Object[args.length+1];
                                    System.arraycopy(args, 0, _args, 1, args.length);
                                    _args[0] = key;
                                    result = QtJambi_LibraryUtilities.internal.invokeMethod(createMethod, factory, _args);
                                }else {
                                    result = QtJambi_LibraryUtilities.internal.invokeMethod(createMethod, factory, args);
                                }
                                if(pluginClass.isInterface() && result instanceof QObject) {
                                    return QMetaObject.cast(pluginClass, (QObject)result);
                                }else if(result!=null){
                                    return pluginClass.cast(result);
                                }
                            } catch (RuntimeException | Error ex) {
                                throw ex;
                            } catch (Throwable ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static native Class<? extends QtObjectInterface> registeredPluginInterface(String iid);
}// class

class QPluginLoader_java__{
    /**
     * @deprecated Use {@link #qRegisterStaticPluginFunction(QObject)} instead.
     */
    @Deprecated
    public static void registerStaticPluginFunction(QObject instance){
        qRegisterStaticPluginFunction(instance);
    }

    /**
     * @deprecated Use {@link #qRegisterStaticPluginFunction(QObject, QJsonObject)} instead.
     */
    @Deprecated
    public static void registerStaticPluginFunction(QObject instance, QJsonObject metaData){
        qRegisterStaticPluginFunction(instance, metaData);
    }

    /**
     * @deprecated Use {@link #qRegisterStaticPluginFunction(QObject, java.util.Map)} instead.
     */
    @Deprecated
    public static void registerStaticPluginFunction(QObject instance, java.util.Map<String, Object> metaData){
        qRegisterStaticPluginFunction(instance, metaData);
    }

    /**
     * @deprecated Use {@link #qRegisterStaticPluginFunction(Class)} instead.
     */
    @Deprecated
    public static void registerStaticPluginFunction(Class<? extends QObject> pluginClass){
        qRegisterStaticPluginFunction(pluginClass, (QJsonObject)null);
    }

    /**
     * @deprecated Use {@link #qRegisterStaticPluginFunction(Class, QJsonObject)} instead.
     */
    @Deprecated
    public static void registerStaticPluginFunction(Class<? extends QObject> pluginClass, QJsonObject metaData){
        qRegisterStaticPluginFunction(pluginClass, metaData);
    }

    /**
     * @deprecated Use {@link #qRegisterStaticPluginFunction(Class, java.util.Map)} instead.
     */
    @Deprecated
    public static void registerStaticPluginFunction(Class<? extends QObject> pluginClass, java.util.Map<String, Object> metaData){
        qRegisterStaticPluginFunction(pluginClass, metaData);
    }

    /**
     * @deprecated Use {@link #qRegisterPluginInterface(Class)} instead.
     */
    @Deprecated
    public static void registerPluginInterface(Class<? extends QtObjectInterface> factoryClass){
        qRegisterPluginInterface(factoryClass);
    }

    public static native void qRegisterPluginInterface(Class<? extends QtObjectInterface> iface);

    /**
     * <p>See <a href="https://doc.qt.io/qt/qpluginloader.html#instance">QPluginLoader::instance()</a></p>
     */
    @QtUninvokable
    public final <T extends QObject> T instance(Class<T> type){
        return QMetaObject.cast(type, instance());
    }

    private static class PluginClassLoader extends URLClassLoader {
        public PluginClassLoader() {
            super(new URL[0]);
        }

        protected void addURLs(Collection<URL> urls) {
            for (URL url : urls) {
                super.addURL(url);
                QResource.addClassPath(url);
            }
        }
    }
    private static final PluginClassLoader pluginClassLoader = new PluginClassLoader();

    /**
     * <p>See <a href="https://doc.qt.io/qt/qpluginloader.html#qRegisterStaticPluginFunction">qRegisterStaticPluginFunction(QStaticPlugin)</a></p>
     */
    public static void qRegisterStaticPluginFunction(QObject instance){
        qRegisterStaticPluginFunction(instance, (QJsonObject)null);
    }

    /**
     * <p>See <a href="https://doc.qt.io/qt/qpluginloader.html#qRegisterStaticPluginFunction">qRegisterStaticPluginFunction(QStaticPlugin)</a></p>
     */
    public static void qRegisterStaticPluginFunction(QObject instance, QJsonObject metaData) {
        if (metaData == null) {
                metaData = loadMetaDataFromClass(QtJambi_LibraryUtilities.internal.getClass(instance));
        }
        qRegisterStaticPluginFunctionInstance(instance, QtJambi_LibraryUtilities.internal.nativeId(metaData));
    }

    /**
     * <p>See <a href="https://doc.qt.io/qt/qpluginloader.html#qRegisterStaticPluginFunction">qRegisterStaticPluginFunction(QStaticPlugin)</a></p>
     */
    public static void qRegisterStaticPluginFunction(QObject instance, java.util.Map<String, Object> metaData){
        qRegisterStaticPluginFunction(instance, QJsonObject.fromVariantHash(metaData));
    }

    /**
     * <p>See <a href="https://doc.qt.io/qt/qpluginloader.html#qRegisterStaticPluginFunction">qRegisterStaticPluginFunction(QStaticPlugin)</a></p>
     */
    public static void qRegisterStaticPluginFunction(Class<? extends QObject> pluginClass){
        qRegisterStaticPluginFunction(pluginClass, (QJsonObject)null);
    }

    /**
     * <p>See <a href="https://doc.qt.io/qt/qpluginloader.html#qRegisterStaticPluginFunction">qRegisterStaticPluginFunction(QStaticPlugin)</a></p>
     */
    public static void qRegisterStaticPluginFunction(Class<? extends QObject> pluginClass, java.util.Map<String, Object> metaData){
        qRegisterStaticPluginFunction(pluginClass, QJsonObject.fromVariantHash(metaData));
    }

    /**
     * <p>See <a href="https://doc.qt.io/qt/qpluginloader.html#qRegisterStaticPluginFunction">qRegisterStaticPluginFunction(QStaticPlugin)</a></p>
     */
    public static void qRegisterStaticPluginFunction(Class<? extends QObject> pluginClass, QJsonObject metaData) {
        try {
            if (metaData == null) {
                metaData = loadMetaDataFromClass(pluginClass);
            }
            qRegisterStaticPluginFunctionClass(pluginClass, QtJambi_LibraryUtilities.internal.nativeId(metaData));
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to register plugin " + pluginClass.getName(), e);
        }
    }

    private static native void qRegisterStaticPluginFunctionClass(Class<? extends QObject> pluginClass, long metaData);

    private static native void qRegisterStaticPluginFunctionInstance(QObject instance, long metaData);

    private static native void qRegisterStaticPluginFunction(Supplier<Class<? extends QObject>> classSupplier, String className, String iid, long metaData, long pluginInfo);

    private static QJsonObject loadMetaDataFromClass(Class<? extends QObject> pluginClass) {
        QtPluginMetaData pluginMetaData = pluginClass.getAnnotation(QtPluginMetaData.class);
        if (pluginMetaData != null) {
            QJsonDocument.FromJsonResult result;
            if (!pluginMetaData.file().isEmpty()) {
                QFile pluginMetaDataFile = new QFile(":" + pluginClass.getPackage().getName().replace('.', '/') + "/" + pluginMetaData.file());
                if (pluginMetaDataFile.exists() && pluginMetaDataFile.open(QIODevice.OpenModeFlag.ReadOnly)) {
                    result = QJsonDocument.fromJson(pluginMetaDataFile.readAll());
                    if (result.document != null)
                        return result.document.object();
                } else {
                    try {
                        InputStream stream = pluginClass.getClassLoader().getResourceAsStream(pluginClass.getPackage().getName().replace('.', '/') + "/" + pluginMetaData.file());
                        if (stream != null) {
                            ByteArrayOutputStream bas = new ByteArrayOutputStream();
                            try {
                                byte[] buffer = new byte[1024];
                                int length = stream.read(buffer);
                                while (length > 0) {
                                    bas.write(buffer, 0, length);
                                    length = stream.read(buffer);
                                }
                            } finally {
                                stream.close();
                            }
                            result = QJsonDocument.fromJson(new QByteArray(bas.toByteArray()));
                            if (result.error == null || result.error.error() == QJsonParseError.ParseError.NoError)
                                return result.document.object();
                            else
                                throw new IOException(result.error.errorString());
                        }
                    } catch (IOException t) {
                        Logger.getLogger("internal").throwing(QPluginLoader.class.getName(), "loadMetaDataFromClass", t);
                    }
                }
            } else if (!pluginMetaData.json().isEmpty()) {
                result = QJsonDocument.fromJson(new QByteArray(pluginMetaData.json()));
                if (result.document != null)
                    return result.document.object();
            }
        }
        return new QJsonObject();
    }

    @NativeAccess
    private static QObject loadPluginInstance(String libPath, String className, String pluginName) throws Throwable {
        Class<?> foundClass = null;
        try {
            foundClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            List<String> classNameSplit = new ArrayList<>();
            classNameSplit.addAll(Arrays.asList(className.split("\\.")));
            StringBuilder name = new StringBuilder();
            while(!classNameSplit.isEmpty()) {
                if(name.length()!=0)
                    name.append('.');
                name.append(classNameSplit.remove(0));
                try {
                    Class.forName(name.toString());
                    while(!classNameSplit.isEmpty()) {
                        name.append('$');
                        name.append(classNameSplit.remove(0));
                    }
                    foundClass = Class.forName(name.toString());
                } catch (ClassNotFoundException e2) {}
            }
        }
        if(foundClass==null) {
            QFileInfo libFile = new QFileInfo(QDir.fromNativeSeparators(libPath));
            if (libFile.exists()) {
                List<URL> urls = new ArrayList<URL>();
                if(QOperatingSystemVersion.current().isAnyOfType(QOperatingSystemVersion.OSType.Android)) {
                    String fileName = libFile.fileName();
                    String suffix;
                    switch (QSysInfo.currentCpuArchitecture()) {
                    case "armeabi-v7a":
                    case "arm":
                        suffix = pluginName+"_armeabi-v7a.so";
                        break;
                    case "aarch64":
                    case "arm64-v8a":
                    case "arm64":
                        suffix = pluginName+"_arm64-v8a.so";
                        break;
                    case "x86":
                    case "i386":
                        suffix = pluginName+"_x86.so";
                        break;
                    default:
                        suffix = pluginName+"_x86_64.so";
                        break;
                    }
                    if(fileName.startsWith("lib") && fileName.endsWith(suffix)) {
                        fileName = fileName.substring(3, fileName.length()-suffix.length());
                        fileName = fileName.replace('_', '/');
                        URL url = QPluginLoader.class.getClassLoader().getResource(fileName + pluginName + ".jar");
                        if(url!=null)
                             urls.add(url);
                        else {
                            QDir subdir = new QDir(":/" + fileName + pluginName);
                            if (subdir.exists()) {
                                for (String jar : subdir.entryList(Arrays.asList("*.jar"), QDir.Filter.Files)) {
                                    url = QPluginLoader.class.getClassLoader().getResource(fileName + pluginName + jar);
                                    if(url!=null)
                                        urls.add(url);
                                }
                            }
                        }
                    }
                }else {
                    QDir dir = libFile.dir();
                    if (dir.exists(pluginName + ".jar")) {
                        QFileInfo jarFile = new QFileInfo(dir.filePath(pluginName + ".jar"));
                        if (jarFile.isFile()) {
                            urls.add(new File(QDir.toNativeSeparators(jarFile.absoluteFilePath())).toURI().toURL());
                        }
                    } else {
                        QDir subdir = new QDir(dir.filePath(pluginName));
                        if (subdir.exists()) {
                            for (String jar : subdir.entryList(Arrays.asList("*.jar"), QDir.Filter.Files)) {
                                urls.add(new File(QDir.toNativeSeparators(subdir.absoluteFilePath(jar))).toURI().toURL());
                            }
                            if (!urls.isEmpty()) {
                                System.setProperty("java.library.path", System.getProperty("java.library.path", "")
                                                + File.pathSeparatorChar + QDir.toNativeSeparators(subdir.absolutePath()));
                            }
                        }
                    }
                }
                if (!urls.isEmpty()) {
                    pluginClassLoader.addURLs(urls);
                    try{
                        foundClass = pluginClassLoader.loadClass(className);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(foundClass!=null) {
            try {
                QObject result = (QObject)QtJambi_LibraryUtilities.internal.invokeContructor(foundClass.getDeclaredConstructor());
                QtJambi_LibraryUtilities.internal.setCppOwnership(result);
                return result;
            } catch (Throwable e) {
                e.printStackTrace();
                throw e;
            }
        }
        return null;
    }
}// class

class QStaticPlugin_java__{
    /**
     * <p>See <a href="@docRoot/qstaticplugin.html#instance-var">QStaticPlugin::instance</a></p>
     */
    @io.qt.QtUninvokable
    public final native io.qt.core.QObject instance();
}// class

class QThread___{    
    static native void initialize();
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(ThreadGroup group, String name, long stackSize, io.qt.core.QObject parent) {
        super((QPrivateConstructor)null);        
        initialize_native(this, parent);
        initialize(group);
        if(name!=null)
            setName(name);
        if(stackSize>=0 && stackSize <= 0x0ffffffffL)
            setStackSize( (int)(stackSize & 0x0ffffffffL) );
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(Runnable)
     */
    public static QThread create(Runnable runnable) {
        return create(null, runnable, null, -1, null);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(Runnable)
     */
    public static QThread create(Runnable runnable, io.qt.core.QObject parent) {
        return create(null, runnable, null, -1, parent);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public static QThread create(Runnable runnable, long stackSize) {
        return create(null, runnable, null, stackSize, null);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public static QThread create(Runnable runnable, long stackSize, io.qt.core.QObject parent) {
        return create(null, runnable, null, stackSize, parent);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public static QThread create(ThreadGroup group, Runnable runnable, long stackSize) {
        return create(group, runnable, null, stackSize, null);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public static QThread create(ThreadGroup group, Runnable runnable, long stackSize, io.qt.core.QObject parent) {
        return create(group, runnable, null, stackSize, parent);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(ThreadGroup, Runnable, String)
     */
    public static QThread create(ThreadGroup group, Runnable runnable, String name) {
        return create(group, runnable, name, -1, null);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(ThreadGroup, Runnable, String)
     */
    public static QThread create(ThreadGroup group, Runnable runnable, String name, io.qt.core.QObject parent) {
        return create(group, runnable, name, -1, parent);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public static QThread create(ThreadGroup group, Runnable runnable, String name, long stackSize) {
        return create(group, runnable, name, stackSize, null);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(Runnable, String, long)
     */
    public static QThread create(Runnable runnable, String name, long stackSize, io.qt.core.QObject parent) {
        return create(null, runnable, name, stackSize, parent);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(Runnable, String, long)
     */
    public static QThread create(Runnable runnable, String name, long stackSize) {
        return create(null, runnable, name, stackSize, null);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(Runnable, String)
     */
    public static QThread create(Runnable runnable, String name, io.qt.core.QObject parent) {
        return create(null, runnable, name, -1, parent);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(Runnable, String)
     */
    public static QThread create(Runnable runnable, String name) {
        return create(null, runnable, name, -1, null);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(ThreadGroup, Runnable)
     */
    public static QThread create(ThreadGroup group, Runnable runnable) {
        return create(group, runnable, null, -1, null);
    }
    
    /**
     * <p>See <a href="@docRoot/qthread.html#create">QThread::create(Function, Args...)</a></p>
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public static QThread create(ThreadGroup group, Runnable runnable, String name, long stackSize, io.qt.core.QObject parent) {
        return new Runner(group, runnable, name, stackSize, parent);
    }
    
    private final static class Runner extends QThread{
        Runner(ThreadGroup group, Runnable runnable, String name, long stackSize, QObject parent) {
            super(group, name, stackSize, parent);
            this.runnable = runnable;
        }
        private final Runnable runnable;
        
        @Override
        protected void run() {
            if(runnable!=null){
                runnable.run();
            }
        }
    }
    
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(String name) {
        this(null, name, -1, null);
    }
    
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(ThreadGroup group) {
        this(group, null, -1, null);
    }
    
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(String name, io.qt.core.QObject parent) {
        this(null, name, -1, parent);
    }
    
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(ThreadGroup group, io.qt.core.QObject parent) {
        this(group, null, -1, parent);
    }
    
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(String name, long stackSize) {
        this(null, name, stackSize, null);
    }
    
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(ThreadGroup group, long stackSize) {
        this(group, null, stackSize, null);
    }
    
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(String name, long stackSize, io.qt.core.QObject parent) {
        this(null, name, stackSize, parent);
    }
    
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(ThreadGroup group, long stackSize, io.qt.core.QObject parent) {
        this(group, null, stackSize, parent);
    }
    
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(ThreadGroup group, String name, long stackSize) {
        this(group, name, stackSize, null);
    }
    
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(ThreadGroup group, String name, io.qt.core.QObject parent) {
        this(group, name, -1, parent);
    }
    
    /**
     * @see Thread#Thread(ThreadGroup, Runnable, String, long)
     */
    public QThread(ThreadGroup group, String name) {
        this(group, name, -1, null);
    }
    
    private void initialize(ThreadGroup group) {
        if(group==null)
            group = Thread.currentThread().getThreadGroup();
        __qt_initialize(group);
    }
    
    /**
     * @see Thread#getThreadGroup()
     */
    public final ThreadGroup getThreadGroup() {
        if(javaThread!=null) {
            return javaThread.getThreadGroup();
        }
        return __qt_getThreadGroup();
    }
    
    private native ThreadGroup __qt_getThreadGroup();
    
    /**
     * @see Thread#setName(String)
     */
    public void setName(String name) {
        if(!isRunning() && javaThread==null)
            __qt_setName(name);
    }
    
    private native void __qt_setName(String name);
    
    /**
     * @see Thread#getName()
     */
    public final String getName() {
        if(javaThread!=null) {
            return javaThread.getName();
        }
        return __qt_getName();
    }
    
    private native String __qt_getName();
    
    /**
     * @see Thread#setDaemon(boolean)
     */
    public void setDaemon(boolean daemon) {
        if(!isRunning() && javaThread==null)
            __qt_setDaemon(daemon);
    }
    
    private native void __qt_setDaemon(boolean daemon);
    
    /**
     * @see Thread#isDaemon()
     */
    public final boolean isDaemon() {
        if(javaThread!=null) {
            return javaThread.isDaemon();
        }
        return __qt_isDaemon();
    }
    
    private native boolean __qt_isDaemon();
    
    /**
     * @see Thread#setUncaughtExceptionHandler(java.lang.Thread.UncaughtExceptionHandler)
     */
    public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        if(javaThread!=null) {
            javaThread.setUncaughtExceptionHandler(handler);
        }else {
            __qt_setUncaughtExceptionHandler(handler);
        }
    }
    
    private native void __qt_setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler);
    
    /**
     * @see Thread#getUncaughtExceptionHandler()
     */
    public final Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        if(javaThread!=null) {
            return javaThread.getUncaughtExceptionHandler();
        }
        return __qt_getUncaughtExceptionHandler();
    }
    
    private native Thread.UncaughtExceptionHandler __qt_getUncaughtExceptionHandler();
    
    /**
     * @see Thread#setContextClassLoader(ClassLoader)
     */
    public void setContextClassLoader(ClassLoader cl) {
        if(javaThread!=null) {
            javaThread.setContextClassLoader(cl);
        }else {
            __qt_setContextClassLoader(cl);
        }
    }
    
    private native void __qt_setContextClassLoader(ClassLoader cl);
    
    /**
     * @see Thread#getContextClassLoader()
     */
    public final ClassLoader getContextClassLoader() {
        if(javaThread!=null) {
            return javaThread.getContextClassLoader();
        }
        return __qt_getContextClassLoader();
    }
    
    private native ClassLoader __qt_getContextClassLoader();
    
    private native void __qt_initialize(ThreadGroup group);
    private final Thread javaThread = null;
    
    public final Thread javaThread() { return javaThread==null ? __qt_javaThread() : javaThread; }
    
    private native Thread __qt_javaThread();
    public static native QThread thread(Thread thread);
    
    /**
     * @see Thread#isAlive()
     */
    public final boolean isAlive() {
        return isRunning();
    }
    
    /**
     * @see Thread#isInterrupted()
     */
    public final boolean isInterrupted() {
        if(javaThread!=null && javaThread.isInterrupted())
            return true;
        return isInterruptionRequested();
    }
    
    /**
     * @see Thread#interrupted()
     */
    public static boolean interrupted() {
        return Thread.interrupted();
    }
    
    /**
     * @see Thread#interrupt()
     */
    public final void interrupt() {
        requestInterruption();
    }
}// class

class QSharedMemory_java__{
    
    private static class CleanTask implements Runnable{
        private CleanTask(QSharedMemory sharedMemory) {
            super();
            this.sharedMemory = sharedMemory;
        }
        
        @Override
        public void run() {
            if(!isClosed) {
                isClosed = true;
                try{
                    if(!sharedMemory.isDisposed())
                        sharedMemory.unlock();
                }finally {
                    sharedMemory.__qt_isInUse = false;
                }
            }
        }
        private final QSharedMemory sharedMemory;
        private boolean isClosed = false;
    }
    
    public static class DataAccess implements AutoCloseable{
        
        private final QSharedMemory sharedMemory;
        private final io.qt.InternalAccess.Cleanable cleanable;
        
        DataAccess(QSharedMemory sharedMemory){
            this.sharedMemory = sharedMemory;
            cleanable = QtJambi_LibraryUtilities.internal.registerCleaner(this, new CleanTask(sharedMemory));
        }
        
        @Override
        public void close(){
            cleanable.clean();
        }
        
        @io.qt.QtUninvokable
        public java.nio.ByteBuffer data() {
            if(!sharedMemory.__qt_isInUse) {
                throw new IllegalStateException();
            }
            java.nio.ByteBuffer result = sharedMemory.data();
            if(sharedMemory.__qt_accessMode==AccessMode.ReadOnly){
                result = result.asReadOnlyBuffer();
            }
            return result;
        }
        
    }
    
    private AccessMode __qt_accessMode = AccessMode.ReadOnly;
    private boolean __qt_isInUse;
    
    public DataAccess access() throws IllegalStateException{
        if(this.isAttached() && !__qt_isInUse && lock()) {
            __qt_isInUse = true;
            return new DataAccess(this);
        }
        throw new IllegalStateException();
    }
    
}// class

class QSettings__{

}// class

class QModelRoleData___{
    
    @io.qt.QtUninvokable
    public native final void setData(Object data);
    
}// class

class QDir__{
    /**
     * <p>Overloaded function for {@link #entryInfoList(io.qt.core.QDir.Filters, io.qt.core.QDir.SortFlags)}
     *  with <code>sort = new io.qt.core.QDir.SortFlags(-1)</code>.</p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QList<io.qt.core.QFileInfo> entryInfoList(io.qt.core.QDir.Filter... filters) {
        return entryInfoList(new io.qt.core.QDir.Filters(filters), new io.qt.core.QDir.SortFlags(-1));
    }
    
    /**
     * <p>Overloaded function for {@link #entryList(io.qt.core.QDir.Filters, io.qt.core.QDir.SortFlags)}
     *  with <code>sort = new io.qt.core.QDir.SortFlags(-1)</code>.</p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QList<java.lang.String> entryList(io.qt.core.QDir.Filter... filters) {
        return entryList(new io.qt.core.QDir.Filters(filters), new io.qt.core.QDir.SortFlags(-1));
    }
    
    /**
     * <p>Overloaded function for {@link #entryList(java.util.Collection, io.qt.core.QDir.Filters, io.qt.core.QDir.SortFlags)}
     *  with <code>sort = new io.qt.core.QDir.SortFlags(-1)</code>.</p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QList<java.lang.String> entryList(java.util.Collection<java.lang.String> nameFilters, io.qt.core.QDir.Filter... filters) {
        return entryList(nameFilters, new io.qt.core.QDir.Filters(filters), new io.qt.core.QDir.SortFlags(-1));
    }
    
    /**
     * <p>Overloaded function for {@link #entryInfoList(java.util.Collection, io.qt.core.QDir.Filters, io.qt.core.QDir.SortFlags)}
     *  with <code>sort = new io.qt.core.QDir.SortFlags(-1)</code>.</p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QList<io.qt.core.QFileInfo> entryInfoList(java.util.Collection<java.lang.String> nameFilters, io.qt.core.QDir.Filter... filters) {
        return entryInfoList(nameFilters, new io.qt.core.QDir.Filters(filters), new io.qt.core.QDir.SortFlags(-1));
    }
}// class

class QFile__{
    /**
     * Result class for {@link #moveToTrash(java.lang.String)}
     */
    public static class TrashResult{
        public final boolean success;
        public final String pathInTrash;
        private TrashResult(boolean success, String pathInTrash) {
            super();
            this.success = success;
            this.pathInTrash = pathInTrash;
        }
    }
}// class

class QUntypedPropertyBinding_java__{
    QUntypedPropertyBinding(Object functor){
        initialize_native(this, java.util.Objects.requireNonNull(functor));
    }
    private native static void initialize_native(QUntypedPropertyBinding instance, Object functor);
    
    QUntypedPropertyBinding(QPropertyBindingData bindingData) {
        initialize_native(this, bindingData);
    }
    private native static void initialize_native(QUntypedPropertyBinding instance, QPropertyBindingData bindingData);
    
    QUntypedPropertyBinding(QUntypedPropertyBinding other){
        super((QPrivateConstructor)null);
        initialize_native(this, other);
    }
    
    private native static void initialize_native(QUntypedPropertyBinding instance, QUntypedPropertyBinding other);
}// class

class QPropertyObserver_shell__{
public:
    QPropertyObserver_shell(QPropertyObserverBase::ChangeHandler);
}// class

class QPropertyObserver_native__{

QPropertyObserver_shell::QPropertyObserver_shell(QPropertyObserverBase::ChangeHandler changeHandler0)
    : QPropertyObserver(changeHandler0)
{
    QTJAMBI_DEBUG_METHOD_PRINT_WHERE("shell", "QPropertyObserver_shell::QPropertyObserver_shell(QPropertyObserverBase::ChangeHandler changeHandler0)", __shell())
    __shell()->constructed(typeid(QPropertyObserver));
}

void __qt_construct_QPropertyObserver_with_ChangeHandler(void* __qtjambi_ptr, void (*changeHandler)(QPropertyObserver*, QUntypedPropertyData *))
{
    new(__qtjambi_ptr) QPropertyObserver_shell(changeHandler);
}

size_t sizeof_QPropertyObserver_shell(){
    return sizeof(QPropertyObserver_shell);
}

}// class

class QUntypedBindable_java__{
    QUntypedBindable(io.qt.core.QUntypedPropertyData d, io.qt.core.QBindableInterface i){
        super((QPrivateConstructor)null);
        initialize_native(this, d, i);
    }
    
    private native static void initialize_native(QUntypedBindable instance, io.qt.core.QUntypedPropertyData d, io.qt.core.QBindableInterface i);
    
    /**
     * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
     * <p>The returned property change handler object keeps track of the registration. 
     * As long as the change handler is alive i.e. as long as a reference to the {@link QPropertyChangeHandler} instance exists, 
     * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
     * @param f
     * @return property change handler
     * @see QPropertyChangeHandler
     */
    @io.qt.QtUninvokable
    public final QPropertyChangeHandler onValueChanged(Runnable f)
    {
        QPropertyChangeHandler handler = new QPropertyChangeHandler(f);
        observe(handler);
        return handler;
    }

    /**
     * Subscribes the given functor f as a callback that is called immediately and whenever the value of the bindable changes in the future.
     * @param f
     * @return property change handler
     * @see QPropertyChangeHandler
     * @see #onValueChanged(Runnable)
     */
    @io.qt.QtUninvokable
    public final QPropertyChangeHandler subscribe(Runnable f)
    {
        f.run();
        return onValueChanged(f);
    }
    
    /**
     * <p>Registers the given functor f as a callback that shall be called whenever the value of the bindable changes.</p>
     * <p>The returned property notifier object keeps track of the registration. 
     * As long as the notifier is alive i.e. as long as a reference to the {@link QPropertyNotifier} instance exists, 
     * the callback remains installed. When the garbage collection deletes the instance, the callback is de-registered.</p>
     * @param f
     * @return property notifier
     * @see QPropertyNotifier
     */
    @io.qt.QtUninvokable
    public final QPropertyNotifier addNotifier(Runnable f)
    {
        QPropertyNotifier handler = new QPropertyNotifier(f);
        observe(handler);
        return handler;
    }
    
    @io.qt.QtUninvokable
    final io.qt.core.QUntypedPropertyBinding overrideBinding(io.qt.core.QUntypedPropertyBinding binding){
        return overrideBinding(this.iface(), this.data(), binding);
    }
    
    @io.qt.QtUninvokable
    private native io.qt.core.QUntypedPropertyBinding overrideBinding(io.qt.core.QBindableInterface iface, io.qt.core.QUntypedPropertyData data, io.qt.core.QUntypedPropertyBinding binding);
    
}// class

class QBindableInterface__{
    @io.qt.QtUninvokable
    final native QMetaType metaType();
}// class

class QLoggingCategory__{
    private QByteArray __rcCategory;
    /**
     * <p>Overloaded constructor for {@link #QLoggingCategory(java.lang.String, io.qt.core.QtMsgType)}
     *  with <code>severityLevel = io.qt.core.QtMsgType.QtDebugMsg</code>.</p>
     */
    public QLoggingCategory(java.lang.String category){
        this(category==null ? null : new QByteArray(category));
    }
    
    /**
     * <p>See <a href="@docRoot/qloggingcategory.html#QLoggingCategory-1">QLoggingCategory::QLoggingCategory(const char*,QtMsgType)</a></p>
     */
    public QLoggingCategory(java.lang.String category, io.qt.core.QtMsgType severityLevel){
        this(category==null ? null : new QByteArray(category), severityLevel);
    }
    
}// class

class QFunctionPointer__{
    /**
     * Overloaded function for <code>invoke(void.class, arguments)</code>.
     * @param arguments
     */
    @io.qt.QtDeclaredFinal
    @io.qt.QtUninvokable
    public void invoke(Object... arguments) throws io.qt.QUnsuccessfulInvocationException {
        invoke_native(this, void.class, arguments);
    }
    
    /**
     * Overloaded function for <code>invoke(QGenericReturnType.of(returnType), arguments)</code>.
     * @param returnType
     * @param arguments
     */
    @io.qt.QtDeclaredFinal
    @io.qt.QtUninvokable
    public <R> R invoke(Class<R> returnType, Object... arguments) throws io.qt.QUnsuccessfulInvocationException {
        return invoke_native(this, returnType, arguments);
    }
    
    /**
     * <p>Invokes the underlying function pointer with given arguments.</p>
     * <p>Object types are used as pointers, value types as call-by-value. This also applies for the return value.</p>
     * <p>Use {@link QGenericArgument} to specify call-by-reference.
     * E.g. <code>QGenericArgument.value("any string").asConstRef()</code>
     * for <code>const QString&amp;</code></p>
     * <p>Requires Java Native Access library (JNA) in class path.</p>
     * @see https://github.com/java-native-access/jna
     * @param returnType
     * @param arguments argument values or wrapped as QGenericArgument
     */
    @io.qt.QtUninvokable
    public <R> R invoke(QGenericReturnType<R> returnType, Object... arguments) throws io.qt.QUnsuccessfulInvocationException {
        if(arguments==null) {
            return invoke_native(this, returnType, null);
        }else {
            return invoke_native(this, returnType, arguments);
        }
    }
    
    /**
     * Converts this function pointer into given functional interface type.
     * @param functionalInterface
     * @return converted function pointer
     */
    @io.qt.QtUninvokable
    public <T extends io.qt.QtObjectInterface> T cast(Class<T> functionalInterface) {
        return cast(this, functionalInterface);
    }
    
    /**
     * Converts the given function into given functional interface type.
     * @param functionalInterface
     * @return converted function pointer
     */
    @io.qt.QtUninvokable
    public native static <T extends io.qt.QtObjectInterface> T cast(io.qt.QtObjectInterface function, Class<T> functionalInterface);
    
    @io.qt.QtUninvokable
    static native <R> R invoke_native(io.qt.QtObjectInterface function, Object returnType, Object[] arguments);
}// class

class QResource__{
    private static class CoreUtility extends io.qt.internal.CoreUtility{
        protected static void addClassPath(String path) {
            io.qt.internal.CoreUtility.addClassPath(path);
        }

        protected static void removeClassPath(String path) {
            io.qt.internal.CoreUtility.removeClassPath(path);
        }

        protected static void addClassPath(java.net.URL path) {
            io.qt.internal.CoreUtility.addClassPath(path);
        }
    }

    /**
     * Adds <code>path</code> to the set of classpaths in which QtJambi should search for resources.
     */
    public static void addClassPath(String path) {
        CoreUtility.addClassPath(path);
    }
    
    /**
     * Removes <code>path</code> from the set of classpaths in which QtJambi searches
     * for resources.
     */
    public static void removeClassPath(String path) {
        CoreUtility.removeClassPath(path);
    }

    static void addClassPath(java.net.URL url) {
        CoreUtility.addClassPath(url);
    }
    
}// class

class QCryptographicHash___{
    /**
     * <p>See <a href="@docRoot/qcryptographichash.html#addData-1">QCryptographicHash::addData(const char*)</a></p>
     */
    @io.qt.QtUninvokable
    public final void addData(java.nio.ByteBuffer data){
        addData(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qcryptographichash.html#hash">QCryptographicHash::hash(const char*,QCryptographicHash::Algorithm)</a></p>
     */
    public static io.qt.core.QByteArray hash(java.nio.ByteBuffer data, io.qt.core.QCryptographicHash.Algorithm method){
        return hash(new io.qt.core.QByteArrayView(data), method);
    }
    
    /**
     * <p>See <a href="@docRoot/qcryptographichash.html#addData-1">QCryptographicHash::addData(QByteArray)</a></p>
     */
    @io.qt.QtUninvokable
    public final void addData(io.qt.core.QByteArray data){
        addData(new io.qt.core.QByteArrayView(data));
    }
    
    /**
     * <p>See <a href="@docRoot/qcryptographichash.html#hash">QCryptographicHash::hash(QByteArray,QCryptographicHash::Algorithm)</a></p>
     */
    public static io.qt.core.QByteArray hash(io.qt.core.QByteArray data, io.qt.core.QCryptographicHash.Algorithm method){
        return hash(new io.qt.core.QByteArrayView(data), method);
    }
}// class

class QDataStream_5__{
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    public final <T> QDataStream writeObject(T object){
        int metaType = QMetaType.fromObject(object).id();
        if(!QMetaType.save(this, metaType, object)){
            this.setStatus(Status.WriteFailed);
            QLogging.qWarning("Unable to write object of type %1$s", object==null ? "null" : QtJambi_LibraryUtilities.internal.getClass(object).getTypeName());
        }
        return this;
    }
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-gt-gt-1">QDataStream::operator&gt;&gt;</a></p>
     */
    @SuppressWarnings("unchecked")
    public final <T> T readObject(Class<T> cl, QMetaType...instantiations){
        int metaType = QMetaType.fromType(cl, instantiations).id();
        java.util.Optional<Object> optional = QMetaType.load(this, metaType);
        if(!optional.isPresent()){
            setStatus(Status.ReadCorruptData);
            QLogging.qWarning("Unable to read object of type %1$s", QMetaType.typeName(metaType));
        }
        return (T)optional.orElse(null);
    }
}// class

class QDataStream_6__{
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    public final <T> QDataStream writeObject(T object){
        QMetaType metaType = QMetaType.fromObject(object);
        if(!metaType.save(this, object)){
            this.setStatus(Status.WriteFailed);
            QLogging.qWarning("Unable to write object of type %1$s", object==null ? "null" : QtJambi_LibraryUtilities.internal.getClass(object).getTypeName());
        }
        return this;
    }
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-gt-gt-1">QDataStream::operator&gt;&gt;</a></p>
     */
    @SuppressWarnings("unchecked")
    public final <T> T readObject(Class<T> cl, QMetaType...instantiations){
        QMetaType metaType = QMetaType.fromType(cl, instantiations);
        java.util.Optional<Object> optional = metaType.load(this);
        if(!optional.isPresent()){
            setStatus(Status.ReadCorruptData);
            QLogging.qWarning("Unable to read object of type %1$s", metaType.name());
        }
        return (T)optional.orElse(null);
    }
}// class

class QDataStream___{
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final <T> QDataStream append(T t){
        return writeObject(t);
    }
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QDataStream append(java.lang.String s){
        return writeString(s);
    }
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QDataStream append(double v){
        return writeDouble(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QDataStream append(byte v){
        return writeByte(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QDataStream append(byte[] v){
        writeBytes(v);
        return this;
    }
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QDataStream append(char v){
        return writeChar(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QDataStream append(float v){
        return writeFloat(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QDataStream append(int v){
        return writeInt(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QDataStream append(long v){
        return writeLong(v);
    }
    
    /**
     * <p>See <a href="@docRoot/qdatastream.html#operator-lt-lt-1">QDataStream::operator&lt;&lt;</a></p>
     */
    @io.qt.QtUninvokable
    public final QDataStream append(short v){
        return writeShort(v);
    }
}// class

class QString__{
    /**
     * {@inheritDoc}
     */
    @Override
    @io.qt.QtUninvokable
    public final char charAt(int index) {
        return at(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @io.qt.QtUninvokable
    public final QString subSequence(int start, int end) {
        return mid(start, end-start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @io.qt.QtUninvokable
    public final QString append(CharSequence csq, int start, int end) throws java.io.IOException {
        return append(csq==null ? "null" : csq.subSequence(start, end));
    }
    
    /**
     * <p>See <a href="@docRoot/qstring.html#arg">QString::arg(Args &amp;&amp;... args) const</a></p>
     */
    @io.qt.QtUninvokable
    public final io.qt.core.QString arg(Object... args){
        QString _this = this.clone();
        for(Object arg : args) {
            if(arg instanceof Integer)
                _this = _this.arg((int)arg);
            else if(arg instanceof Character)
                _this = _this.arg((char)arg);
            else if(arg instanceof Double)
                _this = _this.arg((double)arg);
            else if(arg instanceof Byte)
                _this = _this.arg((byte)arg);
            else if(arg instanceof Short)
                _this = _this.arg((short)arg);
            else if(arg instanceof Long)
                _this = _this.arg((long)arg);
            else if(arg instanceof Float)
                _this = _this.arg((float)arg);
            else if(arg instanceof CharSequence)
                _this = _this.arg((CharSequence)arg);
            else if(arg!=null)
                _this = _this.arg(arg.toString());
            else
                _this = _this.arg("null");
        }
        return _this;
    }
    
    /**
     * Similar to {@link java.lang.String#format(String, Object...)} but using {@link QString#arg(Object...)}.
     * @param format format string
     * @param args arguments
     * @return formatted string
     */
    @io.qt.QtUninvokable
    public static io.qt.core.QString format(CharSequence format, Object... args){
        io.qt.core.QString strg;
        if(format instanceof io.qt.core.QString){
            strg = (io.qt.core.QString)format;
        }else{
            strg = new io.qt.core.QString(format);
        }
        return strg.arg(args);
    }
    
    /**
     * Static version of {@link QString#toUtf8()}
     * @param string
     * @return utf8
     */
    @io.qt.QtUninvokable
    public static native io.qt.core.QByteArray toUtf8(CharSequence string);

    /**
     * Static version of {@link QString#toLatin1()}
     * @param string
     * @return latin1
     */
    @io.qt.QtUninvokable
    public static native io.qt.core.QByteArray toLatin1(CharSequence string);

    /**
     * Static version of {@link QString#toLocal8Bit()}
     * @param string
     * @return local8Bit
     */
    @io.qt.QtUninvokable
    public static native io.qt.core.QByteArray toLocal8Bit(CharSequence string);
}// class

class QtGlobal_5_ {
    /**
     * <p>See <a href="@docRoot/qhash.html#qHash-28">qHash(T,0)</a></p>
     */
    public static <T> int qHash(T object) throws UnsupportedOperationException {
        return qHash(object, 0);
    }

    /**
     * <p>See <a href="@docRoot/qhash.html#qHash-28">qHash(T,int)</a></p>
     */
    public static native <T> int qHash(T object, int seed) throws UnsupportedOperationException;
}// class

class QtGlobal_6_ {
    /**
     * <p>See <a href="@docRoot/qhash.html#qHash-28">qHash(T,0)</a></p>
     */
    public static <T> long qHash(T object) throws UnsupportedOperationException {
        return qHash(object, 0);
    }

    /**
     * <p>See <a href="@docRoot/qhash.html#qHash-28">qHash(T,long)</a></p>
     */
    public static native <T> long qHash(T object, long seed) throws UnsupportedOperationException;

    /**
     * <p>See <a href="@docRoot/qhash.html#qHashMulti">qHashMulti(long,T...)</a></p>
     */
    public static native long qHashMulti(long seed, Object... objects) throws UnsupportedOperationException;

    /**
     * <p>See <a href="@docRoot/qhash.html#qHashMultiCommutative">qHashMultiCommutative(long,T...)</a></p>
     */
    public static native long qHashMultiCommutative(long seed, Object... objects) throws UnsupportedOperationException;
}// class
