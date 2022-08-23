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
import java.util.concurrent.ExecutorService;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;
import org.sleuthkit.autopsy.mainui.datamodel.ReportsRowDTO;
import org.sleuthkit.autopsy.mainui.datamodel.SearchResultsDTO;
import org.sleuthkit.datamodel.Report;

/**
 * A node representing a single row when viewing credit cards by file.
 */
public class ReportNode extends BaseNode<SearchResultsDTO, ReportsRowDTO> {
    public ReportNode(SearchResultsDTO results, ReportsRowDTO rowData, ExecutorService backgroundTasksPool) {
            super(Children.LEAF, 
                Lookups.fixed(rowData.getReport()),
                results, 
                rowData,
                backgroundTasksPool);
            
            setName(rowData.getReportName() + rowData.getId());
            setDisplayName(rowData.getSourceModuleName());
            setIconBaseWithExtension("org/sleuthkit/autopsy/images/report_16.png");
    }
    
    @Override
    public Optional<Report> getReport() {
        return Optional.ofNullable(this.getRowDTO().getReport());
    }
}