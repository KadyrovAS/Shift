package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Shift {
    public static void main(String[] args) {
        for (String arg: args)
        {
             readFromFile(arg);
        }
    }



    //Читает файл и формирует output
    public static void readFromFile(String fileName)
    {
        try(FileReader reader = new FileReader(fileName))
        {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while (bufferedReader.ready())
            {
                line = bufferedReader.readLine();
                System.out.println(line);
            }
        }
        catch (IOException ex)
        {
            System.out.println("Error read " + fileName);
        }
    }

}
