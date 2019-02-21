package bazi.star.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author chenx 2019-02-21 17:55
 */
@Entity
public class SuccessOrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /**
     * 支付二维码地址
     */
    private String ewmUrl;
    /**
     * 请求参数
     */
    private String parameter;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEwmUrl() {
        return ewmUrl;
    }

    public void setEwmUrl(String ewmUrl) {
        this.ewmUrl = ewmUrl;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
