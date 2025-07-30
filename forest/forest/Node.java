package forest;

/**
 * ノードの座標情報や名前を保持する型
 */
public class Node extends Object {

	/**
	 * ノードの左上のX座標を保持する変数
	 */
	private Integer x;

	/**
	 * ノードの左上のY座標を保持する変数
	 */
	private Integer y;

	/**
	 * ノードの名前を保持する変数
	 */
	private String name;

	/**
	 * 自身の子孫の中で一番Y座標が大きいノードのY座標の値を保持する変数
	 */
	private Integer maxY;

	/**
	 * 自身の子孫の中で一番Y座標が小さいノードのY座標の値を保持する変数
	 */
	private Integer minY;

	/*
	 * ノードの横方向の辺の長さを保存する変数
	 */
	private Integer rectWidth;

	/**
	 * ノードの縦方向の辺の長さを保存する変数
	 */
	private Integer rectHeight;

	/**
	 * ノード情報を保持するためのインスタンスを生成するためのコンストラクタ
	 */
	public Node(Integer x, Integer y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.maxY = y; 
		this.minY = y; 
	}

	/**
	 * 自身のX座標を返すメソッド
	 */
	public Integer getX() {
		return this.x;
	}

	/**
	 * 自身のX座標を引数の値に変更するメソッド
	 */
	public void setX(Integer setterX) {
		this.x = setterX;
	}

	/**
	 * 自身のY座標を返すメソッド
	 */
	public Integer getY() {
		return this.y;
	}

	/**
	 * 自身のY座標を引数の値に変更するメソッド
	 */
	public void setY(Integer setterY) {
		this.y = setterY;
	}

	/**
	 * 自身のMaxY座標を返すメソッド
	 */
	public Integer getMaxY() {
		return this.maxY;
	}

	/**
	 * 自身のMaxY座標を引数の値に変更するメソッド
	 */
	public void setMaxY(Integer setterMaxY) {
		if (setterMaxY > this.maxY) {
			this.maxY = setterMaxY;
		}
	}

	/**
	 * 自身のMinY座標を返すメソッド
	 */
	public Integer getMinY() {
		return this.minY;
	}

	/**
	 * 自身のMinY座標を引数の値に変更するメソッド
	 */
	public void setMinY(Integer setterMinY) {
		if (setterMinY < this.minY) {
			this.minY = setterMinY;
		}
	}

	/**
	 * 自身の名前を返すメソッド
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 自身の横方向の辺の長さを返すメソッド
	 */
	public Integer getRectWidth() {
		return this.rectWidth;
	}

	/**
	 * 自身の横方向の辺の長さを引数の値に変更するメソッド
	 */
	public void setRectWidth(Integer setterRectWidth) {
		this.rectWidth = setterRectWidth;
	}

	/**
	 * 自身の縦方向の辺の長さを返すメソッド
	 */
	public Integer getRectHeight() {
		return this.rectHeight;
	}

	/**
	 * 自身の縦方向の辺の長さを引数の値に変更するメソッド
	 */
	public void setRectHeight(Integer setterRectHeight) {
		this.rectHeight = setterRectHeight;
	}

}
