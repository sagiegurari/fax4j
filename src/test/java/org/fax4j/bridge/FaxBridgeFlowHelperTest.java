package org.fax4j.bridge;

import java.io.File;
import java.util.Properties;
import org.fax4j.FaxJob.FaxJobPriority;
import org.fax4j.bridge.FaxBridgeFlowHelper.FlowResponse;
import org.fax4j.bridge.process.Process2FaxBridge;
import org.fax4j.test.TestUtil;
import org.fax4j.util.IOHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FaxBridgeFlowHelperTest
{
	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void submitFaxJobFullTest() throws Exception
	{
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("abc",file);

		String[] inputData=new String[]{"-priority",FaxJobPriority.LOW_PRIORITY.name(),
									    "-target_address","12345",
									    "-target_name","tname",
									    "-sender_name","sname",
									    "-sender_fax_number","sfax",
									    "-sender_email","smail",
									    "-property:p1","v1",
									    "-property:p2","v2",
									    "-property:","v3",
									    "-file",file.getPath()};
		Process2FaxBridge faxBridge=new Process2FaxBridge();
		Properties configuration=TestUtil.createEmptyFaxClientSpiConfiguration(null);
		FaxBridgeFlowHelper<String[]> helper=new FaxBridgeFlowHelper<String[]>(faxBridge,null,configuration,this);
		FlowResponse response=helper.submitFaxJob(inputData,inputData,true);
		
		file.delete();
		
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getFaxJob());
		Assert.assertTrue(response.isContinueFlow());
	}

	/**
	 * Test 
	 * 
	 * @throws 	Exception
	 * 			Any exception
	 */
	@Test
	public void submitFaxJobNoVendorPolicyTest() throws Exception
	{
		File file=File.createTempFile("temp_",".txt");
		file.deleteOnExit();
		IOHelper.writeTextFile("abc",file);

		String[] inputData=new String[]{"-priority",FaxJobPriority.LOW_PRIORITY.name(),
									    "-target_address","12345",
									    "-target_name","tname",
									    "-sender_name","sname",
									    "-sender_fax_number","sfax",
									    "-sender_email","smail",
									    "-property:p1","v1",
									    "-property:p2","v2",
									    "-property:","v3",
									    "-file",file.getPath()};
		Properties configuration=TestUtil.createEmptyFaxClientSpiConfiguration(null);
		configuration.setProperty(FaxBridge.VENDOR_POLICY_CLASS_NAME,"");
		Process2FaxBridge faxBridge=new Process2FaxBridge();
		FaxBridgeFlowHelper<String[]> helper=new FaxBridgeFlowHelper<String[]>(faxBridge,null,configuration,this);
		FlowResponse response=helper.submitFaxJob(inputData,inputData,true);
		
		file.delete();
		
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getFaxJob());
		Assert.assertTrue(response.isContinueFlow());
	}
}