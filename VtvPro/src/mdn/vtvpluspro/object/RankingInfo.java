package mdn.vtvpluspro.object;

public class RankingInfo {
	private String id;
	private String name;
	private String point;

	public RankingInfo(String id, String name, String point) {
		super();
		this.id = id;
		this.name = name;
		this.point = point;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

}
