#include "esp8266.h"
#include "delay.h"

// 默认连接的ssid：test-wifi：1234567888
// ip 端口可以自己修改
void Esp8266_Init(void)
{
	DelayS(1);
	USART_Printf(USART1,"AT+RST\r\n");
	DelayS(5);//初始化重启，等待连接wifi	
	USART_Printf(USART1,"AT+CIPMUX=0\r\n");
	DelayS(1);//单连接模式
	USART_Printf(USART1,"AT+CIPSTART=\"TCP\",\"129.211.127.83\",8000\r\n");
	DelayS(2);//连接远程服务器端口获取数据
	USART_Printf(USART1,"AT+CIPMODE=1\r\n");
	DelayS(1);//设置模块传输模式为透传模式
	USART_Printf(USART1,"AT+CIPSEND\r\n");
	DelayS(1);//开启透传模式向服务器发送数据
}


// 连接wifi热点：
//AT+CWMODE_DEF=1
//AT+CWJAP="test-wifi","1234567888"