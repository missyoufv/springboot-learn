package cn.duw.springboot.datasource;

public enum DataSourceEnum {

    LOCAL("local"), TEST("test");


    /**
     * 数据源
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    DataSourceEnum(java.lang.String name) {
        this.name = name;
    }
}
