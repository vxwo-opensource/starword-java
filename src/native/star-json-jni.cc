#include <jni.h>

#include <vector>

#include "core/src/star_json.h"

extern "C" JNIEXPORT jlong JNICALL
Java_org_vxwo_jni_starjson_StarJsonEngine_nativeCreate(JNIEnv *env,
                                                       jobject thisObject,
                                                       jobjectArray keywords,
                                                       jboolean skipNumber,
                                                       jint border) {
  StarJson *starJson = new StarJson(skipNumber, border);
  jsize count = env->GetArrayLength(keywords);
  for (int i = 0; i < count; ++i) {
    jstring keyword = (jstring)env->GetObjectArrayElement(keywords, i);
    const jchar *buffer = env->GetStringCritical(keyword, 0);
    starJson->AddPrefix((char16_t *)buffer, env->GetStringLength(keyword));
    env->ReleaseStringCritical(keyword, buffer);
  }
  return (jlong)starJson;
}

extern "C" JNIEXPORT void JNICALL
Java_org_vxwo_jni_starjson_StarJsonEngine_nativeCleanup(JNIEnv *env,
                                                        jobject thisObject,
                                                        jlong ptr) {
  delete (StarJson *)ptr;
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_vxwo_jni_starjson_StarJsonEngine_nativeProcess(JNIEnv *env,
                                                        jobject thisObject,
                                                        jlong ptr,
                                                        jstring content) {
  jsize length = env->GetStringLength(content);
  std::vector<char16_t> buffer(length + 1);
  env->GetStringRegion(content, 0, length, (jchar *)buffer.data());

  StarJson *starJson = (StarJson *)ptr;
  if (!starJson->ProcessBuffer(buffer.data(), length)) {
    return content;
  }

  return env->NewString((jchar *)buffer.data(), length);
}
