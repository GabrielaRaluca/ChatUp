package tests;

import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.Test;

import client.Client;

public class TestConnection 
{
	@Test
	public void test() 
	{
			boolean canConnect = true;
			Client client = new Client("192.168.0.13", 9999);
			try
			{
				client.clientSocket = new Socket("192.168.0.13", 9999);
			}
			catch(Exception e)
			{
				canConnect = false;
			}
			assertEquals(canConnect, true);
	}

}
