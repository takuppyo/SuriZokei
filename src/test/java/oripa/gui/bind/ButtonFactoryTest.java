package oripa.gui.bind;

public class ButtonFactoryTest {
//	@Test
//	public void test() {
//
//		JPanel parent = new JPanel();
//
//		var actionHolder = mock(MouseActionHolder.class);
//		var screenUpdater = mock(ScreenUpdater.class);
//
//		// line input buttons
//		assertButtonCreated(parent, StringID.DIRECT_V_ID, actionHolder, screenUpdater, false);
//		assertButtonCreated(parent, StringID.ON_V_ID, actionHolder, screenUpdater, false);
//		assertButtonCreated(parent, StringID.SYMMETRIC_ID, actionHolder, screenUpdater, false);
//		assertButtonCreated(parent, StringID.TRIANGLE_ID, actionHolder, screenUpdater, false);
//		assertButtonCreated(parent, StringID.BISECTOR_ID, actionHolder, screenUpdater, false);
//		assertButtonCreated(parent, StringID.VERTICAL_ID, actionHolder, screenUpdater, false);
//		assertButtonCreated(parent, StringID.MIRROR_ID, actionHolder, screenUpdater, false);
//		assertButtonCreated(parent, StringID.BY_VALUE_ID, actionHolder, screenUpdater, false);
////		assertButtonCreated(parent, StringID.PICK_LENGTH_ID, false);
////		assertButtonCreated(parent, StringID.PICK_ANGLE_ID, false);
//		assertButtonCreated(parent, StringID.PERPENDICULAR_BISECTOR_ID, actionHolder, screenUpdater,
//				false);
//		assertButtonCreated(parent, StringID.ANGLE_SNAP_ID, actionHolder, screenUpdater,
//				false);
//
//		// edit buttons
//		assertButtonCreated(parent, StringID.SELECT_ID, actionHolder, screenUpdater, true);
//		assertButtonCreated(parent, StringID.ADD_VERTEX_ID, actionHolder, screenUpdater, true);
//		assertButtonCreated(parent, StringID.CHANGE_LINE_TYPE_ID, actionHolder, screenUpdater,
//				true);
//		assertButtonCreated(parent, StringID.DELETE_LINE_ID, actionHolder, screenUpdater, true);
//		assertButtonCreated(parent, StringID.DELETE_VERTEX_ID, actionHolder, screenUpdater, true);
//		assertButtonCreated(parent, StringID.COPY_PASTE_ID, actionHolder, screenUpdater, true);
//		assertButtonCreated(parent, StringID.CUT_PASTE_ID, actionHolder, screenUpdater, true);
//		assertButtonCreated(parent, StringID.EDIT_CONTOUR_ID, actionHolder, screenUpdater, true);
//		assertButtonCreated(parent, StringID.SELECT_ALL_LINE_ID, actionHolder, screenUpdater, true);
//	}
//
//	private void assertButtonCreated(final Component parent, final String id,
//			final MouseActionHolder actionHolder,
//			final ScreenUpdater screenUpdater,
//			final boolean hasLabel) {
//		PaintContextFactory contextFactory = new PaintContextFactory();
//		var stateFactory = mock(PaintBoundStateFactory.class);
//		var context = contextFactory.createContext();
//		ButtonFactory paintFactory = new PaintActionButtonFactory(
//				stateFactory, context, actionHolder, screenUpdater);
//
//		var state = mock(PaintBoundState.class);
//		when(stateFactory.create(parent, actionHolder, context, screenUpdater, id))
//				.thenReturn(state);
//		var keyListener = mock(KeyListener.class);
//		JButton button;
//		button = paintFactory.create(parent, JButton.class,
//				id,
//				keyListener);
//
//		verify(stateFactory).create(parent, actionHolder, context, screenUpdater, id);
//
//		String text = button.getText();
//		System.out.println(id + " text:" + text);
//
//		if (hasLabel) {
//			assertNotNull(text);
//			assertTrue(text.length() > 0);
//		}
//
//		assertEquals(keyListener, button.getKeyListeners()[0]);
//		// button.doClick();
//		// test hint text
//
//		// test paint action
//
//	}
}
