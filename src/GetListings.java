import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.ajr.rhrn.RHRN;

import com.caucho.hessian.client.HessianProxyFactory;

public class GetListings extends AbstractJavaSamplerClient 
{
	private RHRN rhrn = null;
	
	public void setupTest(JavaSamplerContext context) 
	{
		HessianProxyFactory factory = new HessianProxyFactory();		
		String url = context.getParameter("Service URL");		
		try 
		{
			rhrn = (RHRN) factory.create(RHRN.class, url);
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public Arguments getDefaultParameters()
	{
		Arguments args = new Arguments();
		args.addArgument("Service URL", Properties.hessianURL);
		return args;	
	}
	
	public SampleResult runTest(JavaSamplerContext context) 
	{
		SampleResult result = new SampleResult();
		result.setSampleLabel("GetListing");
		result.setDataType(SampleResult.TEXT);
		result.sampleStart();
		
		try 
		{
			rhrn.getListings(0, 0);

			result.setSuccessful(true);
			result.setResponseCodeOK();
			result.setResponseMessage("Successful");
		}
		catch (Throwable e) 
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			result.setSuccessful(false);
			result.setResponseMessage(e.getMessage());
			result.setResponseData(sw.toString().getBytes());
		}

		result.sampleEnd();
		return result;
	}
}
