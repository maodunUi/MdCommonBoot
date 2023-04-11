package cn.maodun.processor;

/**
 * 处理器
 *
 * @author DELL
 * @date 2023/4/11
 */
public interface Processor {
    /**
     * 是否执行处理器
     */
    boolean needExecute(ProcessRequest processRequest, ProcessContext processContext);

    /**
     * 执行处理器
     */
    void execute(ProcessRequest processRequest, ProcessContext processContext);
}
