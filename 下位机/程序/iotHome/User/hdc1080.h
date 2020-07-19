#ifndef __HDC1080_H
#define __HDC1080_H

#include "sys.h"

/////////���㹫ʽ///////////////////////////////////////////
//temper=[(d15:d0)/2^16]*165c-40c
//relative=[(d15:d0)/2^16]*100%
///////////HDC1080�Ĵ���˵��////////////////////////////////

//�Ĵ�����ַ
#define HDC1080_I2C_ADDR   0x80   //HDC1080��IIC��ַΪ1000000��7λ��ַ��+ ��(1)��д(0)����λ
#define Temperature        0x00   //�¶ȼĴ���,��λֵΪ0x0000 
#define Humidity           0x01   //ʪ�ȼĴ���,��λֵΪ0x0000 
#define Configuration      0x02   //���üĴ���,��λֵΪ0x1000 
#define Manufacturer_ID    0xFE   //������ID�Ĵ���,0x5449
#define Device_ID          0xFF   //�豸ID�Ĵ���,0x1050 

//�Ĵ���ֵ
#define Manufacturer_ID_value  0x5449   //������IDֵ
#define Device_ID_value        0x1050   //�豸IDֵ
#define Config_heateroff       0x1000   //�ر�Heater,ͬʱ�ɼ��¶Ⱥ�ʪ��(�¶���ǰ),�¶ȷֱ���Ϊ14λ,ʪ�ȷֱ���Ϊ14λ
#define Config_heateron        0x3000   //����Heater,ͬʱ�ɼ��¶Ⱥ�ʪ��(�¶���ǰ),�¶ȷֱ���Ϊ14λ,ʪ�ȷֱ���Ϊ14λ

void HDC1080_Init(void);   //��ʼ��HDC1080
void HDC1080_WriteReg(u8 WriteAddr,unsigned int Data);   //д�����ݵ��Ĵ�����
u8 HDC1080_ReadReg(u8 ReadAddr,u8 *pBuffer,u16 length);   //��ȡ�Ĵ����ڵ�����
void Read_Temp_Humi_Reg(u8 *datax);   //��ȡ�¶Ⱥ�ʪ�ȼĴ�����ֵ
void Convert_HDC1080_TempHumidity(float *temp,float *humi);   //ת���¶Ⱥ�ʪ��


#endif

