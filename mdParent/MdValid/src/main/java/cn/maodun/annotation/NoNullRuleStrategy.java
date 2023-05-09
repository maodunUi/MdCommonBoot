package cn.maodun.annotation;

import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author DELL
 * @date 2022/8/2
 */
@Service
public class NoNullRuleStrategy implements RuleStrategy{

    @Override
    public String getRuleName() {
        return ParamCheckRuleEnum.NONULL;
    }

    @Override
    public void rule(Object o, String ruleName, String msg) {
        if (Objects.equals(getRuleName(),ruleName)){
            if (Objects.isNull(o)){
                throw new RuntimeException(msg);
            }
        }
    }

}
