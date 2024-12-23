> # Инструкция для работы с Shift.jar
При запуске утилиты в командной строке подается несколько файлов, содержащих в
перемешку целые числа, строки и вещественные числа. В качестве разделителя
используется перевод строки. Строки из файлов читаются по очереди в соответствии с их
перечислением в командной строке.

Утилита записывает разные типы данных в разные файлы. Целые числа в один
выходной файл, вещественные в другой, строки в третий. По умолчанию файлы с
результатами располагаются в текущей папке с именами _integers.txt_, _floats.txt_, _strings.txt_.
Дополнительно с помощью опции **-o** можно задавать путь для результатов. Опция **-p**
задает префикс имен выходных файлов. Например **-o** _/some/path -p result-_ задают вывод в
файлы _/some/path/result-integers.txt_, _/some/path/result_strings.txt_ и тд.

По умолчанию файлы результатов перезаписываются. С помощью опции **-a** можно задать
режим добавления в существующие файлы.

Если какого-то типа данных во входящих файлах нет, то соответствующие выходные файлы не создаются.

В процессе фильтрации данных собирается статистика по каждому типу данных.
Статистика поддерживается двух видов: _краткая_ и _полная_. Выбор статистики
производится опциями **-s** и **-f** соответственно. Краткая статистика содержит только
количество элементов записанных в исходящие файлы. Полная статистика для чисел
дополнительно содержит минимальное и максимальное значения, сумма и среднее.
Полная статистика для строк, помимо их количества, содержит также размер самой
короткой строки и самой длинной.

Статистику по каждому типу отфильтрованных данных утилита выводит в консоль.

Все возникающие ошибки также выводятся в консоль.