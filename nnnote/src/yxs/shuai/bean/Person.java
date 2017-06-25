package yxs.shuai.bean;

/*
 *	GetSet类
 */
public class Person {
	private Long id;
	private String name;
	private String many;
	private String Time;
	private String ZH;
	private String imgAddress;

	public String getImgAddress() {
		return imgAddress;
	}

	public void setImgAddress(String imgAddress) {
		this.imgAddress = imgAddress;
	}

	public String getZH() {
		return ZH;
	}

	public void setZH(String zH) {
		ZH = zH;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public Long getId() {
		return id;
	}

	public Person(Long id, String name, String many, String time) {
		super();
		this.id = id;
		this.name = name;
		this.many = many;
		Time = time;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMany() {
		return many;
	}

	public void setMany(String many) {
		this.many = many;
	}

	public Person(Long id, String name, String many) {
		super();
		this.id = id;
		this.name = name;
		this.many = many;
	}

	public Person(String name, String many) {
		super();
		this.name = name;
		this.many = many;
	}

	public Person() {
		super();
	}

	@Override
	public String toString() {
		return "顺序=" + id + ", 商品=" + name + ", 价格=" + many + "]";
	}

}
