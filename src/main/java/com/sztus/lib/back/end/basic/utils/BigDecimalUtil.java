package com.sztus.lib.back.end.basic.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数学计算工具
 * <pre>
 * 2018-10-10 Wolf
 *  1. 添加数据比较大小方法
 * 2018-09-30 Wolf
 *  1. 添加高精度数据取min方法
 *  2. 添加高精度数据取max方法
 * 2018-09-18 Wolf
 *  1. 添加精确计算除法、乘法
 * 2018-09-12 Wolf
 *  1. 更新toDecimal方法，添加非空数据处理
 * 2018-09-03 Wolf
 *  1. 添加四舍五入方法
 * 2018-09-01 Wolf
 *  1. 添加小数位数取整方法
 *  2. 添加精确计算方法
 * </pre>
 */
public class BigDecimalUtil {
    private BigDecimalUtil() {
    }

    /**
     * 高精度：一年的天数
     */
    public static final BigDecimal BIG_DAYS_IN_YEAR = toBigDecimal(365);
    /**
     * 高精度：1
     */
    public static final BigDecimal BIG_ONE = toBigDecimal(1);
    /**
     * 高精度：0
     */
    public static final BigDecimal BIG_ZERO = toBigDecimal(0);

    /**
     * 比较两个数的大小
     *
     * @param x
     * @param y
     * @return
     */
    public static int compare(Number x, Number y) {
        if (x != null && y != null) {
            BigDecimal bigDecimalX = toBigDecimal(x);
            BigDecimal bigDecimalY = toBigDecimal(y);

            return bigDecimalX.compareTo(bigDecimalY);
        } else {
            if (x == null) {
                return LESS_THAN;
            } else {
                return GREATER_THAN;
            }
        }
    }

    /**
     * 返回绝对值
     *
     * @param x
     * @return
     */
    public static BigDecimal abs(Number x) {
        return toBigDecimal(x).abs();
    }

    /**
     * 返回较大的BigDecimal
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal max(Number x, Number y) {
        if (compare(x, y) == LESS_THAN) {
            return toBigDecimal(y);
        } else {
            return toBigDecimal(x);
        }
    }

    /**
     * 返回较小的BigDecimal
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal min(Number x, Number y) {
        if (compare(x, y) == GREATER_THAN) {
            return toBigDecimal(y);
        } else {
            return toBigDecimal(x);
        }
    }

    /**
     * 精确计算：除法
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal div(Number x, Number y) {
        BigDecimal bigDecimalX = toBigDecimal(x);
        BigDecimal bigDecimalY = toBigDecimal(y);

        try {
            return bigDecimalX.divide(bigDecimalY, DEFAULT_SCALE, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            return BIG_ZERO;
        }
    }

    /**
     * 精确计算：除法（自定义精度与舍入方式）
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal div(Number x, Number y, int scale, int roundingMode) {
        BigDecimal bigDecimalX = toBigDecimal(x);
        BigDecimal bigDecimalY = toBigDecimal(y);

        try {
            return bigDecimalX.divide(bigDecimalY, scale, roundingMode);
        } catch (ArithmeticException e) {
            return BIG_ZERO;
        }
    }

    /**
     * 精确计算：乘法
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal mul(Number x, Number y) {
        BigDecimal bigDecimalX = toBigDecimal(x);
        BigDecimal bigDecimalY = toBigDecimal(y);

        return bigDecimalX.multiply(bigDecimalY).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 精确计算：乘法（自定义精度与舍入方式）
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal mul(Number x, Number y, int scale, int roundingMode) {
        BigDecimal bigDecimalX = toBigDecimal(x);
        BigDecimal bigDecimalY = toBigDecimal(y);

        return bigDecimalX.multiply(bigDecimalY).setScale(scale, roundingMode);
    }

    /**
     * 精确计算：累积乘法（自定义精度与舍入方式）
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal mul(int scale, int roundingMode, Number... numbers) {
        if (numbers != null && numbers.length > 0) {
            BigDecimal bigDecimalTemp = toBigDecimal(numbers[0]);

            for (int i = 1; i < numbers.length; ++i) {
                bigDecimalTemp = bigDecimalTemp.multiply(toBigDecimal(numbers[i]));
            }

            return bigDecimalTemp.setScale(scale, roundingMode);
        } else {
            return null;
        }
    }

    /**
     * 幂运算
     *
     * @param x 底数
     * @param y 幂数
     * @return
     */
    public static BigDecimal pow(Number x, Number y) {
        BigDecimal bigDecimalX = toBigDecimal(x);
        BigDecimal bigDecimalY = toBigDecimal(y);

        return bigDecimalX.pow(bigDecimalY.intValue());
    }

    /**
     * 精确计算：加法
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal add(Number x, Number y) {
        BigDecimal bigDecimalX = toBigDecimal(x);
        BigDecimal bigDecimalY = toBigDecimal(y);

        return bigDecimalX.add(bigDecimalY);
    }

    /**
     * 精确计算：累加
     *
     * @param numbers
     * @return
     */
    public static BigDecimal add(Number... numbers) {
        if (numbers != null && numbers.length > 0) {
            BigDecimal sum = toBigDecimal(numbers[0]);

            for (int i = 1; i < numbers.length; ++i) {
                sum = add(sum, numbers[i]);
            }

            return sum;
        } else {
            return null;
        }
    }

    /**
     * 精确计算：减法
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal sub(Number x, Number y) {
        BigDecimal bigDecimalX = toBigDecimal(x);
        BigDecimal bigDecimalY = toBigDecimal(y);

        return bigDecimalX.subtract(bigDecimalY);
    }

    /**
     * 精确计算：累减
     *
     * @param x       被减数
     * @param numbers
     * @return
     */
    public static BigDecimal sub(Number x, Number... numbers) {
        BigDecimal boh = toBigDecimal(x);

        if (numbers != null && numbers.length > 0) {
            for (Number number : numbers) {
                boh = sub(boh, number);
            }
        }
        return boh;
    }

    /**
     * 以指定小数位数向下取整
     *
     * @param x
     * @param scale
     * @return
     */
    public static BigDecimal roundDown(Number x, int scale) {
        BigDecimal bigDecimalX = toBigDecimal(x);

        return bigDecimalX.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * 以默认小数位数向下取整
     *
     * @param x
     * @return
     */
    public static BigDecimal roundDown(Number x) {
        return roundDown(x, DEFAULT_SCALE);
    }

    /**
     * 以指定小数位数向上取整
     *
     * @param x
     * @param scale
     * @return
     */
    public static BigDecimal roundUp(Number x, int scale) {
        BigDecimal bigDecimalX = toBigDecimal(x);

        return bigDecimalX.setScale(scale, BigDecimal.ROUND_UP);
    }

    /**
     * 以默认小数位数向上取整
     *
     * @param x
     * @return
     */
    public static BigDecimal roundUp(Number x) {
        return roundUp(x, DEFAULT_SCALE);
    }

    /**
     * 以指定小数位数四舍五入
     *
     * @param x
     * @param scale
     * @return
     */
    public static BigDecimal roundHalfUp(Number x, int scale) {
        BigDecimal bigDecimalX = toBigDecimal(x);

        return bigDecimalX.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 以默认小数位数四舍五入
     *
     * @param x
     * @return
     */
    public static BigDecimal roundHalfUp(Number x) {
        return roundHalfUp(x, DEFAULT_SCALE);
    }

    /**
     * 转换数据为BigDecimal类型
     *
     * @param n
     * @return
     */
    public static BigDecimal toBigDecimal(Number n) {
        if (n != null) {
            return new BigDecimal(n.toString());
        } else {
            return BIG_ZERO;
        }
    }

    /**
     * 默认小数位数
     */
    private static final int DEFAULT_SCALE = 2;

    private static final int LESS_THAN = -1;
    private static final int GREATER_THAN = +1;

}
