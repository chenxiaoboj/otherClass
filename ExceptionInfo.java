package bazi.star.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author chenx 2019-02-20 18:39
 */
@Entity
public class ExceptionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /**
     * 异常信息
     */
    private String message;
    /**
     * 异常的cookie
     */
    private String cookie;
    /**
     * 异常步骤(1:验证码 2：下订单 3：获取支付验证码)
     */
    private Integer count;

    /**
     * 获取支付二维码的url
     */
    private String getEwmUrl;
    /**
     * 订单id
     */
    private String authOrdersId;

    public String getAuthOrdersId() {
        return authOrdersId;
    }

    public void setAuthOrdersId(String authOrdersId) {
        this.authOrdersId = authOrdersId;
    }

    public String getGetEwmUrl() {
        return getEwmUrl;
    }

    public void setGetEwmUrl(String getEwmUrl) {
        this.getEwmUrl = getEwmUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
