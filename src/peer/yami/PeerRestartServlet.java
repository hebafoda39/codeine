package yami;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;

import yami.configuration.ConfigurationManager;
import yami.configuration.GlobalConfiguration;

import com.google.inject.Inject;

public class PeerRestartServlet extends HttpServlet
{
	private static final Logger log = Logger.getLogger(PeerRestartServlet.class);
	private static final long serialVersionUID = 1L;
	@Inject
	private Server peerHTTPserver;
	@Inject
	private ConfigurationManager configurationManager;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		log.info("ClientRestartServlet started");
		PrintWriter writer = res.getWriter();
		writer.println("Recived restart request");
		new PeerRestartThread(peerHTTPserver,writer,configurationManager);
	}
	
	boolean verifyConfiguration(GlobalConfiguration conf)
	{
		return true;
	}
	
	
}
