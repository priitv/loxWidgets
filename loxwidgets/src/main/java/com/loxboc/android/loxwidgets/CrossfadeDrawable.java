package com.loxboc.android.loxwidgets;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Enhanced {@link android.graphics.drawable.TransitionDrawable} that has ability to cross-fade between multiple drawables.<br>
 * <br>
 * CrossfadeDrawable crossfader = new CrossfadeDrawable(fromBitmapDrawable);<br>
 * getRootView().setBackground(crossfader);<br>
 * crossfader.startTransition(300, toBitmapDrawable);<br>
 */
@SuppressWarnings("unused")
public class CrossfadeDrawable extends Drawable implements Drawable.Callback {
	private static final int TRANSITION_STARTING = 0;
	private static final int TRANSITION_RUNNING = 1;
	private static final int TRANSITION_NONE = 2;
	private int transitionState = TRANSITION_NONE;

	private long startTimeMillis;
	private int from, to; // if we want to implement reverse transition
	private int duration;
	private int alpha = 0;
	private int maxAlpha = 0xFF;

	@Nullable
	private Drawable fromDrawable;
	@Nullable
	private Drawable toDrawable;

	public CrossfadeDrawable() {
	}

	/**
	 * Initialize with first drawable
	 *
	 * @param fromDrawable firstly displayed drawable. Can be null in which case drawable will be transparent
	 */
	public CrossfadeDrawable(@Nullable Drawable fromDrawable) {
		this.fromDrawable = fromDrawable;
	}

	/**
	 * Cross-fade to new drawable from last drawable that was set to this class
	 *
	 * @param durationMillis animation duration
	 * @param toDrawable     drawable to be cross-faded into
	 */
	public void startTransition(int durationMillis, Drawable toDrawable) {
		Drawable tempToDrawable = this.toDrawable;
		if (tempToDrawable != null) {
			Drawable fromDrawable = this.fromDrawable;
			if (fromDrawable != null) {
				fromDrawable.setCallback(null);
			}
			this.fromDrawable = tempToDrawable;
		}

		toDrawable.setCallback(this);
		this.toDrawable = toDrawable;
		from = 0;
		to = maxAlpha;
		alpha = 0;
		duration = durationMillis;
		transitionState = TRANSITION_STARTING;
		invalidateSelf();
	}

	/**
	 * Specify an alpha value for the drawable. 0 means fully transparent, and
	 * 255 means fully opaque.
	 * Next call to {@link #startTransition} animates only to this alpha level.
	 */
	@Override
	public void setAlpha(int alpha) {
		maxAlpha = alpha;
		if (fromDrawable != null) {
			fromDrawable.setAlpha(alpha);
		}
		if (toDrawable != null) {
			toDrawable.setAlpha(alpha);
		}
	}

	@Override
	public void setColorFilter(ColorFilter colorFilter) {
		if (fromDrawable != null) {
			fromDrawable.setColorFilter(colorFilter);
		}
		if (toDrawable != null) {
			toDrawable.setColorFilter(colorFilter);
		}
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	@Override
	public void draw(Canvas canvas) {
		setDrawableBounds(canvas, fromDrawable);
		setDrawableBounds(canvas, toDrawable);
		boolean done = loadState();
		if (done) {
			drawDoneState(canvas);
			return;
		} else {
			drawProgress(canvas);
		}
		invalidateSelf();
	}

	private void setDrawableBounds(@NonNull Canvas canvas, Drawable drawable) {
		if (drawable != null) {
			drawable.setBounds(canvas.getClipBounds());
		}
	}

	/**
	 * @return true if transition has ended
	 */
	private boolean loadState() {
		boolean done = true;
		switch (transitionState) {
			case TRANSITION_STARTING:
				startTimeMillis = SystemClock.uptimeMillis();
				done = false;
				transitionState = TRANSITION_RUNNING;
				break;
			case TRANSITION_RUNNING:
				long startTimeMillis = this.startTimeMillis;
				if (startTimeMillis >= 0) {
					float normalized = (float)
							(SystemClock.uptimeMillis() - startTimeMillis) / duration;
					done = normalized >= 1.0f;
					normalized = Math.min(normalized, 1.0f);
					alpha = (int) (from + (to - from) * normalized);
				}
				break;
		}
		return done;
	}

	private void drawProgress(Canvas canvas) {
		int alpha = this.alpha, maxAlpha = this.maxAlpha;
		Drawable d;
		if (fromDrawable != null) {
			d = fromDrawable;
			if (maxAlpha < 0xFF) d.setAlpha(maxAlpha - alpha);
			d.draw(canvas);
			if (maxAlpha < 0xFF) d.setAlpha(maxAlpha);
		}
		if (alpha > 0 && toDrawable != null) {
			d = toDrawable;
			d.setAlpha(alpha);
			d.draw(canvas);
			d.setAlpha(maxAlpha);
		}
	}

	private void drawDoneState(Canvas canvas) {
		int maxAlpha = this.maxAlpha;
		Drawable d = null;
		if (alpha == 0 && fromDrawable != null) {
			d = fromDrawable;
		}
		if (alpha == maxAlpha && toDrawable != null) {
			d = toDrawable;
		}
		if (d != null) {
			if (maxAlpha < 0xFF) d.setAlpha(maxAlpha);
			d.draw(canvas);
		}
	}

	@Override
	public void invalidateDrawable(Drawable who) {
		invalidateSelf();
	}

	@Override
	public void scheduleDrawable(Drawable who, Runnable what, long when) {
		scheduleSelf(what, when);
	}

	@Override
	public void unscheduleDrawable(Drawable who, Runnable what) {
		unscheduleSelf(what);
	}
}