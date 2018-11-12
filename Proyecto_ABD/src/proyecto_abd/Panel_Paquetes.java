package proyecto_abd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;


public class Panel_Paquetes extends javax.swing.JFrame {

    Panel_Inventario pv = new Panel_Inventario();
    ArrayList<String> registrosPro=new ArrayList<>();
    DefaultTableModel modeloCon1 = new DefaultTableModel();
    DefaultTableModel modeloCon2 = new DefaultTableModel();
    DefaultTableModel modeloCrear = new DefaultTableModel();
    DefaultTableModel modeloProductos = new DefaultTableModel();
    DefaultTableModel modeloAct1 = new DefaultTableModel();
    DefaultTableModel modeloAct2 = new DefaultTableModel();
    Conexion mysql=new Conexion();
    Connection cn=mysql.Conexion();
    float precioPro,precioPaq;

    public Panel_Paquetes() {
        initComponents();
        String[] titulosPaquetesVer1={"Id_paquete","Nombre","Cantidad de productos","Precio"};
        modeloCon1.setColumnIdentifiers(titulosPaquetesVer1);
        JTablaPaquetesCon1.setModel(modeloCon1);
        String[] titulosPaquetesVer2={"Id_producto","Nombre","Tipo producto","Precio producto","Cantidad del producto"};
        modeloCon2.setColumnIdentifiers(titulosPaquetesVer2);
        JTablaPaquetesCon2.setModel(modeloCon2);
        String[] titulosPaquetesCrear={"Id_producto","Nombre de producto","Cantidad del producto","Precio"};
        modeloCrear.setColumnIdentifiers(titulosPaquetesCrear);
        JTablaPaquetesCrear.setModel(modeloCrear);
        String[] titulosProductosCrear={"Nombre de producto","Cantidad productos","Precio"};
        modeloProductos.setColumnIdentifiers(titulosProductosCrear);
        JTablaProductosEx.setModel(modeloProductos);
        String[] titulosPaquetesAct1={"Id_paquete","Nombre","Cantidad de productos","Precio"};
        modeloAct1.setColumnIdentifiers(titulosPaquetesAct1);
        JTablaPaqueteAct1.setModel(modeloAct1);
        String[] titulosPaquetesAct2={"Id_producto","Nombre","Tipo producto","Precio producto","Cantidad del producto"};
        modeloAct2.setColumnIdentifiers(titulosPaquetesAct2);
        JTablaPaqueteAct2.setModel(modeloAct2);
    }

    //metodo para el limpiado de todas las tablas
    public void limpiarTabla(DefaultTableModel modeloE,JTable tablaE){
        while(modeloE.getRowCount()>0){
            modeloE.removeRow(0);
        }
        tablaE.setModel(modeloE);
    }
    
    //metodo para buscar paquetes
    public void buscarPaquetes(String entrada,DefaultTableModel modeloE,JTable tablaE,boolean band){
        String registros[]=new String[4];
        String SQLs;
        limpiarTabla(modeloE, tablaE);
        registros=new String[4];
        
        if(band){
            SQLs="SELECT paquetes.id_paquete,paquetes.nombre,paquetes.cantidad_productos,paquetes.precio_paquete "
                + "FROM paquetes,tipo_productos "
                + "WHERE paquetes.id_tipo_producto=tipo_productos.id_tipo_producto ;";
        }else{
            SQLs="SELECT paquetes.id_paquete,paquetes.nombre,paquetes.cantidad_productos,paquetes.precio_paquete "
                + "FROM paquetes,tipo_productos "
                + "WHERE paquetes.nombre='%"+entrada+"%'"
                + "AND paquetes.id_tipo_producto=tipo_productos.id_tipo_producto ;";
        }
        
        try {
            Statement st = cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            if(band){
                while(rs.next()){
                    registros[0]=rs.getString(1);
                    registros[1]=rs.getString(2);
                    registros[2]=rs.getString(3);
                    registros[3]=rs.getString(4);
                    modeloE.addRow(registros);
                }
                tablaE.setModel(modeloE);
            }else{
                while(rs.next()){
                    registros[0]=rs.getString(1);
                    registros[1]=rs.getString(2);
                    registros[2]=rs.getString(3);
                    registros[3]=rs.getString(4);
                    modeloE.addRow(registros);
                }
                tablaE.setModel(modeloE);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la carga de datos en paquetes\n"+ex.getLocalizedMessage()
            ,"Error en la base de datos", JOptionPane.WARNING_MESSAGE);
        }
    }

    //metodo para buscar productos de los paquetes
    public void buscarProductos(String entrada,DefaultTableModel modeloE,JTable tablaE,boolean band){
        String registros[];
        String SQLs;
        if(band){
            SQLs="SELECT inventario.id_inventario,inventario.nombre,tipo_productos.tipo_producto,id_inventario.precio "
                + "FROM productos, tipo_productos, inventario, paq_pro, paquetes "
                + "WHERE productos.id_producto = '"+entrada+"' "
                + "AND paqutes.id_paquete = paq_pro.id_paquetes "
                + "AND paq_pro.id_productos = productos.id_productos "
                + "AND productos.id_tipo_producto = tipo_productos.id_tipo_producto "
                + "AND inventario.id_inventario = productos.id_inventario ;";
        }else{
            SQLs="SELECT inventario.id_inventario,inventario.nombre,tipo_productos.tipo_producto,inventario.precio,paq_pro.cantidad_productos "
                + "FROM productos, tipo_productos, inventario, paq_pro, paquetes "
                + "WHERE paquetes.id_paquete = '"+entrada+"' "
                + "AND paquetes.id_paquete = paq_pro.id_paquete "
                + "AND paq_pro.id_producto = productos.id_producto "
                + "AND productos.id_tipo_producto = tipo_productos.id_tipo_producto "
                + "AND inventario.id_inventario = productos.id_inventario ;";
        }
        
        //limpiar tabla
        limpiarTabla(modeloE, tablaE);
            
       if(band){
           registros=new String [4];
       }else{
           registros=new String [5];
       }
        
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
                if(band){
                    while(rs.next()){
                        registros[0]=rs.getString(1);
                        registros[1]=rs.getString(2);
                        registros[2]=rs.getString(3);
                        registros[3]=rs.getString(4);
                        
                        modeloE.addRow(registros);
                    }
                }else{
                    while(rs.next()){
                        registros[0]=rs.getString(1);
                        registros[1]=rs.getString(2);
                        registros[2]=rs.getString(3);
                        registros[3]=rs.getString(4);
                        registros[4]=rs.getString(5);
                        
                        modeloE.addRow(registros);
                    }
                }
                tablaE.setModel(modeloE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //metodo para la actualizacion de paquetes
    public void actualizarPaquetes(int id,String nombre,String precio,int cantidad,boolean band){
        String SQLu1=null;
        if(band){
            SQLu1="UPDATE paquetes "
                + "SET nombre='"+nombre+"',precio_paquete='"+precio+"' "
                + "WHERE id_paquete='"+id+"' ;";
        }else{
            SQLu1="UPDATE paquetes "
                + "SET cantidad_productos='"+cantidad+"'"
                + "WHERE id_paquete='"+id+"' ;";
        }
        
        try {
            PreparedStatement pps;
            pps = cn.prepareStatement(SQLu1);
            pps.executeUpdate();
            JLmensajeAct.setText("El paquete se a modificado");
        } catch (SQLException ex) {
            JLmensajeAct.setText("Ocurrio un error en la actualizacion");
        }
    }
    
    //metodo para eliminar un producto de un paquete
    public void eliminarProductoPaquete(int id){
        String SQLe="DELETE FROM paq_pro WHERE paq_pro.id_prodcuto = '"+id+"'";
        try {
            PreparedStatement pps = cn.prepareStatement(SQLe);
            pps.executeUpdate();
            JLmensajeAct.setText("Se ha eliminado el registro con exito");
            
        } catch (SQLException ex) {
            JLmensajeAct.setText("Error en eliminar el registro");
        }
        String id_paquetes=JTablaPaqueteAct1.getValueAt(JTablaPaqueteAct1.getSelectedRow(), 0).toString();
        buscarProductos(id_paquetes, modeloAct2, JTablaPaqueteAct2, false);
    }
    
    //metodo para obtener los productos nombre de los productos.
    public void desplegarProductosLista(){
        int cont_Aux=0;
        String [] registros=new String[3];
        String SQLs="SELECT inventario.nombre,inventario.precio FROM productos,inventario "
                  + "WHERE productos.id_inventario=inventario.id_inventario ;";
        
        limpiarTabla(modeloProductos, JTablaProductosEx);
        
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            while(rs.next()){
                registros[0]=rs.getString(1);
                registros[1]="0";
                registros[2]=rs.getString(2);
                
                registrosPro.add(cont_Aux,rs.getString(1)+" "+rs.getString(2));
                
                modeloProductos.addRow(registros);
            }
            JTablaProductosEx.setModel(modeloProductos);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //metodo para agregar el paquete
    public void agregarPaquete(String entrada1,String entrada2,ArrayList arrayE){
        int cantP=0;
        ArrayList<String> cantidadesP= new ArrayList<>();
        String SQLvarios,id_paquete=null,id_producto=null;
        for(int i=0;i<arrayE.size();i++){
            String [] cadenaAux=arrayE.get(i).toString().split("_");
            cantidadesP.add(i,cadenaAux[1]);
            cantP+=Integer.parseInt(cadenaAux[1]);
        }
        SQLvarios="INSERT INTO paquetes VALUES (NULL,'"+entrada1+"','"+cantP+"','6','"+entrada2+"');";
        try{
            PreparedStatement pst = cn.prepareStatement(SQLvarios);
            pst.execute();
            
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error en insertar los datos\n"+e); 
        }
        
        //siguiente paso agregar a la tabla paq_pro los productos
         //primero guardamos el id del paquete creado
        SQLvarios="SELECT id_paquete,nombre FROM paquetes WHERE nombre LIKE '"+entrada1+"' ;";
            
        try {
            Statement st = cn.createStatement();
            ResultSet rs=st.executeQuery(SQLvarios);

            while(rs.next()){
                id_paquete=rs.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la carga de datos en paquetes\n"+ex.getLocalizedMessage()
            ,"Error en la base de datos", JOptionPane.WARNING_MESSAGE);
        }
         
         //segundo con el id guardado insertamos los productos que fueron almacendados en el array
        for(int i=0;i<arrayE.size();i++){
            String [] datos=arrayE.get(i).toString().split("_");
            SQLvarios="SELECT productos.id_producto "
                    + "FROM productos,inventario "
                    + "WHERE inventario.nombre LIKE '"+datos[0]+"' "
                    + "AND inventario.id_inventario=productos.id_inventario ;";
            
            try {
                Statement st = cn.createStatement();
                ResultSet rs=st.executeQuery(SQLvarios);

                while(rs.next()){
                    id_producto=rs.getString(1);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error en la carga de datos en paq_pro\n"+ex.getLocalizedMessage()
                ,"Error en la base de datos", JOptionPane.WARNING_MESSAGE);
            }
            
            SQLvarios="INSERT INTO paq_pro VALUES ('"+id_paquete+"','"+id_producto+"','"+cantidadesP.get(i)+"');";
            
            try{
                PreparedStatement pst = cn.prepareStatement(SQLvarios);
                pst.execute();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null,"Error en insertar los datos\n"+e); 
            }
        }
    }
    
    
    //sumar los precios del array
    public void tomarPrecioArray(String nombre){
        for(int i=0;i<registrosPro.size();i++){
            String datos[]=registrosPro.get(i).split(" ");
            if(datos[0].equalsIgnoreCase(nombre)){
                precioPro=Float.parseFloat(datos[1]);
            }
        }
    }
   
    //metodo para sacer el precio original del paquete
    public void preciosPaquete(int numFilas){
        precioPaq=0;
        for(int i=0;i<numFilas;i++){
            precioPaq+=Float.parseFloat(JTablaProductosEx.getValueAt(i, 2).toString());
        }
        JTFprecioOriginal.setText(precioPaq+"");
        float precioP1,precioP2;
        
        precioP1=(float) (precioPaq*0.15);
        precioP2=precioPaq-precioP1;
        JTFprecioPaquete.setText(precioP2+"");
    }
    
    //metodo para sacar las catidades de la tabla act2
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPMactualizacion = new javax.swing.JPopupMenu();
        JMIagregarPro = new javax.swing.JMenuItem();
        JMImodificarPro = new javax.swing.JMenuItem();
        JMIeliminarPro = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTablaPaquetesCon1 = new javax.swing.JTable();
        JBverpaquetes = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        JTablaPaquetesCon2 = new javax.swing.JTable();
        JPcrear = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        JTFnombrePaquete = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        JTFprecioPaquete = new javax.swing.JTextField();
        JBagregarPaquete = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        JTFprecioOriginal = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTablaPaquetesCrear = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        JTablaProductosEx = new javax.swing.JTable();
        JLmensajeCrear = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        JTFbuscarPaquete = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        JTablaPaqueteAct1 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        JTablaPaqueteAct2 = new javax.swing.JTable();
        JBbuscarAct = new javax.swing.JButton();
        JLmensajeAct = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        JMIregresar = new javax.swing.JMenuItem();

        JMIagregarPro.setText("Agregar producto al paquete");
        JMIagregarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIagregarProActionPerformed(evt);
            }
        });
        JPMactualizacion.add(JMIagregarPro);

        JMImodificarPro.setText("Modificar producto del paquete");
        JMImodificarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMImodificarProActionPerformed(evt);
            }
        });
        JPMactualizacion.add(JMImodificarPro);

        JMIeliminarPro.setText("Eliminar producto del paquete");
        JMIeliminarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIeliminarProActionPerformed(evt);
            }
        });
        JPMactualizacion.add(JMIeliminarPro);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        JTablaPaquetesCon1 = new javax.swing.JTable(){
            public boolean isCellEditable(int indexRow,int indexCol){
                return false;
            }
        };
        JTablaPaquetesCon1.setModel(new javax.swing.table.DefaultTableModel(
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
        JTablaPaquetesCon1.getTableHeader().setReorderingAllowed(false);
        JTablaPaquetesCon1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTablaPaquetesCon1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(JTablaPaquetesCon1);

        JBverpaquetes.setText("Ver paquetes");
        JBverpaquetes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBverpaquetesActionPerformed(evt);
            }
        });

        JTablaPaquetesCon2 = new javax.swing.JTable(){
            public boolean isCellEditable(int indexRow,int indexCol){
                return false;
            }
        };
        JTablaPaquetesCon2.setModel(new javax.swing.table.DefaultTableModel(
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
        JTablaPaquetesCon2.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(JTablaPaquetesCon2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(JBverpaquetes)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JBverpaquetes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Consultar", jPanel3);

        jLabel1.setText("Nombre del paquete:");

        jLabel2.setText("Lista de productos:");

        jLabel3.setText("Precio del paquete:");

        JTFprecioPaquete.setEditable(false);

        JBagregarPaquete.setText("Agregar paquete");
        JBagregarPaquete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBagregarPaqueteActionPerformed(evt);
            }
        });

        jLabel4.setText("Precio original de los productos:");

        JTFprecioOriginal.setEditable(false);

        JTablaPaquetesCrear = new javax.swing.JTable(){
            public boolean isCellEditable(int indexRow,int indexCol){
                return false;
            }
        };
        JTablaPaquetesCrear.setModel(new javax.swing.table.DefaultTableModel(
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
        JTablaPaquetesCrear.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(JTablaPaquetesCrear);

        JTablaProductosEx = new javax.swing.JTable(){
            public boolean isCellEditable(int colIndex,int rowIndex){
                boolean editRow;
                if(rowIndex==1){
                    editRow=true;
                }else{
                    editRow=false;
                }
                return editRow;
            }
        };
        JTablaProductosEx.setModel(new javax.swing.table.DefaultTableModel(
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
        JTablaProductosEx.getTableHeader().setReorderingAllowed(false);
        JTablaProductosEx.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                JTablaProductosExPropertyChange(evt);
            }
        });
        jScrollPane7.setViewportView(JTablaProductosEx);

        javax.swing.GroupLayout JPcrearLayout = new javax.swing.GroupLayout(JPcrear);
        JPcrear.setLayout(JPcrearLayout);
        JPcrearLayout.setHorizontalGroup(
            JPcrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPcrearLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPcrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPcrearLayout.createSequentialGroup()
                        .addGroup(JPcrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPcrearLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JTFnombrePaquete))
                            .addGroup(JPcrearLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPcrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPcrearLayout.createSequentialGroup()
                                .addGroup(JPcrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(JPcrearLayout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(JTFprecioPaquete, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(JPcrearLayout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(JTFprecioOriginal, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(JBagregarPaquete))
                            .addComponent(JLmensajeCrear))))
                .addContainerGap())
        );
        JPcrearLayout.setVerticalGroup(
            JPcrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPcrearLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPcrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPcrearLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(JPcrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(JPcrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(JTFprecioOriginal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JPcrearLayout.createSequentialGroup()
                                .addComponent(JBagregarPaquete, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JLmensajeCrear))
                    .addGroup(JPcrearLayout.createSequentialGroup()
                        .addGroup(JPcrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(JTFnombrePaquete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(JTFprecioPaquete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(JPcrearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPcrearLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JPcrearLayout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(jLabel2)))))
                .addGap(13, 13, 13)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Crear", JPcrear);

        jLabel5.setText("Buscar paquete:");

        JTablaPaqueteAct1 = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int rowCol){
                boolean outEditable;
                if(rowCol == 2||rowCol == 0){
                    outEditable=false;
                }else{
                    outEditable=true;
                }
                return outEditable;
            }
        };
        JTablaPaqueteAct1.setModel(new javax.swing.table.DefaultTableModel(
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
        JTablaPaqueteAct1.getTableHeader().setReorderingAllowed(false);
        JTablaPaqueteAct1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTablaPaqueteAct1MouseClicked(evt);
            }
        });
        JTablaPaqueteAct1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                JTablaPaqueteAct1PropertyChange(evt);
            }
        });
        jScrollPane4.setViewportView(JTablaPaqueteAct1);

        JTablaPaqueteAct2 = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int rowCol){
                boolean cellEdit;
                if(rowCol==4){
                    cellEdit=true;
                }else{
                    cellEdit=false;
                }
                return cellEdit;
            }
        };
        JTablaPaqueteAct2.setModel(new javax.swing.table.DefaultTableModel(
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
        JTablaPaqueteAct2.setComponentPopupMenu(JPMactualizacion);
        JTablaPaqueteAct2.getTableHeader().setReorderingAllowed(false);
        JTablaPaqueteAct2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                JTablaPaqueteAct2PropertyChange(evt);
            }
        });
        jScrollPane6.setViewportView(JTablaPaqueteAct2);

        JBbuscarAct.setText("Buscar");
        JBbuscarAct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBbuscarActActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTFbuscarPaquete, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLmensajeAct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JBbuscarAct, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(JTFbuscarPaquete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JBbuscarAct)
                    .addComponent(JLmensajeAct))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Modificar", jPanel2);

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
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //accion para buscar paquetes
    private void JBverpaquetesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBverpaquetesActionPerformed
        buscarPaquetes(null, modeloCon1, JTablaPaquetesCon1, true);
    }//GEN-LAST:event_JBverpaquetesActionPerformed

    //accion para agregar nuevo paquete
    private void JBagregarPaqueteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBagregarPaqueteActionPerformed
        ArrayList<String> productos=new ArrayList<>();
        int contCeros=0,contAux=0;
        if(JTFnombrePaquete.getText().isEmpty()||JTFprecioPaquete.getText().isEmpty()){
            for(int f=0;f<JTablaProductosEx.getRowCount();f++){
                if(JTablaProductosEx.getValueAt(f, 1).toString().equals("0")){
                    contCeros++;
                }
            }
            if(contCeros==JTablaProductosEx.getRowCount()){
                JOptionPane.showMessageDialog(null, "No se pueden dejar los campos en blanco y\nlas casillas de cantidad en 0",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }else{
            for(int f=0;f<JTablaProductosEx.getRowCount();f++){
                if(JTablaProductosEx.getValueAt(f, 1).toString().equals("0")){
                    contCeros++;
                }else{
                    String nombreP=JTablaProductosEx.getValueAt(f, 0).toString();
                    String cantP=JTablaProductosEx.getValueAt(f, 1).toString();
                    productos.add(contAux,nombreP+"_"+cantP);
                }
            }
            if(contCeros==JTablaProductosEx.getRowCount()){
                JOptionPane.showMessageDialog(null, "No se pueden dejar los campos en blanco y\nlas casillas de cantidad en 0",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            }else{
                agregarPaquete(JTFnombrePaquete.getText(),JTFprecioPaquete.getText(),productos);
            }
        }
    }//GEN-LAST:event_JBagregarPaqueteActionPerformed

    //metodo de buscar paquetes en actualizacion
    private void JBbuscarActActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBbuscarActActionPerformed
        if(JTFbuscarPaquete.getText().isEmpty()){
            buscarPaquetes(null, modeloAct1, JTablaPaqueteAct1, true);
        }else{
            buscarPaquetes(JTFbuscarPaquete.getText(), modeloAct1, JTablaPaqueteAct1, false);
        }
    }//GEN-LAST:event_JBbuscarActActionPerformed

    //accion de jmenu agregar producto
    private void JMIagregarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIagregarProActionPerformed
        pv.show();
        pv.jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_JMIagregarProActionPerformed

    //accion del jmenu modificar producto
    private void JMImodificarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMImodificarProActionPerformed
        pv.show();
        pv.jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_JMImodificarProActionPerformed

    //accion del jmenu eliminar producto de paquete
    private void JMIeliminarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIeliminarProActionPerformed
        int filaS=JTablaPaqueteAct2.getSelectedRow();
        
        if(filaS <= -1){
            JOptionPane.showMessageDialog(null, "Debes de seleccionar una fila",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }else{
            int id_producto=Integer.parseInt(JTablaPaqueteAct2.getValueAt(filaS, 0).toString());
            eliminarProductoPaquete(id_producto);
        }
    }//GEN-LAST:event_JMIeliminarProActionPerformed

    //accion del jmenu regresar
    private void JMIregresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIregresarActionPerformed
        dispose();
    }//GEN-LAST:event_JMIregresarActionPerformed

    //actualizar paquetes Act1
    private void JTablaPaqueteAct1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_JTablaPaqueteAct1PropertyChange
        if(evt.getPropertyName().equals("tableCellEditor")){
            int filaS = JTablaPaqueteAct1.getSelectedRow();
            int id=Integer.parseInt(JTablaPaqueteAct1.getValueAt(filaS, 0).toString());
            String nombre=JTablaPaqueteAct1.getValueAt(filaS, 1).toString();
            String precio=JTablaPaqueteAct1.getValueAt(filaS, 3).toString();
            actualizarPaquetes(id,nombre,precio,0,true);
        }
    }//GEN-LAST:event_JTablaPaqueteAct1PropertyChange

    //al hacer click en la tabla
    private void JTablaPaqueteAct1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTablaPaqueteAct1MouseClicked
        int filaS = JTablaPaqueteAct1.getSelectedRow();

        if(filaS <= -1){
            JOptionPane.showMessageDialog(null, "Debes de seleccionar una fila",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }else{
            String id_paquetes=JTablaPaqueteAct1.getValueAt(filaS, 0).toString();
            buscarProductos(id_paquetes, modeloAct2, JTablaPaqueteAct2, false);
        }
    }//GEN-LAST:event_JTablaPaqueteAct1MouseClicked

    //accion del click en la tabla de consulta1
    private void JTablaPaquetesCon1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTablaPaquetesCon1MouseClicked
        int filaS = JTablaPaquetesCon1.getSelectedRow();

        if(filaS <= -1){
            JOptionPane.showMessageDialog(null, "Debes de seleccionar una fila",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }else{
            String id_paquetes=JTablaPaquetesCon1.getValueAt(filaS, 0).toString();
            buscarProductos(id_paquetes, modeloCon2, JTablaPaquetesCon2, false);
        }
    }//GEN-LAST:event_JTablaPaquetesCon1MouseClicked

    //accion para los menu paneles
    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        if(jTabbedPane1.getSelectedIndex()==1){
            buscarPaquetes(null, modeloCrear, JTablaPaquetesCrear, true);
            desplegarProductosLista();
        }
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    //accion para sacar cuantos de que producto desea
    private void JTablaProductosExPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_JTablaProductosExPropertyChange
        if(evt.getPropertyName().equals("tableCellEditor")){
            int filaS = JTablaProductosEx.getSelectedRow();
            int cantidad=Integer.parseInt(JTablaProductosEx.getValueAt(filaS, 1).toString());
            if(cantidad<0){
                JOptionPane.showMessageDialog(null, "No se puede poner una cantidad menor a 0\n"
            ,"Error de cantidad de datos", JOptionPane.ERROR_MESSAGE);
                JTablaProductosEx.setValueAt("0",filaS, 1);
            }else{
                String nombreP=JTablaProductosEx.getValueAt(filaS, 0).toString();
                tomarPrecioArray(nombreP);
                JTablaProductosEx.setValueAt(precioPro*cantidad, filaS, 2);
            }
            int numFilas = JTablaProductosEx.getRowCount();
            preciosPaquete(numFilas);
        }
    }//GEN-LAST:event_JTablaProductosExPropertyChange

    //accion para sacar la cantidad del paquete con los mismos productos
    private void JTablaPaqueteAct2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_JTablaPaqueteAct2PropertyChange
        if(evt.getPropertyName().equals("tableCellEditor")){
            int filaS = JTablaPaqueteAct2.getSelectedRow();
            int cantidad=Integer.parseInt(JTablaPaqueteAct2.getValueAt(filaS, 4).toString());
            if(cantidad<1){
                JOptionPane.showMessageDialog(null, "En ves de poner 0 elimina el producto\n"
            ,"Error en las cantidades", JOptionPane.ERROR_MESSAGE);
                JTablaProductosEx.setValueAt(cantidad,filaS, 1);
            }else{
                int cantT=0;
                for(int i=0;i<JTablaPaqueteAct2.getRowCount();i++){
                    cantT+=Integer.parseInt(JTablaPaqueteAct2.getValueAt(i, 4).toString());
                }
                int id=Integer.parseInt(JTablaPaqueteAct1.getValueAt(filaS, 0).toString());
                actualizarPaquetes(id, null, null, cantT, false);
            }
            
        }
    }//GEN-LAST:event_JTablaPaqueteAct2PropertyChange

    public static void main(String args[]) {
 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Panel_Paquetes().setVisible(true);
            }
        });
    }
// <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBagregarPaquete;
    private javax.swing.JButton JBbuscarAct;
    private javax.swing.JButton JBverpaquetes;
    private javax.swing.JLabel JLmensajeAct;
    private javax.swing.JLabel JLmensajeCrear;
    private javax.swing.JMenuItem JMIagregarPro;
    private javax.swing.JMenuItem JMIeliminarPro;
    private javax.swing.JMenuItem JMImodificarPro;
    private javax.swing.JMenuItem JMIregresar;
    private javax.swing.JPopupMenu JPMactualizacion;
    private javax.swing.JPanel JPcrear;
    private javax.swing.JTextField JTFbuscarPaquete;
    private javax.swing.JTextField JTFnombrePaquete;
    private javax.swing.JTextField JTFprecioOriginal;
    private javax.swing.JTextField JTFprecioPaquete;
    private javax.swing.JTable JTablaPaqueteAct1;
    private javax.swing.JTable JTablaPaqueteAct2;
    private javax.swing.JTable JTablaPaquetesCon1;
    private javax.swing.JTable JTablaPaquetesCon2;
    private javax.swing.JTable JTablaPaquetesCrear;
    private javax.swing.JTable JTablaProductosEx;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
// </editor-fold>
