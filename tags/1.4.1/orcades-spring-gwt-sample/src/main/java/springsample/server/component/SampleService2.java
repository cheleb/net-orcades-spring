package springsample.server.component;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import springsample.client.SampleCheckedException;

@Component
public class SampleService2 extends SampleService {
    private static final Logger LOGGER = Logger.getLogger(SampleService2.class);
	

	public void buggy() throws SampleCheckedException{
		LOGGER.debug("Okai");
	}
	
	
	
}
