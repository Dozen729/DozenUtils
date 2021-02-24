package com.dozen.commonbase.utils;

/**
 * @author: Dozen
 * @description:
 * @time: 2020/11/24
 */
public class DigitUtils {
    /**
     * 格式化数字，用逗号分割
     *
     * @param number 1000000.7569 to 1,000,000.76 or
     * @return
     */
    public static String formatNumberWithCommaSplit(double number) {
        String firstStr = "";//第一个字符
        String middleStr = "";//中间字符
        String endStr = "";//小数后两位
        if (number < 0) {
            firstStr = "-";
        } else if (number != 0 && number < 0.1) {
            return number + "";
        }

        String tempNumberStr = number + "00000000000000000";
        int endIndex = tempNumberStr.lastIndexOf(".");
        endStr = tempNumberStr.substring(endIndex+1, endIndex + 3);

        String numberStr = Math.abs((long) number) + "";//取正

        int firstIndex = numberStr.length() % 3;
        int bitCount = numberStr.length() / 3;

        if (firstIndex > 0) {
            middleStr += numberStr.substring(0, firstIndex) + ",";
        }
        for (int i = 0; i < bitCount; i++) {
            middleStr += numberStr.substring(firstIndex + i * 3, firstIndex + i * 3 + 3) + ",";
        }
        if (middleStr.length() > 1) {
            middleStr = middleStr.substring(0, middleStr.length() - 1);
        }
        return firstStr + middleStr + "." + endStr;
    }

    /**
     * 格式化数字，用逗号分割
     *
     * @param number 1000000 to 1,000,000
     * @return
     */
    public static String formatLongNumber(long number) {
        String firstStr = "";//第一个字符
        String middleStr = "";//中间字符
        if (number < 0) {
            firstStr = "-";
        } else if (number != 0 && number < 0.1) {
            return number + "";
        }

        String numberStr = Math.abs((long) number) + "";//取正

        int firstIndex = numberStr.length() % 3;
        int bitCount = numberStr.length() / 3;

        if (firstIndex > 0) {
            middleStr += numberStr.substring(0, firstIndex) + ",";
        }
        for (int i = 0; i < bitCount; i++) {
            middleStr += numberStr.substring(firstIndex + i * 3, firstIndex + i * 3 + 3) + ",";
        }
        if (middleStr.length() > 1) {
            middleStr = middleStr.substring(0, middleStr.length() - 1);
        }
        return firstStr + middleStr;
    }
}
