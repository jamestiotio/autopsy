/*
 * Autopsy Forensic Browser
 * 
 * Copyright 2013-2021 Basis Technology Corp.
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
package org.sleuthkit.autopsy.report.modules.stix;

import java.io.File;
import javax.swing.JFileChooser;
import org.sleuthkit.autopsy.guicomponeontutils.JFileChooserHelper;

/**
 * Configuration panel for STIX report generation.
 */
@SuppressWarnings("PMD.SingularField") // UI widgets cause lots of false positives
public class STIXReportModuleConfigPanel extends javax.swing.JPanel {

    String stixFile = null;
    boolean showAllResults;
    private final JFileChooserHelper chooserHelper;

    /**
     * Creates new form STIXReportModuleConfigPanel
     */
    public STIXReportModuleConfigPanel() {
        initComponents();
        showAllResults = false;
        jCheckBox1.setSelected(false);
        chooserHelper = JFileChooserHelper.getHelper();
    }
    
    void setConfiguration(STIXReportModuleSettings settings) {
        jStixFileTextField.setText(settings.getStixFile());
        showAllResults = settings.isShowAllResults();
        jCheckBox1.setSelected(settings.isShowAllResults());
    }

    STIXReportModuleSettings getConfiguration() {
        return new STIXReportModuleSettings(jStixFileTextField.getText(), jCheckBox1.isSelected());
    }

    String getStixFile() {
        return stixFile;
    }

    boolean getShowAllResults() {
        return showAllResults;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jStixFileTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        jLabel2.setText(org.openide.util.NbBundle.getMessage(STIXReportModuleConfigPanel.class, "STIXReportModuleConfigPanel.jLabel2.text")); // NOI18N

        jStixFileTextField.setText(org.openide.util.NbBundle.getMessage(STIXReportModuleConfigPanel.class, "STIXReportModuleConfigPanel.jStixFileTextField.text")); // NOI18N
        jStixFileTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jStixFileTextFieldActionPerformed(evt);
            }
        });
        jStixFileTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jStixFileTextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jStixFileTextFieldKeyTyped(evt);
            }
        });

        jButton1.setText(org.openide.util.NbBundle.getMessage(STIXReportModuleConfigPanel.class, "STIXReportModuleConfigPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText(org.openide.util.NbBundle.getMessage(STIXReportModuleConfigPanel.class, "STIXReportModuleConfigPanel.jCheckBox1.text")); // NOI18N
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jStixFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jCheckBox1))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jStixFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addContainerGap(225, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jStixFileTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStixFileTextFieldActionPerformed

    }//GEN-LAST:event_jStixFileTextFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        JFileChooser fileChooser = chooserHelper.getChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        File currentSelection = new File(jStixFileTextField.getText());
        if (currentSelection.exists()) {
            fileChooser.setCurrentDirectory(currentSelection);
        }

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            stixFile = fileChooser.getSelectedFile().getAbsolutePath();
            jStixFileTextField.setText(stixFile);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jStixFileTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jStixFileTextFieldKeyTyped

    }//GEN-LAST:event_jStixFileTextFieldKeyTyped

    private void jStixFileTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jStixFileTextFieldKeyReleased
        stixFile = jStixFileTextField.getText();
    }//GEN-LAST:event_jStixFileTextFieldKeyReleased

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        showAllResults = jCheckBox1.isSelected();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jStixFileTextField;
    // End of variables declaration//GEN-END:variables
}
