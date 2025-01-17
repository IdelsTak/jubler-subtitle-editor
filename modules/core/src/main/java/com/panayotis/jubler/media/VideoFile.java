/*
 * VideoFile.java
 *
 * Created on August 5, 2007, 12:04 PM
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

package com.panayotis.jubler.media;

import static com.panayotis.jubler.i18n.I18N.__;

import com.panayotis.jubler.media.filters.MediaFileFilter;
import com.panayotis.jubler.media.preview.decoders.DecoderInterface;
import com.panayotis.jubler.options.Options;
import com.panayotis.jubler.os.DEBUG;
import com.panayotis.jubler.os.FileCommunicator;
import com.panayotis.jubler.subs.Subtitles;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/** @author teras */
public class VideoFile extends File {

  /* Default video properties */
  private static final int DEFAULT_WIDTH = 320;
  private static final int DEFAULT_HEIGHT = 288;
  private static final int DEFAULT_LENGTH = 60;
  private static final int DEFAULT_FPS = 25;
  private static final int INVALID = -1;
  /* Various video file properties */
  private int width = INVALID;
  private int height = INVALID;
  private float length = INVALID;
  private float fps = INVALID;

  /**
   * Creates a new instance of VideoFile
   *
   * @param videoFilePath
   * @param decoder
   */
  public VideoFile(String videoFilePath, DecoderInterface decoder) {
    super(videoFilePath);
    getVideoProperties(decoder);
  }

  public VideoFile(File vf, DecoderInterface decoder) {
    this(vf.getPath(), decoder);
  }

  public void setInformation(int width, int height, float length, float fps) {
    this.width = width;
    this.height = height;
    this.length = length;
    this.fps = fps;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public float getLength() {
    return length;
  }

  public float getFPS() {
    return fps;
  }

  public void getVideoProperties(DecoderInterface decoder) {
    if (decoder != null) decoder.retrieveInformation(this);
    if (width < 0) {

      /* Use MPlayer if no decoder is valid */
      String cmd[] = {
        Options.getOption("Player.MPlayer.Path", "mplayer"),
        "-vo",
        "null",
        "-ao",
        "null",
        "-identify",
        "-endpos",
        "0",
        getPath()
      };
      Process proc;
      try {
        proc = Runtime.getRuntime().exec(cmd);
        BufferedReader infopipe = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        while ((line = infopipe.readLine()) != null) {
          if (line.startsWith("ID_VIDEO_HEIGHT"))
            height = Math.round(getValue(line.substring(line.indexOf('=') + 1)));
          if (line.startsWith("ID_VIDEO_WIDTH"))
            width = Math.round(getValue(line.substring(line.indexOf('=') + 1)));
          if (line.startsWith("ID_VIDEO_FPS"))
            fps = getValue(line.substring(line.indexOf('=') + 1));
          if (line.startsWith("ID_LENGTH")) {
            length = getValue(line.substring(line.indexOf('=') + 1));
            break;
          }
        }
        proc.destroy();
      } catch (IOException ex) {
        length = fps = height = width = INVALID;
      }
    }
    if (width < 0) {
      height = DEFAULT_HEIGHT;
      width = DEFAULT_WIDTH;
      length = DEFAULT_LENGTH;
      fps = DEFAULT_FPS;
      DEBUG.debug("Could not retrieve actual video properties. Using defaults.");
    }
  }

  private static float getValue(String info) {
    try {
      return Float.parseFloat(info);
    } catch (NumberFormatException e) {
    }
    return 0;
  }

  /**
   * The following function is used in order to guess the filename of the avi/audio/jacache based on
   * the name of the original file.
   *
   * @param subs
   * @param filter
   * @param decoder
   * @return
   */
  public static VideoFile guessFile(
      Subtitles subs, MediaFileFilter filter, DecoderInterface decoder) {
    // the parent directory of the subtitle
    File dir;
    // List of video files in the same directory as the subtitle
    File files[];
    // best match so far
    int matchCount;
    // best file match so far
    File match;
    // Subtitles filename (in lowercase) & file in the same director
    String subFilename, curFilename;
    int size;
    int i, j;

    File subFile;
    if (subs == null || subs.getSubFile().getStrippedFile() == null)
      subFile = new File(FileCommunicator.getDefaultDirPath() + __("Untitled"));
    else subFile = subs.getSubFile().getStrippedFile();

    dir = subFile.getParentFile();
    if (dir == null)
      return new VideoFile(subFile.getPath() + "." + filter.getExtensions()[0], decoder);

    subFilename = subFile.getPath().toLowerCase();

    // From a list of possible filenames,
    // get the one with the best match
    matchCount = 0;
    match = null;
    files = dir.listFiles(filter);
    if (files != null) {
      for (i = 0; i < files.length; i++)
        if (!files[i].isDirectory()) {
          j = 0;
          curFilename = files[i].getPath().toLowerCase();
          size =
              (subFilename.length() > curFilename.length())
                  ? curFilename.length()
                  : subFilename.length();
          while (j < size && subFilename.charAt(j) == curFilename.charAt(j)) j++;
          if (matchCount < j) {
            matchCount = j;
            match = files[i];
          }
        }
      if (match != null) return new VideoFile(match.getPath(), decoder);
    }
    return new VideoFile(subFile.getPath() + filter.getExtensions()[0], decoder);
  }
}
