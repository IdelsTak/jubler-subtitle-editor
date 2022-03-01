/*
 * JVideofileSelector.java
 *
 * Created on 26 Ιούνιος 2005, 10:20 μμ
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

import com.panayotis.jubler.media.filters.AudioFileFilter;
import com.panayotis.jubler.media.filters.VideoFileFilter;
import com.panayotis.jubler.os.FileCommunicator;
import com.panayotis.jubler.os.SystemDependent;
import java.io.File;
import javax.swing.JFileChooser;

import static com.panayotis.jubler.i18n.I18N.__;

/**
 *
 * @author teras
 */
public class JVideofileSelector extends javax.swing.JPanel {

    private MediaFile mfile;
    private JFileChooser fdialog;
    private AudioFileFilter afilter;
    private VideoFileFilter vfilter;

    /**
     * Creates new form PlayerOptions
     */
    public JVideofileSelector() {
        initComponents();
        vfilter = new VideoFileFilter();
        afilter = new AudioFileFilter();

        fdialog = new JFileChooser();
        fdialog.addChoosableFileFilter(vfilter);
        fdialog.addChoosableFileFilter(afilter);
        fdialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fdialog.setSelectedFile(new File(FileCommunicator.getDefaultDirPath(), "."));
    }

    private void updateFiles() {
        VFName.setText(mfile.getVideoFile().getPath());

        if (mfile.getAudioFile().isSameAsVideo()) {
            AFName.setText("");
            AFName.setEnabled(false);
            ExternalAudioB.setSelected(false);
            AudioBrowse.setEnabled(false);
        } else {
            AFName.setText(mfile.getAudioFile().getPath());
            AFName.setEnabled(true);
            ExternalAudioB.setSelected(true);
            AudioBrowse.setEnabled(true);
        }

        CFName.setText(mfile.getCacheFile().getPath());
    }

    public void setEnabled(boolean status) {
        VideoBrowse.setEnabled(status);
        ExternalAudioB.setEnabled(status);
        if (status && mfile != null && mfile.getAudioFile() != null && (!mfile.getAudioFile().isSameAsVideo())) {
            AudioBrowse.setEnabled(true);
        } else {
            AudioBrowse.setEnabled(false);
        }
        CacheBrowse.setEnabled(status);
    }

    public void setMediaFile(MediaFile mfile) {
        this.mfile = mfile;
        updateFiles();
    }

    public File getAudioFileee() {
        if (ExternalAudioB.isSelected()) {
            return null; // afile
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AudioGroup = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        VFileL = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        VFName = new javax.swing.JTextField();
        VideoBrowse = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        AFName = new javax.swing.JTextField();
        AudioBrowse = new javax.swing.JButton();
        ExternalAudioB = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        CFName = new javax.swing.JTextField();
        CacheBrowse = new javax.swing.JButton();
        CFileL = new javax.swing.JLabel();

        setOpaque(false);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout());

        VFileL.setText(__("Use the following video file"));
        jPanel2.add(VFileL, java.awt.BorderLayout.NORTH);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.BorderLayout());

        VFName.setEditable(false);
        VFName.setColumns(40);
        VFName.setToolTipText(__("Filename of the video file. Use the \"Browse\" button to change it."));
        jPanel1.add(VFName, java.awt.BorderLayout.CENTER);

        VideoBrowse.setText(__("Browse"));
        VideoBrowse.setToolTipText(__("Change the video filename"));
        SystemDependent.setCommandButtonStyle(VideoBrowse, "only");
        VideoBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VideoBrowseActionPerformed(evt);
            }
        });
        jPanel1.add(VideoBrowse, java.awt.BorderLayout.EAST);

        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

        add(jPanel2);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 0, 0, 0));
        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.BorderLayout());

        AFName.setEditable(false);
        AFName.setColumns(40);
        AFName.setToolTipText(__("Filename of the external audio file. Use the \"Browse\" button to change it."));
        jPanel4.add(AFName, java.awt.BorderLayout.CENTER);

        AudioBrowse.setText(__("Browse"));
        AudioBrowse.setToolTipText(__("Change the external audio filename"));
        AudioBrowse.setEnabled(false);
        SystemDependent.setCommandButtonStyle(AudioBrowse, "only");
        AudioBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AudioBrowseActionPerformed(evt);
            }
        });
        jPanel4.add(AudioBrowse, java.awt.BorderLayout.EAST);

        ExternalAudioB.setText(__("Use a different audio stream"));
        ExternalAudioB.setToolTipText(__("Use an audio stream outside from this video file.\n(E.g. a WAV file or a previously cached audio stream)"));
        ExternalAudioB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExternalAudioBActionPerformed(evt);
            }
        });
        jPanel4.add(ExternalAudioB, java.awt.BorderLayout.NORTH);

        add(jPanel4);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout());

        CFName.setEditable(false);
        CFName.setColumns(40);
        CFName.setToolTipText(__("Filename of the previous audio cache. Use the \"Browse\" button to change it."));
        jPanel5.add(CFName, java.awt.BorderLayout.CENTER);

        CacheBrowse.setText(__("Browse"));
        CacheBrowse.setToolTipText(__("Change the audio cache filename"));
        SystemDependent.setCommandButtonStyle(CacheBrowse, "only");
        CacheBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CacheBrowseActionPerformed(evt);
            }
        });
        jPanel5.add(CacheBrowse, java.awt.BorderLayout.EAST);

        CFileL.setText(__("Filename of the cached audio stream"));
        jPanel5.add(CFileL, java.awt.BorderLayout.NORTH);

        add(jPanel5);
    }// </editor-fold>//GEN-END:initComponents

    private void CacheBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CacheBrowseActionPerformed
        fdialog.setSelectedFile(mfile.getCacheFile());
        afilter.setCheckForValidCache(mfile.getVideoFile());
        fdialog.setFileFilter(afilter);
        if (fdialog.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        FileCommunicator.setDefaultDir(fdialog.getCurrentDirectory());
        mfile.setCacheFile(fdialog.getSelectedFile());
        updateFiles();
    }//GEN-LAST:event_CacheBrowseActionPerformed

    private void AudioBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AudioBrowseActionPerformed
        fdialog.setSelectedFile(mfile.getAudioFile());
        afilter.setCheckForValidCache(null);
        fdialog.setFileFilter(afilter);
        if (fdialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            mfile.setAudioFile(fdialog.getSelectedFile());
            FileCommunicator.setDefaultDir(fdialog.getCurrentDirectory());
        }
        updateFiles();
    }//GEN-LAST:event_AudioBrowseActionPerformed

    private void ExternalAudioBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExternalAudioBActionPerformed
        if (ExternalAudioB.isSelected()) {
            AudioBrowseActionPerformed(evt);
        } else {
            mfile.setAudioFileUnused();
            updateFiles();
        }
    }//GEN-LAST:event_ExternalAudioBActionPerformed

    private void VideoBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VideoBrowseActionPerformed
        fdialog.setSelectedFile(mfile.getVideoFile());
        fdialog.setFileFilter(vfilter);
        if (fdialog.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        FileCommunicator.setDefaultDir(fdialog.getCurrentDirectory());
        mfile.setVideoFile(fdialog.getSelectedFile());
        updateFiles();
    }//GEN-LAST:event_VideoBrowseActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AFName;
    private javax.swing.JButton AudioBrowse;
    private javax.swing.ButtonGroup AudioGroup;
    private javax.swing.JTextField CFName;
    private javax.swing.JLabel CFileL;
    private javax.swing.JButton CacheBrowse;
    private javax.swing.JCheckBox ExternalAudioB;
    private javax.swing.JTextField VFName;
    private javax.swing.JLabel VFileL;
    private javax.swing.JButton VideoBrowse;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables
}
