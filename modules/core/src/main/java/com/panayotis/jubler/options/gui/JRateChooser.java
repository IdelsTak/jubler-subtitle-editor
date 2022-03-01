/*
 * JRateChooser.java
 *
 * Created on November 30, 2006, 1:12 PM
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
package com.panayotis.jubler.options.gui;

import com.panayotis.jubler.JubFrame;
import static com.panayotis.jubler.i18n.I18N.__;
import com.panayotis.jubler.media.MediaFile;
import com.panayotis.jubler.os.JIDialog;
import com.panayotis.jubler.os.SystemDependent;
import com.panayotis.jubler.plugins.Theme;
import com.panayotis.jubler.subs.SubFile;
import com.panayotis.jubler.subs.Subtitles;
import javax.swing.JPanel;

/**
 *
 * @author teras
 */
public class JRateChooser extends JPanel {

    private MediaFile mfile = null;
    private Subtitles subs = null;

    /**
     * Creates new form JRateChooser
     */
    public JRateChooser() {
        initComponents();
    }

    public float getFPSValue() {
        try {
            return Float.parseFloat(getFPS());
        } catch (NumberFormatException e) {
        }
        return SubFile.getDefaultFPS();
    }

    public void setDataFiles(MediaFile m, Subtitles s) {
        mfile = m;
        subs = s;
    }

    public void setFPS(float fps) {
        FPSChooser.setSelectedItem(fps);
    }

    public String getFPS() {
        return FPSChooser.getSelectedItem().toString();
    }

    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        FPSChooser.setEnabled(value);
        FromFPSB.setEnabled(value);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FromFPSB = new javax.swing.JButton();
        FPSChooser = new javax.swing.JComboBox();

        setLayout(new java.awt.BorderLayout());

        FromFPSB.setIcon(Theme.loadIcon("videofile.png"));
        FromFPSB.setToolTipText(__("Get FPS from the video file"));
        SystemDependent.setCommandButtonStyle(FromFPSB, "only");
        FromFPSB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FromFPSBFPSBActionPerformed(evt);
            }
        });
        add(FromFPSB, java.awt.BorderLayout.EAST);

        FPSChooser.setEditable(true);
        FPSChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "15", "20", "23.976", "24", "25", "29.97", "30" }));
        FPSChooser.setSelectedItem("25");
        FPSChooser.setToolTipText(__("Frames per second"));
        FPSChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FPSChooserActionPerformed(evt);
            }
        });
        add(FPSChooser, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void FPSChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FPSChooserActionPerformed
        String action = evt.getActionCommand().trim();
        if (action.equals("")) {
            return;
        }
        if (action.startsWith("combo")) {
            return;
        }
        try {
            Float.parseFloat(action);
            FPSChooser.setSelectedItem(action);
        } catch (NumberFormatException e) {
            JIDialog.error(this, "Not a valid number: {0}", __("Wrong FPS"));
        }
    }//GEN-LAST:event_FPSChooserActionPerformed

    private void FromFPSBFPSBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FromFPSBFPSBActionPerformed
        if (mfile == null) {
            return;
        }
        if (!mfile.validateMediaFile(subs, false, JubFrame.windows.get(0))) {
            return;
        }

        float fps = mfile.getVideoFile().getFPS();
        if (fps > 0) {
            FPSChooser.setSelectedItem(fps);
        }
    }//GEN-LAST:event_FromFPSBFPSBActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox FPSChooser;
    private javax.swing.JButton FromFPSB;
    // End of variables declaration//GEN-END:variables
}
