package oripa.gui.viewchange.main.uipanel;

import oripa.gui.view.main.UIPanelSetting;
import oripa.gui.viewchange.ChangeViewSetting;

public class ChangeOnAngleSnapButtonSelected implements ChangeViewSetting {
	private final UIPanelSetting setting;

	/**
	 * change UIPanel view settings for Angle Snap Line Input Tool
	 */
	public ChangeOnAngleSnapButtonSelected(final UIPanelSetting uiPanelSetting) {
		setting = uiPanelSetting;
	}

	@Override
	public void changeViewSetting() {
		setting.selectInputMode();

		setting.setByValuePanelVisible(false);

		setting.setLineSelectionPanelVisible(false);
		setting.setLineInputPanelVisible(true);

		setting.setAngleStepPanelVisible(true);

		setting.setAlterLineTypePanelVisible(false);
	}

}
