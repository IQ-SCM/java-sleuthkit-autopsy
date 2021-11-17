/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sleuthkit.autopsy.mainui.nodes.sco;

import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.apache.commons.lang3.tuple.Pair;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.sleuthkit.autopsy.centralrepository.datamodel.CentralRepository;
import org.sleuthkit.autopsy.centralrepository.datamodel.CorrelationAttributeInstance;
import org.sleuthkit.autopsy.centralrepository.datamodel.CorrelationAttributeUtil;
import org.sleuthkit.autopsy.core.UserPreferences;
import org.sleuthkit.autopsy.corecomponents.DataResultViewerTable;
import org.sleuthkit.autopsy.datamodel.NodeProperty;
import org.sleuthkit.autopsy.mainui.nodes.sco.SCOFetcher.SCOData;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.AnalysisResult;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.DataArtifact;
import org.sleuthkit.datamodel.OsAccount;
import org.sleuthkit.datamodel.OsAccountInstance;
import org.sleuthkit.datamodel.Score;
import org.sleuthkit.datamodel.Tag;
import org.sleuthkit.datamodel.TskCoreException;

/**
 *
 * @author kelly
 */
public class SCOFetcher<T extends Content> extends SwingWorker<SCOData, Void> {

    private final WeakReference<SCOSupporter> weakSupporterRef;
    private static final Logger logger = Logger.getLogger(SCOFetcher.class.getName());
    
    public SCOFetcher(WeakReference<SCOSupporter> weakSupporterRef) {
        this.weakSupporterRef = weakSupporterRef;
    }

    @NbBundle.Messages({"SCOFetcher_occurrences_defaultDescription=No correlation properties found",
        "SCOFetcher_occurrences_multipleProperties=Multiple different correlation properties exist for this result"})
    @Override
    protected SCOData doInBackground() throws Exception {
        SCOSupporter scoSupporter = weakSupporterRef.get();
        Content content = scoSupporter.getContent().get();
        //Check for stale reference or if columns are disabled
        if (content == null || UserPreferences.getHideSCOColumns()) {
            return null;
        }
        // get the SCO  column values
        Pair<Score, String> scoreAndDescription;
        Pair<Long, String> countAndDescription = null;
        scoreAndDescription = scoSupporter.getScorePropertyAndDescription();

        String description = Bundle.SCOFetcher_occurrences_defaultDescription();
        List<CorrelationAttributeInstance> listOfPossibleAttributes = new ArrayList<>();
        //the lists returned will be empty if the CR is not enabled
        if (content instanceof AbstractFile) {
            listOfPossibleAttributes.addAll(CorrelationAttributeUtil.makeCorrAttrsForSearch((AbstractFile) content));
        } else if (content instanceof AnalysisResult) {
            listOfPossibleAttributes.addAll(CorrelationAttributeUtil.makeCorrAttrsForSearch((AnalysisResult) content));
        } else if (content instanceof DataArtifact) {
            listOfPossibleAttributes.addAll(CorrelationAttributeUtil.makeCorrAttrsForSearch((DataArtifact) content));
        } else if (content instanceof OsAccount) {
            try {
                List<OsAccountInstance> osAccountInstances = ((OsAccount) content).getOsAccountInstances();

                /*
                 * In the most common use cases it will not matter which
                 * OsAccountInstance is selected, so choosing the first one is
                 * the most efficient solution.
                 */
                OsAccountInstance osAccountInstance = osAccountInstances.isEmpty() ? null : osAccountInstances.get(0);
                /*
                 * If we have a Case whith both data sources in the CR and data
                 * sources not in the CR, some of the OsAccountInstances for
                 * this OsAccount have not been processed into the CR. In this
                 * situation the counts may not always be accurate or
                 * consistent.
                 *
                 * In order to ensure conistency in all use cases we would need
                 * to ensure we always had an OsAccountInstance whose data
                 * source was in the CR when such an OsAccountInstance was
                 * available.
                 *
                 * The following block of code has been commented out because it
                 * reduces efficiency in what are believed to be the most common
                 * use cases. It would serve the purpose of providing
                 * consistency in edge cases where users are putting some but
                 * not all the data concerning OS Accounts, which is present in
                 * a single Case, into the CR. See TODO-JIRA-8031 for a similar
                 * issue in the OO viewer.
                 */

//                if (CentralRepository.isEnabled() && !osAccountInstances.isEmpty()) {
//                    try {
//                        CentralRepository centralRepo = CentralRepository.getInstance();
//                        //Correlation Cases are cached when we get them so this shouldn't involve a round trip for every node.
//                        CorrelationCase crCase = centralRepo.getCase(Case.getCurrentCaseThrows());
//                        for (OsAccountInstance caseOsAccountInstance : osAccountInstances) {
//                            //correlation data sources are also cached so once should not involve round trips every time.
//                            CorrelationDataSource correlationDataSource = centralRepo.getDataSource(crCase, caseOsAccountInstance.getDataSource().getId());
//                            if (correlationDataSource != null) {
//                                //we have found a data source which exists in the CR we will use it instead of the arbitrary first instance
//                                osAccountInstance = caseOsAccountInstance;
//                                break;
//                            }
//                        }
//                    } catch (CentralRepoException ex) {
//                        logger.log(Level.SEVERE, "Error checking CR for data sources which exist in it", ex);
//                    } catch (NoCurrentCaseException ex) {
//                        logger.log(Level.WARNING, "The current case was closed while attempting to find a data source in the central repository", ex);
//                    }
//                }
                listOfPossibleAttributes.addAll(CorrelationAttributeUtil.makeCorrAttrsForSearch(osAccountInstance));
            } catch (TskCoreException ex) {
                logger.log(Level.SEVERE, "Unable to get the DataSource or OsAccountInstances from an OsAccount with ID: " + content.getId(), ex);
            }
        }
        
        Optional<List<Tag>> optionalList = scoSupporter.getAllTagsFromDatabase();
        
        DataResultViewerTable.HasCommentStatus commentStatus = DataResultViewerTable.HasCommentStatus.NO_COMMENT;

        if(optionalList.isPresent()) {
            commentStatus = scoSupporter.getCommentProperty(optionalList.get(), listOfPossibleAttributes);
        }
        
        CorrelationAttributeInstance corInstance = null;
        if (CentralRepository.isEnabled()) {
            if (listOfPossibleAttributes.size() > 1) {
                //Don't display anything if there is more than 1 correlation property for an artifact but let the user know
                description = Bundle.SCOFetcher_occurrences_multipleProperties();
            } else if (!listOfPossibleAttributes.isEmpty()) {
                //there should only be one item in the list
                corInstance = listOfPossibleAttributes.get(0);
            }
            countAndDescription = scoSupporter.getCountPropertyAndDescription(corInstance, description);
        }
        if (isCancelled()) {
            return null;
        }
       
        return new SCOData(scoreAndDescription, commentStatus, countAndDescription);
    }
    
    @Messages({
        "SCOFetcher_score_display_name=S",
        "SCOFetcher_comment_display_name=C",
        "SCOFetcher_count_display_name=O"
    
    })
    @Override
    public void done() {
        if (isCancelled() || UserPreferences.getHideSCOColumns()) {
            return;
        }
        
        try {
            SCOData data = get();
            
            if(data == null) {
                return;
            }
            
            List<NodeProperty<?>> props = new ArrayList<>();
            
            if(data.getScoreAndDescription() != null) {
                props.add(new NodeProperty<>(
                                Bundle.SCOFetcher_score_display_name(),
                                Bundle.SCOFetcher_score_display_name(),
                                data.getScoreAndDescription().getRight(),
                                data.getScoreAndDescription().getLeft()));
            }
            
            if(data.getComment() != null) {
                props.add(new NodeProperty<>(
                                Bundle.SCOFetcher_comment_display_name(),
                                Bundle.SCOFetcher_comment_display_name(),
                                "",
                                data.getComment()));
            }
            
            if(data.getCountAndDescription() != null) {
                props.add(new NodeProperty<>(
                                Bundle.SCOFetcher_count_display_name(),
                                Bundle.SCOFetcher_count_display_name(),
                                data.getCountAndDescription().getRight(),
                                data.getCountAndDescription().getLeft()));
            }
            
             SCOSupporter scoSupporter = weakSupporterRef.get();
             
            if(!props.isEmpty() && scoSupporter != null) {
               scoSupporter.updateSheet(props);
            }
            
        } catch (InterruptedException | ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        } 
    }

    public static class SCOData {

        private final Pair<Score, String> scoreAndDescription;
        private final DataResultViewerTable.HasCommentStatus comment;
        private final Pair<Long, String> countAndDescription;

        SCOData(Pair<Score, String> scoreAndDescription, DataResultViewerTable.HasCommentStatus comment, Pair<Long, String> countAndDescription) {
            this.scoreAndDescription = scoreAndDescription;
            this.comment = comment;
            this.countAndDescription = countAndDescription;
        }

        Pair<Score, String> getScoreAndDescription() {
            return scoreAndDescription;
        }

        DataResultViewerTable.HasCommentStatus getComment() {
            return comment;
        }

        Pair<Long, String> getCountAndDescription() {
            return countAndDescription;
        }
    }

}
