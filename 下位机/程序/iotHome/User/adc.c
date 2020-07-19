#include "adc.h"
#include "delay.h"

void ADC_GPIO_Init(void)
{
	GPIO_InitTypeDef t_gpio;
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_GPIOA,ENABLE);//ʹ�� GPIOA ʱ��
	t_gpio.GPIO_Pin=GPIO_Pin_0|GPIO_Pin_1|GPIO_Pin_7;  //GPIOA.0.1.7
	t_gpio.GPIO_Mode=GPIO_Mode_AIN;//ģ������ģʽ
	GPIO_Init(GPIOA,&t_gpio);//��ʼ��GPIOA
}
 
void ADC_Conf_Init(void)
{
	ADC_InitTypeDef t_adc;	
	
	ADC_GPIO_Init();
	
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_ADC1,ENABLE);//ʹ��ADC1ʱ��
	t_adc.ADC_Mode = ADC_Mode_Independent;  //����ģʽ��ADC1��ADC2����
	t_adc.ADC_ScanConvMode = DISABLE; //����ͨ��ɨ��
	t_adc.ADC_ContinuousConvMode = DISABLE; //��������ת��
	t_adc.ADC_ExternalTrigConv = ADC_ExternalTrigConv_None;//��ʹ���ⲿ����
	t_adc.ADC_DataAlign = ADC_DataAlign_Right; //����λ�Ҷ���
	t_adc.ADC_NbrOfChannel = 1; //ת��ͨ����Ϊ1
	ADC_Init(ADC1,&t_adc); //��ʼ��ADC1	
	RCC_ADCCLKConfig(RCC_PCLK2_Div8);//����ADCʱ��ΪPCLK2��8��Ƶ
	ADC_Cmd(ADC1,ENABLE); //ʹ��ADC1
	ADC_ResetCalibration(ADC1); 
	while(ADC_GetResetCalibrationStatus(ADC1)); 
	ADC_StartCalibration(ADC1); //У׼
	while(ADC_GetCalibrationStatus(ADC1)){};
}
 
u16 adc_read_value(u8 ch)
{
	u16 adc_value = 0;
	ADC_RegularChannelConfig(ADC1, ch, 1, ADC_SampleTime_7Cycles5 ); //����ADC1ͨ��ch��ת������Ϊ7.5���������ڣ���������Ϊ1
	ADC_SoftwareStartConvCmd(ADC1, ENABLE);//ʹ���������
	while(!ADC_GetFlagStatus(ADC1, ADC_FLAG_EOC )){};//�ȴ�ת�����
	adc_value = ADC_GetConversionValue(ADC1); //��ȡת��ֵ	
	return adc_value;
}

u16 ADC_Read_Stable(u8 ch)
{
	int i;
	u32 value=0;
	for(i=0;i<10;++i){
		value+=adc_read_value(ch);
		DelayUs(20);
	}
	return value/10;
}



