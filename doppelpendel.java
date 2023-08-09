import java.awt.*;
import java.lang.*;

public class doppelpendel extends Canvas{       
  
  public int x1a=0,x2a=0,y1a=0,y2a=0;
  public double len1o=0.,len2o=0.,phi1o=0.,phi2o=0.;
  public int ix1, iy1, ix2, iy2;

  public int xm,ym;
  public double f1,f2,w1,w2;
  public double m1,m2,l1,l2;
  
  public double m,l1l2;
  public double f1o,  f2o, w1o, w2o;
  public double f1n,  f2n, w1n, w2n;
  public double k11, k12, k13, k14, k21, k22, k23, k24, k31, k32, k33, k34, k41, k42, k43, k44;
  public double g,h,h2;
  
  public double sn, cs, df, dw, t1, t2, t3, t4, g1, g2, g3, g4;

  public double m(){return m1+m2;}
  public double l1l2(){return l1*l2;}
  
  public double w1func(double f1,double f2, double w1, double w2) {return w1;}
  public double w2func(double f1,double f2, double w1, double w2) {return w2;}
  public double fx(double f1,double f2, double w1, double w2) {
    df = f1 - f2;
    dw = w1 - w2;
    sn = (double) Math.sin(df);
    cs = (double) Math.cos(df);
    t1 = m1 * l2 * w1 * w2 * sn / (l1 * m());
    t2 = g * (double) Math.sin(f1) / l1;
    t3 = cs * (l1 * w1 * w1 * sn - g * (double) Math.sin(f2)) / (m() * l1l2());
    t4 = 1 + cs * cs / (m() * l2);
    return -((t1 + t2 + t3) / t4);
  }
  
  public double gx(double f1,double f2, double w1, double w2) {
    df = f1 - f2;
    dw = w1 - w2;
    sn = (double) Math.sin(df);
    cs = (double) Math.cos(df);
    g1 = (l1 * w1 * w1 * sn - g * (double) Math.sin(f2)) / l2;
    g2 = m2 / m() * w1 * w2 * cs * sn;
    g3 = g / l2 * (double) Math.sin(f1) * cs;
    g4 = 1 + l1 / l2 * cs * cs * m2 / m();
    return (g1 + g2 + g3) / g4;
  }
  
  
  public double k11() {return w1func(f1o,f2o,w1o,w2o);}
  public double k12() {return w2func(f1o,f2o,w1o,w2o);}
  public double k13() {return fx(f1o,f2o,w1o,w2o);}
  public double k14() {return gx(f1o,f2o,w1o,w2o);}
  
  public double k21() {return w1func( f1o + h2 * k11(),f2o + h2 * k12(),w1o + h2 * k13(),w2o + h2 * k14());}
  public double k22() {return w2func( f1o + h2 * k11(),f2o + h2 * k12(),w1o + h2 * k13(),w2o + h2 * k14());}
  public double k23() {return fx( f1o + h2 * k11(),f2o + h2 * k12(),w1o + h2 * k13(),w2o + h2 * k14());}
  public double k24() {return gx( f1o + h2 * k11(),f2o + h2 * k12(),w1o + h2 * k13(),w2o + h2 * k14());}
  
  public double k31() {return w1func(f1o + h2 * k21(),f2o + h2 * k22(),w1o + h2 * k23(),w2o + h2 * k24());}
  public double k32() {return w2func(f1o + h2 * k21(),f2o + h2 * k22(),w1o + h2 * k23(),w2o + h2 * k24());}
  public double k33() {return fx( f1o + h2 * k21(),f2o + h2 * k22(),w1o + h2 * k23(),w2o + h2 * k24());}
  public double k34() {return gx( f1o + h2 * k21(),f2o + h2 * k22(),w1o + h2 * k23(),w2o + h2 * k24());}
  
  public double k41() {return w1func( f1o + h2 * k31(),f2o + h2 * k32(),w1o + h2 * k33(),w2o + h2 * k34());}
  public double k42() {return w2func( f1o + h2 * k31(),f2o + h2 * k32(),w1o + h2 * k33(),w2o + h2 * k34());}
  public double k43() {return fx( f1o + h2 * k31(),f2o + h2 * k32(),w1o + h2 * k33(),w2o + h2 * k34());}
  public double k44() {return gx( f1o + h2 * k31(),f2o + h2 * k32(),w1o + h2 * k33(),w2o + h2 * k34());}
  
  public double f1n() {return f1o +  h * (k11()	+ 2 * k21() + 2 * k31()	+ k41()) / 6;}
  public double f2n() {return f2o +  h * (k12()	+ 2 * k22() + 2 * k32()	+ k42()) / 6;}
  public double w1n() {return w1o +  h * (k13()	+ 2 * k23() + 2 * k33()	+ k43()) / 6;}
  public double w2n() {return w2o +  h * (k14()	+ 2 * k24() + 2 * k34()	+ k44()) / 6;}
  
  public int ix1() {return xm + (int) Math.round(l1 * (double) Math.sin(f1n()));}
  public int iy1() {return ym + (int) Math.round(l1 * (double) Math.cos(f1n()));}
  public int ix2() {return ix1() + (int) Math.round(l2 * (double) Math.sin(f2n()));}
  public int iy2() {return iy1() + (int) Math.round(l2 * (double) Math.cos(f2n()));}

  public double len1o()	{return (double) Math.sqrt((x1a-xm)*(x1a-xm)+(y1a-ym)*(y1a-ym));}
  public double len2o()	{return (double) Math.sqrt((x2a-x1a)*(x2a-x1a)+(y2a-y1a)*(y2a-y1a));}
  public double phi1o()	{
    double c1,c2;
    double ang;
    c1=(x1a-xm);
    c2=(y1a-ym);
    ang=datan(c1,c2);
    return ang;
  }
  public double phi2o()	{
    double c1,c2;
    double ang;
    c1=(x2a-x1a);
    c2=(y2a-y1a);
    ang=datan(c1,c2);    
    return ang;
  }
  public double datan(double y,double x){
    double ang;
    double pi=4.*(double) Math.atan(1.);
    
    ang=(double) Math.atan((double) Math.abs(y/x));
    if (x < 0) { 
      if ((y >0)) {
	ang=pi-ang;
      }
      else {
	ang=-pi+ang;
      }
    }      
    else
      {if (y<0) ang=-ang;}
    return ang;
  }
}

