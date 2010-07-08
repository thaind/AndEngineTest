package org.anddev.andengine.test.entity;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;

import android.os.SystemClock;
import android.test.AndroidTestCase;
import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 15:27:27 - 12.05.2010
 */
public class SceneTest extends AndroidTestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Engine mEngine;
	private Camera mCamera;
	private Scene mScene;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void setUp() throws Exception {
		this.mCamera = new Camera(0, 0, 100, 100);
		this.mEngine = new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new FillResolutionPolicy(), this.mCamera, true));
		this.mEngine.setSurfaceSize(100, 100);

		this.mScene = new Scene(1);

		this.mEngine.setScene(this.mScene);

		this.mEngine.start();
	}

	@Override
	public void tearDown() throws Exception {
		this.mEngine.stop();
	}

	// ===========================================================
	// Test-Methods
	// ===========================================================

	public void testSceneTouchCenter() throws Exception {
		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final float expectedX = 50;
		final float expectedY = 50;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testSceneTouchEdge() throws Exception {
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 100;

		final float expectedX = 0;
		final float expectedY = 100;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testSceneTouchOffsetCamera() throws Exception {
		this.mCamera.setCenter(0, 0);

		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final float expectedX = 0;
		final float expectedY = 0;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testChildSceneTouch() throws Exception {
		final Scene childScene = new Scene(1);

		this.mScene.setChildSceneModal(childScene);

		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final float expectedX = 50;
		final float expectedY = 50;

		this.testSceneTouchWorker(childScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testChildSceneTouchOffsetCamera() throws Exception {
		this.mCamera.setCenter(0, 0);

		final Scene childScene = new Scene(1);

		this.mScene.setChildSceneModal(childScene);

		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final float expectedX = 0;
		final float expectedY = 0;

		this.testSceneTouchWorker(childScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testAreaTouchSimple() throws Exception {
		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final ITouchArea touchArea = new Rectangle(0, 0, 50, 50);
		
		assertTrue(this.testAreaTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, touchArea));
	}
	
	public void testAreaTouchOutside() throws Exception {
		final int surfaceTouchX = 51;
		final int surfaceTouchY = 51;

		final ITouchArea touchArea = new Rectangle(0, 0, 50, 50);
		
		assertFalse(this.testAreaTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, touchArea));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void testSceneTouchWorker(final Scene pScene, final int pSurfaceTouchX, final int pSurfaceTouchY, final float pExpectedX, final float pExpectedY) {
		pScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final MotionEvent pSceneMotionEvent) {
				final float actualX = pSceneMotionEvent.getX();
				final float actualY = pSceneMotionEvent.getY();
				assertEquals(pExpectedX, actualX);
				assertEquals(pExpectedY, actualY);
				return true;
			}
		});

		final long uptimeMillis = SystemClock.uptimeMillis();

		final boolean result = this.mEngine.onTouch(null, MotionEvent.obtain(uptimeMillis, uptimeMillis, MotionEvent.ACTION_DOWN, pSurfaceTouchX, pSurfaceTouchY, 0));

		assertTrue(result);
	}

	private boolean testAreaTouchWorker(final Scene pScene, final int pSurfaceTouchX, final int pSurfaceTouchY, final ITouchArea pExpectedTouchArea) {
		pScene.registerTouchArea(pExpectedTouchArea);
		
		pScene.setOnAreaTouchListener(new IOnAreaTouchListener() {
			@Override
			public boolean onAreaTouched(final ITouchArea pActualTouchArea, final MotionEvent pSceneMotionEvent) {
				assertSame(pExpectedTouchArea, pActualTouchArea);
				return true;
			}
		});

		final long uptimeMillis = SystemClock.uptimeMillis();

		return this.mEngine.onTouch(null, MotionEvent.obtain(uptimeMillis, uptimeMillis, MotionEvent.ACTION_DOWN, pSurfaceTouchX, pSurfaceTouchY, 0));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}