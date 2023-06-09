/*
 * Autopsy Forensic Browser
 * 
 * Copyright 2011-2021 Basis Technology Corp.
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
package org.sleuthkit.autopsy.datasourceprocessors;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.NbBundle.Messages;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.casemodule.NoCurrentCaseException;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataSourceProcessor;
import org.sleuthkit.autopsy.coreutils.ModuleSettings;
import org.sleuthkit.autopsy.coreutils.PathValidator;
import org.sleuthkit.autopsy.coreutils.TimeZoneUtils;
import org.sleuthkit.autopsy.guiutils.JFileChooserFactory;

/**
 * Allows examiner to supply a raw data source.
 */
@SuppressWarnings("PMD.SingularField") // UI widgets cause lots of false positives
final class RawDSInputPanel extends JPanel implements DocumentListener {
    private static final long TWO_GB = 2000000000L;
    private static final long serialVersionUID = 1L;    //default
    private final String PROP_LASTINPUT_PATH = "LBL_LastInputFile_PATH";
    private JFileChooser fc;
    private JFileChooserFactory chooserHelper = new JFileChooserFactory();
    // Externally supplied name is used to store settings 
    private final String contextName;
    /**
     * Creates new form RawDSInputPanel
     */
    private RawDSInputPanel(String context) {
        initComponents();

        errorLabel.setVisible(false);
        this.contextName = context;
    }

    /**
     * Creates and returns an instance of a RawDSInputPanel.
     */
    static synchronized RawDSInputPanel createInstance(String context) {
        RawDSInputPanel instance = new RawDSInputPanel(context);

        instance.postInit();
        instance.createTimeZoneList();

        return instance;
    }

    //post-constructor initialization to properly initialize listener support
    //without leaking references of uninitialized objects
    private void postInit() {
        pathTextField.getDocument().addDocumentListener(this);
    }
    
    /**
     * Creates the drop down list for the time zones and then makes the local
     * machine time zone to be selected.
     */
    private void createTimeZoneList() {
        List<String> timeZoneList = TimeZoneUtils.createTimeZoneList();
        for (String timeZone : timeZoneList) {
            timeZoneComboBox.addItem(timeZone);
        }

        // set the selected timezone
        timeZoneComboBox.setSelectedItem(TimeZoneUtils.createTimeZoneString(Calendar.getInstance().getTimeZone()));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infileTypeButtonGroup = new javax.swing.ButtonGroup();
        pathLabel = new javax.swing.JLabel();
        pathTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        j2GBBreakupRadioButton = new javax.swing.JRadioButton();
        jBreakFileUpLabel = new javax.swing.JLabel();
        jNoBreakupRadioButton = new javax.swing.JRadioButton();
        errorLabel = new javax.swing.JLabel();
        timeZoneLabel = new javax.swing.JLabel();
        timeZoneComboBox = new javax.swing.JComboBox<>();

        org.openide.awt.Mnemonics.setLocalizedText(pathLabel, org.openide.util.NbBundle.getMessage(RawDSInputPanel.class, "RawDSInputPanel.pathLabel.text")); // NOI18N

        pathTextField.setText(org.openide.util.NbBundle.getMessage(RawDSInputPanel.class, "RawDSInputPanel.pathTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(RawDSInputPanel.class, "RawDSInputPanel.browseButton.text")); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        infileTypeButtonGroup.add(j2GBBreakupRadioButton);
        j2GBBreakupRadioButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(j2GBBreakupRadioButton, org.openide.util.NbBundle.getMessage(RawDSInputPanel.class, "RawDSInputPanel.j2GBBreakupRadioButton.text")); // NOI18N
        j2GBBreakupRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j2GBBreakupRadioButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jBreakFileUpLabel, org.openide.util.NbBundle.getMessage(RawDSInputPanel.class, "RawDSInputPanel.jBreakFileUpLabel.text")); // NOI18N

        infileTypeButtonGroup.add(jNoBreakupRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(jNoBreakupRadioButton, org.openide.util.NbBundle.getMessage(RawDSInputPanel.class, "RawDSInputPanel.jNoBreakupRadioButton.text")); // NOI18N
        jNoBreakupRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNoBreakupRadioButtonActionPerformed(evt);
            }
        });

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));
        org.openide.awt.Mnemonics.setLocalizedText(errorLabel, org.openide.util.NbBundle.getMessage(RawDSInputPanel.class, "RawDSInputPanel.errorLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(timeZoneLabel, org.openide.util.NbBundle.getMessage(RawDSInputPanel.class, "RawDSInputPanel.timeZoneLabel.text")); // NOI18N

        timeZoneComboBox.setMaximumRowCount(30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pathTextField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(browseButton))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pathLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(timeZoneLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeZoneComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBreakFileUpLabel)
                    .addComponent(errorLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(j2GBBreakupRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jNoBreakupRadioButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pathLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeZoneLabel)
                    .addComponent(timeZoneComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorLabel)
                .addGap(5, 5, 5)
                .addComponent(jBreakFileUpLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jNoBreakupRadioButton)
                    .addComponent(j2GBBreakupRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    @SuppressWarnings("deprecation")
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        if (fc == null) {
            fc = chooserHelper.getChooser();
            fc.setDragEnabled(false);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setMultiSelectionEnabled(false);
        }

        String oldText = pathTextField.getText();
        // set the current directory of the FileChooser if the ImagePath Field is valid
        File currentDir = new File(oldText);
        if (currentDir.exists()) {
            fc.setCurrentDirectory(currentDir);
        }

        int retval = fc.showOpenDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getPath();
            pathTextField.setText(path);
        }
    }//GEN-LAST:event_browseButtonActionPerformed

    private void j2GBBreakupRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j2GBBreakupRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_j2GBBreakupRadioButtonActionPerformed

    private void jNoBreakupRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNoBreakupRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jNoBreakupRadioButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.ButtonGroup infileTypeButtonGroup;
    private javax.swing.JRadioButton j2GBBreakupRadioButton;
    private javax.swing.JLabel jBreakFileUpLabel;
    private javax.swing.JRadioButton jNoBreakupRadioButton;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JTextField pathTextField;
    private javax.swing.JComboBox<String> timeZoneComboBox;
    private javax.swing.JLabel timeZoneLabel;
    // End of variables declaration//GEN-END:variables
    /**
     * Get the path of the user selected image.
     *
     * @return the image path
     */
    String getImageFilePath() {
        return pathTextField.getText();
    }

    void reset() {
        //reset the UI elements to default 
        pathTextField.setText(null);
        j2GBBreakupRadioButton.setSelected(true);
    }

    long getChunkSize() {
        if (jNoBreakupRadioButton.isSelected()) {
            return -1;
        } else { //if have more choices here, the selection of each radiobutton should be checked
            return TWO_GB;
        }
    }

    String getTimeZone() {
        String tz = timeZoneComboBox.getSelectedItem().toString();
        return tz.substring(tz.indexOf(")") + 2).trim();
    }

    /**
     * Should we enable the next button of the wizard?
     *
     * @return true if a proper image has been selected, false otherwise
     */
    boolean validatePanel() {
        errorLabel.setVisible(false);
        String path = getImageFilePath();
        if (path == null || path.isEmpty()) {
            return false;
        }

        // display warning if there is one (but don't disable "next" button)
        warnIfPathIsInvalid(path);

        boolean isExist = new File(path).exists();

        return (isExist);
    }

    /**
     * Validates path to selected data source and displays warning if it is
     * invalid.
     *
     * @param path Absolute path to the selected data source
     */
    @Messages({"RawDSInputPanel.error.text=Path to multi-user data source is on \"C:\" drive",
        "RawDSInputPanel.noOpenCase.errMsg=Exception while getting open case."})
    private void warnIfPathIsInvalid(String path) {
        try {
        if (!PathValidator.isValidForCaseType(path, Case.getCurrentCaseThrows().getCaseType())) {
            errorLabel.setVisible(true);
            errorLabel.setText(Bundle.RawDSInputPanel_error_text());
        }
        } catch (NoCurrentCaseException ex) {
            errorLabel.setVisible(true);
            errorLabel.setText(Bundle.RawDSInputPanel_noOpenCase_errMsg());
        }
    }

    void storeSettings() {
        String inFilePath = getImageFilePath();
        if (null != inFilePath) {
            String imagePath = inFilePath.substring(0, inFilePath.lastIndexOf(File.separator) + 1);
            ModuleSettings.setConfigSetting(contextName, PROP_LASTINPUT_PATH, imagePath);
        }
    }

    void readSettings() {
        String inFilePath = ModuleSettings.getConfigSetting(contextName, PROP_LASTINPUT_PATH);
        if (null != inFilePath) {
            if (!inFilePath.isEmpty()) {
                pathTextField.setText(inFilePath);
            }
        }
    }

    /**
     * Update functions are called by the pathTextField which has this set as
     * it's DocumentEventListener. Each update function fires a property change
     * to be caught by the parent panel.
     *
     * @param e the event, which is ignored
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        firePropertyChange(DataSourceProcessor.DSP_PANEL_EVENT.UPDATE_UI.toString(), false, true);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        firePropertyChange(DataSourceProcessor.DSP_PANEL_EVENT.UPDATE_UI.toString(), false, true);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        firePropertyChange(DataSourceProcessor.DSP_PANEL_EVENT.UPDATE_UI.toString(), false, true);
    }

    /**
     * Set the focus to the pathTextField.
     */
    void select() {
        pathTextField.requestFocusInWindow();
    }
}
