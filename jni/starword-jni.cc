#include <jni.h>

#include <vector>

#include "core/src/star_base.h"
#include "core/src/star_json.h"
#include "core/src/star_text.h"

static void ParseObjectToOptions(JNIEnv *env, jobject optionsObject,
                                 StarOptions &options) {
  jclass optionsClass = env->GetObjectClass(optionsObject);
  jmethodID iicMid = env->GetMethodID(optionsClass, "isIgnoreCase", "()Z");
  options.ignore_case = (jboolean)env->CallBooleanMethod(optionsObject, iicMid);
  jmethodID glbMid = env->GetMethodID(optionsClass, "getLeftBorder", "()I");
  options.left_border = (jboolean)env->CallBooleanMethod(optionsObject, glbMid);
  jmethodID grbMid = env->GetMethodID(optionsClass, "getRightBorder", "()I");
  options.right_border =
      (jboolean)env->CallBooleanMethod(optionsObject, grbMid);
}

extern "C" JNIEXPORT jlong JNICALL
Java_org_vxwo_free_starword_internal_NativeEngine_starJsonCreate(
    JNIEnv *env, jobject thisObject, jobjectArray keywords, jboolean skipNumber,
    jobject optionsObject) {
  StarOptions options{false, 0, 0};
  ParseObjectToOptions(env, optionsObject, options);

  StarJson *starJson = new StarJson(options, skipNumber);
  jsize count = env->GetArrayLength(keywords);
  for (int i = 0; i < count; ++i) {
    jstring keyword = (jstring)env->GetObjectArrayElement(keywords, i);
    const jchar *buffer = env->GetStringCritical(keyword, 0);
    starJson->AddWord((skchar_t *)buffer, env->GetStringLength(keyword));
    env->ReleaseStringCritical(keyword, buffer);
    env->DeleteLocalRef(keyword);
  }
  starJson->FinishAdd();

  return (jlong)starJson;
}

extern "C" JNIEXPORT void JNICALL
Java_org_vxwo_free_starword_internal_NativeEngine_starJsonCleanup(
    JNIEnv *env, jobject thisObject, jlong ptr) {
  delete (StarJson *)ptr;
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_vxwo_free_starword_internal_NativeEngine_starJsonProcess(
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
Java_org_vxwo_free_starword_internal_NativeEngine_starTextCreate(
    JNIEnv *env, jobject thisObject, jobjectArray keywords,
    jobject optionsObject) {
  StarOptions options{false, 0, 0};
  ParseObjectToOptions(env, optionsObject, options);

  StarText *starText = new StarText(options);
  jsize count = env->GetArrayLength(keywords);
  for (int i = 0; i < count; ++i) {
    jstring keyword = (jstring)env->GetObjectArrayElement(keywords, i);
    const jchar *buffer = env->GetStringCritical(keyword, 0);
    starText->AddWord((skchar_t *)buffer, env->GetStringLength(keyword));
    env->ReleaseStringCritical(keyword, buffer);
    env->DeleteLocalRef(keyword);
  }
  starText->FinishAdd();

  return (jlong)starText;
}

extern "C" JNIEXPORT void JNICALL
Java_org_vxwo_free_starword_internal_NativeEngine_starTextCleanup(
    JNIEnv *env, jobject thisObject, jlong ptr) {
  delete (StarText *)ptr;
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_vxwo_free_starword_internal_NativeEngine_starTextProcess(
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
