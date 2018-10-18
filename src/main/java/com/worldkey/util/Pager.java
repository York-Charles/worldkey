package com.worldkey.util;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * <b> 分页通用类 </b>
 *
 * @author kangxu
 * @param <T>
 * 
 */
public class Pager<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4542617637761955078L;

	/**
	 * pageNum 当前页
	 */
	private int pageNum = 1;
	/**
	 * pageSize 每页大小
	 */
	private int pageSize = 10;
	/**
	 * pageTotal 总页数
	 */
	private int pageTotal;
	/**
	 * recordTotal 总条数
	 */
	private int recordTotal;
	/**
	 * previousPage 前一页
	 */
	private int previousPage;
	/**
	 * nextPage 下一页
	 */
	private int nextPage;
	/**
	 * firstPage 第一页
	 */
	private int firstPage = 1;
	/**
	 * lastPage 最后一页
	 */
	private int lastPage;
	/**
	 * content 每页的内容
	 */
	private List<T> content;
	
	/**
	 * everyPage 每一页数据
	 */
	private List<T> everyPage;
	// 以下set方式是需要赋值的
	/**
	 * 设置当前页 <br>
	 * 
	 * @author kangxu
	 * 
	 * @param pageNum
	 */
	public void setpageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * 设置每页大小,也可以不用赋值,默认大小为10条 <br>
	 * 
	 * @author kangxu
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 设置总条数,默认为0 <br>
	 * 
	 * @author kangxu
	 * 
	 * @param recordTotal
	 */
	public void setRecordTotal(int recordTotal) {
		this.recordTotal = recordTotal;
		otherAttr();
	}

	/**
	 * 设置分页内容 <br>
	 * 
	 * @author kangxu
	 * 
	 * @param content
	 */
	public void setContent(List<T> content) {
		this.content = content;
		otherAttr();
	}
	

	/**
	 * 设置其他参数
	 * 
	 * @author kangxu
	 * 
	 */
	public void otherAttr() {
		this.recordTotal = content.size();
		// 总页数
		this.pageTotal = this.recordTotal % this.pageSize > 0 ? this.recordTotal / this.pageSize + 1
				: this.recordTotal / this.pageSize;
		// 第一页
		this.firstPage = 1;
		// 最后一页
		this.lastPage = this.pageTotal;
		// 前一页
		if (this.pageNum > 1) {
			this.previousPage = this.pageNum - 1;
		} else {
			this.previousPage = this.firstPage;
		}
		// 下一页
		if (this.pageNum < this.lastPage) {
			this.nextPage = this.pageNum + 1;
		} else {
			this.nextPage = this.lastPage;
		}
		//每页显示几条
		int a = recordTotal%pageSize;
		int b = recordTotal/pageSize;
		if(pageNum>b){
			this.everyPage = content.subList((pageNum-1)*pageSize, recordTotal);
		}else{
			this.everyPage = content.subList((pageNum-1)*pageSize, pageNum*pageSize);
		}
		//内容数据清空
		this.content= null;
	}

	// 放开私有属性
	public int getpageNum() {
		return pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public int getRecordTotal() {
		return recordTotal;
	}

	public int getPreviousPage() {
		return previousPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public List<T> getContent() {
		return content;
	}
	
	public List<T> getEveryPage(){
		return everyPage;
	}

	
	public Pager(int pageNum, int pageSize, List<T> content) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.content = content;
		otherAttr();
	}

	@Override
	public String toString() {
		return "Pager [pageNum=" + pageNum + ", pageSize=" + pageSize + ", pageTotal=" + pageTotal
				+ ", recordTotal=" + recordTotal + ", previousPage=" + previousPage + ", nextPage=" + nextPage
				+ ", firstPage=" + firstPage + ", lastPage=" + lastPage + ", everyPage=" + everyPage + "]";
	}

}