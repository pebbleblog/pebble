/*
 * Copyright (c) 2003-2011, Simon Brown
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *   - Neither the name of Pebble nor the names of its contributors may
 *     be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sourceforge.pebble.domain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Tests for the FileManager class.
 *
 * @author    Simon Brown
 */
public class FileManagerTest extends SingleBlogTestCase {

  private FileManager fileManager;

  protected void setUp() throws Exception {
    super.setUp();

    fileManager = new FileManager(blog, FileMetaData.BLOG_FILE);
  }

  /**
   * Tests that a file manager for blog images can be created.
   */
  public void testCreateFileManagerForBlogImages() {
    fileManager = new FileManager(blog, FileMetaData.BLOG_IMAGE);
    assertEquals(new File(blog.getImagesDirectory()), fileManager.getRootDirectory());
  }

  /**
   * Tests that a file manager for blog files can be created.
   */
  public void testCreateFileManagerForBlogFiles() {
    fileManager = new FileManager(blog, FileMetaData.BLOG_FILE);
    assertEquals(new File(blog.getFilesDirectory()), fileManager.getRootDirectory());
  }

  /**
   * Tests that a file manager for theme files can be created.
   */
  public void testCreateFileManagerForThemeFiles() {
    Theme theme = new Theme(blog, "custom", "/some/path");
    blog.setEditableTheme(theme);
    fileManager = new FileManager(blog, FileMetaData.THEME_FILE);
    assertEquals(blog.getEditableTheme().getPathToLiveTheme(), fileManager.getRootDirectory());
  }

  /**
   * Tests that a file manager for blog data can be created.
   */
  public void testCreateFileManagerForBlogData() {
    fileManager = new FileManager(blog, FileMetaData.BLOG_DATA);
    assertEquals(new File(blog.getRoot()), fileManager.getRootDirectory());
  }

  /**
   * Tests that a information about a file can be retrieved using an
   * absolute path.
   */
  public void testGetFileMetaDataWithAbsolutePath() {
    FileMetaData file = fileManager.getFileMetaData("/afile.zip");
    assertFalse(file.isDirectory());
    assertEquals("/", file.getPath());
    assertEquals("afile.zip", file.getName());

    file = fileManager.getFileMetaData("/a/a.txt");
    assertFalse(file.isDirectory());
    assertEquals("/a", file.getPath());
    assertEquals("a.txt", file.getName());
  }

  /**
   * Tests that a information about a file can be retrieved using a
   * path and name.
   */
  public void testGetFileMetaDataWithPathAndName() {
    FileMetaData file = fileManager.getFileMetaData("/", "afile.zip");
    assertFalse(file.isDirectory());
    assertEquals("/", file.getPath());
    assertEquals("afile.zip", file.getName());

    file = fileManager.getFileMetaData("/a", "a.txt");
    assertFalse(file.isDirectory());
    assertEquals("/a", file.getPath());
    assertEquals("a.txt", file.getName());
  }

  /**
   * Tests that a java.io.File reference to a path can be created.
   */
  public void testGetFileFromAbsolutePath() {
    File file = fileManager.getFile("/afile.zip");
    assertEquals(new File(blog.getFilesDirectory(), "afile.zip"), file);
  }

  /**
   * Tests that a java.io.File reference to a path can be created.
   */
  public void testGetFileFromRelativePath() throws Exception {
    File file = fileManager.getFile("afile.zip");
    assertEquals(new File(blog.getFilesDirectory(), "afile.zip"), file);

    file = fileManager.getFile("./afile.zip");
    assertEquals(new File(blog.getFilesDirectory(), "afile.zip").getCanonicalFile(), file.getCanonicalFile());
  }

  /**
   * Tests whether whether a file is underneath the root directory.
   */
  public void testFileIsUnderneathRootDirectory() {
    File file = fileManager.getFile("/afile.zip");
    assertTrue(fileManager.isUnderneathRootDirectory(file));
  }

  /**
   * Tests whether whether a file is underneath the root directory.
   */
  public void testFileIsNotUnderneathRootDirectory() {
    File file = fileManager.getFile("../afile.zip");
    assertFalse(fileManager.isUnderneathRootDirectory(file));
  }

  /**
   * Tests that a directory can be created.
   */
  public void testCreateDirectory() throws Exception {
    File newDirectory = fileManager.createDirectory("/", "asubdirectory");
    assertEquals(new File(blog.getFilesDirectory(), "asubdirectory"), newDirectory);
    assertTrue(newDirectory.exists());

    // and clean up this test
    newDirectory.delete();
  }

  /**
   * Tests that a directory can't be created outside of the root.
   */
  public void testCreateDirectoryThrowsExceptionWhenOutsideOfRoot() {
    try {
      fileManager.createDirectory("/", "../asubdirectory");
      fail("Creating a directory outside of the root isn't allowed");
    } catch (IllegalFileAccessException ifae) {
    }
  }

  /**
   * Tests that a file isn't copied when no name is specified.
   */
  public void testFileNotCopiedWhenNoNameGiven() throws Exception {
    assertNull(fileManager.copyFile("/", "afile.txt", null));
    assertNull(fileManager.copyFile("/", "afile.txt", ""));
  }

  /**
   * Tests that a file isn't copied when the same name is specified.
   */
  public void testFileNotCopiedWhenSameNameGiven() throws Exception {
    assertNull(fileManager.copyFile("/", "afile.txt", "afile.txt"));
  }

  /**
   * Tests that a file can't be copied when the original file is outside of
   * the root.
   */
  public void testCopyFileThrowsExceptionWhenOriginalFileOutsideOfRoot() throws Exception {
    try {
      assertNull(fileManager.copyFile("/", "../afile.txt", "afile.txt"));
      fail("Copying a file outside of the root isn't allowed");
    } catch (IllegalFileAccessException ifae) {
    }
  }

  /**
   * Tests that a file can't be copied when the new file is outside of
   * the root.
   */
  public void testCopyFileThrowsExceptionWhenNewFileOutsideOfRoot() throws Exception {
    try {
      assertNull(fileManager.copyFile("/", "afile.txt", "../afile.txt"));
      fail("Copying a file outside of the root isn't allowed");
    } catch (IllegalFileAccessException ifae) {
    }
  }

  /**
   * Tests that a file can be copied.
   */
  public void testCopyFile() throws Exception {
    File file = fileManager.getFile("/afile.txt");
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    writer.write("Testing...");
    writer.flush();
    writer.close();

    File newFile = fileManager.copyFile("/", "afile.txt", "anewfile.txt");
    assertNotNull(newFile);
    assertTrue(newFile.exists());
    assertEquals(file.length(), newFile.length());

    // and clean up
    file.delete();
    newFile.delete();
  }

  /**
   * Tests that a file isn't renamed when no name is specified.
   */
  public void testFileNotRenamedWhenNoNameGiven() throws Exception {
    assertNull(fileManager.renameFile("/", "afile.txt", null));
    assertNull(fileManager.renameFile("/", "afile.txt", ""));
  }

  /**
   * Tests that a file isn't renamed when the same name is specified.
   */
  public void testFileNotRenamedWhenSameNameGiven() throws Exception {
    assertNull(fileManager.renameFile("/", "afile.txt", "afile.txt"));
  }

  /**
   * Tests that a file can't be renamed when the original file is outside of
   * the root.
   */
  public void testRenameFileThrowsExceptionWhenOriginalFileOutsideOfRoot() throws Exception {
    try {
      assertNull(fileManager.renameFile("/", "../afile.txt", "afile.txt"));
      fail("Renaming a file outside of the root isn't allowed");
    } catch (IllegalFileAccessException ifae) {
    }
  }

  /**
   * Tests that a file can't be renamed when the new file is outside of
   * the root.
   */
  public void testRenameFileThrowsExceptionWhenNewFileOutsideOfRoot() throws Exception {
    try {
      assertNull(fileManager.renameFile("/", "afile.txt", "../afile.txt"));
      fail("Renaming a file outside of the root isn't allowed");
    } catch (IllegalFileAccessException ifae) {
    }
  }

  /**
   * Tests that a file can be renamed.
   */
  public void testRenameFile() throws Exception {
    File file = fileManager.getFile("/afile.txt");
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    writer.write("Testing...");
    writer.flush();
    writer.close();
    long length = file.length();

    File newFile = fileManager.renameFile("/", "afile.txt", "anewfile.txt");
    assertNotNull(newFile);
    assertFalse(file.exists());
    assertTrue(newFile.exists());
    assertEquals(length, newFile.length());

    // and clean up
    newFile.delete();
  }

  /**
   * Tests that a file can't be deleted outside of the root.
   */
  public void testDeleteFileThrowsExceptionWhenNewFileOutsideOfRoot() throws Exception {
    try {
      fileManager.deleteFile("/", "../afile.txt");
      fail("Deleting a file outside of the root isn't allowed");
    } catch (IllegalFileAccessException ifae) {
    }
  }

  /**
   * Tests that a file can be loaded.
   */
  public void testLoadFile() throws Exception {
    File file = fileManager.getFile("/afile.txt");
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    writer.write("First line");
    writer.newLine();
    writer.write("Second line");
    writer.flush();
    writer.close();

    StringBuffer expectedContent = new StringBuffer();
    expectedContent.append("First line");
    expectedContent.append(System.getProperty("line.separator"));
    expectedContent.append("Second line");

    String content = fileManager.loadFile("/", "afile.txt");
    assertEquals(expectedContent.toString(), content);

    // and clean up
    file.delete();
  }

  /**
   * Tests that a file can't be loaded from outside of the root.
   */
  public void testLoadFileThrowsExceptionWhenNewFileOutsideOfRoot() throws Exception {
    try {
      fileManager.loadFile("/", "../afile.txt");
      fail("Loading a file outside of the root isn't allowed");
    } catch (IllegalFileAccessException ifae) {
    }
  }

  /**
   * Tests that a file can be saved.
   */
  public void testSaveFile() throws Exception {
    StringBuffer content = new StringBuffer();
    content.append("First line");
    content.append(System.getProperty("line.separator"));
    content.append("Second line");

    fileManager.saveFile("/", "afile.txt", content.toString());
    assertEquals(content.toString(), fileManager.loadFile("/", "afile.txt"));

    // and clean up
    File file = fileManager.getFile("/afile.txt");
    file.delete();
  }

  /**
   * Tests that a file can't be saved outside of the root.
   */
  public void testSaveFileThrowsExceptionWhenNewFileOutsideOfRoot() throws Exception {
    try {
      fileManager.saveFile("/", "../afile.txt", "some content");
      fail("Saving a file outside of the root isn't allowed");
    } catch (IllegalFileAccessException ifae) {
    }
  }

  /**
   * Tests that files can be accessed.
   */
  public void testGetFiles() throws Exception {
    // create some files and directories
    fileManager.createDirectory("/", "a");
    fileManager.createDirectory("/", "z");
    fileManager.saveFile("/", "y.txt", "Some content");
    fileManager.saveFile("/", "b.txt", "Some content");

    List files = fileManager.getFiles("/");
    assertEquals(4, files.size());

    // the files should be in this order
    // - a, z, b.txt, y.txt (directories followed by files, both alphabetically)
    FileMetaData file = (FileMetaData)files.get(0);
    assertEquals("a", file.getName());
    assertEquals("/", file.getPath());
    assertTrue(file.isDirectory());
    file = (FileMetaData)files.get(1);
    assertEquals("z", file.getName());
    assertEquals("/", file.getPath());
    assertTrue(file.isDirectory());
    file = (FileMetaData)files.get(2);
    assertEquals("b.txt", file.getName());
    assertEquals("/", file.getPath());
    assertFalse(file.isDirectory());
    file = (FileMetaData)files.get(3);
    assertEquals("y.txt", file.getName());
    assertEquals("/", file.getPath());
    assertFalse(file.isDirectory());

    // and clean up
    fileManager.deleteFile("/", "a");
    fileManager.deleteFile("/", "z");
    fileManager.deleteFile("/", "y.txt");
    fileManager.deleteFile("/", "b.txt");
  }

  /**
   * Tests that files can be accessed recursively.
   */
  public void testGetFilesRecursively() throws Exception {
    // create some files and directories
    fileManager.createDirectory("/", "a");
    fileManager.saveFile("/a", "a.txt", "Some content");
    fileManager.saveFile("/", "b.txt", "Some content");

    List files = fileManager.getFiles("/", true);
    assertEquals(3, files.size());

    // the files should be in this order
    // - a, a/a.txt, b.txt (directories followed by files, alphabetically and recursively)
    FileMetaData file = (FileMetaData)files.get(0);
    assertEquals("a", file.getName());
    assertEquals("/", file.getPath());
    assertTrue(file.isDirectory());
    file = (FileMetaData)files.get(1);
    assertEquals("a.txt", file.getName());
    assertEquals("/a", file.getPath());
    assertFalse(file.isDirectory());
    file = (FileMetaData)files.get(2);
    assertEquals("b.txt", file.getName());
    assertEquals("/", file.getPath());
    assertFalse(file.isDirectory());

    // and clean up
    fileManager.deleteFile("/a", "a.txt");
    fileManager.deleteFile("/", "a");
    fileManager.deleteFile("/", "b.txt");
  }

  /**
   * Tests that files can be accessed when directory is empty.
   */
  public void testGetFilesFromEmptyDirectory() throws Exception {
    List files = fileManager.getFiles("/");
    assertEquals(0, files.size());
  }

  /**
   * Tests that files can be accessed from a non-existent directory.
   */
  public void testGetFilesFromNonExistentDirectory() throws Exception {
    // the theme path "/some/path" doesn't exist
    Theme theme = new Theme(blog, "custom", "/some/path");
    blog.setEditableTheme(theme);
    fileManager = new FileManager(blog, FileMetaData.THEME_FILE);
    List files = fileManager.getFiles("/");
    assertEquals(0, files.size());
  }

  /**
   * Tests that files can't be accessed outside of the root.
   */
  public void testGetFilesThrowsExceptionWhenNewFileOutsideOfRoot() throws Exception {
    try {
      fileManager.getFiles("../");
      fail("Accessing files outside of the root isn't allowed");
    } catch (IllegalFileAccessException ifae) {
    }
  }

  /**
   * Tests that the URLs for blog files are set correctly.
   */
  public void testUrlForBlogFile() throws Exception {
    fileManager = new FileManager(blog, FileMetaData.BLOG_FILE);
    fileManager.saveFile("/", "a.txt", "Some content");

    List files = fileManager.getFiles("/");
    FileMetaData file = (FileMetaData)files.get(0);
    assertEquals("files/a.txt", file.getUrl());

    // and clean up
    fileManager.deleteFile("/", "a.txt");
  }

  /**
   * Tests that the URLs for blog images are set correctly.
   */
  public void testUrlForBlogImage() throws Exception {
    fileManager = new FileManager(blog, FileMetaData.BLOG_IMAGE);
    fileManager.saveFile("/", "a.txt", "Some content");

    List files = fileManager.getFiles("/");
    FileMetaData file = (FileMetaData)files.get(0);
    assertEquals("images/a.txt", file.getUrl());

    // and clean up
    fileManager.deleteFile("/", "a.txt");
  }

  /**
   * Tests that the URLs for theme files are set correctly.
   */
  public void testUrlForThemeFile() throws Exception {
    Theme theme = new Theme(blog, "theme", blog.getRoot());
    File themeDirectory = new File(blog.getThemeDirectory());
    themeDirectory.mkdir();
    blog.setEditableTheme(theme);
    fileManager = new FileManager(blog, FileMetaData.THEME_FILE);
    fileManager.saveFile("/", "a.txt", "Some content");

    List files = fileManager.getFiles("/");
    FileMetaData file = (FileMetaData)files.get(0);
    assertEquals("theme/a.txt", file.getUrl());

    // and clean up
    fileManager.deleteFile("/", "a.txt");
    themeDirectory.delete();
  }

}
