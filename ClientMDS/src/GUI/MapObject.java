package GUI;

import java.sql.Timestamp;
import java.util.Date;

public class MapObject 
{
	Conversation conversation;
	Timestamp date;
	
	public MapObject(Conversation conversation)
	{
		this.conversation = conversation;
		Date d = new Date();
		this.date = new Timestamp(d.getTime());
	}
	
	public void setDate(Timestamp date)
	{
		this.date = date;
	}
}
