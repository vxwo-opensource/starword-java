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

    public static void loadLibrary(String libname) {
        String sysName = null;
        String libFile = null;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            sysName = "win32";
            libFile = libname + ".dll";
        } else if (osName.contains("mac")) {
            sysName = "darwin";
            libFile = "lib" + libname + ".dylib";
        } else {
            sysName = "linux";
            libFile = "lib" + libname + ".so";
        }

        String targetDir = System.getProperty("java.io.tmpdir");
        String resourcePath = "/native/" + libname + "/" + sysName + "/" + libFile;

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
