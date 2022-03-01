/*
 * MediaFile.java
 *
 * Created on November 30, 2006, 4:14 PM
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

import com.panayotis.jubler.os.JIDialog;
import com.panayotis.jubler.media.filters.VideoFileFilter;
import com.panayotis.jubler.media.preview.decoders.DecoderInterface;
import com.panayotis.jubler.media.preview.decoders.AudioPreview;
import com.panayotis.jubler.media.preview.decoders.DecoderListener;
import com.panayotis.jubler.media.preview.decoders.FFMPEG;
import com.panayotis.jubler.os.SystemDependent;
import com.panayotis.jubler.subs.Subtitles;
import java.awt.Frame;
import java.awt.Image;
import java.io.File;
import java.util.Objects;

/** @author teras */
public class MediaFile {

  private VideoFile videoFile; /* Video file */

  private AudioFile audioFile; /* Audio file - possibly same as video file */

  private CacheFile cacheFile; /* Cache file */

  /* Decoder framework to display frames, audio clips etc. */
  private DecoderInterface decoder;
  /** File chooser dialog for video */
  public JVideofileSelector videoselector;

  /** Creates a new instance of MediaFile */
  public MediaFile() {
    this(null, null, null);
  }

  public MediaFile(MediaFile mediaFile) {
    this(mediaFile.videoFile, mediaFile.audioFile, mediaFile.cacheFile);
  }

  public MediaFile(VideoFile videoFile, AudioFile audioFile, CacheFile cacheFile) {
    this.videoFile = videoFile;
    this.audioFile = audioFile;
    this.cacheFile = cacheFile;
    
    decoder = new FFMPEG();
    videoselector = new JVideofileSelector();
  }

  public boolean validateMediaFile(Subtitles subs, boolean force_new, Frame frame) {
    if ((!force_new) && isValid(videoFile)) return true;

    VideoFile old_v = videoFile;
    AudioFile old_a = audioFile;
    CacheFile old_c = cacheFile;

    /* Guess files from subtitle file - only for initialization */
    guessMediaFiles(subs);

    /* Now let the user select which files are the proper media files */
    boolean isok;
    do {
      if (!JIDialog.action(frame, videoselector, __("Select video"))) {
        videoFile = old_v;
        audioFile = old_a;
        cacheFile = old_c;
        return false;
      }
      isok = isValid(videoFile);
      if (!isok)
        JIDialog.warning(
            null,
            __("This file does not exist.\nPlease provide a valid file name."),
            __("Error in videofile selection"));
    } while (!isok);

    return true;
  }

  private boolean isValid(File f) {
    return (f != null && f.exists());
  }

  public void guessMediaFiles(Subtitles subs) {
    if (!isValid(videoFile)) {
      videoFile = VideoFile.guessFile(subs, new VideoFileFilter(), decoder);
      if (!isValid(audioFile)) setAudioFileUnused();
      if (!isValid(cacheFile)) updateCacheFile(audioFile);
    }
    videoselector.setMediaFile(this);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof MediaFile) {
      MediaFile m = (MediaFile) o;

      /* We have to do all these tests to prevent null pointer exceptions */
      if (videoFile == null && m.videoFile != null) return false;
      if (!(videoFile == m.videoFile || videoFile.equals(m.videoFile))) return false;

      if (audioFile == null && m.audioFile != null) return false;
      if (!(audioFile == m.audioFile || audioFile.equals(m.audioFile))) return false;

      if (cacheFile == null && m.cacheFile != null) return false;
      return cacheFile == m.cacheFile || cacheFile.equals(m.cacheFile);
    }
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 41 * hash + Objects.hashCode(this.videoFile);
    hash = 41 * hash + Objects.hashCode(this.audioFile);
    hash = 41 * hash + Objects.hashCode(this.cacheFile);
    return hash;
  }

  public VideoFile getVideoFile() {
    return videoFile;
  }

  public AudioFile getAudioFile() {
    return audioFile;
  }

  public CacheFile getCacheFile() {
    return cacheFile;
  }

  public DecoderInterface getDecoder() {
    return decoder;
  }

  public void setVideoFile(File vf) {
    if (vf == null || (!vf.exists())) return;

    videoFile = new VideoFile(vf, decoder);

    if (audioFile.isSameAsVideo()) setAudioFile(videoFile);
  }

  public void setAudioFile(File af) {
    if (af == null || (!af.exists())) return;

    audioFile = new AudioFile(af, videoFile);
    updateCacheFile(audioFile);
  }

  public void setCacheFile(File cf) {
    if (cf == null) return;
    updateCacheFile(cf);

    /* Set audio file, from the cache file */
    String audioname = AudioPreview.getNameFromCache(cf);
    if (audioname != null) {
      AudioFile newafile = new AudioFile(cf.getParent(), audioname, videoFile);
      if (newafile.exists()) audioFile = newafile;
    }
  }

  private void updateCacheFile(File cf) {
    if (cf == null) return;

    /* Find a write enabled cache file */
    if (!(SystemDependent.canWrite(cf.getParentFile())
        && ((!cf.exists()) || SystemDependent.canWrite(cf)))) {
      String strippedfilename = cf.getName();
      int point = strippedfilename.lastIndexOf('.');
      if (point < 0) point = strippedfilename.length();
      cf =
          new File(
              System.getProperty("java.io.tmpdir")
                  + File.separator
                  + strippedfilename.substring(0, point)
                  + AudioPreview.getExtension());
    } else {
      int point = cf.getPath().lastIndexOf('.');
      if (point < 0) point = cf.getPath().length();
      cf = new File(cf.getPath().substring(0, point) + AudioPreview.getExtension());
    }
    if (cacheFile != null && cacheFile.getPath().equals(cf.getPath())) return; // Same cache

    closeAudioCache(); // Close old cache file, if exists
    cacheFile = new CacheFile(cf.getPath());
  }

  public void setAudioFileUnused() {
    audioFile = new AudioFile(videoFile, videoFile);
    updateCacheFile(videoFile);
  }

  /* Decoder actions */
  public boolean initAudioCache(DecoderListener listener) {
    return decoder.initAudioCache(audioFile, cacheFile, listener);
  }

  public AudioPreview getAudioPreview(double from, double to) {
    return decoder.getAudioPreview(cacheFile, from, to);
  }

  public void closeAudioCache() {
    if (cacheFile != null) decoder.closeAudioCache(cacheFile);
  }

  public Image getFrame(double time, float resize) {
    if (videoFile == null) return null;
    return decoder.getFrame(videoFile, time, resize);
  }

  public void playAudioClip(double from, double to) {
    if (audioFile != null) decoder.playAudioClip(audioFile, from, to);
  }

  public void interruptCacheCreation(boolean status) {
    decoder.setInterruptStatus(status);
  }
}
