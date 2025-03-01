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
    packageName: "io.qt.multimedia"
    defaultSuperClass: "io.qt.QtObject"
    qtLibrary: "QtMultimedia"
    module: "qtjambi.multimedia"
    description: "Classes for audio, video, radio and camera functionality."
    InjectCode{
        target: CodeClass.MetaInfo
        position: Position.Position1
        Text{content: "void initialize_meta_info_QtMultimedia();"}
    }
    
    InjectCode{
        target: CodeClass.MetaInfo
        Text{content: "initialize_meta_info_QtMultimedia();"}
    }
    
    SuppressedWarning{text: "WARNING(CppImplGenerator) :: protected function '*' in final class '*'"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: visibility of function '*' modified in class '*'"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: hiding of function '*' in class '*'"}
    
    Rejection{
        className: "QAbstractVideoBuffer"
        since: 6
    }
    
    Rejection{
        className: "QVideoFrame"
        functionName: "videoBuffer"
        since: 6
    }
    
    Rejection{
        className: "QCamera"
        functionName: "platformCamera"
        since: [6, 3]
    }
    
    Rejection{
        className: "QImageCapture"
        functionName: "platformImageCapture"
        since: [6, 3]
    }
    
    Rejection{
        className: "QMediaRecorder"
        functionName: "platformRecoder"
        since: [6, 3]
    }
    
    Rejection{
        className: "QAudioFrame"
    }
    
    Rejection{
        className: "QAudioBuffer::StereoFrame"
    }
    
    Rejection{
        className: "QAudioBuffer::StereoFrameDefault"
    }
    
    Rejection{
        className: "QAudioDevicePrivate"
    }
    
    Rejection{
        className: "QPlatformVideoSink"
    }
    
    Rejection{
        className: "QRhi"
    }
    
    Rejection{
        className: "QPlatformMediaCaptureSession"
    }
    
    Rejection{
        className: "QPlatformMediaRecorder"
    }
    
    Rejection{
        className: ""
        functionName: "qmediacontrol_iid"
        until: [5, 15]
    }
    
    NamespaceType{
        name: "QAudio"
        Include{
            fileName: "qaudio.h"
            location: Include.Global
        }
    }
    
    NamespaceType{
        name: "QMultimedia"
        until: 5
    }
    
    
    EnumType{
        name: "QAudio::Error"
    }
    
    EnumType{
        name: "QAudio::Mode"
    }
    
    EnumType{
        name: "QAudio::State"
    }
    
    EnumType{
        name: "QAudio::Role"
    }
    
    EnumType{
        name: "QAudio::VolumeScale"
    }
    
    EnumType{
        name: "QMultimedia::AvailabilityStatus"
        until: 5
    }
    
    EnumType{
        name: "QMultimedia::EncodingMode"
        until: 5
    }
    
    EnumType{
        name: "QMultimedia::EncodingQuality"
        until: 5
    }
    
    EnumType{
        name: "QMultimedia::SupportEstimate"
        until: 5
    }
    
    EnumType{
        name: "QVideoFrame::FieldType"
    }
    
    EnumType{
        name: "QVideoFrame::PixelFormat"
    }
    
    EnumType{
        name: "QVideoFrame::RotationAngle"
    }
    
    EnumType{
        name: "QAbstractVideoBuffer::HandleType"
        until: 5
    }
    
    EnumType{
        name: "QAbstractVideoBuffer::MapMode"
        until: 5
    }
    
    EnumType{
        name: "QAbstractVideoSurface::Error"
        until: 5
    }
    
    EnumType{
        name: "QAudioFormat::Endian"
    }
    
    EnumType{
        name: "QAudioFormat::SampleType"
    }
    
    EnumType{
        name: "QAudioFormat::SampleFormat"
    }
    
    EnumType{
        name: "QVideoSurfaceFormat::Direction"
        until: 5
    }
    
    EnumType{
        name: "QVideoSurfaceFormat::YCbCrColorSpace"
        until: 5
    }
    
    EnumType{
        name: "QAudioDecoder::Error"
    }
    
    EnumType{
        name: "QAudioDecoder::State"
    }
    
    EnumType{
        name: "QCamera::CaptureMode"
        flags: "QCamera::CaptureModes"
    }
    
    EnumType{
        name: "QCamera::Error"
    }
    
    EnumType{
        name: "QCamera::LockChangeReason"
    }
    
    EnumType{
        name: "QCamera::LockStatus"
    }
    
    EnumType{
        name: "QCamera::LockType"
        flags: "QCamera::LockTypes"
    }
    
    EnumType{
        name: "QCamera::State"
    }
    
    EnumType{
        name: "QCamera::Status"
    }
    
    EnumType{
        name: "QCamera::Position"
    }
    
    EnumType{
        name: "QCamera::ExposureMode"
    }
    
    EnumType{
        name: "QCamera::Feature"
        flags: "QCamera::Features"
    }
    
    EnumType{
        name: "QCamera::FlashMode"
    }
    
    EnumType{
        name: "QCamera::FocusMode"
    }
    
    EnumType{
        name: "QCamera::WhiteBalanceMode"
    }
    
    EnumType{
        name: "QCamera::TorchMode"
    }
    
    EnumType{
        name: "QCameraExposure::ExposureMode"
        until: 5
    }
    
    EnumType{
        name: "QCameraExposure::FlashMode"
        flags: "QCameraExposure::FlashModes"
        until: 5
    }
    
    EnumType{
        name: "QCameraExposure::MeteringMode"
        until: 5
    }
    
    EnumType{
        name: "QCameraExposureControl::ExposureParameter"
        until: 5
    }
    
    EnumType{
        name: "QCameraFeedbackControl::EventType"
        until: 5
    }
    
    EnumType{
        name: "QCameraFocus::FocusMode"
        flags: "QCameraFocus::FocusModes"
        until: 5
    }
    
    EnumType{
        name: "QCameraFocus::FocusPointMode"
        until: 5
    }
    
    EnumType{
        name: "QCameraFocusZone::FocusZoneStatus"
        until: 5
    }
    
    EnumType{
        name: "QCameraImageCapture::CaptureDestination"
        flags: "QCameraImageCapture::CaptureDestinations"
        until: 5
    }
    
    EnumType{
        name: "QCameraImageCapture::DriveMode"
        until: 5
    }
    
    EnumType{
        name: "QCameraImageCapture::Error"
        until: 5
    }
    
    EnumType{
        name: "QCameraImageProcessing::WhiteBalanceMode"
        until: 5
    }
    
    EnumType{
        name: "QCameraImageProcessing::ColorFilter"
        until: 5
    }
    
    EnumType{
        name: "QCameraImageProcessingControl::ProcessingParameter"
        until: 5
    }
    
    EnumType{
        name: "QCameraViewfinderSettingsControl::ViewfinderParameter"
        until: 5
    }
    
    EnumType{
        name: "QMediaPlayer::Error"
    }
    
    EnumType{
        name: "QMediaPlayer::Flag"
        flags: "QMediaPlayer::Flags"
    }
    
    EnumType{
        name: "QMediaPlayer::PlaybackState"
    }
    
    EnumType{
        name: "QMediaPlayer::MediaStatus"
    }
    
    EnumType{
        name: "QMediaPlayer::State"
    }
    
    EnumType{
        name: "QMediaPlayer::Loops"
    }
    
    EnumType{
        name: "QMediaPlaylist::Error"
        until: 5
    }
    
    EnumType{
        name: "QMediaPlaylist::PlaybackMode"
        until: 5
    }
    
    EnumType{
        name: "QMediaRecorder::Error"
    }
    
    EnumType{
        name: "QMediaRecorder::State"
    }
    
    EnumType{
        name: "QMediaRecorder::Status"
    }
    
    EnumType{
        name: "QMediaRecorder::EncodingMode"
    }
    
    EnumType{
        name: "QMediaRecorder::Quality"
    }
    
    EnumType{
        name: "QMediaRecorder::RecorderState"
    }
    
    EnumType{
        name: "QMediaStreamsControl::StreamType"
        until: 5
    }
    
    EnumType{
        name: "QRadioData::Error"
        until: 5
    }
    
    EnumType{
        name: "QRadioData::ProgramType"
        until: 5
    }
    
    EnumType{
        name: "QRadioTuner::Band"
        until: 5
    }
    
    EnumType{
        name: "QRadioTuner::Error"
        until: 5
    }
    
    EnumType{
        name: "QRadioTuner::SearchMode"
        until: 5
    }
    
    EnumType{
        name: "QRadioTuner::State"
        until: 5
    }
    
    EnumType{
        name: "QRadioTuner::StereoMode"
        until: 5
    }
    
    EnumType{
        name: "QVideoFrame::HandleType"
    }
    
    EnumType{
        name: "QVideoFrame::MapMode"
    }
    
    EnumType{
        name: "QSound::Loop"
        until: 5
    }
    
    EnumType{
        name: "QSoundEffect::Loop"
    }
    
    EnumType{
        name: "QSoundEffect::Status"
    }
    
    EnumType{
        name: "QCameraControl::PropertyChangeType"
        until: 5
    }
    
    EnumType{
        name: "QMediaServiceProviderHint::Feature"
        flags: "QMediaServiceProviderHint::Features"
        until: 5
    }
    
    EnumType{
        name: "QMediaServiceProviderHint::Type"
        until: 5
    }
    
    EnumType{
        name: "QVideoFilterRunnable::RunFlag"
        flags: "QVideoFilterRunnable::RunFlags"
        until: 5
    }
    
    EnumType{
        name: "QVideoFrame::PaintOptions::PaintFlag"
        flags: "QVideoFrame::PaintOptions::PaintFlags"
        since: [6, 2]
    }
    
    ValueType{
        name: "QVideoFrame::PaintOptions"
        since: [6, 2]
    }
    
    ValueType{
        name: "QVideoFrame"
        ModifyFunction{
            signature: "operator=(const QVideoFrame &)"
            rename: "set"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "void"
                }
            }
        }
        ModifyFunction{
            signature: "bits()"
            remove: RemoveFlag.All
            until: 5
        }
        ModifyFunction{
            signature: "bits(int)"
            remove: RemoveFlag.All
            until: 5
        }
        ModifyFunction{
            signature: "bits(int)const"
            remove: RemoveFlag.All
            until: 5
        }
        ModifyFunction{
            signature: "bits(int) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "byte[]"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = %env->NewByteArray(__qt_this->mappedBytes(%1));\n"+
                                  "%env->SetByteArrayRegion(%out, 0, __qt_this->mappedBytes(%1), reinterpret_cast<const jbyte *>(%in));"}
                }
            }
            since: [6, 2]
        }
        ModifyFunction{
            signature: "bits(int)"
            rename: "buffer"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "java.nio.ByteBuffer"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = %env->NewDirectByteBuffer(%in, __qt_this->mappedBytes(%1));"}
                }
            }
            since: [6, 2]
        }
        ModifyFunction{
            signature: "bits() const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "byte[]"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = %env->NewByteArray(__qt_this->mappedBytes());\n"+
                                  "%env->SetByteArrayRegion(%out, 0, __qt_this->mappedBytes(), reinterpret_cast<const jbyte *>(%in));"}
                }
            }
            until: 5
        }
    }
    
    ValueType{
        name: "QAudioBuffer"
        ModifyFunction{
            signature: "operator=(const QAudioBuffer &)"
            remove: RemoveFlag.All
        }
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "data() const"
            rename: "toByteArray"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "byte[]"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = %env->NewByteArray(__qt_this->byteCount());\n"+
                                  "%env->SetByteArrayRegion(%out, 0, __qt_this->byteCount(), reinterpret_cast<const jbyte *>(%in));"}
                }
            }
        }
        ModifyFunction{
            signature: "constData() const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "java.nio.ByteBuffer"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = %env->NewDirectByteBuffer(const_cast<void*>(%in), __qt_this->byteCount());\n"+
                                  "%out = Java::Runtime::ByteBuffer::asReadOnlyBuffer(%env, %out);"}
                }
            }
        }
        ModifyFunction{
            signature: "data()"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "java.nio.ByteBuffer"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = %env->NewDirectByteBuffer(%in, __qt_this->byteCount());"}
                }
            }
        }
    }
    
    ObjectType{
        name: "QAbstractVideoBuffer"
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        InjectCode{
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiMultimedia.java"
                quoteAfterLine: "class QAbstractVideoBuffer__"
                quoteBeforeLine: "}// class"
            }
        }
        ModifyFunction{
            signature: "map(QAbstractVideoBuffer::MapMode, int*, int*)"
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "int %in = 0;\n"+
                                  "int* %out = &%in;"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "//"}
                }
            }
            ModifyArgument{
                index: 3
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "int %in = 0;\n"+
                                  "int* %out = &%in;"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "//"}
                }
            }
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QAbstractVideoBuffer$MapResult"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jobject buffer = %env->NewDirectByteBuffer(%in, %2);\n"+
                                  "jobject %out = Java::QtMultimedia::QAbstractVideoBuffer$MapResult::newInstance(%env, buffer, jint(%3), false);"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "uchar* %out = nullptr;\n"+
                                  "if(%in){\n"+
                                  "jobject data = Java::QtMultimedia::QAbstractVideoBuffer$MapResult::data(%env, %in);\n"+
                                  "if(%3){\n"+
                                  "*%3 = int(Java::QtMultimedia::QAbstractVideoBuffer$MapResult::bytesPerLine(%env, %in));\n"+
                                  "}\n"+
                                  "if(%2){\n"+
                                  "*%2 = int(%env->GetDirectBufferCapacity(data));\n"+
                                  "}\n"+
                                  "%out = reinterpret_cast<uchar*>(%env->GetDirectBufferAddress(data));\n"+
                                  "}else{\n"+
                                  "if(%2){\n"+
                                  "*%2 = 0;\n"+
                                  "}\n"+
                                  "if(%3){\n"+
                                  "*%3 = 0;\n"+
                                  "}\n"+
                                  "}"}
                }
            }
        }
        ModifyFunction{
            signature: "mapPlanes(QAbstractVideoBuffer::MapMode, int *, Array, Array)"
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "int %in = 0;\n"+
                                  "int* %out = &%in;"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "//"}
                }
            }
            ModifyArgument{
                index: 3
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "int %out[4];\n"+
                                  "%out[0] = 0;\n"+
                                  "%out[1] = 0;\n"+
                                  "%out[2] = 0;\n"+
                                  "%out[3] = 0;"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "//"}
                }
            }
            ModifyArgument{
                index: 4
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "uchar* %out[4];\n"+
                                  "%out[0] = nullptr;\n"+
                                  "%out[1] = nullptr;\n"+
                                  "%out[2] = nullptr;\n"+
                                  "%out[3] = nullptr;"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "//"}
                }
            }
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QAbstractVideoBuffer$MapResult[]"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "if(%in>0){\n"+
                                  "if(%in>4)\n"+
                                  "%in = 4;\n"+
                                  "%out = Java::QtMultimedia::QAbstractVideoBuffer$MapResult::newArray(%env, jsize(%in));\n"+
                                  "for(int i=0; i<%in; ++i){\n"+
                                  "jobject buffer = %env->NewDirectByteBuffer(__qt_%4[i], %2);\n"+
                                  "jobject element = Java::QtMultimedia::QAbstractVideoBuffer$MapResult::newInstance(%env, buffer, jint(__qt_%3[i]), false);\n"+
                                  "%env->SetObjectArrayElement(%out, jsize(i), element);\n"+
                                  "JavaException::check(%env QTJAMBI_STACKTRACEINFO );\n"+
                                  "}\n"+
                                  "}"}
                }
            }
        }
        ModifyField{
            name: "m_type"
            read: false
            rename: "handleType"
        }
        until: 5
    }
    
    ObjectType{
        name: "QAbstractPlanarVideoBuffer"
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "map(QAbstractVideoBuffer::MapMode, int *, Array, Array)"
            rename: "mapPlanesImpl"
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "int %in = 0;\n"+
                                  "int* %out = &%in;"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "//"}
                }
            }
            ModifyArgument{
                index: 3
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "int %out[4];\n"+
                                  "%out[0] = 0;\n"+
                                  "%out[1] = 0;\n"+
                                  "%out[2] = 0;\n"+
                                  "%out[3] = 0;"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "//"}
                }
            }
            ModifyArgument{
                index: 4
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "uchar* %out[4];\n"+
                                  "%out[0] = nullptr;\n"+
                                  "%out[1] = nullptr;\n"+
                                  "%out[2] = nullptr;\n"+
                                  "%out[3] = nullptr;"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "//"}
                }
            }
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QAbstractVideoBuffer$MapResult[]"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jobjectArray %out = nullptr;\n"+
                                  "if(%in>0){\n"+
                                  "if(%in>4)\n"+
                                  "%in = 4;\n"+
                                  "%out = Java::QtMultimedia::QAbstractVideoBuffer$MapResult::newArray(%env, jsize(%in));\n"+
                                  "for(int i=0; i<%in; ++i){\n"+
                                  "jobject buffer = %env->NewDirectByteBuffer(__qt_%4[i], %2);\n"+
                                  "jobject element = Java::QtMultimedia::QAbstractVideoBuffer$MapResult::newInstance(%env, buffer, jint(__qt_%3[i]), false);\n"+
                                  "%env->SetObjectArrayElement(%out, jsize(i), element);\n"+
                                  "JavaException::check(%env QTJAMBI_STACKTRACEINFO );\n"+
                                  "}\n"+
                                  "}"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "int %out = 0;\n"+
                                  "%3[0] = 0;\n"+
                                  "%3[1] = 0;\n"+
                                  "%3[2] = 0;\n"+
                                  "%3[3] = 0;\n"+
                                  "%4[0] = nullptr;\n"+
                                  "%4[1] = nullptr;\n"+
                                  "%4[2] = nullptr;\n"+
                                  "%4[3] = nullptr;\n"+
                                  "if(%in){\n"+
                                  "%out = qMin(4, int(%env->GetArrayLength(%in)));\n"+
                                  "for(int i=0; i<%out; ++i){\n"+
                                  "jobject element = %env->GetObjectArrayElement(%in, jsize(i));\n"+
                                  "if(!element){\n"+
                                  "%out = i;\n"+
                                  "break;\n"+
                                  "}\n"+
                                  "jobject data = Java::QtMultimedia::QAbstractVideoBuffer$MapResult::data(%env, element);\n"+
                                  "if(!data){\n"+
                                  "%out = i;\n"+
                                  "break;\n"+
                                  "}\n"+
                                  "%3[i] = int(Java::QtMultimedia::QAbstractVideoBuffer$MapResult::bytesPerLine(%env, element));\n"+
                                  "if(%2){\n"+
                                  "int capacity = int(%env->GetDirectBufferCapacity(data));\n"+
                                  "if(capacity<=0){\n"+
                                  "%out = 0;\n"+
                                  "%3[0] = 0;\n"+
                                  "%3[1] = 0;\n"+
                                  "%3[2] = 0;\n"+
                                  "%3[3] = 0;\n"+
                                  "%4[0] = nullptr;\n"+
                                  "%4[1] = nullptr;\n"+
                                  "%4[2] = nullptr;\n"+
                                  "%4[3] = nullptr;\n"+
                                  "break;\n"+
                                  "}\n"+
                                  "if(i>0){\n"+
                                  "*%2 = qMin(*%2, capacity);\n"+
                                  "}else{\n"+
                                  "*%2 = capacity;\n"+
                                  "}\n"+
                                  "}\n"+
                                  "%4[i] = reinterpret_cast<uchar*>(%env->GetDirectBufferAddress(data));\n"+
                                  "}\n"+
                                  "}else{\n"+
                                  "if(%2){\n"+
                                  "*%2 = 0;\n"+
                                  "}\n"+
                                  "}"}
                }
            }
        }
    }
    
    ValueType{
        name: "QAudioDeviceInfo"
        ModifyFunction{
            signature: "operator=(const QAudioDeviceInfo &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator!=(const QAudioDeviceInfo &)const"
            remove: RemoveFlag.All
        }
        until: 5
    }
    
    EnumType{
        name: "QAudioFormat::AudioChannelPosition"
        since: [6, 2]
    }
    
    EnumType{
        name: "QAudioFormat::ChannelConfig"
        extensible: true
        since: [6, 2]
    }
    
    ValueType{
        name: "QAudioFormat"
        ModifyFunction{
            signature: "operator=(const QAudioFormat &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator==(const QAudioFormat &)"
            remove: RemoveFlag.All
            since: [6, 2]
        }
        ModifyFunction{
            signature: "operator!=(const QAudioFormat &)"
            remove: RemoveFlag.All
            since: [6, 2]
        }
        ModifyFunction{
            signature: "operator!=(const QAudioFormat &)const"
            remove: RemoveFlag.All
            until: 5
        }
        InjectCode{
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiMultimedia.java"
                quoteAfterLine: "class QAudioFormat__"
                quoteBeforeLine: "}// class"
                since: [6, 2]
            }
        }
        ModifyFunction{
            signature: "normalizedSampleValue(const void*)const"
            ModifyArgument{
                index: 1
                ArrayType{
                    asBuffer: true
                    minLength: 1
                }
            }
            since: [6, 2]
        }
    }
    
    ValueType{
        name: "QVideoSurfaceFormat"
        ModifyFunction{
            signature: "operator=(const QVideoSurfaceFormat &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator!=(const QVideoSurfaceFormat &)const"
            remove: RemoveFlag.All
        }
        ExtraIncludes{
            Include{
                fileName: "QtCore/QScopedPointer"
                location: Include.Global
            }
            Include{
                fileName: "QtCore/QByteArray"
                location: Include.Global
            }
        }
        ModifyFunction{
            signature: "operator=(const QVideoSurfaceFormat)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator==(const QVideoSurfaceFormat)const"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator!=(const QVideoSurfaceFormat)const"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "property(const char*) const"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "setProperty(const char *, const QVariant &)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        until: 5
    }
    
    ValueType{
        name: "QAudioEncoderSettings"
        ModifyFunction{
            signature: "operator=(const QAudioEncoderSettings &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator!=(const QAudioEncoderSettings &)const"
            remove: RemoveFlag.All
        }
    }
    
    ValueType{
        name: "QCameraFocusZone"
        ModifyFunction{
            signature: "operator=(const QCameraFocusZone &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator!=(const QCameraFocusZone &)const"
            remove: RemoveFlag.All
        }
    }
    
    ValueType{
        name: "QImageEncoderSettings"
        ModifyFunction{
            signature: "operator=(const QImageEncoderSettings &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator!=(const QImageEncoderSettings &)const"
            remove: RemoveFlag.All
        }
    }
    
    ValueType{
        name: "QMediaContent"
        ModifyFunction{
            signature: "operator=(const QMediaContent &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator!=(const QMediaContent &)const"
            remove: RemoveFlag.All
        }
    }
    
    ValueType{
        name: "QMediaResource"
        ModifyFunction{
            signature: "operator=(const QMediaResource &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator!=(const QMediaResource &)const"
            remove: RemoveFlag.All
        }
    }
    
    ValueType{
        name: "QMediaTimeInterval"
        ModifyFunction{
            signature: "operator=(const QMediaTimeInterval &)"
            remove: RemoveFlag.All
            since: [5, 14]
        }
    }
    
    ValueType{
        name: "QMediaTimeRange"
        ModifyFunction{
            signature: "operator=(const QMediaTimeRange::Interval&)"
            remove: RemoveFlag.All
            since: [6, 2]
        }
        ModifyFunction{
            signature: "operator=(const QMediaTimeInterval&)"
            remove: RemoveFlag.All
            until: 5
        }
        ModifyFunction{
            signature: "operator=(const QMediaTimeRange &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator+=(const QMediaTimeRange::Interval&)"
            remove: RemoveFlag.All
            since: [6, 2]
        }
        ModifyFunction{
            signature: "operator+=(const QMediaTimeInterval&)"
            remove: RemoveFlag.All
            until: 5
        }
        ModifyFunction{
            signature: "operator+=(const QMediaTimeRange &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator-=(const QMediaTimeRange::Interval&)"
            remove: RemoveFlag.All
            since: [6, 2]
        }
        ModifyFunction{
            signature: "operator-=(const QMediaTimeInterval&)"
            remove: RemoveFlag.All
            until: 5
        }
        ModifyFunction{
            signature: "operator-=(const QMediaTimeRange &)"
            remove: RemoveFlag.All
        }
    }
    
    ValueType{
        name: "QVideoEncoderSettings"
        ModifyFunction{
            signature: "operator=(const QVideoEncoderSettings &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator!=(const QVideoEncoderSettings &)const"
            remove: RemoveFlag.All
        }
    }
    
    ValueType{
        name: "QMediaServiceProviderHint"
        ModifyFunction{
            signature: "operator=(const QMediaServiceProviderHint &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "operator!=(const QMediaServiceProviderHint &)const"
            remove: RemoveFlag.All
        }
        until: 5
    }
    
    ValueType{
        name: "QCameraViewfinderSettings"
        ModifyFunction{
            signature: "operator=(const QCameraViewfinderSettings &)"
            remove: RemoveFlag.All
        }
    }
    
    ValueType{
        name: "QCamera::FrameRateRange"
    }
    
    ObjectType{
        name: "QAbstractVideoSurface"
        until: 5
    }
    
    ObjectType{
        name: "QAudioOutput"
        ModifyFunction{
            signature: "QAudioOutput(const QAudioFormat &,QObject*)"
            ModifyArgument{
                index: 1
                ReplaceDefaultExpression{
                    expression: "new QAudioFormat()"
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "QAudioOutput(const QAudioDeviceInfo &,const QAudioFormat &,QObject*)"
            ModifyArgument{
                index: 2
                ReplaceDefaultExpression{
                    expression: "new QAudioFormat()"
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "notify()"
            rename: "notifySignal"
            until: 5
        }
    }
    
    ObjectType{
        name: "QAudioDecoder"
        ModifyFunction{
            signature: "setSourceDevice(QIODevice*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
        }
    }
    
    ObjectType{
        name: "QAudioDecoderControl"
        ModifyFunction{
            signature: "setSourceDevice(QIODevice*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
        }
    }
    
    ObjectType{
        name: "QAudioEncoderSettingsControl"
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "supportedSampleRates(const QAudioEncoderSettings &, bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<Integer>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jobject %out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%2));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<int> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<int>>(%env, __tmp_%in);\n"+
                                  "if(%2){\n"+
                                  "*%2 = Java::QtMultimedia::QMediaService$Result::continuous(%env, %in);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
        }
    }
    
    ObjectType{
        name: "QAudioInput"
        ModifyFunction{
            signature: "QAudioInput(const QAudioFormat &,QObject*)"
            ModifyArgument{
                index: 1
                ReplaceDefaultExpression{
                    expression: "new QAudioFormat()"
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "QAudioInput(const QAudioDeviceInfo &,const QAudioFormat &,QObject*)"
            ModifyArgument{
                index: 2
                ReplaceDefaultExpression{
                    expression: "new QAudioFormat()"
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "notify()"
            rename: "notifySignal"
            until: 5
        }
    }
    
    ObjectType{
        name: "QAudioInputSelectorControl"
    }
    
    ObjectType{
        name: "QAudioOutputSelectorControl"
    }
    
    ObjectType{
        name: "QAudioProbe"
        ModifyFunction{
            signature: "setSource(QMediaObject*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcSource"
                    condition: "__qt_return_value"
                    action: ReferenceCount.Set
                }
            }
        }
        ModifyFunction{
            signature: "setSource(QMediaRecorder*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcSource"
                    condition: "__qt_return_value"
                    action: ReferenceCount.Set
                }
            }
        }
    }
    
    ObjectType{
        name: "QAudioRecorder"
    }
    
    ObjectType{
        name: "QCamera"
        ModifyFunction{
            signature: "setViewfinder(QAbstractVideoSurface*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "setViewfinder(QGraphicsVideoItem*)"
            remove: RemoveFlag.All
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
            until: 5
        }
        InjectCode{
            until: 5
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiMultimedia.java"
                quoteAfterLine: "class QCamera__"
                quoteBeforeLine: "}// class"
            }
        }
        ModifyFunction{
            signature: "setViewfinder(QVideoWidget*)"
            rename: "setViewfinderImpl"
            access: Modification.Private
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "io.qt.core.QObject"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "QVideoWidget* %out = reinterpret_cast<QVideoWidget*>(qtjambi_cast<QObject*>(%env, %in));"}
                }
                ReferenceCount{
                    variableName: "__rcViewfinder"
                    action: ReferenceCount.Set
                }
            }
            until: 5
        }
    }
    
    ValueType{
        name: "QCameraInfo"
        ModifyFunction{
            signature: "operator=(const QCameraInfo&)"
            remove: RemoveFlag.All
        }
    }
    
    ObjectType{
        name: "QCameraCaptureBufferFormatControl"
    }
    
    ObjectType{
        name: "QCameraCaptureDestinationControl"
    }
    
    ObjectType{
        name: "QCameraControl"
        until: 5
    }
    
    ObjectType{
        name: "QCameraInfoControl"
    }
    
    ObjectType{
        name: "QCameraExposureControl"
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "supportedParameterRange(const QCameraExposureControl::ExposureParameter &, bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<Object>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jobject %out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%2));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<QVariant> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<QVariant>>(%env, __tmp_%in);\n"+
                                  "if(%2){\n"+
                                  "*%2 = Java::QtMultimedia::QMediaService$Result::continuous(%env, %in);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
        }
        until: 5
    }
    
    ObjectType{
        name: "QCameraFeedbackControl"
        until: 5
    }
    
    ObjectType{
        name: "QCameraFlashControl"
    }
    
    ObjectType{
        name: "QCameraExposure"
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "supportedApertures(bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<Double>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%1));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<double> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<double>>(%env, __tmp_%in);\n"+
                                  "if(%1){\n"+
                                  "*%1 = %env->GetObjectField(%in, Java::QtMultimedia::QMediaService$Result(%env).continuous);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 1
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
        }
        ModifyFunction{
            signature: "supportedIsoSensitivities(bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<Integer>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%1));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<int> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<int>>(%env, __tmp_%in);\n"+
                                  "if(%1){\n"+
                                  "*%1 = %env->GetObjectField(%in, Java::QtMultimedia::QMediaService$Result(%env).continuous);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 1
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
        }
        ModifyFunction{
            signature: "supportedShutterSpeeds(bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<Double>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%1));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<double> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<double>>(%env, __tmp_%in);\n"+
                                  "if(%1){\n"+
                                  "*%1 = %env->GetObjectField(%in, Java::QtMultimedia::QMediaService$Result(%env).continuous);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 1
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
        }
        until: 5
    }
    
    ObjectType{
        name: "QCameraFocus"
        until: 5
    }
    
    ObjectType{
        name: "QCameraFocusControl"
    }
    
    ObjectType{
        name: "QCameraImageCapture"
        ModifyFunction{
            signature: "setMediaObject(QMediaObject*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
            until: 5
        }
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "supportedResolutions(const QImageEncoderSettings &, bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<io.qt.core.QSize>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%2));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<QSize> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<QSize>>(%env, __tmp_%in);\n"+
                                  "if(%2){\n"+
                                  "*%2 = Java::QtMultimedia::QMediaService$Result::continuous(%env, %in);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
        }
        until: 5
    }
    
    ObjectType{
        name: "QCameraImageCaptureControl"
    }
    
    ObjectType{
        name: "QCameraImageProcessing"
        until: 5
    }
    
    ObjectType{
        name: "QCameraImageProcessingControl"
        until: 5
    }
    
    ObjectType{
        name: "QCameraLocksControl"
    }
    
    ObjectType{
        name: "QCameraViewfinderSettingsControl"
        until: 5
    }
    
    ObjectType{
        name: "QCameraViewfinderSettingsControl2"
        until: 5
    }
    
    ObjectType{
        name: "QCameraZoomControl"
    }
    
    ObjectType{
        name: "QImageEncoderControl"
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "supportedResolutions(const QImageEncoderSettings &, bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<io.qt.core.QSize>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jobject %out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%2));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<QSize> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<QSize>>(%env, __tmp_%in);\n"+
                                  "if(%2){\n"+
                                  "*%2 = Java::QtMultimedia::QMediaService$Result::continuous(%env, %in);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
        }
    }
    
    ObjectType{
        name: "QMediaAudioProbeControl"
    }
    
    ObjectType{
        name: "QMediaAvailabilityControl"
    }
    
    ObjectType{
        name: "QMediaContainerControl"
    }
    
    ObjectType{
        name: "QMediaControl"
    }
    
    ObjectType{
        name: "QAudioRoleControl"
    }
    
    ObjectType{
        name: "QMediaGaplessPlaybackControl"
    }
    
    ObjectType{
        name: "QMediaNetworkAccessControl"
    }
    
    ObjectType{
        name: "QAbstractAudioDeviceInfo"
    }
    
    ObjectType{
        name: "QAbstractAudioInput"
        ModifyFunction{
            signature: "notify()"
            rename: "notifySignal"
        }
    }
    
    ObjectType{
        name: "QAbstractAudioOutput"
        ModifyFunction{
            signature: "notify()"
            rename: "notifySignal"
        }
    }
    
    ObjectType{
        name: "QMediaObject"
    }
    
    ObjectType{
        name: "QMediaPlayer"
        ModifyFunction{
            signature: "setVideoOutput(QGraphicsVideoItem*)"
            remove: RemoveFlag.All
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "setVideoOutput(QAbstractVideoSurface*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "setPlaylist(QMediaPlaylist*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "setMedia(QMediaContent,QIODevice*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "setAudioOutput(QAudioOutput*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcAudioOutput"
                    action: ReferenceCount.Set
                }
            }
            since: [6, 2]
        }
        ModifyFunction{
            signature: "setSourceDevice(QIODevice*,QUrl)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcSourceDevice"
                    action: ReferenceCount.Set
                }
            }
            since: [6, 2]
        }
        ModifyFunction{
            signature: "setVideoOutput(QObject*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcVideoOutput"
                    action: ReferenceCount.Set
                }
            }
            since: [6, 2]
        }
        ModifyFunction{
            signature: "setVideoSink(QVideoSink*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcVideoSink"
                    action: ReferenceCount.Set
                }
            }
            since: [6, 2]
        }
        ModifyFunction{
            signature: "setVideoOutput(QVideoWidget*)"
            rename: "setVideoOutputImpl"
            access: Modification.Private
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcVideoOutput"
                    action: ReferenceCount.Set
                }
                ReplaceType{
                    modifiedType: "io.qt.core.QObject"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "QVideoWidget* %out = reinterpret_cast<QVideoWidget*>(qtjambi_cast<QObject*>(%env, %in));"}
                }
            }
            until: 5
        }
        InjectCode{
            until: 5
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiMultimedia.java"
                quoteAfterLine: "class QMediaPlayer__"
                quoteBeforeLine: "}// class"
            }
        }
    }
    
    ObjectType{
        name: "QMediaPlayerControl"
        ModifyFunction{
            signature: "setMedia(QMediaContent,QIODevice*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
        }
    }
    
    ObjectType{
        name: "QMediaPlaylist"
        ModifyFunction{
            signature: "setMediaObject(QMediaObject*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
            until: 5
        }
        ExtraIncludes{
            Include{
                fileName: "QtCore/QScopedPointer"
                location: Include.Global
            }
            Include{
                fileName: "QtCore/QByteArray"
                location: Include.Global
            }
        }
        ModifyFunction{
            signature: "load(const QNetworkRequest &, const char *)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "load(const QUrl &, const char *)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "save(const QUrl &, const char *)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "save(QIODevice*, const char *)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        ModifyFunction{
            signature: "load(QIODevice*, const char *)"
            ModifyArgument{
                index: 2
                ReplaceType{
                    modifiedType: "java.lang.String"
                }
            }
        }
        until: 5
    }
    
    ObjectType{
        name: "QMediaRecorder"
        ModifyFunction{
            signature: "setMediaObject(QMediaObject*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
            until: 5
        }
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "supportedAudioSampleRates(const QAudioEncoderSettings &, bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<Integer>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%2));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<int> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<int>>(%env, __tmp_%in);\n"+
                                  "if(%2){\n"+
                                  "*%2 = Java::QtMultimedia::QMediaService$Result::continuous(%env, %in);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "supportedFrameRates(const QVideoEncoderSettings &, bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<Double>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%2));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<double> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<double>>(%env, __tmp_%in);\n"+
                                  "if(%2){\n"+
                                  "*%2 = Java::QtMultimedia::QMediaService$Result::continuous(%env, %in);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
            until: 5
        }
        ModifyFunction{
            signature: "supportedResolutions(const QVideoEncoderSettings &, bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<io.qt.core.QSize>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "%out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%2));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<QSize> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<QSize>>(%env, __tmp_%in);\n"+
                                  "if(%2){\n"+
                                  "*%2 = Java::QtMultimedia::QMediaService$Result::continuous(%env, %in);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
            until: 5
        }
    }
    
    ObjectType{
        name: "QMediaRecorderControl"
    }
    
    ObjectType{
        name: "QMediaService"
        InjectCode{
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiMultimedia.java"
                quoteAfterLine: "class QMediaService___"
                quoteBeforeLine: "}// class"
            }
        }
        ExtraIncludes{
            Include{
                fileName: "QtCore/QScopedPointer"
                location: Include.Global
            }
            Include{
                fileName: "QtCore/QByteArray"
                location: Include.Global
            }
        }
        ModifyFunction{
            signature: "requestControl(const char *)"
            AddTypeParameter{
                name: "T"
                extending: "io.qt.multimedia.QMediaControl"
            }
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "T"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jobject %out = QtJambiAPI::convertQObjectToJavaObject(%env, %in, jclass(%1));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QMediaControl* %out = qtjambi_cast<QMediaControl*>(%env, %in);"}
                }
            }
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "java.lang.Class<T>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "const char* %out = RegistryAPI::mediaControlIID(%env, jclass(%in));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "jclass %out = RegistryAPI::classByMediaControlIID(%env, %in);"}
                }
            }
        }
    }
    
    ObjectType{
        name: "QMediaStreamsControl"
        until: 5
    }
    
    ObjectType{
        name: "QMediaVideoProbeControl"
    }
    
    ObjectType{
        name: "QMetaDataReaderControl"
    }
    
    ObjectType{
        name: "QMetaDataWriterControl"
    }
    
    ObjectType{
        name: "QRadioData"
        ModifyFunction{
            signature: "setMediaObject(QMediaObject*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
            until: 5
        }
        until: 5
    }
    
    ObjectType{
        name: "QRadioDataControl"
    }
    
    ObjectType{
        name: "QRadioTuner"
        until: 5
    }
    
    ObjectType{
        name: "QRadioTunerControl"
    }
    
    ObjectType{
        name: "QSound"
        until: 5
    }
    
    ObjectType{
        name: "QSoundEffect"
    }
    
    ObjectType{
        name: "QVideoDeviceSelectorControl"
    }
    
    ObjectType{
        name: "QVideoEncoderSettingsControl"
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "supportedFrameRates(const QVideoEncoderSettings &, bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<java.lang.Double>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jobject %out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%2));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<double> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<double>>(%env, __tmp_%in);\n"+
                                  "if(%2){\n"+
                                  "*%2 = Java::QtMultimedia::QMediaService$Result::continuous(%env, %in);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
        }
        ModifyFunction{
            signature: "supportedResolutions(const QVideoEncoderSettings &, bool *) const"
            ModifyArgument{
                index: 0
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QMediaService$ListResult<io.qt.core.QSize>"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "jobject %out = Java::QtMultimedia::QMediaService$ListResult::newInstance(\n"+
                                  "%env, qtjambi_cast<jobject>(%env, %in), jboolean(%2));"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "QList<QSize> %out;\n"+
                                  "if(%in){\n"+
                                  "jobject __tmp_%in = Java::QtMultimedia::QMediaService$Result::result(%env, %in);\n"+
                                  "if(__tmp_%in)\n"+
                                  "%out = qtjambi_cast<QList<QSize>>(%env, __tmp_%in);\n"+
                                  "if(%2){\n"+
                                  "*%2 = Java::QtMultimedia::QMediaService$Result::continuous(%env, %in);\n"+
                                  "}\n"+
                                  "}"}
                }
            }
            ModifyArgument{
                index: 2
                RemoveArgument{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "bool %in = false;\n"+
                                  "bool* %out = &%in;"}
                }
            }
        }
    }
    
    ObjectType{
        name: "QVideoProbe"
        ModifyFunction{
            signature: "setSource(QMediaObject*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcSource"
                    condition: "__qt_return_value"
                    action: ReferenceCount.Set
                }
            }
        }
        ModifyFunction{
            signature: "setSource(QMediaRecorder*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcSource"
                    condition: "__qt_return_value"
                    action: ReferenceCount.Set
                }
            }
        }
    }
    
    ObjectType{
        name: "QVideoRendererControl"
        ModifyFunction{
            signature: "setSurface(QAbstractVideoSurface*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcSurface"
                    action: ReferenceCount.Set
                    access: Modification.Friendly
                }
            }
        }
    }
    
    ObjectType{
        name: "QVideoWindowControl"
    }
    
    ObjectType{
        name: "QMediaServiceProviderPlugin"
        ModifyFunction{
            signature: "create(const QString&)"
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
        name: "QAudioSystemPlugin"
        ModifyFunction{
            signature: "createDeviceInfo(const QByteArray&, QAudio::Mode)"
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
        ModifyFunction{
            signature: "createInput(const QByteArray&)"
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
        ModifyFunction{
            signature: "createOutput(const QByteArray&)"
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
        name: "QGstBufferPoolPlugin"
    }
    
    ObjectType{
        name: "QMediaPlaylistIOPlugin"
    }
    
    ObjectType{
        name: "QMediaResourcePolicyPlugin"
    }
    
    ObjectType{
        name: "QSGVideoNodeFactoryPlugin"
    }
    
    ObjectType{
        name: "QAbstractVideoFilter"
        ModifyFunction{
            signature: "createFilterRunnable()"
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
        name: "QCustomAudioRoleControl"
    }
    
    ObjectType{
        name: "QVideoFilterRunnable"
        ExtraIncludes{
            Include{
                fileName: "utils_p.h"
                location: Include.Local
            }
        }
        ModifyFunction{
            signature: "run(QVideoFrame *, const QVideoSurfaceFormat &, QFlags<QVideoFilterRunnable::RunFlag>)"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "io.qt.multimedia.QVideoFrame"
                }
                NoNullPointer{
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "QVideoFrame* %out = &qtjambi_cast<QVideoFrame&>(%env, %scope, %in);"}
                }
                ConversionRule{
                    codeClass: CodeClass.Shell
                    Text{content: "jobject %out = qtjambi_cast<jobject>(%env, %in);"}
                }
            }
        }
        until: 5
    }
    
    InterfaceType{
        name: "QAudioSystemFactoryInterface"
        ModifyFunction{
            signature: "createDeviceInfo(const QByteArray&, QAudio::Mode)"
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
        ModifyFunction{
            signature: "createInput(const QByteArray&)"
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
        ModifyFunction{
            signature: "createOutput(const QByteArray&)"
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
    
    InterfaceType{
        name: "QDeclarativeVideoBackendFactoryInterface"
    }
    
    InterfaceType{
        name: "QMediaBindableInterface"
        ModifyFunction{
            signature: "setMediaObject(QMediaObject*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    action: ReferenceCount.Ignore
                }
            }
        }
    }
    
    InterfaceType{
        name: "QMediaServiceFeaturesInterface"
    }
    
    InterfaceType{
        name: "QMediaServiceSupportedDevicesInterface"
    }
    
    InterfaceType{
        name: "QMediaServiceSupportedFormatsInterface"
    }
    
    InterfaceType{
        name: "QMediaServiceProviderFactoryInterface"
        ModifyFunction{
            signature: "create(const QString&)"
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
        InjectCode{
            target: CodeClass.JavaInterface
            ImportFile{
                name: ":/io/qtjambi/generator/typesystem/QtJambiMultimedia.java"
                quoteAfterLine: "class QMediaServiceProviderFactoryInterface_java__"
                quoteBeforeLine: "}// class"
            }
        }
    }
    
    InterfaceType{
        name: "QMediaServiceCameraInfoInterface"
    }
    
    InterfaceType{
        name: "QMediaServiceDefaultDeviceInterface"
    }
    
    EnumType{
        name: "QAudioDevice::Mode"
        since: [6, 2]
    }
    
    ValueType{
        name: "QCameraFormat"
        ModifyFunction{
            signature: "operator=(const QCameraFormat &)"
            remove: RemoveFlag.All
        }
        since: [6, 2]
    }
    
    EnumType{
        name: "QCameraDevice::Position"
        since: [6, 2]
    }
    
    ValueType{
        name: "QCameraDevice"
        ModifyFunction{
            signature: "operator=(const QCameraDevice &)"
            remove: RemoveFlag.All
        }
        since: [6, 2]
    }
    
    ValueType{
        name: "QAudioDevice"
        ModifyFunction{
            signature: "operator=(const QAudioDevice &)"
            remove: RemoveFlag.All
        }
        since: [6, 2]
    }
    
    ValueType{
        name: "QMediaFormat"
        ModifyField{
            name: "audio"
            read: false
            write: false
        }
        ModifyField{
            name: "video"
            read: false
            write: false
        }
        ModifyField{
            name: "fmt"
            read: false
            write: false
        }
        ModifyFunction{
            signature: "operator=(const QMediaFormat &)"
            remove: RemoveFlag.All
        }
        since: [6, 2]
    }
    
    EnumType{
        name: "QMediaFormat::FileFormat"
        RejectEnumValue{
            name: "LastFileFormat"
        }
        since: [6, 2]
    }
    
    EnumType{
        name: "QMediaFormat::AudioCodec"
        RejectEnumValue{
            name: "LastAudioCodec"
        }
        since: [6, 2]
    }
    
    EnumType{
        name: "QMediaFormat::VideoCodec"
        RejectEnumValue{
            name: "LastVideoCodec"
        }
        since: [6, 2]
    }
    
    EnumType{
        name: "QMediaFormat::ConversionMode"
        since: [6, 2]
    }
    
    EnumType{
        name: "QMediaFormat::ResolveFlags"
        since: [6, 2]
    }
    
    ObjectType{
        name: "QAudioSink"
        since: [6, 2]
    }
    
    ObjectType{
        name: "QAudioSource"
        since: [6, 2]
    }
    
    EnumType{
        name: "QImageCapture::Error"
        since: [6, 2]
    }
    
    EnumType{
        name: "QImageCapture::Quality"
        since: [6, 2]
    }
    
    EnumType{
        name: "QImageCapture::FileFormat"
        RejectEnumValue{
            name: "LastFileFormat"
        }
        since: [6, 2]
    }
    
    ObjectType{
        name: "QImageCapture"
        since: [6, 2]
    }

    ObjectType{
        name: "QScreenCapture"
        ModifyFunction{
            signature: "setScreen(QScreen*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcScreen"
                    action: ReferenceCount.Set
                }
            }
        }
        ModifyFunction{
            signature: "setWindow(QWindow*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcWindow"
                    action: ReferenceCount.Set
                }
            }
        }
        since: [6, 5]
    }

    EnumType{
        name: "QScreenCapture::Error"
        since: [6, 5]
    }
    
    ObjectType{
        name: "QMediaCaptureSession"
        ModifyFunction{
            signature: "setAudioInput(QAudioInput*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcAudioInput"
                    action: ReferenceCount.Set
                }
            }
        }
        ModifyFunction{
            signature: "setAudioOutput(QAudioOutput*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcAudioOutput"
                    action: ReferenceCount.Set
                }
            }
        }
        ModifyFunction{
            signature: "setCamera(QCamera*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcCamera"
                    action: ReferenceCount.Set
                }
            }
        }
        ModifyFunction{
            signature: "setRecorder(QMediaRecorder*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcRecorder"
                    action: ReferenceCount.Set
                }
            }
        }
        ModifyFunction{
            signature: "setImageCapture(QImageCapture*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcImageCapture"
                    action: ReferenceCount.Set
                }
            }
        }
        ModifyFunction{
            signature: "setVideoOutput(QObject*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcVideoOutput"
                    action: ReferenceCount.Set
                }
            }
        }
        ModifyFunction{
            signature: "setVideoSink(QVideoSink*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcVideoSink"
                    action: ReferenceCount.Set
                }
            }
        }
        ModifyFunction{
            signature: "setScreenCapture(QScreenCapture*)"
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcScreenCapture"
                    action: ReferenceCount.Set
                }
            }
            since: [6, 5]
        }
        since: [6, 2]
    }
    
    ObjectType{
        name: "QVideoSink"
        since: [6, 2]
    }
    
    ObjectType{
        name: "QWaveDecoder"
        ModifyFunction{
            signature: "setIODevice(QIODevice *)"
            remove: RemoveFlag.All
            ModifyArgument{
                index: 1
                ReferenceCount{
                    variableName: "__rcIODevice"
                    action: ReferenceCount.Set
                }
            }
        }
        since: [6, 2]
    }
    
    Rejection{
        className: "QWaveDecoder::chunk"
        since: [6, 2]
    }
    
    Rejection{
        className: "QWaveDecoder"
        functionName: "peekChunk"
        since: [6, 2]
    }
    
    ObjectType{
        name: "QMediaDevices"
        since: [6, 2]
    }
    
    ValueType{
        name: "QMediaTimeRange::Interval"
        since: [6, 2]
    }
    
    ValueType{
        name: "QMediaMetaData"
        ModifyFunction{
            signature: "operator[](QMediaMetaData::Key)"
            remove: RemoveFlag.All
        }
        since: [6, 2]
    }
    
    ValueType{
        name: "QVideoFrameFormat"
        ModifyFunction{
            signature: "operator=(const QVideoFrameFormat &)"
            remove: RemoveFlag.All
        }
        ModifyFunction{
            signature: "updateUniformData(QByteArray *, const QVideoFrame &, const QMatrix4x4 &, float) const"
            ModifyArgument{
                index: 1
                ReplaceType{
                    modifiedType: "io.qt.core.QByteArray"
                }
                ConversionRule{
                    codeClass: CodeClass.Native
                    Text{content: "QByteArray* %out = qtjambi_cast<QByteArray*>(%env, %in);"}
                }
            }
            since: 6
        }
        since: [6, 2]
    }
    
    EnumType{
        name: "QMediaMetaData::Key"
        since: [6, 2]
    }
    
    EnumType{
        name: "QVideoFrameFormat::PixelFormat"
        since: [6, 2]
    }
    
    EnumType{
        name: "QVideoFrameFormat::Direction"
        since: [6, 2]
    }
    
    EnumType{
        name: "QVideoFrameFormat::YCbCrColorSpace"
        since: [6, 2]
    }
    
    EnumType{
        name: "QVideoFrameFormat::ColorTransfer"
        since: [6, 4]
    }
    
    EnumType{
        name: "QVideoFrameFormat::ColorRange"
        since: [6, 4]
    }
    
    EnumType{
        name: "QVideoFrameFormat::ColorSpace"
        since: [6, 4]
    }
    
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QAudioBuffer::QAudioBuffer', unmatched parameter type 'QAbstractAudioBuffer*'"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QMediaService::requestControl', unmatched return type 'T'"}
    SuppressedWarning{text: "WARNING(JavaGenerator) :: No ==/!= operator found for value type QWebEngine*."}
    SuppressedWarning{text: "WARNING(JavaGenerator) :: No ==/!= operator found for value type QAudioBuffer."}
    SuppressedWarning{text: "WARNING(JavaGenerator) :: No ==/!= operator found for value type QCameraViewfinderSettings."}
    SuppressedWarning{text: "WARNING(JavaGenerator) :: No ==/!= operator found for value type QVideoSurfaceFormat."}
    SuppressedWarning{
        text: "WARNING(MetaJavaBuilder) :: skipping function 'QVideoFrame::QVideoFrame', unmatched parameter type 'QAbstractVideoBuffer*'"
        since: [6, 2]
    }
    SuppressedWarning{
        text: "WARNING(MetaJavaBuilder) :: skipping function 'QMediaCaptureSession::platformSession', unmatched return type 'QPlatformMediaCaptureSession*'"
        since: [6, 2]
    }
    SuppressedWarning{
        text: "WARNING(MetaJavaBuilder) :: skipping function 'QAudioOutput::handle', unmatched return type 'QPlatformAudioOutput*'"
        since: [6, 2]
    }
    SuppressedWarning{
        text: "WARNING(MetaJavaBuilder) :: skipping function 'QAudioInput::handle', unmatched return type 'QPlatformAudioInput*'"
        since: [6, 2]
    }
    SuppressedWarning{
        text: "WARNING(MetaJavaBuilder) :: skipping function 'QVideoSink::platformVideoSink', unmatched return type 'QPlatformVideoSink*'"
        since: [6, 2]
    }
    SuppressedWarning{
        text: "WARNING(MetaJavaBuilder) :: skipping function *, unmatched *type 'QRhi*'"
        since: [6, 2]
    }
    SuppressedWarning{
        text: "WARNING(MetaJavaBuilder) :: private virtual function '*' in 'QWaveDecoder'"
        since: [6, 2]
    }
    SuppressedWarning{
        text: "WARNING(MetaJavaBuilder) :: Class 'QMediaMetaData' has equals operators but no qHash() function. Hashcode of objects will consistently be 0."
        since: [6, 2]
    }
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QCamera::platformCamera', unmatched return type 'QPlatformCamera*'"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QImageCapture::platformImageCapture', unmatched return type 'QPlatformImageCapture*'"}
    SuppressedWarning{text: "WARNING(MetaJavaBuilder) :: skipping function 'QMediaRecorder::platformRecoder', unmatched return type 'QPlatformMediaRecorder*'"}
}
