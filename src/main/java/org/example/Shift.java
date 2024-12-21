package org.example;

import java.io.*;
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

    static ValueStatistic<Integer> integerValueStatistic; //Статистика для целых чисел
    static ValueStatistic<Float> floatValueStatistic; //Статистика для чисел с плавающей точкой
    static ValueStatistic<String> stringValueStatistic; //Статистика для строк

    static boolean isFileIntegerCreated = false; //Файл для записи целых чисел был создан
    static boolean isFileFloatCreated = false; //Файл для записи чисел с плавающей точкой был создан
    static boolean isFileStringCreated = false; //Файл для записи строк был создан

    static FileWriter fileIntegerWrite; //Файл для записи целых чисел
    static FileWriter fileFloatWrite; //Файл для записи чисел с плавающей точкой
    static FileWriter fileStringWrite; //Файл для записи строк

    static String fileNameInteger; //Имя файла для записи целых чисел
    static String fileNameFloat; //Имя файла для записи чисел с плавающей точкой
    static String fileNameString; //Имя файла для записи строк

    static int valueInteger; // для возврата значения целого числа
    static float valueFloat; // для возврата числа с плавающей точкой

    /**
     * Аргументы командной строки
     * -a. Информация будет добавлена в результирующий файл
     * -s. Вывести краткую статистику
     * -l. Вывести полную статистику
     * -p. Префикс для файла с результатами (Например: -p output_)
     * -o. Путь к файлу с результатами (Например: -o c:/shift/test)
     */

    public static void main(String[] args) {
//        args = new String[]{"input.txt", "-p", "out-", "-l", "-o", "out"};
        parseArguments(args); // Распознать аргументы командной строки

        if (path.compareTo("") != 0 && path.substring(path.length() - 1).compareTo("/") != 0)
            path += "/";

        //Формирование полных имен файлов с результатами
        fileNameInteger = path + prefix + "integers.txt";
        fileNameFloat = path + prefix + "floats.txt";
        fileNameString = path + prefix + "strings.txt";

        if (isStatLong) isStatShort = false;

        //создание объектов для учета статистики по каждому типу данных
        integerValueStatistic = new ValueStatistic<Integer>(isStatShort, isStatLong); //для целых чисел
        floatValueStatistic = new ValueStatistic<Float>(isStatShort, isStatLong); //для чисел с плавающей точкой
        stringValueStatistic = new ValueStatistic<String>(isStatShort, isStatLong); //для строк

        for (String fileName : inputFiles)
            readFromFile(fileName); //чтение данных из файлов

        if (!isStatShort && !isStatLong) return; //статистику выводить не надо

        if (integerValueStatistic.getCount() > 0) { //есть статистика по целым числам
            System.out.println("Количество целых чисел: " + integerValueStatistic.getCount());
            if (isStatLong) {
                System.out.println("Сумма целых чисел: " + integerValueStatistic.getSum());
                System.out.println("Среднее арифметическое целых чисел: " +
                        (Integer) integerValueStatistic.getSum() / integerValueStatistic.getCount());
                System.out.println("Минимальное целое число: " + integerValueStatistic.getMin());
                System.out.println("Максимальное целое число: " + integerValueStatistic.getMax());
            }
        }

        if (floatValueStatistic.getCount() > 0) { //есть статистика по числам с плавающей точкой
            System.out.println("Количество чисел с плавающей точкой: " + floatValueStatistic.getCount());
            if (isStatLong) {
                System.out.println("Сумма чисел с плавающей точкой: " + floatValueStatistic.getSum());
                System.out.println("Среднее арифметическое целых чисел: " +
                        (Float) floatValueStatistic.getSum() / floatValueStatistic.getCount());
                System.out.println("Минимальное число с плавающей точкой: " + floatValueStatistic.getMin());
                System.out.println("Максимальное число с плавающей точкой: " + floatValueStatistic.getMax());
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
     * Метод читает исходные файлы построчно, сортирует данные по их типу, записывает в соответствующий
     * результирующий файл
     */
    public static void readFromFile(String filename) {
        FileReader reader;
        try {
            reader = new FileReader(filename);
        } catch (IOException e) {
            System.out.println("Ошибка открытия файла с исходными данными " + filename);
            return;
        }
        BufferedReader br = new BufferedReader(reader);
        String line;
        while (true) {
            try {
                line = br.readLine();
            } catch (IOException e) {
                break;
            }
            if (line == null) return;
            if (isInteger(line)) {
                integerValueStatistic.put(valueInteger);
                if (!isFileIntegerCreated) {
                    try {
                        fileIntegerWrite = new FileWriter(fileNameInteger, isAppend);
                    } catch (IOException e) {
                        System.out.println("Ошибка открытия файла " + fileNameInteger);
                        continue;
                    }
                    isFileIntegerCreated = true;
                }
                print(fileIntegerWrite, line);
            } else if (isFloat(line)) {
                floatValueStatistic.put(valueFloat);
                if (!isFileFloatCreated) {
                    try {
                        fileFloatWrite = new FileWriter(fileNameFloat, isAppend);
                    } catch (IOException e) {
                        System.out.println("Ошибка открытия файла " + fileNameFloat);
                        continue;
                    }
                    isFileFloatCreated = true;
                }
                print(fileFloatWrite, line);
            } else {
                if (!isFileStringCreated) {
                    try {
                        fileStringWrite = new FileWriter(fileNameString, isAppend);
                    } catch (IOException e) {
                        System.out.println("Ошибка открытия файла " + fileNameString);
                        continue;
                    }
                    isFileStringCreated = true;
                }
                stringValueStatistic.put(line);
                print(fileStringWrite, line);
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
            valueInteger = Integer.parseInt(value);
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
            valueFloat = Float.parseFloat(value);
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

    /**
     * Осуществляет запись данных в соответствующий файл
     */
    public static void print(FileWriter fileWriter, String value) {
        try {
            fileWriter.write(value + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println("Ошибка записи " + "\"" + value + "\"");
            return;
        }
    }
}