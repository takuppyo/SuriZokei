/**
 * ORIPA - Origami Pattern Editor
 * Copyright (C) 2005-2009 Jun Mitani http://mitani.cs.tsukuba.ac.jp/

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package oripa.swing.view.main;

import static javax.swing.SwingConstants.*;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oripa.gui.presenter.creasepattern.EditMode;
import oripa.gui.view.View;
import oripa.gui.view.main.InitialVisibilities;
import oripa.gui.view.main.KeyProcessing;
import oripa.gui.view.main.MainViewSetting;
import oripa.gui.view.main.PainterScreenSetting;
import oripa.gui.view.main.UIPanelSetting;
import oripa.gui.view.main.UIPanelView;
import oripa.resource.ButtonIcon;
import oripa.resource.ButtonIconResource;
import oripa.resource.Constants;
import oripa.resource.ResourceHolder;
import oripa.resource.ResourceKey;
import oripa.resource.StringID;
import oripa.swing.view.util.Dialogs;
import oripa.swing.view.util.GridBagConstraintsBuilder;
import oripa.swing.view.util.ImageResourceLoader;
import oripa.swing.view.util.KeyStrokes;
import oripa.swing.view.util.TitledBorderFactory;

public class UIPanel extends JPanel implements UIPanelView {

	private static final Logger logger = LoggerFactory.getLogger(UIPanel.class);

	private final ResourceHolder resources = ResourceHolder.getInstance();
	private final MainDialogService dialogService = new MainDialogService(resources);

	private final UIPanelSetting setting;
//	private final ByValueSetting valueSetting = setting.getValueSetting();

	// main three panels
	private final JPanel editModePanel = new JPanel();
	private final JPanel toolSettingsPanel = new JPanel();
	private final JPanel generalSettingsPanel = new JPanel();

	// ---------------------------------------------------------------------------------------------------------------------------
	// Binding edit mode
	private final ButtonGroup editModeGroup;

	private final JRadioButton editModeInputLineButton = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.INPUT_LINE_ID));
	private final JRadioButton editModeLineSelectionButton = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.SELECT_ID));
	private final JRadioButton editModeDeleteLineButton = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.DELETE_LINE_ID));
	private final JRadioButton editModeLineTypeButton = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.CHANGE_LINE_TYPE_ID));
	private final JRadioButton editModeAddVertex = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.ADD_VERTEX_ID));
	private final JRadioButton editModeDeleteVertex = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.DELETE_VERTEX_ID));

	// Line Selection Tools panel
	private final JPanel lineSelectionPanel = new JPanel();

	private final JRadioButton selectionButton = new JRadioButton();
	private final JRadioButton enlargementButton = new JRadioButton();

	// Insert Line Tools Panel
	private final JPanel lineInputPanel = new JPanel();

	private final JRadioButton lineInputDirectVButton = new JRadioButton();
	private final JRadioButton lineInputOnVButton = new JRadioButton();
	private final JRadioButton lineInputVerticalLineButton = new JRadioButton();
	private final JRadioButton lineInputAngleBisectorButton = new JRadioButton();
	private final JRadioButton lineInputTriangleSplitButton = new JRadioButton();
	private final JRadioButton lineInputSymmetricButton = new JRadioButton();
	private final JRadioButton lineInputMirrorButton = new JRadioButton();
	private final JRadioButton lineInputByValueButton = new JRadioButton();
	private final JRadioButton lineInputPBisectorButton = new JRadioButton();
	private final JRadioButton lineInputAngleSnapButton = new JRadioButton();
	private final JRadioButton lineInputSuggestionButton = new JRadioButton();

	// lineTypePanel
	private final JPanel lineTypePanel = new JPanel();

	private final JRadioButton lineTypeAuxButton = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.AUX_ID));
	private final JRadioButton lineTypeMountainButton = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.MOUNTAIN_ID));
	private final JRadioButton lineTypeValleyButton = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.VALLEY_ID));
	private final JRadioButton lineTypeUnassignedButton = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.UNASSIGNED_ID));

	// byValuePanel for length and angle
	private final JPanel byValuePanel = new JPanel();

	private JFormattedTextField textFieldLength;
	private final JButton buttonLength = new JButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.MEASURE_ID));
	private JFormattedTextField textFieldAngle;
	private final JButton buttonAngle = new JButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.MEASURE_ID));

	// AlterLineTypePanel
	private final JPanel alterLineTypePanel = new JPanel();

	private final JComboBox<String> alterLineComboFrom = new JComboBox<>();
	private final JComboBox<String> alterLineComboTo = new JComboBox<>();

	// Angle Step Panel
	private final JPanel angleStepComboPanel = new JPanel();

	private final JComboBox<String> angleStepCombo = new JComboBox<>();

	// gridPanel
	private final JPanel gridPanel = new JPanel();

	private final JCheckBox dispGridCheckBox = new JCheckBox(
			resources.getString(ResourceKey.LABEL, StringID.UI.SHOW_GRID_ID),
			InitialVisibilities.GRID);
	private JFormattedTextField textFieldGrid;
	private final JButton gridSmallButton = new JButton("x2");
	private final JButton gridLargeButton = new JButton("x1/2");
	private final JButton gridChangeButton = new JButton(
			resources.getString(ResourceKey.LABEL,
					StringID.UI.GRID_SIZE_CHANGE_ID));

	// plug-in
	private final JPanel pluginPanel = new JPanel();
	private final List<JRadioButton> pluginButtons = new ArrayList<>();

	// view Panel
	private final JPanel viewPanel = new JPanel();

	private final JCheckBox dispMVULinesCheckBox = new JCheckBox(
			resources.getString(ResourceKey.LABEL, StringID.UI.SHOW_MVU_ID),
			InitialVisibilities.MVU);
	private final JCheckBox dispAuxLinesCheckBox = new JCheckBox(
			resources.getString(ResourceKey.LABEL, StringID.UI.SHOW_AUX_ID),
			InitialVisibilities.AUX);
	private final JCheckBox dispVertexCheckBox = new JCheckBox(
			resources.getString(ResourceKey.LABEL, StringID.UI.SHOW_VERTICES_ID),
			InitialVisibilities.VERTEX);
	private final JCheckBox doFullEstimationCheckBox = new JCheckBox(
			resources.getString(ResourceKey.LABEL,
					StringID.UI.FULL_ESTIMATION_ID),
			false);
	private final JCheckBox zeroLineWidthCheckBox = new JCheckBox(
			resources.getString(ResourceKey.LABEL, StringID.UI.ZERO_LINE_WIDTH_ID),
			InitialVisibilities.ZERO_LINE_WIDTH);

	// ActionButtons Panel
	private final JPanel buttonsPanel = new JPanel();

	private final JButton buildButton = new JButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.FOLD_ID));
	private final JButton checkWindowButton = new JButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.CHECK_WINDOW_ID));

	private Color estimationResultFrontColor;
	private Color estimationResultBackColor;
	private BiConsumer<Color, Color> estimationResultSaveColorsListener;

	private PropertyChangeListener paperDomainOfModelChangeListener;

	private Runnable modelComputationListener;
	private Runnable showFoldedModelWindowsListener;

	public UIPanel(final MainViewSetting viewSetting) {

		setting = viewSetting.getUiPanelSetting();

		setShortcuts();

		// edit mode Selection panel
		editModeGroup = new ButtonGroup();
		editModeGroup.add(editModeInputLineButton);
//		editModeGroup.add(editModeLineSelectionButton);
		editModeGroup.add(editModeDeleteLineButton);
		editModeGroup.add(editModeLineTypeButton);
//		editModeGroup.add(editModeAddVertex);
//		editModeGroup.add(editModeDeleteVertex);

		editModePanel.setBorder(createTitledBorderFrame(
				resources.getString(ResourceKey.LABEL, StringID.UI.TOOL_PANEL_ID)));
		editModePanel.setLayout(new GridBagLayout());

		var gbBuilder = new GridBagConstraintsBuilder(1)
				.setAnchor(GridBagConstraints.LINE_START)
				.setInsets(0, 5, 0, 0);

		editModePanel.add(editModeInputLineButton, gbBuilder.getLineField());
//		editModePanel.add(editModeLineSelectionButton, gbBuilder.getLineField());
		editModePanel.add(editModeDeleteLineButton, gbBuilder.getLineField());
		editModePanel.add(editModeLineTypeButton, gbBuilder.getLineField());
//		editModePanel.add(editModeAddVertex, gbBuilder.getLineField());
//		editModePanel.add(editModeDeleteVertex, gbBuilder.getLineField());

		// Tool settings panel
//		buildLineSelectionPanel();
		buildLineInputPanel();
		buildAlterLineTypePanel();
//		buildAngleStepPanel();
//		buildEditByValuePanel();

		toolSettingsPanel.setLayout(new GridBagLayout());
		toolSettingsPanel.setBorder(createTitledBorderFrame(
				resources.getString(ResourceKey.LABEL, StringID.UI.TOOL_SETTINGS_PANEL_ID)));

		gbBuilder = new GridBagConstraintsBuilder(1).setAnchor(GridBagConstraints.PAGE_START)
				.setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1);

//		toolSettingsPanel.add(lineSelectionPanel, gbBuilder.getNextField());
		toolSettingsPanel.add(lineInputPanel, gbBuilder.getLineField());
		toolSettingsPanel.add(alterLineTypePanel, gbBuilder.getLineField());
//		toolSettingsPanel.add(byValuePanel, gbBuilder.getLineField());
		toolSettingsPanel.add(angleStepComboPanel, gbBuilder.getLineField());

		// general settings panel
		buildGridPanel();
//		buildViewPanel();
		buildButtonsPanel();

		generalSettingsPanel.setLayout(new GridBagLayout());
		generalSettingsPanel.setBorder(createTitledBorderFrame(
				resources.getString(ResourceKey.LABEL, StringID.UI.GENERAL_SETTINGS_ID)));

		gbBuilder = new GridBagConstraintsBuilder(1).setAnchor(GridBagConstraints.PAGE_START)
				.setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0.5);

		generalSettingsPanel.add(gridPanel, gbBuilder.getLineField());
		generalSettingsPanel.add(viewPanel, gbBuilder.getLineField());
		generalSettingsPanel.add(buttonsPanel, gbBuilder.getLineField());

		// the main UIPanel
		setLayout(new GridBagLayout());

		gbBuilder = new GridBagConstraintsBuilder(1).setAnchor(GridBagConstraints.FIRST_LINE_START)
				.setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0.0);
		add(editModePanel, gbBuilder.getLineField());

		gbBuilder.setWeight(1, 1).setFill(GridBagConstraints.BOTH);
		add(toolSettingsPanel, gbBuilder.getLineField());

		gbBuilder.setWeight(1, 0.0).setFill(GridBagConstraints.HORIZONTAL)
				.setAnchor(GridBagConstraints.LAST_LINE_START);
		add(generalSettingsPanel, gbBuilder.getLineField());

		addPropertyChangeListenersToSetting(viewSetting.getPainterScreenSetting());
		buildButton.addActionListener(e -> showFoldedModelWindows());
	}

	@Override
	public void initializeButtonSelection(final String angleStep, final String typeFrom, final String typeTo) {
		// -------------------------------------------------
		// Initialize selection
		// -------------------------------------------------
		editModeInputLineButton.setSelected(true);
		angleStepCombo.setSelectedItem(angleStep);

		// of paint command
		selectionButton.doClick();
		lineInputDirectVButton.doClick();

		// of line type on setting
		alterLineComboFrom.setSelectedItem(typeFrom);
		alterLineComboTo.setSelectedItem(typeTo);

		doFullEstimationCheckBox.setSelected(true);
		lineTypeMountainButton.doClick();

	}

	@Override
	public void addMouseActionPluginListener(final String name, final Runnable listener,
			final KeyProcessing keyProcessing) {
		var button = new JRadioButton(name);

		addButtonListener(button, listener, keyProcessing);

		pluginButtons.add(button);
	}

	@Override
	public void updatePluginPanel() {

		var gbBuilder = new GridBagConstraintsBuilder(1) // 1 column used
				.setAnchor(GridBagConstraints.CENTER)
				.setWeight(1, 1)
				.setFill(GridBagConstraints.HORIZONTAL);

		pluginPanel.removeAll();

		pluginPanel.setBorder(createTitledBorder("Plug-in"));

		ButtonGroup buttonGroup = new ButtonGroup();

		pluginPanel.setLayout(new GridBagLayout());

		pluginButtons.forEach(button -> buttonGroup.add(button));

		logger.debug("{} plugins", pluginButtons.size());
		pluginButtons.forEach(button -> pluginPanel.add(button, gbBuilder.getNextField()));

		pluginPanel.setVisible(true);
	}

//	private void buildLineSelectionPanel() {
//		ButtonGroup lineSelectionGroup = new ButtonGroup();
//
//		lineSelectionGroup.add(selectionButton);
//		lineSelectionGroup.add(enlargementButton);
//
//		lineSelectionPanel.setLayout(new GridBagLayout());
//		lineSelectionPanel.setBorder(createTitledBorder(
//				resources.getString(ResourceKey.LABEL, StringID.UI.SELECT_ID)));
//
//		var gbBuilder = new GridBagConstraintsBuilder(4) // 4 columns used
//				.setAnchor(GridBagConstraints.CENTER) // anchor items in the
//														// center of their Box
//				.setWeight(0.5, 1);
//
//		var commandsLabel = new JLabel("Command");
//		commandsLabel.setHorizontalAlignment(SwingConstants.CENTER);
//		lineSelectionPanel.add(commandsLabel, gbBuilder.getLineField());
//
//		gbBuilder.setWeight(0.5, 0.5);
//
//		lineSelectionPanel.add(selectionButton, gbBuilder.getNextField());
//		lineSelectionPanel.add(enlargementButton, gbBuilder.getNextField());
//		// dummies to align buttons left
//		lineSelectionPanel.add(new JPanel(), gbBuilder.getNextField());
//		lineSelectionPanel.add(new JPanel(), gbBuilder.getNextField());
//
//		setLineSelectionButtonIcons();
//
//		lineSelectionPanel.setVisible(false);
//	}

	/**
	 * panel containing line input methods and line type selection
	 */
	private void buildLineInputPanel() {
		// extra panel just for line types
		ButtonGroup lineTypeGroup = new ButtonGroup();
		lineTypeGroup.add(lineTypeMountainButton);
		lineTypeGroup.add(lineTypeValleyButton);
		lineTypeGroup.add(lineTypeUnassignedButton);
		lineTypeGroup.add(lineTypeAuxButton);

		var lineTypeGbBuilder = new GridBagConstraintsBuilder(2);
		lineTypePanel.setLayout(new GridBagLayout());
		lineTypePanel.add(lineTypeMountainButton, lineTypeGbBuilder.getNextField());
		lineTypePanel.add(lineTypeValleyButton, lineTypeGbBuilder.getNextField());
		lineTypePanel.add(lineTypeUnassignedButton, lineTypeGbBuilder.getNextField());
		lineTypePanel.add(lineTypeAuxButton, lineTypeGbBuilder.getNextField());

		// How to enter the line
		ButtonGroup lineInputGroup = new ButtonGroup();
		lineInputGroup.add(lineInputDirectVButton);
		lineInputGroup.add(lineInputOnVButton);
//		lineInputGroup.add(lineInputTriangleSplitButton);
		lineInputGroup.add(lineInputAngleBisectorButton);
		lineInputGroup.add(lineInputVerticalLineButton);
		lineInputGroup.add(lineInputSymmetricButton);
		lineInputGroup.add(lineInputMirrorButton);
//		lineInputGroup.add(lineInputByValueButton);
		lineInputGroup.add(lineInputPBisectorButton);
		lineInputGroup.add(lineInputAngleSnapButton);
		lineInputGroup.add(lineInputSuggestionButton);

		// put layout together
		lineInputPanel.setLayout(new GridBagLayout());
		lineInputPanel.setBorder(createTitledBorder(
				resources.getString(ResourceKey.LABEL, StringID.UI.LINE_INPUT_PANEL_ID)));

		var gbBuilder = new GridBagConstraintsBuilder(4) // 4 columns used
				.setAnchor(GridBagConstraints.CENTER) // anchor items in the
														// center of their Box
				.setWeight(0.5, 1.0); // distribute evenly accross both axis,
										// 1.0 is needed to force max size
										// (maybe?)

		var lineTypeLabel = new JLabel("Line Type");
		lineTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lineInputPanel.add(lineTypeLabel, gbBuilder.getLineField());

		lineInputPanel.add(lineTypePanel, gbBuilder.getLineField());

		var commandsLabel = new JLabel("Command");
		commandsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lineInputPanel.add(commandsLabel, gbBuilder.getLineField());

		gbBuilder.setWeight(0.5, 0.5);
		// put operation buttons in order
		lineInputPanel.add(lineInputDirectVButton, gbBuilder.getNextField());
		lineInputPanel.add(lineInputOnVButton, gbBuilder.getNextField());
		lineInputPanel.add(lineInputPBisectorButton, gbBuilder.getNextField());
		lineInputPanel.add(lineInputAngleBisectorButton, gbBuilder.getNextField());
//		lineInputPanel.add(lineInputTriangleSplitButton, gbBuilder.getNextField());
		lineInputPanel.add(lineInputVerticalLineButton, gbBuilder.getNextField());
		lineInputPanel.add(lineInputSymmetricButton, gbBuilder.getNextField());
		lineInputPanel.add(lineInputMirrorButton, gbBuilder.getNextField());
//		lineInputPanel.add(lineInputByValueButton, gbBuilder.getNextField());
		lineInputPanel.add(lineInputAngleSnapButton, gbBuilder.getNextField());
		lineInputPanel.add(lineInputSuggestionButton, gbBuilder.getNextField());

		// dummies to align buttons left
		lineInputPanel.add(new JPanel(), gbBuilder.getNextField());

		setLineInputButtonIcons();

		gbBuilder.setFill(GridBagConstraints.HORIZONTAL);
//		lineInputPanel.add(pluginPanel, gbBuilder.getLineField());

	}

	/**
	 * display combobox for angle step line drawing tool
	 */
//	private void buildAngleStepPanel() {
//		angleStepComboPanel.setLayout(new GridBagLayout());
//		angleStepComboPanel.setBorder(createTitledBorder(
//				resources.getString(ResourceKey.LABEL, StringID.UI.ANGLE_STEP_ID)));
//
//		angleStepComboPanel.add(angleStepCombo, new GridBagConstraintsBuilder(1)
//				.setAnchor(GridBagConstraints.CENTER)
//				.setFill(GridBagConstraints.BOTH)
//				.getLineField());
//
//		angleStepComboPanel.setVisible(false);
//	}

	/**
	 * change line type tool settings panel
	 */
	private void buildAlterLineTypePanel() {
		var fromLabel = new JLabel(
				resources.getString(ResourceKey.LABEL,
						StringID.UI.CHANGE_LINE_TYPE_FROM_ID));

		var toLabel = new JLabel(
				resources.getString(ResourceKey.LABEL,
						StringID.UI.CHANGE_LINE_TYPE_TO_ID));

		alterLineTypePanel.setLayout(new GridBagLayout());
		alterLineTypePanel.setBorder(createTitledBorder(
				resources.getString(ResourceKey.LABEL, StringID.UI.ALTER_LINE_TYPE_PANEL_ID)));

		var gbBuilder = new GridBagConstraintsBuilder(2);

		alterLineTypePanel.add(fromLabel, gbBuilder.getNextField());
		alterLineTypePanel.add(alterLineComboFrom, gbBuilder.getNextField());
		alterLineTypePanel.add(toLabel, gbBuilder.getNextField());
		alterLineTypePanel.add(alterLineComboTo, gbBuilder.getNextField());

		alterLineTypePanel.setVisible(false);
	}

	/**
	 * input line by value tool
	 */
//	private void buildEditByValuePanel() {
//		var lengthLabel = new JLabel(
//				resources.getString(ResourceKey.LABEL, StringID.UI.LENGTH_ID));
//
//		var angleLabel = new JLabel(
//				resources.getString(ResourceKey.LABEL, StringID.UI.ANGLE_ID));
//
//		textFieldLength = new JFormattedTextField();
//		textFieldAngle = new JFormattedTextField();
//
//		textFieldLength.setColumns(5);
//		textFieldLength.setValue(0.0);
//		textFieldLength.setHorizontalAlignment(RIGHT);
//
//		textFieldAngle.setColumns(5);
//		textFieldAngle.setValue(0.0);
//		textFieldAngle.setHorizontalAlignment(RIGHT);
//
//		byValuePanel.setLayout(new GridBagLayout());
//		byValuePanel.setBorder(createTitledBorder(
//				resources.getString(ResourceKey.LABEL, StringID.UI.INSERT_BY_VALUE_PANEL_ID)));
//
//		var gbBuilder = new GridBagConstraintsBuilder(3)
//				.setAnchor(GridBagConstraints.CENTER)
//				.setFill(GridBagConstraints.NONE)
//				.setWeight(0, 1);
//
//		byValuePanel.add(lengthLabel, gbBuilder.getNextField());
//		byValuePanel.add(textFieldLength, gbBuilder.getNextField());
//		byValuePanel.add(buttonLength, gbBuilder.getNextField());
//
//		byValuePanel.add(angleLabel, gbBuilder.getNextField());
//		byValuePanel.add(textFieldAngle, gbBuilder.getNextField());
//		byValuePanel.add(buttonAngle, gbBuilder.getNextField());
//
//		byValuePanel.setVisible(false);
//	}

	/**
	 * grid size settings panel
	 */
	private void buildGridPanel() {
		var gridDivideLabel = new JLabel(
				resources.getString(ResourceKey.LABEL,
						StringID.UI.GRID_DIVIDE_NUM_ID));

		textFieldGrid = new JFormattedTextField(new DecimalFormat("#"));
		textFieldGrid.setColumns(2);
		textFieldGrid.setValue(Constants.DEFAULT_GRID_DIV_NUM);
		textFieldGrid.setHorizontalAlignment(RIGHT);

		gridPanel.setLayout(new GridBagLayout());
		gridPanel.setBorder(createTitledBorder(
				resources.getString(ResourceKey.LABEL, StringID.UI.GRID_SETTINGS_PANEL_ID)));

		var gbBuilder = new GridBagConstraintsBuilder(3);

		gridPanel.add(dispGridCheckBox, gbBuilder.getLineField());

		gridPanel.add(gridDivideLabel, gbBuilder.getNextField());
		gridPanel.add(textFieldGrid, gbBuilder.setWeight(1, 0.5).getNextField());
		gridPanel.add(gridChangeButton, gbBuilder.setWeight(0.5, 0.5).getNextField());

		gridPanel.add(gridSmallButton, gbBuilder.getNextField());
		gbBuilder.getNextField(); // empty field
		gridPanel.add(gridLargeButton, gbBuilder.getNextField());
	}

	/**
	 * view/display settings panel
	 */
//	private void buildViewPanel() {
//		viewPanel.setLayout(new GridBagLayout());
//
//		viewPanel.setBorder(createTitledBorder(
//				resources.getString(ResourceKey.LABEL, StringID.UI.VIEW_SETTINGS_PANEL_ID)));
//
//		var gbBuilder = new GridBagConstraintsBuilder(3);
//
//		viewPanel.add(zeroLineWidthCheckBox, gbBuilder.getLineField());
//
//		viewPanel.add(dispMVULinesCheckBox, gbBuilder.getLineField());
//		viewPanel.add(dispAuxLinesCheckBox, gbBuilder.getLineField());
//		viewPanel.add(dispVertexCheckBox, gbBuilder.getLineField());
//	}

	private void buildButtonsPanel() {
		buttonsPanel.setLayout(new GridBagLayout());

		buttonsPanel.setBorder(new MatteBorder(1, 0, 0, 0,
				getBackground().darker().darker()));

		var gbBuilder = new GridBagConstraintsBuilder(3);

		buttonsPanel.add(doFullEstimationCheckBox, gbBuilder.getLineField());

		buttonsPanel.add(checkWindowButton, gbBuilder.getLineField());
		buttonsPanel.add(buildButton, gbBuilder.getLineField());
	}

	private TitledBorder createTitledBorder(final String text) {
		return new TitledBorderFactory().createTitledBorder(this, text);
	}

	private TitledBorder createTitledBorderFrame(final String text) {
		return new TitledBorderFactory().createTitledBorderFrame(this, text);
	}

	private void setShortcuts() {
		setShortcut(editModeInputLineButton, KeyStrokes.get(KeyEvent.VK_I),
				StringID.UI.INPUT_LINE_ID);

		setShortcut(editModeDeleteLineButton, KeyStrokes.get(KeyEvent.VK_D),
				StringID.DELETE_LINE_ID);

		setShortcut(editModeLineTypeButton, KeyStrokes.get(KeyEvent.VK_T),
				StringID.CHANGE_LINE_TYPE_ID);

//		setShortcut(editModeAddVertex, KeyStrokes.get(KeyEvent.VK_X),
//				StringID.ADD_VERTEX_ID);
//
//		setShortcut(editModeDeleteVertex, KeyStrokes.get(KeyEvent.VK_Y),
//				StringID.DELETE_VERTEX_ID);

		// ---------------------------------------------------------------------------------------------------------------------------
		// Binding line selection tools
//		setLineSelectionGlobalShortcut(selectionButton, KeyStrokes.get(KeyEvent.VK_S),
//				StringID.SELECT_LINE_ID);
//
//		setLineSelectionGlobalShortcut(enlargementButton, KeyStrokes.getWithShiftDown(KeyEvent.VK_S),
//				StringID.ENLARGE_ID);

		// ---------------------------------------------------------------------------------------------------------------------------
		// Binding how to enter the line

		setLineInputGlobalShortcut(lineInputDirectVButton, KeyStrokes.get(KeyEvent.VK_E),
				StringID.DIRECT_V_ID);

		setLineInputGlobalShortcut(lineInputOnVButton, KeyStrokes.get(KeyEvent.VK_O),
				StringID.ON_V_ID);

		setLineInputGlobalShortcut(lineInputVerticalLineButton, KeyStrokes.get(KeyEvent.VK_V),
				StringID.VERTICAL_ID);

		setLineInputGlobalShortcut(lineInputAngleBisectorButton, KeyStrokes.get(KeyEvent.VK_B),
				StringID.BISECTOR_ID);

//		setLineInputGlobalShortcut(lineInputTriangleSplitButton, KeyStrokes.get(KeyEvent.VK_R),
//				StringID.TRIANGLE_ID);

		setLineInputGlobalShortcut(lineInputSymmetricButton, KeyStrokes.get(KeyEvent.VK_W),
				StringID.SYMMETRIC_ID);

		setLineInputGlobalShortcut(lineInputMirrorButton, KeyStrokes.get(KeyEvent.VK_M),
				StringID.MIRROR_ID);

//		setLineInputGlobalShortcut(lineInputByValueButton, KeyStrokes.get(KeyEvent.VK_L),
//				StringID.BY_VALUE_ID);

		setLineInputGlobalShortcut(lineInputPBisectorButton, KeyStrokes.get(KeyEvent.VK_P),
				StringID.PERPENDICULAR_BISECTOR_ID);

		setLineInputGlobalShortcut(lineInputAngleSnapButton, KeyStrokes.get(KeyEvent.VK_A),
				StringID.ANGLE_SNAP_ID);

		setLineInputGlobalShortcut(lineInputSuggestionButton, KeyStrokes.get(KeyEvent.VK_F),
				StringID.SUGGESTION_ID);

		setShortcut(lineTypeMountainButton, KeyStrokes.getWithShiftDown(KeyEvent.VK_M),
				StringID.UI.MOUNTAIN_ID);

		setShortcut(lineTypeValleyButton, KeyStrokes.getWithShiftDown(KeyEvent.VK_V),
				StringID.UI.VALLEY_ID);

		setShortcut(lineTypeAuxButton, KeyStrokes.getWithShiftDown(KeyEvent.VK_A),
				StringID.UI.AUX_ID);

		setShortcut(lineTypeUnassignedButton, KeyStrokes.getWithShiftDown(KeyEvent.VK_U),
				StringID.UI.UNASSIGNED_ID);

	}

	/**
	 * Assigns given key stroke to the button as the stroke invokes the click
	 * event of the button. The shortcut works only if the button is visible.
	 *
	 * @param button
	 *            is to be assigned a shortcut.
	 * @param keyStroke
	 *            a {@code KeyStroke} instance.
	 * @param id
	 *            is an ID string to distinguish shortcut action.
	 */
	private void setShortcut(final AbstractButton button, final KeyStroke keyStroke,
			final String id) {
		setShortcut(button, keyStroke, id, new AbstractAction(button.getText()) {
			@Override
			public void actionPerformed(final ActionEvent e) {
				button.doClick();
			}
		});
		button.setToolTipText(resources.getString(ResourceKey.LABEL, StringID.UI.SHORTCUT_ID));
	}

//	private void setLineSelectionGlobalShortcut(final AbstractButton button, final KeyStroke keyStroke,
//			final String id) {
//		setToolSettingGlobalShortcut(editModeLineSelectionButton, button, keyStroke, id);
//	}

	/**
	 * Assigns given key stroke to this panel as the stroke invokes the click
	 * event of given line input button even if the button is hidden.
	 *
	 * @param button
	 *            is assumed to be a line input button.
	 * @param keyStroke
	 *            a {@code KeyStroke} instance.
	 * @param id
	 *            is an ID string to distinguish shortcut action.
	 */
	private void setLineInputGlobalShortcut(final AbstractButton button, final KeyStroke keyStroke,
			final String id) {
		setToolSettingGlobalShortcut(editModeInputLineButton, button, keyStroke, id);
	}

	/**
	 * Assigns given key stroke to this panel as the stroke invokes the click
	 * event of given tool-setting button even if the button is hidden.
	 *
	 * @param toolButton
	 *            is a radio button controlling visibility of the tool-setting
	 *            panel.
	 * @param settingButton
	 *            is assumed to be a tool-setting button.
	 * @param keyStroke
	 *            a {@code KeyStroke} instance.
	 * @param id
	 *            is an ID string to distinguish shortcut action.
	 */
	private void setToolSettingGlobalShortcut(final JRadioButton toolButton,
			final AbstractButton settingButton, final KeyStroke keyStroke,
			final String id) {
		setShortcut(this, keyStroke, id, new AbstractAction() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (!toolButton.isSelected()) {
					toolButton.doClick();
				}
				settingButton.doClick();
			}
		});
		settingButton.setToolTipText(resources.getString(ResourceKey.LABEL, StringID.UI.SHORTCUT_ID)
				+ String.join("-", keyStroke.toString().replaceFirst("pressed ", "").split(" ")));
	}

	private void setShortcut(final JComponent focusTarget, final KeyStroke keyStroke,
			final String id, final Action action) {
		var inputMap = focusTarget.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		var actionMap = focusTarget.getActionMap();

		if (inputMap.get(keyStroke) != null) {
			throw new IllegalArgumentException(
					"Wrong key configuration: Shortcut by " + keyStroke.toString() + " is already set.");
		}

		inputMap.put(keyStroke, id);
		actionMap.put(id, action);
	}

//	private void setLineSelectionButtonIcons() {
//		setButtonIcon(selectionButton, ButtonIcon.SELECT);
//		setButtonIcon(enlargementButton, ButtonIcon.ENLARGE);
//	}

	private void setLineInputButtonIcons() {
		setButtonIcon(lineInputDirectVButton, ButtonIcon.DIRECT_V);
		setButtonIcon(lineInputOnVButton, ButtonIcon.ON_V);
		setButtonIcon(lineInputPBisectorButton, ButtonIcon.PERPENDICULAR_BISECTOR);
		setButtonIcon(lineInputAngleBisectorButton, ButtonIcon.BISECTOR);
//		setButtonIcon(lineInputTriangleSplitButton, ButtonIcon.TRIANGLE);
		setButtonIcon(lineInputVerticalLineButton, ButtonIcon.VERTICAL);
		setButtonIcon(lineInputSymmetricButton, ButtonIcon.SYMMETRIC);
		setButtonIcon(lineInputMirrorButton, ButtonIcon.MIRROR);
//		setButtonIcon(lineInputByValueButton, ButtonIcon.BY_VALUE);
		setButtonIcon(lineInputAngleSnapButton, ButtonIcon.ANGLE_SNAP);
		setButtonIcon(lineInputSuggestionButton, ButtonIcon.SUGGESTION);
	}

	private void setButtonIcon(final AbstractButton button, final ButtonIconResource icon) {
		var imageLoader = new ImageResourceLoader();

		button.setIcon(imageLoader.loadAsIcon(icon.getIconResourcePath()));
		button.setSelectedIcon(imageLoader.loadAsIcon(icon.getSelectedIconResourcePath()));
	}

	@Override
	public void addItemOfAlterLineComboFrom(final String item) {
		alterLineComboFrom.addItem(item);
	}

	@Override
	public void addItemOfAlterLineComboTo(final String item) {
		alterLineComboTo.addItem(item);
	}

	@Override
	public void addItemOfAngleStepCombo(final String item) {
		angleStepCombo.addItem(item);
	}

	@Override
	public void addEditModeInputLineButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(editModeInputLineButton, listener, keyProcessing);
	}

	@Override
	public void addEditModeLineSelectionButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(editModeLineSelectionButton, listener, keyProcessing);
	}

	@Override
	public void addEditModeDeleteLineButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(editModeDeleteLineButton, listener, keyProcessing);
	}

	@Override
	public void addEditModeLineTypeButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(editModeLineTypeButton, listener, keyProcessing);
	}

	@Override
	public void addAlterLineComboFromSelectionListener(final Consumer<String> listener) {
		addSelectionListener(alterLineComboFrom, listener);
	}

	@Override
	public void addAlterLineComboToSelectionListener(final Consumer<String> listener) {
		addSelectionListener(alterLineComboTo, listener);
	}

	@SuppressWarnings("unchecked")
	private <T> void addSelectionListener(final JComboBox<T> combo, final Consumer<T> listener) {
		combo.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				listener.accept((T) e.getItem());
			}
		});
	}

	@Override
	public void addEditModeAddVertexButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(editModeAddVertex, listener, keyProcessing);
	}

	@Override
	public void addEditModeDeleteVertexButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(editModeDeleteVertex, listener, keyProcessing);
	}

	@Override
	public void addSelectionButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(selectionButton, listener, keyProcessing);
	}

	@Override
	public void addEnlargementButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(enlargementButton, listener, keyProcessing);
	}

	@Override
	public void addLineInputDirectVButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(lineInputDirectVButton, listener, keyProcessing);
	}

	@Override
	public void addLineInputOnVButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(lineInputOnVButton, listener, keyProcessing);
	}

	@Override
	public void addLineInputVerticalLineButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(lineInputVerticalLineButton, listener, keyProcessing);
	}

	@Override
	public void addLineInputAngleBisectorButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(lineInputAngleBisectorButton, listener, keyProcessing);
	}

	@Override
	public void addLineInputTriangleSplitButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(lineInputTriangleSplitButton, listener, keyProcessing);
	}

	@Override
	public void addLineInputSymmetricButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(lineInputSymmetricButton, listener, keyProcessing);
	}

	@Override
	public void addLineInputMirrorButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(lineInputMirrorButton, listener, keyProcessing);
	}

	@Override
	public void addLineInputByValueButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(lineInputByValueButton, listener, keyProcessing);
	}

	@Override
	public void addLengthButtonListener(final Runnable listener) {
		buttonLength.addActionListener(e -> listener.run());
	}

	@Override
	public void addAngleButtonListener(final Runnable listener) {
		buttonAngle.addActionListener(e -> listener.run());
	}

	@Override
	public void addLengthTextFieldListener(final Consumer<Double> listener) {
//		addMeasureDocumentListener(textFieldLength, listener);
	}

	@Override
	public void addAngleTextFieldListener(final Consumer<Double> listener) {
//		addMeasureDocumentListener(textFieldAngle, listener);
	}

//	private void addMeasureDocumentListener(final JFormattedTextField field, final Consumer<Double> listener) {
//		field.getDocument().addDocumentListener(new DocumentListener() {
//			@Override
//			public void insertUpdate(final DocumentEvent e) {
//				setValue(e);
//
//			}
//
//			@Override
//			public void removeUpdate(final DocumentEvent e) {
//				setValue(e);
//
//			}
//
//			@Override
//			public void changedUpdate(final DocumentEvent e) {
//				setValue(e);
//			}
//
//			private void setValue(final DocumentEvent e) {
//				Document document = e.getDocument();
//				try {
//					String text = document.getText(0, document.getLength());
//					double value = Double.valueOf(text);
//
//					listener.accept(value);
//				} catch (Exception ex) {
//
//				}
//			}
//		});
//	}

	@Override
	public void addLineInputPBisectorButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(lineInputPBisectorButton, listener, keyProcessing);
	}

	@Override
	public void addLineInputAngleSnapButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(lineInputAngleSnapButton, listener, keyProcessing);
	}

	@Override
	public void addLineInputSuggestionButtonListener(final Runnable listener, final KeyProcessing keyProcessing) {
		addButtonListener(lineInputSuggestionButton, listener, keyProcessing);
	}

	@Override
	public void addAngleStepComboListener(final Consumer<String> listener) {
		angleStepCombo.addItemListener(e -> listener.accept((String) e.getItem()));
	}

	private void addButtonListener(final AbstractButton button, final Runnable listener,
			final KeyProcessing keyProcessing) {
		button.addActionListener(e -> listener.run());
		button.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(final KeyEvent e) {

			}

			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.isControlDown()) {
					keyProcessing.controlKeyPressed();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					keyProcessing.escapeKeyPressed();
				}

			}

			@Override
			public void keyReleased(final KeyEvent e) {
				keyProcessing.keyReleased();
			}
		});
	}

	@Override
	public void addLineTypeMountainButtonListener(final Runnable listener) {
		lineTypeMountainButton.addActionListener(e -> listener.run());
	}

	@Override
	public void addLineTypeValleyButtonListener(final Runnable listener) {
		lineTypeValleyButton.addActionListener(e -> listener.run());
	}

	@Override
	public void addLineTypeUnassignedButtonListener(final Runnable listener) {
		lineTypeUnassignedButton.addActionListener(e -> listener.run());
	}

	@Override
	public void addLineTypeAuxButtonListener(final Runnable listener) {
		lineTypeAuxButton.addActionListener(e -> listener.run());
	}

	@Override
	public void addDispGridCheckBoxListener(final Consumer<Boolean> listener) {
		dispGridCheckBox.addActionListener(e -> listener.accept(dispGridCheckBox.isSelected()));
	}

	@Override
	public void addGridSmallButtonListener(final Runnable listener) {
		gridSmallButton.addActionListener(e -> listener.run());
	}

	@Override
	public void addGridLargeButtonListener(final Runnable listener) {
		gridLargeButton.addActionListener(e -> listener.run());
	}

	@Override
	public void addGridChangeButtonListener(final Consumer<Integer> listener) {
		gridChangeButton.addActionListener(e -> listener.accept(parseGridDivNumText()));
		textFieldGrid.addActionListener(e -> listener.accept(parseGridDivNumText()));
	}

	private int parseGridDivNumText() {
		try {
			return Integer.parseInt(textFieldGrid.getText());
		} catch (Exception e) {
			logger.debug("wrong text for grid div. num.", e);
			textFieldGrid.setText(Integer.toString(Constants.DEFAULT_GRID_DIV_NUM));
			return Constants.DEFAULT_GRID_DIV_NUM;
		}
	}

	@Override
	public void addDispVertexCheckBoxListener(final Consumer<Boolean> listener) {
		dispVertexCheckBox.addActionListener(e -> listener.accept(dispVertexCheckBox.isSelected()));
	}

	@Override
	public void addDispMVLinesCheckBoxListener(final Consumer<Boolean> listener) {
		dispMVULinesCheckBox.addActionListener(e -> listener.accept(dispMVULinesCheckBox.isSelected()));
	}

	@Override
	public void addDispAuxLinesCheckBoxListener(final Consumer<Boolean> listener) {
		dispAuxLinesCheckBox.addActionListener(e -> listener.accept(dispAuxLinesCheckBox.isSelected()));
	}

	@Override
	public void addZeroLineWidthCheckBoxListener(final Consumer<Boolean> listener) {
		zeroLineWidthCheckBox.addActionListener(e -> listener.accept(zeroLineWidthCheckBox.isSelected()));
	}

	@Override
	public void setGridDivNum(final int gridDivNum) {
		textFieldGrid.setValue(gridDivNum);
	}

	public void setPaperDomainOfModelChangeListener(final PropertyChangeListener listener) {
		paperDomainOfModelChangeListener = listener;
	}

	@Override
	public void setEstimationResultColors(final Color frontColor, final Color backColor) {
		estimationResultFrontColor = frontColor;
		estimationResultBackColor = backColor;
	}

	public void setEstimationResultSaveColorsListener(final BiConsumer<Color, Color> listener) {
		estimationResultSaveColorsListener = (front, back) -> {
			setEstimationResultColors(front, back);
			listener.accept(front, back);
		};
	}

	@Override
	public void setValuePanelFractionDigits(final int lengthDigitCount, final int angleDigitCount) {
//		setFractionDigits(textFieldLength, lengthDigitCount);
//		setFractionDigits(textFieldAngle, angleDigitCount);
	}

//	private void setFractionDigits(final JFormattedTextField field, final int digitCount) {
//		var valueFormat = NumberFormat
//				.getNumberInstance(Locale.US);
//		valueFormat.setMinimumFractionDigits(digitCount);
//
//		field.setFormatterFactory(
//				new DefaultFormatterFactory(new NumberFormatter(valueFormat)));
//	}

	@Override
	public void addCheckWindowButtonListener(final Runnable listener) {
		checkWindowButton.addActionListener(e -> listener.run());
	}

	private void addPropertyChangeListenersToSetting(final PainterScreenSetting mainScreenSetting) {
		mainScreenSetting.addPropertyChangeListener(
				PainterScreenSetting.ZERO_LINE_WIDTH,
				e -> zeroLineWidthCheckBox.setSelected((boolean) e.getNewValue()));

		mainScreenSetting.addPropertyChangeListener(
				PainterScreenSetting.GRID_VISIBLE, e -> {
					dispGridCheckBox.setSelected((boolean) e.getNewValue());
					repaint();
				});

		mainScreenSetting.addPropertyChangeListener(
				PainterScreenSetting.VERTEX_VISIBLE, e -> {
					logger.debug("vertexVisible property change: {}", e.getNewValue());
					dispVertexCheckBox.setSelected((boolean) e.getNewValue());
				});

		mainScreenSetting.addPropertyChangeListener(
				PainterScreenSetting.MV_LINE_VISIBLE, e -> {
					logger.debug("mvLineVisible property change: {}", e.getNewValue());
					dispMVULinesCheckBox.setSelected((boolean) e.getNewValue());
				});

		mainScreenSetting.addPropertyChangeListener(
				PainterScreenSetting.AUX_LINE_VISIBLE, e -> {
					logger.debug("auxLineVisible property change: {}", e.getNewValue());
					dispAuxLinesCheckBox.setSelected((boolean) e.getNewValue());
				});

		setting.addPropertyChangeListener(
				UIPanelSetting.SELECTED_MODE, this::onChangeEditModeButtonSelection);

		setting.addPropertyChangeListener(
				UIPanelSetting.BY_VALUE_PANEL_VISIBLE,
				e -> byValuePanel.setVisible((boolean) e.getNewValue()));

		setting.addPropertyChangeListener(
				UIPanelSetting.ALTER_LINE_TYPE_PANEL_VISIBLE,
				e -> alterLineTypePanel.setVisible((boolean) e.getNewValue()));

		setting.addPropertyChangeListener(UIPanelSetting.LINE_INPUT_PANEL_VISIBLE,
				e -> lineInputPanel.setVisible((boolean) e.getNewValue()));

		setting.addPropertyChangeListener(UIPanelSetting.LINE_SELECTION_PANEL_VISIBLE,
				e -> lineSelectionPanel.setVisible((boolean) e.getNewValue()));

		setting.addPropertyChangeListener(
				UIPanelSetting.ANGLE_STEP_PANEL_VISIBLE,
				e -> angleStepComboPanel.setVisible((boolean) e.getNewValue()));
	}

	private void onChangeEditModeButtonSelection(final PropertyChangeEvent e) {
		switch (EditMode.fromString(setting.getSelectedModeString()).get()) {
		case INPUT:
			selectEditModeButton(editModeInputLineButton);
			break;
		case SELECT:
			selectEditModeButton(editModeLineSelectionButton);
			break;
		default:
			break;
		}
	}

	private void selectEditModeButton(final AbstractButton modeButton) {
		editModeGroup.setSelected(modeButton.getModel(), true);
	}

	@Override
	public View getTopLevelView() {
		return (View) SwingUtilities.getWindowAncestor(this);
	}

	@Override
	public Color getEstimationResultFrontColor() {
		return estimationResultFrontColor;
	}

	@Override
	public Color getEstimationResultBackColor() {
		return estimationResultBackColor;
	}

	@Override
	public BiConsumer<Color, Color> getEstimationResultSaveColorsListener() {
		return estimationResultSaveColorsListener;
	}

	@Override
	public boolean isFullEstimation() {
		return doFullEstimationCheckBox.isSelected();
	}

	@Override
	public PropertyChangeListener getPaperDomainOfModelChangeListener() {
		return paperDomainOfModelChangeListener;
	}

	@Override
	public void setBuildButtonEnabled(final boolean enabled) {
		buildButton.setEnabled(enabled);
	}

	@Override
	public void setByValueAngle(final double angle) {
		textFieldAngle.setValue(angle);
	}

	@Override
	public void setByValueLength(final double length) {
		textFieldLength.setValue(length);
	}

	@Override
	public void showNoAnswerMessage() {
		dialogService.showNoAnswerMessage(this);
	}

	/**
	 * Do not call this method from Event Dispatch Thread.
	 */
	@Override
	public boolean showCleaningUpDuplicationDialog() {
		RunnableFuture<Boolean> f = new FutureTask<>(
				() -> dialogService.showCleaningUpDuplicationDialog(this) == JOptionPane.YES_OPTION);
		SwingUtilities.invokeLater(f);
		try {
			return f.get();
		} catch (InterruptedException | ExecutionException e) {
			return false;
		}
	}

	/**
	 * Do not call this method from Event Dispatch Thread.
	 */
	@Override
	public void showCleaningUpMessage() {
		try {
			SwingUtilities.invokeAndWait(() -> dialogService.showCleaningUpMessage(this));
		} catch (InvocationTargetException | InterruptedException e) {
		}
	}

	/**
	 * Do not call this method from Event Dispatch Thread.
	 */
	@Override
	public void showFoldFailureMessage() {
		try {
			SwingUtilities.invokeAndWait(() -> dialogService.showFoldFailureMessage(this));
		} catch (InvocationTargetException | InterruptedException e) {
		}
	}

	@Override
	public void setModelComputationListener(final Runnable listener) {
		modelComputationListener = listener;
	}

	@Override
	public void setShowFoldedModelWindowsListener(final Runnable listener) {
		showFoldedModelWindowsListener = listener;
	}

	/**
	 * open window with folded model
	 */
	private void showFoldedModelWindows() {

		var frame = (JFrame) SwingUtilities.getWindowAncestor(this);

		// modal dialog while folding
		var dialogWhileFolding = new DialogWhileFolding(frame, resources);

		var worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					modelComputationListener.run();
				} catch (Exception e) {
					logger.error("error when folding", e);
					showErrorMessage(e);
				}
				return null;
			}
		};

//		dialogWhileFolding.setWorker(worker);

		worker.addPropertyChangeListener(e -> {
			if ("state".equals(e.getPropertyName())
					&& SwingWorker.StateValue.DONE == e.getNewValue()) {
				dialogWhileFolding.setVisible(false);
				dialogWhileFolding.dispose();
			}
		});

		worker.execute();

		setBuildButtonEnabled(false);
		dialogWhileFolding.setVisible(true);

		try {
			worker.get();

			// this action moves the main window to front.
			setBuildButtonEnabled(true);

			showFoldedModelWindowsListener.run();

		} catch (Exception e) {
			logger.info("folding failed or cancelled.", e);
			showErrorMessage(e);
		}
	}

	@Override
	public void showLocalFlatFoldabilityViolationMessage() {
		dialogService.showLocalFlatFoldabilityViolationDialog(this);
	}

	@Override
	public void showErrorMessage(final Exception e) {
		Dialogs.showErrorDialog(this,
				resources.getString(ResourceKey.ERROR, StringID.Error.DEFAULT_TITLE_ID), e);
	}
}
