package com.eleinco.ezvoxptt.business;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

//expone logica para conectarse a un webserce SOAP
public class WebService {
		//Configuración del WebService
	    public static SoapObject InvokeObjectMethod(String methodName) throws IOException, Exception
	    {
	    	SoapObject request = new SoapObject(App.NAMESPACE, methodName);
	    	
	    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    	envelope.dotNet = true;
			 envelope.setOutputSoapObject(request);
			 HttpTransportSE transporte = new HttpTransportSE(App.URL);
	    	
			transporte.call(App.SOAP_ACTION + methodName, envelope);
			SoapObject resSoap =(SoapObject)envelope.getResponse();
			return resSoap;
	    }
	    public static SoapObject InvokeObjectMethod1Parametro(String methodName, String npar1, Object par1) throws IOException, Exception
	    {
	    	SoapObject request = new SoapObject(App.NAMESPACE, methodName);
	    	request.addProperty(npar1, par1);
	    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    	envelope.dotNet = true;
			 envelope.setOutputSoapObject(request);
			 HttpTransportSE transporte = new HttpTransportSE(App.URL);
	    	
			transporte.call(App.SOAP_ACTION + methodName, envelope);
			SoapObject resSoap =(SoapObject)envelope.getResponse();
			return resSoap;
	    }
	    public static SoapObject InvokeObjectMethod2Parametro(String methodName, String npar1, Object par1, String npar2, Object par2) throws IOException, Exception
	    {
	    	SoapObject request = new SoapObject(App.NAMESPACE, methodName);
	    	request.addProperty(npar1, par1);
	    	request.addProperty(npar2, par2);
	    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    	envelope.dotNet = true;
			 envelope.setOutputSoapObject(request);
			 HttpTransportSE transporte = new HttpTransportSE(App.URL);
	    	
			transporte.call(App.SOAP_ACTION + methodName, envelope);
			SoapObject resSoap =(SoapObject)envelope.getResponse();
			return resSoap;
	    }
	    public static SoapObject InvokeObjectMethod3Parametro(String methodName, String npar1, Object par1, String npar2, Object par2, String npar3, Object par3) throws IOException, Exception
	    {
			SoapObject request = new SoapObject(App.NAMESPACE, methodName);
			request.addProperty(npar1, par1);
			request.addProperty(npar2, par2);
			request.addProperty(npar3, par3);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE transporte = new HttpTransportSE(App.URL);
			transporte.call(App.SOAP_ACTION + methodName, envelope);
			SoapObject resSoap =(SoapObject)envelope.getResponse();
			return resSoap;
	    } 
	    public static String InvokePrimitiveMethod(String methodName) throws IOException, XmlPullParserException
		{
	    	SoapObject request = new SoapObject(App.NAMESPACE, methodName);		 		 
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE transporte = new HttpTransportSE(App.URL);    	
			transporte.call(App.SOAP_ACTION + methodName, envelope);
			SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
			if(resultado_xml.toString().startsWith("ERROR:"))
			{
				throw new IOException(resultado_xml.toString().split(":")[1]);
			}
			return resultado_xml.toString();
	    }
	    public static String InvokePrimitiveMethod1Parametro(String methodName, 
	    		String npar1, Object par1) throws IOException, XmlPullParserException
	    {
			SoapObject request = new SoapObject(App.NAMESPACE, methodName);
			request.addProperty(npar1, par1);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE transporte = new HttpTransportSE(App.URL);
	    	
			transporte.call(App.SOAP_ACTION + methodName, envelope);
			SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
			if(resultado_xml == null)
				return null;
			if(resultado_xml.toString().startsWith("ERROR:"))
			{
				throw new IOException(resultado_xml.toString().split(":")[1]);
			}
			return resultado_xml.toString();
	    }
	    public static String InvokePrimitiveMethod2Parametro(String methodName, 
	    		String npar1, Object par1, String npar2, Object par2) throws IOException, XmlPullParserException
	    {
			SoapObject request = new SoapObject(App.NAMESPACE, methodName);
			request.addProperty(npar1, par1);
			request.addProperty(npar2, par2);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE transporte = new HttpTransportSE(App.URL);
	    	
			transporte.call(App.SOAP_ACTION + methodName, envelope);
			SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
			if(resultado_xml.toString().startsWith("ERROR:"))
			{
				throw new IOException(resultado_xml.toString().split(":")[1]);
			}
			return resultado_xml.toString();
	    	
	    }
	    public static String InvokePrimitiveMethod3Parametro(String methodName, 
	    		String npar1, Object par1, String npar2, Object par2, 
	    		String npar3, Object par3) throws IOException, XmlPullParserException
	    {
			SoapObject request = new SoapObject(App.NAMESPACE, methodName);
			request.addProperty(npar1, par1);
			request.addProperty(npar2, par2);
			request.addProperty(npar3, par3);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE transporte = new HttpTransportSE(App.URL);
	    	
			transporte.call(App.SOAP_ACTION + methodName, envelope);
			SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
			if(resultado_xml.toString().startsWith("ERROR:"))
			{
				throw new IOException(resultado_xml.toString().split(":")[1]);
			}
			return resultado_xml.toString();
	    	
	    }
	    public static String InvokePrimitiveMethod4Parametro(String methodName, 
	    		String npar1, Object par1, 
	    		String npar2, Object par2, 
	    		String npar3, Object par3, 
	    		String npar4, Object par4) throws IOException, XmlPullParserException
	    {
			 SoapObject request = new SoapObject(App.NAMESPACE, methodName);
			 request.addProperty(npar1, par1);
			 request.addProperty(npar2, par2);
			 request.addProperty(npar3, par3);
			 request.addProperty(npar4, par4);
			 
			 SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			 envelope.dotNet = true;
			 envelope.setOutputSoapObject(request);
			 HttpTransportSE transporte = new HttpTransportSE(App.URL);
	    	
			transporte.call(App.SOAP_ACTION + methodName, envelope);
			SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
			if(resultado_xml.toString().startsWith("ERROR:"))
			{
				throw new IOException(resultado_xml.toString().split(":")[1]);
			}
			return resultado_xml.toString();
	    }
	    public static String InvokePrimitiveMethod5Parametro(String methodName, 
	    		String npar1, Object par1, 
	    		String npar2, Object par2, 
	    		String npar3, Object par3, 
	    		String npar4, Object par4, 
	    		String npar5, Object par5) throws IOException, XmlPullParserException
	    {
			 SoapObject request = new SoapObject(App.NAMESPACE, methodName);
			 request.addProperty(npar1, par1);
			 request.addProperty(npar2, par2);
			 request.addProperty(npar3, par3);
			 request.addProperty(npar4, par4);
			 request.addProperty(npar5, par5);
			 
			 SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			 envelope.dotNet = true;
			 envelope.setOutputSoapObject(request);
			 HttpTransportSE transporte = new HttpTransportSE(App.URL);
	    	
			transporte.call(App.SOAP_ACTION + methodName, envelope);
			SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
			if(resultado_xml.toString().startsWith("ERROR:"))
			{
				throw new IOException(resultado_xml.toString().split(":")[1]);
			}
			return resultado_xml.toString();
	    }
	    public static String InvokePrimitiveMethod6Parametro(String methodName, 
	    		String npar1, Object par1, 
	    		String npar2, Object par2, 
	    		String npar3, Object par3, 
	    		String npar4, Object par4, 
	    		String npar5, Object par5,
	    		String npar6, Object par6) throws IOException, XmlPullParserException
	    {
			 SoapObject request = new SoapObject(App.NAMESPACE, methodName);
			 request.addProperty(npar1, par1);
			 request.addProperty(npar2, par2);
			 request.addProperty(npar3, par3);
			 request.addProperty(npar4, par4);
			 request.addProperty(npar5, par5);
			 request.addProperty(npar6, par6);
			 
			 SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			 envelope.dotNet = true;
			 envelope.setOutputSoapObject(request);
			 HttpTransportSE transporte = new HttpTransportSE(App.URL);
	    	
			transporte.call(App.SOAP_ACTION + methodName, envelope);
			SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
			if(resultado_xml.toString().startsWith("ERROR:"))
			{
				throw new IOException(resultado_xml.toString().split(":")[1]);
			}
			return resultado_xml.toString();
	    }
}
