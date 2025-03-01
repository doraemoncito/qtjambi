/****************************************************************************
**
** Copyright (C) 2009-2023 Dr. Peter Droste, Omix Visualization GmbH & Co. KG. All rights reserved.
**
** This file is part of QtJambi.
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

import QtJambiGenerator 1.0

TypeSystem{
    packageName: "io.qt.test"
    defaultSuperClass: "io.qt.QtObject"
    qtLibrary: "QtTest"
    module: "qtjambi.test"
    description: "Classes for unit testing Qt applications and libraries."
    Template{
        name: "test.stringsupplier.function"
        Text{content: "JObjectWrapper %in_wrapper(%env, %in);\n"+
                      "qxp::function_ref<const char*()> %out = [%in_wrapper]() -> const char* {\n"+
                      "                                            if(JniEnvironment env{200}){\n"+
                      "                                                jstring value = jstring(Java::Runtime::Supplier::get(env, %in_wrapper.object()));\n"+
                      "                                                jsize length = env->GetStringUTFLength(value);\n"+
                      "                                                char* c = new char[size_t(length)];\n"+
                      "                                                env->GetStringUTFRegion(value, 0, length, c);\n"+
                      "                                                return c;\n"+
                      "                                            }\n"+
                      "                                            return nullptr;\n"+
                      "                                        };"}
    }
    
    RequiredLibrary{
        name: "QtDBus"
        mode: RequiredLibrary.Optional
    }
    
    RequiredLibrary{
        name: "QtWidgets"
    }
    
    Rejection{
        className: "QTest"
        functionName: "compare_ptr_helper"
    }
    
    
    Rejection{
        className: "QTestData"
    }
    
    Rejection{
        className: "QSignalSpy"
        functionName: "initArgs"
    }
    
    
    Rejection{
        className: "QSignalSpy"
        functionName: "qt_metacall"
    }
    
    
    EnumType{
        name: "QTest::ComparisonOperation"
        since: [6, 4]
    }
    
    EnumType{
        name: "QTest::AttributeIndex"
    }
    
    EnumType{
        name: "QTest::KeyAction"
    }
    
    EnumType{
        name: "QTest::LogElementType"
    }
    
    EnumType{
        name: "QTest::MouseAction"
    }
    
    EnumType{
        name: "QTest::QBenchmarkMetric"
    }
    
    EnumType{
        name: "QTest::SkipMode"
    }
    
    EnumType{
        name: "QTest::TestFailMode"
    }
    
    NamespaceType{
        name: "QTest"
        ExtraIncludes{
            Include{
                fileName: "QTest"
                location: Include.Global
            }
            Include{
                fileName: "QtJambi/JObjectWrapper"
                location: Include.Global
            }
            Include{
                fileName: "QtJambi/CoreAPI"
                location: Include.Global
            }
            Include{
                fileName: "QtJambi/JavaAPI"
                location: Include.Global
            }
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "qRun()"
            noExcept: true
            blockExceptions: true
        }
        ModifyFunction{
            signature: "qExec(QObject *, QStringList)"
            noExcept: true
            blockExceptions: true
        }
        ModifyFunction{
            signature: "touchEvent(QWindow *, QTouchDevice *, bool)"
            remove: RemoveFlag.All
            until: 5
        }
        ModifyFunction{
            signature: "touchEvent(QWidget *, QTouchDevice *, bool)"
            remove: RemoveFlag.All
            until: 5
        }
        ModifyFunction{
            signature: "touchEvent(QWindow *, QPointingDevice *, bool)"
            remove: RemoveFlag.All
            since: 6
        }
        ModifyFunction{
            signature: "touchEvent(QWidget *, QPointingDevice *, bool)"
            remove: RemoveFlag.All
            since: 6
        }
        ModifyFunction{
            signature: "formatString(const char *, const char *, size_t)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "compare_helper(bool,const char *,const char *,int)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "compare_string_helper(const char *,const char *,const char *,const char *,const char *,int)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toHexRepresentation(const char*,int)"
            remove: RemoveFlag.All
            until: [6, 3]
        }
        ModifyFunction{
            signature: "toHexRepresentation(const char*,qsizetype)"
            remove: RemoveFlag.All
            since: [6, 4]
        }
        ModifyFunction{
            signature: "toString(const char*)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toString(const void*)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toString(const QObject*)"
            remove: RemoveFlag.All
            since: [6, 2]
        }
        ModifyFunction{
            signature: "toString(QSizePolicy)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toString(QGeoAreaMonitorInfo)"
            remove: RemoveFlag.All
            since: [6, 2]
        }
        ModifyFunction{
            signature: "toString(QGeoPositionInfo)"
            remove: RemoveFlag.All
            since: [6, 2]
        }
        ModifyFunction{
            signature: "toString(QGeoSatelliteInfo)"
            remove: RemoveFlag.All
            since: [6, 2]
        }
        ModifyFunction{
            signature: "toString(QSizePolicy::ControlType)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toString(QSizePolicy::Policy)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toString(QNetworkReply::NetworkError)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toString(QFlags<QSizePolicy::ControlType>)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toString(QList<QNetworkCookie>)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toString(QNetworkCookie)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toString(std::nullptr_t)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toString<T1,T2>(const QPair<T1, T2> &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "toPrettyCString(const char *, int)"
            remove: RemoveFlag.All
            until: [6, 3]
        }
        ModifyFunction{
            signature: "toPrettyCString(const char *, qsizetype)"
            remove: RemoveFlag.All
            since: [6, 4]
        }
        ModifyFunction{
            signature: "toPrettyUnicode(QStringView)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "qFindTestData(const char*, const char*, int, const char*)"
            remove: RemoveFlag.All
            until: 5
        }
        ModifyFunction{
            signature: "qFindTestData(const char*, const char*, int, const char*, const char*)"
            remove: RemoveFlag.All
            since: 6
        }
        ModifyFunction{
            signature: "qCompare(QStringView, QLatin1StringView, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
            since: [6, 4]
        }
        ModifyFunction{
            signature: "qCompare(QStringView, QLatin1String, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
            until: [6, 3]
        }
        ModifyFunction{
            signature: "qCompare(QString, QLatin1StringView, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
            since: [6, 4]
        }
        ModifyFunction{
            signature: "qCompare(QString, QLatin1String, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
            until: [6, 3]
        }
        ModifyFunction{
            signature: "qCompare(QString, QString, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "qCompare(QLatin1StringView, QStringView, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
            since: [6, 4]
        }
        ModifyFunction{
            signature: "qCompare(QLatin1String, QStringView, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
            until: [6, 3]
        }
        ModifyFunction{
            signature: "qCompare(QLatin1StringView, QString, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
            since: [6, 4]
        }
        ModifyFunction{
            signature: "qCompare(QLatin1String, QString, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
            until: [6, 3]
        }
        ModifyFunction{
            signature: "qCompare(char *, char *, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "qCompare(const char *, char *, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "qCompare(char *, const char *, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "qCompare(const char *, const char *, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "qCompare(qfloat16, qfloat16, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "qCompare(unsigned, unsigned, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "qCompare<T,N>(const QList<T> &, std::initializer_list<T>, const char *, const char *, const char *, int)"
            remove: RemoveFlag.All
            since: 6
        }
        ModifyFunction{
            signature: "failOnWarning(const char*)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            since: [6, 3]
        }
        ModifyFunction{
            signature: "qCaught(const char*,const char*,const char*,int)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            since: [6, 3]
        }
        ModifyFunction{
            signature: "createTouchDevice(QInputDevice::DeviceType,QFlags<QInputDevice::Capability>)"
            ModifyArgument{
                index: "return"
                DefineOwnership{
                    codeClass: CodeClass.Native
                    ownership: Ownership.Java
                }
                DefineOwnership{
                    codeClass: CodeClass.Shell
                    ownership: Ownership.Cpp
                }
            }
            since: 6
        }
        ModifyFunction{
            signature: "createTouchDevice(QTouchDevice::DeviceType)"
            ModifyArgument{
                index: "return"
                DefineOwnership{
                    codeClass: CodeClass.Native
                    ownership: Ownership.Java
                }
                DefineOwnership{
                    codeClass: CodeClass.Shell
                    ownership: Ownership.Cpp
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "qInit(QObject *, int, char **)"
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "int %out = 0;"}
                }
            }
            ModifyArgument{
                index: 3
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char ** %out = nullptr;"}
                }
            }
        }
        ModifyFunction{
            signature: "qExec(QObject *, int, char **)"
            noExcept: true
            blockExceptions: true
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "int %out = 0;"}
                }
            }
            ModifyArgument{
                index: 3
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char ** %out = nullptr;"}
                }
            }
        }
        ModifyFunction{
            signature: "qFail(const char *,const char *,int)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "currentTestFunction()"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "ignoreMessage(QtMsgType,const char *)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "setMainSourcePath(const char *, const char *)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qFindTestData(const QString&, const char*, int, const char*)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "qFindTestData(const QString&, const char*, int, const char*, const char*)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            since: 6
        }
        ModifyFunction{
            signature: "qVerify(bool,const char *,const char *,const char *,int)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qSkip(const char*,const char*,int)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qWarn(const char *,const char *,int)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qCompare(int, int, const char *, const char *, const char *, int)"
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qCompare(double, double, const char *, const char *, const char *, int)"
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qCompare(float, double, const char *, const char *, const char *, int)"
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qCompare(float, float, const char *, const char *, const char *, int)"
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qCompare(double, float, const char *, const char *, const char *, int)"
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qCompare(QPixmap, QPixmap, const char *, const char *, const char *, int)"
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qCompare(QIcon, QIcon, const char *, const char *, const char *, int)"
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qCompare(QImage, QImage, const char *, const char *, const char *, int)"
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qCompare(QStringView, QStringView, const char *, const char *, const char *, int)"
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qWaitFor<Functor>(Functor,int)"
            Instantiation{
                Argument{
                    type: "QVariant"
                    isImplicit: true
                }
                ModifyArgument{
                    index: 1
                    ReplaceType{
                        modifiedType: "java.util.function.BooleanSupplier"
                    }
                    ConversionRule{
                        codeClass: CodeClass.Native
                        Text{content: "JObjectWrapper functor(%env, %in);\n"+
                                      "auto %out = [functor]() -> bool {\n"+
                                      "                    if(JniEnvironment env{200}){\n"+
                                      "                        return Java::Runtime::BooleanSupplier::getAsBoolean(env, functor.object());\n"+
                                      "                    }\n"+
                                      "                    else return false;\n"+
                                      "                };"}
                    }
                }
            }
        }
        ModifyFunction{
            signature: "addColumn<T>(const char *, T *)"
            Instantiation{
                Argument{
                    type: "int"
                    isImplicit: true
                }
                AddTypeParameter{
                    name: "T"
                }
                AddArgument{
                    name: "type"
                    type: "java.lang.Class<T>"
                }
                AddArgument{
                    name: "instantiations"
                    type: "io.qt.core.QMetaType..."
                }
                ModifyArgument{
                    index: 1
                    ReplaceType{
                        modifiedType: "java.lang.String"
                    }
                }
                ModifyArgument{
                    index: 2
                    RemoveArgument{
                    }
                    ConversionRule{
                        codeClass: CodeClass.Native
                        Text{content: "int %out = CoreAPI::metaTypeId(%env, type, instantiations);"}
                    }
                }
            }
        }
        ModifyFunction{
            signature: "qCompare<T>(const QList<T> &, const QList<T> &, const char *, const char *, const char *, int)"
            Instantiation{
                Argument{
                    type: "QString"
                    isImplicit: true
                }
                ModifyArgument{
                    index: 3
                    ReplaceType{
                        modifiedType: "java.lang.String"
                    }
                }
                ModifyArgument{
                    index: 4
                    ReplaceType{
                        modifiedType: "java.lang.String"
                    }
                }
                ModifyArgument{
                    index: 5
                    ReplaceType{
                        modifiedType: "java.lang.String"
                    }
                }
            }
            Instantiation{
                Argument{
                    type: "QVariant"
                    isImplicit: true
                }
                AddTypeParameter{
                    name: "T"
                }
                ModifyArgument{
                    index: 1
                    ReplaceType{
                        modifiedType: "io.qt.core.QList<T>"
                    }
                }
                ModifyArgument{
                    index: 2
                    ReplaceType{
                        modifiedType: "io.qt.core.QList<T>"
                    }
                }
                ModifyArgument{
                    index: 3
                    ReplaceType{
                        modifiedType: "java.lang.String"
                    }
                }
                ModifyArgument{
                    index: 4
                    ReplaceType{
                        modifiedType: "java.lang.String"
                    }
                }
                ModifyArgument{
                    index: 5
                    ReplaceType{
                        modifiedType: "java.lang.String"
                    }
                }
            }
        }
        ModifyFunction{
            signature: "compare_helper(bool,const char *,char *,char *,const char *,const char *,const char *,int)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jsize %in_length = %env->GetStringUTFLength(%in);\n"+
                                  "char* %out = new char[size_t(%in_length)];\n"+
                                  "%env->GetStringUTFRegion(%in, 0, %in_length, %out);"}
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jsize %in_length = %env->GetStringUTFLength(%in);\n"+
                                  "char* %out = new char[size_t(%in_length)];\n"+
                                  "%env->GetStringUTFRegion(%in, 0, %in_length, %out);"}
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 6
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 7
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "compare_helper(bool, const char *, const char *, const char *, const char *, int)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jsize %in_length = %env->GetStringUTFLength(%in);\n"+
                                  "char* %out = new char[size_t(%in_length)];\n"+
                                  "%env->GetStringUTFRegion(%in, 0, %in_length, %out);"}
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jsize %in_length = %env->GetStringUTFLength(%in);\n"+
                                  "char* %out = new char[size_t(%in_length)];\n"+
                                  "%env->GetStringUTFRegion(%in, 0, %in_length, %out);"}
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qExpectFail(const char *,const char *,QTest::TestFailMode,const char *,int)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "currentAppName()"
            ModifyArgument{
                index: "return"
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "currentDataTag()"
            ModifyArgument{
                index: "return"
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "currentTestFunction()"
            ModifyArgument{
                index: "return"
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "qData(const char *,int)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: "return"
                ReplaceType{
                    modifiedType: "java.lang.Object"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "QVariant variant_%in(%2, %in);\n"+
                                  "%out = qtjambi_cast<jobject>(%env, variant_%in);"}
                    until: 5
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "#if QT_VERSION < QT_VERSION_CHECK(6, 4, 0)\n"+
                                  "if(!QMetaType(%2).iface()->copyCtr\n"+
                                  "            || (!%in && !QMetaType(%2).iface()->defaultCtr)){\n"+
                                  "    Java::Runtime::UnsupportedOperationException::throwNew(%env, QStringLiteral(\"Unable to create result of type %\"\"1.\").arg(QLatin1String(QMetaType::typeName(%2))) QTJAMBI_STACKTRACEINFO );\n"+
                                  "}\n"+
                                  "#endif\n"+
                                  "QVariant variant_%in(QMetaType(%2), %in);\n"+
                                  "%out = qtjambi_cast<jobject>(%env, variant_%in);"}
                    since: 6
                }
            }
        }
        ModifyFunction{
            signature: "qElementData(const char *,int)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: "return"
                ReplaceType{
                    modifiedType: "java.lang.Object"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "QVariant variant_%in(%2, %in);\n"+
                                  "%out = qtjambi_cast<jobject>(%env, variant_%in);"}
                    until: 5
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "#if QT_VERSION < QT_VERSION_CHECK(6, 4, 0)\n"+
                                  "if(!QMetaType(%2).iface()->copyCtr\n"+
                                  "                || (!%in && !QMetaType(%2).iface()->defaultCtr)){\n"+
                                  "    Java::Runtime::UnsupportedOperationException::throwNew(%env, QStringLiteral(\"Unable to create result of type %\"\"1.\").arg(QLatin1String(QMetaType::typeName(%2))) QTJAMBI_STACKTRACEINFO );\n"+
                                  "}\n"+
                                  "#endif\n"+
                                  "QVariant variant_%in(QMetaType(%2), %in);\n"+
                                  "%out = qtjambi_cast<jobject>(%env, variant_%in);"}
                    since: 6
                }
            }
        }
        ModifyFunction{
            signature: "qGlobalData(const char *,int)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: "return"
                ReplaceType{
                    modifiedType: "java.lang.Object"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "QVariant variant_%in(%2, %in);\n"+
                                  "%out = qtjambi_cast<jobject>(%env, variant_%in);"}
                    until: 5
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "#if QT_VERSION < QT_VERSION_CHECK(6, 4, 0)\n"+
                                  "if(!QMetaType(%2).iface()->copyCtr\n"+
                                  "                || (!%in && !QMetaType(%2).iface()->defaultCtr)){\n"+
                                  "    Java::Runtime::UnsupportedOperationException::throwNew(%env, QStringLiteral(\"Unable to create result of type %\"\"1.\").arg(QLatin1String(QMetaType::typeName(%2))) QTJAMBI_STACKTRACEINFO );\n"+
                                  "}\n"+
                                  "#endif\n"+
                                  "QVariant variant_%in(QMetaType(%2), %in);\n"+
                                  "%out = qtjambi_cast<jobject>(%env, variant_%in);"}
                    since: 6
                }
            }
        }
        ModifyFunction{
            signature: "addColumnInternal(int,const char *)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "asciiToKey(char)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "char"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char %out = QChar(%in).toLatin1();\n"+
                                  "if(%in!=%out)\n"+
                                  "    Java::Runtime::IllegalArgumentException::throwNew(%env, QStringLiteral(\"Not an ASCII character: '%\"\"1'\").arg(QChar(%in)) QTJAMBI_STACKTRACEINFO );"}
                }
            }
        }
        ModifyFunction{
            signature: "keyClick(QWindow *, char, QFlags<Qt::KeyboardModifier>, int)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "char"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char %out = QChar(%in).toLatin1();\n"+
                                  "if(%in!=%out)\n"+
                                  "    Java::Runtime::IllegalArgumentException::throwNew(%env, QStringLiteral(\"Not an ASCII character: '%\"\"1'\").arg(QChar(%in)) QTJAMBI_STACKTRACEINFO );"}
                }
            }
        }
        ModifyFunction{
            signature: "keyClick(QWidget *, char, QFlags<Qt::KeyboardModifier>, int)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "char"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char %out = QChar(%in).toLatin1();\n"+
                                  "if(%in!=%out)\n"+
                                  "    Java::Runtime::IllegalArgumentException::throwNew(%env, QStringLiteral(\"Not an ASCII character: '%\"\"1'\").arg(QChar(%in)) QTJAMBI_STACKTRACEINFO );"}
                }
            }
        }
        ModifyFunction{
            signature: "keyPress(QWindow *, char, QFlags<Qt::KeyboardModifier>, int)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "char"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char %out = QChar(%in).toLatin1();\n"+
                                  "if(%in!=%out)\n"+
                                  "    Java::Runtime::IllegalArgumentException::throwNew(%env, QStringLiteral(\"Not an ASCII character: '%\"\"1'\").arg(QChar(%in)) QTJAMBI_STACKTRACEINFO );"}
                }
            }
        }
        ModifyFunction{
            signature: "keyPress(QWidget *, char, QFlags<Qt::KeyboardModifier>, int)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "char"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char %out = QChar(%in).toLatin1();\n"+
                                  "if(%in!=%out)\n"+
                                  "    Java::Runtime::IllegalArgumentException::throwNew(%env, QStringLiteral(\"Not an ASCII character: '%\"\"1'\").arg(QChar(%in)) QTJAMBI_STACKTRACEINFO );"}
                }
            }
        }
        ModifyFunction{
            signature: "keyRelease(QWindow *, char, QFlags<Qt::KeyboardModifier>, int)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "char"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char %out = QChar(%in).toLatin1();\n"+
                                  "if(%in!=%out)\n"+
                                  "    Java::Runtime::IllegalArgumentException::throwNew(%env, QStringLiteral(\"Not an ASCII character: '%\"\"1'\").arg(QChar(%in)) QTJAMBI_STACKTRACEINFO );"}
                }
            }
        }
        ModifyFunction{
            signature: "keyRelease(QWidget *, char, QFlags<Qt::KeyboardModifier>, int)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "char"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char %out = QChar(%in).toLatin1();\n"+
                                  "if(%in!=%out)\n"+
                                  "    Java::Runtime::IllegalArgumentException::throwNew(%env, QStringLiteral(\"Not an ASCII character: '%\"\"1'\").arg(QChar(%in)) QTJAMBI_STACKTRACEINFO );"}
                }
            }
        }
        ModifyFunction{
            signature: "keyEvent(QTest::KeyAction, QWindow *, char, QFlags<Qt::KeyboardModifier>, int)"
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "char"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char %out = QChar(%in).toLatin1();\n"+
                                  "if(%in!=%out)\n"+
                                  "    Java::Runtime::IllegalArgumentException::throwNew(%env, QStringLiteral(\"Not an ASCII character: '%\"\"1'\").arg(QChar(%in)) QTJAMBI_STACKTRACEINFO );"}
                }
            }
        }
        ModifyFunction{
            signature: "keyEvent(QTest::KeyAction, QWidget *, char, QFlags<Qt::KeyboardModifier>, int)"
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "char"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char %out = QChar(%in).toLatin1();\n"+
                                  "if(%in!=%out)\n"+
                                  "    Java::Runtime::IllegalArgumentException::throwNew(%env, QStringLiteral(\"Not an ASCII character: '%\"\"1'\").arg(QChar(%in)) QTJAMBI_STACKTRACEINFO );"}
                }
            }
        }
        ModifyFunction{
            signature: "sendKeyEvent(QTest::KeyAction, QWidget *, Qt::Key, char, QFlags<Qt::KeyboardModifier>, int)"
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "char"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char %out = QChar(%in).toLatin1();\n"+
                                  "if(%in!=%out)\n"+
                                  "    Java::Runtime::IllegalArgumentException::throwNew(%env, QStringLiteral(\"Not an ASCII character: '%\"\"1'\").arg(QChar(%in)) QTJAMBI_STACKTRACEINFO );"}
                }
            }
        }
        ModifyFunction{
            signature: "sendKeyEvent(QTest::KeyAction, QWindow *, Qt::Key, char, QFlags<Qt::KeyboardModifier>, int)"
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "char"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "char %out = QChar(%in).toLatin1();\n"+
                                  "if(%in!=%out)\n"+
                                  "    Java::Runtime::IllegalArgumentException::throwNew(%env, QStringLiteral(\"Not an ASCII character: '%\"\"1'\").arg(QChar(%in)) QTJAMBI_STACKTRACEINFO );"}
                }
            }
        }
        ModifyFunction{
            signature: "compare_helper(bool,const char*,qxp::function_ref<const char*()>,qxp::function_ref<const char*()>,const char*,const char*,const char*,int)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.util.function.Supplier<String>"
                }
                NoNullPointer{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    InsertTemplate{
                        name: "test.stringsupplier.function"
                    }
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.util.function.Supplier<String>"
                }
                NoNullPointer{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    InsertTemplate{
                        name: "test.stringsupplier.function"
                    }
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 6
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 7
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            since: [6, 4]
        }
        ModifyFunction{
            signature: "reportResult(bool,qxp::function_ref<const char*()>,qxp::function_ref<const char*()>,const char*,const char*,QTest::ComparisonOperation,const char*,int)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.util.function.Supplier<String>"
                }
                NoNullPointer{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    InsertTemplate{
                        name: "test.stringsupplier.function"
                    }
                }
            }
            ModifyArgument{
                index: 3
                ReplaceType{
                    modifiedType: "java.util.function.Supplier<String>"
                }
                NoNullPointer{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    InsertTemplate{
                        name: "test.stringsupplier.function"
                    }
                }
            }
            ModifyArgument{
                index: 4
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 5
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            ModifyArgument{
                index: 7
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
            since: [6, 4]
        }
        InjectCode{
            target: CodeClass.Java
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiTest.java"
                quoteAfterLine: "class QTest__"
                quoteBeforeLine: "}// class"
            }
        }
        InjectCode{
            target: CodeClass.Java
            until: 5
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiTest.java"
                quoteAfterLine: "class QTest_5__"
                quoteBeforeLine: "}// class"
            }
        }
        InjectCode{
            target: CodeClass.Java
            since: 6
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiTest.java"
                quoteAfterLine: "class QTest_6__"
                quoteBeforeLine: "}// class"
            }
        }
        InjectCode{
            target: CodeClass.Java
            since: [6, 4]
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiTest.java"
                quoteAfterLine: "class QTest_64__"
                quoteBeforeLine: "}// class"
            }
        }

        ModifyFunction{
            signature: "qRegisterTestCase(QString, QTest::TestEntryFunction)"
            ppCondition: "QT_CONFIG(batch_test_support)"
            remove: RemoveFlag.All
            since: [6, 5]
        }
    }

    FunctionalType{
        name: "QTest::TestEntryFunction"
        generate: false
        ppCondition: "QT_CONFIG(batch_test_support)"
        since: [6, 5]
    }
    
    ObjectType{
        name: "QTest::QBenchmarkIterationController"
    }
    
    EnumType{
        name: "QTest::QBenchmarkIterationController::RunMode"
    }
    
    ObjectType{
        name: "QTest::QTouchEventSequence"
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "QTouchEventSequence(QWindow *, QPointingDevice *, bool)"
            access: Modification.Friendly
            since: 6
        }
        ModifyFunction{
            signature: "stationary(int)"
            ModifyArgument{
                index: "return"
                replaceValue: "this"
                ReplaceType{
                    modifiedType: "io.qt.test.QTest$QTouchEventSequence"
                }
            }
        }
        ModifyFunction{
            signature: "point(int)"
            ModifyArgument{
                index: "return"
                ReplaceType{
                    modifiedType: "io.qt.gui.QEventPoint"
                    since: 6
                }
                ReplaceType{
                    modifiedType: "io.qt.gui.QTouchEvent$TouchPoint"
                    until: 5
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jobject %out = qtjambi_cast<jobject>(%env, &%in);"}
                }
            }
        }
        ModifyFunction{
            signature: "pointOrPreviousPoint(int)"
            ModifyArgument{
                index: "return"
                ReplaceType{
                    modifiedType: "io.qt.gui.QEventPoint"
                    since: 6
                }
                ReplaceType{
                    modifiedType: "io.qt.gui.QTouchEvent$TouchPoint"
                    until: 5
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jobject %out = qtjambi_cast<jobject>(%env, &%in);"}
                }
            }
        }
        ModifyFunction{
            signature: "move(int, QPoint, QWindow *)"
            ModifyArgument{
                index: "return"
                replaceValue: "this"
                ReplaceType{
                    modifiedType: "io.qt.test.QTest$QTouchEventSequence"
                }
            }
        }
        ModifyFunction{
            signature: "press(int, QPoint, QWindow *)"
            ModifyArgument{
                index: "return"
                replaceValue: "this"
                ReplaceType{
                    modifiedType: "io.qt.test.QTest$QTouchEventSequence"
                }
            }
        }
        ModifyFunction{
            signature: "release(int, QPoint, QWindow *)"
            ModifyArgument{
                index: "return"
                replaceValue: "this"
                ReplaceType{
                    modifiedType: "io.qt.test.QTest$QTouchEventSequence"
                }
            }
        }
        ModifyFunction{
            signature: "move(int, QPoint, QWidget *)"
            ModifyArgument{
                index: "return"
                replaceValue: "this"
                ReplaceType{
                    modifiedType: "io.qt.test.QTest$QTouchEventSequence"
                }
            }
            ModifyArgument{
                index: 3
                RemoveDefaultExpression{
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "press(int, QPoint, QWidget *)"
            ModifyArgument{
                index: "return"
                replaceValue: "this"
                ReplaceType{
                    modifiedType: "io.qt.test.QTest$QTouchEventSequence"
                }
            }
            ModifyArgument{
                index: 3
                RemoveDefaultExpression{
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "release(int, QPoint, QWidget *)"
            ModifyArgument{
                index: "return"
                replaceValue: "this"
                ReplaceType{
                    modifiedType: "io.qt.test.QTest$QTouchEventSequence"
                }
            }
            ModifyArgument{
                index: 3
                RemoveDefaultExpression{
                }
            }
            until: 5
        }
        ModifyField{
            name: "device"
            ReferenceCount{
                action: ReferenceCount.Set
                variableName: "__rcDevice"
            }
        }
        ModifyField{
            name: "targetWindow"
            ReferenceCount{
                action: ReferenceCount.Set
                variableName: "__rcTargetWindow"
            }
        }
    }
    
    ObjectType{
        name: "QTest::QTouchEventWidgetSequence"
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "stationary(int)"
            ModifyArgument{
                index: "return"
                replaceValue: "this"
                ReplaceType{
                    modifiedType: "io.qt.test.QTest$QTouchEventWidgetSequence"
                }
            }
        }
        ModifyFunction{
            signature: "move(int, QPoint, QWidget *)"
            ModifyArgument{
                index: "return"
                replaceValue: "this"
                ReplaceType{
                    modifiedType: "io.qt.test.QTest$QTouchEventWidgetSequence"
                }
            }
            ModifyArgument{
                index: 3
                RemoveDefaultExpression{
                }
            }
        }
        ModifyFunction{
            signature: "press(int, QPoint, QWidget *)"
            ModifyArgument{
                index: "return"
                replaceValue: "this"
                ReplaceType{
                    modifiedType: "io.qt.test.QTest$QTouchEventWidgetSequence"
                }
            }
            ModifyArgument{
                index: 3
                RemoveDefaultExpression{
                }
            }
        }
        ModifyFunction{
            signature: "release(int, QPoint, QWidget *)"
            ModifyArgument{
                index: "return"
                replaceValue: "this"
                ReplaceType{
                    modifiedType: "io.qt.test.QTest$QTouchEventWidgetSequence"
                }
            }
            ModifyArgument{
                index: 3
                RemoveDefaultExpression{
                }
            }
        }
        since: 6
    }
    
    
    ObjectType{
        name: "QSignalSpy"
        implementing: "List<java.util.List<java.lang.Object>>"
        DelegateBaseClass{
            baseClass: "QList<QList<QVariant>>"
            delegate: "list"
        }
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "list()"
            access: Modification.Private
        }
        ModifyFunction{
            signature: "wait(int)"
            rename: "waitForTimeout"
        }
        ModifyFunction{
            signature: "QSignalSpy(const QObject *, const char *)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ExtraIncludes{
            Include{
                fileName: "java.util.*"
                location: Include.Java
            }
            Include{
                fileName: "java.util.function.*"
                location: Include.Java
            }
            Include{
                fileName: "java.util.stream.Stream"
                location: Include.Java
            }
            Include{
                fileName: "io.qt.core.*"
                location: Include.Java
            }
        }
        InjectCode{
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiTest.java"
                quoteAfterLine: "class QSignalSpy__"
                quoteBeforeLine: "}// class"
            }
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiTest.java"
                quoteAfterLine: "class QSignalSpy_5_"
                quoteBeforeLine: "}// class"
                until: 5
            }
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiTest.java"
                quoteAfterLine: "class QSignalSpy_6_"
                quoteBeforeLine: "}// class"
                since: 6
            }
        }
    }
    
    ObjectType{
        name: "QTestEventLoop"
    }
    
    EnumType{
        name: "QAbstractItemModelTester::FailureReportingMode"
    }
    
    ObjectType{
        name: "QAbstractItemModelTester"
    }
    
    ObjectType{
        name: "QSpontaneKeyEvent"
        ModifyField{
            name: "d"
            read: false
            write: false
        }
        ModifyField{
            name: "t"
            read: false
            write: false
        }
        ModifyFunction{
            signature: "setSpontaneous(QEvent*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
        }
    }
    
    ObjectType{
        name: "QTestEvent"
        ModifyFunction{
            signature: "clone()const"
            ModifyArgument{
                index: "return"
                DefineOwnership{
                    codeClass: CodeClass.Native
                    ownership: Ownership.Java
                }
                DefineOwnership{
                    codeClass: CodeClass.Shell
                    ownership: Ownership.Cpp
                }
            }
        }
    }
    
    ObjectType{
        name: "QTestKeyEvent"
        ModifyFunction{
            signature: "clone()const"
            ModifyArgument{
                index: "return"
                DefineOwnership{
                    codeClass: CodeClass.Native
                    ownership: Ownership.Java
                }
                DefineOwnership{
                    codeClass: CodeClass.Shell
                    ownership: Ownership.Cpp
                }
            }
        }
        ModifyField{
            name: "_action"
            rename: "action"
        }
        ModifyField{
            name: "_delay"
            rename: "delay"
        }
        ModifyField{
            name: "_delay"
            rename: "delay"
        }
        ModifyField{
            name: "_modifiers"
            rename: "modifiers"
        }
        ModifyField{
            name: "_ascii"
            rename: "ascii"
        }
        ModifyField{
            name: "_key"
            rename: "key"
        }
    }
    
    ObjectType{
        name: "QTestKeyClicksEvent"
        ModifyFunction{
            signature: "clone()const"
            ModifyArgument{
                index: "return"
                DefineOwnership{
                    codeClass: CodeClass.Native
                    ownership: Ownership.Java
                }
                DefineOwnership{
                    codeClass: CodeClass.Shell
                    ownership: Ownership.Cpp
                }
            }
        }
    }
    
    ObjectType{
        name: "QTestMouseEvent"
        ModifyFunction{
            signature: "clone()const"
            ModifyArgument{
                index: "return"
                DefineOwnership{
                    codeClass: CodeClass.Native
                    ownership: Ownership.Java
                }
                DefineOwnership{
                    codeClass: CodeClass.Shell
                    ownership: Ownership.Cpp
                }
            }
        }
    }
    
    ObjectType{
        name: "QTestDelayEvent"
        ModifyFunction{
            signature: "clone()const"
            ModifyArgument{
                index: "return"
                DefineOwnership{
                    codeClass: CodeClass.Native
                    ownership: Ownership.Java
                }
                DefineOwnership{
                    codeClass: CodeClass.Shell
                    ownership: Ownership.Cpp
                }
            }
        }
    }
    
    ValueType{
        name: "QTestEventList"
        InjectCode{
            target: CodeClass.Native
            Text{content: "namespace QtJambiPrivate{\n"+
                          "    template<>\n"+
                          "    struct supports_stream_operators<QTestEventList> : std::false_type{};\n"+
                          "}"}
        }
        ModifyFunction{
            signature: "clear()"
            rename: "purge"
        }
    }
    
    GlobalFunction{
        signature: "qt_handleTouchEventv2(QWindow*, const QPointingDevice*, const QList<QEventPoint>&, Qt::KeyboardModifiers)"
        targetType: "QTest"
    }
    
    GlobalFunction{
        signature: "qt_handleKeyEvent(QWindow*, QEvent::Type, int, Qt::KeyboardModifiers, QString, bool, ushort)"
        targetType: "QTest"
    }
    
    GlobalFunction{
        signature: "qt_handleMouseEvent(QWindow*, QPointF, QPointF, Qt::MouseButtons, Qt::MouseButton, QEvent::Type, Qt::KeyboardModifiers, int)"
        targetType: "QTest"
    }
    
    GlobalFunction{
        signature: "qt_sendShortcutOverrideEvent(QObject*, ulong, int, Qt::KeyboardModifiers, QString, bool, ushort)"
        targetType: "QTest"
    }
    
    FunctionalType{
        name: "QTest::Supplier"
        generate: false
        using: "qxp::function_ref<const char*()>"
        since: [6, 4]
    }
    
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: signature 'qWaitForWindowShown(QWidget*)' for function modification in 'QTest' not found. Possible candidates:"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: signature 'qWarn(const char*)' for function modification in 'QTest' not found. Possible candidates: qWarn(const char*,const char*,int) in QTest"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QTest::newRow', unmatched return type '*QTestData*'"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QTest::addColumn', unmatched parameter type '*'"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QTest::toString*', unmatched parameter type '*'"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QTest::qTest', unmatched parameter type '*'"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: signature 'compare_helper(bool,const char*,const char*,int)' for function modification in 'QTest' not found. Possible candidates: compare_helper(bool,const char*,char*,char*,const char*,const char*,const char*,int) in QTest"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QTest::compare_ptr_helper', unmatched parameter type 'void const volatile*'"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QTest::addRow', unmatched return type '*QTestData*'"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QTest::qCompare*', unmatched parameter type '*'"}
    SuppressedWarning{text: "WARNING(JavaGenerator) :: No ==/!= operator found for value type QTestEventList."}
}
