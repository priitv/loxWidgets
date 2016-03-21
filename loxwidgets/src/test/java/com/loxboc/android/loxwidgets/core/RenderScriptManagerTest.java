package com.loxboc.android.loxwidgets.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class RenderScriptManagerTest {
	@Test
	public void imageScalingWorks() throws Exception {
		int[] size;
		size = RenderScriptManager.getScaledRect(300, 600, 300);
		System.out.println("300, 600, 300: " + Arrays.toString(size));
		Assert.assertEquals(150, size[0]);
		Assert.assertEquals(300, size[1]);

		size = RenderScriptManager.getScaledRect(600, 300, 300);
		System.out.println("600, 300, 300: " + Arrays.toString(size));
		Assert.assertEquals(300, size[0]);
		Assert.assertEquals(150, size[1]);

		size = RenderScriptManager.getScaledRect(400, 180, 300);
		System.out.println("400, 180, 300: " + Arrays.toString(size));
		Assert.assertEquals(300, size[0]);
		Assert.assertEquals(135, size[1]);
	}
}
