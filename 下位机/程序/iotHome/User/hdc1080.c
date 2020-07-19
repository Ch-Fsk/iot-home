#include "hdc1080.h"
#include "mi2c.h"
#include "delay.h"
#include "stdio.h"
#include "math.h"
#include "oled.h"
/*********************************************************************************
  * @ ������  �� HDC1080_Init
  * @ ����˵���� ��ʼ��HDC1080
  * @ ����    �� ��
  * @ ����ֵ  �� ��
  ********************************************************************************/
char hdc_buf[32];

void HDC1080_Init(void)
{
	uint8_t temp[2];
	unsigned int manufac_id,dev_id,Config_Reg ;
	
	IIC_Init();   //IIC��ʼ��
	
	HDC1080_ReadReg(Manufacturer_ID,temp,2);
	manufac_id=(unsigned int)(temp[0]<<8);
	manufac_id+=temp[1];
	sprintf(hdc_buf,"MID = 0x%x\r\n",manufac_id);   /*��ӡ������ID */
	OLED_ShowStr(0,0,hdc_buf,1);
	
	HDC1080_ReadReg(Device_ID,temp,2);
	dev_id=(unsigned int)(temp[0]<<8);
	dev_id+=temp[1];
	sprintf(hdc_buf,"Device_ID = 0x%x ",dev_id);   /*��ӡ�豸ID*/
	OLED_ShowStr(0,1,hdc_buf,1);
	if((manufac_id != Manufacturer_ID_value)||(dev_id != Device_ID_value))
	{
		sprintf(hdc_buf,"!!!ERROR,Please Check!!!");   /*��ӡ�豸���ӳɹ�*/
	}
	else 
	{
		sprintf(hdc_buf,"Online!!!");
	}
	OLED_ShowStr(0,2,hdc_buf,1);
	
	Config_Reg=Config_heateroff;   //�ر�Heater,ͬʱ�ɼ��¶Ⱥ�ʪ��(�¶���ǰ),�¶ȷֱ���Ϊ14λ,ʪ�ȷֱ���Ϊ14λ
	sprintf(hdc_buf,"config = 0x%x",Config_Reg);
	OLED_ShowStr(0,3,hdc_buf,1);
	HDC1080_WriteReg(Configuration,Config_Reg);  
	
	HDC1080_ReadReg(Configuration,temp,2);
	Config_Reg=(unsigned int)(temp[0]<<8);
	Config_Reg+=temp[1];
	if(Config_Reg == Config_heateroff)   /*����Ƿ�д��ɹ�*/
	{
			sprintf(hdc_buf,"Config success.");   
	}
	else 
	{
			sprintf(hdc_buf,"Configu reg failed.");   
	}
	OLED_ShowStr(0,4,hdc_buf,1);
	
}

/*********************************************************************************
  * @ ������  �� HDC1080_WriteReg
  * @ ����˵���� д���ݵ��Ĵ�����
  * @ ����    �� 
	*              WriteAddr:�ڲ��Ĵ�����ַ
	*							 Data:��Ҫд�������
  * @ ����ֵ  �� ��
  ********************************************************************************/
void HDC1080_WriteReg(u8 WriteAddr,unsigned int Data)
{
	unsigned char data[2];
	data[0] = (uint8_t)((Data& 0xFF00) >> 8);
	data[1] = (uint8_t)(Data & 0x00FF);
	IIC_Start();   //��ʼ�ź�
	IIC_Send_Byte(HDC1080_I2C_ADDR);   //����HDC1080�豸��ַ+д�ź�
	IIC_Wait_Ack();
	IIC_Send_Byte(WriteAddr);   //�ڲ��Ĵ�����ַ
	IIC_Wait_Ack();
	IIC_Send_Byte(data[0]);   //д���ڲ��Ĵ�������
	IIC_Wait_Ack();
	IIC_Send_Byte(data[1]);   //д���ڲ��Ĵ�������
	IIC_Wait_Ack();
	IIC_Stop();   //ֹͣ�ź�
	
}

/*********************************************************************************
  * @ ������  �� HDC1080_ReadReg
  * @ ����˵���� ��ȡ�Ĵ����ڵ�����
  * @ ����    �� 
	*              ReadAddr:�ڲ��Ĵ�����ַ
  *              pBuffer:���������׵�ַ
	*              length:Ҫ�������ݵĸ���
  * @ ����ֵ  �� ��
  ********************************************************************************/
u8 HDC1080_ReadReg(u8 ReadAddr,u8 *pBuffer,u16 length)
{
		  	    																 
  IIC_Start();   //��ʼ�ź�
	IIC_Send_Byte(HDC1080_I2C_ADDR);   //����HDC1080�豸��ַ+д�ź�
	IIC_Wait_Ack();  
	IIC_Send_Byte(ReadAddr);   //�ڲ��Ĵ�����ַ
	IIC_Wait_Ack();
	IIC_Start();   //��ʼ�ź�
	IIC_Send_Byte(HDC1080_I2C_ADDR+1);   //����HDC1080�豸��ַ+���ź�
	IIC_Wait_Ack(); 
	while(length)
	{
		*pBuffer++=IIC_Read_Byte(1);   //�����Ĵ�������
		length--;
	}
	IIC_NAck();   //����nACK
  IIC_Stop();   //ֹͣ�ź�	    
	return 0;
	
}

/*********************************************************************************
  * @ ������  �� Read_Temp_Humi_Reg
  * @ ����˵���� ��ȡ�¶Ⱥ�ʪ�ȼĴ�����ֵ
  * @ ����    �� 
	*              data:�¶Ⱥ�ʪ�ȼĴ�����ֵ
  * @ ����ֵ  �� ��
  ********************************************************************************/
void Read_Temp_Humi_Reg(u8 *data)
{
	data[0]=0;
	data[1]=0;
	data[2]=0;
	data[3]=0;
	IIC_Start();   //��ʼ�ź�
	IIC_Send_Byte(HDC1080_I2C_ADDR);   //����HDC1080�豸��ַ+д�ź�
	IIC_Wait_Ack();  
	IIC_Send_Byte(Temperature);   //��������
	IIC_Wait_Ack();
	DelayMs(20);   /* Tempת��ʱ�� + Humiת��ʱ�� = 20ms */
	IIC_Start();   //��ʼ�ź�
	IIC_Send_Byte(HDC1080_I2C_ADDR+1);   //����HDC1080�豸��ַ+���ź�
	IIC_Wait_Ack(); 
	data[0]=IIC_Read_Byte(1);   /* ��ȡ�¶ȼĴ�����ֵ */
	data[1]=IIC_Read_Byte(1);
	data[2]=IIC_Read_Byte(1);   /* ��ȡʪ�ȼĴ�����ֵ */
	data[3]=IIC_Read_Byte(1);
	IIC_Stop();   //ֹͣ�ź�  
	
}

/*********************************************************************************
  * @ ������  �� Convert_HDC1080_TempHumidity
  * @ ����˵���� ת���¶Ⱥ�ʪ��
  * @ ����    �� 
	*              data:�¶Ⱥ�ʪ�ȼĴ�����ֵ
  * @ ����ֵ  �� ��
  ********************************************************************************/
void Convert_HDC1080_TempHumidity(float *temp,float *humi)
{
		u8 data[4];
    /* �ɼ���ʪ�� */
    Read_Temp_Humi_Reg(data);      
    /* �¶�ת�� */
    *temp=(float)(data[0]<<8|data[1]);  
    *temp=(*temp/pow(2,16))*165-40;
    /* ʪ��ת�� */
    *humi=0;  
    *humi=(float)(data[2]<<8|data[3]);
    *humi=(*humi/pow(2,16))*100;
	
}








