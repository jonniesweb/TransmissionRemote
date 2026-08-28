package net.yupol.transmissionremote.app;

import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public abstract class BaseActivity extends AppCompatActivity {

    private final OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (handleBackPressByFragments()) return;

            setEnabled(false);
            try {
                getOnBackPressedDispatcher().onBackPressed();
            } finally {
                setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOnBackPressedDispatcher().addCallback(this, backPressedCallback);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        View actionBarContainer = findViewById(androidx.appcompat.R.id.action_bar_container);
        View content = findViewById(android.R.id.content);
        if (actionBarContainer == null || content == null) return;

        View.OnLayoutChangeListener applyInsets = (view, left, top, right, bottom,
                oldLeft, oldTop, oldRight, oldBottom) ->
                applyActionBarInsets(actionBarContainer, content, ViewCompat.getRootWindowInsets(content));
        actionBarContainer.addOnLayoutChangeListener(applyInsets);
        ViewCompat.setOnApplyWindowInsetsListener(content, (view, windowInsets) -> {
            applyActionBarInsets(actionBarContainer, view, windowInsets);
            return windowInsets;
        });
        content.post(() -> applyActionBarInsets(
                actionBarContainer,
                content,
                ViewCompat.getRootWindowInsets(content)
        ));
        ViewCompat.requestApplyInsets(content);
    }

    private static void applyActionBarInsets(
            View actionBarContainer,
            View content,
            @Nullable WindowInsetsCompat windowInsets
    ) {
        Insets systemBars = windowInsets == null
                ? Insets.NONE
                : windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
        content.setPadding(
                systemBars.left,
                actionBarContainer.getBottom(),
                systemBars.right,
                systemBars.bottom
        );
    }

    /**
     * @return {@code true} if back press handled by visible fragments
     */
    protected boolean handleBackPressByFragments() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof OnBackPressedListener && fragment.isVisible()) {
                boolean handled = ((OnBackPressedListener) fragment).onBackPressed();
                if (handled) return true;
            }
        }
        return false;
    }
}
