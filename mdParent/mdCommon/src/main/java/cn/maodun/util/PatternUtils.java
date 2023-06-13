package cn.maodun.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具
 *
 * @author DELL
 * @date 2023/6/13
 */
public class PatternUtils {

    /**
     * 在这个示例中，validatePhoneNumber方法接收一个字符串参数phoneNumber，
     * 并将其与正则表达式^1[3-9]\\d{9}$进行匹配。这个正则表达式可以验证以1开头，第二位为3-9的数字，后面跟着9个数字的手机号。
     *
     * @param phoneNumber 校验值
     * @return 如果匹配成功，返回true，否则返回false。
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        String regex = "^1[3-9]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }


    /**
     * 这段正则表达式用于验证中国大陆身份证号码的格式。以下是对每个部分的解释：
     * <p>
     * ^[1-9]：以非零数字开头。
     * \\d{5}：接下来是5个数字。
     * (18|19|20)?：年份的前两位可能是18、19或20，这是一个可选的分组。
     * \\d{2}：接下来是2个数字，表示年份的后两位。
     * (0[1-9]|1[0-2])：月份，可以是01到12之间的数字。
     * (0[1-9]|[12]\\d|3[01])：日期，可以是01到31之间的数字，考虑了每个月的不同天数。
     * \\d{3}：接下来是3个数字，表示地区码。
     * (\\d|X|x)：最后一位可以是数字或大写/小写字母X，表示校验码。
     * 整体上，这个正则表达式可以匹配符合中国大陆身份证号码格式要求的字符串，其中包括地区码、年份、月份、日期和校验码的合法组合。
     * <p>
     * 需要注意的是，该正则表达式仅验证身份证号码的格式，而不对号码的真实性进行验证。如果需要验证身份证号码的真实性，需要使用其他方法，例如校验码的计算等。
     *
     * @param idNumber 校验值
     * @return 如果匹配成功，返回true，否则返回false。
     */
    public static boolean validateIdNumber(String idNumber) {
        String regex = "^[1-9]\\d{5}(18|19|20)?\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|X|x)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(idNumber);
        return matcher.matches();
    }


    /**
     * 正则表达式"^[\\p{L}\\p{M}\\p{N}]+$"的含义如下：
     * <p>
     * ^：表示匹配字符串的开头。
     * [\\p{L}\\p{M}\\p{N}]：表示一个字符类，用于匹配Unicode属性为字母（\\p{L}）、标记（\\p{M}）或数字（\\p{N}）的字符。
     * +：表示匹配前面的字符类一次或多次。
     * $：表示匹配字符串的结尾。
     * 综合起来，这个正则表达式可以匹配满足以下条件的字符串：
     * <p>
     * 字符串中的每个字符都是Unicode属性为字母、标记或数字的字符。
     * 字符串包含至少一个字符。
     * 换句话说，它可以用来验证一个字符串是否只包含全文字（即不包含数字、符号、空格等非文字字符）。
     * <p>
     * 需要注意的是，\\p{L}匹配各种字母字符，包括中文、英文字母等；\\p{M}匹配各种标记字符，如重音符号、变音符号等；\\p{N}匹配各种数字字符。这个正则表达式的目的是匹配多种语言和字符集下的文字字符。
     *
     * @param text 校验值
     * @return 如果匹配成功，返回true，否则返回false。
     */
    public static boolean validateFullText(String text) {
        String regex = "^[\\p{L}\\p{M}\\p{N}]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }


    /**
     * 正则表达式"^[0-9]+$"的含义如下：
     * <p>
     * ^：表示匹配字符串的开头。
     * [0-9]：表示一个字符类，用于匹配数字字符。这里的0-9表示范围从0到9的数字字符。
     * +：表示匹配前面的字符类一次或多次。
     * $：表示匹配字符串的结尾。
     * 综合起来，这个正则表达式可以匹配满足以下条件的字符串：
     * <p>
     * 字符串中的每个字符都是数字字符（0到9）。
     * 字符串包含至少一个字符。
     * 换句话说，它可以用来验证一个字符串是否只包含数字字符。
     * <p>
     * 需要注意的是，这个正则表达式仅匹配ASCII数字字符（0到9）。如果需要匹配其他字符集的数字字符，可能需要使用对应字符集的范围或Unicode属性。
     * <p>
     * 此外，这个正则表达式不会考虑负号、小数点、千位分隔符等其他数字相关的特殊字符。如果需要包含这些特殊字符，请根据具体需求修改正则表达式。
     *
     * @param input 校验值
     * @return 如果匹配成功，返回true，否则返回false。
     */
    public static boolean validateAllDigits(String input) {
        String regex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
