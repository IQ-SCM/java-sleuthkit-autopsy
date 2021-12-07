/*
 * Autopsy Forensic Browser
 *
 * Copyright 2021 Basis Technology Corp.
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
package org.sleuthkit.autopsy.mainui.nodes;

import java.util.Optional;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.datamodel.AnalysisResultItem;
import org.sleuthkit.autopsy.datamodel.FileTypeExtensions;
import org.sleuthkit.autopsy.datamodel.utils.IconsUtil;
import org.sleuthkit.autopsy.mainui.datamodel.AnalysisResultRowDTO;
import org.sleuthkit.autopsy.mainui.datamodel.AnalysisResultTableSearchResultsDTO;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.AnalysisResult;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * Node to display AnalysResult.
 */
public class AnalysisResultNode extends ArtifactNode<AnalysisResult, AnalysisResultRowDTO> {

    private static final Logger logger = Logger.getLogger(AnalysisResultNode.class.getName());

    /**
     * Construct a new node for the given table and row DTO objects.
     *
     * @param tableData The table search result DTO.
     * @param resultRow The row DTO.
     */
    AnalysisResultNode(AnalysisResultTableSearchResultsDTO tableData, AnalysisResultRowDTO resultRow) {
        this(tableData, resultRow, IconsUtil.getIconFilePath(tableData.getArtifactType().getTypeID()));
    }

    /**
     * Construct a new node for the given table and row DTO objects.
     *
     * @param tableData The table search result DTO.
     * @param resultRow The row DTO.
     * @param iconPath  The path for the node icon.
     */
    AnalysisResultNode(AnalysisResultTableSearchResultsDTO tableData, AnalysisResultRowDTO resultRow, String iconPath) {
        super(resultRow, tableData.getColumns(), tableData.getArtifactType(), createLookup(resultRow), iconPath);
    }

    /**
     * Create the lookup for the AnalysisResultNode.
     *
     * @param row The RowDTO data.
     *
     * @return The lookup for the node.
     */
    private static Lookup createLookup(AnalysisResultRowDTO row) {
        AnalysisResultItem resultItem = new AnalysisResultItem(row.getAnalysisResult(), row.getSrcContent());
        if (row.getSrcContent() == null) {
            return Lookups.fixed(row.getAnalysisResult(), resultItem);
        }

        return Lookups.fixed(row.getAnalysisResult(), resultItem, row.getSrcContent());
    }

    @Override
    public boolean supportsContentTagAction() {
        return getSourceContent().isPresent() && getSourceContent().get() instanceof AbstractFile;
    }

    @Override
    public Optional<AbstractFile> getExtractArchiveWithPasswordActionFile() {
        Optional<Content> optionalSourceContent = getSourceContent();
        // TODO: See JIRA-8099
        boolean encryptionDetected = false;
        if(optionalSourceContent.isPresent()) {
            if (optionalSourceContent.get() instanceof AbstractFile) {
                AbstractFile file = (AbstractFile) optionalSourceContent.get();
                boolean isArchive = FileTypeExtensions.getArchiveExtensions().contains("." + file.getNameExtension().toLowerCase());
                try {
                    encryptionDetected = isArchive && file.getArtifacts(BlackboardArtifact.ARTIFACT_TYPE.TSK_ENCRYPTION_DETECTED).size() > 0;
                } catch (TskCoreException ex) {
                    // TODO
                }
                if(encryptionDetected) {
                    return Optional.of(file);
                }
            }
        }
        return Optional.empty();
    }
}