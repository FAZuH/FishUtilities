package com.spiritlight.fishutils.misc.arrays.primitive;

import com.spiritlight.fishutils.misc.arrays.PrimitiveArrayLike;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntToDoubleFunction;
import java.util.stream.StreamSupport;

/**
 * A class representing an optionally mutable double array.
 */
public class DoubleArray extends PrimitiveArrayLike<Double> {
    private final double[] array;
    private final boolean mutable;

    public DoubleArray() {
        super(double.class, Double.class);
        this.array = new double[0];
        this.mutable = false;
    }

    public DoubleArray(double[] array) {
        super(double.class, Double.class);
        this.array = array;
        this.mutable = false;
    }

    public DoubleArray(double[] array, boolean mutable) {
        super(double.class, Double.class);
        this.array = array;
        this.mutable = mutable;
    }

    @Override
    public Double get(int index) {
        checkRange(index);
        return array[index];
    }

    @Override
    public double getAsDouble(int index) {
        checkRange(index);
        return array[index];
    }

    @Override
    public void set(int index, Double value) {
        setDouble(index, value);
    }

    @Override
    public void setDouble(int index, double value) {
        if(mutable) {
            checkRange(index);
            array[index] = value;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public double[] toDoubleArray() {
        return StreamSupport.stream(this.spliterator(), false).mapToDouble(Number::doubleValue).toArray();
    }

    protected void checkRange(int val) {
        if(val < 0 || val >= size()) throw new IndexOutOfBoundsException(val);
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    public DoubleArray toMutable() {
        if(this.mutable) return this;
        return new DoubleArray(this.array.clone(), true);
    }

    public DoubleArray toImmutable() {
        if(!this.mutable) return this;
        return new DoubleArray(this.array.clone(), false);
    }

    @Override
    public int size() {
        return array.length;
    }

    /*
        Static collections
     */

    public static final DoubleUnaryOperator NON_NEGATIVE = i -> Math.max(0, i);

    public static final DoubleUnaryOperator REVERSE = i -> -i;

    /**
     * Converts the given double array to an immutable DoubleArray
     * @param array the array
     * @return the wrapped DoubleArray
     */
    public static DoubleArray fromArray(double... array) {
        return new DoubleArray(array);
    }

    /**
     * Creates an immutable array with size {@code size} and all elements set to 0.
     * @param size the size
     * @return an immutable array filled with zeroes.
     */
    public static DoubleArray createEmpty(int size) {
        return new DefaultDoubleArray(size, 0);
    }

    /**
     * Creates an immutable array with size {@code size} and all elements set to {@code value}.
     * @param size the size
     * @param value the value
     * @return an immutable array filled with {@code value}.
     */
    public static DoubleArray create(int size, double value) {
        return new DefaultDoubleArray(size, value);
    }

    /**
     * Creates an immutable array with size {@code size} and mapped
     * individually to its position as described by {@code mapper}
     * @param size the size
     * @param mapper the mapper
     * @return a new DoubleArray
     */
    public static DoubleArray create(int size, IntToDoubleFunction mapper) {
        double[] v = new double[size];
        for(int i = 0; i < size; i++) {
            v[i] = mapper.applyAsDouble(i);
        }
        return new DoubleArray(v);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DoubleArray arr = (DoubleArray) object;
        return mutable == arr.mutable && Arrays.equals(array, arr.array);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mutable);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    private static class DefaultDoubleArray extends DoubleArray {
        private final int size;
        private final double value;

        private DefaultDoubleArray(int size, double value) {
            super(null, false);
            if(size < 0) throw new IllegalArgumentException("size cannot be negative");
            this.size = size;
            this.value = value;
        }

        @Override
        public Double get(int index) {
            checkRange(index);
            return value;
        }

        @Override
        public double getAsDouble(int index) {
            checkRange(index);
            return value;
        }

        @Override
        protected void checkRange(int val) {
            if(val < 0 || val >= size) throw new IndexOutOfBoundsException(val);
        }
    }
}