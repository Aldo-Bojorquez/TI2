
package proyecto_abd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class Panel_Acceso extends javax.swing.JFrame {

    Conexion mysql=new Conexion();
    Connection cn=mysql.Conexion();
    String nombre,puesto;

    public Panel_Acceso() {
        initComponents();
    }
    
    public boolean comprobarUsuario(String contra,String id_empleado){
        boolean res=true;
        String SQLs="SELECT empleados.id_empleado,empleados.password,empleados.nombres,empleados.apellidos,tipo_empleados.puesto "
                + "FROM empleados,tipo_empleados "
                + "WHERE empleados.password='"+contra+"' "
                + "AND empleados.id_empleado='"+id_empleado+"'"
                + "AND empleados.id_tipo=tipo_empleados.id_tipo ;";
        
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            while(rs.next()){
                nombre=rs.getString(3)+" "+rs.getString(4); 
                puesto=rs.getString(5); 
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la contraseña o el id del usuario.\n"+ex,
                    "Error al cargar los datos del empleado",JOptionPane.ERROR_MESSAGE);
            res=false;
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JBaccesar = new javax.swing.JButton();
        JTFcalve = new javax.swing.JTextField();
        JPFpass = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Clave usuario:");

        jLabel2.setText("Contraseña:");

        JBaccesar.setText("Accesar");
        JBaccesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBaccesarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JTFcalve))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(28, 28, 28)
                                .addComponent(JPFpass, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(JBaccesar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JTFcalve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JPFpass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(JBaccesar)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JBaccesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBaccesarActionPerformed
        if(JTFcalve.getText().isEmpty()||JPFpass.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "No se pueden dejar los campos vacios",
                    "Error en los datos",JOptionPane.WARNING_MESSAGE);
        }else{
            if(comprobarUsuario(JPFpass.getText(),JTFcalve.getText())){
                Panel_Ventas pv=new Panel_Ventas();
                Panel_Ventas.JLusuario.setText(nombre);
                try{
                    if(!puesto.equals("Administrador(a)")){
                        Panel_Ventas.JMopcionesA.setEnabled(false);
                    }
                    pv.show();
                    dispose();
                }catch(NullPointerException e){
                    JOptionPane.showMessageDialog(null, "Error en la contraseña o el id del usuario.",
                    "Error al cargar los datos del empleado",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Error en la contraseña o el id del usuario.",
                    "Error al cargar los datos del empleado",JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_JBaccesarActionPerformed

    public static void main(String args[]) {
 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Panel_Acceso().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBaccesar;
    private javax.swing.JPasswordField JPFpass;
    private javax.swing.JTextField JTFcalve;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
