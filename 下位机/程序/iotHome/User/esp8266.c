#include "esp8266.h"
#include "delay.h"

// Ĭ�����ӵ�ssid��test-wifi��1234567888
// ip �˿ڿ����Լ��޸�
void Esp8266_Init(void)
{
	DelayS(1);
	USART_Printf(USART1,"AT+RST\r\n");
	DelayS(5);//��ʼ���������ȴ�����wifi	
	USART_Printf(USART1,"AT+CIPMUX=0\r\n");
	DelayS(1);//������ģʽ
	USART_Printf(USART1,"AT+CIPSTART=\"TCP\",\"129.211.127.83\",8000\r\n");
	DelayS(2);//����Զ�̷������˿ڻ�ȡ����
	USART_Printf(USART1,"AT+CIPMODE=1\r\n");
	DelayS(1);//����ģ�鴫��ģʽΪ͸��ģʽ
	USART_Printf(USART1,"AT+CIPSEND\r\n");
	DelayS(1);//����͸��ģʽ���������������
}


// ����wifi�ȵ㣺
//AT+CWMODE_DEF=1
//AT+CWJAP="test-wifi","1234567888"