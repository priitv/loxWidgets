package com.loxboc.android.loxwidgets.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

/**
 * RenderScript allows implementing heavy computations which are<br>
 * scaled transparently to all cores available on the executing device.<br>
 * Useful specially for image processing.<br>
 * <br>
 * RenderScriptManager rs = RenderScriptManager.create(context);<br>
 * Bitmap bitmap = rs.blur(image);<br>
 * rs.destroy();<br>
 */
@SuppressWarnings("unused")
public class RenderScriptManager {
	public static final float BLUR_DEFAULT_RADIUS = 14f;
	public static final int BLUR_DEFAULT_IMAGE_HEIGHT = 100;
	private RenderScript renderScript;

	private RenderScriptManager(@NonNull Context ctx) {
		renderScript = RenderScript.create(ctx);
	}

	/**
	 * Less instances of RenderScript's created is better
	 * @param ctx context to be used for RenderScript creation
	 * @return instance of RenderScriptManager
	 */
	@NonNull
	@CheckResult
	public static RenderScriptManager create(@NonNull Context ctx) {
		return new RenderScriptManager(ctx);
	}

	/** @see #blurImage(Bitmap, float, int) */
	public Bitmap blurImage(@NonNull Bitmap image) {
		return blurImage(image, BLUR_DEFAULT_RADIUS, BLUR_DEFAULT_IMAGE_HEIGHT);
	}

	/**
	 * Generate blurred version of inputted bitmap
	 * @param image to be blurred
	 * @param blurRadius {@link android.renderscript.ScriptIntrinsicBlur#setRadius(float)}
	 * @param outputMaxSideLength scale output image within box defined by given length
	 * @return blurred image
	 */
	public Bitmap blurImage(@NonNull Bitmap image, float blurRadius, int outputMaxSideLength) {
		int[] rect = getScaledRect(image.getWidth(), image.getHeight(), outputMaxSideLength);

		Bitmap inputBitmap = Bitmap.createScaledBitmap(image, rect[0], rect[1], false);
		Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

		RenderScript renderScript = this.renderScript;
		ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
		Allocation tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap);
		Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
		theIntrinsic.setRadius(blurRadius);
		theIntrinsic.setInput(tmpIn);
		theIntrinsic.forEach(tmpOut);
		tmpOut.copyTo(outputBitmap);

		return outputBitmap;
	}

	/**
	 * Calculate scaled image sizes
	 * @param origWidth original image width
	 * @param origHeight original image height
	 * @param outputMaxSideLength max side length for output image
	 * @return integer array where on first pos is width and on second pos height value
	 */
	public static int[] getScaledRect(int origWidth, int origHeight, int outputMaxSideLength) {
		boolean byHeight = origHeight >= origWidth;
		float scale = byHeight ? (float)origWidth/origHeight : (float)origHeight/origWidth;
		int height = byHeight ? outputMaxSideLength : Math.round(outputMaxSideLength*scale);
		int width = byHeight ? Math.round(outputMaxSideLength*scale) : outputMaxSideLength;
		return new int[]{width, height};
	}

	public void destroy() {
		renderScript.destroy();
	}
}