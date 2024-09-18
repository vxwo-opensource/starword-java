#include "core/star_json.h"
#include <jni.h>
#include <vector>

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
    starJson->add_prefix((JWCharBuffer)buffer, env->GetStringLength(keyword));
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
  std::vector<JWChar> buffer(length + 1);
  env->GetStringRegion(content, 0, length, buffer.data());

  StarJson *starJson = (StarJson *)ptr;
  if (!starJson->process_buffer(buffer.data(), length)) {
    return content;
  }

  return env->NewString(buffer.data(), length);
}
