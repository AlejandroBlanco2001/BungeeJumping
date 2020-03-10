import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 
import javax.swing.*; 
import java.lang.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class BongeeJumping_IsaacBlanco_AlexFigueroa_IlamRojas extends PApplet {

/* //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>//
 CONVERSIONES TIEMPOS, ALTURAS..... INGAME 
 -  720 Pixeles -> 300 metros
 -  1 Pixeles -> 0.4166666667 metros;
 
 -  1 segundos de la vida real -> 0.1 segundos de processing
 -  ? segundos de la vida real -> 0.2 segundos de proccesing
 
 - 1N/m = 2.4 N/p
 
 
 - 1m/s -> (1p/0.4166666667m)*(1sp/10s);
 - 1m/s -> 0.24 p/sp
 - 2 m/s -> 0.48 p/sp
 
 CONVERSIONES ESCALAS GRAFICAS
 - 200 Metros es la escala, de 20 en 20 metros
 - Puntos de traslacion es [0,0] de nuestra grafica
 - Posicion vs Tiempo :   
 - 130 segundos -> 360 pixeles
 - x segundos -> y pixeles
 - 180 metro -> 150 pixeles   
 - 50 metros ->  x pixeles  
 - Velocidad vs Tiempo :
 - 54 m/s -> 110 pixeles
 - y m/s -> x pixeles
 */

// Importaciones Necesarias

// Sonido






 


Minim minim;
AudioPlayer player;

// Declaracion de Tablas, Vector De posicion y velocidad, Tipografias e Imagenes.
Table tabla_fase1;
Table tabla_fase2;
PVector posicion;
PFont letra2, letra;
PVector velocidad_v;
PImage instrucciones, previa_mapa, personaje_caminando_derecho1, personaje_caminando_derecho, personaje_caminando_derecho2, personaje_caminando_derecho3, personaje_de_pie, cuerda_img, persona_cayendo_jalon_arriba, persona_cayendo, soporte_cuerda, game_over_ui, fondo_1, fondo_2, fondo_3, fondo_4, datos, puente, bungee_j, boton_menu, fondo, bg, buttons_r, buttons_l, bungee_l;

// Declaracion de arreglos donde se guardaran las posciones, tiempo y velocidad de las fases.
float[] posiciones_antiguas_y = new float[2000];
float[] velocidad_instante = new float[2000];
float[] tiempo_graf = new float[2000];
float[] tiempo_graf_2 = new float[2000];
float[] velocidad_instante_2 = new float[2000];
float[] posiciones_antiguas_y_2 = new float[2000];
PImage[] animacion_caminando = {personaje_caminando_derecho, personaje_caminando_derecho1, personaje_caminando_derecho2, personaje_caminando_derecho3};

// Declaracion de variables a utilizar
float puente_a, cronometro_fase2, posicion_2, velocidad_2, posicion_2v, velocidad_2v;
float p1, p2, p3, p4;
String respuesta, marcas;
int puente_con, previa_map, contador_fondo, aumento, adelanto_ani, contador_musica, cuerda, contador_saltos, altura_puente, contador_ingresos_vector_fase2, contador_a, i, contador_ingreos_vector_fase1, gamestate, contador; 
float C2_f1, C1_f1, posicion_traslacion_plano, posicion_ante_2;
float altura_persona, c1, c2, beta, alfa, constante_k;
float velocidad_n, posicion_n, aire, gravedad_v, velo_vector, posicion_y, posicion_puntos_y, cronometro, tiempo, velocidad_final, fuerza, masa, gravedad, coeficiente_r, velocidad;
boolean instru, pausa_moviento, game_over, pausa, fase2, menu_musica, accedio, reiniciar, saca_las_graficas, dejacaer, para;


public void setup() {

  // Dato importar a Excel
  tabla_fase1 = new Table();
  tabla_fase1.addColumn("Tiempo (s)");
  tabla_fase1.addColumn("Masa (kg)");
  tabla_fase1.addColumn("Cantidad de Cuerda (m)");
  tabla_fase1.addColumn("Altura (m)");
  tabla_fase1.addColumn("Posicion (m)");
  tabla_fase1.addColumn("Velocidad (m/s)");
  tabla_fase1.addColumn("Constante de Elasticidad (N/m)");
  tabla_fase1.addColumn("Constante del Aire");
  tabla_fase1.addColumn("Gravedad (m/s2)");

  tabla_fase2 = new Table();
  tabla_fase2.addColumn("Tiempo (s)");
  tabla_fase2.addColumn("Masa (kg)");
  tabla_fase2.addColumn("Cantidad de Cuerda (m)");
  tabla_fase2.addColumn("Altura (m)");
  tabla_fase2.addColumn("Posicion (m)");
  tabla_fase2.addColumn("Velocidad (m/s)");
  tabla_fase2.addColumn("Constante de Elasticidad (N/m)");
  tabla_fase2.addColumn("Constante del Aire");
  tabla_fase2.addColumn("Gravedad (m/s2)");

  //Configuracion Pantalla
  

  //* Audio
  minim = new Minim(this);
  player = minim.loadFile("data/Resources/Sounds/Music/cancion_1.mp3");
  player.shiftGain(player.getGain(), -7, 200); // Ganancia del Volumen, se usa para bajarlo

  // Valores iniciales de los vectores, tomando el vector de posicion como el eje (0,0) y la velocidad con 1m/s
  posicion = new PVector(0, 0);
  velocidad_v = new PVector(0, 0.24f);

  // Declaracion de variables booleanas
  pausa = false;
  fase2 = false;
  accedio = false;
  reiniciar = false;
  saca_las_graficas = false;
  dejacaer = false;
  game_over = false;
  pausa_moviento = false;
  instru = false;

  // Declaracion de variables de tipo numerico
  adelanto_ani = 15;
  gravedad = 9.81f;
  gravedad_v = 0.2f;
  posicion_y = 0;
  contador = 0;
  contador_ingresos_vector_fase2 = 0;
  contador_musica = 1;
  gamestate = -1;
  contador_ingreos_vector_fase1 = 0;
  contador_saltos = 0;
  previa_map = 1;
  aumento = 1;
  contador_fondo= 1;
  puente_con = 1;

  // Declaracion de imagenes y tipografias
  bg = loadImage("data/Resources/Backgrounds/background_image_1.jpg");  // fondo del Juego
  buttons_r = loadImage("data/Resources/Characters/pixil-frame_d.png"); // Flecha derecha
  buttons_l = loadImage("data/Resources/Characters/pixil-frame_i.png"); // Flecha izquierda
  fondo = loadImage("data/Resources/Backgrounds/background_menu.jpg");  // Fondo de la pantalla de Carga
  fondo_1 = loadImage("data/Resources/Backgrounds/background_menu_1.jpg"); 
  fondo_2 = loadImage("data/Resources/Backgrounds/background_menu_2.jpg");
  fondo_3 = loadImage("data/Resources/Backgrounds/background_menu_3.jpg");
  fondo_4 = loadImage("data/Resources/Backgrounds/background_menu_4.jpg");
  game_over_ui = loadImage("data/Resources/Backgrounds/game_over.png");
  boton_menu = loadImage("data/Resources/Characters/boton_m.png");
  puente = loadImage("data/Resources/Characters/bridge_1.png");
  datos = loadImage("data/Resources/Backgrounds/imagen_datos.jpg");
  soporte_cuerda = loadImage("data/Resources/Characters/soporte.png");
  persona_cayendo = loadImage("data/Resources/Characters/persona_cayendo.png");
  persona_cayendo_jalon_arriba = loadImage("data/Resources/Characters/persona_cayendo_jalon_arriba.png");
  cuerda_img = loadImage("data/Resources/Characters/cuerda.png");
  personaje_de_pie = loadImage("data/Resources/Characters/personaje_de_pie.png");
  personaje_caminando_derecho = loadImage("data/Resources/Characters/personaje_caminando_derecho1.png");
  personaje_caminando_derecho1 = loadImage("data/Resources/Characters/personaje_caminando_derecho2.png");
  personaje_caminando_derecho2 = loadImage("data/Resources/Characters/personaje_caminando_derecho3.png");
  personaje_caminando_derecho3 = loadImage("data/Resources/Characters/personaje_caminando_derecho4.png");
  previa_mapa = loadImage("data/Resources/Backgrounds/miniatura_mapa_1.png");
  instrucciones = loadImage("data/Resources/Backgrounds/instrucciones.png");
  letra2 = loadFont("ArialMT-48.vlw");
}

//////////////////////////////////////////////////////// SUBRUTINAS PRINCIPALES DE EJECUCION  /////////////////////////////////////////

// 60FPS
public void draw() {

  // Verificar que el juego aun no ha comenzado
  if (gamestate != -1) {
    translate(276, 125);
  }

  // Verificar que aun se encuentra en el menu de carga
  if (gamestate == -1) {
    clear();
    instru = false;
    translate(0, 0);
    Menu_Inicio();
    player.play();
    animacion_fondo(); // Subrutina encargada de cambiar las imagenes para generar el efecto de animacion
  }

  //* Verificar que se dibuje todo de nuevo
  if (para == true) {
    CreacionObjetos();
  }

  // Verificar que se presiono el boton de Lanzarse
  if (dejacaer == true) {
    coeficiente_r = masa*gravedad/velocidad; 
    Caida_Fase1();
  }

  // Verificar que se presiono "R"
  if (reiniciar == true) {
    CreacionObjetos();
    Reiniciar();
    reiniciar = false;
  }

  // Verificar que se inicio la transcion a la fase 2
  if (fase2 == true) {
    Caida_Fase2();
  }

  // Verificar que el usuario no haya muerto
  if (game_over == true) {
    GameOver();
    noLoop();
  }

  if (instru == true) {
    clear();
    background(instrucciones);
  }

  // Verificar si la cancion no ha terminado

  /* 
   Debido a que se puede tratar el reproductor como un vector, si su posicion es igual que su tamaño 
   signfica que se acabo la cancion, por lo tanto, pasa a la siguiente
   */
  if (player.position() >= player.length()-400) {
    if (contador_musica == 7) {
      contador_musica = 0;
    }
    contador_musica += 1; 
    player = minim.loadFile("data/Resources/Sounds/Music/cancion_"+contador_musica+".mp3");
    player.shiftGain(player.getGain(), -7, 200);
    player.play();
  }
}


//////////////////////////////////////////////////////// FUNCIONES Y SUBRUTINAS PROPIAS  /////////////////////////////////////////


// Realizar la caida libre con friccion del aire sin accion de la cuerda
public void Caida_Fase1() {
  String velocidad, tiempo_t, pos; // Variables locales para imprimir en textos los valores numericos de la misma
  background(bg);
  CreacionEcuaciones_Fase1();
  image(soporte_cuerda, -115, altura_persona);
  imageMode(CENTER);
  image(puente, -228, puente_a);
  fill(0xffb805fa);
  rect(540, 235, 535, 720);
  Calcular_fuerza("neta fase 1"); // Se llama a la subrutina Calcular_Fuerza para que calcule la velocidad de este movimiento
  velocidad_v.y =  velo_vector; // Se le asigna al vector engcargado de la velocidad, la reciente velocidad obtenida.
  posicion.add(0, velocidad_v.y);
  velocidad = str(velocidad_n);
  tiempo_t = str(cronometro/0.1f);
  pos = str(((300*posicion.y)/720));
  System.out.println("La velocidad en el instante x= " + ((300*posicion.y)/720) + "m con t = " + cronometro/0.1f + " es de " + velocidad_n + " m/s");
  pushMatrix(); // Se guarda el sistema de referencia actual del programa
  translate(0, posicion_traslacion_plano); // Se traslada este mismo a la posicion 0, con la respectiva altura que haya escogido la persona
  image(persona_cayendo, posicion.x, posicion.y); 
  line(0, 0, 0, posicion.y-5);
  popMatrix(); // Se "explota" la matriz actual y se devuelve a la original que se manejo antes de este proceso
  strokeWeight(4);
  fill(0xff000000);
  strokeWeight(1);
  fill(0xffffffff);
  rect(400, 495, 185, 35);
  rect(400, 545, 185, 35);
  rect(600, 520, 185, 35);
  textFont(letra2);
  textSize(16);
  fill(0xfff7b016);
  // Se transforma los valores numeros convertidos a metros para ser impresos
  text("Posicion: " + pos + " m", 400, 500);
  text("Tiempo: " + tiempo_t + " s", 400, 550);
  text("Velocidad: " + velocidad + " m/s", 600, 525);
  velocidad_instante[contador_ingreos_vector_fase1] = velocidad_v.y*0.24f; 
  posiciones_antiguas_y[contador_ingreos_vector_fase1] = posicion.y*300/720;
  if (posicion.y>= cuerda) { // Se verifica si la persona ya llego a la posicion que indico como cuerda para comenzar la fase 2
    if (posicion.y*300/720 > altura_puente) { // Se verifica si la persona no se choca contra el piso debido a que su posicion es mayor que el limite de la pantalla
      game_over = true; // Si pasa se acaba el juego
    } else {
      dejacaer = false; 
      // Se dispone llenar una tabla tomando los datos necesarios para ser exportada como ".csv" pasando los vectores como parametros que contiene todos los datos
      LLenarTabla(tiempo_graf, velocidad_instante, posiciones_antiguas_y, tabla_fase1, contador_saltos, "fase 1", contador_ingreos_vector_fase1);
      contador_ingreos_vector_fase1 = 0;
      fase2 = true;
      // Se le pasa la velocidad y el tiempo final como parametro, para poder hallar las ecuaciones de la fase 2 para cada caso particular
      CreacionEcuaciones_Fase2(cronometro, velocidad_n);
      // El tiempo se comparte entre las fases, se puede eliminar esta linea, si se cambia en Calcular_fuerza, la variable y en el momento de imprimirlas
      cronometro_fase2 = cronometro/0.1f;
    }
  }
  // Se verifica si la persona aun no ha llegado a la distancia de la cuerda
  if (((300*posicion.y)/720) <= cuerda) {
    cronometro = cronometro + 0.01f;
    tiempo_graf[contador_ingreos_vector_fase1] = cronometro/0.1f;
    // Se crean las graficas en tiempo real llamando a la funcion Crear_Graficas
    Crear_Graficas("fase 1", "Tiempo", "Posicion", "Velocidad", tiempo_graf, posiciones_antiguas_y, velocidad_instante, contador_ingreos_vector_fase1);    
    // Al no saber la cantidad de elementos a disponer en esta misma, se incrementa el indice hasta que el juego salte a la fase 2, la persona reinicie o se muera.
    contador_ingreos_vector_fase1 = contador_ingreos_vector_fase1 + 1;
    rectMode(CENTER);
    fill(0xffffffff);
  }
}

// Encargado de generar la animacion de la caida con tensiom
public void Caida_Fase2() {
  String velocidad, tiempo, pos;
  background(bg);
  fill(0xff03fc31);
  image(soporte_cuerda, -115, altura_persona);
  imageMode(CENTER);
  image(puente, -228, puente_a);
  fill(0xffb805fa);
  rect(540, 235, 535, 720);
  stroke(33);
  fill(0xfffc030b);
  // Se calcula alfa y beta como parte de la ecuacion general de la ecuacion y se redondean sus decimales a 2
  alfa = -coeficiente_r/(2*masa);
  beta = pow(coeficiente_r, 2)-(4*masa*constante_k);
  beta = sqrt(beta * -1)/(2*masa); // Debido a que no se puede pasar una raiz negativa, se multiplica por -1
  beta = redondearDecimales(beta)-0.01f;
  Calcular_fuerza("neta fase 2"); // Se calcula la velocidad en cada instante llamando a la subrutina Calcular_fuerza
  if (posicion.y >= 584) { // Se verifica si la persona no se ha golpeado contra el fondo de la pantalla
    GameOver(); // Se llama a la subrutina GameOver
    noLoop(); // Se dispone a ejecutar la instruccion draw una vez mas, para no hacerlo mas.
  } else {
    posicion.add(0, velocidad_v.y);
    velocidad_v.y = velocidad_2v;
  }
  fill(0xfffc030b);
  pushMatrix(); // Se guarda el sistema de referencia actual del programa
  translate(0, posicion_traslacion_plano); // Se traslada este mismo a la posicion 0, con la respectiva altura que haya escogido la persona
  if (velocidad_2 > 0) { // Si la velocidad es mayor a cero el muñeco recibe el viento en la cara 
    image(persona_cayendo, posicion.x, posicion.y);
  } else if (velocidad_2 < 0) { // Si la velocidad es menor a cero el muñeco recibe el viento en contra 
    image(persona_cayendo_jalon_arriba, posicion.x, posicion.y);
  }
  line(0, 0, 0, posicion.y-5);
  popMatrix(); // Se "explota" la matriz actual y se devuelve a la original que se manejo antes de este proceso
  strokeWeight(4);

  strokeWeight(1);
  System.out.println("La velocidad en el instante x= " + (posicion_2 +(cuerda*300/720)) + "m con t = " + cronometro_fase2 + "s es de " + velocidad_2v/0.24f + " m/s");
  textFont(letra2);
  textSize(16);
  fill(0xfff7b016);
  // Se transforma los valores numeros convertidos a metros para ser impresos
  pos = str((posicion_2 +(cuerda*300/720)));
  velocidad = str(velocidad_2v/0.24f);
  tiempo = str(cronometro_fase2);
  fill(0xffffffff);
  rect(400, 495, 185, 35);
  rect(400, 545, 185, 35);
  rect(600, 520, 185, 35);
  textFont(letra2);
  textSize(16);
  fill(0xfff7b016);
  text("Posicion: " + pos + " m", 400, 500);
  text("Tiempo: " + tiempo + " s", 400, 550);
  text("Velocidad: " + velocidad + " m/s", 600, 525);
  // Se ingresan los valores en sus respectivos vectores en tiempo real
  tiempo_graf_2[contador_ingresos_vector_fase2] = cronometro_fase2;
  velocidad_instante_2[contador_ingresos_vector_fase2] = velocidad_2v/0.24f;
  posiciones_antiguas_y_2[contador_ingresos_vector_fase2] = posicion_2;
  //Al ejecutar el noLoop, esto provoca que se ejecute una vez, pero para no mostrar las graficas, se verifica esto mismo
  if (game_over==false) {
    Crear_Graficas("fase 2", "Tiempo", "Posicion", "Velocidad", tiempo_graf_2, posiciones_antiguas_y_2, velocidad_instante_2, contador_ingresos_vector_fase2);
  }
  // Si ya lleva mas de 2 minutos y 10 segundos se detiene el juego
  if (cronometro_fase2 >= 130) {
    noLoop();
  } 
  cronometro_fase2 = cronometro_fase2 + 0.1f;
  // Al no saber la cantidad de elementos a disponer en esta misma, se incrementa el indice hasta que el juego se detenga o la persona reincie
  contador_ingresos_vector_fase2 = contador_ingresos_vector_fase2 + 1;
}

/*
  Subrutina encargada de hallar las constantes para cada salto particular, al resolver la ecuacion mg - bv = ma
 */
public void CreacionEcuaciones_Fase1() {
  C1_f1= -masa*gravedad/-coeficiente_r;
  C2_f1 = -masa/coeficiente_r * C1_f1;
}

/*
  Subrutina encargada hallar las constantes para cada salto particular, al resolver la ecuacion mg - bv - kx = ma
 
 Esta utiliza un sistema de ecuaciones 2x2 por el metodo de Crammer para solucionar el antes nombrado tomando:
 
 Consideracion: Este despeje sirve para movimientos subamortiguados, donde el discrimante es imaginario
 al tomar en cuenta los otros sistemas (Criticamente amortiguado o sobreamortiguado) fallara. Por lo tanto, si 
 el movimiento cambia, solo tendra que cambiar la derivada y la funcion.
 
 1.- La funcion para el desplazamiento
 2.- La funcion x´(t) que por definicion es velocidad
 3.- Alfa y Beta, soluciones generales para el caso particular
 4.- Velocidad y Tiempo de la fase 1, para hallar los valores
 
 Para resolver esta ecuacion se usara el metodo de Crammer, hallando el determinate del sistema, de C1 y C2.
 C1 C2   S
 a - b = e
 c - d = f
 
 link de referencia: https://es.wikipedia.org/wiki/Regla_de_Cramer
 
 */

public void CreacionEcuaciones_Fase2(float tiempo, float velocidad) {
  float alfa, beta, a, b, c, d, e, f;
  e = -masa*gravedad/constante_k;
  f = redondearDecimales(velocidad);
  tiempo = tiempo/0.1f;
  alfa = -coeficiente_r/(2*masa);
  beta = pow(coeficiente_r, 2)-(4*masa*constante_k); 
  beta = sqrt(beta * -1)/(2*masa); 
  beta = redondearDecimales(beta)-0.01f; // Para tener el valor completamente redondeado, se le resta 0.01, debido a un error de ese margen 
  a = pow(2.71f, alfa*tiempo)*sin(beta*tiempo);
  b = pow(2.71f, alfa*tiempo)*cos(beta*tiempo);
  c = beta * pow(2.71f, alfa*tiempo) * cos(beta*tiempo) - alfa*pow(2.71f, alfa*tiempo)*sin(beta*tiempo); // Primera parte de la derivada
  d = alfa *pow(2.71f, alfa*tiempo) * cos(beta*tiempo) - beta * pow(2.71f, alfa*tiempo)*sin(beta*tiempo); // Segunda parte de la derivada
  if ((a*d)-(b*c) != 0) { // Se verifica si el determinante no es 0, caso imposible de suceder
    c1 = ((e*d)-(b*f))/((a*d)-(b*c)); // Se halla la constante 1 , C1
    c2 = ((a*f)-(e*c))/((a*d)-(b*c)); // Se halla la constante 2 , C2
  } else {
    System.out.println("No hay solucion para el siguiente caso");
  }
  c1 = redondearDecimales(c1);
  c2 = redondearDecimales(c2);
  System.out.println("C1 = " + c1 + " C2 = " + c2 + " alfa = " + alfa + " beta = " + beta);
}


/* 
 Subrutina encargada de hallar las velocidaes y posiciones, para cada fase, tomando en cuenta el parametro
 de tipo Cadena que se le pasa, indicando la fase a buscar, al resolver las ecuaciones de las fases:
 
 1.- mg - bv = ma
 2.- mg - bv - kx = ma
 
 Recomendacion: No manipular en ningun caso, mientras la ecuacion no cambie o los ejes tomados tampoco, 
 si las ecuaciones anteriores son modificadas o contiene una dependecia cuadratica, esta no sirve debido
 a su naturaleza.
 
 En la fase 1, el cronometro es pasado en pixeles_segundos , por eso se divide entre 0.1, si la escala se cambia, 
 cambie ese numero por el de su escala.
 
 En la fase 2, el cronometro_fase2 es en segundos
 */

public void Calcular_fuerza(String fuerza) {
  fuerza = fuerza.toLowerCase();
  if (fuerza.equals("neta fase 1")) {
    velocidad_n= (masa/coeficiente_r)*C1_f1*((-coeficiente_r/masa)*pow(2.71f, -coeficiente_r/masa*(cronometro/0.1f)))+masa*gravedad/coeficiente_r;
    velo_vector = velocidad_n * 0.24f;
  } else if (fuerza.equals("neta fase 2")) {
    p1 =  c1 * pow(2.71f, alfa*cronometro_fase2)*sin(beta*cronometro_fase2);
    p2 =  c2 * pow(2.71f, alfa*cronometro_fase2)*cos(beta*cronometro_fase2);
    p3 = c1 * (beta*pow(2.71f, alfa*cronometro_fase2) * cos(beta*cronometro_fase2) - alfa * pow(2.71f, alfa*cronometro_fase2)*sin(beta*cronometro_fase2)); 
    p4 = c2 * (alfa * pow(2.71f, alfa*cronometro_fase2) * cos(beta*cronometro_fase2) - beta * pow(2.71f, alfa*cronometro_fase2)*sin(beta*cronometro_fase2));
    posicion_2 = p1 + p2 + (masa*gravedad/constante_k); 
    velocidad_2 = p3 + p4;
    posicion_2v = posicion_2*720/300;
    velocidad_2v = velocidad_2 * 0.24f;
  }
}

/*
  Funcion encargada de redondear a 2 cifras significativas cada valor que se le ingresa
 */
public float redondearDecimales(float valorInicial) {
  float resultado, parteEntera;
  resultado = valorInicial;
  parteEntera = floor(resultado);
  resultado = (resultado-parteEntera) * 100;
  resultado = round(resultado);
  resultado = resultado / 100 + parteEntera;
  return resultado;
}

/*
  Subrutina encargada de graficar en tiempo real cada funcion (tiempo vs posicion o tiempo vs velocidad) para cada fase 2
 
 Se le ingresan como parametrosa ademas de lso textos decoratvios, se le pasan los vectores y sus respectivos contadores
 
 Recomendacion: Si se le cambia el punto donde se cortan los ejes x Y y, se debe cambiar la ubicacion del translate(),
 esta linea tendra un asterisco.
 
 */

public void Crear_Graficas(String fase, String ejex, String ejey1, String ejey2, float[] tiempo_fase, float[] posicion_fase, float[] velocidad_fase, int contador) {
  // Crear fondo y todo lo demas estetico 
  fill(0xffffffff);
  rect(540, 65, 400, 200);
  rect(540, 365, 400, 200);
  fill(0xff000000);
  textAlign(CENTER);
  textFont(letra2, 14);
  noSmooth();
  // Verificar que fase graficar
  if (fase.equals("fase 1")) {
    text("Grafica " + ejex + " VS " + ejey1 + " de la " + fase, 538, -5);
    line(360, -35, 360, 165);
    line(340, 145, 740, 145);
    text("Grafica " + ejex + " VS " + ejey2 + " de la " + fase, 538, 290 );
    line(360, 265, 360, 465);
    line(340, 400, 740, 400);
    strokeWeight(3);
    stroke(0xfffc0303);
    pushMatrix(); // Se guarda el sistema de referencia actual del programa
    translate(360, 145); // Se traslada el plano de referencia a donde se cortan los ejes en la grafica *
    // Ciclo for, encargado de dibujar de 0 cada grafica en cada fps, debido a la funcion background
    for (int c = 1; c <= 12; c++) {
      strokeWeight(2);
      line(-3, -c*12.5f, 3, -c*12.5f);
      textFont(letra2);
      textSize(11);
      text(c*15 + "m", -25, -c*12.5f);
      strokeWeight(3);
    }
    for (int c = 1; c <= 24; c++) {
      strokeWeight(2);
      line(c*15, -3, c*15, 3);
      textFont(letra2);
      textSize(7);
      text(c*5 + "s", c*15-10, 12);
      strokeWeight(3);
    }
    for (int c = 0; c < contador; c++) {
      point(tiempo_fase[c], -150*posicion_fase[c]/180);
    }
    popMatrix(); // Se "explota" la matriz actual y se devuelve a la original que se manejo antes de este proceso
    pushMatrix();
    translate(360, 400);
    for (int c = 1; c <= 9; c++) {
      strokeWeight(2);
      line(-3, -c*12.22f, 3, -c*12.22f);
      textFont(letra2);
      textSize(11);
      text(c*6 + "m/s", -25, -c*12.22f);
      strokeWeight(3);
    }
    for (int c = 1; c <= 24; c++) {
      strokeWeight(2);
      line(c*15, -3, c*15, 3);
      textFont(letra2);
      textSize(7);
      text(c*5 + "s", c*15-10, 12);
      strokeWeight(3);
    }
    for (int c = 0; c < contador; c++) {
      point(tiempo_fase[c], -110*velocidad_fase[c]/54);
    }
    popMatrix();
    strokeWeight(1);
  } else if (fase.equals("fase 2")) {
    text("Grafica " + ejex + " VS " + ejey1 + " de la " + fase, 538, -5);
    line(360, -35, 360, 165);
    line(340, 145, 740, 145);
    text("Grafica " + ejex + " VS " + ejey2 + " de la " + fase, 538, 290 );
    line(360, 265, 360, 465);
    line(340, 400, 740, 400);
    strokeWeight(3);
    stroke(0xfffc0303);
    pushMatrix();
    translate(360, 145);
    for (int c = 1; c <= 12; c++) {
      strokeWeight(2);
      line(-3, -c*12.5f, 3, -c*12.5f);
      textFont(letra2);
      textSize(11);
      text(c*15 + "m", -25, -c*12.5f);
      strokeWeight(3);
    }
    for (int c = 1; c <= 24; c++) {
      strokeWeight(2);
      line(c*15, -3, c*15, 3);
      textFont(letra2);
      textSize(7);
      text(c*5 + "s", c*15-10, 12);
      strokeWeight(3);
    }
    for (int c = 0; c < contador; c++) {
      point(tiempo_fase[c], -150*posicion_fase[c]/180);
    }
    popMatrix();
    pushMatrix();
    translate(360, 400);
    for (int c = 1; c <= 9; c++) {
      strokeWeight(2);
      line(-3, -c*12.22f, 3, -c*12.22f);
      textFont(letra2);
      textSize(11);
      text(c*6 + "m/s", -25, -c*12.22f);
      strokeWeight(3);
    }
    for (int c = 1; c <= 24; c++) {
      strokeWeight(2);
      line(c*15, -3, c*15, 3);
      textFont(letra2);
      textSize(7);
      text(c*5 + "s", c*15-10, 12);
      strokeWeight(3);
    }
    for (int c = 0; c < contador; c++) {
      point(tiempo_fase[c], -110*velocidad_fase[c]/54);
    }
    popMatrix();
    strokeWeight(1);
  }
}

/*
  Subrutina encargada de llenar de 0 los vectores encargados de almecenar las posiciones, velocidades y tiempos de cada fase.
 */
public void Limpiar_Arreglos(float velocidad_fase1[], float tiempo_fase1[], float posicion_fase1[], float velocidad_fase2 [], float tiempo_fase2 [], float posicion_fase2[]) {
  for (int i = 0; i <= 2000; i++);
  velocidad_fase1[i] = 0;
  tiempo_fase1[i] = 0;
  posicion_fase1[i] = 0;
  velocidad_fase2[i] = 0;
  tiempo_fase2[i] = 0;
  posicion_fase2[i] = 0;
}


/*
  Subrutina encargada de llnear una tabla en Processing con el tipo de dato Table en tiempo real a medida de que sucede la fase
 , que luego se exportara como ".csv"
 
 Se pasa como parametros los vectores a su momento de la fase y sus contadores.
 
 Recomendacion: Si se desea agregar mas columnas, dirigirse a la parte superior del programa donde se declaran las columnas
 y agregar las respectivas filas con su respectivo dato (variable o vector) despues.
 
 Se puede exportar tambien tipo ".html" o ".bin"
 
 */
public void LLenarTabla(float[] tiempo, float[] velocidad, float[] posicion, Table tabla, int contador_saltos, String fase, int contador_fases) {
  for (int i = 0; i < contador_fases; i++) {
    TableRow newRow = tabla.addRow();
    newRow.setFloat("Tiempo (s)", tiempo[i]);
    newRow.setFloat("Masa (kg)", masa);
    newRow.setInt("Cantidad de Cuerda (m)", cuerda);
    newRow.setFloat("Altura (m)", altura_puente);
    newRow.setFloat("Posicion (m)", posicion[i]);
    newRow.setFloat("Velocidad (m/s)", velocidad[i]);
    newRow.setFloat("Constante de Elasticidad (N/m)", constante_k);
    newRow.setFloat("Constante del Aire", coeficiente_r);
    newRow.setFloat("Gravedad (m/s2)", gravedad);
    saveTable(tabla, "data/Datos Excel/tablaSalto#"+contador_saltos+"de la " + fase +".csv");
  }
}

/*
  Reiniciar los valores ingresados por el usuario a su estado inicial
 */
public void Reiniciar() {
  altura_persona = 0;
  puente_a = 75;
  altura_puente = 240;
  masa = 45;
  cuerda = 70;
  constante_k = 12;
  velocidad = 54;
  tiempo = tiempo*0.1f;
  para = true;
  cronometro = 0.1f;
  posicion = new PVector(0, 0);
  velocidad_v = new PVector(0, 0.24f);
  // Lleva el conteo de saltos que lleva la aplicacion mientras no se cierre
  contador_saltos = contador_saltos + 1;
}

public void Menu_Inicio() {
  background(fondo);
}

/*
  Subrutina encargada de crear los objetos para cada nuevo lanzamiento
 */
public void CreacionObjetos() {
  soporte_cuerda.resize(75, 75);
  stroke(1);
  background(bg);
  image(datos, 540, 236);
  image(soporte_cuerda, -115, altura_persona);
  imageMode(CENTER);
  image(puente, -228, puente_a);
  image(personaje_de_pie, posicion.x, altura_persona);
  image(buttons_l, 445, 5);
  image(buttons_r, 635, 5);
  image(buttons_l, 445, 150);
  image(buttons_r, 635, 150);
  image(buttons_l, 445, 295);
  image(buttons_r, 635, 295);
  image(buttons_l, 445, 435);
  image(buttons_r, 635, 435);
  image(previa_mapa, 521, 545);
  fill(0xff000000);
  textAlign(CENTER);
  textFont(letra2);
  textSize(20);
  text(masa + " kg", 540, 10);
  text(cuerda + " m", 540, 155);
  text(constante_k + " N/m", 540, 300);
  text(altura_puente + " m", 540, 440);
}

/*
  Subrutina encargada de crear el efecto de animicacion si el mouse se encuentra sobre los botones
 */
public void animacion_fondo() {
  if ((mouseX >= 857 && mouseX <= 1006) && (mouseY >= 339 && mouseY <= 396) && gamestate == -1) {
    background(fondo_1);
  }
  if ((mouseX >= 857 && mouseX <= 1006) && (mouseY >= 430 && mouseY <= 491) && gamestate == -1) {
    background(fondo_2);
  }
  if ((mouseX >= 857 && mouseX <= 1006) && (mouseY >= 531 && mouseY <= 588) && gamestate == -1 ) {
    background(fondo_3);
  }
  if ((mouseX >= 857 && mouseX <= 1006) && (mouseY >= 620 && mouseY <= 679) && gamestate == -1) {
    background(fondo_4);
  }
}


/*
  Subrutina encargada de verificar la seleccion del menu
 */
public void seleccion_menu(int seleccion) {
  if (seleccion == 0) { // Inciar juego
    reiniciar = true;
  } else if (seleccion == 1) {
    instru = true;
  } else if (seleccion == 2) { // Cambiar la musica
    menu_musica = true;
  } else {
    exit();
  }
}

/*
  Subrutina encarga de poner la imagen de Game Over, reproducir la musica y frenar el juego hasta que se presione R
 */

public void GameOver() {
  game_over = true;
  player.pause();
  player = minim.loadFile("data/Resources/Sounds/Music/game_over.mp3");
  player.play();
  imageMode(CENTER);
  image(game_over_ui, 270, 235);
}

//////////////////////////////////////////////////////// FUNCIONES Y SUBRUTINAS DE PROCESSING /////////////////////////////////////////

/*
  Subrutina de Processing encargada de controlar las teclas que presiona el usuario
 */
public void keyPressed() {

  // Presionar "R" para reiniciar el juego
  if ( key == 'r') {
    LLenarTabla(tiempo_graf_2, velocidad_instante_2, posiciones_antiguas_y_2, tabla_fase2, contador_saltos, "fase 2", contador_ingresos_vector_fase2); // Genera y exporta la tabla de la fase 2
    game_over = false;
    reiniciar = true;
    fase2 = false;
    dejacaer=false;
    saca_las_graficas = false;
    Limpiar_Arreglos(velocidad_instante, tiempo_graf, posiciones_antiguas_y, velocidad_instante_2, tiempo_graf_2, posiciones_antiguas_y_2);
    contador_ingreos_vector_fase1 = 0;
    contador_ingresos_vector_fase2 = 0;
    tabla_fase1.clearRows();
    tabla_fase2.clearRows();
    if (contador_musica == 7) { // Si es la ultima cancion regresa a la primera
      contador_musica = 0;
    }
    contador_musica += 1; 
    player.pause();
    player = minim.loadFile("data/Resources/Sounds/Music/cancion_"+contador_musica+".mp3");
    player.shiftGain(player.getGain(), -7, 200);
    player.play();
    loop();
  }

  // Presionar "p" para pausar y accionar el juego
  if ( key == 'p' && gamestate != -1) {
    pausa = !pausa;
    if (pausa) {
      noLoop();
    } else {
      loop();
    }
  }

  //Presiona "Q" para aumentar el incremento de las variables
  if ( key == 'q' && gamestate != -1) {
    if (aumento != 1) {
      aumento = aumento - 1;
    }
  }

  //Presiona "E" para reducir el incremento de las variables
  if (key == 'e' && gamestate != -1) {
    if (aumento != 10) {
      aumento = aumento + 1;
    }
  }

  //Presiona "M" para cambiar la musica durante el juego
  if (key == 'm' && gamestate != -1) {
    // Si se desea agregar más musica aumentar el numero 7 al numero de cancion que se dispongan en ese momento
    if (contador_musica == 7) { // Si es la ultima cancion regresa a la primera
      contador_musica = 0;
    }
    contador_musica += 1; 
    player.pause();
    player = minim.loadFile("data/Resources/Sounds/Music/cancion_"+contador_musica+".mp3");
    player.shiftGain(player.getGain(), -7, 200);
    player.play();
  }

  // Presiona "N" para parar la musica dentro del juego
  if (key == 'n') {
    if (player.isPlaying() == true) {
      player.pause();
    } else {
      player.play();
    }
  }

  // Presiona "B" para tirar para atras 
  if (key == 'b' && gamestate != -1) {
    gamestate = -1;
    para = false;
  }
}

/*
  Subrutina encargada de verificar las areas donde se da click
 */

public void mouseClicked() {
  println(mouseX + " con " + mouseY);
  // Jugar
  if ((mouseX >= 857 && mouseX <= 1006) && (mouseY >= 339 && mouseY <= 396) && gamestate == -1 ) {
    gamestate = 0;
    seleccion_menu(gamestate);
  }
  // Instrucciones
  if ((mouseX >= 857 && mouseX <= 1006) && (mouseY >= 430 && mouseY <= 491) && gamestate == -1) {
    gamestate = 1;
    seleccion_menu(gamestate);
  }
  // Seleccionar Musica
  if ((mouseX >= 857 && mouseX <= 1006) && (mouseY >= 531 && mouseY <= 588) && gamestate == -1 ) {
    if (contador_musica == 7) {
      contador_musica = 0;
    }
    contador_musica += 1; 
    player.pause();
    player = minim.loadFile("data/Resources/Sounds/Music/cancion_"+contador_musica+".mp3");
    player.shiftGain(player.getGain(), -7, 200);
    player.play();
  }
  //* Salir
  if ((mouseX >= 857 && mouseX <= 1006) && (mouseY >= 620 && mouseY <= 679) && gamestate == -1) {
    gamestate = 3;
    seleccion_menu(gamestate);
  }
  // Verifica si se da click en el area del mapa
  if ((mouseX >= 962 && mouseX <= 1045) && (mouseY >= 638 && mouseY <= 701) && para == true) {
    dejacaer = true;
    cuerda = (720*cuerda)/300;
    posicion_traslacion_plano = altura_persona;
    para = false;
    // Si esta el fondo de la luna
    if (contador_fondo == 5) {
      gravedad_v = 0.03f;
      gravedad = 1.62f ;
    } else if (contador_fondo == 2) { // Si esta en el fondo de marte
      gravedad_v = 0.07f;
      gravedad = 3.7f;
    }
  }
  if ((mouseX >= 421 && mouseX <= 654) && (mouseY >= 213 && mouseY <= 286)) {
    noStroke();
    fill(0xff3b98ed);
    rectMode(CENTER);
    rect(540, 250, 250, 75, 75);
  }

  /* 
   Encargado de llevar los conteos de: 
   1.- Masa 
   2.- Longitud Cuerda
   3.- Constante de Elasticidad
   4.- Altura del puente al piso
   Siendo el primero el incremento y el segundo decremento
   */
  if ((mouseX >= 693 && mouseX <= 748) && (mouseY >= 103 && mouseY <= 154)) {
    masa -= aumento;  
    if (masa < 45) {
      masa = 45;
    }
  }
  if ((mouseX >= 882 && mouseX <= 940) && (mouseY >= 103 && mouseY <= 154)) {
    masa += aumento;
    if (masa > 105) {
      masa = 105;
    }
  }
  if ((mouseX >= 693 && mouseX <=749) && (mouseY >= 248 && mouseY <= 299)) {
    cuerda -= aumento;
    if (cuerda < 50) {
      cuerda = 50;
    }
  }
  if ((mouseX >= 882 && mouseX <= 940) && (mouseY >= 248 && mouseY <= 299)) {
    cuerda += aumento;
    if (cuerda > 240) {
      cuerda = 240;
    }
  }
  if ((mouseX >= 693 && mouseX <= 748) && (mouseY >= 393 && mouseY <= 446)) {
    constante_k -= aumento;
    if (constante_k < 12) {
      constante_k = 12;
    }
  }
  if ((mouseX >= 882 && mouseX <= 940) && (mouseY >= 393 && mouseY <= 446)) {
    constante_k += aumento;
    if (constante_k > 200) {
      constante_k = 200;
    }
  }
  if ((mouseX >= 693 && mouseX <= 748) && (mouseY >= 535 && mouseY <= 584)) {
    if (altura_puente <= 70) {
      altura_puente = 240;
      puente_a = 75;
      altura_persona = 0;
    } else {
      altura_puente -= aumento;
      // Mueve a la persona con respecto al puente
      puente_a = aumento*2.40f + puente_a;
      altura_persona = aumento*2.40f + altura_persona;
    }
  }
  if ((mouseX >= 883 && mouseX <= 940) && (mouseY >= 535 && mouseY <= 584)) {
    if (altura_puente >= 240) {
      altura_puente = 240;
      puente_a = 75;
      altura_persona = 0;
    } else {
      altura_puente += aumento;
      // Mueve a la persona con respecto al puente
      puente_a = 2.40f*-aumento + puente_a;
      altura_persona = 2.40f*-aumento + altura_persona;
    }
  }
  // Encargado de verificar los mapas donde se encuentran
  if ((mouseX >= 650 && mouseX <= 942) && (mouseY >= 641 && mouseY <= 705)) {
    if (contador_fondo == 7) {
      contador_fondo = 1;
    } else {
      contador_fondo += 1;
    }
    if (contador_fondo == 1 || contador_fondo == 3 || contador_fondo == 4 || contador_fondo == 6) {
      bg  = loadImage("data/Resources/Backgrounds/background_image_"+contador_fondo+".jpg");
    } else {
      bg  = loadImage("data/Resources/Backgrounds/background_image_"+contador_fondo+".png");
    }
    // Cambiar el puente a amarillo si esta en fondo negro
    if (contador_fondo == 5 || contador_fondo == 7) {
      puente_con = 2;
    } else {
      puente_con = 1;
    }
    if (previa_map == 7) {
      previa_map = 1;
    } else {
      previa_map += 1;
    }
    puente = loadImage("data/Resources/Characters/bridge_"+puente_con+".png");
    previa_mapa = loadImage("data/Resources/Backgrounds/miniatura_mapa_"+previa_map+".png");
  }
}
  public void settings() {  size(1080, 720); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "BongeeJumping_IsaacBlanco_AlexFigueroa_IlamRojas" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
