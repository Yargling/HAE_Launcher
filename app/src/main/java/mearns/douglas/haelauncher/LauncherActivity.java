package mearns.douglas.haelauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);

        final List<AppDetail> appList = getListOfApps();
        createAppDisplayList(appList);
        addClickListener(appList);
    }

    private List<AppDetail> getListOfApps() {
        manager = getPackageManager();
        List<AppDetail> apps = new ArrayList<>();

        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> availableActivities = manager.queryIntentActivities(intent, 0);
        for (ResolveInfo ri : availableActivities) {
            apps.add(new AppDetail(ri.loadLabel(manager), ri.activityInfo.packageName, ri.activityInfo.loadIcon(manager)));
        }

        return apps;
    }

    private void createAppDisplayList(final List<AppDetail> appList) {
        list = findViewById(R.id.apps_list);

        list.setAdapter(new ArrayAdapter<AppDetail>(this,
                R.layout.menu_item,
                appList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.menu_item, null);
                }

                final AppDetail details = appList.get(position);

                convertView.<ImageView>findViewById(R.id.item_app_icon)
                        .setImageDrawable(details.icon);
                convertView.<TextView>findViewById(R.id.item_app_label).setText(details.label);
                convertView.<TextView>findViewById(R.id.item_app_name).setText(details.name);

                return convertView;
            }
        });
    }

    private void addClickListener(final List<AppDetail> appList) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                Intent i = manager.getLaunchIntentForPackage(appList.get(pos).name.toString());
                LauncherActivity.this.startActivity(i);
            }
        });
    }

    private ListView list;
    private PackageManager manager;

    private static class AppDetail {
        AppDetail(CharSequence label, CharSequence name, Drawable icon) {
            this.label = label;
            this.name = name;
            this.icon = icon;
        }

        final CharSequence label;
        final CharSequence name;
        final Drawable icon;
    }
}
