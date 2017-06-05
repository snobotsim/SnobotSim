package com.snobot.simulator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class JniLibraryResourceLoader
{
    private static final File TEMP_DIR;

    static
    {
        long rando = new Random().nextLong();
        TEMP_DIR = new File("temp/" + rando + "/");
        TEMP_DIR.mkdirs();
        TEMP_DIR.deleteOnExit();

    }

    private static void createAndLoadTempLibrary(File aTempDir, String aResourceName) throws IOException
    {
        String fileName = aResourceName.substring(aResourceName.lastIndexOf("/") + 1);

        InputStream is = JniLibraryResourceLoader.class.getResourceAsStream(aResourceName);
        if (is != null)
        {
            File jniLibrary = new File(aTempDir, fileName);

            // flag for delete on exit
            jniLibrary.deleteOnExit();
            OutputStream os = new FileOutputStream(jniLibrary);

            byte[] buffer = new byte[1024];
            int readBytes;
            try
            {
                while ((readBytes = is.read(buffer)) != -1)
                {
                    os.write(buffer, 0, readBytes);
                }
            }
            finally
            {
                os.close();
                is.close();
            }

            System.out.println("Created temporary library at " + jniLibrary.getAbsolutePath() + " from resource " + aResourceName);
            System.load(jniLibrary.getAbsolutePath());
        }
        else
        {
        	System.err.println("Could not find resource at " + aResourceName);
        }
    }

    private static void loadLibrary(File aTempDir, String aLibraryname)
    {
        String osname = System.getProperty("os.name");
        String resname;
        if (osname.startsWith("Windows"))
        {
            resname = "/Windows/" + System.getProperty("os.arch") + "/";
        }
        else
        {
            resname = "/" + osname + "/" + System.getProperty("os.arch") + "/";
        }

        if (osname.startsWith("Windows"))
        {
            resname += aLibraryname + ".dll";
        }
        else if (osname.startsWith("Mac"))
        {
            resname += aLibraryname + ".dylib";
        }
        else
        {
            resname += "lib" + aLibraryname + ".so";
        }

        try
        {
            if (aTempDir == null)
            {
                File f = new File("../2017MockWpi/native_wpi_libs" + resname);
                System.out.println(f.getAbsolutePath());
                System.load(f.getAbsolutePath());
            }
            else
            {
                createAndLoadTempLibrary(aTempDir, resname);
            }
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
