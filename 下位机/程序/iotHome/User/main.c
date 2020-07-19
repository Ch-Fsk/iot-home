#include "stm32f10x.h"
#include "oled.h"
#include "delay.h"
#include "led.h"
#include "usart.h"
#include "adc.h"
#include "timer.h"
#include "hdc1080.h"
#include "stdio.h"
#include "alert.h"
#include "esp8266.h"

u16 Threshold_YW = 500;
u16 Threshold_MH = 500;

u16 node_id=1889;

int main(void)
{
	u16 light,mq2,mq135;
	float temperature,humidity;
	
	extern u8 App_Beep_Sw,App_Fan_Sw;
	extern char hdc_buf[];
		
	DelayInit();  //  延时功能初始化
	
	//LED_Init();
	USART1_Init(115200);

	OLED_Init();  // OLED 显示初始化
	
	OLED_ShowStr(0,2,"Wifi Initing...",2);
	
	Esp8266_Init();
	
	ADC_Conf_Init(); 				// ADC 初始化
	
	TIM3_Init(9999,7199); 	// 定时器3初始化	
	
	Alert_Init();
	
	HDC1080_Init();	    //HDC初始化	

	App_Beep_Sw = App_Fan_Sw = OFF;
	
	OLED_CLS();
	
	sprintf(hdc_buf,"{\"did\":%d,\"role\":2}\n",node_id);
	USART_Printf(USART1,hdc_buf);	  // 发送节点认证数据
	
			
	while(1)
	{
		sprintf(hdc_buf,"ID:%d",node_id);
		OLED_ShowStr(0,1,hdc_buf,1);
		Convert_HDC1080_TempHumidity(&temperature,&humidity);
		sprintf(hdc_buf,"Tem:%.1fC",temperature);
		OLED_ShowStr(0,2,hdc_buf,1);
		sprintf(hdc_buf,"Hum:%.1f%%",humidity);
		OLED_ShowStr(60,2,hdc_buf,1);
		
		mq2 = ADC_Read_Stable(7)/3.0;
		mq135 = ADC_Read_Stable(1)/3.0;
		light = ADC_Read_Stable(0)/4.0;
		sprintf(hdc_buf,"Light:%5d",light);
		OLED_ShowStr(0,3,hdc_buf,1);
		sprintf(hdc_buf,"Mq2:%5d",mq2);
		OLED_ShowStr(0,4,hdc_buf,1);
		sprintf(hdc_buf,"Mq135:%5d",mq135);
		OLED_ShowStr(0,5,hdc_buf,1);
		
		// 阈值判断
		if(mq2 > Threshold_MH || SW_Scan(1)==0 || App_Beep_Sw == ON)
		{
			Beep_Set(ON);
			OLED_ShowStr(0,6,"Beep:ON ",1);
		}
		else{
			Beep_Set(OFF);
			OLED_ShowStr(0,6,"Beep:OFF",1);
		}
		
		if(mq135 > Threshold_YW || SW_Scan(2)==0 || App_Fan_Sw == ON)
		{
			Fan_Set(ON);
			OLED_ShowStr(80,6,"Fan:ON ",1);
		}
		else {
			Fan_Set(OFF);
			OLED_ShowStr(80,6,"Fan:OFF",1);
		}		
		
		// 上传tcp服务器
		USART_Printf(USART1,"{\"light\":%d,\"mq2\":%d,\"mq135\":%d,\"temp\":%.2f,\"wet\":%.2f}\n",light,mq2,mq135,temperature,humidity);
		
		DelayS(1);
	}
}
