package com.github.lwhite1.tablesaw.kapi

import com.github.lwhite1.tablesaw.api.ColumnType
import com.github.lwhite1.tablesaw.columns.Column
import com.github.lwhite1.tablesaw.store.ColumnMetadata
import com.github.lwhite1.tablesaw.util.Selection

/**
 *
 */
interface Col {

    fun size(): Int = target().size()

    fun summary(): Dataframe = Dataframe(target().summary())

    fun select(rows: Selection): Col {
        val c = this.emptyCopy()
        for (row in rows) {
            c.appendCell(getString(row))
        }
        return c
    }

    /**
     * Returns the count of missing values in this column
     */
    fun countMissing(): Int = target().countMissing()

    /**
     * Returns the count of unique values in this column
     */
    fun countUnique(): Int = target().countUnique()

    /**
     * Returns a column of the same type as the receiver, containing only the unique values of the receiver
     */
    fun unique(): Col

    /**
     * Returns this column's ColumnType
     */
    fun type(): ColumnType = target().type()

    fun name(): String = target().name()

    fun setName(name: String) = target().setName(name)

    /**
     * Returns a string representation of the value at the given row
     */
    fun getString(row: Int): String = target().getString(row)

    /**
     * Returns a deep copy of the receiver
     */
    fun copy(): Col

    /**
     * Returns an empty copy of the receiver, with its internal storage initialized to the given row size
     */
    fun emptyCopy(rowSize: Int = 0): Col

    fun clear() = target().clear()

    fun sortAscending()

    fun sortDescending()

    /**
     * Returns true if the column has no data
     */
    fun isEmpty(): Boolean = target().isEmpty

    fun appendCell(stringValue: String) = target().appendCell(stringValue)

    /**
     * Returns a unique string that identifies this column
     */
    fun id(): String = target().id()

    /**
     * Returns a String containing the column's metadata in json format
     */
    fun metadataString(): String = target().metadata()

    fun columnMetadata(): ColumnMetadata = target().columnMetadata()

//    fun rowComparator(): IntComparator

    fun first(): String {
        return getString(0)
    }

    fun last(): String {
        return getString(size() - 1)
    }

    // fun append(column: Column)

    fun first(numRows: Int): Col {
        val col = emptyCopy()
        val rows = Math.min(numRows, size())
        for (i in 0..rows - 1) {
            col.appendCell(getString(i))
        }
        return col
    }

    fun last(numRows: Int): Col {
        val col = emptyCopy()
        val rows = Math.min(numRows, size())
        for (i in size() - rows..size() - 1) {
            col.appendCell(getString(i))
        }
        return col
    }

    fun show(): String = target().print()


    fun title(): String {
        return "Col: " + name() + '\n'
    }

    fun toDoubleArray(): DoubleArray {
        throw UnsupportedOperationException("Method toDoubleArray() is not supported on non-numeric columns")
    }

    fun target(): Column<*>

//    fun isMissing(): Selection

//    fun isNotMissing(): Selection

}

/*
    */
/**
 * Returns a new column of the same type as the receiver, such that the values in the new column
 * contain the difference between each cell in the original and it's predecessor.
 * The Missing Value Indicator is used for the first cell in the new column.
 * (e.g. IntColumn.MISSING_VALUE)
 */
/*
    fun difference(): E
*/
