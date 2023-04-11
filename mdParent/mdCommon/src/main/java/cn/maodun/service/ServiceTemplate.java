package cn.maodun.service;

/**
 * @author 抽象类
 * @date 2023/4/11
 */
public abstract class ServiceTemplate<T, R> {

    public R process(T request) {
        validParam(request);

        R response = doProcess(request);

        return response;
    }

    abstract R doProcess(T request);

    abstract void validParam(T request);
}
