package org.example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

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
    }

    /**
     * Метод рассчитывает статистическую информацию по каждому типу данных
     */
    void put(T value)
    {
        if (statShort || statLong) this.countValue ++;
        if (!statLong) return;

        String valueType = value.getClass().getTypeName();
        if (valueType.indexOf("BigInteger") > 0) { //Передано целое число
            BigInteger localSum = (BigInteger) this.sumValue;
            BigInteger localValue = (BigInteger) value;
            BigInteger localMin = (BigInteger) this.minValue;
            BigInteger localMax = (BigInteger) this.maxValue;


            if (this.sumValue == null) localSum = localValue;
            else localSum = localSum.add(localValue);
            this.sumValue = (Number) localSum;

            if (this.minValue == null) localMin = localValue;
            else if (localValue.compareTo(localMin) < 0) localMin = localValue;
            this.minValue = (Number) localMin;

            if (this.maxValue == null) localMax = localValue;
            else if (localValue.compareTo(localMax) > 0) localMax = localValue;
            this.maxValue = (Number) localMax;
        }
        else if (valueType.indexOf("BigDecimal") > 0) {
            BigDecimal localSum = (BigDecimal) this.sumValue;
            BigDecimal localValue = (BigDecimal) value;
            BigDecimal localMin = (BigDecimal) this.minValue;
            BigDecimal localMax = (BigDecimal) this.maxValue;

            if (this.sumValue == null) localSum = localValue;
            else localSum = localSum.add(localValue);
            this.sumValue = (Number) localSum;

            if (this.minValue == null) localMin = localValue;
            else if (localValue.compareTo(localMin) < 0) localMin = localValue;
            this.minValue = (Number) localMin;

            if (this.maxValue == null) localMax = localValue;
            else if (localValue.compareTo(localMax) > 0) localMax = localValue;
            this.maxValue = (Number) localMax;
        }
        else if (valueType.indexOf("String") > 0) {
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

    public BigDecimal getAverrage(){
        BigDecimal sum = new BigDecimal(this.sumValue.toString());
        BigDecimal count = new BigDecimal(this.countValue);
        return sum.divide(count, RoundingMode.HALF_UP);
    }
}