package net.yupol.transmissionremote.app.notifications;

import android.content.Context;
import android.preference.PreferenceManager;

import net.yupol.transmissionremote.app.R;

public class BackgroundUpdater {

    public static void start(Context context) {
        boolean onlyUnmeteredNetwork = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.background_update_only_unmetered_wifi_key), true);
        BackgroundUpdateWorker.schedule(context, onlyUnmeteredNetwork);
    }

    public static void stop(Context context) {
        BackgroundUpdateWorker.cancel(context);
    }

    public static void restart(Context context) {
        stop(context);
        start(context);
    }
}
