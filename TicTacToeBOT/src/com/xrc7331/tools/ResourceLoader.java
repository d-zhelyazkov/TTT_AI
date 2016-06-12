package com.xrc7331.tools;

import java.io.File;
import java.net.URL;

/**
 * Created by XRC_7331 on 3/8/2016.
 */
public class ResourceLoader {
    private static ResourceLoader INSTANCE = new ResourceLoader();

    public static ResourceLoader getInstance() {
        return INSTANCE;
    }

    private ResourceLoader() {}

    public File[] loadResourceFiles(final String resourceName) {
        URL resource = loadResource(resourceName);
        File resourceFolder = new File(resource.getPath());
        return resourceFolder.listFiles();
    }

    public URL loadResource(final String resourceName) {
        return this.getClass().getClassLoader().getResource(resourceName);
    }
}
