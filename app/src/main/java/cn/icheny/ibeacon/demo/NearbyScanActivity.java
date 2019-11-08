package cn.icheny.ibeacon.demo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

/**
 * NearbyScanActivity扫描附近的ibeacon设备，模拟从后台取商品信息
 *
 * @author www.icheny.cn
 */
public class NearbyScanActivity extends Activity {

    private CommodityAdapter mLeDeviceListAdapter;
    /**
     * 搜索BLE终端
     */
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private ListView lv_ommodity;
    // 扫描频率

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_scan);
        lv_ommodity = findViewById(R.id.lv_ommodity);

        // 判断手机等设备是否支持BLE，即是否可以扫描iBeacon设备
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // 开启蓝牙
        mBluetoothAdapter.enable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        // scanLeDevice(true);
        mLeDeviceListAdapter = new CommodityAdapter(this);
        lv_ommodity.setAdapter(mLeDeviceListAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        mLeDeviceListAdapter.clear();
    }

    // iBeacon设备扫描回调结果
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

            final IBeacon ibeacon = IBeaconProductor.fromScanData(device, rssi, scanRecord);
            final Commodity commodity = getCommodity(ibeacon);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addCommodity(commodity);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    /**
     * 模拟根据ibeacon信息从后台取对应商品据信息
     * 这里面的uuid，major，minor 都是我这边的iBeacon设备
     * 你可以填你自己的iBeacon设备相关信息，进行iBeacon设备过滤
     *
     * @param ibeacon
     * @return
     */
    private Commodity getCommodity(IBeacon ibeacon) {
        if ("fda50693-a4e2-4fb1-afcf-c6eb07647825".equalsIgnoreCase(ibeacon.uuid) && 10001 == ibeacon.major// 这里是对照UUID，major,minor作为模拟唯一的识别id
                && 64120 == ibeacon.minor) {
            return new Commodity("1122", R.drawable.a, 288.00, "老诚一锅6-8人餐\n6-8人餐,免费wifi,美味营养,回味无穷!", ibeacon.distance);

        } else if ("fda50693-a4e2-4fb1-afcf-c6eb07647825".equalsIgnoreCase(ibeacon.uuid) && 10 == ibeacon.major
                && 7 == ibeacon.minor) {
            return new Commodity("4455", R.drawable.b, 258.00, "净味真烤羊腿套餐\n烤羊腿套餐,可使用包间", ibeacon.distance);

        } else if ("fda50693-a4e2-4fb1-afcf-c6eb07647825".equalsIgnoreCase(ibeacon.uuid)
                && 10111 == ibeacon.major && 7 == ibeacon.minor) {
            return new Commodity("7788", R.drawable.c, 258.00, "新疆纸皮核桃\n【全国免费配送】新疆纸皮核桃2袋共1000g,仅55元，享价值116元（原价值每袋68元）",
                    ibeacon.distance);
        }
        return null;
    }
}