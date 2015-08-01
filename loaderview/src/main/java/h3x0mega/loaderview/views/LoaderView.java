package h3x0mega.loaderview.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import h3x0mega.loaderview.R;

/**
 * View including a progress bar and an error layout to be used in asynchronous operation
 * Created by H3x0mega on 8/1/2015.
 */
public class LoaderView extends FrameLayout {

    private final static int ANIMATION_DURATION = 600;

    private RelativeLayout progressContainer;
    private RelativeLayout errorContainer;

    private TryAgainCallback listener;
    private AnimatorSet showErrorAnimatorSet;
    private AnimatorSet showProgressAnimatorSet;
    private List<Animator> queue;
    private boolean currentlyPlaying;

    public LoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    /**
     * Initializes this view's attributes
     * @param attrs Set containing the following arguments
     *              error_image (Reference): ID of the image resource to be shown. May be null
     *              error_message (String): Message to be displayed on the error screen. May be null
     *              loader_message (String): Message to be displayed on the loading screen. May be null
     */
    private void initialize(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoaderView);
        View root = inflate(getContext(), R.layout.view_loader_layout, null);

        progressContainer = (RelativeLayout) root.findViewById(R.id.progress_container);
        errorContainer = (RelativeLayout) root.findViewById(R.id.error_container);
        final TextView progressMessage = (TextView) root.findViewById(R.id.progress_message);
        TextView errorMessage  = (TextView) root.findViewById(R.id.error_message);
        final ImageView errorImage = (ImageView) root.findViewById(R.id.error_image);
        Button tryAgain = (Button) root.findViewById(R.id.try_again);

        ColorDrawable colorDrawable = new ColorDrawable(typedArray.getColor(R.styleable.LoaderView_loader_color, getResources().getColor(R.color.accent)));
        ((ProgressBar) root.findViewById(R.id.progress_bar)).setProgressDrawable(colorDrawable);

        String progressString = typedArray.getString(R.styleable.LoaderView_loader_message);
        progressMessage.setText(progressString != null ? progressString : getContext().getString(R.string.default_loading));
        progressMessage.setTextColor(typedArray.getColor(R.styleable.LoaderView_progress_message_text_color, getResources().getColor(R.color.secondary_text)));

        String errorString = typedArray.getString(R.styleable.LoaderView_error_message);
        errorMessage.setText(errorString != null ? errorString : getContext().getString(R.string.default_error));
        errorMessage.setTextColor(typedArray.getColor(R.styleable.LoaderView_error_message_text_color, getResources().getColor(R.color.secondary_text)));

        tryAgain.setTextColor(typedArray.getColor(R.styleable.LoaderView_button_text_color, getResources().getColor(R.color.accent_dark)));

        Drawable drawable = typedArray.getDrawable(R.styleable.LoaderView_error_image);
        if (drawable != null)
            errorImage.setImageDrawable(drawable);

        typedArray.recycle();

        queue = new ArrayList<>();

        tryAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onTryAgain();
                    hideError();
                }
            }
        });

        showErrorAnimatorSet = new AnimatorSet();
        showProgressAnimatorSet = new AnimatorSet();

        ObjectAnimator hideProgress = ObjectAnimator.ofFloat(progressContainer, "alpha", 1f, 0f);
        ObjectAnimator showProgress = ObjectAnimator.ofFloat(progressContainer, "alpha", 0f, 1f);
        ObjectAnimator hideError = ObjectAnimator.ofFloat(errorContainer, "alpha", 1f, 0f);
        ObjectAnimator showError = ObjectAnimator.ofFloat(errorContainer, "alpha", 0f, 1f);

        showErrorAnimatorSet.playSequentially(hideProgress, showError);
        showErrorAnimatorSet.setDuration(ANIMATION_DURATION);
        showErrorAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressContainer.setVisibility(GONE);
                LoaderView.this.onAnimationEnded();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                errorContainer.setVisibility(VISIBLE);
            }
        });

        showProgressAnimatorSet.playSequentially(hideError, showProgress);
        showProgressAnimatorSet.setDuration(ANIMATION_DURATION);
        showProgressAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                errorContainer.setVisibility(GONE);
                LoaderView.this.onAnimationEnded();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                progressContainer.setVisibility(VISIBLE);
            }
        });

        addView(root);
    }

    /**
     * Plays in order the Animators contained in the queue. The queue is respected,
     * making the animators wait until the previous one has already completed. This method
     * works in along with the onAnimationEnded method
     * @param animator If != null, the animator will be added to the queue
     */
    private void playAnimatorQueue(Animator animator) {
        if (animator != null) {
            queue.add(animator);
        }

        if (queue.size() > 0 && !currentlyPlaying) {
            queue.get(0).start();
            currentlyPlaying = true;
        }
    }

    /**
     * This method continues to play the queue if any remaining Animators are still present. This
     * should be called on the Animator's AnimatorListenerAdapter's onAnimationEnd to be able
     * to maintain the queue's order
     */
    private void onAnimationEnded() {
        if (queue.size() > 0) {
            currentlyPlaying = false;
            queue.remove(0);

            if (queue.size() > 0) {
                playAnimatorQueue(null);
            }
        }
    }

    /**
     * Shows the error layout
     */
    public void showError() {
        playAnimatorQueue(showErrorAnimatorSet);
    }

    /**
     * Hides the error layout
     */
    private void hideError() {
        playAnimatorQueue(showProgressAnimatorSet);
    }

    /**
     * Hides the loader completely using a fade animation
     */
    public void finish() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        animator.setDuration(ANIMATION_DURATION);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                LoaderView.this.setVisibility(GONE);
            }
        });

        playAnimatorQueue(animator);
    }

    /**
     * Sets the listener for the 'Try Again' action
     * @param listener The listener for the button's OnClick
     */
    public void setTryAgainListener(TryAgainCallback listener) {
        if (listener != null)
            this.listener = listener;
    }

    /**
     * Removes the listener for the 'Try Again' action of this view
     */
    public void removeTryAgainListener() {
        this.listener = null;
    }

    /**
     * Callback for the 'Try Again' button
     */
    public interface TryAgainCallback {
        void onTryAgain();
    }
}
