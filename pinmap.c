#include "pinmap.h"
/* 
 * Standard Map for pins from stm32 to arduino
 * PA0  0
 * PA1  1
 * PA2  2
 * PA3  3
 * PA4  4
 * PA5  5
 * PA6  6
 * PA7  7
 * PA8  8
 * PA9  9
 * PA10 10
 * PA11 11
 * PA12 12
 * PA13 13
 * PA14 14
 * PA15 15
 * PB0  16
 * PB1  17
 * PB2  18
 * PB3  19
 * PB4  20
 * PB5  21
 * PB6  22
 * PB7  23
 * PB8  24  
 * PB9  25
 * PB10 26
 * PB11 27
 * PB12 28
 * PB13 29
 * PB14 30
 * PB15 31
 * PC0  32
 * PC1  33
 * PC2  34
 * PC3  35 
 * PC4  36
 * PC5  37
 * PC6  38
 * PC7  39
 * PC8  40
 * PC9  41
 * PC10 42
 * PC11 43
 * PC12 44
 * PC13 45
 * PC14 46
 * PC15 47
 */
int pinmap(int x){
    short int pin[13];
    pin[0]=18;  // PB2 Boot 1 jumper.. Too much in need for pins.
    pin[1]=18;  // PB2
    pin[2]=19;  // PB3
    pin[3]=20;  // PB4
    pin[4]=21;  // PB5
    pin[5]=22;  // PB6
    pin[6]=23;  // PB7
    pin[7]=24;  // PB8
    pin[8]=25;  // PB9
    pin[9]=46;  // PC14
    pin[10]=47; // PC15
    pin[13]=45; // PC13 // LED
    // PB10 and PB11 is used for i2c
    return pin[x];
}
