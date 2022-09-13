package montecarlo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Graphics;

public class VentanaPrincipal extends javax.swing.JFrame {
    DefaultTableModel valores = new DefaultTableModel();
    DefaultTableModel calculos = new DefaultTableModel();
    DecimalFormat decimas = new DecimalFormat("#0.0000");
    private int contador_xi, cc = 0,xi_n;
    private double  suma = 0;
    private double[] xi_vec;
    private double[] xi;
    private double[] xi_gen;
    private int[] individuos;
    private int [] gen_1 = new int[6];
    private int [] gen_2 = new int[6];
    private int [] gen_3 = new int[6];
    private int [] gen_4 = new int[6];
    private int cgen_1 = 0, cgen_2 = 0, cgen_3 = 0, cgen_4= 0;

    public VentanaPrincipal() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Problema de Montecarlo");
    }
    
    private String[] abrirArchivo() throws ClassNotFoundException {
      
        try {
            JFileChooser file = new JFileChooser();
            file.showOpenDialog(this);
            File abre = file.getSelectedFile();

            if (abre != null) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(abre));
                double[] arch_leer = (double[]) in.readObject();
                int c = 0;

                // LEE DE ARCHIVO Y CUENTA LOS NUMEROS
                for (Double item : arch_leer) {
                    c = c + 1;
                }
                contador_xi = c;

                int cantidad = Integer.parseInt(JOptionPane.showInputDialog("TOTAL: " + contador_xi + "\n Ingrese cantidad de numeros a usar: "));
                xi_n = cantidad;
                while (cantidad > contador_xi || cantidad < 2) {
                    JOptionPane.showMessageDialog(null, "Tiene que ser menor o igual a " + contador_xi + " ó mayor a 2", "Error", JOptionPane.ERROR_MESSAGE);
                    cantidad = Integer.parseInt(JOptionPane.showInputDialog("TOTAL: " + contador_xi + "\n Ingrese cantidad de numeros a usar: "));
                    xi_n=cantidad;
                }
                //ORDENACION DE NUMEROS
                //Arrays.sort(arch_leer);
                xi_vec = new double[cantidad];
                xi = new double[cantidad];
                xi_gen = new double[cantidad];
                for (int l = 0; l < cantidad; l++) {
                    cc = cc + 1;
                    xi_vec[l] = (arch_leer[l]);
                    xi_gen[l] = (arch_leer[l]);
                    xi[l] = xi_vec[l];
                    suma = xi[l] + suma;
                    //System.out.println("la suma es: " + suma);
                }
                
                //DESPUES DE ASIGNAR VALORES A XI_GEN LLAMA A MONTECARLO
                
                montecarlo();
                
                
                in.close();
            }
            tblMontecarlo.setModel(valores);
            tblGen.setModel(calculos);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex + ""
                    + "\n No se ha encontrado el archivo",
                    "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }
    
    public void select_xi (){
        //SELECCIONA 4 VARIBLES ALEATORIAS DE XI_GEN
        individuos = new int[4]; 
        for(int i = 0; i <= 3 ; i++){
            individuos[i] = (int) (xi_gen[(int) (Math.random()*xi_n)]*100);
            if (individuos[i] > 63){
                individuos[i] = 63;
            }
        }
    }
    
    public void select_indvs (){
     //CONVIERTE LAS 4 VARIABLES EN BINARIOS
     int num = individuos[0];
     int binario[] = new int[6];
     int index = 0;
     
     while(num > 0){
       binario[index++] = num%2;
       num = num/2;
     }
     for(int i = index-1;i >= 0;i--){
       gen_1[i]=binario[i];
     }
     
     num = individuos[1];
     index = 0;
     
     while(num > 0){
       binario[index++] = num%2;
       num = num/2;
     }
     for(int i = index-1;i >= 0;i--){
       gen_2[i]=binario[i];
     }
     
     num = individuos[2];
     index = 0;
     
     while(num > 0){
       binario[index++] = num%2;
       num = num/2;
     }
     for(int i = index-1;i >= 0;i--){
       gen_3[i]=binario[i];
     }
     
     num = individuos[3];
     index = 0;
     
     while(num > 0){
       binario[index++] = num%2;
       num = num/2;
     }
     for(int i = index-1;i >= 0;i--){
       gen_4[i]=binario[i];
     }
    
    /*
    System.out.println(Arrays.toString(gen_1));
    System.out.println(Arrays.toString(gen_2));
    System.out.println(Arrays.toString(gen_3));
    System.out.println(Arrays.toString(gen_4));
    */
    }
    
    
    
    
    public void montecarlo (){
    //INICIRÑIZAR TODAS LAS VARIABLES  
    String[] titulo = new String []{"Individuo", "Fenotipo", "Genotipo", "F(X)", "Ps", "Pa", "Clases"};
    valores.setColumnIdentifiers(titulo);
    String[] titulo2 = new String []{"Individuo", "Hijo", "Mutacion"};
    calculos.setColumnIdentifiers(titulo2);
    //LLAMA A LOS MTODOS
    select_xi();
    select_indvs();
    //GENO ES SOLO PARA MOSTRAR EN TABLA
    int [] geno = new int [6];
    //HIJO GENERADO DE BIN, HIJO SELECCINADO POR CLASES, HIJO MUTADO
    int [] hijo_1 = new int [6];
    int [] hijo_2 = new int [6];
    int [] hijo_3 = new int [6];
    int [] hijo_4 = new int [6];
    int [] hijos_1 = new int [6];
    int [] hijos_2 = new int [6];
    int [] hijos_3 = new int [6];
    int [] hijos_4 = new int [6];
    int [] hijom_1 = new int [6];
    int [] hijom_2 = new int [6];
    int [] hijom_3 = new int [6];
    int [] hijom_4 = new int [6];
    //RANGOS DE CLASES A = MINIMO B = MAXIMO
    double [] c_a = new double [4];
    double [] c_b = new double [4];
    //VARIABLES PARA GENERAR PS PA FX ETC
    double fx = 0, sum=0, ps=0,pa=0, pdm=0.0010;
    //CICLOS PARA MUTACIONES Y ZONAS DE CORTES PARA LOS GENES PADRES
    int ciclo = Integer.parseInt(JOptionPane.showInputDialog("Ingrese numero de ciclos"));
    int pos1 = Integer.parseInt(JOptionPane.showInputDialog("Ingrese posicion de corte 1"));
    int pos2 = Integer.parseInt(JOptionPane.showInputDialog("Ingrese posicion de corte 2"));
    
    //MUESTRA EN TABLA BINARIOS ORDENADOS
    for (int i = 0; i < 4 ; i++){
        switch (i) {
            case 0:
                {
                    int c = 0;
                    for(int j = 5;j >= 0;j--){
                        geno[c]=gen_1[j];
                        c = c+1;
                    }         break;
                }
            case 1:
                {
                    int c = 0;
                    for(int j = 5;j >= 0;j--){
                        geno[c]=gen_2[j];
                        c = c+1;
                    }         break;
                }
            case 2:
                {
                    int c = 0;
                    for(int j = 5;j >= 0;j--){
                        geno[c]=gen_3[j];
                        c = c+1;
                    }         break;  
                }
            case 3:
                {
                    int c = 0;
                    for(int j = 5;j >= 0;j--){
                        geno[c]=gen_4[j];
                        c = c+1;
                    }         break;
                }
            default:
                break;
        }
        
        //REALIZA SUMATORIA PARA GENERAR PS Y PA
        if(i == 0){
          for (int k = 0; k < 4; k++){
            fx = (126*individuos[k])-((Math.pow(individuos[k],2)));
            sum = sum + fx;
            }   
        }
        //EVALUA LOS 4 GENES PRIMARIOS EN F(X) PARA GENERAR PS Y PA
        fx = (126*individuos[i])-((Math.pow(individuos[i],2)));
        //System.out.println(sum);
        ps=(1/sum)*fx;
        pa=pa+ps;
        
        //ASIGNA CLASES
        c_b[i]=pa;
        
        if (i == 0){
            c_a[i]=0;
        }else{
            c_a[i]=c_b[i-1];
        }

        //AGREGA VARIABLES A TABLA
        valores.addRow(new Object[]{i+1,individuos[i],Arrays.toString(geno),fx,ps,pa,"DE: "+c_a[i]+" A: "+c_b[i]});
    }
    

    
    
    //AQUI REPITE LOS CILOS DE MUTACIONES
    for (int f = 0; f < ciclo; f++){
        
        //SELECCIONA 4 VARIABLES ALEATORIAS PARA DEFINIR PADRES Y MADRES DEFINIDOS POR LAS CLASES
        for (int z = 0; z < 4 ; z++)
        {
            double rand = xi_gen[(int) (Math.random()*xi_n)];
          
            
            if(rand > 0 && rand < c_b[0] ){
                if (z == 0){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_1[a]=gen_1[a];
                    } 
                }
                
                if (z == 1){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_2[a]=gen_1[a];
                    } 
                }
                
                if (z == 2){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_3[a]=gen_1[a];
                    } 
                }
                
                if (z == 3){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_4[a]=gen_1[a];
                    } 
                }
            }else if(rand > c_b[0] && rand < c_b[1] ){
                 if (z == 0){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_1[a]=gen_2[a];
                    } 
                }
                
                if (z == 1){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_2[a]=gen_2[a];
                    } 
                }
                
                if (z == 2){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_3[a]=gen_2[a];
                    } 
                }
                
                if (z == 3){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_4[a]=gen_2[a];
                    } 
                }
                
            }else if (rand > c_b[1] && rand < c_b[2]){
                 if (z == 0){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_1[a]=gen_3[a];
                    } 
                }
                
                if (z == 1){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_2[a]=gen_3[a];
                    } 
                }
                
                if (z == 2){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_3[a]=gen_3[a];
                    } 
                }
                
                if (z == 3){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_4[a]=gen_3[a];
                    } 
                }
                
            }else if (rand > c_b[2] && rand < c_b[3]){
                 if (z == 0){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_1[a]=gen_4[a];
                    } 
                }
                
                if (z == 1){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_2[a]=gen_4[a];
                    } 
                }
                
                if (z == 2){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_3[a]=gen_4[a];
                    } 
                }
                
                if (z == 3){
                for(int a = 0 ; a < 6 ;a++){
                       hijos_4[a]=gen_4[a];
                    } 
                }
            }
            
        }
        
        /*
        System.out.println("------------------------");
        System.out.println(Arrays.toString(hijos_1));
        System.out.println(Arrays.toString(hijos_2));
        System.out.println(Arrays.toString(hijos_3));
        System.out.println(Arrays.toString(hijos_4));
        System.out.println("------------------------");
        */
        
        //EVALUA SI HAY MUTACION EN CADA HIJO POR VALOR DE BINARIO Y CAMBIA DE 1 A 0 (0 A 1)
        for(int p = 0; p < 6; p++){
            if( p < pos1){
                hijo_1[p]=hijos_1[p];
                hijo_2[p]=hijos_2[p];
            }else if(p >= pos1){
                hijo_1[p]=hijos_2[p];
                hijo_2[p]=hijos_1[p];
            }
            
            if( p < pos2){
                hijo_3[p]=hijos_3[p];
                hijo_4[p]=hijos_4[p];
            }else if(p >= pos2){
                hijo_3[p]=hijos_4[p];
                hijo_4[p]=hijos_3[p];
            } 
        }
        
        for (int v = 0 ; v < 6 ; v++){
            if(xi_gen[(int) (Math.random()*xi_n)] > pdm){
                hijom_1[v]=hijo_1[v];
            }else{
                if(hijo_1[v]==1){
                    hijom_1[v]=0;
                }else if (hijo_1[v]==0){
                    hijom_1[v]=1;
                }
                cgen_1=cgen_1+1;
            }
            
            if(xi_gen[(int) (Math.random()*xi_n)] > pdm){
                hijom_2[v]=hijo_2[v];
            }else{
                if(hijo_2[v]==1){
                    hijom_2[v]=0;
                }else if (hijo_2[v]==0){
                    hijom_2[v]=1;
                }
                cgen_2=cgen_2+1;
            }
            
            if(xi_gen[(int) (Math.random()*xi_n)] > pdm){
                hijom_3[v]=hijo_3[v];
            }else{
                if(hijo_3[v]==1){
                    hijom_3[v]=0;
                }else if (hijo_3[v]==0){
                    hijom_3[v]=1;
                }
                cgen_3=cgen_3+1;
            }
            
            if(xi_gen[(int) (Math.random()*xi_n)] > pdm){
                hijom_4[v]=hijo_4[v];
            }else{
                if(hijo_4[v]==1){
                    hijom_4[v]=0;
                }else if (hijo_4[v]==0){
                    hijom_4[v]=1;
                }  
                cgen_4=cgen_4+1;
            }
            
             
            
            for(int t = 0; t <6 ; t++){
                gen_1[t]=hijom_1[t];
                gen_2[t]=hijom_2[t];
                gen_3[t]=hijom_3[t];
                gen_4[t]=hijom_4[t];  
            }
  
        }
        
        
        //AGREGA FINALMENTE A TABLA EL RESULTADO DE CADA HIJOCONSU MUTACION SI FUE DADA
      calculos.addRow(new Object[]{Arrays.toString(gen_1),Arrays.toString(hijo_1),Arrays.toString(hijom_1)});
      calculos.addRow(new Object[]{Arrays.toString(gen_2),Arrays.toString(hijo_2),Arrays.toString(hijom_2)});
      calculos.addRow(new Object[]{Arrays.toString(gen_3),Arrays.toString(hijo_3),Arrays.toString(hijom_3)});
      calculos.addRow(new Object[]{Arrays.toString(gen_4),Arrays.toString(hijo_4),Arrays.toString(hijom_4)});
      

   }
    // GRAFICA ACUMULADO DE MUTACIONES POR GEN CON METODO PAINT
    repaint();
    
   // JOptionPane.showMessageDialog(null,"El Gen 1 muto: "+cgen_1+" Gen 2: "+cgen_2+" Gen 3: "+cgen_3+" Gen 4: "+cgen_4);
    

 }
    
    
        @Override
    public void paint (Graphics g)
    {
        
        //DIBUJA EN EL PROGRAMA LO GRAFICO
        super.paint(g);
        //LINEAS
        g.setColor(Color.red);
        g.fillRect(490, 450, 310, 2);
        g.fillRect(490, 450, 2, -250);
        
        for(int i = 0; i < 26; i++){

          g.fillRect(480, 450-(i*10), 10, 1);
         
        }

        
        //VALORES
        g.setColor(Color.BLUE);
        g.drawString("GEN.1", 510, 465);
        g.drawString("GEN.2", 590, 465);
        g.drawString("GEN.3", 670, 465);
        g.drawString("GEN.4", 750, 465);
        
        //CONTADOR DE MUTS
        g.fillRect(500, 450, 50, (-cgen_1*2));
        g.fillRect(580, 450, 50, (-cgen_2*2));
        g.fillRect(660, 450, 50, (-cgen_3*2));
        g.fillRect(740, 450, 50, (-cgen_4*2));
        
        g.setColor(Color.GRAY);
        g.drawString("HISTOGRAMA DE MUTACIONES", 660, 200);
        g.drawString(""+cgen_1, 520, 445);
        g.drawString(""+cgen_2, 600, 445);
        g.drawString(""+cgen_3, 680, 445);
        g.drawString(""+cgen_4, 760, 445);

  
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMontecarlo = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGen = new javax.swing.JTable();
        btn_reset = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menu_archivo = new javax.swing.JMenu();
        menu_abrir = new javax.swing.JMenuItem();
        menu_guardar = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menu_generar = new javax.swing.JMenuItem();
        menu_generarXi = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(248, 248, 248));

        tblMontecarlo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Individuo", "Fenotipo", "Genotipo", "f(x)", "Ps", "Pa", "Clases"
            }
        ));
        jScrollPane1.setViewportView(tblMontecarlo);
        if (tblMontecarlo.getColumnModel().getColumnCount() > 0) {
            tblMontecarlo.getColumnModel().getColumn(3).setHeaderValue("f(x)");
            tblMontecarlo.getColumnModel().getColumn(4).setHeaderValue("Ps");
            tblMontecarlo.getColumnModel().getColumn(5).setHeaderValue("Pa");
            tblMontecarlo.getColumnModel().getColumn(6).setHeaderValue("Clases");
        }

        jLabel6.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        jLabel6.setText("Problema de Montecarlo - Virus H1N1");

        tblGen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Individuo", "Hijo", "Mutacion"
            }
        ));
        jScrollPane2.setViewportView(tblGen);

        btn_reset.setText("Reset");
        btn_reset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_reset))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(388, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(359, 359, 359))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_reset))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)))
                .addContainerGap())
        );

        menu_archivo.setText("Archivo");
        menu_archivo.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N

        menu_abrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        menu_abrir.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        menu_abrir.setText("Abrir");
        menu_abrir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menu_abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_abrirActionPerformed(evt);
            }
        });
        menu_archivo.add(menu_abrir);

        menu_guardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        menu_guardar.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        menu_guardar.setText("Guardar");
        menu_guardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menu_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_guardarActionPerformed(evt);
            }
        });
        menu_archivo.add(menu_guardar);
        menu_archivo.add(jSeparator1);

        menu_generar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        menu_generar.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        menu_generar.setText("Generar Ri");
        menu_generar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menu_generar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_generarActionPerformed(evt);
            }
        });
        menu_archivo.add(menu_generar);

        menu_generarXi.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        menu_generarXi.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        menu_generarXi.setText("Generar Xi");
        menu_generarXi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_generarXiActionPerformed(evt);
            }
        });
        menu_archivo.add(menu_generarXi);

        jMenuBar1.add(menu_archivo);

        jMenu2.setText("Guia");
        jMenu2.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menu_abrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_abrirActionPerformed
        try {
            abrirArchivo();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menu_abrirActionPerformed

    private void menu_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_guardarActionPerformed
        
    }//GEN-LAST:event_menu_guardarActionPerformed

    private void menu_generarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_generarActionPerformed
        Ventana_Ri ventanaRi = new Ventana_Ri();
        ventanaRi.setVisible(true);
    }//GEN-LAST:event_menu_generarActionPerformed

    private void menu_generarXiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_generarXiActionPerformed
        Ventana_Xi ventanaXi = new Ventana_Xi();
        ventanaXi.setVisible(true);
    }//GEN-LAST:event_menu_generarXiActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        dispose();
        VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
        ventanaPrincipal.setVisible(true);
       
    }//GEN-LAST:event_btn_resetActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_reset;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuItem menu_abrir;
    private javax.swing.JMenu menu_archivo;
    private javax.swing.JMenuItem menu_generar;
    private javax.swing.JMenuItem menu_generarXi;
    private javax.swing.JMenuItem menu_guardar;
    private javax.swing.JTable tblGen;
    private javax.swing.JTable tblMontecarlo;
    // End of variables declaration//GEN-END:variables
}
