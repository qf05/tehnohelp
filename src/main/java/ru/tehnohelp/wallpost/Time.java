package ru.tehnohelp.wallpost;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Time {

    private Map<Integer, Integer> addTime = new HashMap<>();
    private Map<Integer, Integer> randomTime = new HashMap<>();

    public Time(int dayAdd, int dayRandom, int hourAdd, int hourRandom, int minAdd, int minRandom, int secAdd, int secRandom) {
        addTime.put(Calendar.DAY_OF_YEAR,dayAdd);
        addTime.put(Calendar.HOUR_OF_DAY,hourAdd);
        addTime.put(Calendar.MINUTE,minAdd);
        addTime.put(Calendar.SECOND,secAdd);
        randomTime.put(Calendar.DAY_OF_YEAR,dayRandom);
        randomTime.put(Calendar.HOUR_OF_DAY,hourRandom);
        randomTime.put(Calendar.MINUTE,minRandom);
        randomTime.put(Calendar.SECOND,secRandom);
    }

    public Map<Integer, Integer> getAddTime() {
        return addTime;
    }

    public Map<Integer, Integer> getRandomTime() {
        return randomTime;
    }
}
