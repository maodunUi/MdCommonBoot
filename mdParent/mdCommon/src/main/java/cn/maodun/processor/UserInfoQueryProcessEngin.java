package cn.maodun.processor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DELL
 * @date 2023/4/11
 */
public class UserInfoQueryProcessEngin extends AbstractProcessEngineImpl{

    private static final List<ProcessNameEnum> processorList = new ArrayList<>();

    static {
        processorList.add(ProcessNameEnum.SUB_QUERY_INFO);
    }

    @Override
    List<ProcessNameEnum> getProcessors() {
        return processorList;
    }
}
