/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * reportPanel.java
 *
 * Created on Feb 21, 2012, 12:13:14 PM
 */
package org.sleuthkit.autopsy.report;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Alex
 */
public class reportPanel extends javax.swing.JPanel {

    /** Creates new form reportPanel */
    public reportPanel(String report) {
        initComponents();
        setReportWindow(report);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jOptionPane1 = new javax.swing.JOptionPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jButton1 = new javax.swing.JButton();
        saveReport = new javax.swing.JButton();

        jEditorPane1.setContentType(org.openide.util.NbBundle.getMessage(reportPanel.class, "reportPanel.jEditorPane1.contentType")); // NOI18N
        jEditorPane1.setEditable(false);
        jScrollPane1.setViewportView(jEditorPane1);

        jButton1.setText(org.openide.util.NbBundle.getMessage(reportPanel.class, "reportPanel.jButton1.text")); // NOI18N

        saveReport.setText(org.openide.util.NbBundle.getMessage(reportPanel.class, "reportPanel.saveReport.text")); // NOI18N
        saveReport.setActionCommand(org.openide.util.NbBundle.getMessage(reportPanel.class, "reportPanel.saveReport.actionCommand")); // NOI18N
        saveReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 863, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(320, 320, 320)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 391, Short.MAX_VALUE)
                        .addComponent(saveReport)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(saveReport))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("");
        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents

private void saveReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveReportActionPerformed
  
    saveReportAction();
}//GEN-LAST:event_saveReportActionPerformed
  /**
     * Sets the listener for the OK button
     *
     * @param e  The action listener
     */
    public void setjButton1ActionListener(ActionListener e){
       jButton1.addActionListener(e);
    }

    
    private void setReportWindow(String report)
    {
        jEditorPane1.setText(report);
        jEditorPane1.setCaretPosition(0);
    }
    
    
    private void saveReportAction(){
        
        int option = jFileChooser1.showOpenDialog(this);  
        if(option == jFileChooser1.APPROVE_OPTION){  
        if(jFileChooser1.getSelectedFile()!=null){  
        String path = jFileChooser1.getSelectedFile().toString();
        exportReport(path);
        }
        }
    }
    
    private void exportReport(String path){
        
       String htmlpath = reportUtils.changeExtension(path, ".html");
       String xmlpath = reportUtils.changeExtension(path, ".xml");
         try {
                  FileOutputStream out = new FileOutputStream(htmlpath);
                  out.write(reportHTML.formatted_Report.toString().getBytes());
                  out.flush();
                  out.close();
                  
                  FileOutputStream xmlout = new FileOutputStream(xmlpath);
                  XMLOutputter serializer = new XMLOutputter();
                  serializer.output(reportXML.xmldoc, xmlout);
                  xmlout.flush();
                  xmlout.close();
                   jOptionPane1.showMessageDialog(this, "Report has been successfully saved!");
                }
            catch (IOException e) {
              System.err.println(e);
                }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton saveReport;
    // End of variables declaration//GEN-END:variables


}
