package com.siminfo;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Arguments;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoWcdma;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthWcdma;
import android.net.TrafficStats;
import android.content.Context;
import android.util.Log;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;

public class SIMInfoModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext mContext;
    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    private static final String TAG = "MyActivity";
    private TelephonyManager tm = null;

    public SIMInfoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return "SIMInfo";
    }

    @ReactMethod
    public void getSIMInfo(Callback msgcallback) {
        // Network Speed
        String mobile_download = Long.toString(TrafficStats.getMobileRxBytes());
        String mobile_upload = Long.toString(TrafficStats.getMobileTxBytes());
        String total_download = Long.toString(TrafficStats.getTotalRxBytes());
        String total_upload = Long.toString(TrafficStats.getTotalTxBytes());

        tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        // String operator = tm.getNetworkOperator();
        // String imei = tm.getImei();
        // String carrierIdName = tm.getSimCarrierIdName().toString();
        List<CellInfo> cellInfoList = tm.getAllCellInfo();
        String cellInfoList_str = cellInfoList.toString();
        WritableMap res_info = Arguments.createMap();
        res_info.putInt("list_size", cellInfoList.size());
        if (cellInfoList != null) {
            for (final CellInfo info : cellInfoList) {
                // Network Type
                if (info instanceof CellInfoGsm) { // info instanceof CellInfoGsm
                    final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                    final CellIdentityGsm identityGsm = ((CellInfoGsm) info).getCellIdentity();
                    res_info.putString("type", "gsm");
                    res_info.putInt("gsm-strength", gsm.getDbm());
                    res_info.putInt("gsm-rssi", gsm.getAsuLevel());
                    res_info.putInt("gsm-arfcn", identityGsm.getArfcn());
                    res_info.putInt("gsm-bsic", identityGsm.getBsic());
                    res_info.putInt("gsm-cid", identityGsm.getCid());
                    res_info.putString("gsm-mcc", identityGsm.getMccString());
                    res_info.putString("gsm-mnc", identityGsm.getMncString());
                    res_info.putString("gsm-mobilenetworkoperator", identityGsm.getMobileNetworkOperator());
                    res_info.putInt("gsm-lac", identityGsm.getLac());
                } else if (info instanceof CellInfoCdma) { // info instanceof CellInfoCdma
                    final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                    final CellIdentityCdma identityCdma = ((CellInfoCdma) info).getCellIdentity();
                    res_info.putString("type", "cdma");
                    res_info.putInt("cdma-strength", cdma.getDbm());
                    res_info.putInt("cdma-rssi", cdma.getCdmaDbm());
                    res_info.putInt("cdma-cid", identityCdma.getBasestationId());
                    res_info.putInt("cdma-mnc", identityCdma.getSystemId());
                    res_info.putInt("cdma-lac", identityCdma.getNetworkId());
                    res_info.putInt("cdma-sid", identityCdma.getSystemId());
                } else if (info instanceof CellInfoLte) { // info instanceof CellInfoLte
                    final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                    final CellIdentityLte identityLte = ((CellInfoLte) info).getCellIdentity();
                    res_info.putString("type", "lte");
                    res_info.putInt("lte-strength", lte.getDbm());
                    res_info.putInt("lte-rsrp", lte.getRsrp());
                    res_info.putInt("lte-rsrq", lte.getRsrq());
                    res_info.putInt("lte-bandwidth", identityLte.getBandwidth());
                    res_info.putInt("lte-cid", identityLte.getCi());
                    res_info.putInt("lte-earfcn", identityLte.getEarfcn());
                    res_info.putString("lte-mcc", identityLte.getMccString());
                    res_info.putString("lte-mnc", identityLte.getMncString());
                    res_info.putInt("lte-pci", identityLte.getPci());
                    res_info.putInt("lte-tac", identityLte.getTac());
                } else if (info instanceof CellInfoWcdma) { // info instanceof CellInfoWcdma
                    final CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                    final CellIdentityWcdma identityWcdma = ((CellInfoWcdma) info).getCellIdentity();
                    res_info.putString("type", "wcdma");
                    res_info.putInt("wcdma-strength", wcdma.getDbm());
                    res_info.putInt("wcdma-cid", identityWcdma.getCid());
                    res_info.putInt("wcdma-lac", identityWcdma.getLac());
                    res_info.putString("wcdma-mcc", identityWcdma.getMccString());
                    res_info.putString("wcdma-mnc", identityWcdma.getMncString());
                    res_info.putString("wcdma-mobilenetworkoperator", identityWcdma.getMobileNetworkOperator());
                    res_info.putInt("wcdma-psc", identityWcdma.getPsc());
                    res_info.putInt("wcdma-uarfcn", identityWcdma.getUarfcn());
                } else {
                    Log.i(TAG, "Unknown type of cell signal!" + "\n ClassName: " + info.getClass().getSimpleName()
                            + "\n ToString: " + info.toString());
                }
            }
        }
        msgcallback.invoke(cellInfoList_str, res_info, mobile_download, mobile_upload, total_download, total_upload, "just for test");
    }
}