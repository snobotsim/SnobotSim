package com.snobot.simulator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import edu.wpi.first.wpiutil.RuntimeDetector;

public class JniLibraryResourceLoader
{
    private static final File TEMP_DIR_ROOT;
    private static final File TEMP_DIR;
    private static final Set<String> LOADED_LIBS;

    static
    {
        TEMP_DIR_ROOT = new File("temp");
        removeOldLibraries(TEMP_DIR_ROOT, "");

        long rando = new Random().nextLong();
        TEMP_DIR = new File(TEMP_DIR_ROOT, "" + rando);
        TEMP_DIR.mkdirs();
        TEMP_DIR.deleteOnExit();

        LOADED_LIBS = new HashSet<>();
    }

    private static void removeOldLibraries(File f, String indent)
    {
        if (f.isDirectory())
        {
            for (File childFile : f.listFiles())
            {
                removeOldLibraries(childFile, indent + " ");
            }
        }

        f.delete();
    }

    public static boolean copyResourceFromJar(String aResourceName, File aResourceFile) throws IOException
    {
        return copyResourceFromJar(aResourceName, aResourceFile, true);
    }

    public static boolean copyResourceFromJar(String aResourceName, File aResourceFile, boolean aDeleteOnExit) throws IOException
    {
        boolean success = false;

        InputStream is = JniLibraryResourceLoader.class.getResourceAsStream(aResourceName);
        if (is != null)
        {

            // flag for delete on exit
            if (aDeleteOnExit)
            {
                aResourceFile.deleteOnExit();
            }
            OutputStream os = new FileOutputStream(aResourceFile);

            byte[] buffer = new byte[1024];
            int readBytes;
            try
            {
                while ((readBytes = is.read(buffer)) != -1)
                {
                    os.write(buffer, 0, readBytes);
                }

                success = true;
            }
            finally
            {
                os.close();
                is.close();
            }

            System.out.println("Copied resource to " + aResourceFile.getAbsolutePath() + " from resource " + aResourceName);
        }
        else
        {
            System.err.println("Could not find resource at " + aResourceName);
        }

        return success;
    }

    private static void createAndLoadTempLibrary(File aTempDir, String aResourceName) throws IOException
    {
        String fileName = aResourceName.substring(aResourceName.lastIndexOf("/") + 1);
        File resourceFile = new File(aTempDir, fileName);

        if (copyResourceFromJar(aResourceName, resourceFile))
        {
            System.load(resourceFile.getAbsolutePath());
        }
    }

    private static void loadLibrary(File aTempDir, String aLibraryName)
    {
        if (LOADED_LIBS.contains(aLibraryName))
        {
            // System.out.println("Already loaded " + aLibraryName);
            return;
        }
        String resname = RuntimeDetector.getLibraryResource(aLibraryName);

        try
        {
            createAndLoadTempLibrary(aTempDir, resname);
            LOADED_LIBS.add(aLibraryName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Could not load " + resname);
        }
    }

    public static void loadLibrary(String aLibraryname)
    {
        loadLibrary(TEMP_DIR, aLibraryname);
    }
}
