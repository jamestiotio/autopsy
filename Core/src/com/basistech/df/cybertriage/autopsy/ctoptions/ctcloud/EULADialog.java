/*
 * Autopsy Forensic Browser
 *
 * Copyright 2023 Basis Technology Corp.
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
package com.basistech.df.cybertriage.autopsy.ctoptions.ctcloud;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javax.swing.SwingUtilities;
import org.apache.commons.io.IOUtils;
import org.sleuthkit.autopsy.coreutils.Logger;

/**
 *
 * @author gregd
 */
public class EULADialog extends javax.swing.JDialog {

    private static final Logger LOGGER = Logger.getLogger(EULADialog.class.getName());
    private static final String EULA_RESOURCE = "EULA.htm";

    private boolean acceptPressed = false;

    /**
     * Creates new form EULADialog
     */
    public EULADialog(java.awt.Frame parent, boolean modal) throws IOException {
        super(parent, modal);
        initComponents();
        loadEULA();
    }

    boolean isAcceptPressed() {
        return acceptPressed;
    }

    private void loadEULA() throws IOException {
        InputStream eulaInputStream = EULADialog.class.getResourceAsStream(EULA_RESOURCE);
        final String htmlString = IOUtils.toString(eulaInputStream, StandardCharsets.UTF_8);
        final JFXPanel fxPanel = new JFXPanel();
        this.viewablePanel.add(fxPanel, BorderLayout.CENTER);
        Platform.runLater(() -> {
            WebView webView = new WebView();
            webView.setMaxSize(Short.MAX_VALUE, Short.MAX_VALUE);
            webView.setPrefSize(Short.MAX_VALUE, Short.MAX_VALUE);
            webView.setMinSize(100, 100);
            webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                if (newState == State.SUCCEEDED) {
                    SwingUtilities.invokeLater(() -> EULADialog.this.acceptButton.setEnabled(true));        
                }
            });
            webView.getEngine().loadContent(htmlString, "text/html");
            VBox root = new VBox(webView);
            Scene scene = new Scene(root, Color.RED);
            fxPanel.setScene(scene);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        viewablePanel = new javax.swing.JPanel();
        javax.swing.JPanel paddingPanel = new javax.swing.JPanel();
        acceptButton = new javax.swing.JButton();
        javax.swing.JButton cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(EULADialog.class, "EULADialog.title")); // NOI18N
        setMaximumSize(new java.awt.Dimension(32767, 32767));
        setPreferredSize(new java.awt.Dimension(550, 550));
        setSize(new java.awt.Dimension(550, 550));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        viewablePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(viewablePanel, gridBagConstraints);

        paddingPanel.setMaximumSize(new java.awt.Dimension(32767, 0));

        javax.swing.GroupLayout paddingPanelLayout = new javax.swing.GroupLayout(paddingPanel);
        paddingPanel.setLayout(paddingPanelLayout);
        paddingPanelLayout.setHorizontalGroup(
            paddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        paddingPanelLayout.setVerticalGroup(
            paddingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(paddingPanel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(acceptButton, org.openide.util.NbBundle.getMessage(EULADialog.class, "EULADialog.acceptButton.text")); // NOI18N
        acceptButton.setEnabled(false);
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        getContentPane().add(acceptButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, org.openide.util.NbBundle.getMessage(EULADialog.class, "EULADialog.cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(cancelButton, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        acceptPressed = true;
        dispose();
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JPanel viewablePanel;
    // End of variables declaration//GEN-END:variables
}
