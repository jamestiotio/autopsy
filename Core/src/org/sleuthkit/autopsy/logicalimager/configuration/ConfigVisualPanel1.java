/*
 * Autopsy
 *
 * Copyright 2019-2021 Basis Technology Corp.
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
package org.sleuthkit.autopsy.logicalimager.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.lang.StringUtils;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.guicomponeontutils.JFileChooserHelper;
import org.sleuthkit.autopsy.logicalimager.dsp.DriveListUtils;

/**
 * Configuration Visual Panel 1
 */
@SuppressWarnings("PMD.SingularField") // UI widgets cause lots of false positives
final class ConfigVisualPanel1 extends JPanel {

    private static final Logger logger = Logger.getLogger(ConfigVisualPanel1.class.getName());
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_CONFIG_FILE_NAME = "logical-imager-config.json";
    private static final String UPDATE_UI_EVENT_NAME = "UPDATE_UI";
    private String configFilename;
    private final JFileChooserHelper chooserHelper;

    /**
     * Creates new form ConfigVisualPanel1
     */
    ConfigVisualPanel1() {
        initComponents();
        configFileTextField.getDocument().addDocumentListener(new MyDocumentListener(this));
        SwingUtilities.invokeLater(() -> {
            refreshDriveList();
            updateControls();
        });
        chooserHelper = JFileChooserHelper.getHelper();
    }

    @NbBundle.Messages({
        "ConfigVisualPanel1.selectConfigurationFile=Select location"
    })
    @Override
    public String getName() {
        return Bundle.ConfigVisualPanel1_selectConfigurationFile();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        configurationLocationButtonGroup = new javax.swing.ButtonGroup();
        configFileTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        descriptionScrollPane = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        configureDriveRadioButton = new javax.swing.JRadioButton();
        configureFolderRadioButton = new javax.swing.JRadioButton();
        driveListScrollPane = new javax.swing.JScrollPane();
        driveList = new javax.swing.JList<>();
        refreshButton = new javax.swing.JButton();
        warningLabel = new javax.swing.JLabel();

        configFileTextField.setEditable(false);
        configFileTextField.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(ConfigVisualPanel1.class, "ConfigVisualPanel1.browseButton.text")); // NOI18N
        browseButton.setToolTipText(org.openide.util.NbBundle.getMessage(ConfigVisualPanel1.class, "ConfigVisualPanel1.browseButton.toolTipText")); // NOI18N
        browseButton.setEnabled(false);
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        descriptionTextArea.setEditable(false);
        descriptionTextArea.setBackground(new java.awt.Color(240, 240, 240));
        descriptionTextArea.setColumns(20);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setRows(4);
        descriptionTextArea.setText(org.openide.util.NbBundle.getMessage(ConfigVisualPanel1.class, "ConfigVisualPanel1.descriptionTextArea.text")); // NOI18N
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setEnabled(false);
        descriptionScrollPane.setViewportView(descriptionTextArea);

        configurationLocationButtonGroup.add(configureDriveRadioButton);
        configureDriveRadioButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(configureDriveRadioButton, org.openide.util.NbBundle.getMessage(ConfigVisualPanel1.class, "ConfigVisualPanel1.configureDriveRadioButton.text_1")); // NOI18N
        configureDriveRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configureDriveRadioButtonActionPerformed(evt);
            }
        });

        configurationLocationButtonGroup.add(configureFolderRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(configureFolderRadioButton, org.openide.util.NbBundle.getMessage(ConfigVisualPanel1.class, "ConfigVisualPanel1.configureFolderRadioButton.text_1")); // NOI18N
        configureFolderRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configureFolderRadioButtonActionPerformed(evt);
            }
        });

        driveList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        driveList.setEnabled(false);
        driveList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                driveListMouseReleasedSelection(evt);
            }
        });
        driveList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                driveListKeyReleasedSelection(evt);
            }
        });
        driveListScrollPane.setViewportView(driveList);

        org.openide.awt.Mnemonics.setLocalizedText(refreshButton, org.openide.util.NbBundle.getMessage(ConfigVisualPanel1.class, "ConfigVisualPanel1.refreshButton.text")); // NOI18N
        refreshButton.setEnabled(false);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        warningLabel.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(warningLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descriptionScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(driveListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(configFileTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(browseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(configureDriveRadioButton)
                            .addComponent(configureFolderRadioButton))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(descriptionScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(configureDriveRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(driveListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(refreshButton)
                        .addGap(0, 87, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(configureFolderRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(configFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(warningLabel)
                .addContainerGap(52, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @NbBundle.Messages({
        "ConfigVisualPanel1.chooseFileTitle=Select a Logical Imager configuration"
    })
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        chooseFile(Bundle.ConfigVisualPanel1_chooseFileTitle());
    }//GEN-LAST:event_browseButtonActionPerformed

    private void configureFolderRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configureFolderRadioButtonActionPerformed
        updateControls();
    }//GEN-LAST:event_configureFolderRadioButtonActionPerformed

    private void configureDriveRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configureDriveRadioButtonActionPerformed
        updateControls();
    }//GEN-LAST:event_configureDriveRadioButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        refreshDriveList();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void driveListKeyReleasedSelection(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_driveListKeyReleasedSelection
        firePropertyChange(UPDATE_UI_EVENT_NAME, false, true); // NON-NLS
    }//GEN-LAST:event_driveListKeyReleasedSelection

    private void driveListMouseReleasedSelection(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_driveListMouseReleasedSelection
        firePropertyChange(UPDATE_UI_EVENT_NAME, false, true); // NON-NLS
    }//GEN-LAST:event_driveListMouseReleasedSelection

    /*
     * Return the Windows file system name of the drive
     * @param drive File system drive, should be of the form "C:\"
     *
     */
    @Messages({"ConfigVisualPanel1.unknown=Unknown"})
    private String getFileSystemName(String drive) {
        FileSystem fileSystem = FileSystems.getDefault();
        FileSystemProvider provider = fileSystem.provider();
        try {
            FileStore fileStore = provider.getFileStore(Paths.get(drive));
            return fileStore.type();
        } catch (IOException ex) {
            return Bundle.ConfigVisualPanel1_unknown();
        }
    }

    /**
     * Refresh the list of local drives on the current machine
     */
    @NbBundle.Messages({
        "ConfigVisualPanel1.messageLabel.noExternalDriveFound=No drive found",
        "# {0} - root", "# {1} - description", "# {2} - size with unit", "# {3} - file system",
        "ConfigVisualPanel1.driveListItem={0} ({1}) ({2}) - File system: {3}"
    })
    private void refreshDriveList() {
        List<String> listData = new ArrayList<>();
        File[] roots = File.listRoots();
        int firstRemovableDrive = -1;
        int i = 0;
        for (File root : roots) {
            if (DriveListUtils.isNetworkDrive(root.toString().replace(":\\", ""))) {
                continue;
            }            
            String description = FileSystemView.getFileSystemView().getSystemTypeDescription(root);
            long spaceInBytes = root.getTotalSpace();
            String sizeWithUnit = DriveListUtils.humanReadableByteCount(spaceInBytes, false);
            String fileSystem = getFileSystemName(root.toString());
            listData.add(Bundle.ConfigVisualPanel1_driveListItem(root, description, sizeWithUnit, fileSystem));
            if (firstRemovableDrive == -1) {
                try {
                    FileStore fileStore = Files.getFileStore(root.toPath());
                    if ((boolean) fileStore.getAttribute("volume:isRemovable")) { //NON-NLS
                        firstRemovableDrive = i;
                    }
                } catch (IOException ignored) {
                    //unable to get this removable drive for default selection will try and select next removable drive by default 
                    logger.log(Level.INFO, String.format("Unable to select first removable drive found %s", root.toString())); // NON-NLS
                }
            }
            i++;
        }
        driveList.setListData(listData.toArray(new String[listData.size()]));
        if (!listData.isEmpty()) {
            // auto-select the first external drive, if any
            driveList.setSelectedIndex(firstRemovableDrive == -1 ? 0 : firstRemovableDrive);
            firePropertyChange(UPDATE_UI_EVENT_NAME, false, true); // NON-NLS
            driveList.requestFocusInWindow();
            warningLabel.setText("");
        } else {
            warningLabel.setText(Bundle.ConfigVisualPanel1_messageLabel_noExternalDriveFound());
        }
    }

    /**
     * Update which controls are enabled to reflect the current radio button
     * selection.
     */
    private void updateControls() {
        browseButton.setEnabled(configureFolderRadioButton.isSelected());
        refreshButton.setEnabled(configureDriveRadioButton.isSelected());
        driveList.setEnabled(configureDriveRadioButton.isSelected());
        driveListScrollPane.setEnabled(configureDriveRadioButton.isSelected());
        firePropertyChange(UPDATE_UI_EVENT_NAME, false, true); // NON-NLS
    }

    /**
     * Open a file chooser to allow users to choose a json configuration file in
     * a folder.
     *
     * @param title the dialog title
     */
    @NbBundle.Messages({
        "ConfigVisualPanel1.fileNameExtensionFilter=Configuration JSON File",
        "ConfigVisualPanel1.invalidConfigJson=Invalid config JSON: ",
        "ConfigVisualPanel1.configurationError=Configuration error",})
    private void chooseFile(String title) {
        final String jsonExt = ".json"; // NON-NLS
        JFileChooser fileChooser = chooserHelper.getChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setDragEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter(Bundle.ConfigVisualPanel1_fileNameExtensionFilter(), new String[]{"json"}); // NON-NLS
        fileChooser.setFileFilter(filter);
        fileChooser.setSelectedFile(new File(DEFAULT_CONFIG_FILE_NAME)); // default
        fileChooser.setMultiSelectionEnabled(false);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getPath();
            if (new File(path).exists()) {
                try {
                    loadConfigFile(path);
                    configFilename = path;
                    configFileTextField.setText(path);
                } catch (JsonIOException | JsonSyntaxException | IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            Bundle.ConfigVisualPanel1_invalidConfigJson() + ex.getMessage(),
                            Bundle.ConfigVisualPanel1_configurationError(),
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (!path.endsWith(jsonExt)) {
                    path += jsonExt;
                }
                configFilename = path;
                configFileTextField.setText(path);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JTextField configFileTextField;
    private javax.swing.ButtonGroup configurationLocationButtonGroup;
    private javax.swing.JRadioButton configureDriveRadioButton;
    private javax.swing.JRadioButton configureFolderRadioButton;
    private javax.swing.JScrollPane descriptionScrollPane;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JList<String> driveList;
    private javax.swing.JScrollPane driveListScrollPane;
    private javax.swing.JButton refreshButton;
    private javax.swing.JLabel warningLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Load a json config file specified by the path argument.
     *
     *
     * @param path the path of the json config to load
     *
     * @return the LogicalImagerConfig which contains the rules from the loaded
     *         config.
     *
     * @throws FileNotFoundException
     * @throws JsonIOException
     * @throws JsonSyntaxException
     * @throws IOException
     */
    @NbBundle.Messages({
        "# {0} - filename",
        "ConfigVisualPanel1.configFileIsEmpty=Configuration file {0} is empty",})
    private LogicalImagerConfig loadConfigFile(String path) throws FileNotFoundException, JsonIOException, JsonSyntaxException, IOException {
        try (FileInputStream is = new FileInputStream(path)) {
            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(LogicalImagerConfig.class, new LogicalImagerConfigDeserializer());
            Gson gson = gsonBuilder.create();
            LogicalImagerConfig loadedConfig = gson.fromJson(reader, LogicalImagerConfig.class);
            if (loadedConfig == null) {
                // This happens if the file is empty. Gson doesn't call the deserializer and doesn't throw any exception.
                throw new IOException(Bundle.ConfigVisualPanel1_configFileIsEmpty(path));
            }
            return loadedConfig;
        }
    }

    /**
     * Get the LogicalImagerConfig for the currently selected config file.
     *
     * @return the LogicalImagerConfig which contains the rules from the loaded
     *         config.
     */
    LogicalImagerConfig getConfig() {
        String configFileName = getConfigPath();
        if (configFileName != null && new File(configFileName).exists()) {
            try {
                return loadConfigFile(configFileName);
            } catch (JsonIOException | JsonSyntaxException | IOException ex) {
                return new LogicalImagerConfig();
            }
        } else {
            return new LogicalImagerConfig();
        }
    }

    /**
     * Get the path of the currently selected json config file.
     *
     * @return the path of the currently selected config file or null if invalid
     *         settings are selected
     */
    String getConfigPath() {
        if (configureFolderRadioButton.isSelected()) {
            return configFilename;
        } else {
            String selectedStr = driveList.getSelectedValue();
            if (selectedStr == null) {
                return null;
            }
            return selectedStr.substring(0, 3) + DEFAULT_CONFIG_FILE_NAME;
        }
    }

    /**
     * The name of the event which signifies an update to the settings reflected
     * by ConfigVisualPanel1
     *
     * @return UPDATE_UI_EVENT_NAME
     */
    static String getUpdateEventName() {
        return UPDATE_UI_EVENT_NAME;
    }

    void setConfigFilename(String filename) {
        configFileTextField.setText(filename);
    }

    /**
     * Checks if the current panel has valid settings selected.
     *
     * @return true if panel has valid settings selected, false otherwise
     */
    boolean isPanelValid() {
        return !StringUtils.isBlank(getConfigPath()) 
                && !(getFileSystemName(getConfigPath().substring(0, 3)).equals("FAT") // NON-NLS
                    || getFileSystemName(getConfigPath().substring(0, 3)).equals("FAT32")) // NON-NLS
                && ((configureDriveRadioButton.isSelected() && !StringUtils.isBlank(driveList.getSelectedValue()))
                    || (configureFolderRadioButton.isSelected() && (!configFileTextField.getText().isEmpty())));
    }

    /**
     * Document Listener for textfield
     */
    private static class MyDocumentListener implements DocumentListener {

        private final ConfigVisualPanel1 panel;

        MyDocumentListener(ConfigVisualPanel1 aThis) {
            this.panel = aThis;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            fireChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            fireChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            fireChange();
        }

        private void fireChange() {
            panel.firePropertyChange(UPDATE_UI_EVENT_NAME, false, true); // NON-NLS
        }
    }

}
