package emoji;

public class Emoji 
{
	String code;
	String path;
	
	Emoji(String code, String path)
	{
		this.code = code;
		this.path = path;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
