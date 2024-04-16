package com.sztus.lib.back.end.basic.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.google.common.base.CaseFormat;
import com.sztus.lib.back.end.basic.type.constant.GlobalConst;
import com.sztus.lib.back.end.basic.type.constant.MatchType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 字符串工具类
 *
 * @author Rocky
 * @date 2019/04/27
 */
@Slf4j
public class StringUtil {

    private StringUtil() {
    }

    /**
     * 格式化字符串：驼峰转下划线格式
     *
     * @param camelStr 骆驼str
     * @return {@link String}
     */
    public static String formatCamelToUnderscore(String camelStr) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camelStr);
    }

    /**
     * 格式化字符串：下划线转驼峰格式
     *
     * @param underscoreStr 强调str
     * @return {@link String}
     */
    public static String formatUnderscoreToCamel(String underscoreStr) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, underscoreStr);
    }

    /**
     * 数组拼接为字符串
     *
     * @param separator 分隔符
     * @param args      arg游戏
     * @return {@link String}
     */
    public static String connectArray(String separator, Object... args) {
        StringBuilder builder = new StringBuilder();
        String s = GlobalConst.STR_EMPTY;
        if (args != null) {
            for (Object arg : args) {
                if (arg != null) {
                    builder.append(s).append(arg);

                    if (GlobalConst.STR_EMPTY.equals(s)) {
                        s = separator;
                    }
                }
            }
        }

        return builder.toString();
    }


    /**
     * 数组拼接为字符串
     *
     * @param args arg游戏
     * @return {@link String}
     */
    public static String connectArray(Object... args) {
        return connectArray(GlobalConst.STR_EMPTY, args);
    }

    /**
     * 字符串分割为数字数组
     *
     * @param source    源
     * @param separator 分隔符
     * @return {@link List}<{@link Integer}>
     */
    public static List<Integer> splitToIntList(String source, String separator) {
        List<Integer> targetList = new ArrayList<>();

        String[] itemArray = source.split(separator);
        for (String itemStr : itemArray) {
            try {
                Integer itemInt = Integer.parseInt(itemStr);
                targetList.add(itemInt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return targetList;
    }

    public static String toCurrency(BigDecimal number) {
        if (number == null) {
            number = new BigDecimal(0);
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String newRateVal = df.format(number);
        return toCurrency(newRateVal).replace("\\", "");
    }

    public static String toCurrency(Double number) {
        if (number == null) {
            number = 0.0;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String newRateVal = df.format(number);
        return toCurrency(newRateVal).replace("\\", "");
    }

    public static String toCurrency(String num) {
        //将num中的$,去掉，将num变成一个纯粹的数据格式字符串

        String sign = num.contains(GlobalConst.STR_HYPHEN) ? GlobalConst.STR_HYPHEN : GlobalConst.STR_EMPTY;
        //如果存在小数点，则获取数字的小数部分
        StringBuilder cents = new StringBuilder(num.contains(GlobalConst.STR_DOT) ? num.substring(num.indexOf(GlobalConst.STR_DOT)) : ".00");
        for (int i = 0; i < 3 - cents.length(); ++i) {
            cents.append(GlobalConst.INT_ZERO);
        }

        //获取数字的整数数部分
        int numStart = 0;
        if (GlobalConst.STR_HYPHEN.equals(sign)) {
            numStart = GlobalConst.INT_ONE;
        }
        num = num.contains(GlobalConst.STR_DOT) ? num.substring(numStart, (num.indexOf(GlobalConst.STR_DOT))) : num.substring(numStart);
        //针对整数部分进行格式化处理，这是此方法的核心，也是稍难理解的一个地方，逆向的来思考或者采用简单的事例来实现就容易多了
    /*
      也可以这样想象，现在有一串数字字符串在你面前，如果让你给他家千分位的逗号的话，你是怎么来思考和操作的?
      字符串长度为0/1/2/3时都不用添加
      字符串长度大于3的时候，从右往左数，有三位字符就加一个逗号，然后继续往前数，直到不到往前数少于三位字符为止
     */
        for (int i = 0; i < Math.floor(((double) (num.length() - (1 + i)) / 3)); i++) {
            num = num.substring(0, num.length() - (4 * i + 3)) + GlobalConst.STR_COMMA + num.substring(num.length() - (4 * i + 3));
        }

        if (num.startsWith(GlobalConst.STR_COMMA)) {
            num = num.substring(1);
        }

        //将数据（符号、整数部分、小数部分）整体组合返回
        if ("-".equals(sign)) {
            return "(\\$" + num + cents + ")";
        } else {
            return "\\$" + num + cents;
        }
    }

    public static String toCurrencyWithNoTransfer(String num) {
        return toCurrency(num).replace("\\", "");
    }


    /**
     * 渲染字符串（JSON数据填充模板化字符串）
     *
     * @param sourceContent sourceContent
     * @param dataJson      dataJson
     * @return {@link String}
     */
    public static String renderString(String sourceContent, JSONObject dataJson) {
        final String BEGIN_EACH_PREFIX = "${BEGIN_EACH(";
        final String BEGIN_EACH_SUFFIX = ")}";
        final String END_EACH = "${END_EACH}";
        final String INDEX = "${INDEX}";

        int x;
        int y;
        int z;
        StringBuilder sourceBuffer = new StringBuilder(sourceContent);
        do {
            String temp = sourceBuffer.toString();
            x = temp.indexOf(BEGIN_EACH_PREFIX);
            if (x != -1) {
                y = temp.indexOf(BEGIN_EACH_SUFFIX);

                String key = temp.substring(x + BEGIN_EACH_PREFIX.length(), y);

                temp = temp.substring(y + BEGIN_EACH_SUFFIX.length());

                z = temp.indexOf(END_EACH);

                temp = temp.substring(0, z);

                StringBuilder itemBuffer = new StringBuilder();
                JSONArray itemArray = dataJson.getJSONArray(key);
                if (itemArray != null) {
                    for (int i = 0; i < itemArray.size(); ++i) {
                        JSONObject itemJson = itemArray.getJSONObject(i);

                        String itemString = fillStringByJson(temp, itemJson);

                        if (itemString.contains(INDEX)) {
                            itemString = itemString.replace(INDEX, String.valueOf(i + 1));
                        }

                        itemBuffer.append(itemString);
                    }
                }

                sourceBuffer.replace(x, y + BEGIN_EACH_SUFFIX.length() + z + END_EACH.length(), itemBuffer.toString());
            }
        } while (x != -1);

        return fillStringByJson(sourceBuffer.toString(), dataJson);
    }

    private static String fillStringByJson(String source, JSONObject dataJson) {
        Set<String> keySet = dataJson.keySet();

        String target = source;
        for (String key : keySet) {
            String regex = "\\$\\{" + key + "\\}";

            String value;
            Object obj = dataJson.get(key);
            if (obj == null) {
                value = "";
            } else {
                value = obj.toString();
            }

            value = value.replace("&", "&amp;");
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(target);
            target = matcher.replaceAll(value);
        }

        return target;
    }

    public static String toPercent(String num) {
        String result = "";
        try {
            if (!StringUtils.isEmpty(num)) {
                BigDecimal bigDecimal = new BigDecimal(num);
                bigDecimal = BigDecimalUtil.roundHalfUp(bigDecimal, 4);
                DecimalFormat df = new DecimalFormat("0.00%");
                result = df.format(bigDecimal);
            }
        } catch (Exception e) {
            log.warn("Convert exception");
        }
        return result;
    }


    public static void convertEntityKey2DataBaseKey(JSONObject jsonObject) {
        if (jsonObject != null) {
            Set<String> keySet = jsonObject.keySet();
            HashSet<String> cloneKeySet = new HashSet<>(keySet);
            for (String key : cloneKeySet) {
                String lowerUnderScoreKey = formatCamelToUnderscore(key);
                Object value = jsonObject.get(key);
                jsonObject.remove(key);
                jsonObject.put(lowerUnderScoreKey, value);
            }
        }
    }

    /**
     * 将传入字符串做去驼峰处理,转换成 "_" 连接的变量
     */
    private static final Pattern humpPattern = Pattern.compile("[A-Z]");

    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 传入一个字符串和指定的一个长度.
     * 如果字符串超过指定长度就切割字符串超过长度的部分,
     * 如果没有到制定的长度就在原字符串后面补空格
     */
    public static String spaceFilling(String originalString, int targetLength) {
        if (originalString.length() > targetLength) {
            return originalString.substring(0, targetLength);
        } else if (originalString.length() == targetLength) {
            return originalString;
        } else {
            StringBuilder targetString = new StringBuilder(originalString);
            for (int i = 0; i < targetLength - originalString.length(); i++) {
                targetString.append(" ");
            }
            return targetString.toString();
        }
    }

    /**
     * 传入一个字符串和指定的一个长度.
     * 如果字符串超过指定长度就切割字符串超过长度的部分,
     * 如果没有到制定的长度就在原字符串前面补数字0
     */
    public static String forwardPaddingZero(String originalString, int targetLength) {
        if (originalString.length() > targetLength) {
            return originalString.substring(0, targetLength);
        } else if (originalString.length() == targetLength) {
            return originalString;
        } else {
            StringBuilder targetString = new StringBuilder();
            for (int i = 0; i < targetLength - originalString.length(); i++) {
                targetString.append("0");
            }
            targetString.append(originalString);
            return targetString.toString();
        }
    }

    /**
     * remove the 0 at the begin and end
     */
    public static String trimZero(String originalString) {
        String targetString = originalString;
        while (targetString.startsWith("0")) {
            targetString = targetString.substring(1);
        }

        while (targetString.endsWith("0")) {
            targetString = targetString.substring(0, targetString.length() - 1);
        }

        return targetString;
    }

    public static Boolean isAnyNull(Object... args) {
        for (Object arg : args) {
            if (Objects.isNull(arg)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isAllNull(Object... args) {
        for (Object arg : args) {
            if (!Objects.isNull(arg)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param source 字符串数组
     * @param target 匹配的值
     * @param type   匹配类型
     * @return Boolean  true false null
     * @description 根据匹配类型去数组中匹配字符串(不区分大小写)
     */
    public static Boolean isMatch(String source, String target, Integer type) {
        Boolean result = null;

        if (Boolean.TRUE.equals(isAnyNull(source, target, type))) {
            return result;
        }

        String sourceUpperCase = source.toUpperCase();
        final String targetUpperCase = target.trim().toUpperCase();

        String[] sourceItems = sourceUpperCase.split(",");
        Stream<String> sourceStream = Arrays.stream(sourceItems);

        switch (type) {
            case MatchType.ENDS_WITH_TARGET:
                result = sourceStream.anyMatch(sourceItem -> sourceItem.trim().endsWith(targetUpperCase));
                break;
            case MatchType.STARTS_WITH_TARGET:
                result = sourceStream.anyMatch(sourceItem -> sourceItem.trim().startsWith(targetUpperCase));
                break;
            case MatchType.CONTAINS_TARGET:
                result = sourceStream.anyMatch(sourceItem -> sourceItem.trim().contains(targetUpperCase));
                break;
            case MatchType.ENDS_WITH_SOURCE:
                result = sourceStream.anyMatch(sourceItem -> targetUpperCase.endsWith(sourceItem.trim()));
                break;
            case MatchType.STARTS_WITH_SOURCE:
                result = sourceStream.anyMatch(sourceItem -> targetUpperCase.startsWith(sourceItem.trim()));
                break;
            case MatchType.CONTAINS_SOURCE:
                result = sourceStream.anyMatch(sourceItem -> targetUpperCase.contains(sourceItem.trim()));
                break;
            case MatchType.EQUAL:
            default:
                result = sourceStream.anyMatch(sourceItem -> sourceItem.trim().equals(targetUpperCase));
                break;
        }

        return result;
    }

    /**
     * @param source 字符串数组
     * @param target 匹配的值
     * @return Boolean  true false null
     * @description 匹配数组中相等的字符串(不区分大小写)
     */
    public static Boolean isMatch(String source, String target) {
        return isMatch(source, target, MatchType.EQUAL);
    }

    public static void removeLast(StringBuilder sb) {
        sb.deleteCharAt(sb.length() - 1);
    }
}
