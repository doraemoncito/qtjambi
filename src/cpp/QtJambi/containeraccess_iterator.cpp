#include "qtjambiapi.h"
#include <QtCore/QReadWriteLock>
#include <QtCore/QMap>
#include <QtCore/QSharedPointer>
#include "containeraccess_p.h"
#include "functionpointer.h"
#include "registryutil_p.h"

AutoSequentialConstIteratorAccess::~AutoSequentialConstIteratorAccess() = default;
AutoSequentialConstIteratorAccess::AutoSequentialConstIteratorAccess(
        const InternalToExternalConverter& internalToExternalConverter,
        IncrementFn increment,
        DecrementFn decrement,
        ValueFn value,
        LessThanFn lessThan,
        EqualsFn equals
    )
    : AbstractSequentialConstIteratorAccess(),
      m_internalToExternalConverter(internalToExternalConverter),
      m_increment(increment),
      m_decrement(decrement),
      m_value(value),
      m_lessThan(lessThan),
      m_equals(equals)
{
    Q_ASSERT(m_value);
}

void AutoSequentialConstIteratorAccess::dispose() {delete this;}

AbstractSequentialConstIteratorAccess* AutoSequentialConstIteratorAccess::clone()
{
    return new AutoSequentialConstIteratorAccess(
                m_internalToExternalConverter,
                m_increment,
                m_decrement,
                m_value,
                m_lessThan,
                m_equals);
}

jobject AutoSequentialConstIteratorAccess::value(JNIEnv * env, const void* iterator)
{
    const void* v = m_value(iterator);
    jvalue jval;
    jval.l = nullptr;
    if(m_internalToExternalConverter(env, nullptr, v, &jval, true))
        return jval.l;
    return nullptr;
}

void AutoSequentialConstIteratorAccess::increment(JNIEnv *, void* iterator)
{
    m_increment(iterator);
}

void AutoSequentialConstIteratorAccess::decrement(JNIEnv *, void* iterator)
{
    m_decrement(iterator);
}

jboolean AutoSequentialConstIteratorAccess::lessThan(JNIEnv *, const void* iterator, const void* other)
{
    return m_lessThan(iterator, other);
}

bool AutoSequentialConstIteratorAccess::canLess()
{
    if(m_lessThan)
        return true;
    else return false;
}

jboolean AutoSequentialConstIteratorAccess::equals(JNIEnv *, const void* iterator, const void* other)
{
    return m_equals(iterator, other);
}

AutoAssociativeConstIteratorAccess::~AutoAssociativeConstIteratorAccess(){}
AutoAssociativeConstIteratorAccess::AutoAssociativeConstIteratorAccess(
        const InternalToExternalConverter& internalToExternalConverter,
        IncrementFn increment,
        DecrementFn decrement,
        ValueFn value,
        LessThanFn lessThan,
        EqualsFn equals,
        const InternalToExternalConverter& keyInternalToExternalConverter,
        KeyFn key
        )
    : AbstractAssociativeConstIteratorAccess(),
      AutoSequentialConstIteratorAccess(internalToExternalConverter,
                        increment,
                        decrement,
                        value,
                        lessThan,
                        equals),
      m_keyInternalToExternalConverter(keyInternalToExternalConverter),
      m_key(std::move(key))
{
    Q_ASSERT(m_key);
}

AbstractSequentialConstIteratorAccess* AutoAssociativeConstIteratorAccess::clone(){
    AbstractAssociativeConstIteratorAccess* access = new AutoAssociativeConstIteratorAccess(
                m_internalToExternalConverter,
                m_increment,
                m_decrement,
                m_value,
                m_lessThan,
                m_equals,
                m_keyInternalToExternalConverter,
                m_key);
    return access;
}

jobject AutoAssociativeConstIteratorAccess::key(JNIEnv * env, const void* iterator){
    const void* v = m_key(iterator);
    jvalue jval;
    jval.l = nullptr;
    if(m_keyInternalToExternalConverter(env, nullptr, v, &jval, true))
        return jval.l;
    return nullptr;
}

jobject AutoAssociativeConstIteratorAccess::value(JNIEnv * env, const void* iterator){return AutoSequentialConstIteratorAccess::value(env, iterator);}
void AutoAssociativeConstIteratorAccess::increment(JNIEnv * env, void* iterator){AutoSequentialConstIteratorAccess::increment(env, iterator);}
void AutoAssociativeConstIteratorAccess::decrement(JNIEnv * env, void* iterator){AutoSequentialConstIteratorAccess::decrement(env, iterator);}
jboolean AutoAssociativeConstIteratorAccess::lessThan(JNIEnv * env, const void* iterator, const void* other){return AutoSequentialConstIteratorAccess::lessThan(env, iterator, other);}
bool AutoAssociativeConstIteratorAccess::canLess(){return AutoSequentialConstIteratorAccess::canLess();}
jboolean AutoAssociativeConstIteratorAccess::equals(JNIEnv * env, const void* iterator, const void* other){return AutoSequentialConstIteratorAccess::equals(env, iterator, other);}

AutoSequentialIteratorAccess::~AutoSequentialIteratorAccess() = default;
AutoSequentialIteratorAccess::AutoSequentialIteratorAccess(
        const InternalToExternalConverter& internalToExternalConverter,
        IncrementFn increment,
        DecrementFn decrement,
        ValueFn value,
        LessThanFn lessThan,
        EqualsFn equals,
        const ExternalToInternalConverter& externalToInternalConverter,
        SetValueFn setValue
    )
    : AbstractSequentialIteratorAccess(),
      AutoSequentialConstIteratorAccess(internalToExternalConverter,
                                          increment,
                                          decrement,
                                          value,
                                          lessThan,
                                          equals),
      m_externalToInternalConverter(externalToInternalConverter),
      m_setValue(setValue)
{
    Q_ASSERT(m_value);
    Q_ASSERT(m_setValue);
}

AbstractSequentialConstIteratorAccess* AutoSequentialIteratorAccess::clone()
{
    AbstractSequentialIteratorAccess* access = new AutoSequentialIteratorAccess(
                m_internalToExternalConverter,
                m_increment,
                m_decrement,
                m_value,
                m_lessThan,
                m_equals,
                m_externalToInternalConverter,
                m_setValue);
    return access;
}

void AutoSequentialIteratorAccess::setValue(JNIEnv * env, void* iterator, jobject newValue){
    void* newval = m_setValue(iterator);
    jvalue jval;
    jval.l = newValue;
    m_externalToInternalConverter(env, nullptr, jval, newval, jValueType::l);
}

jobject AutoSequentialIteratorAccess::value(JNIEnv * env, const void* iterator){return AutoSequentialConstIteratorAccess::value(env, iterator);}
void AutoSequentialIteratorAccess::increment(JNIEnv * env, void* iterator){AutoSequentialConstIteratorAccess::increment(env, iterator);}
void AutoSequentialIteratorAccess::decrement(JNIEnv * env, void* iterator){AutoSequentialConstIteratorAccess::decrement(env, iterator);}
jboolean AutoSequentialIteratorAccess::lessThan(JNIEnv * env, const void* iterator, const void* other){return AutoSequentialConstIteratorAccess::lessThan(env, iterator, other);}
bool AutoSequentialIteratorAccess::canLess(){return AutoSequentialConstIteratorAccess::canLess();}
jboolean AutoSequentialIteratorAccess::equals(JNIEnv * env, const void* iterator, const void* other){return AutoSequentialConstIteratorAccess::equals(env, iterator, other);}


AutoAssociativeIteratorAccess::~AutoAssociativeIteratorAccess(){}
AutoAssociativeIteratorAccess::AutoAssociativeIteratorAccess(
        const InternalToExternalConverter& valueInternalToExternalConverter,
        IncrementFn increment,
        DecrementFn decrement,
        ValueFn value,
        LessThanFn lessThan,
        EqualsFn equals,
        const InternalToExternalConverter& keyInternalToExternalConverter,
        KeyFn key,
        const ExternalToInternalConverter& valueExternalToInternalConverter,
        SetValueFn setValue
        )
    : AbstractAssociativeIteratorAccess(),
      AutoSequentialConstIteratorAccess(valueInternalToExternalConverter,
                        increment,
                        decrement,
                        value,
                        lessThan,
                        equals),
      AutoAssociativeConstIteratorAccess(valueInternalToExternalConverter,
                        increment,
                        decrement,
                        value,
                        lessThan,
                        equals,
                        keyInternalToExternalConverter,
                        std::move(key)),
      m_valueExternalToInternalConverter(valueExternalToInternalConverter),
      m_setValue(setValue)
{
    Q_ASSERT(setValue);
}

AbstractSequentialConstIteratorAccess* AutoAssociativeIteratorAccess::clone(){
    AbstractAssociativeIteratorAccess* access = new AutoAssociativeIteratorAccess(
                m_internalToExternalConverter,
                m_increment,
                m_decrement,
                m_value,
                m_lessThan,
                m_equals,
                m_keyInternalToExternalConverter,
                m_key,
                m_valueExternalToInternalConverter,
                m_setValue);
    return access;
}

void AutoAssociativeIteratorAccess::setValue(JNIEnv * env, void* iterator, jobject newValue){
    void* newval = m_setValue(iterator);
    jvalue jval;
    jval.l = newValue;
    m_valueExternalToInternalConverter(env, nullptr, jval, newval, jValueType::l);
}

jobject AutoAssociativeIteratorAccess::value(JNIEnv * env, const void* iterator){return AutoSequentialConstIteratorAccess::value(env, iterator);}
void AutoAssociativeIteratorAccess::increment(JNIEnv * env, void* iterator){AutoSequentialConstIteratorAccess::increment(env, iterator);}
void AutoAssociativeIteratorAccess::decrement(JNIEnv * env, void* iterator){AutoSequentialConstIteratorAccess::decrement(env, iterator);}
jboolean AutoAssociativeIteratorAccess::lessThan(JNIEnv * env, const void* iterator, const void* other){return AutoSequentialConstIteratorAccess::lessThan(env, iterator, other);}
bool AutoAssociativeIteratorAccess::canLess(){return AutoSequentialConstIteratorAccess::canLess();}
jboolean AutoAssociativeIteratorAccess::equals(JNIEnv * env, const void* iterator, const void* other){return AutoSequentialConstIteratorAccess::equals(env, iterator, other);}
jobject AutoAssociativeIteratorAccess::key(JNIEnv * env, const void* iterator){return AutoAssociativeConstIteratorAccess::key(env, iterator);}
