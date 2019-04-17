package cn.icheny.ibeacon.demo;

import android.bluetooth.BluetoothDevice;

/**
 * IBeacon生产包装类
 * 帮助android扫描到的蓝牙设备对象转成IBeacon对象
 *
 * @author www.icheny.cn
 * @date 2019.04.16
 */
public class IBeaconProductor {

    /**
     * 从蓝牙设备对象中解析出IBeacon对象
     *
     * @param device
     * @param rssi
     * @param scanData
     * @return
     */
    public static IBeacon fromScanData(BluetoothDevice device, int rssi, byte[] scanData) {

        int startByte = 2;
        boolean patternFound = false;
        while (startByte <= 5) {
            if (((int) scanData[startByte + 2] & 0xff) == 0x02 && ((int) scanData[startByte + 3] & 0xff) == 0x15) {
                // 这是 iBeacon
                patternFound = true;
                break;
            } else if (((int) scanData[startByte] & 0xff) == 0x2d && ((int) scanData[startByte + 1] & 0xff) == 0x24
                    && ((int) scanData[startByte + 2] & 0xff) == 0xbf
                    && ((int) scanData[startByte + 3] & 0xff) == 0x16) {
                IBeacon beacon = new IBeacon();
                beacon.major = 0;
                beacon.minor = 0;
                beacon.uuid = "00000000-0000-0000-0000-000000000000";
                beacon.txPower = -55;
                beacon.distance = String.format("%.2f", calculateAccuracy(beacon.txPower, rssi));
                return beacon;
            } else if (((int) scanData[startByte] & 0xff) == 0xad && ((int) scanData[startByte + 1] & 0xff) == 0x77
                    && ((int) scanData[startByte + 2] & 0xff) == 0x00
                    && ((int) scanData[startByte + 3] & 0xff) == 0xc6) {

                IBeacon beacon = new IBeacon();
                beacon.major = 0;
                beacon.minor = 0;
                beacon.uuid = "00000000-0000-0000-0000-000000000000";
                beacon.txPower = -55;
                beacon.distance = String.format("%.2f", calculateAccuracy(beacon.txPower, rssi));
                return beacon;
            }
            startByte++;
        }

        if (patternFound == false) {
            // 这不是iBeacon
            return null;
        }

        IBeacon beacon = new IBeacon();

        beacon.major = (scanData[startByte + 20] & 0xff) * 0x100 + (scanData[startByte + 21] & 0xff);
        beacon.minor = (scanData[startByte + 22] & 0xff) * 0x100 + (scanData[startByte + 23] & 0xff);
        beacon.txPower = (int) scanData[startByte + 24];
        beacon.rssi = rssi;

        // 格式化UUID
        byte[] uuidBytes = new byte[16];
        System.arraycopy(scanData, startByte + 4, uuidBytes, 0, 16);
        String hexString = bytesToHexString(uuidBytes);
        StringBuilder sb = new StringBuilder();
        sb.append(hexString.substring(0, 8));
        sb.append("-");
        sb.append(hexString.substring(8, 12));
        sb.append("-");
        sb.append(hexString.substring(12, 16));
        sb.append("-");
        sb.append(hexString.substring(16, 20));
        sb.append("-");
        sb.append(hexString.substring(20, 32));
        beacon.uuid = sb.toString();

        if (device != null) {
            beacon.address = device.getAddress();
            beacon.name = device.getName();
        }
        beacon.distance = String.format("%.2f", calculateAccuracy(beacon.txPower, rssi));
        return beacon;
    }

    /**
     * 转换十进制
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 估算用户设备到IBeacon的距离
     *
     * @param txPower
     * @param rssi
     * @return
     */
    private static double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }
}
