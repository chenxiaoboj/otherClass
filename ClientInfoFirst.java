package bazi.star.dto;

import javax.persistence.Entity;

/**
 * @author chenx 2019-02-19 14:03
 */
@Entity
public class ClientInfoFirst {
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String idNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
