package ly.school.bean;

/**
 * Created by Administrator on 2016/11/17.
 */
public class SchoolBean {
    private String name;
    private String xuehao;
    private String yuanxi;
    private String zhuanye;
    private String banji;

    public SchoolBean(String name, String xuehao, String yuanxi, String zhuanye, String banji) {
        this.name = name;
        this.xuehao = xuehao;
        this.yuanxi = yuanxi;
        this.zhuanye = zhuanye;
        this.banji = banji;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXuehao() {
        return xuehao;
    }

    public void setXuehao(String xuehao) {
        this.xuehao = xuehao;
    }

    public String getYuanxi() {
        return yuanxi;
    }

    public void setYuanxi(String yuanxi) {
        this.yuanxi = yuanxi;
    }

    public String getZhuanye() {
        return zhuanye;
    }

    public void setZhuanye(String zhuanye) {
        this.zhuanye = zhuanye;
    }

    public String getBanji() {
        return banji;
    }

    public void setBanji(String banji) {
        this.banji = banji;
    }
}
