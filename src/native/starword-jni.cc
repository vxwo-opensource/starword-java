#include <jni.h>

#include <vector>

#include "core/src/star_base.h"
#include "core/src/star_json.h"
#include "core/src/star_text.h"

static void ParseObjectToMethod(JNIEnv *env, jobject methodObject,
                                StarMethod &method) {
  jclass methodClass = env->GetObjectClass(methodObject);

  jmethodID glbMid = env->GetMethodID(methodClass, "getLeftBorder", "()I");
  method.left_border = env->CallIntMethod(methodObject, glbMid);

  jmethodID grbMid = env->GetMethodID(methodClass, "getRightBorder", "()I");
  method.right_border = env->CallIntMethod(methodObject, grbMid);

  jmethodID glbcMid =
      env->GetMethodID(methodClass, "getRightBorderChar", "()C");
  method.right_border_char = env->CallCharMethod(methodObject, glbcMid);
}

extern "C" JNIEXPORT jstring JNICALL
Java_org_vxwo_free_starword_internal_NativeEngine_starProcess(
    JNIEnv *env, jobject thisObject, jobject methodObject, jstring content) {
  StarMethod method;
  if (methodObject) {
    ParseObjectToMethod(env, methodObject, method);
  }

  jsize length = content == nullptr ? 0 : env->GetStringLength(content);
  if (length < 1) {
    return content;
  }

  std::vector<skchar_t> buffer(length + 1);
  env->GetStringRegion(content, 0, length, (jchar *)buffer.data());

  if (!StarBase::ProcessBuffer(buffer.data(), length, method)) {
    return content;
  }

  return env->NewString((jchar *)buffer.data(), length);
}

static void ParseObjectToJsonOptions(JNIEnv *env, jobject optionsObject,
                                     StarJsonOptions &options) {
  jclass optionsClass = env->GetObjectClass(optionsObject);

  jmethodID iicMid = env->GetMethodID(optionsClass, "isIgnoreCase", "()Z");
  options.ignore_case = env->CallBooleanMethod(optionsObject, iicMid);

  jmethodID gmMid = env->GetMethodID(optionsClass, "getMethod",
                                     "()Lorg/vxwo/free/starword/StarMethod;");
  jobject methodObject = env->CallObjectMethod(optionsObject, gmMid);
  ParseObjectToMethod(env, methodObject, options.method);
}

extern "C" JNIEXPORT jlong JNICALL
Java_org_vxwo_free_starword_internal_NativeEngine_starJsonCreate(
    JNIEnv *env, jobject thisObject, jobject optionsObject,
    jobjectArray keywordsArray, jobjectArray strategiesArray) {
  StarJsonOptions options{false, {0, 0, kNULL}};
  if (optionsObject) {
    ParseObjectToJsonOptions(env, optionsObject, options);
  }

  StarJson *starJson = new StarJson(options);
  jsize count =
      keywordsArray == nullptr ? 0 : env->GetArrayLength(keywordsArray);
  for (int i = 0; i < count; ++i) {
    jstring keyword = (jstring)env->GetObjectArrayElement(keywordsArray, i);
    if (keyword) {
      const jchar *buffer = env->GetStringCritical(keyword, 0);
      starJson->AddWord((skchar_t *)buffer, env->GetStringLength(keyword));
      env->ReleaseStringCritical(keyword, buffer);
      env->DeleteLocalRef(keyword);
    }
  }

  jsize sCount =
      strategiesArray == nullptr ? 0 : env->GetArrayLength(strategiesArray);
  for (int i = 0; i < sCount; ++i) {
    jobject strategyObject = env->GetObjectArrayElement(strategiesArray, i);
    if (!strategyObject) {
      continue;
    }

    jclass strategyClass = env->GetObjectClass(strategyObject);

    jmethodID gmMid = env->GetMethodID(strategyClass, "getMethod",
                                       "()Lorg/vxwo/free/starword/StarMethod;");
    jobject methodObject = env->CallObjectMethod(strategyObject, gmMid);

    StarMethod method{};
    if (methodObject) {
      ParseObjectToMethod(env, methodObject, method);
    }

    jmethodID gksMid =
        env->GetMethodID(strategyClass, "getKeywords", "()[Ljava/lang/String;");
    jobjectArray strategyKeywords =
        (jobjectArray)env->CallObjectMethod(strategyObject, gksMid);

    jsize skCount =
        strategyKeywords == nullptr ? 0 : env->GetArrayLength(strategyKeywords);
    for (int j = 0; j < skCount; ++j) {
      jstring keyword =
          (jstring)env->GetObjectArrayElement(strategyKeywords, j);
      if (keyword) {
        const jchar *buffer = env->GetStringCritical(keyword, 0);
        starJson->AddWord((skchar_t *)buffer, env->GetStringLength(keyword),
                          method);
        env->ReleaseStringCritical(keyword, buffer);
        env->DeleteLocalRef(keyword);
      }
    }

    env->DeleteLocalRef(strategyObject);
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
  jsize length = content == nullptr ? 0 : env->GetStringLength(content);
  if (length < 1) {
    return content;
  }

  std::vector<skchar_t> buffer(length + 1);
  env->GetStringRegion(content, 0, length, (jchar *)buffer.data());

  StarJson *starJson = (StarJson *)ptr;
  if (!starJson->ProcessBuffer(buffer.data(), length)) {
    return content;
  }

  return env->NewString((jchar *)buffer.data(), length);
}

static void ParseObjectToTextOptions(JNIEnv *env, jobject optionsObject,
                                     StarTextOptions &options) {
  jclass optionsClass = env->GetObjectClass(optionsObject);

  jmethodID iicMid = env->GetMethodID(optionsClass, "isIgnoreCase", "()Z");
  options.ignore_case = env->CallBooleanMethod(optionsObject, iicMid);

  jmethodID glbMid = env->GetMethodID(optionsClass, "getLeftBorder", "()I");
  options.left_border = env->CallIntMethod(optionsObject, glbMid);

  jmethodID grbMid = env->GetMethodID(optionsClass, "getRightBorder", "()I");
  options.right_border = env->CallIntMethod(optionsObject, grbMid);
}

extern "C" JNIEXPORT jlong JNICALL
Java_org_vxwo_free_starword_internal_NativeEngine_starTextCreate(
    JNIEnv *env, jobject thisObject, jobject optionsObject,
    jobjectArray keywordsArray) {
  StarTextOptions options{false, 0, 0};
  if (optionsObject) {
    ParseObjectToTextOptions(env, optionsObject, options);
  }

  StarText *starText = new StarText(options);
  jsize count =
      keywordsArray == nullptr ? 0 : env->GetArrayLength(keywordsArray);
  for (int i = 0; i < count; ++i) {
    jstring keyword = (jstring)env->GetObjectArrayElement(keywordsArray, i);
    if (keyword) {
      const jchar *buffer = env->GetStringCritical(keyword, 0);
      starText->AddWord((skchar_t *)buffer, env->GetStringLength(keyword));
      env->ReleaseStringCritical(keyword, buffer);
      env->DeleteLocalRef(keyword);
    }
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
  jsize length = content == nullptr ? 0 : env->GetStringLength(content);
  if (length < 1) {
    return content;
  }

  std::vector<skchar_t> buffer(length + 1);
  env->GetStringRegion(content, 0, length, (jchar *)buffer.data());

  StarText *starText = (StarText *)ptr;
  if (!starText->ProcessBuffer(buffer.data(), length)) {
    return content;
  }

  return env->NewString((jchar *)buffer.data(), length);
}
