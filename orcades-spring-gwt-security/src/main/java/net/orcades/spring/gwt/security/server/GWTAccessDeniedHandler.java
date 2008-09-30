package net.orcades.spring.gwt.security.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.orcades.spring.gwt.security.client.GWTAccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.ui.AccessDeniedHandler;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RPCServletUtils;

public class GWTAccessDeniedHandler implements AccessDeniedHandler {

	
	@Autowired
	private GWTPayloadHelper payloadHelper;
	
	
	@Autowired
	private GWTAuthenticationProcessingFilter authenticationProcessingFilter;

	
	public void handle(ServletRequest req, ServletResponse resp,
			AccessDeniedException accessDeniedException) throws IOException,
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
								new GWTAccessDeniedException(authenticationProcessingFilter.getFilterProcessesUrl(),
										"Access denied !!"), rpcRequest
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

}
