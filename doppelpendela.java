import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import java.applet.*;

public class doppelpendela extends Applet {
  //
  // (c) 1998 by U.Schwickerath 
  //
  
  Panel controls;
  PCanvas canvas;
  public String ms1,ms2,gs1,gs2;

  
  protected double get_double(String name, double defval){
    String value = this.getParameter(name);
    if (value == null) return defval;
    try {return Double.valueOf(value).doubleValue();}
    catch (Exception e) {return defval;}
  }

  protected int get_integer(String name, int defval){
    String value = this.getParameter(name);
    if (value == null) return defval;
    try {return Integer.parseInt(value);}
    catch (Exception e) {return defval;}
  }
  
  public void params(){
  }
  
  public void init(){
    int xm0,ym0,xp1,xp2,yp1,yp2;
    
    setBackground(Color.white);
    setLayout(new BorderLayout());
    //first create and 
    //init the thing

    xp1=get_integer("xp1",-150);
    xp2=get_integer("xp2",-150);
    yp1=get_integer("yp1",200);
    yp2=get_integer("yp2",200);
    xm0=get_integer("xm",100);
    ym0=get_integer("ym",100);

    canvas   = new PCanvas(xm0,ym0,xp1,xp2,yp1,yp2,xp1,xp2,yp1,yp2,50.,50.);
    canvas.pendel = new doppelpendel();
    
    canvas.pendel.xm=xm0;
    canvas.pendel.ym=ym0;

    canvas.pendel.x1a=xp1;
    canvas.pendel.x2a=xp2;
    canvas.pendel.y1a=yp1;
    canvas.pendel.y2a=yp2;
    
    canvas.pendel.l1=canvas.pendel.len1o();
    canvas.pendel.l2=canvas.pendel.len2o();
    canvas.pendel.f1=canvas.pendel.phi1o();
    canvas.pendel.f2=canvas.pendel.phi2o();
    canvas.pendel.f1o=canvas.pendel.phi1o();
    canvas.pendel.f2o=canvas.pendel.phi2o();

    canvas.pendel.w1o=get_double("ohm1",0.);
    canvas.pendel.w2o=get_double("ohm2",0.);    
    canvas.pendel.g=get_double("g",9.81);
    canvas.pendel.h=get_double("h",0.02);
    canvas.pendel.h2=get_double("h2",0.02);



    ms1 = getParameter("m1");
    ms2 = getParameter("m2");
    canvas.setmass1(ms1);
    canvas.setmass2(ms2);
    gs1 = getParameter("ohm1");
    gs2 = getParameter("ohm2");

    controls = new Panel();
    controls.setLayout(new GridLayout(4,1));
    controls.add(canvas.controlsm1 =  new ControlsMass1(ms1,canvas));
    controls.add(canvas.controlsm2 =  new ControlsMass2(ms2,canvas));
    controls.add(canvas.controlsv1 = new ControlsVelo1(gs1,canvas));
    controls.add(canvas.controlsv2 = new ControlsVelo2(gs2,canvas));
    add("Center",canvas);
    add("East", controls);
  }
}

class PCanvas extends doppelpendel {
  
  static public doppelpendel pendel;
  static int xm,ym,x1,x2,y1,y2;
  static int       x1a,x2a,y1a,y2a;
  ControlsMass1 controlsm1;
  ControlsMass2 controlsm2;
  ControlsVelo1 controlsv1;
  ControlsVelo2 controlsv2;
  static int r1,r2;
  static int r1o,r2o;
  static double m1,m2;
  private static boolean drag1,drag2;
  
  
  public PCanvas(int xm, int ym, int x1, int x2, int y1, int y2,
		 int x1a,int x2a,int y1a,int y2a, double m1, double m2){
    this.xm=xm;
    this.ym=ym;
    this.x1=x1;
    this.x2=x2;
    this.y1=y1;
    this.y2=y2;
    this.x1a=x1a;
    this.x2a=x2a;
    this.y1a=y1a;
    this.y2a=y2a;
    this.m1=m1;
    this.m2=m2;
    this.r1=(int) Math.round((double) Math.pow(m1,1/2.));
    this.r2=(int) Math.round((double) Math.pow(m2,1/2.));
    this.r1o=r1;
    this.r2o=r2;
    drag1=false;
    drag2=false;
    enableEvents(AWTEvent.MOUSE_EVENT_MASK |
		 AWTEvent.MOUSE_MOTION_EVENT_MASK |
		 AWTEvent.COMPONENT_EVENT_MASK |
		 AWTEvent.KEY_EVENT_MASK);
    
  }
  public PCanvas(){
    drag1=false;
    drag2=false;
    enableEvents(AWTEvent.MOUSE_EVENT_MASK |
		 AWTEvent.MOUSE_MOTION_EVENT_MASK |
		 AWTEvent.COMPONENT_EVENT_MASK |
		 AWTEvent.KEY_EVENT_MASK);
    
  }
  
  public void setvelo1(String gm1){
    try{pendel.w1o=Double.valueOf(gm1).doubleValue();}
    catch (Exception e) {return;}
  }
  
  public void setvelo2(String gm2){
    try{pendel.w2o=Double.valueOf(gm2).doubleValue();}
    catch (Exception e) {return;}    
  }

  public void setmass1(String mass1){
    try{pendel.m1=Double.valueOf(mass1).doubleValue();}
    catch (Exception e) {return;}
    try{redraw(xm,ym,x1a,x2a,y1a,y2a,
	       x1,x2,y1,y2,pendel.m1,pendel.m2);}
    catch (Exception e) {return;}    
  }

  public void setmass2(String mass2){
    try{pendel.m2=Double.valueOf(mass2).doubleValue();}
    catch (Exception e) {return;}
    try{redraw(xm,ym,x1a,x2a,y1a,y2a,
	       x1,x2,y1,y2,pendel.m1,pendel.m2);}
    catch (Exception e) {return;}    
  }

  public void paint_me(){
    double f1,f2,w1,w2;
    int x1n,y1n,x2n,y2n;
    x1a=x1;
    y1a=y1;
    x2a=x2;
    y2a=y2;
    x1n=pendel.ix1();
    y1n=pendel.iy1();
    x2n=pendel.ix2();
    y2n=pendel.iy2();
    this.x1=x1n;
    this.y1=y1n;
    this.x2=x2n;
    this.y2=y2n;
    
    redraw(pendel.xm,pendel.ym,x1a,x2a,y1a,y2a,
	   x1n,x2n,y1n,y2n,pendel.m1,pendel.m2);
    
    pendel.x1a=x1a;
    pendel.y1a=y1a;
    pendel.x2a=x2a;
    pendel.y2a=y2a;
    
    f1 = pendel.f1n();
    f2 = pendel.f2n();
    w1 = pendel.w1n();
    w2 = pendel.w2n();
    
    pendel.f1o=f1;
    pendel.f2o=f2;
    pendel.w1o=w1;
    pendel.w2o=w2;
    controlsv1.updatew1(pendel.w1o);
    controlsv2.updatew2(pendel.w2o);
  }
  
  
  public void processKeyEvent(KeyEvent e){
    paint_me();
  }
  
  public void processMouseEvent(MouseEvent e){
    int lastx=0,lasty=0;
    if (e.getID()==MouseEvent.MOUSE_PRESSED){      
      lastx = e.getX();
      lasty = e.getY();
      if (((int) Math.abs(lastx-pendel.x1a) < 10) &&
	  ((int) Math.abs(lasty-pendel.y1a) < 10)) {
	drag1 = true;
	drag2 = false;
      }
      else if (((int) Math.abs(lastx-pendel.x2a) < 10) &&
	       ((int) Math.abs(lasty-pendel.y2a) < 10)) {
	drag2 = true;
	drag1 = false;
      }
      else {
	drag1=false;
	drag2=false;
      }
    }
    else super.processMouseEvent(e);
  }
  
  public void processMouseMotionEvent(MouseEvent e){
    if ((drag1 | drag2 ) && (e.getID() == MouseEvent.MOUSE_DRAGGED)){
      if (drag1){
	pendel.x1a=e.getX();
	pendel.y1a=e.getY();
      }
      else if (drag2) {
	pendel.x2a=e.getX();
	pendel.y2a=e.getY();
      }
      pendel.l1=pendel.len1o();
      pendel.l2=pendel.len2o();
      pendel.f1=pendel.phi1o();
      pendel.f2=pendel.phi2o();
      pendel.f1o=pendel.phi1o();
      pendel.f2o=pendel.phi2o();
      // reset also starting velocity
      pendel.w1=0.;
      pendel.w2=0.;
      pendel.w1o=0.;
      pendel.w2o=0.;
      redraw(xm,ym,x1,x2,y1,y2,
	     pendel.x1a,pendel.x2a,pendel.y1a,pendel.y2a,pendel.m1,pendel.m2);
    }
    else super.processMouseMotionEvent(e);
  }
  
  public void paint(Graphics gr){
    loeschen(gr,xm,ym,x1a,y1a,x2a,y2a);
    malen(gr,xm,ym,x1,y1,x2,y2 );
  }
  
  public void update(Graphics gr){
    paint(gr);
  }
  
  public void malen(Graphics gr,int x0, int y0, int x1, int y1, int x2, int y2){
    gr.setColor(Color.black);
    gr.drawLine(x0,y0,x1,y1);
    gr.drawLine(x1,y1,x2,y2);
    gr.drawLine(x0-20,y0,x0+20,y0);
    gr.drawLine(x0-25,y0-10,x0-20,y0);    
    gr.drawLine(x0-20,y0-10,x0-15,y0);    
    gr.drawLine(x0-15,y0-10,x0-10,y0);    
    gr.drawLine(x0-10,y0-10,x0-5,y0);    
    gr.drawLine(x0-5,y0-10,x0-0,y0);    
    gr.drawLine(x0+0,y0-10,x0+5,y0);    
    gr.drawLine(x0+5,y0-10,x0+10,y0);    
    gr.drawLine(x0+10,y0-10,x0+15,y0);    
    gr.drawLine(x0+15,y0-10,x0+20,y0);    
    gr.setColor(Color.red);
    gr.fillArc(x1-r1/2,y1-r1/2,r1,r1,0,360);
    gr.setColor(Color.green);
    gr.fillArc(x2-r2/2,y2-r2/2,r2,r2,0,360);
  }
  
  public void loeschen(Graphics gr,int x0, int y0, int x1, int y1, int x2, int y2){
    gr.setColor(Color.white);
    gr.fillArc(x1-r1o/2,y1-r1o/2,r1o,r1o,0,360);
    gr.fillArc(x2-r2o/2,y2-r2o/2,r2o,r2o,0,360);
    gr.drawLine(x0,y0,x1,y1);
    gr.drawLine(x1,y1,x2,y2);
  }
  
  public void redraw(int xm, int ym, int x1a, int x2a, int y1a, int y2a,
		                     int x1,int x2,int y1,int y2, double m1, double m2){
    this.xm=xm;
    this.ym=ym;
    this.x1=x1;
    this.x2=x2;
    this.y1=y1;
    this.y2=y2;
    this.x1a=x1a;
    this.x2a=x2a;
    this.y1a=y1a;
    this.y2a=y2a;
    this.m1=m1;
    this.m2=m2;
    r1o=r1;
    r2o=r2;
    this.r1=(int) Math.round((double) Math.pow(m1,1/2.));
    this.r2=(int) Math.round((double) Math.pow(m2,1/2.));
    Graphics g=this.getGraphics();
    paint(g);
  }  
}

class ControlsVelo1 extends Panel
                     implements ActionListener {
  
  TextField gesc1;
  String gs1;
  PCanvas canvas;
  
  public ControlsVelo1(String gs1,PCanvas canvas){
    this.canvas=canvas;
    Button b=null;
    Label l3=null;
    this.gs1=gs1;
    add(l3 = new Label("Geschw. m1:"));
    add(gesc1 = new TextField(gs1, 20));
    
    b = new Button("OK");
    b.addActionListener(this);
    add(b);
    canvas.setvelo1(gs1);
  }

  public void updatew1(double v){gesc1.setText(String.valueOf(v));}
		       
  public void actionPerformed(ActionEvent ev) {
    String label = ev.getActionCommand();
    if (label.equals("OK")){
      gs1=(gesc1.getText().trim());
      canvas.setvelo1(gs1);
    }
  }
}  


class ControlsVelo2 extends Panel
                     implements ActionListener {
  
  TextField gesc2;
  String gs2;
  PCanvas canvas;
  
  public ControlsVelo2(String gs2,PCanvas canvas){
    this.gs2=gs2;
    this.canvas=canvas;
    Button b = null;
    Label  l4 =null;
    add(l4 = new Label("Geschw. m2:"));
    add(gesc2 = new TextField(gs2, 20));
    
    b = new Button("OK");
    b.addActionListener(this);
    add(b);
    canvas.setvelo2(gs2);
  }

  public void updatew2(double v){gesc2.setText(String.valueOf(v));}
		       
  public void actionPerformed(ActionEvent ev) {
    String label = ev.getActionCommand();
    if (label.equals("OK")){
      gs2=(gesc2.getText().trim());
      canvas.setvelo2(gs2);
    }
  }
}  


class ControlsMass1 extends Panel
                     implements ActionListener {
  
  TextField mass1;
  String ms1;
  PCanvas canvas;

  public ControlsMass1(String ms1,PCanvas canvas){
    this.ms1=ms1;
    this.canvas=canvas;
    Button b = null;
    Label  l1 =null;
    add(l1 = new Label("erste Masse:"));
    add(mass1 = new TextField(ms1, 4));
    
    b = new Button("OK");
    b.addActionListener(this);
    add(b);
    canvas.setmass1(ms1);
  }

  public void actionPerformed(ActionEvent ev) {
    String label = ev.getActionCommand();
    String ms1;
    if (label.equals("OK")){
      ms1=(mass1.getText().trim());
      canvas.setmass1(ms1);
    }
  }
}  

class ControlsMass2 extends Panel
                     implements ActionListener {
  
  TextField mass2;
  String ms2;
  PCanvas canvas;

  public ControlsMass2(String ms2,PCanvas canvas){
    this.ms2=ms2;
    this.canvas=canvas;
    Button b = null;
    Label  l2 =null;
    add(l2 = new Label("zweite Masse:"));
    add(mass2 = new TextField(ms2, 4));
    
    b = new Button("OK");
    b.addActionListener(this);
    add(b);
    canvas.setmass2(ms2);
  }

  public void actionPerformed(ActionEvent ev) {
    String label = ev.getActionCommand();
    String ms2;
    if (label.equals("OK")){
      ms2=(mass2.getText().trim());
      canvas.setmass2(ms2);
    }
  }
}  


