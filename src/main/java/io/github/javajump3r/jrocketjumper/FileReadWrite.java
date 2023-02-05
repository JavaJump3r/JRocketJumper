package io.github.javajump3r.jrocketjumper;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

public class FileReadWrite {
    public static void write(File file, String text) {
        try{
            file.getParentFile().mkdirs();
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] strToBytes = text.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String read(File file) {
        try{
            return new String(Files.readAllBytes(file.toPath()));
        }
        catch (Exception e)
        {
            write(file,"");
            return read(file);
        }
    }
    public static String readJson(File file){
        String str = read(file);
        if(str.equals("")){
            str="{"+str+"}";
        }
        return str;
    }
}
