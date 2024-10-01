package org.vxwo.free.starkeyword.internal;

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
        if (osName.equals("win32")) {
            libFile = libname + ".dll";
        } else if (osName.equals("darwin")) {
            libFile = "lib" + libname + ".dylib";
        } else {
            libFile = "lib" + libname + ".so";
        }

        String filePath = "/jni/" + libFile;
        String resourcePath = "/jni/" + osName + "/" + getArch() + "/" + libFile;

        File outFile = null;
        InputStream in = null;
        try {
            in = NativeLoader.class.getResourceAsStream(resourcePath);
            if (in != null) {
                outFile =
                        new File(System.getProperty("java.io.tmpdir") + resourceToSystem(filePath));
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }
                Files.copy(in, outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Throwable ex) {
            outFile = null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (outFile != null) {
            System.load(outFile.getPath());
        } else {
            System.loadLibrary(libname);
        }
    }
}
