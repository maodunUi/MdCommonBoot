package cn.maodun.processor;

/**
 * 流程引擎接口
 *
 * @author DELL
 * @date 2023/4/11
 */
public interface ProcessEngine {
    /**
     * 启动流程引擎
     */
    void start(ProcessRequest processRequest, ProcessContext processContext);
}
