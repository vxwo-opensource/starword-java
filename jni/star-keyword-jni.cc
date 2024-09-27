#include <jni.h>

#include <vector>

#include "core/src/star_json.h"
#include "core/src/star_text.h"

extern "C" JNIEXPORT jlong JNICALL
Java_org_vxwo_starkeyword_internal_NativeEngine_starJsonCreate(
    JNIEnv *env, jobject thisObject, jobjectArray keywords, jboolean ignoreCase,
    jboolean skipNumber, jint leftBorder, jint rightBorder) {
  StarJson *starJson =
      new StarJson(ignoreCase, skipNumber, leftBorder, rightBorder);
  jsize count = env->GetArrayLength(keywords);
  for (int i = 0; i < count; ++i) {
    jstring keyword = (jstring)env->GetObjectArrayElement(keywords, i);
    const jchar *buffer = env->GetStringCritical(keyword, 0);
    starJson->AddKeyword((skchar_t *)buffer, env->GetStringLength(keyword));
    env->ReleaseStringCritical(keyword, buffer);
    env->DeleteLocalRef(keyword);
  }
  return (jlong)starJson;
}

extern "C" JNIEXPORT void JNICALL
Java_org_vxwo_starkeyword_internal_NativeEngine_starJsonCleanup(
    JNIEnv *env, jobject thisObject, jlong ptr) {
  delete (StarJson *)ptr;
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_vxwo_starkeyword_internal_NativeEngine_starJsonProcess(
    JNIEnv *env, jobject thisObject, jlong ptr, jstring content) {
  jsize length = env->GetStringLength(content);
  std::vector<skchar_t> buffer(length + 1);
  env->GetStringRegion(content, 0, length, (jchar *)buffer.data());

  StarJson *starJson = (StarJson *)ptr;
  if (!starJson->ProcessBuffer(buffer.data(), length)) {
    return content;
  }

  return env->NewString((jchar *)buffer.data(), length);
}

extern "C" JNIEXPORT jlong JNICALL
Java_org_vxwo_starkeyword_internal_NativeEngine_starTextCreate(
    JNIEnv *env, jobject thisObject, jobjectArray keywords, jboolean ignoreCase,
    jint leftBorder, jint rightBorder) {
  StarText *starText = new StarText(ignoreCase, leftBorder, rightBorder);
  jsize count = env->GetArrayLength(keywords);
  for (int i = 0; i < count; ++i) {
    jstring keyword = (jstring)env->GetObjectArrayElement(keywords, i);
    const jchar *buffer = env->GetStringCritical(keyword, 0);
    starText->AddKeyword((skchar_t *)buffer, env->GetStringLength(keyword));
    env->ReleaseStringCritical(keyword, buffer);
    env->DeleteLocalRef(keyword);
  }
  return (jlong)starText;
}

extern "C" JNIEXPORT void JNICALL
Java_org_vxwo_starkeyword_internal_NativeEngine_starTextCleanup(
    JNIEnv *env, jobject thisObject, jlong ptr) {
  delete (StarText *)ptr;
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_vxwo_starkeyword_internal_NativeEngine_starTextProcess(
    JNIEnv *env, jobject thisObject, jlong ptr, jstring content) {
  jsize length = env->GetStringLength(content);
  std::vector<skchar_t> buffer(length + 1);
  env->GetStringRegion(content, 0, length, (jchar *)buffer.data());

  StarText *starText = (StarText *)ptr;
  if (!starText->ProcessBuffer(buffer.data(), length)) {
    return content;
  }

  return env->NewString((jchar *)buffer.data(), length);
}
