/*
 * Autopsy Forensic Browser
 *
 * Copyright 2014-2018 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.timeline;

import java.awt.Component;
import java.util.logging.Level;
import javafx.application.Platform;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import org.joda.time.Interval;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.casemodule.NoCurrentCaseException;
import org.sleuthkit.autopsy.core.Installer;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.coreutils.MessageNotifyUtil;
import org.sleuthkit.autopsy.coreutils.ModuleSettings;
import org.sleuthkit.autopsy.coreutils.ThreadConfined;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * An Action that opens the Timeline window. Has methods to open the window in
 * various specific states (e.g., showing a specific artifact in the List View)
 */
@ActionID(category = "Tools", id = "org.sleuthkit.autopsy.timeline.Timeline")
@ActionRegistration(displayName = "#CTL_MakeTimeline", lazy = false)
@ActionReferences(value = {
    @ActionReference(path = "Menu/Tools", position = 104),
    @ActionReference(path = "Toolbars/Case", position = 104)})
public final class OpenTimelineAction extends CallableSystemAction {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(OpenTimelineAction.class.getName());
    private static final int FILE_LIMIT = 6_000_000;

    private final JMenuItem menuItem;
    private final JButton toolbarButton = new JButton(getName(),
            new ImageIcon(getClass().getResource("images/btn_icon_timeline_colorized_26.png"))); //NON-NLS

    public OpenTimelineAction() {
        toolbarButton.addActionListener(actionEvent -> performAction());
        menuItem = super.getMenuPresenter();
        this.setEnabled(false);
    }

    @Override
    public boolean isEnabled() {
        /**
         * We used to also check if Case.getCurrentOpenCase().hasData() was
         * true. We disabled that check because if it is executed while a data
         * source is being added, it blocks the edt. We still do that in
         * ImageGallery.
         */
        return super.isEnabled() && Case.isCaseOpen() && Installer.isJavaFxInited();
    }

    @Override
    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    public void performAction() {
        if (tooManyFiles()) {
            Platform.runLater(PromptDialogManager::showTooManyFiles);
            setEnabled(false);
        } else if ("false".equals(ModuleSettings.getConfigSetting("timeline", "enable_timeline"))) {
            Platform.runLater(PromptDialogManager::showTimeLineDisabledMessage);
            setEnabled(false);
        } else {
            try {
                showTimeline();
            } catch (TskCoreException ex) {
                MessageNotifyUtil.Message.error(Bundle.OpenTimelineAction_settingsErrorMessage());
                logger.log(Level.SEVERE, "Error showingtimeline.", ex);
            }
        }
    }

    @NbBundle.Messages({
        "OpenTimelineAction.settingsErrorMessage=Failed to initialize timeline settings.",
        "OpenTimeLineAction.msgdlg.text=Could not create timeline, there are no data sources."})
    synchronized private void showTimeline(AbstractFile file, BlackboardArtifact artifact, Interval timeSpan) throws TskCoreException {
        try {
            Case currentCase = Case.getCurrentCaseThrows();
            if (currentCase.hasDataSource() == false) {
                MessageNotifyUtil.Message.info(Bundle.OpenTimeLineAction_msgdlg_text());
                logger.log(Level.INFO, "Could not create timeline, there are no data sources.");// NON-NLS
                return;
            }
            TimeLineController controller = TimeLineModule.getController();
            // if file or artifact not specified, specify the time range as either 
            // a) full range if timeSpan is null or b) the timeSpan
            if (file == null && artifact == null) {
                if (timeSpan == null) {
                    controller.showFullRange();
                } else {
                    controller.pushTimeRange(timeSpan);
                }
            }
            controller.showTimeLine(file, artifact);
        } catch (NoCurrentCaseException e) {
            //there is no case...   Do nothing.
        }
    }

    /**
     * Open the Timeline window with the default initial view.
     */
    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    public void showTimeline() throws TskCoreException {
        showTimeline(null, null, null);
    }

    /**
     * Open timeline with the given timeSpan time range.
     *
     * @param timeSpan The time range to display.
     * @throws TskCoreException
     */
    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    public void showTimeline(Interval timeSpan) throws TskCoreException {
        showTimeline(null, null, timeSpan);
    }

    /**
     * Open the Timeline window with the given file selected in ListView. The
     * user will be prompted to choose which timestamp to use for the file, and
     * how much time to show around it.
     *
     * @param file The AbstractFile to show in the Timeline.
     */
    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    public void showFileInTimeline(AbstractFile file) throws TskCoreException {
        showTimeline(file, null, null);
    }

    /**
     * Open the Timeline window with the given artifact selected in ListView.
     * The how much time to show around it.
     *
     * @param artifact The BlackboardArtifact to show in the Timeline.
     */
    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    public void showArtifactInTimeline(BlackboardArtifact artifact) throws TskCoreException {
        showTimeline(null, artifact, null);
    }

    @Override
    @NbBundle.Messages("OpenTimelineAction.displayName=Timeline")
    public String getName() {
        return Bundle.OpenTimelineAction_displayName();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean asynchronous() {
        return false; // run on edt
    }

    /**
     * Set this action to be enabled/disabled
     *
     * @param enable whether to enable this action or not
     */
    @Override
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        menuItem.setEnabled(enable);
        toolbarButton.setEnabled(enable);
    }

    /**
     * Returns the toolbar component of this action
     *
     * @return component the toolbar button
     */
    @Override
    public Component getToolbarPresenter() {
        return toolbarButton;
    }

    @Override
    public JMenuItem getMenuPresenter() {
        return menuItem;
    }

    private boolean tooManyFiles() {
        try {
            return FILE_LIMIT < Case.getCurrentCaseThrows().getSleuthkitCase().countFilesWhere("1 = 1");
        } catch (NoCurrentCaseException ex) {
            logger.log(Level.SEVERE, "Can not open timeline with no case open.", ex);
        } catch (TskCoreException ex) {
            logger.log(Level.SEVERE, "Error counting files in the DB.", ex);
        }
        //if there is any doubt (no case, tskcore error, etc) just disable .
        return false;
    }
}
