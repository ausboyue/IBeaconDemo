package cn.icheny.ibeacon.demo;

/**
 * IBeacon实体类
 *
 * @author www.icheny.cn
 * @date 2019.04.16
 */
public class IBeacon {
    // 名称
    public String name;
    // major主码
    public int major;
    // minor次码
    public int minor;
    // 唯一识别码 （统一厂商的唯一识别码可能一样，不具唯一性）
    public String uuid;
    // 设备地址（mac地址，如： "00:11:22:AA:BB:CC"）
    public String address;
    // 电量
    public int txPower;
    // 信号强度
    public int rssi;
    // 估算距离
    public String distance;
}
