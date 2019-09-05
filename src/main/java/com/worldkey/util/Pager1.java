package com.worldkey.util;

import java.util.List;

public class Pager1 {// 分页工具类
	private int pageIndex;// 当前页
	private int pageSize;// 分页数据大小
	private int totalCount;// 查询总数据
	private int pages;// 分页页数
	private List list;// 查询到的数据集合

	/******* 开始结束页面初始化 **********/
	/*
	 * private int startPage; private int endPage;
	 */

	private int startIndex;
	private int endIndex;

	public Pager1(int pageIndex, int pageSize, int totalCount, List list) {
		super();
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.list = list;
		/********* 计算分页总页数 **********/
		if (pageSize != 0) {
			if (totalCount % pageSize == 0) {
				this.pages = totalCount / pageSize;
			} else {
				this.pages = totalCount / pageSize + 1;//

			}

		}
		/********* 计算分页总页数 **********/
		startIndex = 1;
		endIndex = this.pages;

		// ------------------------算法：每次生成10格个数字------------------------------
		if (pages <= 10) {
			startIndex = 1;
			endIndex = pages;
		}
		// >> 总页码大于10页时，就只显示当前页附近的共10个页码
		else {
			// 默认显示 前4页 + 当前页 + 后5页
			startIndex = pageIndex - 4; // 7 - 4 = 3;
			endIndex = pageIndex + 5; // 7 + 5 = 12; --> 3 ~ 12
			// 如果前面不足4个页码时，则显示前10页
			if (startIndex < 1) {
				startIndex = 1;
				endIndex = 10;
			}
			// 如果后面不足5个页码时，则显示后10页
			else if (startIndex > pages) {
				startIndex = pages;
				endIndex = pages - 9;
			}
		}
		if (endIndex >= pages) {
			endIndex = pages;
		}
		// ---------------------------------------------------------------

	}

	public Pager1(int totalCount) {
		super();
		this.totalCount = totalCount;
		if (totalCount % pageSize == 0) {
			this.pages = totalCount / pageSize;
		} else {
			this.pages = totalCount / pageSize + 1;//

		}
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getpages() {
		return pages;
	}

	public void setpages(int pages) {
		this.pages = pages;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public int getStartPage() {
		return startIndex;
	}

	public void setStartPage(int startPage) {
		this.startIndex = startPage;
	}

	public int getEndPage() {
		return endIndex;
	}

	public void setEndPage(int endPage) {
		this.endIndex = endPage;
	}

	public Pager1() {
		super();
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
}
