package springsample.server.component;

import org.springframework.stereotype.Component;

import springsample.client.ISampleService;

@Component
public class SampleService implements ISampleService {

	public String sayHelo(String who) {

		return "Helo " + who;
	}

}
