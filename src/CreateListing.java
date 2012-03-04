import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Random;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.ajr.rhrn.RHRN;
import com.ajr.rhrn.ServerException;
import com.ajr.rhrn.database.Member;
import com.caucho.hessian.client.HessianProxyFactory;


public class CreateListing extends AbstractJavaSamplerClient 
{
	private RHRN rhrn = null;
	private Member member = null;
	private Random random = new Random();
	
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
		
		try 
		{
			member = rhrn.createMember("TestingItBitch", "", String.valueOf(Math.abs(random.nextInt())), "a");
		} 
		catch (ServerException e) 
		{
			e.printStackTrace();
		}
	}
	
	public Arguments getDefaultParameters()
	{
		Arguments args = new Arguments();
		args.addArgument("Service URL", Properties.hessianURL);
		args.addArgument("StateID", new String("2208"));
		args.addArgument("CityID", new String("48362"));
		return args;	
	}
	
	public SampleResult runTest(JavaSamplerContext context) 
	{
		SampleResult result = new SampleResult();
		result.setSampleLabel("CreateListing");
		result.setDataType(SampleResult.TEXT);
		result.sampleStart();
		
		try 
		{
			rhrn.createListing(member.getID(), "TestingItBitch", member.getPassword(), Integer.valueOf(context.getParameter("StateID")), "California", Integer.valueOf(context.getParameter("CityID")), "Sacramento", "Title asdf asdf asdf asdfsd f", "Activity", "Location", "Identifier", 4, 5);

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
