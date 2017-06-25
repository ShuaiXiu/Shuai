package yan.yxs.music.bean;

/**
 * Bean 业务类 Get Set方法
 * 
 * @author 坐怀灬不乱
 * 
 */
public class Music {
	private int id;
	private String Music_title;
	private String url;
	private String Music_name;
	private String duration;
	private int like;
	private int isAll;

	public int getIsAll() {
		return isAll;
	}

	public void setIsAll(int isAll) {
		this.isAll = isAll;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public String getMusic_title() {
		return Music_title;
	}

	public void setMusic_title(String music_title) {
		Music_title = music_title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMusic_name() {
		return Music_name;
	}

	public void setMusic_name(String music_name) {
		Music_name = music_name;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
}
