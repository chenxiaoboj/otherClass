package bazi.star.dto;

import java.util.List;

/**
 * @author chenx 2019-02-19 10:14
 */
public class Paramet {
    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 使用日期
     */
    private String playDate;
    /**
     * 人数
     */
    private Integer amount;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String idNumber;
    /**
     * 详细时间点
     */
    private String timeSlotDamoylxs;

    public String getTimeSlotDamoylxs() {
        return timeSlotDamoylxs;
    }

    public void setTimeSlotDamoylxs(String timeSlotDamoylxs) {
        this.timeSlotDamoylxs = timeSlotDamoylxs;
    }

    /**
     * 游客信息集合
     */
    private List<TouristInfo> touristInfoList;

    public List<TouristInfo> getTouristInfoList() {
        return touristInfoList;
    }

    public void setTouristInfoList(List<TouristInfo> touristInfoList) {
        this.touristInfoList = touristInfoList;
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getPlayDate() {
        return playDate;
    }

    public void setPlayDate(String playDate) {
        this.playDate = playDate;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
