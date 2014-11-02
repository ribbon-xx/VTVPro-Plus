package mdn.vtvpluspro.object.home;

import mdn.vtvpluspro.object.ItemVtvPlusInfo;

public class HomeItemLandListview extends HomeObject {
	private ItemVtvPlusInfo itemFirst;
	private ItemVtvPlusInfo itemSecond;
	private ItemVtvPlusInfo itemThird;
	private ItemVtvPlusInfo itemFourth;
	
	public HomeItemLandListview() {
		setIsCategory(false);
		itemFirst = null;
		itemSecond = null;
		itemThird = null;
		itemFourth = null;
	}

	public ItemVtvPlusInfo getItemFirst() {
		return itemFirst;
	}

	public void setItemFirst(ItemVtvPlusInfo itemFirst) {
		this.itemFirst = itemFirst;
	}

	public ItemVtvPlusInfo getItemSecond() {
		return itemSecond;
	}

	public void setItemSecond(ItemVtvPlusInfo itemSecond) {
		this.itemSecond = itemSecond;
	}

	public ItemVtvPlusInfo getItemThird() {
		return itemThird;
	}

	public void setItemThird(ItemVtvPlusInfo itemThird) {
		this.itemThird = itemThird;
	}

	public ItemVtvPlusInfo getItemFourth() {
		return itemFourth;
	}

	public void setItemFourth(ItemVtvPlusInfo itemFourth) {
		this.itemFourth = itemFourth;
	}

}
