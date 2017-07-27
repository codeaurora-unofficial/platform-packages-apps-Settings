/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.gestures;

import android.content.Context;
import android.provider.Settings;

import com.android.settings.testutils.SettingsRobolectricTestRunner;
import com.android.settings.TestConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static android.provider.Settings.Secure.SYSTEM_NAVIGATION_KEYS_ENABLED;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SettingsRobolectricTestRunner.class)
@Config(manifest = TestConfig.MANIFEST_PATH, sdk = TestConfig.SDK_VERSION)
public class SwipeToNotificationPreferenceControllerTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Context mContext;

    private SwipeToNotificationPreferenceController mController;
    private static final String KEY_SWIPE_DOWN = "gesture_swipe_down_fingerprint";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mController = new SwipeToNotificationPreferenceController(mContext, null, KEY_SWIPE_DOWN);
    }

    @Test
    public void isAvailable_configIsTrue_shouldReturnTrue() {
        when(mContext.getResources().
                getBoolean(com.android.internal.R.bool.config_supportSystemNavigationKeys))
                .thenReturn(true);

        assertThat(mController.isAvailable()).isTrue();
    }

    @Test
    public void isAvailable_configIsFalse_shouldReturnFalse() {
        when(mContext.getResources().
                getBoolean(com.android.internal.R.bool.config_supportSystemNavigationKeys))
                .thenReturn(false);

        assertThat(mController.isAvailable()).isFalse();
    }

    @Test
    public void testSwitchEnabled_configIsSet_shouldReturnTrue() {
        // Set the setting to be enabled.
        final Context context = ShadowApplication.getInstance().getApplicationContext();
        Settings.System.putInt(context.getContentResolver(), SYSTEM_NAVIGATION_KEYS_ENABLED, 1);
        mController = new SwipeToNotificationPreferenceController(context, null, KEY_SWIPE_DOWN);

        assertThat(mController.isSwitchPrefEnabled()).isTrue();
    }

    @Test
    public void testSwitchEnabled_configIsNotSet_shouldReturnFalse() {
        // Set the setting to be disabled.
        final Context context = ShadowApplication.getInstance().getApplicationContext();
        Settings.System.putInt(context.getContentResolver(), SYSTEM_NAVIGATION_KEYS_ENABLED, 0);
        mController = new SwipeToNotificationPreferenceController(context, null, KEY_SWIPE_DOWN);

        assertThat(mController.isSwitchPrefEnabled()).isFalse();
    }
}
