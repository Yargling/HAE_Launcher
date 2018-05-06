package mearns.douglas.haelauncher.widgets;

import android.content.Context;
import android.location.Address;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import mearns.douglas.haelauncher.R;
import mearns.douglas.haelauncher.geo.CountryInfoRetriever;
import mearns.douglas.haelauncher.geo.AddressMonitor;

public final class LocationInformation extends FrameLayout implements AddressMonitor.AddressReceiver {

    public LocationInformation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.location_info_layout, this, true);

        gpsDisplay = findViewById(R.id.countryName);
        capitalDisplay = findViewById(R.id.capitalText);
        currencyDisplay = findViewById(R.id.currencyText);
    }

    @Override
    public void processAddress(final Address address) {
        CountryInfoRetriever.instance().get(address.getCountryCode(), new CountryInfoRetriever.InformationProcessor() {
            @Override
            public void process(final JSONObject data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gpsDisplay.setText(getLocalisedCountryName(data));
                        currencyDisplay.setText(getCurrencyName(data));
                        capitalDisplay.setText(getCapitalName(data));
                    }
                });
            }
        });
    }

    private String getLocalisedCountryName(JSONObject data) {
        try {
            return data.getJSONObject("translations").getString(Locale.getDefault().getLanguage());
        } catch (JSONException e) {
            try {
                return data.getString("name");
            } catch (JSONException f) {
                f.printStackTrace();
                return "ERROR";
            }
        }
    }

    private String getCapitalName(JSONObject data) {
        try {
            return data.getString("capital");
        } catch (JSONException e) {
            e.printStackTrace();
            return "<unknown capital name>";
        }
    }

    private String getCurrencyName(JSONObject data) {
        try {
            final JSONArray currencies = data.getJSONArray("currencies");
            return ((JSONObject) currencies.get(0)).getString("name");
        } catch (Exception e) {
            e.printStackTrace();
            return "<unknown currency>";
        }
    }

    @Override
    public void processNoAddress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                gpsDisplay.setText("Country unknown");
                capitalDisplay.setText("");
                currencyDisplay.setText("");
            }
        });
    }

    private final Handler handler = new Handler();
    private final TextView gpsDisplay;
    private final TextView capitalDisplay;
    private final TextView currencyDisplay;
}

