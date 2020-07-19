#ifndef __MI2C_
#define __MI2C_

#include "sys.h"
 
//IO��������
//ʹ��IIC1 ����M24C02,OLED,LM75AD,HT1382    PB6,PB7  PA5:SDA\PA6:SCL 
#define SDA_IN()  {GPIOA->CRL&=0XFF0FFFFF;GPIOA->CRL|=(u32)8<<20;}
#define SDA_OUT() {GPIOA->CRL&=0XFF0FFFFF;GPIOA->CRL|=(u32)3<<20;}

//IO��������	 
#define IIC_SCL    PAout(6) //SCL
#define IIC_SDA    PAout(5) //SDA	 
#define READ_SDA   PAin(5)  //����SDA 

//IIC���в�������
void IIC_Init(void);                //��ʼ��IIC��IO��				 
void IIC_Start(void);				//����IIC��ʼ�ź�
void IIC_Stop(void);	  			//����IICֹͣ�ź�
void IIC_Send_Byte(u8 txd);			//IIC����һ���ֽ�
u8 IIC_Read_Byte(unsigned char ack);//IIC��ȡһ���ֽ�
u8 IIC_Wait_Ack(void); 				//IIC�ȴ�ACK�ź�
void IIC_Ack(void);					//IIC����ACK�ź�
void IIC_NAck(void);				//IIC������ACK�ź�

void IIC_Write_One_Byte(u8 daddr,u8 addr,u8 data);
u8 IIC_Read_One_Byte(u8 daddr,u8 addr);	

#endif




