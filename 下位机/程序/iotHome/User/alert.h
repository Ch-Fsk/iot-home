#ifndef __ALERT_H
#define __ALERT_H

#include "sys.h"

//  ¿ª¹Ø
#define SW1 PBin(13) 
#define SW2 PBin(12)
#define ON  1
#define OFF 0

// ·äÃùÆ÷¡¢·çÉÈ
#define BEEP PBout(11)
#define FAN  PCout(14)

void Alert_Init(void);
void Beep_Set(u8 s);
void Fan_Set(u8 s);
u8 SW_Scan(u8 k);

#endif
