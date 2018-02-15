package androidadministration.com.deviceadministrator;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final int DPM_ACTIVATION_REQUEST_CODE = 100;
    public static final int DPM_REQUEST_PASSWORD_CODE = 200;

    private Button requestPasswordChange;
    private ComponentName adminComponent;
    private DevicePolicyManager devicePolicyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(getPackageName(),getPackageName() + ".DeviceAdministrator");

        // Request device admin activation if not enabled.
        if (!devicePolicyManager.isAdminActive(adminComponent)) {

            Intent activateDeviceAdmin = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            activateDeviceAdmin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
            startActivityForResult(activateDeviceAdmin, DPM_ACTIVATION_REQUEST_CODE);

        }

        setContentView(R.layout.activity_main);

        requestPasswordChange = (Button) findViewById(R.id.requestPasswordChange);
        requestPasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Set password requirements.
                devicePolicyManager.setPasswordQuality(adminComponent, DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC); // Password must contain numbers and letters.
                devicePolicyManager.setPasswordMinimumLength(adminComponent, 5); // Password must be 5 characters long.

                // Request password change.
                Intent enforcePassword = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                startActivityForResult(enforcePassword, DPM_REQUEST_PASSWORD_CODE);

            }
        });

    }

}
