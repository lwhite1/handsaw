package com.github.lwhite1.tablesaw.index;

import com.github.lwhite1.tablesaw.api.DoubleColumn;
import com.github.lwhite1.tablesaw.util.BitmapBackedSelection;
import com.github.lwhite1.tablesaw.util.Selection;
import it.unimi.dsi.fastutil.doubles.Double2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * An index for four-byte floating point columns
 */
public class DoubleIndex {

    private final Double2ObjectAVLTreeMap<IntArrayList> index;

    public DoubleIndex(DoubleColumn column) {
        int sizeEstimate = Integer.min(1_000_000, column.size() / 100);
        Double2ObjectOpenHashMap<IntArrayList> tempMap = new Double2ObjectOpenHashMap<>(sizeEstimate);
        for (int i = 0; i < column.size(); i++) {
            double value = column.get(i);
            IntArrayList recordIds = tempMap.get(value);
            if (recordIds == null) {
                recordIds = new IntArrayList();
                recordIds.add(i);
                tempMap.trim();
                tempMap.put(value, recordIds);
            } else {
                recordIds.add(i);
            }
        }
        index = new Double2ObjectAVLTreeMap<>(tempMap);
    }

    private static void addAllToSelection(IntArrayList tableKeys, Selection selection) {
        for (int i : tableKeys) {
            selection.add(i);
        }
    }

    /**
     * Returns a bitmap containing row numbers of all cells matching the given int
     *
     * @param value This is a 'key' from the index perspective, meaning it is a value from the standpoint of the column
     */
    public Selection get(double value) {
        Selection selection = new BitmapBackedSelection();
        IntArrayList list = index.get(value);
        addAllToSelection(list, selection);
        return selection;
    }

    public Selection atLeast(float value) {
        Selection selection = new BitmapBackedSelection();
        Double2ObjectSortedMap<IntArrayList> tail = index.tailMap(value);
        for (IntArrayList keys : tail.values()) {
            addAllToSelection(keys, selection);
        }
        return selection;
    }

    public Selection greaterThan(float value) {
        Selection selection = new BitmapBackedSelection();
        Double2ObjectSortedMap<IntArrayList> tail = index.tailMap(value + 0.000001f);
        for (IntArrayList keys : tail.values()) {
            addAllToSelection(keys, selection);
        }
        return selection;
    }

    public Selection atMost(float value) {
        Selection selection = new BitmapBackedSelection();
        Double2ObjectSortedMap<IntArrayList> head = index.headMap(value + 0.000001f);  // we append 1 to get values equal
        // to the arg
        for (IntArrayList keys : head.values()) {
            addAllToSelection(keys, selection);
        }
        return selection;
    }

    public Selection lessThan(float value) {
        Selection selection = new BitmapBackedSelection();
        Double2ObjectSortedMap<IntArrayList> head = index.headMap(value);  // we append 1 to get values equal to the arg
        for (IntArrayList keys : head.values()) {
            addAllToSelection(keys, selection);
        }
        return selection;
    }
}