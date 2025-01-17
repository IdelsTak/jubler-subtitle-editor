/*
 * SystemFileFinder.java
 *
 * Created on 6 Νοέμβριος 2006, 3:25 πμ
 *
 * This file is part of Jubler.
 *
 * Jubler is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2.
 *
 *
 * Jubler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jubler; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package com.panayotis.jubler.os;

import com.panayotis.jubler.JubFrame;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/** @author teras */
public class SystemFileFinder {

  private static final boolean isJarBased;
  public static final File AppPath;

  static {
    boolean jarBased;
    File aPath;
    try {
      File classpath =
          new File(
              JubFrame.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
      jarBased = classpath.isFile();
      aPath = jarBased ? classpath.getParentFile() : classpath;
    } catch (URISyntaxException e) {
      jarBased = false;
      aPath = new File("");
    }
    isJarBased = jarBased;
    AppPath = aPath;
  }

  private static File findFile(String name) {
    File current = new File(AppPath, name);
    return current.exists() ? current : null;
  }

  public static boolean loadLibrary(String name) {
    if (loadLibraryImpl(name) || loadLibraryImpl(name + "_32") || loadLibraryImpl(name + "_64"))
      return true;
    DEBUG.debug("Unable to locate library " + name);
    return false;
  }

  private static boolean loadLibraryImpl(String name) {
    File libfile = findFile("lib" + File.separator + SystemDependent.mapLibraryName(name));
    if (libfile != null)
      try {
        System.load(libfile.getAbsolutePath());
        return true;
      } catch (UnsatisfiedLinkError e) {
        System.err.println(e.toString());
      }
    return false;
  }
}
