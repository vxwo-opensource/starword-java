package org.vxwo.jni.starjson.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class NativeLoader {
    private static String resourceToSystem(String resourcePath) {
        return File.separatorChar == '/' ? resourcePath
                : resourcePath.replace('/', File.separatorChar);
    }

    public static void loadLibrary(String libname) {
        String extName = null;
        String sysName = null;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            extName = ".dll";
            sysName = "win32";
        } else if (osName.contains("mac")) {
            extName = ".jnilib";
            sysName = "darwin";
        } else {
            extName = ".so";
            sysName = "linux";
        }

        String targetDir = System.getProperty("java.io.tmpdir");

        String resourcePath = "/native/starjson/" + sysName + "/" + libname + extName;
        File tempFile = new File(targetDir + resourceToSystem(resourcePath));
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }

        try (InputStream in = NativeLoader.class.getResourceAsStream(resourcePath)) {
            Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
        }

        System.load(tempFile.getPath());
    }
}
