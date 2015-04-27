package com.wgt.mybluetooth.activity;

import com.wgt.mybluetooth.R;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener {
	/**
	 * 蓝牙相关
	 */
	private BluetoothAdapter mBluetoothAdapter;

	/**
	 * 控件相关
	 */
	private ImageView iv;
	private Button btn_search;
	private ListView lv;
	private ArrayAdapter mArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initUI();
		updateStatus();

		// 创建一个接收ACTION_FOUND广播的BroadcastReceiver
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// 发现设备
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// 从Intent中获取设备对象
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// 将设备名称和地址放入array adapter，以便在ListView中显示
					mArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				}
			}
		};
		// 注册BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // 不要忘了之后解除绑定

	}

	private void initUI() {
		/**
		 * 配置本机蓝牙模块
		 */
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(intent, 0x1);
		iv = (ImageView) findViewById(R.id.iv);
		iv.setOnClickListener(this);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
		lv = (ListView) findViewById(R.id.lv);
		mArrayAdapter = new ArrayAdapter(this, 0);
		lv.setAdapter(mArrayAdapter);
	}

	/**
	 * 获得蓝牙模块状态
	 */
	private void updateStatus() {
		iv.setImageResource(mBluetoothAdapter.isEnabled() ? R.drawable.close_button
				: R.drawable.open_button);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv:
			if (!mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.enable();
				updateStatus();
				// 打开本机的蓝牙发现功能（默认打开120秒，可以将时间最多延长至300秒）
				Intent discoveryIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoveryIntent.putExtra(
						BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);// 设置持续时间（最多300秒）
			} else {
				mBluetoothAdapter.disable();
				updateStatus();
			}
			break;
		case R.id.btn_search:
			mBluetoothAdapter.startDiscovery();
		default:
			break;
		}
	}

}
