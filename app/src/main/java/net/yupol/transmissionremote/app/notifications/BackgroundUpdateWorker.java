package net.yupol.transmissionremote.app.notifications;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import net.yupol.transmissionremote.app.TransmissionRemote;
import net.yupol.transmissionremote.app.model.json.Torrents;
import net.yupol.transmissionremote.app.server.Server;
import net.yupol.transmissionremote.app.transport.RequestExecutor;
import net.yupol.transmissionremote.app.transport.request.TorrentGetRequest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class BackgroundUpdateWorker extends Worker {

    private static final String UNIQUE_WORK_NAME = "update_torrents";
    private static final String TAG = BackgroundUpdateWorker.class.getSimpleName();

    public BackgroundUpdateWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        List<Server> servers = TransmissionRemote.getApplication(context).getServers();
        CountDownLatch countDownLatch = new CountDownLatch(servers.size());
        RequestExecutor requestExecutor = new RequestExecutor(context);
        FinishedTorrentsNotificationManager notificationManager =
                new FinishedTorrentsNotificationManager(context);

        for (Server server : servers) {
            requestExecutor.executeRequest(new TorrentGetRequest(), server, new RequestListener<Torrents>() {
                @Override
                public void onRequestSuccess(Torrents torrents) {
                    notificationManager.checkForFinishedTorrents(server, torrents);
                    countDownLatch.countDown();
                }

                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    Log.e(TAG, "Failed to retrieve torrent list from " + server.getName(), spiceException);
                    countDownLatch.countDown();
                }
            });
        }

        try {
            if (!countDownLatch.await(2, TimeUnit.MINUTES)) {
                requestExecutor.unregisterAllListeners();
                return Result.retry();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            requestExecutor.unregisterAllListeners();
            return Result.retry();
        }

        return Result.success();
    }

    public static void schedule(Context context, boolean onlyUnmeteredNetwork) {
        NetworkType networkType = onlyUnmeteredNetwork
                ? NetworkType.UNMETERED
                : NetworkType.CONNECTED;
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(networkType)
                .build();
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                BackgroundUpdateWorker.class,
                15,
                TimeUnit.MINUTES
        ).setConstraints(constraints).build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
        );
    }

    public static void cancel(Context context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_NAME);
    }
}
