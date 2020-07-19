#include "alert.h"


void Alert_Init(void)
{
	GPIO_InitTypeDef GPIO_InitStructure;

 	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOB|RCC_APB2Periph_GPIOC,ENABLE);//ʹ��PORTB,PORTCʱ��

	//GPIO_PinRemapConfig(GPIO_Remap_SWJ_JTAGDisable, ENABLE);//�ر�jtag��ʹ��SWD��������SWDģʽ����
	
	//���ÿ���IO
	GPIO_InitStructure.GPIO_Pin  = GPIO_Pin_12 | GPIO_Pin_13;  //PB12 \PB13
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IPU; //���ó���������
 	GPIO_Init(GPIOB, &GPIO_InitStructure);//��ʼ��
	
	// ����Beep
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_11;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP ;   //�������
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	GPIO_Init(GPIOB, &GPIO_InitStructure);
	// ����fan
	GPIO_InitStructure.GPIO_Pin = GPIO_Pin_14;
	GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP ;   //�������
	GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
	GPIO_Init(GPIOC, &GPIO_InitStructure);
	
}

void Beep_Set(u8 s)
{
	if(s==ON){
		BEEP = ON;
	}else{
		BEEP = OFF;
	}
}

void Fan_Set(u8 s)
{
	if(s==ON){
		FAN = ON;
	}else{
		FAN = OFF;
	}
}

u8 SW_Scan(u8 k)
{
	u8 sw1,sw2;
	sw1 = SW1;
	sw2 = SW2;
	if( k == 1)
	{ 
			return  sw1;
	}
	if( k == 2)
	{
		return sw2;
	}
	return 0xff;
}