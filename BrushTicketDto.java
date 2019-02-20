package bazi.star.dto;

/**
 * @author chenx 2019-02-19 9:36
 */
public class BrushTicketDto {
    /**
     * 代理ip
     */
    private String hostName;
    /**
     * 端口
     */
    private Integer port;

    public BrushTicketDto(String hostName, Integer port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}
