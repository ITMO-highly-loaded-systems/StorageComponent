package com.storage.SS;

import com.storage.SS.Interfaces.ISSTableNameGenerator;

import java.util.HashMap;

public class SSTableNameGenerator implements ISSTableNameGenerator {
    private final HashMap<Integer, Integer> filesCount;

    public SSTableNameGenerator() {
        filesCount = new HashMap<>();
    }

    @Override
    public String getName(int tableNumber)
    {
        var count = filesCount.get(tableNumber);
        if (count == null)
        {
            filesCount.put(tableNumber, 1);
            count = 0;
        }
        return "SSTable"+tableNumber+"."+count+".txt";
    }
}
