package cn.maodun.annotation;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author DELL
 * @date 2023/6/13
 */
public class PatternRuleStrategy implements RuleStrategy {
    @Override
    public String getRuleName() {
        return ParamCheckRuleEnum.PATTERN;
    }

    @Override
    public void rule(Object o, ParamCheck annotation) {
        if (Objects.equals(getRuleName(), annotation.rule())) {

            String regex = annotation.patternRegex();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher((String) o);
            boolean matches = matcher.matches();

            if (!matches) {
                throw new RuntimeException(annotation.msg());
            }
        }
    }
}
