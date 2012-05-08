package org.andengine.entity.primitive;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.andengine.entity.primitive.Line;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.Constants;
import org.andengine.util.algorithm.collision.LineCollisionChecker;

/**
 * @author Nicolas Gramlich
 * @since 13:50:55 - 22.06.2011
 */
public class LineTest extends TestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float DELTA = 0.0001f;

	private static final float[] VERTICES = new float[4];

	// ===========================================================
	// Fields
	// ===========================================================

	private final VertexBufferObjectManager mVertexBufferObjectManager = new VertexBufferObjectManager();

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void setUp() throws Exception {
		this.mVertexBufferObjectManager.onDestroy();
		this.mVertexBufferObjectManager.onCreate();
	}

	@Override
	public void tearDown() throws Exception {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void testSimple() {
		final Line line = new Line(-1, 0, 1, 0, this.mVertexBufferObjectManager);

		this.assertLine(-1, 0, 1, 0, line);
	}

	public void testReverse() {
		final Line line = new Line(1, 0, -1, 0, this.mVertexBufferObjectManager);

		this.assertLine(1, 0, -1, 0, line);
	}

	public void testRotation90() {
		final Line line = new Line(-1, 0, 1, 0, this.mVertexBufferObjectManager);
		line.setRotation(90);

		this.assertLine(0, 1, 0, -1, line);
	}

	public void testRotation180() {
		final Line line = new Line(-1, 0, 1, 0, this.mVertexBufferObjectManager);
		line.setRotation(180);

		this.assertLine(1, 0, -1, 0, line);
	}

	public void testReverseRotation180() {
		final Line line = new Line(1, 0, -1, 0, this.mVertexBufferObjectManager);
		line.setRotation(180);

		this.assertLine(-1, 0, 1, 0, line);
	}

	private void assertLine(final int pX1, final int pY1, final int pX2, final int pY2, final Line pLine) {
		LineCollisionChecker.fillVertices(pLine, LineTest.VERTICES);

		Assert.assertEquals(pX1, LineTest.VERTICES[0 + Constants.VERTEX_INDEX_X], LineTest.DELTA);
		Assert.assertEquals(pY1, LineTest.VERTICES[0 + Constants.VERTEX_INDEX_Y], LineTest.DELTA);
		Assert.assertEquals(pX2, LineTest.VERTICES[2 + Constants.VERTEX_INDEX_X], LineTest.DELTA);
		Assert.assertEquals(pY2, LineTest.VERTICES[2 + Constants.VERTEX_INDEX_Y], LineTest.DELTA);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
