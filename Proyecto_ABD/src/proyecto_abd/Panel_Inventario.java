
package proyecto_abd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

public class Panel_Inventario extends javax.swing.JFrame {

    JMenuItem Omi;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DefaultTableModel modeloI = new DefaultTableModel();
    DefaultTableModel modeloP = new DefaultTableModel();
    Conexion mysql=new Conexion();
    Connection cn=mysql.Conexion();
    String registrosI[]=new String[5];

    public Panel_Inventario() {
        initComponents();
        String[] titulosInventario={"Id inventario","Nombre","Medida","Precio","Fecha de Caducidad"};
        modeloI.setColumnIdentifiers(titulosInventario);
        JTablaInventario.setModel(modeloI);
        String[] titulosProductos={"Id producto","Nombre producto","Tipo producto","Precio"};
        modeloP.setColumnIdentifiers(titulosProductos);
        JTablaProductos.setModel(modeloP);
        JTFcantidad.setEnabled(false);
        JDCfechaCadu.setEnabled(false);
        JTFnombre.setEnabled(false);
        JBagregar.setEnabled(false);
        JBmodificar.setEnabled(false);
        JTFprecio.setEnabled(false);
    }

    //Busqueda inventario
    public boolean buscarInventario(){
        limpiarTabla(modeloI, JTablaInventario);
        
        //cuerpo del boton
        
        String barraBusqueda=JTFbuscarInv.getText();
        boolean resul;
            String SQLs1="SELECT * FROM inventario WHERE nombre LIKE '%"+barraBusqueda+"%';";

            try{
                Statement st=cn.createStatement();
                ResultSet rs=st.executeQuery(SQLs1);
                while(rs.next()){
                    registrosI[0]=rs.getString(1);
                    registrosI[1]=rs.getString(2);
                    registrosI[2]=rs.getString(3);
                    registrosI[3]=rs.getString(4);
                    registrosI[4]=rs.getString(5);

                    modeloI.addRow(registrosI);
                }
                JTablaInventario.setModel(modeloI);
                int row=JTablaInventario.getRowCount();
                resul = row > 0;
            }catch(SQLException ex){
                //JOptionPane.showMessageDialog(null, ex);
                resul=false;
            }
                
        return resul;
    }
    
    //Desplegar todo el inventario
    public void mostrarTodoInventario(){
        String [] registros=new String[5];
        
        //limpiar tabla
        limpiarTabla(modeloI, JTablaInventario);
        
        String sSQL="SELECT * FROM inventario";
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(sSQL);
            
            while(rs.next()){
                
                registros[0]=rs.getString(1);
                registros[1]=rs.getString(2);
                registros[2]=rs.getString(3);
                registros[3]=rs.getString(4);
                registros[4]=rs.getString(5);
                
                modeloI.addRow(registros);
            }
            JTablaInventario.setModel(modeloI);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //Agregar inventario
    public void agregarInventario(){
        String nombre = null, cantidad=null, fechacadu=null;
        float precio=0;

        nombre=JTFnombre.getText();
        cantidad=JTFcantidad.getText();
        precio=Float.parseFloat(JTFprecio.getText());
        fechacadu=sdf.format(JDCfechaCadu.getDate());
        String SQLi=" INSERT INTO inventario VALUES (NULL,'"+nombre+"','"+cantidad+"','"+precio+"','"+fechacadu+"'); " ;
        try{
            PreparedStatement pst = cn.prepareStatement(SQLi);
            pst.execute();
            
            JLmensajeI.setText("Se han insertado los datos de manera correcta.");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e); 
        }
        JTFcantidad.setText("");
        JTFnombre.setText("");
        JDCfechaCadu.setDate(null);
        
        JBbuscarInventario.setEnabled(true);
        JTFbuscarInv.setEnabled(true);
        JTFcantidad.setEnabled(false);
        JDCfechaCadu.setEnabled(false);
        JTFnombre.setEnabled(false);
        JBagregar.setEnabled(false);
    }
    
    //Modificar inventario
    public void actualizarInventario(int fila){
        
        String fechacadu=sdf.format(JDCfechaCadu.getDate());
        String id=JTablaInventario.getValueAt(fila, 0).toString();
        float precio=Float.parseFloat(JTFprecio.getText());
        String SQLa="UPDATE inventario "
                + "SET nombre = '"+JTFnombre.getText()+"', medida = '"+JTFcantidad.getText()+"', fecha_caducidad = '"+fechacadu+"',"
                + " precio='"+precio+"' "
                + "WHERE inventario.id_inventario = '"+id+"';";
        try {
            PreparedStatement pps;
            pps = cn.prepareStatement(SQLa);
            pps.executeUpdate();
            JLmensajeI.setText("El producto se a modificado");
        } catch (SQLException ex) {
            JLmensajeI.setText("Ocurrio un error en la actualizacion");
            JOptionPane.showMessageDialog(null, ex, "Error MySQL", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    //metodo para eliminar del inventario
    public void bajaInventario(int filaS){
        String id=JTablaInventario.getValueAt(filaS, 0).toString();
        String SQLe="DELETE FROM inventario WHERE inventario.id_inventario = '"+id+"'";
        try {
            PreparedStatement pps = cn.prepareStatement(SQLe);
            pps.executeUpdate();
            JLmensajeI.setText("Se ha eliminado el registro con exito");
            mostrarTodoInventario();
        } catch (SQLException ex) {
            JLmensajeI.setText("Error en eliminar el registro");
        }
    }
    
     //metodo para mostrar todos los productos
    public void mostrarTodoProductos(){
        String registros[]=new String [4];
        //limpiar tabla
        limpiarTabla(modeloP, JTablaProductos);
        
        String SQLs="SELECT productos.id_producto, inventario.nombre, tipo_productos.tipo_producto, inventario.precio "
                + "FROM productos, tipo_productos, inventario "
                + "WHERE productos.id_tipo_producto = tipo_productos.id_tipo_producto "
                + "AND inventario.id_inventario = productos.id_inventario ;";
        
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            while(rs.next()){
                
                registros[0]=rs.getString(1);
                registros[1]=rs.getString(2);
                registros[2]=rs.getString(3);
                registros[3]=rs.getString(4);
                
                modeloP.addRow(registros);
            }
            JTablaProductos.setModel(modeloP);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //metodo para buscar productos
    public void buscarProductos(String entrada,DefaultTableModel modeloE,JTable tablaE){
        String registros[]=new String [4];
        String SQLs="SELECT productos.id_producto,inventario.nombre,tipo_productos.tipo_producto, inventario.precio "
                + "FROM productos, tipo_productos, inventario "
                + "WHERE productos.nombre LIKE '%"+entrada+"%' "
                + "AND productos.id_tipo_producto = tipo_productos.id_tipo_producto "
                + "AND inventario.id_inventario = productos.id_inventario ;";
        //limpiar tabla
        limpiarTabla(modeloE, tablaE);
        
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            while(rs.next()){
                registros[0]=rs.getString(1);
                registros[1]=rs.getString(2);
                registros[2]=rs.getString(3);
                registros[3]=rs.getString(4);

                modeloE.addRow(registros);
            }
            tablaE.setModel(modeloE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //metodo para agregar productos
    public void agregarProducto(String id_inv,int id_tipo){
         String SQLi=" INSERT INTO productos VALUES (NULL,'"+id_tipo+"','"+id_inv+"'); " ;
         
        try{
            PreparedStatement pst = cn.prepareStatement(SQLi);
            pst.execute();
            
            JLmensajeP.setText("Se han insertado los datos de manera correcta.");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e); 
        }
    }
    
    //metodo para la actualizacion del producto
    public void actualizarProducto(String id_pro,int id_tipo){
        String SQLa="UPDATE productos SET "
                + "id_tipo_producto = '"+id_tipo+"' "
                + "WHERE id_producto = '"+id_pro+"'";
        
        try {
            PreparedStatement pps;
            pps = cn.prepareStatement(SQLa);
            pps.executeUpdate();
            JLmensajeP.setText("El producto se a modificado");
        } catch (SQLException ex) {
            JLmensajeP.setText("Ocurrio un error en la actualizacion");
            JOptionPane.showMessageDialog(null, ex, "Error MySQL", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    //metodo para dar de baja el producto
    public void bajaProducto(String id){
        String SQLe="DELETE FROM prodcutos WHERE id_producto = '"+id+"'";
        try {
            PreparedStatement pps = cn.prepareStatement(SQLe);
            pps.executeUpdate();
            JLmensajeI.setText("Se ha eliminado el registro con exito");
            mostrarTodoInventario();
        } catch (SQLException ex) {
            JLmensajeI.setText("Error en eliminar el registro");
        }
    }
    
    //metodo para desplegar los items en el combo box
    public void obtenerItemsJCB(){
        String SQLs="SELECT * FROM tipo_productos WHERE id_tipo_producto != 6;";
        JCBproductos.removeAllItems();
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            JCBproductos.addItem("Seleccione tipo produto");
            while(rs.next()){
                JCBproductos.addItem(rs.getString(2));
            }
             
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //metodo para obtener los paquetes y poder agregar los productos
    public void obtenerPaquetes(){
        String SQLs="SELECT * FROM paquetes;";
        JMpaquetes.removeAll();
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            while(rs.next()){
                JMpaquetes.add(Omi = new JMenuItem(rs.getString(2)));
            }
             
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //metodo para el limpiado de todas las tablas
    public void limpiarTabla(DefaultTableModel modeloE,JTable tablaE){
        while(modeloE.getRowCount()>0){
            modeloE.removeRow(0);
        }
        tablaE.setModel(modeloE);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPMinventario = new javax.swing.JPopupMenu();
        JMIactualizarI = new javax.swing.JMenuItem();
        JMIeliminarI = new javax.swing.JMenuItem();
        JMIagrearProducto = new javax.swing.JMenuItem();
        JPMproductos = new javax.swing.JPopupMenu();
        JMIactualizarP = new javax.swing.JMenuItem();
        JMIeliminarP = new javax.swing.JMenuItem();
        JMpaquetes = new javax.swing.JMenu();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        JTFbuscarInv = new javax.swing.JTextField();
        JBbuscarInventario = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTablaInventario = new javax.swing.JTable();
        JLmensajeI = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        JTFnombre = new javax.swing.JTextField();
        JTFprecio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        JDCfechaCadu = new com.toedter.calendar.JDateChooser();
        JTFcantidad = new javax.swing.JTextField();
        JBagregar = new javax.swing.JButton();
        JBmodificar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        JTFbuscarProducto = new javax.swing.JTextField();
        JBbuscarProducto = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTablaProductos = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        JCBproductos = new javax.swing.JComboBox<>();
        JBagregarP = new javax.swing.JButton();
        JBmodificarP = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        JLdatosInventario = new javax.swing.JLabel();
        JLmensajeP = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        JMIregresar = new javax.swing.JMenuItem();

        JMIactualizarI.setText("Actualizar");
        JMIactualizarI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIactualizarIActionPerformed(evt);
            }
        });
        JPMinventario.add(JMIactualizarI);

        JMIeliminarI.setText("Eliminar");
        JMIeliminarI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIeliminarIActionPerformed(evt);
            }
        });
        JPMinventario.add(JMIeliminarI);

        JMIagrearProducto.setText("Añadir a productos");
        JMIagrearProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIagrearProductoActionPerformed(evt);
            }
        });
        JPMinventario.add(JMIagrearProducto);

        JMIactualizarP.setText("Modificar producto");
        JMIactualizarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIactualizarPActionPerformed(evt);
            }
        });
        JPMproductos.add(JMIactualizarP);

        JMIeliminarP.setText("Eliminar producto");
        JMIeliminarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIeliminarPActionPerformed(evt);
            }
        });
        JPMproductos.add(JMIeliminarP);

        JMpaquetes.setText("Agregar a paquete");
        JMpaquetes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JMpaquetesMouseEntered(evt);
            }
        });
        JPMproductos.add(JMpaquetes);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inventario");
        setUndecorated(true);

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jLabel1.setText("Buscar el producto:");

        JBbuscarInventario.setText("Buscar");
        JBbuscarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBbuscarInventarioActionPerformed(evt);
            }
        });

        JTablaInventario = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int rowCol){
                return false;
            }
        };
        JTablaInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        JTablaInventario.setComponentPopupMenu(JPMinventario);
        JTablaInventario.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(JTablaInventario);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Multiprocesos"));

        jLabel2.setText("Nombre: ");

        jLabel5.setText("Precio: ");

        jLabel3.setText("Medida: ");

        jLabel4.setText("Fecha de Caducidad: ");

        JDCfechaCadu.setDateFormatString("yyyy-MM-dd");

        JBagregar.setText("Agregar Producto");
        JBagregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBagregarActionPerformed(evt);
            }
        });

        JBmodificar.setText("Modificar");
        JBmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBmodificarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTFprecio, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JDCfechaCadu, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTFnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addGap(69, 69, 69)
                        .addComponent(JTFcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JBagregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(JBmodificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(JDCfechaCadu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(JTFnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)
                                .addComponent(JTFcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JBagregar)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(JTFprecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))
                            .addComponent(JBmodificar, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(JLmensajeI)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTFbuscarInv, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JBbuscarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JTFbuscarInv, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JBbuscarInventario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(JLmensajeI))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Inventario", jPanel2);

        jLabel6.setText("Ingrese el nombre del producto:");

        JBbuscarProducto.setText("Buscar");
        JBbuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBbuscarProductoActionPerformed(evt);
            }
        });

        JTablaProductos=new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        JTablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        JTablaProductos.setComponentPopupMenu(JPMproductos);
        jScrollPane2.setViewportView(JTablaProductos);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Multiprocesos"));

        jLabel7.setText("Tipo de producto:");

        JBagregarP.setText("Agregar Producto");
        JBagregarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBagregarPActionPerformed(evt);
            }
        });

        JBmodificarP.setText("Modificar Producto");
        JBmodificarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBmodificarPActionPerformed(evt);
            }
        });

        jLabel8.setText("Datos del producto:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(JCBproductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JBagregarP)
                        .addGap(18, 18, 18)
                        .addComponent(JBmodificarP))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(JLdatosInventario)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(JCBproductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JBagregarP)
                    .addComponent(JBmodificarP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(JLdatosInventario))
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTFbuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLmensajeP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JBbuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(JTFbuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JBbuscarProducto)
                    .addComponent(JLmensajeP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Productos", jPanel1);

        jMenu1.setText("Opciones");

        JMIregresar.setText("Regresar");
        JMIregresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIregresarActionPerformed(evt);
            }
        });
        jMenu1.add(JMIregresar);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //accion de boton buscar
    private void JBbuscarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBbuscarInventarioActionPerformed
        if(JTFbuscarInv.getText().isEmpty()){
            mostrarTodoInventario();
        }else{
            if(!buscarInventario()){
                int seleccion=JOptionPane.showConfirmDialog(null, "No se encontro el registro\ndesea agregarlo a la tabla"
                        ,"Error en la busqueda",JOptionPane.ERROR_MESSAGE);
                switch(seleccion){
                    case JOptionPane.YES_OPTION:
                        JBbuscarInventario.setEnabled(false);
                        JTFbuscarInv.setEnabled(false);
                        JTFcantidad.setEnabled(true);
                        JDCfechaCadu.setEnabled(true);
                        JTFnombre.setEnabled(true);
                        JBagregar.setEnabled(true);
                        JTFbuscarInv.setText("");
                    break;
                    case JOptionPane.NO_OPTION:
                        JTFbuscarInv.setText("");
                    break;
                }
            }
        }
    }//GEN-LAST:event_JBbuscarInventarioActionPerformed

    //accion del boton agregar
    private void JBagregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBagregarActionPerformed
        if(JTFnombre.getText().isEmpty()||JTFcantidad.getText().isEmpty()||JDCfechaCadu.getDate()==null){
            JLmensajeI.setText("No se pueden dejar espacios vacios en el registro.");
        }else{
            agregarInventario();
        }
    }//GEN-LAST:event_JBagregarActionPerformed

    //accion del menu item actualizar
    private void JMIactualizarIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIactualizarIActionPerformed
        int filaS=JTablaInventario.getSelectedRow();
        String nombre,cantidad,precio;
        SimpleDateFormat fechaAux=new SimpleDateFormat("yyyy-MM-dd");
        
        if(filaS <= -1){
            JLmensajeI.setText("No se a seleccionado una fila para que sea actualizada");
        }else{
            JBbuscarInventario.setEnabled(false);
            JTFbuscarInv.setEnabled(false);
            JTFcantidad.setEnabled(true);
            JDCfechaCadu.setEnabled(true);
            JTFnombre.setEnabled(true);
            JTFprecio.setEnabled(true);
            JBmodificar.setEnabled(true);
            JTablaInventario.setEnabled(false);
            
            nombre=JTablaInventario.getValueAt(filaS, 1).toString();
            cantidad=JTablaInventario.getValueAt(filaS, 2).toString();
            precio=JTablaInventario.getValueAt(filaS, 3).toString();
            
            JTFnombre.setText(nombre);
            JTFcantidad.setText(cantidad);
            JTFprecio.setText(precio);
            try {
                JDCfechaCadu.setDate(fechaAux.parse(JTablaInventario.getValueAt(filaS, 4).toString()));
            } catch (ParseException ex) {
                Logger.getLogger(Panel_Inventario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_JMIactualizarIActionPerformed

    //accion del menu item pop modificar
    private void JBmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBmodificarActionPerformed
        if(JTFnombre.getText().isEmpty()||JTFcantidad.getText().isEmpty()||JDCfechaCadu.getDate()==null){
            JLmensajeI.setText("No se pueden dejar espacios vacios en el registro.");
        }else{
            int filaS=JTablaInventario.getSelectedRow();
            actualizarInventario(filaS);
            limpiarTabla(modeloI, JTablaInventario);
            JBbuscarInventario.setEnabled(true);
            JTFbuscarInv.setEnabled(true);
            JTFcantidad.setEnabled(false);
            JDCfechaCadu.setEnabled(false);
            JTFnombre.setEnabled(false);
            JTFprecio.setEnabled(false);
            JBmodificar.setEnabled(false);
            JTablaInventario.setEnabled(true);
            JBagregar.setEnabled(false);
            JBmodificar.setEnabled(false);
        }
    }//GEN-LAST:event_JBmodificarActionPerformed

    //accion del menu item pop eliminar
    private void JMIeliminarIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIeliminarIActionPerformed
        int filaS=JTablaInventario.getSelectedRow();
        
        if(filaS <= -1){
            JLmensajeI.setText("No se a seleccionado una fila para que sea actualizada");
        }else{
            bajaInventario(filaS);
        }
    }//GEN-LAST:event_JMIeliminarIActionPerformed

    //accion menu item regresar
    private void JMIregresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIregresarActionPerformed
        dispose();
    }//GEN-LAST:event_JMIregresarActionPerformed

    //accion de boton buscar producto
    private void JBbuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBbuscarProductoActionPerformed
        if(JTFbuscarProducto.getText().isEmpty()){
            mostrarTodoProductos();
        }else{
            buscarProductos(JTFbuscarProducto.getText(),modeloP,JTablaProductos);
        }
    }//GEN-LAST:event_JBbuscarProductoActionPerformed

    //accion para agregar producto
    private void JBagregarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBagregarPActionPerformed
        String [] JLseparado=JLdatosInventario.getText().split(" ");
        int itemS = JCBproductos.getSelectedIndex();
        
        agregarProducto(JLseparado[0],itemS);
    }//GEN-LAST:event_JBagregarPActionPerformed

    //accion para modificar el procto
    private void JBmodificarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBmodificarPActionPerformed
        int itemS=JCBproductos.getSelectedIndex();
        String [] JLseparado=JLdatosInventario.getText().split(" ");        
        actualizarProducto(JLseparado[0],itemS);
    }//GEN-LAST:event_JBmodificarPActionPerformed

    //accion menu item pasar datos a otro panel
    private void JMIagrearProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIagrearProductoActionPerformed
        int filaS=JTablaInventario.getSelectedRow();
        String id=JTablaInventario.getValueAt(filaS, 0).toString();
        String nombre=JTablaInventario.getValueAt(filaS, 1).toString();
        String tipo=JTablaInventario.getValueAt(filaS, 2).toString();
        String precio=JTablaInventario.getValueAt(filaS, 3).toString();
        JLdatosInventario.setText(id+" "+nombre+" "+tipo+" "+precio);
        
        jTabbedPane1.setSelectedIndex(1);
        
        obtenerItemsJCB();
    }//GEN-LAST:event_JMIagrearProductoActionPerformed

    //accion para mostrar los elementos del combo box
    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        if(jTabbedPane1.getSelectedIndex()==1){
            obtenerItemsJCB();
        }
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    //accion para pasar los datos al JL
    private void JMIactualizarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIactualizarPActionPerformed
        int filaS=JTablaProductos.getSelectedRow();
        
        if(filaS < -1){
            JOptionPane.showMessageDialog(null, "Debes de seleccionar una fila de la tabla");
        }else{
            String id=JTablaProductos.getValueAt(filaS, 0).toString();
            String nombre=JTablaProductos.getValueAt(filaS, 1).toString();
            String tipo=JTablaProductos.getValueAt(filaS, 2).toString();
            String precio=JTablaProductos.getValueAt(filaS, 3).toString();
            JLdatosInventario.setText(id+" "+nombre+" "+tipo+" "+precio);
        }
    }//GEN-LAST:event_JMIactualizarPActionPerformed

    //accion para eliminar el producto de la tabla
    private void JMIeliminarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIeliminarPActionPerformed
        int filaS=JTablaProductos.getSelectedRow();
        String id=JTablaProductos.getValueAt(filaS, 0).toString();
        
        bajaProducto(id);
    }//GEN-LAST:event_JMIeliminarPActionPerformed

    private void JMpaquetesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JMpaquetesMouseEntered
        obtenerPaquetes();
    }//GEN-LAST:event_JMpaquetesMouseEntered

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Panel_Inventario().setVisible(true);
            }
        });
    }
// <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBagregar;
    private javax.swing.JButton JBagregarP;
    private javax.swing.JButton JBbuscarInventario;
    private javax.swing.JButton JBbuscarProducto;
    private javax.swing.JButton JBmodificar;
    private javax.swing.JButton JBmodificarP;
    private javax.swing.JComboBox<String> JCBproductos;
    private com.toedter.calendar.JDateChooser JDCfechaCadu;
    private javax.swing.JLabel JLdatosInventario;
    private javax.swing.JLabel JLmensajeI;
    private javax.swing.JLabel JLmensajeP;
    private javax.swing.JMenuItem JMIactualizarI;
    private javax.swing.JMenuItem JMIactualizarP;
    private javax.swing.JMenuItem JMIagrearProducto;
    private javax.swing.JMenuItem JMIeliminarI;
    private javax.swing.JMenuItem JMIeliminarP;
    private javax.swing.JMenuItem JMIregresar;
    private javax.swing.JMenu JMpaquetes;
    private javax.swing.JPopupMenu JPMinventario;
    private javax.swing.JPopupMenu JPMproductos;
    private javax.swing.JTextField JTFbuscarInv;
    private javax.swing.JTextField JTFbuscarProducto;
    private javax.swing.JTextField JTFcantidad;
    private javax.swing.JTextField JTFnombre;
    private javax.swing.JTextField JTFprecio;
    private javax.swing.JTable JTablaInventario;
    private javax.swing.JTable JTablaProductos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
// </editor-fold>