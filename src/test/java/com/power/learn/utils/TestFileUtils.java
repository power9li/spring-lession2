package com.power.learn.utils;


import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by shenli on 2017/1/1.
 */
public class TestFileUtils {

    @Test
    public void testOverWrite() throws IOException {
        File f = new File("/tmp/testa.txt");
        FileUtils.writeStringToFile(f, "1");
        String str = FileUtils.readFileToString(f);
        Assert.assertEquals("1", str);
        FileUtils.writeStringToFile(f, "2");
        str = FileUtils.readFileToString(f);
        Assert.assertEquals("2", str);
        f.delete();
    }
}
