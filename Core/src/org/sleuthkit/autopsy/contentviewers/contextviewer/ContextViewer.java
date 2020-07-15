/*
 * Autopsy Forensic Browser
 *
 * Copyright 2019 Basis Technology Corp.
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
package org.sleuthkit.autopsy.contentviewers.contextviewer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.BoxLayout;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import org.apache.commons.lang.StringUtils;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.casemodule.NoCurrentCaseException;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataContentViewer;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.BlackboardArtifact;
import static org.sleuthkit.datamodel.BlackboardArtifact.ARTIFACT_TYPE.TSK_ASSOCIATED_OBJECT;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.SleuthkitCase;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * Displays additional context for the selected file, such as its source, and
 * usage, if known.
 *
 */
@ServiceProvider(service = DataContentViewer.class, position = 7)
public final class ContextViewer extends javax.swing.JPanel implements DataContentViewer {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ContextViewer.class.getName());
    private static final int ARTIFACT_STR_MAX_LEN = 1024;
    private static final int ATTRIBUTE_STR_MAX_LEN = 200;

    // defines a list of artifacts that provide context for a file
    private static final List<BlackboardArtifact.ARTIFACT_TYPE> SOURCE_CONTEXT_ARTIFACTS = new ArrayList<>();
    private final List<javax.swing.JPanel> contextSourcePanels = new ArrayList<>();
    private final List<javax.swing.JPanel> contextUsagePanels = new ArrayList<>();

    static {
        SOURCE_CONTEXT_ARTIFACTS.add(TSK_ASSOCIATED_OBJECT);
    }

    /**
     * Creates new form ContextViewer
     */
    public ContextViewer() {

        initComponents();
        jScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSourcePanel = new javax.swing.JPanel();
        javax.swing.JLabel jSourceLabel = new javax.swing.JLabel();
        jUsagePanel = new javax.swing.JPanel();
        javax.swing.JLabel jUsageLabel = new javax.swing.JLabel();
        jUnknownPanel = new javax.swing.JPanel();
        javax.swing.JLabel jUnknownLabel = new javax.swing.JLabel();
        jScrollPane = new javax.swing.JScrollPane();

        jSourceLabel.setFont(jSourceLabel.getFont().deriveFont(jSourceLabel.getFont().getStyle() | java.awt.Font.BOLD, jSourceLabel.getFont().getSize()+1));
        org.openide.awt.Mnemonics.setLocalizedText(jSourceLabel, org.openide.util.NbBundle.getMessage(ContextViewer.class, "ContextViewer.jSourceLabel.text")); // NOI18N

        javax.swing.GroupLayout jSourcePanelLayout = new javax.swing.GroupLayout(jSourcePanel);
        jSourcePanel.setLayout(jSourcePanelLayout);
        jSourcePanelLayout.setHorizontalGroup(
            jSourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSourcePanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jSourceLabel)
                .addContainerGap(304, Short.MAX_VALUE))
        );
        jSourcePanelLayout.setVerticalGroup(
            jSourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSourcePanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jSourceLabel)
                .addGap(2, 2, 2))
        );

        jUsageLabel.setFont(jUsageLabel.getFont().deriveFont(jUsageLabel.getFont().getStyle() | java.awt.Font.BOLD, jUsageLabel.getFont().getSize()+1));
        org.openide.awt.Mnemonics.setLocalizedText(jUsageLabel, org.openide.util.NbBundle.getMessage(ContextViewer.class, "ContextViewer.jUsageLabel.text")); // NOI18N

        javax.swing.GroupLayout jUsagePanelLayout = new javax.swing.GroupLayout(jUsagePanel);
        jUsagePanel.setLayout(jUsagePanelLayout);
        jUsagePanelLayout.setHorizontalGroup(
            jUsagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jUsagePanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jUsageLabel)
                .addContainerGap(298, Short.MAX_VALUE))
        );
        jUsagePanelLayout.setVerticalGroup(
            jUsagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jUsagePanelLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jUsageLabel)
                .addGap(2, 2, 2))
        );

        org.openide.awt.Mnemonics.setLocalizedText(jUnknownLabel, org.openide.util.NbBundle.getMessage(ContextViewer.class, "ContextViewer.jUnknownLabel.text")); // NOI18N

        javax.swing.GroupLayout jUnknownPanelLayout = new javax.swing.GroupLayout(jUnknownPanel);
        jUnknownPanel.setLayout(jUnknownPanelLayout);
        jUnknownPanelLayout.setHorizontalGroup(
            jUnknownPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jUnknownPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jUnknownLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jUnknownPanelLayout.setVerticalGroup(
            jUnknownPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jUnknownPanelLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jUnknownLabel)
                .addGap(2, 2, 2))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(495, 358));

        jScrollPane.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void setNode(Node selectedNode) {
        if ((selectedNode == null) || (!isSupported(selectedNode))) {
            resetComponent();
            return;
        }

        AbstractFile file = selectedNode.getLookup().lookup(AbstractFile.class);
        try {
            populateSourceContextData(file);
        } catch (NoCurrentCaseException | TskCoreException ex) {
            logger.log(Level.SEVERE, String.format("Exception displaying context for file %s", file.getName()), ex); //NON-NLS
        }
    }

    @NbBundle.Messages({
        "ContextViewer.title=Context",
        "ContextViewer.toolTip=Displays context for selected file."
    })

    @Override
    public String getTitle() {
        return Bundle.ContextViewer_title();
    }

    @Override
    public String getToolTip() {
        return Bundle.ContextViewer_toolTip();
    }

    @Override
    public DataContentViewer createInstance() {
        return new ContextViewer();
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void resetComponent() {
        contextSourcePanels.clear();
        contextUsagePanels.clear();
    }

    @Override
    public boolean isSupported(Node node) {

        // check if the node has an abstract file and the file has any context defining artifacts.
        if (node.getLookup().lookup(AbstractFile.class) != null) {
            AbstractFile abstractFile = node.getLookup().lookup(AbstractFile.class);
            for (BlackboardArtifact.ARTIFACT_TYPE artifactType : SOURCE_CONTEXT_ARTIFACTS) {
                List<BlackboardArtifact> artifactsList;
                try {
                    artifactsList = abstractFile.getArtifacts(artifactType);
                    if (!artifactsList.isEmpty()) {
                        return true;
                    }
                } catch (TskCoreException ex) {
                    logger.log(Level.SEVERE, String.format("Exception while looking up context artifacts for file %s", abstractFile), ex); //NON-NLS
                }
            }

        }

        return false;
    }

    @Override
    public int isPreferred(Node node) {
        // this is a low preference viewer.
        return 1;
    }

    @NbBundle.Messages({
        "ContextViewer.unknownSource=Unknown ",
    })
    /**
     * Looks for context providing artifacts for the given file and populates
     * the source context.
     *
     * @param sourceFile File for which to show the context.
     *
     * @throws NoCurrentCaseException
     * @throws TskCoreException
     */
    private void populateSourceContextData(AbstractFile sourceFile) throws NoCurrentCaseException, TskCoreException {

        SleuthkitCase tskCase = Case.getCurrentCaseThrows().getSleuthkitCase();
        
        // Check for all context artifacts
        boolean foundASource = false;
        for (BlackboardArtifact.ARTIFACT_TYPE artifactType : SOURCE_CONTEXT_ARTIFACTS) {
            List<BlackboardArtifact> artifactsList = tskCase.getBlackboardArtifacts(artifactType, sourceFile.getId());

            foundASource = !artifactsList.isEmpty();
            for (BlackboardArtifact contextArtifact : artifactsList) {
                addSourceEntry(contextArtifact);
            }
        }
        javax.swing.JPanel contextContainer = new javax.swing.JPanel();
        contextContainer.add(jSourcePanel);
        contextContainer.setLayout(new BoxLayout(contextContainer, BoxLayout.Y_AXIS));
        if (contextSourcePanels.isEmpty()) {
            contextContainer.add(jUnknownPanel);
        } else {
            for (javax.swing.JPanel sourcePanel : contextSourcePanels) {
                contextContainer.add(sourcePanel);
            }
        }
        contextContainer.add(jUsagePanel);
        if (contextUsagePanels.isEmpty()) {
            contextContainer.add(jUnknownPanel);
        } else {
            for (javax.swing.JPanel usagePanel : contextUsagePanels) {
                contextContainer.add(usagePanel);
            }
        }
        contextContainer.setEnabled(foundASource);
        contextContainer.setVisible(foundASource);
        jScrollPane.getViewport().setView(contextContainer);
        jScrollPane.setEnabled(foundASource);
        jScrollPane.setVisible(foundASource);
        jScrollPane.repaint();
        jScrollPane.revalidate();
        
        
    }

    /**
     * Adds a source context entry for the selected file based on the given
     * context providing artifact.
     *
     * @param artifact Artifact that may provide context.
     *
     * @throws NoCurrentCaseException
     * @throws TskCoreException
     */
    private void addSourceEntry(BlackboardArtifact artifact) throws TskCoreException {

        if (BlackboardArtifact.ARTIFACT_TYPE.TSK_ASSOCIATED_OBJECT.getTypeID() == artifact.getArtifactTypeID()) {
            BlackboardAttribute associatedArtifactAttribute = artifact.getAttribute(new BlackboardAttribute.Type(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_ASSOCIATED_ARTIFACT));
            if (associatedArtifactAttribute != null) {
                long artifactId = associatedArtifactAttribute.getValueLong();
                BlackboardArtifact associatedArtifact = artifact.getSleuthkitCase().getBlackboardArtifact(artifactId);

                setSourceFields(associatedArtifact);
            }
        }
    }

    /**
     * Sets the source label and text fields based on the given associated
     * artifact.
     *
     * @param associatedArtifact - associated artifact
     *
     * @throws TskCoreException
     */
    @NbBundle.Messages({
        "ContextViewer.attachmentSource=Attached to: ",
        "ContextViewer.downloadSource=Downloaded from: ",
        "ContextViewer.recentDocs=Recent Documents: ",
        "ContextViewer.programExecution=Program Execution: "
    })
    private void setSourceFields(BlackboardArtifact associatedArtifact) throws TskCoreException {
        if (BlackboardArtifact.ARTIFACT_TYPE.TSK_MESSAGE.getTypeID() == associatedArtifact.getArtifactTypeID()
                || BlackboardArtifact.ARTIFACT_TYPE.TSK_EMAIL_MSG.getTypeID() == associatedArtifact.getArtifactTypeID()) {
            String sourceName = Bundle.ContextViewer_attachmentSource();
            String sourceText = msgArtifactToAbbreviatedString(associatedArtifact);
            javax.swing.JPanel sourcePanel = new ContextSourcePanel(sourceName, sourceText, associatedArtifact);
            contextSourcePanels.add(sourcePanel);

        } else if (BlackboardArtifact.ARTIFACT_TYPE.TSK_WEB_DOWNLOAD.getTypeID() == associatedArtifact.getArtifactTypeID()
                || BlackboardArtifact.ARTIFACT_TYPE.TSK_WEB_CACHE.getTypeID() == associatedArtifact.getArtifactTypeID()) {
            String sourceName = Bundle.ContextViewer_downloadSource();
            String sourceText = webDownloadArtifactToString(associatedArtifact);
            javax.swing.JPanel sourcePanel = new ContextSourcePanel(sourceName, sourceText, associatedArtifact);
            contextSourcePanels.add(sourcePanel);

        } else if (BlackboardArtifact.ARTIFACT_TYPE.TSK_RECENT_OBJECT.getTypeID() == associatedArtifact.getArtifactTypeID()) {
            String sourceName = Bundle.ContextViewer_recentDocs();
            String sourceText = recentDocArtifactToString(associatedArtifact);
            javax.swing.JPanel usagePanel = new ContextUsagePanel(sourceName, sourceText, associatedArtifact);        
            contextUsagePanels.add(usagePanel);
            
        } else if (BlackboardArtifact.ARTIFACT_TYPE.TSK_PROG_RUN.getTypeID() == associatedArtifact.getArtifactTypeID()) {
            String sourceName = Bundle.ContextViewer_programExecution();
            String sourceText = programExecArtifactToString(associatedArtifact);
            javax.swing.JPanel usagePanel = new ContextUsagePanel(sourceName, sourceText, associatedArtifact);        
            contextUsagePanels.add(usagePanel);
        }
    }

    /**
     * Returns a display string with download source URL from the given
     * artifact.
     *
     * @param artifact artifact to get download source URL from.
     *
     * @return Display string with download URL and date/time.
     *
     * @throws TskCoreException
     */
    @NbBundle.Messages({
        "ContextViewer.downloadURL=URL",
        "ContextViewer.downloadedOn=On"
    })
    private String webDownloadArtifactToString(BlackboardArtifact artifact) throws TskCoreException {
        StringBuilder sb = new StringBuilder(ARTIFACT_STR_MAX_LEN);
        Map<BlackboardAttribute.ATTRIBUTE_TYPE, BlackboardAttribute> attributesMap = getAttributesMap(artifact);

        if (BlackboardArtifact.ARTIFACT_TYPE.TSK_WEB_DOWNLOAD.getTypeID() == artifact.getArtifactTypeID()
                || BlackboardArtifact.ARTIFACT_TYPE.TSK_WEB_CACHE.getTypeID() == artifact.getArtifactTypeID()) {
            appendAttributeString(sb, BlackboardAttribute.ATTRIBUTE_TYPE.TSK_URL, attributesMap, Bundle.ContextViewer_downloadURL());
            appendAttributeString(sb, BlackboardAttribute.ATTRIBUTE_TYPE.TSK_DATETIME_CREATED, attributesMap, Bundle.ContextViewer_downloadedOn());
        }
        return sb.toString();
    }

    /**
     * Returns a display string with recent Doc
     * artifact.
     *
     * @param artifact artifact to get doc from.
     *
     * @return Display string with download URL and date/time.
     *
     * @throws TskCoreException
     */
    @NbBundle.Messages({
        "ContextViewer.on=Opened at",
        "ContextViewer.unknown=Opened at unknown time"
    })
    private String recentDocArtifactToString(BlackboardArtifact artifact) throws TskCoreException {
        StringBuilder sb = new StringBuilder(ARTIFACT_STR_MAX_LEN);
        Map<BlackboardAttribute.ATTRIBUTE_TYPE, BlackboardAttribute> attributesMap = getAttributesMap(artifact);
        
        BlackboardAttribute attribute = attributesMap.get(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_DATETIME);
        
        if (BlackboardArtifact.ARTIFACT_TYPE.TSK_RECENT_OBJECT.getTypeID() == artifact.getArtifactTypeID()) {
            if (attribute != null && attribute.getValueLong() > 0) {
                appendAttributeString(sb, BlackboardAttribute.ATTRIBUTE_TYPE.TSK_DATETIME, attributesMap, Bundle.ContextViewer_on());
            } else {
                sb.append(Bundle.ContextViewer_unknown());
            }
        }
        return sb.toString();
    }

    /**
     * Returns a display string with Program Execution
     * artifact.
     *
     * @param artifact artifact to get doc from.
     *
     * @return Display string with download URL and date/time.
     *
     * @throws TskCoreException
     */
    @NbBundle.Messages({
        "ContextViewer.runOn=Program Run On",
        "ContextViewer.runUnknown= Program Run at unknown time"
    })
    private String programExecArtifactToString(BlackboardArtifact artifact) throws TskCoreException {
        StringBuilder sb = new StringBuilder(ARTIFACT_STR_MAX_LEN);
        Map<BlackboardAttribute.ATTRIBUTE_TYPE, BlackboardAttribute> attributesMap = getAttributesMap(artifact);
        
        BlackboardAttribute attribute = attributesMap.get(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_DATETIME);
        
        if (BlackboardArtifact.ARTIFACT_TYPE.TSK_PROG_RUN.getTypeID() == artifact.getArtifactTypeID()) {
            if (attribute != null && attribute.getValueLong() > 0) {
                appendAttributeString(sb, BlackboardAttribute.ATTRIBUTE_TYPE.TSK_DATETIME, attributesMap, Bundle.ContextViewer_runOn());
            } else {
                sb.append(Bundle.ContextViewer_runUnknown());
            }
        }
        return sb.toString();
    }

    /**
     * Returns a abbreviated display string for a message artifact.
     *
     * @param artifact artifact to get download source URL from.
     *
     * @return Display string for message artifact.
     *
     * @throws TskCoreException
     */
    @NbBundle.Messages({
        "ContextViewer.message=Message",
        "ContextViewer.email=Email",
        "ContextViewer.messageFrom=From",
        "ContextViewer.messageTo=To",
        "ContextViewer.messageOn=On",})
    private String msgArtifactToAbbreviatedString(BlackboardArtifact artifact) throws TskCoreException {

        StringBuilder sb = new StringBuilder(ARTIFACT_STR_MAX_LEN);
        Map<BlackboardAttribute.ATTRIBUTE_TYPE, BlackboardAttribute> attributesMap = getAttributesMap(artifact);

        if (BlackboardArtifact.ARTIFACT_TYPE.TSK_MESSAGE.getTypeID() == artifact.getArtifactTypeID()) {
            sb.append(Bundle.ContextViewer_message()).append(' ');
            appendAttributeString(sb, BlackboardAttribute.ATTRIBUTE_TYPE.TSK_PHONE_NUMBER_FROM, attributesMap, Bundle.ContextViewer_messageFrom());
            appendAttributeString(sb, BlackboardAttribute.ATTRIBUTE_TYPE.TSK_PHONE_NUMBER_TO, attributesMap, Bundle.ContextViewer_messageTo());
            appendAttributeString(sb, BlackboardAttribute.ATTRIBUTE_TYPE.TSK_DATETIME, attributesMap, Bundle.ContextViewer_messageOn());
        } else if (BlackboardArtifact.ARTIFACT_TYPE.TSK_EMAIL_MSG.getTypeID() == artifact.getArtifactTypeID()) {
            sb.append(Bundle.ContextViewer_email()).append(' ');
            appendAttributeString(sb, BlackboardAttribute.ATTRIBUTE_TYPE.TSK_EMAIL_FROM, attributesMap, Bundle.ContextViewer_messageFrom());
            appendAttributeString(sb, BlackboardAttribute.ATTRIBUTE_TYPE.TSK_EMAIL_TO, attributesMap, Bundle.ContextViewer_messageTo());
            appendAttributeString(sb, BlackboardAttribute.ATTRIBUTE_TYPE.TSK_DATETIME_SENT, attributesMap, Bundle.ContextViewer_messageOn());
        }
        return sb.toString();
    }

    /**
     * Looks up specified attribute in the given map and, if found, appends its
     * value to the given string builder.
     *
     * @param sb            String builder to append to.
     * @param attribType    Attribute type to look for.
     * @param attributesMap Attributes map.
     * @param prependStr    Optional string that is prepended before the
     *                      attribute value.
     */
    private void appendAttributeString(StringBuilder sb, BlackboardAttribute.ATTRIBUTE_TYPE attribType,
            Map<BlackboardAttribute.ATTRIBUTE_TYPE, BlackboardAttribute> attributesMap, String prependStr) {

        BlackboardAttribute attribute = attributesMap.get(attribType);
        if (attribute != null) {
            String attrVal = attribute.getDisplayString();
            if (!StringUtils.isEmpty(attrVal)) {
                if (!StringUtils.isEmpty(prependStr)) {
                    sb.append(prependStr).append(' ');
                }
                sb.append(StringUtils.abbreviate(attrVal, ATTRIBUTE_STR_MAX_LEN)).append(' ');
            }
        }
    }

    /**
     * Gets all attributes for the given artifact, and returns a map of
     * attributes keyed by attribute type.
     *
     * @param artifact Artifact for which to get the attributes.
     *
     * @return Map of attribute type and value.
     *
     * @throws TskCoreException
     */
    private Map<BlackboardAttribute.ATTRIBUTE_TYPE, BlackboardAttribute> getAttributesMap(BlackboardArtifact artifact) throws TskCoreException {
        Map<BlackboardAttribute.ATTRIBUTE_TYPE, BlackboardAttribute> attributeMap = new HashMap<>();

        List<BlackboardAttribute> attributeList = artifact.getAttributes();
        for (BlackboardAttribute attribute : attributeList) {
            BlackboardAttribute.ATTRIBUTE_TYPE type = BlackboardAttribute.ATTRIBUTE_TYPE.fromID(attribute.getAttributeType().getTypeID());
            attributeMap.put(type, attribute);
        }

        return attributeMap;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JPanel jSourcePanel;
    private javax.swing.JPanel jUnknownPanel;
    private javax.swing.JPanel jUsagePanel;
    // End of variables declaration//GEN-END:variables
}
