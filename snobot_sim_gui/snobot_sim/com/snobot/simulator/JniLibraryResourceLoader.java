package com.snobot.simulator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class JniLibraryResourceLoader
{
    private static final File TEMP_DIR;
	private static final Set<String> LOADED_LIBS;

    static
    {
        long rando = new Random().nextLong();
        TEMP_DIR = new File("temp/" + rando + "/");
        TEMP_DIR.mkdirs();
        TEMP_DIR.deleteOnExit();

		LOADED_LIBS = new HashSet<>();
    }
    
    public static boolean copyResourceFromJar(String aResourceName, File resourceFile) throws IOException
    {
    	boolean success = false;

        InputStream is = JniLibraryResourceLoader.class.getResourceAsStream(aResourceName);
        if (is != null)
        {

            // flag for delete on exit
            resourceFile.deleteOnExit();
            OutputStream os = new FileOutputStream(resourceFile);

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

            System.out.println("Copied resource to " + resourceFile.getAbsolutePath() + " from resource " + aResourceName);
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
        
    	if(copyResourceFromJar(aResourceName, resourceFile))
    	{
    		System.load(resourceFile.getAbsolutePath());
    	}
    }

	private static void loadLibrary(File aTempDir, String aLibraryName)
    {
		if (LOADED_LIBS.contains(aLibraryName)) {
			System.out.println("Already loaded " + aLibraryName);
			return;
		}

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
			resname += aLibraryName + ".dll";
        }
        else if (osname.startsWith("Mac"))
        {
			resname += aLibraryName + ".dylib";
        }
        else
        {
			resname += "lib" + aLibraryName + ".so";
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
				LOADED_LIBS.add(aLibraryName);
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
