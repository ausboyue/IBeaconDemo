package cn.icheny.ibeacon.demo;


/**
 * 商品实体类
 *
 * @author www.icheny.cn
 * @date 2019.04.16
 */
public class Commodity {

    // 商品编号
    private String commodityId;
    // 商品图片资源id
    private int iconId;
    // 价格
    private double price;
    // 商品描述
    private String desc;
    // IBeacon设备与用户距离
    private String distance;


    public Commodity() {
    }

    public Commodity(String commodityId, int iconId, double price, String desc, String distance) {
        super();
        this.commodityId = commodityId;
        this.iconId = iconId;
        this.price = price;
        this.desc = desc;
        this.distance = distance;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public double getPrice() {
        return price;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}
