package com.storage.SS;

import java.util.HashMap;

public class SSTableNameGenerator implements ISSTableNameGenerator{
    private HashMap<Integer, Integer> filesCount;

    @Override
    public String getName(int tableNumber)
    {
        var count = filesCount.get(tableNumber);
        if (count == null)
        {
            filesCount.put(tableNumber, 1);
            count = 0;
        }
        return "SSTable"+tableNumber+"."+count;
    }
}
