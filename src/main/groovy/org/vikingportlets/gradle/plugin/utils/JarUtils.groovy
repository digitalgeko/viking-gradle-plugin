package org.vikingportlets.gradle.plugin.utils

import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

/**
 * Created by mardo on 5/15/15.
 */
class JarUtils {

    public static int BUFFER_SIZE = 10240;
    
    static void createJarArchive(File sourceFile, File destFile) {

        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            // Open archive file
            FileOutputStream stream = new FileOutputStream(destFile);
            JarOutputStream out = new JarOutputStream(stream);

            sourceFile.eachFileRecurse { file ->
                if (file.exists() && !file.isDirectory()) {
                    def entryName = file.path.substring("$sourceFile.path/".length())

                    // Add archive entry
                    JarEntry jarAdd = new JarEntry(entryName);

                    jarAdd.setTime(file.lastModified());
                    out.putNextEntry(jarAdd);

                    // Write file to archive
                    FileInputStream fin = new FileInputStream(file);
                    while (true) {
                        int nRead = fin.read(buffer, 0, buffer.length);
                        if (nRead <= 0)
                            break;
                        out.write(buffer, 0, nRead);
                    }
                    fin.close();
                }
            }

            out.close();
            stream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex.getMessage());
        }
    }

}
