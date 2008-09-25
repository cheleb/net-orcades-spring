package net.orcades.spring.gwt.security.server;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RPCServletUtils;

public class GWTAuthenticationProcessingFilter extends
		AuthenticationProcessingFilter {

	private ThreadLocal<GWTPayloadHelper> gwtPayloadHelper = new ThreadLocal<GWTPayloadHelper>();
	private ThreadLocal<RPCRequest> perThreadRPCRequest = new ThreadLocal<RPCRequest>();

	
	
	
	
	
	public void doFilterHttp(HttpServletRequest request,
			HttpServletResponse response, FilterChain arg2) throws IOException,
			ServletException {

	          	
		
		if (requiresAuthentication(request, response)) {

			GWTPayloadHelper payloadHelper = new GWTPayloadHelper(request,
					response);
			gwtPayloadHelper.set(payloadHelper);

			try {
				// Store the request & response objects in thread-local storage.
				//

				// Read the request fully.
				//
				String requestPayload = RPCServletUtils
						.readContentAsUtf8(request);

				// Let subclasses see the serialized request.
				//
				payloadHelper.onBeforeRequestDeserialized(requestPayload);

				RPCRequest rpcRequest = RPC.decodeRequest(requestPayload, null,
						payloadHelper);
				
				perThreadRPCRequest.set(rpcRequest);

				super.doFilterHttp(request, response, arg2);

			} catch (Throwable e) {
				// Give a subclass a chance to either handle the exception or
				// rethrow it
				//
				payloadHelper.doUnexpectedFailure(e);
			} finally {
				// null the thread-locals to avoid holding request/response
				//
				gwtPayloadHelper.set(null);
				perThreadRPCRequest.set(null);
			}
		} else {
			super.doFilterHttp(request, response, arg2);
		}
	}

	

	protected String obtainUsername(HttpServletRequest request) {
	
		RPCRequest rpcRequest = perThreadRPCRequest.get();
		Object[] parameter = rpcRequest.getParameters();
	
		return (String) parameter[0];
	}

	
	
	
	@Override
	protected String obtainPassword(HttpServletRequest request) {
		RPCRequest rpcRequest = perThreadRPCRequest.get();
		Object[] parameter = rpcRequest.getParameters();
	
		return (String) parameter[1];	}
	
	public String getFilterProcessesUrl() {
		// TODO Auto-generated method stub
		return super.getFilterProcessesUrl();
	}

	

	
	@Override
	protected void sendRedirect(HttpServletRequest request,
			HttpServletResponse response, String url) throws IOException {
		RPCRequest rpcRequest = perThreadRPCRequest.get();
		try {
			RPCServletUtils.writeResponse(request.getSession().getServletContext(), (HttpServletResponse)response, RPC.encodeResponseForSuccess(rpcRequest.getMethod(), Boolean.TRUE), false);
		} catch (SerializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		RPCRequest rpcRequest = perThreadRPCRequest.get();
		try {
			RPCServletUtils.writeResponse(request.getSession().getServletContext(), (HttpServletResponse)response, RPC.encodeResponseForSuccess(rpcRequest.getMethod(), Boolean.FALSE), false);
		} catch (SerializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
