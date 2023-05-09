package cn.maodun.logs;

/**
 * 用于获取当前请求标识数据（例如用户名，用户ID，token等等）
 * @author Diamond
 */
public interface UserPrincipalProvider {
    /**
     * 由实现层去实现，该方法的返回值将会打印在日志中
     * @return 当期请求主要标识
     */
    Object getPrincipal();
}
