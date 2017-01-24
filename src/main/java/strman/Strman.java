package strman;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 一个没有任何依赖关系的字符串处理库， 基于JDK1.7开发。 <br/>
 * 原版库来自于GitHub上JDK1.8版本的 Strman-Java库
 * 2017-01-24
 */
public abstract class Strman {
	
	private static final String NULL_STRING_PREDICATE = null;
	private static final String NULL_STRING_MSG_SUPPLIER = "'value' should be not null.";
	
	private Strman(){}
	
	 /**
     * 在一个字符串后追加任意个数的字符串
     *
     * @param value  初始的字符串
     * @param appends 追加的字符串
     * @return 完整的字符串
     */
    public static String append(final String value, final String... appends) {
        return appendArray(value, appends);
    }
	
    
    /**
     * 在一个字符串后先后追加一个字符串数组中的元素
     *
     * @param value   初始的字符串
     * @param appends 追加的字符数组
     * @return full String
     */
    public static String appendArray(final String value, final String[] appends) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (appends == null || appends.length == 0) {
            return value;
        }
        StringBuilder joiner = new StringBuilder("");
        for (String append : appends) {
            joiner.append(append);
        }
        return value + joiner.toString();
    }

    /**
     * 根据字符串的索引获取到对应的字符。如果索引是负数,则逆向获取,超出则抛出异常
     *
     * @param value 输入的字符串
     * @param index 索引的位置
     * @return 查找到的字符串，查找不到则返回空字符串
     */
    public static String at(final String value, int index) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        int length = value.length();
        if (index < 0) {
            index = length + index;
        }
        return (index < length && index >= 0) ? String.valueOf(value.charAt(index)) : "";
    }

    /**
     * 得到一个字符串中,开始字符串和结束字符串之间的字符串的数组
     *
     * @param value 输入的字符串
     * @param start 开始字符串
     * @param end   结束字符串
     * @return 数组包含开始和结束之间的不同部分。
     */

    public static String[] between(final String value, final String start, final String end) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        validate(start, NULL_STRING_PREDICATE, "'start' should be not null.");
        validate(end, NULL_STRING_PREDICATE, "'end' should be not null.");

        String[] parts = value.split(end);
        int length = parts.length;
        String[] strs = new String[length];
        for(int i=0; i<length; i++){
        	String subPart = parts[i];
        	strs[i] = subPart.substring(subPart.indexOf(start) + start.length());
        	
        }
        return strs;
    }

    /**
     * 得到一个字符串中所有字符构成的字符串数组
     *
     * @param value 输入的字符串
     * @return 字符构成的字符串数组
     */
    public static String[] chars(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return removeEmptyStrings(value.split(""));
    }


    /**
     * 替换掉连续的多个空格为一个空格
     *
     * @param value 输入的字符串
     * @return 替换后的字符串
     */
    public static String collapseWhitespace(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.trim().replaceAll("\\s\\s+", " ");
    }


    /**
     * 判断一个字符串是否包含另外一个字符串， 大小写不敏感
     *
     * @param value  准备查找的字符串
     * @param needle 查找的字符串数据
     * @return 查询到返回true 查询不到返回false
     */
    public static boolean contains(final String value, final String needle) {
        return contains(value, needle, false);
    }

    /**
     * 判断一个字符串是否包含另外一个字符串，第三个参数,表示字符串大小写是否敏感
     *
     * @param value  准备查找的字符串
     * @param needle 查找的字符串数据
     * @return 查询到返回true 查询不到返回false
     */
    public static boolean contains(final String value, final String needle, final boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.contains(needle);
        }
        return value.toLowerCase().contains(needle.toLowerCase());
    }

    /**
     * 判断一个字符串是否包含某字符串数组中的所有元素， 大小写不敏感
     *
     * @param value   输入要验证的字符串
     * @param needles 输入验证的数组数据
     * @return 如果都包含返回true 否则 返回 false
     */
    public static boolean containsAll(final String value, final String[] needles) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        boolean flag = false;
        for(String needle : needles){
        	flag = contains(value, needle, false);
        }
        return flag;
    }

    /**
     * 判断一个字符串是否包含某字符串数组中的所有元素， 第三个参数表示是否大小写敏感
     *
     * @param value   输入要验证的字符串
     * @param needles 输入验证的数组数据
     * @return 如果都包含返回true 否则 返回 false
     */
    public static boolean containsAll(final String value, final String[] needles, final boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        boolean flag = false;
        for(String needle : needles){
        	flag = contains(value, needle, caseSensitive);
        }
        return flag;
    }

    /**
     * 判断一个字符串是否包含某字符串数组中的任意一个元素， 大小写不敏感
     *
     * @param value   输入要验证的字符串
     * @param needles 输入验证的数组数据
     * @return 如果都包含任意一个返回true 否则 返回 false
     */
    public static boolean containsAny(final String value, final String[] needles) {
        return containsAny(value, needles, false);
    }

    /**
     * 判断一个字符串是否包含某字符串数组中的任意一个元素， 第三个参数表示大小写是否敏感
     *
     * @param value   输入要验证的字符串
     * @param needles 输入验证的数组数据
     * @return 如果都包含任意一个返回true 否则 返回 false
     */
    public static boolean containsAny(final String value, final String[] needles, final boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        boolean flag = false;
        for(String needle : needles){
        	flag = contains(value, needle, caseSensitive);
        }
        return flag;
    }

    /**
     * 判断一个字符串包含某字符串的个数， 区分大小写
     *
     * @param value  输入要验证的字符串
     * @param subStr 输入要匹配的字符串
     * @return 包含匹配字符串的个数
     */
    public static long countSubstr(final String value, final String subStr) {
        return countSubstr(value, subStr, true, false);
    }

    /**
     *计算字符串的子串出现的次数
	 *
	 * @param value 	输入的要验的字符串
	 * @param subStr 	搜索字符串的子串
	 * @param caseSensitive 	搜索是否应该区分大小写的
	 * @param allowOverlapping 	布尔类型考虑重叠
	 * @return 子字符串的个数
     */
    public static long countSubstr(final String value, final String subStr, final boolean caseSensitive, boolean allowOverlapping) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return countSubstr(caseSensitive ? value : value.toLowerCase(), caseSensitive ? subStr : subStr.toLowerCase(), allowOverlapping, 0L);
    }

    /**
     * 判断一个字符串是否以某个字符串结尾， 大小敏感
     *
     * @param value 值输入字符串
     * @param search 搜索字符串搜索
     * @return  如果是已某个字符串结尾，则返回true 否则返回 false
     */
    public static boolean endsWith(final String value, final String search) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return endsWith(value, search, value.length(), true);
    }

    /**
     * 检测字符串是否是以某个字符串结尾
     *
     * @param value         要检查的字符串
     * @param search        要匹配的字符串
     * @param caseSensitive 是否大小写敏感， true敏感， false 不敏感
     * @return 如果是返回true 否则返回false
     */
    public static boolean endsWith(final String value, final String search, final boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return endsWith(value, search, value.length(), caseSensitive);
    }

    /**
     * 判断一个字符串是否以某个字符串结尾
     *
     * @param value         输入的字符串
     * @param search        搜索的字符串
     * @param position      开始搜索的位置
     * @param caseSensitive 是否大小写敏感， true 敏感， false 不敏感
     * @return 如果搜索到， 返回true 否则返回 false
     */
    public static boolean endsWith(final String value, final String search, final int position, final boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        int remainingLength = position - search.length();
        if (caseSensitive) {
            return value.indexOf(search, remainingLength) > -1;
        }
        return value.toLowerCase().indexOf(search.toLowerCase(), remainingLength) > -1;
    }

    /**
     * 确保一个字符串以某个字符串开头,如果不是,则在前面追加该字符串,并将字符串结果返回
     *
     * @param value  输入的字符串
     * @param prefix 匹配的字符串
     * @return 返回匹配后的字符串
     */
    public static String ensureLeft(final String value, final String prefix) {
        return ensureLeft(value, prefix, true);
    }

    /**
     * 确保一个字符串以某个字符串开头,如果不是,则在前面追加该字符串,并将字符串结果返回
     *
     * @param value  输入的字符串
     * @param prefix 匹配的字符串
     * @param caseSensitive 是否大小写敏感， 敏感 true 不敏感 false
     * @return 返回匹配后的字符串
     */
    public static String ensureLeft(final String value, final String prefix, final boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.startsWith(prefix) ? value : prefix + value;
        }
        String _value = value.toLowerCase();
        String _prefix = prefix.toLowerCase();
        return _value.startsWith(_prefix) ? value : prefix + value;
    }

    /**
     * 通过MIME Base64的方式加密数据
     *
     * @param value 要加密的数据
     * @return 		加密后的数据
     */
    public static String base64Decode(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return new String(Base64.decodeBase64(value));
    }

    /**
     * 通过MIME Base64的方式解密数据
     *
     * @param value 要解密的字符串
     * @return 解密后的字符串数据
     */
    public static String base64Encode(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return Base64.encodeBase64String(value.getBytes());
    }
    
    /**
     * MD5加密传入的字符串数据
     * @param value 要加密的字符串数据
     * @return	加密后的字符串数据
     */
    public static String MD5(String value){
    	return DigestUtils.md5Hex(value);
    }

    /**
     * 将二进制编码（16位）转成字符串字符
     *
     * @param value 要转换的二进制编码
     * @return 转码后的字符串数据
     */
    public static String binDecode(final String value) {
        return decode(value, 16, 2);
    }

    /**
     * 将字符串字符转成二进制编码（16位）
     *
     * @param value 要转换的字符串数据
     * @return 转换后的二进制编码
     */
    public static String binEncode(final String value) {
        return encode(value, 16, 2);
    }

    /**
     * 将十进制编码（5位）转成字符串字符
     *
     * @param value 要转换的十进制编码
     * @return 转换后的字符串数据
     */
    public static String decDecode(final String value) {
        return decode(value, 5, 10);
    }

    /**
     * 将字符串转成十进制编码（5位）
     *
     * @param value 要进行转换的字符串
     * @return 转换后的字符串数据
     */
    public static String decEncode(final String value) {
        return encode(value, 5, 10);
    }

    /**
     * 确保一个字符串以某个字符串开头,如果不是,则在前面追加该字符串,并将字符串结果返回
     *
     * @param value  输入的字符串
     * @param suffix 要在开头匹配的字符串
     * @return 返回匹配后的字符串数据
     */
    public static String ensureRight(final String value, final String suffix) {
        return ensureRight(value, suffix, true);
    }

    /**
     * 确保一个字符串以某个字符串开头,如果不是,则在前面追加该字符串,并将字符串结果返回
     *
     * @param value  输入的字符串
     * @param suffix 要在开头匹配的字符串
     * @param caseSensitive 是否大小写敏感， 大小写敏感 true ， 大小写不敏感 false
     * @return 返回匹配后的字符串数据
     */
    public static String ensureRight(final String value, final String suffix, boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return endsWith(value, suffix, caseSensitive) ? value : append(value, suffix);
    }

    /**
     * 得到从字符串开始到索引n的字符串
     *
     * @param value 输入的字符串
     * @param n     索引位置的字符串
     * @return 返回截取后的字符串
     */
    public static String first(final String value, final int n) {
    	validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
    	return (value.isEmpty() || value.length() < n) ? value : value.substring(n);
    }

    /**
     * 得到字符串的第一个字符
     *
     * @param value 输入的字符串
     * @return 字符串的第一个字符
     */
    public static String head(final String value) {
        return first(value, 1);
    }

    /**
     * 使用参数格式化字符串
     *
     * @param value  要格式化的字符串
     * @param params 描述字符串的参数
     * @return 格式化之后的字符串
     */
    public static String format(final String value, String... params) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        Pattern p = Pattern.compile("\\{(\\w+)\\}");
        Matcher m = p.matcher(value);
        String result = value;
        while (m.find()) {
            int paramNumber = Integer.parseInt(m.group(1));
            if (params == null || paramNumber >= params.length) {
                throw new IllegalArgumentException("params does not have value for " + m.group());
            }
            result = result.replace(m.group(), params[paramNumber]);
        }
        return result;
    }

    /**
     * 将字符串字符转成十六进制编码（4位）
     *
     * @param value 要转化的字符串
     * @return 转换之后的字符串
     */
    public static String hexDecode(final String value) {
        return decode(value, 4, 16);
    }

    /**
     * 将十六进制编码（4位）转成字符串字符
     *
     * @param value 要转换的字符串
     * @return 转换后的字符串
     */
    public static String hexEncode(final String value) {
        return encode(value, 4, 16);
    }

    /**
     * indexOf()方法返回索引调用字符串中第一次出现的指定值,在fromIndex开始搜索。
	 * 返回1,如果没有找到的值。
	 *
	 * @param 输入字符串值
	 * @param 针搜索字符串
	 * @param 抵消抵消开始搜索。
	 * @param caseSensitive 布尔表明是否搜索应该是区分大小写的
     */
    public static int indexOf(final String value, final String needle, int offset, boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.indexOf(needle, offset);
        }
        return value.toLowerCase().indexOf(needle.toLowerCase(), offset);
    }

    /**
     * 测试如果两个字符串是不平等的
     *
     * @param 首先第一个字符串
     * @param 第二第二个字符串
     * @return 如果第一和第二否则不相等则返回false
     */
    public static boolean unequal(final String first, final String second) {
    	validate(first, NULL_STRING_PREDICATE, "'first' should be not null.");
    	validate(second, NULL_STRING_PREDICATE, "'second' should be not null.");
        return !first.equals(second);
    }

    /**
     * 测试如果两个字符串是不平等的，(方法已被弃用， 请使用 unequal)
     *
     * @param 首先第一个字符串
     * @param 第二第二个字符串
     * @return 如果第一和第二否则不相等则返回false
     * @deprecated 使用不平等
     */
    public static boolean inequal(final String first, final String second) {
    	validate(first, NULL_STRING_PREDICATE, "'first' should be not null.");
    	validate(second, NULL_STRING_PREDICATE, "'second' should be not null.");
        return !first.equals(second);
    }

    /**
     * 在 value字符串中的index位置插入字符串substr
     *
     * @param value  输入字符串值
     * @param substr 字符串的子串字符串插入
     * @param index  指数索引插入字符串的子串
     * @return 插入后的字符串的
     */
    public static String insert(final String value, final String substr, final int index) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        validate(substr, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (index > value.length()) {
            return value;
        }
        return append(value.substring(0, index), substr, value.substring(index));
    }

    /**
     * Verifies if String is uppercase
     *
     * @param value The input String
     * @return true if String is uppercase false otherwise
     */
    public static boolean isUpperCase(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        for (int i = 0; i < value.length(); i++) {
            if (Character.isLowerCase(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies if String is lower case
     *
     * @param value The input String
     * @return true if String is lowercase false otherwise
     */
    public static boolean isLowerCase(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        for (int i = 0; i < value.length(); i++) {
            if (Character.isUpperCase(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return the last n chars of String
     *
     * @param value The input String
     * @param n     Number of chars to return
     * @return n Last characters
     */
    public static String last(final String value, int n) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (n > value.length()) {
            return value;
        }
        return value.substring(value.length() - n);
    }

    /**
     * Returns a new string of a given length such that the beginning of the string is padded.
     *
     * @param value  The input String
     * @param pad    The pad
     * @param length Length of the String we want
     * @return Padded String
     */
    public static String leftPad(final String value, final String pad, final int length) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        validate(pad, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (value.length() > length) {
            return value;
        }
        return append(repeat(pad, length - value.length()), value);
    }

    /**
     * Checks whether Object is String
     *
     * @param value The input String
     * @return true if Object is a String false otherwise
     */
    public static boolean isString(final Object value) {
        if (null == value) {
            throw new IllegalArgumentException("value can't be null");
        }
        return value instanceof String;
    }

    /**
     * This method returns the index within the calling String object of the last occurrence of the specified value, searching backwards from the offset.
     * Returns -1 if the value is not found. The search starts from the end and case sensitive.
     *
     * @param value  The input String
     * @param needle The search String
     * @return Return position of the last occurrence of 'needle'.
     */
    public static int lastIndexOf(final String value, final String needle) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return lastIndexOf(value, needle, value.length(), true);
    }

    /**
     * This method returns the index within the calling String object of the last occurrence of the specified value, searching backwards from the offset.
     * Returns -1 if the value is not found. The search starts from the end and case sensitive.
     *
     * @param value         The input String
     * @param needle        The search String
     * @param caseSensitive true or false
     * @return Return position of the last occurrence of 'needle'.
     */
    public static int lastIndexOf(final String value, final String needle, boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return lastIndexOf(value, needle, value.length(), caseSensitive);
    }

    /**
     * This method returns the index within the calling String object of the last occurrence of the specified value, searching backwards from the offset.
     * Returns -1 if the value is not found.
     *
     * @param value         The input String
     * @param needle        The search String
     * @param offset        The index to start search from
     * @param caseSensitive whether search should be case sensitive
     * @return Return position of the last occurrence of 'needle'.
     */
    public static int lastIndexOf(final String value, final String needle, final int offset, final boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        validate(needle, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.lastIndexOf(needle, offset);
        }
        return value.toLowerCase().lastIndexOf(needle.toLowerCase(), offset);
    }

    /**
     * Removes all spaces on left
     *
     * @param value The input String
     * @return String without left border spaces
     */
    public static String leftTrim(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.replaceAll("^\\s+", "");
    }

    /**
     * Returns length of String. Delegates to java.lang.String length method.
     *
     * @param value The input String
     * @return Length of the String
     */
    public static int length(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.length();
    }

    /**
     * Return a new String starting with prepends
     *
     * @param value    The input String
     * @param prepends Strings to prepend
     * @return The prepended String
     */
    public static String prepend(final String value, final String... prepends) {
        return prependArray(value, prepends);
    }

    /**
     * Return a new String starting with prepends
     *
     * @param value    The input String
     * @param prepends Strings to prepend
     * @return The prepended String
     */
    public static String prependArray(final String value, final String[] prepends) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (prepends == null || prepends.length == 0) {
            return value;
        }
        StringBuilder joiner = new StringBuilder("");
        for (String prepend : prepends) {
            joiner.append(prepend);
        }
        return joiner.toString() + value;
    }

    /**
     * Remove empty Strings from string array
     *
     * @param strings Array of String to be cleaned
     * @return Array of String without empty Strings
     */
    public static String[] removeEmptyStrings(String[] strings) {
        if (null == strings) {
            throw new IllegalArgumentException("Input array should not be null");
        }
        List<String> strs = new ArrayList<String>();
        for(String str : strings){
        	if(str != null && !str.trim().isEmpty()){
        		strs.add(str);
        	}
        }
        return strs.toArray(new String[strs.size()]);
    }

    /**
     * Returns a new String with the prefix removed, if present. This is case sensitive.
     *
     * @param value  The input String
     * @param prefix String to remove on left
     * @return The String without prefix
     */
    public static String removeLeft(final String value, final String prefix) {
        return removeLeft(value, prefix, true);
    }

    /**
     * Returns a new String with the prefix removed, if present.
     *
     * @param value         The input String
     * @param prefix        String to remove on left
     * @param caseSensitive ensure case sensitivity
     * @return The String without prefix
     */
    public static String removeLeft(final String value, final String prefix, final boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        validate(prefix, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.startsWith(prefix) ? value.substring(prefix.length()) : value;
        }
        return value.toLowerCase().startsWith(prefix.toLowerCase()) ? value.substring(prefix.length()) : value;
    }

    /**
     * Remove all non word characters.
     *
     * @param value The input String
     * @return String without non-word characters
     */
    public static String removeNonWords(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.replaceAll("[^\\w]+", "");
    }

    /**
     * Returns a new string with the 'suffix' removed, if present. Search is case sensitive.
     *
     * @param value  The input String
     * @param suffix The suffix to remove
     * @return The String without suffix!
     */
    public static String removeRight(final String value, final String suffix) {
        return removeRight(value, suffix, true);
    }

    /**
     * Returns a new string with the 'suffix' removed, if present.
     *
     * @param value         The input String
     * @param suffix        The suffix to remove
     * @param caseSensitive whether search should be case sensitive or not
     * @return The String without suffix!
     */
    public static String removeRight(final String value, final String suffix, final boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        validate(suffix, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return endsWith(value, suffix, caseSensitive) ? value.substring(0, value.toLowerCase().lastIndexOf(suffix.toLowerCase())) : value;
    }

    /**
     * Remove all spaces and replace for value.
     *
     * @param value The input String
     * @return String without spaces
     */
    public static String removeSpaces(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.replaceAll("\\s", "");
    }

    /**
     * Returns a repeated string given a multiplier.
     *
     * @param value      The input String
     * @param multiplier Number of repeats
     * @return The String repeated
     */
    public static String repeat(final String value, final int multiplier) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        StringBuilder builder = new StringBuilder(value);
        for(int i=1; i<multiplier; i++){
        	builder.append(value);
        }
        return builder.toString();
    }

    /**
     * Replace all occurrences of 'search' value to 'newvalue'. Uses String replace method.
     *
     * @param value         The input
     * @param search        The String to search
     * @param newValue      The String to replace
     * @param caseSensitive whether search should be case sensitive or not
     * @return String replaced with 'newvalue'.
     */
    public static String replace(final String value, final String search, final String newValue, final boolean caseSensitive) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        validate(search, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.replace(search, newValue);
        }
        return Pattern.compile(search, Pattern.CASE_INSENSITIVE).matcher(value).replaceAll(Matcher.quoteReplacement(newValue));
    }

    /**
     * Reverse the input String
     *
     * @param value The input String
     * @return Reversed String
     */
    public static String reverse(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return new StringBuilder(value).reverse().toString();
    }

    /**
     * Returns a new string of a given length such that the ending of the string is padded.
     *
     * @param value  The input String
     * @param length Max length of String.
     * @param pad    Character to repeat
     * @return Right padded String
     */
    public static String rightPad(final String value, String pad, final int length) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (value.length() > length) {
            return value;
        }
        return append(value, repeat(pad, length - value.length()));
    }

    /**
     * Remove all spaces on right.
     *
     * @param value The String
     * @return String without right boarders spaces.
     */
    public static String rightTrim(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.replaceAll("\\s+$", "");
    }

    /**
     * Truncate the string securely, not cutting a word in half. It always returns the last full word.
     *
     * @param value  The input String
     * @param length Max size of the truncated String
     * @param filler String that will be added to the end of the return string. Example: '...'
     * @return The truncated String
     */
    public static String safeTruncate(final String value, final int length, final String filler) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (length == 0) {
            return "";
        }
        if (length >= value.length()) {
            return value;
        }

        String[] words = words(value);
        StringBuilder result = new StringBuilder(" ");
        int spaceCount = 0;
        for (String word : words) {
            if (result.length() + word.length() + filler.length() + spaceCount > length) {
                break;
            } else {
                result.append(word);
                spaceCount++;
            }
        }
        return append(result.toString(), filler);
    }

    /**
     * Alias to String split function. Defined only for completeness.
     *
     * @param value The input String
     * @param regex The delimiting regular expression
     * @return String Array
     */
    public static String[] split(final String value, final String regex) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.split(regex);
    }

    /**
     * Splits a String to words
     *
     * @param value The input String
     * @return Words Array
     */
    public static String[] words(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.split("\\W+");
    }

    /**
     * Truncate the unsecured form string, cutting the independent string of required position.
     *
     * @param value  Value will be truncated unsecurely.
     * @param length Size of the returned string.
     * @param filler Value that will be added to the end of the return string. Example: '...'
     * @return String truncated unsafely.
     */
    public static String truncate(final String value, final int length, final String filler) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (length == 0) {
            return "";
        }
        if (length >= value.length()) {
            return value;
        }
        return append(value.substring(0, length - filler.length()), filler);
    }

    /**
     * Converts all HTML entities to applicable characters.
     *
     * @param encodedHtml The encoded HTML
     * @return The decoded HTML
     */
    public static String htmlDecode(final String encodedHtml) {
        validate(encodedHtml, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String[] entities = encodedHtml.split("&\\W+;");
        StringBuilder builder = new StringBuilder("");
        for(String c : entities){
        	builder.append(HtmlEntities.decodedEntities.get(c));
        }
        return builder.toString();
    }

    /**
     * Convert all applicable characters to HTML entities.
     *
     * @param html The HTML to encode
     * @return The encoded data
     */
    public static String htmlEncode(final String html) {
        validate(html, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        char[] chars = html.toCharArray();
        StringBuilder builder = new StringBuilder("");
        for(char c : chars){
        	builder.append(HtmlEntities.encodedEntities.get("\\u" + String.format("%04x", c).toUpperCase()));
        }
        return builder.toString();
    }

    /**
     * It returns a string with its characters in random order.
     *
     * @param value The input String
     * @return The shuffled String
     */
    public static String shuffle(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        char[] chars = value.toCharArray();
        Random random = new Random();
        for (int i = 0; i < chars.length; i++) {
            int r = random.nextInt(chars.length);
            char tmp = chars[i];
            chars[i] = chars[r];
            chars[r] = tmp;
        }
        return String.valueOf(chars);
    }

    /**
     * Alias of substring method
     *
     * @param value The input String
     * @param begin Start of slice.
     * @param end   End of slice.
     * @return The String sliced!
     */
    public static String slice(final String value, int begin, int end) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.substring(begin, end);
    }

    /**
     * Convert a String to a slug
     *
     * @param value The value to slugify
     * @return The slugified value
     */
    public static String slugify(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String transliterated = transliterate(collapseWhitespace(value.trim().toLowerCase()));
        String[] strs = words(transliterated.replace("&", "-and-"));
        StringBuilder builder = new StringBuilder("");
        for(String str : strs){
        	builder.append(str).append("-");
        }
        return builder.deleteCharAt(builder.length()-1).toString();
    }

    /**
     * Remove all non valid characters.
     *
     * @param value The input String
     * @return String without non valid characters.
     */
    public static String transliterate(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String result = value;
        Set<Map.Entry<String, List<String>>> entries = Ascii.ascii.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            for (String ch : entry.getValue()) {
                result = result.replace(ch, entry.getKey());
            }
        }
        return result;
    }


    /**
     * Surrounds a 'value' with the given 'prefix' and 'suffix'.
     *
     * @param value  The input String
     * @param prefix prefix. If suffix is null then prefix is used
     * @param suffix suffix
     * @return The String with surround substrs!
     */
    public static String surround(final String value, final String prefix, final String suffix) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String _prefix = (null == prefix) ? "" : prefix;
        return append(_prefix, value, (null == suffix) ? "" : suffix);
    }

    /**
     * Transform to camelCase
     *
     * @param value The input String
     * @return String in camelCase.
     */
    public static String toCamelCase(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String str = toStudlyCase(value);
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * Transform to StudlyCaps.
     *
     * @param value The input String
     * @return String in StudlyCaps.
     */
    public static String toStudlyCase(final String value) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String[] words = collapseWhitespace(value.trim()).split("\\s*(_|-|\\s)\\s*");
        StringBuilder builder = new StringBuilder();
        for(String w : words){
        	if(!w.trim().isEmpty()){
        		builder.append(upperFirst(w));
        	}
        }
        return builder.toString();
    }

    /**
     * Return tail of the String
     *
     * @param value The input String
     * @return String tail
     */
    public static String tail(final String value) {
    	validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
    	return !value.isEmpty() ? last(value, value.length()-1) : value;
    }

    /**
     * Decamelize String
     *
     * @param value The input String
     * @param chr   string to use
     * @return String decamelized.
     */
    public static String toDecamelize(final String value, final String chr) {
        String camelCasedString = toCamelCase(value);
        String[] words = camelCasedString.split("(?=\\p{Upper})");
        StringBuilder builder = new StringBuilder();
        for(String w : words){
        	builder.append(w.toLowerCase()).append(chr);
        }
        return builder.deleteCharAt(builder.length()-1).toString();
    }

    /**
     * Transform to kebab-case.
     *
     * @param value The input String
     * @return String in kebab-case.
     */
    public static String toKebabCase(final String value) {
        return toDecamelize(value, "-");
    }

    /**
     * Transform to snake_case.
     *
     * @param value The input String
     * @return String in snake_case.
     */
    public static String toSnakeCase(final String value) {
        return toDecamelize(value, "_");
    }

    public static String decode(final String value, final int digits, final int radix) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String[] strs = value.split("(?<=\\G.{" + digits + "})");
        StringBuilder builder = new StringBuilder();
        for(String data : strs){
        	builder.append(String.valueOf(Character.toChars(Integer.parseInt(data, radix))));
        }
        return builder.toString();
    }

    public static String encode(final String value, final int digits, final int radix) {
        validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        char[] chars = value.toCharArray();
        StringBuilder builder = new StringBuilder();
        for(char ch : chars){
        	builder.append(leftPad(Integer.toString(ch, radix), "0", digits));
        }
        return builder.toString();
    }


    /**
     * Join concatenates all the elements of the strings array into a single String. The separator string is placed between elements in the resulting string.
     *
     * @param strings   The input array to concatenate
     * @param separator The separator to use
     * @return Concatenated String
     */
    public static String join(final String[] strings, final String separator) throws IllegalArgumentException {
        if (strings == null) {
            throw new IllegalArgumentException("Input array 'strings' can't be null");
        }
        if (separator == null) {
            throw new IllegalArgumentException("separator can't be null");
        }
        StringBuilder joiner = new StringBuilder(separator);
        for (String el : strings) {
            joiner.append(el);
        }
        return joiner.toString();
    }

    /**
     * Converts the first character of string to upper case and the remaining to lower case.
     *
     * @param input The string to capitalize
     * @return The capitalized string
     */
    public static String capitalize(final String input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        if (input.length() == 0) {
            return "";
        }
        char[] chars = input.toCharArray();
        for(int i=1, j=chars.length; i<j; i++){
        	chars[i] = Character.toLowerCase(chars[i]);
        }
        chars[0] = Character.toUpperCase(chars[0]);
        return String.valueOf(chars);
    }

    /**
     * Converts the first character of string to lower case.
     *
     * @param input The string to convert
     * @return The converted string
     * @throws IllegalArgumentException
     */
    public static String lowerFirst(final String input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        if (input.length() == 0) {
            return "";
        }
        char[] chars = input.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }

    /**
     * Verifies whether String is enclosed by encloser
     *
     * @param input    The input String
     * @param encloser String which encloses input String
     * @return true if enclosed false otherwise
     */
    public static boolean isEnclosedBetween(final String input, final String encloser) {
        return isEnclosedBetween(input, encloser, encloser);
    }

    /**
     * Verifies whether String is enclosed by encloser
     *
     * @param input         The input String
     * @param leftEncloser  String which encloses input String at left start
     * @param rightEncloser String which encloses input String at the right end
     * @return true if enclosed false otherwise
     */
    public static boolean isEnclosedBetween(final String input, final String leftEncloser, String rightEncloser) {
        if (input == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        if (leftEncloser == null) {
            throw new IllegalArgumentException("leftEncloser can't be null");
        }
        if (rightEncloser == null) {
            throw new IllegalArgumentException("rightEncloser can't be null");
        }
        return input.startsWith(leftEncloser) && input.endsWith(rightEncloser);
    }

    /**
     * Converts the first character of string to upper case.
     *
     * @param input The string to convert.
     * @return Returns the converted string.
     */
    public static String upperFirst(String input) {
        if (input == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        char[] chars = input.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return String.valueOf(chars);
    }

    /**
     * Removes leading whitespace from string.
     *
     * @param input The string to trim.
     * @return Returns the trimmed string.
     */
    public static String trimStart(final String input) {
    	validate(input, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
    	return !input.isEmpty() ? leftTrim(input) : input;
    }


    /**
     * Removes leading characters from string.
     *
     * @param input The string to trim.
     * @param chars The characters to trim.
     *
     * @return Returns the trimmed string.
     */
    public static String trimStart(final String input, String... chars) {
    	validate(input, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
    	return !input.isEmpty() ? input.replaceAll(String.format("^[%s]+", join(chars, "\\")), "") : input;
    }

    /**
     * Removes trailing whitespace from string.
     *
     * @param input The string to trim.
     * @return Returns the trimmed string.
     */
    public static String trimEnd(final String input) {
    	validate(input, NULL_STRING_PREDICATE, NULL_STRING_PREDICATE);
    	return !input.isEmpty() ? rightTrim(input) : input;
    }


    /**
     * Removes trailing characters from string.
     *
     * @param input The string to trim.
     * @param chars The characters to trim.
     *
     * @return Returns the trimmed string.
     */
    public static String trimEnd(final String input, String... chars) {
    	validate(input, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
    	return !input.isEmpty() ? input.replaceAll(String.format("[%s]+$", join(chars, "\\")), input) : input;
    }

    /*
     * 验证字符串是否为Null， 如果为Null， 则抛出空指针异常
     */
    private static void validate(String value, String predicate, final String supplier) {
        if (value == predicate) {
            throw new IllegalArgumentException(supplier);
        }
    }

    /*
     * 
     */
    private static long countSubstr(String value, String subStr, boolean allowOverlapping, long count) {
        int position = value.indexOf(subStr);
        if (position == -1) {
            return count;
        }
        int offset;
        if (!allowOverlapping) {
            offset = position + subStr.length();
        } else {
            offset = position + 1;
        }
        return countSubstr(value.substring(offset), subStr, allowOverlapping, ++count);
    }

}
