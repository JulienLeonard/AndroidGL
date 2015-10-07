package julienl.androidgl.utils;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by JulienL on 10/7/2015.
 */
public class Utils {

    public static <T> T lcircular(ArrayList<T> list, int index_) {
        int index = index_ % list.size();
        return list.get(index);
    }

    public static <T> ArrayList<Pair<T,T>> pairs(ArrayList<T> values) {
        ArrayList<Pair<T,T>> result = new ArrayList<Pair<T,T>>();

        for (int index = 0; index < values.size()-1; index += 1) {
            result.add(Pair.create(values.get(index),values.get(index+1)));

        }

        return result;
    }
}
