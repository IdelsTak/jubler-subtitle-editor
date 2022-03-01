/*
 * StaticJubler.java
 *
 * Created on 9 Φεβρουάριος 2006, 9:56 μμ
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

package com.panayotis.jubler;

import com.panayotis.jubler.os.JIDialog;

import static com.panayotis.jubler.i18n.I18N.__;

import com.panayotis.jubler.information.JAbout;
import com.panayotis.jubler.options.Options;
import com.panayotis.jubler.options.gui.JUnsaved;
import com.panayotis.jubler.os.AutoSaver;
import com.panayotis.jubler.rmi.JublerServer;
import com.panayotis.jubler.subs.SubFile;
import com.panayotis.jubler.subs.Subtitles;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

/** @author teras */
public class StaticJubler {

  private static final int SCREEN_DELTAX = 24;
  private static final int SCREEN_DELTAY = 24;
  /* */
  private static Stack<SubFile> recentFiles;
  private static int screenX, screenY, screenWidth, screenHeight, screenState;

  static {
    loadWindowPosition();
    recentFiles = Options.loadFileList();
  }

  public static void setWindowPosition(JubFrame current_window, boolean save) {
    if (current_window == null) return;
    screenX = current_window.getX();
    screenY = current_window.getY();
    screenWidth = current_window.getWidth();
    screenHeight = current_window.getHeight();
    screenState = current_window.getExtendedState();
    if (save && screenWidth > 0) {
      String vals =
          "(("
              + screenX
              + ","
              + screenY
              + "),("
              + screenWidth
              + ","
              + screenHeight
              + "),"
              + screenState
              + ")";
      Options.setOption("System.WindowState", vals);
      Options.saveOptions();
    }
    jumpWindowPosition(true);
  }

  public static void putWindowPosition(JubFrame new_window) {
    if (screenWidth <= 0) return;

    new_window.setLocationByPlatform(false);
    new_window.setBounds(screenX, screenY, screenWidth, screenHeight);
    new_window.setExtendedState(screenState);
    jumpWindowPosition(true);
  }

  public static void jumpWindowPosition(boolean forth) {
    if (forth) {
      screenX += SCREEN_DELTAX;
      screenY += SCREEN_DELTAY;
    } else {
      screenX -= SCREEN_DELTAX;
      screenY -= SCREEN_DELTAY;
    }
  }

  public static void loadWindowPosition() {
    int[] values = new int[5];
    int pos = 0;

    for (int i = 0; i < 5; i++) values[i] = -1;

    String props = Options.getOption("System.WindowState", "");
    if (props != null && (!props.equals(""))) {
      StringTokenizer st = new StringTokenizer(props, "(),");
      while (st.hasMoreTokens() && pos < 5) values[pos++] = Integer.parseInt(st.nextToken());
    }
    screenX = values[0];
    screenY = values[1];
    screenWidth = values[2];
    screenHeight = values[3];
    screenState = values[4];
    if (screenWidth < 800) screenWidth = 800;
    if (screenHeight < 600) screenHeight = 600;
  }

  public static void showAbout() {
    JIDialog.about(JubFrame.windows.get(0), new JAbout(), __("About Jubler"), "jubler-logo.png");
  }

  public static boolean requestQuit(JubFrame request) {
    @SuppressWarnings("UseOfObsoleteCollectionType")
    java.util.Vector<String> unsaved = new java.util.Vector<>();
    for (JubFrame j : JubFrame.windows)
      if (j.isUnsaved()) unsaved.add(j.getSubtitles().getSubFile().getStrippedFile().getName());
    if (!unsaved.isEmpty())
      if (!JIDialog.question(null, new JUnsaved(unsaved), __("Quit Jubler"))) return false;

    JublerServer.stopServer();

    if (request == null && !JubFrame.windows.isEmpty())
      request = JubFrame.windows.get(JubFrame.windows.size() - 1);
    if (request != null) setWindowPosition(request, true);

    AutoSaver.cleanup();
    return true;
  }

  public static void updateMenus(JubFrame j) {
    JubFrame.prefs.setMenuShortcuts(j.JublerMenuBar);
  }

  public static void updateAllMenus() {
    for (JubFrame j : JubFrame.windows) updateMenus(j);
  }

  public static void updateRecents() {
    /* Get filenames of all files */
    Subtitles subs;
    for (JubFrame j : JubFrame.windows) {
      subs = j.getSubtitles();
      if (subs != null) {
        SubFile sfile = subs.getSubFile();
        if (sfile.exists()) {
          int which = recentFiles.indexOf(subs.getSubFile());
          if (which >= 0) {
            recentFiles.remove(which);
            recentFiles.push(subs.getSubFile());
          } else recentFiles.add(subs.getSubFile());
        }
      }
    }
    Options.saveFileList(recentFiles);

    /* Get filenames of closed files */
    Stack<SubFile> menulist = new Stack<>();
    menulist.addAll(recentFiles);
    for (JubFrame j : JubFrame.windows) {
      subs = j.getSubtitles();
      if (subs != null) menulist.remove(subs.getSubFile());
    }

    /* Update menus */
    JMenu recent_menu;
    for (JubFrame j : JubFrame.windows) {
      recent_menu = j.RecentsFM;

      /* Add clone entry */
      recent_menu.removeAll();
      if (j.getSubtitles() != null) {
        recent_menu.add(addNewMenu(__("Clone current"), true, true, j, -1));
        recent_menu.add(new JSeparator());
      }
      if (menulist.isEmpty())
        recent_menu.add(addNewMenu(__("-Not any recent items-"), false, false, j, -1));
      else {
        int counter = 1;
        for (int i = menulist.size() - 1; i >= 0; i--)
          recent_menu.add(
              addNewMenu(menulist.get(i).getSaveFile().getPath(), false, true, j, counter++));
      }
    }
  }

  private static JMenuItem addNewMenu(
      String text, boolean isclone, boolean enabled, JubFrame jub, int counter) {
    JMenuItem item = new JMenuItem(text);
    item.setEnabled(enabled);
    if (counter >= 0)
      item.setAccelerator(
          KeyStroke.getKeyStroke(
              KeyEvent.VK_0 + counter, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

    final boolean isclone_f = isclone;
    final String text_f = text;
    final JubFrame jubFrame = jub;
    item.addActionListener(
        ae -> {
          if (isclone_f) jubFrame.recentMenuCallback(null);
          else {
            SubFile prototype = new SubFile(new File(text_f), SubFile.EXTENSION_GIVEN);
            int where = recentFiles.indexOf(prototype);
            if (where >= 0) jubFrame.recentMenuCallback(recentFiles.get(where));
            else JIDialog.error(jubFrame, "Unable to load recent item", "Error");
          }
        });
    return item;
  }
}
