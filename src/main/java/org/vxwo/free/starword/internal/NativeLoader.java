package org.vxwo.free.starword.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class NativeLoader {
    private final static String BINARY_VERSION = "v20241115";


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

    private static String getTempFilePath(String resourcePath) {
        String filePath = File.separatorChar == '/' ? resourcePath
                : resourcePath.replace('/', File.separatorChar);

        return System.getProperty("java.io.tmpdir") + File.separatorChar + "starword-"
                + BINARY_VERSION + File.separatorChar + filePath;
    }

    public static void loadLibrary(String libname) {
        String libFile = null;
        String osName = getOs();
        if (osName.equals("win32")) {
            libFile = "lib" + libname + ".dll";
        } else if (osName.equals("darwin")) {
            libFile = "lib" + libname + ".dylib";
        } else {
            libFile = "lib" + libname + ".so";
        }

        String resourcePath = "/native/" + osName + "-" + getArch() + "/" + libFile;

        File targetFile = new File(getTempFilePath(resourcePath));
        try {
            System.load(targetFile.getPath());
        } catch (UnsatisfiedLinkError ex1) {
            InputStream in = null;
            try {
                in = NativeLoader.class.getResourceAsStream(resourcePath);
                if (in != null) {
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Throwable ex) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }

            System.load(targetFile.getPath());
        }
    }
}
