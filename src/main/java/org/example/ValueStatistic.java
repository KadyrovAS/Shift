package org.example;

/**
 * Класс для учета статистики по каждому типу данных
 */
public class ValueStatistic<T>{
    private final boolean statShort;
    private final boolean statLong;
    private int countValue; //Количество чисел
    private Number sumValue; //Сумма чисел
    private Number minValue; //Минимальное число или минимальная длина строки
    private Number maxValue; //Максимальное число или максимальная длина строки

    ValueStatistic(boolean statShort, boolean statLong)
    {
        this.statShort = statShort;
        this.statLong = statLong;
        this.countValue = 0;
        this.sumValue = 0;
    }

    /**
     * Метод рассчитывает статистическую информацию по каждому типу данных
     */
    void put(T value)
    {
        if (statShort || statLong) countValue ++;
        if (!statLong) return;

        String valueType = value.getClass().getTypeName();
        if (valueType.indexOf("Integer") > 0) //Передано целое число
        {
            sumValue = sumValue.intValue() + (Integer) value;
            if (minValue == null) minValue = (Integer) value;
            else if((Integer)value < (Integer) minValue) minValue = (Integer) value;
            if (maxValue == null) maxValue = (Integer) value;
            else if((Integer)value > (Integer) maxValue) maxValue = (Integer) value;
        }
        else if (valueType.indexOf("Float") > 0)
        {
            sumValue = sumValue.floatValue() + (Float) value;
            if (minValue == null) minValue = (Float) value;
            else if ((Float)value < (Float) minValue) minValue = (Float) value;
            if (maxValue == null) maxValue = (Float)value;
            else if((Float)value > (Float) maxValue) maxValue = (Float) value;
        }
        else if (valueType.indexOf("String") > 0)
        {
            if (minValue == null) minValue = ((String)value).length();
            else if (((String)value).length() < (Integer)minValue) minValue = ((String)value).length();
            if (maxValue == null) maxValue = ((String)value).length();
            else if (((String)value).length() > (Integer)maxValue) maxValue = ((String)value).length();
        }
    }

    /**
     * Метод возвращает количество элементов заданного типа
     */
    public int getCount(){
        return this.countValue;
    }

    /**
     * Метод возвращает сумму чисел
     */
    public Number getSum(){
        return this.sumValue;
    }

    /**
     * Метод возвращает минимальное число или размер самой короткой строки
     */
    public Number getMin(){
        return this.minValue;
    }

    /**
     * Метод возвращает максимальное число или размер самой длинной строки
     */
    public Number getMax(){
        return this.maxValue;
    }
}