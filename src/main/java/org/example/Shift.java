package org.example;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс читает все исходные данные, определяет их тип и публикует статистику
 */
public class Shift {
    //Глобальные переменные
    static String prefix = ""; //Опция -p Префикс для файлов с результатами
    static String path = ""; //Опция -o. Путь к файлу с результатами
    static Boolean isAppend = false; //Опция -a. true - результирующий файл дополняется новой записью
    static boolean isStatShort = false; //Опция -s. Требуется краткая статистика
    static boolean isStatLong = false; //Опция -l. Требуется полная статистика
    static List<String> inputFiles = new ArrayList<>(); //Список файлов с исходными данными

    static ValueStatistic<BigInteger> integerValueStatistic; //Статистика для целых чисел
    static ValueStatistic<BigDecimal> floatValueStatistic; //Статистика для чисел с плавающей точкой
    static ValueStatistic<String> stringValueStatistic; //Статистика для строк

    static String fileNameInteger; //Имя файла для записи целых чисел
    static String fileNameFloat; //Имя файла для записи чисел с плавающей точкой
    static String fileNameString; //Имя файла для записи строк

    static BigInteger valueInteger; // для возврата значения целого числа
    static BigDecimal valueFloat; // для возврата числа с плавающей точкой

    /**
     * Аргументы командной строки
     * -a. Информация будет добавлена в результирующий файл
     * -s. Вывести краткую статистику
     * -l. Вывести полную статистику
     * -p. Префикс для файла с результатами (Например: -p output_)
     * -o. Путь к файлу с результатами (Например: -o c:/shift/test)
     */

    public static void main(String[] args) {
        String line;
        parseArguments(args); // Распознать аргументы командной строки

        if (path.compareTo("") != 0 && path.substring(path.length() - 1).compareTo("/") != 0)
            path += "/";

        //Формирование полных имен файлов с результатами
        fileNameInteger = path + prefix + "integers.txt";
        fileNameFloat = path + prefix + "floats.txt";
        fileNameString = path + prefix + "strings.txt";

        if (isStatLong) isStatShort = false;

        //создание объектов для учета статистики по каждому типу данных
        integerValueStatistic = new ValueStatistic<BigInteger>(isStatShort, isStatLong); //для целых чисел
        floatValueStatistic = new ValueStatistic<BigDecimal>(isStatShort, isStatLong); //для вещественных чисел
        stringValueStatistic = new ValueStatistic<String>(isStatShort, isStatLong); //для строк

        FileManager fileManager = new FileManager(inputFiles);
        line = fileManager.getNext();
        while (line != null){
            if (isInteger(line)){
                integerValueStatistic.put(valueInteger);
                fileManager.writeToFile(fileNameInteger, line, isAppend, "integer");
            }
            else if(isFloat(line)){
                floatValueStatistic.put(valueFloat);
                fileManager.writeToFile(fileNameFloat, line, isAppend, "float");
            }
            else{
                stringValueStatistic.put(line);
                fileManager.writeToFile(fileNameString, line, isAppend, "string");
            }
            line = fileManager.getNext();
        }

        if (!isStatShort && !isStatLong) return; //статистику выводить не надо

        if (integerValueStatistic.getCount() > 0) { //есть статистика по целым числам
            System.out.println("Количество целых чисел: " + integerValueStatistic.getCount());
            if (isStatLong) {
                System.out.println("Сумма целых чисел: " + integerValueStatistic.getSum());
                System.out.println("Среднее арифметическое целых чисел: " +
                        integerValueStatistic.getAverrage().toString());
                System.out.println("Минимальное целое число: " + integerValueStatistic.getMin());
                System.out.println("Максимальное целое число: " + integerValueStatistic.getMax());
                System.out.println();
            }
        }

        if (floatValueStatistic.getCount() > 0) { //есть статистика по числам с плавающей точкой
            System.out.println("Количество чисел с плавающей точкой: " + floatValueStatistic.getCount());
            if (isStatLong) {
                System.out.println("Сумма чисел с плавающей точкой: " + floatValueStatistic.getSum());
                System.out.println("Среднее арифметическое целых чисел: " +
                        floatValueStatistic.getAverrage().toString());
                System.out.println("Минимальное число с плавающей точкой: " + floatValueStatistic.getMin());
                System.out.println("Максимальное число с плавающей точкой: " + floatValueStatistic.getMax());
                System.out.println();
            }
        }

        if (stringValueStatistic.getCount() > 0) { //есть статистика по строкам
            System.out.println("Количество строк: " + stringValueStatistic.getCount());
            if (isStatLong) {
                System.out.println("Размер самой короткой строки: " + stringValueStatistic.getMin());
                System.out.println("Размер самой длинной строки: " + stringValueStatistic.getMax());
            }
        }

    }


    /**
     * Проверяет наличие элемента String в массиве
     * Если такой элемент есть, возвращает true
     */
    public static boolean inArray(String value, String[] arr) {
        for (String arg : arr) {
            if (value.compareTo(arg) == 0) return true;
        }
        return false;
    }


    /**
     * Проверяет, является ли входной параметр целым числом
     */
    public static boolean isInteger(String value) {
        try {
            valueInteger = new BigInteger(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


    /**
     * Проверяет, является ли входной параметр числом с плавающей точкой
     */
    public static boolean isFloat(String value) {
        try {
            valueFloat = new BigDecimal(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Парсит командную строку и обновляет глобальные переменные
     */
    public static void parseArguments(String[] args) {
        int position = 0;
        String[] shortArgs = {"-p", "-o", "-a", "-s", "-l"};

        while (position < args.length) {
            if (args[position].compareTo("-a") == 0) isAppend = true;
            else if (args[position].compareTo("-s") == 0) isStatShort = true;
            else if (args[position].compareTo("-l") == 0) isStatLong = true;
            else if (args[position].compareTo("-p") == 0) {
                position++;
                if (position == args.length || inArray(args[position], shortArgs)) {
                    prefix = "";
                    break;
                }
                prefix = args[position];
            } else if (args[position].compareTo("-o") == 0) {
                position++;
                if (position == args.length || inArray(args[position], shortArgs)) {
                    path = "";
                    break;
                }
                path = args[position];
            } else {
                //Входной файл
                inputFiles.add(args[position]);
            }
            position++;
        }
    }

}