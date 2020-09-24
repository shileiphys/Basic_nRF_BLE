package com.example.mybasicnrf;

import android.Manifest;
import android.bluetooth.BluetoothManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public class SecondFragment extends Fragment {
    private final static String TAG = "ScannerFragment";

    private final static String PARAM_UUID = "param_uuid";
    private final static long SCAN_DURATION = 5000;
    private ParcelUuid uuid;
//    public static final ParcelUuid uuid = ParcelUuid.fromString("0000b81d-0000-1000-8000-00805f9b34fb");

    private final static int REQUEST_PERMISSION_REQ_CODE = 34;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.scan_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
//                Log.v(TAG, "start scan");
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "granted start", Toast.LENGTH_SHORT).show();
                    startScan();
                }
            }
        }
    }


    private void startScan() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_REQ_CODE);

            return;
        }

        final BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
        final ScanSettings settings = new ScanSettings.Builder()
                .setLegacy(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(1000)
                .setUseHardwareBatchingIfSupported(false)
                .build();

        List<ScanFilter> filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder().setServiceUuid(uuid).build());
        scanner.startScan(filters, settings, scanCallback);
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(final int callbackType, @NonNull final ScanResult result) {
            // do nothing
        }

        @Override
        public void onBatchScanResults(@NonNull final List<ScanResult> results) {
//            Log.v(TAG, "size:" + results.size());
            Toast myToast = Toast.makeText(getActivity(), "has " + results.size(), Toast.LENGTH_SHORT);
            myToast.show();
        }

        @Override
        public void onScanFailed(final int errorCode) {
            // should never be called
        }
    };
}