package com.magic.framework.utils;


import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {

    public static void main(String[] args) throws Exception {
        String packageName = "com.magic.common.utils";
        String suffixPath = packageName.replaceAll("\\.", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = loader.getResources(suffixPath);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            System.out.println(url);
            if ("file".equals(url.getProtocol())) {
                File file = new File(url.getPath());
                File[] files = file.listFiles();
                System.out.println(Arrays.toString(files));
                System.out.println(packageName + "." + files[0].getName().split("\\.")[0]);
                Class clazz = Class.forName(packageName + "." + files[0].getName().split("\\.")[0]);
                System.out.println(clazz);
            } else if ("jar".equals(url.getProtocol())) {
                JarFile file = ((JarURLConnection) url.openConnection()).getJarFile();
                Enumeration<JarEntry> entries = file.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    System.out.println(entry);
                }
            }

        }
    }
}
