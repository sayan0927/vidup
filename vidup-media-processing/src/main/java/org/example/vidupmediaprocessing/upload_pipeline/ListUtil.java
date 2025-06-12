package org.example.vidupmediaprocessing.upload_pipeline;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static  <T> List<List<T>> partitionListIntoChunks(List<T> list, int chunkSize)
    {
        List<List<T>> result = new ArrayList<>();

        int start = 0;

        while(start < list.size())
        {
            int rightBound  = start + chunkSize;
            List<T> chunk = list.subList(start, Math.min(rightBound, list.size()));
            result.add(chunk);
            start = rightBound;
        }

        return result;
    }
}
