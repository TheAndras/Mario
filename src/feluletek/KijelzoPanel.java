/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feluletek;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import vezerles.Vezerlo;

/**
 *
 * @author thean
 */
public class KijelzoPanel extends javax.swing.JPanel {

    /**
    Márió halála esetén ezen a panelen lehet látni Márió megmaradt életét.
    A játék nehézsége miatt a szokásos 3 élet helyett, most végtelen életünk van (Vagyis inkább ameddig van türelme a játékosnak :) )
    */
    public KijelzoPanel() {
        initComponents();
        this.setVisible(false);
    }
    private int hatterX = 0, hatterY = 0;
    private Image hatterKep = new ImageIcon(this.getClass().getResource("/media/eletMutato.png")).getImage();
    private Vezerlo vezerlo;
    private int elet;

    public void setElet(int elet) {
        this.elet = elet;
        lblElet.setText(""+elet);
    }
    

    public void setVezerlo(Vezerlo vezerlo) {
        this.vezerlo = vezerlo;
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(hatterKep, hatterX, hatterY, this.getWidth(), this.getHeight(), null);
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblElet = new javax.swing.JLabel();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        lblElet.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblElet.setForeground(new java.awt.Color(255, 255, 255));
        lblElet.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblElet.setText("-100");
        lblElet.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(450, Short.MAX_VALUE)
                .addComponent(lblElet, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(294, 294, 294))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(277, 277, 277)
                .addComponent(lblElet, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(298, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
         panelValtas();
    }//GEN-LAST:event_formMouseClicked



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblElet;
    // End of variables declaration//GEN-END:variables

    public void panelValtas() {
        if(vezerlo!=null && this.isVisible()){
        vezerlo.panelValtas();
        }
    }
}