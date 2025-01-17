/*
 * JFixer.java
 *
 * Created on 5 Ιούλιος 2005, 12:34 μμ
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
package com.panayotis.jubler.tools;

import com.panayotis.jubler.os.SystemDependent;
import java.awt.BorderLayout;
import com.panayotis.jubler.time.gui.JDuration;
import java.text.DecimalFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import static com.panayotis.jubler.i18n.I18N.__;

/**
 *
 * @author teras
 */
public class FixerGUI extends JPanel {

    JDuration mintime, maxtime;

    public FixerGUI() {
        initComponents();
        mintime = new JDuration();
        MinTimeP.add(mintime, BorderLayout.CENTER);
        maxtime = new JDuration();
        MaxTimeP.add(maxtime, BorderLayout.CENTER);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        SortB = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        FixT = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        PushModelB = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        GapB = new javax.swing.JCheckBox();
        GapNum = new JFormattedTextField( new DecimalFormat("#######") );
        jPanel4 = new javax.swing.JPanel();
        MinTimeP = new javax.swing.JPanel();
        MaxTimeP = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jPanel6.setLayout(new java.awt.BorderLayout());

        SortB.setSelected(true);
        SortB.setText(__("Sort first  (strongly recommended)"));
        jPanel6.add(SortB, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel6);

        jPanel7.setLayout(new java.awt.BorderLayout());

        FixT.setSelected(true);
        FixT.setText(__("Prevent overlapping"));
        FixT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FixTActionPerformed(evt);
            }
        });
        jPanel7.add(FixT, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel7);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1));
        jPanel2.setLayout(new java.awt.BorderLayout());

        PushModelB.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
            __("Evenly distribute subtitles"),
            __("Equally divide overriding duration"),
            __("Shift subtitles")
        }));
        PushModelB.setToolTipText(__("Model how to solve overriding subtitles"));
        jPanel2.add(PushModelB, java.awt.BorderLayout.PAGE_END);

        jPanel3.add(jPanel2);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1));
        jPanel5.setLayout(new java.awt.BorderLayout());

        GapB.setText(__("Leave gap between subtitles (in milliseconds)") + "  ");
        GapB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GapBActionPerformed(evt);
            }
        });
        jPanel5.add(GapB, java.awt.BorderLayout.WEST);

        GapNum.setText("100");
        GapNum.setEnabled(false);
        jPanel5.add(GapNum, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel5);

        add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        MinTimeP.setBorder(SystemDependent.getBorder(__("Minimum subtitle duration")));
        MinTimeP.setLayout(new java.awt.BorderLayout());
        jPanel4.add(MinTimeP);

        MaxTimeP.setBorder(SystemDependent.getBorder(__("Maximum subtitle duration")));
        MaxTimeP.setLayout(new java.awt.BorderLayout());
        jPanel4.add(MaxTimeP);

        add(jPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void GapBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GapBActionPerformed
        GapNum.setEnabled(GapB.isSelected());
    }//GEN-LAST:event_GapBActionPerformed

    private void FixTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FixTActionPerformed
        boolean en = FixT.isSelected();
        PushModelB.setEnabled(en);
        GapB.setEnabled(en);

        if (en) {
            GapNum.setEnabled(GapB.isSelected());
        } else {
            GapNum.setEnabled(false);
        }
    }//GEN-LAST:event_FixTActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JCheckBox FixT;
    javax.swing.JCheckBox GapB;
    public javax.swing.JFormattedTextField GapNum;
    private javax.swing.JPanel MaxTimeP;
    private javax.swing.JPanel MinTimeP;
    javax.swing.JComboBox PushModelB;
    javax.swing.JCheckBox SortB;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    // End of variables declaration//GEN-END:variables
}
