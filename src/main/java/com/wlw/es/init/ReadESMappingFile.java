package com.wlw.es.init;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author fuxg
 * @create 2017-04-18 15:54
 */
public class ReadESMappingFile {

    public static String read(String filename) {
        String filePath = ReadESMappingFile.class.getClassLoader().getResource("index/" + filename).getPath();
        File file = new File(filePath);
        LineIterator iterator = null;
        StringBuilder sb = new StringBuilder();
        try {
            iterator = FileUtils.lineIterator(file, "utf-8");
            while (iterator.hasNext()) {
                sb.append(iterator.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (iterator != null) {
                LineIterator.closeQuietly(iterator);
            }
        }
        return StringUtils.trim(sb.toString());
    }
}
