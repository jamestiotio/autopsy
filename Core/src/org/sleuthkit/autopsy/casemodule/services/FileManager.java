/*
 *
 * Autopsy Forensic Browser
 * 
 * Copyright 2012 Basis Technology Corp.
 * 
 * Copyright 2012 42six Solutions.
 * Contact: aebadirad <at> 42six <dot> com
 * Project Contact/Architect: carrier <at> sleuthkit <dot> org
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
package org.sleuthkit.autopsy.casemodule.services;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.ingest.IngestServices;
import org.sleuthkit.autopsy.ingest.ModuleContentEvent;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.DerivedFile;
import org.sleuthkit.datamodel.FsContent;
import org.sleuthkit.datamodel.Image;
import org.sleuthkit.datamodel.LocalFile;
import org.sleuthkit.datamodel.SleuthkitCase;
import org.sleuthkit.datamodel.TskCoreException;
import org.sleuthkit.datamodel.TskData.TSK_DB_FILES_TYPE_ENUM;
import org.sleuthkit.datamodel.VirtualDirectory;

/**
 * Abstraction to facilitate access to files and directories.
 */
public class FileManager implements Closeable {

    private SleuthkitCase tskCase;
    private static final Logger logger = Logger.getLogger(FileManager.class.getName());
    private volatile long localFilesRootId = -1;
    private volatile VirtualDirectory localFilesRoot = null;

    public FileManager(SleuthkitCase tskCase) {
        this.tskCase = tskCase;
        init();
    }

    /**
     * initialize the file manager for the case
     */
    private synchronized void init() {
        //get a handle of local files root
        //or create if not present
        //this saves number of queries, and ensures LocalFiles is the first file object created for consistency
        if (localFilesRootId == -1) {
            try {
                localFilesRootId = tskCase.getLocalFilesRootDirectoryId();
                localFilesRoot = (VirtualDirectory) tskCase.getAbstractFileById(localFilesRootId);
            } catch (TskCoreException ex) {
                logger.log(Level.SEVERE, "Error getting/creating local files root at file manager init", ex);
            }
        }

    }

    /**
     * @param image image where to find files
     * @param fileName the name of the file or directory to match
     * @return a list of FsContent for files/directories whose name matches the
     * given fileName
     */
    public synchronized List<FsContent> findFiles(Image image, String fileName) throws TskCoreException {
        if (tskCase == null) {
            throw new TskCoreException("Attempted to use FileManager after it was closed.");
        }
        return tskCase.findFiles(image, fileName);
    }

    /**
     * @param image image where to find files
     * @param fileName the name of the file or directory to match
     * @param dirName the name of a parent directory of fileName
     * @return a list of FsContent for files/directories whose name matches
     * fileName and whose parent directory contains dirName.
     */
    public synchronized List<FsContent> findFiles(Image image, String fileName, String dirName) throws TskCoreException {
        if (tskCase == null) {
            throw new TskCoreException("Attempted to use FileManager after it was closed.");
        }
        return tskCase.findFiles(image, fileName, dirName);
    }

    /**
     * @param image image where to find files
     * @param fileName the name of the file or directory to match
     * @param parentFsContent
     * @return a list of FsContent for files/directories whose name matches
     * fileName and that were inside a directory described by parentFsContent.
     */
    public synchronized List<FsContent> findFiles(Image image, String fileName, FsContent parentFsContent) throws TskCoreException {
        if (tskCase == null) {
            throw new TskCoreException("Attempted to use FileManager after it was closed.");
        }
        return findFiles(image, fileName, parentFsContent.getName());
    }

    /**
     * @param image image where to find files
     * @param filePath The full path to the file(s) of interest. This can
     * optionally include the image and volume names.
     * @return a list of FsContent that have the given file path.
     */
    public synchronized List<FsContent> openFiles(Image image, String filePath) throws TskCoreException {
        if (tskCase == null) {
            throw new TskCoreException("Attempted to use FileManager after it was closed.");
        }
        return tskCase.openFiles(image, filePath);
    }

    /**
     * Creates a derived file, adds it to the database and returns it.
     *
     * @param fileName file name the derived file
     * @param localPath local path of the derived file, including the file name.
     * The path is relative to the database path.
     * @param size size of the derived file in bytes
     * @param ctime
     * @param crtime
     * @param atime
     * @param mtime
     * @param isFile whether a file or directory, true if a file
     * @param parentFile the parent file object this the new file was derived
     * from, either a fs file or parent derived file/dikr\\r
     * @param rederiveDetails details needed to re-derive file (will be specific
     * to the derivation method), currently unused
     * @param toolName name of derivation method/tool, currently unused
     * @param toolVersion version of derivation method/tool, currently unused
     * @param otherDetails details of derivation method/tool, currently unused
     * @return newly created derived file object added to the database
     * @throws TskCoreException exception thrown if the object creation failed
     * due to a critical system error or of the file manager has already been
     * closed
     *
     */
    public synchronized DerivedFile addDerivedFile(String fileName, String localPath, long size,
            long ctime, long crtime, long atime, long mtime,
            boolean isFile, AbstractFile parentFile,
            String rederiveDetails, String toolName, String toolVersion, String otherDetails) throws TskCoreException {

        if (tskCase == null) {
            throw new TskCoreException("Attempted to use FileManager after it was closed.");
        }

        return tskCase.addDerivedFile(fileName, localPath, size,
                ctime, crtime, atime, mtime,
                isFile, parentFile, rederiveDetails, toolName, toolVersion, otherDetails);
    }

    /**
     * Add local files and dirs - the general method that does it all for files
     * and dirs and in bulk/
     *
     * @param localAbsPaths list of absolute paths to local files and dirs
     * @return list of root AbstractFile objects created to represent roots of
     * added local files/dirs
     * @throws TskCoreException exception thrown if the object creation failed
     * due to a critical system error or of the file manager has already been
     * closed. There is no "revert" logic if one of the additions fails. The
     * addition stops with the first error encountered.
     */
    public synchronized List<AbstractFile> addLocalFilesDirs(List<String> localAbsPaths) throws TskCoreException {
        final List<AbstractFile> added = new ArrayList<AbstractFile>();

        final List<java.io.File> rootsToAdd = new ArrayList<java.io.File>();
        //first validate all the inputs before any additions
        for (String absPath : localAbsPaths) {
            java.io.File localFile = new java.io.File(absPath);
            if (!localFile.exists() || !localFile.canRead()) {
                String msg = "One of the local files/dirs to add is not readable: " + localFile.getAbsolutePath() + ", aborting the process before any files added";
                logger.log(Level.SEVERE, msg);
                throw new TskCoreException(msg);
            }
            rootsToAdd.add(localFile);
        }

        for (java.io.File localRootToAdd : rootsToAdd) {
            AbstractFile localFileAdded = null;
            if (localRootToAdd.isFile()) {
                localFileAdded = addLocalFileSingle(localRootToAdd.getAbsolutePath());
            } else {
                localFileAdded = this.addLocalDir(localRootToAdd.getAbsolutePath());
            }
            if (localFileAdded == null) {
                String msg = "One of the local files/dirs could not be added: " + localRootToAdd.getAbsolutePath();
                logger.log(Level.SEVERE, msg);
                throw new TskCoreException(msg);
            } else {
                added.add(localFileAdded);
                //send new content event
                //for now reusing ingest events, in future this will be replaced by datamodel / observer sending out events
                IngestServices.getDefault().fireModuleContentEvent(new ModuleContentEvent(localFileAdded));
            }
        }


        return added;
    }

    /**
     * Helper (internal) to add child of local dir recursively
     *
     * @param parentVd
     * @param childLocalFile
     * @throws TskCoreException
     */
    private void addLocalDirectoryRecInt(VirtualDirectory parentVd,
            java.io.File childLocalFile) throws TskCoreException {

        final boolean isDir = childLocalFile.isDirectory();

        if (isDir) {
            //create virtual folder
            final VirtualDirectory childVd = tskCase.addVirtualDirectory(parentVd.getId(), childLocalFile.getName());
            //add children recursively
            for (java.io.File childChild : childLocalFile.listFiles()) {
                addLocalDirectoryRecInt(childVd, childChild);
            }
        } else {
            //add leaf file, base case
            this.addLocalFileSingle(childLocalFile.getAbsolutePath(), parentVd);
        }

    }

    /**
     * Gets matching (LocalFile or VirtualDirectory) child of LocalFiles root
     * with a matching name, or null if it does not exist
     *
     * @param name
     * @return matching root child or null
     * @throws TskCoreException
     */
    private AbstractFile getMatchingLocalFilesRootChild(String name) throws TskCoreException {
        AbstractFile ret = null;
        try {
            for (Content child : localFilesRoot.getChildren()) {
                TSK_DB_FILES_TYPE_ENUM fileType = ((AbstractFile) child).getType();
                if ((fileType.equals(TSK_DB_FILES_TYPE_ENUM.VIRTUAL_DIR)
                        || fileType.equals(TSK_DB_FILES_TYPE_ENUM.LOCAL))
                        && child.getName().equals(name)) {
                    ret = (AbstractFile) child;
                    break;
                }
            }
        } catch (TskCoreException ex) {
            logger.log(Level.SEVERE, "Error quering matching children of local files root, ", ex);
            throw new TskCoreException("Error quering matching children of local files root, ", ex);
        }

        return ret;
    }

    /**
     * Return count of local files already in this case that represent the same
     * file/dir (have the same localAbsPath)
     *
     * @param parent parent dir of the files to check
     * @param localAbsPath local absolute path of the file to check
     * @param localName the name of the file to check
     * @return count of objects representing the same local file
     * @throws TskCoreException
     */
    private int getCountMatchingLocalFiles(AbstractFile parent, String localAbsPath, String localName) throws TskCoreException {
        int count = 0;

        for (Content child : parent.getChildren()) {

            if (child instanceof VirtualDirectory && localName.equals(child.getName())) {
                ++count;
            } else if (child instanceof AbstractFile
                    && localAbsPath.equals(((AbstractFile) child).getLocalAbsPath())) {
                ++count;
            }

        }

        return count;

    }

    /**
     * Return count of local files already in this case that represent the same
     * file/dir (have the same localAbsPath), for a folder directly under
     * LocalFiles root dir
     *
     * @param localAbsPath local absolute path of the file to check
     * @param localName the name of the file to check
     * @return count of objects representing the same local file
     * @throws TskCoreException
     */
    private int getCountMatchingLocalFiles(String localAbsPath, String localName) throws TskCoreException {
        int count = 0;

        for (Content child : this.localFilesRoot.getChildren()) {
            if (child instanceof VirtualDirectory && localName.equals(child.getName())) {
                ++count;
            } else if (child instanceof AbstractFile
                    && localAbsPath.equals(((AbstractFile) child).getLocalAbsPath())) {
                ++count;
            }
        }

        return count;

    }

    /**
     * Add a local directory and its children recursively. Parent container of
     * the local dir is added for context.
     *
     *
     * @param localAbsPath local absolute path of root folder whose children are
     * to be added recursively. If there is a parent dir, it is added as a
     * container, for context.
     * @return parent virtual directory folder created representing the
     * localAbsPath node
     * @throws TskCoreException exception thrown if the object creation failed
     * due to a critical system error or of the file manager has already been
     * closed, or if the localAbsPath could not be accessed
     */
    private synchronized VirtualDirectory addLocalDir(String localAbsPath) throws TskCoreException {
        if (tskCase == null) {
            throw new TskCoreException("Attempted to use FileManager after it was closed.");
        }

        final java.io.File localDir = new java.io.File(localAbsPath);
        final String localName = localDir.getName();
        if (!localDir.exists()) {
            throw new TskCoreException("Attempted to add a local dir that does not exist: " + localAbsPath);
        }
        if (!localDir.canRead()) {
            throw new TskCoreException("Attempted to add a local dir that is not readable: " + localAbsPath);
        }

        if (!localDir.isDirectory()) {
            throw new TskCoreException("Attempted to add a local dir that is not a directory: " + localAbsPath);
        }

        String parentName = null;
        java.io.File parentDir = localDir.getParentFile();
        if (parentDir != null) {
            parentName = parentDir.getName();
        }

        String rootVdName = localDir.getName();

        VirtualDirectory rootVd = null;
        try {
            if (parentName == null) {
                //check if parentless dir already exists, if so, append number to it
                int existingCount = getCountMatchingLocalFiles(localAbsPath, localName);
                if (existingCount > 0) {
                    rootVdName = rootVdName + "_" + Integer.toString(existingCount);
                }
                rootVd = tskCase.addVirtualDirectory(localFilesRootId, rootVdName);

            } else {
                //add parent dir for context
                VirtualDirectory contextDir = null;
                final AbstractFile existing = getMatchingLocalFilesRootChild(parentName);
                if (existing != null && existing instanceof VirtualDirectory) {
                    contextDir = (VirtualDirectory) existing;
                } else {
                    contextDir = tskCase.addVirtualDirectory(localFilesRootId, parentName);
                }

                //check if parentless dir already exists, if so, append number to it
                int existingCount = getCountMatchingLocalFiles(contextDir, localAbsPath, localName);
                if (existingCount > 0) {
                    rootVdName = rootVdName + "_" + Integer.toString(existingCount);
                }
                rootVd = tskCase.addVirtualDirectory(contextDir.getId(), rootVdName);

            }
        } catch (TskCoreException e) {
            //log and rethrow
            final String msg = "Error creating root dir for local dir to be added, can't addLocalDir: " + localDir;
            logger.log(Level.SEVERE, msg, e);
            throw new TskCoreException(msg);
        }

        try {
            java.io.File[] localChildren = localDir.listFiles();
            for (java.io.File localChild : localChildren) {
                //add children recursively, at a time in separate transaction
                //consider a single transaction for everything
                addLocalDirectoryRecInt(rootVd, localChild);
            }
        } catch (TskCoreException e) {
            final String msg = "Error creating local children for local dir to be added, can't fully add addLocalDir: " + localDir;
            logger.log(Level.SEVERE, msg, e);
            throw new TskCoreException(msg);
        }


        return rootVd;
    }

    /**
     * Creates a single local file under $LocalFiles for the case, adds it to
     * the database and returns it. Does not refresh the views of data.
     *
     * @param localAbsPath local absolute path of the local file, including the
     * file name.
     * @return newly created local file object added to the database
     * @throws TskCoreException exception thrown if the object creation failed
     * due to a critical system error or of the file manager has already been
     * closed, or if the localAbsPath could not be accessed
     *
     */
    private synchronized LocalFile addLocalFileSingle(String localAbsPath) throws TskCoreException {

        if (tskCase == null) {
            throw new TskCoreException("Attempted to use FileManager after it was closed.");
        }

        return addLocalFileSingle(localAbsPath, null);
    }

    /**
     * Creates a single local file under parentFile for the case, adds it to the
     * database and returns it. Does not refresh the views of data.
     *
     * @param localAbsPath local absolute path of the local file, including the
     * file name
     * @param parentFile parent file object (such as virtual directory, another
     * local file, or fscontent File),
     * @return newly created local file object added to the database
     * @throws TskCoreException exception thrown if the object creation failed
     * due to a critical system error or of the file manager has already been
     * closed
     *
     */
    private synchronized LocalFile addLocalFileSingle(String localAbsPath, AbstractFile parentFile) throws TskCoreException {

        if (tskCase == null) {
            throw new TskCoreException("Attempted to use FileManager after it was closed.");
        }

        java.io.File localFile = new java.io.File(localAbsPath);
        if (!localFile.exists()) {
            throw new TskCoreException("Attempted to add a local file that does not exist: " + localAbsPath);
        }
        if (!localFile.canRead()) {
            throw new TskCoreException("Attempted to add a local file that is not readable: " + localAbsPath);
        }

        long size = localFile.length();
        boolean isFile = localFile.isFile();

        long ctime = 0;
        long crtime = 0;
        long atime = 0;
        long mtime = 0;

        String fileName = localFile.getName();

        int existingCount = getCountMatchingLocalFiles(localFilesRoot, localAbsPath, fileName);
        if (existingCount > 0) {
            fileName = fileName + "_" + Integer.toString(existingCount);
        }
        LocalFile lf = tskCase.addLocalFile(fileName, localAbsPath, size,
                ctime, crtime, atime, mtime,
                isFile, parentFile);


        return lf;
    }

    @Override
    public synchronized void close() throws IOException {
        tskCase = null;
    }
}
