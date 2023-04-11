package cn.maodun.processor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DELL
 * @date 2023/4/11
 */
public class SubAProcessor implements Processor {
    @Override
    public boolean needExecute(ProcessRequest processRequest, ProcessContext processContext) {
        return true;
    }

    @Override
    public void execute(ProcessRequest processRequest, ProcessContext processContext) {
        Integer id = processRequest.getId();
        List<String> infos = getById(id);
        processContext.setInfos(infos);
    }

    private List<String> getById(Integer id) {
        return new ArrayList<>();
    }
}
