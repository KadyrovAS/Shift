package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс позволяет читать строки из файлов с исходными данными и
 * записывать информацию в файлы с результатами
 */
public class FileManager {
    private final List<BufferedReader> listBufferedReader;
    private final List<String> fileNames;
    private int manage;

    private static boolean isFileIntegerCreated = false;
    private static boolean isFileFloatCreated = false;
    private static boolean isFileStringCreated = false;

    private static boolean errorOpenFileInteger = false;
    private static boolean errorOpenFileFloat = false;
    private static boolean errorOpenFileString = false;

    /**
     * Метод последовательно перебирает файлы с исходной информацией и поочередно
     * читает по одной строке из каждого файла
     */
    FileManager(List<String> fileNames) {
        listBufferedReader = new ArrayList<>();
        this.fileNames = fileNames;
        for (String fileName: fileNames) {
            this.listBufferedReader.add(openFile(fileName));
        }
        this.manage = 0;
    }

    /**
     * Открывает файл с заданным именем и возвращает ссылку на открытый файл
     */
    private BufferedReader openFile(String fileName){
        FileReader reader;
        try {
            reader = new FileReader(fileName);
        } catch (IOException e) {
            System.out.println("Ошибка открытия файла с исходными данными " + fileName);
            return null;
        }
        return new BufferedReader(reader);
    }

    /**
     * Читает по одной строке из всех файлов в списке по очереди
     */
    public String getNext(){
        if (listBufferedReader.isEmpty()) return null;
        boolean[]wasHear = new boolean[listBufferedReader.size()];
        BufferedReader br;
        String line = "";
        int localManage = this.manage;
        while (true){
            if (localManage == listBufferedReader.size()) localManage = 0;
            if (wasHear[localManage]) return null;
            wasHear[localManage] = true;

            br = listBufferedReader.get(localManage);
            if (br == null){
                localManage++;
                continue;
            }
            try{
                line = br.readLine();
            }catch (IOException e){
                System.out.println("Ошибка чтения исходного файла " + fileNames.get(localManage));
                localManage++;
            }
            if (line == null){
                localManage ++;
                continue;
            }
            this.manage = localManage + 1;
            if (this.manage == listBufferedReader.size()) this.manage = 0;
            return line;
        }
    }

    /**
     * Осуществляет запись в заданный файл
     * При необходимости этот файл создается
     */
    public void writeToFile(String fileName, String value, boolean isAppend, String type){
        FileWriter fileWriter = null;
        boolean toConsole = false;

        if (type.compareTo("integer") == 0 && !isFileIntegerCreated) isFileIntegerCreated = true;
        if (type.compareTo("float") == 0 && !isFileFloatCreated) isFileFloatCreated = true;
        if (type.compareTo("string") == 0 && !isFileStringCreated) isFileStringCreated = true;


        try {
            fileWriter = new FileWriter(fileName, isAppend);
        }catch (IOException e){
            if (type.compareTo("integer") == 0 && !errorOpenFileInteger){
                errorOpenFileInteger = true;
                toConsole = true;
            } else if (type.compareTo("float") == 0 && !errorOpenFileFloat) {
                errorOpenFileFloat = true;
                toConsole = true;
            } else if (type.compareTo("string") == 0 && !errorOpenFileString) {
                errorOpenFileString = true;
                toConsole = true;
            }
            if (toConsole){
                System.out.println("Ошибка создания файла " + fileName);
                return;
            }
        }

        try{
            fileWriter.write(value + "\n");
            fileWriter.flush();
        }catch (IOException e){
            System.out.println("Ошибка записи " + "\"" + value + "\" в файл " + fileName);
            return;
        }
    }
}
