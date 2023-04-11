package cn.maodun.processor;

/**
 * @author DELL
 * @date 2023/4/11
 */
public enum ProcessNameEnum {
    SUB_QUERY_INFO("subAProcessor");

    private String name;


    ProcessNameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
