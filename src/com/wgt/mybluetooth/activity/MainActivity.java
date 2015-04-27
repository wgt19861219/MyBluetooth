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
	 * �������
	 */
	private BluetoothAdapter mBluetoothAdapter;

	/**
	 * �ؼ����
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

		// ����һ������ACTION_FOUND�㲥��BroadcastReceiver
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// �����豸
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// ��Intent�л�ȡ�豸����
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// ���豸���ƺ͵�ַ����array adapter���Ա���ListView����ʾ
					mArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				}
			}
		};
		// ע��BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // ��Ҫ����֮������

	}

	private void initUI() {
		/**
		 * ���ñ�������ģ��
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
	 * �������ģ��״̬
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
				// �򿪱������������ֹ��ܣ�Ĭ�ϴ�120�룬���Խ�ʱ������ӳ���300�룩
				Intent discoveryIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoveryIntent.putExtra(
						BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);// ���ó���ʱ�䣨���300�룩
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
