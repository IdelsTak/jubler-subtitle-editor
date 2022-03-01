/*
 * HelpBrowser.java
 *
 * Created on 6 Νοέμβριος 2006, 2:07 μμ
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
package com.panayotis.jubler.information;

import static com.panayotis.jubler.i18n.I18N.__;

import com.panayotis.jubler.os.DEBUG;
import com.panayotis.jubler.os.SystemDependent;
import com.panayotis.jubler.os.SystemFileFinder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * @author teras
 */
public class HelpBrowser extends javax.swing.JDialog {

    private ArrayList<String> history;

    /**
     * Creates new form HelpBrowser
     */
    public HelpBrowser(String start) {
        super((JFrame) null, false);
        initComponents();

        history = new ArrayList<>();

        String initpage = new File(SystemFileFinder.AppPath.getAbsolutePath(), "help/jubler-faq.html").toURI().toString();
        setPage(initpage);
        history.add(initpage);

        HelpPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    String currentURL = evt.getURL().toString();
                    setPage(currentURL);
                    if (!currentURL.startsWith("http")) {
                        history.add(currentURL);
                    }
                    BackB.setEnabled(true);
                }
            }
        });
    }

    private void setPage(String url) {
        try {
            if (url.startsWith("http")) {
                Desktop.getDesktop().browse(new URI(url));
                return;
            }
            HelpPane.setPage(url);
        } catch (IOException | URISyntaxException e) {
            DEBUG.debug("Error while opening FAQ file \"" + url + "\" : " + e.getClass().getName());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        BackB = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        HelpPane = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Jubler FAQ");

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 8, 8));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        BackB.setText(__("Back"));
        BackB.setToolTipText(__("Go to previous page"));
        BackB.setEnabled(false);
        BackB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackBActionPerformed(evt);
            }
        });
        jPanel2.add(BackB);

        jPanel1.add(jPanel2, java.awt.BorderLayout.WEST);

        jPanel3.add(jPanel1, java.awt.BorderLayout.NORTH);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(400, 300));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 300));

        HelpPane.setEditable(false);
        jScrollPane1.setViewportView(HelpPane);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BackBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackBActionPerformed
        int last = history.size() - 1;
        history.remove(last);
        setPage(history.get(last - 1));
        if (last < 2) {
            BackB.setEnabled(false);
        }
    }//GEN-LAST:event_BackBActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BackB;
    private javax.swing.JEditorPane HelpPane;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
