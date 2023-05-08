package cn.maodun.handlerChain;

/**
 * 责任连
 *
 * @author DELL
 * @date 2023/5/8
 */
public abstract class ApprovalHandler {

    private ApprovalHandler next;

    /**
     * 设置下一个处理对象
     *
     * @param approvalHandler
     */
    public void nextHandler(ApprovalHandler approvalHandler) {
        this.next = approvalHandler;
    }

    /**
     * 处理
     *
     * @param approvalContext
     */
    public abstract void approval(ApprovalContext approvalContext);

    /**
     * 调用下一个处理对象
     *
     * @param approvalContext
     */
    protected void invokeNext(ApprovalContext approvalContext) {
        if (next != null) {
            next.approval(approvalContext);
        }
    }
}
