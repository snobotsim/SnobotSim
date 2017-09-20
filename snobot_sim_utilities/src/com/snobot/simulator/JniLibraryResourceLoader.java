package com.snobot.simulator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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

            Logger.getLogger(JniLibraryResourceLoader.class).log(Level.DEBUG,
                    "Copied resource to " + aResourceFile.getAbsolutePath() + " from resource " + aResourceName);
        }
        else
        {
            System.err.println("Could not find resource at " + aResourceName);
        }

        return success;
    }

    private static boolean createAndLoadTempLibrary(File aTempDir, String aResourceName) throws IOException
    {
        String fileName = aResourceName.substring(aResourceName.lastIndexOf("/") + 1);
        File resourceFile = new File(aTempDir, fileName);
        boolean success = false;

        if (copyResourceFromJar(aResourceName, resourceFile))
        {
            System.load(resourceFile.getAbsolutePath());
            success = true;
        }

        return success;
    }

    private static boolean loadLibrary(File aTempDir, String aLibraryName)
    {
        if (LOADED_LIBS.contains(aLibraryName))
        {
            Logger.getLogger(JniLibraryResourceLoader.class).log(Level.TRACE, "Already loaded " + aLibraryName);
            return true;
        }

        boolean output = false;
        String resname = RuntimeDetector.getLibraryResource(aLibraryName);

        try
        {
            output = createAndLoadTempLibrary(aTempDir, resname);
            if (output)
            {
                LOADED_LIBS.add(aLibraryName);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Could not load " + resname);
        }

        return output;
    }

    public static boolean loadLibrary(String aLibraryname)
    {
        return loadLibrary(TEMP_DIR, aLibraryname);
    }
}
