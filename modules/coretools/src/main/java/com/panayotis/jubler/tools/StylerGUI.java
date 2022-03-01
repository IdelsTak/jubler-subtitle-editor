/*
 * JStyler.java
 *
 * Created on 26 Ιούνιος 2005, 12:59 πμ
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

import javax.swing.JPanel;
import static com.panayotis.jubler.i18n.I18N.__;

/**
 *
 * @author teras
 */
public class StylerGUI extends JPanel {

    public StylerGUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        StyleSel = new javax.swing.JComboBox();

        setToolTipText("Select the color to use in order to mark the area");
        setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText(__("Style to use")+"  ");
        add(jLabel1, java.awt.BorderLayout.WEST);

        StyleSel.setToolTipText(__("Select the desired style from the drop down list"));
        add(StyleSel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JComboBox StyleSel;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
