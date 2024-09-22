package org.vxwo.starkeyword.internal;

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

    private static String getOs() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            return "win32";
        } else if (osName.contains("mac")) {
            return "darwin";
        } else if (osName.contains("linux")) {
            return "linux";
        } else {
            return osName;
        }
    }

    private static String getArch() {
        String osArch = System.getProperty("os.arch").toLowerCase();
        if (osArch.equals("amd64")) {
            return "x86_64";
        } else if (osArch.equals("arm64")) {
            return "aarch64";
        } else {
            return osArch;
        }
    }

    public static void loadLibrary(String libname) {
        String libFile = null;
        String osName = getOs();
        if (osName.contains("windows")) {
            libFile = libname + ".dll";
        } else if (osName.contains("mac")) {
            libFile = "lib" + libname + ".dylib";
        } else {
            libFile = "lib" + libname + ".so";
        }

        String targetDir = System.getProperty("java.io.tmpdir");
        String resourcePath = "/jni/" + osName + "/" + getArch() + "/" + libFile;

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
