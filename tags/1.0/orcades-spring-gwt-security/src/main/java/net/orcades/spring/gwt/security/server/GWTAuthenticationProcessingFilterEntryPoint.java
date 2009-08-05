package net.orcades.spring.gwt.security.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.orcades.spring.gwt.component.GWTPayloadHelper;
import net.orcades.spring.gwt.security.client.GWTAuthorizationRequiredException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.AuthenticationEntryPoint;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RPCServletUtils;

public class GWTAuthenticationProcessingFilterEntryPoint implements AuthenticationEntryPoint, InitializingBean {


	@Autowired
	private GWTPayloadHelper payloadHelper;
	
	@Autowired
	private GWTAuthenticationProcessingFilter authenticationProcessingFilter;

	public void commence(ServletRequest req, ServletResponse resp,
			AuthenticationException authException) throws IOException,
			ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// Store the request & response objects in thread-local storage.

		payloadHelper.begin(request, response);

		try {
			// Read the request fully.
			//
			String requestPayload = RPCServletUtils.readContentAsUtf8(request);

			// Let subclasses see the serialized request.
			//
			payloadHelper.onBeforeRequestDeserialized(requestPayload);

			RPCRequest rpcRequest = RPC.decodeRequest(requestPayload, null,
					payloadHelper);

			try {
				RPCServletUtils.writeResponse(request.getSession()
						.getServletContext(), (HttpServletResponse) response,
						RPC.encodeResponseForFailure(null,
								new GWTAuthorizationRequiredException(authenticationProcessingFilter.getFilterProcessesUrl(),
										"Auth required"), rpcRequest
										.getSerializationPolicy()), false);
			} catch (SerializationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Throwable e) {
			// Give a subclass a chance to either handle the exception or
			// rethrow it
			//
			payloadHelper.doUnexpectedFailure(e);
		} finally {
			// null the thread-locals to avoid holding request/response
			//
			payloadHelper.end();
		}

	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
