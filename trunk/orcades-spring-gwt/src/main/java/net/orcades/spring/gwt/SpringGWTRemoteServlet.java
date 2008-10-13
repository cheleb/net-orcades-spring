package net.orcades.spring.gwt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This servlet simply delegate an RPC request to a spring managed bean that is
 * implementing the RPC service.
 * 
 * @author Nouguier Olivier olivier.nouguier@gmail.com
 * 
 */
public class SpringGWTRemoteServlet extends RemoteServiceServlet {

	public static final Logger LOGGER = Logger
			.getLogger(SpringGWTRemoteServlet.class);
	/**
	 * Serialization UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Spring application context.
	 */
	private ApplicationContext applicationContext;

	/**
	 * 
	 * Initialization, simply retrieve the application context.
	 * 
	 * @param config
	 *            The servlet config.
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(config.getServletContext());
	}

	/**
	 * Processes the request:
	 * <ul>
	 * Decode the GWT payload
	 * </ul>
	 * <ul>
	 * Locate / Call implementation service</b>
	 * <ul>
	 * Encode the response
	 * </ul>
	 * 
	 * @param payload
	 *            The GWT payload.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String processCall(String payload) throws SerializationException {

		RPCRequest request = RPC.decodeRequest(payload, null, this);

		Method method = request.getMethod();

		Class clazz = method.getDeclaringClass();

		Map map = applicationContext.getBeansOfType(clazz);
		if (map.isEmpty()) {
			LOGGER.error("No implementation of [" + clazz.getName()
					+ "] found in context");
			logRPCCall(Level.ERROR, request, method);
			throw new SerializationException("No implementation of ["
					+ clazz.getName() + "] found in context");
		}
		//
		// FIXME warn if multiple implementation are found
		// Perhaps use "named" bean.
		//
		try {
			Object targetInjectedInstance = map.values().iterator().next();

			if (LOGGER.isDebugEnabled()) {
				logRPCCall(Level.DEBUG, request, method);
			}
			// The call.
			Object result = method.invoke(targetInjectedInstance, request
					.getParameters());

			if (LOGGER.isDebugEnabled()) {
				logResult(result);
			}

			return RPC.encodeResponseForSuccess(method, result, request
					.getSerializationPolicy());
		} catch (Throwable th) {
			logRPCCall(Level.ERROR, request, method);
			LOGGER.error(th);
			if (th instanceof InvocationTargetException) {
				InvocationTargetException invocationTargetException = (InvocationTargetException) th;
				LOGGER.error(clazz.getName() + "::" + method.getName()
						+ "() failed!", invocationTargetException
						.getTargetException());
			}
			return RPC.encodeResponseForFailure(method, th, request
					.getSerializationPolicy());

		}

	}

	/**
	 * Log helper.
	 * 
	 * @param priority
	 *            Of log (level).
	 * @param request
	 *            The rpc request.
	 * @param method
	 *            The call method.
	 */
	@SuppressWarnings("unchecked")
	private void logRPCCall(org.apache.log4j.Level priority,
			RPCRequest request, Method method) {
		Class parameterClasses[] = method.getParameterTypes();
		StringBuffer stringBuffer = new StringBuffer(method.getName());
		stringBuffer.append(" (");
		for (int i = 0; i < parameterClasses.length; i++) {
			Class paramClass = parameterClasses[i];
			stringBuffer.append(paramClass.getName());
			if (i < parameterClasses.length - 1) {
				stringBuffer.append(", ");
			}
		}
		stringBuffer.append(")");
		LOGGER.log(priority, stringBuffer.toString());
		Object parameter[] = request.getParameters();
		for (int i = 0; i < parameter.length; i++) {
			Object object = parameter[i];
			LOGGER.log(priority, "params[" + i + "]: " + object);
		}
	}

	@SuppressWarnings("unchecked")
	private void logResult(Object result) {
		if (result == null) {
			LOGGER.debug("Return null!");
		} else if (result instanceof List) {
			List list = (List) result;
			if (list.size() > 0) {
				LOGGER.debug(list.size() + " object in returned list");
			} else {
				LOGGER.debug("Return an empty list");
			}
			int c = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext(); c++) {
				LOGGER.debug(c + ": " + iterator.next());
			}
		} else {
			LOGGER.debug("Return: " + result);
		}

	}

}
