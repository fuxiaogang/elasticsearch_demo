package com.wlw.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author fuxg
 * @create 2017-04-10 19:23
 */
public class ScriptReader {

    private static String sortAllScript;
    private static String sortStoreScript;
    private static String sortWithdistanceScript;

    static {
        sortAllScript = read("sort_all.groovy");
    }

    public static String getSortAllScript() {
        return sortAllScript;
    }


    public static String read(String filename) {
        String filePath = ScriptReader.class.getClassLoader().getResource("script/" + filename).getPath();
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
