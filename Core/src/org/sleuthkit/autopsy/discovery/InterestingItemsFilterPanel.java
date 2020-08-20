/*
 * Autopsy
 *
 * Copyright 2020 Basis Technology Corp.
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
package org.sleuthkit.autopsy.discovery;

import java.util.List;
import java.util.logging.Level;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * Class to allow configuration of the Interesting Items filter.
 */
final class InterestingItemsFilterPanel extends AbstractDiscoveryFilterPanel {

    private static final long serialVersionUID = 1L;
    private final static Logger logger = Logger.getLogger(InterestingItemsFilterPanel.class.getName());

    /**
     * Creates new form InterestingItemsFilterPanel.
     */
    InterestingItemsFilterPanel() {
        initComponents();
        setUpInterestingItemsFilter();
    }

    /**
     * Initialize the interesting items filter.
     */
    private void setUpInterestingItemsFilter() {
        int count = 0;
        try {
            DefaultListModel<String> intListModel = (DefaultListModel<String>) interestingItemsList.getModel();
            intListModel.removeAllElements();
            List<String> setNames = DiscoveryUiUtils.getSetNames(BlackboardArtifact.ARTIFACT_TYPE.TSK_INTERESTING_FILE_HIT,
                    BlackboardAttribute.ATTRIBUTE_TYPE.TSK_SET_NAME);
            for (String name : setNames) {
                intListModel.add(count, name);
                count++;
            }
        } catch (TskCoreException ex) {
            logger.log(Level.SEVERE, "Error loading interesting file set names", ex);
            interestingItemsCheckbox.setEnabled(false);
            interestingItemsList.setEnabled(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        interestingItemsCheckbox = new javax.swing.JCheckBox();
        interestingItemsScrollPane = new javax.swing.JScrollPane();
        interestingItemsList = new javax.swing.JList<>();

        org.openide.awt.Mnemonics.setLocalizedText(interestingItemsCheckbox, org.openide.util.NbBundle.getMessage(InterestingItemsFilterPanel.class, "InterestingItemsFilterPanel.interestingItemsCheckbox.text")); // NOI18N
        interestingItemsCheckbox.setMaximumSize(new java.awt.Dimension(150, 25));
        interestingItemsCheckbox.setMinimumSize(new java.awt.Dimension(150, 25));
        interestingItemsCheckbox.setPreferredSize(new java.awt.Dimension(150, 25));
        interestingItemsCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interestingItemsCheckboxActionPerformed(evt);
            }
        });

        setMinimumSize(new java.awt.Dimension(250, 30));
        setPreferredSize(new java.awt.Dimension(250, 30));

        interestingItemsScrollPane.setPreferredSize(new java.awt.Dimension(27, 27));

        interestingItemsList.setModel(new DefaultListModel<String>());
        interestingItemsList.setEnabled(false);
        interestingItemsList.setVisibleRowCount(2);
        interestingItemsScrollPane.setViewportView(interestingItemsList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(interestingItemsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(interestingItemsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void interestingItemsCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interestingItemsCheckboxActionPerformed
        interestingItemsList.setEnabled(interestingItemsCheckbox.isSelected());
    }//GEN-LAST:event_interestingItemsCheckboxActionPerformed

    @Override
    void configurePanel(boolean selected, int[] indicesSelected) {
        boolean hasInterestingItems = interestingItemsList.getModel().getSize() > 0;
        interestingItemsCheckbox.setEnabled(hasInterestingItems);
        interestingItemsCheckbox.setSelected(selected && hasInterestingItems);
        if (interestingItemsCheckbox.isEnabled() && interestingItemsCheckbox.isSelected()) {
            interestingItemsScrollPane.setEnabled(true);
            interestingItemsList.setEnabled(true);
            if (indicesSelected != null) {
                interestingItemsList.setSelectedIndices(indicesSelected);
            }
        } else {
            interestingItemsScrollPane.setEnabled(false);
            interestingItemsList.setEnabled(false);
        }
    }

    @Override
    JCheckBox getCheckbox() {
        return interestingItemsCheckbox;
    }

    @Override
    JLabel getAdditionalLabel() {
        return null;
    }

    @Override
    String checkForError() {
        if (interestingItemsCheckbox.isSelected() && interestingItemsList.getSelectedValuesList().isEmpty()) {
            return "At least one interesting file set name must be selected";
        }
        return "";
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox interestingItemsCheckbox;
    private javax.swing.JList<String> interestingItemsList;
    private javax.swing.JScrollPane interestingItemsScrollPane;
    // End of variables declaration//GEN-END:variables

    @Override
    JList<?> getList() {
        return interestingItemsList;
    }

    @Override
    AbstractFilter getFilter() {
        if (interestingItemsCheckbox.isSelected()) {
            return new SearchFiltering.InterestingFileSetFilter(interestingItemsList.getSelectedValuesList());
        }
        return null;
    }
}
