package cn.maodun.handlerChain;

/**
 * @author DELL
 * @date 2023/5/8
 */
public class ApprovalHandlerChain {


    public static void main(String[] args) {
        ApprovalHandlerChain approvalHandlerChain = new ApprovalHandlerChain();
        ApprovalHandler chain = approvalHandlerChain.getChain();
        chain.approval(null);
    }


    public ApprovalHandler getChain() {
        GroupLeaderApprovalHandler groupLeaderApprovalHandler = new GroupLeaderApprovalHandler();

        //组长处理完下一个处理对象是主管
        DirectorApprovalHandler directorApprovalHandler = new DirectorApprovalHandler();


        //主管处理完下一个处理对象是hr
        HrApprovalHandler hrApprovalHandler = new HrApprovalHandler();


        groupLeaderApprovalHandler.nextHandler(directorApprovalHandler);

        directorApprovalHandler.nextHandler(hrApprovalHandler);

        //返回组长，这样就从组长开始审批，一条链就完成了
        return groupLeaderApprovalHandler;
    }
}
