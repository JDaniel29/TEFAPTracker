package jdaniel29.tefaptracker.data;

import android.os.Environment;

import java.io.File;

public class FileManager {

    public static File currentFileDir = new File(Environment.getExternalStorageDirectory() +
                                                    File.separator + "Food Tracker");
    public static File currentFile;

    private FileManager(){

    }

    public static void setupDirectory(){

        if(!currentFileDir.exists()){

            if(currentFileDir.mkdir()){
                System.out.println(currentFileDir.getPath() + " succesfully created.");
            } else {
                System.out.println(currentFileDir.getPath() + " creation failed.");
            }

        }


    }

    public static void writeFile(Commodity[] commodities) throws Exception{

    }

    public static Commodity[] readFile() throws Exception{


        return null;
    }




}
